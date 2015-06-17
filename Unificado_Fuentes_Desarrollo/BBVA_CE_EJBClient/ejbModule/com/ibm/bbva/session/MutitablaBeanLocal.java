package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Multitabla;

public interface MutitablaBeanLocal 
{
	public List<Multitabla> buscarPorPadre(Long idPadre);
	
}
