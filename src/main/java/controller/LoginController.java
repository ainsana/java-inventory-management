package controller;

import dao.DBConnection;
import dao.LoginDAO;
import view.LoginView;

import java.sql.SQLException;

public class LoginController {
	
	//Variabili private
	private LoginView view;
    private LoginDAO loginDAO;

    //Costruttore
    public LoginController(LoginView view) {
        this.view = view;
        this.loginDAO = new LoginDAO();
    }

    //Metodo Login
    public void login(String host, String username, String password, String database) {
        try {
            if (database != null && !database.isEmpty()) {
                DBConnection.configuraConDB(host, database, username, password);
                boolean success = loginDAO.checkLogin(username, password);
                if (success) {
                    view.mostraEsito("Login riuscito");
                    if (view.getOnLoginSuccess() != null) {                    	
                        view.getOnLoginSuccess().run();
                    }
                } else {
                    view.mostraErrore("Credenziali errate");
                }
            } else {
                DBConnection.configura(host, username, password);
                view.mostraEsito("Connessione al server riuscita. Nessun DB selezionato.");
                if (view.getOnLoginSuccess() != null) {
                    view.getOnLoginSuccess().run();
                }
            }
        } catch (SQLException e) {
            view.mostraErrore("Errore: " + e.getMessage());
        }
    }
    
}

