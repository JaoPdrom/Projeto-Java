/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.vo;

public class CidadeVO {
    private int cid_id;
    private String cid_descricao;

    public CidadeVO() {}

    public CidadeVO(int cid_id, String cid_descricao) {
        this.cid_id = cid_id;
        this.cid_descricao = cid_descricao;
    }

    public int getCid_id() {
        return cid_id;
    }

    public void setCid_id(int cid_id) {
        this.cid_id = cid_id;
    }

    public String getCid_descricao() {
        return cid_descricao;
    }

    public void setCid_descricao(String cid_descricao) {
        this.cid_descricao = cid_descricao;
    }

    @Override
    public String toString() {
        return cid_descricao;
    }
}
