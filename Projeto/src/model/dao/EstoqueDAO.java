/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.EstoqueVO;
import model.vo.ProdutoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO {
    private Connection con_estoque;
    private ProdutoDAO produtoDAO;

    public EstoqueDAO(Connection con_estoque) {
        this.con_estoque = con_estoque;
        this.produtoDAO = new ProdutoDAO(con_estoque);
    }

    // adicionar novo estoque
    public int adicionarNovo(EstoqueVO estoque) throws SQLException {
        String sql = "INSERT INTO tb_estoque (est_dtCompra, est_produto_id, est_custo, est_qtdToal, est_qtdMin, est_qtdMax, est_lote, est_dtValidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement estoque_add = con_estoque.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            estoque_add.setDate(1, toSqlDate(estoque.getEst_dtCompra()));
            estoque_add.setInt(2, estoque.getEst_produto_id().getProduto_id());
            estoque_add.setDouble(3, estoque.getEst_custo());
            estoque_add.setDouble(4, estoque.getQtdTotal());
            estoque_add.setDouble(5, estoque.getEst_qtdMin());
            estoque_add.setDouble(6, estoque.getEst_qtdMax());
            estoque_add.setString(7, estoque.getEst_lote());
            estoque_add.setDate(8, toSqlDate(estoque.getEst_dtValidade()));
            estoque_add.executeUpdate();
            try (ResultSet rs = estoque_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // update estoque por id
    public void atualizarPorId(EstoqueVO estoque) throws SQLException {
        String sql = "UPDATE tb_estoque SET est_dtCompra = ?, est_produto_id = ?, est_custo = ?, est_qtdToal = ?, est_qtdMin = ?, est_qtdMax = ?, est_lote = ?, est_dtValidade = ? WHERE est_id = ?";
        try (PreparedStatement estoque_att_id = con_estoque.prepareStatement(sql)) {
            estoque_att_id.setDate(1, toSqlDate(estoque.getEst_dtCompra()));
            estoque_att_id.setInt(2, estoque.getEst_produto_id().getProduto_id());
            estoque_att_id.setDouble(3, estoque.getEst_custo());
            estoque_att_id.setDouble(4, estoque.getQtdTotal());
            estoque_att_id.setDouble(5, estoque.getEst_qtdMin());
            estoque_att_id.setDouble(6, estoque.getEst_qtdMax());
            estoque_att_id.setString(7, estoque.getEst_lote());
            estoque_att_id.setDate(8, toSqlDate(estoque.getEst_dtValidade()));
            estoque_att_id.setInt(9, estoque.getEst_id());
            estoque_att_id.executeUpdate();
        }
    }

    // update estoque por lote
    public void atualizarPorLote(String loteAntigo, EstoqueVO estoqueNovo) throws SQLException {
        String sql = "UPDATE tb_estoque SET est_dtCompra = ?, est_produto_id = ?, est_custo = ?, est_qtdToal = ?, est_qtdMin = ?, est_qtdMax = ?, est_lote = ?, est_dtValidade = ? WHERE est_lote = ?";
        try (PreparedStatement estoque_att_lote = con_estoque.prepareStatement(sql)) {
            estoque_att_lote.setDate(1, toSqlDate(estoqueNovo.getEst_dtCompra()));
            estoque_att_lote.setInt(2, estoqueNovo.getEst_produto_id().getProduto_id());
            estoque_att_lote.setDouble(3, estoqueNovo.getEst_custo());
            estoque_att_lote.setDouble(4, estoqueNovo.getQtdTotal());
            estoque_att_lote.setDouble(5, estoqueNovo.getEst_qtdMin());
            estoque_att_lote.setDouble(6, estoqueNovo.getEst_qtdMax());
            estoque_att_lote.setString(7, estoqueNovo.getEst_lote());
            estoque_att_lote.setDate(8, toSqlDate(estoqueNovo.getEst_dtValidade()));
            estoque_att_lote.setString(9, loteAntigo);
            estoque_att_lote.executeUpdate();
        }
    }

    // busca estoque por id
    public EstoqueVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_estoque WHERE est_id = ?";
        EstoqueVO estoque = null;
        try (PreparedStatement estoque_bsc_id = con_estoque.prepareStatement(sql)) {
            estoque_bsc_id.setInt(1, id);
            try (ResultSet rs = estoque_bsc_id.executeQuery()) {
                if (rs.next()) {
                    estoque = new EstoqueVO();
                    estoque.setEst_id(rs.getInt("est_id"));
                    estoque.setEst_dtCompra(toLocalDate(rs.getDate("est_dtCompra")));
                    estoque.setEst_custo(rs.getDouble("est_custo"));
                    estoque.setQtdTotal(rs.getDouble("est_qtdToal"));
                    estoque.setEst_qtdMin(rs.getDouble("est_qtdMin"));
                    estoque.setEst_qtdMax(rs.getDouble("est_qtdMax"));
                    estoque.setEst_lote(rs.getString("est_lote"));
                    estoque.setEst_dtValidade(toLocalDate(rs.getDate("est_dtValidade")));

                    // Buscando objetos relacionados
                    ProdutoVO produto = produtoDAO.buscarPorId(rs.getInt("est_produto_id"));
                    estoque.setEst_produto_id(produto);
                }
            }
        }
        return estoque;
    }

    // busca estoque por lote
    public EstoqueVO buscarPorLote(String lote) throws SQLException {
        String sql = "SELECT * FROM tb_estoque WHERE est_lote = ?";
        EstoqueVO estoque = null;
        try (PreparedStatement estoque_bsc_lote = con_estoque.prepareStatement(sql)) {
            estoque_bsc_lote.setString(1, lote);
            try (ResultSet rs = estoque_bsc_lote.executeQuery()) {
                if (rs.next()) {
                    estoque = new EstoqueVO();
                    estoque.setEst_id(rs.getInt("est_id"));
                    estoque.setEst_dtCompra(toLocalDate(rs.getDate("est_dtCompra")));
                    estoque.setEst_custo(rs.getDouble("est_custo"));
                    estoque.setQtdTotal(rs.getDouble("est_qtdToal"));
                    estoque.setEst_qtdMin(rs.getDouble("est_qtdMin"));
                    estoque.setEst_qtdMax(rs.getDouble("est_qtdMax"));
                    estoque.setEst_lote(rs.getString("est_lote"));
                    estoque.setEst_dtValidade(toLocalDate(rs.getDate("est_dtValidade")));

                    // Buscando objetos relacionados
                    ProdutoVO produto = produtoDAO.buscarPorId(rs.getInt("est_produto_id"));
                    estoque.setEst_produto_id(produto);
                }
            }
        }
        return estoque;
    }

    // lista entradas de estoque por produto (apenas com saldo) em ordem FIFO
    public List<EstoqueVO> listarPorProdutoOrdenado(int produtoId) throws SQLException {
        String sql = "SELECT * FROM tb_estoque WHERE est_produto_id = ? AND est_qtdToal > 0 ORDER BY est_dtCompra ASC, est_dtValidade ASC, est_id ASC";
        List<EstoqueVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_estoque.prepareStatement(sql)) {
            ps.setInt(1, produtoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EstoqueVO e = new EstoqueVO();
                    e.setEst_id(rs.getInt("est_id"));
                    e.setEst_dtCompra(toLocalDate(rs.getDate("est_dtCompra")));
                    e.setEst_custo(rs.getDouble("est_custo"));
                    e.setQtdTotal(rs.getDouble("est_qtdToal"));
                    e.setEst_qtdMin(rs.getDouble("est_qtdMin"));
                    e.setEst_qtdMax(rs.getDouble("est_qtdMax"));
                    e.setEst_lote(rs.getString("est_lote"));
                    e.setEst_dtValidade(toLocalDate(rs.getDate("est_dtValidade")));
                    e.setEst_produto_id(produtoDAO.buscarPorId(rs.getInt("est_produto_id")));
                    lista.add(e);
                }
            }
        }
        return lista;
    }

    // soma do saldo disponível por produto
    public double somarSaldoPorProduto(int produtoId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(est_qtdToal),0) AS saldo FROM tb_estoque WHERE est_produto_id = ?";
        try (PreparedStatement ps = con_estoque.prepareStatement(sql)) {
            ps.setInt(1, produtoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo");
                }
            }
        }
        return 0.0;
    }

    // lista todos os registros de estoque
    public List<EstoqueVO> listarTodos() throws SQLException {
        String sql = "SELECT * FROM tb_estoque ORDER BY est_dtCompra ASC, est_id ASC";
        List<EstoqueVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_estoque.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EstoqueVO e = new EstoqueVO();
                    e.setEst_id(rs.getInt("est_id"));
                    e.setEst_dtCompra(toLocalDate(rs.getDate("est_dtCompra")));
                    e.setEst_custo(rs.getDouble("est_custo"));
                    e.setQtdTotal(rs.getDouble("est_qtdToal"));
                    e.setEst_qtdMin(rs.getDouble("est_qtdMin"));
                    e.setEst_qtdMax(rs.getDouble("est_qtdMax"));
                    e.setEst_lote(rs.getString("est_lote"));
                    e.setEst_dtValidade(toLocalDate(rs.getDate("est_dtValidade")));
                    e.setEst_produto_id(produtoDAO.buscarPorId(rs.getInt("est_produto_id")));
                    lista.add(e);
                }
            }
        }
        return lista;
    }

    // lista lotes vencidos (validade < data)
    public List<EstoqueVO> listarVencidos(java.util.Date data) throws SQLException {
        String sql = "SELECT * FROM tb_estoque WHERE est_dtValidade < ? ORDER BY est_dtValidade ASC";
        List<EstoqueVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_estoque.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(data.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EstoqueVO e = new EstoqueVO();
                    e.setEst_id(rs.getInt("est_id"));
                    e.setEst_dtCompra(toLocalDate(rs.getDate("est_dtCompra")));
                    e.setEst_custo(rs.getDouble("est_custo"));
                    e.setQtdTotal(rs.getDouble("est_qtdToal"));
                    e.setEst_qtdMin(rs.getDouble("est_qtdMin"));
                    e.setEst_qtdMax(rs.getDouble("est_qtdMax"));
                    e.setEst_lote(rs.getString("est_lote"));
                    e.setEst_dtValidade(toLocalDate(rs.getDate("est_dtValidade")));
                    e.setEst_produto_id(produtoDAO.buscarPorId(rs.getInt("est_produto_id")));
                    lista.add(e);
                }
            }
        }
        return lista;
    }

    // lista lotes com validade até a data limite (inclusive)
    public List<EstoqueVO> listarVencendoAte(java.util.Date limite) throws SQLException {
        String sql = "SELECT * FROM tb_estoque WHERE est_dtValidade <= ? ORDER BY est_dtValidade ASC";
        List<EstoqueVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_estoque.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(limite.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EstoqueVO e = new EstoqueVO();
                    e.setEst_id(rs.getInt("est_id"));
                    e.setEst_dtCompra(toLocalDate(rs.getDate("est_dtCompra")));
                    e.setEst_custo(rs.getDouble("est_custo"));
                    e.setQtdTotal(rs.getDouble("est_qtdToal"));
                    e.setEst_qtdMin(rs.getDouble("est_qtdMin"));
                    e.setEst_qtdMax(rs.getDouble("est_qtdMax"));
                    e.setEst_lote(rs.getString("est_lote"));
                    e.setEst_dtValidade(toLocalDate(rs.getDate("est_dtValidade")));
                    e.setEst_produto_id(produtoDAO.buscarPorId(rs.getInt("est_produto_id")));
                    lista.add(e);
                }
            }
        }
        return lista;
    }

    private java.sql.Date toSqlDate(LocalDate data) {
        if (data == null) {
            return null;
        }
        return java.sql.Date.valueOf(data);
    }

    private LocalDate toLocalDate(java.sql.Date data) {
        if (data == null) {
            return null;
        }
        return data.toLocalDate();
    }
}
