package com.ibm.bbva.util.cvs;

public class FormatoFactory {

    private FormatoCSV[] formatos;

    public FormatoFactory(int columnas) {
        formatos = new FormatoCSV[columnas];
        for (int i=0; i<columnas; i++) {
            formatos[i] = FormatoDefault.getInstance();
        }
    }

    public void addFormato (FormatoCSV formato, int[] indices) {
        for (int i=0, n=indices.length; i<n; i++) {
            formatos[indices[i]] = formato;
        }
    }

    public String formato (int columna, Object valor) {
        return formatos[columna].formato(valor);
    }

}
