package com.ibm.mant.tabla.core.dao;


import com.ibm.mant.tabla.dao.ColumnaDAO;
import com.ibm.mant.tabla.dao.PosibleValorDAO;
import com.ibm.mant.tabla.dao.RegistroTablaDAO;
import com.ibm.mant.tabla.dao.TablaDAO;
import com.ibm.mant.tabla.dao.ValorForaneoDAO;
import com.ibm.mant.tabla.dao.ValorForaneoDataDAO;

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
	
}
