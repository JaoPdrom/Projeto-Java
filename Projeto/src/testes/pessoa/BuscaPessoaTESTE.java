/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.pessoa;

import model.rn.PessoaRN;
import model.vo.PessoaVO;

import java.util.List;

public class BuscaPessoaTESTE {
    public static void main(String[] args) {
        try {
            PessoaRN pessoaRN = new PessoaRN();

            // 🔍 Buscar todas (passando nome vazio)
            List<PessoaVO> pessoas = pessoaRN.buscarTodasPessoas("Joao");

            if (pessoas.isEmpty()) {
                System.out.println("Nenhuma pessoa encontrada.");
            } else {
                System.out.println("=== Pessoas encontradas ===");
                for (PessoaVO p : pessoas) {
                    System.out.println(
                            "CPF: " + p.getPes_cpf() +
                                    " | Nome: " + p.getPes_nome() +
                                    " | Email: " + p.getPes_email()
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
