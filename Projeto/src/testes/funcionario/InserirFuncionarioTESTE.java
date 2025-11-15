// /*
//  * Copyright (c) 2025.
//  * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
//  */

// package testes.funcionario;

// import model.rn.FuncionarioRN;
// import model.vo.*;

// import java.sql.SQLException;
// import java.time.LocalDate;

// public class InserirFuncionarioTESTE {
//     public static void main(String[] args) {
//         System.out.println("=== Teste da classe FuncionarioRN ===");

//         // cria o objeto VO
//         FuncionarioVO funcionario = new FuncionarioVO();

//         // primeiro cria as instancias de pessoa
//         funcionario.setPes_cpf("00022255588");
//         funcionario.setPes_nome("Joao Pedropp");

//         SexoVO sexo = new SexoVO();
//         sexo.setSex_id(1);
//         funcionario.setPes_sexo(sexo);

//         funcionario.setPes_dt_nascimento(LocalDate.of(2004, 5, 15));
//         funcionario.setPes_email("emailjp@email.com");

//         CidadeVO cidade = new CidadeVO();
//         cidade.setCid_id(1);

//         EstadoVO estado = new EstadoVO();
//         estado.setEst_sigla("SP");

//         BairroVO bairro = new BairroVO();
//         bairro.setBairro_id(1);

//         LogradouroVO logradouro = new LogradouroVO();
//         logradouro.setLogradouro_id(1);

//         EndPostalVO endPostal = new EndPostalVO();
//         endPostal.setEndP_bairro(bairro);
//         endPostal.setEndP_cidade(cidade);
//         endPostal.setEndP_estado(estado);
//         endPostal.setEndP_logradouro(logradouro);
//         endPostal.setEndP_cep("85892000");
//         endPostal.setEndP_nomeRua("Parana");

//         EnderecoVO endereco = new EnderecoVO();
//         endereco.setEnd_complemento("Kitnet");
//         endereco.setEnd_numero("1234");
//         endereco.setEnd_endP_id(endPostal);

//         java.util.List<EnderecoVO> enderecos = new java.util.ArrayList<>();
//         enderecos.add(endereco);
//         funcionario.setEndereco(enderecos);

//         // instancias relacionadas a funcionario
//         TelefoneVO telefone = new TelefoneVO();
//         funcionario.setFnc_dtContratacao(LocalDate.now());
//         funcionario.setFnc_salario(3500.0);

//         CargoVO cargo = new CargoVO();
//         cargo.setCar_id(1);
//         cargo.setCargo_descricao("Vendedor");
//         funcionario.setFnc_cargo(cargo);

//         funcionario.setPes_ativo(true);


//         // instancia a regra de negócio
//         FuncionarioRN rn = new FuncionarioRN();

//         try {
//             rn.adicionarFuncionario(funcionario);
//             System.out.println("✅ Funcionário cadastrado com sucesso!");
//         } catch (Exception e) {
//             System.out.println("❌ Erro ao cadastrar funcionário: " + e.getMessage());

//             // Se for erro SQL, mostra detalhes
//             if (e instanceof SQLException) {
//                 SQLException sqlEx = (SQLException) e;
//                 System.err.println("\n=== 💥 ERRO SQL DETALHADO ===");
//                 System.err.println("Mensagem: " + sqlEx.getMessage());
//                 System.err.println("Código de erro: " + sqlEx.getErrorCode());
//                 System.err.println("Estado SQL: " + sqlEx.getSQLState());
//                 System.err.println("Classe da exceção: " + sqlEx.getClass().getName());
//                 System.err.println("==============================\n");
//                 sqlEx.printStackTrace();
//             } else {
//                 // Para qualquer outro tipo de exceção
//                 e.printStackTrace();
//             }
//         }


//     }
// }