/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.TipoPagamentoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoPagamentoDAO {
    private Connection con_tipoPagamento;

    public TipoPagamentoDAO(Connection con_tipoPagamento) {
        this.con_tipoPagamento = con_tipoPagamento;
    }

    // adicionar novo
    public int adicionarNovo(TipoPagamentoVO tipoPagamento) throws SQLException {
        String sql = "INSERT INTO tb_tipoPagamento (tipoPagamento_descricao) VALUES (?)";
        try (PreparedStatement tipoPagamento_add = con_tipoPagamento.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            tipoPagamento_add.setString(1, tipoPagamento.getTipoPagamento_descricao());
            tipoPagamento_add.executeUpdate();
            try (ResultSet rs = tipoPagamento_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update tipo de pagamento por id
    public void atualizarPorId(TipoPagamentoVO tipoPagamento) throws SQLException {
        String sql = "UPDATE tb_tipoPagamento SET tipoPagamento_descricao = ? WHERE tipoPagamento_id = ?";
        try (PreparedStatement tipoPagamento_att_id = con_tipoPagamento.prepareStatement(sql)) {
            tipoPagamento_att_id.setString(1, tipoPagamento.getTipoPagamento_descricao());
            tipoPagamento_att_id.setInt(2, tipoPagamento.getTipoPagamento_id());
            tipoPagamento_att_id.executeUpdate();
        }
    }

    // update tipo de pagamento por nome
    public void atualizarPorNome(String nomeAntigo, String nomeNovo) throws SQLException {
        String sql = "UPDATE tb_tipoPagamento SET tipoPagamento_descricao = ? WHERE tipoPagamento_descricao = ?";
        try (PreparedStatement tipoPagamento_att_nome = con_tipoPagamento.prepareStatement(sql)) {
            tipoPagamento_att_nome.setString(1, nomeNovo);
            tipoPagamento_att_nome.setString(2, nomeAntigo);
            tipoPagamento_att_nome.executeUpdate();
        }
    }

    // busca tipo de pagamento por id
    public TipoPagamentoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_tipoPagamento WHERE tipoPagamento_id = ?";
        try (PreparedStatement tipoPagamento_bsc_id = con_tipoPagamento.prepareStatement(sql)) {
            tipoPagamento_bsc_id.setInt(1, id);
            try (ResultSet rs = tipoPagamento_bsc_id.executeQuery()) {
                if (rs.next()) {
                    TipoPagamentoVO tipoPagamento = new TipoPagamentoVO();
                    tipoPagamento.setTipoPagamento_id(rs.getInt("tipoPagamento_id"));
                    tipoPagamento.setTipoPagamento_descricao(rs.getString("tipoPagamento_descricao"));
                    return tipoPagamento;
                }
            }
        }
        return null;
    }

    // busca tipo de pagamento por nome
    public TipoPagamentoVO buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_tipoPagamento WHERE tipoPagamento_descricao = ?";
        try (PreparedStatement tipoPagamento_bsc_nome = con_tipoPagamento.prepareStatement(sql)) {
            tipoPagamento_bsc_nome.setString(1, nome);
            try (ResultSet rs = tipoPagamento_bsc_nome.executeQuery()) {
                if (rs.next()) {
                    TipoPagamentoVO tipoPagamento = new TipoPagamentoVO();
                    tipoPagamento.setTipoPagamento_id(rs.getInt("tipoPagamento_id"));
                    tipoPagamento.setTipoPagamento_descricao(rs.getString("tipoPagamento_descricao"));
                    return tipoPagamento;
                }
            }
        }
        return null;
    }
}
