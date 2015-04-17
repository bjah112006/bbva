package com.ibm.bbva.session;

import java.util.List;

import javax.ejb.Local;

import com.ibm.bbva.entities.Persona;

@Local
public interface PersonaBeanLocal {
	
	public List<Persona> buscarTodos();
	public Persona buscarPorId(long id);
}
