package model.vo;

public class DescontoVO {
    private int desconto_id;
    private String desconto_descricao;

    public DescontoVO() {}

    public DescontoVO(int desconto_id, String desconto_descricao) {
        this.desconto_id = desconto_id;
        this.desconto_descricao = desconto_descricao;
    }

    public int getDesconto_id() {
        return desconto_id;
    }

    public void setDesconto_id(int desconto_id) {
        this.desconto_id = desconto_id;
    }

    public String getDesconto_descricao() {
        return desconto_descricao;
    }

    public void setDesconto_descricao(String desconto_descricao) {
        this.desconto_descricao = desconto_descricao;
    }
}
