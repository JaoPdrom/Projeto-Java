package model.dao;

import model.vo.FormIngreVO;
import model.vo.FormulaVO;
import model.vo.ProdutoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FormIngreDAO {

    private Connection con_formIngre;

    public FormIngreDAO(Connection con_formIngre) {
        this.con_formIngre = con_formIngre;
    }

    // adicionar novo ingrediente de formula
    public void adicionarNovo(FormIngreVO formIngre) throws SQLException {
        String sql = "INSERT INTO tb_formIngre (formIngre_formula_id, formIgre_produto_id, formIngre_qtd) VALUES (?, ?, ?)";
        try (PreparedStatement formIngre_add = con_formIngre.prepareStatement(sql)) {
            formIngre_add.setInt(1, formIngre.getFormIngre_formula_id().getFormula_id());
            formIngre_add.setInt(2, formIngre.getFormIngre_produto_id().getProduto_id());
            formIngre_add.setDouble(3, formIngre.getFormIngre_qtd());
            formIngre_add.executeUpdate();
        }
    }

    // update ingrediente de formula
    public void atualizar(FormIngreVO formIngre) throws SQLException {
        String sql = "UPDATE tb_formIngre SET formIngre_qtd = ? WHERE formIngre_formula_id = ? AND formIgre_produto_id = ?";
        try (PreparedStatement formIngre_att = con_formIngre.prepareStatement(sql)) {
            formIngre_att.setDouble(1, formIngre.getFormIngre_qtd());
            formIngre_att.setInt(2, formIngre.getFormIngre_formula_id().getFormula_id());
            formIngre_att.setInt(3, formIngre.getFormIngre_produto_id().getProduto_id());
            formIngre_att.executeUpdate();
        }
    }

    // busca ingrediente por id
    public FormIngreVO buscarPorId(int formulaId, int produtoId) throws SQLException {
        String sql = "SELECT * FROM tb_formIngre WHERE formIngre_formula_id = ? AND formIgre_produto_id = ?";
        FormIngreVO formIngre = null;
        try (PreparedStatement formIngre_bsc = con_formIngre.prepareStatement(sql)) {
            formIngre_bsc.setInt(1, formulaId);
            formIngre_bsc.setInt(2, produtoId);
            try (ResultSet rs = formIngre_bsc.executeQuery()) {
                if (rs.next()) {
                    formIngre = new FormIngreVO();
                    formIngre.setFormIngre_formula_id(new FormulaDAO(con_formIngre).buscarPorId(rs.getInt("formIngre_formula_id")));
                    formIngre.setFormIngre_produto_id(new ProdutoDAO(con_formIngre).buscarPorId(rs.getInt("formIgre_produto_id")));
                    formIngre.setFormIngre_qtd(rs.getDouble("formIngre_qtd"));
                }
            }
        }
        return formIngre;
    }

    // busca todos os ingredientes de uma formula
    public List<FormIngreVO> buscarPorFormula(int formulaId) throws SQLException {
        String sql = "SELECT * FROM tb_formIngre WHERE formIngre_formula_id = ?";
        List<FormIngreVO> ingredientes = new ArrayList<>();
        try (PreparedStatement formIngre_bsc = con_formIngre.prepareStatement(sql)) {
            formIngre_bsc.setInt(1, formulaId);
            try (ResultSet rs = formIngre_bsc.executeQuery()) {
                while (rs.next()) {
                    FormIngreVO formIngre = new FormIngreVO();
                    formIngre.setFormIngre_formula_id(new FormulaDAO(con_formIngre).buscarPorId(rs.getInt("formIngre_formula_id")));
                    formIngre.setFormIngre_produto_id(new ProdutoDAO(con_formIngre).buscarPorId(rs.getInt("formIgre_produto_id")));
                    formIngre.setFormIngre_qtd(rs.getDouble("formIngre_qtd"));
                    ingredientes.add(formIngre);
                }
            }
        }
        return ingredientes;
    }

}
