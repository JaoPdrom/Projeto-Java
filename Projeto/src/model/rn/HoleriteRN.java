/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.rn;

public class HoleriteRN {

    // Gera holerite para um funcionário em um período, com proventos e descontos, calculando valor líquido
    public int gerarHolerite(model.vo.FuncionarioVO funcionario,
                             java.util.Date periodo,
                             model.vo.InfoEmpresaVO empresa,
                             java.util.List<model.vo.HoleriteProventoVO> proventos,
                             java.util.List<model.vo.HoleriteDescontoVO> descontos) throws Exception {
        java.sql.Connection con = null;
        try {
            if (funcionario == null || funcionario.getFnc_id() <= 0) throw new Exception("Funcionário inválido.");
            if (periodo == null) throw new Exception("Período do holerite é obrigatório.");
            if (empresa == null || empresa.getEmp_cnpj() == null || empresa.getEmp_cnpj().isBlank())
                throw new Exception("Empresa (CNPJ) é obrigatória.");

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.FuncionarioDAO funcionarioDAO = new model.dao.FuncionarioDAO(con);
            model.dao.InfoEmpresaDAO infoDAO = new model.dao.InfoEmpresaDAO(con);
            model.dao.HoleriteDAO holeriteDAO = new model.dao.HoleriteDAO(con);
            model.dao.HoleriteProventoDAO holProvDAO = new model.dao.HoleriteProventoDAO(con);
            model.dao.HoleriteDescontoDAO holDescDAO = new model.dao.HoleriteDescontoDAO(con);

            if (funcionarioDAO.buscarPorId(funcionario.getFnc_id()) == null) {
                throw new Exception("Funcionário não encontrado: id=" + funcionario.getFnc_id());
            }
            if (infoDAO.buscarPorCnpj(empresa.getEmp_cnpj()) == null) {
                throw new Exception("Empresa não encontrada: cnpj=" + empresa.getEmp_cnpj());
            }

            double base = funcionario.getFnc_salario();
            double somaProv = 0.0;
            if (proventos != null) {
                for (model.vo.HoleriteProventoVO p : proventos) {
                    if (p == null || p.getProvento() == null || p.getProvento().getProvento_id() <= 0)
                        throw new Exception("Provento inválido.");
                    if (p.getHoleriteProvento_valor() < 0) throw new Exception("Valor de provento não pode ser negativo.");
                    somaProv += p.getHoleriteProvento_valor();
                }
            }
            double somaDesc = 0.0;
            if (descontos != null) {
                for (model.vo.HoleriteDescontoVO d : descontos) {
                    if (d == null || d.getDesconto() == null || d.getDesconto().getDesconto_id() <= 0)
                        throw new Exception("Desconto inválido.");
                    if (d.getHoleriteDesconto_valor() < 0) throw new Exception("Valor de desconto não pode ser negativo.");
                    somaDesc += d.getHoleriteDesconto_valor();
                }
            }

            double liquido = base + somaProv - somaDesc;

            model.vo.HoleriteVO hol = new model.vo.HoleriteVO();
            hol.setFuncionario(funcionario);
            hol.setInfoEmpresa(empresa);
            hol.setHolerite_periodo(periodo);
            hol.setHolerite_valor_liquido(liquido);

            int holId = holeriteDAO.adicionarNovo(hol);
            if (holId <= 0) throw new Exception("Falha ao gerar holerite.");
            hol.setHolerite_id(holId);

            if (proventos != null) {
                for (model.vo.HoleriteProventoVO p : proventos) {
                    p.setHolerite(hol);
                    holProvDAO.adicionarNovo(p);
                }
            }
            if (descontos != null) {
                for (model.vo.HoleriteDescontoVO d : descontos) {
                    d.setHolerite(hol);
                    holDescDAO.adicionarNovo(d);
                }
            }

            con.commit();
            return holId;
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao gerar holerite: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    // Atualiza valores (recalcula líquido) e itens (upsert simples)
    public void atualizarHolerite(model.vo.HoleriteVO holerite) throws Exception {
        java.sql.Connection con = null;
        try {
            if (holerite == null || holerite.getHolerite_id() <= 0) throw new Exception("Holerite inválido para atualização.");
            if (holerite.getFuncionario() == null || holerite.getFuncionario().getFnc_id() <= 0) throw new Exception("Funcionário inválido.");
            if (holerite.getInfoEmpresa() == null || holerite.getInfoEmpresa().getEmp_cnpj() == null) throw new Exception("Empresa inválida.");
            if (holerite.getHolerite_periodo() == null) throw new Exception("Período é obrigatório.");

            con = model.dao.ConexaoDAO.getConexao();
            con.setAutoCommit(false);

            model.dao.HoleriteDAO holeriteDAO = new model.dao.HoleriteDAO(con);
            model.dao.HoleriteProventoDAO holProvDAO = new model.dao.HoleriteProventoDAO(con);
            model.dao.HoleriteDescontoDAO holDescDAO = new model.dao.HoleriteDescontoDAO(con);

            if (holeriteDAO.buscarPorId(holerite.getHolerite_id()) == null) {
                throw new Exception("Holerite não encontrado.");
            }

            double base = holerite.getFuncionario().getFnc_salario();
            double somaProv = 0.0;
            if (holerite.getProventos() != null) {
                for (model.vo.HoleriteProventoVO p : holerite.getProventos()) {
                    somaProv += p.getHoleriteProvento_valor();
                    p.setHolerite(holerite);
                    model.vo.HoleriteProventoVO ex = holProvDAO.buscarPorId(holerite.getHolerite_id(), p.getProvento().getProvento_id());
                    if (ex == null) holProvDAO.adicionarNovo(p); else holProvDAO.atualizar(p);
                }
            }

            double somaDesc = 0.0;
            if (holerite.getDescontos() != null) {
                for (model.vo.HoleriteDescontoVO d : holerite.getDescontos()) {
                    somaDesc += d.getHoleriteDesconto_valor();
                    d.setHolerite(holerite);
                    model.vo.HoleriteDescontoVO ex = holDescDAO.buscarPorId(holerite.getHolerite_id(), d.getDesconto().getDesconto_id());
                    if (ex == null) holDescDAO.adicionarNovo(d); else holDescDAO.atualizar(d);
                }
            }

            holerite.setHolerite_valor_liquido(base + somaProv - somaDesc);
            holeriteDAO.atualizarPorId(holerite);

            con.commit();
        } catch (java.sql.SQLException e) {
            if (con != null) con.rollback();
            throw new Exception("Erro ao atualizar holerite: " + e.getMessage(), e);
        } finally {
            if (con != null) con.close();
        }
    }

    public model.vo.HoleriteVO buscarPorId(int id) throws Exception {
        if (id <= 0) throw new Exception("ID inválido.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.HoleriteDAO holDAO = new model.dao.HoleriteDAO(con);
            model.dao.HoleriteProventoDAO holProvDAO = new model.dao.HoleriteProventoDAO(con);
            model.dao.HoleriteDescontoDAO holDescDAO = new model.dao.HoleriteDescontoDAO(con);
            model.vo.HoleriteVO hol = holDAO.buscarPorId(id);
            if (hol != null) {
                hol.setProventos(holProvDAO.buscarPorHolerite(id));
                hol.setDescontos(holDescDAO.buscarPorHolerite(id));
            }
            return hol;
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar holerite: " + e.getMessage(), e);
        }
    }

    public java.util.List<model.vo.HoleriteVO> buscarPorFuncionario(int fncId) throws Exception {
        if (fncId <= 0) throw new Exception("Funcionário inválido.");
        try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
            model.dao.HoleriteDAO holDAO = new model.dao.HoleriteDAO(con);
            model.dao.HoleriteProventoDAO holProvDAO = new model.dao.HoleriteProventoDAO(con);
            model.dao.HoleriteDescontoDAO holDescDAO = new model.dao.HoleriteDescontoDAO(con);
            java.util.List<model.vo.HoleriteVO> lista = holDAO.buscarPorFuncionario(fncId);
            for (model.vo.HoleriteVO h : lista) {
                h.setProventos(holProvDAO.buscarPorHolerite(h.getHolerite_id()));
                h.setDescontos(holDescDAO.buscarPorHolerite(h.getHolerite_id()));
            }
            return lista;
        } catch (java.sql.SQLException e) {
            throw new Exception("Erro ao buscar holerites por funcionário: " + e.getMessage(), e);
        }
    }
}
