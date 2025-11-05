/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.EnderecoVO;
import model.vo.InfoEmpresaVO;
import model.vo.TelefoneVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoEmpresaDAO {
    private Connection con_infoEmpresa;

    public InfoEmpresaDAO(Connection con_infoEmpresa) {
        this.con_infoEmpresa = con_infoEmpresa;
    }

    // Adicionar nova empresa
    public String adicionarNovo(InfoEmpresaVO infoEmpresa) throws SQLException {
        String sql = "INSERT INTO tb_infoEmpresa (emp_cnpj, emp_nome, emp_end_id, emp_tel_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement infoEmpresa_add = con_infoEmpresa.prepareStatement(sql)) {
            infoEmpresa_add.setString(1, infoEmpresa.getEmp_cnpj());
            infoEmpresa_add.setString(2, infoEmpresa.getEmp_nome());
            infoEmpresa_add.setInt(3, infoEmpresa.getEmp_end_id().getEnd_id());
            infoEmpresa_add.setInt(4, infoEmpresa.getEmp_tel_id().getTel_id());
            infoEmpresa_add.executeUpdate();
            return infoEmpresa.getEmp_cnpj();
        }
    }

    // Update empresa por cnpj
    public void atualizarPorCnpj(InfoEmpresaVO infoEmpresa) throws SQLException {
        String sql = "UPDATE tb_infoEmpresa SET emp_nome = ?, emp_end_id = ?, emp_tel_id = ? WHERE emp_cnpj = ?";
        try (PreparedStatement infoEmpresa_att = con_infoEmpresa.prepareStatement(sql)) {
            infoEmpresa_att.setString(1, infoEmpresa.getEmp_nome());
            infoEmpresa_att.setInt(2, infoEmpresa.getEmp_end_id().getEnd_id());
            infoEmpresa_att.setInt(3, infoEmpresa.getEmp_tel_id().getTel_id());
            infoEmpresa_att.setString(4, infoEmpresa.getEmp_cnpj());
            infoEmpresa_att.executeUpdate();
        }
    }

    // Busca empresa por cnpj
    public InfoEmpresaVO buscarPorCnpj(String cnpj) throws SQLException {
        String sql = "SELECT * FROM tb_infoEmpresa WHERE emp_cnpj = ?";
        InfoEmpresaVO infoEmpresa = null;
        try (PreparedStatement infoEmpresa_bsc = con_infoEmpresa.prepareStatement(sql)) {
            infoEmpresa_bsc.setString(1, cnpj);
            try (ResultSet rs = infoEmpresa_bsc.executeQuery()) {
                if (rs.next()) {
                    infoEmpresa = new InfoEmpresaVO();
                    infoEmpresa.setEmp_cnpj(rs.getString("emp_cnpj"));
                    infoEmpresa.setEmp_nome(rs.getString("emp_nome"));
                }
            }
        }
        return infoEmpresa;
    }

    // Update empresa por nome
    public void atualizarPorNome(String nomeAntigo, String nomeNovo) throws SQLException {
        String sql = "UPDATE tb_infoEmpresa SET emp_nome = ? WHERE emp_nome = ?";
        try (PreparedStatement infoEmpresa_att_nome = con_infoEmpresa.prepareStatement(sql)) {
            infoEmpresa_att_nome.setString(1, nomeNovo);
            infoEmpresa_att_nome.setString(2, nomeAntigo);
            infoEmpresa_att_nome.executeUpdate();
        }
    }
}
