/*
 * Copyright (c) 2025.
 * Criado por Joao Pedro Missiagia. Todos os direitos reservados.
 */

package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDAO {
    public static Connection getConexao() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Conectando ao banco de dados.");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/db_poo?serverTimezone=UTC", "root", "Nwdcyd71!");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e.getMessage());
        }

    }
}
