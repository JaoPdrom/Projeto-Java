package model.dao;

import model.vo.TipoPessoaVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoPessoaDAO {
    public TipoPessoaVO buscarPorId(int id) throws java.sql.SQLException {
        String sql = "SELECT tipo_pessoa_id, codigo, descricao FROM tb_tipoPessoa WHERE tipo_pessoa_id = ?";
        try (Connection con = ConexaoDAO.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoPessoaVO(
                            rs.getInt("tipo_pessoa_id"),
                            rs.getString("codigo"),
                            rs.getString("descricao")
                    );
                }
            }
        }
        return null;
    }
    public List<TipoPessoaVO> listarTodos() throws java.sql.SQLException {
        String sql = "SELECT tipo_pessoa_id, codigo, descricao FROM tb_tipoPessoa ORDER BY tipo_pessoa_id";
        try (Connection con = ConexaoDAO.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<TipoPessoaVO> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new TipoPessoaVO(
                        rs.getInt("tipo_pessoa_id"),
                        rs.getString("codigo"),
                        rs.getString("descricao")
                ));
            }
            return lista;
        }
    }
}
