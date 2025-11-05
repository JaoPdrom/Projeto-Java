package model.vo;

public class InfoEmpresaVO {
    private String emp_cnpj;
    private String emp_nome;
    private EnderecoVO emp_end_id;
    private TelefoneVO emp_tel_id;

    public InfoEmpresaVO() {}

    public InfoEmpresaVO(String emp_cnpj, String emp_nome, EnderecoVO emp_end_id, TelefoneVO emp_tel_id) {
        this.emp_cnpj = emp_cnpj;
        this.emp_nome = emp_nome;
        this.emp_end_id = emp_end_id;
        this.emp_tel_id = emp_tel_id;
    }

    public String getEmp_cnpj() {
        return emp_cnpj;
    }

    public void setEmp_cnpj(String emp_cnpj) {
        this.emp_cnpj = emp_cnpj;
    }

    public String getEmp_nome() {
        return emp_nome;
    }

    public void setEmp_nome(String emp_nome) {
        this.emp_nome = emp_nome;
    }

    public EnderecoVO getEmp_end_id() {
        return emp_end_id;
    }

    public void setEmp_end_id(EnderecoVO emp_end_id) {
        this.emp_end_id = emp_end_id;
    }

    public TelefoneVO getEmp_tel_id() {
        return emp_tel_id;
    }

    public void setEmp_tel_id(TelefoneVO emp_tel_id) {
        this.emp_tel_id = emp_tel_id;
    }
}
