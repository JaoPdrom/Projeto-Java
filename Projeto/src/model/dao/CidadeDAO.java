/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.vo.CidadeVO;
import model.vo.EstadoVO;

public class CidadeDAO {
    private Connection con;

    public CidadeDAO(Connection con) {
        this.con = con;
    }

    public CidadeDAO() throws SQLException {}


    // adicionar cidade
    public int adicionarCidade(CidadeVO cidade) throws SQLException {
        String sql = "INSERT INTO tb_cidade (cid_descricao) VALUES (?)";
        try (PreparedStatement cid_add = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            cid_add.setString(1, cidade.getCid_descricao());
            cid_add.executeUpdate();

            // obtem o id gerado
            try (ResultSet rs = cid_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // UPDATE CIDADE POR ID
    public void atualizarCidade(CidadeVO cidade) throws SQLException {
        // A instrução SQL de UPDATE precisa de uma cláusula WHERE
        String sql = "UPDATE tb_cidade SET cid_descricao = ? WHERE cid_id = ?";
        try (PreparedStatement cid_att_id = con.prepareStatement(sql)) {
            cid_att_id.setString(1, cidade.getCid_descricao());
            cid_att_id.setInt(2, cidade.getCid_id()); // Adicionado o ID da cidade como parâmetro
            cid_att_id.executeUpdate();
        }
    }

    // UPDATE CIDADE POR NOME
    public void atualizarCidadePorNome(String nomeAntigo, String novoNome) throws SQLException {
        String sql = "UPDATE tb_cidade SET cid_descricao = ? WHERE cid_descricao = ?";
        try (PreparedStatement cid_att_nome = con.prepareStatement(sql)) {
            cid_att_nome.setString(1, novoNome);
            cid_att_nome.setString(2, nomeAntigo);
            cid_att_nome.executeUpdate();
        }
    }

    // buscar cidade por id
    public CidadeVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_cidade WHERE cid_id = ?";
        CidadeVO cidade = null;
        try (PreparedStatement cid_bsc_id = con.prepareStatement(sql)) {
            cid_bsc_id.setInt(1, id);
            try (ResultSet rs = cid_bsc_id.executeQuery()) {
                if (rs.next()) {
                    cidade = new CidadeVO();
                    cidade.setCid_id(rs.getInt("cid_id"));
                    cidade.setCid_descricao(rs.getString("cid_descricao"));
                }
            }
        }
        return cidade;
    }

    // buscar cidade por nome
    public CidadeVO buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_cidade WHERE cid_descricao = ?";
        CidadeVO cidade = null;
        try (PreparedStatement cid_bsc_nome = con.prepareStatement(sql)) {
            cid_bsc_nome.setString(1, nome);
            try (ResultSet rs = cid_bsc_nome.executeQuery()) {
                if (rs.next()) {
                    cidade = new CidadeVO();
                    cidade.setCid_id(rs.getInt("cid_id"));
                    cidade.setCid_descricao(rs.getString("cid_descricao"));
                }
            }
        }
        return cidade;
    }

    public List<CidadeVO> buscarTodasCidades() throws SQLException {
        List<CidadeVO> cidades = new ArrayList<>();
        String sql = "SELECT cid_id, cid_descricao FROM tb_cidade";
        try (PreparedStatement cid_bsc_id = con.prepareStatement(sql);
             ResultSet rs = cid_bsc_id.executeQuery()) {

            while (rs.next()) {
                cidades.add(new CidadeVO(
                        rs.getInt("cid_id"),              // ← agora é int
                        rs.getString("cid_descricao")));  // ← String
            }
        }
        return cidades;
    }

    // listar cidades por estado (usando tabela de ligação tb_cidEst)
    public List<CidadeVO> listarPorEstado(String estSigla) throws SQLException {
        List<CidadeVO> cidades = new ArrayList<>();
        String sql = "SELECT c.cid_id, c.cid_descricao " +
                "FROM tb_cidade c " +
                "JOIN tb_cidEst ce ON ce.cidEstPai_cid_id = c.cid_id " +
                "WHERE ce.cidEstPai_est_sigla = ? " +
                "ORDER BY c.cid_descricao";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estSigla);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cidades.add(new CidadeVO(
                            rs.getInt("cid_id"),
                            rs.getString("cid_descricao")
                    ));
                }
            }
        }
        return cidades;
    }


}
