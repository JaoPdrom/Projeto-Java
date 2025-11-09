package model.vo;

public class TipoPessoaVO {
    private int tipo_pessoa_id;
    private String codigo; // 'F' ou 'J'
    private String descricao;

    public TipoPessoaVO() {}

    public TipoPessoaVO(int id, String codigo, String descricao) {
        this.tipo_pessoa_id = id;
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getTipo_pessoa_id() { 
        return tipo_pessoa_id; 
    }

    public void setTipo_pessoa_id(int tipo_pessoa_id) { 
        this.tipo_pessoa_id = tipo_pessoa_id; 
    }

    public String getCodigo() { 
        return codigo; 
    }

    public void setCodigo(String codigo) { 
        this.codigo = codigo; 
    }

    public String getDescricao() { 
        return descricao; 
    }

    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
    }

    @Override
    public String toString() {
        return codigo + " - " + descricao;
    }

}

