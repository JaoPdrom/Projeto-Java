/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.produto;

import model.rn.ProdutoRN;
import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class EditarProdutoTESTE {

    public static void main(String[] args) {

        int id = 1;

        try {
            ProdutoRN rn = new ProdutoRN();
            ProdutoVO produto = rn.buscarPorId(id);

            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            // Alterações de teste
            produto.setProduto_nome(produto.getProduto_nome() + " - Editado");
            produto.setProduto_peso(15);
            produto.setProduto_ativo(Boolean.TRUE);

            // Mantém o tipo atual, ou usa 1 caso não exista
            TipoProdutoVO tipo = new TipoProdutoVO();
            tipo.setTipoPdt_id(
                    produto.getProduto_tipoPdt() != null
                            ? produto.getProduto_tipoPdt().getTipoPdt_id()
                            : 1
            );
            produto.setProduto_tipoPdt(tipo);

            rn.editarProduto(produto);

            System.out.println("Produto editado com sucesso. ID: " + produto.getProduto_id());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
