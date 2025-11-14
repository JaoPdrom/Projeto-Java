-- -----------------------------------------------------
-- Registros POO
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Database db_poo
-- -----------------------------------------------------
USE db_poo;

-- -----------------------------------------------------
-- Table tb_sexo
-- -----------------------------------------------------
INSERT INTO tb_sexo
  VALUES (NULL, 'Masculino'), (NULL, 'Feminino'), (NULL, 'Outros');
SELECT * FROM tb_sexo;

-- -----------------------------------------------------
-- Table tb_tipoPessoa (domínio PF/PJ)
-- -----------------------------------------------------
INSERT INTO tb_tipoPessoa (tipo_pessoa_id, codigo, descricao)
  VALUES (NULL, 'F', 'Física'), (NULL, 'J', 'Jurídica');

-- -----------------------------------------------------
-- Table tb_logradouro
-- -----------------------------------------------------
INSERT INTO tb_logradouro
  VALUES
	(NULL, 'Alameda'),				(NULL, 'Acesso'),			(NULL, 'Adro'),					(NULL, 'Aeroporto'),			(NULL, 'Alto'),
	(NULL, 'Área'),					(NULL, 'Área Especial'),	(NULL, 'Artéria'),  			(NULL, 'Atalho'),				(NULL, 'Avenida'),
    (NULL, 'Avenida Contorno'),		(NULL, 'Baixa'),	        (NULL, 'Balão'),				(NULL, 'Balneário'),			(NULL, 'Beco'),
    (NULL, 'Belvedere'),			(NULL, 'Bloco'),			(NULL, 'Bosque'),				(NULL, 'Boulevard'),			(NULL, 'Buraco'),
    (NULL, 'Cais'),					(NULL, 'Calçada'),			(NULL, 'Caminho'),				(NULL, 'Campo'),	    		(NULL, 'Canal'),
    (NULL, 'Chácara'),				(NULL, 'Chapadão'),			(NULL, 'Circular'),				(NULL, 'Complexo Viário'),		(NULL, 'Colônia'),
    (NULL, 'Condomínio'),			(NULL, 'Conjunto'),			(NULL, 'Corredor'),				(NULL, 'Córrego'),				(NULL, 'Descida'),
    (NULL, 'Desvio'),			    (NULL, 'Distrito'),			(NULL, 'Elevada'),				(NULL, 'Entrada Particular'),	(NULL, 'Escada'),
    (NULL, 'Entre Quadra'),			(NULL, 'Esplanada'),        (NULL, 'Estação'),				(NULL, 'Estacionamento'),		(NULL, 'Estádio'),
    (NULL, 'Estância'),				(NULL, 'Estrada'),			(NULL, 'Estrada Municipal'),	(NULL, 'Favela'),				(NULL, 'Fazenda'),
    (NULL, 'Feira'),				(NULL, 'Ferrovia'),			(NULL, 'Fonte'),				(NULL, 'Forte'),		        (NULL, 'Galeria'),
    (NULL, 'Granja'),				(NULL, 'Habitacional'),		(NULL, 'Ilha'),					(NULL, 'Jardim'),				(NULL, 'Jardinete'),
    (NULL, 'Ladeira'),				(NULL, 'Lago'),				(NULL, 'Lagoa'),				(NULL, 'Largo'),				(NULL, 'Loteamento'),
    (NULL, 'Marina'),		        (NULL, 'Módulo'),			(NULL, 'Monte'),				(NULL, 'Morro'),				(NULL, 'Núcleo'),
    (NULL, 'Parada'),				(NULL, 'Paradouro'),	    (NULL, 'Paralela'),				(NULL, 'Parque'),				(NULL, 'Passagem'),
    (NULL, 'Passagem Subterrânea'),	(NULL, 'Passarela'),		(NULL, 'Passeio'),				(NULL, 'Pátio'),				(NULL, 'Ponta'),
    (NULL, 'Ponte'),				(NULL, 'Porto'),			(NULL, 'Praça'),				(NULL, 'Praça de Esportes'),	(NULL, 'Praia'),
    (NULL, 'Prolongamento'),		(NULL, 'Quadra'),			(NULL, 'Quinta'),				(NULL, 'Quintas'),				(NULL, 'Ramal'),
    (NULL, 'Rampa'),				(NULL, 'Recanto'),			(NULL, 'Residencial'),			(NULL, 'Reta'),					(NULL, 'Retiro'),
    (NULL, 'Retorno'),				(NULL, 'Rodo Anel'),		(NULL, 'Rodovia'),				(NULL, 'Rotatória'),			(NULL, 'Rótula'),
    (NULL, 'Rua'),					(NULL, 'Rua de Ligação'),	(NULL, 'Rua de Pedestre'),		(NULL, 'Servidão'),				(NULL, 'Setor'),
    (NULL, 'Sítio'),				(NULL, 'Subida'),			(NULL, 'Terminal'),				(NULL, 'Travessa Particular'),	(NULL, 'Travessa'),
    (NULL, 'Trecho'),				(NULL, 'Trevo'),			(NULL, 'Trincheira'),			(NULL, 'Túnel'),				(NULL, 'Unidade'),
    (NULL, 'Vala'),					(NULL, 'Vale'),				(NULL, 'Variante'),				(NULL, 'Vereda'),				(NULL, 'Via'),
    (NULL, 'Via de Acesso'),		(NULL, 'Via de Pedestre'),	(NULL, 'Via Elevado'),			(NULL, 'Via Expressa'),			(NULL, 'Viaduto'),
    (NULL, 'Viela'),				(NULL, 'Vila'),				(NULL, 'Zigue-Zague');
SELECT * FROM tb_logradouro;

-- -----------------------------------------------------
-- Table tb_bairro
-- -----------------------------------------------------
INSERT INTO tb_bairro
	 VALUES
		(NULL, 'Alto Alegre'),			(NULL, 'Brasília'),				(NULL, 'Brasmadeira'),			(NULL, 'Canadá'),
		(NULL, 'Santos Dumont'),		(NULL, 'Cascavel Velho'),		(NULL, 'Cataratas'),			(NULL, 'Coqueiral'),
		(NULL, 'Country'),				(NULL, 'Recanto Tropical'),     (NULL, 'Floresta'),				(NULL, 'Interlagos'),
		(NULL, 'Maria Luiza'),			(NULL, 'Neva'),					(NULL, 'Parque São Paulo'),     (NULL, 'Parque Verde'),
		(NULL, 'Periolo'),				(NULL, 'São Cristóvão'),		(NULL, 'Esmeralda'),			(NULL, 'Região do Lago'),
        (NULL, 'Santa Cruz'),			(NULL, 'Santa Felicidade'),		(NULL, 'Santo Onofre'),			(NULL, 'Cancelli'),
		(NULL, 'Universitário'),		(NULL, 'XIV De Novembro'),		(NULL, 'Centro'),				(NULL, 'Pioneiros Catarinenses'),
		(NULL, 'Fundinho'),				(NULL, 'Martins'),				(NULL, 'Osvaldo Rezende'),		(NULL, 'Nossa Senhora Aparecida'),
		(NULL, 'Bom Jesus'),			(NULL, 'Brasil'),				(NULL, 'Lídice'),				(NULL, 'Presidente Roosevelt'),
		(NULL, 'Daniel Fonseca'),		(NULL, 'Tabajaras'),			(NULL, 'Cazeca'),				(NULL, 'Jardim Brasília'),
        (NULL, 'São José'),				(NULL, 'Marta Helena'),			(NULL, 'Pacaembu'),				(NULL, 'Santa Rosa'),
		(NULL, 'Residencial Gramado'),  (NULL, 'Minas Gerais'),			(NULL, 'Maravilha'),			(NULL, 'Chácaras Panorama'),
		(NULL, 'Planalto'), 			(NULL, 'Chácaras Tubalina'),	(NULL, 'Jaraguá'),				(NULL, 'Nossa Senhora das Graças'),
		(NULL, 'Luizote de Freitas'),	(NULL, 'Mansour'),				(NULL, 'Jardim Patrícia'),		(NULL, 'Distrito Industrial Norte'),
		(NULL, 'Jardim Holanda'),		(NULL, 'Jardim Europa'),		(NULL, 'Jardim Canaã'),			(NULL, 'Jardim das Palmeiras'),
        (NULL, 'Dona Zulmira'),			(NULL, 'Taiaman'),				(NULL, 'Guarani'),				(NULL, 'Tocantins'),
		(NULL, 'Morada Nova'),	        (NULL, 'Morada do Sol'),		(NULL, 'Monte Hebron'),			(NULL, 'Residencial Pequis'),
		(NULL, 'Tubalina'),				(NULL, 'Cidade Jardim'),        (NULL, 'Nova Uberlândia'),		(NULL, 'Patrimônio'),
		(NULL, 'Morada da Colina'),		(NULL, 'Saraiva'),				(NULL, 'Vigilato Pereira'),     (NULL, 'Lagoinha'),
		(NULL, 'Carajás'),				(NULL, 'Jardim Karaíba'),		(NULL, 'Pampulha'),				(NULL, 'Jardim Inconfidência'),
		(NULL, 'Santa Luzia'),			(NULL, 'Granada'),				(NULL, 'São Jorge'),			(NULL, 'Laranjeiras'),
		(NULL, 'Shopping Park'),        (NULL, 'Jardim Sul'),			(NULL, 'Gávea'),				(NULL, 'Seringueiras'),
		(NULL, 'Tibery'),				(NULL, 'Santa Mônica'),	        (NULL, 'Segismundo Pereira'),	(NULL, 'Umuarama'),
		(NULL, 'Alto Umuarama'),		(NULL, 'Aclimação'),			(NULL, 'Custódio Pereira'),     (NULL, 'Mansões Aeroporto'),
		(NULL, 'Alvorada'),				(NULL, 'Novo Mundo'),			(NULL, 'Morumbi'),				(NULL, 'Nova Alvorada'),
        (NULL, 'Morada dos Pássaros'),	(NULL, 'Jardim Ipanema'),		(NULL, 'Portal do Vale'),		(NULL, 'Grand Ville'),
		(NULL, 'Granja Marileusa'),		(NULL, 'Baixada Amarela'),		(NULL, 'Vila Rica'),			(NULL, 'Residencial Integração'),
		(NULL, 'São Luiz'),				(NULL, 'Cidade Alta'),	        (NULL, 'Marinas'),				(NULL, 'Padre Martinho'),
		(NULL, 'Jardim Ipê'),			(NULL, 'Tiradentes'),			(NULL, 'Jardim Acácia'),        (NULL, 'Jardim Floresta'),
		(NULL, 'Terra das Águas'),		(NULL, 'Cristo Rei'),			(NULL, 'Cidade Nova'),			(NULL, 'Progresso'),
        (NULL, 'Cesar Augusto'),		(NULL, 'Novo Progresso'),		(NULL, 'Tieme'),				(NULL, 'Bela Vista'),
		(NULL, 'Sol Nascente'),	        (NULL, 'Central'),				(NULL, 'Santa Cecília'),		(NULL, 'Bach'),
		(NULL, 'Aliança'),				(NULL, 'Nova Petropolis');
SELECT * FROM tb_bairro;

-- -----------------------------------------------------
-- Table tb_cidade
-- -----------------------------------------------------
INSERT INTO tb_cidade
	VALUES
		(NULL, 'São Paulo'),		(NULL, 'Teresina'),		(NULL, 'Salvador'),			(NULL, 'Brasília'),		(NULL, 'Fortaleza'),
		(NULL, 'Belo Horizonte'),	(NULL, 'Manaus'),		(NULL, 'Curitiba'),			(NULL, 'Recife'),		(NULL, 'Porto Alegre'),
        (NULL, 'Belém'),			(NULL, 'Goiânia'),		(NULL, 'Guarulhos'),		(NULL, 'Campinas'),		(NULL, 'Jaboatão dos Guararapes'),
        (NULL, 'São Gonçalo'),		(NULL, 'Maceió'),		(NULL, 'Duque de Caxias'),	(NULL, 'Natal'),		(NULL, 'Rio de Janeiro'),
        (NULL, 'Campo Grande'),		(NULL, 'Nova Iguaçu'),	(NULL, 'João Pessoa'),		(NULL, 'Santo André'),	(NULL, 'São Bernardo do Campo'),
        (NULL, 'Osasco'),			(NULL, 'São Luís'),		(NULL, 'Ribeirão Preto'),	(NULL, 'Uberlândia'),	(NULL, 'São José dos Campos'),
        (NULL, 'Contagem'),			(NULL, 'Sorocaba'),		(NULL, 'Aracaju'),			(NULL, 'Cuiabá'),		(NULL, 'Feira de Santana'),
        (NULL, 'Joinville'),		(NULL, 'Juiz de Fora'),	(NULL, 'Londrina'),			(NULL, 'Niterói'),		(NULL, 'Aparecida de Goiânia'),
        (NULL, 'Ananindeua'),		(NULL, 'Porto Velho'),	(NULL, 'Belford Roxo'),		(NULL, 'Serra'),		(NULL, 'Campos dos Goytacazes'),
        (NULL, 'Caxias do Sul'),	(NULL, 'Vila Velha'),	(NULL, 'Florianópolis'),	(NULL, 'Mauá'),			(NULL, 'São João de Meriti'),
        (NULL, 'Macapá'),			(NULL, 'Santos'),		(NULL, 'Mogi das Cruzes'),	(NULL, 'Diadema'),		(NULL, 'São José do Rio Preto'),
        (NULL, 'Campina Grande'),	(NULL, 'Betim'),		(NULL, 'Jundiaí'),			(NULL, 'Olinda'),		(NULL, 'Carapicuíba'),
        (NULL, 'Montes Claros'),	(NULL, 'Maringá'),		(NULL, 'Piracicaba'),		(NULL, 'Cariacica'),	(NULL, 'Juazeiro do Norte'),
        (NULL, 'Anápolis'),			(NULL, 'Rio Branco'),	(NULL, 'São Vicente'),		(NULL, 'Vitória'),		(NULL, 'Caucaia'),
        (NULL, 'Itaquaquecetuba'),	(NULL, 'Pelotas'),		(NULL, 'Canoas'),			(NULL, 'Caruaru'),		(NULL, 'Vitória da Conquista'),
        (NULL, 'Franca'),			(NULL, 'Ponta Grossa'),	(NULL, 'Blumenau'),			(NULL, 'Petrolina'),	(NULL, 'Ribeirão das Neves'),
        (NULL, 'Paulista'),			(NULL, 'Uberaba'),		(NULL, 'Boa Vista'),		(NULL, 'Guarujá'),		(NULL, 'São José dos Pinhais'),
        (NULL, 'Petrópolis'),		(NULL, 'Taubaté'),		(NULL, 'Limeira'),			(NULL, 'Santarém'),		(NULL, 'Praia Grande'),
        (NULL, 'Cascavel'),			(NULL, 'Mossoró'),		(NULL, 'Suzano'),			(NULL, 'Camaçari'),		(NULL, 'Governador Valadares'),
        (NULL, 'Santa Maria'),		(NULL, 'Gravataí'),		(NULL, 'Taboão da Serra'),	(NULL, 'Sumaré'),		(NULL, 'Várzea Grande'),
        (NULL, 'Volta Redonda'),	(NULL, 'Bauru'),		(NULL, 'Foz do Iguaçu'),	(NULL, 'Palmas'),		(NULL, 'Barueri'),
        (NULL, 'Embu'),				(NULL, 'Ipatinga'),		(NULL, 'Marabá'),			(NULL, 'Imperatriz'),	(NULL, 'Nossa Senhora do Socorro'),
        (NULL, 'Novo Hamburgo'),	(NULL, 'São Carlos'),	(NULL, 'Magé'),				(NULL, 'Parnamirim'),	(NULL, 'Francisco Morato'),
        (NULL, 'Arapiraca'),		(NULL, 'Sete Lagoas'),	(NULL, 'Colombo'),			(NULL, 'Divinópolis'),	(NULL, 'São Leopoldo'),
        (NULL, 'Itaboraí'),			(NULL, 'São José'),		(NULL, 'Americana'),		(NULL, 'Macaé'),		(NULL, 'Ferraz de Vasconcelos'),
        (NULL, 'Indaiatuba'),		(NULL, 'Araraquara'),	(NULL, 'Cotia'),			(NULL, 'Itabuna'),		(NULL, 'Presidente Prudente'),
        (NULL, 'Maracanaú'),		(NULL, 'Itapevi'),		(NULL, 'Juazeiro'),			(NULL, 'Santa Luzia'),	(NULL, 'Hortolândia'),
        (NULL, 'Rondonópolis'),		(NULL, 'Dourados'),		(NULL, 'Rio Grande'),		(NULL, 'Alvorada'),		(NULL, 'Cachoeiro de Itapemirim'),
        (NULL, 'Criciúma'),			(NULL, 'Cabo Frio'),	(NULL, 'Chapecó'),			(NULL, 'Itajaí'),		(NULL, 'Lauro de Freitas'),
        (NULL, 'Rio Verde'),		(NULL, 'Rio Claro'),	(NULL, 'Passo Fundo'),		(NULL, 'Araçatuba'),	(NULL, 'Cabo de Santo Agostinho'),
        (NULL, 'Luziânia'),			(NULL, 'Ilhéus'),		(NULL, 'Angra dos Reis'),	(NULL, 'Viamão'),		(NULL, 'Santa Bárbara d’Oeste'),
        (NULL, 'Castanhal'),		(NULL, 'Sobral'),		(NULL, 'Barra Mansa'),		(NULL, 'Jacareí'),		(NULL, 'Águas Lindas de Goiás'),
        (NULL, 'Parauapebas'),		(NULL, 'Guarapuava'),	(NULL, 'Nova Friburgo'),	(NULL, 'Mesquita'),		(NULL, 'São José de Ribamar'),
        (NULL, 'Ibirité'),			(NULL, 'Teresópolis'),	(NULL, 'Araguaína'),		(NULL, 'Itu'),			(NULL, 'Itapecerica da Serra'),
        (NULL, 'Marília'),			(NULL, 'Timon'),		(NULL, 'Poços de Caldas'),	(NULL, 'Jequié'),		(NULL, 'São Caetano do Sul'),
        (NULL, 'Lages'),			(NULL, 'Nilópolis'),	(NULL, 'Pindamonhangaba'),	(NULL, 'Linhares'),		(NULL, 'Bragança Paulista'),
        (NULL, 'Jaraguá do Sul'),	(NULL, 'Caxias'),		(NULL, 'Itapetininga'),		(NULL, 'Alagoinhas'),	(NULL, 'Teixeira de Freitas'),
        (NULL, 'Camaragibe'),		(NULL, 'Barreiras'),	(NULL, 'Patos de Minas'),	(NULL, 'Parnaíba'),		(NULL, 'Paranaguá'),
        (NULL, 'Abaetetuba'),		(NULL, 'Palhoça'),		(NULL, 'Mogi Guaçu'),		(NULL, 'Toledo'),		(NULL, 'Valparaíso de Goiás'),
        (NULL, 'Queimados'),		(NULL, 'Porto Seguro'),	(NULL, 'Pouso Alegre'),		(NULL, 'Jaú'),			(NULL, 'Teófilo Otoni'),
        (NULL, 'Maricá'),			(NULL, 'Botucatu'),		(NULL, 'Sapucaia do Sul'),	(NULL, 'Garanhuns'),	(NULL, 'Atibaia'),
        (NULL, 'Barbacena'),		(NULL, 'Sabará'),		(NULL, 'VarginhaM'),		(NULL, 'Simões Filho'),	(NULL, 'Vitória de Santo Antão'),
        (NULL, 'Uruguaiana'),		(NULL, 'Araucária'),	(NULL, 'Franco da Rocha'),	(NULL, 'Apucarana'),	(NULL, 'Ji-Paraná'),
        (NULL, 'Cametá'),			(NULL, 'Crato'),		(NULL, 'Araras'),			(NULL, 'Santa Rita'),	(NULL, 'Conselheiro Lafaiete'),
        (NULL, 'Resende'),			(NULL, 'Pinhais'),		(NULL, 'Cachoeirinha'),		(NULL, 'Sinop'),		(NULL, 'Santa Cruz do Sul'),
        (NULL, 'Itapipoca'),		(NULL, 'Bagé'),			(NULL, 'Rio das Ostras'),	(NULL, 'Cubatão'),		(NULL, 'Santana de Parnaíba'),
        (NULL, 'Campo Largo'),		(NULL, 'São Mateus'),	(NULL, 'Colatina'),			(NULL, 'Maranguape'),	(NULL, 'Balneário Camboriú'),
        (NULL, 'Codó'),				(NULL, 'Araruama'),		(NULL, 'Ribeirão Pires'),	(NULL, 'Bragança'),		(NULL, 'Catanduva'),
        (NULL, 'Barretos'),			(NULL, 'Marituba'),		(NULL, 'Guaratinguetá'),	(NULL, 'Sertãozinho'),	(NULL, 'Paulo Afonso'),
        (NULL, 'Brusque'),			(NULL, 'Valinhos'),		(NULL, 'Guarapari'),		(NULL, 'Jandira'),		(NULL, 'Birigui'),
        (NULL, 'Itabira'),			(NULL, 'Votorantim'),	(NULL, 'Itaguaí'),			(NULL, 'Araguari'),		(NULL, 'Vespasiano'),
        (NULL, 'Tatuí'),			(NULL, 'Trindade'),		(NULL, 'Várzea Paulista'),	(NULL, 'Passos'),		(NULL, 'Arapongas'),
        (NULL, 'Salto'),			(NULL, 'Poá'),			(NULL, 'Paço do Lumiar'),	(NULL, 'Assis'),		(NULL, 'Eunápolis'),
        (NULL, 'Barcarena'),		(NULL, 'Itatiba'),		(NULL, 'Caraguatatuba'),	(NULL, 'Três Lagoas'),	(NULL, 'Almirante Tamandaré'),
        (NULL, 'Igarassu'),			(NULL, 'Parintins'),	(NULL, 'Santana'),			(NULL, 'Ourinhos'),		(NULL, 'Coronel Fabriciano'),
        (NULL, 'Ubá'),				(NULL, 'Formosa'), 		(NULL, 'Açailândia'),		(NULL, 'Corumbá'),		(NULL, 'São Lourenço da Mata'),
        (NULL, 'Umuarama'),			(NULL, 'Muriaé'),		(NULL, 'Altamira'),			(NULL, 'Patos'),		(NULL, 'São Félix do Xingu'),
        (NULL, 'Paragominas'),		(NULL, 'Tucuruí'),		(NULL, 'Novo Gama'),		(NULL, 'Bayeux'),		(NULL, 'Cambé'),
        (NULL, 'Ituiutaba'),		(NULL, 'Bacabal'),		(NULL, 'Tubarão'),			(NULL, 'Ariquemes'),	(NULL, 'Erechim'),
        (NULL, 'Piraquara'),		(NULL, 'Lagarto'),		(NULL, 'Bento Gonçalves'),	(NULL, 'Iguatu'),		(NULL, 'Araxá'),
        (NULL, 'Santa Helena'), 	(NULL, 'Missal'),		(NULL, 'Diamantina'),		(NULL, 'Sacramento'),	(NULL, 'Ouro Preto');
SELECT * FROM tb_cidade;

-- -----------------------------------------------------
-- Table tb_estado
-- -----------------------------------------------------
INSERT INTO tb_estado
	 VALUES
		('AC', 'Acre'),					('AL', 'Alagoas'),		('AP', 'Amapá'),				('AM', 'Amazonas'),
		('BA', 'Bahia'),				('CE', 'Ceará'),		('DF', 'Distrito Federal'),		('ES', 'Espírito Santo'),
		('GO', 'Goiás'),				('MA', 'Maranhão'),		('MT', 'Mato Grosso'),			('MS', 'Mato Grosso do Sul'),
		('MG', 'Minas Gerais'),			('PA', 'Pará'),			('PB', 'Paraíba'),				('PR', 'Paraná'),
		('PE', 'Pernambuco'),			('PI', 'Piauí'),	    ('RJ', 'Rio de Janeiro'),		('RN', 'Rio Grande do Norte'),
		('RS', 'Rio Grande do Sul'),	('RO', 'Rondônia'),		('RR', 'Roraima'),				('SC', 'Santa Catarina'),
        ('SP', 'São Paulo'),			('SE', 'Sergipe'),		('TO', 'Tocantins');
SELECT * FROM tb_estado;

-- -----------------------------------------------------
-- Table tb_cidEstPai
-- -----------------------------------------------------
INSERT INTO tb_cidEst
     VALUES (1, 'SP'),    (2, 'PI'),      (3, 'BA'),      (4, 'DF'),      (5, 'CE'),      (6, 'MG'),
          (7, 'AM'),    (8, 'PR'),      (9, 'PE'),      (10, 'RS'),     (11, 'PA'),     (12, 'GO'),
            (13, 'SP'),    (14, 'SP'),     (15, 'PE'),     (16, 'RJ'),     (17, 'AL'),     (18, 'RJ'),
            (19, 'RN'),    (20, 'RJ'),     (21, 'MS'),     (22, 'RJ'),     (23, 'PB'),     (24, 'SP'),
            (25, 'SP'),    (26, 'SP'),     (27, 'MA'),     (28, 'SP'),     (29, 'MG'),     (30, 'SP'),
            (31, 'MG'),    (32, 'SP'),     (33, 'SE'),     (34, 'MT'),     (35, 'BA'),     (36, 'SC'),
            (37, 'MG'),    (38, 'PR'),     (39, 'RJ'),     (40, 'GO'),     (41, 'PA'),     (42, 'RO'),
            (43, 'RJ'),    (44, 'ES'),     (45, 'RJ'),     (46, 'RS'),     (47, 'ES'),     (48, 'SC'),
            (49, 'SP'),    (50, 'RJ'),     (51, 'AP'),     (52, 'SP'),     (53, 'SP'),     (54, 'SP'),
            (55, 'SP'),    (56, 'PB'),     (57, 'MG'),     (58, 'SP'),     (59, 'PE'),     (60, 'SP'),
            (61, 'MG'),    (62, 'PR'),     (63, 'SP'),     (64, 'ES'),     (65, 'CE'),     (66, 'GO'),
            (67, 'AC'),    (68, 'SP'),     (69, 'ES'),     (70, 'CE'),     (71, 'SP'),     (72, 'RS'),
            (73, 'RS'),    (74, 'PE'),     (75, 'BA'),     (76, 'SP'),     (77, 'PR'),     (78, 'SC'),
            (79, 'PE'),    (80, 'MG'),     (81, 'PE'),     (82, 'MG'),     (83, 'RR'),     (84, 'SP'),
            (85, 'PR'),      (86, 'RJ'),     (87, 'SP'),     (88, 'SP'),     (89, 'PA'),     (90, 'SP'),
            (91, 'PR'),      (92, 'RN'),     (93, 'SP'),     (94, 'BA'),     (95, 'MG'),     (96, 'RS'),
            (97, 'RS'),      (98, 'SP'),     (99, 'SP'),     (100, 'MT'),    (101, 'RJ'),    (102, 'SP'),
            (103, 'PR'),     (104, 'TO'),    (105, 'SP'),    (106, 'SP'),    (107, 'MG'),    (108, 'PA'),
            (109, 'MA'),     (110, 'SE'),    (111, 'RS'),    (112, 'SP'),    (113, 'RJ'),    (114, 'RN'),
            (115, 'SP'),     (116, 'AL'),    (117, 'MG'),    (118, 'PR'),    (119, 'MG'),    (120, 'RS'),
            (121, 'RJ'),     (122, 'SC'),    (123, 'SP'),    (124, 'RJ'),    (125, 'SP'),    (126, 'SP'),
            (127, 'SP'),     (128, 'SP'),    (129, 'BA'),    (130, 'SP'),    (131, 'CE'),    (132, 'SP'),
            (133, 'BA'),     (134, 'MG'),    (135, 'SP'),    (136, 'MT'),    (137, 'MS'),    (138, 'RS'),
            (139, 'RS'),     (140, 'ES'),    (141, 'SC'),    (142, 'RJ'),    (143, 'SC'),    (144, 'SC'),
            (145, 'BA'),     (146, 'GO'),    (147, 'SP'),    (148, 'RS'),    (149, 'SP'),    (150, 'PE'),
            (151, 'GO'),     (152, 'BA'),    (153, 'RJ'),    (154, 'RS'),    (155, 'SP'),    (156, 'PA'),
            (157, 'CE'),     (158, 'RJ'),    (159, 'SP'),    (160, 'GO'),    (161, 'PA'),    (162, 'PR'),
            (163, 'RJ'),     (164, 'RJ'),    (165, 'MA'),    (166, 'MG'),    (167, 'RJ'),    (168, 'TO'),
            (169, 'SP'),     (170, 'SP'),    (171, 'SP'),    (172, 'MA'),    (173, 'MG'),    (174, 'BA'),
            (175, 'SP'),     (176, 'SC'),    (177, 'RJ'),    (178, 'SP'),    (179, 'ES'),    (180, 'SP'),
            (181, 'SC'),     (182, 'MA'),    (183, 'SP'),    (184, 'BA'),    (185, 'BA'),    (186, 'PE'),
            (187, 'BA'),     (188, 'MG'),    (189, 'PI'),    (190, 'PR'),    (191, 'PR'),    (192, 'SC'),
            (193, 'SP'),     (194, 'PR'),    (195, 'GO'),    (196, 'RJ'),    (197, 'BA'),    (198, 'MG'),
            (199, 'SP'),     (200, 'MG'),    (201, 'RJ'),    (202, 'SP'),    (203, 'RS'),    (204, 'PE'),
            (205, 'SP'),     (206, 'MG'),    (207, 'MG'),    (208, 'MG'),    (209, 'BA'),    (210, 'PE'),
            (211, 'RS'),     (212, 'PR'),    (213, 'SP'),    (214, 'PR'),    (215, 'RO'),    (216, 'PA'),
            (217, 'CE'),     (218, 'SP'),    (219, 'PB'),    (220, 'MG'),    (221, 'RJ'),    (222, 'PR'),
            (223, 'RS'),     (224, 'MT'),    (225, 'RS'),    (226, 'CE'),    (227, 'RS'),    (228, 'RJ'),
            (229, 'SP'),     (230, 'SP'),    (231, 'PR'),    (232, 'ES'),    (233, 'ES'),    (234, 'CE'),
            (235, 'SC'),     (236, 'MA'),    (237, 'RJ'),    (238, 'SP'),    (239, 'PA'),    (240, 'SP'),
            (241, 'SP'),     (242, 'PA'),    (243, 'SP'),    (244, 'SP'),    (245, 'BA'),    (246, 'SC'),
            (247, 'SP'),     (248, 'ES'),    (249, 'SP'),    (250, 'SP'),    (251, 'MG'),    (252, 'SP'),
            (253, 'RJ'),     (254, 'MG'),    (255, 'MG'),    (256, 'SP'),    (257, 'GO'),    (258, 'SP'),
            (259, 'MG'),     (260, 'PR'),    (261, 'SP'),    (262, 'SP'),    (263, 'MA'),    (264, 'SP'),
            (265, 'BA'),     (266, 'PA'),    (267, 'SP'),    (268, 'SP'),    (269, 'MS'),    (270, 'PR'),
            (271, 'PE'),     (272, 'AM'),    (273, 'AP'),    (274, 'SP'),    (275, 'MG'),    (276, 'MG'),
            (277, 'GO'),     (278, 'MA'),    (279, 'MS'),    (280, 'PE'),    (281, 'PR'),    (282, 'MG'),
            (283, 'PA'),     (284, 'PB'),    (285, 'PA'),    (286, 'PA'),    (287, 'PA'),    (288, 'GO'),
            (289, 'PB'),     (290, 'PR'),    (291, 'MG'),    (292, 'MA'),    (293, 'SC'),    (294, 'RO'),
            (295, 'RS'),     (296, 'PR'),    (297, 'SE'),    (298, 'RS'),    (299, 'CE'),    (300, 'MG'),
            (301, 'PR'),     (302, 'PR'),    (303, 'PR'),    (304, 'MG'),    (305, 'MG');
SELECT * FROM tb_cidEst;

-- -----------------------------------------------------
-- Table tb_endPostal
-- -----------------------------------------------------
INSERT INTO tb_endPostal (endP_id, endP_logradouro_id, endP_nomeRua, endP_bairro_id, endP_cep, endP_cid_id, endP_est_sigla)
	  VALUES (NULL, 101, 'SALVADOR', 32, '38400111', 29, 'MG'),
			 (NULL, 10, 'BRASIL', 27, '85892000', 301, 'PR'),
             (NULL, 101, 'DR. ALBERTO DE OLIVEIRA LIMA', 99, '05650002', 1, 'SP'),
             (NULL, 83, 'Academia', 27,	'20020030', 20,'RJ'),
             (NULL, 101, 'das Andorinhas', 130, '87010080', 62, 'PR'),
             (NULL, 110, 'das Flores', 130, '15010080', 44, 'ES'),
             (NULL, 88, 'da Boa Vista', 66, '95052080', 9, 'PE'),
             (NULL, 58, 'PARAÍSO', 14, '11082000', 5, 'CE'),
             (NULL, 37, 'MARTINS', 39, '44487100', 66, 'GO'),
             (NULL, 24, 'ROSAL', 128, '45082090', 21, 'MS'),
             (NULL, 101, 'BAHIA', 18, '12548635', 8, 'PR'),
             (NULL, 101, 'MORRINHOS', 40, '56842136', 29, 'MG'),
             (NULL, 101, 'MARIANO CAMILO PAGANOTTO', 130, '78542698', 50, 'RJ'),
             (NULL, 101, 'PARANA', 58, '12403598', 82, 'MG'),
             (NULL, 101, 'CAMORIM', 9, '14587362', 118, 'PR' ),
             (NULL, 101, 'PARAGUAI', 88, '12032068', 199, 'SP'),
             (NULL, 10, 'BRASILIA', 101, '15784256', 236, 'MA'),
             (NULL, 10, 'GRAMADO', 21, '45879625', 280, 'PE');
select * FROM tb_endPostal;

-- -----------------------------------------------------
-- Table tb_endereco
-- -----------------------------------------------------
INSERT INTO tb_endereco
	 VALUES (NULL, 1, 121, 'CASA'),		(NULL, 1, 558, 'Apto 15'),		(NULL, 1, 54, 'Apto 02'),		(NULL, 1, 447, 'CASA'),		(NULL, 1, 987, 'CASA 01'),
			(NULL, 2, 825, 'CASA'),		(NULL, 2, 856, 'Apto 10'),		(NULL, 2, 65, 'Apto 04'),		(NULL, 2, 456, 'CASA'),		(NULL, 2, 298, 'CASA 02'),
			(NULL, 3, 325, 'CASA'),		(NULL, 3, 54, 'Apto 11'),		(NULL, 3, 312, 'Apto 12'),		(NULL, 3, 456, 'CASA'),		(NULL, 3, 485, 'CASA 03'),
			(NULL, 4, 221, 'CASA'),		(NULL, 4, 14, 'Apto 09'),		(NULL, 4, 456, 'Apto 22'),		(NULL, 4, 544, 'CASA'),		(NULL, 4, 963, 'CASA 04'),
			(NULL, 5, 552, 'CASA'),		(NULL, 5, 3654, 'Apto 01'),		(NULL, 5, 5364, 'Apto 25'),		(NULL, 5, 454, 'CASA'),		(NULL, 5, 955, 'CASA 05'),
			(NULL, 6, 8887, 'CASA'),	(NULL, 6, 3425, 'Apto 88'),		(NULL, 6, 46, 'Apto 48'),		(NULL, 6, 4232, 'CASA'),	(NULL, 6, 447, 'CASA 06'),
			(NULL, 7, 554, 'CASA'),		(NULL, 7, 1559, 'Apto 14'),		(NULL, 7, 6463, 'Apto 10'),		(NULL, 7, 454, 'CASA'),		(NULL, 7, 821, 'CASA 01'),
			(NULL, 8, 2210, 'CASA'),	(NULL, 8, 185, 'Apto 52'),		(NULL, 8, 345, 'Apto 08'),		(NULL, 8, 312, 'CASA'),		(NULL, 8, 778, 'CASA 02'),
			(NULL, 9, 586, 'CASA'),		(NULL, 9, 921, 'Apto 20'),		(NULL, 9, 36, 'Apto 07'),		(NULL, 9, 889, 'CASA'),		(NULL, 9, 8951, 'CASA 08'),
			(NULL, 10, 778, 'CASA'),	(NULL, 10, 546, 'Apto 19'),		(NULL, 10, 654, 'Apto 31'),		(NULL, 10, 667, 'CASA'),	(NULL, 10, 925, 'CASA 09'),
			(NULL, 11, 457, 'CASA'),	(NULL, 11, 921, 'Apto 20'),		(NULL, 11, 36, 'Apto 207'),		(NULL, 11, 889, 'CASA'),	(NULL, 11, 89, 'CASA 10'),
			(NULL, 12, 997, 'CASA'),	(NULL, 12, 865, 'Apto 102'),	(NULL, 12, 45, 'Apto 201'),		(NULL, 12, 65667, 'CASA'),	(NULL, 12, 97, 'CASA 11'),
			(NULL, 13, 365, 'CASA'),	(NULL, 13, 45, 'Apto 101'),		(NULL, 13, 874, 'Apto 860'),	(NULL, 13, 858, 'CASA'),	(NULL, 13, 87, 'CASA 08'),
			(NULL, 14, 784, 'CASA'),	(NULL, 14, 657, 'Apto 05'),		(NULL, 14, 857, 'Apto 1254'),	(NULL, 14, 88, 'CASA'),		(NULL, 14, 575, 'CASA 10'),
			(NULL, 15, 454, 'CASA'),	(NULL, 15, 68, 'Apto 07'),		(NULL, 15, 75, 'Apto 85'),		(NULL, 15, 58, 'CASA'),		(NULL, 15, 57, 'CASA 02'),
			(NULL, 16, 584, 'CASA'),	(NULL, 16, 42, 'Apto 100'),		(NULL, 16, 4254, 'Apto 104'),	(NULL, 16, 24, 'CASA'),		(NULL, 16, 8, 'CASA 01'),
			(NULL, 17, 212, 'CASA'),	(NULL, 17, 02, 'Apto 201'),		(NULL, 17, 454, 'Apto 182'),	(NULL, 17, 245, 'CASA'),	(NULL, 17, 578, 'CASA 05'),
			(NULL, 18, 783, 'CASA'),	(NULL, 18, 78, 'Apto 204'),		(NULL, 18, 685, 'Apto 107'),	(NULL, 8, 98, 'CASA'),		(NULL, 18, 324, 'CASA 03');
SELECT * FROM tb_endereco ;

-- -----------------------------------------------------
-- Table tb_pessoa
-- -----------------------------------------------------
INSERT INTO tb_pessoa (pes_documento, pes_tipo_pessoa_id, pes_nome, pes_sex_id, pes_dtNascimento, pes_email)
    VALUES
    ('00022255544', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Maria dos Santos', 2, '1982-03-25','ms@gmail.com'),
    ('11144477755', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Roberto Martes', 1, '1985-10-02','rm@gmail.com'),
    ('88899955522', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Paulo Correia', 1, '1951-06-18','pc@gmail.com'),
    ('88774411226', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Sara Melo', 2, '1983-12-25','sm@gmail.com'),
    ('88774410005', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Roberta Calais', 2, '1952-11-05','rc@gmail.com'),
    ('88877744411', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Kairo Barbosa', 1, '1941-01-15','kb@gmail.com'),
    ('55588877711', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Milene Andrade', 2, '1999-10-06','ma@gmail.com'),
    ('88800011177', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Luca Garcia', 1, '1988-03-04','lg@gmail.com'),
    ('88899666331', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Maite Lima', 2, '1998-11-14','ml@gmail.com'),
    ('00011224477', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Gael Medeiros', 1, '1945-07-29','gm@gmail.com'),
    ('58745214572', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Fernanda Braga', 2, '1991-10-07','gb@gmail.com'),
    ('88465285253', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Eduardo Botteon', 1, '1978-04-02','eb@gmail.com'),
    ('78963236962', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Guilherme Tosi', 1, '1965-11-12','gt@gmail.com'),
    ('52036586320', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Claudio Gomes', 3, '1989-09-25','cg@gmail.com'),
    ('78962512036', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Zenir Andrade', 3, '1990-10-30','za@gmail.com'),
    ('52378965222', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Joao Bazoni', 1, '1971-02-11','jb@gmail.com'),
    ('85452247865', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Ana Larsen', 2, '1966-08-10','al@gmail.com'),
    ('88651232058', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Juliente Ribeiro', 2, '1987-07-09','jr@gmail.com'),
    ('58965212578', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Daiana Martins', 2, '1970-08-03','dm@gmail.com'),
    ('52324785225', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Pedro Barbosa', 1, '1950-07-19','pb@gmail.com'),
    ('48412315641', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Talita Reis', 1, '1984-12-11','tr@gmail.com'),
    ('54761432489', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Saulo Santos', 1, '1970-10-12','ss@gmail.com'),
    ('46876117651', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Gustavo Battos', 1, '1985-05-09','gb@gmail.com'),
    ('87136102647', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Mauro Lima', 1, '1943-01-25','ml@gmail.com'),
    ('87894132132', (SELECT tipo_pessoa_id FROM tb_tipoPessoa WHERE codigo='F'), 'Paulo Nunes', 1, '1954-06-30','pn@gmail.com');
SELECT * FROM tb_pessoa;

-- -----------------------------------------------------
-- Table tb_telefone
-- -----------------------------------------------------
INSERT INTO tb_telefone (tel_id, tel_pes_documento, tel_codPais, tel_ddd, tel_numero)
	 VALUES (NULL, '00022255544', '55', 34, '8889996665'),
			(NULL, '11144477755', '55', 44, '8887774441'),
            (NULL, '11144477755', '55', 88, '9998884441'),
            (NULL, '88899955522', '55', 77, '8877441100'),
            (NULL, '88899955522', '55', 65, '9988556692'),
            (NULL, '88774411226', '55', 45, '9999878877'),
            (NULL, '88774410005', '55', 44, '8874453665'),
            (NULL, '88774410005', '55', 21, '2256556144'),
            (NULL, '88877744411', '55', 11, '5867665455'),
            (NULL, '55588877711', '55', 89, '5465536496'),
            (NULL, '88800011177', '55', 69, '6456864564'),
            (NULL, '88899666331', '55', 66, '6456864564'),
            (NULL, '88899666331', '55', 41, '6456864564'),
            (NULL, '00011224477', '55', 34, '6456864564'),
            (NULL, '58745214572', '55', 37, '6456864564'),
            (NULL, '88465285253', '55', 45, '6456864564'),
            (NULL, '88465285253', '55', 43, '6456864564'),
            (NULL, '78963236962', '55', 68, '6456864564'),
            (NULL, '52036586320', '55', 11, '6456864564'),
            (NULL, '78962512036', '55', 21, '6456864564'),
            (NULL, '78962512036', '55', 12, '6456864564'),
            (NULL, '52378965222', '55', 28, '6456864564'),
            (NULL, '85452247865', '55', 74, '6456864564'),
            (NULL, '88651232058', '55', 75, '6456864564'),
            (NULL, '58965212578', '55', 89, '6456864564'),
            (NULL, '52324785225', '55', 92, '6456864564'),
			(NULL, '48412315641', '55', 46, '6544654138'),
            (NULL, '54761432489', '55', 93, '2345847841'),
            (NULL, '46876117651', '55', 19, '7884121356'),
            (NULL, '87136102647', '55', 55, '5781321367'),
            (NULL, '87894132132', '55', 68, '5478712368');
SELECT * FROM tb_telefone;

-- -----------------------------------------------------
-- Table tb_cargo
-- -----------------------------------------------------
INSERT INTO tb_cargo
	 VALUES (1, 'Gerente', 1),
	        (2, 'Atendente', 1),
	        (3, 'RH', 1),
	        (4, 'Financeiro', 1),
	        (5, 'Motorista',1 );
SELECT * FROM tb_cargo;

-- -----------------------------------------------------
-- Table tb_funcionario
-- -----------------------------------------------------
INSERT INTO tb_funcionario (fnc_id, fnc_pes_documento, fnc_cargo_id, fnc_salario, fnc_dtContratacao, fnc_dtDemissao)
	 VALUES (NULL, '78963236962', 3, 2500, '1989-10-03', '2020-01-01'),
			(NULL, '88465285253', 2, 1800, '2008-07-12', NULL),
			(NULL, '85452247865', 1, 2200, '2016-10-08', NULL),
			(NULL, '52036586320', 1, 2200, '2008-04-05', '2015-07-19'),
			(NULL, '88774411226', 4, 4000, '2004-02-15', '2020-10-21'),
			(NULL, '52324785225', 1, 2200, '1950-07-19', '1970-02-02'),
			(NULL, '88899666331', 2, 1800, '2020-11-22', NULL),
			(NULL, '88774410005', 2, 1800, '1992-11-05', NULL),
            (NULL, '55588877711', 1, 2500, '2001-12-15', NULL),
            (NULL, '54761432489', 1, 3000, '2015-09-08', NULL),
            (NULL, '87894132132', 2, 1800, '2021-05-07', NULL),
            (NULL, '78962512036', 4, 1700, '1995-06-28', NULL);
SELECT * FROM tb_funcionario;

-- -----------------------------------------------------
-- Table tb_pesEnd
-- -----------------------------------------------------
INSERT INTO tb_pesEnd (pesEnd_pes_documento, pesEnd_end_id) VALUES
('00011224477', 1),
('00011224477', 2),
('00022255544', 6),
('00022255544', 7),
('11144477755', 11),
('46876117651', 16),
('48412315641', 21),
('52036586320', 26),
('52324785225', 31),
('52378965222', 36),
('54761432489', 41),
('55588877711', 46),
('58745214572', 51);
SELECT * FROM tb_pesEnd;

-- -----------------------------------------------------
-- Table tb_tipoPagamento
-- -----------------------------------------------------
INSERT INTO tb_tipoPagamento
	 VALUES (NULL, 'Credito'), (NULL, 'Debito'), (NULL, 'Parcelado'), (NULL, 'Pix'), (NULL, 'Dinheiro');
SELECT * FROM tb_tipoPagamento;

-- -----------------------------------------------------
-- Table tb_fornecedor
-- -----------------------------------------------------
INSERT INTO tb_fornecedor
	 VALUES ('45124874000154', 'Emp 1 Ltda', 'Emp 1', 1),
			('89744561000142', 'Emp 2 Ltda', 'Emp 2', 0),
            ('85925857000176', 'Emp 3 Ltda', 'Emp 3', 0),
            ('45489744000187', 'Emp 4 Ltda', 'Emp 4', 1),
            ('87451877000178', 'Emp 5 Ltda', 'Emp 5', 1),
            ('78132168000112', 'Emp 6 Ltda', 'Emp 6', 1),
            ('88412187000189', 'Emp 7 Ltda', 'Emp 7', 1),
            ('56457544000174', 'Emp 8 Ltda', 'Emp 8', 0);
SELECT * FROM tb_fornecedor;

-- -----------------------------------------------------
-- Table tb_statusvenda
-- -----------------------------------------------------
INSERT INTO tb_statusvenda
    VALUES (NULL, 'FINALIZADA'),
           (NULL, 'EM ANDAMENTO'),
           (NULL, 'NAO FINALIZADA');
SELECT * FROM tb_statusvenda;

-- -----------------------------------------------------
-- Table tb_statuspedido
-- -----------------------------------------------------
INSERT INTO tb_statuspedido
    VALUES (NULL, 'EM PRODUCAO'),
           (NULL, 'FINALIZADO');
SELECT * FROM tb_statuspedido;

-- -----------------------------------------------------
-- Table tb_tipoentrega
-- -----------------------------------------------------
INSERT INTO tb_tipoentrega
    VALUES (NULL, 'RETIRADA'),
           (NULL, 'DELIVERY');
SELECT * FROM tb_tipoentrega;

-- -----------------------------------------------------
-- Table tb_tipopdt
-- -----------------------------------------------------
INSERT INTO tb_tipopdt
    VALUES (NULL, 'RACAO PRONTA'),
           (NULL, 'FORMULA');
SELECT * FROM tb_tipopdt;

-- -----------------------------------------------------
-- Table tb_produto
-- -----------------------------------------------------
INSERT INTO tb_produto (produto_id, produto_nome, produto_peso, produto_ativo, produto_tipoPdt)
    VALUES (NULL, 'RACAO PREMIUM FILHOTES', 10, TRUE,
                    (SELECT tipoPdt_id FROM tb_tipopdt WHERE tipoPdt_descricao = 'RACAO PRONTA')),
           (NULL, 'RACAO MANUTENCAO ADULTOS', 20, TRUE,
                    (SELECT tipoPdt_id FROM tb_tipopdt WHERE tipoPdt_descricao = 'RACAO PRONTA')),
           (NULL, 'FORMULA ESPECIAL SENIOR', 25, TRUE,
                    (SELECT tipoPdt_id FROM tb_tipopdt WHERE tipoPdt_descricao = 'FORMULA'));
SELECT * FROM tb_produto;

-- -----------------------------------------------------
-- Table tb_estoque
-- -----------------------------------------------------
INSERT INTO tb_estoque (est_id, est_dtCompra, est_produto_id, est_custo, est_qtdToal, est_qtdMin, est_qtdMax, est_lote, est_dtValidade)
    VALUES (NULL, '2025-01-05', 1, 150.00, 50, 10, 80, 'L2025-RPF-01', '2025-12-31'),
           (NULL, '2025-02-12', 2, 220.00, 40, 8, 70, 'L2025-RMA-01', '2025-11-30'),
           (NULL, '2025-03-08', 3, 260.00, 30, 5, 60, 'L2025-FES-01', '2025-10-15');
SELECT * FROM tb_estoque;
