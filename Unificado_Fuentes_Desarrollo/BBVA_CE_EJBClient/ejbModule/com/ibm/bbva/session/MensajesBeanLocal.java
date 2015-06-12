package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Mensajes;

public interface MensajesBeanLocal {
	
	public List<Mensajes> buscarTodos();
	public Mensajes create(Mensajes entity);
	public Mensajes buscarPorId(long id);
	public void update(Mensajes entity);
	public void delete(Mensajes entity);
	

}
