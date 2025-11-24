package com.guvi.mindfulness.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Simple singleton helper that manages a single MySQL JDBC connection for the project.
 * The class hides DriverManager details and exposes convenience helpers for DAO classes.
 */
public final class DBConnection {

    private static final String DB_HOST = System.getProperty("DB_HOST", "localhost");
    private static final String DB_PORT = System.getProperty("DB_PORT", "3306");
    private static final String DB_NAME = System.getProperty("DB_NAME", "mindfulnessdb");
    private static final String DB_USER = System.getProperty("DB_USER", "root");
    private static final String DB_PASSWORD = System.getProperty("DB_PASSWORD", "");
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true";

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL driver not found in classpath", e);
        }
    }

    private DBConnection() {
    }

    /**
     * Returns a shared connection instance. A lightweight guard recreates the connection
     * if it has been closed by previous DAO calls.
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        return connection;
    }

    /**
     * Executes the SQL statements from {@code /db/schema.sql} to create tables used in demos.
     * This helper can be invoked from integration tests or the bootstrap servlet.
     */
    public static void initializeSchema() throws SQLException {
        try (InputStream schemaStream = DBConnection.class.getResourceAsStream("/db/schema.sql")) {
            if (schemaStream == null) {
                throw new IllegalStateException("Unable to find schema.sql in resources/db");
            }
            String sql = new BufferedReader(new InputStreamReader(schemaStream))
                    .lines()
                    .collect(Collectors.joining("\n"));
            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    try (Statement stmt = getConnection().createStatement()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (IOException e) {
            throw new SQLException("Failed to load schema.sql: " + e.getMessage(), e);
        }
    }

    /**
     * Utility to quietly close the shared connection during application shutdown.
     */
    public static synchronized void closeConnection() {
        if (Objects.nonNull(connection)) {
            try {
                connection.close();
            } catch (SQLException ignored) {
                // Nothing else to do during shutdown
            }
        }
    }
}

