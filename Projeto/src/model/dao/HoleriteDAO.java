/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HoleriteDAO {
    private Connection con_holerite;

    public HoleriteDAO(Connection con_holerite) {
        this.con_holerite = con_holerite;
    }

    // adicionar holerite
    public int adicionarNovo(HoleriteVO holerite) throws SQLException {
        String sql = "INSERT INTO tb_holerite (holerite_periodo, holerite_valor_liquido, holerite_fnc_id, holerite_infoEmpresa_emp_cnpj) VALUES (?, ?, ?, ?)";
        try (PreparedStatement holerite_add = con_holerite.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            holerite_add.setDate(1, new java.sql.Date(holerite.getHolerite_periodo().getTime()));
            holerite_add.setDouble(2, holerite.getHolerite_valor_liquido());
            holerite_add.setInt(3, holerite.getFuncionario().getFnc_id());
            holerite_add.setString(4, holerite.getInfoEmpresa().getEmp_cnpj());

            holerite_add.executeUpdate();

            try (ResultSet rs = holerite_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // atualizarPessoa holerite por id
    public void atualizarPorId(HoleriteVO holerite) throws SQLException {
        String sql = "UPDATE tb_holerite SET holerite_periodo = ?, holerite_valor_liquido = ?, holerite_fnc_id = ?, holerite_infoEmpresa_emp_cnpj = ? WHERE holerite_id = ?";
        try (PreparedStatement holerite_att_id = con_holerite.prepareStatement(sql)) {
            holerite_att_id.setDate(1, new Date(holerite.getHolerite_periodo().getTime()));
            holerite_att_id.setDouble(2, holerite.getHolerite_valor_liquido());
            holerite_att_id.setInt(3, holerite.getFuncionario().getFnc_id());
            holerite_att_id.setString(4, holerite.getInfoEmpresa().getEmp_cnpj());
            holerite_att_id.setInt(5, holerite.getHolerite_id());
            holerite_att_id.executeUpdate();
        }
    }

    // busca holerite por id
    public HoleriteVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_holerite WHERE holerite_id = ?";
        HoleriteVO holerite = null;
        try (PreparedStatement holerite_bsc_id = con_holerite.prepareStatement(sql)) {
            holerite_bsc_id.setInt(1, id);
            try (ResultSet rs = holerite_bsc_id.executeQuery()) {
                if (rs.next()) {
                    holerite = new HoleriteVO();
                    holerite.setHolerite_id(rs.getInt("holerite_id"));
                    holerite.setHolerite_periodo(rs.getDate("holerite_periodo"));
                    holerite.setHolerite_valor_liquido(rs.getDouble("holerite_valor_liquido"));

                    // Busca o FuncionarioVO
                    FuncionarioDAO funcionarioDAO = new FuncionarioDAO(con_holerite);
                    holerite.setFuncionario(funcionarioDAO.buscarPorId(rs.getInt("holerite_fnc_id")));

                    // Busca o InfoEmpresaVO
                    InfoEmpresaDAO infoEmpresaDAO = new InfoEmpresaDAO(con_holerite);
                    holerite.setInfoEmpresa(infoEmpresaDAO.buscarPorCnpj(rs.getString("holerite_infoEmpresa_emp_cnpj")));
                }
            }
        }
        return holerite;
    }

    // busca holerite por funcionario
    public List<HoleriteVO> buscarPorFuncionario(int idFuncionario) throws SQLException {
        String sql = "SELECT * FROM tb_holerite WHERE holerite_fnc_id = ?";
        List<HoleriteVO> holerites = new ArrayList<>();
        try (PreparedStatement holerite_bsc_fnc = con_holerite.prepareStatement(sql)) {
            holerite_bsc_fnc.setInt(1, idFuncionario);
            try (ResultSet rs = holerite_bsc_fnc.executeQuery()) {
                while (rs.next()) {
                    HoleriteVO holerite = new HoleriteVO();
                    holerite.setHolerite_id(rs.getInt("holerite_id"));
                    holerite.setHolerite_periodo(rs.getDate("holerite_periodo"));
                    holerite.setHolerite_valor_liquido(rs.getDouble("holerite_valor_liquido"));

                    // Busca o FuncionarioVO
                    FuncionarioDAO funcionarioDAO = new FuncionarioDAO(con_holerite);
                    holerite.setFuncionario(funcionarioDAO.buscarPorId(rs.getInt("holerite_fnc_id")));

                    // Busca o InfoEmpresaVO
                    InfoEmpresaDAO infoEmpresaDAO = new InfoEmpresaDAO(con_holerite);
                    holerite.setInfoEmpresa(infoEmpresaDAO.buscarPorCnpj(rs.getString("holerite_infoEmpresa_emp_cnpj")));

                    holerites.add(holerite);
                }
            }
        }
        return holerites;
    }
}
