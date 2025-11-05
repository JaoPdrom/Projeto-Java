/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.vo;

import java.time.LocalDate;
import java.util.List;

public class ClienteVO extends PessoaVO{
    private int cli_id;
    private PessoaVO cli_pes_cpf;
    private LocalDate cli_dtCadastro;
    // TipoCliente removido; PF/PJ agora está em Pessoa (TipoPessoa)

    public ClienteVO() {}

    public ClienteVO(String pes_cpf, String pes_nome, SexoVO pes_sexo, LocalDate pes_dt_nascimento, String pes_email, Boolean pes_ativo, List<TelefoneVO> telefone, List<EnderecoVO> endereco, int cli_id, PessoaVO cli_pes_cpf, LocalDate cli_dtCadastro) {
        super(pes_cpf, pes_nome, pes_sexo, pes_dt_nascimento, pes_email, pes_ativo, telefone, endereco);
        this.cli_id = cli_id;
        this.cli_pes_cpf = cli_pes_cpf;
        this.cli_dtCadastro = cli_dtCadastro;
    }

    public int getCli_id() {
        return cli_id;
    }

    public void setCli_id(int cli_id) {
        this.cli_id = cli_id;
    }

    public PessoaVO getCli_pes_cpf() {
        return cli_pes_cpf;
    }

    public void setCli_pes_cpf(PessoaVO cli_pes_cpf) {
        this.cli_pes_cpf = cli_pes_cpf;
    }

    public LocalDate getCli_dtCadastro() {
        return cli_dtCadastro;
    }

    public void setCli_dtCadastro(LocalDate cli_dtCadastro) {
        this.cli_dtCadastro = cli_dtCadastro;
    }

    // TipoCliente removido
}
