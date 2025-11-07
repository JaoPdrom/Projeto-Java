package model.vo;

public class BairroVO {
    private int bairro_id;
    private String bairro_descricao;

    public BairroVO() {}

    public BairroVO(int bairro_id, String bairro_descricao) {
        this.bairro_id = bairro_id;
        this.bairro_descricao = bairro_descricao;
    }

    public int getBairro_id() {
        return bairro_id;
    }

    public void setBairro_id(int bairro_id) {
        this.bairro_id = bairro_id;
    }

    public String getBairro_descricao() {
        return bairro_descricao;
    }

    public void setBairro_descricao(String bairro_descricao) {
        this.bairro_descricao = bairro_descricao;
    }

    @Override
    public String toString() {
        return bairro_descricao;
    }
}
