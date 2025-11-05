package model.vo;

public class VendaDocumentoItemVO {
    private ProdutoVO produto;
    private double quantidade;
    private double precoUnitario;
    private double subtotal;

    public VendaDocumentoItemVO() {}

    public VendaDocumentoItemVO(ProdutoVO produto, double quantidade, double precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = quantidade * precoUnitario;
    }

    public ProdutoVO getProduto() { return produto; }
    public void setProduto(ProdutoVO produto) { this.produto = produto; }
    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}

