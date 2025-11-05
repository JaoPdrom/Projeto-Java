/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.cliente;

import model.rn.ClienteRN;
import model.vo.ClienteVO;
import model.vo.SexoVO;

import java.time.LocalDate;

public class AtualizarClienteTESTE {
    public static void main(String[] args) {
        try {
            ClienteRN cliRN = new ClienteRN();

            ClienteVO cli = new ClienteVO();
            cli.setPes_cpf("12345678901"); // CPF existente no banco (mesmo do inserir)
            cli.setPes_nome("Cliente Teste Atualizado");
            cli.setPes_email("cliente.atualizado@teste.com");
            cli.setPes_dt_nascimento(LocalDate.of(1996, 2, 20));
            cli.setPes_ativo(true);

            // Sexo
            SexoVO sexo = new SexoVO();
            sexo.setSex_id(1);
            cli.setPes_sexo(sexo);

            // Dados específicos de cliente
            cli.setCli_dtCadastro(LocalDate.now());
            //TipoClienteVO tipo = new TipoClienteVO();
            //tipo.setTipo_cliente_id(1);
            //cli.setCli_tipo_cliente_id(tipo);

            cliRN.atualizarCliente(cli);

            System.out.println("OK. Cliente atualizado com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao testar atualização de cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
