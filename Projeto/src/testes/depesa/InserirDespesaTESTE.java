/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.depesa;

import model.dao.ConexaoDAO;
import model.dao.TipoDespesaDAO;
import model.rn.DespesaRN;
import model.vo.DespesaVO;
import model.vo.TipoDespesaVO;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Date;

public class InserirDespesaTESTE {
    public static void main(String[] args) {
        System.out.println("=== Teste de Inserção de Despesa ===");

        try {
            // Garante que exista um Tipo de Despesa
            int tipoId;
            String tipoNome = "OPERACIONAL";
            try (Connection con = ConexaoDAO.getConexao()) {
                TipoDespesaDAO tipoDAO = new TipoDespesaDAO(con);
                TipoDespesaVO tipo = tipoDAO.buscarPorNome(tipoNome);
                if (tipo == null) {
                    TipoDespesaVO novo = new TipoDespesaVO();
                    novo.setTipoDespesa_nome(tipoNome);
                    int novoId = tipoDAO.adicionarNovo(novo);
                    if (novoId <= 0) {
                        throw new Exception("Falha ao criar TipoDespesa de teste.");
                    }
                    tipoId = novoId;
                    System.out.println("TipoDespesa criado: id=" + tipoId + ", nome=" + tipoNome);
                } else {
                    tipoId = tipo.getTipoDespesa_id();
                    System.out.println("TipoDespesa existente: id=" + tipoId + ", nome=" + tipo.getTipoDespesa_nome());
                }
            }

            // Monta a despesa
            DespesaVO despesa = new DespesaVO();
            despesa.setDespesa_descricao("Conta de Luz - TESTE");
            despesa.setDespesa_dtRealizacao(LocalDate.now());
            despesa.setDespesa_valor_pago(123.45);
            TipoDespesaVO tipoRef = new TipoDespesaVO();
            tipoRef.setTipoDespesa_id(tipoId);
            despesa.setDespesa_tipo(tipoRef);

            // Chama RN para inserir
            DespesaRN rn = new DespesaRN();
            int idGerado = rn.adicionarDespesa(despesa);
            System.out.println("✅ Despesa inserida com sucesso. ID=" + idGerado);

            // Busca e imprime para conferência
            DespesaVO conferido = rn.buscarPorId(idGerado);
            if (conferido != null) {
                System.out.println("— Conferência —");
                System.out.println("ID: " + conferido.getDespesa_id());
                System.out.println("Descrição: " + conferido.getDespesa_descricao());
                System.out.println("Data: " + conferido.getDespesa_dtRealizacao());
                System.out.println("Valor: R$ " + conferido.getDespesa_valor_pago());
                System.out.println("Tipo: " + (conferido.getDespesa_tipo() != null ? conferido.getDespesa_tipo().getTipoDespesa_id() : null));
            }
        } catch (Exception e) {
            System.err.println("❌ Erro no teste de inserção de despesa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
