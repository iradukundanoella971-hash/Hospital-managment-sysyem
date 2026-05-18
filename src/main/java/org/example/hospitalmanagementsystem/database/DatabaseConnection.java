package org.example.hospitalmanagementsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection.java
 *
 * Connects to the PostgreSQL database using JDBC DriverManager.
 * Call getConnection() every time you need to talk to the database.
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:postgresql://localhost:5433/HOSPITALDB";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
