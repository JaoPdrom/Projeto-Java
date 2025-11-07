package model.vo;

public class LogradouroVO {
    private int logradouro_id;
    private String logradouro_descricao;

    public LogradouroVO() {}

    public LogradouroVO(int logradouro_id, String logradouro_descricao) {
        this.logradouro_id = logradouro_id;
        this.logradouro_descricao = logradouro_descricao;
    }

    public int getLogradouro_id() {
        return logradouro_id;
    }

    public void setLogradouro_id(int logradouro_id) {
        this.logradouro_id = logradouro_id;
    }

    public String getLogradouro_descricao() {
        return logradouro_descricao;
    }

    public void setLogradouro_descricao(String logradouro_descricao) {
        this.logradouro_descricao = logradouro_descricao;
    }

    @Override
    public String toString() {
        return logradouro_descricao;
    }
}
