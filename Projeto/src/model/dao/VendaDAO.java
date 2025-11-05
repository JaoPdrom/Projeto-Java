/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendaDAO {
    private Connection con_venda;

    public VendaDAO(Connection con_venda) throws SQLException {
        this.con_venda = con_venda;
    }

    // adicionar nova venda
    public int adicionarNovo(VendaVO venda) throws SQLException {
        String sql = "INSERT INTO tb_venda (venda_data, venda_pes_cpf, venda_fnc_id, venda_statusVenda, venda_tipo_pagamento) VALUES (?, ?, ?, ?, ?)";
        int vendaId = -1;
        try (PreparedStatement venda_add = con_venda.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            venda_add.setDate(1, new java.sql.Date(venda.getVenda_data().getTime()));
            venda_add.setString(2, venda.getVenda_pes_cpf().getPes_cpf());
            venda_add.setInt(3, venda.getVenda_func_cpf().getFnc_id());
            venda_add.setInt(4, venda.getVenda_statusVenda().getStatusVenda_id());
            venda_add.setInt(5, venda.getVenda_tipoPagamento().getTipoPagamento_id());
            venda_add.executeUpdate();
            try (ResultSet rs = venda_add.getGeneratedKeys()) {
                if (rs.next()) {
                    vendaId = rs.getInt(1);
                }
            }
        }
        return vendaId;
    }

    // update venda por id
    public void atualizarPorId(VendaVO venda) throws SQLException {
        String sql = "UPDATE tb_venda SET venda_data = ?, venda_pes_cpf = ?, venda_fnc_id = ?, venda_statusVenda = ?, venda_tipo_pagamento = ? WHERE venda_id = ?";
        try (PreparedStatement venda_att = con_venda.prepareStatement(sql)) {
            venda_att.setDate(1, new java.sql.Date(venda.getVenda_data().getTime()));
            venda_att.setString(2, venda.getVenda_pes_cpf().getPes_cpf());
            venda_att.setInt(3, venda.getVenda_func_cpf().getFnc_id());
            venda_att.setInt(4, venda.getVenda_statusVenda().getStatusVenda_id());
            venda_att.setInt(5, venda.getVenda_tipoPagamento().getTipoPagamento_id());
            venda_att.setInt(6, venda.getVenda_id());
            venda_att.executeUpdate();
        }
    }

    // busca venda por id
    public VendaVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_venda WHERE venda_id = ?";
        VendaVO venda = null;
        try (PreparedStatement venda_bsc = con_venda.prepareStatement(sql)) {
            venda_bsc.setInt(1, id);
            try (ResultSet rs = venda_bsc.executeQuery()) {
                if (rs.next()) {
                    venda = new VendaVO();
                    venda.setVenda_id(rs.getInt("venda_id"));
                    venda.setVenda_data(rs.getDate("venda_data"));

                    // Buscar objetos completos
                    PessoaDAO pessoaDAO = new PessoaDAO(con_venda);
                    FuncionarioDAO funcionarioDAO = new FuncionarioDAO(con_venda);
                    StatusVendaDAO statusVendaDAO = new StatusVendaDAO(con_venda);
                    TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO(con_venda);

                    venda.setVenda_pes_cpf(pessoaDAO.buscarPesCpf(rs.getString("venda_pes_cpf")));
                    venda.setVenda_func_cpf(funcionarioDAO.buscarPorId(rs.getInt("venda_fnc_id")));
                    venda.setVenda_statusVenda(statusVendaDAO.buscarPorId(rs.getInt("venda_statusVenda")));
                    venda.setVenda_tipoPagamento(tipoPagamentoDAO.buscarPorId(rs.getInt("venda_tipo_pagamento")));
                }
            }
        }
        return venda;
    }

    // busca venda por cpf
    public List<VendaVO> buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM tb_venda WHERE venda_pes_cpf = ?";
        List<VendaVO> vendas = new ArrayList<>();
        try (PreparedStatement venda_bsc = con_venda.prepareStatement(sql)) {
            venda_bsc.setString(1, cpf);
            try (ResultSet rs = venda_bsc.executeQuery()) {
                while (rs.next()) {
                    VendaVO venda = new VendaVO();
                    venda.setVenda_id(rs.getInt("venda_id"));
                    venda.setVenda_data(rs.getDate("venda_data"));

                    // Buscar objetos completos
                    PessoaDAO pessoaDAO = new PessoaDAO(con_venda);
                    FuncionarioDAO funcionarioDAO = new FuncionarioDAO(con_venda);
                    StatusVendaDAO statusVendaDAO = new StatusVendaDAO(con_venda);
                    TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO(con_venda);

                    venda.setVenda_pes_cpf(pessoaDAO.buscarPesCpf(rs.getString("venda_pes_cpf")));
                    venda.setVenda_func_cpf(funcionarioDAO.buscarPorId(rs.getInt("venda_fnc_id")));
                    venda.setVenda_statusVenda(statusVendaDAO.buscarPorId(rs.getInt("venda_statusVenda")));
                    venda.setVenda_tipoPagamento(tipoPagamentoDAO.buscarPorId(rs.getInt("venda_tipo_pagamento")));

                    vendas.add(venda);
                }
            }
        }
        return vendas;
    }

    public List<VendaVO> buscarVendasPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM tb_venda WHERE venda_pes_cpf = ?";
        List<VendaVO> vendas = new ArrayList<>();
        try (PreparedStatement pstmt = con_venda.prepareStatement(sql)) {
            pstmt.setString(1, cpf);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    VendaVO venda = new VendaVO();
                    venda.setVenda_id(rs.getInt("venda_id"));
                    venda.setVenda_data(rs.getDate("venda_data"));

                    // Mapeia o cliente, funcionario, status e tipo de pagamento
                    PessoaDAO pessoaDAO = new PessoaDAO(con_venda);
                    FuncionarioDAO funcionarioDAO = new FuncionarioDAO(con_venda);
                    StatusVendaDAO statusVendaDAO = new StatusVendaDAO(con_venda);
                    TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO(con_venda);

                    venda.setVenda_pes_cpf(pessoaDAO.buscarPesCpf(rs.getString("venda_pes_cpf")));
                    venda.setVenda_func_cpf(funcionarioDAO.buscarPorId(rs.getInt("venda_fnc_id")));
                    venda.setVenda_statusVenda(statusVendaDAO.buscarPorId(rs.getInt("venda_statusVenda")));
                    venda.setVenda_tipoPagamento(tipoPagamentoDAO.buscarPorId(rs.getInt("venda_tipo_pagamento")));

                    vendas.add(venda);
                }
            }
        }
        return vendas;
    }

    public List<VendaVO> buscarVendasPorNomeCliente(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_venda v JOIN tb_pessoa p ON v.venda_pes_cpf = p.pes_cpf WHERE p.pes_nome LIKE ?";
        List<VendaVO> vendas = new ArrayList<>();
        try (PreparedStatement pstmt = con_venda.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    VendaVO venda = new VendaVO();
                    venda.setVenda_id(rs.getInt("venda_id"));
                    venda.setVenda_data(rs.getDate("venda_data"));

                    // Mapeia o cliente, funcionario, status e tipo de pagamento
                    PessoaDAO pessoaDAO = new PessoaDAO(con_venda);
                    FuncionarioDAO funcionarioDAO = new FuncionarioDAO(con_venda);
                    StatusVendaDAO statusVendaDAO = new StatusVendaDAO(con_venda);
                    TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO(con_venda);

                    venda.setVenda_pes_cpf(pessoaDAO.buscarPesCpf(rs.getString("venda_pes_cpf")));
                    venda.setVenda_func_cpf(funcionarioDAO.buscarPorId(rs.getInt("venda_fnc_id")));
                    venda.setVenda_statusVenda(statusVendaDAO.buscarPorId(rs.getInt("venda_statusVenda")));
                    venda.setVenda_tipoPagamento(tipoPagamentoDAO.buscarPorId(rs.getInt("venda_tipo_pagamento")));

                    vendas.add(venda);
                }
            }
        }
        return vendas;
    }

    // soma total de vendas (itens) no período, opcionalmente filtrando por status
    public double somarTotalNoPeriodo(java.util.Date inicio, java.util.Date fim, Integer statusVendaId) throws SQLException {
        String base = "SELECT COALESCE(SUM(iv.itemVenda_qtd * iv.itemVenda_preco),0) AS total " +
                "FROM tb_venda v JOIN tb_itemVenda iv ON iv.itemVenda_venda_id = v.venda_id " +
                "WHERE v.venda_data BETWEEN ? AND ?";
        String sql = statusVendaId != null ? base + " AND v.venda_statusVenda = ?" : base;
        try (PreparedStatement ps = con_venda.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(inicio.getTime()));
            ps.setDate(2, new java.sql.Date(fim.getTime()));
            if (statusVendaId != null) ps.setInt(3, statusVendaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("total");
            }
        }
        return 0.0;
    }
}
