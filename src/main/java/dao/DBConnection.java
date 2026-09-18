package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static String host;
    private static String username;
    private static String password;
    private static String nomeDB;
    private static Connection connection;

    private DBConnection() {
        // Utility class
    }

    public static void configura(
            String host,
            String username,
            String password
    ) throws SQLException {
        closeCurrentConnection();

        DBConnection.host = host;
        DBConnection.username = username;
        DBConnection.password = password;
        DBConnection.nomeDB = null;

        connection = openServerConnection();
    }

    public static void configuraConDB(
            String host,
            String nomeDB,
            String username,
            String password
    ) throws SQLException {
        closeCurrentConnection();

        DBConnection.host = host;
        DBConnection.username = username;
        DBConnection.password = password;
        DBConnection.nomeDB = nomeDB;

        connection = openDatabaseConnection(nomeDB);
    }

    public static void selezionaDB(String nomeDB) throws SQLException {
        ensureConfigured();
        closeCurrentConnection();

        connection = openDatabaseConnection(nomeDB);
        DBConnection.nomeDB = nomeDB;
    }

    public static void selezionaServer() throws SQLException {
        ensureConfigured();
        closeCurrentConnection();

        connection = openServerConnection();
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

        if (connection == null || connection.isClosed()) {
            connection = nomeDB == null
                    ? openServerConnection()
                    : openDatabaseConnection(nomeDB);
        }

        return connection;
    }

    private static Connection openServerConnection() throws SQLException {
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

    private static void ensureConfigured() throws SQLException {
        if (host == null || username == null || password == null) {
            throw new SQLException("Configurazione database non disponibile.");
        }
    }

    private static void closeCurrentConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }

        connection = null;
    }
}