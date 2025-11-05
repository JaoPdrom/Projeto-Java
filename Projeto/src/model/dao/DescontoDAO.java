package model.dao;

import model.vo.DescontoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DescontoDAO {
    private Connection con_desconto;

    public DescontoDAO(Connection con_desconto) {
        this.con_desconto = con_desconto;
    }

    // adicionar novo desconto
    public int adicionarNovo(DescontoVO desconto) throws SQLException {
        String sql = "INSERT INTO tb_desconto (desconto_descricao) VALUES (?)";
        try (PreparedStatement desconto_add = con_desconto.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            desconto_add.setString(1, desconto.getDesconto_descricao());
            desconto_add.executeUpdate();
            try (ResultSet rs = desconto_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update desconto por id
    public void atualizarPorId(DescontoVO desconto) throws SQLException {
        String sql = "UPDATE tb_desconto SET desconto_descricao = ? WHERE desconto_id = ?";
        try (PreparedStatement desconto_att_id = con_desconto.prepareStatement(sql)) {
            desconto_att_id.setString(1, desconto.getDesconto_descricao());
            desconto_att_id.setInt(2, desconto.getDesconto_id());
            desconto_att_id.executeUpdate();
        }
    }

    // update desconto por descricao
    public void atualizarPorDescricao(String descricaoAntiga, String descricaoNova) throws SQLException {
        String sql = "UPDATE tb_desconto SET desconto_descricao = ? WHERE desconto_descricao = ?";
        try (PreparedStatement desconto_att_desc = con_desconto.prepareStatement(sql)) {
            desconto_att_desc.setString(1, descricaoNova);
            desconto_att_desc.setString(2, descricaoAntiga);
            desconto_att_desc.executeUpdate();
        }
    }

    // busca desconto por id
    public DescontoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_desconto WHERE desconto_id = ?";
        DescontoVO desconto = null;
        try (PreparedStatement desconto_bsc_id = con_desconto.prepareStatement(sql)) {
            desconto_bsc_id.setInt(1, id);
            try (ResultSet rs = desconto_bsc_id.executeQuery()) {
                if (rs.next()) {
                    desconto = new DescontoVO();
                    desconto.setDesconto_id(rs.getInt("desconto_id"));
                    desconto.setDesconto_descricao(rs.getString("desconto_descricao"));
                }
            }
        }
        return desconto;
    }

    // busca desconto por descricao
    public DescontoVO buscarPorDescricao(String descricao) throws SQLException {
        String sql = "SELECT * FROM tb_desconto WHERE desconto_descricao = ?";
        DescontoVO desconto = null;
        try (PreparedStatement desconto_bsc_desc = con_desconto.prepareStatement(sql)) {
            desconto_bsc_desc.setString(1, descricao);
            try (ResultSet rs = desconto_bsc_desc.executeQuery()) {
                if (rs.next()) {
                    desconto = new DescontoVO();
                    desconto.setDesconto_id(rs.getInt("desconto_id"));
                    desconto.setDesconto_descricao(rs.getString("desconto_descricao"));
                }
            }
        }
        return desconto;
    }
}
