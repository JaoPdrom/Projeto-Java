package model.dao;

import model.vo.ContratacaoVO;
import model.vo.FaseContratacaoVO;
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
    private FaseContratacaoDAO faseContratacaoDAO;

    public ContratacaoDAO(Connection con) {
        this.con = con;
        this.faseContratacaoDAO = new FaseContratacaoDAO(con);
    }

    public int adicionar(ContratacaoVO contratacao) throws SQLException {
        String sql = "INSERT INTO tb_contratacao (contratacao_fase_id, contratacao_data, contratacao_fnc_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Verifica se tem fase válida
            int faseId = 0;
            if (contratacao.getFase_contratacao() != null && contratacao.getFase_contratacao().getFase_contratacao_id() > 0) {
                faseId = contratacao.getFase_contratacao().getFase_contratacao_id();
            } else if (contratacao.getContratacao_fase() != null && !contratacao.getContratacao_fase().isBlank()) {
                // Compatibilidade: busca fase por descrição se não tiver ID
                List<FaseContratacaoVO> fases = faseContratacaoDAO.buscarTodas();
                for (FaseContratacaoVO fase : fases) {
                    if (fase.getFase_contratacao_descricao().equalsIgnoreCase(contratacao.getContratacao_fase())) {
                        faseId = fase.getFase_contratacao_id();
                        contratacao.setFase_contratacao(fase);
                        break;
                    }
                }
                // Se não encontrou, busca por "Contratação" como padrão
                if (faseId == 0) {
                    for (FaseContratacaoVO fase : fases) {
                        if (fase.getFase_contratacao_descricao().equalsIgnoreCase("Contratação")) {
                            faseId = fase.getFase_contratacao_id();
                            contratacao.setFase_contratacao(fase);
                            break;
                        }
                    }
                }
            }
            
            if (faseId <= 0) {
                throw new SQLException("Fase de contratação inválida.");
            }
            
            ps.setInt(1, faseId);
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
        String sql = "SELECT c.contratacao_id, c.contratacao_fase_id, c.contratacao_data, " +
                     "f.fase_contratacao_id, f.fase_contratacao_descricao, f.fase_contratacao_ativo " +
                     "FROM tb_contratacao c " +
                     "INNER JOIN tb_fase_contratacao f ON c.contratacao_fase_id = f.fase_contratacao_id " +
                     "WHERE c.contratacao_fnc_id = ? ORDER BY c.contratacao_data";
        List<ContratacaoVO> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, funcionarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ContratacaoVO vo = new ContratacaoVO();
                    vo.setContratacao_id(rs.getInt("contratacao_id"));
                    
                    FaseContratacaoVO fase = new FaseContratacaoVO();
                    fase.setFase_contratacao_id(rs.getInt("fase_contratacao_id"));
                    fase.setFase_contratacao_descricao(rs.getString("fase_contratacao_descricao"));
                    fase.setFase_contratacao_ativo(rs.getBoolean("fase_contratacao_ativo"));
                    vo.setFase_contratacao(fase);
                    
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

    @Deprecated
    public List<ContratacaoVO> listarFases() throws SQLException {
        // Método mantido para compatibilidade, mas agora usa FaseContratacaoDAO
        List<FaseContratacaoVO> fases = faseContratacaoDAO.buscarTodas();
        List<ContratacaoVO> resultado = new ArrayList<>();
        for (FaseContratacaoVO fase : fases) {
            ContratacaoVO vo = new ContratacaoVO();
            vo.setFase_contratacao(fase);
            resultado.add(vo);
        }
        return resultado;
    }
}
