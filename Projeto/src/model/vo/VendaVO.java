package model.vo;

import java.util.Date;

public class VendaVO {
    private int venda_id;
    private Date venda_data;
    private PessoaVO venda_pes_cpf;
    private FuncionarioVO venda_func_cpf;
    private StatusVendaVO venda_statusVenda;
    private TipoPagamentoVO venda_tipoPagamento;

    public VendaVO() {}

    public VendaVO(int venda_id, Date venda_data, PessoaVO venda_pes_cpf, FuncionarioVO venda_func_cpf, StatusVendaVO venda_statusVenda, TipoPagamentoVO venda_tipoPagamento) {
        this.venda_id = venda_id;
        this.venda_data = venda_data;
        this.venda_pes_cpf = venda_pes_cpf;
        this.venda_func_cpf = venda_func_cpf;
        this.venda_statusVenda = venda_statusVenda;
        this.venda_tipoPagamento = venda_tipoPagamento;
    }

    public int getVenda_id() {
        return venda_id;
    }

    public void setVenda_id(int venda_id) {
        this.venda_id = venda_id;
    }

    public Date getVenda_data() {
        return venda_data;
    }

    public void setVenda_data(Date venda_data) {
        this.venda_data = venda_data;
    }

    public PessoaVO getVenda_pes_cpf() {
        return venda_pes_cpf;
    }

    public void setVenda_pes_cpf(PessoaVO venda_pes_cpf) {
        this.venda_pes_cpf = venda_pes_cpf;
    }

    public FuncionarioVO getVenda_func_cpf() {
        return venda_func_cpf;
    }

    public void setVenda_func_cpf(FuncionarioVO venda_func_cpf) {
        this.venda_func_cpf = venda_func_cpf;
    }

    public StatusVendaVO getVenda_statusVenda() {
        return venda_statusVenda;
    }

    public void setVenda_statusVenda(StatusVendaVO venda_statusVenda) {
        this.venda_statusVenda = venda_statusVenda;
    }

    public TipoPagamentoVO getVenda_tipoPagamento() {
        return venda_tipoPagamento;
    }

    public void setVenda_tipoPagamento(TipoPagamentoVO venda_tipoPagamento) {
        this.venda_tipoPagamento = venda_tipoPagamento;
    }
}
