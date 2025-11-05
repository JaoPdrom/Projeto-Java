package model.vo;

public class DebitoVO {
    private int deb_id;
    private VendaVO deb_venda_id;
    private Double deb_juros;
    private StatusDebitoVO deb_status;

    public DebitoVO() {}

    public DebitoVO(int deb_id, VendaVO deb_venda_id, Double deb_juros, StatusDebitoVO deb_status) {
        this.deb_id = deb_id;
        this.deb_venda_id = deb_venda_id;
        this.deb_juros = deb_juros;
        this.deb_status = deb_status;
    }

    public int getDeb_id() {
        return deb_id;
    }

    public void setDeb_id(int deb_id) {
        this.deb_id = deb_id;
    }

    public VendaVO getDeb_venda_id() {
        return deb_venda_id;
    }

    public void setDeb_venda_id(VendaVO deb_venda_id) {
        this.deb_venda_id = deb_venda_id;
    }

    public Double getDeb_juros() {
        return deb_juros;
    }

    public void setDeb_juros(Double deb_juros) {
        this.deb_juros = deb_juros;
    }

    public StatusDebitoVO getDeb_status() {
        return deb_status;
    }

    public void setDeb_status(StatusDebitoVO deb_status) {
        this.deb_status = deb_status;
    }
}
