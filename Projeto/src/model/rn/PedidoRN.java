/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

public class PedidoRN {

    // criar pedido (vinculado a uma venda existente)
    public int criarPedido(model.vo.PedidoVO pedido) throws Exception {
        java.sql.Connection con = null;
        try {
            if (pedido == null) throw new Exception("Pedido não informado.");
            if (pedido.getPedido_statusPedido() == null || pedido.getPedido_statusPedido().getStatusPedido_id() <= 0) {
                throw new Exception("Status do pedido é obrigatório e deve ser válido.");
            }
            if (pedido.getPedido_tipoEntrega() == null || pedido.getPedido_tipoEntrega().getTipoEntrega_id() <= 0) {
                throw new Exception("Tipo de entrega é obrigatório e deve ser válido.");
            }
            if (pedido.getPedido_venda_id() == null || pedido.getPedido_venda_id().getVenda_id() <= 0) {
                throw new Exception("Venda vinculada ao pedido é obrigatória e deve ser válida.");
            }
            if (pedido.getPedido_fnc_id() == null || pedido.getPedido_fnc_id().getFnc_id() <= 0) {
                throw new Exception("Funcionário responsável é obrigatório e deve ser válido.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            // DAOs relacionados
            model.dao.StatusPedidoDAO statusDAO = new model.dao.StatusPedidoDAO(con);
            model.dao.TipoEntregaDAO tipoEntDAO = new model.dao.TipoEntregaDAO(con);
            model.dao.VendaDAO vendaDAO = new model.dao.VendaDAO(con);
            model.dao.FuncionarioDAO funcDAO = new model.dao.FuncionarioDAO(con);
            model.dao.PedidoDAO pedidoDAO = new model.dao.PedidoDAO(con);

            // valida chaves
            if (statusDAO.buscarPorId(pedido.getPedido_statusPedido().getStatusPedido_id()) == null) {
                throw new Exception("Status de pedido não encontrado.");
            }
            if (tipoEntDAO.buscarPorId(pedido.getPedido_tipoEntrega().getTipoEntrega_id()) == null) {
                throw new Exception("Tipo de entrega não encontrado.");
            }
            if (vendaDAO.buscarPorId(pedido.getPedido_venda_id().getVenda_id()) == null) {
                throw new Exception("Venda não encontrada para o ID informado.");
            }
            if (funcDAO.buscarPorId(pedido.getPedido_fnc_id().getFnc_id()) == null) {
                throw new Exception("Funcionário não encontrado para o ID informado.");
            }

            int id = pedidoDAO.adicionarNovo(pedido);
            if (id <= 0) throw new Exception("Falha ao inserir pedido.");
            pedido.setPedido_id(id);

            con.commit();
            return id;
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao criar pedido: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // atualizar todos os campos do pedido (mantendo o ID)
    public void atualizarPedido(model.vo.PedidoVO pedido) throws Exception {
        java.sql.Connection con = null;
        try {
            if (pedido == null) throw new Exception("Pedido não informado.");
            if (pedido.getPedido_id() <= 0) throw new Exception("ID do pedido é obrigatório para atualização.");
            if (pedido.getPedido_statusPedido() == null || pedido.getPedido_statusPedido().getStatusPedido_id() <= 0)
                throw new Exception("Status do pedido inválido.");
            if (pedido.getPedido_tipoEntrega() == null || pedido.getPedido_tipoEntrega().getTipoEntrega_id() <= 0)
                throw new Exception("Tipo de entrega inválido.");
            if (pedido.getPedido_venda_id() == null || pedido.getPedido_venda_id().getVenda_id() <= 0)
                throw new Exception("Venda inválida.");
            if (pedido.getPedido_fnc_id() == null || pedido.getPedido_fnc_id().getFnc_id() <= 0)
                throw new Exception("Funcionário inválido.");

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.PedidoDAO pedidoDAO = new model.dao.PedidoDAO(con);
            model.dao.PedidoDAO checkDAO = pedidoDAO;
            if (checkDAO.buscarPorId(pedido.getPedido_id()) == null) {
                throw new Exception("Pedido não encontrado para o ID: " + pedido.getPedido_id());
            }

            // validar FKs
            model.dao.StatusPedidoDAO statusDAO = new model.dao.StatusPedidoDAO(con);
            model.dao.TipoEntregaDAO tipoEntDAO = new model.dao.TipoEntregaDAO(con);
            model.dao.VendaDAO vendaDAO = new model.dao.VendaDAO(con);
            model.dao.FuncionarioDAO funcDAO = new model.dao.FuncionarioDAO(con);
            if (statusDAO.buscarPorId(pedido.getPedido_statusPedido().getStatusPedido_id()) == null)
                throw new Exception("Status de pedido não encontrado.");
            if (tipoEntDAO.buscarPorId(pedido.getPedido_tipoEntrega().getTipoEntrega_id()) == null)
                throw new Exception("Tipo de entrega não encontrado.");
            if (vendaDAO.buscarPorId(pedido.getPedido_venda_id().getVenda_id()) == null)
                throw new Exception("Venda não encontrada.");
            if (funcDAO.buscarPorId(pedido.getPedido_fnc_id().getFnc_id()) == null)
                throw new Exception("Funcionário não encontrado.");

            pedidoDAO.atualizar(pedido);
            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao atualizar pedido: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // atualizar somente o status do pedido, preservando demais campos
    public void atualizarStatus(int pedidoId, int statusId) throws Exception {
        java.sql.Connection con = null;
        try {
            if (pedidoId <= 0) throw new Exception("ID do pedido inválido.");
            if (statusId <= 0) throw new Exception("ID do status inválido.");

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.PedidoDAO pedidoDAO = new model.dao.PedidoDAO(con);
            model.vo.PedidoVO pedido = pedidoDAO.buscarPorId(pedidoId);
            if (pedido == null) throw new Exception("Pedido não encontrado.");

            // valida status
            model.dao.StatusPedidoDAO statusDAO = new model.dao.StatusPedidoDAO(con);
            model.vo.StatusPedidoVO novoStatus = statusDAO.buscarPorId(statusId);
            if (novoStatus == null) throw new Exception("Status de pedido não encontrado.");

            // regra simples de transição (poderia ser expandida)
            pedido.setPedido_statusPedido(novoStatus);
            pedidoDAO.atualizar(pedido);

            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao atualizar status do pedido: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    public model.vo.PedidoVO buscarPorId(int id) throws Exception {
        if (id <= 0) throw new Exception("ID inválido.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.PedidoDAO dao = new model.dao.PedidoDAO(con);
            return dao.buscarPorId(id);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar pedido: " + e.getMessage(), e);
        }
    }

    public java.util.List<model.vo.PedidoVO> buscarPorFuncionario(int fncId) throws Exception {
        if (fncId <= 0) throw new Exception("ID do funcionário inválido.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.PedidoDAO dao = new model.dao.PedidoDAO(con);
            return dao.buscarPorFuncionario(fncId);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar pedidos por funcionário: " + e.getMessage(), e);
        }
    }
}
