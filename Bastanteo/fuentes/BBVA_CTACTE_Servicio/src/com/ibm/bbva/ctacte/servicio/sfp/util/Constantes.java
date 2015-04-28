package com.ibm.bbva.ctacte.servicio.sfp.util;

import java.util.Properties;

import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

public class Constantes {

	public static final String CODIGO_DOCUMENTO_FRDF = "FRFI0";
	public static final Integer ID_DOCUMENTO_FRDF = 3;
	public static final String CARPETA_FTP = "docsce";
	public static final String RUTA_DESCARGA_TIF = "c:\\temp";
	private static final Properties PROPIEDADES = ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF);
	//public static final String URL_CONVERTIR_ARCHIVOS = "http://118.180.34.15:9080/BBVA_ConvertFiles/services/ConvertirArchivos";
	public static final String URL_CONVERTIR_ARCHIVOS = "http://"+PROPIEDADES.getProperty(ConstantesParametros.SERVIDOR_CONV_PDF)+":"+
			PROPIEDADES.getProperty(ConstantesParametros.PUERTO_CONV_PDF)+"/BBVA_ConvertFiles/services/ConvertirArchivos";
}
