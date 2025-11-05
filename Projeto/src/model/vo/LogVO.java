package model.vo;

import java.time.LocalDateTime;

public class LogVO {
    private int log_id;
    private String log_acao;
    private LocalDateTime log_dataHora;
    private int log_fnc_id;

    public LogVO() {}

    public LogVO(int log_id, String log_acao, LocalDateTime log_dataHora, FuncionarioVO log_fnc_id) {
        this.log_id = log_id;
        this.log_acao = log_acao;
        this.log_dataHora = log_dataHora;
        this.log_fnc_id = log_fnc_id.getFnc_id();
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public String getLog_acao() {
        return log_acao;
    }

    public void setLog_acao(String log_acao) {
        this.log_acao = log_acao;
    }

    public LocalDateTime getLog_dataHora() {
        return log_dataHora;
    }

    public void setLog_dataHora(LocalDateTime log_dataHora) {
        this.log_dataHora = log_dataHora;
    }

    public int getLog_fnc_id() {
        return log_fnc_id;
    }

    public void setLog_fnc_id(int log_fnc_id) {
        this.log_fnc_id = log_fnc_id;
    }
}
