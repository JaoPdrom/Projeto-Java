/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.produto;

import model.rn.ProdutoRN;
import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class AdicionarProdutoTESTE {

    public static void main(String[] args) {
        try {
            ProdutoVO produto = new ProdutoVO();
            produto.setProduto_nome("Produto Teste " + System.currentTimeMillis());
            produto.setProduto_peso(10);
            produto.setProduto_ativo(Boolean.TRUE);

            TipoProdutoVO tipo = new TipoProdutoVO();
            tipo.setTipoPdt_id(1); // ajuste conforme tipos cadastrados
            produto.setProduto_tipoPdt(tipo);

            ProdutoRN rn = new ProdutoRN();
            int id = rn.adicionarProduto(produto);
            System.out.println("Produto cadastrado com sucesso. ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
