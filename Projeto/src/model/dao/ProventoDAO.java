package model.dao;

import model.vo.ProventoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProventoDAO {
    private Connection con_provento;

    public ProventoDAO(Connection con_provento) {
        this.con_provento = con_provento;
    }

    // adicionar novo provento
    public int adicionarNovo(ProventoVO provento) throws SQLException {
        String sql = "INSERT INTO tb_provento (provento_descricao) VALUES (?)";
        try (PreparedStatement provento_add = con_provento.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            provento_add.setString(1, provento.getProvento_descricao());
            provento_add.executeUpdate();
            try (ResultSet rs = provento_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update provento por id
    public void atualizarPorId(ProventoVO provento) throws SQLException {
        String sql = "UPDATE tb_provento SET provento_descricao = ? WHERE provento_id = ?";
        try (PreparedStatement provento_att_id = con_provento.prepareStatement(sql)) {
            provento_att_id.setString(1, provento.getProvento_descricao());
            provento_att_id.setInt(2, provento.getProvento_id());
            provento_att_id.executeUpdate();
        }
    }

    // update provento por descricao
    public void atualizarPorDescricao(String descricaoAntiga, String descricaoNova) throws SQLException {
        String sql = "UPDATE tb_provento SET provento_descricao = ? WHERE provento_descricao = ?";
        try (PreparedStatement provento_att_desc = con_provento.prepareStatement(sql)) {
            provento_att_desc.setString(1, descricaoNova);
            provento_att_desc.setString(2, descricaoAntiga);
            provento_att_desc.executeUpdate();
        }
    }

    // busca provento por id
    public ProventoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_provento WHERE provento_id = ?";
        ProventoVO provento = null;
        try (PreparedStatement provento_bsc_id = con_provento.prepareStatement(sql)) {
            provento_bsc_id.setInt(1, id);
            try (ResultSet rs = provento_bsc_id.executeQuery()) {
                if (rs.next()) {
                    provento = new ProventoVO();
                    provento.setProvento_id(rs.getInt("provento_id"));
                    provento.setProvento_descricao(rs.getString("provento_descricao"));
                }
            }
        }
        return provento;
    }

    // busca provento por descricao
    public ProventoVO buscarPorDescricao(String descricao) throws SQLException {
        String sql = "SELECT * FROM tb_provento WHERE provento_descricao = ?";
        ProventoVO provento = null;
        try (PreparedStatement provento_bsc_desc = con_provento.prepareStatement(sql)) {
            provento_bsc_desc.setString(1, descricao);
            try (ResultSet rs = provento_bsc_desc.executeQuery()) {
                if (rs.next()) {
                    provento = new ProventoVO();
                    provento.setProvento_id(rs.getInt("provento_id"));
                    provento.setProvento_descricao(rs.getString("provento_descricao"));
                }
            }
        }
        return provento;
    }
}
