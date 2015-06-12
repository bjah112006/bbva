package com.ibm.bbva.session;

import java.util.List;

import com.ibm.bbva.entities.EstadoTareaCE;


public interface EstadoTareaCEBeanLocal {
	public List<EstadoTareaCE> buscarPorIdEstado(long idEstado);
	public List<EstadoTareaCE> buscarPorIdTarea(long idTarea);
	public List<EstadoTareaCE> buscarPorIdPerfil(long idPerfil);
	public List<EstadoTareaCE> buscar();
	public List<EstadoTareaCE> buscarPorIdPerfilMenosEnDesuso(long idPerfil);
	public List<EstadoTareaCE> buscarPorTareaEstado(long idtarea, String estado);
}
