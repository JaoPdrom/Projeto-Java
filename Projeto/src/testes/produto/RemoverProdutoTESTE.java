/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.produto;

import model.rn.ProdutoRN;

public class RemoverProdutoTESTE {

    public static void main(String[] args) {

        int id = 1;

        try {
            ProdutoRN rn = new ProdutoRN();
            rn.excluirProduto(id);

            System.out.println("Produto desativado com sucesso. ID: " + id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
