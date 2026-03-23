package com.ecoloop.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {

    // Ajuste URL/USER/PASSWORD conforme seu ambiente
    private static final String URL = "jdbc:mysql://localhost:3306/ecopontos?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USER = "root";
    private static final String PASSWORD = "370607";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // driver moderno
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL não encontrado!", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }
}