/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

public class EstoqueRN {
    // adicionar entrada de estoque
    public int adicionarEstoque(model.vo.EstoqueVO estoque) throws Exception {
        java.sql.Connection con = null;
        try {
            if (estoque == null) {
                throw new Exception("Estoque não informado.");
            }

            if (estoque.getEst_dtCompra() == null) {
                throw new Exception("Data de compra é obrigatória.");
            }

            if (estoque.getEst_dtValidade() == null) {
                throw new Exception("Data de validade é obrigatória.");
            }

            if (estoque.getEst_lote() == null || estoque.getEst_lote().isBlank()) {
                throw new Exception("Lote é obrigatório.");
            }

            if (estoque.getQtdTotal() < 0) {
                throw new Exception("Quantidade total não pode ser negativa.");
            }

            if (estoque.getEst_custo() < 0) {
                throw new Exception("Custo não pode ser negativo.");
            }

            if (estoque.getEst_produto_id() == null || estoque.getEst_produto_id().getProduto_id() <= 0) {
                throw new Exception("Produto inválido ou não informado.");
            }
            
            if (estoque.getEst_forn_cnpj() == null || estoque.getEst_forn_cnpj().getForn_cnpj() == null
                    || estoque.getEst_forn_cnpj().getForn_cnpj().isBlank()) {
                throw new Exception("Fornecedor (CNPJ) é obrigatório.");
            }

            // normalização e regras adicionais
            estoque.setEst_lote(estoque.getEst_lote().trim());
            if (estoque.getEst_dtValidade().before(estoque.getEst_dtCompra())) {
                throw new Exception("Data de validade deve ser igual ou após a data de compra.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            // valida existência de produto/fornecedor
            model.dao.ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);
            if (produtoDAO.buscarPorId(estoque.getEst_produto_id().getProduto_id()) == null) {
                throw new Exception("Produto não encontrado: id=" + estoque.getEst_produto_id().getProduto_id());
            }

            model.dao.FornecedorDAO fornecedorDAO = new model.dao.FornecedorDAO(con);
            if (fornecedorDAO.buscarPorCnpj(estoque.getEst_forn_cnpj().getForn_cnpj()) == null) {
                throw new Exception("Fornecedor não encontrado: cnpj=" + estoque.getEst_forn_cnpj().getForn_cnpj());
            }

            // regra: lote único por produto
            model.dao.EstoqueDAO estoqueDAOVal = new model.dao.EstoqueDAO(con);
            model.vo.EstoqueVO duplicado = estoqueDAOVal.buscarPorLote(estoque.getEst_lote());
            if (duplicado != null &&
                duplicado.getEst_produto_id() != null &&
                duplicado.getEst_produto_id().getProduto_id() == estoque.getEst_produto_id().getProduto_id()) {
                throw new Exception("Já existe uma entrada com este lote para o mesmo produto.");
            }

            model.dao.EstoqueDAO dao = new model.dao.EstoqueDAO(con);
            int id = dao.adicionarNovo(estoque);
            if (id <= 0) {
                throw new Exception("Falha ao inserir estoque.");
            }

            con.commit();
            return id;
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao adicionar estoque: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // atualizar registro de estoque por ID
    public void atualizarEstoque(model.vo.EstoqueVO estoque) throws Exception {
        java.sql.Connection con = null;
        try {
            if (estoque == null) {
                throw new Exception("Estoque não informado.");
            }
            if (estoque.getEst_id() <= 0) {
                throw new Exception("ID do estoque é obrigatório para atualização.");
            }
            if (estoque.getEst_dtCompra() == null) {
                throw new Exception("Data de compra é obrigatória.");
            }
            if (estoque.getEst_dtValidade() == null) {
                throw new Exception("Data de validade é obrigatória.");
            }
            if (estoque.getEst_lote() == null || estoque.getEst_lote().isBlank()) {
                throw new Exception("Lote é obrigatório.");
            }
            if (estoque.getQtdTotal() < 0) {
                throw new Exception("Quantidade total não pode ser negativa.");
            }
            if (estoque.getEst_custo() < 0) {
                throw new Exception("Custo não pode ser negativo.");
            }
            if (estoque.getEst_produto_id() == null || estoque.getEst_produto_id().getProduto_id() <= 0) {
                throw new Exception("Produto inválido ou não informado.");
            }
            if (estoque.getEst_forn_cnpj() == null || estoque.getEst_forn_cnpj().getForn_cnpj() == null
                    || estoque.getEst_forn_cnpj().getForn_cnpj().isBlank()) {
                throw new Exception("Fornecedor (CNPJ) é obrigatório.");
            }

            // normalização e regras adicionais
            estoque.setEst_lote(estoque.getEst_lote().trim());
            if (estoque.getEst_dtValidade().before(estoque.getEst_dtCompra())) {
                throw new Exception("Data de validade deve ser igual ou após a data de compra.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.EstoqueDAO dao = new model.dao.EstoqueDAO(con);
            model.vo.EstoqueVO existente = dao.buscarPorId(estoque.getEst_id());
            if (existente == null) {
                throw new Exception("Estoque não encontrado para ID: " + estoque.getEst_id());
            }

            // valida chaves estrangeiras
            model.dao.ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);
            if (produtoDAO.buscarPorId(estoque.getEst_produto_id().getProduto_id()) == null) {
                throw new Exception("Produto não encontrado: id=" + estoque.getEst_produto_id().getProduto_id());
            }
            model.dao.FornecedorDAO fornecedorDAO = new model.dao.FornecedorDAO(con);
            if (fornecedorDAO.buscarPorCnpj(estoque.getEst_forn_cnpj().getForn_cnpj()) == null) {
                throw new Exception("Fornecedor não encontrado: cnpj=" + estoque.getEst_forn_cnpj().getForn_cnpj());
            }

            // regra: lote único por produto (exceto o próprio registro)
            model.vo.EstoqueVO duplicado = dao.buscarPorLote(estoque.getEst_lote());
            if (duplicado != null &&
                duplicado.getEst_id() != existente.getEst_id() &&
                duplicado.getEst_produto_id() != null &&
                duplicado.getEst_produto_id().getProduto_id() == estoque.getEst_produto_id().getProduto_id()) {
                throw new Exception("Já existe outra entrada com este lote para o mesmo produto.");
            }

            dao.atualizarPorId(estoque);
            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao atualizar estoque: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // atualizar por lote (útil se regra de negócio usar lote como referência)
    public void atualizarPorLote(String loteAntigo, model.vo.EstoqueVO estoqueNovo) throws Exception {
        java.sql.Connection con = null;
        try {
            if (loteAntigo == null || loteAntigo.isBlank()) {
                throw new Exception("Lote antigo é obrigatório.");
            }
            if (estoqueNovo == null) {
                throw new Exception("Novo estoque não informado.");
            }

            if (estoqueNovo.getEst_dtCompra() == null) {
                throw new Exception("Data de compra é obrigatória.");
            }
            if (estoqueNovo.getEst_dtValidade() == null) {
                throw new Exception("Data de validade é obrigatória.");
            }
            if (estoqueNovo.getEst_lote() == null || estoqueNovo.getEst_lote().isBlank()) {
                throw new Exception("Novo lote é obrigatório.");
            }
            if (estoqueNovo.getQtdTotal() < 0) {
                throw new Exception("Quantidade total não pode ser negativa.");
            }
            if (estoqueNovo.getEst_custo() < 0) {
                throw new Exception("Custo não pode ser negativo.");
            }
            if (estoqueNovo.getEst_produto_id() == null || estoqueNovo.getEst_produto_id().getProduto_id() <= 0) {
                throw new Exception("Produto inválido ou não informado.");
            }
            if (estoqueNovo.getEst_forn_cnpj() == null || estoqueNovo.getEst_forn_cnpj().getForn_cnpj() == null
                    || estoqueNovo.getEst_forn_cnpj().getForn_cnpj().isBlank()) {
                throw new Exception("Fornecedor (CNPJ) é obrigatório.");
            }

            // normalização e regras adicionais
            loteAntigo = loteAntigo.trim();
            estoqueNovo.setEst_lote(estoqueNovo.getEst_lote().trim());
            if (estoqueNovo.getEst_dtValidade().before(estoqueNovo.getEst_dtCompra())) {
                throw new Exception("Data de validade deve ser igual ou após a data de compra.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.EstoqueDAO dao = new model.dao.EstoqueDAO(con);
            model.vo.EstoqueVO existente = dao.buscarPorLote(loteAntigo);
            if (existente == null) {
                throw new Exception("Estoque não encontrado para lote: " + loteAntigo);
            }

            // valida chaves estrangeiras
            model.dao.ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);
            if (produtoDAO.buscarPorId(estoqueNovo.getEst_produto_id().getProduto_id()) == null) {
                throw new Exception("Produto não encontrado: id=" + estoqueNovo.getEst_produto_id().getProduto_id());
            }
            model.dao.FornecedorDAO fornecedorDAO = new model.dao.FornecedorDAO(con);
            if (fornecedorDAO.buscarPorCnpj(estoqueNovo.getEst_forn_cnpj().getForn_cnpj()) == null) {
                throw new Exception("Fornecedor não encontrado: cnpj=" + estoqueNovo.getEst_forn_cnpj().getForn_cnpj());
            }

            // regra: lote único por produto (se novo lote conflitar com outro registro diferente do atual)
            model.vo.EstoqueVO duplicado = dao.buscarPorLote(estoqueNovo.getEst_lote());
            if (duplicado != null &&
                duplicado.getEst_id() != existente.getEst_id() &&
                duplicado.getEst_produto_id() != null &&
                duplicado.getEst_produto_id().getProduto_id() == estoqueNovo.getEst_produto_id().getProduto_id()) {
                throw new Exception("Já existe outra entrada com este lote para o mesmo produto.");
            }

            dao.atualizarPorLote(loteAntigo, estoqueNovo);
            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao atualizar por lote: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    public model.vo.EstoqueVO buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido.");
        }
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.EstoqueDAO dao = new model.dao.EstoqueDAO(con);
            return dao.buscarPorId(id);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar estoque por ID: " + e.getMessage(), e);
        }
    }

    public model.vo.EstoqueVO buscarPorLote(String lote) throws Exception {
        if (lote == null || lote.isBlank()) {
            throw new Exception("Lote é obrigatório.");
        }
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.EstoqueDAO dao = new model.dao.EstoqueDAO(con);
            return dao.buscarPorLote(lote);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar estoque por lote: " + e.getMessage(), e);
        }
    }

    // baixa de estoque por lote
    public void baixarEstoquePorLote(String lote, double quantidade) throws Exception {
        java.sql.Connection con = null;
        try {
            if (lote == null || lote.isBlank()) {
                throw new Exception("Lote é obrigatório para baixa de estoque.");
            }
            if (quantidade <= 0) {
                throw new Exception("Quantidade para baixa deve ser maior que zero.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.EstoqueDAO dao = new model.dao.EstoqueDAO(con);
            model.vo.EstoqueVO existente = dao.buscarPorLote(lote.trim());
            if (existente == null) {
                throw new Exception("Estoque não encontrado para o lote informado.");
            }

            double saldo = existente.getQtdTotal();
            if (saldo < quantidade) {
                throw new Exception("Quantidade solicitada excede o saldo disponível. Saldo atual: " + saldo);
            }

            existente.setQtdTotal(saldo - quantidade);
            dao.atualizarPorId(existente);

            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao baixar estoque: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // lista produtos com saldo total abaixo do mínimo configurado
    public java.util.List<model.vo.ProdutoVO> listarProdutosAbaixoMinimo() throws Exception {
        java.util.List<model.vo.ProdutoVO> resultado = new java.util.ArrayList<>();
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);
            model.dao.EstoqueDAO estoqueDAO = new model.dao.EstoqueDAO(con);
            java.util.List<model.vo.ProdutoVO> todos = produtoDAO.buscarTodos(true);
            for (model.vo.ProdutoVO p : todos) {
                if (p.getProduto_qtdMin() == null) continue;
                double saldo = estoqueDAO.somarSaldoPorProduto(p.getProduto_id());
                if (saldo < p.getProduto_qtdMin()) resultado.add(p);
            }
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao listar produtos abaixo do mínimo: " + e.getMessage(), e);
        }
        return resultado;
    }

    // lista produtos com saldo total acima do máximo configurado
    public java.util.List<model.vo.ProdutoVO> listarProdutosAcimaMaximo() throws Exception {
        java.util.List<model.vo.ProdutoVO> resultado = new java.util.ArrayList<>();
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);
            model.dao.EstoqueDAO estoqueDAO = new model.dao.EstoqueDAO(con);
            java.util.List<model.vo.ProdutoVO> todos = produtoDAO.buscarTodos(true);
            for (model.vo.ProdutoVO p : todos) {
                if (p.getProduto_qtdMax() == null) continue;
                double saldo = estoqueDAO.somarSaldoPorProduto(p.getProduto_id());
                if (saldo > p.getProduto_qtdMax()) resultado.add(p);
            }
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao listar produtos acima do máximo: " + e.getMessage(), e);
        }
        return resultado;
    }

    // lista lotes vencidos até a data atual
    public java.util.List<model.vo.EstoqueVO> listarLotesVencidos() throws Exception {
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.EstoqueDAO dao = new model.dao.EstoqueDAO(con);
            java.util.Date hoje = new java.util.Date();
            return dao.listarVencidos(hoje);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao listar lotes vencidos: " + e.getMessage(), e);
        }
    }

    // lista lotes com validade até a data limite (inclusive)
    public java.util.List<model.vo.EstoqueVO> listarLotesVencendoAte(java.util.Date limite) throws Exception {
        if (limite == null) {
            throw new Exception("Data limite é obrigatória.");
        }
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.EstoqueDAO dao = new model.dao.EstoqueDAO(con);
            return dao.listarVencendoAte(limite);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao listar lotes vencendo: " + e.getMessage(), e);
        }
    }

    // baixa de estoque por produto com política FIFO (distribui entre lotes)
    public void baixarEstoquePorProdutoFIFO(int produtoId, double quantidade) throws Exception {
        java.sql.Connection con = null;
        try {
            if (produtoId <= 0) {
                throw new Exception("Produto inválido.");
            }
            if (quantidade <= 0) {
                throw new Exception("Quantidade deve ser > 0.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

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
            if (restante > 1e-9) {
                throw new Exception("Saldo insuficiente para baixa FIFO. Restante=" + restante);
            }
            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao baixar estoque FIFO: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }
}
