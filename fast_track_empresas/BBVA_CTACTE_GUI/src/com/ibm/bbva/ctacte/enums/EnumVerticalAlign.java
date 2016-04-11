package com.ibm.bbva.ctacte.enums;

public enum EnumVerticalAlign {
    
        VerticalAlign_Top("Top"),
        VerticalAlign_Bottom("Bottom"),
        VerticalAlign_Middle("Middle");
       
    private String tipoPosicionamiento;

    private EnumVerticalAlign(String tipo) {
        this.tipoPosicionamiento = tipo;
    }

    public String getTipoPosicionamiento() {
        return this.tipoPosicionamiento;
    }
   

}
