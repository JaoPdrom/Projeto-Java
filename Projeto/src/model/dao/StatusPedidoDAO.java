/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.StatusPedidoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatusPedidoDAO {
    private Connection con_statusPedido;

    public StatusPedidoDAO(Connection con_statusPedido) {
        this.con_statusPedido = con_statusPedido;
    }

    // adicionar novo status de pedido
    public int adicionarNovo(StatusPedidoVO statusPedido) throws SQLException {
        String sql = "INSERT INTO tb_statusPedido (statusPedido_descricao) VALUES (?)";
        try (PreparedStatement statusPedido_add = con_statusPedido.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statusPedido_add.setString(1, statusPedido.getStatusPedido_descricao());
            statusPedido_add.executeUpdate();
            try (ResultSet rs = statusPedido_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update status de pedido por id
    public void atualizarPorId(StatusPedidoVO statusPedido) throws SQLException {
        String sql = "UPDATE tb_statusPedido SET statusPedido_descricao = ? WHERE statusPedido_id = ?";
        try (PreparedStatement statusPedido_att_id = con_statusPedido.prepareStatement(sql)) {
            statusPedido_att_id.setString(1, statusPedido.getStatusPedido_descricao());
            statusPedido_att_id.setInt(2, statusPedido.getStatusPedido_id());
            statusPedido_att_id.executeUpdate();
        }
    }

    // update status de pedido por descricao
    public void atualizarPorDescricao(String descricaoAntiga, StatusPedidoVO statusPedidoNovo) throws SQLException {
        String sql = "UPDATE tb_statusPedido SET statusPedido_descricao = ? WHERE statusPedido_descricao = ?";
        try (PreparedStatement statusPedido_att_desc = con_statusPedido.prepareStatement(sql)) {
            statusPedido_att_desc.setString(1, statusPedidoNovo.getStatusPedido_descricao());
            statusPedido_att_desc.setString(2, descricaoAntiga);
            statusPedido_att_desc.executeUpdate();
        }
    }

    // busca status de pedido por id
    public StatusPedidoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_statusPedido WHERE statusPedido_id = ?";
        StatusPedidoVO statusPedido = null;
        try (PreparedStatement statusPedido_bsc_id = con_statusPedido.prepareStatement(sql)) {
            statusPedido_bsc_id.setInt(1, id);
            try (ResultSet rs = statusPedido_bsc_id.executeQuery()) {
                if (rs.next()) {
                    statusPedido = new StatusPedidoVO();
                    statusPedido.setStatusPedido_id(rs.getInt("statusPedido_id"));
                    statusPedido.setStatusPedido_descricao(rs.getString("statusPedido_descricao"));
                }
            }
        }
        return statusPedido;
    }

    // busca status de pedido por descricao
    public StatusPedidoVO buscarPorDescricao(String descricao) throws SQLException {
        String sql = "SELECT * FROM tb_statusPedido WHERE statusPedido_descricao = ?";
        StatusPedidoVO statusPedido = null;
        try (PreparedStatement statusPedido_bsc_desc = con_statusPedido.prepareStatement(sql)) {
            statusPedido_bsc_desc.setString(1, descricao);
            try (ResultSet rs = statusPedido_bsc_desc.executeQuery()) {
                if (rs.next()) {
                    statusPedido = new StatusPedidoVO();
                    statusPedido.setStatusPedido_id(rs.getInt("statusPedido_id"));
                    statusPedido.setStatusPedido_descricao(rs.getString("statusPedido_descricao"));
                }
            }
        }
        return statusPedido;
    }
}
