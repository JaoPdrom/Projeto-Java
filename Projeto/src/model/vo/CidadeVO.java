package model.vo;

public class CidadeVO {
    private int cid_id;
    private String cid_descricao;
//    private EstadoVO estado;

    public CidadeVO() {}

    public CidadeVO(int cid_id, String cid_descricao) {
        this.cid_id = cid_id;
        this.cid_descricao = cid_descricao;
//        this.estado = estado;
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

//    public EstadoVO getEstado() {
//        return estado;
//    }
//
//    public void setEstado(EstadoVO estado) {
//        this.estado = estado;
//    }

    @Override
    public String toString() {
        return cid_descricao;
    }
}
