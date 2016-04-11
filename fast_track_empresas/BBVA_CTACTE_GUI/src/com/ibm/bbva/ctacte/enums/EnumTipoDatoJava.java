package com.ibm.bbva.ctacte.enums;

public enum EnumTipoDatoJava {
    Long("java.lang.Long"),
    BigDecimal("java.math.BigDecimal"),
    String("java.lang.String"),
    Date("java.util.Date"),
    Integer("java.lang.Integer");
    
    private String tipoDato;

    private EnumTipoDatoJava(String tipo) {
        this.tipoDato = tipo;
    }

    public String getTipoDato() {
        return this.tipoDato;
    }
    public void ddd(){
        Long dd=0L;
        
    }
}
