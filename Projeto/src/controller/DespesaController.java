
package controller;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import model.dao.TipoDespesaDAO;
import model.rn.DespesaRN;
import model.vo.DespesaVO;
import model.vo.PessoaVO;
import model.vo.TipoDespesaVO;
import model.vo.TipoPessoaVO;

public class DespesaController implements Initializable {

    // elementos de botoes
    @FXML private Button btnDespesaNova;
    @FXML private Button btnDespesaSalvar;
    @FXML private Button btnDespesaEditar;
    @FXML private Button btnDespesaAtualizar;
    @FXML private Button btnDespesaCancelar;
    @FXML private Button btnDespesaExcluir;
    @FXML private Button btnDespesaNovoTipoDespesa;
    @FXML private Button cbDespesaBuscar;

    // elementos de combobox
    @FXML private ComboBox<TipoDespesaVO> cbDespesaBuscaTipoDespesa;
    @FXML private ComboBox<TipoDespesaVO> cbDespesaTipoDespesa;

    // elementos de radio button
    @FXML private RadioButton rBtnDespesaBuscaSemDatas;
    @FXML private RadioButton rBtnDespesaBuscaData;
    @FXML private RadioButton rBtnDespesaBuscaPeriodo;

    //elementos de date picker
    @FXML private DatePicker dtpDespesaBuscaDtInicial;
    @FXML private DatePicker dtpDespesaBuscaDtFinal;
    @FXML private DatePicker dtpDespesaDataRealizacao;

    // elementos de text field
    @FXML private TextField txtDespesaBusca;
    @FXML private TextField txtDespesaValorPago;
    @FXML private TextField txtDespesaDescricao;

    // elementos de table view e table column
    @FXML private TableView<DespesaVO> tbvDespesa;
    @FXML private TableColumn<DespesaVO, String> tbcDespesaId;
    @FXML private TableColumn<DespesaVO, String>tbcDespesaDescricao;
    @FXML private TableColumn<DespesaVO, String>tbcDespesaData;
    @FXML private TableColumn<DespesaVO, String>tbcDespesaValor;
    @FXML private TableColumn<DespesaVO, String>tbcDespesaTipo;  

    DespesaRN despesaRN = new DespesaRN();
    DespesaVO despesaVO = new DespesaVO();

    // carregadores de combobox
    private void carregarDespesaTipoDespesa() {
        try {
            List<TipoDespesaVO> tiposDespesa = despesaRN.listarTiposDespesaRN();
            ObservableList<TipoDespesaVO> observableTiposDespesa = FXCollections.observableArrayList(tiposDespesa);
            cbDespesaBuscaTipoDespesa.setItems(observableTiposDespesa);
            cbDespesaTipoDespesa.setItems(observableTiposDespesa);
            info("Lista de tipos de despesas carregadas.");
        } catch (Exception e) {
            erro("Erro ao listar tipos de despesas em busca", e);
        }
    }

    // selecao de combobox
    private TipoDespesaVO obterCbDespesaBuscaTipoDespesa() {
        if (cbDespesaBuscaTipoDespesa != null){
            info("Tipo despesa busca selecionada");
            return cbDespesaBuscaTipoDespesa.getValue();
        }
        return null;
    }

    private TipoDespesaVO obterCbDespesaTipoDespesa() {
        if (cbDespesaTipoDespesa != null){
            info("Tipo despesa selecionada");
            return cbDespesaTipoDespesa.getValue();
        }
        return null;
    }

    private DespesaVO criarDespesa() {
        
        try {
            //leitura dos campos de texto
            String descricao = null;
            if (txtDespesaDescricao.getText() != null) {
                descricao = txtDespesaDescricao.getText().trim();
            }

                String valorPago = txtDespesaValorPago.getText();
            if (valorPago == null || valorPago.isBlank()) {
                throw new IllegalArgumentException("Valor pago eh um campo obrigatorio.");
            }
            // Remove moeda/espacos (inclui NBSP) e normaliza separadores
            String valorLimpo = valorPago.replaceAll("[^\\d,.-]", "");
            valorLimpo = valorLimpo.replace(".", "").replace(",", ".");
            double valor;
            try { 
                valor = Double.parseDouble(valorLimpo); 
            } catch (NumberFormatException nfe) { 
                throw new IllegalArgumentException("Valor pago invalido."); 
            }

            TipoDespesaVO tipoDespesa = obterCbDespesaTipoDespesa();
            LocalDate dataRealizacao = dtpDespesaDataRealizacao.getValue();

            if (descricao == null || descricao.isBlank()) {
                throw new IllegalArgumentException("Descricao eh um campo obrigatorio.");
            }

            if (tipoDespesa == null) {
            throw new IllegalArgumentException("Tipo de despesa eh um campo obrigatorio.");
            }

            if (dataRealizacao == null){
                throw new IllegalArgumentException("Data de realizacao eh um campo obrigatorio.");
            }

            DespesaVO vo = new DespesaVO();
            vo.setDespesa_descricao(descricao);
            vo.setDespesa_dtRealizacao(dataRealizacao);
            vo.setDespesa_valor_pago(valor);
            vo.setDespesa_tipo(tipoDespesa);

            return vo;
        } catch (IllegalArgumentException e) {
            alerta("Erro de validação: " + e.getMessage());
            return null;
        } catch (Exception e) {
            erro("Erro ao criar despesa", e);
            return null;
        }
    }

    private void carregarTabela() {
        try {
            List<DespesaVO> todasDespesas = despesaRN.buscarTodas();
            ObservableList<DespesaVO> observableDespesas = FXCollections.observableArrayList(todasDespesas);

            tbcDespesaId.setCellValueFactory(cd -> {
                if (cd.getValue() != null && cd.getValue().getDespesa_id() > 0){
                    return new SimpleStringProperty(String.valueOf(cd.getValue().getDespesa_id()));
                } else {
                    return new SimpleStringProperty("");
                }
            });

            tbcDespesaDescricao.setCellValueFactory(cd -> {
                if (cd.getValue() != null && cd.getValue().getDespesa_descricao() != null){
                    return new SimpleStringProperty(String.valueOf(cd.getValue().getDespesa_descricao()));
                } else {
                    return new SimpleStringProperty("");
                }
            });


            tbcDespesaData.setCellValueFactory(cd -> {
                if (cd.getValue() != null && cd.getValue().getDespesa_dtRealizacao() != null){
                    return new SimpleStringProperty(String.valueOf(cd.getValue().getDespesa_dtRealizacao()));
                } else {
                    return new SimpleStringProperty("");
                }
            });

            tbcDespesaValor.setCellValueFactory(cd -> {
                if (cd.getValue() != null && cd.getValue().getDespesa_valor_pago() > 0){
                    return new SimpleStringProperty(String.valueOf(cd.getValue().getDespesa_valor_pago()));
                } else {
                    return new SimpleStringProperty("");
                }
            });

            tbcDespesaTipo.setCellValueFactory(cd -> {
                String tipoDespesa = "";
                if (cd.getValue() != null && cd.getValue().getDespesa_tipo() != null) {
                    if (cd.getValue().getDespesa_tipo().getTipoDespesa_nome() != null) {
                        tipoDespesa = cd.getValue().getDespesa_tipo().getTipoDespesa_nome();
                    } else if (cd.getValue().getDespesa_tipo().getTipoDespesa_id() > 0) {
                        tipoDespesa = String.valueOf(cd.getValue().getDespesa_tipo().getTipoDespesa_id());
                    }
                }
                return new SimpleStringProperty(tipoDespesa);
            });

            tbvDespesa.setItems(observableDespesas);
            info("Despesas carregadas na tabela.");
        } catch (Exception e) {
            erro("Erro ao carregar dados na tabeala", e);
        }
    }


    @FXML
    private void onNovaDespesa(ActionEvent event) {
        info("Botao nova pressionado");
        btnDespesaSalvar.setDisable(false);
    }

    @FXML void onSalvarDespesa(ActionEvent event) throws Exception {
        DespesaVO despesaCriada = criarDespesa();
        if (despesaCriada == null) {
            alerta("Erro ao criar despesa");
        }

        try {
            despesaRN.adicionarDespesa(despesaCriada);
            info("Despesa criada!");
            habilitarCampos(false);
        } catch (Exception e) {
            erro("Erro ao criar despesa", e);
        }
    }





    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTabela();
        habilitarCampos(false);
        carregarDespesaTipoDespesa();

        txtDespesaValorPago.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // perdeu o foco
                try {
                    double valor = Double.parseDouble(txtDespesaValorPago.getText().replace(",", "."));
                    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
                    txtDespesaValorPago.setText(nf.format(valor));
                } catch (NumberFormatException e) {
                    txtDespesaValorPago.setText("");
                }
            }
        });
    }


    // metodo de campos
    private void habilitarCampos(boolean habilitado){
        btnDespesaSalvar.setDisable(!habilitado);
        btnDespesaEditar.setDisable(!habilitado);
        btnDespesaAtualizar.setDisable(!habilitado);
        btnDespesaExcluir.setDisable(!habilitado);
        btnDespesaNovoTipoDespesa.setDisable(!habilitado);
        cbDespesaBuscar.setDisable(!habilitado);
    }

    // Cor ANSI para o console (funciona no VS Code, IntelliJ e CMD moderno)
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    private void info(String msg) {
        System.out.println(GREEN + "[INFO:DespesaController] " + msg + RESET);
    }

    private void alerta(String msg) {
        System.out.println(YELLOW + "[AVISO:DespesaController] " + msg + RESET);
    }

    private void erro(String msg, Exception e) {
        System.err.println(RED + "[ERRO:DespesaController] " + msg + RESET);
        if (e != null) e.printStackTrace();
    }

    private void limparFormulario() {
        try {
            txtDespesaDescricao.clear();
            txtDespesaValorPago.clear();
            dtpDespesaDataRealizacao.setValue(null);
            if (cbDespesaTipoDespesa != null) cbDespesaTipoDespesa.setValue(null);
        } catch (Exception ignore) {}
    }
}
