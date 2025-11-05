package model.vo;

import java.util.Date;

public class EstoqueVO {
    private int est_id;
    private Date est_dtCompra;
    private ProdutoVO est_produto_id;
    private double est_custo;
    private double qtdTotal;
    private String est_lote;
    private Date est_dtValidade;
    private FornecedorVO est_forn_cnpj;

    public EstoqueVO() {}

    public EstoqueVO(int est_id, Date est_dtCompra, ProdutoVO est_produto_id, double est_custo, double qtdTotal, String est_lote, Date est_dtValidade, FornecedorVO est_forn_cnpj) {
        this.est_id = est_id;
        this.est_dtCompra = est_dtCompra;
        this.est_produto_id = est_produto_id;
        this.est_custo = est_custo;
        this.qtdTotal = qtdTotal;
        this.est_lote = est_lote;
        this.est_dtValidade = est_dtValidade;
        this.est_forn_cnpj = est_forn_cnpj;
    }

    public int getEst_id() {
        return est_id;
    }

    public void setEst_id(int est_id) {
        this.est_id = est_id;
    }

    public Date getEst_dtCompra() {
        return est_dtCompra;
    }

    public void setEst_dtCompra(Date est_dtCompra) {
        this.est_dtCompra = est_dtCompra;
    }

    public ProdutoVO getEst_produto_id() {
        return est_produto_id;
    }

    public void setEst_produto_id(ProdutoVO est_produto_id) {
        this.est_produto_id = est_produto_id;
    }

    public double getEst_custo() {
        return est_custo;
    }

    public void setEst_custo(double est_custo) {
        this.est_custo = est_custo;
    }

    public double getQtdTotal() {
        return qtdTotal;
    }

    public void setQtdTotal(double qtdTotal) {
        this.qtdTotal = qtdTotal;
    }

    public String getEst_lote() {
        return est_lote;
    }

    public void setEst_lote(String est_lote) {
        this.est_lote = est_lote;
    }

    public Date getEst_dtValidade() {
        return est_dtValidade;
    }

    public void setEst_dtValidade(Date est_dtValidade) {
        this.est_dtValidade = est_dtValidade;
    }

    public FornecedorVO getEst_forn_cnpj() {
        return est_forn_cnpj;
    }

    public void setEst_forn_cnpj(FornecedorVO est_forn_cnpj) {
        this.est_forn_cnpj = est_forn_cnpj;
    }
}
