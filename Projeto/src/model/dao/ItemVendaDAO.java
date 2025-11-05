package model.dao;

import model.vo.ItemVendaVO;
import model.vo.VendaVO;
import model.vo.ProdutoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaDAO {

    private Connection con_itemVenda;

    public ItemVendaDAO(Connection con_itemVenda) {
        this.con_itemVenda = con_itemVenda;
    }

    // adicionar novo item de venda
    public void adicionarNovo(ItemVendaVO itemVenda) throws SQLException {
        String sql = "INSERT INTO tb_itemVenda (itemVenda_venda_id, itemVenda_produto_id, itemVenda_qtd, itemVenda_preco) VALUES (?, ?, ?, ?)";
        try (PreparedStatement itemVenda_add = con_itemVenda.prepareStatement(sql)) {
            itemVenda_add.setInt(1, itemVenda.getItemVenda_venda_id().getVenda_id());
            itemVenda_add.setInt(2, itemVenda.getItemVenda_produto_id().getProduto_id());
            itemVenda_add.setDouble(3, itemVenda.getItemVenda_qtd());
            itemVenda_add.setDouble(4, itemVenda.getItemVenda_preco());
            itemVenda_add.executeUpdate();
        }
    }

    // busca item de venda por id
    public ItemVendaVO buscarPorId(int vendaId, int produtoId) throws SQLException {
        String sql = "SELECT * FROM tb_itemVenda WHERE itemVenda_venda_id = ? AND itemVenda_produto_id = ?";
        ItemVendaVO itemVenda = null;
        try (PreparedStatement itemVenda_bsc = con_itemVenda.prepareStatement(sql)) {
            itemVenda_bsc.setInt(1, vendaId);
            itemVenda_bsc.setInt(2, produtoId);
            try (ResultSet rs = itemVenda_bsc.executeQuery()) {
                if (rs.next()) {
                    itemVenda = new ItemVendaVO();
                    itemVenda.setItemVenda_venda_id(new VendaDAO(con_itemVenda).buscarPorId(rs.getInt("itemVenda_venda_id")));
                    itemVenda.setItemVenda_produto_id(new ProdutoDAO(con_itemVenda).buscarPorId(rs.getInt("itemVenda_produto_id")));
                    itemVenda.setItemVenda_qtd(rs.getDouble("itemVenda_qtd"));
                    itemVenda.setItemVenda_preco(rs.getDouble("itemVenda_preco"));
                }
            }
        }
        return itemVenda;
    }

    // busca todos os itens de venda de uma venda
    public List<ItemVendaVO> buscarPorVenda(int vendaId) throws SQLException {
        String sql = "SELECT * FROM tb_itemVenda WHERE itemVenda_venda_id = ?";
        List<ItemVendaVO> itens = new ArrayList<>();
        try (PreparedStatement itemVenda_bsc = con_itemVenda.prepareStatement(sql)) {
            itemVenda_bsc.setInt(1, vendaId);
            try (ResultSet rs = itemVenda_bsc.executeQuery()) {
                VendaDAO vendaDAO = new VendaDAO(con_itemVenda);
                ProdutoDAO produtoDAO = new ProdutoDAO(con_itemVenda);
                while (rs.next()) {
                    ItemVendaVO itemVenda = new ItemVendaVO();
                    itemVenda.setItemVenda_venda_id(vendaDAO.buscarPorId(rs.getInt("itemVenda_venda_id")));
                    itemVenda.setItemVenda_produto_id(produtoDAO.buscarPorId(rs.getInt("itemVenda_produto_id")));
                    itemVenda.setItemVenda_qtd(rs.getDouble("itemVenda_qtd"));
                    itemVenda.setItemVenda_preco(rs.getDouble("itemVenda_preco"));
                    itens.add(itemVenda);
                }
            }
        }
        return itens;
    }

    // update item de venda
    public void atualizarItemVenda(ItemVendaVO itemVenda) throws SQLException {
        String sql = "UPDATE tb_itemVenda SET itemVenda_qtd = ?, itemVenda_preco = ? WHERE itemVenda_venda_id = ? AND itemVenda_produto_id = ?";
        try (PreparedStatement itemVenda_att = con_itemVenda.prepareStatement(sql)) {
            itemVenda_att.setDouble(1, itemVenda.getItemVenda_qtd());
            itemVenda_att.setDouble(2, itemVenda.getItemVenda_preco());
            itemVenda_att.setInt(3, itemVenda.getItemVenda_venda_id().getVenda_id());
            itemVenda_att.setInt(4, itemVenda.getItemVenda_produto_id().getProduto_id());
            itemVenda_att.executeUpdate();
        }
    }
}
