package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.Oficina;

public interface OficinaBeanLocal {

	public List<Oficina> buscarTodos();
	public Oficina buscarPorId(long id);
	public Oficina buscarPorCodigo(String codigo);
	public List<Oficina> buscarPorIdTerritorio(long idTerritorio);
	public List<Oficina> buscarPorIdPerfilEmpleados(long idPerfil);
	public Oficina create(Oficina oficina);
	public void edit(Oficina oficina);
}
