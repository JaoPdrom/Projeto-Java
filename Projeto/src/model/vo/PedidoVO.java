package model.vo;

public class PedidoVO {
    private int pedido_id;
    private StatusPedidoVO pedido_statusPedido;
    private TipoEntregaVO pedido_tipoEntrega;
    private VendaVO pedido_venda_id;
    private FuncionarioVO pedido_fnc_id;

    public PedidoVO() {}

    public PedidoVO(int pedido_id, StatusPedidoVO pedido_statusPedido, TipoEntregaVO pedido_tipoEntrega, VendaVO pedido_venda_id, FuncionarioVO pedido_fnc_id) {
        this.pedido_id = pedido_id;
        this.pedido_statusPedido = pedido_statusPedido;
        this.pedido_tipoEntrega = pedido_tipoEntrega;
        this.pedido_venda_id = pedido_venda_id;
        this.pedido_fnc_id = pedido_fnc_id;
    }

    public int getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(int pedido_id) {
        this.pedido_id = pedido_id;
    }

    public StatusPedidoVO getPedido_statusPedido() {
        return pedido_statusPedido;
    }

    public void setPedido_statusPedido(StatusPedidoVO pedido_statusPedido) {
        this.pedido_statusPedido = pedido_statusPedido;
    }

    public TipoEntregaVO getPedido_tipoEntrega() {
        return pedido_tipoEntrega;
    }

    public void setPedido_tipoEntrega(TipoEntregaVO pedido_tipoEntrega) {
        this.pedido_tipoEntrega = pedido_tipoEntrega;
    }

    public VendaVO getPedido_venda_id() {
        return pedido_venda_id;
    }

    public void setPedido_venda_id(VendaVO pedido_venda_id) {
        this.pedido_venda_id = pedido_venda_id;
    }

    public FuncionarioVO getPedido_fnc_id() {
        return pedido_fnc_id;
    }

    public void setPedido_fnc_id(FuncionarioVO pedido_fnc_id) {
        this.pedido_fnc_id = pedido_fnc_id;
    }
}
