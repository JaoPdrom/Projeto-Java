/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.vo;

public class TipoClienteVO {
    private int tipo_cliente_id;
    private String tipo_cliente_descricao;

    public TipoClienteVO() {}

    public TipoClienteVO(int tipo_cliente_id, String tipo_cliente_descricao) {
        this.tipo_cliente_id = tipo_cliente_id;
        this.tipo_cliente_descricao = tipo_cliente_descricao;
    }

    public int getTipo_cliente_id() {
        return tipo_cliente_id;
    }

    public void setTipo_cliente_id(int tipo_cliente_id) {
        this.tipo_cliente_id = tipo_cliente_id;
    }

    public String getTipo_cliente_descricao() {
        return tipo_cliente_descricao;
    }

    public void setTipo_cliente_descricao(String tipo_cliente_descricao) {
        this.tipo_cliente_descricao = tipo_cliente_descricao;
    }
}
