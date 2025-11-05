/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.PessoaVO;
import model.vo.TelefoneVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TelefoneDAO {
    private Connection con_telefone;

    public TelefoneDAO(Connection con_telefone) {
        this.con_telefone = con_telefone;
    }

    // Adiciona um novo telefone (garantindo o vínculo com a pessoa)
    public int adicionarNovo(TelefoneVO telefone, String documentoPessoa) throws SQLException {
        String sql = "INSERT INTO tb_telefone (tel_codPais, tel_ddd, tel_numero, tel_pes_documento) VALUES (?, ?, ?, ?)";
        try (PreparedStatement tel_add = con_telefone.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            tel_add.setString(1, telefone.getTel_codPais());
            tel_add.setString(2, telefone.getTel_ddd());
            tel_add.setString(3, telefone.getTel_numero());
            tel_add.setString(4, documentoPessoa); // <- garante o vínculo com pessoa
            tel_add.executeUpdate();
            try (ResultSet rs = tel_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public void atualizarTelefoneCpf(String documento, List<TelefoneVO> telefones) throws SQLException {
        String sql = "UPDATE tb_telefone SET tel_codPais = ?, tel_ddd = ?, tel_numero = ? WHERE tel_id = ? AND tel_pes_documento = ?";

        try (PreparedStatement tel_att = con_telefone.prepareStatement(sql)) {
            for (TelefoneVO tel : telefones) {
                tel_att.setString(1, tel.getTel_codPais());
                tel_att.setString(2, tel.getTel_ddd());
                tel_att.setString(3, tel.getTel_numero());
                tel_att.setInt(4, tel.getTel_id()); // importante para identificar o telefone específico
                tel_att.setString(5, documento);
                tel_att.executeUpdate();
            }
        }
    }



    // Atualiza um telefone com base no seu ID.
    public void atualizarPorId(TelefoneVO telefone) throws SQLException {
        String sql = "UPDATE tb_telefone SET tel_codPais = ?, tel_ddd = ?, tel_numero = ?, tel_pes_documento = ? WHERE tel_id = ?";
        try (PreparedStatement tel_att = con_telefone.prepareStatement(sql)) {
            tel_att.setString(1, telefone.getTel_codPais());
            tel_att.setString(2, telefone.getTel_ddd());
            tel_att.setString(3, telefone.getTel_numero());
            tel_att.setString(4, telefone.getTel_pes_cpf());
            tel_att.setInt(5, telefone.getTel_id());
            tel_att.executeUpdate();
        }
    }

    // Atualiza o número de telefone com base no número antigo.
    public void atualizarPorNumero(String numeroAntigo, String numeroNovo) throws SQLException {
        String sql = "UPDATE tb_telefone SET tel_numero = ? WHERE tel_numero = ?";
        try (PreparedStatement tel_att_numero = con_telefone.prepareStatement(sql)) {
            tel_att_numero.setString(1, numeroNovo);
            tel_att_numero.setString(2, numeroAntigo);
            tel_att_numero.executeUpdate();
        }
    }

    // Busca um telefone com base no seu ID.
    public TelefoneVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_telefone WHERE tel_id = ?";
        try (PreparedStatement stmt = con_telefone.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    TelefoneVO telefone = new TelefoneVO();
                    telefone.setTel_id(rs.getInt("tel_id"));
                    telefone.setTel_codPais(rs.getString("tel_codPais"));
                    telefone.setTel_ddd(rs.getString("tel_ddd"));
                    telefone.setTel_numero(rs.getString("tel_numero"));
                    telefone.setTel_pes_cpf(rs.getString("tel_pes_documento"));
                    return telefone;
                }
            }
        }
        return null;
    }

    // Busca um ou mais telefones com base no CPF da pessoa.
    public List<TelefoneVO> buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM tb_telefone WHERE tel_pes_documento = ?";
        List<TelefoneVO> listaTelefones = new ArrayList<>();
        try (PreparedStatement stmt = con_telefone.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TelefoneVO telefone = new TelefoneVO();
                    telefone.setTel_id(rs.getInt("tel_id"));
                    telefone.setTel_codPais(rs.getString("tel_codPais"));
                    telefone.setTel_ddd(rs.getString("tel_ddd"));
                    telefone.setTel_numero(rs.getString("tel_numero"));
                    telefone.setTel_pes_cpf(rs.getString("tel_pes_documento"));
                    listaTelefones.add(telefone);
                }
            }
        }
        return listaTelefones;
    }

    public void deletarPorCpf(String cpf) throws SQLException {

    }
}

