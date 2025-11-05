DROP DATABASE IF EXISTS db_poo;

CREATE DATABASE IF NOT EXISTS db_poo;

USE db_poo;

-- Tabelas de localização
CREATE TABLE IF NOT EXISTS tb_sexo(
    sex_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    sex_descricao VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_cidade(
    cid_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cid_descricao VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_estado(
    est_sigla CHAR(2) NOT NULL PRIMARY KEY,
    est_descricao VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_bairro(
    bairro_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    bairro_descricao VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_cidEst(
    cidEstPai_cid_id INT,
    cidEstPai_est_sigla CHAR(2),
    PRIMARY KEY (cidEstPai_cid_id, cidEstPai_est_sigla),
    FOREIGN KEY (cidEstPai_cid_id) REFERENCES tb_cidade (cid_id),
    FOREIGN KEY (cidEstPai_est_sigla) REFERENCES tb_estado (est_sigla)
);

CREATE TABLE IF NOT EXISTS tb_logradouro(
    logradouro_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    log_descricao VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_endPostal(
    endP_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    endP_logradouro_id INT NOT NULL,
    endP_nomeRua VARCHAR(45) NOT NULL,
    endP_cep CHAR(9) NOT NULL,
    endP_cid_id INT NOT NULL,
    endP_est_sigla CHAR(2) NOT NULL,
    endP_bairro_id INT NOT NULL,
    FOREIGN KEY (endP_logradouro_id) REFERENCES tb_logradouro (logradouro_id),
    FOREIGN KEY (endP_cid_id) REFERENCES tb_cidEst (cidEstPai_cid_id),
    FOREIGN KEY (endP_est_sigla) REFERENCES tb_cidEst (cidEstPai_est_sigla),
    FOREIGN KEY (endP_bairro_id) REFERENCES tb_bairro(bairro_id)
);

CREATE TABLE IF NOT EXISTS tb_endereco(
    end_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    end_endP_id INT NOT NULL,
    end_numero VARCHAR(10) NOT NULL,
    end_complemento VARCHAR(45),
    FOREIGN KEY (end_endP_id) REFERENCES tb_endPostal (endP_id)
);

-- Tabela principal de Pessoa (única para clientes, funcionários, etc.)
CREATE TABLE IF NOT EXISTS tb_tipoPessoa(
    tipo_pessoa_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    codigo CHAR(1) NOT NULL UNIQUE,
    descricao VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_pessoa(
    pes_documento VARCHAR(14) NOT NULL PRIMARY KEY,
    pes_tipo_pessoa_id INT NOT NULL,
    pes_nome VARCHAR(100) NOT NULL,
    pes_sex_id INT NULL,
    pes_dtNascimento DATE NULL,
    pes_email VARCHAR(100),
    pes_ativo  BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT chk_pes_documento CHECK (pes_documento REGEXP '^[0-9]{11}$|^[0-9]{14}$'),
    FOREIGN KEY (pes_tipo_pessoa_id) REFERENCES tb_tipoPessoa (tipo_pessoa_id),
    FOREIGN KEY (pes_sex_id) REFERENCES tb_sexo (sex_id)
);

CREATE TABLE IF NOT EXISTS tb_cliente(
    cli_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT UNIQUE,
    cli_pes_documento VARCHAR(14) NOT NULL,
    cli_dtCadastro DATE NOT NULL,
    FOREIGN KEY (cli_pes_documento) REFERENCES tb_pessoa (pes_documento)
);

-- Tabela de relacionamento para permitir múltiplos endereços por pessoa
CREATE TABLE IF NOT EXISTS tb_pesEnd(
    pesEnd_pes_documento VARCHAR(14) NOT NULL,
    pesEnd_end_id INT NOT NULL,
    PRIMARY KEY (pesEnd_pes_documento, pesEnd_end_id),
    FOREIGN KEY (pesEnd_pes_documento) REFERENCES tb_pessoa(pes_documento),
    FOREIGN KEY (pesEnd_end_id) REFERENCES tb_endereco(end_id)
);

-- Tabela de relacionamento para permitir múltiplos telefones por pessoa
CREATE TABLE IF NOT EXISTS tb_telefone(
    tel_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    tel_codPais CHAR(2) NOT NULL,
    tel_ddd CHAR(2) NOT NULL,
    tel_numero VARCHAR(15) NOT NULL,
    tel_pes_documento VARCHAR(14) NOT NULL,
    FOREIGN KEY (tel_pes_documento) REFERENCES tb_pessoa (pes_documento)
);

-- Tabelas de Funcionários e Cargos
CREATE TABLE IF NOT EXISTS tb_cargo(
    cargo_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cargo_descricao VARCHAR(45) NOT NULL,
    cargo_ativo BOOLEAN DEFAULT TRUE NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_funcionario(
    fnc_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fnc_dtContratacao DATE NOT NULL,
    fnc_dtDemissao DATE NULL,
    fnc_salario DOUBLE NOT NULL,
    fnc_cargo_id INT NOT NULL,
    fnc_pes_documento VARCHAR(14) NOT NULL,
    fnc_ativo BOOLEAN DEFAULT TRUE NOT NULL,
    FOREIGN KEY (fnc_cargo_id) REFERENCES tb_cargo(cargo_id),
    FOREIGN KEY (fnc_pes_documento) REFERENCES tb_pessoa(pes_documento)
);

-- Tabelas de Vendas e Pagamentos
CREATE TABLE IF NOT EXISTS tb_statusVenda(
    statusVenda_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    statusVenda_descricao VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_tipoPagamento(
    tipoPagamento_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tipoPagamento_descricao VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_venda(
    venda_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    venda_data DATE NOT NULL,
    venda_pes_documento VARCHAR(14) NOT NULL,
    venda_fnc_id INT NOT NULL,
    venda_statusVenda INT NOT NULL,
    venda_tipo_pagamento INT NOT NULL,
    FOREIGN KEY (venda_pes_documento) REFERENCES tb_pessoa (pes_documento),
    FOREIGN KEY (venda_fnc_id) REFERENCES tb_funcionario (fnc_id),
    FOREIGN KEY (venda_statusVenda) REFERENCES tb_statusVenda (statusVenda_id),
    FOREIGN KEY (venda_tipo_pagamento) REFERENCES tb_tipoPagamento (tipoPagamento_id)
);

CREATE TABLE IF NOT EXISTS tb_tipoPdt(
    tipoPdt_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tipoPdt_descricao VARCHAR(45)
);

CREATE TABLE IF NOT EXISTS tb_produto(
    produto_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    produto_nome VARCHAR(45) NOT NULL,
    produto_qtdMax DOUBLE NOT NULL,
    produto_qtdMin DOUBLE NOT NULL,
    produto_tipoPdt INT NOT NULL,
    produto_ativo BOOLEAN DEFAULT TRUE NOT NULL,
    FOREIGN KEY (produto_tipoPdt) REFERENCES tb_tipoPdt (tipoPdt_id)
);

CREATE TABLE IF NOT EXISTS tb_itemVenda(
    itemVenda_venda_id INT NOT NULL,
    itemVenda_produto_id INT NOT NULL,
    itemVenda_qtd DOUBLE NOT NULL,
    itemVenda_preco DOUBLE NOT NULL,
    PRIMARY KEY (itemVenda_venda_id, itemVenda_produto_id),
    FOREIGN KEY (itemVenda_venda_id) REFERENCES tb_venda (venda_id),
    FOREIGN KEY (itemVenda_produto_id) REFERENCES tb_produto (produto_id)
);

-- Tabelas de Produtos e Estoque
CREATE TABLE IF NOT EXISTS tb_fornecedor(
    forn_cnpj VARCHAR(14) NOT NULL PRIMARY KEY,
    forn_razaSocial VARCHAR(100) NOT NULL,
    forn_nomeFantasia VARCHAR(45) NOT NULL,
    forn_ativo BOOLEAN DEFAULT TRUE NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_estoque(
    est_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    est_dtCompra DATE NOT NULL,
    est_produto_id INT NOT NULL,
    est_custo DOUBLE NOT NULL,
    est_qtdToal DOUBLE NOT NULL,
    est_lote VARCHAR(45) NOT NULL,
    est_dtValidade DATE NOT NULL,
    est_forn_cnpj CHAR(14) NOT NULL,
    FOREIGN KEY (est_produto_id) REFERENCES tb_produto(produto_id),
    FOREIGN KEY (est_forn_cnpj) REFERENCES tb_fornecedor(forn_cnpj)
);

-- Tabelas de Pedidos e Entregas
CREATE TABLE IF NOT EXISTS tb_statusPedido(
    statusPedido_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    statusPedido_descricao VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_tipoEntrega(
    tipoEntrega_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tipoEntrega_descricao VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_pedido(
    pedido_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    pedido_statusPedido INT NOT NULL,
    pedido_tipoEntrega INT NOT NULL,
    pedido_venda_id INT NOT NULL,
    pedido_fnc_id INT NOT NULL,
    FOREIGN KEY (pedido_venda_id) REFERENCES tb_venda (venda_id),
    FOREIGN KEY (pedido_fnc_id) REFERENCES tb_funcionario (fnc_id),
    FOREIGN KEY (pedido_statusPedido) REFERENCES tb_statuspedido (statusPedido_id),
    FOREIGN KEY (pedido_tipoEntrega) REFERENCES tb_tipoEntrega (tipoEntrega_id)
);

-- Tabelas Financeiras
CREATE TABLE IF NOT EXISTS tb_statusDebito(
    statusDeb_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    statusDeb_descricao VARCHAR(45) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_debito(
    deb_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    deb_venda_id INT NOT NULL,
    deb_juros DOUBLE,
    deb_status_id INT NOT NULL,
    FOREIGN KEY (deb_venda_id) REFERENCES tb_venda(venda_id),
    FOREIGN KEY (deb_status_id) REFERENCES tb_statusDebito(statusDeb_id)
);

CREATE TABLE IF NOT EXISTS td_despesa(
    despesa_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    despesa_descricao VARCHAR(45) NOT NULL,
    despesa_dtRealiazacao DATE NOT NULL,
    despesa_valor_pago DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_infoEmpresa(
    emp_cnpj VARCHAR(14) NOT NULL PRIMARY KEY,
    emp_nome VARCHAR(45) NOT NULL,
    emp_end_id INT NOT NULL,
    emp_tel_id INT NOT NULL,
    emp_ativo BOOLEAN DEFAULT TRUE NOT NULL,
    FOREIGN KEY (emp_end_id) REFERENCES tb_endereco (end_id),
    FOREIGN KEY (emp_tel_id) REFERENCES tb_telefone (tel_id)
);

CREATE TABLE IF NOT EXISTS tb_provento(
    provento_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    provento_descricao VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_desconto(
    desconto_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    desconto_descricao VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_holerite(
    holerite_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    holerite_periodo DATE NOT NULL,
    holerite_valor_liquido DOUBLE NOT NULL,
    holerite_fnc_id INT NOT NULL,
    holerite_infoEmpresa_emp_cnpj VARCHAR(14) NOT NULL,
    FOREIGN KEY (holerite_fnc_id) REFERENCES tb_funcionario(fnc_id),
    FOREIGN KEY (holerite_infoEmpresa_emp_cnpj) REFERENCES tb_infoEmpresa(emp_cnpj)
);

CREATE TABLE IF NOT EXISTS tb_holeriteProvento(
    holeriteProvento_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    holeriteProvento_holerite_id INT NOT NULL,
    holeriteProvento_provento_id INT NOT NULL,
    holeriteProvento_valor DOUBLE NOT NULL,
    FOREIGN KEY (holeriteProvento_holerite_id) REFERENCES tb_holerite(holerite_id),
    FOREIGN KEY (holeriteProvento_provento_id) REFERENCES tb_provento(provento_id)
);

CREATE TABLE IF NOT EXISTS tb_holeriteDesconto(
    holeriteDesconto_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    holeriteDesconto_holerite_id INT NOT NULL,
    holeriteDesconto_desconto_id INT NOT NULL,
    holeriteDesconto_valor DOUBLE NOT NULL,
    FOREIGN KEY (holeriteDesconto_holerite_id) REFERENCES tb_holerite(holerite_id),
    FOREIGN KEY (holeriteDesconto_desconto_id) REFERENCES tb_desconto(desconto_id)
);

-- Tabelas de Log e Fórmulas
CREATE TABLE IF NOT EXISTS tb_log(
    log_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    log_acao VARCHAR(100) NOT NULL,
    log_dataHora DATETIME NOT NULL,
    log_fnc_id INT NOT NULL,
    FOREIGN KEY (log_fnc_id) REFERENCES tb_funcionario(fnc_id)
);

CREATE TABLE IF NOT EXISTS tb_formula(
    formula_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    formula_dtValidade DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_formIngre(
    formIngre_formula_id INT NOT NULL,
    formIgre_produto_id INT NOT NULL,
    formIngre_qtd DOUBLE NOT NULL,
    PRIMARY KEY(formIngre_formula_id, formIgre_produto_id),
    FOREIGN KEY (formIngre_formula_id) REFERENCES tb_formula(formula_id),
    FOREIGN KEY (formIgre_produto_id) REFERENCES tb_produto(produto_id)
);
