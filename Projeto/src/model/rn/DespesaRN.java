/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.dao.ConexaoDAO;
import model.dao.DespesaDAO;
import model.dao.TipoDespesaDAO;
import model.vo.DespesaVO;
import model.vo.TipoDespesaVO;

public class DespesaRN {

    // Métodos padrão: adicionar, atualizar, consultar por id, consultar por descrição (tipo), consultar todas

    public int adicionarDespesa(model.vo.DespesaVO despesa) throws Exception {
        java.sql.Connection con = null;
        try {
            if (despesa == null) {
                throw new Exception("Despesa não informada.");
            }

            if (despesa.getDespesa_descricao() == null || despesa.getDespesa_descricao().isBlank()) {
                throw new Exception("Descrição da despesa é obrigatória.");
            }

            if (despesa.getDespesa_dtRealizacao() == null) {
                throw new Exception("Data de realização é obrigatória.");
            }
            // valor: aceita zero, mas não nulo

            if (despesa.getDespesa_tipo() == null || despesa.getDespesa_tipo().getTipoDespesa_id() <= 0) {
                throw new Exception("Tipo de despesa é obrigatório.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.DespesaDAO dao = new model.dao.DespesaDAO(con);
            model.dao.TipoDespesaDAO tipoDAO = new model.dao.TipoDespesaDAO(con);

            if (tipoDAO.buscarPorId(despesa.getDespesa_tipo().getTipoDespesa_id()) == null) {
                throw new Exception("Tipo de despesa não encontrado: id=" + despesa.getDespesa_tipo().getTipoDespesa_id());
            }
            int id = dao.adicionarNovo(despesa);
            if (id <= 0) {
                throw new Exception("Falha ao inserir despesa.");
            }

            con.commit();
            return id;
        } catch (java.sql.SQLException e) {
            if (con != null) {
                con.rollback();
            }

            throw new Exception("Erro ao adicionar despesa: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public void atualizarDespesa(model.vo.DespesaVO despesa) throws Exception {
        java.sql.Connection con = null;
        try {
            if (despesa == null) {
                throw new Exception("Despesa não informada.");
            }

            if (despesa.getDespesa_id() <= 0) {
                throw new Exception("ID da despesa é obrigatório para atualização.");
            }

            if (despesa.getDespesa_descricao() == null || despesa.getDespesa_descricao().isBlank()) {
                throw new Exception("Descrição da despesa é obrigatória.");
            }

            if (despesa.getDespesa_dtRealizacao() == null) {
                throw new Exception("Data de realização é obrigatória.");
            }

            if (despesa.getDespesa_tipo() == null || despesa.getDespesa_tipo().getTipoDespesa_id() <= 0) {
                throw new Exception("Tipo de despesa é obrigatório.");
            }

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.DespesaDAO dao = new model.dao.DespesaDAO(con);
            model.dao.TipoDespesaDAO tipoDAO = new model.dao.TipoDespesaDAO(con);

            // garante existência
            model.vo.DespesaVO existente = dao.buscarPorId(despesa.getDespesa_id());
            if (existente == null) {
                throw new Exception("Despesa não encontrada para o ID: " + despesa.getDespesa_id());
            }

            if (tipoDAO.buscarPorId(despesa.getDespesa_tipo().getTipoDespesa_id()) == null) {
                throw new Exception("Tipo de despesa não encontrado: id=" + despesa.getDespesa_tipo().getTipoDespesa_id());
            }

            // atualiza todos os campos (menos id)
            dao.atualizarPorId(despesa);

            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) {
                con.rollback();
            }
            throw new Exception("Erro ao atualizar despesa: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public model.vo.DespesaVO buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido.");
        }
        
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.DespesaDAO dao = new model.dao.DespesaDAO(con);
            return dao.buscarPorId(id);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar despesa: " + e.getMessage(), e);
        }
    }

    public java.util.List<model.vo.DespesaVO> buscarPorDescricao(String termo) throws Exception {
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.DespesaDAO dao = new model.dao.DespesaDAO(con);
            return dao.buscarPorDescricaoLike(termo);
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar despesas por descrição: " + e.getMessage(), e);
        }
    }

    public java.util.List<model.vo.DespesaVO> buscarTodas() throws Exception {
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.DespesaDAO dao = new model.dao.DespesaDAO(con);
            return dao.buscarTodas();
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao listar despesas: " + e.getMessage(), e);
        }
    }

    public List<TipoDespesaVO> listarTiposDespesaRN() throws Exception {
        try (java.sql.Connection con = ConexaoDAO.getConexao()) {
            TipoDespesaDAO tipoDespesaDAO = new TipoDespesaDAO(con);
            return tipoDespesaDAO.listarTodosTiposDespesa();
        } catch (Exception e) {
            throw new Exception("DESPESARN: erro ao listar tipos de despesa. " + e.getMessage(), e);
        }
    }

    public void removerDespesa(int despesaId) throws Exception {
        if (despesaId <= 0) {
            throw new Exception("ID inválido para exclusão.");
        }

        try (Connection con = ConexaoDAO.getConexao()) {
            DespesaDAO dao = new DespesaDAO(con);
            DespesaVO existente = dao.buscarPorId(despesaId);
            if (existente == null) {
                throw new Exception("Despesa não encontrada para o ID: " + despesaId);
            }
            dao.excluirPorId(despesaId);
        } catch (SQLException e) {
            throw new Exception("Erro ao excluir despesa: " + e.getMessage(), e);
        }
    }

}
