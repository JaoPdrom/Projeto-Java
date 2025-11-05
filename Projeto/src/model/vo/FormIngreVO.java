package model.vo;

public class FormIngreVO {
    private FormulaVO formIngre_formula_id;
    private ProdutoVO formIngre_produto_id;
    private double formIngre_qtd;

    public FormIngreVO() {}

    public FormIngreVO(FormulaVO formIngre_formula_id, ProdutoVO formIngre_produto_id, double formIngre_qtd) {
        this.formIngre_formula_id = formIngre_formula_id;
        this.formIngre_produto_id = formIngre_produto_id;
        this.formIngre_qtd = formIngre_qtd;
    }

    public FormulaVO getFormIngre_formula_id() {
        return formIngre_formula_id;
    }

    public void setFormIngre_formula_id(FormulaVO formIngre_formula_id) {
        this.formIngre_formula_id = formIngre_formula_id;
    }

    public ProdutoVO getFormIngre_produto_id() {
        return formIngre_produto_id;
    }

    public void setFormIngre_produto_id(ProdutoVO formIngre_produto_id) {
        this.formIngre_produto_id = formIngre_produto_id;
    }

    public double getFormIngre_qtd() {
        return formIngre_qtd;
    }

    public void setFormIngre_qtd(double formIngre_qtd) {
        this.formIngre_qtd = formIngre_qtd;
    }
}
