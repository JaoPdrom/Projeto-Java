package model.vo;

public class TipoProdutoVO {
    private int tipoPdt_id;
    private String tipoPdt_descricao;

    public TipoProdutoVO() {}

    public TipoProdutoVO(int tipoPdt_id, String tipoPdt_descricao) {
        this.tipoPdt_id = tipoPdt_id;
        this.tipoPdt_descricao = tipoPdt_descricao;
    }

    public int getTipoPdt_id() {
        return tipoPdt_id;
    }

    public void setTipoPdt_id(int tipoPdt_id) {
        this.tipoPdt_id = tipoPdt_id;
    }

    public String getTipoPdt_descricao() {
        return tipoPdt_descricao;
    }

    public void setTipoPdt_descricao(String tipoPdt_descricao) {
        this.tipoPdt_descricao = tipoPdt_descricao;
    }

    @Override
    public String toString() {
        return tipoPdt_descricao;
    }

}
