package view;

import controller.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginView extends JFrame {
	
	//Variabili private
	private JTextField hostField = new JTextField("localhost");
    private JTextField userField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JTextField dbField = new JTextField();
    private JButton loginButton = new JButton("Connetti");
    private JTextArea esitoArea = new JTextArea(3, 30);
    private LoginController controller;
    private Runnable onLoginSuccess;

    //Costruttore
    public LoginView() {
        setTitle("Connessione database");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());
        esitoArea.setEditable(false);     
        esitoArea.setFocusable(false);    
        esitoArea.setOpaque(false);       

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Host:"));
        inputPanel.add(hostField);
        inputPanel.add(new JLabel("Utente MySQL:"));
        inputPanel.add(userField);
        inputPanel.add(new JLabel("Password MySQL:"));
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("Database (opzionale):"));
        inputPanel.add(dbField);

        add(inputPanel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);
        add(new JScrollPane(esitoArea), BorderLayout.NORTH);
        
        loginButton.addActionListener((ActionEvent e) -> {
            controller.login(
                hostField.getText(),
                userField.getText(),
                new String(passwordField.getPassword()),
                dbField.getText()
            );
        });
    }

    //Metodi Visualizzazione Messaggio
    public void mostraEsito(String msg) {
        esitoArea.setText(msg);
    }

    public void mostraErrore(String msg) {
        esitoArea.setText(msg);
    }
    
    //Setter Controller Login
    public void setController(LoginController controller) {
        this.controller = controller;
    }

    //Getter e Setter Successo Login
    public Runnable getOnLoginSuccess() {
        return onLoginSuccess; }
    public void setOnLoginSuccess(Runnable callback) {
        this.onLoginSuccess = callback; }    
    
}
