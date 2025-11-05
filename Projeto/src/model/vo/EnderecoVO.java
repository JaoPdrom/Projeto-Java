package model.vo;

public class EnderecoVO {
    private int end_id;
    private EndPostalVO end_endP_id;
    private String end_numero;
    private String end_complemento;

    public EnderecoVO() {}

    public EnderecoVO(int end_id, EndPostalVO end_endP_id, String end_numero, String end_complemento) {
        this.end_id = end_id;
        this.end_endP_id = end_endP_id;
        this.end_numero = end_numero;
        this.end_complemento = end_complemento;
    }

    public int getEnd_id() {
        return end_id;
    }

    public void setEnd_id(int end_id) {
        this.end_id = end_id;
    }

    public EndPostalVO getEnd_endP_id() {
        return end_endP_id;
    }

    public void setEnd_endP_id(EndPostalVO end_endP_id) {
        this.end_endP_id = end_endP_id;
    }

    public String getEnd_numero() {
        return end_numero;
    }

    public void setEnd_numero(String end_numero) {
        this.end_numero = end_numero;
    }

    public String getEnd_complemento() {
        return end_complemento;
    }

    public void setEnd_complemento(String end_complemento) {
        this.end_complemento = end_complemento;
    }
}
