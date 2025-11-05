package model.dao;

import model.vo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DebitoDAO {

    private final Connection con_debito;

    public DebitoDAO(Connection con_debito) {
        this.con_debito = con_debito;
    }

    // inserir débito
    public int adicionarNovo(DebitoVO debito) throws SQLException {
        String sql = "INSERT INTO tb_debito (deb_venda_id, deb_juros, deb_statusDebito) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con_debito.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, debito.getDeb_venda_id().getVenda_id());
            if (debito.getDeb_juros() == null) {
                ps.setNull(2, java.sql.Types.DOUBLE);
            } else {
                ps.setDouble(2, debito.getDeb_juros());
            }
            ps.setInt(3, debito.getDeb_status().getStatusDeb_id());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    // atualizar status
    public void atualizarStatus(int debitoId, int statusId) throws SQLException {
        String sql = "UPDATE tb_debito SET deb_statusDebito = ? WHERE deb_id = ?";
        try (PreparedStatement ps = con_debito.prepareStatement(sql)) {
            ps.setInt(1, statusId);
            ps.setInt(2, debitoId);
            ps.executeUpdate();
        }
    }

    // atualizar juros
    public void atualizarJuros(int debitoId, Double juros) throws SQLException {
        String sql = "UPDATE tb_debito SET deb_juros = ? WHERE deb_id = ?";
        try (PreparedStatement ps = con_debito.prepareStatement(sql)) {
            if (juros == null) {
                ps.setNull(1, java.sql.Types.DOUBLE);
            } else {
                ps.setDouble(1, juros);
            }
            ps.setInt(2, debitoId);
            ps.executeUpdate();
        }
    }

    // buscar por id
    public DebitoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_debito WHERE deb_id = ?";
        try (PreparedStatement ps = con_debito.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // buscar por venda
    public DebitoVO buscarPorVenda(int vendaId) throws SQLException {
        String sql = "SELECT * FROM tb_debito WHERE deb_venda_id = ?";
        try (PreparedStatement ps = con_debito.prepareStatement(sql)) {
            ps.setInt(1, vendaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // listar por CPF do cliente (via join venda)
    public List<DebitoVO> listarPorCpfCliente(String cpf, Integer statusId) throws SQLException {
        String base = "SELECT d.* FROM tb_debito d JOIN tb_venda v ON d.deb_venda_id = v.venda_id WHERE v.venda_pes_cpf = ?";
        String sql = statusId != null ? base + " AND d.deb_statusDebito = ?" : base;
        List<DebitoVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_debito.prepareStatement(sql)) {
            ps.setString(1, cpf);
            if (statusId != null) ps.setInt(2, statusId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    // listar por status
    public List<DebitoVO> listarPorStatus(int statusId) throws SQLException {
        String sql = "SELECT * FROM tb_debito WHERE deb_statusDebito = ?";
        List<DebitoVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_debito.prepareStatement(sql)) {
            ps.setInt(1, statusId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    private DebitoVO mapRow(ResultSet rs) throws SQLException {
        DebitoVO d = new DebitoVO();
        d.setDeb_id(rs.getInt("deb_id"));
        d.setDeb_juros(rs.getObject("deb_juros") == null ? null : rs.getDouble("deb_juros"));

        // relacionados
        VendaDAO vendaDAO = new VendaDAO(con_debito);
        StatusDebitoDAO statusDAO = new StatusDebitoDAO(con_debito);
        d.setDeb_venda_id(vendaDAO.buscarPorId(rs.getInt("deb_venda_id")));
        d.setDeb_status(statusDAO.buscarPorId(rs.getInt("deb_statusDebito")));
        return d;
    }
}

