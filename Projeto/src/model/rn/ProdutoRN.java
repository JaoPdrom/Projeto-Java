/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;
import java.sql.SQLException;
import java.util.List;

import model.dao.ConexaoDAO;
import model.dao.ProdutoDAO;
import model.dao.TipoProdutoDAO;
import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;


public class ProdutoRN {

    // adicionar novo produto
    public int adicionarProduto(model.vo.ProdutoVO produto) throws Exception {
        java.sql.Connection con = null;
        try {
            info("Iniciando cadastro de produto.");
            validarProduto(produto);

            produto.setProduto_nome(produto.getProduto_nome().trim());
            if (produto.getProduto_ativo() == null) {
                produto.setProduto_ativo(Boolean.TRUE);
            }

            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            TipoProdutoDAO tipoDAO = new TipoProdutoDAO(con);
            TipoProdutoVO tipo = tipoDAO.buscarPorId(produto.getProduto_tipoPdt().getTipoPdt_id());
            if (tipo == null) {
                String msg = "Tipo de produto nao encontrado.";
                alerta(msg);
                throw new Exception(msg);
            }
            produto.setProduto_tipoPdt(tipo);

            ProdutoDAO produtoDAO = new model.dao.ProdutoDAO(con);
            ProdutoVO duplicado = produtoDAO.buscarPorNome(produto.getProduto_nome());
            if (duplicado != null) {
                String msg = "Ja existe um produto cadastrado com este nome.";
                alerta(msg);
                throw new Exception(msg);
            }

            int id = produtoDAO.adicionarNovo(produto);
            if (id <= 0) {
                String msg = "Falha ao inserir produto.";
                erro(msg, null);
                throw new Exception(msg);
            }

            info("Produto cadastrado com sucesso. ID: " + id);
            con.commit();
            return id;
        } catch (SQLException e) {
            if (con != null) con.rollback();
            erro("Erro ao adicionar produto.", e);
            throw new Exception("Erro ao adicionar produto: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    private void validarProduto(ProdutoVO produto) throws Exception {
        if (produto == null) {
            String msg = "Produto nao informado.";
            alerta(msg);
            throw new Exception(msg);
        }

        if (produto.getProduto_nome() == null || produto.getProduto_nome().isBlank()) {
            String msg = "Nome do produto e obrigatorio.";
            alerta(msg);
            throw new Exception(msg);
        }

        if (produto.getProduto_nome().trim().length() > 45) {
            String msg = "Nome do produto deve possuir no maximo 45 caracteres.";
            alerta(msg);
            throw new Exception(msg);
        }

        if (produto.getProduto_tipoPdt() == null || produto.getProduto_tipoPdt().getTipoPdt_id() <= 0) {
            String msg = "Tipo de produto invalido.";
            alerta(msg);
            throw new Exception(msg);
        }

        validarPeso(produto.getProduto_peso());
    }

    private void validarPeso(int peso) throws Exception {
        if (peso <= 0) {
            String msg = "Peso do produto nao informado.";
            alerta(msg);
            throw new Exception(msg);
        }

        if (peso < 5 || peso > 50) {
            String msg = "Peso do produto deve estar entre 5kg e 50kg.";
            alerta(msg);
            throw new Exception(msg);
        }
        
        if (peso % 5 != 0) {
            String msg = "Peso do produto deve ser multiplo de 5kg.";
            alerta(msg);
            throw new Exception(msg);
        }
    }

    public ProdutoVO buscarPorId(int produtoId) throws Exception {
        if (produtoId <= 0) {
            String msg = "ID de produto invalido.";
            alerta(msg);
            throw new Exception(msg);
        }

        try (java.sql.Connection con = ConexaoDAO.getConexao()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(con);
            ProdutoVO produto = produtoDAO.buscarPorId(produtoId);
            if (produto == null) {
                String msg = "Produto nao encontrado para o ID informado.";
                alerta(msg);
                throw new Exception(msg);
            }
            return produto;
        } catch (SQLException e) {
            erro("Erro ao buscar produto por ID.", e);
            throw new Exception("Erro ao buscar produto por ID: " + e.getMessage(), e);
        }
    }

    public List<ProdutoVO> buscarPorNome(String termo) throws Exception {
        if (termo == null || termo.isBlank()) {
            throw new Exception("Nome do produto para busca nao informado.");
        }

        try (java.sql.Connection con = ConexaoDAO.getConexao()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(con);
            return produtoDAO.buscarPorNomeParcial(termo.trim());
        } catch (SQLException e) {
            erro("Erro ao buscar produtos por nome.", e);
            throw new Exception("Erro ao buscar produtos por nome: " + e.getMessage(), e);
        }
    }

    public void editarProduto(ProdutoVO produto) throws Exception {
        java.sql.Connection con = null;
        try {
            if (produto == null || produto.getProduto_id() <= 0) {
                String msg = "Produto ou ID invalido para edicao.";
                alerta(msg);
                throw new Exception(msg);
            }

            validarProduto(produto);

            produto.setProduto_nome(produto.getProduto_nome().trim());

            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            ProdutoDAO produtoDAO = new ProdutoDAO(con);
            ProdutoVO existente = produtoDAO.buscarPorId(produto.getProduto_id());
            if (existente == null) {
                String msg = "Produto nao encontrado para o ID informado.";
                alerta(msg);
                throw new Exception(msg);
            }

            TipoProdutoDAO tipoDAO = new TipoProdutoDAO(con);
            TipoProdutoVO tipo = tipoDAO.buscarPorId(produto.getProduto_tipoPdt().getTipoPdt_id());
            if (tipo == null) {
                String msg = "Tipo de produto nao encontrado.";
                alerta(msg);
                throw new Exception(msg);
            }
            produto.setProduto_tipoPdt(tipo);

            ProdutoVO duplicado = produtoDAO.buscarPorNome(produto.getProduto_nome());
            if (duplicado != null && duplicado.getProduto_id() != produto.getProduto_id()) {
                String msg = "Ja existe outro produto com este nome.";
                alerta(msg);
                throw new Exception(msg);
            }

            produtoDAO.atualizarPorId(produto);
            con.commit();
            info("Produto atualizado com sucesso. ID: " + produto.getProduto_id());
        } catch (SQLException e) {
            if (con != null) con.rollback();
            erro("Erro ao editar produto.", e);
            throw new Exception("Erro ao editar produto: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    public void excluirProduto(int produtoId) throws Exception {
        java.sql.Connection con = null;
        try {
            if (produtoId <= 0) {
                String msg = "ID de produto invalido para exclusao.";
                alerta(msg);
                throw new Exception(msg);
            }

            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            ProdutoDAO produtoDAO = new ProdutoDAO(con);
            ProdutoVO existente = produtoDAO.buscarPorId(produtoId);
            if (existente == null) {
                String msg = "Produto nao encontrado para o ID informado.";
                alerta(msg);
                throw new Exception(msg);
            }

            produtoDAO.desativarPorId(produtoId);
            con.commit();
            info("Produto desativado com sucesso. ID: " + produtoId);
        } catch (SQLException e) {
            if (con != null) con.rollback();
            erro("Erro ao desativar produto.", e);
            throw new Exception("Erro ao desativar produto: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    public List<TipoProdutoVO> listarTiposProdutos() throws Exception {
        try (java.sql.Connection con = ConexaoDAO.getConexao()) {
            TipoProdutoDAO tipoProdutoDAO = new TipoProdutoDAO(con);
            return tipoProdutoDAO.listarTiposProdutos();
        } catch (SQLException e) {
            erro("Erro ao retornar tipos de produtos.", e);
            throw new Exception("Erro ao retornar tipos de produtos: " + e.getMessage(), e);
        }
    }

    // Cor ANSI para o console (funciona no VS Code, IntelliJ e CMD moderno)
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    private void info(String msg) {
        System.out.println(GREEN + "[INFO:ProdutoRN] " + msg + RESET);
    }

    private void alerta(String msg) {
        System.out.println(YELLOW + "[AVISO:ProdutoRN] " + msg + RESET);
    }

    private void erro(String msg, Exception e) {
        System.err.println(RED + "[ERRO:ProdutoRN] " + msg + RESET);
        if (e != null) e.printStackTrace();
    }
}
