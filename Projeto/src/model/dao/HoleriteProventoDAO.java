/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.HoleriteProventoVO;
import model.vo.HoleriteVO;
import model.vo.ProventoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HoleriteProventoDAO {
    private Connection con_holeriteProvento;

    public HoleriteProventoDAO(Connection con_holeriteProvento) {
        this.con_holeriteProvento = con_holeriteProvento;
    }

    // adicionar novo provento a holerite
    public void adicionarNovo(HoleriteProventoVO holeriteProvento) throws SQLException {
        String sql = "INSERT INTO tb_holeriteProvento (holeriteProvento_holerite_id, holeriteProvento_provento_id, holeriteProvento_valor) VALUES (?, ?, ?)";
        try (PreparedStatement holeriteProvento_add = con_holeriteProvento.prepareStatement(sql)) {
            holeriteProvento_add.setInt(1, holeriteProvento.getHolerite().getHolerite_id());
            holeriteProvento_add.setInt(2, holeriteProvento.getProvento().getProvento_id());
            holeriteProvento_add.setDouble(3, holeriteProvento.getHoleriteProvento_valor());
            holeriteProvento_add.executeUpdate();
        }
    }

    // update provento em holerite
    public void atualizar(HoleriteProventoVO holeriteProvento) throws SQLException {
        String sql = "UPDATE tb_holeriteProvento SET holeriteProvento_valor = ? WHERE holeriteProvento_holerite_id = ? AND holeriteProvento_provento_id = ?";
        try (PreparedStatement holeriteProvento_att = con_holeriteProvento.prepareStatement(sql)) {
            holeriteProvento_att.setDouble(1, holeriteProvento.getHoleriteProvento_valor());
            holeriteProvento_att.setInt(2, holeriteProvento.getHolerite().getHolerite_id());
            holeriteProvento_att.setInt(3, holeriteProvento.getProvento().getProvento_id());
            holeriteProvento_att.executeUpdate();
        }
    }

    // busca provento por holerite e id do provento
    public HoleriteProventoVO buscarPorId(int holeriteId, int proventoId) throws SQLException {
        String sql = "SELECT * FROM tb_holeriteProvento WHERE holeriteProvento_holerite_id = ? AND holeriteProvento_provento_id = ?";
        HoleriteProventoVO holeriteProvento = null;
        try (PreparedStatement holeriteProvento_bsc = con_holeriteProvento.prepareStatement(sql)) {
            holeriteProvento_bsc.setInt(1, holeriteId);
            holeriteProvento_bsc.setInt(2, proventoId);
            try (ResultSet rs = holeriteProvento_bsc.executeQuery()) {
                if (rs.next()) {
                    holeriteProvento = new HoleriteProventoVO();
                    holeriteProvento.setHoleriteProvento_valor(rs.getDouble("holeriteProvento_valor"));

                    // Buscar objetos completos
                    HoleriteDAO holeriteDAO = new HoleriteDAO(con_holeriteProvento);
                    ProventoDAO proventoDAO = new ProventoDAO(con_holeriteProvento);

                    holeriteProvento.setHolerite(holeriteDAO.buscarPorId(rs.getInt("holeriteProvento_holerite_id")));
                    holeriteProvento.setProvento(proventoDAO.buscarPorId(rs.getInt("holeriteProvento_provento_id")));
                }
            }
        }
        return holeriteProvento;
    }

    // busca todos os proventos de um holerite
    public List<HoleriteProventoVO> buscarPorHolerite(int holeriteId) throws SQLException {
        String sql = "SELECT * FROM tb_holeriteProvento WHERE holeriteProvento_holerite_id = ?";
        List<HoleriteProventoVO> proventos = new ArrayList<>();
        try (PreparedStatement holeriteProvento_bsc = con_holeriteProvento.prepareStatement(sql)) {
            holeriteProvento_bsc.setInt(1, holeriteId);
            try (ResultSet rs = holeriteProvento_bsc.executeQuery()) {
                while (rs.next()) {
                    HoleriteProventoVO holeriteProvento = new HoleriteProventoVO();
                    holeriteProvento.setHoleriteProvento_valor(rs.getDouble("holeriteProvento_valor"));

                    // Buscar objetos completos
                    HoleriteDAO holeriteDAO = new HoleriteDAO(con_holeriteProvento);
                    ProventoDAO proventoDAO = new ProventoDAO(con_holeriteProvento);

                    holeriteProvento.setHolerite(holeriteDAO.buscarPorId(rs.getInt("holeriteProvento_holerite_id")));
                    holeriteProvento.setProvento(proventoDAO.buscarPorId(rs.getInt("holeriteProvento_provento_id")));

                    proventos.add(holeriteProvento);
                }
            }
        }
        return proventos;
    }
}
