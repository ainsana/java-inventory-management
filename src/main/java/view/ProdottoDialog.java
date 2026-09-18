package view;

import model.*;
import util.CompatibilitaValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class ProdottoDialog extends JDialog {

	//Variabili private final
	private final JTextField nomeField = new JTextField();
    private final JComboBox<Categoria> categoriaBox = new JComboBox<>(Categoria.values());
    private final JComboBox<Tipologia> tipologiaBox = new JComboBox<>();
    private final JComboBox<Taglia> tagliaBox = new JComboBox<>();
    private final Map<Colore, JCheckBox> coloreCheckBoxes = new LinkedHashMap<>();
    private final JTextField quantitaField = new JTextField();
    private final JTextField prezzoAcquistoField = new JTextField();
    private final JTextField prezzoVenditaField = new JTextField();
    private final JButton okButton = new JButton("OK");
    
    //Variabile Prodotto Inserito
    private Prodotto prodottoInserito;

    //Costruttori
    
    //Costruttore Vuoto
    public ProdottoDialog(JFrame parent) {
        this(parent, null);
    }

    //Costruttore Completo
    public ProdottoDialog(JFrame parent, Prodotto prodottoPreesistente) {
        super(parent, (prodottoPreesistente == null ? "Nuovo Prodotto" : "Modifica Prodotto"), true);
        setSize(450, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        tipologiaBox.addItem(null);
        for (Tipologia t : Tipologia.values()) {
            tipologiaBox.addItem(t);
        }

        tagliaBox.addItem(null);
        for (Taglia t : Taglia.values()) {
            tagliaBox.addItem(t);
        }

        //Panel Input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int row = 0;

        //Nome
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        inputPanel.add(nomeField, gbc);

        //Categoria
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        inputPanel.add(categoriaBox, gbc);

        //Tipologia
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(new JLabel("Tipologia:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        inputPanel.add(tipologiaBox, gbc);

        //Taglia
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(new JLabel("Taglia:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        inputPanel.add(tagliaBox, gbc);

        //Colori - Checkbox Multiplo
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        inputPanel.add(new JLabel("Colori (seleziona uno o più):"), gbc);
        row++;
        JPanel coloriPanel = new JPanel(new GridLayout(0, 3));
        for (Colore c : Colore.values()) {
            JCheckBox checkBox = new JCheckBox(c.toString());
            coloreCheckBoxes.put(c, checkBox);
            coloriPanel.add(checkBox);
        }
        
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        JScrollPane scrollPane = new JScrollPane(coloriPanel);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        inputPanel.add(scrollPane, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        //Quantità
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Quantità:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        inputPanel.add(quantitaField, gbc);

        //Prezzo Acquisto
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(new JLabel("Prezzo Acquisto:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        inputPanel.add(prezzoAcquistoField, gbc);

        //Prezzo Vendita
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(new JLabel("Prezzo Vendita:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = row++;
        inputPanel.add(prezzoVenditaField, gbc);

        add(inputPanel, BorderLayout.CENTER);
        add(okButton, BorderLayout.SOUTH);
        
        if (prodottoPreesistente != null) {
            nomeField.setText(prodottoPreesistente.getNome());
            categoriaBox.setSelectedItem(prodottoPreesistente.getCategoria());
            tipologiaBox.setSelectedItem(prodottoPreesistente.getTipologia());
            tagliaBox.setSelectedItem(prodottoPreesistente.getTaglia());       
            Set<Colore> coloriProdotto = prodottoPreesistente.getColori();
            if (coloriProdotto != null) {
                for (Colore c : coloreCheckBoxes.keySet()) {
                    coloreCheckBoxes.get(c).setSelected(coloriProdotto.contains(c));
                }
            }

            quantitaField.setText(String.valueOf(prodottoPreesistente.getQuantita()));
            prezzoAcquistoField.setText(String.valueOf(prodottoPreesistente.getPrezzoAcquisto()));
            prezzoVenditaField.setText(String.valueOf(prodottoPreesistente.getPrezzoVendita()));
        }

        okButton.addActionListener((ActionEvent e) -> {
            if (validaInput()) {
                try {
                    Prodotto p = new Prodotto();
                    p.setNome(nomeField.getText().trim());                    
                    p.setCategoria((Categoria) categoriaBox.getSelectedItem());
                    Tipologia tip = (Tipologia) tipologiaBox.getSelectedItem();
                    p.setTipologia(tip); 
                    Taglia tag = (Taglia) tagliaBox.getSelectedItem();
                    p.setTaglia(tag); 
                    Set<Colore> coloriSelezionati = new HashSet<>();
                    for (Map.Entry<Colore, JCheckBox> entry : coloreCheckBoxes.entrySet()) {
                        if (entry.getValue().isSelected()) {
                            coloriSelezionati.add(entry.getKey());
                        }
                    }
                    
                    p.setColori(coloriSelezionati);
                    p.setQuantita(Integer.parseInt(quantitaField.getText().trim()));
                    p.setPrezzoAcquisto(Double.parseDouble(prezzoAcquistoField.getText().trim()));
                    p.setPrezzoVendita(Double.parseDouble(prezzoVenditaField.getText().trim()));
                    this.prodottoInserito = p;
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Errore nei campi numerici (quantità/prezzi).",
                            "Errore di validazione",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Errore nei dati inseriti: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    //Metodo Validazione Input
    private boolean validaInput() {
        String nome = nomeField.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il nome è obbligatorio.", "Errore", JOptionPane.ERROR_MESSAGE);
            nomeField.requestFocus();
            return false;
        }

        boolean almenoUnColoreSelezionato = false;
        for (JCheckBox checkBox : coloreCheckBoxes.values()) {
            if (checkBox.isSelected()) {
                almenoUnColoreSelezionato = true;
                break;
            }
        }
        if (!almenoUnColoreSelezionato) {
            JOptionPane.showMessageDialog(this, "Seleziona almeno un colore.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int quantita;
        try {
            quantita = Integer.parseInt(quantitaField.getText().trim());
            if (quantita < 0) {
                JOptionPane.showMessageDialog(this, "La quantità deve essere >= 0.", "Errore", JOptionPane.ERROR_MESSAGE);
                quantitaField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La quantità non è un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            quantitaField.requestFocus();
            return false;
        }

        double prezzoAcquisto;
        try {
            prezzoAcquisto = Double.parseDouble(prezzoAcquistoField.getText().trim());
            if (prezzoAcquisto < 0) {
                JOptionPane.showMessageDialog(this, "Il prezzo di acquisto deve essere >= 0.", "Errore", JOptionPane.ERROR_MESSAGE);
                prezzoAcquistoField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Prezzo di acquisto non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            prezzoAcquistoField.requestFocus();
            return false;
        }

        double prezzoVendita;
        try {
            prezzoVendita = Double.parseDouble(prezzoVenditaField.getText().trim());
            if (prezzoVendita < 0) {
                JOptionPane.showMessageDialog(this, "Il prezzo di vendita deve essere >= 0.", "Errore", JOptionPane.ERROR_MESSAGE);
                prezzoVenditaField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Prezzo di vendita non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            prezzoVenditaField.requestFocus();
            return false;
        }

        // Compatibilità categoria-tipologia-taglia
        Categoria categoria = (Categoria) categoriaBox.getSelectedItem();
        Tipologia tipologia = (Tipologia) tipologiaBox.getSelectedItem(); 
        Taglia taglia = (Taglia) tagliaBox.getSelectedItem();           
        String errore = CompatibilitaValidator.validaCompatibilita(categoria, tipologia, taglia);
        if (errore != null) {
            JOptionPane.showMessageDialog(this, errore, "Errore validazione", JOptionPane.ERROR_MESSAGE);
            categoriaBox.requestFocus();
            return false;
        }
        return true;
    }

    //Getter Prodotto Inserito
    public Prodotto getProdottoInserito() {
        return prodottoInserito;
    }
    
}



