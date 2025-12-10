/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import model.vo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    private Connection con_fnc;
    private PessoaDAO pessoaDAO;
    private CargoDAO cargoDAO;
    private SexoDAO sexoDAO;
    private TipoPessoaDAO tipoPessoaDAO;
    private TelefoneDAO telefoneDAO;
    private EnderecoDAO enderecoDAO;
    private ContratacaoDAO contratacaoDAO;
    private DemissaoDAO demissaoDAO;

    public FuncionarioDAO(Connection con_fnc) {
        this.con_fnc = con_fnc;
        this.pessoaDAO = new PessoaDAO(con_fnc);
        this.cargoDAO = new CargoDAO(con_fnc);
        this.sexoDAO = new SexoDAO(con_fnc);
        this.tipoPessoaDAO = new TipoPessoaDAO();
        this.telefoneDAO = new TelefoneDAO(con_fnc);
        this.enderecoDAO = new EnderecoDAO(con_fnc);
        this.contratacaoDAO = new ContratacaoDAO(con_fnc);
        this.demissaoDAO = new DemissaoDAO(con_fnc);
    }

    // adicionar novo funcionario
    public int adicionarNovoFuncionario(FuncionarioVO funcionario) throws SQLException {
        String sql = "INSERT INTO tb_funcionario (fnc_numPis, fnc_salario, fnc_cargo_id, fnc_pes_documento, fnc_ativo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement fnc_add = con_fnc.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            fnc_add.setString(1, funcionario.getFnc_numPis());
            fnc_add.setDouble(2, funcionario.getFnc_salario());
            fnc_add.setInt(3, funcionario.getFnc_cargo().getCar_id());
            fnc_add.setString(4, funcionario.getPes_cpf());
            fnc_add.setBoolean(5, funcionario.getPes_ativo() != null ? funcionario.getPes_ativo() : true);
            fnc_add.executeUpdate();

            try (ResultSet rs = fnc_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // atualizar dados de funcionário e pessoa
    public void atualizarFuncionario(FuncionarioVO funcionario) throws SQLException {
        String sqlPessoa = """
        UPDATE tb_pessoa
        SET pes_nome = ?, 
            pes_sex_id = ?, 
            pes_dtNascimento = ?, 
            pes_email = ?, 
            pes_ativo = ?
        WHERE pes_documento = ?;
    """;

        String sqlFuncionario = """
        UPDATE tb_funcionario
        SET fnc_numPis = ?,
            fnc_cargo_id = ?, 
            fnc_salario = ?,
            fnc_ativo = ?
        WHERE fnc_pes_documento = ?;
    """;

        try (PreparedStatement psPessoa = con_fnc.prepareStatement(sqlPessoa);
             PreparedStatement psFunc = con_fnc.prepareStatement(sqlFuncionario)) {

            // atualiza pessoa
            psPessoa.setString(1, funcionario.getPes_nome());
            psPessoa.setInt(2, funcionario.getPes_sexo().getSex_id());
            psPessoa.setDate(3, java.sql.Date.valueOf(funcionario.getPes_dt_nascimento()));
            psPessoa.setString(4, funcionario.getPes_email());
            psPessoa.setBoolean(5, funcionario.getPes_ativo());
            psPessoa.setString(6, funcionario.getPes_cpf());
            psPessoa.executeUpdate();

            // atualiza funcionário
            psFunc.setString(1, funcionario.getFnc_numPis());
            psFunc.setInt(2, funcionario.getFnc_cargo().getCar_id());
            psFunc.setDouble(3, funcionario.getFnc_salario());
            psFunc.setBoolean(4, funcionario.getPes_ativo() != null ? funcionario.getPes_ativo() : true);
            psFunc.setString(5, funcionario.getPes_cpf());
            psFunc.executeUpdate();
        }
    }



    // busca funcionario por id
    public FuncionarioVO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tb_funcionario WHERE fnc_id = ?";
        FuncionarioVO funcionario = null;
        try (PreparedStatement fnc_bsc = con_fnc.prepareStatement(sql)) {
            fnc_bsc.setInt(1, id);
            try (ResultSet rs = fnc_bsc.executeQuery()) {
                if (rs.next()) {
                    PessoaVO pessoa = pessoaDAO.buscarPesCpf(rs.getString("fnc_pes_documento"));
                    CargoVO cargo = cargoDAO.buscarPorId(rs.getInt("fnc_cargo_id"));
                    funcionario = new FuncionarioVO();
                    funcionario.setPes_cpf(pessoa.getPes_cpf());
                    funcionario.setPes_nome(pessoa.getPes_nome());
                    funcionario.setPes_sexo(pessoa.getPes_sexo());
                    funcionario.setPes_dt_nascimento(pessoa.getPes_dt_nascimento());
                    funcionario.setPes_email(pessoa.getPes_email());
                    funcionario.setPes_ativo(pessoa.getPes_ativo());
                    funcionario.setTelefone(pessoa.getTelefone());
                    funcionario.setEndereco(pessoa.getEndereco());
                    funcionario.setFnc_id(rs.getInt("fnc_id"));
                    funcionario.setFnc_numPis(rs.getString("fnc_numPis"));
                    funcionario.setFnc_salario(rs.getDouble("fnc_salario"));
                    funcionario.setFnc_cargo(cargo);
                    funcionario.setFnc_pes_cpf(pessoa);
                }
            }
        }
        return funcionario;
    }


    // busca genérica por nome
    public List<FuncionarioVO> buscarFuncNome(String nome) throws SQLException {
        String sql = """
            SELECT 
                f.fnc_id,
                f.fnc_numPis,
                f.fnc_salario,
                f.fnc_cargo_id,
                p.pes_documento,
                p.pes_nome,
                p.pes_dtNascimento,
                p.pes_email,
                p.pes_sex_id,
                p.pes_ativo
            FROM tb_funcionario f
            JOIN tb_pessoa p ON f.fnc_pes_documento = p.pes_documento
            WHERE p.pes_nome LIKE ?
        """;

        List<FuncionarioVO> funcionarios = new ArrayList<>();

        try (PreparedStatement stmt = con_fnc.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FuncionarioVO func = new FuncionarioVO();

                    func.setPes_cpf(rs.getString("pes_documento"));
                    func.setPes_nome(rs.getString("pes_nome"));
                    func.setPes_email(rs.getString("pes_email"));
                    func.setPes_dt_nascimento(rs.getDate("pes_dtNascimento").toLocalDate());
                    func.setPes_ativo(rs.getBoolean("pes_ativo"));
                    func.setPes_sexo(sexoDAO.buscarPorId(rs.getInt("pes_sex_id")));

                    CargoVO cargo = cargoDAO.buscarPorId(rs.getInt("fnc_cargo_id"));
                    func.setFnc_cargo(cargo);

                    func.setFnc_id(rs.getInt("fnc_id"));
                    func.setFnc_numPis(rs.getString("fnc_numPis"));
                    func.setFnc_salario(rs.getDouble("fnc_salario"));

                    funcionarios.add(func);
                }
            }
        }

        return funcionarios;
    }

    public FuncionarioVO buscarFuncCpf(String cpf) throws SQLException {
        String sql = """
        SELECT 
            f.fnc_id,
            f.fnc_numPis,
            f.fnc_salario,
            f.fnc_cargo_id,
            p.pes_documento,
            p.pes_nome,
            p.pes_dtNascimento,
            p.pes_email,
            p.pes_sex_id,
            p.pes_ativo,
            s.sex_descricao,
            c.cargo_descricao
        FROM tb_funcionario f
        JOIN tb_pessoa p ON f.fnc_pes_documento = p.pes_documento
        JOIN tb_sexo s ON p.pes_sex_id = s.sex_id
        JOIN tb_cargo c ON f.fnc_cargo_id = c.cargo_id
        WHERE p.pes_documento = ?
    """;

        try (PreparedStatement stmt = con_fnc.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairFuncionario(rs);
                }
            }
        }
        return null;
    }

    public List<FuncionarioVO> listarTodosCompletos() throws SQLException {
        String sql = """
        SELECT 
            f.fnc_id,
            f.fnc_numPis,
            f.fnc_salario,
            f.fnc_cargo_id,
            f.fnc_pes_documento,
            f.fnc_ativo,
            p.pes_nome,
            p.pes_dtNascimento,
            p.pes_email,
            p.pes_sex_id,
            p.pes_tipo_pessoa_id,
            p.pes_ativo,
            s.sex_descricao,
            c.cargo_descricao
        FROM tb_funcionario f
        JOIN tb_pessoa p ON p.pes_documento = f.fnc_pes_documento
        LEFT JOIN tb_sexo s ON s.sex_id = p.pes_sex_id
        LEFT JOIN tb_cargo c ON c.cargo_id = f.fnc_cargo_id
        ORDER BY f.fnc_id
        """;

        List<FuncionarioVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_fnc.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                FuncionarioVO funcionario = new FuncionarioVO();
                funcionario.setFnc_id(rs.getInt("fnc_id"));
                funcionario.setFnc_numPis(rs.getString("fnc_numPis"));
                funcionario.setFnc_salario(rs.getDouble("fnc_salario"));
                funcionario.setPes_cpf(rs.getString("fnc_pes_documento"));
                funcionario.setPes_nome(rs.getString("pes_nome"));
                funcionario.setPes_dt_nascimento(rs.getDate("pes_dtNascimento") != null ? rs.getDate("pes_dtNascimento").toLocalDate() : null);
                funcionario.setPes_email(rs.getString("pes_email"));
                funcionario.setPes_ativo(rs.getBoolean("fnc_ativo"));

                int sexoId = rs.getInt("pes_sex_id");
                if (!rs.wasNull()) {
                    funcionario.setPes_sexo(sexoDAO.buscarPorId(sexoId));
                }

                int tipoPessoaId = rs.getInt("pes_tipo_pessoa_id");
                if (!rs.wasNull()) {
                    TipoPessoaVO tipoPessoa = tipoPessoaDAO.buscarPorId(tipoPessoaId);
                    funcionario.setPes_tipo_pessoa(tipoPessoa);
                }

                CargoVO cargo = cargoDAO.buscarPorId(rs.getInt("fnc_cargo_id"));
                funcionario.setFnc_cargo(cargo);

                List<TelefoneVO> telefones = telefoneDAO.buscarPorCpf(funcionario.getPes_cpf());
                funcionario.setTelefone(telefones);

                List<EnderecoVO> enderecos = enderecoDAO.buscarPorDocumento(funcionario.getPes_cpf());
                funcionario.setEndereco(enderecos);

                List<ContratacaoVO> fases = contratacaoDAO.listarPorFuncionario(funcionario.getFnc_id());
                if (!fases.isEmpty()) {
                    ContratacaoVO ultima = fases.get(fases.size() - 1);
                    ultima.setFuncionario(funcionario);
                    funcionario.setContratacao(ultima);
                    funcionario.setFnc_dtContratacao(ultima.getContratacao_dtContratacao());
                }

                List<DemissaoVO> demissoes = demissaoDAO.listarPorFuncionario(funcionario.getFnc_id());
                if (!demissoes.isEmpty()) {
                    DemissaoVO ultimaDemissao = demissoes.get(0);
                    ultimaDemissao.setFuncionario(funcionario);
                    funcionario.setDemissao(ultimaDemissao);
                    funcionario.setFnc_dtDemissao(ultimaDemissao.getDemissao_data());
                    funcionario.setFnc_motivo_demissao(ultimaDemissao.getDemissao_motivo());
                }

                lista.add(funcionario);
            }
        }
        return lista;
    }


    // constroi um objeto FuncionarioVO completo a partir do ResultSet
    private FuncionarioVO extrairFuncionario(ResultSet rs) throws SQLException {
        FuncionarioVO funcionario = new FuncionarioVO();

        // campos herdados de PessoaVO
        funcionario.setPes_cpf(rs.getString("pes_documento"));
        funcionario.setPes_nome(rs.getString("pes_nome"));
        funcionario.setPes_email(rs.getString("pes_email"));

        java.sql.Date dataNasc = rs.getDate("pes_dtNascimento");
        funcionario.setPes_dt_nascimento(dataNasc != null ? dataNasc.toLocalDate() : null);

        funcionario.setPes_ativo(rs.getBoolean("pes_ativo"));
        funcionario.setFnc_numPis(rs.getString("fnc_numPis"));

        // sexo
        SexoVO sexo = new SexoVO();
        sexo.setSex_id(rs.getInt("pes_sex_id"));
        sexo.setSex_descricao(rs.getString("sex_descricao"));
        funcionario.setPes_sexo(sexo);

        // cargo
        CargoVO cargo = new CargoVO();
        cargo.setCar_id(rs.getInt("fnc_cargo_id")); // nome real da coluna
        cargo.setCargo_descricao(rs.getString("cargo_descricao"));
        funcionario.setFnc_cargo(cargo);

        // campos específicos de Funcionário
        funcionario.setFnc_id(rs.getInt("fnc_id"));
        funcionario.setFnc_salario(rs.getDouble("fnc_salario"));
        funcionario.setFnc_dtContratacao(null);
        funcionario.setFnc_dtDemissao(null);
        funcionario.setFnc_motivo_demissao(null);
        return funcionario;
    }


    // soft delete do funcionário e pessoa
    public void deletarFuncionario(FuncionarioVO funcionario) throws SQLException {
        String sqlPessoa = "UPDATE tb_pessoa SET pes_ativo = 0 WHERE pes_documento = ?";
        String sqlFuncionario = "UPDATE tb_funcionario SET fnc_ativo = 0 WHERE fnc_pes_documento = ?";

        try (
                PreparedStatement psPessoa = con_fnc.prepareStatement(sqlPessoa);
                PreparedStatement psFunc = con_fnc.prepareStatement(sqlFuncionario)
        ) {
            // desativa na tb_pessoa
            psPessoa.setString(1, funcionario.getPes_cpf());
            psPessoa.executeUpdate();

            // desativa na tb_funcionario
            psFunc.setString(1, funcionario.getPes_cpf());
            psFunc.executeUpdate();
        }
    }


    // monta um objeto FuncionarioVO completo (campos de pessoa/funcionario + telefones, enderecos e historico)
    private FuncionarioVO montarFuncionarioCompleto(ResultSet rs) throws SQLException {
        FuncionarioVO funcionario = new FuncionarioVO();

        funcionario.setFnc_id(rs.getInt("fnc_id"));
        funcionario.setFnc_numPis(rs.getString("fnc_numPis"));
        funcionario.setFnc_salario(rs.getDouble("fnc_salario"));
        funcionario.setPes_cpf(rs.getString("fnc_pes_documento"));
        funcionario.setPes_nome(rs.getString("pes_nome"));
        java.sql.Date dtNasc = rs.getDate("pes_dtNascimento");
        funcionario.setPes_dt_nascimento(dtNasc != null ? dtNasc.toLocalDate() : null);
        funcionario.setPes_email(rs.getString("pes_email"));
        funcionario.setPes_ativo(rs.getBoolean("fnc_ativo"));

        int sexoId = rs.getInt("pes_sex_id");
        if (!rs.wasNull()) {
            funcionario.setPes_sexo(sexoDAO.buscarPorId(sexoId));
        }

        int tipoPessoaId = rs.getInt("pes_tipo_pessoa_id");
        if (!rs.wasNull()) {
            TipoPessoaVO tipoPessoa = tipoPessoaDAO.buscarPorId(tipoPessoaId);
            funcionario.setPes_tipo_pessoa(tipoPessoa);
        }

        CargoVO cargo = cargoDAO.buscarPorId(rs.getInt("fnc_cargo_id"));
        funcionario.setFnc_cargo(cargo);

        List<TelefoneVO> telefones = telefoneDAO.buscarPorCpf(funcionario.getPes_cpf());
        funcionario.setTelefone(telefones);

        List<EnderecoVO> enderecos = enderecoDAO.buscarPorDocumento(funcionario.getPes_cpf());
        funcionario.setEndereco(enderecos);

        List<ContratacaoVO> fases = contratacaoDAO.listarPorFuncionario(funcionario.getFnc_id());
        if (!fases.isEmpty()) {
            ContratacaoVO ultima = fases.get(fases.size() - 1);
            ultima.setFuncionario(funcionario);
            funcionario.setContratacao(ultima);
            funcionario.setFnc_dtContratacao(ultima.getContratacao_dtContratacao());
        }

        List<DemissaoVO> demissoes = demissaoDAO.listarPorFuncionario(funcionario.getFnc_id());
        if (!demissoes.isEmpty()) {
            DemissaoVO ultimaDemissao = demissoes.get(0);
            ultimaDemissao.setFuncionario(funcionario);
            funcionario.setDemissao(ultimaDemissao);
            funcionario.setFnc_dtDemissao(ultimaDemissao.getDemissao_data());
            funcionario.setFnc_motivo_demissao(ultimaDemissao.getDemissao_motivo());
        }

        return funcionario;
    }


    // busca completo por ID de funcionario
    public FuncionarioVO buscarFuncionarioCompletoPorId(int id) throws SQLException {
        String sql = """
        SELECT 
            f.fnc_id,
            f.fnc_numPis,
            f.fnc_salario,
            f.fnc_cargo_id,
            f.fnc_pes_documento,
            f.fnc_ativo,
            p.pes_nome,
            p.pes_dtNascimento,
            p.pes_email,
            p.pes_sex_id,
            p.pes_tipo_pessoa_id,
            p.pes_ativo
        FROM tb_funcionario f
        JOIN tb_pessoa p ON p.pes_documento = f.fnc_pes_documento
        WHERE f.fnc_id = ?
        """;

        try (PreparedStatement ps = con_fnc.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montarFuncionarioCompleto(rs);
                }
            }
        }
        return null;
    }


    // busca completo por CPF/documento da pessoa
    public FuncionarioVO buscarFuncionarioCompletoPorCpf(String cpf) throws SQLException {
        String sql = """
        SELECT 
            f.fnc_id,
            f.fnc_numPis,
            f.fnc_salario,
            f.fnc_cargo_id,
            f.fnc_pes_documento,
            f.fnc_ativo,
            p.pes_nome,
            p.pes_dtNascimento,
            p.pes_email,
            p.pes_sex_id,
            p.pes_tipo_pessoa_id,
            p.pes_ativo
        FROM tb_funcionario f
        JOIN tb_pessoa p ON p.pes_documento = f.fnc_pes_documento
        WHERE p.pes_documento = ?
        """;

        try (PreparedStatement ps = con_fnc.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montarFuncionarioCompleto(rs);
                }
            }
        }
        return null;
    }


    // busca completa por nome
    public List<FuncionarioVO> buscarFuncionariosCompletosPorNome(String nome) throws SQLException {
        String sql = """
        SELECT 
            f.fnc_id,
            f.fnc_numPis,
            f.fnc_salario,
            f.fnc_cargo_id,
            f.fnc_pes_documento,
            f.fnc_ativo,
            p.pes_nome,
            p.pes_dtNascimento,
            p.pes_email,
            p.pes_sex_id,
            p.pes_tipo_pessoa_id,
            p.pes_ativo
        FROM tb_funcionario f
        JOIN tb_pessoa p ON p.pes_documento = f.fnc_pes_documento
        WHERE p.pes_nome LIKE ?
        ORDER BY f.fnc_id
        """;

        List<FuncionarioVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con_fnc.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FuncionarioVO funcionario = montarFuncionarioCompleto(rs);
                    lista.add(funcionario);
                }
            }
        }
        return lista;
    }
}
