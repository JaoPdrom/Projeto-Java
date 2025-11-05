package model.vo;

public class ProventoVO {
    private int provento_id;
    private String provento_descricao;

    public ProventoVO() {}

    public ProventoVO(int provento_id, String provento_descricao) {
        this.provento_id = provento_id;
        this.provento_descricao = provento_descricao;
    }

    public int getProvento_id() {
        return provento_id;
    }

    public void setProvento_id(int provento_id) {
        this.provento_id = provento_id;
    }

    public String getProvento_descricao() {
        return provento_descricao;
    }

    public void setProvento_descricao(String provento_descricao) {
        this.provento_descricao = provento_descricao;
    }
}
