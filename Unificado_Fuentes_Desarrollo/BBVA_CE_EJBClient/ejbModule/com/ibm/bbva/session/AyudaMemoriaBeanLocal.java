package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.AyudaMemoria;

public interface AyudaMemoriaBeanLocal {

	public List<AyudaMemoria> buscarPorIdExpediente(long idExpediente);
	public AyudaMemoria create(AyudaMemoria ayudaMemoria);
	public AyudaMemoria  buscarPorId(long idAyudaMemoria);
	public void delete(AyudaMemoria ayudaMemoria);	
	public void update(AyudaMemoria ayudaMemoria);
	
	
}
