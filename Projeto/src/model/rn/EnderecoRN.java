package model.rn;

import model.dao.*;
import model.vo.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EnderecoRN {

    public List<EstadoVO> listarEstados() throws Exception {
        try (Connection con = ConexaoDAO.getConexao()) {
            EstadoDAO dao = new EstadoDAO(con);
            return dao.buscarTodosEstados();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar estados: " + e.getMessage(), e);
        }
    }

    public List<CidadeVO> listarCidadesPorEstado(String estSigla) throws Exception {
        if (estSigla == null || estSigla.isBlank()) {
            throw new Exception("Sigla do estado obrigatória.");
        }
        try (Connection con = ConexaoDAO.getConexao()) {
            CidadeDAO dao = new CidadeDAO(con);
            return dao.listarPorEstado(estSigla);
        } catch (SQLException e) {
            throw new Exception("Erro ao listar cidades por estado: " + e.getMessage(), e);
        }
    }

    public List<BairroVO> listarBairros() throws Exception {
        try (Connection con = ConexaoDAO.getConexao()) {
            BairroDAO dao = new BairroDAO(con);
            return dao.buscarTodosBairros();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar bairros: " + e.getMessage(), e);
        }
    }

    public List<LogradouroVO> listarLogradouros() throws Exception {
        try (Connection con = ConexaoDAO.getConexao()) {
            LogradouroDAO dao = new LogradouroDAO(con);
            return dao.buscarTodosLogradouros();
        } catch (SQLException e) {
            throw new Exception("Erro ao listar logradouros: " + e.getMessage(), e);
        }
    }

    public EndPostalVO buscarEndPostalPorCep(String cep) throws Exception {
        if (cep == null || cep.isBlank()) {
            throw new Exception("CEP obrigatório.");
        }
        try (Connection con = ConexaoDAO.getConexao()) {
            EndPostalDAO dao = new EndPostalDAO(con);
            return dao.buscarPorCep(cep);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar EndPostal por CEP: " + e.getMessage(), e);
        }
    }

    public int criarOuObterEndPostal(EndPostalVO endPostal) throws Exception {
        try (Connection con = ConexaoDAO.getConexao()) {
            return criarOuObterEndPostal(con, endPostal);
        } catch (SQLException e) {
            throw new Exception("Erro ao criar/obter EndPostal: " + e.getMessage(), e);
        }
    }

    public int adicionarEndereco(String documento, EnderecoVO endereco) throws Exception {
        if (documento == null || documento.isBlank()) {
            throw new Exception("Documento obrigatório.");
        }
        if (endereco == null || endereco.getEnd_endP_id() == null) {
            throw new Exception("Endereço e EndPostal obrigatórios.");
        }

        Connection con = null;
        try {
            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            // garante EndPostal com ID
            if (endereco.getEnd_endP_id().getEndP_id() == 0) {
                int endPId = criarOuObterEndPostal(con, endereco.getEnd_endP_id());
                endereco.getEnd_endP_id().setEndP_id(endPId);
            }

            EnderecoDAO endDAO = new EnderecoDAO(con);
            int endId = endDAO.adicionarNovo(endereco);
            if (endId <= 0) {
                throw new Exception("Falha ao inserir endereço.");
            }

            PesEndDAO pesEndDAO = new PesEndDAO(con);
            pesEndDAO.vincular(documento, endId);

            con.commit();
            return endId;
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ignore) {}
            throw new Exception("Erro ao adicionar endereço: " + e.getMessage(), e);
        } finally {
            if (con != null) try { con.close(); } catch (SQLException ignore) {}
        }
    }

    public void atualizarEndereco(EnderecoVO endereco) throws Exception {
        if (endereco == null || endereco.getEnd_id() == 0) {
            throw new Exception("Endereço com ID obrigatório para atualização.");
        }

        Connection con = null;
        try {
            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            if (endereco.getEnd_endP_id() != null && endereco.getEnd_endP_id().getEndP_id() == 0) {
                int endPId = criarOuObterEndPostal(con, endereco.getEnd_endP_id());
                endereco.getEnd_endP_id().setEndP_id(endPId);
            }

            EnderecoDAO endDAO = new EnderecoDAO(con);
            endDAO.atualizar(endereco);

            con.commit();
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ignore) {}
            throw new Exception("Erro ao atualizar endereço: " + e.getMessage(), e);
        } finally {
            if (con != null) try { con.close(); } catch (SQLException ignore) {}
        }
    }

    public List<EnderecoVO> listarEnderecosPorPessoa(String documento) throws Exception {
        if (documento == null || documento.isBlank()) {
            throw new Exception("Documento obrigatório.");
        }
        try (Connection con = ConexaoDAO.getConexao()) {
            EnderecoDAO endDAO = new EnderecoDAO(con);
            return endDAO.buscarPorDocumento(documento);
        } catch (SQLException e) {
            throw new Exception("Erro ao listar endereços por pessoa: " + e.getMessage(), e);
        }
    }

    public void sincronizarEnderecos(String documento, List<EnderecoVO> enderecos) throws Exception {
        if (documento == null || documento.isBlank()) {
            throw new Exception("Documento obrigatório.");
        }
        if (enderecos == null || enderecos.isEmpty()) {
            return;
        }

        Connection con = null;
        try {
            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            for (EnderecoVO e : enderecos) {
                if (e.getEnd_endP_id() == null) {
                    throw new Exception("EndPostal obrigatório para cada endereço.");
                }
                if (e.getEnd_endP_id().getEndP_id() == 0) {
                    int endPId = criarOuObterEndPostal(con, e.getEnd_endP_id());
                    e.getEnd_endP_id().setEndP_id(endPId);
                }
            }

            EnderecoDAO endDAO = new EnderecoDAO(con);
            endDAO.sincronizarPorPessoa(documento, enderecos);

            con.commit();
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ignore) {}
            throw new Exception("Erro ao sincronizar endereços: " + e.getMessage(), e);
        } finally {
            if (con != null) try { con.close(); } catch (SQLException ignore) {}
        }
    }

    private int criarOuObterEndPostal(Connection con, EndPostalVO endPostal) throws Exception {
        if (endPostal == null) {
            throw new Exception("Objeto EndPostal obrigatório.");
        }
        if (endPostal.getEndP_logradouro() == null || endPostal.getEndP_bairro() == null
                || endPostal.getEndP_cidade() == null || endPostal.getEndP_estado() == null) {
            throw new Exception("EndPostal incompleto: logradouro, bairro, cidade e estado são obrigatórios.");
        }
        if (endPostal.getEndP_nomeRua() == null || endPostal.getEndP_nomeRua().isBlank()) {
            throw new Exception("Nome da rua obrigatório.");
        }
        if (endPostal.getEndP_cep() == null || endPostal.getEndP_cep().isBlank()) {
            throw new Exception("CEP obrigatório.");
        }

        try {
            EndPostalDAO dao = new EndPostalDAO(con);
            EndPostalVO existente = dao.buscarPorCep(endPostal.getEndP_cep());
            if (existente != null) {
                return existente.getEndP_id();
            }
            int id = dao.adicionarNovo(endPostal);
            if (id <= 0) {
                throw new Exception("Falha ao inserir EndPostal.");
            }
            return id;
        } catch (SQLException e) {
            throw new Exception("Erro ao criar/obter EndPostal: " + e.getMessage(), e);
        }
    }
}

