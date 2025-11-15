package model.vo;

import java.time.LocalDate;

public class DemissaoVO {
    private int demissao_id;
    private LocalDate demissao_data;
    private String demissao_motivo;
    private FuncionarioVO funcionario;

    public DemissaoVO() {}

    public DemissaoVO(int demissao_id, LocalDate demissao_data, String demissao_motivo, FuncionarioVO funcionario) {
        this.demissao_id = demissao_id;
        this.demissao_data = demissao_data;
        this.demissao_motivo = demissao_motivo;
        this.funcionario = funcionario;
    }

    public int getDemissao_id() {
        return demissao_id;
    }

    public void setDemissao_id(int demissao_id) {
        this.demissao_id = demissao_id;
    }

    public LocalDate getDemissao_data() {
        return demissao_data;
    }

    public void setDemissao_data(LocalDate demissao_data) {
        this.demissao_data = demissao_data;
    }

    public String getDemissao_motivo() {
        return demissao_motivo;
    }

    public void setDemissao_motivo(String demissao_motivo) {
        this.demissao_motivo = demissao_motivo;
    }

    public FuncionarioVO getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioVO funcionario) {
        this.funcionario = funcionario;
    }
}
