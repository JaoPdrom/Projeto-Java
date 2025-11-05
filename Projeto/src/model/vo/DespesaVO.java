package model.vo;

import java.util.Date;

public class DespesaVO {
    private int despesa_id;
    private String despesa_descricao;
    private Date despesa_dtRealizacao;
    private double despesa_valor_pago;

    public DespesaVO() {}

    public DespesaVO(int despesa_id, String despesa_descricao, Date despesa_dtRealizacao, double despesa_valor_pago) {
        this.despesa_id = despesa_id;
        this.despesa_descricao = despesa_descricao;
        this.despesa_dtRealizacao = despesa_dtRealizacao;
        this.despesa_valor_pago = despesa_valor_pago;
    }

    public int getDespesa_id() {
        return despesa_id;
    }

    public void setDespesa_id(int despesa_id) {
        this.despesa_id = despesa_id;
    }

    public String getDespesa_descricao() {
        return despesa_descricao;
    }

    public void setDespesa_descricao(String despesa_descricao) {
        this.despesa_descricao = despesa_descricao;
    }

    public Date getDespesa_dtRealizacao() {
        return despesa_dtRealizacao;
    }

    public void setDespesa_dtRealizacao(Date despesa_dtRealizacao) {
        this.despesa_dtRealizacao = despesa_dtRealizacao;
    }

    public double getDespesa_valor_pago() {
        return despesa_valor_pago;
    }

    public void setDespesa_valor_pago(double despesa_valor_pago) {
        this.despesa_valor_pago = despesa_valor_pago;
    }
}
