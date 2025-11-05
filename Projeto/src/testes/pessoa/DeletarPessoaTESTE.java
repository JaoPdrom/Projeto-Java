/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.pessoa;

import model.rn.PessoaRN;
import model.vo.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class DeletarPessoaTESTE {
    public static void main(String[] args) throws SQLException {
        PessoaVO p1 = new PessoaVO();

        p1.setPes_cpf("33344455566");
        p1.setPes_nome("Paula");

        SexoVO sexo1 = new SexoVO();
        sexo1.setSex_id(1);
        p1.setPes_sexo(sexo1);

        p1.setPes_dt_nascimento(LocalDate.of(2004, 9, 15));
        p1.setPes_email("email@email.com");

        CidadeVO cidade = new CidadeVO();
        cidade.setCid_id(1);

        EstadoVO estado = new EstadoVO();
        estado.setEst_sigla("SP");

        BairroVO bairro = new BairroVO();
        bairro.setBairro_id(1);

        LogradouroVO logradouro = new LogradouroVO();
        logradouro.setLogradouro_id(1);

        EndPostalVO endPostal = new EndPostalVO();
        endPostal.setEndP_bairro(bairro);
        endPostal.setEndP_cidade(cidade);
        endPostal.setEndP_estado(estado);
        endPostal.setEndP_logradouro(logradouro);
        endPostal.setEndP_cep("85892000");
        endPostal.setEndP_nomeRua("Parana");

        EnderecoVO endereco = new EnderecoVO();
        endereco.setEnd_complemento("Casa");
        endereco.setEnd_numero("1234");
        endereco.setEnd_endP_id(endPostal);

        java.util.List<EnderecoVO> enderecos = new java.util.ArrayList<>();
        enderecos.add(endereco);
        p1.setEndereco(enderecos);

        TelefoneVO telefone1 = new TelefoneVO();
        telefone1.setTel_codPais("55");
        telefone1.setTel_ddd("17");
        telefone1.setTel_numero("5555555555");
        telefone1.setTel_pes_cpf(p1.getPes_cpf());

        java.util.List<TelefoneVO> telefones = new java.util.ArrayList<>();
        telefones.add(telefone1);
        p1.setTelefone(telefones);

        p1.setPes_ativo(true);

        PessoaRN pessoaRN = new PessoaRN();

        try {
            pessoaRN.adicionarPessoa(p1);
            System.out.println("✅ Pessoa cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao Pessoa funcionário: " + e.getMessage());

            // Se for erro SQL, mostra detalhes
            if (e instanceof SQLException) {
                SQLException sqlEx = (SQLException) e;
                System.err.println("\n=== 💥 ERRO SQL DETALHADO ===");
                System.err.println("Mensagem: " + sqlEx.getMessage());
                System.err.println("Código de erro: " + sqlEx.getErrorCode());
                System.err.println("Estado SQL: " + sqlEx.getSQLState());
                System.err.println("Classe da exceção: " + sqlEx.getClass().getName());
                System.err.println("==============================\n");
                sqlEx.printStackTrace();
            } else {
                // Para qualquer outro tipo de exceção
                e.printStackTrace();
            }
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Digite 1 para continuar: ");
        int id = sc.nextInt();

        try {
            pessoaRN.deletarPessoa(p1.getPes_cpf());
            System.out.println("✅ Pessoa removida com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao Pessoa funcionário: " + e.getMessage());

            // Se for erro SQL, mostra detalhes
            if (e instanceof SQLException) {
                SQLException sqlEx = (SQLException) e;
                System.err.println("\n=== 💥 ERRO SQL DETALHADO ===");
                System.err.println("Mensagem: " + sqlEx.getMessage());
                System.err.println("Código de erro: " + sqlEx.getErrorCode());
                System.err.println("Estado SQL: " + sqlEx.getSQLState());
                System.err.println("Classe da exceção: " + sqlEx.getClass().getName());
                System.err.println("==============================\n");
                sqlEx.printStackTrace();
            } else {
                // Para qualquer outro tipo de exceção
                e.printStackTrace();
            }
        }
    }
}
