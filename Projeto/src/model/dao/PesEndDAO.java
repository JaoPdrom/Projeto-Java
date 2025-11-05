/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PesEndDAO {
    private final Connection con;

    public PesEndDAO(Connection con) {
        this.con = con;
    }

    public void vincular(String documento, int endId) throws SQLException {
        String sql = "INSERT INTO tb_pesEnd (pesEnd_pes_documento, pesEnd_end_id) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            ps.setInt(2, endId);
            ps.executeUpdate();
        }
    }

    public void desvincular(String documento, int endId) throws SQLException {
        String sql = "DELETE FROM tb_pesEnd WHERE pesEnd_pes_documento = ? AND pesEnd_end_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            ps.setInt(2, endId);
            ps.executeUpdate();
        }
    }

    public Integer buscarEndIdPorDocumento(String documento) throws SQLException {
        String sql = "SELECT pesEnd_end_id FROM tb_pesEnd WHERE pesEnd_pes_documento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("pesEnd_end_id");
                }
            }
        }
        return null; // caso não encontre
    }

}

