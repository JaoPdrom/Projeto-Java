package model.vo;

public class EndPostalVO {
    private int endP_id;
    private LogradouroVO endP_logradouro;
    private String endP_nomeRua;
    private String endP_cep;
    private BairroVO endP_bairro;
    private CidadeVO endP_cidade;
    private EstadoVO endP_estado;

    public EndPostalVO() {}

    public EndPostalVO(int endP_id, LogradouroVO endP_logradouro, String endP_nomeRua, String endP_cep, BairroVO endP_bairro, CidadeVO endP_cidade, EstadoVO endP_estado) {
        this.endP_id = endP_id;
        this.endP_logradouro = endP_logradouro;
        this.endP_nomeRua = endP_nomeRua;
        this.endP_cep = endP_cep;
        this.endP_bairro = endP_bairro;
        this.endP_cidade = endP_cidade;
        this.endP_estado = endP_estado;
    }

    public int getEndP_id() {
        return endP_id;
    }

    public void setEndP_id(int endP_id) {
        this.endP_id = endP_id;
    }

    public LogradouroVO getEndP_logradouro() {
        return endP_logradouro;
    }

    public void setEndP_logradouro(LogradouroVO endP_logradouro) {
        this.endP_logradouro = endP_logradouro;
    }

    public String getEndP_nomeRua() {
        return endP_nomeRua;
    }

    public void setEndP_nomeRua(String endP_nomeRua) {
        this.endP_nomeRua = endP_nomeRua;
    }

    public String getEndP_cep() {
        return endP_cep;
    }

    public void setEndP_cep(String endP_cep) {
        this.endP_cep = endP_cep;
    }

    public BairroVO getEndP_bairro() {
        return endP_bairro;
    }

    public void setEndP_bairro(BairroVO endP_bairro) {
        this.endP_bairro = endP_bairro;
    }

    public CidadeVO getEndP_cidade() {
        return endP_cidade;
    }

    public void setEndP_cidade(CidadeVO endP_cidade) {
        this.endP_cidade = endP_cidade;
    }

    public EstadoVO getEndP_estado() {
        return endP_estado;
    }

    public void setEndP_estado(EstadoVO endP_estado) {
        this.endP_estado = endP_estado;
    }
}
