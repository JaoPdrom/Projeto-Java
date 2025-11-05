/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.cliente;

import model.rn.ClienteRN;
import model.vo.*;

import java.sql.SQLException;
import java.time.LocalDate;

public class InserirClienteTESTE {
    public static void main(String[] args) {
        try {
        ClienteVO cli = new ClienteVO();
        cli.setPes_cpf("12345678901");
        cli.setPes_nome("Cliente Teste");

            SexoVO sexo1 = new SexoVO();
            sexo1.setSex_id(1);
            cli.setPes_sexo(sexo1);

            cli.setPes_dt_nascimento(LocalDate.of(1995, 1, 10));
            cli.setPes_email("cliente.teste@email.com");
            cli.setPes_ativo(true);
            cli.setCli_dtCadastro(LocalDate.now());

            // Endereço
            EndPostalVO endPostal = new EndPostalVO();
            endPostal.setEndP_bairro(new BairroVO()); endPostal.getEndP_bairro().setBairro_id(1);
            endPostal.setEndP_cidade(new CidadeVO()); endPostal.getEndP_cidade().setCid_id(1);
            endPostal.setEndP_estado(new EstadoVO()); endPostal.getEndP_estado().setEst_sigla("SP");
            endPostal.setEndP_logradouro(new LogradouroVO()); endPostal.getEndP_logradouro().setLogradouro_id(1);
            endPostal.setEndP_cep("85892000");
            endPostal.setEndP_nomeRua("Parana");

            EnderecoVO endereco = new EnderecoVO();
            endereco.setEnd_complemento("Casa");
            endereco.setEnd_numero("1234");
            endereco.setEnd_endP_id(endPostal);

            java.util.List<EnderecoVO> enderecos = new java.util.ArrayList<>();
            enderecos.add(endereco);

            // Telefone
            TelefoneVO telefone1 = new TelefoneVO();
            telefone1.setTel_codPais("55");
            telefone1.setTel_ddd("17");
            telefone1.setTel_numero("5555555555");

            java.util.List<TelefoneVO> telefones = new java.util.ArrayList<>();
            telefones.add(telefone1);

            // Chama RN
            ClienteRN clienteRN = new ClienteRN();
            clienteRN.salvarNovo(cli, telefones, enderecos);

            System.out.println("OK. Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
}

