/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.FaseContratacaoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FaseContratacaoDAO {
    private final Connection con;

    public FaseContratacaoDAO(Connection con) {
        this.con = con;
    }

    public FaseContratacaoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_fase_contratacao WHERE fase_contratacao_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FaseContratacaoVO fase = new FaseContratacaoVO();
                    fase.setFase_contratacao_id(rs.getInt("fase_contratacao_id"));
                    fase.setFase_contratacao_descricao(rs.getString("fase_contratacao_descricao"));
                    fase.setFase_contratacao_ativo(rs.getBoolean("fase_contratacao_ativo"));
                    return fase;
                }
            }
        }
        return null;
    }

    public List<FaseContratacaoVO> buscarTodas() throws SQLException {
        String sql = "SELECT * FROM tb_fase_contratacao WHERE fase_contratacao_ativo = TRUE ORDER BY fase_contratacao_descricao";
        List<FaseContratacaoVO> listaFases = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                FaseContratacaoVO fase = new FaseContratacaoVO();
                fase.setFase_contratacao_id(rs.getInt("fase_contratacao_id"));
                fase.setFase_contratacao_descricao(rs.getString("fase_contratacao_descricao"));
                fase.setFase_contratacao_ativo(rs.getBoolean("fase_contratacao_ativo"));
                listaFases.add(fase);
            }
        }
        return listaFases;
    }
}

