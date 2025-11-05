package model.vo;

public class HoleriteDescontoVO {
    private int holeriteDesconto_id;
    private HoleriteVO holerite;
    private DescontoVO desconto;
    private double holeriteDesconto_valor;

    public HoleriteDescontoVO() {}

    public HoleriteDescontoVO(int holeriteDesconto_id, HoleriteVO holerite, DescontoVO desconto, double holeriteDesconto_valor) {
        this.holeriteDesconto_id = holeriteDesconto_id;
        this.holerite = holerite;
        this.desconto = desconto;
        this.holeriteDesconto_valor = holeriteDesconto_valor;
    }

    public int getHoleriteDesconto_id() {
        return holeriteDesconto_id;
    }

    public void setHoleriteDesconto_id(int holeriteDesconto_id) {
        this.holeriteDesconto_id = holeriteDesconto_id;
    }

    public HoleriteVO getHolerite() {
        return holerite;
    }

    public void setHolerite(HoleriteVO holerite) {
        this.holerite = holerite;
    }

    public DescontoVO getDesconto() {
        return desconto;
    }

    public void setDesconto(DescontoVO desconto) {
        this.desconto = desconto;
    }

    public double getHoleriteDesconto_valor() {
        return holeriteDesconto_valor;
    }

    public void setHoleriteDesconto_valor(double holeriteDesconto_valor) {
        this.holeriteDesconto_valor = holeriteDesconto_valor;
    }
}
