package com.ibm.bbva.session;

import javax.ejb.Local;

import com.ibm.bbva.entities.TareaAd;

@Local
public interface TareaAdBeanLocal {

	public TareaAd buscarPorId(long id);
	
}
