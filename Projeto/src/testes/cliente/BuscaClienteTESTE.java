/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.cliente;

import model.rn.ClienteRN;
import model.vo.ClienteVO;
import model.vo.TipoPessoaVO;

import java.util.List;

public class BuscaClienteTESTE {
    public static void main(String[] args) {
        try {
            ClienteRN clienteRN = new ClienteRN();

            List<ClienteVO> clientes = clienteRN.buscarTodosClientes("Cliente");

            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente encontrado.");
            } else {
                System.out.println("=== Clientes encontrados ===");
                for (ClienteVO c : clientes) {
                    String tipo = "-";
                    TipoPessoaVO tp = c.getPes_tipo_pessoa();
                    if (tp != null && tp.getCodigo() != null) tipo = tp.getCodigo();
                    System.out.println(
                            "Documento: " + c.getPes_cpf() +
                            " | Nome: " + c.getPes_nome() +
                            " | Email: " + c.getPes_email() +
                            " | TipoPessoa: " + tipo
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
