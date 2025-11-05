package model.vo;

import java.util.Date;

public class CondicaoPagamentoVO {
    private Integer numeroParcelas;      // null ou >= 1
    private Integer diasEntreParcelas;   // null ou >= 0
    private Date primeiroVencimento;     // opcional

    public CondicaoPagamentoVO() {}

    public CondicaoPagamentoVO(Integer numeroParcelas, Integer diasEntreParcelas, Date primeiroVencimento) {
        this.numeroParcelas = numeroParcelas;
        this.diasEntreParcelas = diasEntreParcelas;
        this.primeiroVencimento = primeiroVencimento;
    }

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public Integer getDiasEntreParcelas() {
        return diasEntreParcelas;
    }

    public void setDiasEntreParcelas(Integer diasEntreParcelas) {
        this.diasEntreParcelas = diasEntreParcelas;
    }

    public Date getPrimeiroVencimento() {
        return primeiroVencimento;
    }

    public void setPrimeiroVencimento(Date primeiroVencimento) {
        this.primeiroVencimento = primeiroVencimento;
    }
}

