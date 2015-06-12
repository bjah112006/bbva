package com.ibm.bbva.tabla.core.dao;


import com.ibm.bbva.tabla.dao.ColumnaDAO;
import com.ibm.bbva.tabla.dao.ColumnaDependenciaDAO;
import com.ibm.bbva.tabla.dao.DatosGeneradosConsDAO;
import com.ibm.bbva.tabla.dao.DatosGeneradosHisDAO;
import com.ibm.bbva.tabla.dao.ParametrosConfDAO;
import com.ibm.bbva.tabla.dao.PosibleValorDAO;
import com.ibm.bbva.tabla.dao.RegistroTablaDAO;
import com.ibm.bbva.tabla.dao.TablaDAO;
import com.ibm.bbva.tabla.dao.ValorForaneoDAO;
import com.ibm.bbva.tabla.dao.ValorForaneoDataDAO;
import com.ibm.bbva.tabla.reporte.dao.DatosGeneradosDAO;
import com.ibm.bbva.tabla.reporte.dao.DatosPorcentajeDAO;
import com.ibm.bbva.tabla.reporte.dao.DatosUnidadDAO;

public abstract class DAOFactory {

	public enum DAOFactoryType {
		DEFAULT, ORACLE, DB2, DERBY
	};

	public static DAOFactory getDefaultDAOFactory() {
		return new DefaultDAOFactory();
	}

	public static DAOFactory getDAOFactory(DAOFactoryType type) {
		if (DAOFactoryType.DEFAULT.equals(type)) {
			return getDefaultDAOFactory();
		}

		throw new RuntimeException(
				"The method getDAOFactory() is not yet implemented");
	}

	public abstract TablaDAO getTablaDAO();
	
	public abstract ColumnaDAO getColumnaDAO();
	
	public abstract RegistroTablaDAO getRegistroTablaDAO();
	
	public abstract PosibleValorDAO getPosibleValorDAO();

	public abstract ValorForaneoDAO getValorForaneoDAO();
	
	public abstract ValorForaneoDataDAO getValorForaneoDataDAO();
	
	public abstract ColumnaDependenciaDAO getColumnaDependenciaDAO();
	
	public abstract ParametrosConfDAO getParametrosConfDAO();
	
	public abstract DatosGeneradosDAO getDatosGeneradosDAO();
	
	public abstract DatosPorcentajeDAO getDatosPorcentajeDAO();	
	
	public abstract DatosUnidadDAO getDatosUnidadDAO();
	
	public abstract DatosGeneradosConsDAO getDatosGeneradosConsDAO();
	
	public abstract DatosGeneradosHisDAO getDatosGeneradosHisDAO();	
}