package model.vo;

public class ProdutoVO {
    private int produto_id;
    private String produto_nome;
    private int produto_peso;
    private Boolean produto_ativo;
    private TipoProdutoVO produto_tipoPdt;
    

    public ProdutoVO() {}

    public ProdutoVO(int produto_id, String produto_nome, int produto_peso, Boolean produto_ativo, TipoProdutoVO produto_tipoPdt) {
        this.produto_id = produto_id;
        this.produto_nome = produto_nome;
        this.produto_peso = produto_peso;
        this.produto_ativo = produto_ativo;
        this.produto_tipoPdt = produto_tipoPdt;
    }

    public ProdutoVO(String produto_nome, int produto_peso, Boolean produto_ativo, TipoProdutoVO produto_tipoPdt) {
        this.produto_nome = produto_nome;
        this.produto_peso = produto_peso;
        this.produto_ativo = produto_ativo;
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

    public int getProduto_peso() {
        return produto_peso;
    }

    public void setProduto_peso(int produto_peso) {
        this.produto_peso = produto_peso;
    }

    @Override
    public String toString() {
        return produto_nome;
    }
}
