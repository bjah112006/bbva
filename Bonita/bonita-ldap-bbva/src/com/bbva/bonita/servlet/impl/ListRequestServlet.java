package com.bbva.bonita.servlet.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.data.ArchivedDataInstance;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;

import com.bbva.bonita.dto.SolicitudDTO;
import com.bbva.bonita.util.Constante;
import com.bbva.bonita.util.ConstantesEnum;
import com.bbva.bonita.util.Filtro;
import com.bbva.bonita.util.Utils;

public class ListRequestServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger("ListRequestServlet");
	private static final long serialVersionUID = 1L;
	private ProcessAPI processAPI;
	private IdentityAPI identityAPI;
	
	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
		logger.log(Level.INFO, "=== SERVICE");
		
		try {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			final HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor((HttpServletRequest) servletRequest);
			final APISession apiSession = requestAccessor.getApiSession();
			
			setProcessAPI(TenantAPIAccessor.getProcessAPI(apiSession));
			setIdentityAPI(TenantAPIAccessor.getIdentityAPI(apiSession));
			
			Filtro filtro = new Filtro();
			filtro.setCodigoFiltro(request.getParameter("filtro"));
			filtro.setValorFiltro(request.getParameter("valorFiltro"));
			
			logger.log(Level.INFO, "=== FILTRO: " + filtro.getCodigoFiltro());
			logger.log(Level.INFO, "=== VALOR FILTRO: " + filtro.getValorFiltro());
			
			//TODO: OBTENEMOS SOLICITUDES PENDIENTES
			SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 100);
			SearchResult<ProcessInstance> processInstanceResults = getProcessAPI().searchOpenProcessInstances(builder.done());
			List<ProcessInstance> listaSolicitudesPendientes = processInstanceResults.getResult();
			
			//TODO: OBTENEMOS SOLICITUDES ARCHIVADAS
			builder = new SearchOptionsBuilder(0, 100);
			SearchResult<ArchivedProcessInstance> archivedProcessInstanceResults = getProcessAPI().searchArchivedProcessInstances(builder.done());
			List<ArchivedProcessInstance> listArchivedProcessInstances = archivedProcessInstanceResults.getResult();	
			
			List<SolicitudDTO> listaSolicitud = obtenerSolicitudes(listaSolicitudesPendientes, listArchivedProcessInstances, filtro);
			Integer cantidadSolicitudes = listaSolicitud!=null?listaSolicitud.size():0;
			logger.log(Level.INFO, "=== CANTIDAD DE SOLICITUDES: " + cantidadSolicitudes);
			obtenerSalida(listaSolicitud, servletResponse);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "=== ERROR AL LISTAR SOLICITUDES: ", e);
		}
	}
	
	private void obtenerSalida(List<SolicitudDTO> listaSolicitud, ServletResponse servletResponse) throws IOException{
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		StringBuilder builder = new StringBuilder();
		if(listaSolicitud!=null && !listaSolicitud.isEmpty()){
			for(SolicitudDTO solicitudDTO:listaSolicitud){
				if(builder.length() > 0) {
					builder.append(", ");
				}
				builder.append("{\"nroSolicitud\":\"" + obtenerCadena(solicitudDTO.getNroSolicitud()) + 
						"\", \"ruc\":\"" + obtenerCadena(solicitudDTO.getNroDOICliente()) +  
						"\", \"nombre\":\"" + obtenerCadena(solicitudDTO.getNombreCliente()) + 
						"\", \"estado\":\"" + obtenerCadena(solicitudDTO.getEstado()) + 
						"\", \"tipoOferta\":\"" + obtenerCadena(solicitudDTO.getTipoOferta()) + 
						"\", \"oficina\":\"" + obtenerCadena(solicitudDTO.getOficinaSolicitud()) +
						"\", \"fechaIngreso\":\"" + obtenerCadena(solicitudDTO.getFechaLlegada()) + 
						"\", \"usuario\":\"" + obtenerCadena(solicitudDTO.getUsuarioEjecutorTarea()) +
						"\", \"rvgl\":\"" + obtenerCadena(solicitudDTO.getNroRVGL()) + 
						"\", \"producto\":\"" + obtenerCadena(solicitudDTO.getProducto()) +
						"\", \"campania\":\"" + obtenerCadena(solicitudDTO.getCampania()) + 
						"\", \"clasificacion\":\"" + obtenerCadena(solicitudDTO.getClasificacion_clte()) +
						"\", \"nombreProceso\":\"" + obtenerCadena(solicitudDTO.getNombreProceso()) +
						"\", \"version\":\"" + obtenerCadena(solicitudDTO.getVersionProceso()) +
						"\", \"variable\":\"" + obtenerCadena(solicitudDTO.getVariable()) +
						"\", \"idArchivada\":\"" + obtenerCadena(solicitudDTO.getIdArchivada()) +
						"\", \"abnRegistrante\":\"" + obtenerCadena(solicitudDTO.getAbn_registante()) + "\"}");
			}
			//logger.log(Level.INFO, builder.toString());
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.write("{\"solicitudes\": ["+ builder.toString()+ "]}");
			out.close();
		}
	}
	
	private String obtenerCadena(String cadena){
		return cadena!=null?cadena:"";
	}
	
	private List<SolicitudDTO> obtenerSolicitudes(List<ProcessInstance> listaSolicitudesPendientes, 
												List<ArchivedProcessInstance> listArchivedProcessInstances, Filtro filtro) throws Exception{
		List<SolicitudDTO> listaSolicitud = new ArrayList<SolicitudDTO>();
		if(listaSolicitudesPendientes!=null && !listaSolicitudesPendientes.isEmpty()){
			Iterator<ProcessInstance> itrSolicitudesPendientes = listaSolicitudesPendientes.iterator();
			while(itrSolicitudesPendientes.hasNext()){
				ProcessInstance elementoSolicitud = itrSolicitudesPendientes.next();
				SolicitudDTO solicitudDTO = obtenerDatosSolicitudPendiente(elementoSolicitud);
				listaSolicitud.add(solicitudDTO);
			}
		}
		List<SolicitudDTO> listaSolicitudArchivadas = obtenerSolicitudesArchivadas(listArchivedProcessInstances, filtro);
		
		listaSolicitud.addAll(listaSolicitudArchivadas);
		listaSolicitud = obtenerSolicitudFiltro(listaSolicitud, filtro);
		return listaSolicitud;
	}
	
	private List<SolicitudDTO> obtenerSolicitudesArchivadas(List<ArchivedProcessInstance> listArchivedProcessInstances, 
																						Filtro filtro) throws Exception{
		List<SolicitudDTO> listaSolicitud = new ArrayList<SolicitudDTO>();
		if(listArchivedProcessInstances!=null && !listArchivedProcessInstances.isEmpty()){
			Iterator<ArchivedProcessInstance> itrSolicitudesArchivadas = listArchivedProcessInstances.iterator();
			while(itrSolicitudesArchivadas.hasNext()){
				ArchivedProcessInstance elementoSolicitud = itrSolicitudesArchivadas.next();
				SolicitudDTO solicitudDTO = obtenerDatosSolicitudArchivada(elementoSolicitud);
				listaSolicitud.add(solicitudDTO);
			}
		}
		return listaSolicitud;
	}
	
	private List<SolicitudDTO> obtenerSolicitudFiltro(List<SolicitudDTO> listaSolicitudes, Filtro filtro){
		List<SolicitudDTO> listaSolicitud = new ArrayList<SolicitudDTO>();
		for(SolicitudDTO solicitud:listaSolicitudes){
			switch (Integer.valueOf(filtro.getCodigoFiltro())) {
			case 1: //TODO: FILTRO POR NRO DE SOLICITUD
				if(filtro.getValorFiltro().compareTo(solicitud.getNroSolicitud())==0){
					listaSolicitud.add(solicitud);
				}
				break;
			case 2: //TODO: FILTRO POR DOI DE CLIENTE
				if(filtro.getValorFiltro().contains(solicitud.getNroDOICliente())){
					listaSolicitud.add(solicitud);
				}
				break;
			case 3: //TODO: FILTRO POR RVGL
				if(filtro.getValorFiltro().compareTo(solicitud.getNroRVGL())==0){
					listaSolicitud.add(solicitud);
				}
			case 4: //TODO: FILTRO POR SOLICITUD PRE IMPRESO
				if(solicitud.getNum_preimpreso()!=null &&
						filtro.getValorFiltro().compareTo(solicitud.getNum_preimpreso())==0){
					listaSolicitud.add(solicitud);
				}
				break;
			}
		}
		return listaSolicitud;
	}
	
	private SolicitudDTO obtenerDatosSolicitudPendiente(ProcessInstance elementoSolicitud) throws Exception{
		ProcessDefinition processDefinition = getProcessAPI().getProcessDefinition(elementoSolicitud.getProcessDefinitionId());
		SolicitudDTO solicitudDTO = new SolicitudDTO();
		solicitudDTO.setNroSolicitud(String.valueOf(elementoSolicitud.getRootProcessInstanceId()));
		solicitudDTO.setNombreProceso(processDefinition.getName());
		solicitudDTO.setVersionProceso(processDefinition.getVersion());
		solicitudDTO.setVariable(Constante.PARAMETRO_VARIABLE);
		solicitudDTO.setFechaLlegada(Utils.convertirDateEnCadena(elementoSolicitud.getStartDate(), ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
		User user = getIdentityAPI().getUser(elementoSolicitud.getStartedBy());
		solicitudDTO.setUsuarioEjecutorTarea(user.getUserName().concat("-").concat(user.getFirstName()).concat(" " + user.getLastName()));
		
		List<DataInstance> datosPendientesAdicionales = getProcessAPI().getProcessDataInstances(elementoSolicitud.getRootProcessInstanceId(), 1, 1000);
		Iterator<DataInstance> itrDataCaso = datosPendientesAdicionales.iterator();
		while(itrDataCaso.hasNext()) {
			DataInstance elementDataCaso = (DataInstance) itrDataCaso.next();
			String nombre = elementDataCaso.getName();
			String valor = elementDataCaso.getValue()==null?ConstantesEnum.CADENA_VACIA.getNombre():elementDataCaso.getValue().toString();
			solicitudDTO = setearDetalleSolicitud(solicitudDTO, nombre, valor);
		}	
		return solicitudDTO;
	}
	
	private SolicitudDTO obtenerDatosSolicitudArchivada(ArchivedProcessInstance elementoSolicitud) throws Exception{
		ProcessDefinition processDefinition = getProcessAPI().getProcessDefinition(elementoSolicitud.getProcessDefinitionId());
		SolicitudDTO solicitudDTO = new SolicitudDTO();
		solicitudDTO.setNroSolicitud(String.valueOf(elementoSolicitud.getRootProcessInstanceId()));
		solicitudDTO.setIdArchivada(String.valueOf(elementoSolicitud.getId()));
		solicitudDTO.setNombreProceso(processDefinition.getName());
		solicitudDTO.setVersionProceso(processDefinition.getVersion());
		solicitudDTO.setVariable("archived"+Constante.PARAMETRO_VARIABLE);
		solicitudDTO.setFechaLlegada(Utils.convertirDateEnCadena(elementoSolicitud.getStartDate(), ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
		User user = getIdentityAPI().getUser(elementoSolicitud.getStartedBy());
		solicitudDTO.setUsuarioEjecutorTarea(user.getUserName().concat("-").concat(user.getFirstName()).concat(" " + user.getLastName()));
		
		List<ArchivedDataInstance> datosArchivadosAdicionales = getProcessAPI().getArchivedProcessDataInstances(elementoSolicitud.getRootProcessInstanceId(), 1, 1000);
		Iterator<ArchivedDataInstance> itrDataCaso = datosArchivadosAdicionales.iterator();
		while(itrDataCaso.hasNext()) {
			ArchivedDataInstance elementDataCaso = (ArchivedDataInstance) itrDataCaso.next();
			String nombre = elementDataCaso.getName();
			String valor = elementDataCaso.getValue()==null?ConstantesEnum.CADENA_VACIA.getNombre():elementDataCaso.getValue().toString();
			solicitudDTO = setearDetalleSolicitud(solicitudDTO, nombre, valor);
		}	
		return solicitudDTO;
	}
	
	private SolicitudDTO setearDetalleSolicitud(SolicitudDTO solicitudDTO,String nombre,String valor){
		if("tipo_doi_cliente".compareTo(nombre)==0){
			solicitudDTO.setTipoDOICliente((valor==null || valor.equals("null"))?"":valor);
		}else if("num_doi_cliente".compareTo(nombre)==0){
			solicitudDTO.setNroDOICliente((valor==null || valor.equals("null"))?"":valor);
		}else if("nombre_cliente".compareTo(nombre)==0){
			solicitudDTO.setNombreCliente((valor==null || valor.equals("null"))?"":valor);
		}else if("estado_solicitud".compareTo(nombre)==0){
			solicitudDTO.setEstado((valor==null || valor.equals("null"))?"":valor);
		}else if("oferta_aprobada".compareTo(nombre)==0){
			solicitudDTO.setTipoOferta((valor==null || valor.equals("null"))?"":valor);
		}else if("ofi_registro".compareTo(nombre)==0){//codigo_centro_negocio
			solicitudDTO.setOficinaSolicitud((valor==null || valor.equals("null"))?"":valor);
		}else if("num_rvgl".compareTo(nombre)==0){
			solicitudDTO.setNroRVGL((valor==null || valor.equals("null"))?"":valor);
		}else if("producto".compareTo(nombre)==0){
			solicitudDTO.setProducto((valor==null || valor.equals("null"))?"":valor);
		}else if("lst_campanias".compareTo(nombre)==0){//campania
			solicitudDTO.setCampania((valor==null || valor.equals("null"))?"":valor);
		}else if("clte_clasificacion".compareTo(nombre)==0){
			solicitudDTO.setClasificacion_clte((valor==null || valor.equals("null"))?"":valor);
		}else if("num_tramite".compareTo(nombre)==0){
			solicitudDTO.setNum_preimpreso((valor==null || valor.equals("null"))?"":valor);
		}else if("usu_registro".compareTo(nombre)==0){
			solicitudDTO.setAbn_registante((valor==null || valor.equals("null"))?"":valor);
		}
		return solicitudDTO;
	}

	public ProcessAPI getProcessAPI() {
		return processAPI;
	}

	public void setProcessAPI(ProcessAPI processAPI) {
		this.processAPI = processAPI;
	}

	public IdentityAPI getIdentityAPI() {
		return identityAPI;
	}

	public void setIdentityAPI(IdentityAPI identityAPI) {
		this.identityAPI = identityAPI;
	}
	
}
