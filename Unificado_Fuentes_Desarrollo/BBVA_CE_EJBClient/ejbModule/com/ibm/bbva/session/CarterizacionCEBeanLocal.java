package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.CarterizacionCE;

public interface CarterizacionCEBeanLocal {	
	//Emitson
	public CarterizacionCE buscarPorIdProdIdTerrIdPerfil(String codigo);
	
	public List<CarterizacionCE> buscarTodos();
	
}
