/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */
package model.rn;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.dao.ConexaoDAO;
import model.dao.EstoqueDAO;
import model.dao.ProdutoDAO;
import model.vo.EstoqueVO;
import model.vo.ProdutoVO;

public class EstoqueRN {

    public int adicionarEstoque(EstoqueVO estoque) throws Exception {
        Connection con = null;
        try {
            validarEstoque(estoque);

            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            ProdutoDAO produtoDAO = new ProdutoDAO(con);
            ProdutoVO produto = produtoDAO.buscarPorId(estoque.getEst_produto_id().getProduto_id());
            if (produto == null) {
                throw new Exception("Produto do estoque nao encontrado.");
            }

            EstoqueDAO estoqueDAO = new EstoqueDAO(con);
            int id = estoqueDAO.adicionarNovo(estoque);
            if (id <= 0) {
                throw new Exception("Falha ao inserir estoque.");
            }

            con.commit();
            return id;
        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            throw new Exception("Erro ao adicionar estoque: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public void atualizarEstoque(EstoqueVO estoque) throws Exception {
        Connection con = null;
        try {
            if (estoque == null || estoque.getEst_id() <= 0) {
                throw new Exception("ID de estoque invalido.");
            }

            validarEstoque(estoque);

            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            ProdutoDAO produtoDAO = new ProdutoDAO(con);
            ProdutoVO produto = produtoDAO.buscarPorId(estoque.getEst_produto_id().getProduto_id());
            if (produto == null) {
                throw new Exception("Produto do estoque nao encontrado.");
            }

            EstoqueDAO estoqueDAO = new EstoqueDAO(con);
            estoqueDAO.atualizarPorId(estoque);

            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            throw new Exception("Erro ao atualizar estoque: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public EstoqueVO buscarEntradaPorProduto(int produtoId) throws Exception {
        try (Connection con = ConexaoDAO.getConexao()) {
            EstoqueDAO estoqueDAO = new EstoqueDAO(con);
            List<EstoqueVO> lista = estoqueDAO.listarPorProdutoOrdenado(produtoId);
            if (!lista.isEmpty()) {
                return lista.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar estoque do produto: " + e.getMessage(), e);
        }
    }

    public List<EstoqueVO> listarTodosEstoques() throws Exception {
        try (Connection con = ConexaoDAO.getConexao()) {
            EstoqueDAO estoqueDAO = new EstoqueDAO(con);
            return estoqueDAO.listarTodos();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar estoques: " + e.getMessage(), e);
        }
    }

    private void validarEstoque(EstoqueVO estoque) throws Exception {
        if (estoque == null) {
            throw new Exception("Estoque nao informado.");
        }

        if (estoque.getEst_produto_id() == null || estoque.getEst_produto_id().getProduto_id() <= 0) {
            throw new Exception("Produto do estoque nao informado.");
        }

        if (estoque.getEst_dtCompra() == null) {
            throw new Exception("Data de compra e obrigatoria.");
        }

        if (estoque.getEst_dtValidade() != null &&
            estoque.getEst_dtValidade().isBefore(estoque.getEst_dtCompra())) {
            throw new Exception("Data de validade nao pode ser anterior a data de compra.");
        }

        if (estoque.getEst_qtdMin() < 0) {
            throw new Exception("Quantidade minima deve ser maior ou igual a zero.");
        }

        if (estoque.getEst_qtdMax() <= 0 || estoque.getEst_qtdMax() < estoque.getEst_qtdMin()) {
            throw new Exception("Quantidade maxima deve ser maior que zero e maior/igual a minima.");
        }

        if (estoque.getQtdTotal() <= 0) {
            throw new Exception("Quantidade inicial deve ser maior que zero.");
        }

        if (estoque.getEst_custo() < 0) {
            throw new Exception("Custo deve ser maior ou igual a zero.");
        }
    }
}
