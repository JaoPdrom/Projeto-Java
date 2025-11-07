package model.vo;

public class SexoVO {
    private int sex_id;
    private String sex_descricao;

    public SexoVO() {}

    public SexoVO(int sex_id, String sex_descricao) {
        this.sex_id = sex_id;
        this.sex_descricao = sex_descricao;
    }

    public int getSex_id() {
        return sex_id;
    }

    public void setSex_id(int sex_id) {
        this.sex_id = sex_id;
    }

    public String getSex_descricao() {
        return sex_descricao;
    }

    public void setSex_descricao(String sex_descricao) {
        this.sex_descricao = sex_descricao;
    }

    @Override
    public String toString() {
        return sex_descricao;
    }
}
