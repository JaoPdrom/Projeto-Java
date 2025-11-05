package model.dao;

import model.vo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FuncionarioDAO {

    private Connection con_fnc;
    private PessoaDAO pessoaDAO;
    private CargoDAO cargoDAO;
    private SexoDAO sexoDAO;

    public FuncionarioDAO(Connection con_fnc) {
        this.con_fnc = con_fnc;
        this.pessoaDAO = new PessoaDAO(con_fnc);
        this.cargoDAO = new CargoDAO(con_fnc);
        this.sexoDAO = new SexoDAO(con_fnc);
    }

    // adicionar novo funcionario
    public int adicionarNovoFuncionario(FuncionarioVO funcionario) throws SQLException {
        // salva os dados da pessoa
//        pessoaDAO.adicionarNovaPessoa(funcionario);

        String sql = "INSERT INTO tb_funcionario (fnc_dtContratacao, fnc_dtDemissao, fnc_salario, fnc_cargo_id, fnc_pes_cpf) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement fnc_add = con_fnc.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            fnc_add.setDate(1, java.sql.Date.valueOf(funcionario.getFnc_dtContratacao()));

            fnc_add.setNull(2, java.sql.Types.DATE);

            fnc_add.setDouble(3, funcionario.getFnc_salario());
            fnc_add.setInt(4, funcionario.getFnc_cargo().getCar_id());
            fnc_add.setString(5, funcionario.getPes_cpf());
            fnc_add.executeUpdate();

            try (ResultSet rs = fnc_add.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // Atualiza dados de funcionário e pessoa dentro de uma transação
    public void atualizarFuncionario(FuncionarioVO funcionario) throws SQLException {
        String sqlPessoa = """
        UPDATE tb_pessoa
        SET pes_nome = ?, 
            pes_sex_id = ?, 
            pes_dtNascimento = ?, 
            pes_email = ?, 
            pes_ativo = ?
        WHERE pes_cpf = ?;
    """;

        String sqlFuncionario = """
        UPDATE tb_funcionario
        SET fnc_cargo_id = ?, 
            fnc_dtContratacao = ?, 
            fnc_dtDemissao = ?, 
            fnc_salario = ?
        WHERE fnc_pes_cpf = ?;
    """;

        // ❌ sem commit/rollback aqui!
        try (PreparedStatement psPessoa = con_fnc.prepareStatement(sqlPessoa);
             PreparedStatement psFunc = con_fnc.prepareStatement(sqlFuncionario)) {

            // --- Atualiza pessoa ---
            psPessoa.setString(1, funcionario.getPes_nome());
            psPessoa.setInt(2, funcionario.getPes_sexo().getSex_id());
            psPessoa.setDate(3, java.sql.Date.valueOf(funcionario.getPes_dt_nascimento()));
            psPessoa.setString(4, funcionario.getPes_email());
            psPessoa.setBoolean(5, funcionario.getPes_ativo());
            psPessoa.setString(6, funcionario.getPes_cpf());
            psPessoa.executeUpdate();

            // --- Atualiza funcionário ---
            psFunc.setInt(1, funcionario.getFnc_cargo().getCar_id());
            psFunc.setDate(2, java.sql.Date.valueOf(funcionario.getFnc_dtContratacao()));

            if (funcionario.getFnc_dtDemissao() != null) {
                psFunc.setDate(3, java.sql.Date.valueOf(funcionario.getFnc_dtDemissao()));
            } else {
                psFunc.setNull(3, java.sql.Types.DATE);
            }

            psFunc.setDouble(4, funcionario.getFnc_salario());
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
                    PessoaVO pessoa = pessoaDAO.buscarPesCpf(rs.getString("fnc_pes_cpf"));
                    CargoVO cargo = cargoDAO.buscarPorId(rs.getInt("fnc_cargo_id"));
                    funcionario = new FuncionarioVO(
                            pessoa.getPes_cpf(),
                            pessoa.getPes_nome(),
                            pessoa.getPes_sexo(),
                            pessoa.getPes_dt_nascimento(),
                            pessoa.getPes_email(),
                            pessoa.getPes_ativo(),
                            pessoa.getTelefone(),
                            pessoa.getEndereco(),
                            rs.getInt("fnc_id"),
                            rs.getDate("fnc_dtContratacao") != null ? rs.getDate("fnc_dtContratacao").toLocalDate() : null,
                            rs.getDate("fnc_dtDemissao") != null ? rs.getDate("fnc_dtDemissao").toLocalDate() : null,
                            rs.getDouble("fnc_salario"),
                            cargo,
                            pessoa
                    );
                }
            }
        }
        return funcionario;
    }


    // 🔍 Busca genérica por nome (igual PessoaDAO.buscarPesNome)
    public List<FuncionarioVO> buscarFuncNome(String nome) throws SQLException {
        String sql = """
            SELECT 
                f.fnc_id,
                f.fnc_dtContratacao,
                f.fnc_dtDemissao,
                f.fnc_salario,
                f.fnc_cargo_id,
                p.pes_cpf,
                p.pes_nome,
                p.pes_dtNascimento,
                p.pes_email,
                p.pes_sex_id,
                p.pes_ativo
            FROM tb_funcionario f
            JOIN tb_pessoa p ON f.fnc_pes_cpf = p.pes_cpf
            WHERE p.pes_nome LIKE ?
        """;

        List<FuncionarioVO> funcionarios = new ArrayList<>();

        try (PreparedStatement stmt = con_fnc.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FuncionarioVO func = new FuncionarioVO();

                    func.setPes_cpf(rs.getString("pes_cpf"));
                    func.setPes_nome(rs.getString("pes_nome"));
                    func.setPes_email(rs.getString("pes_email"));
                    func.setPes_dt_nascimento(rs.getDate("pes_dtNascimento").toLocalDate());
                    func.setPes_ativo(rs.getBoolean("pes_ativo"));
                    func.setPes_sexo(sexoDAO.buscarPorId(rs.getInt("pes_sex_id")));

                    CargoVO cargo = cargoDAO.buscarPorId(rs.getInt("fnc_cargo_id"));
                    func.setFnc_cargo(cargo);

                    func.setFnc_id(rs.getInt("fnc_id"));
                    func.setFnc_salario(rs.getDouble("fnc_salario"));
                    func.setFnc_dtContratacao(rs.getDate("fnc_dtContratacao").toLocalDate());
                    Date demissao = rs.getDate("fnc_dtDemissao");
                    func.setFnc_dtDemissao(demissao != null ? ((java.sql.Date) demissao).toLocalDate() : null);

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
            f.fnc_dtContratacao,
            f.fnc_dtDemissao,
            f.fnc_salario,
            f.fnc_cargo_id,
            p.pes_cpf,
            p.pes_nome,
            p.pes_dtNascimento,
            p.pes_email,
            p.pes_sex_id,
            p.pes_ativo,
            s.sex_descricao,
            c.cargo_descricao
        FROM tb_funcionario f
        JOIN tb_pessoa p ON f.fnc_pes_cpf = p.pes_cpf
        JOIN tb_sexo s ON p.pes_sex_id = s.sex_id
        JOIN tb_cargo c ON f.fnc_cargo_id = c.cargo_id
        WHERE p.pes_cpf = ?
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





    // Constrói um objeto FuncionarioVO completo a partir do ResultSet
    private FuncionarioVO extrairFuncionario(ResultSet rs) throws SQLException {
        FuncionarioVO funcionario = new FuncionarioVO();

        // === Campos herdados de PessoaVO ===
        funcionario.setPes_cpf(rs.getString("pes_cpf"));
        funcionario.setPes_nome(rs.getString("pes_nome"));
        funcionario.setPes_email(rs.getString("pes_email"));

        java.sql.Date dataNasc = rs.getDate("pes_dtNascimento");
        funcionario.setPes_dt_nascimento(dataNasc != null ? dataNasc.toLocalDate() : null);

        funcionario.setPes_ativo(rs.getBoolean("pes_ativo"));

        // === Sexo ===
        SexoVO sexo = new SexoVO();
        sexo.setSex_id(rs.getInt("pes_sex_id"));
        sexo.setSex_descricao(rs.getString("sex_descricao"));
        funcionario.setPes_sexo(sexo);

        // === Cargo ===
        CargoVO cargo = new CargoVO();
        cargo.setCar_id(rs.getInt("fnc_cargo_id")); // ✅ nome real da coluna
        cargo.setCargo_descricao(rs.getString("cargo_descricao"));
        funcionario.setFnc_cargo(cargo);

        // === Campos específicos de Funcionário ===
        funcionario.setFnc_id(rs.getInt("fnc_id"));
        funcionario.setFnc_salario(rs.getDouble("fnc_salario"));

        java.sql.Date dtCont = rs.getDate("fnc_dtContratacao");
        java.sql.Date dtDem = rs.getDate("fnc_dtDemissao");
        funcionario.setFnc_dtContratacao(dtCont != null ? dtCont.toLocalDate() : null);
        funcionario.setFnc_dtDemissao(dtDem != null ? dtDem.toLocalDate() : null);

        return funcionario;
    }


    // 🗑️ Soft delete do funcionário e pessoa
    public void deletarFuncionario(FuncionarioVO funcionario) throws SQLException {
        String sqlPessoa = "UPDATE tb_pessoa SET pes_ativo = 0 WHERE pes_cpf = ?";
        String sqlFuncionario = "UPDATE tb_funcionario SET fnc_ativo = 0 WHERE fnc_pes_cpf = ?";

        try (
                PreparedStatement psPessoa = con_fnc.prepareStatement(sqlPessoa);
                PreparedStatement psFunc = con_fnc.prepareStatement(sqlFuncionario)
        ) {
            // Desativa na tb_pessoa
            psPessoa.setString(1, funcionario.getPes_cpf());
            psPessoa.executeUpdate();

            // Desativa na tb_funcionario
            psFunc.setString(1, funcionario.getPes_cpf());
            psFunc.executeUpdate();
        }
    }


}
