package main;

import controller.*;
import dao.*;
import view.LoginView;
import view.MainView;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
        	
            // Istanza della LoginView
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView);
            loginView.setController(loginController);
            loginView.setVisible(true);
            
            // Azione dopo login riuscito
            loginView.setOnLoginSuccess(() -> {
            	
                // Chiude LoginView
                loginView.dispose(); 
                
                // Crea e mostra la MainView
                ProdottoDAO prodottoDAO = new ProdottoDAO();
                MainView view = new MainView();
                ProdottoController prodottoController = new ProdottoController(prodottoDAO, view);
                view.setProdottoController(prodottoController);
                DBController dbController = new DBController(view, prodottoController);                                
                dbController.aggiornaListaTabelle(DBConnection.getNomeDB());
                view.setVisible(true);
            });
        });
    }
    
}
