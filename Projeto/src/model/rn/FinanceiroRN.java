/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

public class FinanceiroRN {

    // Total de vendas (R$) no mês (1..12). Se statusVendaId for null, considera todos os status.
    public double totalVendasMes(int ano, int mes1a12, java.lang.Integer statusVendaId) throws Exception {
        java.util.Date[] periodo = periodoMensal(ano, mes1a12);
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.VendaDAO vendaDAO = new model.dao.VendaDAO(con);
            return vendaDAO.somarTotalNoPeriodo(periodo[0], periodo[1], statusVendaId);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao somar vendas do mês: " + e.getMessage(), e);
        }
    }

    // Total de despesas (R$) no mês (1..12)
    public double totalDespesasMes(int ano, int mes1a12) throws Exception {
        java.util.Date[] periodo = periodoMensal(ano, mes1a12);
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.DespesaDAO despesaDAO = new model.dao.DespesaDAO(con);
            return despesaDAO.somarTotalNoPeriodo(periodo[0], periodo[1]);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao somar despesas do mês: " + e.getMessage(), e);
        }
    }

    // Lucro do mês = vendas - despesas (aceita filtro de status para vendas)
    public double lucroMes(int ano, int mes1a12, java.lang.Integer statusVendaId) throws Exception {
        double vendas = totalVendasMes(ano, mes1a12, statusVendaId);
        double despesas = totalDespesasMes(ano, mes1a12);
        return vendas - despesas;
    }

    // Comparativo de vendas: mês informado vs mês anterior
    public java.util.Map<String, Double> comparativoVendasMes(int ano, int mes1a12, java.lang.Integer statusVendaId) throws Exception {
        int[] anterior = mesAnterior(ano, mes1a12);
        double atual = totalVendasMes(ano, mes1a12, statusVendaId);
        double prev = totalVendasMes(anterior[0], anterior[1], statusVendaId);
        double dif = atual - prev;
        double perc = (prev != 0.0) ? (dif / prev) * 100.0 : (atual > 0 ? 100.0 : 0.0);
        java.util.Map<String, Double> mapa = new java.util.LinkedHashMap<>();
        mapa.put("mesAtual", atual);
        mapa.put("mesAnterior", prev);
        mapa.put("diferenca", dif);
        mapa.put("percentual", perc);
        return mapa;
    }

    // Comparativo de lucro: mês informado vs mês anterior
    public java.util.Map<String, Double> comparativoLucroMes(int ano, int mes1a12, java.lang.Integer statusVendaId) throws Exception {
        int[] anterior = mesAnterior(ano, mes1a12);
        double atual = lucroMes(ano, mes1a12, statusVendaId);
        double prev = lucroMes(anterior[0], anterior[1], statusVendaId);
        double dif = atual - prev;
        double perc = (prev != 0.0) ? (dif / prev) * 100.0 : (atual > 0 ? 100.0 : 0.0);
        java.util.Map<String, Double> mapa = new java.util.LinkedHashMap<>();
        mapa.put("mesAtual", atual);
        mapa.put("mesAnterior", prev);
        mapa.put("diferenca", dif);
        mapa.put("percentual", perc);
        return mapa;
    }

    // util: calcula início e fim (inclusive) do mês
    private java.util.Date[] periodoMensal(int ano, int mes1a12) throws Exception {
        if (mes1a12 < 1 || mes1a12 > 12) throw new Exception("Mês inválido: use 1..12.");
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.clear();
        c.set(java.util.Calendar.YEAR, ano);
        c.set(java.util.Calendar.MONTH, mes1a12 - 1);
        c.set(java.util.Calendar.DAY_OF_MONTH, 1);
        java.util.Date inicio = c.getTime();

        c.add(java.util.Calendar.MONTH, 1);
        c.add(java.util.Calendar.DAY_OF_MONTH, -1);
        java.util.Date fim = c.getTime();
        return new java.util.Date[]{inicio, fim};
    }

    private int[] mesAnterior(int ano, int mes1a12) {
        int novoMes = mes1a12 - 1;
        int novoAno = ano;
        if (novoMes < 1) {
            novoMes = 12;
            novoAno = ano - 1;
        }
        return new int[]{novoAno, novoMes};
    }
}

