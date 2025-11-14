package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.rn.EstoqueRN;
import model.rn.ProdutoRN;
import model.vo.EstoqueVO;
import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class ProdutoEstoqueController implements Initializable {

    @FXML private Button btnProdutoEstoqueNovo;
    @FXML private Button btnProdutoEstoqueSalvar;
    @FXML private Button btnProdutoEstoqueEditar;
    @FXML private Button btnProdutoEstoqueAtualizar;
    @FXML private Button btnProdutoEstoqueExcluir;
    @FXML private Button btnProdutoEstoqueCancelar;
    @FXML private Button btnProdutoEstoqueBuscar;

    @FXML private ComboBox<TipoProdutoVO> cbProdutoTipo;
    @FXML private ComboBox<Integer> cbProdutoPeso;
    @FXML private ComboBox<String> cbEstoqueUnidade;

    @FXML private DatePicker dtpEstoqueDtCompra;
    @FXML private DatePicker dtpEstoqueDtValidade;

    @FXML private TextField txtProdutoEstoqueBusca;
    @FXML private TextField txtProdutoNome;
    @FXML private TextField txtEstoqueQtdMin;
    @FXML private TextField txtEstoqueQtdMax;
    @FXML private TextField txtEstoqueQtdInicial;
    @FXML private TextField txtEstoqueCusto;

    @FXML private TableView<EstoqueVO> tbvProdutoEstoque;
    @FXML private TableColumn<EstoqueVO, Number> tbcProdutoId;
    @FXML private TableColumn<EstoqueVO, String> tbcProdutoNome;
    @FXML private TableColumn<EstoqueVO, String> tbcProdutoTipo;
    @FXML private TableColumn<EstoqueVO, Number> tbcProdutoPeso;
    @FXML private TableColumn<EstoqueVO, Number> tbcProdutoEstoqueValor;
    @FXML private TableColumn<EstoqueVO, Boolean> tbcProdutoAtivo;

    @FXML private CheckBox chbProdutoAtivo;

    private final ProdutoRN produtoRN = new ProdutoRN();
    private final EstoqueRN estoqueRN = new EstoqueRN();

    private final ObservableList<EstoqueVO> dadosTabela = FXCollections.observableArrayList();
    private EstoqueVO estoqueSelecionado;
    private boolean emEdicao = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarCbProdutoPeso();
        carregarCbProdutoTipo();
        carregarCbEstoqueUnidade();
        configurarTabela();
        registrarListenerTabela();
        carregarTabelaEstoque();
        habilitarCampos(false);
        habilitarBotoes(false);
    }

    private void carregarCbProdutoTipo() {
        try {
            List<TipoProdutoVO> tiposProdutos = produtoRN.listarTiposProdutos();
            ObservableList<TipoProdutoVO> observableTipoProduto = FXCollections.observableArrayList(tiposProdutos);
            cbProdutoTipo.setItems(observableTipoProduto);
            info("Tipos de produto carregados.");
        } catch (Exception e) {
            erro("Erro ao carregar tipos de produtos.", e);
        }
    }

    private void carregarCbProdutoPeso() {
        cbProdutoPeso.setItems(FXCollections.observableArrayList(
            5, 10, 15, 20, 25, 30, 35, 40, 45, 50
        ));
    }

    private void carregarCbEstoqueUnidade() {
        if (cbEstoqueUnidade != null) {
            ObservableList<String> unidades = FXCollections.observableArrayList("UN", "KG", "SACA", "PCT");
            cbEstoqueUnidade.setItems(unidades);
            cbEstoqueUnidade.getSelectionModel().selectFirst();
            info("Unidades do estoque carregadas.");
        }
    }

    private double lerValorNumerico(TextField campo, String nomeCampo) throws Exception {
        if (campo == null || campo.getText() == null || campo.getText().isBlank()) {
            throw new Exception(nomeCampo + " e obrigatorio.");
        }
        String valorNormalizado = campo.getText().trim().replace(",", ".");
        try {
            return Double.parseDouble(valorNormalizado);
        } catch (NumberFormatException e) {
            throw new Exception("Valor invalido para " + nomeCampo + ".");
        }
    }

    private LocalDate lerData(DatePicker picker, String nomeCampo, boolean obrigatoria) throws Exception {
        if (picker == null || picker.getValue() == null) {
            if (obrigatoria) {
                throw new Exception("Selecione " + nomeCampo + ".");
            }
            return null;
        }
        return picker.getValue();
    }

    private String obterUnidadeSelecionada() {
        if (cbEstoqueUnidade == null) {
            return null;
        }
        return cbEstoqueUnidade.getSelectionModel().getSelectedItem();
    }

    private int obterPeso() {
        if (cbProdutoPeso != null && cbProdutoPeso.getValue() != null) {
            info("Peso do produto selecionado.");
            return cbProdutoPeso.getValue();
        }
        return -1;
    }

    private TipoProdutoVO obterTipoProduto() {
        try {
            if (cbProdutoTipo != null) {
                info("Tipo de produto selecionado.");
                return cbProdutoTipo.getValue();
            }
        } catch (Exception e) {
            erro("Erro ao obter tipo de produto.", e);
        }
        return null;
    }

    private ProdutoVO criarProduto() {
        try {
            String nomeProduto = null;
            if (txtProdutoNome.getText() != null) {
                nomeProduto = txtProdutoNome.getText().trim();
            }

            int peso = obterPeso();
            Boolean ativo = chbProdutoAtivo.isSelected();
            TipoProdutoVO tipoProdutoSelecionado = obterTipoProduto();

            ProdutoVO produtoCriado = new ProdutoVO(); 
            produtoCriado.setProduto_nome(nomeProduto);
            produtoCriado.setProduto_peso(peso);
            produtoCriado.setProduto_ativo(ativo);
            produtoCriado.setProduto_tipoPdt(tipoProdutoSelecionado);

            info("Produto criado a partir do formulario.");
            return produtoCriado;
        } catch (Exception e) {
            erro("Erro ao criar produto.", e);
            return null;
        }
    }

    private EstoqueVO criarEstoque(ProdutoVO produtoSalvo) {
        try {
            if (produtoSalvo == null || produtoSalvo.getProduto_id() <= 0) {
                throw new Exception("Salve o produto antes de criar o estoque.");
            }

            EstoqueVO estoque = new EstoqueVO();
            estoque.setEst_produto_id(produtoSalvo);
            estoque.setEst_qtdMin(lerValorNumerico(txtEstoqueQtdMin, "Quantidade minima"));
            estoque.setEst_qtdMax(lerValorNumerico(txtEstoqueQtdMax, "Quantidade maxima"));
            estoque.setQtdTotal(lerValorNumerico(txtEstoqueQtdInicial, "Quantidade de entrada"));
            estoque.setEst_custo(lerValorNumerico(txtEstoqueCusto, "Custo"));

            LocalDate dataCompra = lerData(dtpEstoqueDtCompra, "a data de compra", true);
            LocalDate dataValidade = lerData(dtpEstoqueDtValidade, "a data de validade", false);

            estoque.setEst_dtCompra(dataCompra);
            estoque.setEst_dtValidade(dataValidade);

            String unidade = obterUnidadeSelecionada();
            if (unidade == null || unidade.isBlank()) {
                throw new Exception("Selecione a unidade do estoque.");
            }
            estoque.setEst_lote(unidade);

            if (estoque.getEst_qtdMin() < 0) {
                throw new Exception("Quantidade minima deve ser maior ou igual a zero.");
            }
            if (estoque.getEst_qtdMax() <= 0 || estoque.getEst_qtdMax() < estoque.getEst_qtdMin()) {
                throw new Exception("Quantidade maxima deve ser maior que zero e maior/igual a minima.");
            }
            if (estoque.getQtdTotal() <= 0) {
                throw new Exception("Quantidade de entrada deve ser maior que zero.");
            }
            if (dataValidade != null && dataValidade.isBefore(dataCompra)) {
                throw new Exception("Data de validade nao pode ser anterior a data de compra.");
            }

            info("Estoque criado a partir do formulario.");
            return estoque;
        } catch (Exception e) {
            erro("Erro ao criar estoque.", e);
            alerta(e.getMessage());
            return null;
        }
    }

    private void configurarTabela() {
        tbcProdutoId.setCellValueFactory(celula -> {
            ProdutoVO produto = celula.getValue().getEst_produto_id();
            Integer valor = produto != null ? produto.getProduto_id() : null;
            return new ReadOnlyObjectWrapper<>(valor);
        });

        tbcProdutoNome.setCellValueFactory(celula -> {
            ProdutoVO produto = celula.getValue().getEst_produto_id();
            String valor = produto != null ? produto.getProduto_nome() : null;
            return new ReadOnlyObjectWrapper<>(valor);
        });

        tbcProdutoTipo.setCellValueFactory(celula -> {
            ProdutoVO produto = celula.getValue().getEst_produto_id();
            String valor = null;
            if (produto != null && produto.getProduto_tipoPdt() != null) {
                valor = produto.getProduto_tipoPdt().getTipoPdt_descricao();
            }
            return new ReadOnlyObjectWrapper<>(valor);
        });

        tbcProdutoPeso.setCellValueFactory(celula -> {
            ProdutoVO produto = celula.getValue().getEst_produto_id();
            Integer valor = produto != null ? produto.getProduto_peso() : null;
            return new ReadOnlyObjectWrapper<>(valor);
        });

        tbcProdutoEstoqueValor.setCellValueFactory(celula ->
            new ReadOnlyObjectWrapper<>(celula.getValue().getEst_custo())
        );

        tbcProdutoAtivo.setCellValueFactory(celula -> {
            ProdutoVO produto = celula.getValue().getEst_produto_id();
            Boolean valor = produto != null ? produto.getProduto_ativo() : Boolean.FALSE;
            return new ReadOnlyObjectWrapper<>(valor);
        });

        tbvProdutoEstoque.setItems(dadosTabela);
    }

    private void registrarListenerTabela() {
        tbvProdutoEstoque.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (emEdicao) {
                return;
            }
            montarProdutoEstoque(selecionado);
            atualizarBotoesSelecao(selecionado != null);
        });
    }

    private void carregarTabelaEstoque() {
        try {
            List<EstoqueVO> estoques = estoqueRN.listarTodosEstoques();
            dadosTabela.setAll(estoques);
            tbvProdutoEstoque.refresh();
        } catch (Exception e) {
            erro("Erro ao carregar tabela de produtos e estoque.", e);
        }
    }

    private void montarProdutoEstoque(EstoqueVO estoque) {
        estoqueSelecionado = estoque;
        if (estoque == null) {
            limparCampos();
            return;
        }

        ProdutoVO produto = estoque.getEst_produto_id();
        if (produto != null) {
            txtProdutoNome.setText(produto.getProduto_nome());
            cbProdutoPeso.setValue(produto.getProduto_peso());
            selecionarTipoProduto(produto.getProduto_tipoPdt());
            chbProdutoAtivo.setSelected(Boolean.TRUE.equals(produto.getProduto_ativo()));
        } else {
            txtProdutoNome.clear();
            cbProdutoPeso.setValue(null);
            cbProdutoTipo.setValue(null);
            chbProdutoAtivo.setSelected(false);
        }

        txtEstoqueQtdMin.setText(formatarNumero(estoque.getEst_qtdMin()));
        txtEstoqueQtdMax.setText(formatarNumero(estoque.getEst_qtdMax()));
        txtEstoqueQtdInicial.setText(formatarNumero(estoque.getQtdTotal()));
        txtEstoqueCusto.setText(formatarNumero(estoque.getEst_custo()));
        selecionarUnidade(estoque.getEst_lote());
        dtpEstoqueDtCompra.setValue(estoque.getEst_dtCompra());
        dtpEstoqueDtValidade.setValue(estoque.getEst_dtValidade());
    }

    private void selecionarTipoProduto(TipoProdutoVO tipo) {
        if (cbProdutoTipo == null) {
            return;
        }
        if (tipo == null) {
            cbProdutoTipo.getSelectionModel().clearSelection();
            return;
        }
        for (TipoProdutoVO item : cbProdutoTipo.getItems()) {
            if (item.getTipoPdt_id() == tipo.getTipoPdt_id()) {
                cbProdutoTipo.getSelectionModel().select(item);
                return;
            }
        }
        cbProdutoTipo.getSelectionModel().clearSelection();
    }

    private void selecionarUnidade(String unidade) {
        if (cbEstoqueUnidade == null) {
            return;
        }
        if (unidade == null || unidade.isBlank()) {
            cbEstoqueUnidade.getSelectionModel().clearSelection();
            return;
        }
        if (!cbEstoqueUnidade.getItems().contains(unidade)) {
            cbEstoqueUnidade.getItems().add(unidade);
        }
        cbEstoqueUnidade.getSelectionModel().select(unidade);
    }

    private String formatarNumero(double valor) {
        if (valor == (long) valor) {
            return String.format(Locale.US, "%d", (long) valor);
        }
        return String.format(Locale.US, "%.2f", valor);
    }

    private void atualizarBotoesSelecao(boolean selecionado) {
        btnProdutoEstoqueEditar.setDisable(!selecionado);
        btnProdutoEstoqueExcluir.setDisable(!selecionado);
        if (!emEdicao) {
            btnProdutoEstoqueAtualizar.setDisable(true);
        }
    }

    private boolean confirmarExclusao(String nomeProduto) {
        Alert alertaConfirmacao = new Alert(AlertType.CONFIRMATION);
        alertaConfirmacao.setTitle("Confirmar exclusao");
        alertaConfirmacao.setHeaderText("Deseja realmente excluir este produto?");
        alertaConfirmacao.setContentText(nomeProduto != null ? "Produto: " + nomeProduto : "");
        Optional<ButtonType> resultado = alertaConfirmacao.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    @FXML
    private void onNovoProduto(ActionEvent event) {
        emEdicao = false;
        estoqueSelecionado = null;
        tbvProdutoEstoque.getSelectionModel().clearSelection();
        limparCampos();
        habilitarCampos(true);
        btnProdutoEstoqueSalvar.setDisable(false);
        btnProdutoEstoqueAtualizar.setDisable(true);
        btnProdutoEstoqueEditar.setDisable(true);
        btnProdutoEstoqueExcluir.setDisable(true);
        info("Botao de novo pressionado.");
    }

    @FXML
    private void onSalvarProduto(ActionEvent event) {
        try {
            ProdutoVO produtoCriado = criarProduto();
            if (produtoCriado == null) {
                alerta("Erro ao criar produto.");
                return;
            }

            int produtoId = produtoRN.adicionarProduto(produtoCriado);
            produtoCriado.setProduto_id(produtoId);
            info("Produto salvo com ID " + produtoId + ".");

            EstoqueVO estoqueCriado = criarEstoque(produtoCriado);
            if (estoqueCriado == null) {
                return;
            }

            int estoqueId = estoqueRN.adicionarEstoque(estoqueCriado);
            info("Estoque salvo com ID " + estoqueId + ".");
            alerta("Produto e estoque cadastrados com sucesso.");

            habilitarCampos(false);
            btnProdutoEstoqueSalvar.setDisable(true);
            carregarTabelaEstoque();
            tbvProdutoEstoque.getSelectionModel().clearSelection();
            limparCampos();
        } catch (Exception e) {
            erro("Erro ao salvar produto/estoque.", e);
            alerta("Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void onBuscarProduto(ActionEvent event) {
        String termo = txtProdutoEstoqueBusca.getText();
        if (termo == null || termo.isBlank()) {
            carregarTabelaEstoque();
            return;
        }

        try {
            dadosTabela.clear();
            termo = termo.trim();
            if (termo.matches("\\d+")) {
                int id = Integer.parseInt(termo);
                ProdutoVO produto = produtoRN.buscarPorId(id);
                EstoqueVO estoque = estoqueRN.buscarEntradaPorProduto(id);
                if (estoque != null) {
                    dadosTabela.add(estoque);
                } else if (produto != null) {
                    EstoqueVO semEstoque = new EstoqueVO();
                    semEstoque.setEst_produto_id(produto);
                    dadosTabela.add(semEstoque);
                }
            } else {
                List<ProdutoVO> produtos = produtoRN.buscarPorNome(termo);
                for (ProdutoVO produto : produtos) {
                    EstoqueVO estoque = estoqueRN.buscarEntradaPorProduto(produto.getProduto_id());
                    if (estoque != null) {
                        dadosTabela.add(estoque);
                    } else {
                        EstoqueVO semEstoque = new EstoqueVO();
                        semEstoque.setEst_produto_id(produto);
                        dadosTabela.add(semEstoque);
                    }
                }
            }
            tbvProdutoEstoque.refresh();
            if (dadosTabela.isEmpty()) {
                alerta("Nenhum registro encontrado para a busca.");
            }
        } catch (Exception e) {
            erro("Erro ao buscar produtos.", e);
            alerta("Erro ao buscar: " + e.getMessage());
        }
    }

    @FXML
    private void onEditarProduto(ActionEvent event) {
        if (estoqueSelecionado == null) {
            alerta("Selecione um item da tabela para editar.");
            return;
        }
        emEdicao = true;
        habilitarCampos(true);
        btnProdutoEstoqueSalvar.setDisable(true);
        btnProdutoEstoqueAtualizar.setDisable(false);
        btnProdutoEstoqueEditar.setDisable(true);
        info("Modo edicao habilitado.");
    }

    @FXML
    private void onAtualizarProduto(ActionEvent event) {
        if (estoqueSelecionado == null) {
            alerta("Selecione um item para atualizar.");
            return;
        }

        try {
            ProdutoVO produtoEditado = criarProduto();
            if (produtoEditado == null) {
                alerta("Erro ao montar produto para atualizacao.");
                return;
            }
            produtoEditado.setProduto_id(estoqueSelecionado.getEst_produto_id().getProduto_id());

            produtoRN.editarProduto(produtoEditado);

            EstoqueVO estoqueAtualizado = criarEstoque(produtoEditado);
            if (estoqueAtualizado == null) {
                return;
            }
            estoqueAtualizado.setEst_id(estoqueSelecionado.getEst_id());

            estoqueRN.atualizarEstoque(estoqueAtualizado);

            alerta("Produto e estoque atualizados com sucesso.");
            emEdicao = false;
            habilitarCampos(false);
            btnProdutoEstoqueAtualizar.setDisable(true);
            carregarTabelaEstoque();
            tbvProdutoEstoque.getSelectionModel().clearSelection();
            limparCampos();
            atualizarBotoesSelecao(false);
        } catch (Exception e) {
            erro("Erro ao atualizar produto/estoque.", e);
            alerta("Erro ao atualizar: " + e.getMessage());
        }
    }

    @FXML
    private void onExcluirProduto(ActionEvent event) {
        if (estoqueSelecionado == null || estoqueSelecionado.getEst_produto_id() == null) {
            alerta("Selecione um item para excluir.");
            return;
        }

        String nomeProduto = estoqueSelecionado.getEst_produto_id().getProduto_nome();
        if (!confirmarExclusao(nomeProduto)) {
            info("Exclusao cancelada pelo usuario.");
            return;
        }

        try {
            int produtoId = estoqueSelecionado.getEst_produto_id().getProduto_id();
            produtoRN.excluirProduto(produtoId);
            alerta("Produto excluido (soft delete) com sucesso.");

            emEdicao = false;
            habilitarCampos(false);
            btnProdutoEstoqueSalvar.setDisable(true);
            btnProdutoEstoqueAtualizar.setDisable(true);
            btnProdutoEstoqueEditar.setDisable(true);
            btnProdutoEstoqueExcluir.setDisable(true);

            carregarTabelaEstoque();
            limparCampos();
            tbvProdutoEstoque.getSelectionModel().clearSelection();
        } catch (Exception e) {
            erro("Erro ao excluir produto.", e);
            alerta("Erro ao excluir: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelarProduto(ActionEvent event) {
        emEdicao = false;
        habilitarCampos(false);
        btnProdutoEstoqueSalvar.setDisable(true);
        btnProdutoEstoqueAtualizar.setDisable(true);
        limparCampos();
            tbvProdutoEstoque.getSelectionModel().clearSelection();
        atualizarBotoesSelecao(false);
    }

    private void habilitarCampos(boolean habilitado) {
        txtProdutoNome.setDisable(!habilitado);
        txtEstoqueQtdMin.setDisable(!habilitado);
        txtEstoqueQtdMax.setDisable(!habilitado);
        txtEstoqueQtdInicial.setDisable(!habilitado);
        txtEstoqueCusto.setDisable(!habilitado);
        cbProdutoTipo.setDisable(!habilitado);
        cbProdutoPeso.setDisable(!habilitado);
        cbEstoqueUnidade.setDisable(!habilitado);
        dtpEstoqueDtCompra.setDisable(!habilitado);
        dtpEstoqueDtValidade.setDisable(!habilitado);
        chbProdutoAtivo.setDisable(!habilitado);
    }
    
    private void habilitarBotoes(boolean habilitado) {
        btnProdutoEstoqueSalvar.setDisable(!habilitado);
        btnProdutoEstoqueEditar.setDisable(!habilitado);
        btnProdutoEstoqueAtualizar.setDisable(!habilitado);
        btnProdutoEstoqueExcluir.setDisable(!habilitado);
    }

    private void limparCampos() {
        txtProdutoNome.clear();
        txtEstoqueQtdMin.clear();
        txtEstoqueQtdMax.clear();
        txtEstoqueQtdInicial.clear();
        txtEstoqueCusto.clear();
        txtProdutoEstoqueBusca.clear();
        cbProdutoTipo.getSelectionModel().clearSelection();
        cbProdutoPeso.getSelectionModel().clearSelection();
        cbEstoqueUnidade.getSelectionModel().clearSelection();
        dtpEstoqueDtCompra.setValue(null);
        dtpEstoqueDtValidade.setValue(null);
        chbProdutoAtivo.setSelected(false);
        estoqueSelecionado = null;
    }

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    private void info(String msg) {
        System.out.println(GREEN + "[INFO:ProdutoEstoqueController] " + msg + RESET);
    }

    private void alerta(String msg) {
        System.out.println(YELLOW + "[AVISO:ProdutoEstoqueController] " + msg + RESET);
    }

    private void erro(String msg, Exception e) {
        System.err.println(RED + "[ERRO:ProdutoEstoqueController] " + msg + RESET);
        if (e != null) e.printStackTrace();
    }
}
