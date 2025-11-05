package model.vo;

public class StatusVendaVO {
    private int statusVenda_id;
    private String statusVenda_descricao;

    public StatusVendaVO() {}

    public StatusVendaVO(int statusVenda_id, String statusVenda_descricao) {
        this.statusVenda_id = statusVenda_id;
        this.statusVenda_descricao = statusVenda_descricao;
    }

    public int getStatusVenda_id() {
        return statusVenda_id;
    }

    public void setStatusVenda_id(int statusVenda_id) {
        this.statusVenda_id = statusVenda_id;
    }

    public String getStatusVenda_descricao() {
        return statusVenda_descricao;
    }

    public void setStatusVenda_descricao(String statusVenda_descricao) {
        this.statusVenda_descricao = statusVenda_descricao;
    }
}
