package model.dao;

import model.vo.DemissaoVO;
import model.vo.FuncionarioVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DemissaoDAO {

    private final Connection con;

    public DemissaoDAO(Connection con) {
        this.con = con;
    }

    public int registrarDemissao(DemissaoVO demissao) throws SQLException {
        String sql = "INSERT INTO tb_demissao (demissao_data, demissao_motivo, demissao_fnc_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            LocalDate data = demissao.getDemissao_data();
            if (data == null) {
                data = LocalDate.now();
            }
            ps.setDate(1, java.sql.Date.valueOf(data));
            ps.setString(2, demissao.getDemissao_motivo());
            if (demissao.getFuncionario() == null || demissao.getFuncionario().getFnc_id() <= 0) {
                throw new SQLException("Funcionário inválido para registro de demissão.");
            }
            ps.setInt(3, demissao.getFuncionario().getFnc_id());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public List<DemissaoVO> listarPorFuncionario(int funcionarioId) throws SQLException {
        String sql = "SELECT demissao_id, demissao_data, demissao_motivo FROM tb_demissao WHERE demissao_fnc_id = ? ORDER BY demissao_data DESC";
        List<DemissaoVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, funcionarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DemissaoVO vo = new DemissaoVO();
                    vo.setDemissao_id(rs.getInt("demissao_id"));
                    java.sql.Date dt = rs.getDate("demissao_data");
                    vo.setDemissao_data(dt != null ? dt.toLocalDate() : null);
                    vo.setDemissao_motivo(rs.getString("demissao_motivo"));
                    FuncionarioVO func = new FuncionarioVO();
                    func.setFnc_id(funcionarioId);
                    vo.setFuncionario(func);
                    lista.add(vo);
                }
            }
        }
        return lista;
    }
}
