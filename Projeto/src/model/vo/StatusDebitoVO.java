/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.vo;

public class StatusDebitoVO {
    private int statusDeb_id;
    private String statusDeb_descricao;

    public StatusDebitoVO() {
    }

    public StatusDebitoVO(int statusDeb_id, String statusDeb_descricao) {
        this.statusDeb_id = statusDeb_id;
        this.statusDeb_descricao = statusDeb_descricao;
    }

    public int getStatusDeb_id() {
        return statusDeb_id;
    }

    public void setStatusDeb_id(int statusDeb_id) {
        this.statusDeb_id = statusDeb_id;
    }

    public String getStatusDeb_descricao() {
        return statusDeb_descricao;
    }

    public void setStatusDeb_descricao(String statusDeb_descricao) {
        this.statusDeb_descricao = statusDeb_descricao;
    }
}
