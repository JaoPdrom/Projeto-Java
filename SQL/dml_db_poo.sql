
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

INSERT INTO tb_cargo
	 VALUES (1, 'Gerente', 1),
	        (2, 'Atendente', 1),
	        (3, 'RH', 1),
	        (4, 'Financeiro', 1),
	        (5, 'Motorista',1 );
SELECT * FROM tb_cargo;

INSERT INTO tb_tipopdt(tipoPdt_id, tipoPdt_descricao)
    VALUES(NULL, 'RACAO PRONTA'),
          (NULL, 'FORMULA');
select * from tb_tipopdt;

-- -----------------------------------------------------
-- Table tb_tipoDespesa
-- -----------------------------------------------------
INSERT INTO tb_tipoDespesa (tipoDespesa_id, tipoDespesa_nome)
VALUES
    (NULL, 'Aluguel'),
    (NULL, 'Energia'),
    (NULL, 'Água'),
    (NULL, 'Combustível'),
    (NULL, 'Manutenção'),
    (NULL, 'Outros');
SELECT * FROM tb_tipoDespesa;

INSERT INTO tb_fase_contratacao (fase_contratacao_descricao, fase_contratacao_ativo) VALUES
    ('Entrevista', true),
    ('Aprovação', true),
    ('Contratação', true),
    ('Integração', true);
SELECT * FROM tb_fase_contratacao;