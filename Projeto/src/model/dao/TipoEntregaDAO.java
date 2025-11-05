/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.TipoEntregaVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TipoEntregaDAO {
    private Connection con_tipoEntrega;

    public TipoEntregaDAO(Connection con_tipoEntrega) {
        this.con_tipoEntrega = con_tipoEntrega;
    }

    // adicionar novo tipo de entrega
    public int adicionarNovo(TipoEntregaVO tipoEntrega) throws SQLException {
        String sql = "INSERT INTO tb_tipoEntrega (tipoEntrega_descricao) VALUES (?)";
        try (PreparedStatement tipoEntrega_add = con_tipoEntrega.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            tipoEntrega_add.setString(1, tipoEntrega.getTipoEntrega_descricao());
            tipoEntrega_add.executeUpdate();
            try (ResultSet rs = tipoEntrega_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update tipo de entrega por id
    public void atualizarPorId(TipoEntregaVO tipoEntrega) throws SQLException {
        String sql = "UPDATE tb_tipoEntrega SET tipoEntrega_descricao = ? WHERE tipoEntrega_id = ?";
        try (PreparedStatement tipoEntrega_att_id = con_tipoEntrega.prepareStatement(sql)) {
            tipoEntrega_att_id.setString(1, tipoEntrega.getTipoEntrega_descricao());
            tipoEntrega_att_id.setInt(2, tipoEntrega.getTipoEntrega_id());
            tipoEntrega_att_id.executeUpdate();
        }
    }

    // update tipo de entrega por descricao
    public void atualizarPorDescricao(String descricaoAntiga, TipoEntregaVO tipoEntregaNovo) throws SQLException {
        String sql = "UPDATE tb_tipoEntrega SET tipoEntrega_descricao = ? WHERE tipoEntrega_descricao = ?";
        try (PreparedStatement tipoEntrega_att_desc = con_tipoEntrega.prepareStatement(sql)) {
            tipoEntrega_att_desc.setString(1, tipoEntregaNovo.getTipoEntrega_descricao());
            tipoEntrega_att_desc.setString(2, descricaoAntiga);
            tipoEntrega_att_desc.executeUpdate();
        }
    }

    // busca tipo de entrega por id
    public TipoEntregaVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_tipoEntrega WHERE tipoEntrega_id = ?";
        TipoEntregaVO tipoEntrega = null;
        try (PreparedStatement tipoEntrega_bsc_id = con_tipoEntrega.prepareStatement(sql)) {
            tipoEntrega_bsc_id.setInt(1, id);
            try (ResultSet rs = tipoEntrega_bsc_id.executeQuery()) {
                if (rs.next()) {
                    tipoEntrega = new TipoEntregaVO();
                    tipoEntrega.setTipoEntrega_id(rs.getInt("tipoEntrega_id"));
                    tipoEntrega.setTipoEntrega_descricao(rs.getString("tipoEntrega_descricao"));
                }
            }
        }
        return tipoEntrega;
    }

    // busca tipo de entrega por descricao
    public TipoEntregaVO buscarPorDescricao(String descricao) throws SQLException {
        String sql = "SELECT * FROM tb_tipoEntrega WHERE tipoEntrega_descricao = ?";
        TipoEntregaVO tipoEntrega = null;
        try (PreparedStatement tipoEntrega_bsc_desc = con_tipoEntrega.prepareStatement(sql)) {
            tipoEntrega_bsc_desc.setString(1, descricao);
            try (ResultSet rs = tipoEntrega_bsc_desc.executeQuery()) {
                if (rs.next()) {
                    tipoEntrega = new TipoEntregaVO();
                    tipoEntrega.setTipoEntrega_id(rs.getInt("tipoEntrega_id"));
                    tipoEntrega.setTipoEntrega_descricao(rs.getString("tipoEntrega_descricao"));
                }
            }
        }
        return tipoEntrega;
    }
}
