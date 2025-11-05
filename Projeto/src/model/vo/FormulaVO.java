package model.vo;

import java.util.Date;

public class FormulaVO {
    private int formula_id;
    private Date formula_dtValidade;

    public FormulaVO() {}

    public FormulaVO(int formula_id, Date formula_dtValidade) {
        this.formula_id = formula_id;
        this.formula_dtValidade = formula_dtValidade;
    }

    public int getFormula_id() {
        return formula_id;
    }

    public void setFormula_id(int formula_id) {
        this.formula_id = formula_id;
    }

    public Date getFormula_dtValidade() {
        return formula_dtValidade;
    }

    public void setFormula_dtValidade(Date formula_dtValidade) {
        this.formula_dtValidade = formula_dtValidade;
    }
}
