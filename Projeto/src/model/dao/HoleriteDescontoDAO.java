/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.HoleriteDescontoVO;
import model.vo.HoleriteVO;
import model.vo.DescontoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoleriteDescontoDAO {
    private Connection con_holeriteDesconto;

    public HoleriteDescontoDAO(Connection con_holeriteDesconto) {
        this.con_holeriteDesconto = con_holeriteDesconto;
    }

    // adicionar novo desconto a holerite
    public void adicionarNovo(HoleriteDescontoVO holeriteDesconto) throws SQLException {
        String sql = "INSERT INTO tb_holeriteDesconto (holeriteDesconto_holerite_id, holeriteDesconto_desconto_id, holeriteDesconto_valor) VALUES (?, ?, ?)";
        try (PreparedStatement holeriteDesconto_add = con_holeriteDesconto.prepareStatement(sql)) {
            holeriteDesconto_add.setInt(1, holeriteDesconto.getHolerite().getHolerite_id());
            holeriteDesconto_add.setInt(2, holeriteDesconto.getDesconto().getDesconto_id());
            holeriteDesconto_add.setDouble(3, holeriteDesconto.getHoleriteDesconto_valor());
            holeriteDesconto_add.executeUpdate();
        }
    }

    // update desconto em holerite
    public void atualizar(HoleriteDescontoVO holeriteDesconto) throws SQLException {
        String sql = "UPDATE tb_holeriteDesconto SET holeriteDesconto_valor = ? WHERE holeriteDesconto_holerite_id = ? AND holeriteDesconto_desconto_id = ?";
        try (PreparedStatement holeriteDesconto_att = con_holeriteDesconto.prepareStatement(sql)) {
            holeriteDesconto_att.setDouble(1, holeriteDesconto.getHoleriteDesconto_valor());
            holeriteDesconto_att.setInt(2, holeriteDesconto.getHolerite().getHolerite_id());
            holeriteDesconto_att.setInt(3, holeriteDesconto.getDesconto().getDesconto_id());
            holeriteDesconto_att.executeUpdate();
        }
    }

    // busca desconto por holerite e id do desconto
    public HoleriteDescontoVO buscarPorId(int holeriteId, int descontoId) throws SQLException {
        String sql = "SELECT * FROM tb_holeriteDesconto WHERE holeriteDesconto_holerite_id = ? AND holeriteDesconto_desconto_id = ?";
        HoleriteDescontoVO holeriteDesconto = null;
        try (PreparedStatement holeriteDesconto_bsc = con_holeriteDesconto.prepareStatement(sql)) {
            holeriteDesconto_bsc.setInt(1, holeriteId);
            holeriteDesconto_bsc.setInt(2, descontoId);
            try (ResultSet rs = holeriteDesconto_bsc.executeQuery()) {
                if (rs.next()) {
                    holeriteDesconto = new HoleriteDescontoVO();
                    holeriteDesconto.setHoleriteDesconto_id(rs.getInt("holeriteDesconto_id"));
                    holeriteDesconto.setHoleriteDesconto_valor(rs.getDouble("holeriteDesconto_valor"));

                    // Buscar objetos completos
                    HoleriteDAO holeriteDAO = new HoleriteDAO(con_holeriteDesconto);
                    DescontoDAO descontoDAO = new DescontoDAO(con_holeriteDesconto);

                    holeriteDesconto.setHolerite(holeriteDAO.buscarPorId(rs.getInt("holeriteDesconto_holerite_id")));
                    holeriteDesconto.setDesconto(descontoDAO.buscarPorId(rs.getInt("holeriteDesconto_desconto_id")));
                }
            }
        }
        return holeriteDesconto;
    }

    // busca todos os descontos de um holerite
    public List<HoleriteDescontoVO> buscarPorHolerite(int holeriteId) throws SQLException {
        String sql = "SELECT * FROM tb_holeriteDesconto WHERE holeriteDesconto_holerite_id = ?";
        List<HoleriteDescontoVO> descontos = new ArrayList<>();
        try (PreparedStatement holeriteDesconto_bsc = con_holeriteDesconto.prepareStatement(sql)) {
            holeriteDesconto_bsc.setInt(1, holeriteId);
            try (ResultSet rs = holeriteDesconto_bsc.executeQuery()) {
                while (rs.next()) {
                    HoleriteDescontoVO holeriteDesconto = new HoleriteDescontoVO();
                    holeriteDesconto.setHoleriteDesconto_id(rs.getInt("holeriteDesconto_id"));
                    holeriteDesconto.setHoleriteDesconto_valor(rs.getDouble("holeriteDesconto_valor"));

                    // Buscar objetos completos
                    HoleriteDAO holeriteDAO = new HoleriteDAO(con_holeriteDesconto);
                    DescontoDAO descontoDAO = new DescontoDAO(con_holeriteDesconto);

                    holeriteDesconto.setHolerite(holeriteDAO.buscarPorId(rs.getInt("holeriteDesconto_holerite_id")));
                    holeriteDesconto.setDesconto(descontoDAO.buscarPorId(rs.getInt("holeriteDesconto_desconto_id")));

                    descontos.add(holeriteDesconto);
                }
            }
        }
        return descontos;
    }
}
