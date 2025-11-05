/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

public class FormulaRN {

    // Cria fórmula e seus ingredientes
    public int criarFormula(model.vo.FormulaVO formula, java.util.List<model.vo.FormIngreVO> ingredientes) throws Exception {
        java.sql.Connection con = null;
        try {
            if (formula == null) throw new Exception("Fórmula não informada.");
            if (formula.getFormula_dtValidade() == null) throw new Exception("Data de validade da fórmula é obrigatória.");

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.FormulaDAO formulaDAO = new model.dao.FormulaDAO(con);
            model.dao.FormIngreDAO formIngreDAO = new model.dao.FormIngreDAO(con);
            model.dao.ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);

            int formulaId = formulaDAO.adicionarNovo(formula);
            if (formulaId <= 0) throw new Exception("Falha ao criar fórmula.");
            formula.setFormula_id(formulaId);

            if (ingredientes != null) {
                for (model.vo.FormIngreVO ing : ingredientes) {
                    if (ing == null || ing.getFormIngre_produto_id() == null || ing.getFormIngre_produto_id().getProduto_id() <= 0) {
                        throw new Exception("Ingrediente inválido na fórmula.");
                    }
                    if (ing.getFormIngre_qtd() <= 0) throw new Exception("Quantidade do ingrediente deve ser > 0.");
                    if (produtoDAO.buscarPorId(ing.getFormIngre_produto_id().getProduto_id()) == null) {
                        throw new Exception("Produto do ingrediente não encontrado: id=" + ing.getFormIngre_produto_id().getProduto_id());
                    }
                    ing.setFormIngre_formula_id(formula);
                    formIngreDAO.adicionarNovo(ing);
                }
            }

            con.commit();
            return formulaId;
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao criar fórmula: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // Atualiza validade e ingredientes (upsert simples dos ingredientes)
    public void atualizarFormula(model.vo.FormulaVO formula, java.util.List<model.vo.FormIngreVO> ingredientes) throws Exception {
        java.sql.Connection con = null;
        try {
            if (formula == null || formula.getFormula_id() <= 0) throw new Exception("Fórmula inválida para atualização.");
            if (formula.getFormula_dtValidade() == null) throw new Exception("Data de validade é obrigatória.");

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.FormulaDAO formulaDAO = new model.dao.FormulaDAO(con);
            model.dao.FormIngreDAO formIngreDAO = new model.dao.FormIngreDAO(con);
            model.dao.ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);

            if (formulaDAO.buscarPorId(formula.getFormula_id()) == null) {
                throw new Exception("Fórmula não encontrada para o ID: " + formula.getFormula_id());
            }
            formulaDAO.atualizarPorId(formula);

            if (ingredientes != null) {
                for (model.vo.FormIngreVO ing : ingredientes) {
                    if (ing == null || ing.getFormIngre_produto_id() == null || ing.getFormIngre_produto_id().getProduto_id() <= 0) {
                        throw new Exception("Ingrediente inválido na atualização.");
                    }
                    if (ing.getFormIngre_qtd() <= 0) throw new Exception("Quantidade do ingrediente deve ser > 0.");
                    if (produtoDAO.buscarPorId(ing.getFormIngre_produto_id().getProduto_id()) == null) {
                        throw new Exception("Produto do ingrediente não encontrado: id=" + ing.getFormIngre_produto_id().getProduto_id());
                    }

                    model.vo.FormIngreVO existente = formIngreDAO.buscarPorId(formula.getFormula_id(), ing.getFormIngre_produto_id().getProduto_id());
                    ing.setFormIngre_formula_id(formula);
                    if (existente == null) {
                        formIngreDAO.adicionarNovo(ing);
                    } else {
                        formIngreDAO.atualizar(ing);
                    }
                }
            }

            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao atualizar fórmula: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    public model.vo.FormulaVO buscarPorId(int id) throws Exception {
        if (id <= 0) throw new Exception("ID inválido.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.FormulaDAO dao = new model.dao.FormulaDAO(con);
            return dao.buscarPorId(id);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar fórmula: " + e.getMessage(), e);
        }
    }

    public java.util.List<model.vo.FormIngreVO> listarIngredientes(int formulaId) throws Exception {
        if (formulaId <= 0) throw new Exception("ID de fórmula inválido.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.FormIngreDAO dao = new model.dao.FormIngreDAO(con);
            return dao.buscarPorFormula(formulaId);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao listar ingredientes: " + e.getMessage(), e);
        }
    }

    // Calcula insumos (kg) para um total em kg
    // Regra: se a soma das quantidades dos ingredientes <= 1.0, assume proporções (frações). Caso contrário, assume percentuais 0..100.
    public java.util.Map<model.vo.ProdutoVO, java.lang.Double> calcularInsumosKg(int formulaId, double totalKg) throws Exception {
        if (totalKg <= 0) throw new Exception("Total (kg) deve ser > 0.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.FormIngreDAO formIngreDAO = new model.dao.FormIngreDAO(con);
            model.dao.ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);
            java.util.List<model.vo.FormIngreVO> ingredientes = formIngreDAO.buscarPorFormula(formulaId);
            if (ingredientes == null || ingredientes.isEmpty()) throw new Exception("Fórmula sem ingredientes.");

            double soma = 0.0;
            for (model.vo.FormIngreVO ing : ingredientes) soma += ing.getFormIngre_qtd();
            boolean emPercentual = soma > 1.0 + 1e-9; // tolerância

            java.util.Map<model.vo.ProdutoVO, java.lang.Double> mapa = new java.util.LinkedHashMap<>();
            for (model.vo.FormIngreVO ing : ingredientes) {
                double frac = emPercentual ? (ing.getFormIngre_qtd() / 100.0) : ing.getFormIngre_qtd();
                double kg = totalKg * frac;
                // opcional: normalização mínima
                if (kg < 0) kg = 0;

                // garantir objeto completo de produto
                model.vo.ProdutoVO p = ing.getFormIngre_produto_id();
                if (p == null || p.getProduto_id() <= 0) continue;
                model.vo.ProdutoVO completo = produtoDAO.buscarPorId(p.getProduto_id());
                if (completo == null) completo = p;
                mapa.put(completo, kg);
            }
            return mapa;
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao calcular insumos: " + e.getMessage(), e);
        }
    }

    // Conversões kg <-> sacas
    public double converterKgParaSacas(double totalKg, double pesoPorSacaKg) throws Exception {
        if (totalKg < 0 || pesoPorSacaKg <= 0) throw new Exception("Parâmetros inválidos para conversão.");
        return totalKg / pesoPorSacaKg;
    }

    public double converterSacasParaKg(double qtdSacas, double pesoPorSacaKg) throws Exception {
        if (qtdSacas < 0 || pesoPorSacaKg <= 0) throw new Exception("Parâmetros inválidos para conversão.");
        return qtdSacas * pesoPorSacaKg;
    }

    // Custo total estimado da fórmula para um total em kg, usando custo médio ponderado do estoque atual
    public double calcularCustoTotalEstimado(int formulaId, double totalKg) throws Exception {
        java.util.Map<model.vo.ProdutoVO, java.lang.Double> insumos = calcularInsumosKg(formulaId, totalKg);
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.EstoqueDAO estoqueDAO = new model.dao.EstoqueDAO(con);
            double custo = 0.0;
            for (java.util.Map.Entry<model.vo.ProdutoVO, java.lang.Double> e : insumos.entrySet()) {
                int produtoId = e.getKey().getProduto_id();
                double qtdKg = e.getValue();
                // custo médio ponderado com base nas entradas atuais
                java.util.List<model.vo.EstoqueVO> entradas = estoqueDAO.listarPorProdutoOrdenado(produtoId);
                double somaValor = 0.0;
                double somaQtd = 0.0;
                for (model.vo.EstoqueVO ent : entradas) {
                    somaValor += ent.getEst_custo() * ent.getQtdTotal();
                    somaQtd += ent.getQtdTotal();
                }
                double custoUnit = somaQtd > 0 ? (somaValor / somaQtd) : 0.0;
                custo += custoUnit * qtdKg;
            }
            return custo;
        } catch (java.sql.SQLException ex) {
            throw new Exception("Erro ao calcular custo estimado: " + ex.getMessage(), ex);
        }
    }

    // Produz um lote do produto final a partir de uma fórmula, baixando insumos por FIFO e inserindo a entrada do produto final
    public int produzir(int formulaId,
                        double totalKg,
                        model.vo.ProdutoVO produtoFinal,
                        String loteFinal,
                        java.util.Date validadeFinal,
                        String fornecedorCnpjInterno) throws Exception {
        java.sql.Connection con = null;
        try {
            if (formulaId <= 0) throw new Exception("Fórmula inválida.");
            if (totalKg <= 0) throw new Exception("Quantidade total deve ser > 0.");
            if (produtoFinal == null || produtoFinal.getProduto_id() <= 0) throw new Exception("Produto final inválido.");
            if (loteFinal == null || loteFinal.isBlank()) throw new Exception("Lote do produto final é obrigatório.");
            if (validadeFinal == null) throw new Exception("Validade do produto final é obrigatória.");
            if (fornecedorCnpjInterno == null || fornecedorCnpjInterno.isBlank()) throw new Exception("CNPJ do fornecedor interno é obrigatório.");

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.FormulaDAO formulaDAO = new model.dao.FormulaDAO(con);
            model.dao.FormIngreDAO formIngreDAO = new model.dao.FormIngreDAO(con);
            model.dao.ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);
            model.dao.EstoqueDAO estoqueDAO = new model.dao.EstoqueDAO(con);
            model.dao.FornecedorDAO fornecedorDAO = new model.dao.FornecedorDAO(con);

            if (formulaDAO.buscarPorId(formulaId) == null) throw new Exception("Fórmula não encontrada.");
            if (produtoDAO.buscarPorId(produtoFinal.getProduto_id()) == null) throw new Exception("Produto final não encontrado.");
            if (fornecedorDAO.buscarPorCnpj(fornecedorCnpjInterno) == null) throw new Exception("Fornecedor interno não encontrado: cnpj=" + fornecedorCnpjInterno);

            // insumos necessários (kg por produto)
            java.util.List<model.vo.FormIngreVO> ingredientes = formIngreDAO.buscarPorFormula(formulaId);
            if (ingredientes == null || ingredientes.isEmpty()) throw new Exception("Fórmula sem ingredientes.");

            // construir mapa produto -> kg necessários
            java.util.Map<Integer, java.lang.Double> necessarioPorProduto = new java.util.LinkedHashMap<>();
            double soma = 0.0;
            for (model.vo.FormIngreVO ing : ingredientes) soma += ing.getFormIngre_qtd();
            boolean percentual = soma > 1.0 + 1e-9;
            for (model.vo.FormIngreVO ing : ingredientes) {
                int pid = ing.getFormIngre_produto_id().getProduto_id();
                double frac = percentual ? (ing.getFormIngre_qtd() / 100.0) : ing.getFormIngre_qtd();
                double kg = totalKg * frac;
                necessarioPorProduto.put(pid, kg);
            }

            // valida saldo disponível para cada insumo
            for (java.util.Map.Entry<Integer, java.lang.Double> e : necessarioPorProduto.entrySet()) {
                int pid = e.getKey();
                double needed = e.getValue();
                double saldo = estoqueDAO.somarSaldoPorProduto(pid);
                if (saldo + 1e-9 < needed) {
                    throw new Exception("Estoque insuficiente para insumo id=" + pid + ". Necessário=" + needed + ", saldo=" + saldo);
                }
            }

            // baixa FIFO de cada insumo dentro da transação
            for (java.util.Map.Entry<Integer, java.lang.Double> e : necessarioPorProduto.entrySet()) {
                baixarEstoqueFIFO(con, e.getKey(), e.getValue());
            }

            // custo unitário estimado (média ponderada atual) calculado dentro da mesma conexão
            double custoTotal = 0.0;
            for (java.util.Map.Entry<Integer, java.lang.Double> e : necessarioPorProduto.entrySet()) {
                int produtoId = e.getKey();
                double qtdKg = e.getValue();
                java.util.List<model.vo.EstoqueVO> entradas = estoqueDAO.listarPorProdutoOrdenado(produtoId);
                double somaValor = 0.0;
                double somaQtd = 0.0;
                for (model.vo.EstoqueVO ent : entradas) {
                    somaValor += ent.getEst_custo() * ent.getQtdTotal();
                    somaQtd += ent.getQtdTotal();
                }
                double custoUnit = somaQtd > 0 ? (somaValor / somaQtd) : 0.0;
                custoTotal += custoUnit * qtdKg;
            }
            double custoUnitFinal = totalKg > 0 ? (custoTotal / totalKg) : 0.0;

            // insere entrada de estoque do produto final
            model.vo.EstoqueVO novo = new model.vo.EstoqueVO();
            novo.setEst_dtCompra(new java.util.Date());
            novo.setEst_produto_id(produtoFinal);
            novo.setEst_custo(custoUnitFinal);
            novo.setQtdTotal(totalKg);
            novo.setEst_lote(loteFinal.trim());
            novo.setEst_dtValidade(validadeFinal);
            model.vo.FornecedorVO forn = new model.vo.FornecedorVO();
            forn.setForn_cnpj(fornecedorCnpjInterno);
            novo.setEst_forn_cnpj(forn);

            int estId = estoqueDAO.adicionarNovo(novo);
            if (estId <= 0) throw new Exception("Falha ao inserir estoque do produto final.");

            con.commit();
            return estId;
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao produzir fórmula: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // overload: produção em sacas
    public int produzirEmSacas(int formulaId,
                               double quantidadeSacas,
                               double pesoPorSacaKg,
                               model.vo.ProdutoVO produtoFinal,
                               String loteFinal,
                               java.util.Date validadeFinal,
                               String fornecedorCnpjInterno) throws Exception {
        double totalKg = converterSacasParaKg(quantidadeSacas, pesoPorSacaKg);
        return produzir(formulaId, totalKg, produtoFinal, loteFinal, validadeFinal, fornecedorCnpjInterno);
    }

    // baixa fifo util (similar ao usado em VendaRN)
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
        if (restante > 1e-9) {
            throw new Exception("Estoque insuficiente durante baixa de insumos. Produto id=" + produtoId + ", restante=" + restante);
        }
    }
}
