package model.vo;

public class EstadoVO {
    private String est_sigla;
    private String est_descricao;

    public EstadoVO() {}

    public EstadoVO(String est_sigla, String est_descricao) {
        this.est_sigla = est_sigla;
        this.est_descricao = est_descricao;
    }

    public String getEst_sigla() {
        return est_sigla;
    }

    public void setEst_sigla(String est_sigla) {
        this.est_sigla = est_sigla;
    }

    public String getEst_descricao() {
        return est_descricao;
    }

    public void setEst_descricao(String est_descricao) {
        this.est_descricao = est_descricao;
    }

    @Override
    public String toString() {
        return est_descricao;
    }
}
