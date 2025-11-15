package model.dao;

import model.vo.ContratacaoVO;
import model.vo.FuncionarioVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratacaoDAO {

    private final Connection con;

    public ContratacaoDAO(Connection con) {
        this.con = con;
    }

    public int adicionar(ContratacaoVO contratacao) throws SQLException {
        String sql = "INSERT INTO tb_contratacao (contratacao_fase, contratacao_data, contratacao_fnc_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, contratacao.getContratacao_fase());
            LocalDate dt = contratacao.getContratacao_dtContratacao();
            if (dt != null) {
                ps.setDate(2, java.sql.Date.valueOf(dt));
            } else {
                ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            }
            if (contratacao.getFuncionario() == null || contratacao.getFuncionario().getFnc_id() <= 0) {
                throw new SQLException("Funcionário inválido para registro de contratação.");
            }
            ps.setInt(3, contratacao.getFuncionario().getFnc_id());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public List<ContratacaoVO> listarPorFuncionario(int funcionarioId) throws SQLException {
        String sql = "SELECT contratacao_id, contratacao_fase, contratacao_data FROM tb_contratacao WHERE contratacao_fnc_id = ? ORDER BY contratacao_data";
        List<ContratacaoVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, funcionarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ContratacaoVO vo = new ContratacaoVO();
                    vo.setContratacao_id(rs.getInt("contratacao_id"));
                    vo.setContratacao_fase(rs.getString("contratacao_fase"));
                    java.sql.Date dt = rs.getDate("contratacao_data");
                    vo.setContratacao_dtContratacao(dt != null ? dt.toLocalDate() : null);
                    FuncionarioVO func = new FuncionarioVO();
                    func.setFnc_id(funcionarioId);
                    vo.setFuncionario(func);
                    lista.add(vo);
                }
            }
        }
        return lista;
    }

    public List<ContratacaoVO> listarFases() throws SQLException {
        String sql = "SELECT DISTINCT contratacao_fase FROM tb_contratacao ORDER BY contratacao_fase";
        List<ContratacaoVO> fases = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String fase = rs.getString("contratacao_fase");
                if (fase != null) {
                    ContratacaoVO vo = new ContratacaoVO();
                    vo.setContratacao_fase(fase);
                    fases.add(vo);
                }
            }
        }
        return fases;
    }
}
