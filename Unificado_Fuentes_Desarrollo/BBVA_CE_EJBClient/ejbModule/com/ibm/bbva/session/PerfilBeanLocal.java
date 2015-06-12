package com.ibm.bbva.session;

import java.util.List;

import javax.ejb.Local;

import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.Perfil;

@Local
public interface PerfilBeanLocal {

	//public List<Perfil> obtenerExisteCarterizacion(long idTerritorio);
	public List<Perfil> buscarPorIdCaract(long idCaract);
	public Perfil buscarPorId(long id);
	public List<Perfil> buscarPorIdJefe(long idPerfilJefe);
	public List<Perfil> buscarTodos();
	public List<Perfil> buscarPorProceso();
	
}
