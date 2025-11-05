/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.pessoa;

import model.rn.PessoaRN;
import model.vo.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AtualizarPessoaTESTE {
    public static void main(String[] args) throws SQLException {
        PessoaRN pessoaRN = new PessoaRN();
        Scanner sc = new Scanner(System.in);

        System.out.println("=== 🔄 TESTE DE ATUALIZAÇÃO DE PESSOA EXISTENTE ===");
        System.out.print("👉 Digite o CPF de uma pessoa que já existe no banco: ");
        String cpfExistente = sc.nextLine().trim();

        // ============================================================
        // 1) CRIA OBJETO DE PESSOA PARA ATUALIZAÇÃO
        // ============================================================
        PessoaVO pessoaAtualizada = new PessoaVO();
        pessoaAtualizada.setPes_cpf(cpfExistente);

        // altera nome e email
        pessoaAtualizada.setPes_nome("Nome Atualizado TESTE");
        pessoaAtualizada.setPes_email("email.atualizado@teste.com");

        // altera sexo
        SexoVO sexo2 = new SexoVO();
        sexo2.setSex_id(2); // supondo 1 = masculino, 2 = feminino
        pessoaAtualizada.setPes_sexo(sexo2);

        // altera data de nascimento
        pessoaAtualizada.setPes_dt_nascimento(LocalDate.of(2001, 8, 12));

        // mantém como ativo
        pessoaAtualizada.setPes_ativo(true);

        // ============================================================
        // 2) NOVOS TELEFONES (exemplo simples)
        // ============================================================
        TelefoneVO telefoneNovo = new TelefoneVO();
        telefoneNovo.setTel_codPais("55");
        telefoneNovo.setTel_ddd("17");
        telefoneNovo.setTel_numero("999999999");
        telefoneNovo.setTel_pes_cpf(cpfExistente);

        List<TelefoneVO> novosTelefones = new ArrayList<>();
        novosTelefones.add(telefoneNovo);
        pessoaAtualizada.setTelefone(novosTelefones);

        // ============================================================
        // 3) ENDEREÇOS (mantém o mesmo, ou cria fictício se quiser testar)
        // ============================================================
        EndPostalVO endPostal = new EndPostalVO();
        endPostal.setEndP_id(1); // supondo que já existe no banco

        EnderecoVO endereco = new EnderecoVO();
        endereco.setEnd_numero("1000");
        endereco.setEnd_complemento("Casa");
        endereco.setEnd_endP_id(endPostal);

        List<EnderecoVO> enderecos = new ArrayList<>();
        enderecos.add(endereco);
        pessoaAtualizada.setEndereco(enderecos);

        // ============================================================
        // 4) EXECUTA O MÉTODO DE ATUALIZAÇÃO
        // ============================================================
        try {
            System.out.println("\n🔄 Iniciando atualização parcial...");
            pessoaRN.atualizarPessoa(pessoaAtualizada);
            System.out.println("✅ Pessoa atualizada com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao atualizar pessoa: " + e.getMessage());
            if (e instanceof SQLException sqlEx) {
                System.err.println("\n=== 💥 ERRO SQL DETALHADO ===");
                System.err.println("Mensagem: " + sqlEx.getMessage());
                System.err.println("Código de erro: " + sqlEx.getErrorCode());
                System.err.println("Estado SQL: " + sqlEx.getSQLState());
                System.err.println("Classe da exceção: " + sqlEx.getClass().getName());
                System.err.println("==============================\n");
                sqlEx.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }

        // ============================================================
        // 5) RESULTADO FINAL
        // ============================================================
        System.out.println("\n🏁 Fim do teste de atualização automática.");
    }
}