package view;

import javax.swing.*;

public class TabellaDialog extends JDialog {
    
	//Variabili private
	private JTextField nomeTabellaField = new JTextField(20);
    private JButton confermaButton = new JButton("Conferma");
    private String nomeTabella;

    //Costruttore
    public TabellaDialog(JFrame parent) {
        super(parent, "Nuova Tabella", true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(new JLabel("Nome tabella:"));
        add(nomeTabellaField);
        add(confermaButton);

        confermaButton.addActionListener(e -> {
            nomeTabella = nomeTabellaField.getText().trim();
            if (!nomeTabella.isEmpty()) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Inserire un nome valido.");
            }            
        });                               
        pack();
        setLocationRelativeTo(parent);
    }

    //Getter Nome Tabella
    public String getNomeTabella() {
        return nomeTabella;
    }
    
}