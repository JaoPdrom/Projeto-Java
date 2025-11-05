package model.dao;

import model.vo.ClienteVO;
import model.vo.PessoaVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private final Connection con_cli;
    private final PessoaDAO pessoaDAO;

    public ClienteDAO(Connection con_cli) {
        this.con_cli = con_cli;
        this.pessoaDAO = new PessoaDAO(con_cli);
    }

    // inserir cliente (linha em tb_cliente)
    public int adicionarNovoCliente(ClienteVO cliente) throws SQLException {
        String sql = "INSERT INTO tb_cliente (cli_pes_documento, cli_dtCadastro) VALUES (?, ?)";
        try (PreparedStatement ps = con_cli.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (cliente.getCli_dtCadastro() == null) {
                throw new SQLException("cli_dtCadastro não pode ser nulo (NOT NULL)");
            }
            ps.setString(1, cliente.getPes_cpf());
            ps.setDate(2, java.sql.Date.valueOf(cliente.getCli_dtCadastro()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // atualizar dados do cliente
    public void atualizarCliente(ClienteVO cliente) throws SQLException {
        String sql = "UPDATE tb_cliente SET cli_dtCadastro = ? WHERE cli_pes_documento = ?";
        try (PreparedStatement ps = con_cli.prepareStatement(sql)) {
            if (cliente.getCli_dtCadastro() == null) {
                throw new SQLException("cli_dtCadastro não pode ser nulo (NOT NULL)");
            }
            ps.setDate(1, java.sql.Date.valueOf(cliente.getCli_dtCadastro()));
            ps.setString(2, cliente.getPes_cpf());

            int linhas = ps.executeUpdate();
            if (linhas == 0) {
                throw new SQLException("Nenhum cliente atualizado (documento não encontrado): " + cliente.getPes_cpf());
            }
        }
    }

    // compatibilidade: mantém assinatura antiga mas usa documento
    public ClienteVO buscarPorCpf(String documento) throws SQLException {
        return buscarPorDocumento(documento);
    }

    public ClienteVO buscarPorDocumento(String documento) throws SQLException {
        String sql = "SELECT cli_id, cli_pes_documento, cli_dtCadastro FROM tb_cliente WHERE cli_pes_documento = ?";
        try (PreparedStatement ps = con_cli.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PessoaVO pessoa = pessoaDAO.buscarPesCpf(documento);
                    if (pessoa == null) return null;

                    ClienteVO cli = new ClienteVO();
                    // dados pessoa (ClienteVO herda PessoaVO)
                    cli.setPes_cpf(pessoa.getPes_cpf());
                    cli.setPes_nome(pessoa.getPes_nome());
                    cli.setPes_sexo(pessoa.getPes_sexo());
                    cli.setPes_dt_nascimento(pessoa.getPes_dt_nascimento());
                    cli.setPes_email(pessoa.getPes_email());
                    cli.setPes_ativo(pessoa.getPes_ativo());
                    cli.setTelefone(pessoa.getTelefone());
                    cli.setEndereco(pessoa.getEndereco());

                    cli.setCli_id(rs.getInt("cli_id"));
                    cli.setCli_pes_cpf(pessoa);

                    Date dtCad = rs.getDate("cli_dtCadastro");
                    cli.setCli_dtCadastro(dtCad != null ? ((java.sql.Date) dtCad).toLocalDate() : null);
                    return cli;
                }
            }
        }
        return null;
    }

    public List<ClienteVO> buscarComFiltros(String nome, String tipoCodigo, Boolean ativo) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.cli_id, c.cli_pes_documento, c.cli_dtCadastro FROM tb_cliente c ");
        sb.append("JOIN tb_pessoa p ON p.pes_documento = c.cli_pes_documento ");
        sb.append("JOIN tb_tipoPessoa tp ON tp.tipo_pessoa_id = p.pes_tipo_pessoa_id ");
        sb.append("WHERE 1=1 ");
        if (nome != null && !nome.isBlank()) sb.append(" AND p.pes_nome LIKE ? ");
        if (tipoCodigo != null && !tipoCodigo.equalsIgnoreCase("Todos") && !tipoCodigo.isBlank()) sb.append(" AND tp.codigo = ? ");
        if (ativo != null) sb.append(" AND p.pes_ativo = ? ");

        List<ClienteVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_cli.prepareStatement(sb.toString())) {
            int idx = 1;
            if (nome != null && !nome.isBlank()) ps.setString(idx++, "%" + nome + "%");
            if (tipoCodigo != null && !tipoCodigo.equalsIgnoreCase("Todos") && !tipoCodigo.isBlank()) ps.setString(idx++, tipoCodigo.substring(0,1));
            if (ativo != null) ps.setBoolean(idx++, ativo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String documento = rs.getString("cli_pes_documento");
                    PessoaVO pessoa = pessoaDAO.buscarPesCpf(documento);
                    if (pessoa == null) continue;

                    ClienteVO cli = new ClienteVO();
                    cli.setPes_cpf(pessoa.getPes_cpf());
                    cli.setPes_nome(pessoa.getPes_nome());
                    cli.setPes_sexo(pessoa.getPes_sexo());
                    cli.setPes_dt_nascimento(pessoa.getPes_dt_nascimento());
                    cli.setPes_email(pessoa.getPes_email());
                    cli.setPes_ativo(pessoa.getPes_ativo());
                    cli.setTelefone(pessoa.getTelefone());
                    cli.setEndereco(pessoa.getEndereco());

                    cli.setCli_id(rs.getInt("cli_id"));
                    cli.setCli_pes_cpf(pessoa);
                    Date dtCad = rs.getDate("cli_dtCadastro");
                    cli.setCli_dtCadastro(dtCad != null ? ((java.sql.Date) dtCad).toLocalDate() : null);

                    lista.add(cli);
                }
            }
        }
        return lista;
    }

    public void deletarCliente(String documento) throws SQLException {
        String sql = "DELETE FROM tb_cliente WHERE cli_pes_documento = ?";
        try (PreparedStatement ps = con_cli.prepareStatement(sql)) {
            ps.setString(1, documento);
            ps.executeUpdate();
        }
    }
}

