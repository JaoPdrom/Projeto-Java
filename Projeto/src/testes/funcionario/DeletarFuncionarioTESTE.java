/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.funcionario;

import model.rn.FuncionarioRN;

public class DeletarFuncionarioTESTE {
    public static void main(String[] args) {
        try {
            FuncionarioRN func = new FuncionarioRN();

            // ✅ CPF de um funcionário existente no banco
            func.deletarFuncionario("00022255588");

            System.out.println("✅ Funcionário desativado com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao desativar funcionário: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
