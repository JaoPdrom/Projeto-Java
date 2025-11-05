/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.EstadoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadoDAO {
    private Connection con_est;

    public EstadoDAO(Connection con_est) throws SQLException {
        this.con_est = con_est;
    }

    public EstadoDAO() throws SQLException {}

    // registrar novo estado

    public String addEstado(EstadoVO estado) throws SQLException {
        String sql = "INSERT INTO tb_estado (est_sigla, est_descricao) VALUES (?, ?)";
        try (PreparedStatement est_add = con_est.prepareStatement(sql)) {
            est_add.setString(1, estado.getEst_sigla());
            est_add.setString(2, estado.getEst_descricao());
            est_add.executeUpdate();
            return estado.getEst_sigla();
        }
    }

    // update estado por id

    public void atualizarEstado(EstadoVO est_att) throws SQLException{
        // A instrução SQL de UPDATE precisa de uma cláusula WHERE
        String sql = "UPDATE tb_estado SET est_descricao = ? WHERE est_sigla = ?";
        PreparedStatement est_att_id = con_est.prepareStatement(sql); // Removido Statement.RETURN_GENERATED_KEYS

        est_att_id.setString(1, est_att.getEst_descricao());
        est_att_id.setString(2, est_att.getEst_sigla()); // Adicionado o ID da cidade como parâmetro

        est_att_id.executeUpdate();
        est_att_id.close();
    }

    // update estado por nome

    public void attSexoNome(String estNomeAntigo, String estNomeNovo) throws SQLException {
        String sql = "UPDATE tb_estado SET est_descricao = ? WHERE est_descricao = ?";
        PreparedStatement est_att_nome = con_est.prepareStatement(sql);

        est_att_nome.setString(1, estNomeNovo);
        est_att_nome.setString(2, estNomeAntigo);

        est_att_nome.executeUpdate();
        est_att_nome.close();
    }

    // busca estado por sigla

    public EstadoVO buscarPorSigla(String sigla) throws SQLException {
        String sql = "SELECT * FROM tb_estado WHERE est_sigla = ?";
        EstadoVO estado = null;

        try (PreparedStatement est_bsc_nome = con_est.prepareStatement(sql)) {
            est_bsc_nome.setString(1, sigla);
            try (ResultSet rs = est_bsc_nome.executeQuery()) {
                if (rs.next()) {
                    estado = new EstadoVO();
                    estado.setEst_sigla(rs.getString("est_sigla"));
                    estado.setEst_descricao(rs.getString("est_descricao"));
                }
            }
        }
        return estado;
    }

    // busca estado por nome

    public EstadoVO buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_estado WHERE est_descricao = ?";
        EstadoVO estado = null;

        try (PreparedStatement est_bsc_sigla = con_est.prepareStatement(sql)) {
            est_bsc_sigla.setString(1, nome);
            try (ResultSet rs = est_bsc_sigla.executeQuery()) {
                if (rs.next()) {
                    estado = new EstadoVO();
                    estado.setEst_sigla(rs.getString("est_sigla"));
                    estado.setEst_descricao(rs.getString("est_descricao"));
                }
            }
        }
        return estado;
    }

    public List<EstadoVO> buscarTodosEstados() throws SQLException{
        List<EstadoVO> lista = new ArrayList<>();
        String sql = "SELECT est_sigla, est_descricao FROM tb_estado";
        try (PreparedStatement ps = con_est.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new EstadoVO(
                        rs.getString("est_sigla"),
                        rs.getString("est_descricao")));
            }

        }
        return lista;
    }


}
