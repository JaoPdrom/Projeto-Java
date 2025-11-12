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
import java.util.List;

public class AtualizarDespesaTESTE {
    public static void main(String[] args) {
        System.out.println("=== Teste de Atualização de Despesa ===");

        try {
            // Garante o tipo ADMINISTRATIVA
            int tipoAdminId;
            String tipoAdminNome = "ADMINISTRATIVA";
            try (Connection con = ConexaoDAO.getConexao()) {
                TipoDespesaDAO tipoDAO = new TipoDespesaDAO(con);
                TipoDespesaVO tipo = tipoDAO.buscarPorNome(tipoAdminNome);
                if (tipo == null) {
                    TipoDespesaVO novo = new TipoDespesaVO();
                    novo.setTipoDespesa_nome(tipoAdminNome);
                    int novoId = tipoDAO.adicionarNovo(novo);
                    if (novoId <= 0) throw new Exception("Falha ao criar TipoDespesa ADMINISTRATIVA.");
                    tipoAdminId = novoId;
                    System.out.println("TipoDespesa criado: id=" + tipoAdminId + ", nome=" + tipoAdminNome);
                } else {
                    tipoAdminId = tipo.getTipoDespesa_id();
                    System.out.println("TipoDespesa existente: id=" + tipoAdminId + ", nome=" + tipo.getTipoDespesa_nome());
                }
            }

            DespesaRN rn = new DespesaRN();

            // Tenta encontrar a despesa criada no teste de inserção
            List<DespesaVO> possiveis = rn.buscarPorDescricao("Conta de Luz - TESTE");
            DespesaVO alvo = null;
            if (possiveis != null) {
                for (DespesaVO d : possiveis) {
                    if ("Conta de Luz - TESTE".equals(d.getDespesa_descricao())) { alvo = d; break; }
                }
            }
            // Se não encontrar, cria uma nova para atualizar
            if (alvo == null) {
                System.out.println("Despesa base não encontrada. Criando uma nova para atualização...");
                DespesaVO nova = new DespesaVO();
                nova.setDespesa_descricao("Conta de Luz - TESTE");
                nova.setDespesa_dtRealizacao(LocalDate.now());
                nova.setDespesa_valor_pago(150.00);
                // usar OPERACIONAL como base
                int tipoOperId;
                try (Connection con = ConexaoDAO.getConexao()) {
                    TipoDespesaDAO tipoDAO = new TipoDespesaDAO(con);
                    TipoDespesaVO tipoOper = tipoDAO.buscarPorNome("OPERACIONAL");
                    if (tipoOper == null) {
                        TipoDespesaVO novoTipo = new TipoDespesaVO();
                        novoTipo.setTipoDespesa_nome("OPERACIONAL");
                        tipoOperId = tipoDAO.adicionarNovo(novoTipo);
                    } else {
                        tipoOperId = tipoOper.getTipoDespesa_id();
                    }
                }
                TipoDespesaVO ref = new TipoDespesaVO();
                ref.setTipoDespesa_id(tipoOperId);
                nova.setDespesa_tipo(ref);
                int id = rn.adicionarDespesa(nova);
                alvo = rn.buscarPorId(id);
            }

            System.out.println("Alvo antes: id=" + alvo.getDespesa_id() + ", desc='" + alvo.getDespesa_descricao() + "', valor=" + alvo.getDespesa_valor_pago());

            // Atualiza campos
            alvo.setDespesa_descricao("Conta de Luz - TESTE (Atualizada)");
            alvo.setDespesa_valor_pago(200.00);
            TipoDespesaVO adminRef = new TipoDespesaVO();
            adminRef.setTipoDespesa_id(tipoAdminId);
            alvo.setDespesa_tipo(adminRef);

            rn.atualizarDespesa(alvo);
            DespesaVO conferido = rn.buscarPorId(alvo.getDespesa_id());
            System.out.println("✅ Atualizada. Nova descrição: '" + conferido.getDespesa_descricao() + "', valor=" + conferido.getDespesa_valor_pago() + ", tipoId=" + (conferido.getDespesa_tipo()!=null?conferido.getDespesa_tipo().getTipoDespesa_id():null));

        } catch (Exception e) {
            System.err.println("❌ Erro no teste de atualização de despesa: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

