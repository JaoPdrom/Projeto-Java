/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection con_produto;

    public ProdutoDAO(Connection con_produto) {
        this.con_produto = con_produto;
    }

    // adicionar novo
    public int adicionarNovo(ProdutoVO produto) throws SQLException {
        String sql = "INSERT INTO tb_produto (produto_nome, produto_peso, produto_ativo, produto_tipoPdt) VALUES (?, ?, ?, ?)";
        try (PreparedStatement produto_add = con_produto.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            produto_add.setString(1, produto.getProduto_nome());
            produto_add.setInt(2, produto.getProduto_peso());
            boolean ativo = true;
            if (produto.getProduto_ativo() != null) {
                ativo = produto.getProduto_ativo();
            }
            produto_add.setBoolean(3, ativo);
            produto_add.setInt(4, produto.getProduto_tipoPdt().getTipoPdt_id());
            produto_add.executeUpdate();
            try (ResultSet rs = produto_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update produto por id
    public void atualizarPorId(ProdutoVO produto) throws SQLException {
        String sql = "UPDATE tb_produto SET produto_nome = ?, produto_peso = ?, produto_ativo = ?, produto_tipoPdt = ? WHERE produto_id = ?";
        try (PreparedStatement produto_att_id = con_produto.prepareStatement(sql)) {
            produto_att_id.setString(1, produto.getProduto_nome());
            produto_att_id.setInt(2, produto.getProduto_peso());
            boolean ativo = true;
            if (produto.getProduto_ativo() != null) {
                ativo = produto.getProduto_ativo();
            }
            produto_att_id.setBoolean(3, ativo);
            produto_att_id.setInt(4, produto.getProduto_tipoPdt().getTipoPdt_id());
            produto_att_id.setInt(5, produto.getProduto_id());
            produto_att_id.executeUpdate();
        }
    }

    // update produto por nome
    public void atualizarPorNome(String nomeAntigo, ProdutoVO produtoNovo) throws SQLException {
        String sql = "UPDATE tb_produto SET produto_nome = ?, produto_peso = ?, produto_ativo = ?, produto_tipoPdt = ? WHERE produto_nome = ?";
        try (PreparedStatement produto_att_nome = con_produto.prepareStatement(sql)) {
            produto_att_nome.setString(1, produtoNovo.getProduto_nome());
            produto_att_nome.setInt(2, produtoNovo.getProduto_peso());
            boolean ativo = true;
            if (produtoNovo.getProduto_ativo() != null) {
                ativo = produtoNovo.getProduto_ativo();
            }
            produto_att_nome.setBoolean(3, ativo);
            produto_att_nome.setInt(4, produtoNovo.getProduto_tipoPdt().getTipoPdt_id());
            produto_att_nome.setString(5, nomeAntigo);
            produto_att_nome.executeUpdate();
        }
    }

    // busca produto por id
    public ProdutoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_produto WHERE produto_id = ?";
        ProdutoVO produto = null;
        try (PreparedStatement produto_bsc_id = con_produto.prepareStatement(sql)) {
            produto_bsc_id.setInt(1, id);
            try (ResultSet rs = produto_bsc_id.executeQuery()) {
                if (rs.next()) {
                    produto = new ProdutoVO();
                    produto.setProduto_id(rs.getInt("produto_id"));
                    produto.setProduto_nome(rs.getString("produto_nome"));
                    produto.setProduto_peso(rs.getInt("produto_peso"));
                    produto.setProduto_ativo(rs.getBoolean("produto_ativo"));

                    // Buscando o Tipo de Produto
                    TipoProdutoDAO tipoPdtDAO = new TipoProdutoDAO(con_produto);
                    TipoProdutoVO tipoProduto = tipoPdtDAO.buscarPorId(rs.getInt("produto_tipoPdt"));
                    produto.setProduto_tipoPdt(tipoProduto);
                }
            }
        }
        return produto;
    }

    // busca produto por nome exato (retorna um)
    public ProdutoVO buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_produto WHERE produto_nome = ?";
        ProdutoVO produto = null;
        try (PreparedStatement produto_bsc_nome = con_produto.prepareStatement(sql)) {
            produto_bsc_nome.setString(1, nome);
            try (ResultSet rs = produto_bsc_nome.executeQuery()) {
                if (rs.next()) {
                    produto = new ProdutoVO();
                    produto.setProduto_id(rs.getInt("produto_id"));
                    produto.setProduto_nome(rs.getString("produto_nome"));
                    produto.setProduto_peso(rs.getInt("produto_peso"));
                    produto.setProduto_ativo(rs.getBoolean("produto_ativo"));

                    // Buscando o Tipo de Produto
                    TipoProdutoDAO tipoPdtDAO = new TipoProdutoDAO(con_produto);
                    TipoProdutoVO tipoProduto = tipoPdtDAO.buscarPorId(rs.getInt("produto_tipoPdt"));
                    produto.setProduto_tipoPdt(tipoProduto);
                }
            }
        }
        return produto;
    }

    // busca produtos por nome parcial
    public List<ProdutoVO> buscarPorNomeParcial(String termo) throws SQLException {
        String sql = "SELECT * FROM tb_produto WHERE produto_nome LIKE ? ORDER BY produto_nome";
        List<ProdutoVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_produto.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                TipoProdutoDAO tipoPdtDAO = new TipoProdutoDAO(con_produto);
                while (rs.next()) {
                    ProdutoVO produto = new ProdutoVO();
                    produto.setProduto_id(rs.getInt("produto_id"));
                    produto.setProduto_nome(rs.getString("produto_nome"));
                    produto.setProduto_peso(rs.getInt("produto_peso"));
                    produto.setProduto_ativo(rs.getBoolean("produto_ativo"));
                    produto.setProduto_tipoPdt(tipoPdtDAO.buscarPorId(rs.getInt("produto_tipoPdt")));
                    lista.add(produto);
                }
            }
        }
        return lista;
    }

    // listar todos (opcionalmente apenas ativos)
    public List<ProdutoVO> buscarTodos(boolean apenasAtivos) throws SQLException {
        String sql = "SELECT * FROM tb_produto" + (apenasAtivos ? " WHERE produto_ativo = TRUE" : "") + " ORDER BY produto_nome";
        List<ProdutoVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_produto.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                TipoProdutoDAO tipoPdtDAO = new TipoProdutoDAO(con_produto);
                while (rs.next()) {
                    ProdutoVO p = new ProdutoVO();
                    p.setProduto_id(rs.getInt("produto_id"));
                    p.setProduto_nome(rs.getString("produto_nome"));
                    p.setProduto_peso(rs.getInt("produto_peso"));
                    p.setProduto_ativo(rs.getBoolean("produto_ativo"));
                    p.setProduto_tipoPdt(tipoPdtDAO.buscarPorId(rs.getInt("produto_tipoPdt")));
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    // listar por tipo (opcionalmente apenas ativos)
    public List<ProdutoVO> buscarPorTipo(int tipoId, boolean apenasAtivos) throws SQLException {
        String sql = "SELECT * FROM tb_produto WHERE produto_tipoPdt = ?" + (apenasAtivos ? " AND produto_ativo = TRUE" : "") + " ORDER BY produto_nome";
        List<ProdutoVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_produto.prepareStatement(sql)) {
            ps.setInt(1, tipoId);
            try (ResultSet rs = ps.executeQuery()) {
                TipoProdutoDAO tipoPdtDAO = new TipoProdutoDAO(con_produto);
                while (rs.next()) {
                    ProdutoVO p = new ProdutoVO();
                    p.setProduto_id(rs.getInt("produto_id"));
                    p.setProduto_nome(rs.getString("produto_nome"));
                    p.setProduto_peso(rs.getInt("produto_peso"));
                    p.setProduto_ativo(rs.getBoolean("produto_ativo"));
                    p.setProduto_tipoPdt(tipoPdtDAO.buscarPorId(rs.getInt("produto_tipoPdt")));
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    // desativar (remoção lógica)
    public void desativarPorId(int produtoId) throws SQLException {
        String sql = "UPDATE tb_produto SET produto_ativo = FALSE WHERE produto_id = ?";
        try (PreparedStatement ps = con_produto.prepareStatement(sql)) {
            ps.setInt(1, produtoId);
            ps.executeUpdate();
        }
    }

    

}
