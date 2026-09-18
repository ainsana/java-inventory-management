package view;

import javax.swing.*;
import java.awt.*;

public class DBDialog extends JDialog {
   
	//Variabili private
	private JTextField nomeDBField = new JTextField();
    private JButton okButton = new JButton("Crea");
    private String nomeDB;

    //Costruttore
    public DBDialog(JFrame parent) {
        super(parent, "Crea Database", true);
        setLayout(new BorderLayout());
        setSize(300, 120);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Inserisci nome del nuovo database:"));
        panel.add(nomeDBField);
        add(panel, BorderLayout.CENTER);
        add(okButton, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            nomeDB = nomeDBField.getText().trim();
            if (!nomeDB.isEmpty()) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Inserire un nome valido.");
            }
        });
    }

    //Getter Nome DB
    public String getNomeDB() {
        return nomeDB;
    }
    
}

