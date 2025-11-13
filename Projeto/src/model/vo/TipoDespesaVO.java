package model.vo;

public class TipoDespesaVO {
    private int tipoDespesa_id;
    private String tipoDespesa_nome;

    public TipoDespesaVO() {}

    public TipoDespesaVO(int tipoDespesa_id, String tipoDespesa_nome) {
        this.tipoDespesa_id = tipoDespesa_id;
        this.tipoDespesa_nome = tipoDespesa_nome;
    }

    public int getTipoDespesa_id() {
        return tipoDespesa_id;
    }

    public void setTipoDespesa_id(int tipoDespesa_id) {
        this.tipoDespesa_id = tipoDespesa_id;
    }

    public String getTipoDespesa_nome() {
        return tipoDespesa_nome;
    }

    public void setTipoDespesa_nome(String tipoDespesa_nome) {
        this.tipoDespesa_nome = tipoDespesa_nome;
    }

    public String toString(){
        return tipoDespesa_nome;
    }
}

