package model.dao;

import model.vo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EndPostalDAO {
    private Connection con_endp;
    private LogradouroDAO logradouroDAO;
    private BairroDAO bairroDAO;
    private CidadeDAO cidadeDAO;
    private EstadoDAO estadoDAO;


    public EndPostalDAO(Connection con_endp) throws SQLException {
        this.con_endp = con_endp;
        this.logradouroDAO = new LogradouroDAO(con_endp);
        this.bairroDAO = new BairroDAO(con_endp);
        this.cidadeDAO = new CidadeDAO(con_endp);
        this.estadoDAO = new EstadoDAO(con_endp);
    }

    // adicionar novo endereço postal
    public int adicionarNovo(EndPostalVO endPostal) throws SQLException {
        if (endPostal.getEndP_logradouro() == null
                || endPostal.getEndP_bairro() == null
                || endPostal.getEndP_cidade() == null
                || endPostal.getEndP_estado() == null) {
            throw new SQLException("EndPostal incompleto: verifique logradouro, bairro, cidade e estado.");
        }

        String sql = "INSERT INTO tb_endpostal (endP_logradouro_id, endP_nomeRua, endP_cep, endP_cid_id, endP_est_sigla, endP_bairro_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con_endp.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, endPostal.getEndP_logradouro().getLogradouro_id());
            stmt.setString(2, endPostal.getEndP_nomeRua());
            stmt.setString(3, endPostal.getEndP_cep());
            stmt.setInt(4, endPostal.getEndP_cidade().getCid_id());
            stmt.setString(5, endPostal.getEndP_estado().getEst_sigla());
            stmt.setInt(6, endPostal.getEndP_bairro().getBairro_id());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    endPostal.setEndP_id(id); // ✅ seta no VO
                    return id;
                }
            }
        }
        return -1;
    }

    // update endereço postal por id
    public void atualizar(EndPostalVO endPostal) throws SQLException {
        String sql = "UPDATE tb_endPostal SET endP_logradouro_id = ?, endP_nomeRua = ?, endP_cep = ?, endP_cid_id = ?, endP_est_sigla = ?, endP_bairro_id = ? WHERE endP_id = ?";
        try (PreparedStatement endPostal_att = con_endp.prepareStatement(sql)) {
            endPostal_att.setInt(1, endPostal.getEndP_logradouro().getLogradouro_id());
            endPostal_att.setString(2, endPostal.getEndP_nomeRua());
            endPostal_att.setString(3, endPostal.getEndP_cep());
            endPostal_att.setInt(4, endPostal.getEndP_cidade().getCid_id());
            endPostal_att.setString(5, endPostal.getEndP_estado().getEst_sigla());
            endPostal_att.setInt(6, endPostal.getEndP_bairro().getBairro_id());
            endPostal_att.setInt(7, endPostal.getEndP_id());
            endPostal_att.executeUpdate();
        }
    }

    // busca endereço postal por id
    public EndPostalVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_endPostal WHERE endP_id = ?";
        EndPostalVO endPostal = null;

        try (PreparedStatement endPostal_bsc = con_endp.prepareStatement(sql)) {
            endPostal_bsc.setInt(1, id);
            try (ResultSet rs = endPostal_bsc.executeQuery()) {
                if (rs.next()) {
                    endPostal = new EndPostalVO();
                    endPostal.setEndP_id(rs.getInt("endP_id"));
                    endPostal.setEndP_nomeRua(rs.getString("endP_nomeRua"));
                    endPostal.setEndP_cep(rs.getString("endP_cep"));
                    endPostal.setEndP_logradouro(logradouroDAO.buscarPorId(rs.getInt("endP_logradouro_id")));
                    endPostal.setEndP_bairro(bairroDAO.buscarPorId(rs.getInt("endP_bairro_id")));
                    endPostal.setEndP_cidade(cidadeDAO.buscarPorId(rs.getInt("endP_cid_id")));
                    endPostal.setEndP_estado(estadoDAO.buscarPorSigla(rs.getString("endP_est_sigla")));
                }
            }
        }
        return endPostal;
    }

    // busca endereço postal por cep
    public EndPostalVO buscarPorCep(String cep) throws SQLException {
        String sql = "SELECT * FROM tb_endPostal WHERE endP_cep = ?";
        EndPostalVO endPostal = null;
        try (PreparedStatement endPostal_bsc_cep = con_endp.prepareStatement(sql)) {
            endPostal_bsc_cep.setString(1, cep);
            try (ResultSet rs = endPostal_bsc_cep.executeQuery()) {
                if (rs.next()) {
                    endPostal = new EndPostalVO();
                    endPostal.setEndP_id(rs.getInt("endP_id"));
                    endPostal.setEndP_nomeRua(rs.getString("endP_nomeRua"));
                    endPostal.setEndP_cep(rs.getString("endP_cep"));
                    endPostal.setEndP_logradouro(logradouroDAO.buscarPorId(rs.getInt("endP_logradouro_id")));
                    endPostal.setEndP_bairro(bairroDAO.buscarPorId(rs.getInt("endP_bairro_id")));
                    endPostal.setEndP_cidade(cidadeDAO.buscarPorId(rs.getInt("endP_cid_id")));
                    endPostal.setEndP_estado(estadoDAO.buscarPorSigla(rs.getString("endP_est_sigla")));
                }
            }
        }
        return endPostal;
    }

    // update endereço postal por nome da rua
    public void atualizarPorNomeRua(String nomeRuaAntigo, EndPostalVO endPostalNovo) throws SQLException {
        String sql = "UPDATE tb_endPostal SET endP_logradouro_id = ?, endP_nomeRua = ?, endP_cep = ?, endP_cid_id = ?, endP_est_sigla = ?, endP_bairro_id = ? WHERE endP_nomeRua = ?";
        try (PreparedStatement endPostal_att_nome = con_endp.prepareStatement(sql)) {
            endPostal_att_nome.setInt(1, endPostalNovo.getEndP_logradouro().getLogradouro_id());
            endPostal_att_nome.setString(2, endPostalNovo.getEndP_nomeRua());
            endPostal_att_nome.setString(3, endPostalNovo.getEndP_cep());
            endPostal_att_nome.setInt(4, endPostalNovo.getEndP_cidade().getCid_id());
            endPostal_att_nome.setString(5, endPostalNovo.getEndP_estado().getEst_sigla());
            endPostal_att_nome.setInt(6, endPostalNovo.getEndP_bairro().getBairro_id());
            endPostal_att_nome.setString(7, nomeRuaAntigo);
            endPostal_att_nome.executeUpdate();
        }
    }
}
