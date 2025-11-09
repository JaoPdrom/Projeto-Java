package model.dao;

import model.vo.SexoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SexoDAO {
    private Connection con_sex;

    public SexoDAO(Connection con_sex) {
        this.con_sex = con_sex;
    }

    public SexoDAO() throws SQLException {}

    // registrar novo sexo
    public int adicionarNovo(SexoVO sexo) throws SQLException {
        String sql = "INSERT INTO tb_sexo (sex_descricao) VALUES (?)";
        try (PreparedStatement sex_add = con_sex.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sex_add.setString(1, sexo.getSex_descricao());
            sex_add.executeUpdate();
            try (ResultSet rs = sex_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update sexo por id
    public void atualizarPorId(SexoVO sexo) throws SQLException {
        String sql = "UPDATE tb_sexo SET sex_descricao = ? WHERE sex_id = ?";
        try (PreparedStatement sex_att_id = con_sex.prepareStatement(sql)) {
            sex_att_id.setString(1, sexo.getSex_descricao());
            sex_att_id.setInt(2, sexo.getSex_id());
            sex_att_id.executeUpdate();
        }
    }

    // update sexo por nome
    public void atualizarPorDescricao(String descricaoAntiga, SexoVO sexoNovo) throws SQLException {
        String sql = "UPDATE tb_sexo SET sex_descricao = ? WHERE sex_descricao = ?";
        try (PreparedStatement sex_att_nome = con_sex.prepareStatement(sql)) {
            sex_att_nome.setString(1, sexoNovo.getSex_descricao());
            sex_att_nome.setString(2, descricaoAntiga);
            sex_att_nome.executeUpdate();
        }
    }

    // busca sexo por id
    public SexoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_sexo WHERE sex_id = ?";
        SexoVO sexo = null;
        try (PreparedStatement sex_bsc_id = con_sex.prepareStatement(sql)) {
            sex_bsc_id.setInt(1, id);
            try (ResultSet rs = sex_bsc_id.executeQuery()) {
                if (rs.next()) {
                    sexo = new SexoVO();
                    sexo.setSex_id(rs.getInt("sex_id"));
                    sexo.setSex_descricao(rs.getString("sex_descricao"));
                }
            }
        }
        return sexo;
    }

    // busca sexo por descricao
    public SexoVO buscarPorDescricao(String descricao) throws SQLException {
        String sql = "SELECT * FROM tb_sexo WHERE sex_descricao = ?";
        SexoVO sexo = null;
        try (PreparedStatement sex_bsc_desc = con_sex.prepareStatement(sql)) {
            sex_bsc_desc.setString(1, descricao);
            try (ResultSet rs = sex_bsc_desc.executeQuery()) {
                if (rs.next()) {
                    sexo = new SexoVO();
                    sexo.setSex_id(rs.getInt("sex_id"));
                    sexo.setSex_descricao(rs.getString("sex_descricao"));
                }
            }
        }
        return sexo;
    }

    public List<SexoVO> buscarTodosSexo() throws SQLException {
        List<SexoVO> sexos = new ArrayList<>();
        String sql = "SELECT sex_id, sex_descricao FROM tb_sexo";

        try (Connection con = ConexaoDAO.getConexao();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SexoVO sexo = new SexoVO();
                sexo.setSex_id(rs.getInt("sex_id"));
                sexo.setSex_descricao(rs.getString("sex_descricao"));
                sexos.add(sexo);
            }
        }

        return sexos;
    }

}
