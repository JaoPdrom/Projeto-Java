/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

import model.dao.ConexaoDAO;
import model.dao.FuncionarioDAO;
import model.dao.LogDAO;

import model.vo.FuncionarioVO;
import model.vo.LogVO;
import model.dao.PessoaDAO;
import model.vo.PessoaVO;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioRN {

    private FuncionarioDAO funcionarioDAO;
    private LogDAO logDAO;
    private PessoaDAO pessoaDAO;

    public FuncionarioRN() {}

    public void adicionarFuncionario(FuncionarioVO funcionario) throws Exception {
        Connection conn = null;

        try {
            conn = ConexaoDAO.getConexao();
            conn.setAutoCommit(false);

            this.funcionarioDAO = new FuncionarioDAO(conn);
            this.pessoaDAO = new PessoaDAO(conn);

            // 1) Validações
            if (funcionario.getPes_cpf() == null || funcionario.getPes_cpf().isEmpty())
                throw new Exception("CPF é obrigatório.");
            if (funcionario.getPes_nome() == null || funcionario.getPes_nome().isEmpty())
                throw new Exception("Nome é obrigatório.");
            if (funcionario.getPes_sexo() == null || funcionario.getPes_sexo().getSex_id() == 0)
                throw new Exception("Sexo inválido ou não informado.");

            // 2) “Upsert” de Pessoa
            PessoaVO jaExiste = pessoaDAO.buscarPesCpf(funcionario.getPes_cpf());
            if (jaExiste == null) {
                // Insere a pessoa uma única vez
                pessoaDAO.adicionarNovaPessoa(funcionario);
            } else {
                // Atualiza dados básicos (opcional)
                pessoaDAO.atualizarPessoa(funcionario);
            }

            FuncionarioVO fncExistente = funcionarioDAO.buscarFuncCpf(funcionario.getPes_cpf());
            if (fncExistente != null) {
                throw new Exception("Funcionario ja cadastrado no sistema.");
            }

            // 3) Agora insere o Funcionário (NÃO pode reinserir Pessoa aqui!)
            funcionarioDAO.adicionarNovoFuncionario(funcionario);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();

            // erro mais falante no console
            if (e instanceof java.sql.SQLException sqlEx) {
                System.err.println("\n=== ERRO SQL DETALHADO ===");
                System.err.println("Mensagem: " + sqlEx.getMessage());
                System.err.println("Código: " + sqlEx.getErrorCode());
                System.err.println("SQLState: " + sqlEx.getSQLState());
                sqlEx.printStackTrace();
            } else {
                e.printStackTrace();
            }

            throw new Exception("Erro ao adicionar funcionário: " + e.getMessage());
        } finally {
            if (conn != null) conn.close();
        }
    }

    public void atualizarFuncionario(FuncionarioVO funcionario) throws Exception {
        Connection conexao = null;
        try {
            // === 2) Abre conexão e inicia transação ===
            conexao = ConexaoDAO.getConexao();
            conexao.setAutoCommit(false);

            // === 1) Validações básicas ===
            if (funcionario == null) {
                throw new Exception("Objeto Funcionário não informado para atualização.");
            }

            if (funcionario.getPes_cpf() == null || funcionario.getPes_cpf().isEmpty()) {
                throw new Exception("CPF do funcionário é obrigatório para atualização.");
            }

            if (funcionario.getPes_nome() == null || funcionario.getPes_nome().isEmpty()) {
                throw new Exception("Nome do funcionário é obrigatório para atualização.");
            }

            if (funcionario.getFnc_cargo() == null || funcionario.getFnc_cargo().getCar_id() <= 0) {
                throw new Exception("Cargo inválido para o funcionário.");
            }


            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(conexao);

            // === 3) Verifica se funcionário existe ===
            FuncionarioVO existente = funcionarioDAO.buscarFuncCpf(funcionario.getPes_cpf());
            if (existente == null) {
                throw new Exception("Funcionário não encontrado para o CPF informado: " + funcionario.getPes_cpf());
            }

            // === 4) Atualiza dados ===
            funcionarioDAO.atualizarFuncionario(funcionario);
            conexao.commit();

            System.out.println("✅ Funcionário atualizado com sucesso.");

        } catch (SQLException e) {
            if (conexao != null && !conexao.getAutoCommit()) {
                conexao.rollback();
            }

            System.err.println("\n=== 💥 ERRO SQL DETALHADO ===");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("Código de erro: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Classe da exceção: " + e.getClass().getName());

            throw new Exception("Erro ao atualizar funcionário: " + e.getMessage(), e);

        } catch (Exception e) {
            if (conexao != null && !conexao.getAutoCommit()) {
                conexao.rollback();
            }
            throw new Exception("Erro ao atualizar funcionário: " + e.getMessage(), e);

        } finally {
            if (conexao != null && !conexao.isClosed()) {
                conexao.setAutoCommit(true);
                conexao.close();
            }
        }
    }



    public FuncionarioVO buscarPorCpf(String cpf) throws Exception {
        Connection conexao = null;
        conexao.setAutoCommit(false);

        try {
            conexao = ConexaoDAO.getConexao();
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(conexao);

            if (cpf == null || cpf.isEmpty()) {
                throw new Exception("CPF não informado para busca de funcionário.");
            }

            FuncionarioVO funcionario = funcionarioDAO.buscarFuncCpf(cpf);

            if (funcionario == null) {
                throw new Exception("Funcionário não encontrado para o CPF informado: " + cpf);
            }

            return funcionario;

        } catch (SQLException e) {
            System.err.println("\n=== 💥 ERRO SQL DETALHADO ===");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("Código de erro: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Classe da exceção: " + e.getClass().getName());
            throw new Exception("Erro ao buscar funcionário por CPF: " + e.getMessage(), e);

        } catch (Exception e) {
            throw new Exception("Erro ao buscar funcionário: " + e.getMessage(), e);

        } finally {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }
    }


    public List<FuncionarioVO> buscarPorNome(String nome) throws Exception {
        Connection conexao = null;

        try {
            conexao = ConexaoDAO.getConexao();
            conexao.setAutoCommit(false);
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(conexao);

            if (nome == null || nome.isEmpty()) {
                throw new Exception("Nome não informado para busca de funcionários.");
            }

            List<FuncionarioVO> funcionarios = funcionarioDAO.buscarFuncNome(nome);

            if (funcionarios == null || funcionarios.isEmpty()) {
                throw new Exception("Nenhum funcionário encontrado para o nome informado: " + nome);
            }

            return funcionarios;

        } catch (SQLException e) {
            System.err.println("\n=== 💥 ERRO SQL DETALHADO ===");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("Código de erro: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Classe da exceção: " + e.getClass().getName());
            throw new Exception("Erro ao buscar funcionários por nome: " + e.getMessage(), e);

        } catch (Exception e) {
            throw new Exception("Erro ao buscar funcionários: " + e.getMessage(), e);

        } finally {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }
    }


    public List<FuncionarioVO> buscarTodos() throws Exception {
        Connection conexao = null;

        try {
            conexao = ConexaoDAO.getConexao();
            conexao.setAutoCommit(false);
            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(conexao);

            List<FuncionarioVO> funcionarios = funcionarioDAO.buscarFuncNome("");

            if (funcionarios == null || funcionarios.isEmpty()) {
                throw new Exception("Nenhum funcionário encontrado no sistema.");
            }

            return funcionarios;

        } catch (SQLException e) {
            System.err.println("\n=== 💥 ERRO SQL DETALHADO ===");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("Código de erro: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Classe da exceção: " + e.getClass().getName());
            throw new Exception("Erro ao buscar todos os funcionários: " + e.getMessage(), e);

        } catch (Exception e) {
            throw new Exception("Erro ao listar funcionários: " + e.getMessage(), e);

        } finally {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }
    }

    public void deletarFuncionario(String cpf) throws Exception {
        Connection conexao = null;

        try {
            conexao = ConexaoDAO.getConexao();
            conexao.setAutoCommit(false);

            this.funcionarioDAO = new FuncionarioDAO(conexao);

            // 1️⃣ valida CPF
            if (cpf == null || cpf.isEmpty()) {
                throw new Exception("CPF do funcionário é obrigatório para exclusão.");
            }

            // 2️⃣ busca funcionário no banco
            FuncionarioVO funcionario = funcionarioDAO.buscarFuncCpf(cpf);
            if (funcionario == null) {
                throw new Exception("Funcionário não encontrado para o CPF informado: " + cpf);
            }

            // 3️⃣ executa o soft delete
            funcionarioDAO.deletarFuncionario(funcionario);

            conexao.commit();
            System.out.println("🟡 Funcionário desativado com sucesso.");

        } catch (SQLException e) {
            if (conexao != null) conexao.rollback();

            System.err.println("\n=== 💥 ERRO SQL DETALHADO ===");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("Código de erro: " + e.getErrorCode());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Classe da exceção: " + e.getClass().getName());

            throw new Exception("Erro ao desativar funcionário: " + e.getMessage(), e);

        } finally {
            if (conexao != null && !conexao.isClosed()) {
                conexao.setAutoCommit(true);
                conexao.close();
            }
        }
    }


}