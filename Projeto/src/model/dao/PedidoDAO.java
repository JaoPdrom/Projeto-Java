package model.dao;

import model.vo.FuncionarioVO;
import model.vo.PedidoVO;
import model.vo.StatusPedidoVO;
import model.vo.TipoEntregaVO;
import model.vo.VendaVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private Connection con_pedido;
    private StatusPedidoDAO statusPedidoDAO;
    private TipoEntregaDAO tipoEntregaDAO;
    private VendaDAO vendaDAO;
    private FuncionarioDAO funcionarioDAO;

    public PedidoDAO(Connection con_pedido) throws SQLException {
        this.con_pedido = con_pedido;
        this.statusPedidoDAO = new StatusPedidoDAO(con_pedido);
        this.tipoEntregaDAO = new TipoEntregaDAO(con_pedido);
        this.vendaDAO = new VendaDAO(con_pedido);
        this.funcionarioDAO = new FuncionarioDAO(con_pedido);
    }

    // adicionar novo pedido
    public int adicionarNovo(PedidoVO pedido) throws SQLException {
        String sql = "INSERT INTO tb_pedido (pedido_statusPedido, pedido_tipoEntrega, pedido_venda_id, pedido_fnc_id) VALUES (?, ?, ?, ?)";
        int pedidoId = -1;

        try (PreparedStatement pedido_add = con_pedido.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pedido_add.setInt(1, pedido.getPedido_statusPedido().getStatusPedido_id());
            pedido_add.setInt(2, pedido.getPedido_tipoEntrega().getTipoEntrega_id());
            pedido_add.setInt(3, pedido.getPedido_venda_id().getVenda_id());
            pedido_add.setInt(4, pedido.getPedido_fnc_id().getFnc_id());
            pedido_add.executeUpdate();

            try (ResultSet rs = pedido_add.getGeneratedKeys()) {
                if (rs.next()) {
                    pedidoId = rs.getInt(1);
                }
            }
        }
        return pedidoId;
    }

    // update pedido por id
    public void atualizar(PedidoVO pedido) throws SQLException {
        String sql = "UPDATE tb_pedido SET pedido_statusPedido = ?, pedido_tipoEntrega = ?, pedido_venda_id = ?, pedido_fnc_id = ? WHERE pedido_id = ?";
        try (PreparedStatement pedido_att = con_pedido.prepareStatement(sql)) {
            pedido_att.setInt(1, pedido.getPedido_statusPedido().getStatusPedido_id());
            pedido_att.setInt(2, pedido.getPedido_tipoEntrega().getTipoEntrega_id());
            pedido_att.setInt(3, pedido.getPedido_venda_id().getVenda_id());
            pedido_att.setInt(4, pedido.getPedido_fnc_id().getFnc_id());
            pedido_att.setInt(5, pedido.getPedido_id());
            pedido_att.executeUpdate();
        }
    }

    // busca pedido por id
    public PedidoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_pedido WHERE pedido_id = ?";
        PedidoVO pedido = null;
        try (PreparedStatement pedido_bsc = con_pedido.prepareStatement(sql)) {
            pedido_bsc.setInt(1, id);
            try (ResultSet rs = pedido_bsc.executeQuery()) {
                if (rs.next()) {
                    pedido = new PedidoVO();
                    pedido.setPedido_id(rs.getInt("pedido_id"));
                    pedido.setPedido_statusPedido(statusPedidoDAO.buscarPorId(rs.getInt("pedido_statusPedido")));
                    pedido.setPedido_tipoEntrega(tipoEntregaDAO.buscarPorId(rs.getInt("pedido_tipoEntrega")));
                    pedido.setPedido_venda_id(vendaDAO.buscarPorId(rs.getInt("pedido_venda_id")));
                    pedido.setPedido_fnc_id(funcionarioDAO.buscarPorId(rs.getInt("pedido_fnc_id")));
                }
            }
        }
        return pedido;
    }

    // busca pedido por funcionario
    public List<PedidoVO> buscarPorFuncionario(int fncId) throws SQLException {
        String sql = "SELECT * FROM tb_pedido WHERE pedido_fnc_id = ?";
        List<PedidoVO> pedidos = new ArrayList<>();
        try (PreparedStatement pedido_bsc = con_pedido.prepareStatement(sql)) {
            pedido_bsc.setInt(1, fncId);
            try (ResultSet rs = pedido_bsc.executeQuery()) {
                while (rs.next()) {
                    PedidoVO pedido = new PedidoVO();
                    pedido.setPedido_id(rs.getInt("pedido_id"));
                    pedido.setPedido_statusPedido(statusPedidoDAO.buscarPorId(rs.getInt("pedido_statusPedido")));
                    pedido.setPedido_tipoEntrega(tipoEntregaDAO.buscarPorId(rs.getInt("pedido_tipoEntrega")));
                    pedido.setPedido_venda_id(vendaDAO.buscarPorId(rs.getInt("pedido_venda_id")));
                    pedido.setPedido_fnc_id(funcionarioDAO.buscarPorId(rs.getInt("pedido_fnc_id")));
                    pedidos.add(pedido);
                }
            }
        }
        return pedidos;
    }
}
