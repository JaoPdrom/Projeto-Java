package model.vo;

import java.util.Date;
import java.util.List;

public class HoleriteVO {
    private int holerite_id;
    private Date holerite_periodo;
    private double holerite_valor_liquido;
    private FuncionarioVO funcionario;
    private InfoEmpresaVO infoEmpresa;
    private List<HoleriteProventoVO> proventos;
    private List<HoleriteDescontoVO> descontos;

    public HoleriteVO() {}

    public HoleriteVO(int holerite_id, Date holerite_periodo, double holerite_valor_liquido, FuncionarioVO funcionario, InfoEmpresaVO infoEmpresa, List<HoleriteProventoVO> proventos, List<HoleriteDescontoVO> descontos) {
        this.holerite_id = holerite_id;
        this.holerite_periodo = holerite_periodo;
        this.holerite_valor_liquido = holerite_valor_liquido;
        this.funcionario = funcionario;
        this.infoEmpresa = infoEmpresa;
        this.proventos = proventos;
        this.descontos = descontos;
    }

    public int getHolerite_id() {
        return holerite_id;
    }

    public void setHolerite_id(int holerite_id) {
        this.holerite_id = holerite_id;
    }

    public Date getHolerite_periodo() {
        return holerite_periodo;
    }

    public void setHolerite_periodo(Date holerite_periodo) {
        this.holerite_periodo = holerite_periodo;
    }

    public double getHolerite_valor_liquido() {
        return holerite_valor_liquido;
    }

    public void setHolerite_valor_liquido(double holerite_valor_liquido) {
        this.holerite_valor_liquido = holerite_valor_liquido;
    }

    public FuncionarioVO getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioVO funcionario) {
        this.funcionario = funcionario;
    }

    public InfoEmpresaVO getInfoEmpresa() {
        return infoEmpresa;
    }

    public void setInfoEmpresa(InfoEmpresaVO infoEmpresa) {
        this.infoEmpresa = infoEmpresa;
    }

    public List<HoleriteProventoVO> getProventos() {
        return proventos;
    }

    public void setProventos(List<HoleriteProventoVO> proventos) {
        this.proventos = proventos;
    }

    public List<HoleriteDescontoVO> getDescontos() {
        return descontos;
    }

    public void setDescontos(List<HoleriteDescontoVO> descontos) {
        this.descontos = descontos;
    }
}
