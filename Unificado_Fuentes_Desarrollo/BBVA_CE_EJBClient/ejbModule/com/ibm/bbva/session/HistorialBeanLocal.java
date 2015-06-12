package com.ibm.bbva.session;

import java.util.Date;
import java.util.List;

import com.ibm.bbva.entities.Historial;

public interface HistorialBeanLocal {

	public List<Historial> buscarPorIdExpediente(long idExpediente);
	
	public Historial create(Historial historial);
	
	public Historial buscarPorId(long id);
	
	//public List<Historial> buscarXcriterios(Historial h, Date fechaInicio, Date fechaFin);
	
	public List<Historial> buscarXCriterioExpedienteYTarea(Long idExpediente, Long idtarea);
	
	public List<Historial> buscarXCriterioProductoTerritorioPerfil(Long idProducto, Long idTerritorio, Long idPerfil, String flagActivo);
	
	public List<Historial> buscarPorIdExpRetraer(long idExpediente, String flagRetaer);
	
	public void edit(Historial historial);

	List<Historial> buscarXCriterioExpedienteYPerfilEjecutivo(
			Long idExpediente, Long idPerfil);
	
	public List<Historial> buscarPerfilRecientePorId(long idExpediente);
	
	public Historial buscarMasRecienPorId(long idExpediente);

	List<Historial> buscarXCriterioExpedienteYTarea_TareaSiete(
			Long idExpediente, Long idtarea1, Long idtarea2);
	
	public List<Historial> buscarXCriterioExpedienteXPerfil(Long idExpediente, Long idPerfil);

	public List<Historial> buscarXcriterios(Historial h, Date fechaInicio,
			Date fechaFin, List<Long> list);


	
}
