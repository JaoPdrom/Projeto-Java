package controller;

import java.util.List;
import java.util.Observable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import java.time.format.DateTimeFormatter;
import model.rn.ClienteRN;
import model.rn.PessoaRN;
import java.sql.SQLException;
import model.dao.TipoPessoaDAO;
import model.dao.SexoDAO;
import model.dao.ConexaoDAO;
import model.vo.TelefoneVO;
import model.vo.TipoPessoaVO;
import java.time.LocalDate;
import java.util.ArrayList;
import model.vo.BairroVO;
import model.vo.CidadeVO;
import model.vo.ClienteVO;
import model.vo.EndPostalVO;
import model.vo.EnderecoVO;
import model.vo.EstadoVO;
import model.vo.LogradouroVO;
import model.vo.PessoaVO;
import model.vo.SexoVO;
import model.vo.TipoClienteVO;
import model.rn.EnderecoRN;

public class ClienteC implements Initializable {

    @FXML private TextField txtId;

    @FXML private TextField txtDocumento;

    @FXML private TextField txtNome;

    @FXML private ComboBox<SexoVO> cbSexo;

    @FXML private DatePicker dtpDataCadastro;
    @FXML private DatePicker dpNascimentoFundacao;

    @FXML private TextField txtEmail;

    @FXML private ComboBox<String> cbTipoPessoaForm;
    @FXML private ComboBox<String> cbTipoPessoa;

    @FXML private CheckBox chkAtivo;

    @FXML private TextField txtTelPais;

    @FXML private TextField txtTelDdd;

    @FXML private TextField txtTelNumero;

    @FXML private ComboBox<EstadoVO> cbEstado;

    @FXML private ComboBox<CidadeVO> cbCidade;

    @FXML private ComboBox<LogradouroVO> cbLogradouro;

    @FXML private TextField txtCep;

    @FXML private TextField txtRua;

    @FXML private ComboBox<BairroVO> cbBairro;

    @FXML private TextField txtEndNumero;

    @FXML private TextField txtEndComplemento;

    @FXML private TableView<ClienteVO> tblClientes;

    @FXML private TableColumn<ClienteVO, String> colDocumento;

    @FXML private TableColumn<ClienteVO, String> colNome;

    @FXML private TableColumn<ClienteVO, String> colTipo;

    @FXML private TableColumn<ClienteVO, String> colEmail;

    @FXML private TableColumn<ClienteVO, String> colCelular;

    @FXML private TableColumn<ClienteVO, String> colStatus;

    @FXML private TableColumn<ClienteVO, String> colCadastro;

    private List<ClienteVO> listaClientes;
    private ObservableList<ClienteVO> obsListaClientes;

    // RN services
    private final ClienteRN clienteRN = new ClienteRN();
    private PessoaRN pessoaRN; // lazy init devido a checked exception no construtor

    private PessoaRN pessoaRN() throws Exception {
        if (this.pessoaRN == null) {
            
                this.pessoaRN = new PessoaRN();
            
        }
        return this.pessoaRN;
    }

    

    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle rb) {
        // carregar tabela de clientes e preparar combos de endereÃ§o
        try {
            carregarClientes();
            carregarTiposPessoa();
            carregarSexos();
            // popula combos de localizaÃ§Ã£o
            carregarEstados();
            listarBairros();
            listarLogradouros();
            // cidade depende do estado selecionado
            cbCidade.setDisable(true);

            cbEstado.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, novoEstado) -> {
                cbCidade.getItems().clear();
                cbCidade.getSelectionModel().clearSelection();
                if (novoEstado != null) {
                    carregarCidadesPorEstado(novoEstado.getEst_sigla());
                    cbCidade.setDisable(false);
                } else {
                    cbCidade.setDisable(true);
                }
                // limpa campos de endereÃ§o dependentes
                txtRua.clear();
                txtCep.clear();
                txtEndNumero.clear();
                txtEndComplemento.clear();
            });

            System.out.println("Clientes carregados com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        tblClientes.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selecionarItemTableViewCliente(newValue));
    }

    private void carregarSexos() {
        try (var con = ConexaoDAO.getConexao()) {
            var dao = new SexoDAO(con);
            var lista = dao.buscarTodosSexo();
            var obs = FXCollections.observableArrayList(lista);
            cbSexo.setItems(obs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void carregarClientes() throws Exception {

        // Propriedades mapeadas para getters JavaBean de ClienteVO/PessoaVO
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("pes_cpf"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("pes_nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("pes_email"));

        // Tipo (F/J) a partir de pessoa.tipo_pessoa.codigo
        colTipo.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue() != null && cd.getValue().getPes_tipo_pessoa() != null && cd.getValue().getPes_tipo_pessoa().getCodigo() != null
                ? cd.getValue().getPes_tipo_pessoa().getCodigo()
                : ""
        ));

        // Celular/telefone: mostra primeiro número se existir
        if (colCelular != null) {
            colCelular.setCellValueFactory(cd -> {
                var cli = cd.getValue();
                String numero = "";
                if (cli != null && cli.getTelefone() != null && !cli.getTelefone().isEmpty()) {
                    var t = cli.getTelefone().get(0);
                    String ddd = t.getTel_ddd() != null ? t.getTel_ddd() : "";
                    String num = t.getTel_numero() != null ? t.getTel_numero() : "";
                    numero = (ddd.isEmpty() ? "" : "(" + ddd + ") ") + num;
                }
                return new SimpleStringProperty(numero);
            });
        }

        // Status a partir do booleano pes_ativo
        colStatus.setCellValueFactory(cd -> new SimpleStringProperty(
            Boolean.TRUE.equals(cd.getValue().getPes_ativo()) ? "Ativo" : "Inativo"
        ));

        // Data de cadastro formatada
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colCadastro.setCellValueFactory(cd -> new SimpleStringProperty(
            cd.getValue().getCli_dtCadastro() != null ? cd.getValue().getCli_dtCadastro().format(fmt) : ""
        ));

        listaClientes = clienteRN.buscarTodosClientes("");

        obsListaClientes = FXCollections.observableArrayList(listaClientes);
        tblClientes.setItems(obsListaClientes);

    }

    public void selecionarItemTableViewCliente(ClienteVO cliente) {
        if (cliente == null) {
            limparFormulario();
            return;
        }

        try {
            // Dados básicos
            txtId.setText(String.valueOf(cliente.getCli_id()));
            txtDocumento.setText(cliente.getPes_cpf());
            txtNome.setText(cliente.getPes_nome());
            txtEmail.setText(cliente.getPes_email());
            chkAtivo.setSelected(Boolean.TRUE.equals(cliente.getPes_ativo()));

            // Buscar dados completos da pessoa (tipo, sexo, nascimento, telefones, endereços)
            PessoaVO pessoaCompleta = null;
            try { pessoaCompleta = pessoaRN().buscarPorCpfRN(cliente.getPes_cpf()); } catch (Exception ig) { }

            // Tipo de Pessoa (combo do formulário)
            TipoPessoaVO tipo = pessoaCompleta != null ? pessoaCompleta.getPes_tipo_pessoa() : cliente.getPes_tipo_pessoa();
            if (tipo != null) selecionarTipoPessoaForm(tipo);

            // Sexo
            SexoVO sexoSel = pessoaCompleta != null ? pessoaCompleta.getPes_sexo() : cliente.getPes_sexo();
            if (cbSexo != null && sexoSel != null) {
                for (SexoVO item : cbSexo.getItems()) {
                    if ((item.getSex_id() == sexoSel.getSex_id()) ||
                        (item.getSex_descricao() != null && item.getSex_descricao().equalsIgnoreCase(sexoSel.getSex_descricao()))) {
                        cbSexo.getSelectionModel().select(item);
                        break;
                    }
                }
            }

            // Nascimento/Fundação
            if (dpNascimentoFundacao != null) {
                dpNascimentoFundacao.setValue(pessoaCompleta != null ? pessoaCompleta.getPes_dt_nascimento() : cliente.getPes_dt_nascimento());
            }

            // Data de cadastro
            if (dtpDataCadastro != null) {
                dtpDataCadastro.setValue(cliente.getCli_dtCadastro());
            }

            // Telefone (primeiro)
            var tels = pessoaCompleta != null ? pessoaCompleta.getTelefone() : cliente.getTelefone();
            if (tels != null && !tels.isEmpty()) {
                TelefoneVO t = tels.get(0);
                txtTelPais.setText(t.getTel_codPais());
                txtTelDdd.setText(t.getTel_ddd());
                txtTelNumero.setText(t.getTel_numero());
            } else {
                txtTelPais.clear();
                txtTelDdd.clear();
                txtTelNumero.clear();
            }

            // Endereço (primeiro)
            var ends = pessoaCompleta != null ? pessoaCompleta.getEndereco() : cliente.getEndereco();
            if (ends != null && !ends.isEmpty()) {
                EnderecoVO end = ends.get(0);
                EndPostalVO ep = end.getEnd_endP_id();
                if (ep != null) {
                    // Estado
                    if (ep.getEndP_estado() != null && cbEstado != null) {
                        String sigla = ep.getEndP_estado().getEst_sigla();
                        for (EstadoVO est : cbEstado.getItems()) {
                            if (est.getEst_sigla() != null && est.getEst_sigla().equalsIgnoreCase(sigla)) {
                                cbEstado.getSelectionModel().select(est);
                                break;
                            }
                        }
                        if (sigla != null && !sigla.isBlank()) {
                            carregarCidadesPorEstado(sigla);
                            cbCidade.setDisable(false);
                        }
                    }

                    // Cidade
                    if (ep.getEndP_cidade() != null && cbCidade != null) {
                        String cidDesc = ep.getEndP_cidade().getCid_descricao();
                        for (CidadeVO c : cbCidade.getItems()) {
                            if (c.getCid_descricao() != null && c.getCid_descricao().equalsIgnoreCase(cidDesc)) {
                                cbCidade.getSelectionModel().select(c);
                                break;
                            }
                        }
                    }

                    // Logradouro
                    if (ep.getEndP_logradouro() != null && cbLogradouro != null) {
                        String logDesc = ep.getEndP_logradouro().getLogradouro_descricao();
                        for (LogradouroVO lg : cbLogradouro.getItems()) {
                            if (lg.getLogradouro_descricao() != null && lg.getLogradouro_descricao().equalsIgnoreCase(logDesc)) {
                                cbLogradouro.getSelectionModel().select(lg);
                                break;
                            }
                        }
                    }

                    // Bairro
                    if (ep.getEndP_bairro() != null && cbBairro != null) {
                        String baiDesc = ep.getEndP_bairro().getBairro_descricao();
                        for (BairroVO b : cbBairro.getItems()) {
                            if (b.getBairro_descricao() != null && b.getBairro_descricao().equalsIgnoreCase(baiDesc)) {
                                cbBairro.getSelectionModel().select(b);
                                break;
                            }
                        }
                    }

                    // Campos de endereço
                    txtRua.setText(ep.getEndP_nomeRua());
                    txtCep.setText(ep.getEndP_cep());
                    txtEndNumero.setText(end.getEnd_numero());
                    txtEndComplemento.setText(end.getEnd_complemento());
                }
            } else {
                // limpa campos de endereço
                cbEstado.getSelectionModel().clearSelection();
                cbCidade.getItems().clear();
                cbCidade.getSelectionModel().clearSelection();
                cbCidade.setDisable(true);
                cbLogradouro.getSelectionModel().clearSelection();
                cbBairro.getSelectionModel().clearSelection();
                txtCep.clear();
                txtRua.clear();
                txtEndNumero.clear();
                txtEndComplemento.clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void selecionarTipoPessoaForm(TipoPessoaVO tipo) {
        if (tipo == null || cbTipoPessoaForm == null) return;
        String codigo = tipo.getCodigo();
        if (codigo == null) return;
        for (String item : cbTipoPessoaForm.getItems()) {
            if (item != null && item.toUpperCase().startsWith(codigo.toUpperCase())) {
                cbTipoPessoaForm.getSelectionModel().select(item);
                break;
            }
        }
    }

    private void adicionarCliente(PessoaVO pessoa){
        try {
            if (pessoa == null)
                throw new Exception("Pessoa nao informada para criar cliente.");

            String documento = pessoa.getPes_cpf();
            if (documento == null || documento.isBlank())
                throw new Exception("Documento obrigatorio para criar cliente.");

            ClienteVO cliente = new ClienteVO();
            cliente.setPes_cpf(documento);
            // Garantir campos de Pessoa, pois ClienteRN pode atualizar Pessoa via ClienteVO
            cliente.setPes_nome(pessoa.getPes_nome());
            cliente.setPes_email(pessoa.getPes_email());
            if (pessoa.getPes_ativo() != null) {
                cliente.setPes_ativo(pessoa.getPes_ativo());
            } else {
                cliente.setPes_ativo(Boolean.TRUE);
            }
            cliente.setPes_tipo_pessoa(pessoa.getPes_tipo_pessoa());

            LocalDate dt = dtpDataCadastro != null ? dtpDataCadastro.getValue() : null;
            if (dt != null) {
                cliente.setCli_dtCadastro(dt);
            }

            clienteRN.salvarNovo(cliente);
            showInfo("Cliente criado com sucesso para o documento: " + documento);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Falha ao criar Cliente: " + e.getMessage());
        }
    }

    // Helpers de validação e montagem
    private void validatePessoaForm(String documento, String nome, TipoPessoaVO tipoPessoa) throws Exception {
        if (documento == null || documento.isBlank()) {
            throw new Exception("Documento é obrigatório.");
        }
        if (nome == null || nome.isBlank()) {
            throw new Exception("Nome é obrigatório.");
        }
        if (tipoPessoa == null) {
            throw new Exception("Tipo de pessoa é obrigatório.");
        }
    }

    private TelefoneVO buildTelefoneFromFormOptional(String documento) {
        if (txtTelNumero.getText() != null && !txtTelNumero.getText().isBlank()) {
            TelefoneVO tel = new TelefoneVO();
            tel.setTel_codPais(txtTelPais.getText() != null ? txtTelPais.getText().trim() : null);
            tel.setTel_ddd(txtTelDdd.getText() != null ? txtTelDdd.getText().trim() : null);
            tel.setTel_numero(txtTelNumero.getText().trim());
            tel.setTel_pes_cpf(documento);
            return tel;
        }
        return null;
    }

    private PessoaVO buildPessoaFromForm() throws Exception {
        String documento = txtDocumento.getText() != null ? txtDocumento.getText().trim() : null;
        String nome = txtNome.getText() != null ? txtNome.getText().trim() : null;
        String email = txtEmail.getText() != null ? txtEmail.getText().trim() : null;
        Boolean ativo = chkAtivo.isSelected();
        TipoPessoaVO tipoPessoa = obterTipoPessoaSelecionada();

        validatePessoaForm(documento, nome, tipoPessoa);

        EnderecoVO endereco = montarEnderecoAtual();
        TelefoneVO tel = buildTelefoneFromFormOptional(documento);

        PessoaVO pessoa = new PessoaVO();
        pessoa.setPes_cpf(documento);
        pessoa.setPes_nome(nome);
        pessoa.setPes_email(email);
        pessoa.setPes_ativo(ativo);
        pessoa.setPes_tipo_pessoa(tipoPessoa);

        var enderecos = new ArrayList<EnderecoVO>();
        enderecos.add(endereco);
        pessoa.setEndereco(enderecos);
        if (tel != null) {
            var telefones = new ArrayList<TelefoneVO>();
            telefones.add(tel);
            pessoa.setTelefone(telefones);
        }
        return pessoa;
    }

    private ClienteVO buildClienteFromPessoa(PessoaVO pessoa) {
        ClienteVO cliente = new ClienteVO();
        cliente.setPes_cpf(pessoa.getPes_cpf());
        cliente.setPes_nome(pessoa.getPes_nome());
        cliente.setPes_email(pessoa.getPes_email());
        if (pessoa.getPes_ativo() != null) {
            cliente.setPes_ativo(pessoa.getPes_ativo());
        } else {
            cliente.setPes_ativo(Boolean.TRUE);
        }
        cliente.setPes_tipo_pessoa(pessoa.getPes_tipo_pessoa());
        LocalDate dt = dtpDataCadastro != null ? dtpDataCadastro.getValue() : null;
        if (dt != null) cliente.setCli_dtCadastro(dt);
        return cliente;
    }

    public void criarPessoa() {
        try {
            PessoaVO pessoa = buildPessoaFromForm();
            pessoaRN().adicionarPessoa(pessoa);
            showInfo("Pessoa criada com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Falha ao criar Pessoa: " + e.getMessage());
        }
    }

    // Método opcional para manter o fluxo antigo (criar Pessoa e Cliente em sequência)
    public void criarPessoaECliente() {
        try {
            PessoaVO pessoa = buildPessoaFromForm();
            pessoaRN().adicionarPessoa(pessoa);
            adicionarCliente(pessoa);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Falha ao criar Pessoa/Cliente: " + e.getMessage());
        }
    }

    // Resolve TipoPessoa selecionado no combo por cÃ³digo (ex.: "F"/"J")
    private TipoPessoaVO obterTipoPessoaSelecionada() throws Exception {
        String codigo = cbTipoPessoaForm != null ? cbTipoPessoaForm.getValue() : null;
        if (codigo == null || codigo.isBlank()) return null;
        var dao = new TipoPessoaDAO();
        var lista = dao.listarTodos();
        for (TipoPessoaVO tp : lista) {
            if (tp.getCodigo() != null && tp.getCodigo().equalsIgnoreCase(codigo.substring(0, 1))) {
                return tp;
            }
        }
        return null;
    }

    private void carregarTiposPessoa() {
        try {
            var dao = new TipoPessoaDAO();
            var lista = dao.listarTodos();
            var items = FXCollections.<String>observableArrayList();
            for (TipoPessoaVO tp : lista) {
                String cod = tp.getCodigo() != null ? tp.getCodigo() : "";
                String desc = tp.getDescricao() != null ? tp.getDescricao() : "";
                items.add(cod + " - " + desc);
            }
            if (cbTipoPessoaForm != null) {
                cbTipoPessoaForm.setItems(items);
            }
            if (cbTipoPessoa != null) {
                cbTipoPessoa.setItems(items);
            }
            System.out.println("[CONTROLLER] Tipos de pessoa carregados, tipos:");
            // System.out.println(items);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // populadores de cbox de endereÃ§o
    EnderecoRN enderecoRN = new EnderecoRN();
    public void carregarEstados() {
        try {
            List<EstadoVO> estados = enderecoRN.listarEstados();
            ObservableList<EstadoVO> obsEstados = FXCollections.observableArrayList(estados);
            cbEstado.setItems(obsEstados);
            System.out.println("[CONTROLLER] Estados carregados");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     //popula cb de cidade com base no estado selecionado
    public void carregarCidadesPorEstado(String sigla) {
        try {
            List<CidadeVO> cidades = enderecoRN.listarCidadesPorEstado(sigla);
            ObservableList<CidadeVO> obsCidades = FXCollections.observableArrayList(cidades);
            cbCidade.setItems(obsCidades);
            System.out.println("[CONTROLLER] Cidades carregadas para o estado: " + sigla);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //popula cb de logradouro
    public void listarLogradouros() {
        try {
            List<LogradouroVO> logradouros = enderecoRN.listarLogradouros();
            ObservableList<LogradouroVO> obsLogradouros = FXCollections.observableArrayList(logradouros);
            cbLogradouro.setItems(obsLogradouros);
            System.out.println("[CONTROLLER] Logradouros carregados");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //popula cb de bairro com base na cidade selecionada
    public void listarBairros() {
        try {
            List<BairroVO> bairros = enderecoRN.listarBairros();
            ObservableList<BairroVO> obsBairros = FXCollections.observableArrayList(bairros);
            cbBairro.setItems(obsBairros);
            System.out.println("[CONTROLLER] Bairros carregados, bairros:");
            // System.out.println(obsBairros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EstadoVO getEstadoSelecionado() { 
        return cbEstado.getValue(); 
    }

    private CidadeVO getCidadeSelecionada() { 
        return cbCidade.getValue(); 
    }

    private BairroVO getBairroSelecionado() { 
        return cbBairro.getValue(); 
    }

    private LogradouroVO getLogradouroSelecionado() { 
        return cbLogradouro.getValue(); 
    }

    private boolean validarSelecaoEnderecoObrigatoria() {
        return getEstadoSelecionado() != null && getCidadeSelecionada() != null
                && getBairroSelecionado() != null && getLogradouroSelecionado() != null
                && txtRua.getText() != null && !txtRua.getText().isBlank()
                && txtCep.getText() != null && !txtCep.getText().isBlank()
                && txtEndNumero.getText() != null && !txtEndNumero.getText().isBlank();
    }

    // Monta o VO de endereÃ§o a partir das seleÃ§Ãµes atuais (sem persistir)
    private EnderecoVO montarEnderecoAtual() throws Exception {
        if (!validarSelecaoEnderecoObrigatoria()) {
            throw new Exception("Dados de endereÃ§o incompletos.");
        }

        EndPostalVO endPostal = new EndPostalVO();
        System.out.println("[CONTROLLER] Montando EndPostal com:");

        endPostal.setEndP_estado(getEstadoSelecionado());
        System.out.println("[CONTROLLER] setEndP_estado: " + endPostal.getEndP_estado());
        
        endPostal.setEndP_cidade(getCidadeSelecionada());
        System.out.println("[CONTROLLER] setEndP_cidade: " + endPostal.getEndP_cidade());

        endPostal.setEndP_bairro(getBairroSelecionado());
        System.out.println("[CONTROLLER] setEndP_bairro: " + endPostal.getEndP_bairro());

        endPostal.setEndP_logradouro(getLogradouroSelecionado());
        System.out.println("[CONTROLLER] setEndP_logradouro: " + endPostal.getEndP_logradouro());

        endPostal.setEndP_nomeRua(txtRua.getText().trim());
        System.out.println("[CONTROLLER] setEndP_nomeRua: " + endPostal.getEndP_nomeRua());

        endPostal.setEndP_cep(txtCep.getText().trim());
        System.out.println("[CONTROLLER] setEndP_cep: " + endPostal.getEndP_cep());

        EnderecoVO endereco = new EnderecoVO();
        System.out.println("[CONTROLLER] Montando Endereco com:");

        endereco.setEnd_endP_id(endPostal);
        System.out.println("[CONTROLLER] setEnd_endP_id: " + endereco.getEnd_endP_id());

        endereco.setEnd_numero(txtEndNumero.getText().trim());
        System.out.println("[CONTROLLER] setEnd_numero: " + endereco.getEnd_numero());

        endereco.setEnd_complemento(txtEndComplemento.getText() == null ? null : txtEndComplemento.getText().trim());
        System.out.println("[CONTROLLER] setEnd_complemento: " + endereco.getEnd_complemento());

        return endereco;
    }

    // Conecta (persiste) o endereÃ§o atual para o documento informado
    // Pode ser chamado por um handler depois (onAdicionarEndereco/onSalvar)
    public Integer conectarEnderecoAtual() throws Exception {
        String documento = txtDocumento.getText() != null ? txtDocumento.getText().trim() : null;
        if (documento == null || documento.isBlank()) {
            throw new Exception("Documento obrigatÃ³rio para vincular endereÃ§o.");
        }
        EnderecoVO endereco = montarEnderecoAtual();
        System.out.println("[CONTROLLER] Conectando Endereco: " + endereco);
        return enderecoRN.adicionarEndereco(documento, endereco);
    }

    @FXML
    public void onNovo(ActionEvent event) {
        // cÃ³digo para novo cliente
        System.out.println("BotÃ£o Novo clicado");
    }



    @FXML public void onNovo() {
        limparFormulario();
        chkAtivo.setSelected(true);
        if (dtpDataCadastro != null) {
            dtpDataCadastro.setValue(LocalDate.now());
        }
    }

    @FXML public void onEditar() { }

    @FXML public void onSalvar() {
        try {
            if (txtId.getText() != null && !txtId.getText().isBlank()) {
                atualizarCliente();
                carregarClientes();
                limparFormulario();
            } else {
                criarPessoaECliente();
                carregarClientes();
                limparFormulario();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML public void onCancelar() { }

    @FXML public void onExcluir() { }

    @FXML public void onAtualizar() {
        try { carregarClientes(); } catch (Exception e) { e.printStackTrace(); }
    }

    private void limparFormulario() {
        txtId.clear();
        txtDocumento.clear();
        txtNome.clear();
        txtEmail.clear();
        if (cbTipoPessoaForm != null) cbTipoPessoaForm.getSelectionModel().clearSelection();
        if (cbTipoPessoa != null) cbTipoPessoa.getSelectionModel().clearSelection();
        chkAtivo.setSelected(false);
        txtTelPais.clear();
        txtTelDdd.clear();
        txtTelNumero.clear();

        cbEstado.getSelectionModel().clearSelection();
        cbCidade.getItems().clear();
        cbCidade.getSelectionModel().clearSelection();
        cbCidade.setDisable(true);
        cbLogradouro.getSelectionModel().clearSelection();
        cbBairro.getSelectionModel().clearSelection();
        txtCep.clear();
        txtRua.clear();
        txtEndNumero.clear();
        txtEndComplemento.clear();

        if (dtpDataCadastro != null) dtpDataCadastro.setValue(null);
        tblClientes.getSelectionModel().clearSelection();
    }

    private void showInfo(String msg) {
        try { new Alert(AlertType.INFORMATION, msg).showAndWait(); } catch (Exception ex) { System.out.println(msg); }
    }

    private void showError(String msg) {
        try { new Alert(AlertType.ERROR, msg).showAndWait(); } catch (Exception ex) { System.err.println(msg); }
    }

    // Atualiza cliente existente (não altera ID)
    private void atualizarCliente() {
        try {
            String documento = txtDocumento.getText() != null ? txtDocumento.getText().trim() : null;
            String nome = txtNome.getText() != null ? txtNome.getText().trim() : null;
            String email = txtEmail.getText() != null ? txtEmail.getText().trim() : null;
            Boolean ativo = chkAtivo.isSelected();
            TipoPessoaVO tipo = obterTipoPessoaSelecionada();
            ClienteVO selecionado = tblClientes != null ? tblClientes.getSelectionModel().getSelectedItem() : null;
            if (tipo == null && selecionado != null) {
                tipo = selecionado.getPes_tipo_pessoa();
            }
            SexoVO sexo = cbSexo != null ? cbSexo.getValue() : null;

            // validações básicas
            validatePessoaForm(documento, nome, tipo);

            ClienteVO cliente = new ClienteVO();
            cliente.setPes_cpf(documento);
            cliente.setPes_nome(nome);
            cliente.setPes_email(email);
            cliente.setPes_ativo(ativo);
            cliente.setPes_tipo_pessoa(tipo);
            if (sexo != null) cliente.setPes_sexo(sexo);

            LocalDate dt = dtpDataCadastro != null ? dtpDataCadastro.getValue() : null;
            if (dt == null && selecionado != null) {
                dt = selecionado.getCli_dtCadastro();
            }
            if (dt == null) {
                dt = LocalDate.now();
            }
            cliente.setCli_dtCadastro(dt);

            clienteRN.atualizarCliente(cliente);
            showInfo("Cliente atualizado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Falha ao atualizar Cliente: " + e.getMessage());
        }
    }

    @FXML public void onAdicionarEndereco() { }

    @FXML public void onEditarEndereco() { }

    @FXML public void onRemoverEndereco() { }

    @FXML public void onMarcarEnderecoPrincipal() { }

    @FXML public void onAdicionarTelefone() { }

    @FXML public void onEditarTelefone() { }

    @FXML public void onRemoverTelefone() { }
    

}

