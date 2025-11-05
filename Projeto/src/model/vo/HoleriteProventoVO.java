/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.vo;

public class HoleriteProventoVO {
    private int holeriteProvento_id;
    private HoleriteVO holerite;
    private ProventoVO provento;
    private double holeriteProvento_valor;

    public HoleriteProventoVO() {}

    public HoleriteProventoVO(int holeriteProvento_id, HoleriteVO holerite, ProventoVO provento, double holeriteProvento_valor) {
        this.holeriteProvento_id = holeriteProvento_id;
        this.holerite = holerite;
        this.provento = provento;
        this.holeriteProvento_valor = holeriteProvento_valor;
    }

    public int getHoleriteProvento_id() {
        return holeriteProvento_id;
    }

    public void setHoleriteProvento_id(int holeriteProvento_id) {
        this.holeriteProvento_id = holeriteProvento_id;
    }

    public HoleriteVO getHolerite() {
        return holerite;
    }

    public void setHolerite(HoleriteVO holerite) {
        this.holerite = holerite;
    }

    public ProventoVO getProvento() {
        return provento;
    }

    public void setProvento(ProventoVO provento) {
        this.provento = provento;
    }

    public double getHoleriteProvento_valor() {
        return holeriteProvento_valor;
    }

    public void setHoleriteProvento_valor(double holeriteProvento_valor) {
        this.holeriteProvento_valor = holeriteProvento_valor;
    }
}
