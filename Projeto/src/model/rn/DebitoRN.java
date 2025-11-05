/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

public class DebitoRN {

    // cria débito para uma venda (status padrão: EM_ABERTO)
    public int criarDebitoParaVenda(model.vo.VendaVO venda, java.lang.Double juros) throws Exception {
        if (venda == null || venda.getVenda_id() <= 0) throw new Exception("Venda inválida para débito.");
        java.sql.Connection con = null;
        try {
            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.StatusDebitoDAO statusDAO = new model.dao.StatusDebitoDAO(con);
            model.dao.DebitoDAO debitoDAO = new model.dao.DebitoDAO(con);

            // busca status EM_ABERTO (ou equivalente). Caso não exista, erro claro
            model.vo.StatusDebitoVO status = statusDAO.buscarPorDescricao("EM_ABERTO");
            if (status == null) {
                throw new Exception("Status de débito 'EM_ABERTO' não encontrado. Cadastre em tb_statusDebito.");
            }

            model.vo.DebitoVO deb = new model.vo.DebitoVO();
            deb.setDeb_venda_id(venda);
            deb.setDeb_juros(juros);
            deb.setDeb_status(status);

            int id = debitoDAO.adicionarNovo(deb);
            if (id <= 0) {
                throw new Exception("Falha ao inserir débito.");
            }

            con.commit();
            return id;
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao criar débito: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // marcar débito como pago
    public void quitarDebito(int debitoId) throws Exception {
        java.sql.Connection con = null;
        try {
            if (debitoId <= 0) {
                throw new Exception("ID de débito inválido.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.DebitoDAO debitoDAO = new model.dao.DebitoDAO(con);
            model.dao.StatusDebitoDAO statusDAO = new model.dao.StatusDebitoDAO(con);

            model.vo.DebitoVO existente = debitoDAO.buscarPorId(debitoId);
            if (existente == null) {
                throw new Exception("Débito não encontrado.");
            }

            model.vo.StatusDebitoVO pago = statusDAO.buscarPorDescricao("PAGO");
            if (pago == null){
                throw new Exception("Status de débito 'PAGO' não encontrado.");
            } 

            debitoDAO.atualizarStatus(debitoId, pago.getStatusDeb_id());
            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao quitar débito: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // atualizar juros (renegociação)
    public void atualizarJuros(int debitoId, java.lang.Double juros) throws Exception {
        java.sql.Connection con = null;
        try {
            if (debitoId <= 0) {
                throw new Exception("ID de débito inválido.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.DebitoDAO debitoDAO = new model.dao.DebitoDAO(con);
            if (debitoDAO.buscarPorId(debitoId) == null){
                throw new Exception("Débito não encontrado.");
            } 

            debitoDAO.atualizarJuros(debitoId, juros);
            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao atualizar juros do débito: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    public model.vo.DebitoVO buscarPorVenda(int vendaId) throws Exception {
        if (vendaId <= 0){
            throw new Exception("ID de venda inválido.");  
        } 
        
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.DebitoDAO dao = new model.dao.DebitoDAO(con);
            return dao.buscarPorVenda(vendaId);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar débito por venda: " + e.getMessage(), e);
        }
    }

    public java.util.List<model.vo.DebitoVO> listarPorCpfCliente(String cpf, Integer statusId) throws Exception {
        if (cpf == null || cpf.isBlank()) {
            throw new Exception("CPF é obrigatório.");
        }
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.DebitoDAO dao = new model.dao.DebitoDAO(con);
            return dao.listarPorCpfCliente(cpf, statusId);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao listar débitos por CPF: " + e.getMessage(), e);
        }
    }
}
