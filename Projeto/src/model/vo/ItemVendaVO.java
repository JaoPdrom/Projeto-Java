package model.vo;

public class ItemVendaVO {
    private VendaVO itemVenda_venda_id;
    private ProdutoVO itemVenda_produto_id;
    private double itemVenda_qtd;
    private double itemVenda_preco;

    public ItemVendaVO() {}

    public ItemVendaVO(VendaVO itemVenda_venda_id, ProdutoVO itemVenda_produto_id, double itemVenda_qtd, double itemVenda_preco) {
        this.itemVenda_venda_id = itemVenda_venda_id;
        this.itemVenda_produto_id = itemVenda_produto_id;
        this.itemVenda_qtd = itemVenda_qtd;
        this.itemVenda_preco = itemVenda_preco;
    }

    public VendaVO getItemVenda_venda_id() {
        return itemVenda_venda_id;
    }

    public void setItemVenda_venda_id(VendaVO itemVenda_venda_id) {
        this.itemVenda_venda_id = itemVenda_venda_id;
    }

    public ProdutoVO getItemVenda_produto_id() {
        return itemVenda_produto_id;
    }

    public void setItemVenda_produto_id(ProdutoVO itemVenda_produto_id) {
        this.itemVenda_produto_id = itemVenda_produto_id;
    }

    public double getItemVenda_qtd() {
        return itemVenda_qtd;
    }

    public void setItemVenda_qtd(double itemVenda_qtd) {
        this.itemVenda_qtd = itemVenda_qtd;
    }

    public double getItemVenda_preco() {
        return itemVenda_preco;
    }

    public void setItemVenda_preco(double itemVenda_preco) {
        this.itemVenda_preco = itemVenda_preco;
    }
}
