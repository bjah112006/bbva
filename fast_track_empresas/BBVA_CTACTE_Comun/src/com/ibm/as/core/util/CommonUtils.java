package com.ibm.as.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
	
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Verifica que una variable es numerica
	 * 
	 * @param idTabla
	 * @return
	 * @throws TablaException
	 */
	public CommonUtils() {
		new CommonUtils();
	}

	public static boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	public static boolean isEmail(String cadena) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(cadena);
		return matcher.matches();
	}
	
}
