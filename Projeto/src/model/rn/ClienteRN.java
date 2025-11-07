package model.rn;

import model.dao.*;
import model.vo.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ClienteRN {
    private ClienteDAO clienteDAO;
    private PessoaDAO pessoaDAO;

    public ClienteRN() {}

    public void salvarNovo(ClienteVO cliente, List<TelefoneVO> telefones, List<EnderecoVO> enderecos) throws Exception {
        if (cliente == null) 
            throw new Exception("Cliente obrigatorio.");

        String documento = cliente.getPes_cpf();
        if (documento == null || documento.isBlank()) 
            throw new Exception("Documento obrigatorio.");

        if (cliente.getPes_nome() == null || cliente.getPes_nome().isBlank()) 
            throw new Exception("Nome obrigatorio.");

        Connection con = null;
        try {
            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);
            this.pessoaDAO = new PessoaDAO(con);
            this.clienteDAO = new ClienteDAO(con);

            if (pessoaDAO.buscarPesCpf(documento) == null) {
                pessoaDAO.adicionarNovaPessoa(cliente);
            } else {
                pessoaDAO.atualizarPessoa(cliente);
            }

            if (clienteDAO.buscarPorDocumento(documento) != null)
                throw new Exception("Cliente ja cadastrado para o documento informado.");

            if (cliente.getCli_dtCadastro() == null) 
                cliente.setCli_dtCadastro(LocalDate.now());

            int id = clienteDAO.adicionarNovoCliente(cliente);

            if (id <= 0) 
                throw new Exception("Falha ao inserir cliente.");

            if (telefones != null && !telefones.isEmpty()) {
                TelefoneDAO telDAO = new TelefoneDAO(con);
                for (TelefoneVO t : telefones) {
                    if (t.getTel_numero() == null || t.getTel_numero().isBlank()) continue;
                    telDAO.adicionarNovo(t, documento);
                }
            }

            if (enderecos != null && !enderecos.isEmpty()) {
                EnderecoDAO endDAO = new EnderecoDAO(con);
                PesEndDAO pesEndDAO = new PesEndDAO(con);
                for (EnderecoVO e : enderecos) {
                    int endId = endDAO.adicionarNovo(e);
                    if (endId > 0) pesEndDAO.vincular(documento, endId);
                }
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) 
                con.rollback();
            throw new Exception("Erro ao salvar cliente: " + e.getMessage(), e);
        } finally { if (con != null) con.close(); }
    }

    public void atualizarCliente(ClienteVO cliente) throws Exception {
        if (cliente == null) 
            throw new Exception("Objeto Cliente nao informado.");

        String documento = cliente.getPes_cpf();
        if (documento == null || documento.isBlank()) 
            throw new Exception("Documento obrigatorio para atualizacao.");

        Connection con = null;
        try {
            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);
            this.pessoaDAO = new PessoaDAO(con);
            this.clienteDAO = new ClienteDAO(con);
            if (clienteDAO.buscarPorDocumento(documento) == null)
                throw new Exception("Cliente nao encontrado: " + documento);
            pessoaDAO.atualizarPessoa(cliente);
            clienteDAO.atualizarCliente(cliente);
            con.commit();
        } catch (SQLException e) { 
            if (con != null) con.rollback(); throw new Exception("Erro ao atualizar cliente: " + e.getMessage(), e); 
        }
        finally { 
            if (con != null) con.close(); 
        }
    }

    public ClienteVO buscarPorDocumento(String documento) throws Exception {
        if (documento == null || documento.isBlank()) 
            throw new Exception("Documento obrigatorio.");
        try (Connection con = ConexaoDAO.getConexao()) {
            this.clienteDAO = new ClienteDAO(con);
            return clienteDAO.buscarPorDocumento(documento);
        } catch (SQLException e) { 
            throw new Exception("Erro ao buscar cliente: " + e.getMessage(), e); 
        }
    }

    public List<ClienteVO> buscarComFiltros(String nome, String tipoCodigo, Boolean ativo) throws Exception {
        try (Connection con = ConexaoDAO.getConexao()) {
            this.clienteDAO = new ClienteDAO(con);
            return clienteDAO.buscarComFiltros(nome, tipoCodigo, ativo);
        } catch (SQLException e) { 
            throw new Exception("Erro ao buscar clientes: " + e.getMessage(), e); 
        }
    }

    public List<ClienteVO> buscarTodosClientes(String nomeLike) throws Exception {
        // Atalho: busca por nome parcial, sem filtrar tipo/status
        return buscarComFiltros(nomeLike, null, null);
    }

    public void deletarCliente(String documento) throws Exception {
        if (documento == null || documento.isBlank()) throw new Exception("Documento obrigatorio.");
        Connection con = null;
        try {
            con = ConexaoDAO.getConexao();
            con.setAutoCommit(false);
            this.clienteDAO = new ClienteDAO(con);
            if (clienteDAO.buscarPorDocumento(documento) == null)
                throw new Exception("Cliente nao encontrado: " + documento);
            clienteDAO.deletarCliente(documento);
            con.commit();
        } catch (SQLException e) { if (con != null) con.rollback(); throw new Exception("Erro ao deletar cliente: " + e.getMessage(), e); }
        finally { if (con != null) con.close(); }
    }
}
