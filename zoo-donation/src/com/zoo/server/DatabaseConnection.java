package com.zoo.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/zoo_donation";
        String user = "root";
        String password = "sudopw";
        System.out.println("jdbc connection established!");
        return DriverManager.getConnection(url, user, password);
    }
}
