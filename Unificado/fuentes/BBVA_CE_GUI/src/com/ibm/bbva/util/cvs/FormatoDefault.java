package com.ibm.bbva.util.cvs;

public class FormatoDefault implements FormatoCSV {

    private static FormatoDefault formatoDefault;

    private FormatoDefault (){}

    public static synchronized FormatoDefault getInstance () {
        if (formatoDefault==null) {
            formatoDefault = new FormatoDefault();
        }
        return formatoDefault;
    }

    public String formato(Object valor) {
        if (valor == null) {
            return "";
        } else {
            return valor.toString();
        }
    }
}