/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

public class VendaRN {
    // Cria uma venda completa (venda + itens) com desconto/condições e baixa estoque FIFO por produto
    public int criarVenda(model.vo.VendaVO venda, java.util.List<model.vo.ItemVendaVO> itens) throws Exception {
        return criarVenda(venda, itens, null, null);
    }

    public int criarVenda(model.vo.VendaVO venda,
                          java.util.List<model.vo.ItemVendaVO> itens,
                          java.lang.Double descontoTotal,
                          model.vo.CondicaoPagamentoVO condicao) throws Exception {
        java.sql.Connection con = null;
        try {
            // validações básicas
            if (venda == null) throw new Exception("Venda não informada.");
            if (venda.getVenda_data() == null) throw new Exception("Data da venda é obrigatória.");
            if (venda.getVenda_pes_cpf() == null || venda.getVenda_pes_cpf().getPes_cpf() == null || venda.getVenda_pes_cpf().getPes_cpf().isBlank()) {
                throw new Exception("Cliente (CPF) é obrigatório.");
            }
            if (venda.getVenda_statusVenda() == null || venda.getVenda_statusVenda().getStatusVenda_id() <= 0) {
                throw new Exception("Status de venda inválido.");
            }
            if (venda.getVenda_tipoPagamento() == null || venda.getVenda_tipoPagamento().getTipoPagamento_id() <= 0) {
                throw new Exception("Tipo de pagamento inválido.");
            }
            if (itens == null || itens.isEmpty()) {
                throw new Exception("A venda deve possuir ao menos um item.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            // DAOs
            model.dao.PessoaDAO pessoaDAO = new model.dao.PessoaDAO(con);
            model.dao.FuncionarioDAO funcionarioDAO = new model.dao.FuncionarioDAO(con);
            model.dao.StatusVendaDAO statusVendaDAO = new model.dao.StatusVendaDAO(con);
            model.dao.TipoPagamentoDAO tipoPagamentoDAO = new model.dao.TipoPagamentoDAO(con);
            model.dao.ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);
            model.dao.VendaDAO vendaDAO = new model.dao.VendaDAO(con);
            model.dao.ItemVendaDAO itemVendaDAO = new model.dao.ItemVendaDAO(con);
            model.dao.EstoqueDAO estoqueDAO = new model.dao.EstoqueDAO(con);

            // valida entidades relacionadas
            if (pessoaDAO.buscarPesCpf(venda.getVenda_pes_cpf().getPes_cpf()) == null) {
                throw new Exception("Cliente não encontrado: cpf=" + venda.getVenda_pes_cpf().getPes_cpf());
            }

            // funcionario: aceita id em VendaVO; se não vier, tenta resolver pelo CPF interno do objeto
            if (venda.getVenda_func_cpf() == null) throw new Exception("Funcionário é obrigatório.");
            model.vo.FuncionarioVO funcionarioResolved = null;
            if (venda.getVenda_func_cpf().getFnc_id() > 0) {
                funcionarioResolved = funcionarioDAO.buscarPorId(venda.getVenda_func_cpf().getFnc_id());
            } else if (venda.getVenda_func_cpf().getPes_cpf() != null) {
                funcionarioResolved = funcionarioDAO.buscarFuncCpf(venda.getVenda_func_cpf().getPes_cpf());
            }
            if (funcionarioResolved == null) throw new Exception("Funcionário não encontrado/ inválido.");
            venda.setVenda_func_cpf(funcionarioResolved);

            if (statusVendaDAO.buscarPorId(venda.getVenda_statusVenda().getStatusVenda_id()) == null) {
                throw new Exception("Status de venda não encontrado.");
            }
            if (tipoPagamentoDAO.buscarPorId(venda.getVenda_tipoPagamento().getTipoPagamento_id()) == null) {
                throw new Exception("Tipo de pagamento não encontrado.");
            }

            // valida itens e estoque disponível por produto (sem HashMap)
            java.util.List<Integer> produtoIds = new java.util.ArrayList<>();
            for (model.vo.ItemVendaVO item : itens) {
                if (item == null || item.getItemVenda_produto_id() == null || item.getItemVenda_produto_id().getProduto_id() <= 0) {
                    throw new Exception("Item de venda com produto inválido.");
                }
                if (item.getItemVenda_qtd() <= 0) throw new Exception("Quantidade do item deve ser > 0.");
                if (item.getItemVenda_preco() < 0) throw new Exception("Preço do item não pode ser negativo.");

                int produtoId = item.getItemVenda_produto_id().getProduto_id();
                if (produtoDAO.buscarPorId(produtoId) == null) {
                    throw new Exception("Produto não encontrado: id=" + produtoId);
                }
                boolean jaListado = false;
                for (Integer pid : produtoIds) {
                    if (pid == produtoId) { jaListado = true; break; }
                }
                if (!jaListado) produtoIds.add(produtoId);
            }

            // checa saldo agregado por produto com somatório simples
            for (Integer produtoId : produtoIds) {
                double necessario = 0.0;
                for (model.vo.ItemVendaVO item : itens) {
                    if (item.getItemVenda_produto_id().getProduto_id() == produtoId) {
                        necessario += item.getItemVenda_qtd();
                    }
                }
                double saldo = estoqueDAO.somarSaldoPorProduto(produtoId);
                if (saldo < necessario) {
                    throw new Exception("Estoque insuficiente para o produto id=" + produtoId + ". Necessário=" + necessario + ", saldo=" + saldo);
                }
            }

            // calcula subtotal e valida desconto total
            double subtotal = 0.0;
            for (model.vo.ItemVendaVO item : itens) {
                subtotal += item.getItemVenda_qtd() * item.getItemVenda_preco();
            }
            double desc = (descontoTotal == null ? 0.0 : descontoTotal);
            if (desc < 0) throw new Exception("Desconto não pode ser negativo.");
            if (desc > subtotal) throw new Exception("Desconto não pode exceder o subtotal da venda.");

            // persiste venda
            int vendaId = vendaDAO.adicionarNovo(venda);
            if (vendaId <= 0) throw new Exception("Falha ao criar venda.");
            venda.setVenda_id(vendaId);

            // persiste itens e baixa estoque FIFO
            for (model.vo.ItemVendaVO item : itens) {
                item.setItemVenda_venda_id(venda);
                itemVendaDAO.adicionarNovo(item);

                baixarEstoqueFIFO(con, item.getItemVenda_produto_id().getProduto_id(), item.getItemVenda_qtd());
            }

            // gera débito quando pagamento não é "à vista" (condição parcelada)
            String tpDesc = null;
            try { tpDesc = venda.getVenda_tipoPagamento().getTipoPagamento_descricao(); } catch (Exception ignore) {}
            if (tpDesc == null) tpDesc = "";
            String normalizado = java.text.Normalizer.normalize(tpDesc, java.text.Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "").toLowerCase();
            boolean ehAvista = normalizado.contains("vista");
            boolean parcelado = (condicao != null && condicao.getNumeroParcelas() != null && condicao.getNumeroParcelas() > 1);
            if (!ehAvista || parcelado) {
                model.dao.DebitoDAO debitoDAO = new model.dao.DebitoDAO(con);
                model.dao.StatusDebitoDAO statusDebitoDAO = new model.dao.StatusDebitoDAO(con);
                model.vo.StatusDebitoVO emAberto = statusDebitoDAO.buscarPorDescricao("EM_ABERTO");
                if (emAberto == null) {
                    throw new Exception("Status de débito 'EM_ABERTO' não encontrado. Cadastre em tb_statusDebito.");
                }
                model.vo.DebitoVO deb = new model.vo.DebitoVO();
                deb.setDeb_venda_id(venda);
                deb.setDeb_juros(null);
                deb.setDeb_status(emAberto);
                int debId = debitoDAO.adicionarNovo(deb);
                if (debId <= 0) throw new Exception("Falha ao gerar débito da venda.");
            }

            con.commit();
            return vendaId;
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao criar venda: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // Busca venda por id
    public model.vo.VendaVO buscarPorId(int id) throws Exception {
        if (id <= 0) throw new Exception("ID inválido.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.VendaDAO dao = new model.dao.VendaDAO(con);
            return dao.buscarPorId(id);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar venda: " + e.getMessage(), e);
        }
    }

    // Busca vendas por CPF do cliente
    public java.util.List<model.vo.VendaVO> buscarPorCpf(String cpf) throws Exception {
        if (cpf == null || cpf.isBlank()) throw new Exception("CPF é obrigatório.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.VendaDAO dao = new model.dao.VendaDAO(con);
            return dao.buscarVendasPorCpf(cpf);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar vendas por CPF: " + e.getMessage(), e);
        }
    }

    // Busca vendas por nome do cliente (like)
    public java.util.List<model.vo.VendaVO> buscarPorNomeCliente(String nome) throws Exception {
        if (nome == null) nome = "";
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.VendaDAO dao = new model.dao.VendaDAO(con);
            return dao.buscarVendasPorNomeCliente(nome);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar vendas por nome: " + e.getMessage(), e);
        }
    }

    // baixa FIFO de estoque por produto dentro da mesma transação
    private void baixarEstoqueFIFO(java.sql.Connection con, int produtoId, double quantidade) throws Exception {
        model.dao.EstoqueDAO estoqueDAO = new model.dao.EstoqueDAO(con);
        java.util.List<model.vo.EstoqueVO> entradas = estoqueDAO.listarPorProdutoOrdenado(produtoId);
        double restante = quantidade;

        for (model.vo.EstoqueVO e : entradas) {
            if (restante <= 0) break;
            double saldo = e.getQtdTotal();
            if (saldo <= 0) continue;
            double abater = Math.min(saldo, restante);
            e.setQtdTotal(saldo - abater);
            estoqueDAO.atualizarPorId(e);
            restante -= abater;
        }

        if (restante > 0.0000001) {
            throw new Exception("Estoque tornou-se insuficiente durante a baixa (concorrência). Produto id=" + produtoId);
        }
    }

    // Gera um documento (DTO) de venda com itens, subtotal, desconto e total, além das condições
    public model.vo.VendaDocumentoVO gerarDocumentoVenda(int vendaId,
                                                         java.lang.Double descontoTotal,
                                                         model.vo.CondicaoPagamentoVO condicao) throws Exception {
        if (vendaId <= 0) throw new Exception("ID de venda inválido.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.VendaDAO vendaDAO = new model.dao.VendaDAO(con);
            model.dao.ItemVendaDAO itemDAO = new model.dao.ItemVendaDAO(con);

            model.vo.VendaVO venda = vendaDAO.buscarPorId(vendaId);
            if (venda == null) throw new Exception("Venda não encontrada.");

            java.util.List<model.vo.ItemVendaVO> itens = itemDAO.buscarPorVenda(vendaId);
            model.vo.VendaDocumentoVO doc = new model.vo.VendaDocumentoVO();
            doc.setVendaId(venda.getVenda_id());
            doc.setData(venda.getVenda_data());
            doc.setCliente(venda.getVenda_pes_cpf());
            doc.setFuncionario(venda.getVenda_func_cpf());
            doc.setStatus(venda.getVenda_statusVenda());
            doc.setTipoPagamento(venda.getVenda_tipoPagamento());
            if (condicao != null) doc.setCondicaoPagamento(condicao);

            double subtotal = 0.0;
            java.util.List<model.vo.VendaDocumentoItemVO> itensDoc = new java.util.ArrayList<>();
            for (model.vo.ItemVendaVO it : itens) {
                model.vo.VendaDocumentoItemVO row = new model.vo.VendaDocumentoItemVO(
                        it.getItemVenda_produto_id(),
                        it.getItemVenda_qtd(),
                        it.getItemVenda_preco()
                );
                subtotal += row.getSubtotal();
                itensDoc.add(row);
            }
            double desc = (descontoTotal == null ? 0.0 : descontoTotal);
            if (desc < 0) throw new Exception("Desconto não pode ser negativo.");
            if (desc > subtotal) throw new Exception("Desconto não pode exceder o subtotal.");
            double total = subtotal - desc;

            doc.setItens(itensDoc);
            doc.setSubtotal(subtotal);
            doc.setDescontoTotal(desc);
            doc.setTotal(total);

            return doc;
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao gerar documento de venda: " + e.getMessage(), e);
        }
    }
}
