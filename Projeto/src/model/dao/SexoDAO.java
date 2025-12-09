package model.dao;

import model.vo.SexoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SexoDAO {
    private Connection con_sex;

    public SexoDAO(Connection con_sex) {
        this.con_sex = con_sex;
    }

    public SexoDAO() throws SQLException {}

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
