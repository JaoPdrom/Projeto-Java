/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.BairroVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BairroDAO {
    private Connection con_bairro;

    public BairroDAO(Connection con_bairro) {
        this.con_bairro = con_bairro;
    }

    public BairroDAO() throws SQLException {}

    // registrar novo bairro

    public int adicionarNovo(BairroVO bairro) throws SQLException {
        String sql = "INSERT INTO tb_bairro (bairro_descricao) VALUES (?)";
        try (PreparedStatement bairro_add = con_bairro.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bairro_add.setString(1, bairro.getBairro_descricao());
            bairro_add.executeUpdate();
            try (ResultSet rs = bairro_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1; // Retorna -1 se a inserção falhar.
    }

    // update bairro por id
    public void atualizarPorId(BairroVO bairro) throws SQLException {
        String sql = "UPDATE tb_bairro SET bairro_descricao = ? WHERE bairro_id = ?";
        try (PreparedStatement bairro_att = con_bairro.prepareStatement(sql)) {
            bairro_att.setString(1, bairro.getBairro_descricao());
            bairro_att.setInt(2, bairro.getBairro_id());
            bairro_att.executeUpdate();
        }
    }

    // update bairro por nome
    public void atualizarPorNome(String NomeAntiga, String NomeNova) throws SQLException {
        String sql = "UPDATE tb_bairro SET bairro_descricao = ? WHERE bairro_descricao = ?";
        try (PreparedStatement bairro_att_nome = con_bairro.prepareStatement(sql)) {
            bairro_att_nome.setString(1, NomeNova);
            bairro_att_nome.setString(2, NomeAntiga);
            bairro_att_nome.executeUpdate();
        }
    }

    // busca bairro pod id
    public BairroVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_bairro WHERE bairro_id = ?";
        try (PreparedStatement stmt = con_bairro.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BairroVO bairro = new BairroVO();
                    bairro.setBairro_id(rs.getInt("bairro_id"));
                    bairro.setBairro_descricao(rs.getString("bairro_descricao"));
                    return bairro;
                }
            }
        }
        return null;
    }

    // busca bairro por nome
    public BairroVO buscarPorDescricao(String descricao) throws SQLException {
        String sql = "SELECT * FROM tb_bairro WHERE bairro_descricao = ?";
        try (PreparedStatement stmt = con_bairro.prepareStatement(sql)) {
            stmt.setString(1, descricao);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BairroVO bairro = new BairroVO();
                    bairro.setBairro_id(rs.getInt("bairro_id"));
                    bairro.setBairro_descricao(rs.getString("bairro_descricao"));
                    return bairro;
                }
            }
        }
        return null;
    }

    public List<BairroVO> buscarTodosBairros() throws SQLException {
        List<BairroVO> bairros = new ArrayList<>();
        String sql = "SELECT bairro_id, bairro_descricao FROM tb_bairro";

        try (PreparedStatement stmt = con_bairro.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bairros.add(new BairroVO(
                        rs.getInt("bairro_id"),
                        rs.getString("bairro_descricao")));
            }
        }
        return bairros;
    }
}
