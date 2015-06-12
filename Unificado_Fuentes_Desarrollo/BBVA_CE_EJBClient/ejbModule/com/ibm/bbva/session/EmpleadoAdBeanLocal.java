package com.ibm.bbva.session;

import javax.ejb.Local;

import com.ibm.bbva.entities.EmpleadoAd;

@Local
public interface EmpleadoAdBeanLocal {
	
	public EmpleadoAd buscarPorCodigo(String codigo);

}
