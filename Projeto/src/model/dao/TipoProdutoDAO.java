/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.TipoProdutoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoProdutoDAO {
    private Connection con_tipoPdt;

    public TipoProdutoDAO(Connection con_tipoPdt) {
        this.con_tipoPdt = con_tipoPdt;
    }

    // adicionar novo
    public int adicionarNovo(TipoProdutoVO tipoProduto) throws SQLException {
        String sql = "INSERT INTO tb_tipoPdt (tipoPdt_descricao) VALUES (?)";
        try (PreparedStatement tipoPdt_add = con_tipoPdt.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            tipoPdt_add.setString(1, tipoProduto.getTipoPdt_descricao());
            tipoPdt_add.executeUpdate();
            try (ResultSet rs = tipoPdt_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update tipo de produto por id
    public void atualizarPorId(TipoProdutoVO tipoProduto) throws SQLException {
        String sql = "UPDATE tb_tipoPdt SET tipoPdt_descricao = ? WHERE tipoPdt_id = ?";
        try (PreparedStatement tipoPdt_att_id = con_tipoPdt.prepareStatement(sql)) {
            tipoPdt_att_id.setString(1, tipoProduto.getTipoPdt_descricao());
            tipoPdt_att_id.setInt(2, tipoProduto.getTipoPdt_id());
            tipoPdt_att_id.executeUpdate();
        }
    }

    // update tipo de produto por nome
    public void atualizarPorNome(String nomeAntigo, String nomeNovo) throws SQLException {
        String sql = "UPDATE tb_tipoPdt SET tipoPdt_descricao = ? WHERE tipoPdt_descricao = ?";
        try (PreparedStatement tipoPdt_att_nome = con_tipoPdt.prepareStatement(sql)) {
            tipoPdt_att_nome.setString(1, nomeNovo);
            tipoPdt_att_nome.setString(2, nomeAntigo);
            tipoPdt_att_nome.executeUpdate();
        }
    }

    // busca tipo de produto por id
    public TipoProdutoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_tipoPdt WHERE tipoPdt_id = ?";
        try (PreparedStatement tipoPdt_bsc_id = con_tipoPdt.prepareStatement(sql)) {
            tipoPdt_bsc_id.setInt(1, id);
            try (ResultSet rs = tipoPdt_bsc_id.executeQuery()) {
                if (rs.next()) {
                    TipoProdutoVO tipoProduto = new TipoProdutoVO();
                    tipoProduto.setTipoPdt_id(rs.getInt("tipoPdt_id"));
                    tipoProduto.setTipoPdt_descricao(rs.getString("tipoPdt_descricao"));
                    return tipoProduto;
                }
            }
        }
        return null;
    }

    // busca tipo de produto por nome
    public TipoProdutoVO buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_tipoPdt WHERE tipoPdt_descricao = ?";
        try (PreparedStatement tipoPdt_bsc_nome = con_tipoPdt.prepareStatement(sql)) {
            tipoPdt_bsc_nome.setString(1, nome);
            try (ResultSet rs = tipoPdt_bsc_nome.executeQuery()) {
                if (rs.next()) {
                    TipoProdutoVO tipoProduto = new TipoProdutoVO();
                    tipoProduto.setTipoPdt_id(rs.getInt("tipoPdt_id"));
                    tipoProduto.setTipoPdt_descricao(rs.getString("tipoPdt_descricao"));
                    return tipoProduto;
                }
            }
        }
        return null;
    }

    public List<TipoProdutoVO> listarTiposProdutos() throws SQLException {
        List<TipoProdutoVO> lista = new ArrayList<>();

        String sql = "SELECT * FROM tb_tipoPdt";

        try (PreparedStatement ps = con_tipoPdt.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoProdutoVO vo = new TipoProdutoVO();
                vo.setTipoPdt_id(rs.getInt("tipoPdt_id"));
                vo.setTipoPdt_descricao(rs.getString("tipoPdt_descricao"));

                lista.add(vo);
            }
        }

        return lista;
    }
}
