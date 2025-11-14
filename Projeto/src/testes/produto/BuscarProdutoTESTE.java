/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.produto;

import model.rn.ProdutoRN;
import model.vo.ProdutoVO;

public class BuscarProdutoTESTE {

    public static void main(String[] args) {

        int id = 1; 

        try {
            ProdutoRN rn = new ProdutoRN();
            ProdutoVO produto = rn.buscarPorId(id);

            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            System.out.println("Produto encontrado:");
            System.out.println("ID: " + produto.getProduto_id());
            System.out.println("Nome: " + produto.getProduto_nome());
            System.out.println("Peso: " + produto.getProduto_peso());
            System.out.println("Ativo: " + produto.getProduto_ativo());
            System.out.println("Tipo: " + 
                (produto.getProduto_tipoPdt() != null 
                    ? produto.getProduto_tipoPdt().getTipoPdt_id() 
                    : "N/D")
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
