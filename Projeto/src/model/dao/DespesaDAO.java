package model.dao;

import model.vo.DespesaVO;
import model.vo.TipoDespesaVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DespesaDAO {
    private Connection con_despesa;

    public DespesaDAO(Connection con_despesa) {
        this.con_despesa = con_despesa;
    }

    // Adicionar nova despesa
    public int adicionarNovo(DespesaVO despesa) throws SQLException {
        String sql = "INSERT INTO tb_despesa (despesa_descricao, despesa_dtRealiazacao, despesa_valor_pago, despesa_tipo_despesa_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement despesa_add = con_despesa.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            despesa_add.setString(1, despesa.getDespesa_descricao());

            if (despesa.getDespesa_dtRealizacao() != null) {
                despesa_add.setDate(2, java.sql.Date.valueOf(despesa.getDespesa_dtRealizacao()));
            } else {
                despesa_add.setNull(2, java.sql.Types.DATE);
            }

            despesa_add.setDouble(3, despesa.getDespesa_valor_pago());

            int tipoId = (despesa.getDespesa_tipo() != null)
                    ? despesa.getDespesa_tipo().getTipoDespesa_id()
                    : 0;
            despesa_add.setInt(4, tipoId);

            despesa_add.executeUpdate();

            try (ResultSet rs = despesa_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }


    // Update despesa por id
    public void atualizarPorId(DespesaVO despesa) throws SQLException {
        String sql = "UPDATE tb_despesa SET despesa_descricao = ?, despesa_dtRealiazacao = ?, despesa_valor_pago = ?, despesa_tipo_despesa_id = ? WHERE despesa_id = ?";
        try (PreparedStatement despesa_att_id = con_despesa.prepareStatement(sql)) {
            despesa_att_id.setString(1, despesa.getDespesa_descricao());
            despesa_att_id.setDate(2, java.sql.Date.valueOf(despesa.getDespesa_dtRealizacao()));
            despesa_att_id.setDouble(3, despesa.getDespesa_valor_pago());
            int tipoId = despesa.getDespesa_tipo() != null ? despesa.getDespesa_tipo().getTipoDespesa_id() : 0;
            despesa_att_id.setInt(4, tipoId);
            despesa_att_id.setInt(5, despesa.getDespesa_id());
            despesa_att_id.executeUpdate();
        }
    }

    // Update despesa por descricao
    public void atualizarPorDescricao(String descricaoAntiga, String descricaoNova) throws SQLException {
        String sql = "UPDATE tb_despesa SET despesa_descricao = ? WHERE despesa_descricao = ?";
        try (PreparedStatement despesa_att_desc = con_despesa.prepareStatement(sql)) {
            despesa_att_desc.setString(1, descricaoNova);
            despesa_att_desc.setString(2, descricaoAntiga);
            despesa_att_desc.executeUpdate();
        }
    }

    // Busca despesa por id
    public DespesaVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_despesa WHERE despesa_id = ?";
        DespesaVO despesa = null;
        try (PreparedStatement despesa_bsc_id = con_despesa.prepareStatement(sql)) {
            despesa_bsc_id.setInt(1, id);
            try (ResultSet rs = despesa_bsc_id.executeQuery()) {
                if (rs.next()) {
                    despesa = mapDespesa(rs);
                }
            }
        }
        return despesa;
    }

    // Busca despesa por descricao
    public DespesaVO buscarPorDescricao(String descricao) throws SQLException {
        String sql = "SELECT * FROM tb_despesa WHERE despesa_descricao = ?";
        DespesaVO despesa = null;
        try (PreparedStatement despesa_bsc_desc = con_despesa.prepareStatement(sql)) {
            despesa_bsc_desc.setString(1, descricao);
            try (ResultSet rs = despesa_bsc_desc.executeQuery()) {
                if (rs.next()) {
                    despesa = mapDespesa(rs);
                }
            }
        }
        return despesa;
    }

    // Busca despesas por descrição (LIKE)
    public List<DespesaVO> buscarPorDescricaoLike(String termo) throws SQLException {
        String sql = "SELECT * FROM tb_despesa WHERE despesa_descricao LIKE ?";
        List<DespesaVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_despesa.prepareStatement(sql)) {
            ps.setString(1, "%" + (termo == null ? "" : termo) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapDespesa(rs));
                }
            }
        }
        return lista;
    }

    // Lista todas as despesas
    public List<DespesaVO> buscarTodas() throws SQLException {
        String sql = "SELECT * FROM tb_despesa ORDER BY despesa_dtRealiazacao DESC, despesa_id DESC";
        List<DespesaVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_despesa.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapDespesa(rs));
                }
            }
        }
        return lista;
    }

    // Soma total de despesas no período (inclusive)
    public double somarTotalNoPeriodo(java.util.Date inicio, java.util.Date fim) throws SQLException {
        String sql = "SELECT COALESCE(SUM(despesa_valor_pago),0) AS total FROM tb_despesa WHERE despesa_dtRealiazacao BETWEEN ? AND ?";
        try (PreparedStatement ps = con_despesa.prepareStatement(sql)) {
            ps.setDate(1, new Date(inicio.getTime()));
            ps.setDate(2, new Date(fim.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("total");
            }
        }
        return 0.0;
    }

    private DespesaVO mapDespesa(ResultSet rs) throws SQLException {
        DespesaVO d = new DespesaVO();
        d.setDespesa_id(rs.getInt("despesa_id"));
        d.setDespesa_descricao(rs.getString("despesa_descricao"));

        Date dataSql = rs.getDate("despesa_dtRealiazacao");
        if (dataSql != null) {
            d.setDespesa_dtRealizacao(dataSql.toLocalDate());
        }

        d.setDespesa_valor_pago(rs.getDouble("despesa_valor_pago"));

        int tipoId = 0;
        try { 
            tipoId = rs.getInt("despesa_tipo_despesa_id"); 
        } catch (SQLException ignore) {}

        if (tipoId > 0) {
            TipoDespesaDAO tipoDAO = new TipoDespesaDAO(con_despesa);
            TipoDespesaVO tipo = tipoDAO.buscarPorId(tipoId);
            d.setDespesa_tipo(tipo);
        }

        return d;
    }

}
