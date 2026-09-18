package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static String host;
    private static String username;
    private static String password;
    private static String nomeDB;

    private DBConnection() {
        // Utility class
    }

    public static void configura(
            String host,
            String username,
            String password
    ) throws SQLException {

        testServerConnection(host, username, password);

        DBConnection.host = host;
        DBConnection.username = username;
        DBConnection.password = password;
        DBConnection.nomeDB = null;
    }

    public static void configuraConDB(
            String host,
            String nomeDB,
            String username,
            String password
    ) throws SQLException {

        testDatabaseConnection(
                host,
                nomeDB,
                username,
                password
        );

        DBConnection.host = host;
        DBConnection.username = username;
        DBConnection.password = password;
        DBConnection.nomeDB = nomeDB;
    }

    public static void selezionaDB(String nomeDB) throws SQLException {
        ensureConfigured();

        try (Connection ignored = openDatabaseConnection(nomeDB)) {
            // Connection successfully validated.
        }

        DBConnection.nomeDB = nomeDB;
    }

    public static void selezionaServer() throws SQLException {
        ensureConfigured();

        try (Connection ignored = openServerConnection()) {
            // Connection successfully validated.
        }

        nomeDB = null;
    }

    public static String getNomeDB() {
        return nomeDB;
    }

    static Connection getServerConnection() throws SQLException {
        ensureConfigured();
        return openServerConnection();
    }

    static Connection getConnectionForDatabase(String database)
            throws SQLException {
        ensureConfigured();
        return openDatabaseConnection(database);
    }

    public static Connection getConnection() throws SQLException {
        ensureConfigured();

        return nomeDB == null
                ? openServerConnection()
                : openDatabaseConnection(nomeDB);
    }

    private static Connection openServerConnection()
            throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://" + host + "/",
                username,
                password
        );
    }

    private static Connection openDatabaseConnection(String database)
            throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
    }

    private static void testServerConnection(
            String host,
            String username,
            String password
    ) throws SQLException {
        try (
                Connection ignored = DriverManager.getConnection(
                        "jdbc:mysql://" + host + "/",
                        username,
                        password
                )
        ) {
            // Credentials successfully validated.
        }
    }

    private static void testDatabaseConnection(
            String host,
            String database,
            String username,
            String password
    ) throws SQLException {
        try (
                Connection ignored = DriverManager.getConnection(
                        "jdbc:mysql://" + host + "/" + database,
                        username,
                        password
                )
        ) {
            // Database connection successfully validated.
        }
    }

    private static void ensureConfigured() throws SQLException {
        if (host == null || username == null || password == null) {
            throw new SQLException(
                    "Configurazione database non disponibile."
            );
        }
    }
}