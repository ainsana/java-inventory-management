package view;

import model.*;
import util.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class FiltraOrdinaDialog extends JDialog {

    //Variabile private Conferma
    private boolean confermato = false;

    //Variabili private final Filtro e Ordinatore
    private final FiltroMultiplo filtro;
    private final OrdinatoreMultiplo ordinatore;

    //Variabili private Filtri
    private List<JCheckBox> categoriaCheckBoxes;
    private List<JCheckBox> tipologiaCheckBoxes;
    private List<JCheckBox> tagliaCheckBoxes;
    private List<JCheckBox> coloreCheckBoxes;
    private JTextField quantitaMinField;
    private JTextField quantitaMaxField;
    private JTextField prezzoAcquistoMinField;
    private JTextField prezzoAcquistoMaxField;
    private JTextField prezzoVenditaMinField;
    private JTextField prezzoVenditaMaxField;

    //Variabili private Ordinatori
    private DefaultListModel<String> listaOrdinamentiModel;
    private JList<String> listaOrdinamenti;
    private JComboBox<OrdinatoreMultiplo.CampoOrdinamento> campoOrdinamentoBox;
    private JComboBox<OrdinatoreMultiplo.Direzione> direzioneBox;
    private JButton aggiungiOrdinamentoButton;
    private JButton rimuoviOrdinamentoButton;

    //Costruttore
    public FiltraOrdinaDialog(JFrame parent, FiltroMultiplo filtro, OrdinatoreMultiplo ordinatore) {
        super(parent, "Filtra e Ordina", true);
        this.filtro = filtro;
        this.ordinatore = ordinatore;

        initComponents();
        loadFiltro();
        loadOrdinatore();

        pack();
        setLocationRelativeTo(parent);
    }

    //Metodi Costruzione
    private void initComponents() {
        setLayout(new BorderLayout(10,10));

        JPanel filtroPanel = creaPannelloFiltro();
        JPanel ordinamentoPanel = creaPannelloOrdinamento();

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10,10));
        centerPanel.add(filtroPanel);
        centerPanel.add(ordinamentoPanel);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottoniPanel = new JPanel();
        JButton confermaButton = new JButton("Conferma");
        JButton annullaButton = new JButton("Annulla");
        JButton resetFiltroButton = new JButton("Reset Filtro");
        JButton resetOrdinamentoButton = new JButton("Reset Ordinamento");
        confermaButton.addActionListener(e -> {
            if (leggiFiltroDaInput() && leggiOrdinatoreDaInput()) {
                if (validaFiltri()) {
                    confermato = true;
                    setVisible(false);
                }
            }
        });
        annullaButton.addActionListener(e -> {
            confermato = false;
            setVisible(false);
        });
        resetFiltroButton.addActionListener(e -> {
            resetFiltro();
        });
        resetOrdinamentoButton.addActionListener(e -> {
            ordinatore.getCriteri().clear();
            aggiornaComponentiOrdinamento();
        });

        bottoniPanel.add(confermaButton);
        bottoniPanel.add(annullaButton);
        bottoniPanel.add(resetFiltroButton);
        bottoniPanel.add(resetOrdinamentoButton);

        add(bottoniPanel, BorderLayout.SOUTH);
    }

    private JPanel creaSezioneFiltroAccordion(String titolo, List<JCheckBox> checkBoxes) {
        JPanel panel = new JPanel(new BorderLayout());
        JButton toggleButton = new JButton(titolo + " [+]");
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        for (JCheckBox cb : checkBoxes) {
            checkboxPanel.add(cb);
        }

        JScrollPane scrollPane = new JScrollPane(checkboxPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(300, 150));
        scrollPane.setVisible(false);
        toggleButton.addActionListener(e -> {
            boolean visibile = scrollPane.isVisible();
            scrollPane.setVisible(!visibile);
            toggleButton.setText(titolo + (visibile ? " [+]" : " [-]"));
            SwingUtilities.invokeLater(() -> pack());
        });

        panel.add(toggleButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel creaPannelloFiltro() {
        
    	//Pannello Filtro
    	JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Filtri"));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        int row = 0;
        
        gbc.insets = new Insets(2,2,2,2);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        
        //Categoria
        categoriaCheckBoxes = new ArrayList<>();
        for (Categoria c : Categoria.values()) {
            categoriaCheckBoxes.add(new JCheckBox(c.name()));
        }
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(creaSezioneFiltroAccordion("Categoria", categoriaCheckBoxes), gbc);
        row++;

        //Tipologia 
        tipologiaCheckBoxes = new ArrayList<>();
        for (Tipologia t : Tipologia.values()) {
            tipologiaCheckBoxes.add(new JCheckBox(t.name()));
        }
        gbc.gridy = row;
        panel.add(creaSezioneFiltroAccordion("Tipologia", tipologiaCheckBoxes), gbc);
        row++;

        //Taglia 
        tagliaCheckBoxes = new ArrayList<>();
        for (Taglia t : Taglia.values()) {
        	JCheckBox cb = new JCheckBox(t.toString());
            cb.setActionCommand(t.name());  
            tagliaCheckBoxes.add(cb);
        }
        gbc.gridy = row;
        panel.add(creaSezioneFiltroAccordion("Taglia", tagliaCheckBoxes), gbc);
        row++;

        //Colore
        coloreCheckBoxes = new ArrayList<>();
        for (Colore c : Colore.values()) {
            coloreCheckBoxes.add(new JCheckBox(c.name()));
        }
        gbc.gridy = row;
        panel.add(creaSezioneFiltroAccordion("Colore", coloreCheckBoxes), gbc);
        row++;

        gbc.gridwidth = 1;

        //Quantità Min
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Quantità Min:"), gbc);
        quantitaMinField = new JTextField(8);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(quantitaMinField, gbc);
        row++;

        // Quantità Max
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Quantità Max:"), gbc);
        quantitaMaxField = new JTextField(8);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(quantitaMaxField, gbc);
        row++;

        // Prezzo Acquisto Min
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Prezzo Acquisto Min:"), gbc);
        prezzoAcquistoMinField = new JTextField(8);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(prezzoAcquistoMinField, gbc);
        row++;

        // Prezzo Acquisto Max
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Prezzo Acquisto Max:"), gbc);
        prezzoAcquistoMaxField = new JTextField(8);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(prezzoAcquistoMaxField, gbc);
        row++;

        // Prezzo Vendita Min
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Prezzo Vendita Min:"), gbc);
        prezzoVenditaMinField = new JTextField(8);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(prezzoVenditaMinField, gbc);
        row++;

        // Prezzo Vendita Max
        gbc.gridx = 0; gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Prezzo Vendita Max:"), gbc);
        prezzoVenditaMaxField = new JTextField(8);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(prezzoVenditaMaxField, gbc);
        row++;

        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridy = row + 1;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private JPanel creaPannelloOrdinamento() {
    	
    	//Pannello Ordinamento
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Ordinatori"));
        panel.setLayout(new BorderLayout(5,5));

        listaOrdinamentiModel = new DefaultListModel<>();
        listaOrdinamenti = new JList<>(listaOrdinamentiModel);
        listaOrdinamenti.setVisibleRowCount(8);
        JScrollPane scrollPane = new JScrollPane(listaOrdinamenti);

        JPanel controlloPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3,3,3,3);
        gbc.anchor = GridBagConstraints.WEST;

        //Campo
        gbc.gridx = 0; gbc.gridy = 0;
        controlloPanel.add(new JLabel("Campo:"), gbc);
        campoOrdinamentoBox = new JComboBox<>(OrdinatoreMultiplo.CampoOrdinamento.values());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlloPanel.add(campoOrdinamentoBox, gbc);

        //Direzione
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        controlloPanel.add(new JLabel("Direzione:"), gbc);
        direzioneBox = new JComboBox<>(OrdinatoreMultiplo.Direzione.values());
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlloPanel.add(direzioneBox, gbc);

        //Pannello e Bottoni
        JPanel pulsantiPanel = new JPanel(new FlowLayout());
        aggiungiOrdinamentoButton = new JButton("Aggiungi");
        rimuoviOrdinamentoButton = new JButton("Rimuovi Selezionato");
        
        pulsantiPanel.add(aggiungiOrdinamentoButton);
        pulsantiPanel.add(rimuoviOrdinamentoButton);

        aggiungiOrdinamentoButton.addActionListener(e -> {
            OrdinatoreMultiplo.CampoOrdinamento campo = (OrdinatoreMultiplo.CampoOrdinamento) campoOrdinamentoBox.getSelectedItem();
            OrdinatoreMultiplo.Direzione direzione = (OrdinatoreMultiplo.Direzione) direzioneBox.getSelectedItem();
            if (campo != null && direzione != null) {
                boolean duplicato = false;
                for (int i = 0; i < listaOrdinamentiModel.size(); i++) {
                    String elemento = listaOrdinamentiModel.get(i);
                    if (elemento.startsWith(campo.name() + " ")) {
                        duplicato = true;
                        break;
                    }
                }
                //Validazione Ordinatori
                if (duplicato) {
                    JOptionPane.showMessageDialog(this,
                        "Non puoi aggiungere più ordinamenti sullo stesso campo.",
                        "Errore ordinamento duplicato",
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    String elemento = campo.name() + " (" + direzione.name() + ")";
                    listaOrdinamentiModel.addElement(elemento);
                }
            }
        });

        rimuoviOrdinamentoButton.addActionListener(e -> {
            int index = listaOrdinamenti.getSelectedIndex();
            if (index != -1) {
                listaOrdinamentiModel.remove(index);
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(controlloPanel, BorderLayout.NORTH);
        panel.add(pulsantiPanel, BorderLayout.SOUTH);

        return panel;
    }

    //Metodi Caricamento
    private void loadFiltro() {
        caricaCheckboxSelezionate(categoriaCheckBoxes, filtro.getCategorie());
        caricaCheckboxSelezionate(tipologiaCheckBoxes, filtro.getTipologie());
        caricaCheckboxSelezionate(tagliaCheckBoxes, filtro.getTaglie());
        caricaCheckboxSelezionate(coloreCheckBoxes, filtro.getColori());

        quantitaMinField.setText(filtro.getQuantitaMin() != null ? filtro.getQuantitaMin().toString() : "");
        quantitaMaxField.setText(filtro.getQuantitaMax() != null ? filtro.getQuantitaMax().toString() : "");

        prezzoAcquistoMinField.setText(filtro.getPrezzoAcquistoMin() != null ? filtro.getPrezzoAcquistoMin().toString() : "");
        prezzoAcquistoMaxField.setText(filtro.getPrezzoAcquistoMax() != null ? filtro.getPrezzoAcquistoMax().toString() : "");

        prezzoVenditaMinField.setText(filtro.getPrezzoVenditaMin() != null ? filtro.getPrezzoVenditaMin().toString() : "");
        prezzoVenditaMaxField.setText(filtro.getPrezzoVenditaMax() != null ? filtro.getPrezzoVenditaMax().toString() : "");
    }

    private void loadOrdinatore() {
        listaOrdinamentiModel.clear();
        for (OrdinatoreMultiplo.CriterioOrdinamento criterio : ordinatore.getCriteri()) {
            String elemento = criterio.getCampo().name() + " (" + criterio.getDirezione().name() + ")";
            listaOrdinamentiModel.addElement(elemento);
        }
    }

    private <E extends Enum<E>> void caricaCheckboxSelezionate(List<JCheckBox> checkBoxes, Set<E> valoriSelezionati) {
        for (JCheckBox cb : checkBoxes) {
        	cb.setSelected(valoriSelezionati != null && valoriSelezionati.stream().anyMatch(v -> v.name().equals(cb.getActionCommand())));
        }
    }

    //Metodi Lettura
    private boolean leggiFiltroDaInput() {
        filtro.setCategorie(leggiCheckboxSelezionate(categoriaCheckBoxes, Categoria.class));
        filtro.setTipologie(leggiCheckboxSelezionate(tipologiaCheckBoxes, Tipologia.class));
        filtro.setTaglie(leggiCheckboxSelezionate(tagliaCheckBoxes, Taglia.class));
        filtro.setColori(leggiCheckboxSelezionate(coloreCheckBoxes, Colore.class));

        try {
            filtro.setQuantitaMin(parseInteger(quantitaMinField.getText()));
            filtro.setQuantitaMax(parseInteger(quantitaMaxField.getText()));

            filtro.setPrezzoAcquistoMin(parseDouble(prezzoAcquistoMinField.getText()));
            filtro.setPrezzoAcquistoMax(parseDouble(prezzoAcquistoMaxField.getText()));

            filtro.setPrezzoVenditaMin(parseDouble(prezzoVenditaMinField.getText()));
            filtro.setPrezzoVenditaMax(parseDouble(prezzoVenditaMaxField.getText()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valori numerici non validi nei campi quantità o prezzo.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private <E extends Enum<E>> Set<E> leggiCheckboxSelezionate(List<JCheckBox> checkBoxes, Class<E> enumClass) {
        Set<E> selezionati = new HashSet<>();
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                try {
                	E valore = Enum.valueOf(enumClass, cb.getActionCommand());
                    selezionati.add(valore);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return selezionati.isEmpty() ? null : selezionati;
    }

    private boolean leggiOrdinatoreDaInput() {
        ordinatore.getCriteri().clear();
        for (int i = 0; i < listaOrdinamentiModel.size(); i++) {
            String item = listaOrdinamentiModel.get(i);
            int idxPar = item.indexOf('(');
            if (idxPar == -1) continue;
            String campoStr = item.substring(0, idxPar).trim();
            String direzioneStr = item.substring(idxPar+1, item.length()-1).trim();

            OrdinatoreMultiplo.CampoOrdinamento campo;
            OrdinatoreMultiplo.Direzione direzione;
            try {
                campo = OrdinatoreMultiplo.CampoOrdinamento.valueOf(campoStr);
                direzione = OrdinatoreMultiplo.Direzione.valueOf(direzioneStr);
            } catch (IllegalArgumentException ex) {
                continue;
            }

            ordinatore.addCriterio(campo, direzione);
        }
        return true;
    }

    private Integer parseInteger(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return Integer.parseInt(s.trim());
    }

    private Double parseDouble(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return Double.parseDouble(s.trim());
    }

    //Metodi Reset 
    private void resetFiltro() {
        for (JCheckBox cb : categoriaCheckBoxes) cb.setSelected(false);
        for (JCheckBox cb : tipologiaCheckBoxes) cb.setSelected(false);
        for (JCheckBox cb : tagliaCheckBoxes) cb.setSelected(false);
        for (JCheckBox cb : coloreCheckBoxes) cb.setSelected(false);

        quantitaMinField.setText("");
        quantitaMaxField.setText("");
        prezzoAcquistoMinField.setText("");
        prezzoAcquistoMaxField.setText("");
        prezzoVenditaMinField.setText("");
        prezzoVenditaMaxField.setText("");

        filtro.setCategorie(null);
        filtro.setTipologie(null);
        filtro.setTaglie(null);
        filtro.setColori(null);
        filtro.setQuantitaMin(null);
        filtro.setQuantitaMax(null);
        filtro.setPrezzoAcquistoMin(null);
        filtro.setPrezzoAcquistoMax(null);
        filtro.setPrezzoVenditaMin(null);
        filtro.setPrezzoVenditaMax(null);
    }

    private void aggiornaComponentiOrdinamento() {
        listaOrdinamentiModel.clear();

        for (OrdinatoreMultiplo.CriterioOrdinamento c : ordinatore.getCriteri()) {
            String elemento = c.getCampo().name() + " (" + c.getDirezione().name() + ")";
            listaOrdinamentiModel.addElement(elemento);
        }

        if (campoOrdinamentoBox.getItemCount() > 0)
            campoOrdinamentoBox.setSelectedIndex(0);
        if (direzioneBox.getItemCount() > 0)
            direzioneBox.setSelectedIndex(0);
    }

    //Metodo Validazione Filtro
    private boolean validaFiltri() {
        try {
            Integer qMin = parseInteger(quantitaMinField.getText());
            Integer qMax = parseInteger(quantitaMaxField.getText());
            if (qMin != null && qMax != null && qMin > qMax) {
                JOptionPane.showMessageDialog(this, "Quantità min deve essere minore o uguale a quantità max.", "Errore filtro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Double paMin = parseDouble(prezzoAcquistoMinField.getText());
            Double paMax = parseDouble(prezzoAcquistoMaxField.getText());
            if (paMin != null && paMax != null && paMin > paMax) {
                JOptionPane.showMessageDialog(this, "Prezzo acquisto min deve essere minore o uguale a prezzo acquisto max.", "Errore filtro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Double pvMin = parseDouble(prezzoVenditaMinField.getText());
            Double pvMax = parseDouble(prezzoVenditaMaxField.getText());
            if (pvMin != null && pvMax != null && pvMin > pvMax) {
                JOptionPane.showMessageDialog(this, "Prezzo vendita min deve essere minore o uguale a prezzo vendita max.", "Errore filtro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valori numerici non validi.", "Errore filtro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    //Getter Conferma
    public boolean isConfermato() {
        return confermato; }
    
    //Getters Filtri e Ordinatori
    public FiltroMultiplo getFiltro() {
        return filtro; }

    public OrdinatoreMultiplo getOrdinatore() {
        return ordinatore; }
    
}
