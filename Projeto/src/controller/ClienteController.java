package controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import model.dao.TipoPessoaDAO;
import model.rn.ClienteRN;
import model.rn.EnderecoRN;
import model.rn.PessoaRN;
import model.vo.BairroVO;
import model.vo.CidadeVO;
import model.vo.ClienteVO;
import model.vo.EndPostalVO;
import model.vo.EnderecoVO;
import model.vo.EstadoVO;
import model.vo.LogradouroVO;
import model.vo.PessoaVO;
import model.vo.SexoVO;
import model.vo.TelefoneVO;
import model.vo.TipoPessoaVO;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// import com.mysql.cj.result.StringValueFactory;

public class ClienteController implements Initializable{
    //atributos de botoes
    @FXML private Button btnClienteNovo;
    @FXML private Button btnClienteSalvar;
    @FXML private Button btnClienteEditar;
    @FXML private Button btnClienteAtualizar;
    @FXML private Button btnClienteExcluir;
    @FXML private Button btnClienteCancelar;
    @FXML private Button btnClienteBuscar;

    //atributos de textfields
    @FXML private TextField txtClienteBusca;
    @FXML private TextField txtClienteId;
    @FXML private TextField txtClientePesDocumento;
    @FXML private TextField txtClientePesNome;
    @FXML private TextField txtClientePesEmail;
    @FXML private TextField txtClienteTelCodPais;
    @FXML private TextField txtClienteTelDdd;
    @FXML private TextField txtClienteTelNumero;
    @FXML private TextField txtClienteEndCep;
    @FXML private TextField txtClienteEndNomeRua;
    @FXML private TextField txtClienteEndBairro;
    @FXML private TextField txtClienteEndNumero;
    @FXML private TextField txtClienteEndComplemento;

    //atributos de combobox
    @FXML private ComboBox<TipoPessoaVO> cbClientePesTipo; //carregar e selecionar feitos
    @FXML private ComboBox<SexoVO> cbClientePesSexo; // carregar e selecionar feitos
    @FXML private ComboBox<EstadoVO> cbClienteEndEstado; //carregar e selecionar estados feito
    @FXML private ComboBox<CidadeVO> cbClienteEndCidade; //carregar e selecionar cidades por estado feito
    @FXML private ComboBox<LogradouroVO> cbClienteEndLogradouro; //carregar e selecionar logradouros feito
    //obter:tipo pessoa, sexo, estado, cidade, logradouro

    //atributos de tabela
    @FXML private TableView<ClienteVO> tbvCliente;
    @FXML private TableColumn<ClienteVO, String> tbcClienteId;
    @FXML private TableColumn<ClienteVO, String> tbcClientePesNome;
    @FXML private TableColumn<ClienteVO, String> tbcClientePesDocumento;
    @FXML private TableColumn<ClienteVO, String> tbcClientePesEmail;
    @FXML private TableColumn<ClienteVO, String> tbcClientePesTipo;
    @FXML private TableColumn<ClienteVO, String> tbcClienteStatus;

    //outros atributos
    @FXML private DatePicker dtpClientePesDataNascimento;
    @FXML private CheckBox chbClienteAtivo;

    //inicializacao de classes
    private PessoaRN pessoaRN = new PessoaRN();
    private ClienteRN clienteRN = new ClienteRN();
    private EnderecoRN enderecoRN = new EnderecoRN();
    private TelefoneVO telCli = new TelefoneVO();
    private EndPostalVO endPostal = new EndPostalVO();
    private EnderecoVO endereco = new EnderecoVO();
    private PessoaVO pesCliente = new PessoaVO();
    private ClienteVO clienteVO = new ClienteVO();

    // -- CARREGADORES DE COMBOBOX
    public void carregarTipoPessoa() throws SQLException{
        try {
            List<TipoPessoaVO> tipos = pessoaRN.listarTipoPessoa();
            ObservableList<TipoPessoaVO> observableTipos = FXCollections.observableArrayList(tipos);
            cbClientePesTipo.setItems(observableTipos);
            System.out.println("Lista de tipos de pessoas carregada.");
        } catch (Exception e) {
            System.err.println("Erro ao lista tipos de pessoa" + e.getMessage());
        }
    }

    public void carregarSexo(){
        try {
            List<SexoVO> listaSexo = pessoaRN.listarSexo();
            ObservableList<SexoVO> observableSexo = FXCollections.observableArrayList(listaSexo);
            cbClientePesSexo.setItems(observableSexo);
            System.out.println("Lista de sexos carregados.");
        } catch (Exception e) {
            System.err.println("Erro ao carregador lista de sexos. " + e.getMessage());
        }
        
    }

    // EnderecoRN enderecoRN = new EnderecoRN();
    public void carregarEstados(){
        try {
            List<EstadoVO> estados = enderecoRN.listarEstados();
            ObservableList<EstadoVO> observableEstados = FXCollections.observableArrayList(estados);
            cbClienteEndEstado.setItems(observableEstados);
            System.out.println("Estados carregados com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao carregar estados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void carregarCidadesPorEstado(String sigla){
        try {
            List<CidadeVO> cidades = enderecoRN.listarCidadesPorEstado(sigla);
            ObservableList<CidadeVO> observableCidades = FXCollections.observableArrayList(cidades);
            cbClienteEndCidade.setItems(observableCidades);
            System.out.println("Cidades carregadas com sucesso para o estado: " + sigla);
        } catch (Exception e) {
            System.err.println("Erro ao carregar cidades para o estado " + sigla + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void carregarLogradouros(){
        try {
            List<LogradouroVO> logradouros = enderecoRN.listarLogradouros();
            ObservableList<LogradouroVO> observableLogradouros = FXCollections.observableArrayList(logradouros);
            cbClienteEndLogradouro.setItems(observableLogradouros);
            System.out.println("Logradouros carregados com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao carregar logradouros: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // -- SELECAO DE COMBOBOX
    private TipoPessoaVO obterCbTipoPessoaSelecionada() {
        if (cbClientePesTipo != null){
            System.out.println("Tipo pessoa selecionada.");
            return cbClientePesTipo.getValue(); 
        }
        return null;
    }

    private SexoVO obterCbSexoSelecionado() {
        if (cbClientePesSexo != null){
            System.out.println("Sexo selecionado.");
            return cbClientePesSexo.getValue();
        }
        return null;
    }

    private EstadoVO obterCbEstadoSelecionado(){
        if (cbClienteEndEstado != null){
            System.out.println("Sexo selecionado.");
            return cbClienteEndEstado.getValue();
        }
        return null;
    }

    private CidadeVO obterCbCidadeSelecionado(){
        if (cbClienteEndCidade != null){
            System.out.println("Cidade selecionada.");
            return cbClienteEndCidade.getValue();
        }
        return null;
    }

    private LogradouroVO obterCbLogradouroSelecionado(){
        if (cbClienteEndLogradouro != null){
            System.out.println("Logradouro selecionado.");
            return cbClienteEndLogradouro.getValue();
        }
        return null;
    }

    // -- CAPTURA DE TELEFONE --
    private List<TelefoneVO> criarTelefones(String documento) {
        List<TelefoneVO> telefones = new ArrayList<>();

        String codPais = null;
        if (txtClienteTelCodPais.getText() != null) {
            codPais = txtClienteTelCodPais.getText().trim();
        }

        String ddd = null;
        if (txtClienteTelDdd.getText() != null) {
            ddd = txtClienteTelDdd.getText().trim();
        }

        String numero = null;
        if (txtClienteTelNumero.getText() != null) {
            numero = txtClienteTelNumero.getText().trim();
        }

        if (numero != null && !numero.isBlank()) {
            // TelefoneVO telCli = new TelefoneVO();

            telCli.setTel_codPais(codPais);
            telCli.setTel_ddd(ddd);
            telCli.setTel_numero(numero);
            telCli.setTel_pes_cpf(documento);

            telefones.add(telCli);
        }

        return telefones;
    }


    // -- LISTERNERS --
    private void listenerEstado() {
        cbClienteEndEstado.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, novoEstado) -> {
            // Limpa ComboBox de cidades
            cbClienteEndCidade.getItems().clear();
            cbClienteEndCidade.getSelectionModel().clearSelection();

            if (novoEstado != null) {
                try {
                    // Carrega cidades do estado selecionado
                    carregarCidadesPorEstado(novoEstado.getEst_sigla());
                    cbClienteEndCidade.setDisable(false);
                } catch (Exception e) {
                    System.err.println("Erro ao carregar cidades: " + e.getMessage());
                    cbClienteEndCidade.setDisable(true);
                }
            } else {
                cbClienteEndCidade.setDisable(true);
            }

            // Limpa campos de endereço dependentes
            txtClienteEndNomeRua.clear();
            txtClienteEndCep.clear();
            txtClienteEndNumero.clear();
            txtClienteEndComplemento.clear();
        });
    }

    private void criarEndereco() throws Exception {
        // Ler campos de texto
        String cep = null;
        if (txtClienteEndCep != null) {
            String v = txtClienteEndCep.getText();
            if (v != null) {
                cep = v.trim();
            }
        }

        String rua = null;
        if (txtClienteEndNomeRua != null) {
            String v = txtClienteEndNomeRua.getText();
            if (v != null) {
                rua = v.trim();
            }
        }

        String numero = null;
        if (txtClienteEndNumero != null) {
            String v = txtClienteEndNumero.getText();
            if (v != null) {
                numero = v.trim();
            }
        }

        String complemento = null;
        if (txtClienteEndComplemento != null) {
            String v = txtClienteEndComplemento.getText();
            if (v != null) {
                v = v.trim();
                if (!v.isBlank()) {
                    complemento = v;
                }
            }
        }

        String bairro = null;
        if (txtClienteEndBairro != null){
            String v = txtClienteEndBairro.getText();
            if (v != null){
                v = v.trim();
                if (!v.isBlank()){
                    bairro = v;
                    }
                }
            }

        // Ler seleções dos combos
        EstadoVO estado = obterCbEstadoSelecionado();
        CidadeVO cidade = obterCbCidadeSelecionado();
        LogradouroVO logradouro = obterCbLogradouroSelecionado();      

        // Validações obrigatórias
        if (estado == null) {
            throw new IllegalArgumentException("Estado é obrigatório.");
        }

        if (cidade == null) {
            throw new IllegalArgumentException("Cidade é obrigatória.");
        }

        if (logradouro == null) {
            throw new IllegalArgumentException("Logradouro é obrigatório.");
        }

        if (bairro == null) {
            throw new IllegalArgumentException("Bairro é obrigatório.");
        }

        if (rua == null || rua.isBlank()) {
            throw new IllegalArgumentException("Nome da rua é obrigatório.");
        }

        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("CEP é obrigatório.");
        }

        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Número é obrigatório.");
        }

        // Montar EndPostal
        endPostal = new EndPostalVO();
        endPostal.setEndP_estado(estado);
        endPostal.setEndP_cidade(cidade);

        BairroVO bairroApoio = enderecoRN.obterOuCriarBairro(bairro);
        endPostal.setEndP_bairro(bairroApoio);

        endPostal.setEndP_logradouro(logradouro);
        endPostal.setEndP_nomeRua(rua);
        endPostal.setEndP_cep(cep);

        // Garantir ID do EndPostal
        enderecoRN = new EnderecoRN();
        int endPId = enderecoRN.criarOuObterEndPostal(endPostal);
        if (endPId <= 0) {
            throw new Exception("Falha ao criar/obter EndPostal.");
        }
        endPostal.setEndP_id(endPId);

        // Montar Endereco
        endereco = new EnderecoVO();
        endereco.setEnd_endP_id(endPostal);
        endereco.setEnd_numero(numero);
        endereco.setEnd_complemento(complemento);

        // Vincular ao documento do cliente (persistir)
        String documento = null;
        if (txtClientePesDocumento != null) {
            String v = txtClientePesDocumento.getText();
            if (v != null) {
                documento = v.trim();
            }
        }

        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Documento é obrigatório para vincular o endereço.");
        }

        enderecoRN.adicionarEndereco(documento, endereco);
    }


    private PessoaVO criarBasePessoa() throws Exception {
        try {
            //leitura dos campos
            String documento = null;
            if (txtClientePesDocumento.getText() != null) {
                documento = txtClientePesDocumento.getText().trim();
            }

            String nome = null;
            if (txtClientePesNome.getText() != null) {
                nome = txtClientePesNome.getText().trim();
            }

            String email = null;
            if (txtClientePesEmail.getText() != null) {
                email = txtClientePesEmail.getText().trim();
            }

            boolean ativo = chbClienteAtivo.isSelected();

            //dt nascimento
            LocalDate dataNascimento = dtpClientePesDataNascimento.getValue();

            //obter sexo pessoa
            SexoVO sexoSelecionado = obterCbSexoSelecionado();

            //obter tipo pessoa
            TipoPessoaVO tipoPessoa = obterCbTipoPessoaSelecionada();

            //validacoes
            if (documento == null || documento.isBlank()) {
                throw new IllegalArgumentException("Documento é obrigatório.");
            }

            if (nome == null || nome.isBlank()) {
                throw new IllegalArgumentException("Nome é obrigatório.");
            }

            if (tipoPessoa == null) {
                throw new IllegalArgumentException("Tipo de pessoa é obrigatório.");
            }

            if (sexoSelecionado == null) {
                throw new IllegalArgumentException("Sexo eh obrigatorio");
            }

            if (dataNascimento == null) {
                throw new IllegalArgumentException("Data de nascimento eh obrigatorio");
            }

            //criar telefone
            

            //montar pessoaVO
            // pesCliente = new PessoaVO();
            pesCliente.setPes_cpf(documento);
            pesCliente.setPes_nome(nome);
            pesCliente.setPes_email(email);
            pesCliente.setPes_ativo(ativo);
            pesCliente.setPes_dt_nascimento(dataNascimento);
            pesCliente.setPes_sexo(sexoSelecionado);
            pesCliente.setPes_tipo_pessoa(tipoPessoa);
            pesCliente.setEndereco(null);
            pesCliente.setTelefone(criarTelefones(documento));

            return pesCliente;

        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao criar pessoa: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void criarCliente(PessoaVO pessoaVO) throws Exception {
        if (pessoaVO == null){
            throw new Exception("ERRO: informe o objeto de pessoa");
        }
        
        // clienteVO = new ClienteVO();
        clienteVO.setPes_cpf(pessoaVO.getPes_cpf());
        clienteVO.setPes_nome(pessoaVO.getPes_nome());
        clienteVO.setPes_sexo(pessoaVO.getPes_sexo());
        clienteVO.setPes_dt_nascimento(pessoaVO.getPes_dt_nascimento());
        clienteVO.setPes_email(pessoaVO.getPes_email());
        clienteVO.setPes_ativo(pessoaVO.getPes_ativo());
        clienteVO.setPes_tipo_pessoa(pessoaVO.getPes_tipo_pessoa());
        clienteVO.setEndereco(pessoaVO.getEndereco());
        clienteVO.setTelefone(pessoaVO.getTelefone());
        clienteVO.setCli_dtCadastro(LocalDate.now());

        clienteRN.salvarNovo(clienteVO);
        
        
    }
    
    @FXML
    private void handleBtnClienteSalvarAction(ActionEvent event) throws Exception {
        PessoaVO pesCriada = criarBasePessoa();
        if (pesCriada == null){
            System.out.println("erro ao criar base pessoa");
            return;
        }
        System.out.println("Pessoa criada!");

        try {
            criarCliente(pesCriada);
            System.out.println("Cliente criado!");
        } catch (Exception e) {
            System.err.println("Erro ao adicionar cliente: " + e.getMessage());
            return;
        }

        try {
            criarEndereco();
            System.out.println("Endereco criado a cliente");
        } catch (Exception e) {
            System.err.println("Erro ao adicionar endereco: " + e.getMessage());
        }
    }




    //controle de campos
    private void habilitarCampos(boolean habilitar){
        btnClienteNovo.setDisable(habilitar);
        btnClienteSalvar.setDisable(habilitar);
        btnClienteEditar.setDisable(habilitar);
        btnClienteAtualizar.setDisable(habilitar);
        btnClienteExcluir.setDisable(habilitar);
        btnClienteCancelar.setDisable(habilitar);
        btnClienteBuscar.setDisable(habilitar);
        txtClienteBusca.setDisable(habilitar);
        txtClienteId.setDisable(habilitar);
        txtClientePesDocumento.setDisable(habilitar);
        txtClientePesNome.setDisable(habilitar);
        txtClientePesEmail.setDisable(habilitar);
        txtClienteTelCodPais.setDisable(habilitar);
        txtClienteTelDdd.setDisable(habilitar);
        txtClienteTelNumero.setDisable(habilitar);
        txtClienteEndCep.setDisable(habilitar);
        txtClienteEndNomeRua.setDisable(habilitar);
        txtClienteEndBairro.setDisable(habilitar);
        txtClienteEndNumero.setDisable(habilitar);
        txtClienteEndComplemento.setDisable(habilitar);
        cbClientePesTipo.setDisable(habilitar);
        cbClientePesSexo.setDisable(habilitar); 
        cbClienteEndEstado.setDisable(habilitar); 
        cbClienteEndCidade.setDisable(habilitar); 
        cbClienteEndLogradouro.setDisable(habilitar); 
        dtpClientePesDataNascimento.setDisable(habilitar);
        chbClienteAtivo.setDisable(habilitar);

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try{
            habilitarCampos(false);
            carregarTipoPessoa();
            carregarSexo();
            carregarEstados();
            carregarLogradouros();
            listenerEstado();
            carregarTabela();
        } catch (Exception e){
            System.err.println("Erro ao inicializar " + e.getMessage());
        }
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
    
    private void carregarTabela(){
        try {
            String filtro = null;
            if (txtClienteBusca != null) {
                String v = txtClienteBusca.getText();
                if (v != null) {
                    filtro = v.trim();
                }
            }

            List<ClienteVO> lista = clienteRN.buscarTodosClientes(filtro);
            ObservableList<ClienteVO> obs = FXCollections.observableArrayList(lista);

            tbcClienteId.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue() != null && cd.getValue().getCli_id() > 0 ? String.valueOf(cd.getValue().getCli_id()) : ""));
            tbcClientePesNome.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue() != null && cd.getValue().getPes_nome() != null ? cd.getValue().getPes_nome() : ""));
            tbcClientePesDocumento.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue() != null && cd.getValue().getPes_cpf() != null ? cd.getValue().getPes_cpf() : ""));
            tbcClientePesEmail.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue() != null && cd.getValue().getPes_email() != null ? cd.getValue().getPes_email() : ""));
            tbcClientePesTipo.setCellValueFactory(cd -> {
                String tipo = "";
                if (cd.getValue() != null && cd.getValue().getPes_tipo_pessoa() != null) {
                    if (cd.getValue().getPes_tipo_pessoa().getCodigo() != null) {
                        tipo = cd.getValue().getPes_tipo_pessoa().getCodigo();
                    } else if (cd.getValue().getPes_tipo_pessoa().getDescricao() != null) {
                        tipo = cd.getValue().getPes_tipo_pessoa().getDescricao();
                    }
                }
                return new SimpleStringProperty(tipo);
            });
            tbcClienteStatus.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue() != null && cd.getValue().getPes_ativo() != null && cd.getValue().getPes_ativo() ? "Ativo" : "Inativo"));

            tbvCliente.setItems(obs);
        } catch (Exception e) {
            System.err.println("Erro ao carregar tabela de clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
}
