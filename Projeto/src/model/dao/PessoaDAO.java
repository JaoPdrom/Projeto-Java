package model.dao;

import model.vo.PessoaVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PessoaDAO {

    private Connection con_pessoa;
    private SexoDAO sexoDAO;
    private TipoPessoaDAO tipoPessoaDAO;

    public PessoaDAO(Connection con_pessoa) {
        this.con_pessoa = con_pessoa;
        this.sexoDAO = new SexoDAO(con_pessoa);
        this.tipoPessoaDAO = new TipoPessoaDAO();
    }

    // adicionar nova pessoa
    public void adicionarNovaPessoa(PessoaVO pessoa) throws SQLException {
        String sql = "INSERT INTO tb_pessoa (pes_documento, pes_tipo_pessoa_id, pes_nome, pes_sex_id, pes_dtNascimento, pes_email, pes_ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pessoa_add = con_pessoa.prepareStatement(sql)) {
            pessoa_add.setString(1, pessoa.getPes_cpf());
            if (pessoa.getPes_tipo_pessoa() != null) {
                pessoa_add.setInt(2, pessoa.getPes_tipo_pessoa().getTipo_pessoa_id());
            } else {
                pessoa_add.setNull(2, java.sql.Types.INTEGER);
            }

            pessoa_add.setString(3, pessoa.getPes_nome());

            if (pessoa.getPes_sexo() != null) {
                pessoa_add.setInt(4, pessoa.getPes_sexo().getSex_id());
            } else {
                pessoa_add.setNull(4, java.sql.Types.INTEGER);
            }

            if (pessoa.getPes_dt_nascimento() != null) {
                pessoa_add.setDate(5, java.sql.Date.valueOf(pessoa.getPes_dt_nascimento()));
            } else {
                pessoa_add.setNull(5, java.sql.Types.DATE);
            }

            pessoa_add.setString(6, pessoa.getPes_email());
            pessoa_add.setBoolean(7, pessoa.getPes_ativo() != null ? pessoa.getPes_ativo() : true);
            pessoa_add.executeUpdate();
        }
    }

    // update pessoa por cpf
    public void atualizarPessoa(PessoaVO pessoaAtt) throws SQLException {
        PessoaVO pesAntiga = buscarPesCpf(pessoaAtt.getPes_cpf());
        if (pesAntiga == null) {
            throw new SQLException("Pessoa não encontrada para atualizar: " + pessoaAtt.getPes_cpf());
        }

        StringBuilder sql = new StringBuilder("UPDATE tb_pessoa SET ");
        List<Object> parametros = new ArrayList<>();

        // CPF normalmente não deve ser alterado, então ignoramos
        if (!Objects.equals(pessoaAtt.getPes_nome(), pesAntiga.getPes_nome())) {
            sql.append("pes_nome = ?, ");
            parametros.add(pessoaAtt.getPes_nome());
        }

        // sexo (usa ID)
        if (pessoaAtt.getPes_sexo() != null && pesAntiga.getPes_sexo() != null) {
            if (!Objects.equals(pessoaAtt.getPes_sexo().getSex_id(), pesAntiga.getPes_sexo().getSex_id())) {
                sql.append("pes_sex_id = ?, ");
                parametros.add(pessoaAtt.getPes_sexo().getSex_id());
            }
        }

        // data nascimento
        if (!Objects.equals(pessoaAtt.getPes_dt_nascimento(), pesAntiga.getPes_dt_nascimento())) {
            sql.append("pes_dtNascimento = ?, ");
            parametros.add(pessoaAtt.getPes_dt_nascimento());
        }

        // email
        if (!Objects.equals(pessoaAtt.getPes_email(), pesAntiga.getPes_email())) {
            sql.append("pes_email = ?, ");
            parametros.add(pessoaAtt.getPes_email());
        }

        // ativo
        if (pessoaAtt.getPes_ativo() != pesAntiga.getPes_ativo()) {
            sql.append("pes_ativo = ?, ");
            parametros.add(pessoaAtt.getPes_ativo() ? 1 : 0);
        }

        // tipo de pessoa
        if (pessoaAtt.getPes_tipo_pessoa() != null && (pesAntiga.getPes_tipo_pessoa() == null || !Objects.equals(pessoaAtt.getPes_tipo_pessoa().getTipo_pessoa_id(), pesAntiga.getPes_tipo_pessoa().getTipo_pessoa_id()))) {
            sql.append("pes_tipo_pessoa_id = ?, ");
            parametros.add(pessoaAtt.getPes_tipo_pessoa().getTipo_pessoa_id());
        }

        if (parametros.isEmpty()) {
            System.out.println("⚠️ Nenhuma alteração detectada para CPF " + pessoaAtt.getPes_cpf());
            return;
        }

        // Remove última vírgula
        sql.setLength(sql.length() - 2);

        sql.append(" WHERE pes_documento = ?");
        parametros.add(pessoaAtt.getPes_cpf());

        System.out.println("🔧 SQL Gerado: " + sql);
        System.out.println("🧩 Parâmetros: " + parametros);

        try (PreparedStatement ps = con_pessoa.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                Object valor = parametros.get(i);

                // Tratar data local
                if (valor instanceof java.time.LocalDate data) {
                    ps.setDate(i + 1, java.sql.Date.valueOf(data));
                } else {
                    ps.setObject(i + 1, valor);
                }
            }

            int linhas = ps.executeUpdate();
            System.out.println("✅ Linhas afetadas: " + linhas);
            if (linhas == 0) {
                throw new SQLException("Nenhuma linha atualizada (CPF não encontrado ou dados idênticos).");
            }
        }
    }


    // busca pessoa por cpf
    public PessoaVO buscarPesCpf(String cpfOuDocumento) throws SQLException {
        String sql = "SELECT * FROM tb_pessoa WHERE pes_documento = ?";
        PessoaVO pessoa = null;

        try (PreparedStatement stmt = con_pessoa.prepareStatement(sql)) {
            stmt.setString(1, cpfOuDocumento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pessoa = new PessoaVO();
                    pessoa.setPes_cpf(rs.getString("pes_documento"));
                    pessoa.setPes_nome(rs.getString("pes_nome"));
                    pessoa.setPes_email(rs.getString("pes_email"));

                    // ✅ Conversão segura de java.sql.Date → LocalDate
                    java.sql.Date dataSql = rs.getDate("pes_dtNascimento");
                    pessoa.setPes_dt_nascimento(dataSql != null ? dataSql.toLocalDate() : null);

                    // ⚠️ Verifica se sexoDAO está inicializado antes de usar
                    int idSexo = rs.getInt("pes_sex_id");
                    if (sexoDAO != null && idSexo > 0) {
                        pessoa.setPes_sexo(sexoDAO.buscarPorId(idSexo));
                    } else {
                        pessoa.setPes_sexo(null);
                    }

                    try {
                        int idTipo = rs.getInt("pes_tipo_pessoa_id");
                        if (!rs.wasNull()) {
                            pessoa.setPes_tipo_pessoa(tipoPessoaDAO.buscarPorId(idTipo));
                        }
                    } catch (SQLException ignore) {}

                    // Caso exista o campo ativo
                    try {
                        pessoa.setPes_ativo(rs.getBoolean("pes_ativo"));
                    } catch (SQLException ignore) {
                        // Campo pode não existir na tabela
                    }
                }
            }
        }

        return pessoa;
    }



    // busca pessoa por nome
    public List<PessoaVO> buscarPesNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tb_pessoa WHERE pes_nome LIKE ?";
        List<PessoaVO> pessoas = new ArrayList<>();

        try (PreparedStatement pessoa_bsc = con_pessoa.prepareStatement(sql)) {
            pessoa_bsc.setString(1, "%" + nome + "%");

            try (ResultSet rs = pessoa_bsc.executeQuery()) {
                while (rs.next()) {
                    PessoaVO pessoa = new PessoaVO();
                    pessoa.setPes_cpf(rs.getString("pes_documento"));
                    pessoa.setPes_nome(rs.getString("pes_nome"));
                    pessoa.setPes_sexo(sexoDAO.buscarPorId(rs.getInt("pes_sex_id")));

                    // ✅ Correção da conversão de Date para LocalDate
                    java.sql.Date dataSql = rs.getDate("pes_dtNascimento");
                    if (dataSql != null) {
                        pessoa.setPes_dt_nascimento(dataSql.toLocalDate());
                    } else {
                        pessoa.setPes_dt_nascimento(null);
                    }

                    pessoa.setPes_email(rs.getString("pes_email"));
                    pessoas.add(pessoa);
                }
            }
        }

        return pessoas;
    }



    // deletar pessoa
    public void deletarPes(PessoaVO pessoa) throws SQLException {
        String sql = "UPDATE tb_pessoa SET pes_ativo = 0 WHERE pes_documento = ?";

        try (PreparedStatement ps = con_pessoa.prepareStatement(sql)) {
            ps.setString(1, pessoa.getPes_cpf());
            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Nenhuma pessoa encontrada com o CPF informado: " + pessoa.getPes_cpf());
            }
        }
    }


    public void deletarPesCPF(String cpf) throws SQLException {
        String sql = "DELETE FROM tb_pessoa WHERE pes_documento = ?";
        try (PreparedStatement stmt = con_pessoa.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        }
    }



}
