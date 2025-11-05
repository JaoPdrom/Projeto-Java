/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.StatusVendaVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StatusVendaDAO {
    private Connection con_statusVenda;

    public StatusVendaDAO(Connection con_statusVenda) {
        this.con_statusVenda = con_statusVenda;
    }

    // adicionar novo
    public int adicionarNovo(StatusVendaVO statusVenda) throws SQLException {
        String sql = "INSERT INTO tb_statusVenda (statusVenda_descricao) VALUES (?)";
        try (PreparedStatement statusVenda_add = con_statusVenda.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statusVenda_add.setString(1, statusVenda.getStatusVenda_descricao());
            statusVenda_add.executeUpdate();
            try (ResultSet rs = statusVenda_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update status de venda por id
    public void atualizarPorId(StatusVendaVO statusVenda) throws SQLException {
        String sql = "UPDATE tb_statusVenda SET statusVenda_descricao = ? WHERE statusVenda_id = ?";
        try (PreparedStatement statusVenda_att_id = con_statusVenda.prepareStatement(sql)) {
            statusVenda_att_id.setString(1, statusVenda.getStatusVenda_descricao());
            statusVenda_att_id.setInt(2, statusVenda.getStatusVenda_id());
            statusVenda_att_id.executeUpdate();
        }
    }

    // update status de venda por nome
    public void atualizarPorNome(String nomeAntigo, String nomeNovo) throws SQLException {
        String sql = "UPDATE tb_statusVenda SET statusVenda_descricao = ? WHERE statusVenda_descricao = ?";
        try (PreparedStatement statusVenda_att_nome = con_statusVenda.prepareStatement(sql)) {
            statusVenda_att_nome.setString(1, nomeNovo);
            statusVenda_att_nome.setString(2, nomeAntigo);
            statusVenda_att_nome.executeUpdate();
        }
    }

    // busca status de venda por id
    public StatusVendaVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_statusVenda WHERE statusVenda_id = ?";
        try (PreparedStatement statusVenda_bsc_id = con_statusVenda.prepareStatement(sql)) {
            statusVenda_bsc_id.setInt(1, id);
            try (ResultSet rs = statusVenda_bsc_id.executeQuery()) {
                if (rs.next()) {
                    StatusVendaVO statusVenda = new StatusVendaVO();
                    statusVenda.setStatusVenda_id(rs.getInt("statusVenda_id"));
                    statusVenda.setStatusVenda_descricao(rs.getString("statusVenda_descricao"));
                    return statusVenda;
                }
            }
        }
        return null;
    }

    // busca status de venda por nome
    public StatusVendaVO buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_statusVenda WHERE statusVenda_descricao = ?";
        try (PreparedStatement statusVenda_bsc_nome = con_statusVenda.prepareStatement(sql)) {
            statusVenda_bsc_nome.setString(1, nome);
            try (ResultSet rs = statusVenda_bsc_nome.executeQuery()) {
                if (rs.next()) {
                    StatusVendaVO statusVenda = new StatusVendaVO();
                    statusVenda.setStatusVenda_id(rs.getInt("statusVenda_id"));
                    statusVenda.setStatusVenda_descricao(rs.getString("statusVenda_descricao"));
                    return statusVenda;
                }
            }
        }
        return null;
    }
}
