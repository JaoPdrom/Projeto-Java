/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.LogradouroVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LogradouroDAO {
    private Connection con_logradouro;

    public LogradouroDAO(Connection con_logradouro) {
        this.con_logradouro = con_logradouro;
    }

    public LogradouroDAO() throws SQLException {}

    // adicionar novo logradouro
    public int adicionarNovo(LogradouroVO logradouro) throws SQLException {
        String sql = "INSERT INTO tb_logradouro (log_descricao) VALUES (?)";
        try (PreparedStatement log_add = con_logradouro.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            log_add.setString(1, logradouro.getLogradouro_descricao());
            log_add.executeUpdate();
            try (ResultSet rs = log_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update logradouro por id
    public void atualizarPorId(LogradouroVO logradouro) throws SQLException {
        String sql = "UPDATE tb_logradouro SET log_descricao = ? WHERE logradouro_id = ?";
        try (PreparedStatement log_att = con_logradouro.prepareStatement(sql)) {
            log_att.setString(1, logradouro.getLogradouro_descricao());
            log_att.setInt(2, logradouro.getLogradouro_id());
            log_att.executeUpdate();
        }
    }

    // update logradouro por nome
    public void atualizarPorDescricao(String descricaoAntiga, String descricaoNova) throws SQLException {
        String sql = "UPDATE tb_logradouro SET log_descricao = ? WHERE log_descricao = ?";
        try (PreparedStatement log_att_nome = con_logradouro.prepareStatement(sql)) {
            log_att_nome.setString(1, descricaoNova);
            log_att_nome.setString(2, descricaoAntiga);
            log_att_nome.executeUpdate();
        }
    }

    // busca logradouro por id
    public LogradouroVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_logradouro WHERE logradouro_id = ?";
        try (PreparedStatement stmt = con_logradouro.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LogradouroVO logradouro = new LogradouroVO();
                    logradouro.setLogradouro_id(rs.getInt("logradouro_id"));
                    logradouro.setLogradouro_descricao(rs.getString("log_descricao"));
                    return logradouro;
                }
            }
        }
        return null;
    }

    // busca logradouro por nome
    public LogradouroVO buscarPorDescricao(String descricao) throws SQLException {
        String sql = "SELECT * FROM tb_logradouro WHERE log_descricao = ?";
        try (PreparedStatement stmt = con_logradouro.prepareStatement(sql)) {
            stmt.setString(1, descricao);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LogradouroVO logradouro = new LogradouroVO();
                    logradouro.setLogradouro_id(rs.getInt("logradouro_id"));
                    logradouro.setLogradouro_descricao(rs.getString("log_descricao"));
                    return logradouro;
                }
            }
        }
        return null;
    }

    public List<LogradouroVO> buscarTodosLogradouros() throws SQLException {
        List<LogradouroVO> logradouros = new ArrayList<>();
        String sql = "SELECT logradouro_id, log_descricao FROM tb_logradouro";

        try (PreparedStatement stmt = con_logradouro.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                logradouros.add(new LogradouroVO(
                        rs.getInt("logradouro_id"),
                        rs.getString("log_descricao")));
            }
        }
        return logradouros;
    }
}

/*
        carregarEstados();
        carregarCidades();
        carregarSexo();
        carregarBairro();
        carregarLogradouro();
 */
