package dao;

import java.util.ArrayList;

import java.util.List;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDAO {
	
	//Metodi Gestione DB
	public boolean creaDB(String host, String username, String password, String nomeDB) {
        try {
        	Connection tempConn = DriverManager.getConnection("jdbc:mysql://" + host + "/", username, password);
        	Statement tempStmt = tempConn.createStatement();
        	tempStmt.executeUpdate("CREATE DATABASE " + nomeDB);
        	DBConnection.configuraConDB(host, nomeDB, username, password);
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();                                             
            stmt.execute("USE " + nomeDB);             
        	stmt.executeUpdate("CREATE TABLE IF NOT EXISTS utenti (" +
          		  "id INT AUTO_INCREMENT PRIMARY KEY, " +
          		  "username VARCHAR(50) UNIQUE NOT NULL, " +
          		  "password VARCHAR(255) NOT NULL)"
          		);        	        	
            stmt.executeUpdate("INSERT INTO utenti (username, password) VALUES ('" + username + "', '" + password + "')");            
            return true;        	
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }        
	}
    
	public static boolean svuotaDB(String nomeDB) {
	    try (Connection conn = DBConnection.getConnection();
	         Statement stmt = conn.createStatement()) {
	    	stmt.execute("USE " + nomeDB);
	        List<String> tabelle = new java.util.ArrayList<>();
	        try (ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
	            while (rs.next()) {
	                tabelle.add(rs.getString(1));
	            }
	        }
	        for (String tabella : tabelle) {
	            stmt.executeUpdate("DROP TABLE IF EXISTS " + tabella);
	        }
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public static boolean eliminaDB(String host, String username, String password, String nomeDB) {
	    try (var conn = DriverManager.getConnection("jdbc:mysql://" + host + "/", username, password);
	         var stmt = conn.createStatement()) {
	        stmt.executeUpdate("DROP DATABASE IF EXISTS " + nomeDB);
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
    
	//Metodi Gestione Tabelle
    public boolean creaTabella(String host, String username, String password, String nomeDB, String nomeTabella) {
        try {
        	Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + nomeDB, username, password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + nomeTabella + " (" +
            		"id INT AUTO_INCREMENT PRIMARY KEY," +
            		"nome VARCHAR(100)," +
            		"categoria VARCHAR(50)," +
            		"taglia VARCHAR(20)," +
            		"tipologia VARCHAR(20)," +
            		"colori VARCHAR(200)," +
            		"quantita INT," +
            		"prezzoAcquisto DOUBLE," +
            		"prezzoVendita DOUBLE)" 
            		);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminaTabella(String nomeTabella) throws SQLException {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS " + nomeTabella);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean svuotaTabella(String nomeTabella) throws SQLException {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM " + nomeTabella);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Metodo Compatibilità
    public static boolean isTabellaCompatibile(String tabella) throws SQLException {
        Connection conn = DBConnection.getConnection();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getColumns(null, null, tabella, null);
        List<String> colonneRichieste = List.of("id", "nome", "categoria", "taglia", "tipologia", "colori", "quantita", "prezzoAcquisto", "prezzoVendita");
        List<String> colonnePresenti = new ArrayList<>();
        while (rs.next()) {
            colonnePresenti.add(rs.getString("COLUMN_NAME"));
        }
        return colonnePresenti.containsAll(colonneRichieste);
    }
    
    //Getters Liste DB e Tabelle
    public static List<String> getListaDB() throws SQLException {
        List<String> listaDB = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        ResultSet rs = conn.getMetaData().getCatalogs();
        while (rs.next()) {
            listaDB.add(rs.getString(1));
        }
        return listaDB;
    }
        
    public static List<String> getListaTabelle(String nomeDB) throws SQLException {
        List<String> listaTabelle = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getTables(nomeDB, null, "%", new String[] { "TABLE" });
        while (rs.next()) {
            listaTabelle.add(rs.getString("TABLE_NAME"));
        }
        return listaTabelle;
    }
    
}
