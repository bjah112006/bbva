package com.ibm.bbva.ctacte.util;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.ibm.bbva.ctacte.bean.ParametrosConf;
import com.ibm.bbva.ctacte.dao.ParametrosConfDAO;

public class ParametrosSistema {

	public static final String LOG = "LOG";
	public static final String CONF = "CONF";
	public static final String CARGA = "CARGA";
	
	private static volatile ParametrosSistema instance = null;
	
	private HashMap<String, Properties> propiedades;
	
	private ParametrosSistema() {
		ParametrosConfDAO dao = EJBLocator.getParametrosConfDAO();
		if (dao != null) {
			propiedades = new HashMap<String, Properties>();
			List<ParametrosConf> list = dao.findAll();
			for (ParametrosConf param : list) {
				if (propiedades.get(param.getCodigoModulo()) == null) {
					propiedades.put(param.getCodigoModulo(), new Properties());
				}
				propiedades.get(param.getCodigoModulo()).put(param.getNombreVariable(), param.getValorVariable());
			}
		} else {
			System.err.println("**************************************");
			System.err.println("ERROR AL CARGAR PARAMETROS DEL SISTEMA");
			System.err.println("**************************************");
		}
	}
	
	public static ParametrosSistema getInstance() {
		if (instance == null) {
			synchronized (ParametrosSistema.class) {
				if (instance == null) {
					instance = new ParametrosSistema();
				}
			}
		}
		return instance;
	}
	
	public Properties getProperties (String recurso) {
		return propiedades.get(recurso);
	}
}
