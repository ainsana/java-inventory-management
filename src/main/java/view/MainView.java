package view;

import controller.ProdottoController;
import model.Prodotto;
import controller.DBController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainView extends JFrame {
    
	//Variabili Tab1
	
	//Variabili private Totali
	private JLabel totaleQuantitaLabel;
    private JLabel totalePrezzoAcquistoLabel;
    private JLabel totalePrezzoVenditaLabel;
    private JLabel totaleDifferenzaLabel;
	
    //Variabili private e final Tabella
	private final DefaultTableModel prodottoModel;
    private final JTable prodottoTabella;    
    
    //Variabili private e final Bottoni Prodotto
    private final JButton aggiungiButton;
    private final JButton modificaButton;
    private final JButton eliminaButton;
    private final JButton filtraOrdinaButton;
    private final JButton importaCSVButton;
    private final JButton esportaCSVButton;    

    //Variabili Tab2
    
    //Variabili private e final Liste
    private final JList<String> listaDB;
    private final JList<String> listaTabelle;
    
    //Variabili private e final Bottoni DB
    private final JButton creaDBButton;    
    private final JButton selezionaDBButton;
    private final JButton svuotaDBButton;
    private final JButton eliminaDBButton;    

    //Variabili private e final Bottoni Tabelle
    private final JButton creaTabellaButton;
    private final JButton selezionaTabellaButton;
    private final JButton svuotaTabellaButton;
    private final JButton eliminaTabellaButton;

    //Variabili private Controller
    private ProdottoController prodottoController;
    private DBController dbController;

    //Costruttore
    public MainView() {
        setTitle("Gestione Inventario");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        //Tab 1: Gestione Prodotto
        JPanel prodottoPanel = new JPanel(new BorderLayout());
        JPanel totaliPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        totaliPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel bottoniProdottoPanel = new JPanel(new GridLayout(1, 6));

        //Pannello sopra - Totali
        totaleQuantitaLabel = new JLabel("Totale quantità: 0");
        totalePrezzoAcquistoLabel = new JLabel("Totale prezzo acquisto: 0.0");
        totalePrezzoVenditaLabel = new JLabel("Totale prezzo vendita: 0.0");
        totaleDifferenzaLabel = new JLabel("Totale differenza: 0.0");
        
        totaliPanel.add(totaleQuantitaLabel);
        totaliPanel.add(totalePrezzoAcquistoLabel);
        totaliPanel.add(totalePrezzoVenditaLabel);
        totaliPanel.add(totaleDifferenzaLabel);        
        
        //Pannello centro - Tabella
        prodottoModel = new DefaultTableModel(new String[]{
        	    "ID", "Nome", "Categoria", "Taglia", "Tipologia", "Colori", "Quantità", "Acquisto", "Vendita"
        }, 0) {
        	    @Override
        	    public boolean isCellEditable(int row, int column) {
        	        return false; 
        	    }
        	};
        prodottoTabella = new JTable(prodottoModel);
        JScrollPane scrollPane = new JScrollPane(prodottoTabella);
        
        //Pannello sotto - Bottoni Prodotto
        aggiungiButton = new JButton("Aggiungi Prodotto");
        modificaButton = new JButton("Modifica Prodotto");
        eliminaButton = new JButton("Elimina Prodotto");
        filtraOrdinaButton = new JButton("Filtra/Ordina");
        importaCSVButton = new JButton("Importa CSV");
        esportaCSVButton = new JButton("Esporta CSV");
        
        
        bottoniProdottoPanel.add(aggiungiButton);
        bottoniProdottoPanel.add(modificaButton);
        bottoniProdottoPanel.add(eliminaButton);
        bottoniProdottoPanel.add(filtraOrdinaButton);
        bottoniProdottoPanel.add(importaCSVButton);
        bottoniProdottoPanel.add(esportaCSVButton);

        prodottoPanel.add(totaliPanel, BorderLayout.NORTH);
        prodottoPanel.add(scrollPane, BorderLayout.CENTER);
        prodottoPanel.add(bottoniProdottoPanel, BorderLayout.SOUTH);
        
        //Tab 2: Gestione DB
        JPanel dbPanel = new JPanel(new BorderLayout());

        //Pannello centro - Lista DB e Tabelle
        JPanel listePanel = new JPanel(new GridLayout(1, 2));
        listaDB = new JList<>();
        listaTabelle = new JList<>();
        listePanel.add(new JScrollPane(listaDB));
        listePanel.add(new JScrollPane(listaTabelle));

        //Pannello sotto - Bottoni DB e Tabelle
        JPanel bottoniDBPanel = new JPanel(new GridLayout(4, 2));
        creaDBButton = new JButton("Crea DB");
        eliminaDBButton = new JButton("Elimina DB");
        svuotaDBButton = new JButton("Svuota DB");
        selezionaDBButton = new JButton("Seleziona DB");

        creaTabellaButton = new JButton("Crea Tabella");
        eliminaTabellaButton = new JButton("Elimina Tabella");
        svuotaTabellaButton = new JButton("Svuota Tabella");
        selezionaTabellaButton = new JButton("Seleziona Tabella");

        bottoniDBPanel.add(creaDBButton);
        bottoniDBPanel.add(creaTabellaButton);
        bottoniDBPanel.add(selezionaDBButton);
        bottoniDBPanel.add(selezionaTabellaButton);                      
        bottoniDBPanel.add(svuotaDBButton);                
        bottoniDBPanel.add(svuotaTabellaButton); 
        bottoniDBPanel.add(eliminaDBButton); 
        bottoniDBPanel.add(eliminaTabellaButton);
           

        dbPanel.add(listePanel, BorderLayout.CENTER);
        dbPanel.add(bottoniDBPanel, BorderLayout.SOUTH);
        
        //Tabs 
        tabbedPane.addTab("Gestione Prodotto", prodottoPanel);
        tabbedPane.addTab("Gestione Database", dbPanel);

        add(tabbedPane);
    }
       
    //Metodi Visualizzazione Tab1
    public void mostraProdotti(List<Prodotto> prodotti) {
    	prodottoModel.setRowCount(0);
    	for (Prodotto p : prodotti) {
    		prodottoModel.addRow(new Object[]{
    				p.getId(), p.getNome(),
    				p.getCategoria(), p.getTaglia(), p.getTipologia(),
    				String.join(",", p.getColori().stream().map(Enum::name).toList()),
    				p.getQuantita(), p.getPrezzoAcquisto(),
    				p.getPrezzoVendita(), p.getDifferenzaPrezzo()
    		});
    	}
    	aggiornaTotali(prodotti);
    }

    private void aggiornaTotali(List<Prodotto> prodotti) {
    	int totaleQuantita = 0;
	    double totalePrezzoAcquisto = 0.0;
	    double totalePrezzoVendita = 0.0;

	    for (Prodotto p : prodotti) {
	        totaleQuantita += p.getQuantita();
	        totalePrezzoAcquisto += p.getPrezzoAcquisto() * p.getQuantita();
	        totalePrezzoVendita += p.getPrezzoVendita() * p.getQuantita();
	    }

	    double totaleDifferenza = totalePrezzoVendita - totalePrezzoAcquisto;

	    totaleQuantitaLabel.setText("Totale quantità: " + totaleQuantita);
	    totalePrezzoAcquistoLabel.setText(String.format("Totale prezzo acquisto: %.2f", totalePrezzoAcquisto));
	    totalePrezzoVenditaLabel.setText(String.format("Totale prezzo vendita: %.2f", totalePrezzoVendita));
	    totaleDifferenzaLabel.setText(String.format("Totale margine: %.2f", totaleDifferenza));
	}
    
    // Getters Bottoni, Tabella e Liste
    public JTable getProdottoTabella() {
    	return prodottoTabella; }
    
    public JButton getAggiungiButton() { 
    	return aggiungiButton; }
    
    public JButton getModificaButton() { 
    	return modificaButton; }
    
    public JButton getEliminaButton() { 
    	return eliminaButton; }
    
    public JButton getFiltraOrdinaButton() { 
    	return filtraOrdinaButton; }
    
    public JButton getImportaCSVButton() { 
    	return importaCSVButton; }
    
    public JButton getEsportaCSVButton() { 
    	return esportaCSVButton; }
    
    public JButton getCreaDBButton() {
        return creaDBButton;
    }

    public JButton getEliminaDBButton() {
        return eliminaDBButton;
    }

    public JButton getSvuotaDBButton() {
        return svuotaDBButton;
    }

    public JButton getSelezionaDBButton() {
        return selezionaDBButton;
    }

    public JButton getCreaTabellaButton() {
        return creaTabellaButton;
    }

    public JButton getEliminaTabellaButton() {
        return eliminaTabellaButton;
    }

    public JButton getSvuotaTabellaButton() {
        return svuotaTabellaButton;
    }

    public JButton getSelezionaTabellaButton() {
        return selezionaTabellaButton;
    }

    public JList<String> getListaDB() {
        return listaDB;
    }

    public JList<String> getListaTabelle() {
        return listaTabelle;
    }
    
    //Getters e Setters Controller
    public ProdottoController getProdottoController() {
        return prodottoController; }
    public void setProdottoController(ProdottoController controller) {
        this.prodottoController = controller; }
  
    public DBController getDBController() {
        return dbController; }
    public void setDBController(DBController controller) {
        this.dbController = controller; }
   
}


