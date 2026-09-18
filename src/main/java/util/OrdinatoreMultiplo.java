package util;

import model.*;

import java.util.*;
import java.util.function.Function;

public class OrdinatoreMultiplo implements Comparator<Prodotto> {

    public enum CampoOrdinamento {
        ID,
        NOME,
        CATEGORIA,
        TIPOLOGIA,
        TAGLIA,
        COLORE,
        QUANTITA,
        PREZZO_ACQUISTO,
        PREZZO_VENDITA
    }

    public enum Direzione {
        CRESCENTE,
        DECRESCENTE
    }

    //Variabile private e final Lista Criteri
    private final List<CriterioOrdinamento> criteri = new ArrayList<>();

    //Classe public static Criterio
    public static class CriterioOrdinamento {
        private final CampoOrdinamento campo;
        private final Direzione direzione;
        public CriterioOrdinamento(CampoOrdinamento campo, Direzione direzione) {
            this.campo = campo;
            this.direzione = direzione;
        }

        public CampoOrdinamento getCampo() { return campo; }
        public Direzione getDirezione() { return direzione; }
    }

    //Metodi Criteri
    public void addCriterio(CampoOrdinamento campo, Direzione direzione) {
        criteri.add(new CriterioOrdinamento(campo, direzione));
    }
    
    public boolean isEmpty() {
        return criteri.isEmpty();
    }

    //Metodi Confronto
    @Override
    public int compare(Prodotto p1, Prodotto p2) {
        for (CriterioOrdinamento c : criteri) {
            int cmp = confrontaPerCampo(p1, p2, c.getCampo());
            if (c.getDirezione() == Direzione.DECRESCENTE) {
                cmp = -cmp;
            }
            if (cmp != 0) {
                return cmp;
            }
        }
        return 0;
    }

    private int confrontaPerCampo(Prodotto p1, Prodotto p2, CampoOrdinamento campo) {
        switch (campo) {
            case ID:
                return Integer.compare(p1.getId(), p2.getId());

            case NOME:
                return p1.getNome().compareToIgnoreCase(p2.getNome());

            case CATEGORIA:
                return p1.getCategoria().compareTo(p2.getCategoria());

            case TIPOLOGIA:
                if (p1.getTipologia() == null && p2.getTipologia() == null) return 0;
                if (p1.getTipologia() == null) return -1;
                if (p2.getTipologia() == null) return 1;
                return p1.getTipologia().compareTo(p2.getTipologia());

            case TAGLIA:
                return comparaTaglia(p1.getTaglia(), p2.getTaglia());

            case COLORE:
                String colore1 = p1.getColori() == null || p1.getColori().isEmpty() ? "" :
                        p1.getColori().stream().map(Enum::name).min(String::compareTo).orElse("");
                String colore2 = p2.getColori() == null || p2.getColori().isEmpty() ? "" :
                        p2.getColori().stream().map(Enum::name).min(String::compareTo).orElse("");
                return colore1.compareTo(colore2);

            case QUANTITA:
                return Integer.compare(p1.getQuantita(), p2.getQuantita());

            case PREZZO_ACQUISTO:
                return Double.compare(p1.getPrezzoAcquisto(), p2.getPrezzoAcquisto());

            case PREZZO_VENDITA:
                return Double.compare(p1.getPrezzoVendita(), p2.getPrezzoVendita());

            default:
                return 0;
        }
    }

    private int comparaTaglia(Taglia t1, Taglia t2) {
        if (t1 == t2) return 0;
        if (t1 == null) return -1;
        if (t2 == null) return 1;
        List<Taglia> ordineTaglie = List.of(
            Taglia.T_0_1, Taglia.T_1_2, Taglia.T_2_3, Taglia.T_4_6, Taglia.T_7_9, Taglia.T_10_12,
            Taglia.T_12_18, Taglia.T_18_24, Taglia.T_24_36,
            Taglia.T_4, Taglia.T_6, Taglia.T_8, Taglia.T_10, Taglia.T_12, Taglia.T_14,
            Taglia.XS, Taglia.S, Taglia.M, Taglia.L, Taglia.XL, Taglia.XXL, Taglia.XXXL
        );
        int index1 = ordineTaglie.indexOf(t1);
        int index2 = ordineTaglie.indexOf(t2);
        if (index1 == -1 && index2 == -1) return t1.name().compareTo(t2.name());
        if (index1 == -1) return 1;
        if (index2 == -1) return -1;
        return Integer.compare(index1, index2);
    }

    //Getter Lista Criteri
    public List<CriterioOrdinamento> getCriteri() {
        return criteri;
    }    
    
}
