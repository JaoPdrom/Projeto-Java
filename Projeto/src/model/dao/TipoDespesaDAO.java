package model.dao;

import model.vo.TipoDespesaVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoDespesaDAO {
    private Connection con;

    public TipoDespesaDAO(Connection con) {
        this.con = con;
    }

    public int adicionarNovo(TipoDespesaVO tipo) throws SQLException {
        String sql = "INSERT INTO tb_tipoDespesa (tipoDespesa_nome) VALUES (?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tipo.getTipoDespesa_nome());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void atualizarPorId(TipoDespesaVO tipo) throws SQLException {
        String sql = "UPDATE tb_tipoDespesa SET tipoDespesa_nome = ? WHERE tipoDespesa_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo.getTipoDespesa_nome());
            ps.setInt(2, tipo.getTipoDespesa_id());
            ps.executeUpdate();
        }
    }

    public TipoDespesaVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_tipoDespesa WHERE tipoDespesa_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TipoDespesaVO t = new TipoDespesaVO();
                    t.setTipoDespesa_id(rs.getInt("tipoDespesa_id"));
                    t.setTipoDespesa_nome(rs.getString("tipoDespesa_nome"));
                    return t;
                }
            }
        }
        return null;
    }

    public TipoDespesaVO buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_tipoDespesa WHERE tipoDespesa_nome = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TipoDespesaVO t = new TipoDespesaVO();
                    t.setTipoDespesa_id(rs.getInt("tipoDespesa_id"));
                    t.setTipoDespesa_nome(rs.getString("tipoDespesa_nome"));
                    return t;
                }
            }
        }
        return null;
    }

    public List<TipoDespesaVO> listarTodosTiposDespesa() throws SQLException {
        String sql = "SELECT * FROM tb_tipoDespesa ORDER BY tipoDespesa_nome";
        List<TipoDespesaVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TipoDespesaVO t = new TipoDespesaVO();
                    t.setTipoDespesa_id(rs.getInt("tipoDespesa_id"));
                    t.setTipoDespesa_nome(rs.getString("tipoDespesa_nome"));
                    lista.add(t);
                }
            }
        }
        return lista;
    }
}

