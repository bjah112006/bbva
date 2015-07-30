package com.bbva.bonita.servlet.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstancesSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.bbva.bonita.authentication.impl.DBUtil;
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
		logger.log(Level.INFO, "====== SERVICE ======");
		
		try {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletRequestAccessor requestAccessor = new HttpServletRequestAccessor((HttpServletRequest) servletRequest);
			APISession apiSession = requestAccessor.getApiSession();
			Long tiempoInicio = 0L;
			
			setProcessAPI(TenantAPIAccessor.getProcessAPI(apiSession));
			setIdentityAPI(TenantAPIAccessor.getIdentityAPI(apiSession));
			
			Filtro filtro = new Filtro();
			filtro.setCodigoFiltro(request.getParameter("filtro"));
			filtro.setValorFiltro(request.getParameter("valorFiltro"));
			filtro.setEstacion(request.getParameter("estacion"));
			
			logger.log(Level.INFO, "=== FILTRO: " + filtro.getCodigoFiltro());
			logger.log(Level.INFO, "=== VALOR FILTRO: " + filtro.getValorFiltro());
			logger.log(Level.INFO, "=== ESTACION: " + filtro.getEstacion());
			
			List<SolicitudDTO> listaSolicitud = null;
			List<ArchivedProcessInstance> listArchivedProcessInstances = null;
		
			SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 100);
			if(filtro.getCodigoFiltro().compareTo("01")==0 && filtro.getValorFiltro().compareTo("")!=0){
				logger.log(Level.INFO, "=== FILTRO POR NRO. SOLICITUD PENDIENTES: " + filtro.getValorFiltro());
				builder.filter(ProcessInstanceSearchDescriptor.ID, filtro.getValorFiltro());
			}
			
			tiempoInicio = System.currentTimeMillis();
			SearchResult<ProcessInstance> processInstanceResults = getProcessAPI().searchOpenProcessInstances(builder.done());
			List<ProcessInstance> listaSolicitudesPendientes = processInstanceResults.getResult();
			logger.info("=== TIEMPO RESPUESTA CONSULTA SOLIC. PENDIENTES: " + (System.currentTimeMillis()-tiempoInicio)/1000 + " segundos");
			logger.info("=== CANTIDAD DE SOLICITUDES PENDIENTES: " + (listaSolicitudesPendientes!=null?listaSolicitudesPendientes.size():0));
			
			//TODO: SI LA BUSQUEDA ES POR ESTACION, NO SE BUSCAN LAS SOLICITUDES ARCHIVADAS
			if(filtro.getEstacion().compareTo("-1")==0){
				//TODO: OBTENEMOS SOLICITUDES ARCHIVADAS
				builder = new SearchOptionsBuilder(0, 100);
				if(filtro.getCodigoFiltro().compareTo("01")==0){
					logger.log(Level.INFO, "=== FILTRO POR NRO. SOLICITUD ARCHIVADAS: " + filtro.getValorFiltro());
					builder.filter(ArchivedProcessInstancesSearchDescriptor.SOURCE_OBJECT_ID, filtro.getValorFiltro());
				}
				tiempoInicio = System.currentTimeMillis();
				SearchResult<ArchivedProcessInstance> archivedProcessInstanceResults = getProcessAPI().searchArchivedProcessInstances(builder.done());
				listArchivedProcessInstances = archivedProcessInstanceResults.getResult();	
				logger.info("=== TIEMPO RESPUESTA CONSULTA SOLIC. ARCHIVADAS: " + (System.currentTimeMillis()-tiempoInicio)/1000 + " segundos");
				logger.info("=== CANTIDAD DE SOLICITUDES ARCHIVADAS: " + (listArchivedProcessInstances!=null?listArchivedProcessInstances.size():0));
			}
			
			//TODO: FILTRAMOS LAS SOLICITUDES
			listaSolicitud = obtenerSolicitudes(listaSolicitudesPendientes, listArchivedProcessInstances, filtro);
			Collections.sort(listaSolicitud);
			Integer cantidadSolicitudes = listaSolicitud!=null?listaSolicitud.size():0;
			logger.log(Level.INFO, "=== CANTIDAD DE SOLICITUDES A MOSTRAR EN PANTALLA: " + cantidadSolicitudes);
			obtenerSalida(listaSolicitud, servletResponse);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "=== ERROR AL LISTAR SOLICITUDES: ", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void obtenerSalida(List<SolicitudDTO> listaSolicitud, ServletResponse servletResponse) throws Exception{
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if(listaSolicitud!=null && !listaSolicitud.isEmpty()){
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			
			for(SolicitudDTO solicitudDTO:listaSolicitud){
				Map<String, String> map = new HashMap<String, String>();
				map.put("nroSolicitud", obtenerCadena(solicitudDTO.getNroSolicitud()));
				map.put("ruc", obtenerCadena(solicitudDTO.getNroDOICliente()));
				map.put("nombre", obtenerCadena(solicitudDTO.getNombreCliente()));
				map.put("estado", obtenerCadena(solicitudDTO.getEstado()));
				map.put("tipoOferta", obtenerCadena(solicitudDTO.getTipoOferta()));
				map.put("oficina", obtenerCadena(solicitudDTO.getOficinaSolicitud()));
				map.put("fechaIngreso", obtenerCadena(solicitudDTO.getFechaLlegada()));
				map.put("rvgl", obtenerCadena(solicitudDTO.getNroRVGL()));
				map.put("producto", obtenerCadena(solicitudDTO.getProducto()));
				map.put("campania", obtenerCadena(solicitudDTO.getCampania()));
				map.put("clasificacion", obtenerCadena(solicitudDTO.getClasificacion_clte()));
				map.put("nombreProceso", obtenerCadena(solicitudDTO.getNombreProceso()));
				map.put("version", obtenerCadena(solicitudDTO.getVersionProceso()));
				map.put("variable", obtenerCadena(solicitudDTO.getVariable()));
				map.put("idArchivada", obtenerCadena(solicitudDTO.getIdArchivada()));
				map.put("usuario", obtenerCadena(solicitudDTO.getUsuarioEjecutorTarea()));
				map.put("abnRegistrante", obtenerCadena(solicitudDTO.getAbn_registante()));
				jsonArray.add(map);
			}
			jsonObject.put("solicitudes", jsonArray);
			 
			logger.log(Level.INFO, jsonObject.toJSONString());
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.write(jsonObject.toJSONString());
			out.close();
		}
	}
	
	private String obtenerCadena(String cadena){
		return cadena!=null?cadena:"";
	}
	
	private List<SolicitudDTO> obtenerSolicitudes(List<ProcessInstance> listaSolicitudesPendientes, 
												List<ArchivedProcessInstance> listArchivedProcessInstances, Filtro filtro) throws Exception{
		Long inicio = System.currentTimeMillis();
		List<SolicitudDTO> listaSolicitud = new ArrayList<SolicitudDTO>();
		if(listaSolicitudesPendientes!=null && !listaSolicitudesPendientes.isEmpty()){
			Iterator<ProcessInstance> itrSolicitudesPendientes = listaSolicitudesPendientes.iterator();
			while(itrSolicitudesPendientes.hasNext()){
				ProcessInstance elementoSolicitud = itrSolicitudesPendientes.next();
				SolicitudDTO solicitudDTO = obtenerDatosSolicitudPendiente(elementoSolicitud);
				List<FlowNodeInstance> listaTareasPendientes = obtenerListaTareasPendientes(elementoSolicitud.getProcessDefinitionId(), 
																			elementoSolicitud.getRootProcessInstanceId());
				Iterator<FlowNodeInstance> itrTareaPendiente = listaTareasPendientes.iterator();
				while(itrTareaPendiente.hasNext()) {
					FlowNodeInstance elementoTareaPendiente = (FlowNodeInstance) itrTareaPendiente.next();
					solicitudDTO = setearDatosTrazaTareaPendiente(solicitudDTO, elementoTareaPendiente);
				}
				
				listaSolicitud.add(solicitudDTO);
			}
			logger.info("=== TIEMPO RESPUESTA MAPEO SOLICITUDES PENDIENTES: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
		}
		List<SolicitudDTO> listaSolicitudArchivadas = obtenerSolicitudesArchivadas(listArchivedProcessInstances);
		
		listaSolicitud.addAll(listaSolicitudArchivadas);
		inicio = System.currentTimeMillis();
		listaSolicitud = obtenerSolicitudFiltro(listaSolicitud, filtro);
		logger.info("=== TIEMPO RESPUESTA FILTRO SOLICITUDES: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
		return listaSolicitud;
	}
	
	private SolicitudDTO setearDatosTrazaTareaPendiente(SolicitudDTO solicitud_base, FlowNodeInstance elementoTareaPendiente) throws Exception{
		List<DataInstance> datosTareaPendiente = getProcessAPI().getActivityDataInstances(elementoTareaPendiente.getId(), 1, 1000);
		Iterator<DataInstance> itrDataTarea = datosTareaPendiente.iterator();
		while(itrDataTarea.hasNext()) {
			DataInstance elementDataCaso = (DataInstance) itrDataTarea.next();
			String nombre = elementDataCaso.getName();
			String valor = elementDataCaso.getValue()==null?ConstantesEnum.CADENA_VACIA.getNombre():elementDataCaso.getValue().toString();
			
			//TODO: CAPTURAMOS EL ID DEL ROL 
			if(nombre.compareToIgnoreCase("id_rol_actor")==0){
				solicitud_base.setRolEjecutorTarea(valor);
			}
		}
		return solicitud_base;
	}
	
	private List<FlowNodeInstance> obtenerListaTareasPendientes(Long idProcesoPrincipal,Long idElemento) throws SearchException{
		List<FlowNodeInstance> listaTareasPendientes = null;
		SearchOptionsBuilder builder = new SearchOptionsBuilder(0,100);
		builder.filter(FlowNodeInstanceSearchDescriptor.PROCESS_DEFINITION_ID, idProcesoPrincipal);
		builder.filter(FlowNodeInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID,idElemento);
		final SearchResult<FlowNodeInstance> processFlowNodeInstance = getProcessAPI().searchFlowNodeInstances(builder.done());
		 listaTareasPendientes = processFlowNodeInstance.getResult();
		return listaTareasPendientes;
	}
	
	private List<SolicitudDTO> obtenerSolicitudesArchivadas(List<ArchivedProcessInstance> listArchivedProcessInstances) throws Exception{
		Long inicio = System.currentTimeMillis();
		List<SolicitudDTO> listaSolicitud = new ArrayList<SolicitudDTO>();
		if(listArchivedProcessInstances!=null && !listArchivedProcessInstances.isEmpty()){
			Iterator<ArchivedProcessInstance> itrSolicitudesArchivadas = listArchivedProcessInstances.iterator();
			while(itrSolicitudesArchivadas.hasNext()){
				ArchivedProcessInstance elementoSolicitud = itrSolicitudesArchivadas.next();
				SolicitudDTO solicitudDTO = obtenerDatosSolicitudArchivada(elementoSolicitud);
				listaSolicitud.add(solicitudDTO);
			}
			logger.info("=== TIEMPO RESPUESTA MAPEO SOLICITUDES ARCHIVADAS: " + (System.currentTimeMillis()-inicio)/1000 + " segundos");
		}
		return listaSolicitud;
	}
	
	private List<SolicitudDTO> obtenerSolicitudFiltro(List<SolicitudDTO> listaSolicitudes, Filtro filtro){
		List<SolicitudDTO> listaSolicitud = new ArrayList<SolicitudDTO>();
		if(filtro.getEstacion().compareTo("-1")==0){
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
		}else{
			//TODO: FILTRO POR ESTACION
			obtenerSolicitudesXEstacion(listaSolicitudes, filtro);
		}
		return listaSolicitud;
	}
	
	private List<SolicitudDTO> obtenerSolicitudesXEstacion(List<SolicitudDTO> listaSolicitudes, Filtro filtro){
		List<SolicitudDTO> listaSolicitud = new ArrayList<SolicitudDTO>();
		String rolIds = DBUtil.obtenerParametroDetalle(Constante.ID_TABLA_ESTACION, filtro.getEstacion());
		logger.log(Level.INFO, "=== FILTRO POR ROL_ID: " + rolIds);
		String[] rol_ids = rolIds.split("|");
		for(SolicitudDTO solicitud:listaSolicitudes){
			if(rol_ids!=null && rol_ids.length>0){
				for(String rol:rol_ids){
					if(solicitud.getRolEjecutorTarea()!=null && 
							rol.compareTo(solicitud.getRolEjecutorTarea())==0){
						listaSolicitud.add(solicitud);
					}
				}
			}else{
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
		}else if("campania".compareTo(nombre)==0){//campania lst_campanias
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
