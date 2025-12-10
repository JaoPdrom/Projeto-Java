/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CidEstDAO {
    private final Connection con;

    public CidEstDAO(Connection con) {
        this.con = con;
    }

    public boolean garantirRelacionamento(int cidadeId, String estadoSigla) throws SQLException {
        if (cidadeId <= 0 || estadoSigla == null || estadoSigla.isBlank()) {
            throw new SQLException("Cidade ID e sigla do estado são obrigatórios.");
        }

        // Verifica se o relacionamento já existe
        String sqlVerificar = "SELECT COUNT(*) FROM tb_cidEst WHERE cidEstPai_cid_id = ? AND cidEstPai_est_sigla = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlVerificar)) {
            ps.setInt(1, cidadeId);
            ps.setString(2, estadoSigla);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Relacionamento já existe
                    return true;
                }
            }
        }

        // Cria o relacionamento se não existir
        String sqlInserir = "INSERT IGNORE INTO tb_cidEst (cidEstPai_cid_id, cidEstPai_est_sigla) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlInserir)) {
            ps.setInt(1, cidadeId);
            ps.setString(2, estadoSigla);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}

