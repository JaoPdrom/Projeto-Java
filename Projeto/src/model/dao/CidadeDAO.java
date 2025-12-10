/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.vo.CidadeVO;

public class CidadeDAO {
    private Connection con;

    public CidadeDAO(Connection con) {
        this.con = con;
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
