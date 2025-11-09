package model.vo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PessoaVO {
    private String pes_cpf; // CPF/CNPJ
    private String pes_nome;
    private SexoVO pes_sexo;
    private LocalDate pes_dt_nascimento;
    private String pes_email;
    private Boolean pes_ativo;
    private TipoPessoaVO pes_tipo_pessoa; // PF/PJ
    private List<TelefoneVO> telefone;
    private List<EnderecoVO> endereco;

    // Construtor padrão
    public PessoaVO() {
        this.telefone = new ArrayList<>();
        this.endereco = new ArrayList<>();
    }

    // Construtor completo
    public PessoaVO(String pes_cpf, String pes_nome, SexoVO pes_sexo, LocalDate pes_dt_nascimento, String pes_email, Boolean pes_ativo, List<TelefoneVO> telefone, List<EnderecoVO> endereco) {
        this.pes_cpf = pes_cpf;
        this.pes_nome = pes_nome;
        this.pes_sexo = pes_sexo;
        this.pes_dt_nascimento = pes_dt_nascimento;
        this.pes_email = pes_email;
        this.pes_ativo = pes_ativo;
        // inicializa listas vazias se vierem nulas
        this.telefone = (telefone != null) ? telefone : new ArrayList<>();
        this.endereco = (endereco != null) ? endereco : new ArrayList<>();
    }

    // Getters e Setters
    public String getPes_cpf() {
        return pes_cpf;
    }

    public void setPes_cpf(String pes_cpf) {
        this.pes_cpf = pes_cpf;
    }

    public String getPes_nome() {
        return pes_nome;
    }

    public void setPes_nome(String pes_nome) {
        this.pes_nome = pes_nome;
    }

    public SexoVO getPes_sexo() {
        return pes_sexo;
    }

    public void setPes_sexo(SexoVO pes_sexo) {
        this.pes_sexo = pes_sexo;
    }

    public LocalDate getPes_dt_nascimento() {
        return pes_dt_nascimento;
    }

    public void setPes_dt_nascimento(LocalDate pes_dt_nascimento) {
        this.pes_dt_nascimento = pes_dt_nascimento;
    }

    public String getPes_email() {
        return pes_email;
    }

    public void setPes_email(String pes_email) {
        this.pes_email = pes_email;
    }

    public Boolean getPes_ativo() {
        return pes_ativo;
    }

    public void setPes_ativo(Boolean pes_ativo) {
        this.pes_ativo = pes_ativo;
    }

    public TipoPessoaVO getPes_tipo_pessoa() {
        return pes_tipo_pessoa;
    }

    public void setPes_tipo_pessoa(TipoPessoaVO pes_tipo_pessoa) {
        this.pes_tipo_pessoa = pes_tipo_pessoa;
    }

    public List<TelefoneVO> getTelefone() {
        return telefone;
    }

    public void setTelefone(List<TelefoneVO> telefone) {
        if (telefone == null) {
            this.telefone = new ArrayList<>();
        } else {
            this.telefone = telefone;
        }
    }

    public List<EnderecoVO> getEndereco() {
        return endereco;
    }

    public void setEndereco(List<EnderecoVO> endereco) {
        if (endereco == null) {
            this.endereco = new ArrayList<>();
        } else {
            this.endereco = endereco;
        }
    }
}
