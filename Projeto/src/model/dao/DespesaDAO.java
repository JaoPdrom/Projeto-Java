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
        String sql = "INSERT INTO tb_despesa (despesa_descricao, despesa_dtRealizacao, despesa_valor, despesa_tipo_despesa_id) VALUES (?, ?, ?, ?)";

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
        String sql = "UPDATE tb_despesa SET despesa_descricao = ?, despesa_dtRealizacao = ?, despesa_valor = ?, despesa_tipo_despesa_id = ? WHERE despesa_id = ?";
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
        String sql = "SELECT * FROM tb_despesa ORDER BY despesa_dtRealizacao DESC, despesa_id DESC";
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

    /**
     * Busca despesas com filtros opcionais de descrição (LIKE), tipo e intervalo de datas.
     * Campos nulos são ignorados na construção do WHERE.
     */
    public List<DespesaVO> buscarComFiltros(
            String descricaoLike,
            Integer tipoDespesaId,
            java.time.LocalDate dtInicial,
            java.time.LocalDate dtFinal) throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT * FROM tb_despesa WHERE 1=1");
        List<Object> parametros = new ArrayList<>();

        if (descricaoLike != null && !descricaoLike.isBlank()) {
            sql.append(" AND despesa_descricao LIKE ?");
            parametros.add("%" + descricaoLike.trim() + "%");
        }

        if (tipoDespesaId != null && tipoDespesaId > 0) {
            sql.append(" AND despesa_tipo_despesa_id = ?");
            parametros.add(tipoDespesaId);
        }

        if (dtInicial != null && dtFinal != null) {
            sql.append(" AND despesa_dtRealizacao BETWEEN ? AND ?");
            parametros.add(java.sql.Date.valueOf(dtInicial));
            parametros.add(java.sql.Date.valueOf(dtFinal));
        } else if (dtInicial != null) {
            sql.append(" AND despesa_dtRealizacao = ?");
            parametros.add(java.sql.Date.valueOf(dtInicial));
        } else if (dtFinal != null) {
            sql.append(" AND despesa_dtRealizacao = ?");
            parametros.add(java.sql.Date.valueOf(dtFinal));
        }

        sql.append(" ORDER BY despesa_dtRealizacao DESC, despesa_id DESC");

        List<DespesaVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_despesa.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                Object p = parametros.get(i);
                if (p instanceof String) {
                    ps.setString(i + 1, (String) p);
                } else if (p instanceof Integer) {
                    ps.setInt(i + 1, (Integer) p);
                } else if (p instanceof java.sql.Date) {
                    ps.setDate(i + 1, (java.sql.Date) p);
                } else {
                    ps.setObject(i + 1, p);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapDespesa(rs));
                }
            }
        }
        return lista;
    }

    // Exclui despesa por id
    public void excluirPorId(int id) throws SQLException {
        String sql = "DELETE FROM tb_despesa WHERE despesa_id = ?";
        try (PreparedStatement ps = con_despesa.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private DespesaVO mapDespesa(ResultSet rs) throws SQLException {
        DespesaVO d = new DespesaVO();
        d.setDespesa_id(rs.getInt("despesa_id"));
        d.setDespesa_descricao(rs.getString("despesa_descricao"));

        Date dataSql = rs.getDate("despesa_dtRealizacao");
        if (dataSql != null) {
            d.setDespesa_dtRealizacao(dataSql.toLocalDate());
        }

        d.setDespesa_valor_pago(rs.getDouble("despesa_valor"));

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
