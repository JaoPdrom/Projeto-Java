/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.CargoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
    private Connection con_cargo;

    public CargoDAO(Connection con_cargo) {
        this.con_cargo = con_cargo;
    }

    // adicionar novo
    public int adicionarNovo(CargoVO cargo) throws SQLException {
        String sql = "INSERT INTO tb_cargo (cargo_descricao) VALUES (?)";
        try (PreparedStatement cargo_add = con_cargo.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            cargo_add.setString(1, cargo.getCargo_descricao());
            cargo_add.executeUpdate();
            try (ResultSet rs = cargo_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update cargo por id
    public void atualizarPorId(CargoVO cargo) throws SQLException {
        String sql = "UPDATE tb_cargo SET cargo_descricao = ? WHERE cargo_id = ?";
        try (PreparedStatement cargo_att_id = con_cargo.prepareStatement(sql)) {
            cargo_att_id.setString(1, cargo.getCargo_descricao());
            cargo_att_id.setInt(2, cargo.getCar_id());
            cargo_att_id.executeUpdate();
        }
    }

    // update cargo por nome
    public void atualizarPorNome(String nomeAntigo, String nomeNovo) throws SQLException {
        String sql = "UPDATE tb_cargo SET cargo_descricao = ? WHERE cargo_descricao = ?";
        try (PreparedStatement cargo_att_nome = con_cargo.prepareStatement(sql)) {
            cargo_att_nome.setString(1, nomeNovo);
            cargo_att_nome.setString(2, nomeAntigo);
            cargo_att_nome.executeUpdate();
        }
    }

    // busca cargo por id
    public CargoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_cargo WHERE cargo_id = ?";
        try (PreparedStatement cargo_bsc_id = con_cargo.prepareStatement(sql)) {
            cargo_bsc_id.setInt(1, id);
            try (ResultSet rs = cargo_bsc_id.executeQuery()) {
                if (rs.next()) {
                    CargoVO cargo = new CargoVO();
                    cargo.setCar_id(rs.getInt("cargo_id"));
                    cargo.setCargo_descricao(rs.getString("cargo_descricao"));
                    return cargo;
                }
            }
        }
        return null;
    }

    // busca cargo por nome
    public CargoVO buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_cargo WHERE cargo_descricao = ?";
        try (PreparedStatement cargo_bsc_nome = con_cargo.prepareStatement(sql)) {
            cargo_bsc_nome.setString(1, nome);
            try (ResultSet rs = cargo_bsc_nome.executeQuery()) {
                if (rs.next()) {
                    CargoVO cargo = new CargoVO();
                    cargo.setCar_id(rs.getInt("cargo_id"));
                    cargo.setCargo_descricao(rs.getString("cargo_descricao"));
                    return cargo;
                }
            }
        }
        return null;
    }

    // buscar todos
    public List<CargoVO> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM tb_cargo";
        List<CargoVO> listaCargos = new ArrayList<>();
        try (PreparedStatement stmt = con_cargo.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                CargoVO cargo = new CargoVO();
                cargo.setCar_id(rs.getInt("cargo_id"));
                cargo.setCargo_descricao(rs.getString("cargo_descricao"));
                listaCargos.add(cargo);
            }
        }
        return listaCargos;
    }
}
