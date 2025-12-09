/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.TipoProdutoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoProdutoDAO {
    private Connection con_tipoPdt;

    public TipoProdutoDAO(Connection con_tipoPdt) {
        this.con_tipoPdt = con_tipoPdt;
    }

    // busca tipo de produto por id
    public TipoProdutoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_tipoPdt WHERE tipoPdt_id = ?";
        try (PreparedStatement tipoPdt_bsc_id = con_tipoPdt.prepareStatement(sql)) {
            tipoPdt_bsc_id.setInt(1, id);
            try (ResultSet rs = tipoPdt_bsc_id.executeQuery()) {
                if (rs.next()) {
                    TipoProdutoVO tipoProduto = new TipoProdutoVO();
                    tipoProduto.setTipoPdt_id(rs.getInt("tipoPdt_id"));
                    tipoProduto.setTipoPdt_descricao(rs.getString("tipoPdt_descricao"));
                    return tipoProduto;
                }
            }
        }
        return null;
    }

    public List<TipoProdutoVO> listarTiposProdutos() throws SQLException {
        List<TipoProdutoVO> lista = new ArrayList<>();

        String sql = "SELECT * FROM tb_tipoPdt";

        try (PreparedStatement ps = con_tipoPdt.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoProdutoVO vo = new TipoProdutoVO();
                vo.setTipoPdt_id(rs.getInt("tipoPdt_id"));
                vo.setTipoPdt_descricao(rs.getString("tipoPdt_descricao"));

                lista.add(vo);
            }
        }

        return lista;
    }
}
