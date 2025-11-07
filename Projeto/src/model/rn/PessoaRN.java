/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.dao.*;
import model.vo.*;

public class PessoaRN {

    private PessoaDAO pessoaDAO;
    private FuncionarioDAO funcionarioDAO;
    private TelefoneDAO telefoneDAO;
    private PesEndDAO pesEndDAO;
    private EnderecoDAO enderecoDAO;
    private LogDAO logDAO;

    public PessoaRN() throws SQLException {
        this.pessoaDAO = new PessoaDAO(null);
        this.logDAO = new LogDAO(null);
    }

    // add pessoa
    public void adicionarPessoa(PessoaVO pessoa) throws Exception {
        Connection conexao = null;
        try {
            conexao = ConexaoDAO.getConexao();
            conexao.setAutoCommit(false);

            // DAOs
            this.pessoaDAO = new PessoaDAO(conexao);
            this.logDAO    = new LogDAO(conexao);
            EndPostalDAO endPostalDAO = new EndPostalDAO(conexao);
            EnderecoDAO  enderecoDAO  = new EnderecoDAO(conexao);
            PesEndDAO    pesEndDAO    = new PesEndDAO(conexao);
            TelefoneDAO  telefoneDAO  = new TelefoneDAO(conexao);

            // verificacoes
            if (pessoa.getPes_cpf() == null || pessoa.getPes_cpf().isEmpty()) {
                throw new Exception("CPF é um campo obrigatório.");
            }
            if (pessoa.getPes_cpf().length() != 11) {
                throw new Exception("CPF deve ter 11 dígitos (CHAR(11)).");
            }
            if (pessoa.getPes_nome() == null || pessoa.getPes_nome().isEmpty()){
                throw new Exception("Nome é um campo obrigatório.");
            }

            // verifica cpf duplicado
            if (pessoaDAO.buscarPesCpf(pessoa.getPes_cpf()) != null) {
                throw new Exception("O CPF informado já está cadastrado.");
            }

            // insere pessoa
            pessoaDAO.adicionarNovaPessoa(pessoa);

            // enderecos
            if (pessoa.getEndereco() != null && !pessoa.getEndereco().isEmpty()) {
                for (EnderecoVO end : pessoa.getEndereco()) {
                    EndPostalVO endP = end.getEnd_endP_id();
                    if (endP == null) {
                        throw new Exception("Endereço sem EndPostal associado.");
                    }

                    // cria o EndPostal se ainda não tiver ID
                    if (endP.getEndP_id() == 0) {
                        int novoEndPId = endPostalDAO.adicionarNovo(endP);
                        if (novoEndPId <= 0)    
                            throw new Exception("Falha ao criar EndPostal.");
                        endP.setEndP_id(novoEndPId);
                    }

                    // cria o Endereco
                    int novoEndId = enderecoDAO.adicionarNovo(end);
                    if (novoEndId <= 0) throw new Exception("Falha ao criar Endereco.");
                    end.setEnd_id(novoEndId);

                    // vincula pessoa com endereço na tabela de junção
                    pesEndDAO.vincular(pessoa.getPes_cpf(), novoEndId);
                }
            }

            // 5) telefones
            if (pessoa.getTelefone() != null && !pessoa.getTelefone().isEmpty()) {
                for (TelefoneVO tel : pessoa.getTelefone()) {
                    // garanta o vínculo
                    tel.setTel_pes_cpf(pessoa.getPes_cpf());
                    telefoneDAO.adicionarNovo(tel, pessoa.getPes_cpf());
                }
            }

            conexao.commit();
        } catch (SQLException e) {
            if (conexao != null) conexao.rollback();
            throw new Exception("Erro ao adicionar pessoa: " + e.getMessage(), e);
        } finally {
            if (conexao != null) conexao.close();
        }
    }


    public void atualizarPessoa(PessoaVO pessoaAtt) throws Exception {
        Connection conexao = null;

        try {
            conexao = ConexaoDAO.getConexao();
            conexao.setAutoCommit(false); // inicia transação

            this.pessoaDAO = new PessoaDAO(conexao);
            this.logDAO = new LogDAO(conexao);
            this.enderecoDAO = new EnderecoDAO(conexao);
            this.telefoneDAO = new TelefoneDAO(conexao);

            // 1️⃣ validações básicas
            if (pessoaAtt.getPes_cpf() == null || pessoaAtt.getPes_cpf().isEmpty()) {
                throw new Exception("CPF é obrigatório para atualização.");
            }

            // 2️⃣ verifica se pessoa existe no banco
            PessoaVO pessoaExistente = pessoaDAO.buscarPesCpf(pessoaAtt.getPes_cpf());
            if (pessoaExistente == null) {
                throw new Exception("Pessoa não encontrada para CPF: " + pessoaAtt.getPes_cpf());
            }

            // 3️⃣ executa o update parcial (só campos alterados)
            pessoaDAO.atualizarPessoa(pessoaAtt);

            // 4️⃣ atualiza telefones, se enviados
            if (pessoaAtt.getTelefone() != null && !pessoaAtt.getTelefone().isEmpty()) {
                telefoneDAO.atualizarTelefoneCpf(pessoaAtt.getPes_cpf(), pessoaAtt.getTelefone());
            }

            // 5️⃣ atualiza endereços, se enviados
            if (pessoaAtt.getEndereco() != null && !pessoaAtt.getEndereco().isEmpty()) {
                enderecoDAO.sincronizarPorPessoa(pessoaAtt.getPes_cpf(), pessoaAtt.getEndereco());
            }

            conexao.commit();
            System.out.println("✅ Pessoa atualizada com sucesso via PessoaRN!");

        } catch (Exception e) {
            if (conexao != null) {
                try {
                    conexao.rollback();
                    System.err.println("⛔ Rollback executado: " + e.getMessage());
                } catch (SQLException ex) {
                    System.err.println("⚠️ Erro ao executar rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conexao != null) {
                try {
                    conexao.setAutoCommit(true);
                    conexao.close();
                } catch (SQLException e) {
                    System.err.println("⚠️ Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }}

    public void deletarPessoa(String documento) throws Exception {
        Connection conexao = null;
        try {
        conexao = ConexaoDAO.getConexao();
        conexao.setAutoCommit(false);

            this.pessoaDAO = new PessoaDAO(conexao);
            this.funcionarioDAO = new FuncionarioDAO(conexao);
            this.telefoneDAO = new TelefoneDAO(conexao);
            this.pesEndDAO = new PesEndDAO(conexao);

            if (documento == null || documento.isEmpty()) {
                throw new Exception("Documento é um campo obrigatório.");
            }

            FuncionarioVO funcionario = funcionarioDAO.buscarFuncCpf(documento); // ok se seu DAO ainda usa CPF

            if (funcionario != null) {
                throw new Exception("Pessoa é um funcionário, método ainda não implementado");
            }

            PessoaVO pessoa = pessoaDAO.buscarPesCpf(documento);
            if (pessoa == null) {
                throw new Exception("Pessoa não encontrada para deleção.");
            }

            Integer endId = pesEndDAO.buscarEndIdPorDocumento(documento);
            if (endId != null) {
                pesEndDAO.desvincular(documento, endId);
            }

            pessoaDAO.deletarPes(pessoa);
            conexao.commit();

        } catch (SQLException e) {
            if (conexao != null) conexao.rollback();
            throw new Exception("Erro ao deletar pessoa: " + e.getMessage(), e);
        } finally {
            if (conexao != null) conexao.close();
        }
}

    public PessoaVO buscarPorCpfRN(String cpf) throws Exception {
        if (cpf == null || cpf.isBlank()) {
            throw new Exception("CPF é um campo obrigatório.");
        }

        PessoaVO pessoa = null;

        try (Connection conexao = ConexaoDAO.getConexao()) {
            // Inicializa os DAOs com a mesma conexão
            this.pessoaDAO = new PessoaDAO(conexao);
            this.logDAO = new LogDAO(conexao);

            pessoa = pessoaDAO.buscarPesCpf(cpf);

        } catch (SQLException e) {
            throw new Exception("Erro ao buscar pessoa: " + e.getMessage(), e);
        }

        return pessoa;
    }


    public List<PessoaVO> buscarTodasPessoas(String nome) throws Exception {
        if (nome == null || nome.isBlank()) {
            nome = ""; // busca todos
        }

        List<PessoaVO> pessoas;

        try (Connection conexao = ConexaoDAO.getConexao()) {
            this.pessoaDAO = new PessoaDAO(conexao);
            pessoas = pessoaDAO.buscarPesNome(nome);

        } catch (SQLException e) {
            throw new Exception("Erro ao buscar pessoas: " + e.getMessage(), e);
        }

        return pessoas;
    }
}