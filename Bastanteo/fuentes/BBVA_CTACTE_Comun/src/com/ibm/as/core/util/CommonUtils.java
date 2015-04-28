package com.ibm.as.core.util;


public class CommonUtils {
	
	/**
	 * 	Verifica que una variable es numerica
	 * @param idTabla
	 * @return
	 * @throws TablaException
	 */
	public CommonUtils(){
		new CommonUtils();
	}
    public static boolean isNumeric(String cadena){
        try {
        Integer.parseInt(cadena);
        return true;
        } catch (NumberFormatException nfe){
        return false;
        }
        }
}
