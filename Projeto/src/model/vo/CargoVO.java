package model.vo;

import java.lang.invoke.StringConcatFactory;

public class CargoVO {
    private int car_id;
    private String cargo_descricao;

    public CargoVO() {}

    public CargoVO(int car_id, String cargo_descricao) {
        this.car_id = car_id;
        this.cargo_descricao = cargo_descricao;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getCargo_descricao() {
        return cargo_descricao;
    }

    public void setCargo_descricao(String cargo_descricao) {
        this.cargo_descricao = cargo_descricao;
    }
}
