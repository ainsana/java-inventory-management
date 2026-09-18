package controller;

import dao.DBConnection;
import view.LoginView;

import java.sql.SQLException;

public class LoginController {

    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
    }

    public void login(
            String host,
            String username,
            String password,
            String database
    ) {
        try {
            if (database != null && !database.isBlank()) {
                DBConnection.configuraConDB(
                        host,
                        database,
                        username,
                        password
                );

                view.mostraEsito("Connessione al database riuscita.");

                if (view.getOnLoginSuccess() != null) {
                    view.getOnLoginSuccess().run();
                }
            } else {
                DBConnection.configura(
                        host,
                        username,
                        password
                );

                view.mostraEsito(
                        "Connessione al server riuscita. Nessun database selezionato."
                );

                if (view.getOnLoginSuccess() != null) {
                    view.getOnLoginSuccess().run();
                }
            }
        } catch (SQLException e) {
            view.mostraErrore(
                    "Errore di connessione: " + e.getMessage()
            );
        }
    }
}

