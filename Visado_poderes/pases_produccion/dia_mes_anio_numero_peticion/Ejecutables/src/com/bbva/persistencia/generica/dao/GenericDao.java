package com.bbva.persistencia.generica.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import org.hibernate.Session;

import com.hildebrando.visado.dto.MiembroDto;

public interface GenericDao<K, T> {
	
	public abstract Connection getConnection() throws Exception;
	
	public abstract void eliminar(K objeto)  throws Exception;

	public abstract K insertar(K objeto) throws Exception;
	
	public abstract K insertarMerge(K objeto) throws Exception;
	
	public abstract K save(K objeto) throws Exception;

	public abstract K modificar(K objeto) throws Exception;
	
	public abstract K guardarModificar(K objeto) throws Exception;
	
	public abstract List<K> buscarDinamico(final Busqueda filtro) throws Exception;
	
	public abstract List<Integer> buscarDinamicoInteger(final Busqueda filtro) throws Exception;
	
	public abstract List<String> buscarDinamicoString(final Busqueda filtro) throws Exception;

	@SuppressWarnings("unchecked")
	public abstract K buscarById(Class clazz, Serializable id) throws Exception;
	public Session obtenerSession();
}
