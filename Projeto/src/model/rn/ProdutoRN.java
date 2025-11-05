/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

public class ProdutoRN {
    // adicionar produto
    public int adicionarProduto(model.vo.ProdutoVO produto) throws Exception {
        java.sql.Connection con = null;
        try {
            if (produto == null) throw new Exception("Produto não informado.");
            if (produto.getProduto_nome() == null || produto.getProduto_nome().isBlank()) {
                throw new Exception("Nome do produto é obrigatório.");
            }
            if (produto.getProduto_qtdMin() == null || produto.getProduto_qtdMax() == null) {
                throw new Exception("Qtd mínima e máxima são obrigatórias.");
            }
            if (produto.getProduto_qtdMin() < 0) {
                throw new Exception("Qtd mínima não pode ser negativa.");
            }
            if (produto.getProduto_qtdMax() < produto.getProduto_qtdMin()) {
                throw new Exception("Qtd máxima deve ser >= qtd mínima.");
            }
            if (produto.getProduto_tipoPdt() == null || produto.getProduto_tipoPdt().getTipoPdt_id() <= 0) {
                throw new Exception("Tipo de produto é obrigatório e deve ser válido.");
            }

            // default ativo
            if (produto.getProduto_ativo() == null) {
                produto.setProduto_ativo(true);
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.TipoProdutoDAO tipoDao = new model.dao.TipoProdutoDAO(con);
            if (tipoDao.buscarPorId(produto.getProduto_tipoPdt().getTipoPdt_id()) == null) {
                throw new Exception("Tipo de produto não encontrado: id=" + produto.getProduto_tipoPdt().getTipoPdt_id());
            }

            model.dao.ProdutoDAO dao = new model.dao.ProdutoDAO(con);
            int id = dao.adicionarNovo(produto);
            if (id <= 0) throw new Exception("Falha ao inserir produto.");

            con.commit();
            return id;
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao adicionar produto: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // atualizar produto (exceto PK)
    public void atualizarProduto(model.vo.ProdutoVO produto) throws Exception {
        java.sql.Connection con = null;
        try {
            if (produto == null) throw new Exception("Produto não informado.");
            if (produto.getProduto_id() <= 0) throw new Exception("ID do produto é obrigatório para atualização.");
            if (produto.getProduto_nome() == null || produto.getProduto_nome().isBlank()) {
                throw new Exception("Nome do produto é obrigatório.");
            }
            if (produto.getProduto_qtdMin() == null || produto.getProduto_qtdMax() == null) {
                throw new Exception("Qtd mínima e máxima são obrigatórias.");
            }
            if (produto.getProduto_qtdMin() < 0) {
                throw new Exception("Qtd mínima não pode ser negativa.");
            }
            if (produto.getProduto_qtdMax() < produto.getProduto_qtdMin()) {
                throw new Exception("Qtd máxima deve ser >= qtd mínima.");
            }
            if (produto.getProduto_tipoPdt() == null || produto.getProduto_tipoPdt().getTipoPdt_id() <= 0) {
                throw new Exception("Tipo de produto é obrigatório e deve ser válido.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.ProdutoDAO dao = new model.dao.ProdutoDAO(con);
            model.vo.ProdutoVO existente = dao.buscarPorId(produto.getProduto_id());
            if (existente == null) {
                throw new Exception("Produto não encontrado para o ID: " + produto.getProduto_id());
            }

            model.dao.TipoProdutoDAO tipoDao = new model.dao.TipoProdutoDAO(con);
            if (tipoDao.buscarPorId(produto.getProduto_tipoPdt().getTipoPdt_id()) == null) {
                throw new Exception("Tipo de produto não encontrado: id=" + produto.getProduto_tipoPdt().getTipoPdt_id());
            }

            dao.atualizarPorId(produto);
            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao atualizar produto: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    public model.vo.ProdutoVO buscarPorId(int id) throws Exception {
        if (id <= 0) throw new Exception("ID inválido.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.ProdutoDAO dao = new model.dao.ProdutoDAO(con);
            return dao.buscarPorId(id);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar produto: " + e.getMessage(), e);
        }
    }

    public model.vo.ProdutoVO buscarPorNome(String nome) throws Exception {
        if (nome == null || nome.isBlank()) throw new Exception("Nome é obrigatório.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.ProdutoDAO dao = new model.dao.ProdutoDAO(con);
            return dao.buscarPorNome(nome);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar produto por nome: " + e.getMessage(), e);
        }
    }

    public java.util.List<model.vo.ProdutoVO> listarTodos(boolean apenasAtivos) throws Exception {
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.ProdutoDAO dao = new model.dao.ProdutoDAO(con);
            return dao.buscarTodos(apenasAtivos);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao listar produtos: " + e.getMessage(), e);
        }
    }

    public java.util.List<model.vo.ProdutoVO> listarPorTipo(int tipoId, boolean apenasAtivos) throws Exception {
        if (tipoId <= 0) throw new Exception("Tipo inválido.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.ProdutoDAO dao = new model.dao.ProdutoDAO(con);
            return dao.buscarPorTipo(tipoId, apenasAtivos);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao listar por tipo: " + e.getMessage(), e);
        }
    }

    // remoção: preferir desativar (soft delete)
    public void desativarProduto(int produtoId) throws Exception {
        java.sql.Connection con = null;
        try {
            if (produtoId <= 0) throw new Exception("ID inválido.");
            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);
            model.dao.ProdutoDAO dao = new model.dao.ProdutoDAO(con);
            if (dao.buscarPorId(produtoId) == null) {
                throw new Exception("Produto não encontrado para o ID: " + produtoId);
            }
            dao.desativarPorId(produtoId);
            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao desativar produto: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // alias semântico para remoção (soft-delete)
    public void removerProduto(int produtoId) throws Exception {
        desativarProduto(produtoId);
    }
}
