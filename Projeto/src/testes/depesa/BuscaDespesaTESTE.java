/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.depesa;

import model.rn.DespesaRN;
import model.vo.DespesaVO;

import java.util.List;

public class BuscaDespesaTESTE {
    public static void main(String[] args) {
        System.out.println("=== Teste de Busca/Listagem de Despesas ===");
        try {
            DespesaRN rn = new DespesaRN();

            // Busca por descrição (LIKE)
            String termo = "Conta";
            List<DespesaVO> porDescricao = rn.buscarPorDescricao(termo);
            System.out.println("Resultados por descrição contendo '" + termo + "': " + (porDescricao != null ? porDescricao.size() : 0));
            if (porDescricao != null) {
                for (DespesaVO d : porDescricao) {
                    System.out.println(" - [" + d.getDespesa_id() + "] " + d.getDespesa_descricao() + ", valor=" + d.getDespesa_valor_pago() + ", tipoId=" + (d.getDespesa_tipo()!=null?d.getDespesa_tipo().getTipoDespesa_id():null));
                }
            }

            // Busca todas
            List<DespesaVO> todas = rn.buscarTodas();
            System.out.println("Total de despesas cadastradas: " + (todas != null ? todas.size() : 0));
            if (todas != null) {
                int count = 0;
                for (DespesaVO d : todas) {
                    System.out.println(" * [" + d.getDespesa_id() + "] " + d.getDespesa_descricao() + " | " + d.getDespesa_dtRealizacao() + " | R$ " + d.getDespesa_valor_pago());
                    if (++count >= 10) break; // evita log gigante
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erro no teste de busca/listagem de despesas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

