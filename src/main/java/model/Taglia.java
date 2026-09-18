package model;

public enum Taglia {
	T_0_1("0-1"),
	T_1_2("1-2"),
	T_2_3("2-3"),
	T_4_6("4-6"),
	T_7_9("7-9"),
	T_10_12("10-12"),
	T_12_18("12-18"),
	T_18_24("18-24"),
	T_24_36("24-36"),
	T_4("4"),
	T_6("6"),
	T_8("8"),
	T_10("10"),
	T_12("12"),
	T_14("14"),
	XS("XS"),
	S("S"),
	M("M"),
	L("L"),
	XL("XL"),
	XXL("XXL"),
	XXXL("XXXL");
	
	private final String label;

	//Costruttore
    Taglia(String label) {
        this.label = label;
    }    
        
    //Getter Etichetta          
    public String getLabel() {
    	return label;
    }

    //Metodo Visualizzazione 
    @Override
    public String toString() {
        return label;
    }
    
}

