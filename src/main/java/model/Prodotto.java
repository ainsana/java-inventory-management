package model;

import java.util.Set;

public class Prodotto {
	
	//Variabili private Prodotto
    private int id;
    private String nome;
    private Categoria categoria;
    private Taglia taglia; 
    private Tipologia tipologia; 
    private Set<Colore> colori;
    private int quantita;
    private double prezzoAcquisto;
    private double prezzoVendita;
    
    //Costruttori
    
    // Costruttore Vuoto richiesto per Swing, CSV, deserializzazione 
    public Prodotto() {}
    
    // Costruttore completo
    public Prodotto(int id, String nome, Categoria categoria, Taglia taglia, Tipologia tipologia,
                    Set<Colore> colori, int quantita, double prezzoAcquisto, double prezzoVendita) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.taglia = taglia;
        this.tipologia = tipologia;
        this.colori = colori;
        this.quantita = quantita;
        this.prezzoAcquisto = prezzoAcquisto;
        this.prezzoVendita = prezzoVendita;
    }
        
    //Metodo Calcolo Margine
    public double getDifferenzaPrezzo() {
    	return prezzoVendita - prezzoAcquisto;
    }
    
    //Getters e Setters
    public int getId() {
    	return id; }
    public void setId(int id) {
    	this.id = id; }

    public String getNome() {
    	return nome; }
    public void setNome(String nome) {
    	this.nome = nome; }

    public Categoria getCategoria() {
    	return categoria; }
    public void setCategoria(Categoria categoria) {
    	this.categoria = categoria; }

    public Taglia getTaglia() {
    	return taglia; }
    public void setTaglia(Taglia taglia) {
    	this.taglia = taglia; }

    public Tipologia getTipologia() {
    	return tipologia; }
    public void setTipologia(Tipologia tipologia) {
    	this.tipologia = tipologia; }

    public Set<Colore> getColori() {
    	return colori; }
    public void setColori(Set<Colore> colori) {
    	this.colori = colori; }

    public int getQuantita() {
    	return quantita; }
    public void setQuantita(int quantita) {
    	this.quantita = quantita; }

    public double getPrezzoAcquisto() {
    	return prezzoAcquisto; }    
    public void setPrezzoAcquisto(double prezzoAcquisto) {
    	this.prezzoAcquisto = prezzoAcquisto; }

    public double getPrezzoVendita() {
    	return prezzoVendita; }    
    public void setPrezzoVendita(double prezzoVendita) { 
    	this.prezzoVendita = prezzoVendita; }
   
}

