package model.vo;

public class ProdutoVO {
    private int produto_id;
    private String produto_nome;
    private Double produto_qtdMax;
    private Double produto_qtdMin;
    private TipoProdutoVO produto_tipoPdt;
    private Boolean produto_ativo;

    public ProdutoVO() {}

    public ProdutoVO(int produto_id, String produto_nome, Double produto_qtdMax, Double produto_qtdMin, TipoProdutoVO produto_tipoPdt) {
        this.produto_id = produto_id;
        this.produto_nome = produto_nome;
        this.produto_qtdMax = produto_qtdMax;
        this.produto_qtdMin = produto_qtdMin;
        this.produto_tipoPdt = produto_tipoPdt;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public String getProduto_nome() {
        return produto_nome;
    }

    public void setProduto_nome(String produto_nome) {
        this.produto_nome = produto_nome;
    }

    public Double getProduto_qtdMax() {
        return produto_qtdMax;
    }

    public void setProduto_qtdMax(Double produto_qtdMax) {
        this.produto_qtdMax = produto_qtdMax;
    }

    public Double getProduto_qtdMin() {
        return produto_qtdMin;
    }

    public void setProduto_qtdMin(Double produto_qtdMin) {
        this.produto_qtdMin = produto_qtdMin;
    }

    public TipoProdutoVO getProduto_tipoPdt() {
        return produto_tipoPdt;
    }

    public void setProduto_tipoPdt(TipoProdutoVO produto_tipoPdt) {
        this.produto_tipoPdt = produto_tipoPdt;
    }

    public Boolean getProduto_ativo() {
        return produto_ativo;
    }

    public void setProduto_ativo(Boolean produto_ativo) {
        this.produto_ativo = produto_ativo;
    }
}
