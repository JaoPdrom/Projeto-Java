package model.vo;

import java.time.LocalDate;

public class ContratacaoVO {
    private int contratacao_id;
    private String contratacao_fase;
    private LocalDate contratacao_dtContratacao;
    private FuncionarioVO funcionario;

    public ContratacaoVO() {}

    public ContratacaoVO(int contratacao_id, String contratacao_fase, LocalDate contratacao_dtContratacao, FuncionarioVO funcionario) {
        this.contratacao_id = contratacao_id;
        this.contratacao_fase = contratacao_fase;
        this.contratacao_dtContratacao = contratacao_dtContratacao;
        this.funcionario = funcionario;
    }

    public int getContratacao_id() {
        return contratacao_id;
    }

    public void setContratacao_id(int contratacao_id) {
        this.contratacao_id = contratacao_id;
    }

    public String getContratacao_fase() {
        return contratacao_fase;
    }

    public void setContratacao_fase(String contratacao_fase) {
        this.contratacao_fase = contratacao_fase;
    }

    public LocalDate getContratacao_dtContratacao() {
        return contratacao_dtContratacao;
    }

    public void setContratacao_dtContratacao(LocalDate contratacao_dtContratacao) {
        this.contratacao_dtContratacao = contratacao_dtContratacao;
    }

    public FuncionarioVO getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioVO funcionario) {
        this.funcionario = funcionario;
    }

    @Override
    public String toString() {
        return contratacao_fase != null ? contratacao_fase : "";
    }
}
