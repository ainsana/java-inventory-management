package util;

import model.*;

import java.util.HashSet;
import java.util.Set;

public class FiltroMultiplo {

	//Variabili private Filtri
    private Set<Categoria> categorie;
    private Set<Tipologia> tipologie;
    private Set<Taglia> taglie;
    private Set<Colore> colori;

    private Integer quantitaMin;
    private Integer quantitaMax;

    private Double prezzoAcquistoMin;
    private Double prezzoAcquistoMax;

    private Double prezzoVenditaMin;
    private Double prezzoVenditaMax;

    //Variabile public Filtro Multiplo
    public FiltroMultiplo() {
        categorie = new HashSet<>();
        tipologie = new HashSet<>();
        taglie = new HashSet<>();
        colori = new HashSet<>();
    }

    //Metodi Filtro
    public void clear() {
        if (categorie != null) categorie.clear();
        if (tipologie != null) tipologie.clear();
        if (taglie != null) taglie.clear();
        if (colori != null) colori.clear();

        quantitaMin = null;
        quantitaMax = null;
        prezzoAcquistoMin = null;
        prezzoAcquistoMax = null;
        prezzoVenditaMin = null;
        prezzoVenditaMax = null;
    }

    @Override
    public String toString() {
        return "FiltroMultiplo{" +
                "categorie=" + categorie +
                ", tipologie=" + tipologie +
                ", taglie=" + taglie +
                ", colori=" + colori +
                ", quantitaMin=" + quantitaMin +
                ", quantitaMax=" + quantitaMax +
                ", prezzoAcquistoMin=" + prezzoAcquistoMin +
                ", prezzoAcquistoMax=" + prezzoAcquistoMax +
                ", prezzoVenditaMin=" + prezzoVenditaMin +
                ", prezzoVenditaMax=" + prezzoVenditaMax +
                '}';
    }
    
    public boolean test(Prodotto p) {
        if (categorie != null && !categorie.isEmpty() && !categorie.contains(p.getCategoria())) {
            return false;
        }
        
        if (tipologie != null && !tipologie.isEmpty() && !tipologie.contains(p.getTipologia())) {
            return false;
        }
        
        if (taglie != null && !taglie.isEmpty() && !taglie.contains(p.getTaglia())) {
            return false;
        }
        
        if (colori != null && !colori.isEmpty()) {
            Set<Colore> coloriProdotto = p.getColori();
            boolean haColore = false;
            if (coloriProdotto != null) {
                for (Colore c : coloriProdotto) {
                    if (colori.contains(c)) {
                        haColore = true;
                        break;
                    }
                }
            }
            if (!haColore) {
                return false;
            }
        }
        
        if (quantitaMin != null && p.getQuantita() < quantitaMin) {
            return false;
        }
        
        if (quantitaMax != null && p.getQuantita() > quantitaMax) {
            return false;
        }
        
        if (prezzoAcquistoMin != null && p.getPrezzoAcquisto() < prezzoAcquistoMin) {
            return false;
        }
        
        if (prezzoAcquistoMax != null && p.getPrezzoAcquisto() > prezzoAcquistoMax) {
            return false;
        }
        
        if (prezzoVenditaMin != null && p.getPrezzoVendita() < prezzoVenditaMin) {
            return false;
        }
        
        if (prezzoVenditaMax != null && p.getPrezzoVendita() > prezzoVenditaMax) {
            return false;
        }
        
        return true;
    }
    
    // Getters e Setters
    public Set<Categoria> getCategorie() {
        return categorie; }
    public void setCategorie(Set<Categoria> categorie) {
        this.categorie = categorie; }

    public Set<Tipologia> getTipologie() {
        return tipologie; }
    public void setTipologie(Set<Tipologia> tipologie) {
        this.tipologie = tipologie; }

    public Set<Taglia> getTaglie() {
        return taglie; }
    public void setTaglie(Set<Taglia> taglie) {
        this.taglie = taglie; }

    public Set<Colore> getColori() {
        return colori;}
    public void setColori(Set<Colore> colori) {
        this.colori = colori; }

    public Integer getQuantitaMin() {
        return quantitaMin; }
    public void setQuantitaMin(Integer quantitaMin) {
        this.quantitaMin = quantitaMin; }

    public Integer getQuantitaMax() {
        return quantitaMax; }
    public void setQuantitaMax(Integer quantitaMax) {
        this.quantitaMax = quantitaMax; }

    public Double getPrezzoAcquistoMin() {
        return prezzoAcquistoMin; }
    public void setPrezzoAcquistoMin(Double prezzoAcquistoMin) {
        this.prezzoAcquistoMin = prezzoAcquistoMin; }

    public Double getPrezzoAcquistoMax() {
        return prezzoAcquistoMax; }
    public void setPrezzoAcquistoMax(Double prezzoAcquistoMax) {
        this.prezzoAcquistoMax = prezzoAcquistoMax; }

    public Double getPrezzoVenditaMin() {
        return prezzoVenditaMin; }
    public void setPrezzoVenditaMin(Double prezzoVenditaMin) {
        this.prezzoVenditaMin = prezzoVenditaMin; }

    public Double getPrezzoVenditaMax() {
        return prezzoVenditaMax; }
    public void setPrezzoVenditaMax(Double prezzoVenditaMax) {
        this.prezzoVenditaMax = prezzoVenditaMax; }

}


