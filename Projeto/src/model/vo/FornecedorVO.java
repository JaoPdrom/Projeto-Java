package model.vo;

public class FornecedorVO {
    private String forn_cnpj;
    private String forn_razaoSocial;
    private String forn_nomeFantasia;

    public FornecedorVO() {}

    public FornecedorVO(String forn_cnpj, String forn_razaoSocial, String forn_nomeFantasia) {
        this.forn_cnpj = forn_cnpj;
        this.forn_razaoSocial = forn_razaoSocial;
        this.forn_nomeFantasia = forn_nomeFantasia;
    }

    public String getForn_cnpj() {
        return forn_cnpj;
    }

    public void setForn_cnpj(String forn_cnpj) {
        this.forn_cnpj = forn_cnpj;
    }

    public String getForn_razaoSocial() {
        return forn_razaoSocial;
    }

    public void setForn_razaoSocial(String forn_razaoSocial) {
        this.forn_razaoSocial = forn_razaoSocial;
    }

    public String getForn_nomeFantasia() {
        return forn_nomeFantasia;
    }

    public void setForn_nomeFantasia(String forn_nomeFantasia) {
        this.forn_nomeFantasia = forn_nomeFantasia;
    }
}
