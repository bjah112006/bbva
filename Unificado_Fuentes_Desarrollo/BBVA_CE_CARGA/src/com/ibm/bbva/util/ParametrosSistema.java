package com.ibm.bbva.util;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.ibm.as.core.exception.DataAccessException;
import com.ibm.as.core.exception.ObjectNotFoundException;
import com.ibm.bbva.tabla.dao.ParametrosConfDAO;
import com.ibm.bbva.tabla.dto.ParametrosConfDTO;


public class ParametrosSistema {

	public static final String LOG = "LOG";
	public static final String CONF = "CONF";
	public static final String CARGA = "CARGA";
	
	private static volatile ParametrosSistema instance = null;
	
	private HashMap<String, Properties> propiedades;
	
	private ParametrosSistema() {
		int codigoAplicativo = 1;
		String nombreVariable = "";
		
		ParametrosConfDAO dao = EJBLocator.getParametrosConfDAO();
		if (dao != null) {
			propiedades = new HashMap<String, Properties>();
			
			ParametrosConfDTO parametrosConf = new ParametrosConfDTO();
			try {
				parametrosConf = dao.findByVariable(codigoAplicativo, nombreVariable);
				
				if(parametrosConf != null){
					if (propiedades.get(parametrosConf.getCodigoAplicativo()) == null) {
						propiedades.put(parametrosConf.getCodigoAplicativo().toString(), new Properties());
					}
					propiedades.get(parametrosConf.getCodigoAplicativo()).put(parametrosConf.getNombreVariable(), parametrosConf.getValorVariable());
					
				}
				
			} catch (DataAccessException e) {
				// TODO Bloque catch generado automáticamente
				e.printStackTrace();
			} catch (ObjectNotFoundException e) {
				// TODO Bloque catch generado automáticamente
				e.printStackTrace();
			}
			
			
			/*
			List<ParametrosConf> list = dao.findAll();
			for (ParametrosConf param : list) {
				if (propiedades.get(param.getCodigoModulo()) == null) {
					propiedades.put(param.getCodigoModulo(), new Properties());
				}
				propiedades.get(param.getCodigoModulo()).put(param.getNombreVariable(), param.getValorVariable());
			}
			*/
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
