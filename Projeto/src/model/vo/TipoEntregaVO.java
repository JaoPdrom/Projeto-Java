package model.vo;

public class TipoEntregaVO {
    private int tipoEntrega_id;
    private String tipoEntrega_descricao;

    public TipoEntregaVO() {}

    public TipoEntregaVO(int tipoEntrega_id, String tipoEntrega_descricao) {
        this.tipoEntrega_id = tipoEntrega_id;
        this.tipoEntrega_descricao = tipoEntrega_descricao;
    }

    public int getTipoEntrega_id() {
        return tipoEntrega_id;
    }

    public void setTipoEntrega_id(int tipoEntrega_id) {
        this.tipoEntrega_id = tipoEntrega_id;
    }

    public String getTipoEntrega_descricao() {
        return tipoEntrega_descricao;
    }

    public void setTipoEntrega_descricao(String tipoEntrega_descricao) {
        this.tipoEntrega_descricao = tipoEntrega_descricao;
    }
}
