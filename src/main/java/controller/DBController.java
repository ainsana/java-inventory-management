package controller;

import dao.DBDAO;
import dao.DBConnection;
import view.MainView;
import view.TabellaDialog;
import view.DBDialog;

import java.util.List;
import javax.swing.*;
import java.sql.SQLException;

public class DBController {
    
	//Variabili private final
	private final DBDAO dao = new DBDAO();
    private final MainView view;
    private final ProdottoController prodottoController;

    //Costruttore
    public DBController(MainView view, ProdottoController prodottoController) {
        this.view = view;
        this.prodottoController = prodottoController;
        inizializza();
    }
    //Inizializzazione
    private void inizializza() {
        view.getCreaDBButton().addActionListener(e -> apriDialogCreaDB());
        view.getSelezionaDBButton().addActionListener(e -> selezionaDB());
        view.getSvuotaDBButton().addActionListener(e -> svuotaDB());
        view.getEliminaDBButton().addActionListener(e -> eliminaDB());
        view.getCreaTabellaButton().addActionListener(e -> apriDialogCreaTabella());
        view.getSelezionaTabellaButton().addActionListener(e -> selezionaTabella());
        view.getSvuotaTabellaButton().addActionListener(e -> svuotaTabella());
        view.getEliminaTabellaButton().addActionListener(e -> eliminaTabella());
        aggiornaListaDB();
    }

    //Metodi Gestione DB
    private void apriDialogCreaDB() {
        DBDialog dialog = new DBDialog((JFrame) view);
        dialog.setVisible(true);
        String nomeDB = dialog.getNomeDB();
        if (nomeDB != null && !nomeDB.isEmpty()) {
            boolean success = dao.creaDB(nomeDB);
            if (success) {
                JOptionPane.showMessageDialog(view, "Database creato correttamente!");
                aggiornaListaDB();
            } else {
                JOptionPane.showMessageDialog(view, "Errore nella creazione del database.");
            }
        }
    }

    public void aggiornaListaDB() {
        try {
            List<String> db = DBDAO.getListaDB();
            view.getListaDB().setListData(db.toArray(new String[0]));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Errore nel caricamento DB: " + ex.getMessage());
        }
    }

    public void selezionaDB() {
        String dbSelezionato = view.getListaDB().getSelectedValue();
        if (dbSelezionato == null) {
            JOptionPane.showMessageDialog(view, "Seleziona un database");
            return;
        }

        try {
            DBConnection.selezionaDB(dbSelezionato);
            JOptionPane.showMessageDialog(view, "Database '" + dbSelezionato + "' selezionato");
            aggiornaListaTabelle(dbSelezionato);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Errore connessione DB: " + e.getMessage());
        }
    }
    
    public void svuotaDB() {
        String dbSelezionato = view.getListaDB().getSelectedValue();
        if (dbSelezionato == null) {
            JOptionPane.showMessageDialog(view, "Seleziona un database");
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(view,
                "Sei sicuro di voler svuotare il database '" + dbSelezionato + "'?\nTutte le tabelle verranno eliminate.",
                "Conferma svuotamento", JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            try {
                DBConnection.selezionaDB(dbSelezionato);
                boolean ok = DBDAO.svuotaDB(dbSelezionato);
                if (ok) {
                    JOptionPane.showMessageDialog(view, "Database svuotato con successo!");
                    aggiornaListaTabelle(dbSelezionato);
                    view.getListaTabelle().setListData(new String[0]);
                } else {
                    JOptionPane.showMessageDialog(view, "Errore durante lo svuotamento.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Errore connessione: " + ex.getMessage());
            }
        }
    }

    public void eliminaDB() {
        String dbSelezionato = view.getListaDB().getSelectedValue();
        if (dbSelezionato == null) {
            JOptionPane.showMessageDialog(view, "Seleziona un database");
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(view,
                "Vuoi eliminare definitivamente il database '" + dbSelezionato + "'?",
                "Conferma eliminazione", JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            boolean ok = DBDAO.eliminaDB(dbSelezionato);
            if (ok) {
                JOptionPane.showMessageDialog(view, "Database eliminato con successo!");
                try {
                    DBConnection.selezionaServer();
                } catch (SQLException ex) {
                	JOptionPane.showMessageDialog(view, "Errore connessione: " + ex.getMessage());
                }
                SwingUtilities.invokeLater(() -> {
                    aggiornaListaDB();
                    view.getListaDB().clearSelection();
                    view.getListaTabelle().setListData(new String[0]);
                });
                
            } else {
                JOptionPane.showMessageDialog(view, "Errore durante l'eliminazione del database.");
            }
        }
    }
    
    //Metodi Gestione Tabella
    private void apriDialogCreaTabella() {
        TabellaDialog dialog = new TabellaDialog((JFrame) view);
        dialog.setVisible(true);

        String nomeTabella = dialog.getNomeTabella();
        if (nomeTabella != null && !nomeTabella.isEmpty()) {
            String nomeDB = DBConnection.getNomeDB();

            if (nomeDB == null) {
                JOptionPane.showMessageDialog(view, "Nessun database selezionato.");
                return;
            }

            boolean success = dao.creaTabella(nomeDB, nomeTabella);
            if (success) {
                JOptionPane.showMessageDialog(view, "Tabella creata con successo.");
                aggiornaListaTabelle(nomeDB);
            } else {
                JOptionPane.showMessageDialog(view, "Errore nella creazione della tabella.");
            }
        }
    }


    public void aggiornaListaTabelle(String nomeDB) {
        if (nomeDB == null || nomeDB.isBlank()) {
            view.getListaTabelle().setListData(new String[0]); 
            return;
        }
        try {
            List<String> tabelle = DBDAO.getListaTabelle(nomeDB);
            view.getListaTabelle().setListData(tabelle.toArray(new String[0]));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Errore nel caricamento tabelle: " + ex.getMessage());
        }
    }

    public void selezionaTabella() {
        String tabellaSelezionata = view.getListaTabelle().getSelectedValue();
        if (tabellaSelezionata == null) {
            JOptionPane.showMessageDialog(view, "Seleziona una tabella");
            return;
        }

        try {
            if (DBDAO.isTabellaCompatibile(tabellaSelezionata)) {
                prodottoController.setTabellaAttiva(tabellaSelezionata);
                JOptionPane.showMessageDialog(view, "Tabella '" + tabellaSelezionata + "' selezionata");
            } else {
                JOptionPane.showMessageDialog(view, "Tabella non compatibile con struttura Prodotto");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Errore nel controllo tabella: " + ex.getMessage());
        }
    }
    
    public void svuotaTabella() {
        String tabellaSelezionata = view.getListaTabelle().getSelectedValue();
        if (tabellaSelezionata == null) {
            JOptionPane.showMessageDialog(view, "Seleziona una tabella");
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(view,
                "Sei sicuro di voler svuotare la tabella '" + tabellaSelezionata + "'?",
                "Conferma svuotamento", JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            try {
                boolean ok = dao.svuotaTabella(tabellaSelezionata);
                if (ok) {
                    JOptionPane.showMessageDialog(view, "Tabella svuotata con successo!");
                    prodottoController.aggiornaListaProdotti(); 
                } else {
                    JOptionPane.showMessageDialog(view, "Errore durante lo svuotamento.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Errore SQL: " + ex.getMessage());
            }
        }
    }
    
    public void eliminaTabella() {
        String tabellaSelezionata = view.getListaTabelle().getSelectedValue();
        if (tabellaSelezionata == null) {
            JOptionPane.showMessageDialog(view, "Seleziona una tabella");
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(view,
                "Vuoi eliminare definitivamente la tabella '" + tabellaSelezionata + "'?",
                "Conferma eliminazione", JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            try {
                boolean ok = dao.eliminaTabella(tabellaSelezionata);
                if (ok) {
                    JOptionPane.showMessageDialog(view, "Tabella eliminata con successo!");
                    aggiornaListaTabelle(DBConnection.getNomeDB());
                    prodottoController.aggiornaListaProdotti(); 
                } else {
                    JOptionPane.showMessageDialog(view, "Errore durante l'eliminazione.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Errore SQL: " + ex.getMessage());
            }
        }
    }

}


