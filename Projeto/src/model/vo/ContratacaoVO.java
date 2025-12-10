/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.vo;

import java.time.LocalDate;

public class ContratacaoVO {
    private int contratacao_id;
    private FaseContratacaoVO fase_contratacao;
    private LocalDate contratacao_dtContratacao;
    private FuncionarioVO funcionario;

    public ContratacaoVO() {}

    public ContratacaoVO(int contratacao_id, FaseContratacaoVO fase_contratacao, LocalDate contratacao_dtContratacao, FuncionarioVO funcionario) {
        this.contratacao_id = contratacao_id;
        this.fase_contratacao = fase_contratacao;
        this.contratacao_dtContratacao = contratacao_dtContratacao;
        this.funcionario = funcionario;
    }

    public int getContratacao_id() {
        return contratacao_id;
    }

    public void setContratacao_id(int contratacao_id) {
        this.contratacao_id = contratacao_id;
    }

    public FaseContratacaoVO getFase_contratacao() {
        return fase_contratacao;
    }

    public void setFase_contratacao(FaseContratacaoVO fase_contratacao) {
        this.fase_contratacao = fase_contratacao;
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
        return fase_contratacao != null ? fase_contratacao.getFase_contratacao_descricao() : "";
    }
}
