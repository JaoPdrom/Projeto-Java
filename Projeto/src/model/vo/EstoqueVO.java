package model.vo;

import java.time.LocalDate;

public class EstoqueVO {
    private int est_id;
    private LocalDate est_dtCompra;
    private ProdutoVO est_produto_id;
    private double est_custo;
    private double qtdTotal;
    private double est_qtdMin;
    private double est_qtdMax;
    private String est_lote;
    private LocalDate est_dtValidade;

    public EstoqueVO() {}

    public EstoqueVO(int est_id, LocalDate est_dtCompra, ProdutoVO est_produto_id, double est_custo, double qtdTotal, double est_qtdMin, double est_qtdMax ,String est_lote, LocalDate est_dtValidade) {
        this.est_id = est_id;
        this.est_dtCompra = est_dtCompra;
        this.est_produto_id = est_produto_id;
        this.est_custo = est_custo;
        this.qtdTotal = qtdTotal;
        this.est_qtdMin = est_qtdMin;
        this.est_qtdMax = est_qtdMax;
        this.est_lote = est_lote;
        this.est_dtValidade = est_dtValidade;
    }

    public int getEst_id() {
        return est_id;
    }

    public void setEst_id(int est_id) {
        this.est_id = est_id;
    }

    public LocalDate getEst_dtCompra() {
        return est_dtCompra;
    }

    public void setEst_dtCompra(LocalDate est_dtCompra) {
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

    public double getEst_qtdMin() {
        return est_qtdMin;
    }

    public void setEst_qtdMin(double est_qtdMin) {
        this.est_qtdMin = est_qtdMin;
    }

    public double getEst_qtdMax() {
        return est_qtdMax;
    }

    public void setEst_qtdMax(double est_qtdMax) {
        this.est_qtdMax = est_qtdMax;
    }

    public String getEst_lote() {
        return est_lote;
    }

    public void setEst_lote(String est_lote) {
        this.est_lote = est_lote;
    }

    public LocalDate getEst_dtValidade() {
        return est_dtValidade;
    }

    public void setEst_dtValidade(LocalDate est_dtValidade) {
        this.est_dtValidade = est_dtValidade;
    }
}
