package com.pe.bbva.pyme.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bonitasoft.engine.bpm.data.ArchivedDataInstance;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserMembership;
import org.bonitasoft.engine.identity.UserMembershipCriterion;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pe.bbva.pyme.dao.ISolicitudDAO;
import com.pe.bbva.pyme.model.Config;
import com.pe.bbva.pyme.model.Solicitud;
import com.pe.bbva.pyme.utils.ConstantesEnum;
import com.pe.bbva.pyme.utils.Utils;

public class SolicitudDAOImpl extends BonitaDataAccess implements ISolicitudDAO {

    private static final Logger LOG = LoggerFactory.getLogger(SolicitudDAOImpl.class);
    protected int MAX_RESULT_INSTANCES;
    protected int MAX_RESULT_TASKS;
    protected int MAX_TRACE_DAYS;

    public SolicitudDAOImpl() throws Exception {
        super();
        BonitaClientRest bonitaClientRest = new BonitaClientRest();
        try {
            bonitaClientRest.init();
            MAX_RESULT_INSTANCES = Integer.parseInt(bonitaClientRest.obtainValue(BonitaClientRest.MAX_RESULT_INSTANCES));
            MAX_RESULT_TASKS = Integer.parseInt(bonitaClientRest.obtainValue(BonitaClientRest.MAX_RESULT_TASKS));
            MAX_TRACE_DAYS = Math.abs(Integer.parseInt(bonitaClientRest.obtainValue(BonitaClientRest.MAX_TRACE_DAYS))) * -1;
            bonitaClientRest.logout();
        } catch (Exception e) {
            MAX_RESULT_INSTANCES = 10000;
            MAX_RESULT_TASKS = 100;
            MAX_TRACE_DAYS = -31;
        }
    }

    public List<Solicitud> listarSolicitudes(List<Config> params) throws Exception {
    	LOG.debug("...inicia listar Solicitudes");
		List<Solicitud> listaSolicitudes = new ArrayList<Solicitud>();
		//1. Obtenemos el Id del proceso "Contratacion Pymes" activo.		
		Long idProcesoPrincipal = (long) 0;//getProcessDefinition().getId();
		//2. Obtenemos las Solicitudes
		//2.1 Solicitudes Pendientes
		List<ProcessInstance> listProcessInstances = obtenerListaSolicitudesPendientes(idProcesoPrincipal);		
		//2.2 Solicitudes Hechas
		List<ArchivedProcessInstance> listArchivedProcessInstances = obtenerListaSolicitudesHechas(idProcesoPrincipal);	
		//2.3 preparamos las iteraciones
		Iterator<ProcessInstance> itrSolicitudPendiente = listProcessInstances.iterator();
		Iterator<ArchivedProcessInstance> itrSolicitudHecha = listArchivedProcessInstances.iterator();		
		//3. Recorremos las Solicitudes.
		//3.1. Solicitudes Hechas
		while(itrSolicitudHecha.hasNext()) {
			ArchivedProcessInstance elementoSolicitud = (ArchivedProcessInstance) itrSolicitudHecha.next();
	    	Long idElemento = elementoSolicitud.getRootProcessInstanceId();
	    	Date fechaLlegadaAux = elementoSolicitud.getStartDate();	
	    	//3.1.1 obtenemos sus datos
	    	Solicitud solicitudAux = setearDatosGeneralesSolicitudHecha(elementoSolicitud,params);
	    	//3.1.2 obtenemos sus Tareas Pendientes
//	    	List<FlowNodeInstance> listaTareasPendientes = obtenerListaTareasPendientes(elementoSolicitud.getProcessDefinitionId(), idElemento);
	    	//3.1.3 obtenemos sus Tareas Hechas
	    	List<ArchivedFlowNodeInstance> listaTareasHechas = obtenerListaTareasHechas(elementoSolicitud.getProcessDefinitionId(), idElemento);
	    	//4. Recorremos las listas
	    	  Iterator<ArchivedFlowNodeInstance> itrTareaHecha = listaTareasHechas.iterator();
	    	  int index_tarea=0;
		      while(itrTareaHecha.hasNext()) {
		    	  ArchivedFlowNodeInstance elementoTareaHecha = (ArchivedFlowNodeInstance) itrTareaHecha.next();
//		    	  LOG.info("elementoTareaHecha.getExecutedBy() "+elementoTareaHecha.getExecutedBy());
//		    	  if(elementoTareaHecha.getExecutedBy()>0){
		    		  index_tarea++;
		    		  Solicitud solicitudHecha = setearDatosGeneralesSolicitudHecha(elementoSolicitud,params);
		    		  setearDatosTrazaTareaHecha(solicitudHecha,elementoTareaHecha,params);
//			    	  if(!solicitudHecha.getUsuarioEjecutorTarea().equals(ConstantesEnum.USER_SYSTEM.getNombre())){
			    		  solicitudHecha.setFechaLlegada(Utils.convertirDateEnCadena(fechaLlegadaAux,ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
			    		  listaSolicitudes.add(solicitudHecha);
			    		  if(index_tarea>2 && index_tarea<=listaTareasHechas.size()){
			    			  listaSolicitudes.get(listaSolicitudes.size()-2).setEstado(solicitudHecha.getEstado());
			    		  }
			    		  if(index_tarea==listaTareasHechas.size()){
			    			  solicitudHecha.setEstado(solicitudAux.getEstado());
			    		  }
				    	  fechaLlegadaAux = elementoTareaHecha.getArchiveDate();  
//			    	  }  
//		    	  }
		      }	
		      /*Iterator<FlowNodeInstance> itrTareaPendiente = listaTareasPendientes.iterator();
		      while(itrTareaPendiente.hasNext()) {
		    	  FlowNodeInstance elementoTareaPendiente = (FlowNodeInstance) itrTareaPendiente.next();
		    	  Solicitud solicitudPendiente = setearDatosTareaPendiente(solicitudAux,elementoTareaPendiente);
		    	  if(!solicitudPendiente.getUsuarioEjecutorTarea().equals(ConstantesEnum.USER_SYSTEM.getNombre())){
		    		  solicitudPendiente.setFechaLlegada(Utils.convertirDateEnCadena(fechaLlegadaAux,ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));		    	  
			    	  listaSolicitudes.add(solicitudPendiente);
			    	  fechaLlegadaAux = null;  
		    	  }
		      }*/	
		}
        // 3.2 Solicitudes Pendientes
		while(itrSolicitudPendiente.hasNext()) {
			ProcessInstance elementoSolicitud = (ProcessInstance) itrSolicitudPendiente.next();
	    	Long idElemento = elementoSolicitud.getRootProcessInstanceId();
	    	Date fechaLlegadaAux = elementoSolicitud.getStartDate();
	    	//3.2.1 obtenemos sus datos
	    	Solicitud solicitudAux = setearDatosGeneralesSolicitudPendiente(elementoSolicitud,params);
	    	//List<FlowNodeInstance> listaTareasPendientes = obtenerListaTareasPendientes(elementoSolicitud.getProcessDefinitionId(), idElemento);
	    	List<ArchivedFlowNodeInstance> listaTareasHechas = obtenerListaTareasHechas(elementoSolicitud.getProcessDefinitionId(), idElemento);
	    	//4. Recorremos las listas
	    	  Iterator<ArchivedFlowNodeInstance> itrTareaHecha = listaTareasHechas.iterator();
	    	  int index_tarea=0;
		      while(itrTareaHecha.hasNext()) {
		    	  ArchivedFlowNodeInstance elementoTareaHecha = (ArchivedFlowNodeInstance) itrTareaHecha.next();
//		    	  if(elementoTareaHecha.getExecutedBy()>0){
		    		  index_tarea++;
		    		  Solicitud solicitudHecha = setearDatosGeneralesSolicitudPendiente(elementoSolicitud,params);
		    		  setearDatosTrazaTareaHecha(solicitudHecha,elementoTareaHecha,params);
//			    	  if(!solicitudHecha.getUsuarioEjecutorTarea().equals(ConstantesEnum.USER_SYSTEM.getNombre())){
			    		  solicitudHecha.setFechaLlegada(Utils.convertirDateEnCadena(fechaLlegadaAux,ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
				    	  listaSolicitudes.add(solicitudHecha);
				    	  if(index_tarea>2 && index_tarea<=listaTareasHechas.size()){
			    			  listaSolicitudes.get(listaSolicitudes.size()-2).setEstado(solicitudHecha.getEstado());
			    		  }
				    	  if(index_tarea==listaTareasHechas.size()){
			    			  solicitudHecha.setEstado(solicitudAux.getEstado());
			    		  }
				    	  fechaLlegadaAux = elementoTareaHecha.getArchiveDate();  
//			    	  }  
//		    	  }
		      }	
		      /*Iterator<FlowNodeInstance> itrTareaPendiente = listaTareasPendientes.iterator();
		      while(itrTareaPendiente.hasNext()) {
		    	  FlowNodeInstance elementoTareaPendiente = (FlowNodeInstance) itrTareaPendiente.next();
		    	  Solicitud solicitudPendiente = setearDatosTrazaTareaPendiente(solicitudAux,elementoTareaPendiente);
		    	  if(!solicitudPendiente.getUsuarioEjecutorTarea().equals(ConstantesEnum.USER_SYSTEM.getNombre())){
		    		  solicitudPendiente.setFechaLlegada(Utils.convertirDateEnCadena(fechaLlegadaAux,ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));		    	  
			    	  listaSolicitudes.add(solicitudPendiente);
			    	  fechaLlegadaAux = null;  
		    	  }
		      }*/
		}
        return listaSolicitudes;
    }

    private List<ArchivedFlowNodeInstance> obtenerListaTareasHechas(Long idProcesoPrincipal, Long idElemento) throws SearchException {
        List<ArchivedFlowNodeInstance> listaTareasHechas = null;
        List<ArchivedFlowNodeInstance> listaTareasHechas_retorno = null;
        SearchOptionsBuilder builder = new SearchOptionsBuilder(0, MAX_RESULT_TASKS);
        builder.filter(ArchivedFlowNodeInstanceSearchDescriptor.PROCESS_DEFINITION_ID, idProcesoPrincipal);
        builder.filter(ArchivedFlowNodeInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID, idElemento);
        builder.filter(ArchivedFlowNodeInstanceSearchDescriptor.STATE_NAME, ConstantesEnum.FILTRO_ESTADO_COMPLETADO.getNombre());
        builder.differentFrom(ArchivedFlowNodeInstanceSearchDescriptor.FLOW_NODE_TYPE, ConstantesEnum.FILTRO_TIPO_NODO_GATE.getNombre());
        builder.differentFrom(ArchivedFlowNodeInstanceSearchDescriptor.NAME, "Consultar Cliente");
        builder.sort(ArchivedFlowNodeInstanceSearchDescriptor.ARCHIVE_DATE, Order.ASC);
        final SearchResult<ArchivedFlowNodeInstance> processArchivedFlowNodeInstance = getProcessAPI().searchArchivedFlowNodeInstances(builder.done());
        listaTareasHechas = processArchivedFlowNodeInstance.getResult();
        if (listaTareasHechas != null) {
            listaTareasHechas_retorno = new ArrayList<ArchivedFlowNodeInstance>();
            Iterator<ArchivedFlowNodeInstance> itrTareaHecha = listaTareasHechas.iterator();
            while (itrTareaHecha.hasNext()) {
                ArchivedFlowNodeInstance elementoTareaHecha = (ArchivedFlowNodeInstance) itrTareaHecha.next();
                if (elementoTareaHecha.getExecutedBy() > 0) {
                    listaTareasHechas_retorno.add(elementoTareaHecha);
                }
            }
        }
        return listaTareasHechas_retorno;
    }

    private List<ProcessInstance> obtenerListaSolicitudesPendientes(Long idProcesoPrincipal) throws SearchException {
        List<ProcessInstance> listProcessInstances = null;
        SearchOptionsBuilder builder = new SearchOptionsBuilder(0, MAX_RESULT_INSTANCES);
        builder.filter(ProcessInstanceSearchDescriptor.NAME, ConstantesEnum.PROCESS_NAME.getNombre());
        Date startDate = Utils.addDaysTodate(new Date(), MAX_TRACE_DAYS);
        builder.greaterOrEquals(ProcessInstanceSearchDescriptor.START_DATE, startDate.getTime());
        final SearchResult<ProcessInstance> processInstanceResults = getProcessAPI().searchOpenProcessInstances(builder.done());
        listProcessInstances = processInstanceResults.getResult();
        return listProcessInstances;
    }

    private List<ArchivedProcessInstance> obtenerListaSolicitudesHechas(Long idProcesoPrincipal) throws SearchException {
        List<ArchivedProcessInstance> listArchivedProcessInstances = null;
        SearchOptionsBuilder builder = new SearchOptionsBuilder(0, MAX_RESULT_INSTANCES);
        builder.filter(ProcessInstanceSearchDescriptor.NAME, ConstantesEnum.PROCESS_NAME.getNombre());
        Date startDate = Utils.addDaysTodate(new Date(), MAX_TRACE_DAYS);
        builder.greaterOrEquals(ProcessInstanceSearchDescriptor.START_DATE, startDate.getTime());
        final SearchResult<ArchivedProcessInstance> archivedProcessInstanceResults = getProcessAPI().searchArchivedProcessInstances(builder.done());
        listArchivedProcessInstances = archivedProcessInstanceResults.getResult();
        return listArchivedProcessInstances;
    }

    private void setearDatosTrazaTareaHecha(Solicitud solicitudHecha, ArchivedFlowNodeInstance elementoTareaHecha, List<Config> params) throws Exception {
        solicitudHecha.setNombreTarea(elementoTareaHecha.getName());
        solicitudHecha.setFechaEnvio(Utils.convertirDateEnCadena(elementoTareaHecha.getArchiveDate(), ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
        solicitudHecha.setUsuarioEjecutorTarea(ConstantesEnum.USUARIO_SISTEMA_NOMBRE.getNombre());
        solicitudHecha.setRolEjecutorTarea(ConstantesEnum.USUARIO_SISTEMA_ROL.getNombre());
        if (elementoTareaHecha.getExecutedBy() > 0) {
            User userTareaHecha = getIdentityAPI().getUser(elementoTareaHecha.getExecutedBy());
            try {
                List<UserMembership> lst_membership = getIdentityAPI().getUserMemberships(elementoTareaHecha.getExecutedBy(), 0, 1, UserMembershipCriterion.ASSIGNED_DATE_ASC);
                UserMembership rolUserTareaHecha = lst_membership.get(0);
                solicitudHecha.setRolEjecutorTarea(rolUserTareaHecha.getRoleName());
            } catch (Exception e) {
                solicitudHecha.setRolEjecutorTarea("");
            }
            solicitudHecha.setUsuarioEjecutorTarea(userTareaHecha.getUserName());
        }

        List<ArchivedDataInstance> datosTareaArchivada = getProcessAPI().getArchivedActivityDataInstances(elementoTareaHecha.getSourceObjectId(), 1, 200);
        Iterator<ArchivedDataInstance> itrDataTarea = datosTareaArchivada.iterator();
        while (itrDataTarea.hasNext()) {
            ArchivedDataInstance elementDataCaso = (ArchivedDataInstance) itrDataTarea.next();
            String nombre = elementDataCaso.getName();
            String valor = elementDataCaso.getValue() == null ? ConstantesEnum.CADENA_VACIA.getNombre() : elementDataCaso.getValue().toString();
            setearDetallesTarea(solicitudHecha, nombre, valor, params);
        }
    }

    private Solicitud setearDatosGeneralesSolicitudHecha(ArchivedProcessInstance elementoSolicitud, List<Config> params) {
        Solicitud solicitudAux = new Solicitud();
        solicitudAux.setNroSolicitud(String.valueOf(elementoSolicitud.getRootProcessInstanceId()));
        solicitudAux.setFechaLlegada(Utils.convertirDateEnCadena(elementoSolicitud.getStartDate(), ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
        List<ArchivedDataInstance> datosArchivadosAdicionales = getProcessAPI().getArchivedProcessDataInstances(elementoSolicitud.getRootProcessInstanceId(), 1, 200);
        Iterator<ArchivedDataInstance> itrDataCaso = datosArchivadosAdicionales.iterator();
        while (itrDataCaso.hasNext()) {
            ArchivedDataInstance elementDataCaso = (ArchivedDataInstance) itrDataCaso.next();
            String nombre = elementDataCaso.getName();
            String valor = elementDataCaso.getValue() == null ? ConstantesEnum.CADENA_VACIA.getNombre() : elementDataCaso.getValue().toString();
            setearDetalleSolicitud(solicitudAux, nombre, valor, params);
        }
        return solicitudAux;
    }

    private Solicitud setearDatosGeneralesSolicitudPendiente(ProcessInstance elementoSolicitud, List<Config> params) {
        Solicitud solicitudAux = new Solicitud();
        solicitudAux.setNroSolicitud(String.valueOf(elementoSolicitud.getRootProcessInstanceId()));
        solicitudAux.setFechaLlegada(Utils.convertirDateEnCadena(elementoSolicitud.getStartDate(), ConstantesEnum.FORMATO_FECHA_COMPLETA.getNombre()));
        List<DataInstance> datosPendientesAdicionales = getProcessAPI().getProcessDataInstances(elementoSolicitud.getRootProcessInstanceId(), 1, 200);

        Iterator<DataInstance> itrDataCaso = datosPendientesAdicionales.iterator();
        while (itrDataCaso.hasNext()) {
            DataInstance elementDataCaso = (DataInstance) itrDataCaso.next();
            String nombre = elementDataCaso.getName();
            String valor = elementDataCaso.getValue() == null ? ConstantesEnum.CADENA_VACIA.getNombre() : elementDataCaso.getValue().toString();
            setearDetalleSolicitud(solicitudAux, nombre, valor, params);
        }
        if (!(solicitudAux.getEstado() != null && solicitudAux.getEstado().equals(""))) {
            solicitudAux.setEstado(get_last_status(elementoSolicitud.getRootProcessInstanceId()));
        }
        return solicitudAux;
    }

    private String get_last_status(Long process_instance_ID) {
        String last_status = "";
        try {
            DataInstance data_instance = getProcessAPI().getProcessDataInstance("estado_solicitud", process_instance_ID);
            last_status = data_instance.getValue().toString();
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }
        return last_status;
    }

    private void setearDetalleSolicitud(Solicitud solicitudAux, String nombre, String valor, List<Config> params) {
        for (int i = 0; i < params.size(); i++) {
            if(nombre.equalsIgnoreCase(params.get(i).getValColumn2())) {
                solicitudAux.put(nombre, valor);
            }
        }
    }

    private void setearDetallesTarea(Solicitud solicitudAux, String nombre, String valor, List<Config> params) {
        for (int i = 0; i < params.size(); i++) {
            if(nombre.equalsIgnoreCase(params.get(i).getValColumn2())) {
                solicitudAux.put(nombre, valor);
            }
        }
    }
}
