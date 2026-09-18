package controller;

import dao.ProdottoDAO;
import model.*;
import util.FiltroMultiplo;
import util.OrdinatoreMultiplo;
import view.MainView;
import view.ProdottoDialog;
import view.FiltraOrdinaDialog;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class ProdottoController {
    
	//Variabili private
	private ProdottoDAO dao;
    private MainView view;
    private JFrame parentJFrame;
    private FiltroMultiplo filtroAttivo = new FiltroMultiplo();
    private OrdinatoreMultiplo ordinatoreAttivo = new OrdinatoreMultiplo();

    //Costruttore
    public ProdottoController (ProdottoDAO dao, MainView view) {
    	this.dao = dao;
        this.view = view;
        this.parentJFrame = view; 
        setTabellaAttiva(dao.getTabellaAttiva());
        inizializza();
    }  
    
    //Inizializzazione
    public void inizializza() {    	
    	aggiornaListaProdotti();
        view.getAggiungiButton().addActionListener(e -> mostraProdottoDialog());
        view.getModificaButton().addActionListener(e -> modificaProdottoSelezionato());
        view.getEliminaButton().addActionListener(e -> eliminaProdottoSelezionato());
        view.getFiltraOrdinaButton().addActionListener(e -> {
            FiltraOrdinaDialog dialog = new FiltraOrdinaDialog(view, filtroAttivo, ordinatoreAttivo);
            dialog.setVisible(true);
            if (dialog.isConfermato()) {
                FiltroMultiplo filtroSelezionato = dialog.getFiltro();
                OrdinatoreMultiplo ordinatoreSelezionato = dialog.getOrdinatore();
                applicaFiltraOrdina(filtroSelezionato, ordinatoreSelezionato);
            }
        });
        view.getImportaCSVButton().addActionListener(e -> importaCSV());
        view.getEsportaCSVButton().addActionListener(e -> esportaCSV());
    }            
    
    //Metodi Gestione Prodotti
    public void setTabellaAttiva(String tabella) {
        dao.setTabellaAttiva(tabella);
        aggiornaListaProdotti();
    }

    public void aggiornaListaProdotti() {
    	filtroAttivo = new FiltroMultiplo();     
        ordinatoreAttivo = new OrdinatoreMultiplo();  
        List<Prodotto> prodotti = dao.getListaProdotti();
        if (view != null) {
            view.mostraProdotti(prodotti);
        }
    }
        
    public void aggiungiProdotto(Prodotto prodotto) {
        dao.inserisciProdotto(prodotto);
        aggiornaListaProdotti();
    }
    
    public void modificaProdottoSelezionato() {
        int selectedRow = view.getProdottoTabella().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Seleziona un prodotto da modificare.");
            return;
        }

        int idProdotto = (int) view.getProdottoTabella().getValueAt(selectedRow, 0);
        Prodotto prodotto = dao.getProdottoById(idProdotto);
        if (prodotto == null) {
            JOptionPane.showMessageDialog(view, "Prodotto non trovato.");
            return;
        }

        ProdottoDialog dialog = new ProdottoDialog((JFrame) view, prodotto);
        dialog.setVisible(true);
        
        Prodotto modificato = dialog.getProdottoInserito();
        if (modificato != null) {
            modificato.setId(idProdotto); 
            boolean ok = dao.aggiornaProdotto(modificato);
            if (ok) {
                JOptionPane.showMessageDialog(view, "Prodotto aggiornato con successo.");
            } else {
                JOptionPane.showMessageDialog(view, "Errore durante l'aggiornamento.");
            }
            
            aggiornaListaProdotti();
        }
    }

    public void eliminaProdottoSelezionato() {
        int[] selectedRows = view.getProdottoTabella().getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(view, "Seleziona uno o più prodotti da eliminare.");
            return;
        }
        
        int conferma = JOptionPane.showConfirmDialog(view,
                "Sei sicuro di voler eliminare i prodotti selezionati?",
                "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
        if (conferma != JOptionPane.YES_OPTION) return;
        boolean almenoUnoEliminato = false;
        for (int row : selectedRows) {
            int idProdotto = (int) view.getProdottoTabella().getValueAt(row, 0);
            boolean ok = dao.eliminaProdotto(idProdotto);
            if (ok) almenoUnoEliminato = true;
        }

        if (almenoUnoEliminato) {
            JOptionPane.showMessageDialog(view, "Prodotti eliminati con successo.");
        } else {
            JOptionPane.showMessageDialog(view, "Errore durante l'eliminazione dei prodotti.");
        }

        aggiornaListaProdotti();
    }
       
    public void mostraProdottoDialog() {
    	ProdottoDialog dialog = new ProdottoDialog(parentJFrame);
        dialog.setVisible(true);                   	     		    		
    	Prodotto prodotto = dialog.getProdottoInserito();
    	if (prodotto != null) {
    		aggiungiProdotto(prodotto);
    	}    	
    }
    
    //Metodo Filtraggio e Ordinamento
    public void applicaFiltraOrdina(FiltroMultiplo filtro, OrdinatoreMultiplo ordinatore) {
        this.filtroAttivo = filtro;
        this.ordinatoreAttivo = ordinatore;
        List<Prodotto> listaCompleta = dao.getListaProdotti();
        List<Prodotto> filtrata = listaCompleta.stream()
                                    .filter(filtroAttivo::test)
                                    .collect(Collectors.toList());
        if (!ordinatoreAttivo.isEmpty()) {
            filtrata.sort(ordinatoreAttivo);
        }

        view.mostraProdotti(filtrata);
    }

    //Metodi Import/Export
    public void importaCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int risultato = fileChooser.showOpenDialog(view);
        if (risultato == JFileChooser.APPROVE_OPTION) {
            boolean successo = dao.importaDaCSV(fileChooser.getSelectedFile());
            JOptionPane.showMessageDialog(view,
                successo ? "Importazione completata con successo." : "Errore durante l'importazione.");
            aggiornaListaProdotti();
        }
    }

    public void esportaCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int risultato = fileChooser.showSaveDialog(view);
        if (risultato == JFileChooser.APPROVE_OPTION) {
            boolean successo = dao.esportaCSV(fileChooser.getSelectedFile());
            JOptionPane.showMessageDialog(view,
                successo ? "Esportazione completata con successo." : "Errore durante l'esportazione.");
        }
    }
    
}




