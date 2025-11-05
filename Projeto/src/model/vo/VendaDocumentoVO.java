package model.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendaDocumentoVO {
    private int vendaId;
    private Date data;
    private PessoaVO cliente;
    private FuncionarioVO funcionario;
    private StatusVendaVO status;
    private TipoPagamentoVO tipoPagamento;
    private CondicaoPagamentoVO condicaoPagamento; // opcional, não persistida

    private List<VendaDocumentoItemVO> itens = new ArrayList<>();
    private double subtotal;
    private double descontoTotal;
    private double total;

    public int getVendaId() { return vendaId; }
    public void setVendaId(int vendaId) { this.vendaId = vendaId; }
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
    public PessoaVO getCliente() { return cliente; }
    public void setCliente(PessoaVO cliente) { this.cliente = cliente; }
    public FuncionarioVO getFuncionario() { return funcionario; }
    public void setFuncionario(FuncionarioVO funcionario) { this.funcionario = funcionario; }
    public StatusVendaVO getStatus() { return status; }
    public void setStatus(StatusVendaVO status) { this.status = status; }
    public TipoPagamentoVO getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(TipoPagamentoVO tipoPagamento) { this.tipoPagamento = tipoPagamento; }
    public CondicaoPagamentoVO getCondicaoPagamento() { return condicaoPagamento; }
    public void setCondicaoPagamento(CondicaoPagamentoVO condicaoPagamento) { this.condicaoPagamento = condicaoPagamento; }
    public List<VendaDocumentoItemVO> getItens() { return itens; }
    public void setItens(List<VendaDocumentoItemVO> itens) { this.itens = itens; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getDescontoTotal() { return descontoTotal; }
    public void setDescontoTotal(double descontoTotal) { this.descontoTotal = descontoTotal; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}

