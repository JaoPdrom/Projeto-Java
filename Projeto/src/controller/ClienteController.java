package controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.beans.property.SimpleStringProperty;
// import model.dao.TipoPessoaDAO;
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
    // IDs do item selecionado para permitir atualização correta
    private Integer telefoneSelecionadoId;
    private Integer enderecoSelecionadoId;

    // -- CARREGADORES DE COMBOBOX
    public void carregarTipoPessoa() throws SQLException{
        try {
            List<TipoPessoaVO> tipos = pessoaRN.listarTipoPessoa();
            ObservableList<TipoPessoaVO> observableTipos = FXCollections.observableArrayList(tipos);
            cbClientePesTipo.setItems(observableTipos);
            info("Lista de tipos de pessoas carregada");
        } catch (Exception e) {
            erro("Erro ao lista tipos de pessoa", e);
        }
    }

    public void carregarSexo(){
        try {
            List<SexoVO> listaSexo = pessoaRN.listarSexo();
            ObservableList<SexoVO> observableSexo = FXCollections.observableArrayList(listaSexo);
            cbClientePesSexo.setItems(observableSexo);
            info("Lista de sexos carregados");
        } catch (Exception e) {
            erro("Erro ao carregador lista de sexos", e);
        }
        
    }

    public void carregarEstados(){
        try {
            List<EstadoVO> estados = enderecoRN.listarEstados();
            ObservableList<EstadoVO> observableEstados = FXCollections.observableArrayList(estados);
            cbClienteEndEstado.setItems(observableEstados);
            info("Estados carregados com sucesso");
        } catch (Exception e) {
            erro("Erro ao carregar estados", e);
        }
    }

    public List<CidadeVO> carregarCidadesPorEstado(String sigla) {
        List<CidadeVO> cidades = new ArrayList<>();

        try {
            cbClienteEndCidade.getItems().clear();

            if (sigla != null && !sigla.isEmpty()) {
                cidades = enderecoRN.listarCidadesPorEstado(sigla);
                ObservableList<CidadeVO> observableCidades = FXCollections.observableArrayList(cidades);
                cbClienteEndCidade.setItems(observableCidades);
                info("Cidades carregadas com sucesso para o estado: " + sigla);
            } else {
                alerta("Sigla do estado não informada");
            }
        } catch (Exception e) {
            erro("Erro ao carregar cidades para o estado " + sigla, e);
        }

        return cidades;
    }


    public void carregarLogradouros(){
        try {
            List<LogradouroVO> logradouros = enderecoRN.listarLogradouros();
            ObservableList<LogradouroVO> observableLogradouros = FXCollections.observableArrayList(logradouros);
            cbClienteEndLogradouro.setItems(observableLogradouros);
            info("Logradouros carregados com sucesso");
        } catch (Exception e) {
            erro("Erro ao carregar logradouros", e);
        }
    }
    
    // -- SELECAO DE COMBOBOX
    private TipoPessoaVO obterCbTipoPessoaSelecionada() {
        if (cbClientePesTipo != null){
            info("Tipo pessoa selecionada");
            return cbClientePesTipo.getValue(); 
        }
        return null;
    }

    private SexoVO obterCbSexoSelecionado() {
        if (cbClientePesSexo != null){
            info("Sexo selecionado");
            return cbClientePesSexo.getValue();
        }
        return null;
    }

    private EstadoVO obterCbEstadoSelecionado(){
        if (cbClienteEndEstado != null){
            info("Estado selecionado");
            return cbClienteEndEstado.getValue();
        }
        return null;
    }

    private CidadeVO obterCbCidadeSelecionado(){
        if (cbClienteEndCidade != null){
            info("Cidade selecionada");
            return cbClienteEndCidade.getValue();
        }
        return null;
    }

    private LogradouroVO obterCbLogradouroSelecionado(){
        if (cbClienteEndLogradouro != null){
            info("Logradouro selecionado");
            return cbClienteEndLogradouro.getValue();
        }
        return null;
    }

    // -- CAPTURA DE TELEFONE --
    private List<TelefoneVO> criarTelefones(String documento) {
        List<TelefoneVO> telefones = new ArrayList<>();

        String codPais = null;
        if (txtClienteTelCodPais != null) {
            String v = txtClienteTelCodPais.getText();
            if (v != null) {
                info("Cod pais obtido: " + codPais);
                codPais = v.trim();
            }
        }

        String ddd = null;
        if (txtClienteTelDdd != null) {
            String v = txtClienteTelDdd.getText();
            if (v != null) {
                info("DDD obtido: " + ddd);
                ddd = v.trim();
            }
        }

        String numero = null;
        if (txtClienteTelNumero != null) {
            String v = txtClienteTelNumero.getText();
            if (v != null) {
                info("Numero obtido: " + numero);
                numero = v.trim();
            }
        }

        // Validação: todos os campos de telefone são obrigatórios
        if (codPais == null || codPais.isBlank()) {
            alerta("Código do país do telefone é obrigatório.");
            throw new IllegalArgumentException("Código do país do telefone é obrigatório.");
        }
        if (ddd == null || ddd.isBlank()) {
            alerta("DDD do telefone é obrigatório.");
            throw new IllegalArgumentException("DDD do telefone é obrigatório.");
        }
        if (numero == null || numero.isBlank()) {
            alerta("Número do telefone é obrigatório.");
            throw new IllegalArgumentException("Número do telefone é obrigatório.");
        }

        TelefoneVO tel = new TelefoneVO();
        tel.setTel_codPais(codPais);
        info("setTel_codPais FEITO");
        tel.setTel_ddd(ddd);
        info("setTel_ddd FEITO");
        tel.setTel_numero(numero);
        info("setTel_numero FEITO");
        tel.setTel_pes_cpf(documento);
        info("setTel_pes_cpf FEITO");

        telefones.add(tel);
        info("Telefone de cliente criado.");

        return telefones;
    }


    // -- LISTERNERS --
    private void listenerEstado() {
        cbClienteEndEstado.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, novoEstado) -> {
            // Limpa ComboBox de cidades
            cbClienteEndCidade.getItems().clear();
            info("cbClienteEndCidade CLEAR");

            cbClienteEndCidade.getSelectionModel().clearSelection();
            info("cbClienteEndCidade CLEAR SELECTION");

            if (novoEstado != null) {
                try {
                    // Carrega cidades do estado selecionado
                    carregarCidadesPorEstado(novoEstado.getEst_sigla());
                    cbClienteEndCidade.setDisable(false);
                    info("Cidades carregadas por estado.");
                } catch (Exception e) {
                    erro("Erro ao carregar cidades: ", e);
                    cbClienteEndCidade.setDisable(true);
                }
            } else {
                cbClienteEndCidade.setDisable(true);
                info("Campos cbClienteEndCidade desabilitados.");
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
            alerta("Estado é obrigatório");
            throw new IllegalArgumentException("Estado é obrigatório.");
        }

        if (cidade == null) {
            alerta("Cidade é obrigatória");
            throw new IllegalArgumentException("Cidade é obrigatória.");
        }

        if (logradouro == null) {
            alerta("Logradouro é obrigatório");
            throw new IllegalArgumentException("Logradouro é obrigatório.");
        }

        if (bairro == null) {
            alerta("Bairro é obrigatório");
            throw new IllegalArgumentException("Bairro é obrigatório.");
        }

        if (rua == null || rua.isBlank()) {
            alerta("Nome rua é obrigatório");
            throw new IllegalArgumentException("Nome da rua é obrigatório.");
        }

        if (cep == null || cep.isBlank()) {
            alerta("CEP é obrigatório");
            throw new IllegalArgumentException("CEP é obrigatório.");
        }

        if (numero == null || numero.isBlank()) {
            alerta("Numero é obrigatório");
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
            alerta("Erro de validação: " + e.getMessage());
            return null;
        } catch (Exception e) {
            erro("Erro ao criar pessoa", e);
            return null;
        }
    }

    private void criarCliente(PessoaVO pessoaVO) throws Exception {
        if (pessoaVO == null){
            erro("Objeto PessoaVO está nulo ao criar cliente", null);
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

    // METODOS FMXL
    @FXML
    private void handleBtnClienteNovoAction(ActionEvent event){
        info("Botao NOVO pressionado.");
        limparCamposCliente();
        info("Campos zerados.");
        habilitarCampos(true);
        btnClienteSalvar.setDisable(false);
        info("Campos habilitados.");
    }

    @FXML
    private void handleBtnClienteCancelarAction(ActionEvent event){
        limparCamposCliente();
        habilitarCampos(false);
        if (tbvCliente != null && tbvCliente.getSelectionModel() != null) {
            tbvCliente.getSelectionModel().clearSelection();
        }
        info("Campos limpos, desabilitados e seleção da tabela limpa.");
    }
    
    @FXML
    private void handleBtnClienteSalvarAction(ActionEvent event) throws Exception {
        PessoaVO pesCriada = criarBasePessoa();
        if (pesCriada == null){
            alerta("Erro ao criar base pessoa");
            return;
        }
        info("Pessoa criada!");

        try {
            criarCliente(pesCriada);
            info("Cliente criado!");
        } catch (Exception e) {
            erro("Erro ao adicionar cliente", e);
            return;
        }

        try {
            criarEndereco();
            info("Endereço criado para cliente");
        } catch (Exception e) {
            erro("Erro ao adicionar endereco", e);
        }
    }

    @FXML
    private void handleBtnClienteEditar(ActionEvent event){
        try {
            // Habilita edição, mas mantém documento bloqueado
            habilitarCampos(true);
            btnClienteSalvar.setDisable(true);
            btnClienteAtualizar.setDisable(false);
            if (txtClientePesDocumento != null) {
                txtClientePesDocumento.setDisable(true);
            }
            info("Modo edição habilitado. Documento bloqueado.");
        } catch (Exception e) {
            erro("Erro ao entrar em modo de edição", e);
        }

    }

    @FXML
    private void handleBtnClienteAtualizar(ActionEvent event){
        try {
            // Monta base pessoa (sem endereço) para atualizar pessoa e telefones
            PessoaVO pessoa = criarBasePessoa();
            if (pessoa == null) {
                alerta("Não foi possível montar os dados da pessoa para atualização.");
                return;
            }

            // garantir tel_id do telefone selecionado, se houver
            if (pessoa.getTelefone() != null && !pessoa.getTelefone().isEmpty()) {
                TelefoneVO t = pessoa.getTelefone().get(0);
                if (telefoneSelecionadoId != null) {
                    t.setTel_id(telefoneSelecionadoId);
                }
            }

            // Atualiza dados básicos + telefones
            try {
                pessoaRN.atualizarPessoa(pessoa);
                info("Pessoa e telefones atualizados.");
            } catch (Exception e) {
                erro("Falha ao atualizar pessoa/telefones", e);
                return;
            }

            // Monta endereço a partir dos campos e sincroniza via EnderecoRN
            try {
                // Leitura e validação dos campos de endereço
                EstadoVO estado = obterCbEstadoSelecionado();
                CidadeVO cidade = obterCbCidadeSelecionado();
                LogradouroVO logradouro = obterCbLogradouroSelecionado();

                String cep = txtClienteEndCep != null && txtClienteEndCep.getText() != null ? txtClienteEndCep.getText().trim() : null;
                String rua = txtClienteEndNomeRua != null && txtClienteEndNomeRua.getText() != null ? txtClienteEndNomeRua.getText().trim() : null;
                String numero = txtClienteEndNumero != null && txtClienteEndNumero.getText() != null ? txtClienteEndNumero.getText().trim() : null;
                String complemento = txtClienteEndComplemento != null && txtClienteEndComplemento.getText() != null ? txtClienteEndComplemento.getText().trim() : null;
                String bairroTxt = txtClienteEndBairro != null && txtClienteEndBairro.getText() != null ? txtClienteEndBairro.getText().trim() : null;

                if (estado == null) { alerta("Estado é obrigatório"); return; }
                if (cidade == null) { alerta("Cidade é obrigatória"); return; }
                if (logradouro == null) { alerta("Logradouro é obrigatório"); return; }
                if (bairroTxt == null || bairroTxt.isBlank()) { alerta("Bairro é obrigatório"); return; }
                if (rua == null || rua.isBlank()) { alerta("Nome da rua é obrigatório"); return; }
                if (cep == null || cep.isBlank()) { alerta("CEP é obrigatório"); return; }
                if (numero == null || numero.isBlank()) { alerta("Número é obrigatório"); return; }

                EndPostalVO ep = new EndPostalVO();
                ep.setEndP_estado(estado);
                ep.setEndP_cidade(cidade);
                ep.setEndP_logradouro(logradouro);
                ep.setEndP_nomeRua(rua);
                ep.setEndP_cep(cep);
                // garantir/obter bairro
                BairroVO bairroVO = enderecoRN.obterOuCriarBairro(bairroTxt);
                ep.setEndP_bairro(bairroVO);

                EnderecoVO end = new EnderecoVO();
                end.setEnd_endP_id(ep);
                end.setEnd_numero(numero);
                end.setEnd_complemento(complemento != null && !complemento.isBlank() ? complemento : null);
                if (enderecoSelecionadoId != null) {
                    end.setEnd_id(enderecoSelecionadoId);
                }

                // sincroniza endereços garantindo EndPostal id
                enderecoRN.sincronizarEnderecos(pessoa.getPes_cpf(), List.of(end));
                info("Endereço atualizado/sincronizado.");
            } catch (Exception e) {
                erro("Falha ao atualizar endereço", e);
                // não retorna; pessoa já foi atualizada. Apenas avisa.
            }

            carregarTabela();
            alerta("Cliente atualizado com sucesso.");
        } catch (Exception e) {
            erro("Erro geral no fluxo de atualização", e);
        }
    }

    @FXML
    private void handleBtnClienteBuscarAction(ActionEvent event) {
        try {
            String termo = null;
            if (txtClienteBusca != null) {
                String s = txtClienteBusca.getText();
                if (s != null) {
                    termo = s.trim();
                }
            }

            if (termo == null || termo.isBlank()) {
                carregarTabela();
                return;
            }

            //apenasDigitos com 11 caracteres eh CPF; apenasDigitos (outro tamanho) eh ID; caso contrário -> nome
            String apenasDigitos = termo.replaceAll("\\D", "");
            List<ClienteVO> resultado = new ArrayList<>();
            if (!apenasDigitos.isBlank() && apenasDigitos.length() == 11) {
                // Busca por CPF
                try {
                    ClienteVO cli = clienteRN.buscarPorDocumento(apenasDigitos);
                    if (cli != null) {
                        resultado.add(cli);
                    }
                } catch (Exception e) {
                    erro("Erro na busca por CPF", e);
                }
            } else if (!apenasDigitos.isBlank() && termo.equals(apenasDigitos)) {
                // Somente números mas não 11 dígitos: tratar como ID
                try {
                    int id = Integer.parseInt(apenasDigitos);
                    List<ClienteVO> todos = clienteRN.listarClientesCompletos();
                    for (ClienteVO c : todos) {
                        if (c != null && c.getCli_id() == id) {
                            resultado.add(c);
                        }
                    }
                } catch (Exception e) {
                    erro("Erro na busca por ID", e);
                }
            } else {
                // Busca por nome (like)
                try {
                    resultado = clienteRN.buscarTodosClientes(termo);
                } catch (Exception e) {
                    erro("Erro na busca por nome", e);
                }
            }
            if (resultado == null || resultado.isEmpty()) {
                alerta("Nenhum cliente encontrado.");
                // opcional: limpar tabela ou recarregar tudo
                tbvCliente.setItems(FXCollections.observableArrayList());
                return;
            }
            ObservableList<ClienteVO> obs = FXCollections.observableArrayList(resultado);
            tbvCliente.setItems(obs);
            info("Busca concluída: " + obs.size() + " registro(s).");
        } catch (Exception e) {
            erro("Erro geral ao executar busca", e);
        }
    }


    //controle de campos
    private void habilitarCampos(boolean habilitar){
        btnClienteSalvar.setDisable(!habilitar);
        btnClienteEditar.setDisable(!habilitar); 
        btnClienteAtualizar.setDisable(!habilitar);
        btnClienteExcluir.setDisable(!habilitar);
        txtClienteId.setDisable(!habilitar);
        txtClientePesDocumento.setDisable(!habilitar);
        txtClientePesNome.setDisable(!habilitar);
        txtClientePesEmail.setDisable(!habilitar);
        txtClienteTelCodPais.setDisable(!habilitar);
        txtClienteTelDdd.setDisable(!habilitar);
        txtClienteTelNumero.setDisable(!habilitar);
        txtClienteEndCep.setDisable(!habilitar);
        txtClienteEndNomeRua.setDisable(!habilitar);
        txtClienteEndBairro.setDisable(!habilitar);
        txtClienteEndNumero.setDisable(!habilitar);
        txtClienteEndComplemento.setDisable(!habilitar);

        cbClientePesTipo.setDisable(!habilitar);
        cbClientePesSexo.setDisable(!habilitar); 
        cbClienteEndEstado.setDisable(!habilitar); 
        cbClienteEndCidade.setDisable(!habilitar); 
        cbClienteEndLogradouro.setDisable(!habilitar); 
        dtpClientePesDataNascimento.setDisable(!habilitar);
        chbClienteAtivo.setDisable(!habilitar);
    }

    private void limparCamposCliente() {
        txtClienteBusca.clear();
        txtClienteId.clear();
        txtClientePesDocumento.clear();
        txtClientePesNome.clear();
        txtClientePesEmail.clear();
        txtClienteTelCodPais.clear();
        txtClienteTelDdd.clear();
        txtClienteTelNumero.clear();
        txtClienteEndCep.clear();
        txtClienteEndNomeRua.clear();
        txtClienteEndBairro.clear();
        txtClienteEndNumero.clear();
        txtClienteEndComplemento.clear();
        cbClientePesSexo.setValue(null);
        cbClientePesTipo.setValue(null);
        cbClienteEndEstado.setValue(null);
        cbClienteEndCidade.setValue(null);
        cbClienteEndLogradouro.setValue(null);
        telefoneSelecionadoId = null;
        enderecoSelecionadoId = null;
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        info("Inicializando tela de clientes");
        try{
            habilitarCampos(false);
            carregarTipoPessoa();
            carregarSexo();
            carregarEstados();
            carregarLogradouros();
            listenerEstado();
            carregarTabela();
        } catch (Exception e){
            erro("Erro ao inicializar", e);
        }
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
    
    private void carregarTabela(){
        try {
            info("Carregando lista de clientes");
            String filtro = null;
            if (txtClienteBusca != null) {
                String v = txtClienteBusca.getText();
                if (v != null) {
                    filtro = v.trim();
                }
            }

            List<ClienteVO> lista = clienteRN.listarClientesCompletos();
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
            info("Tabela de clientes carregada: " + obs.size() + " registros");
        } catch (Exception e) {
            erro("Erro ao carregar tabela de clientes", e);
        }
    }

    @FXML
    private void selecionarItemTabelaCliente(MouseEvent event) {
        try {
            ClienteVO clienteSelecionado = tbvCliente.getSelectionModel().getSelectedItem();
            limparCamposCliente();
            btnClienteEditar.setDisable(false);
            btnClienteAtualizar.setDisable(false);
            btnClienteExcluir.setDisable(false);

            if (clienteSelecionado == null) {
                info("Nenhum cliente selecionado na tabela.");
                return;
            }

            info("Cliente selecionado: " + clienteSelecionado.getPes_nome());

            // dados basicos
            txtClienteId.setText(String.valueOf(clienteSelecionado.getCli_id()));
            txtClientePesDocumento.setText(clienteSelecionado.getPes_cpf());
            txtClientePesNome.setText(clienteSelecionado.getPes_nome());
            txtClientePesEmail.setText(clienteSelecionado.getPes_email());
            dtpClientePesDataNascimento.setValue(clienteSelecionado.getPes_dt_nascimento());
            info("Dados básicos preenchidos com sucesso.");

            // sexo
            try {
                SexoVO sexo = clienteSelecionado.getPes_sexo();
                if (sexo != null && sexo.getSex_descricao() != null) {
                    // Seleciona no combo pelo id para garantir referência da lista
                    cbClientePesSexo.getItems().stream()
                        .filter(i -> i.getSex_id() == sexo.getSex_id())
                        .findFirst()
                        .ifPresent(cbClientePesSexo::setValue);
                    info("Sexo definido como: " + sexo.getSex_descricao());
                } else {
                    alerta("Sexo não definido para o cliente.");
                }
            } catch (Exception e) {
                erro("Erro ao definir sexo", e);
            }

            // tipo de pessoa
            try {
                TipoPessoaVO tipoPessoa = clienteSelecionado.getPes_tipo_pessoa();
                if (tipoPessoa != null && tipoPessoa.getDescricao() != null) {
                    cbClientePesTipo.setValue(tipoPessoa);
                    info("Tipo de pessoa definido como: " + tipoPessoa.getDescricao());
                } else {
                    alerta("Tipo de pessoa não definido para o cliente.");
                }
            } catch (Exception e) {
                erro("Erro ao definir tipo de pessoa", e);
            }

            // telefone
            try {
                if (clienteSelecionado.getTelefone() != null && !clienteSelecionado.getTelefone().isEmpty()) {
                    TelefoneVO tel = clienteSelecionado.getTelefone().get(0);
                    try { telefoneSelecionadoId = tel.getTel_id(); } catch (Exception ignore) { telefoneSelecionadoId = null; }
                    txtClienteTelCodPais.setText(tel.getTel_codPais());
                    txtClienteTelDdd.setText(tel.getTel_ddd());
                    txtClienteTelNumero.setText(tel.getTel_numero());
                    info("Telefone carregado: +" + tel.getTel_codPais() + " (" + tel.getTel_ddd() + ") " + tel.getTel_numero());
                } else {
                    alerta("Nenhum telefone cadastrado para este cliente.");
                }
            } catch (Exception e) {
                erro("Erro ao definir telefone", e);
            }

            // endereco
            try {
                if (clienteSelecionado.getEndereco() != null && !clienteSelecionado.getEndereco().isEmpty()) {
                    EnderecoVO end = clienteSelecionado.getEndereco().get(0);
                    try { enderecoSelecionadoId = end.getEnd_id(); } catch (Exception ignore) { enderecoSelecionadoId = null; }
                    EndPostalVO endPostal = end.getEnd_endP_id();

                    if (endPostal != null) {
                        txtClienteEndCep.setText(endPostal.getEndP_cep());
                        txtClienteEndNomeRua.setText(endPostal.getEndP_nomeRua());
                        if (endPostal.getEndP_bairro() != null)
                            txtClienteEndBairro.setText(endPostal.getEndP_bairro().getBairro_descricao());

                        // Seleciona Logradouro no combo se disponível
                        LogradouroVO logradouro = endPostal.getEndP_logradouro();
                        if (logradouro != null) {
                            cbClienteEndLogradouro.getItems().stream()
                                .filter(i -> i.getLogradouro_id() == logradouro.getLogradouro_id())
                                .findFirst()
                                .ifPresent(cbClienteEndLogradouro::setValue);
                        }

                        EstadoVO estado = endPostal.getEndP_estado();
                        CidadeVO cidade = endPostal.getEndP_cidade();

                        if (estado != null) {
                            cbClienteEndEstado.setValue(estado);
                            carregarCidadesPorEstado(estado.getEst_sigla());
                            info("Estado definido como: " + estado.getEst_sigla());
                        } else {
                            alerta("Estado não definido.");
                        }

                        if (cidade != null) {
                            cbClienteEndCidade.setValue(cidade);
                            info("Cidade definida como: " + cidade.getCid_descricao());
                        } else {
                            alerta("Cidade não definida.");
                        }
                    }

                    txtClienteEndNumero.setText(end.getEnd_numero());
                    txtClienteEndComplemento.setText(end.getEnd_complemento());
                    info("Endereço carregado com sucesso.");
                } else {
                    alerta("Nenhum endereço cadastrado para este cliente.");
                }
            } catch (Exception e) {
                erro("Erro ao definir endereço", e);
            }

        } catch (Exception e) {
            erro("Erro geral ao selecionar cliente", e);
        }
    }

    // Cor ANSI para o console (funciona no VS Code, IntelliJ e CMD moderno)
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    private void info(String msg) {
        System.out.println(GREEN + "[INFO:ClienteController] " + msg + RESET);
    }

    private void alerta(String msg) {
        System.out.println(YELLOW + "[AVISO:ClienteController] " + msg + RESET);
    }

    private void erro(String msg, Exception e) {
        System.err.println(RED + "[ERRO:ClienteController] " + msg + RESET);
        if (e != null) e.printStackTrace();
    }

    
    
}
