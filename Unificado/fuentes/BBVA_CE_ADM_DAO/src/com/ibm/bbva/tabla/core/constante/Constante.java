/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibm.bbva.tabla.core.constante;

public enum Constante {

    CODIGO_ERROR_DAO("DAO"),
    CODIGO_ERROR_BO("BO"),
    CODIGO_ERROR_SERVICE("SR"),
    CODIGO_ERROR_FACADE("FA"),
    CODIGO_ERROR_MB("MB"),
    CODIGO_ERROR_GENERAL("GEN"),
    ORACLE_TYPES_DOUBLE(8),
    ORACLE_TYPES_CURSOR(-10),
    ORACLE_TYPES_CHAR(1),
    ORACLE_TYPES_INTEGER(4),
    ORACLE_TYPES_DATE(91);


    private String value;
    private String[] valueArray;
    private int valueInt;

    private Constante(String valor) {
        value = valor;
    }

    private Constante(String... valor) {
        valueArray = valor;
    }

    private Constante(int valor) {
        valueInt = valor;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the valueArray
     */
    public String[] getValueArray() {
        return valueArray;
    }

    /**
     * @return the valueInt
     */
    public int getValueInt() {
        return valueInt;
    } 
}
