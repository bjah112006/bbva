package com.ibm.bbva.ctacte.enums;

public enum EnumTextAlign {
    TextAlign_Right("Right"),
    TextAlign_Left("Left"),
    TextAlign_Center("Center");
    
    private String tipoPosicionamiento;

    private EnumTextAlign(String tipo) {
        this.tipoPosicionamiento = tipo;
    }

    public String getTipoPosicionamiento() {
        return this.tipoPosicionamiento;
    }
}
