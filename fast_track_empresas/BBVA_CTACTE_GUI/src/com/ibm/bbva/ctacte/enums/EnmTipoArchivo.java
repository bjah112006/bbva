package com.ibm.bbva.ctacte.enums;

public enum EnmTipoArchivo {
    PDF("PDF"),
    Excel("Excel");
    
    private String tipoArchivo;

    private EnmTipoArchivo(String tipo) {
        this.tipoArchivo = tipo;
    }

    public String getTipoArchivo() {
        return this.tipoArchivo;
    }
}
