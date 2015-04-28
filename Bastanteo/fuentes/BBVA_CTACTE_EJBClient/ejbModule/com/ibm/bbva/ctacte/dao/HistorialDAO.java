package com.ibm.bbva.ctacte.dao;

import java.util.Date;
import java.util.List;

import com.ibm.bbva.ctacte.IGenericDAO;
import com.ibm.bbva.ctacte.bean.Historial;

public interface HistorialDAO extends IGenericDAO<Historial, Integer> {

	public List<Historial> findByExpdiente(int idExpediente);

	public List<Historial> findByFiltrofromHistorial(String codEstadoExp,
			String idTarea, String codUserResponsableTarea,
			String idOficinaTarea, String idTerritorioTarea, String nomTarea,
			String codCentral, String idOperacion, String nroDoi,
			String razonSocial, String codGestor, String nroExp,
			String idTerritorioExp, String idOficinaExp, String idEstudioExp,
			String idAbogadoExp, Date strFechaAtencionLimInf,
			Date strFechaAtencionLimSup, Date strFechaAsignacionLimInf,
			Date strFechaAsignacionLimSup, Date strFechaInicioLimInf,
			Date strFechaInicioLimSup, Date strFechaTerminoLimInf,
			Date strFechaTerminoLimSup);
	
	public Historial findUltimaTarea(int idExpediente, int idTarea);

}
