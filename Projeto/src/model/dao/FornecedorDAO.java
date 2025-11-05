/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.FornecedorVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {
    private Connection con_fornecedor;

    public FornecedorDAO(Connection con_fornecedor) {
        this.con_fornecedor = con_fornecedor;
    }

    // adicionar novo fornecedor
    public void adicionarNovo(FornecedorVO fornecedor) throws SQLException {
        String sql = "INSERT INTO tb_fornecedor (forn_cnpj, forn_razaSocial, forn_nomeFantasia) VALUES (?, ?, ?)";
        try (PreparedStatement fornecedor_add = con_fornecedor.prepareStatement(sql)) {
            fornecedor_add.setString(1, fornecedor.getForn_cnpj());
            fornecedor_add.setString(2, fornecedor.getForn_razaoSocial());
            fornecedor_add.setString(3, fornecedor.getForn_nomeFantasia());
            fornecedor_add.executeUpdate();
        }
    }

    // update fornecedor por cnpj
    public void atualizarPorCnpj(FornecedorVO fornecedor) throws SQLException {
        String sql = "UPDATE tb_fornecedor SET forn_razaSocial = ?, forn_nomeFantasia = ? WHERE forn_cnpj = ?";
        try (PreparedStatement fornecedor_att_cnpj = con_fornecedor.prepareStatement(sql)) {
            fornecedor_att_cnpj.setString(1, fornecedor.getForn_razaoSocial());
            fornecedor_att_cnpj.setString(2, fornecedor.getForn_nomeFantasia());
            fornecedor_att_cnpj.setString(3, fornecedor.getForn_cnpj());
            fornecedor_att_cnpj.executeUpdate();
        }
    }

    // update fornecedor por nome fantasia
    public void atualizarPorNome(String nomeAntigo, FornecedorVO fornecedorNovo) throws SQLException {
        String sql = "UPDATE tb_fornecedor SET forn_razaSocial = ?, forn_nomeFantasia = ? WHERE forn_nomeFantasia = ?";
        try (PreparedStatement fornecedor_att_nome = con_fornecedor.prepareStatement(sql)) {
            fornecedor_att_nome.setString(1, fornecedorNovo.getForn_razaoSocial());
            fornecedor_att_nome.setString(2, fornecedorNovo.getForn_nomeFantasia());
            fornecedor_att_nome.setString(3, nomeAntigo);
            fornecedor_att_nome.executeUpdate();
        }
    }

    // busca fornecedor por cnpj
    public FornecedorVO buscarPorCnpj(String cnpj) throws SQLException {
        String sql = "SELECT * FROM tb_fornecedor WHERE forn_cnpj = ?";
        FornecedorVO fornecedor = null;
        try (PreparedStatement fornecedor_bsc_cnpj = con_fornecedor.prepareStatement(sql)) {
            fornecedor_bsc_cnpj.setString(1, cnpj);
            try (ResultSet rs = fornecedor_bsc_cnpj.executeQuery()) {
                if (rs.next()) {
                    fornecedor = new FornecedorVO();
                    fornecedor.setForn_cnpj(rs.getString("forn_cnpj"));
                    fornecedor.setForn_razaoSocial(rs.getString("forn_razaoSocial"));
                    fornecedor.setForn_nomeFantasia(rs.getString("forn_nomeFantasia"));
                }
            }
        }
        return fornecedor;
    }

    // busca fornecedor por nome fantasia
    public FornecedorVO buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_fornecedor WHERE forn_nomeFantasia = ?";
        FornecedorVO fornecedor = null;
        try (PreparedStatement fornecedor_bsc_nome = con_fornecedor.prepareStatement(sql)) {
            fornecedor_bsc_nome.setString(1, nome);
            try (ResultSet rs = fornecedor_bsc_nome.executeQuery()) {
                if (rs.next()) {
                    fornecedor = new FornecedorVO();
                    fornecedor.setForn_cnpj(rs.getString("forn_cnpj"));
                    fornecedor.setForn_razaoSocial(rs.getString("forn_razaoSocial"));
                    fornecedor.setForn_nomeFantasia(rs.getString("forn_nomeFantasia"));
                }
            }
        }
        return fornecedor;
    }
}
