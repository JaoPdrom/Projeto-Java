package model.vo;

public class FaseContratacaoVO {
    private int fase_contratacao_id;
    private String fase_contratacao_descricao;
    private boolean fase_contratacao_ativo;

    public FaseContratacaoVO() {}

    public FaseContratacaoVO(int fase_contratacao_id, String fase_contratacao_descricao, boolean fase_contratacao_ativo) {
        this.fase_contratacao_id = fase_contratacao_id;
        this.fase_contratacao_descricao = fase_contratacao_descricao;
        this.fase_contratacao_ativo = fase_contratacao_ativo;
    }

    public int getFase_contratacao_id() {
        return fase_contratacao_id;
    }

    public void setFase_contratacao_id(int fase_contratacao_id) {
        this.fase_contratacao_id = fase_contratacao_id;
    }

    public String getFase_contratacao_descricao() {
        return fase_contratacao_descricao;
    }

    public void setFase_contratacao_descricao(String fase_contratacao_descricao) {
        this.fase_contratacao_descricao = fase_contratacao_descricao;
    }

    public boolean isFase_contratacao_ativo() {
        return fase_contratacao_ativo;
    }

    public void setFase_contratacao_ativo(boolean fase_contratacao_ativo) {
        this.fase_contratacao_ativo = fase_contratacao_ativo;
    }

    @Override
    public String toString() {
        return fase_contratacao_descricao != null ? fase_contratacao_descricao : "";
    }
}

