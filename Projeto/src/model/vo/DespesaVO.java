package model.vo;

import java.time.LocalDate;
import java.util.Date;

public class DespesaVO {
    private int despesa_id;
    private String despesa_descricao;
    private LocalDate despesa_dtRealizacao;
    private double despesa_valor_pago;
    private TipoDespesaVO despesa_tipo;

    public DespesaVO() {}

    public DespesaVO(int despesa_id, String despesa_descricao, LocalDate despesa_dtRealizacao, double despesa_valor_pago) {
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

    public LocalDate getDespesa_dtRealizacao() {
        return despesa_dtRealizacao;
    }

    public void setDespesa_dtRealizacao(LocalDate despesa_dtRealizacao) {
        this.despesa_dtRealizacao = despesa_dtRealizacao;
    }

    public double getDespesa_valor_pago() {
        return despesa_valor_pago;
    }

    public void setDespesa_valor_pago(double despesa_valor_pago) {
        this.despesa_valor_pago = despesa_valor_pago;
    }

    public TipoDespesaVO getDespesa_tipo() {
        return despesa_tipo;
    }

    public void setDespesa_tipo(TipoDespesaVO despesa_tipo) {
        this.despesa_tipo = despesa_tipo;
    }
}
