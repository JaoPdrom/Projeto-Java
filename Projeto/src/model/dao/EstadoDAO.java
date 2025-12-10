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
