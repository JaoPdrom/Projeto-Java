package model.vo;

public class TipoPagamentoVO {
    private int tipoPagamento_id;
    private String tipoPagamento_descricao;

    public TipoPagamentoVO() {}

    public TipoPagamentoVO(int tipoPagamento_id, String tipoPagamento_descricao) {
        this.tipoPagamento_id = tipoPagamento_id;
        this.tipoPagamento_descricao = tipoPagamento_descricao;
    }

    public int getTipoPagamento_id() {
        return tipoPagamento_id;
    }

    public void setTipoPagamento_id(int tipoPagamento_id) {
        this.tipoPagamento_id = tipoPagamento_id;
    }

    public String getTipoPagamento_descricao() {
        return tipoPagamento_descricao;
    }

    public void setTipoPagamento_descricao(String tipoPagamento_descricao) {
        this.tipoPagamento_descricao = tipoPagamento_descricao;
    }
}
