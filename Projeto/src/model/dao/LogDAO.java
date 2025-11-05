/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import java.sql.*;

import model.vo.LogVO;

public class LogDAO {
    private Connection con_log;

    public LogDAO(Object o) throws SQLException{
        this.con_log = ConexaoDAO.getConexao();
    }

    // registrar log

    public void registrarAcao(LogVO log) throws SQLException{
        String sql = "INSERT INTO tb_log (log_acao, log_dataHora, log_fnc_id) VALUES (?, ?, ?)";
        PreparedStatement log_add = con_log.prepareStatement(sql);

        log_add.setString(1, log.getLog_acao());
        // Converter java.time.LocalDateTime para java.sql.Timestamp para o PreparedStatement
        log_add.setTimestamp(2, Timestamp.valueOf(log.getLog_dataHora()));
        log_add.setInt(3, log.getLog_fnc_id());

        log_add.executeUpdate();

        log_add.close();
        con_log.close();
    }
}
