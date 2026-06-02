# Sistema de Gerenciamento - Projeto Java

Sistema desktop em **Java** com interface **JavaFX**, desenvolvido como projeto final da disciplina de Programação Orientada a Objetos (POO). A estrutura segue o padrão **MVC**.

## 🚀 Funcionalidades

- **Gestão de Clientes:** Cadastro, atualização e consulta de clientes.
- **Gestão de Funcionários:** Dados dos colaboradores, contratações e demissões.
- **Controle de Estoque e Produtos:** Cadastro de produtos e verificação de disponibilidade.
- **Gestão Financeira (Despesas):** Registro e acompanhamento de custos operacionais.

## 🏗️ Arquitetura e Tecnologias

O projeto segue o padrão **MVC (Model-View-Controller)**:

- **Model:**
  - **VO (Value Objects):** Classes que representam as entidades do domínio.
  - **DAO (Data Access Object):** Comunicação com o banco de dados.
  - **RN (Regras de Negócio):** Validações e processamento.
- **View (`viewfx`):** Telas e layouts em JavaFX.
- **Controller (`controller`):** Captura eventos das telas e aciona a camada Model.

### Stack

- **Linguagem:** Java
- **Interface Gráfica:** JavaFX
- **Banco de Dados:** MySQL
- **Padrões:** MVC, DAO, VO

## 📁 Estrutura do Repositório

```plaintext
├── Projeto/
│   ├── lib/              # Dependências (Driver JDBC, JavaFX)
│   └── src/
│       ├── controller/   # Controladores
│       ├── model/        # Pacotes dao, vo e rn
│       ├── viewfx/       # Telas da interface
│       ├── ClienteApp.java
│       └── Main.java
└── SQL/
    ├── ddl_db_boo.sql    # Criação das tabelas
    └── dml_db_poo.sql    # Dados iniciais
```

## ⚙️ Como Executar

1. **Banco de Dados:**
   - Suba o servidor MySQL (XAMPP, Workbench, etc.).
   - Rode `SQL/ddl_db_boo.sql` para criar o schema.
   - Rode `SQL/dml_db_poo.sql` para inserir os dados de teste.

2. **Aplicação:**
   - Importe a pasta `Projeto` na sua IDE (IntelliJ, Eclipse ou VS Code).
   - Adicione as libs de `Projeto/lib` ao Build Path do projeto.
   - Abra `ConexaoDAO.java` em `src/model/dao` e confira as credenciais do banco local.

3. **Execução:**
   - Rode o `main` em `src/Main.java`.
