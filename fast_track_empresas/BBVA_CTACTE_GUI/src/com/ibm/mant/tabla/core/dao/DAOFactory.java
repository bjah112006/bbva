package com.ibm.mant.tabla.core.dao;


import com.ibm.mant.tabla.dao.ColumnaDAO;
import com.ibm.mant.tabla.dao.PosibleValorDAO;
import com.ibm.mant.tabla.dao.RegistroTablaDAO;
import com.ibm.mant.tabla.dao.TablaDAO;
import com.ibm.mant.tabla.dao.ValorForaneoDAO;
import com.ibm.mant.tabla.dao.ValorForaneoDataDAO;

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
	

}
