package controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

import model.dao.TipoPessoaDAO;
import model.dao.ConexaoDAO;
import model.dao.SexoDAO;
import model.dao.TelefoneDAO;
import model.dao.EndPostalDAO;
import model.dao.EstadoDAO;
import model.dao.CidadeDAO;
import model.dao.BairroDAO;
import model.dao.LogradouroDAO;
import model.vo.ClienteVO;
import model.vo.TipoPessoaVO;
import model.vo.SexoVO;
import model.vo.TelefoneVO;
import model.vo.EnderecoVO;
import model.vo.EndPostalVO;
import model.vo.EstadoVO;
import model.vo.CidadeVO;
import model.vo.BairroVO;
import model.vo.LogradouroVO;
import model.rn.ClienteRN;

public class ClienteController {

    // Toolbar / filtros
    @FXML private Button btnNovo;
    @FXML private Button btnEditar;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;
    @FXML private Button btnExcluir;
    @FXML private Button btnAtualizar;
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cbStatus; // Ativo/Inativo/Todos
    @FXML private ComboBox<String> cbTipoPessoa; // F/J/Todos

    // Tabela de clientes
    @FXML private TableView<ClienteVM> tblClientes;
    @FXML private TableColumn<ClienteVM, String> colDocumento;
    @FXML private TableColumn<ClienteVM, String> colNome;
    @FXML private TableColumn<ClienteVM, String> colTipo;
    @FXML private TableColumn<ClienteVM, String> colEmail;
    @FXML private TableColumn<ClienteVM, String> colCelular;
    @FXML private TableColumn<ClienteVM, String> colStatus;
    @FXML private TableColumn<ClienteVM, LocalDate> colCadastro;

    // FormulÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡rio (Dados)
    @FXML private TextField txtId;
    @FXML private ComboBox<String> cbTipoPessoaForm; // carrega de tb_tipoPessoa (F/J)
    @FXML private TextField txtDocumento;
    @FXML private TextField txtNomeRazao;
    @FXML private DatePicker dpNascimentoFundacao;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> cbSexo;
    @FXML private CheckBox chkAtivo;
    @FXML private TextArea txtObservacoes;

    // Telefone bÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡sico (Dados)
    @FXML private TextField txtTelPais;
    @FXML private TextField txtTelDdd;
    @FXML private TextField txtTelNumero;
    // EndereÃ§o simples
    @FXML private TextField txtCep;
    @FXML private TextField txtEndNumero;
    @FXML private TextField txtEndComplemento;
    // Combos de endereÃ§o na aba Dados
    @FXML private ComboBox<model.vo.EstadoVO> cbEstado;
    @FXML private ComboBox<model.vo.CidadeVO> cbCidade;
    @FXML private ComboBox<model.vo.LogradouroVO> cbLogradouro;
    @FXML private ComboBox<model.vo.BairroVO> cbBairro;
    @FXML private TextField txtRua;

    // EndereÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§os
    @FXML private TableView<EnderecoVM> tblEnderecos;
    @FXML private TableColumn<EnderecoVM, String> colEndCep;
    @FXML private TableColumn<EnderecoVM, String> colEndLogradouro;
    @FXML private TableColumn<EnderecoVM, String> colEndNumero;
    @FXML private TableColumn<EnderecoVM, String> colEndBairro;
    @FXML private TableColumn<EnderecoVM, String> colEndCidade;
    @FXML private TableColumn<EnderecoVM, String> colEndUf;
    @FXML private TableColumn<EnderecoVM, String> colEndPrincipal;

    // Telefones
    @FXML private TableView<TelefoneVM> tblTelefones;
    @FXML private TableColumn<TelefoneVM, String> colTelPais;
    @FXML private TableColumn<TelefoneVM, String> colTelDdd;
    @FXML private TableColumn<TelefoneVM, String> colTelNumero;
    @FXML private TableColumn<TelefoneVM, String> colTelTipo;

    // Status bar
    @FXML private Label lblStatus;

    private final ObservableList<ClienteVM> clientes = FXCollections.observableArrayList();
    private final ObservableList<TelefoneVM> telefones = FXCollections.observableArrayList();
    private final ObservableList<EnderecoVM> enderecos = FXCollections.observableArrayList();
    private final TipoPessoaDAO tipoPessoaDAO = new TipoPessoaDAO();

    @FXML
    private void initialize() {
        // Configura combos de filtro
        cbStatus.setItems(FXCollections.observableArrayList("Todos", "Ativo", "Inativo"));
        cbStatus.getSelectionModel().selectFirst();
        carregarTipos();
        if (cbSexo != null) {
            carregarSexos();
        }

        // Configura tabela de clientes (placeholders de propriedades)
        colDocumento.setCellValueFactory(data -> data.getValue().documentoProperty());
        colNome.setCellValueFactory(data -> data.getValue().nomeProperty());
        colTipo.setCellValueFactory(data -> data.getValue().tipoProperty());
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());
        colCelular.setCellValueFactory(data -> data.getValue().celularProperty());
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        colCadastro.setCellValueFactory(data -> data.getValue().cadastroProperty());

        tblClientes.setItems(clientes);
        tblClientes.setPlaceholder(new Label("Nenhum cliente"));

        // Tabela de telefones
        if (tblTelefones != null) {
            tblTelefones.setItems(telefones);
            if (colTelPais != null) colTelPais.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getPais()));
            if (colTelDdd != null) colTelDdd.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDdd()));
            if (colTelNumero != null) colTelNumero.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNumero()));
            if (colTelTipo != null) colTelTipo.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTipo()));
        }

        // Tabela de endereÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§os
        if (tblEnderecos != null) {
            tblEnderecos.setItems(enderecos);
            if (colEndCep != null) colEndCep.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCep()));
            if (colEndLogradouro != null) colEndLogradouro.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getLogradouro()));
            if (colEndNumero != null) colEndNumero.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNumero()));
            if (colEndBairro != null) colEndBairro.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getBairro()));
            if (colEndCidade != null) colEndCidade.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCidade()));
            if (colEndUf != null) colEndUf.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getUf()));
            if (colEndPrincipal != null) colEndPrincipal.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getPrincipal()));
        }

        // Carrega dados reais do BD
        atualizarLista();

        setModoVisualizacao();
        lblStatus.setText("Pronto");
        // carregar estados/bairros/logradouros e listener de estado
        try {
            if (cbEstado != null) {
                try (java.sql.Connection con = ConexaoDAO.getConexao()) {
                    EstadoDAO estDao = new EstadoDAO(con);
                    var estados = estDao.buscarTodosEstados();
                    cbEstado.setItems(FXCollections.observableArrayList(estados));
                }
                cbEstado.getSelectionModel().selectedItemProperty().addListener((o,ov,nv) -> carregarCidadesPorEstado(nv!=null? nv.getEst_sigla(): null));
            }
            if (cbBairro != null) {
                try (java.sql.Connection con = ConexaoDAO.getConexao()) {
                    BairroDAO bDao = new BairroDAO(con);
                    cbBairro.setItems(FXCollections.observableArrayList(bDao.buscarTodosBairros()));
                }
            }
            if (cbLogradouro != null) {
                try (java.sql.Connection con = ConexaoDAO.getConexao()) {
                    LogradouroDAO lDao = new LogradouroDAO(con);
                    cbLogradouro.setItems(FXCollections.observableArrayList(lDao.buscarTodosLogradouros()));
                }
            }
        } catch (Exception ignore) {};
    }

    // AÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âµes toolbar
    @FXML public void onNovo(ActionEvent e) { setModoEdicao(true); limparFormulario(); lblStatus.setText("Novo cliente"); }
    @FXML public void onEditar(ActionEvent e) { setModoEdicao(true); lblStatus.setText("Editando"); }
    // onSalvar implementado abaixo com persistÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âªncia
    @FXML public void onCancelar(ActionEvent e) { setModoVisualizacao(); lblStatus.setText("Cancelado"); }
    @FXML public void onExcluir(ActionEvent e) { lblStatus.setText("ExcluÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­do"); }
    @FXML public void onAtualizar(ActionEvent e) { atualizarLista(); }
    private void atualizarLista() {
        try {
            String nome = txtBuscar.getText();
            String tipo = cbTipoPessoa.getSelectionModel().getSelectedItem();
            Boolean ativo = null;
            String status = cbStatus.getSelectionModel().getSelectedItem();
            if ("Ativo".equals(status)) ativo = true; else if ("Inativo".equals(status)) ativo = false;
            ClienteRN service = new ClienteRN();
            List<ClienteVO> lista = service.buscarComFiltros(nome, tipo, ativo);
            clientes.clear();
            for (ClienteVO d : lista) {
                ClienteVM vm = new ClienteVM();
                vm.setDocumento(d.getPes_cpf());
                vm.setNome(d.getPes_nome());
                vm.setTipo(d.getPes_tipo_pessoa() != null ? d.getPes_tipo_pessoa().getCodigo() : "");
                vm.setEmail(d.getPes_email());
                vm.setCelular(""); // preencher via DAO de telefone se desejar
                vm.setStatus(Boolean.TRUE.equals(d.getPes_ativo()) ? "Ativo" : "Inativo");
                vm.setCadastro(d.getCli_dtCadastro() != null ? d.getCli_dtCadastro() : LocalDate.now());
                clientes.add(vm);
            }
            lblStatus.setText("Lista atualizada");
        } catch (Exception ex) {
            lblStatus.setText("Erro ao listar: " + ex.getMessage());
        }
    }

    @FXML
    public void onSalvar(ActionEvent e) {
        try {
            String tipoCodigo = cbTipoPessoaForm.getSelectionModel().getSelectedItem();
            String documento = somenteDigitos(txtDocumento.getText());
            String nome = txtNomeRazao.getText();
            String email = txtEmail.getText();
            LocalDate data = dpNascimentoFundacao.getValue();
            boolean ativo = chkAtivo.isSelected();

            if (tipoCodigo == null || tipoCodigo.isBlank()) { alerta("Selecione o Tipo de Pessoa (F/J)"); return; }
            if (documento == null || documento.isBlank() || documento.length()!=11) { alerta("CPF deve ter 11 dÃ­gitos"); return; }
            if (nome == null || nome.isBlank()) { alerta("Informe o nome"); return; }

            try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
                model.dao.SexoDAO sexoDAO = new SexoDAO(con);

                // Monta VO de cliente (ClienteVO herda PessoaVO)
                model.vo.ClienteVO cliente = new model.vo.ClienteVO();
                cliente.setPes_cpf(documento);
                cliente.setPes_nome(nome);
                cliente.setPes_email(email);
                cliente.setPes_dt_nascimento(data);
                cliente.setPes_ativo(ativo);
                model.vo.TipoPessoaVO tp = new model.vo.TipoPessoaVO();
                tp.setTipo_pessoa_id(obterTipoPessoaIdPorCodigo(tipoCodigo)); // jÃ¡ existe no controller
                tp.setCodigo(tipoCodigo);
                cliente.setPes_tipo_pessoa(tp);

                if (cbSexo != null && cbSexo.getSelectionModel().getSelectedItem() != null) {
                    String sexoDesc = cbSexo.getSelectionModel().getSelectedItem();
                    SexoVO sx = sexoDAO.buscarPorDescricao(sexoDesc);
                    if (sx != null) cliente.setPes_sexo(sx);
                }

                String telNumero = somenteDigitos(txtTelNumero.getText());
                java.util.ArrayList<TelefoneVO> tels = new java.util.ArrayList<>();
                if (telNumero != null && !telNumero.isBlank()) {
                    TelefoneVO tel = new TelefoneVO();
                    String telPais = somenteDigitos(txtTelPais.getText());
                    String telDdd = somenteDigitos(txtTelDdd.getText());
                    tel.setTel_codPais((telPais == null || telPais.isBlank()) ? "55" : telPais);
                    tel.setTel_ddd((telDdd == null) ? "" : telDdd);
                    tel.setTel_numero(telNumero);
                    tels.add(tel);
                }

                // EndereÃ§o opcional do formulÃ¡rio
                java.util.ArrayList<model.vo.EnderecoVO> ends = new java.util.ArrayList<>();
                String cepDigits = somenteDigitos(txtCep != null ? txtCep.getText() : null);
                if (cepDigits != null && !cepDigits.isBlank()) {
                    if (cepDigits.length() != 8) { alerta("CEP deve ter 8 dÃ­gitos"); return; }
                    model.dao.EndPostalDAO endPDao = new model.dao.EndPostalDAO(con);
                    model.vo.EndPostalVO endP = endPDao.buscarPorCep(cepDigits);
                    if (endP == null) { alerta("CEP nÃ£o cadastrado na base"); return; }
                    model.vo.EnderecoVO end = new model.vo.EnderecoVO();
                    end.setEnd_endP_id(endP);
                    end.setEnd_numero(txtEndNumero != null ? txtEndNumero.getText() : "");
                    end.setEnd_complemento(txtEndComplemento != null ? txtEndComplemento.getText() : "");
                    ends.add(end);
                }

                cliente.setCli_dtCadastro(java.time.LocalDate.now());
                model.rn.ClienteRN service = new model.rn.ClienteRN();
                service.salvarNovo(cliente, tels, ends);
            }

            atualizarLista();
            setModoVisualizacao();
            lblStatus.setText("Cliente salvo");
        } catch (Exception ex) {
            alerta("Erro ao salvar: " + ex.getMessage());
        }
    }

    private int obterTipoPessoaIdPorCodigo(String codigo) throws java.sql.SQLException {
        for (model.vo.TipoPessoaVO t : tipoPessoaDAO.listarTodos()) {
            if (t.getCodigo() != null && t.getCodigo().equalsIgnoreCase(codigo)) return t.getTipo_pessoa_id();
        }
        return -1;
    }

    private static String somenteDigitos(String s) {
        if (s == null) return null;
        return s.replaceAll("[^0-9]", "");
    }

    private void alerta(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void carregarTipos() {
        try {
            List<TipoPessoaVO> tipos = tipoPessoaDAO.listarTodos();
            // Filtros (top)
            ObservableList<String> filtro = FXCollections.observableArrayList();
            filtro.add("Todos");
            for (TipoPessoaVO t : tipos) {
                filtro.add("F".equalsIgnoreCase(t.getCodigo()) ? "FÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­sica" : "JurÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­dica");
            }
            cbTipoPessoa.setItems(filtro);
            cbTipoPessoa.getSelectionModel().selectFirst();
            // FormulÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡rio (cÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³digo)
            ObservableList<String> form = FXCollections.observableArrayList();
            for (TipoPessoaVO t : tipos) { form.add(t.getCodigo()); }
            cbTipoPessoaForm.setItems(form);
        } catch (Exception e) {
            cbTipoPessoa.setItems(FXCollections.observableArrayList("Todos", "FÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­sica", "JurÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­dica"));
            cbTipoPessoa.getSelectionModel().selectFirst();
            cbTipoPessoaForm.setItems(FXCollections.observableArrayList("F", "J"));
        }
    }

    private void carregarSexos() {
        try (java.sql.Connection con = ConexaoDAO.getConexao()) {
            SexoDAO dao = new SexoDAO(con);
            java.util.List<SexoVO> sexos = dao.buscarTodosSexo();
            ObservableList<String> itens = FXCollections.observableArrayList();
            for (SexoVO s : sexos) itens.add(s.getSex_descricao());
            cbSexo.setItems(itens);
        } catch (Exception ignore) {
            cbSexo.setItems(FXCollections.observableArrayList("Masculino", "Feminino", "Outros"));
        }
    }

    private void carregarCidadesPorEstado(String sigla) {
        if (cbCidade == null) return;
        if (sigla == null || sigla.isBlank()) { cbCidade.getItems().clear(); return; }
        try (java.sql.Connection con = ConexaoDAO.getConexao()) {
            CidadeDAO cDao = new CidadeDAO(con);
            var cidades = cDao.listarPorEstado(sigla);
            cbCidade.setItems(FXCollections.observableArrayList(cidades));
        } catch (Exception ignore) { cbCidade.getItems().clear(); }
    }

    // AÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âµes endereÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§os
    @FXML public void onAdicionarEndereco(ActionEvent e) {
        try (java.sql.Connection con = ConexaoDAO.getConexao()) {
            String documento = somenteDigitos(txtDocumento.getText());
            if (documento == null || documento.isBlank()) { alerta("Informe o documento e Salve o cliente antes de adicionar endereÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§os."); return; }
            TextInputDialog dCep = new TextInputDialog(""); dCep.setHeaderText(null); dCep.setContentText("CEP (8 dÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­gitos):");
            String cep = somenteDigitos(dCep.showAndWait().orElse(""));
            if (cep == null || cep.length()!=8) { alerta("CEP invÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡lido"); return; }
            TextInputDialog dNum = new TextInputDialog(""); dNum.setHeaderText(null); dNum.setContentText("NÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âºmero:");
            String numero = dNum.showAndWait().orElse("");
            TextInputDialog dComp = new TextInputDialog(""); dComp.setHeaderText(null); dComp.setContentText("Complemento:");
            String complemento = dComp.showAndWait().orElse("");

            model.dao.EndPostalDAO endPDao = new model.dao.EndPostalDAO(con);
            var endP = endPDao.buscarPorCep(cep);
            if (endP == null) { alerta("CEP nÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â£o encontrado no cadastro base."); return; }

            model.dao.EnderecoDAO endDao = new model.dao.EnderecoDAO(con);
            model.dao.PesEndDAO pesEndDAO = new model.dao.PesEndDAO(con);
            model.vo.EnderecoVO end = new model.vo.EnderecoVO();
            end.setEnd_endP_id(endP);
            end.setEnd_numero(numero);
            end.setEnd_complemento(complemento);
            int endId = endDao.adicionarNovo(end);
            pesEndDAO.vincular(documento, endId);
            carregarEnderecos(documento);
            lblStatus.setText("EndereÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§o adicionado");
        } catch (Exception ex) { alerta("Erro ao adicionar endereÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§o: " + ex.getMessage()); }
    }
    @FXML public void onEditarEndereco(ActionEvent e) { lblStatus.setText("EndereÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§o editado"); }
    @FXML public void onRemoverEndereco(ActionEvent e) { lblStatus.setText("EndereÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§o removido"); }
    @FXML public void onMarcarEnderecoPrincipal(ActionEvent e) { lblStatus.setText("EndereÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§o principal"); }

    // AÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â§ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âµes telefones
    @FXML public void onAdicionarTelefone(ActionEvent e) {
        try (java.sql.Connection con = ConexaoDAO.getConexao()) {
            String documento = somenteDigitos(txtDocumento.getText());
            if (documento == null || documento.isBlank()) { alerta("Informe o documento e Salve o cliente antes de adicionar telefones."); return; }
            TextInputDialog dPais = new TextInputDialog("55"); dPais.setHeaderText(null); dPais.setContentText("CÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â³digo do PaÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â­s:");
            String pais = dPais.showAndWait().orElse("55");
            TextInputDialog dDdd = new TextInputDialog(""); dDdd.setHeaderText(null); dDdd.setContentText("DDD:");
            String ddd = dDdd.showAndWait().orElse("");
            TextInputDialog dNum = new TextInputDialog(""); dNum.setHeaderText(null); dNum.setContentText("NÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âºmero:");
            String num = dNum.showAndWait().orElse("");
            String numDigits = somenteDigitos(num);
            if (numDigits == null || numDigits.isBlank()) { alerta("NÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Âºmero invÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¡lido"); return; }
            TelefoneDAO telDao = new TelefoneDAO(con);
            TelefoneVO tel = new TelefoneVO();
            tel.setTel_codPais(somenteDigitos(pais));
            tel.setTel_ddd(somenteDigitos(ddd));
            tel.setTel_numero(numDigits);
            telDao.adicionarNovo(tel, documento);
            carregarTelefones(documento);
            lblStatus.setText("Telefone adicionado");
        } catch (Exception ex) { alerta("Erro ao adicionar telefone: " + ex.getMessage()); }
    }
    @FXML public void onEditarTelefone(ActionEvent e) { lblStatus.setText("Telefone editado"); }
    @FXML public void onRemoverTelefone(ActionEvent e) { lblStatus.setText("Telefone removido"); }

    private void limparFormulario() {
        txtId.clear();
        cbTipoPessoaForm.getSelectionModel().clearSelection();
        txtDocumento.clear();
        txtNomeRazao.clear();
        dpNascimentoFundacao.setValue(null);
        txtEmail.clear();
        chkAtivo.setSelected(true);
        txtObservacoes.clear();
    }

    private void carregarTelefones(String documento) {
try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
model.dao.TelefoneDAO dao = new model.dao.TelefoneDAO(con);
java.util.List<model.vo.TelefoneVO> lista = dao.buscarPorCpf(documento);
telefones.clear();
for (model.vo.TelefoneVO t : lista) {
TelefoneVM vm = new TelefoneVM();
vm.pais.set(t.getTel_codPais());
vm.ddd.set(t.getTel_ddd());
vm.numero.set(t.getTel_numero());
vm.tipo.set("");
telefones.add(vm);
}
} catch (Exception ignore) {}
}

private void carregarEnderecos(String documento) {
    try (java.sql.Connection con = model.dao.ConexaoDAO.getConexao()) {
        model.dao.EnderecoDAO endDao = new model.dao.EnderecoDAO(con);
        java.util.List<model.vo.EnderecoVO> lista = endDao.buscarPorDocumento(documento);
        enderecos.clear();
        model.dao.EndPostalDAO endPDao = new model.dao.EndPostalDAO(con);
        for (model.vo.EnderecoVO e : lista) {
            EnderecoVM vm = new EnderecoVM();
            vm.numero.set(e.getEnd_numero());
            var ep = endPDao.buscarPorId(e.getEnd_endP_id().getEndP_id());
            if (ep != null) {
                vm.cep.set(ep.getEndP_cep());
                vm.logradouro.set(ep.getEndP_nomeRua());
                if (ep.getEndP_estado() != null) {
                    vm.uf.set(ep.getEndP_estado().getEst_sigla());
                }

                if (ep.getEndP_bairro() != null) {
                    vm.bairro.set(String.valueOf(ep.getEndP_bairro().getBairro_id()));
                }

                if (ep.getEndP_cidade() != null) {
                    vm.cidade.set(String.valueOf(ep.getEndP_cidade().getCid_id()));
                }
            }
        enderecos.add(vm);
        }
    } catch (Exception ignore) {}
}

    private void setModoEdicao(boolean... enable) {
        boolean editar = enable.length == 0 || enable[0];
        // Toolbar
        btnNovo.setDisable(editar);
        btnEditar.setDisable(editar);
        btnSalvar.setDisable(!editar);
        btnCancelar.setDisable(!editar);
        btnExcluir.setDisable(editar);
        btnAtualizar.setDisable(editar);
        // Filtros
        txtBuscar.setDisable(editar);
        cbStatus.setDisable(editar);
        cbTipoPessoa.setDisable(editar);
        // Form
        txtId.setDisable(true);
        cbTipoPessoaForm.setDisable(!editar);
        txtDocumento.setDisable(!editar);
        txtNomeRazao.setDisable(!editar);
        if (txtTelPais != null) txtTelPais.setDisable(!editar);
        if (txtTelDdd != null) txtTelDdd.setDisable(!editar);
        if (txtTelNumero != null) txtTelNumero.setDisable(!editar);
        if (txtCep != null) txtCep.setDisable(!editar);
        if (txtEndNumero != null) txtEndNumero.setDisable(!editar);
        if (txtEndComplemento != null) txtEndComplemento.setDisable(!editar);
        dpNascimentoFundacao.setDisable(!editar);
        txtEmail.setDisable(!editar);
        chkAtivo.setDisable(!editar);
        txtObservacoes.setDisable(!editar);
    }

    private void setModoVisualizacao() { setModoEdicao(false); }

    // ViewModels simples para tabela (substituir por suas entidades)
    public static class ClienteVM {
        private final javafx.beans.property.SimpleStringProperty documento = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty nome = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty tipo = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty email = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty celular = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty status = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.ObjectProperty<LocalDate> cadastro = new javafx.beans.property.SimpleObjectProperty<>(LocalDate.now());

        public static ClienteVM mock(String doc, String nome, String tipo, String email, String cel, boolean ativo){
            ClienteVM vm = new ClienteVM();
            vm.setDocumento(doc);
            vm.setNome(nome);
            vm.setTipo(tipo);
            vm.setEmail(email);
            vm.setCelular(cel);
            vm.setStatus(ativo ? "Ativo" : "Inativo");
            vm.setCadastro(LocalDate.now());
            return vm;
        }

        public javafx.beans.property.SimpleStringProperty documentoProperty(){ return documento; }
        public javafx.beans.property.SimpleStringProperty nomeProperty(){ return nome; }
        public javafx.beans.property.SimpleStringProperty tipoProperty(){ return tipo; }
        public javafx.beans.property.SimpleStringProperty emailProperty(){ return email; }
        public javafx.beans.property.SimpleStringProperty celularProperty(){ return celular; }
        public javafx.beans.property.SimpleStringProperty statusProperty(){ return status; }
        public javafx.beans.property.ObjectProperty<LocalDate> cadastroProperty(){ return cadastro; }

        public void setDocumento(String v){ documento.set(v);}    
        public void setNome(String v){ nome.set(v);}             
        public void setTipo(String v){ tipo.set(v);}             
        public void setEmail(String v){ email.set(v);}           
        public void setCelular(String v){ celular.set(v);}       
        public void setStatus(String v){ status.set(v);}         
        public void setCadastro(LocalDate v){ cadastro.set(v);}  
    }

    public static class EnderecoVM {
        private final javafx.beans.property.SimpleStringProperty cep = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty logradouro = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty numero = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty bairro = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty cidade = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty uf = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty principal = new javafx.beans.property.SimpleStringProperty("NÃƒÂ£o");
        
        public String getCep() { return cep.get(); }
        public String getLogradouro() { return logradouro.get(); }
        public String getNumero() { return numero.get(); }
        public String getBairro() { return bairro.get(); }
        public String getCidade() { return cidade.get(); }
        public String getUf() { return uf.get(); }
        public String getPrincipal() { return principal.get(); }
        // getters/properties omitidos por brevidade
    }

    

    public static class TelefoneVM {
        private final javafx.beans.property.SimpleStringProperty pais = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty ddd = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty numero = new javafx.beans.property.SimpleStringProperty("");
        private final javafx.beans.property.SimpleStringProperty tipo = new javafx.beans.property.SimpleStringProperty("");

        public String getPais() { return pais.get(); }
    public String getDdd() { return ddd.get(); }
    public String getNumero() { return numero.get(); }
    public String getTipo() { return tipo.get(); }
        // getters/properties omitidos por brevidade
    }
}
