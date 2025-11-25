DROP DATABASE IF EXISTS db_poo;

CREATE DATABASE IF NOT EXISTS db_poo;

USE db_poo;

-- Tabelas de localizaÃ§Ã£o
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

-- Tabela principal de Pessoa (Ãºnica para clientes, funcionÃ¡rios, etc.)
CREATE TABLE IF NOT EXISTS tb_tipoPessoa(
    tipo_pessoa_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    codigo CHAR(1) NOT NULL UNIQUE,
    descricao VARCHAR(50) NOT NULL
);
INSERT INTO tb_tipopessoa (codigo, descricao) VALUES ('J', 'Juridica');


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

-- Tabela de relacionamento para permitir mÃºltiplos endereÃ§os por pessoa
CREATE TABLE IF NOT EXISTS tb_pesEnd(
    pesEnd_pes_documento VARCHAR(14) NOT NULL,
    pesEnd_end_id INT NOT NULL,
    PRIMARY KEY (pesEnd_pes_documento, pesEnd_end_id),
    FOREIGN KEY (pesEnd_pes_documento) REFERENCES tb_pessoa(pes_documento),
    FOREIGN KEY (pesEnd_end_id) REFERENCES tb_endereco(end_id)
);

-- Tabela de relacionamento para permitir mÃºltiplos telefones por pessoa
CREATE TABLE IF NOT EXISTS tb_telefone(
    tel_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    tel_codPais CHAR(2) NOT NULL,
    tel_ddd CHAR(2) NOT NULL,
    tel_numero VARCHAR(15) NOT NULL,
    tel_pes_documento VARCHAR(14) NOT NULL,
    FOREIGN KEY (tel_pes_documento) REFERENCES tb_pessoa (pes_documento)
);

-- Tabelas de FuncionÃ¡rios e Cargos
CREATE TABLE IF NOT EXISTS tb_cargo(
    cargo_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cargo_descricao VARCHAR(45) NOT NULL,
    cargo_ativo BOOLEAN DEFAULT TRUE NOT NULL
);
INSERT INTO tb_cargo (cargo_descricao, cargo_ativo) VALUES ('Motorista', true);


CREATE TABLE IF NOT EXISTS tb_funcionario(
    fnc_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fnc_numPis CHAR(11) NOT NULL,
    fnc_salario DOUBLE NOT NULL,
    fnc_cargo_id INT NOT NULL,
    fnc_pes_documento VARCHAR(14) NOT NULL,
    fnc_ativo BOOLEAN DEFAULT TRUE NOT NULL,
    FOREIGN KEY (fnc_cargo_id) REFERENCES tb_cargo(cargo_id),
    FOREIGN KEY (fnc_pes_documento) REFERENCES tb_pessoa(pes_documento)
);

CREATE TABLE tb_contratacao (
    contratacao_id INT AUTO_INCREMENT PRIMARY KEY,
    contratacao_fase VARCHAR(45) NOT NULL,
    contratacao_data DATE NOT NULL,
    contratacao_fnc_id INT NOT NULL,
    FOREIGN KEY (contratacao_fnc_id) REFERENCES tb_funcionario(fnc_id)
);

CREATE TABLE tb_demissao (
    demissao_id INT AUTO_INCREMENT PRIMARY KEY,
    demissao_data DATE NOT NULL,
    demissao_motivo VARCHAR(250) NOT NULL,
    demissao_fnc_id INT NOT NULL,
    FOREIGN KEY (demissao_fnc_id) REFERENCES tb_funcionario(fnc_id)
);

CREATE TABLE IF NOT EXISTS tb_tipoPdt(
    tipoPdt_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tipoPdt_descricao VARCHAR(45)
);
INSERT INTO tb_tipopdt (tipoPdt_descricao) VALUES ('racao');


CREATE TABLE IF NOT EXISTS tb_produto(
    produto_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    produto_nome VARCHAR(45) NOT NULL,
    produto_peso INT NOT NULL,
    produto_ativo BOOLEAN DEFAULT TRUE NOT NULL,
    produto_tipoPdt INT NOT NULL,
    FOREIGN KEY (produto_tipoPdt) REFERENCES tb_tipoPdt (tipoPdt_id)
);

-- Tabelas de Produtos e Estoque
CREATE TABLE IF NOT EXISTS tb_estoque(
    est_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    est_dtCompra DATE NOT NULL,
    est_produto_id INT NOT NULL,
    est_custo DOUBLE NOT NULL,
    est_qtdToal DOUBLE NOT NULL,
    est_qtdMin DOUBLE NOT NULL,
    est_qtdMax DOUBLE NOT NULL,
    est_lote VARCHAR(45) NOT NULL,
    est_dtValidade DATE NOT NULL,
    FOREIGN KEY (est_produto_id) REFERENCES tb_produto(produto_id)
);

CREATE TABLE IF NOT EXISTS tb_tipoDespesa(
    tipoDespesa_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    tipoDespesa_nome VARCHAR(45) NOT NULL
);

-- Tabela de Log de auditoria
CREATE TABLE IF NOT EXISTS tb_log(
    log_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    log_acao VARCHAR(100) NOT NULL,
    log_dataHora DATETIME NOT NULL,
    log_fnc_id INT NOT NULL,
    FOREIGN KEY (log_fnc_id) REFERENCES tb_funcionario(fnc_id)
);