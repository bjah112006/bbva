package com.ibm.bbva.ctacte.dao;

import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Perfil;

public interface PerfilDAO extends IGenericDAO<Perfil, Integer> {
	
	public Perfil findByCodigo(String codigo);
	public List<Perfil> buscarSupervizados(Integer idPerfil);
	public List<Perfil> buscarTodos();
	public List<Perfil> buscarPerfilesConTareaAsignada();
	public List<Perfil> buscarPorListaIDs(List<Integer> idPerfiles);

}
