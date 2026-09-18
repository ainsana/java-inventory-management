package util;

import model.Categoria;
import model.Tipologia;
import model.Taglia;

public class CompatibilitaValidator {

	//Metodi Validazione Compatibilità
    public static String validaCompatibilita(Categoria categoria, Tipologia tipologia, Taglia taglia) {        

        if (categoria == Categoria.MERCERIA) {
            if (tipologia == null && taglia == null) {
                return null;
            }
            if (tipologia != null) {
                if (taglia != null && !isTagliaCompatibileConTipologia(taglia, tipologia)) {
                    return "Taglia non compatibile con tipologia " + tipologia.name();
                }
                return null; 
            }
            if (taglia != null) {
                return "Se presente la taglia, deve essere presente anche la tipologia";
            }
        } else {
            if (tipologia == null) {
                return "La tipologia è obbligatoria per categoria " + categoria.name();
            }
            if (taglia == null) {
                return "La taglia è obbligatoria per categoria " + categoria.name();
            }
            if (!isTagliaCompatibileConTipologia(taglia, tipologia)) {
                return "Taglia non compatibile con tipologia " + tipologia.name();
            }
        }
        return null; 
    }

    private static boolean isTagliaCompatibileConTipologia(Taglia taglia, Tipologia tipologia) {
        switch (tipologia) {
            case NEONATO:
                return isTagliaNeonatoValida(taglia);
            case BAMBINO:
                return isTagliaBambinoValida(taglia);
            case UOMO:
            case DONNA:
                return isTagliaAdultoValida(taglia);
            default:
                return false;
        }
    }

    private static boolean isTagliaNeonatoValida(Taglia taglia) {
        switch (taglia) {
            case T_0_1:
            case T_1_2:
            case T_2_3:
            case T_4_6:
            case T_7_9:
            case T_10_12:
            case T_12_18:
            case T_18_24:
            case T_24_36:
                return true;
            default:
                return false;
        }
    }

    private static boolean isTagliaBambinoValida(Taglia taglia) {
        switch (taglia) {
            case T_4:
            case T_6:
            case T_8:
            case T_10:
            case T_12:
            case T_14:
                return true;
            default:
                return false;
        }
    }

    private static boolean isTagliaAdultoValida(Taglia taglia) {
        switch (taglia) {
            case XS:
            case S:
            case M:
            case L:
            case XL:
            case XXL:
            case XXXL:
                return true;
            default:
                return false;
        }
    }
    
}

