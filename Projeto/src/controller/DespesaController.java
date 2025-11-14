
package controller;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Action;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
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

    //outros campos
    @FXML private Label DespesaID;

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
    private void selecionarItemTabelaDespesa(MouseEvent event) {
        try {
            DespesaVO despesaSelecionada = tbvDespesa.getSelectionModel().getSelectedItem();
            limparCamposDespesa();

            btnDespesaEditar.setDisable(false);
            btnDespesaExcluir.setDisable(false);

            if (despesaSelecionada == null){
                erro("Nenhuma despesa selecionada", null);
                return;
            }

            info("Despesa selecionada da tabela. " + despesaSelecionada.getDespesa_descricao());

            //carregamento de dados basico
            DespesaID.setText(String.valueOf(despesaSelecionada.getDespesa_id()));
            txtDespesaDescricao.setText(String.valueOf(despesaSelecionada.getDespesa_descricao()));
            txtDespesaValorPago.setText(String.valueOf(despesaSelecionada.getDespesa_valor_pago()));
            dtpDespesaDataRealizacao.setValue(despesaSelecionada.getDespesa_dtRealizacao());

            //obter tipo
            try {
                TipoDespesaVO tipoDespesaSelecionada = despesaSelecionada.getDespesa_tipo();
                if (tipoDespesaSelecionada != null && tipoDespesaSelecionada.getTipoDespesa_nome() != null){
                    cbDespesaTipoDespesa.setValue(tipoDespesaSelecionada);
                } else {
                    System.out.println("Tipo despesa nao informada para a despesa.");
                }
            } catch (Exception e) {
                erro("Erro ao definir tipo de despesa.", e);
            }
            
        } catch (Exception e) {
            erro("Erro ao gerar despesa selecionada", e);
        }
        
    }

    private int obterIdDespesaSelecionada(){
        try {
            if (tbvDespesa != null && tbvDespesa.getSelectionModel() != null) {
                DespesaVO selecionada = tbvDespesa.getSelectionModel().getSelectedItem();
                if (selecionada != null && selecionada.getDespesa_id() > 0) {
                    return selecionada.getDespesa_id();
                }
            }

            if (DespesaID != null && DespesaID.getText() != null) {
                String textoId = DespesaID.getText().trim();
                if (!textoId.isEmpty()) {
                    return Integer.parseInt(textoId);
                }
            }
        } catch (NumberFormatException e) {
            erro("ID de despesa invalido no formulario.", e);
        }
        return 0;
    }

    private void atualizarDespesa(){
        try {
            DespesaVO despesaAtualizar = criarDespesa();
            if (despesaAtualizar == null){
                alerta("Nao foi possivel montar a despesa para atualizar.");
                return;
            }

            int idAtual = obterIdDespesaSelecionada();
            if (idAtual <= 0){
                alerta("Selecione uma despesa na tabela antes de atualizar.");
                return;
            }
            despesaAtualizar.setDespesa_id(idAtual);

            despesaRN.atualizarDespesa(despesaAtualizar);
            info("Despesa atualizada.");
            carregarTabela();
            info("Tabela atualizada.");
            
        } catch (Exception e) {
            erro("Erro ao atualizar despesa.", e);
        }
    }


    @FXML
    private void onNovaDespesa(ActionEvent event) {
        habilitarCamposPadrao(true);
        info("Botao nova pressionado");
        btnDespesaSalvar.setDisable(false);
    }

    @FXML
    private void onSalvarDespesa(ActionEvent event) throws Exception {
        DespesaVO despesaCriada = criarDespesa();
        if (despesaCriada == null) {
            alerta("Erro ao criar despesa");
        }

        try {
            despesaRN.adicionarDespesa(despesaCriada);
            info("Despesa criada!");
            habilitarCampos(false);
            limparCamposDespesa();
            carregarTabela();
        } catch (Exception e) {
            erro("Erro ao criar despesa", e);
        }
    }

    @FXML
    private void onCancelarDespesa(ActionEvent event) {
        limparCamposDespesa();
        habilitarCampos(false);
        if (tbvDespesa != null && tbvDespesa.getSelectionModel() != null){
            tbvDespesa.getSelectionModel().clearSelection();
        }
        info("Botao cancelar pressionado.");
    }

    @FXML
    private void onEditarDespesa(ActionEvent event) {
        try {
            habilitarCampos(true);
            btnDespesaSalvar.setDisable(true);
            btnDespesaAtualizar.setDisable(false);
            info("Modo de edicao iniciado.");
        } catch (Exception e) {
            erro("Erro ao iniciar modo de edicao.", e);
        }
    }
    
    @FXML
    private void onAtualizarDespesa(ActionEvent event) {
        try {
            atualizarDespesa();
        } catch (Exception e) {
            erro("Erro na chamada de atualizar despesa.", e);
        }

    }

    private boolean confirmarExclusao(String descricao) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar exclusão");
        alerta.setHeaderText("Tem certeza que deseja excluir?");
        alerta.setContentText(descricao);
        return alerta.showAndWait()
                    .filter(ButtonType.OK::equals)
                    .isPresent();
    }

    @FXML
    private void onExcluirDespesa(ActionEvent event) {
        try {
            int idSelecionado = obterIdDespesaSelecionada();
            if (idSelecionado <= 0) {
                alerta("Selecione uma despesa na tabela antes de excluir.");
                return;
            }

            DespesaVO selecionada = tbvDespesa.getSelectionModel().getSelectedItem();
            String resumo;
            if (selecionada != null){
                resumo = selecionada.getDespesa_descricao();
            } else {
                resumo = "ID: " + idSelecionado;
            }
            if (!confirmarExclusao("Excluir despesa: " + resumo + "?")){
                return;
            }

            despesaRN.removerDespesa(idSelecionado);
            info("Despesa removida com sucesso.");
            limparCamposDespesa();

            if (tbvDespesa.getSelectionModel() != null) {
                tbvDespesa.getSelectionModel().clearSelection();
            }

            carregarTabela(); // recarrega a TableView
        } catch (Exception e) {
            erro("Erro ao excluir despesa.", e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTabela();
        habilitarCampos(false);
        habilitarCamposPadrao(false);
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


    // metodo de campos -- revisar, nem todos os campos precisam ser desabilitados
    private void habilitarCampos(boolean habilitado){
        btnDespesaSalvar.setDisable(!habilitado); 
        btnDespesaEditar.setDisable(!habilitado);
        btnDespesaAtualizar.setDisable(!habilitado);
        btnDespesaExcluir.setDisable(!habilitado);
        btnDespesaNovoTipoDespesa.setDisable(!habilitado);
        cbDespesaBuscar.setDisable(!habilitado);
        DespesaID.setDisable(!habilitado);
    }

    // metodo para desabilitar apenas campos que nao pertencem a toolbar
    private void habilitarCamposPadrao(boolean habilitado){
        txtDespesaDescricao.setDisable(!habilitado); // tirar
        txtDespesaValorPago.setDisable(!habilitado); // tirar
        dtpDespesaDataRealizacao.setDisable(!habilitado); // tirar
        cbDespesaTipoDespesa.setDisable(!habilitado); // tirar
    }

    private void limparCamposDespesa(){
        txtDespesaDescricao.clear();
        txtDespesaValorPago.clear();
        cbDespesaTipoDespesa.setValue(null);
        dtpDespesaDataRealizacao.setValue(null);
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
