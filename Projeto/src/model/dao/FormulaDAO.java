package model.dao;

import model.vo.FormulaVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

public class FormulaDAO {
    private Connection con_formula;

    public FormulaDAO(Connection con_formula) {
        this.con_formula = con_formula;
    }

    // adicionar nova formula
    public int adicionarNovo(FormulaVO formula) throws SQLException {
        String sql = "INSERT INTO tb_formula (formula_dtValidade) VALUES (?)";
        try (PreparedStatement formula_add = con_formula.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            formula_add.setDate(1, new Date(formula.getFormula_dtValidade().getTime()));
            formula_add.executeUpdate();
            try (ResultSet rs = formula_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update formula por id
    public void atualizarPorId(FormulaVO formula) throws SQLException {
        String sql = "UPDATE tb_formula SET formula_dtValidade = ? WHERE formula_id = ?";
        try (PreparedStatement formula_att_id = con_formula.prepareStatement(sql)) {
            formula_att_id.setDate(1, new Date(formula.getFormula_dtValidade().getTime()));
            formula_att_id.setInt(2, formula.getFormula_id());
            formula_att_id.executeUpdate();
        }
    }

    // busca formula por id
    public FormulaVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_formula WHERE formula_id = ?";
        FormulaVO formula = null;
        try (PreparedStatement formula_bsc_id = con_formula.prepareStatement(sql)) {
            formula_bsc_id.setInt(1, id);
            try (ResultSet rs = formula_bsc_id.executeQuery()) {
                if (rs.next()) {
                    formula = new FormulaVO();
                    formula.setFormula_id(rs.getInt("formula_id"));
                    formula.setFormula_dtValidade(rs.getDate("formula_dtValidade"));
                }
            }
        }
        return formula;
    }
}
