package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.Action;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.dao.StatusPedidoDAO;
import model.vo.FuncionarioVO;
import model.vo.ProdutoVO;
import model.vo.StatusPedidoVO;
import model.vo.StatusVendaVO;
import model.vo.TipoEntregaVO;
import model.vo.TipoPagamentoVO;
import model.vo.VendaVO;

public class VendaController implements Initializable{

    @FXML private Button btnNovaVenda;
    @FXML private Button btnVendaBuscar;
    @FXML private Button btnVendaCancelar;

    @FXML private TextField txtVendaBuscarPdt;
    @FXML private Button btnVendaBuscarProduto;

    @FXML private TableView<ProdutoVO> tbvVendaBuscaProduto;
    @FXML private TableColumn<ProdutoVO, String> tbcVendaIdPdtId;
    @FXML private TableColumn<ProdutoVO, String> tbcVendaIdPdtNome;
    @FXML private TableColumn<ProdutoVO, String> tbcVendaIdPdtCusto;
    @FXML private TableColumn<ProdutoVO, String> tbcVendaIdPdtEstoque;

    @FXML private Label lbVendaNomeProduto;
    @FXML private TextField txtVendaProdutoCusto;
    @FXML private TextField txtVendaProdutoDesconto;
    @FXML private TextField txtVendaProdutoQtd;

    @FXML private Button btnVendaAdicionarProduto;

    @FXML private TextField txtVendaBuscarCliente;
    @FXML private Button btnVendaBuscarCliente;

    @FXML private ComboBox<FuncionarioVO> cbVendaVendedor;

    @FXML private TableView<ProdutoVO> tbvVendaProdutoSelecionado;
    @FXML private TableColumn<ProdutoVO, String> tbcVendaPdtSelecionadoId;
    @FXML private TableColumn<ProdutoVO, String> tbcVendaPdtSelecionadoNome;
    @FXML private TableColumn<ProdutoVO, String> tbcVendaPdtSelecionadoCusto;
    @FXML private TableColumn<ProdutoVO, String> tbcVendaPdtSelecionadoQuantidade;

    @FXML private ComboBox<StatusVendaVO> cbVendaStatus;
    @FXML private ComboBox<TipoPagamentoVO> cbVendaTpPagamento;
    @FXML private DatePicker dtpVendaTpPagamento;
    @FXML private ComboBox cbVendaTpPedido;
    @FXML private ComboBox<StatusPedidoVO> cbVendaStPedido;
    @FXML private ComboBox<TipoEntregaVO> cbVendaTpEntrega;
    @FXML private Label lbVendaTotal;
    @FXML private Button btnVendaFinalizar;

    @FXML private TextField txtVendaBusca;
    @FXML private ComboBox<String> cbVendaTpBusca;
    @FXML private Button btnVendaConsultar;

    @FXML private TableView<VendaVO> tbvVendaBusca;
    @FXML private TableColumn<VendaVO, String> tbcVendaId;
    @FXML private TableColumn<VendaVO, String> tbcVendaCliente;
    @FXML private TableColumn<VendaVO, String> tbcVendaTotal;
    @FXML private TableColumn<VendaVO, String> tbcVendaVendedor;
    @FXML private TableColumn<VendaVO, String> tbcVendaData;
    @FXML private TableColumn<VendaVO, String> tbcVendaStVenda;
    @FXML private TableColumn<VendaVO, String> tbcVendaStPedido;

    // carregadores de combobox
    private void carregarCbVendaVendedor() {
        try {
            
            info("carregarCbVendaVendedor carregados.");
        } catch (Exception e) {
            erro("Erro ao carregarCbVendaVendedor", e);
        }
    }
    
    private void carregarCbVendaStatus() {
        try {
            
            info("carregarCbVendaStatus carregado.");
        } catch (Exception e) {
            erro("Erro ao carregar carregarCbVendaStatus", e);
        }
    }

    private void carregarCbVendaTpPagamento() {
        try {

            info("carregarCbVendaTpPagamento carregados.");
        } catch (Exception e) {
            erro("Erro ao carregarCbVendaTpPagamento", e);
        }
    }

    private void carregarCbVendaStPedido() {
        try {
            
            info("carregarCbVendaStPedido carregado.");
        } catch (Exception e) {
            erro("Erro ao carregar carregarCbVendaStPedido", e);
        }
    }

    private void carregarCbVendaTpEntrega() {
        try {

            info("carregarCbVendaTpEntrega carregado.");
        } catch (Exception e) {
            erro("Erro ao carregarCbVendaTpEntrega", e);
        }
    }


    private void carregarCbVendaTpBusca() {
        try {
            ObservableList<String> tipoBusca = FXCollections.observableArrayList("ID", "Cliente", "Vendedor", "Data", 
                                                                                "Status Venda", "Status Pedido");
            cbVendaTpBusca.setItems(tipoBusca);
            cbVendaTpBusca.getSelectionModel().selectFirst();            
            info("carregarCbVendaTpBusca carregado");
        } catch (Exception e) {
            erro("Erro ao carregarCbVendaTpBusca", e);
        }

    }

    // metodos para obter selecionado no combobox
    private FuncionarioVO obterCbVendaVendedor(){
        if (cbVendaVendedor != null) {
            return cbVendaVendedor.getValue();
        }
        return null;
    }

    private StatusVendaVO obterCbVendaStatus(){
        if (cbVendaStatus != null){
            return cbVendaStatus.getValue();
        }
        return null;
    }

    private TipoPagamentoVO obterCbVendaTpPagamento() {
        if (cbVendaTpPagamento != null) {
            return cbVendaTpPagamento.getValue();
        }
        return null;
    }

    private StatusPedidoVO obterCbVendaStPedido() {
        if (cbVendaStPedido != null) {
            return cbVendaStPedido.getValue();
        }
        return null;
    }

    private TipoEntregaVO obterCbVendaTpEntrega() {
        if (cbVendaTpEntrega != null){
            return cbVendaTpEntrega.getValue();
        }
        return null;
    }

    private String CbVendaTpBusca() {
        if (cbVendaTpBusca != null){
            return cbVendaTpBusca.getValue();
        }
        return null;
    } 
    
    

    @FXML
    private void onBtnNovaVenda(ActionEvent event) {

    }
    
    @FXML
    private void onBtnVendaBuscar(ActionEvent event) {

    }

    @FXML
    private void onBtnVendaCancelar(ActionEvent event) {

    }

    @FXML
    private void onBtnVendaBuscarProduto(ActionEvent event) {

    }

    @FXML
    private void onBtnVendaAdicionarProduto(ActionEvent event) {

    }

    @FXML
    private void onBtnVendaBuscarCliente(ActionEvent event) {

    }

    @FXML
    private void onBtnVendaFinalizar(ActionEvent event) {

    }

    @FXML
    private void onBtnVendaConsultar(ActionEvent event) {

    }
    

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        carregarCbVendaVendedor();
        carregarCbVendaStatus();
        carregarCbVendaTpPagamento();
        carregarCbVendaStPedido();
        carregarCbVendaTpEntrega();
        carregarCbVendaTpBusca();

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    private void info(String msg) {
        System.out.println(GREEN + "[INFO:VendaController] " + msg + RESET);
    }

    private void alerta(String msg) {
        System.out.println(YELLOW + "[AVISO:VendaController] " + msg + RESET);
    }

    private void erro(String msg, Exception e) {
        System.err.println(RED + "[ERRO:VendaController] " + msg + RESET);
        if (e != null) {
            e.printStackTrace();
        }
    }

}