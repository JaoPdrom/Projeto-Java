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
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
    private Connection con_cargo;

    public CargoDAO(Connection con_cargo) {
        this.con_cargo = con_cargo;
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
