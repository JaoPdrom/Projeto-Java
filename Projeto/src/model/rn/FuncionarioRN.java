/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

import model.dao.CargoDAO;
import model.dao.ContratacaoDAO;
import model.dao.ConexaoDAO;
import model.dao.DemissaoDAO;
import model.dao.EnderecoDAO;
import model.dao.FuncionarioDAO;
import model.dao.LogDAO;
import model.dao.PesEndDAO;
import model.dao.TelefoneDAO;

import model.vo.CargoVO;
import model.vo.ContratacaoVO;
import model.vo.FuncionarioVO;
import model.vo.LogVO;
import model.dao.PessoaDAO;
import model.vo.PessoaVO;
import model.vo.TelefoneVO;
import model.vo.EnderecoVO;
import model.vo.DemissaoVO;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;
import java.time.Period;

public class FuncionarioRN {

    private FuncionarioDAO funcionarioDAO;
    private PessoaDAO pessoaDAO;

    public void salvarNovo(FuncionarioVO funcionario) throws Exception {
        info("Iniciando cadastro de novo funcionário.");
        if (funcionario == null) {
            throw new Exception("Funcionario obrigatório.");
        }

        String documento = funcionario.getPes_cpf();
        if (documento == null || documento.isBlank()) {
            throw new Exception("Documento obrigatório.");
        }

        if (funcionario.getPes_nome() == null || funcionario.getPes_nome().isBlank()) {
            throw new Exception("Nome obrigatório.");
        }

        if (funcionario.getPes_email() == null || funcionario.getPes_email().isBlank()) {
            throw new Exception("E-mail obrigatório.");
        }

        if (funcionario.getPes_tipo_pessoa() == null) {
            throw new Exception("Tipo de pessoa obrigatório.");
        }

        if (funcionario.getPes_sexo() == null) {
            throw new Exception("Sexo obrigatório.");
        }

        if (funcionario.getPes_dt_nascimento() == null) {
            throw new Exception("Data de nascimento obrigatória.");
        }
        LocalDate nascimento = funcionario.getPes_dt_nascimento();
        if (Period.between(nascimento, LocalDate.now()).getYears() < 18) {
            throw new Exception("Funcion�rio precisa ter pelo menos 18 anos.");
        }

        if (funcionario.getPes_ativo() == null) {
            throw new Exception("Status (ativo/inativo) obrigatório.");
        }
        if (funcionario.getFnc_numPis() == null || funcionario.getFnc_numPis().isBlank()) {

            throw new Exception("Número do PIS obrigatório.");

        }

        String pis = funcionario.getFnc_numPis().replaceAll("\s+", "");

        if (pis.length() != 11) {

            throw new Exception("N??mero do PIS deve conter 11 d??gitos.");

        }

        funcionario.setFnc_numPis(pis);




        List<TelefoneVO> telefones = funcionario.getTelefone();
        if (telefones == null || telefones.isEmpty()) {
            throw new Exception("Pelo menos um telefone é obrigatório.");
        }

        for (TelefoneVO telefone : telefones) {
            if (telefone.getTel_codPais() == null || telefone.getTel_codPais().isBlank()) {
                throw new Exception("Código do país do telefone é obrigatório.");
            }

            if (telefone.getTel_ddd() == null || telefone.getTel_ddd().isBlank()) {
                throw new Exception("DDD do telefone é obrigatório.");
            }

            if (telefone.getTel_numero() == null || telefone.getTel_numero().isBlank()) {
                throw new Exception("Número do telefone é obrigatório.");
            }
        }

        List<EnderecoVO> enderecos = funcionario.getEndereco();
        if (enderecos == null || enderecos.isEmpty()) {
            throw new Exception("Pelo menos um endereço é obrigatório.");
        }

        for (EnderecoVO endereco : enderecos) {
            if (endereco.getEnd_endP_id() == null || endereco.getEnd_endP_id().getEndP_id() == 0) {
                throw new Exception("Endereço deve possuir um EndPostal válido.");
            }

            if (endereco.getEnd_numero() == null || endereco.getEnd_numero().isBlank()) {
                throw new Exception("Número do endereço é obrigatório.");
            }
        }

        if (funcionario.getFnc_dtContratacao() == null) {
            throw new Exception("Data de contratação obrigatória.");
        }

        if (funcionario.getFnc_cargo() == null || funcionario.getFnc_cargo().getCar_id() <= 0) {
            throw new Exception("Cargo obrigatório.");
        }

        if (funcionario.getFnc_salario() <= 0) {
            throw new Exception("Salário deve ser maior que zero.");
        }

        if (funcionario.getFnc_dtDemissao() != null &&

                (funcionario.getFnc_motivo_demissao() == null || funcionario.getFnc_motivo_demissao().isBlank())) {

            throw new Exception("Motivo da demiss??o obrigat??rio quando informar a data de demiss??o.");

        }



        Connection con = null;
        try {
            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            this.pessoaDAO = new PessoaDAO(con);
            this.funcionarioDAO = new FuncionarioDAO(con);

            if (pessoaDAO.buscarPesCpf(documento) == null) {
                pessoaDAO.adicionarNovaPessoa(funcionario);
            } else {
                pessoaDAO.atualizarPessoa(funcionario);
            }

            if (funcionarioDAO.buscarFuncCpf(documento) != null) {
                throw new Exception("Funcionário já cadastrado para o documento informado.");
            }

            int id = funcionarioDAO.adicionarNovoFuncionario(funcionario);
            if (id <= 0) {
                throw new Exception("Falha ao inserir funcionário.");
            }

            funcionario.setFnc_id(id);

            ContratacaoVO contratacao = funcionario.getContratacao();
            if (contratacao == null) {
                contratacao = new ContratacaoVO();
                contratacao.setContratacao_fase("Contrata��o");
            }
            if (contratacao.getContratacao_fase() == null || contratacao.getContratacao_fase().isBlank()) {
                contratacao.setContratacao_fase("Contrata��o");
            }
            if (contratacao.getContratacao_dtContratacao() == null) {
                LocalDate dt = funcionario.getFnc_dtContratacao() != null ? funcionario.getFnc_dtContratacao() : LocalDate.now();
                contratacao.setContratacao_dtContratacao(dt);
            }
            contratacao.setFuncionario(funcionario);
            ContratacaoDAO contratacaoDAO = new ContratacaoDAO(con);
            contratacaoDAO.adicionar(contratacao);

            TelefoneDAO telefoneDAO = new TelefoneDAO(con);
            for (TelefoneVO telefone : telefones) {
                telefone.setTel_pes_cpf(documento);
                telefoneDAO.adicionarNovo(telefone, documento);
            }

            EnderecoDAO enderecoDAO = new EnderecoDAO(con);
            PesEndDAO pesEndDAO = new PesEndDAO(con);
            for (EnderecoVO endereco : enderecos) {
                int endId = enderecoDAO.adicionarNovo(endereco);
                if (endId > 0) {
                    pesEndDAO.vincular(documento, endId);
                }
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            erro("Erro ao salvar funcionário", e);
            throw new Exception("Erro ao salvar funcionário: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
        info("Funcionário salvo com sucesso.");
    }

    public void deletar(FuncionarioVO funcionario) throws Exception {
        info("Iniciando exclusao de funcionario.");
        if (funcionario == null || funcionario.getPes_cpf() == null || funcionario.getPes_cpf().isBlank()) {
            throw new Exception("Funcionario invalido para exclusao.");
        }

        Connection con = null;
        try {
            con = ConexaoDAO.getConexao();
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(con);
            funcionarioDAO.deletarFuncionario(funcionario);
        } catch (SQLException e) {
            erro("Erro ao excluir funcionario", e);
            throw new Exception("Erro ao excluir funcionario: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) { }
            }
        }
        info("Funcionario excluido com sucesso.");
    }

    public void atualizar(FuncionarioVO funcionario) throws Exception {
        info("Iniciando atualizacao de funcionario.");
        if (funcionario == null || funcionario.getFnc_id() <= 0) {
            throw new Exception("Funcionario invalido para atualizacao.");
        }

        String documento = funcionario.getPes_cpf();
        if (documento == null || documento.isBlank()) {
            throw new Exception("Documento obrigatorio.");
        }

        if (funcionario.getPes_nome() == null || funcionario.getPes_nome().isBlank()) {
            throw new Exception("Nome obrigatorio.");
        }

        if (funcionario.getPes_dt_nascimento() == null) {
            throw new Exception("Data de nascimento obrigatoria.");
        }

        if (funcionario.getPes_tipo_pessoa() == null) {
            throw new Exception("Tipo de pessoa obrigatorio.");
        }

        if (funcionario.getPes_sexo() == null) {
            throw new Exception("Sexo obrigatorio.");
        }

        if (funcionario.getTelefone() == null || funcionario.getTelefone().isEmpty()) {
            throw new Exception("Informe pelo menos um telefone.");
        }

        if (funcionario.getEndereco() == null || funcionario.getEndereco().isEmpty()) {
            throw new Exception("Informe pelo menos um endereco.");
        }

        if (funcionario.getFnc_dtContratacao() == null) {
            throw new Exception("Data de contratacao obrigatoria.");
        }

        if (funcionario.getFnc_cargo() == null || funcionario.getFnc_cargo().getCar_id() <= 0) {
            throw new Exception("Cargo obrigatorio.");
        }

        if (funcionario.getFnc_salario() <= 0) {
            throw new Exception("Salario deve ser maior que zero.");
        }

        if (funcionario.getFnc_dtDemissao() != null && (funcionario.getFnc_motivo_demissao() == null || funcionario.getFnc_motivo_demissao().isBlank())) {
            throw new Exception("Motivo da demissao obrigatorio quando informar a data.");
        }

        java.util.List<TelefoneVO> telefones = funcionario.getTelefone();
        java.util.List<EnderecoVO> enderecos = funcionario.getEndereco();

        for (EnderecoVO endereco : enderecos) {
            if (endereco.getEnd_endP_id() == null || endereco.getEnd_endP_id().getEndP_id() <= 0) {
                throw new Exception("Endereco deve possuir um EndPostal valido.");
            }
        }

        Connection con = null;
        try {
            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            PessoaDAO pessoaDAO = new PessoaDAO(con);
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(con);
            TelefoneDAO telefoneDAO = new TelefoneDAO(con);
            EnderecoDAO enderecoDAO = new EnderecoDAO(con);
            ContratacaoDAO contratacaoDAO = new ContratacaoDAO(con);
            DemissaoDAO demissaoDAO = new DemissaoDAO(con);

            pessoaDAO.atualizarPessoa(funcionario);
            funcionarioDAO.atualizarFuncionario(funcionario);
            telefoneDAO.atualizarTelefoneCpf(documento, telefones);
            enderecoDAO.sincronizarPorPessoa(documento, enderecos);

            ContratacaoVO contratacao = funcionario.getContratacao();
            if (contratacao != null && contratacao.getContratacao_fase() != null && !contratacao.getContratacao_fase().isBlank()) {
                if (contratacao.getContratacao_dtContratacao() == null) {
                    LocalDate dt = funcionario.getFnc_dtContratacao() != null ? funcionario.getFnc_dtContratacao() : LocalDate.now();
                    contratacao.setContratacao_dtContratacao(dt);
                }
                contratacao.setFuncionario(funcionario);
                contratacaoDAO.adicionar(contratacao);
            }

            if (funcionario.getFnc_dtDemissao() != null) {
                DemissaoVO demissao = new DemissaoVO();
                demissao.setFuncionario(funcionario);
                demissao.setDemissao_data(funcionario.getFnc_dtDemissao());
                demissao.setDemissao_motivo(funcionario.getFnc_motivo_demissao());
                demissaoDAO.registrarDemissao(demissao);
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            erro("Erro ao atualizar funcionario", e);
            throw new Exception("Erro ao atualizar funcionario: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
        info("Funcionario atualizado com sucesso.");
    }


    public List<CargoVO> listarCargos() throws Exception {
        try (Connection con = ConexaoDAO.getConexao()) {
            CargoDAO cargoDAO = new CargoDAO(con);
            return cargoDAO.buscarTodos();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar cargos: " + e.getMessage(), e);
        }
    }
    public java.util.List<ContratacaoVO> listarFasesContratacao() throws Exception {
        try (Connection con = ConexaoDAO.getConexao()) {
            ContratacaoDAO contratacaoDAO = new ContratacaoDAO(con);
            return contratacaoDAO.listarFases();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar fases de contrata��o: " + e.getMessage(), e);
        }
    }

    public java.util.List<FuncionarioVO> listarFuncionarios() throws Exception {
        try (Connection con = ConexaoDAO.getConexao()) {
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(con);
            return funcionarioDAO.listarTodosCompletos();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar funcion�rios: " + e.getMessage(), e);
        }
    }

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    private void info(String msg) {
        System.out.println(GREEN + "[INFO:FuncionarioRN] " + msg + RESET);
    }

    private void alerta(String msg) {
        System.out.println(YELLOW + "[AVISO:FuncionarioRN] " + msg + RESET);
    }

    private void erro(String msg, Exception e) {
        System.err.println(RED + "[ERRO:FuncionarioRN] " + msg + RESET);
        if (e != null) e.printStackTrace();
    }
    
}



