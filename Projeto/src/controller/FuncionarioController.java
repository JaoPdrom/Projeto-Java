package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.rn.EnderecoRN;
import model.rn.FuncionarioRN;
import model.rn.PessoaRN;
import model.vo.BairroVO;
import model.vo.ContratacaoVO;
import model.vo.CargoVO;
import model.vo.EndPostalVO;
import model.vo.EnderecoVO;
import model.vo.FuncionarioVO;
import model.vo.EstadoVO;
import model.vo.CidadeVO;
import model.vo.LogradouroVO;
import model.vo.PessoaVO;
import model.vo.SexoVO;
import model.vo.TelefoneVO;
import model.vo.TipoPessoaVO;

public class FuncionarioController implements Initializable{

    // botoes
    @FXML private Button btnFuncionarioNovo;
    @FXML private Button btnFuncionarioSalvar;
    @FXML private Button btnFuncionarioEditar;
    @FXML private Button btnFuncionarioAtualizar;
    @FXML private Button btnFuncionarioExcluir;
    @FXML private Button btnFuncionarioCancelar;
    @FXML private Button btnFuncionarioBuscar;

    // campos pessoa
    @FXML private TextField txtFuncionarioBusca;
    @FXML private TextField txtFuncionarioPesDocumento;
    @FXML private TextField txtFuncionarioPesNome;
    @FXML private ComboBox<TipoPessoaVO> cbFuncionarioPesTipo; // carregador e seletor feito
    @FXML private ComboBox<SexoVO> cbFuncionarioPesSexo; // carregador e seletor feito
    @FXML private DatePicker dtpFuncionarioPesDtNascimento;
    @FXML private TextField txtFuncionarioPesEmail;

    // campos de telefone
    @FXML private TextField txtFuncionarioPesTelCodPais;
    @FXML private TextField txtFuncionarioPesTelDdd;    
    @FXML private TextField txtFuncionarioPesTelNumero;

    // campos de endereco
    @FXML private ComboBox<EstadoVO> cbFuncionarioPesEndEstado; // carregador e seletor feito
    @FXML private ComboBox<CidadeVO> cbFuncionarioPesEndCidade; // carregador por estado e seletor feito
    @FXML private ComboBox<LogradouroVO> cbFuncionarioPesEndLogradouro; // carregador e seletor feito
    @FXML private TextField txtFuncionarioPesEndCep;
    @FXML private TextField txtFuncionarioPesEndNomeRua;
    @FXML private TextField txtFuncionarioPesEndBairro;
    @FXML private TextField txtFuncionarioPesEndNumero;
    @FXML private TextField txtFuncionarioPesEndComplemento;

    // campos de funcionario
    @FXML private Label lbFuncionarioId;
    @FXML private TextField txtFuncionarioNumPis;
    @FXML private CheckBox chbFuncionarioPesAtivo;
    @FXML private DatePicker dtpFuncionarioDtContratacao;
    @FXML private TextField txtFuncionarioSalario;
    @FXML private ComboBox<CargoVO> cbFuncionarioCargo; // carregador e seletor feito
    @FXML private DatePicker dtpFuncionarioDtDemissao;
    @FXML private ComboBox<ContratacaoVO> cbFuncionarioContratacaoFase;
    @FXML private TextField txtFuncionarioDemissaoMotivo;
    @FXML private ComboBox<String> cbFuncionarioTipoBusca;

    @FXML private TableView<FuncionarioVO> tbvFuncionario;
    @FXML private TableColumn<FuncionarioVO, String> tbcFuncionarioId;
    @FXML private TableColumn<FuncionarioVO, String> tbcFuncionarioNome;
    @FXML private TableColumn<FuncionarioVO, String> tbcFuncionarioCargo;
    @FXML private TableColumn<FuncionarioVO, String> tbcFuncionarioAtivo;

    private final PessoaRN pessoaRN = new PessoaRN();
    private final EnderecoRN enderecoRN = new EnderecoRN();
    private final FuncionarioRN funcionarioRN = new FuncionarioRN();
    private FuncionarioVO funcionarioSelecionado;

    // -- CARREGADORES DE COMBOBOX
    public void carregarTipoPessoa() {
        try {
            List<TipoPessoaVO> tipos = pessoaRN.listarTipoPessoa();
            ObservableList<TipoPessoaVO> obs = FXCollections.observableArrayList(tipos);
            cbFuncionarioPesTipo.setItems(obs);
            info("Tipos de pessoa carregados");
        } catch (Exception e) {
            erro("Falha ao carregar tipos de pessoa", e);
        }
    }

    public void carregarSexo() {
        try {
            List<SexoVO> sexos = pessoaRN.listarSexo();
            ObservableList<SexoVO> obs = FXCollections.observableArrayList(sexos);
            cbFuncionarioPesSexo.setItems(obs);
            info("Sexos carregados");
        } catch (Exception e) {
            erro("Falha ao carregar sexos", e);
        }
    }

    public void carregarEstados() {
        try {
            List<EstadoVO> estados = enderecoRN.listarEstados();
            ObservableList<EstadoVO> obs = FXCollections.observableArrayList(estados);
            cbFuncionarioPesEndEstado.setItems(obs);
            info("Estados carregados");
        } catch (Exception e) {
            erro("Falha ao carregar estados", e);
        }
    }

    public List<CidadeVO> carregarCidadesPorEstado(String sigla) {
        List<CidadeVO> cidades = new ArrayList<>();
        try {
            cbFuncionarioPesEndCidade.getItems().clear();
            if (sigla != null && !sigla.isBlank()) {
                cidades = enderecoRN.listarCidadesPorEstado(sigla);
                ObservableList<CidadeVO> obs = FXCollections.observableArrayList(cidades);
                cbFuncionarioPesEndCidade.setItems(obs);
                info("Cidades carregadas para o estado " + sigla);
            } else {
                alerta("Sigla do estado não informada");
            }
        } catch (Exception e) {
            erro("Falha ao carregar cidades do estado " + sigla, e);
        }
        return cidades;
    }

    public void carregarLogradouros() {
        try {
            List<LogradouroVO> logradouros = enderecoRN.listarLogradouros();
            ObservableList<LogradouroVO> obs = FXCollections.observableArrayList(logradouros);
            cbFuncionarioPesEndLogradouro.setItems(obs);
            info("Logradouros carregados");
        } catch (Exception e) {
            erro("Falha ao carregar logradouros", e);
        }
    }

    public void carregarCargos() {
        try {
            List<CargoVO> cargos = funcionarioRN.listarCargos();
            ObservableList<CargoVO> obs = FXCollections.observableArrayList(cargos);
            cbFuncionarioCargo.setItems(obs);
            info("Cargos carregados");
        } catch (Exception e) {
            erro("Falha ao carregar cargos", e);
        }
    }

    public void carregarFases() {
        try {
            List<ContratacaoVO> fases = funcionarioRN.listarFasesContratacao();
            ObservableList<ContratacaoVO> observableFases = FXCollections.observableArrayList(fases);
            cbFuncionarioContratacaoFase.setItems(observableFases);
            info("Fases carregadas");
        } catch (Exception e) {
            erro("Erro ao carregar fases.", e);
        }

    }

    private void carregarTiposBusca() {
        ObservableList<String> tipos = FXCollections.observableArrayList("ID", "Documento", "Nome");
        cbFuncionarioTipoBusca.setItems(tipos);
        cbFuncionarioTipoBusca.getSelectionModel().selectFirst(); // opcional
    }


    private void listenerEstado() {
        if (cbFuncionarioPesEndEstado == null) {
            return;
        }
        cbFuncionarioPesEndEstado.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novoEstado) -> {
            if (cbFuncionarioPesEndCidade != null) {
                cbFuncionarioPesEndCidade.getItems().clear();
            }
            if (novoEstado != null && novoEstado.getEst_sigla() != null) {
                try {
                    carregarCidadesPorEstado(novoEstado.getEst_sigla());
                    if (cbFuncionarioPesEndCidade != null) {
                        cbFuncionarioPesEndCidade.setDisable(false);
                    }
                } catch (Exception e) {
                    erro("Falha ao carregar cidades por estado", e);
                    if (cbFuncionarioPesEndCidade != null) {
                        cbFuncionarioPesEndCidade.setDisable(true);
                    }
                }
            } else {
                if (cbFuncionarioPesEndCidade != null) {
                    cbFuncionarioPesEndCidade.setDisable(true);
                }
            }
        });
    }

    // -- OBTENÇÃO DE COMBOS
    private TipoPessoaVO obterCbTipoPessoaSelecionada() {
        if (cbFuncionarioPesTipo != null) {
            return cbFuncionarioPesTipo.getValue();
        }
        return null;
    }

    private SexoVO obterCbSexoSelecionado() {
        if (cbFuncionarioPesSexo != null) {
            return cbFuncionarioPesSexo.getValue();
        }
        return null;
    }

    private EstadoVO obterCbEstadoSelecionado() {
        if (cbFuncionarioPesEndEstado != null) {
            return cbFuncionarioPesEndEstado.getValue();
        }
        return null;
    }

    private CidadeVO obterCbCidadeSelecionada() {
        if (cbFuncionarioPesEndCidade != null) {
            return cbFuncionarioPesEndCidade.getValue();
        }
        return null;
    }

    private LogradouroVO obterCbLogradouroSelecionado() {
        if (cbFuncionarioPesEndLogradouro != null) {
            return cbFuncionarioPesEndLogradouro.getValue();
        }
        return null;
    }

    private CargoVO obterCbCargoSelecionado() {
        if (cbFuncionarioCargo != null) {
            return cbFuncionarioCargo.getValue();
        }
        return null;
    }

    private ContratacaoVO obterContratacaoFase() {
        if (cbFuncionarioContratacaoFase != null) {
            return cbFuncionarioContratacaoFase.getValue();
        }
        return null;
    }

    private String obterTipoBusca() {
        if (cbFuncionarioTipoBusca != null) {
            return cbFuncionarioTipoBusca.getValue();
        }
        return null;
    }

    private String normalizarDocumento(String doc) {
        if (doc == null) {
            return "";
        }
        return doc.replaceAll("\\D", "");
    }

    private PessoaVO criarBasePessoa() {
        try {
            String documento = null;
            if (txtFuncionarioPesDocumento != null) {
                documento = txtFuncionarioPesDocumento.getText();
            }
            if (documento != null) {
                documento = documento.trim();
            }

            String nome = null;
            if (txtFuncionarioPesNome != null) {
                nome = txtFuncionarioPesNome.getText();
            }
            if (nome != null) {
                nome = nome.trim();
            }

            String email = null;
            if (txtFuncionarioPesEmail != null) {
                email = txtFuncionarioPesEmail.getText();
            }
            if (email != null) {
                email = email.trim();
            }

            boolean ativo = chbFuncionarioPesAtivo != null && chbFuncionarioPesAtivo.isSelected();

            LocalDate nascimento = null;
            if (dtpFuncionarioPesDtNascimento != null) {
                nascimento = dtpFuncionarioPesDtNascimento.getValue();
            }

            SexoVO sexoSelecionado = obterCbSexoSelecionado();
            TipoPessoaVO tipoSelecionado = obterCbTipoPessoaSelecionada();

            if (documento == null || documento.isBlank()) {
                throw new IllegalArgumentException("Documento é obrigatório.");
            }

            if (nome == null || nome.isBlank()) {
                throw new IllegalArgumentException("Nome é obrigatório.");
            }

            if (tipoSelecionado == null) {
                throw new IllegalArgumentException("Tipo de pessoa é obrigatório.");
            }

            if (sexoSelecionado == null) {
                throw new IllegalArgumentException("Sexo é obrigatório.");
            }

            if (nascimento == null) {
                throw new IllegalArgumentException("Data de nascimento é obrigatória.");
            }


            List<TelefoneVO> telefones = criarTelefones(documento);
            List<EnderecoVO> enderecos = criarEnderecos();

            PessoaVO pessoa = new PessoaVO();
            pessoa.setPes_cpf(documento);
            pessoa.setPes_nome(nome);
            pessoa.setPes_email(email);
            pessoa.setPes_ativo(ativo);
            pessoa.setPes_dt_nascimento(nascimento);
            pessoa.setPes_sexo(sexoSelecionado);
            pessoa.setPes_tipo_pessoa(tipoSelecionado);
            pessoa.setTelefone(telefones);
            pessoa.setEndereco(enderecos);
            return pessoa;
        } catch (IllegalArgumentException e) {
            alerta("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            erro("Erro ao criar dados da pessoa", e);
        }
        return null;
    }

    private List<TelefoneVO> criarTelefones(String documento) {
        List<TelefoneVO> telefones = new ArrayList<>();

        String codPais = null;
        if (txtFuncionarioPesTelCodPais != null) {
            codPais = txtFuncionarioPesTelCodPais.getText();
        }

        String ddd = null;
        if (txtFuncionarioPesTelDdd != null) {
            ddd = txtFuncionarioPesTelDdd.getText();
        }

        String numero = null;
        if (txtFuncionarioPesTelNumero != null) {
            numero = txtFuncionarioPesTelNumero.getText();
        }

        if (codPais != null) {
            codPais = codPais.trim();
        }

        if (ddd != null) {
            ddd = ddd.trim();
        }

        if (numero != null) {
            numero = numero.trim();
        }

        if (codPais == null || codPais.isBlank()) {
            throw new IllegalArgumentException("Código do país é obrigatório.");
        }
        if (ddd == null || ddd.isBlank()) {
            throw new IllegalArgumentException("DDD é obrigatório.");
        }
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Número do telefone é obrigatório.");
        }

        TelefoneVO telefone = new TelefoneVO();
        if (funcionarioSelecionado != null && funcionarioSelecionado.getTelefone() != null && !funcionarioSelecionado.getTelefone().isEmpty()) {
            TelefoneVO existente = funcionarioSelecionado.getTelefone().get(0);
            telefone.setTel_id(existente.getTel_id());
        }
        telefone.setTel_codPais(codPais);
        telefone.setTel_ddd(ddd);
        telefone.setTel_numero(numero);
        telefone.setTel_pes_cpf(documento);
        telefones.add(telefone);
        return telefones;
    }

    private List<EnderecoVO> criarEnderecos() throws Exception {
        EstadoVO estado = obterCbEstadoSelecionado();
        CidadeVO cidade = obterCbCidadeSelecionada();
        LogradouroVO logradouro = obterCbLogradouroSelecionado();

        if (estado == null) {
            throw new IllegalArgumentException("Estado é obrigatório.");
        }
        if (cidade == null) {
            throw new IllegalArgumentException("Cidade é obrigatória.");
        }
        if (logradouro == null) {
            throw new IllegalArgumentException("Logradouro é obrigatório.");
        }

        String cep = null;
        if (txtFuncionarioPesEndCep != null) {
            cep = txtFuncionarioPesEndCep.getText();
        }

        String nomeRua = null;
        if (txtFuncionarioPesEndNomeRua != null) {
            nomeRua = txtFuncionarioPesEndNomeRua.getText();
        }

        String bairroTexto = null;
        if (txtFuncionarioPesEndBairro != null) {
            bairroTexto = txtFuncionarioPesEndBairro.getText();
        }

        String numero = null;
        if (txtFuncionarioPesEndNumero != null) {
            numero = txtFuncionarioPesEndNumero.getText();
        }

        String complemento = null;
        if (txtFuncionarioPesEndComplemento != null) {
            complemento = txtFuncionarioPesEndComplemento.getText();
        }

        if (cep != null) {
            cep = cep.trim();
        }

        if (nomeRua != null) {
            nomeRua = nomeRua.trim();
        }

        if (bairroTexto != null) {
            bairroTexto = bairroTexto.trim();
        }

        if (numero != null) {
            numero = numero.trim();
        }

        if (complemento != null) {
            complemento = complemento.trim();
        }

        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("CEP é obrigatório.");
        }

        if (nomeRua == null || nomeRua.isBlank()) {
            throw new IllegalArgumentException("Nome da rua é obrigatório.");
        }

        if (bairroTexto == null || bairroTexto.isBlank()) {
            throw new IllegalArgumentException("Bairro é obrigatório.");
        }

        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Número é obrigatório.");
        }

        BairroVO bairro = enderecoRN.obterOuCriarBairro(bairroTexto);

        EndPostalVO endPostal = new EndPostalVO();
        endPostal.setEndP_estado(estado);
        endPostal.setEndP_cidade(cidade);
        endPostal.setEndP_logradouro(logradouro);
        endPostal.setEndP_nomeRua(nomeRua);
        endPostal.setEndP_cep(cep);
        endPostal.setEndP_bairro(bairro);
        int endPostalId = enderecoRN.criarOuObterEndPostal(endPostal);
        endPostal.setEndP_id(endPostalId);

        EnderecoVO endereco = new EnderecoVO();
        if (funcionarioSelecionado != null && funcionarioSelecionado.getEndereco() != null && !funcionarioSelecionado.getEndereco().isEmpty()) {
            EnderecoVO existente = funcionarioSelecionado.getEndereco().get(0);
            endereco.setEnd_id(existente.getEnd_id());
        }
        endereco.setEnd_endP_id(endPostal);
        endereco.setEnd_numero(numero);
        if (complemento != null && !complemento.isBlank()) {
            endereco.setEnd_complemento(complemento);
        } else {
            endereco.setEnd_complemento(null);
        }

        List<EnderecoVO> lista = new ArrayList<>();
        lista.add(endereco);
        return lista;
    }

    private FuncionarioVO criarFuncionario(PessoaVO pessoa) {
        if (pessoa == null) {
            alerta("Pessoa não informada.");
            return null;
        }

        try {
            String numPis = null;
            if (txtFuncionarioNumPis != null) {
                numPis = txtFuncionarioNumPis.getText();
            }

            if (numPis != null) {
                numPis = numPis.trim();
            }

            if (numPis == null || numPis.isBlank()) {
                throw new IllegalArgumentException("Número do PIS é obrigatório.");
            }

            String salarioTxt = null;
            if (txtFuncionarioSalario != null) {
                salarioTxt = txtFuncionarioSalario.getText();
            }

            if (salarioTxt == null || salarioTxt.isBlank()) {
                throw new IllegalArgumentException("Salário é obrigatório.");
            }

            salarioTxt = salarioTxt.replace(".", "").replace(",", ".");
            double salario;
            try {
                salario = Double.parseDouble(salarioTxt);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Salário inválido.");
            }

            CargoVO cargo = obterCbCargoSelecionado();
            if (cargo == null) {
                throw new IllegalArgumentException("Selecione um cargo.");
            }

            LocalDate dtContratacao = null;
            if (dtpFuncionarioDtContratacao != null) {
                dtContratacao = dtpFuncionarioDtContratacao.getValue();
            }

            if (dtContratacao == null) {
                throw new IllegalArgumentException("Data de contratação é obrigatória.");
            }

            LocalDate dtDemissao = null;
            if (dtpFuncionarioDtDemissao != null) {
                dtDemissao = dtpFuncionarioDtDemissao.getValue();
            }

            String motivo = null;
            if (txtFuncionarioDemissaoMotivo != null) {
                motivo = txtFuncionarioDemissaoMotivo.getText();
            }

            if (motivo != null) {
                motivo = motivo.trim();
            }

            if (dtDemissao != null && (motivo == null || motivo.isBlank())) {
                throw new IllegalArgumentException("Informe o motivo da demissão.");
            }

            FuncionarioVO funcionario = new FuncionarioVO();
            if (lbFuncionarioId != null) {
                String textoId = lbFuncionarioId.getText();
                if (textoId != null && !textoId.isBlank()) {
                    funcionario.setFnc_id(Integer.parseInt(textoId));
                }
            }
            funcionario.setPes_cpf(pessoa.getPes_cpf());
            funcionario.setPes_nome(pessoa.getPes_nome());
            funcionario.setPes_email(pessoa.getPes_email());
            funcionario.setPes_dt_nascimento(pessoa.getPes_dt_nascimento());
            funcionario.setPes_sexo(pessoa.getPes_sexo());
            funcionario.setPes_tipo_pessoa(pessoa.getPes_tipo_pessoa());
            funcionario.setPes_ativo(pessoa.getPes_ativo());
            funcionario.setTelefone(pessoa.getTelefone());
            funcionario.setEndereco(pessoa.getEndereco());

            funcionario.setFnc_numPis(numPis);
            funcionario.setFnc_salario(salario);
            funcionario.setFnc_cargo(cargo);
            funcionario.setFnc_dtContratacao(dtContratacao);
            funcionario.setFnc_dtDemissao(dtDemissao);
            funcionario.setFnc_motivo_demissao(motivo);

            ContratacaoVO selecionada = obterContratacaoFase();
            ContratacaoVO contratacao = new ContratacaoVO();

            if (selecionada != null && selecionada.getContratacao_fase() != null) {
                contratacao.setContratacao_fase(selecionada.getContratacao_fase());
            } else {
                contratacao.setContratacao_fase("Contratação");
            }

            if (dtContratacao != null) {
                contratacao.setContratacao_dtContratacao(dtContratacao);
            } else {
                contratacao.setContratacao_dtContratacao(LocalDate.now());
            }
            funcionario.setContratacao(contratacao);

            return funcionario;
        } catch (IllegalArgumentException e) {
            alerta("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            erro("Erro ao montar dados do funcionário", e);
        }
        return null;
    }

    private void atualizarFuncionario() {
        try {
            if (funcionarioSelecionado == null || funcionarioSelecionado.getFnc_id() <= 0) {
                alerta("Selecione um funcionário para atualizar.");
                return;
            }

            PessoaVO pessoa = criarBasePessoa();
            if (pessoa == null) {
                return;
            }

            FuncionarioVO funcionario = criarFuncionario(pessoa);
            if (funcionario == null) {
                return;
            }
            funcionario.setFnc_id(funcionarioSelecionado.getFnc_id());

            funcionarioRN.atualizar(funcionario);
            alerta("Funcionário atualizado com sucesso.");
            carregarTabela();
            limparCampos();
            habilitarCampos(false);
            habilitarBotoes(false);
        } catch (Exception e) {
            erro("Erro ao atualizar funcionário", e);
            alerta("Falha ao atualizar funcionário: " + e.getMessage());
        }
    }

    
    // -- HANDLERS
    @FXML
    private void onNovoFuncionario(ActionEvent event) {
        limparCampos();
        habilitarCampos(true);
        habilitarBotoes(false);
        btnFuncionarioSalvar.setDisable(false);
        
        info("Botão Novo pressionado (handler ainda não implementado).");
    }

    @FXML
    private void onSalvarFuncionario(ActionEvent event) {
        try {
            PessoaVO pessoa = criarBasePessoa();
            if (pessoa == null) {
                return;
            }

            FuncionarioVO funcionario = criarFuncionario(pessoa);
            if (funcionario == null) {
                return;
            }

            funcionarioRN.salvarNovo(funcionario);
            alerta("Funcionário salvo com sucesso.");
            limparCampos();
            carregarTabela();
            habilitarBotoes(false);
        } catch (Exception e) {
            erro("Erro ao salvar funcionário", e);
            alerta("Falha ao salvar funcionário: " + e.getMessage());
        }
    }

    @FXML
    private void onEditarFuncionario(ActionEvent event) {
        btnFuncionarioAtualizar.setDisable(false);
        habilitarCampos(true);
        info("Botão Editar pressionado.");
    }

    @FXML
    private void onAtualizarFuncionario(ActionEvent event) {
        try {
            atualizarFuncionario();            
        } catch (Exception e) {
            erro("Erro ao atualizar Funcionario.", e);
        }
    }

    @FXML
    private void onExcluirFuncionario(ActionEvent event) {
        try {
            if (funcionarioSelecionado == null) {
                alerta("Selecione um funcionário para excluir.");
                return;
            }
            funcionarioRN.deletar(funcionarioSelecionado);
            alerta("Funcionário excluído com sucesso.");
            carregarTabela();
            limparCampos();
            habilitarCampos(false);
            habilitarBotoes(false);
        } catch (Exception e) {
            erro("Erro ao excluir funcionário", e);
            alerta("Falha ao excluir funcionário: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelarBusca(ActionEvent event) {
        limparCampos();
        habilitarBotoes(false);
        habilitarCampos(false);
        info("Botão Cancelar pressionado.");
    }

    @FXML
    private void onBuscarFuncionario(ActionEvent event) {
        try {
            String termo = txtFuncionarioBusca != null ? txtFuncionarioBusca.getText() : null;
            if (termo != null) {
                termo = termo.trim();
            }

            if (termo == null || termo.isBlank()) {
                carregarTabela();
                return;
            }

            String tipo = obterTipoBusca();
            if (tipo == null) {
                alerta("Selecione um tipo de busca.");
                return;
            }

            List<FuncionarioVO> todos = funcionarioRN.listarFuncionarios();
            List<FuncionarioVO> filtrados = new ArrayList<>();

            if ("ID".equalsIgnoreCase(tipo)) {
                int idBuscado;
                try {
                    idBuscado = Integer.parseInt(termo);
                } catch (NumberFormatException e) {
                    alerta("Informe um ID numérico válido.");
                    return;
                }
                for (FuncionarioVO f : todos) {
                    if (f != null && f.getFnc_id() == idBuscado) {
                        filtrados.add(f);
                    }
                }
            } else if ("Documento".equalsIgnoreCase(tipo)) {
                String docBuscado = normalizarDocumento(termo);
                for (FuncionarioVO f : todos) {
                    if (f != null && normalizarDocumento(f.getPes_cpf()).equals(docBuscado)) {
                        filtrados.add(f);
                    }
                }
            } else { // Nome
                String nomeBuscado = termo.toLowerCase();
                for (FuncionarioVO f : todos) {
                    if (f != null && f.getPes_nome() != null &&
                            f.getPes_nome().toLowerCase().contains(nomeBuscado)) {
                        filtrados.add(f);
                    }
                }
            }

            ObservableList<FuncionarioVO> obs = FXCollections.observableArrayList(filtrados);
            if (tbvFuncionario != null) {
                tbvFuncionario.setItems(obs);
            }

            if (filtrados.isEmpty()) {
                alerta("Nenhum funcionário encontrado para a busca informada.");
            }
        } catch (Exception e) {
            erro("Erro ao buscar funcionário", e);
            alerta("Falha ao buscar funcionário: " + e.getMessage());
        }
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        habilitarCampos(false);
        habilitarBotoes(false);
        listenerEstado();
        carregarCargos();
        carregarEstados();
        carregarLogradouros();
        carregarSexo();
        carregarTipoPessoa();
        carregarFases();
        carregarTabela();
        carregarTiposBusca();
        if (tbvFuncionario != null) {
            tbvFuncionario.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> preencherCamposFuncionario(novo));
        }
    }

    private void habilitarCampos(boolean habilitado){
        // txtFuncionarioBusca.setDisable(!habilitado);
        txtFuncionarioPesDocumento.setDisable(!habilitado);
        txtFuncionarioPesNome.setDisable(!habilitado);
        txtFuncionarioPesEmail.setDisable(!habilitado);
        txtFuncionarioPesTelCodPais.setDisable(!habilitado);
        txtFuncionarioPesTelDdd.setDisable(!habilitado);
        txtFuncionarioPesTelNumero.setDisable(!habilitado);
        txtFuncionarioPesEndCep.setDisable(!habilitado);
        txtFuncionarioPesEndNomeRua.setDisable(!habilitado);
        txtFuncionarioPesEndBairro.setDisable(!habilitado);
        txtFuncionarioPesEndNumero.setDisable(!habilitado);
        txtFuncionarioPesEndComplemento.setDisable(!habilitado);
        txtFuncionarioNumPis.setDisable(!habilitado);
        txtFuncionarioSalario.setDisable(!habilitado);
        txtFuncionarioDemissaoMotivo.setDisable(!habilitado);
        // lbFuncionarioId.setDisable(!habilitado);
        
        dtpFuncionarioPesDtNascimento.setDisable(!habilitado);
        dtpFuncionarioDtContratacao.setDisable(!habilitado);
        dtpFuncionarioDtDemissao.setDisable(!habilitado);
        cbFuncionarioPesTipo.setDisable(!habilitado);
        cbFuncionarioPesSexo.setDisable(!habilitado);
        cbFuncionarioPesEndEstado.setDisable(!habilitado);
        cbFuncionarioPesEndCidade.setDisable(!habilitado);
        cbFuncionarioPesEndLogradouro.setDisable(!habilitado);
        cbFuncionarioCargo.setDisable(!habilitado);
        cbFuncionarioContratacaoFase.setDisable(!habilitado);
        chbFuncionarioPesAtivo.setDisable(!habilitado);
    }

    private void habilitarBotoes(boolean habilitado) {
        btnFuncionarioSalvar.setDisable(!habilitado);
        btnFuncionarioEditar.setDisable(!habilitado);
        btnFuncionarioAtualizar.setDisable(!habilitado);
        btnFuncionarioExcluir.setDisable(!habilitado);
        btnFuncionarioCancelar.setDisable(!habilitado);
    }

    private void limparCampos() {
        funcionarioSelecionado = null;
        txtFuncionarioBusca.clear();
        txtFuncionarioPesDocumento.clear();
        txtFuncionarioPesNome.clear();
        txtFuncionarioPesEmail.clear();
        txtFuncionarioPesTelCodPais.clear();
        txtFuncionarioPesTelDdd.clear();
        txtFuncionarioPesTelNumero.clear();
        txtFuncionarioPesEndCep.clear();
        txtFuncionarioPesEndNomeRua.clear();
        txtFuncionarioPesEndBairro.clear();
        txtFuncionarioPesEndNumero.clear();
        txtFuncionarioPesEndComplemento.clear();
        txtFuncionarioNumPis.clear();
        txtFuncionarioSalario.clear();
        txtFuncionarioDemissaoMotivo.clear();
        lbFuncionarioId.setText("");
        if (dtpFuncionarioPesDtNascimento != null) {
            dtpFuncionarioPesDtNascimento.setValue(null);
        }
        if (dtpFuncionarioDtContratacao != null) {
            dtpFuncionarioDtContratacao.setValue(null);
        }
        if (dtpFuncionarioDtDemissao != null) {
            dtpFuncionarioDtDemissao.setValue(null);
        }
        if (chbFuncionarioPesAtivo != null) {
            chbFuncionarioPesAtivo.setSelected(false);
        }
        if (cbFuncionarioPesTipo != null) {
            cbFuncionarioPesTipo.setValue(null);
        }
        if (cbFuncionarioPesSexo != null) {
            cbFuncionarioPesSexo.setValue(null);
        }
        if (cbFuncionarioPesEndEstado != null) {
            cbFuncionarioPesEndEstado.setValue(null);
        }
        if (cbFuncionarioPesEndCidade != null) {
            cbFuncionarioPesEndCidade.setValue(null);
        }
        if (cbFuncionarioPesEndLogradouro != null) {
            cbFuncionarioPesEndLogradouro.setValue(null);
        }
        if (cbFuncionarioCargo != null) {
            cbFuncionarioCargo.setValue(null);
        }
        if (cbFuncionarioContratacaoFase != null) {
            cbFuncionarioContratacaoFase.setValue(null);
        }
    }

    private void carregarTabela() {
        try {
            List<FuncionarioVO> lista = funcionarioRN.listarFuncionarios();
            ObservableList<FuncionarioVO> obs = FXCollections.observableArrayList(lista);
            if (tbvFuncionario != null) {
                tbvFuncionario.setItems(obs);
            }

            if (tbcFuncionarioId != null) {
                tbcFuncionarioId.setCellValueFactory(cd -> {
                    FuncionarioVO item = cd.getValue();
                    if (item != null && item.getFnc_id() > 0) {
                        return new javafx.beans.property.SimpleStringProperty(String.valueOf(item.getFnc_id()));
                    }
                    return new javafx.beans.property.SimpleStringProperty("");
                });
            }

            if (tbcFuncionarioNome != null) {
                tbcFuncionarioNome.setCellValueFactory(cd -> {
                    FuncionarioVO item = cd.getValue();
                    if (item != null && item.getPes_nome() != null) {
                        return new javafx.beans.property.SimpleStringProperty(item.getPes_nome());
                    }
                    return new javafx.beans.property.SimpleStringProperty("");
                });
            }

            if (tbcFuncionarioCargo != null) {
                tbcFuncionarioCargo.setCellValueFactory(cd -> {
                    FuncionarioVO item = cd.getValue();
                    if (item != null && item.getFnc_cargo() != null && item.getFnc_cargo().getCargo_descricao() != null) {
                        return new javafx.beans.property.SimpleStringProperty(item.getFnc_cargo().getCargo_descricao());
                    }
                    return new javafx.beans.property.SimpleStringProperty("");
                });
            }

            if (tbcFuncionarioAtivo != null) {
                tbcFuncionarioAtivo.setCellValueFactory(cd -> {
                    FuncionarioVO item = cd.getValue();
                    if (item != null && item.getPes_ativo() != null && item.getPes_ativo()) {
                        return new javafx.beans.property.SimpleStringProperty("Ativo");
                    }
                    return new javafx.beans.property.SimpleStringProperty("Inativo");
                });
            }

            info("Tabela de funcionários carregada: " + obs.size() + " registros.");
        } catch (Exception e) {
            erro("Falha ao carregar tabela de funcionários", e);
        }
    }

    private void preencherCamposFuncionario(FuncionarioVO funcionario) {
        if (funcionario == null) {
            limparCampos();
            return;
        }
        funcionarioSelecionado = funcionario;
        // habilitarCampos(true);
        // habilitarBotoes(true);
        btnFuncionarioEditar.setDisable(false);


        if (lbFuncionarioId != null) {
            lbFuncionarioId.setText(String.valueOf(funcionario.getFnc_id()));
        }

        if (txtFuncionarioPesDocumento != null) {
            txtFuncionarioPesDocumento.setText(funcionario.getPes_cpf());
        }
        
        if (txtFuncionarioPesNome != null) {
            txtFuncionarioPesNome.setText(funcionario.getPes_nome());
        }

        if (txtFuncionarioPesEmail != null) {
            txtFuncionarioPesEmail.setText(funcionario.getPes_email());
        }

        if (dtpFuncionarioPesDtNascimento != null) {
            dtpFuncionarioPesDtNascimento.setValue(funcionario.getPes_dt_nascimento());
        }

        if (chbFuncionarioPesAtivo != null) {
            chbFuncionarioPesAtivo.setSelected(funcionario.getPes_ativo() != null && funcionario.getPes_ativo());
        }

        if (txtFuncionarioNumPis != null) {
            txtFuncionarioNumPis.setText(funcionario.getFnc_numPis());
        }

        if (txtFuncionarioSalario != null) {
            txtFuncionarioSalario.setText(funcionario.getFnc_salario() > 0 ? String.valueOf(funcionario.getFnc_salario()) : "");
        }

        if (dtpFuncionarioDtContratacao != null) {
            if (funcionario.getContratacao() != null && funcionario.getContratacao().getContratacao_dtContratacao() != null) {
                dtpFuncionarioDtContratacao.setValue(funcionario.getContratacao().getContratacao_dtContratacao());
            } else {
                dtpFuncionarioDtContratacao.setValue(funcionario.getFnc_dtContratacao());
            }
        }

        if (dtpFuncionarioDtDemissao != null) {
            dtpFuncionarioDtDemissao.setValue(funcionario.getFnc_dtDemissao());
        }

        if (txtFuncionarioDemissaoMotivo != null) {
            txtFuncionarioDemissaoMotivo.setText(funcionario.getFnc_motivo_demissao() != null ? funcionario.getFnc_motivo_demissao() : "");
        }

        if (cbFuncionarioPesTipo != null && funcionario.getPes_tipo_pessoa() != null) {
            cbFuncionarioPesTipo.getItems().stream()
                .filter(tp -> tp.getTipo_pessoa_id() == funcionario.getPes_tipo_pessoa().getTipo_pessoa_id())
                .findFirst().ifPresent(cbFuncionarioPesTipo::setValue);
        }

        if (cbFuncionarioPesSexo != null && funcionario.getPes_sexo() != null) {
            cbFuncionarioPesSexo.getItems().stream()
                .filter(sexo -> sexo.getSex_id() == funcionario.getPes_sexo().getSex_id())
                .findFirst().ifPresent(cbFuncionarioPesSexo::setValue);
        }

        if (cbFuncionarioCargo != null && funcionario.getFnc_cargo() != null) {
            cbFuncionarioCargo.getItems().stream()
                .filter(c -> c.getCar_id() == funcionario.getFnc_cargo().getCar_id())
                .findFirst().ifPresent(cbFuncionarioCargo::setValue);
        }

        if (cbFuncionarioContratacaoFase != null && funcionario.getContratacao() != null) {
            cbFuncionarioContratacaoFase.getItems().stream()
                .filter(f -> funcionario.getContratacao().getContratacao_fase().equalsIgnoreCase(f.getContratacao_fase()))
                .findFirst().ifPresent(cbFuncionarioContratacaoFase::setValue);
        }

        if (funcionario.getTelefone() != null && !funcionario.getTelefone().isEmpty()) {
            TelefoneVO tel = funcionario.getTelefone().get(0);
            if (txtFuncionarioPesTelCodPais != null) {
                txtFuncionarioPesTelCodPais.setText(tel.getTel_codPais());
            }

            if (txtFuncionarioPesTelDdd != null) {
                txtFuncionarioPesTelDdd.setText(tel.getTel_ddd());
            }

            if (txtFuncionarioPesTelNumero != null) {
                txtFuncionarioPesTelNumero.setText(tel.getTel_numero());
            }
        }

        if (funcionario.getEndereco() != null && !funcionario.getEndereco().isEmpty()) {
            EnderecoVO endereco = funcionario.getEndereco().get(0);
            EndPostalVO postal = endereco.getEnd_endP_id();

            info("Estado: " + postal.getEndP_estado());
            info("Cidade: " + postal.getEndP_cidade());
            info("Logradouro: " + postal.getEndP_logradouro());
            info("CEP: " + postal.getEndP_cep());
            info("Nome rua: " + postal.getEndP_nomeRua());
            info("Bairro: " + postal.getEndP_bairro());

            if (postal != null) {
                if (cbFuncionarioPesEndEstado != null && postal.getEndP_estado() != null) {
                    cbFuncionarioPesEndEstado.getItems().stream()
                        .filter(est -> est.getEst_sigla().equalsIgnoreCase(postal.getEndP_estado().getEst_sigla()))
                        .findFirst().ifPresent(cbFuncionarioPesEndEstado::setValue);
                        info("Valor em cbFuncionarioPesEndEstado: " + postal.getEndP_estado().getEst_sigla());
                }

                if (cbFuncionarioPesEndCidade != null && postal.getEndP_cidade() != null) {
                    cbFuncionarioPesEndCidade.getItems().stream()
                        .filter(cidade -> cidade.getCid_id() == postal.getEndP_cidade().getCid_id())
                        .findFirst().ifPresent(cbFuncionarioPesEndCidade::setValue);
                }

                if (cbFuncionarioPesEndLogradouro != null && postal.getEndP_logradouro() != null) {
                    cbFuncionarioPesEndLogradouro.getItems().stream()
                        .filter(log -> log.getLogradouro_id() == postal.getEndP_logradouro().getLogradouro_id())
                        .findFirst().ifPresent(cbFuncionarioPesEndLogradouro::setValue);
                }

                if (txtFuncionarioPesEndNomeRua != null) {
                    txtFuncionarioPesEndNomeRua.setText(postal.getEndP_nomeRua());
                }

                if (txtFuncionarioPesEndCep != null) {
                    txtFuncionarioPesEndCep.setText(postal.getEndP_cep());
                }

                if (txtFuncionarioPesEndBairro != null && postal.getEndP_bairro() != null) {
                    txtFuncionarioPesEndBairro.setText(postal.getEndP_bairro().getBairro_descricao());
                }
            }

            if (txtFuncionarioPesEndNumero != null) {
                txtFuncionarioPesEndNumero.setText(endereco.getEnd_numero());
            }

            if (txtFuncionarioPesEndComplemento != null) {
                txtFuncionarioPesEndComplemento.setText(endereco.getEnd_complemento());
            }
        }
    }

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    private void info(String msg) {
        System.out.println(GREEN + "[INFO:FuncionarioController] " + msg + RESET);
    }

    private void alerta(String msg) {
        System.out.println(YELLOW + "[AVISO:FuncionarioController] " + msg + RESET);
    }

    private void erro(String msg, Exception e) {
        System.err.println(RED + "[ERRO:FuncionarioController] " + msg + RESET);
        if (e != null) {
            e.printStackTrace();
        }
    }
    
}
