/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.cliente;

import model.rn.ClienteRN;

public class DeletarClienteTESTE {
    public static void main(String[] args) {
        try {
            ClienteRN clienteRN = new ClienteRN();

            // CPF utilizado nos testes de inserção/atualização
            String cpf = "12345678901";
            clienteRN.deletarCliente(cpf);

            System.out.println("OK. Cliente removido com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao remover cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

