/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package controller;
import javax.swing.JOptionPane;
import view.JPPesGerenciamento;
import view.*;
import model.rn.PessoaRN;
import model.vo.PessoaVO;

import java.sql.SQLException;

public class PessoaGerenciamento {
    private PessoaRN pesRN;

    public PessoaGerenciamento() throws SQLException {
        this.pesRN = new PessoaRN();
    }

    public void cadastrarPessoa(PessoaVO p) throws Exception {
        pesRN.adicionarPessoa(p);
    }

}
