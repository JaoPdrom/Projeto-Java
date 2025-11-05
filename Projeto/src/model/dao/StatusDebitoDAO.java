/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.StatusDebitoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatusDebitoDAO {
    private Connection con_statusDebito;

    public StatusDebitoDAO(Connection con_statusDebito) {
        this.con_statusDebito = con_statusDebito;
    }

    // adicionar novo status de debito
    public int adicionarNovo(StatusDebitoVO statusDebito) throws SQLException {
        String sql = "INSERT INTO tb_statusDebito (statusDeb_descricao) VALUES (?)";
        try (PreparedStatement statusDebito_add = con_statusDebito.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statusDebito_add.setString(1, statusDebito.getStatusDeb_descricao());
            statusDebito_add.executeUpdate();
            try (ResultSet rs = statusDebito_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update status de debito por id
    public void atualizarPorId(StatusDebitoVO statusDebito) throws SQLException {
        String sql = "UPDATE tb_statusDebito SET statusDeb_descricao = ? WHERE statusDeb_id = ?";
        try (PreparedStatement statusDebito_att_id = con_statusDebito.prepareStatement(sql)) {
            statusDebito_att_id.setString(1, statusDebito.getStatusDeb_descricao());
            statusDebito_att_id.setInt(2, statusDebito.getStatusDeb_id());
            statusDebito_att_id.executeUpdate();
        }
    }

    // update status de debito por descricao
    public void atualizarPorDescricao(String descricaoAntiga, StatusDebitoVO statusDebitoNovo) throws SQLException {
        String sql = "UPDATE tb_statusDebito SET statusDeb_descricao = ? WHERE statusDeb_descricao = ?";
        try (PreparedStatement statusDebito_att_desc = con_statusDebito.prepareStatement(sql)) {
            statusDebito_att_desc.setString(1, statusDebitoNovo.getStatusDeb_descricao());
            statusDebito_att_desc.setString(2, descricaoAntiga);
            statusDebito_att_desc.executeUpdate();
        }
    }

    // busca status de debito por id
    public StatusDebitoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_statusDebito WHERE statusDeb_id = ?";
        StatusDebitoVO statusDebito = null;
        try (PreparedStatement statusDebito_bsc_id = con_statusDebito.prepareStatement(sql)) {
            statusDebito_bsc_id.setInt(1, id);
            try (ResultSet rs = statusDebito_bsc_id.executeQuery()) {
                if (rs.next()) {
                    statusDebito = new StatusDebitoVO();
                    statusDebito.setStatusDeb_id(rs.getInt("statusDeb_id"));
                    statusDebito.setStatusDeb_descricao(rs.getString("statusDeb_descricao"));
                }
            }
        }
        return statusDebito;
    }

    // busca status de debito por descricao
    public StatusDebitoVO buscarPorDescricao(String descricao) throws SQLException {
        String sql = "SELECT * FROM tb_statusDebito WHERE statusDeb_descricao = ?";
        StatusDebitoVO statusDebito = null;
        try (PreparedStatement statusDebito_bsc_desc = con_statusDebito.prepareStatement(sql)) {
            statusDebito_bsc_desc.setString(1, descricao);
            try (ResultSet rs = statusDebito_bsc_desc.executeQuery()) {
                if (rs.next()) {
                    statusDebito = new StatusDebitoVO();
                    statusDebito.setStatusDeb_id(rs.getInt("statusDeb_id"));
                    statusDebito.setStatusDeb_descricao(rs.getString("statusDeb_descricao"));
                }
            }
        }
        return statusDebito;
    }
}
