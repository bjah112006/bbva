package com.bbva.persistencia.generica.dao;

import java.util.Date;
import java.util.List;

import com.hildebrando.visado.dto.AgrupacionDelegadosDto;
import com.hildebrando.visado.dto.AgrupacionPlazoDto;
import com.hildebrando.visado.dto.Estado;
import com.hildebrando.visado.modelo.RecaudacionTipoServ;
import com.hildebrando.visado.modelo.SolicitudesOficina;
import com.hildebrando.visado.modelo.SolicitudesTipoServicio;
import com.hildebrando.visado.modelo.TiivsAnexoSolicitud;
import com.hildebrando.visado.modelo.TiivsMiembroNivel;
import com.hildebrando.visado.modelo.TiivsNivel;
import com.hildebrando.visado.modelo.TiivsSolicitud;
import com.hildebrando.visado.modelo.TiivsSolicitudOperban;

public interface SolicitudDao<K, T> extends GenericDao<K, T>{ 

	public  String obtenerPKNuevaSolicitud() throws Exception;
	public String obtenerMaximoMovimiento(String codSolicitud) throws Exception;
	public TiivsSolicitud obtenerTiivsSolicitud(TiivsSolicitud solicitud) throws Exception;
	public List<TiivsSolicitudOperban> obtenerListarOperacionesBancarias(TiivsSolicitud solicitud) throws Exception;
	public List<TiivsAnexoSolicitud> obtenerListarAnexosSolicitud(TiivsSolicitud solicitud) throws Exception;
	public List<SolicitudesOficina> obtenerListarTotalSolicitudesxEstado(TiivsSolicitud solicitud,String territorio,Date dFechaInicio, Date dFechaFin) throws Exception;
	public List<SolicitudesTipoServicio> obtenerSolicitudesxTipoServicio(TiivsSolicitud solicitud,String idOpeBan, String cadTipoServ,String cadEstudio,
			String rangoImpG,Double importeIni,Double importeFin,Date dFechaInicio, Date dFechaFin) throws Exception;
	public List<RecaudacionTipoServ> obtenerListarRecaudacionxTipoServicio(TiivsSolicitud solicitud, String territorio,Date dFechaInicio, Date dFechaFin) throws Exception;
	public List<AgrupacionPlazoDto> obtenerLiquidacion (String cadEstudio, int anio, int mes, double impuesto) throws Exception;
	public List<AgrupacionPlazoDto> obtenerLiquidacion_2 (String cadEstudio, int anio, int mes, double impuesto) throws Exception;
	
	public List<AgrupacionDelegadosDto>  obtenerDelegados() throws Exception;
	public List<AgrupacionDelegadosDto>  obtenerPKDelegados() throws Exception ;
	public List<TiivsNivel> listarNivelesDistinct() throws Exception;
	public List<String> obtenerCodigosSolicitudesARevocarRechazar() throws Exception ;
	public boolean validarExisteGrupoDelegados(List<TiivsMiembroNivel> listaDelegados);
	public List<Estado> traerEstadosFlujoSolicitud() throws Exception;
}
