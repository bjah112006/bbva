package com.ibm.bbva.tabla.core.dao;

import com.ibm.bbva.tabla.dao.ColumnaDAO;
import com.ibm.bbva.tabla.dao.ColumnaDependenciaDAO;
import com.ibm.bbva.tabla.dao.DatosGeneradosConsDAO;
import com.ibm.bbva.tabla.dao.DatosGeneradosHisDAO;
import com.ibm.bbva.tabla.dao.DatosHistAntiguoDAO;
import com.ibm.bbva.tabla.dao.ParametrosConfDAO;
import com.ibm.bbva.tabla.dao.PosibleValorDAO;
import com.ibm.bbva.tabla.dao.RegistroTablaDAO;
import com.ibm.bbva.tabla.dao.TablaDAO;
import com.ibm.bbva.tabla.dao.ValorForaneoDAO;
import com.ibm.bbva.tabla.dao.ValorForaneoDataDAO;
import com.ibm.bbva.tabla.reporte.dao.DatosGeneradosDAO;
import com.ibm.bbva.tabla.reporte.dao.DatosPorcentajeDAO;
import com.ibm.bbva.tabla.reporte.dao.DatosUnidadDAO;

public class DefaultDAOFactory extends DAOFactory {

	

	public TablaDAO getTablaDAO() {
		return TablaDAO.getInstance();
	}

	
	public ColumnaDAO getColumnaDAO() {
		return ColumnaDAO.getInstance();
	}


	public RegistroTablaDAO getRegistroTablaDAO() {
		return RegistroTablaDAO.getInstance();
	}


	public PosibleValorDAO getPosibleValorDAO() {
		return PosibleValorDAO.getInstance();
	}


	public ValorForaneoDAO getValorForaneoDAO() {
		return ValorForaneoDAO.getInstance();
	}


	public ValorForaneoDataDAO getValorForaneoDataDAO() {
		return ValorForaneoDataDAO.getInstance();
	}
	
	public ColumnaDependenciaDAO getColumnaDependenciaDAO() {
		return ColumnaDependenciaDAO.getInstance();
	}
	
	public DatosGeneradosDAO getDatosGeneradosDAO() {
		return DatosGeneradosDAO.getInstance();
	}
	
	public DatosPorcentajeDAO getDatosPorcentajeDAO() {
		return DatosPorcentajeDAO.getInstance();
	}	
	
	public DatosUnidadDAO getDatosUnidadDAO() {
		return DatosUnidadDAO.getInstance();
	}	

	public ParametrosConfDAO getParametrosConfDAO() {
		return ParametrosConfDAO.getInstance();
	}

	public DatosGeneradosConsDAO getDatosGeneradosConsDAO() {
		return DatosGeneradosConsDAO.getInstance();
	}
	
	public DatosGeneradosHisDAO getDatosGeneradosHisDAO() {
		return DatosGeneradosHisDAO.getInstance();
	}
	
	/*FIX ERIKA ABREGU 27/06/2015
	 * */
	public DatosHistAntiguoDAO getDatosHistAntiguoDAO() {
		return DatosHistAntiguoDAO.getInstance();
	}
	
}