package dao;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	//Variabili private e static Connessione MySQL
	private static String host;
	private static String username;
	private static String password;
	private static String nomeDB;
	private static Connection connection;

	//Metodi Configurazione Connessione
    public static void configura(String host, String username, String password) throws SQLException {
    	DBConnection.host = host;
        DBConnection.username = username;
        DBConnection.password = password;
        nomeDB = null;
    	connection = DriverManager.getConnection("jdbc:mysql://" + host + "/", username, password);    	
    }

    public static void configuraConDB(String host, String nomeDB, String username, String password) throws SQLException {
    	DBConnection.host = host;
        DBConnection.username = username;
        DBConnection.password = password;
        DBConnection.nomeDB = nomeDB;
    	connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + nomeDB, username, password);
    }
    
    //Getters 
    public static String getHost() {
    	return host; }
    
    public static String getUsername() {
    	return username; }
    
    public static String getPassword() {
    	return password; }
    
    public static String getNomeDB() {
        return nomeDB; 
    }
 
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if (host != null && username != null && password != null && nomeDB != null) {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + nomeDB, username, password);
            } else {
                throw new SQLException("Nessuna connessione attiva");
            }
        }
        return connection;
    }    
    
}
