/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.BairroVO;
import model.vo.CidadeVO;
import model.vo.EnderecoVO;
import model.vo.EstadoVO;
import model.vo.LogradouroVO;
import model.vo.EndPostalVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {
    private Connection con_endp;
    private LogradouroDAO logradouroDAO;
    private BairroDAO bairroDAO;
    private CidadeDAO cidadeDAO;
    private EstadoDAO estadoDAO;
    private EndPostalDAO endPostalDAO;
    private PesEndDAO pesEndDAO;

    public EnderecoDAO(Connection con_endp) {
        this.con_endp = con_endp;
        try {
        this.logradouroDAO = new LogradouroDAO(con_endp);
        this.bairroDAO = new BairroDAO(con_endp);
        this.cidadeDAO = new CidadeDAO(con_endp);
        this.estadoDAO = new EstadoDAO(con_endp);
        this.endPostalDAO = new EndPostalDAO(con_endp);
            this.pesEndDAO = new PesEndDAO(con_endp);
        } catch (SQLException e) {
            System.err.println("Erro ao instanciar DAOs auxiliares em EnderecoDAO: " + e.getMessage());
        }
    }

    // adicionar novo endereco
    // Adiciona um novo endereço vinculado a uma pessoa
    public int adicionarNovo(EnderecoVO endereco) throws SQLException {
        String sql = "INSERT INTO tb_endereco (end_endP_id, end_numero, end_complemento) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con_endp.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, endereco.getEnd_endP_id().getEndP_id());
            ps.setString(2, endereco.getEnd_numero());
            ps.setString(3, endereco.getEnd_complemento());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    // ✅ Atualiza um endereço existente
    public void atualizar(EnderecoVO endereco) throws SQLException {
        String sql = "UPDATE tb_endereco SET end_endP_id = ?, end_numero = ?, end_complemento = ? WHERE end_id = ?";
        try (PreparedStatement ps = con_endp.prepareStatement(sql)) {
            ps.setInt(1, endereco.getEnd_endP_id().getEndP_id());
            ps.setString(2, endereco.getEnd_numero());
            ps.setString(3, endereco.getEnd_complemento());
            ps.setInt(4, endereco.getEnd_id());
            ps.executeUpdate();
        }
    }

    public void sincronizarPorPessoa(String documento, List<EnderecoVO> enderecos) throws SQLException {
        for (EnderecoVO novo : enderecos) {
            if (novo.getEnd_id() != 0) {
                atualizar(novo);
            } else {
                int novoId = adicionarNovo(novo);
                pesEndDAO.vincular(documento, novoId);
            }
        }
    }

    public List<EnderecoVO> buscarPorDocumento(String documento) throws SQLException {
        List<EnderecoVO> enderecos = new ArrayList<>();

        String sql = """
        SELECT e.end_id, e.end_endP_id, e.end_numero, e.end_complemento
        FROM tb_endereco e
        INNER JOIN tb_pesEnd pe ON e.end_id = pe.pesEnd_end_id
        WHERE pe.pesEnd_pes_documento = ?
    """;

        try (PreparedStatement ps = con_endp.prepareStatement(sql)) {
            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EnderecoVO end = new EnderecoVO();
                    end.setEnd_id(rs.getInt("end_id"));

                    // Criar o objeto de referência (EndPostal) se existir
                    EndPostalVO endPostal = endPostalDAO.buscarPorId(rs.getInt("end_endP_id"));
                    end.setEnd_endP_id(endPostal);

                    end.setEnd_numero(rs.getString("end_numero"));
                    end.setEnd_complemento(rs.getString("end_complemento"));
                    enderecos.add(end);
                }
            }
        }

        return enderecos;
    }

    // ✅ Busca endereço por ID
    public EnderecoVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_endereco WHERE end_id = ?";
        EnderecoVO endereco = null;
        try (PreparedStatement ps = con_endp.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    endereco = new EnderecoVO();
                    endereco.setEnd_id(rs.getInt("end_id"));
                    endereco.setEnd_numero(rs.getString("end_numero"));
                    endereco.setEnd_complemento(rs.getString("end_complemento"));

                    EndPostalDAO endPostalDAO = new EndPostalDAO(con_endp);
                    EndPostalVO endPostal = endPostalDAO.buscarPorId(rs.getInt("end_endP_id"));
                    endereco.setEnd_endP_id(endPostal);
                }
            }
        }
        return endereco;
    }
}

