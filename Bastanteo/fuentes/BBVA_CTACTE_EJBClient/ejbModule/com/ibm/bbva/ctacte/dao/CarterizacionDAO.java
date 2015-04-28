package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Carterizacion;
import com.ibm.bbva.ctacte.bean.CarterizacionId;
import com.ibm.bbva.ctacte.bean.Empleado;

public interface CarterizacionDAO extends IGenericDAO<Carterizacion, CarterizacionId> {
	public List<Carterizacion> buscarTodos();
	public List<String> obtenerListaCodigos();
	public List<Carterizacion> resultadoBusqueda(Carterizacion entity);
	//public Carterizacion load(CarterizacionId id);		
	
}
