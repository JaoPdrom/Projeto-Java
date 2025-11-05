package model.vo;

public class StatusPedidoVO {
    private int statusPedido_id;
    private String statusPedido_descricao;

    public StatusPedidoVO() {}

    public StatusPedidoVO(int statusPedido_id, String statusPedido_descricao) {
        this.statusPedido_id = statusPedido_id;
        this.statusPedido_descricao = statusPedido_descricao;
    }

    public int getStatusPedido_id() {
        return statusPedido_id;
    }

    public void setStatusPedido_id(int statusPedido_id) {
        this.statusPedido_id = statusPedido_id;
    }

    public String getStatusPedido_descricao() {
        return statusPedido_descricao;
    }

    public void setStatusPedido_descricao(String statusPedido_descricao) {
        this.statusPedido_descricao = statusPedido_descricao;
    }
}
