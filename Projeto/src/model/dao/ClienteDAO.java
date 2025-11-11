package model.dao;

import model.vo.BairroVO;
import model.vo.CidadeVO;
import model.vo.ClienteVO;
import model.vo.EndPostalVO;
import model.vo.EnderecoVO;
import model.vo.EstadoVO;
import model.vo.LogradouroVO;
import model.vo.PessoaVO;
import model.vo.SexoVO;
import model.vo.TelefoneVO;
import model.vo.TipoPessoaVO;

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

    // Busca completa (com joins) por documento
    public ClienteVO buscarClienteCompletoPorDocumento(String documento) throws SQLException {
        String sql = """
        SELECT
            c.cli_id,
            p.pes_documento,
            p.pes_nome,
            p.pes_email,
            p.pes_ativo,
            p.pes_dtNascimento,
            tp.tipo_pessoa_id,
            tp.codigo         AS tipo_codigo,
            tp.descricao      AS tipo_descricao,
            s.sex_id          AS sexo_id,
            s.sex_descricao   AS sexo_descricao,
            e.end_id,
            e.end_numero,
            e.end_complemento,
            ep.endP_nomeRua,
            ep.endP_cep,
            b.bairro_descricao,
            l.logradouro_id      AS logradouro_id,
            l.log_descricao AS log_descricao,
            cid.cid_descricao AS cidade,
            est.est_sigla     AS estado,
            tel.tel_id        AS tel_id,
            tel.tel_codPais,
            tel.tel_ddd,
            tel.tel_numero
        FROM tb_cliente c
        JOIN tb_pessoa p ON c.cli_pes_documento = p.pes_documento
        LEFT JOIN tb_tipoPessoa tp ON tp.tipo_pessoa_id = p.pes_tipo_pessoa_id
        LEFT JOIN tb_sexo s ON s.sex_id = p.pes_sex_id
        LEFT JOIN tb_pesEnd pe ON pe.pesEnd_pes_documento = p.pes_documento
        LEFT JOIN tb_endereco e ON e.end_id = pe.pesEnd_end_id
        LEFT JOIN tb_endPostal ep ON ep.endP_id = e.end_endP_id
        LEFT JOIN tb_bairro b ON b.bairro_id = ep.endP_bairro_id
        LEFT JOIN tb_logradouro l ON l.logradouro_id = ep.endP_logradouro_id
        LEFT JOIN tb_cidEst ce ON ce.cidEstPai_cid_id = ep.endP_cid_id AND ce.cidEstPai_est_sigla = ep.endP_est_sigla
        LEFT JOIN tb_cidade cid ON cid.cid_id = ce.cidEstPai_cid_id
        LEFT JOIN tb_estado est ON est.est_sigla = ce.cidEstPai_est_sigla
        LEFT JOIN tb_telefone tel ON tel.tel_pes_documento = p.pes_documento
        WHERE p.pes_documento = ?
        """;

        try (PreparedStatement ps = con_cli.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClienteVO cliente = new ClienteVO();
                    cliente.setCli_id(rs.getInt("cli_id"));
                    cliente.setPes_cpf(rs.getString("pes_documento"));
                    cliente.setPes_nome(rs.getString("pes_nome"));
                    cliente.setPes_email(rs.getString("pes_email"));
                    cliente.setPes_ativo(rs.getBoolean("pes_ativo"));
                    Date dn = rs.getDate("pes_dtNascimento");
                    cliente.setPes_dt_nascimento(dn != null ? ((java.sql.Date) dn).toLocalDate() : null);

                    TipoPessoaVO tipo = new TipoPessoaVO();
                    tipo.setTipo_pessoa_id(rs.getInt("tipo_pessoa_id"));
                    tipo.setCodigo(rs.getString("tipo_codigo"));
                    tipo.setDescricao(rs.getString("tipo_descricao"));
                    cliente.setPes_tipo_pessoa(tipo);

                    SexoVO sexo = new SexoVO(rs.getInt("sexo_id"), rs.getString("sexo_descricao"));
                    cliente.setPes_sexo(sexo);

                    TelefoneVO tel = new TelefoneVO();
                    try { tel.setTel_id(rs.getInt("tel_id")); } catch (SQLException ignore) {}
                    tel.setTel_codPais(rs.getString("tel_codPais"));
                    tel.setTel_ddd(rs.getString("tel_ddd"));
                    tel.setTel_numero(rs.getString("tel_numero"));
                    cliente.setTelefone(List.of(tel));

                    EndPostalVO endPostal = new EndPostalVO();
                    endPostal.setEndP_nomeRua(rs.getString("endP_nomeRua"));
                    endPostal.setEndP_cep(rs.getString("endP_cep"));
                    endPostal.setEndP_bairro(new BairroVO(0, rs.getString("bairro_descricao")));
                    endPostal.setEndP_logradouro(new LogradouroVO(rs.getInt("logradouro_id"), rs.getString("log_descricao")));
                    endPostal.setEndP_cidade(new CidadeVO(0, rs.getString("cidade")));
                    endPostal.setEndP_estado(new EstadoVO(rs.getString("estado"), null));

                    EnderecoVO end = new EnderecoVO();
                    end.setEnd_id(rs.getInt("end_id"));
                    end.setEnd_numero(rs.getString("end_numero"));
                    end.setEnd_complemento(rs.getString("end_complemento"));
                    end.setEnd_endP_id(endPostal);
                    cliente.setEndereco(List.of(end));

                    return cliente;
                }
            }
        }
        return null;
    }

    // Busca completa com filtros (nome, tipo, ativo)
    public List<ClienteVO> buscarClientesCompletosComFiltros(String nome, String tipoCodigo, Boolean ativo) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("""
        SELECT
            c.cli_id,
            p.pes_documento,
            p.pes_nome,
            p.pes_email,
            p.pes_ativo,
            p.pes_dtNascimento,
            tp.tipo_pessoa_id,
            tp.codigo         AS tipo_codigo,
            tp.descricao      AS tipo_descricao,
            s.sex_id          AS sexo_id,
            s.sex_descricao   AS sexo_descricao,
            e.end_id,
            e.end_numero,
            e.end_complemento,
            ep.endP_nomeRua,
            ep.endP_cep,
            b.bairro_descricao,
            l.logradouro_id      AS logradouro_id,
            l.log_descricao AS log_descricao,
            cid.cid_descricao AS cidade,
            est.est_sigla     AS estado,
            tel.tel_id        AS tel_id,
            tel.tel_codPais,
            tel.tel_ddd,
            tel.tel_numero
        FROM tb_cliente c
        JOIN tb_pessoa p ON c.cli_pes_documento = p.pes_documento
        LEFT JOIN tb_tipoPessoa tp ON tp.tipo_pessoa_id = p.pes_tipo_pessoa_id
        LEFT JOIN tb_sexo s ON s.sex_id = p.pes_sex_id
        LEFT JOIN tb_pesEnd pe ON pe.pesEnd_pes_documento = p.pes_documento
        LEFT JOIN tb_endereco e ON e.end_id = pe.pesEnd_end_id
        LEFT JOIN tb_endPostal ep ON ep.endP_id = e.end_endP_id
        LEFT JOIN tb_bairro b ON b.bairro_id = ep.endP_bairro_id
        LEFT JOIN tb_logradouro l ON l.logradouro_id = ep.endP_logradouro_id
        LEFT JOIN tb_cidEst ce ON ce.cidEstPai_cid_id = ep.endP_cid_id AND ce.cidEstPai_est_sigla = ep.endP_est_sigla
        LEFT JOIN tb_cidade cid ON cid.cid_id = ce.cidEstPai_cid_id
        LEFT JOIN tb_estado est ON est.est_sigla = ce.cidEstPai_est_sigla
        LEFT JOIN tb_telefone tel ON tel.tel_pes_documento = p.pes_documento
        WHERE 1=1
        """);

        if (nome != null && !nome.isBlank()) sb.append(" AND p.pes_nome LIKE ? ");
        if (tipoCodigo != null && !tipoCodigo.equalsIgnoreCase("Todos") && !tipoCodigo.isBlank()) sb.append(" AND tp.codigo = ? ");
        if (ativo != null) sb.append(" AND p.pes_ativo = ? ");

        List<ClienteVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_cli.prepareStatement(sb.toString())) {
            int idx = 1;
            if (nome != null && !nome.isBlank()) ps.setString(idx++, "%" + nome + "%");
            if (tipoCodigo != null && !tipoCodigo.equalsIgnoreCase("Todos") && !tipoCodigo.isBlank()) ps.setString(idx++, tipoCodigo.substring(0, 1));
            if (ativo != null) ps.setBoolean(idx++, ativo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClienteVO cliente = new ClienteVO();
                    cliente.setCli_id(rs.getInt("cli_id"));
                    cliente.setPes_cpf(rs.getString("pes_documento"));
                    cliente.setPes_nome(rs.getString("pes_nome"));
                    cliente.setPes_email(rs.getString("pes_email"));
                    cliente.setPes_ativo(rs.getBoolean("pes_ativo"));
                    Date dn = rs.getDate("pes_dtNascimento");
                    cliente.setPes_dt_nascimento(dn != null ? ((java.sql.Date) dn).toLocalDate() : null);

                    TipoPessoaVO tipo = new TipoPessoaVO();
                    tipo.setTipo_pessoa_id(rs.getInt("tipo_pessoa_id"));
                    tipo.setCodigo(rs.getString("tipo_codigo"));
                    tipo.setDescricao(rs.getString("tipo_descricao"));
                    cliente.setPes_tipo_pessoa(tipo);

                    SexoVO sexo = new SexoVO(rs.getInt("sexo_id"), rs.getString("sexo_descricao"));
                    cliente.setPes_sexo(sexo);

                    TelefoneVO tel = new TelefoneVO();
                    try { tel.setTel_id(rs.getInt("tel_id")); } catch (SQLException ignore) {}
                    tel.setTel_codPais(rs.getString("tel_codPais"));
                    tel.setTel_ddd(rs.getString("tel_ddd"));
                    tel.setTel_numero(rs.getString("tel_numero"));
                    cliente.setTelefone(List.of(tel));

                    EndPostalVO endPostal = new EndPostalVO();
                    endPostal.setEndP_nomeRua(rs.getString("endP_nomeRua"));
                    endPostal.setEndP_cep(rs.getString("endP_cep"));
                    endPostal.setEndP_bairro(new BairroVO(0, rs.getString("bairro_descricao")));
                    endPostal.setEndP_logradouro(new LogradouroVO(rs.getInt("logradouro_id"), rs.getString("log_descricao")));
                    endPostal.setEndP_cidade(new CidadeVO(0, rs.getString("cidade")));
                    endPostal.setEndP_estado(new EstadoVO(rs.getString("estado"), null));

                    EnderecoVO end = new EnderecoVO();
                    end.setEnd_id(rs.getInt("end_id"));
                    end.setEnd_numero(rs.getString("end_numero"));
                    end.setEnd_complemento(rs.getString("end_complemento"));
                    end.setEnd_endP_id(endPostal);
                    cliente.setEndereco(List.of(end));

                    lista.add(cliente);
                }
            }
        }
        return lista;
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


    public List<ClienteVO> listarClientesCompletos() throws SQLException {
        List<ClienteVO> lista = new ArrayList<>();
            String sql = """
    SELECT
        -- Cliente / Pessoa
        c.cli_id,
        p.pes_documento,
        p.pes_nome,
        p.pes_email,
        p.pes_ativo,
        p.pes_dtNascimento,

        -- Tipo de pessoa
        tp.tipo_pessoa_id,
        tp.codigo         AS tipo_codigo,
        tp.descricao      AS tipo_descricao,

        -- Sexo
        s.sex_id          AS sexo_id,
        s.sex_descricao   AS sexo_descricao,

        -- Endereço (um possível endereço vinculado)
        e.end_id,
        e.end_numero,
        e.end_complemento,
        ep.endP_nomeRua,
        ep.endP_cep,
        b.bairro_descricao,
        l.logradouro_id      AS logradouro_id,
        l.log_descricao AS log_descricao,
        cid.cid_descricao AS cidade,
        est.est_sigla     AS estado,

        -- Telefone (um possível telefone vinculado)
        tel.tel_id      AS tel_id,
        tel.tel_codPais,
        tel.tel_ddd,
        tel.tel_numero

    FROM tb_cliente c
    JOIN tb_pessoa p
      ON c.cli_pes_documento = p.pes_documento

    -- Tipo de pessoa / Sexo
    LEFT JOIN tb_tipoPessoa tp
      ON tp.tipo_pessoa_id = p.pes_tipo_pessoa_id
    LEFT JOIN tb_sexo s
      ON s.sex_id = p.pes_sex_id

    -- Endereço(s) da pessoa
    LEFT JOIN tb_pesEnd pe
      ON pe.pesEnd_pes_documento = p.pes_documento
    LEFT JOIN tb_endereco e
      ON e.end_id = pe.pesEnd_end_id
    LEFT JOIN tb_endPostal ep
      ON ep.endP_id = e.end_endP_id
    LEFT JOIN tb_bairro b
      ON b.bairro_id = ep.endP_bairro_id
    LEFT JOIN tb_logradouro l
      ON l.logradouro_id = ep.endP_logradouro_id

    -- Cidade/Estado via chave composta em tb_cidEst
    LEFT JOIN tb_cidEst ce
      ON ce.cidEstPai_cid_id = ep.endP_cid_id
     AND ce.cidEstPai_est_sigla = ep.endP_est_sigla
    LEFT JOIN tb_cidade cid
      ON cid.cid_id = ce.cidEstPai_cid_id
    LEFT JOIN tb_estado est
      ON est.est_sigla = ce.cidEstPai_est_sigla

        -- Telefones (pode haver vários)
        LEFT JOIN tb_telefone tel
      ON tel.tel_pes_documento = p.pes_documento
    ;
    """;




        try (PreparedStatement ps = con_cli.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ClienteVO cliente = new ClienteVO();
                cliente.setCli_id(rs.getInt("cli_id"));
                cliente.setPes_cpf(rs.getString("pes_documento"));
                cliente.setPes_nome(rs.getString("pes_nome"));
                cliente.setPes_email(rs.getString("pes_email"));
                cliente.setPes_ativo(rs.getBoolean("pes_ativo"));
                // Data de nascimento
                Date dn = rs.getDate("pes_dtNascimento");
                cliente.setPes_dt_nascimento(dn != null ? ((java.sql.Date) dn).toLocalDate() : null);

                // Tipo pessoa
                TipoPessoaVO tipo = new TipoPessoaVO();
                tipo.setTipo_pessoa_id(rs.getInt("tipo_pessoa_id"));
                tipo.setCodigo(rs.getString("tipo_codigo"));
                tipo.setDescricao(rs.getString("tipo_descricao"));
                cliente.setPes_tipo_pessoa(tipo);

                // Sexo
                SexoVO sexo = new SexoVO(rs.getInt("sexo_id"), rs.getString("sexo_descricao"));
                cliente.setPes_sexo(sexo);

                // Telefone
                TelefoneVO tel = new TelefoneVO();
                // incluir id do telefone para permitir update posterior
                try {
                    tel.setTel_id(rs.getInt("tel_id"));
                } catch (SQLException ignore) { }
                tel.setTel_codPais(rs.getString("tel_codPais"));
                tel.setTel_ddd(rs.getString("tel_ddd"));
                tel.setTel_numero(rs.getString("tel_numero"));
                cliente.setTelefone(List.of(tel));

                // Endereço
                EndPostalVO endPostal = new EndPostalVO();
                endPostal.setEndP_nomeRua(rs.getString("endP_nomeRua"));
                endPostal.setEndP_cep(rs.getString("endP_cep"));
                endPostal.setEndP_bairro(new BairroVO(0, rs.getString("bairro_descricao")));
                endPostal.setEndP_logradouro(new LogradouroVO(rs.getInt("logradouro_id"), rs.getString("log_descricao")));
                endPostal.setEndP_cidade(new CidadeVO(0, rs.getString("cidade")));
                endPostal.setEndP_estado(new EstadoVO(rs.getString("estado"), null));

                EnderecoVO end = new EnderecoVO();
                end.setEnd_id(rs.getInt("end_id"));
                end.setEnd_numero(rs.getString("end_numero"));
                end.setEnd_complemento(rs.getString("end_complemento"));
                end.setEnd_endP_id(endPostal);
                cliente.setEndereco(List.of(end));

                lista.add(cliente);
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
