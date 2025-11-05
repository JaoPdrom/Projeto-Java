/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package testes.funcionario;

import model.rn.FuncionarioRN;
import model.vo.CargoVO;
import model.vo.FuncionarioVO;
import model.vo.SexoVO;

import java.time.LocalDate;

public class AtualizarFuncionarioTESTE {
    public static void main(String[] args) {
        try {
            FuncionarioRN funcRN = new FuncionarioRN();

            FuncionarioVO func = new FuncionarioVO();
            func.setPes_cpf("00022255588"); // CPF existente no banco
            func.setPes_nome("João Pedro Pepe");
            func.setPes_email("joao.atualizado@empresa.com");
            func.setPes_dt_nascimento(LocalDate.of(2002, 5, 20));
            func.setPes_ativo(true);

            // --- Sexo ---
            SexoVO sexo = new SexoVO();
            sexo.setSex_id(1); // ID válido existente em tb_sexo
            func.setPes_sexo(sexo);

            // --- Cargo ---
            CargoVO cargo = new CargoVO();
            cargo.setCar_id(1); // ID válido existente em tb_cargo
            func.setFnc_cargo(cargo);

            // --- Dados do funcionário ---
            func.setFnc_dtContratacao(LocalDate.of(2023, 1, 15));
            func.setFnc_salario(4200.00);
            func.setFnc_dtDemissao(null); // se ainda estiver ativo

            funcRN.atualizarFuncionario(func);

            System.out.println("✅ Teste concluído: Funcionário atualizado com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro ao testar atualização: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

