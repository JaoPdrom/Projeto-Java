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
import java.util.ArrayList;
import java.util.List;

public class LogradouroDAO {
    private Connection con_logradouro;

    public LogradouroDAO(Connection con_logradouro) {
        this.con_logradouro = con_logradouro;
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
