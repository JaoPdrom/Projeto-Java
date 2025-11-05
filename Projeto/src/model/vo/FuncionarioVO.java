package model.vo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class FuncionarioVO extends PessoaVO{
    private int fnc_id;
    private LocalDate fnc_dtContratacao;
    private LocalDate fnc_dtDemissao;
    private double fnc_salario;
    private CargoVO fnc_cargo;
    private PessoaVO fnc_pes_cpf;

    public FuncionarioVO() {}

    public FuncionarioVO(String pes_cpf, String pes_nome, SexoVO pes_sexo, LocalDate pes_dt_nascimento, String pes_email, Boolean pes_ativo, List<TelefoneVO> telefone, List<EnderecoVO> endereco, int fnc_id, LocalDate fnc_dtContratacao, LocalDate fnc_dtDemissao, double fnc_salario, CargoVO fnc_cargo, PessoaVO fnc_pes_cpf) {
        super(pes_cpf, pes_nome, pes_sexo, pes_dt_nascimento, pes_email, pes_ativo, telefone, endereco);
        this.fnc_id = fnc_id;
        this.fnc_dtContratacao = fnc_dtContratacao;
        this.fnc_dtDemissao = fnc_dtDemissao;
        this.fnc_salario = fnc_salario;
        this.fnc_cargo = fnc_cargo;
        this.fnc_pes_cpf = fnc_pes_cpf;
    }

    public int getFnc_id() {
        return fnc_id;
    }

    public void setFnc_id(int fnc_id) {
        this.fnc_id = fnc_id;
    }

    public LocalDate getFnc_dtContratacao() {
        return fnc_dtContratacao;
    }

    public void setFnc_dtContratacao(LocalDate fnc_dtContratacao) {
        this.fnc_dtContratacao = fnc_dtContratacao;
    }

    public LocalDate getFnc_dtDemissao() {
        return fnc_dtDemissao;
    }

    public void setFnc_dtDemissao(LocalDate fnc_dtDemissao) {
        this.fnc_dtDemissao = fnc_dtDemissao;
    }

    public double getFnc_salario() {
        return fnc_salario;
    }

    public void setFnc_salario(double fnc_salario) {
        this.fnc_salario = fnc_salario;
    }

    public CargoVO getFnc_cargo() {
        return fnc_cargo;
    }

    public void setFnc_cargo(CargoVO fnc_cargo) {
        this.fnc_cargo = fnc_cargo;
    }

    public PessoaVO getFnc_pes_cpf() {
        return fnc_pes_cpf;
    }

    public void setFnc_pes_cpf(PessoaVO fnc_pes_cpf) {
        this.fnc_pes_cpf = fnc_pes_cpf;
    }
}
