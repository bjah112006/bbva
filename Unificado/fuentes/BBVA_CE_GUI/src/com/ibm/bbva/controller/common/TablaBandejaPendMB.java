package com.ibm.bbva.controller.common;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.grupobbva.sce.tc84.CtBodyRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRs;
import pe.com.grupobbva.sce.tc84.CtHeader;
import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bpd.RemoteUtils;
import bbva.ws.api.view.BBVAFacadeLocal;
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.cm.service.impl.Documento;
import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes; 
import com.ibm.bbva.controller.form.BandejaReasigTareasMB;
import com.ibm.bbva.entities.ClienteNatural;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.Horario;
import com.ibm.bbva.entities.HorarioOficina;
import com.ibm.bbva.entities.RetraccionTarea;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.entities.TipoCambioCE;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.HorarioOficinaBeanLocal;
import com.ibm.bbva.session.RetraccionTareaBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TareaPerfilBeanLocal;
import com.ibm.bbva.session.TipoCambioCEBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import com.ibm.bbva.tabla.core.constante.Constante;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHorario;
import com.ibm.bbva.util.ComparatorFactory;
import com.ibm.bbva.util.ExpedienteTCWrapper;
import com.ibm.bbva.util.OrdernarBandejaPendientes;
import com.ibm.bbva.util.Util;
import com.ibm.bbva.util.comparators.ComparatorBase;

@SuppressWarnings("serial")
@ManagedBean(name = "tablaBandejaPend")
@RequestScoped
public class TablaBandejaPendMB extends AbstractSortPagDataTableMBean {
	
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private TareaBeanLocal tareaBean;
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	@EJB
	private EmpleadoBeanLocal empleadoBean;
	@EJB
	private TipoClienteBeanLocal tipoClienteBean;
	@EJB
	private HistorialBeanLocal historialBean;
	@EJB
	private FacadeLocal facade;	
	@EJB
	private TareaPerfilBeanLocal tareaPerfilBean;
	@EJB
	private RetraccionTareaBeanLocal retraccionTareaBean;
	@EJB
	private AnsBeanLocal ansBean;	
	@EJB
	private TipoCambioCEBeanLocal tipoCambioCEBeanLocal;
	@EJB
	private HorarioOficinaBeanLocal horarioOficinaBean;	
	
	
	private final String TAREA_MANT_GEST_CM = "50";
	private final String TAREA_MANT_GEST_SERVICIOS = "51";

	private HtmlDataTable tablaBinding;
	private List<ExpedienteTCWrapper> listTabla;
	private String texto;
	private String passedValue;
	private String codUsuario;
	private boolean desSeleccionado;
	private boolean renderedRa = false;
	private boolean renderedRol = false;
	private boolean renderedUsr = false;
	private boolean renderedProd = false;
	private boolean renderedSprod = false;
	private boolean renderedMon = false;
	private boolean renderedMontoSol = false;
	private boolean renderedTerr = false;
	private boolean renderedCodRVGL = false;
	private boolean renderedNumContrato = false;
	private boolean ejecDblClick = false;
	private boolean renderedDevoluciones;
	private boolean renderedRe;
	private List<Documento> listaDocumentosCM;
	private DocumentoExpTc objDocumentoExpTc;	
	private String numRegistros;
	private Map<Long, ArrayList<Horario>> mapaDatosHorarioOficina;

	private static final Logger LOG = LoggerFactory.getLogger(TablaBandejaPendMB.class);
			
	public TablaBandejaPendMB() {
		
	}
	
	@PostConstruct
	public void init() {
        String nombJSP = getNombreJSPPrincipal();     
        
        mapaDatosHorarioOficina = (Map<Long, ArrayList<Horario>>) getObjectSession(Constantes.LISTA_HORARIO_OFICINA);
        if(mapaDatosHorarioOficina==null){
        	mapaDatosHorarioOficina= new TreeMap<Long, ArrayList<Horario>>();
        	crearMapHorario(mapaDatosHorarioOficina);
        }else
        	LOG.info("Lista horario oficina ya esta cargada");
        
        if (nombJSP.equals("formBandejaPendientes")) {
        	inicializabandejaPendientes();
        	ejecDblClick = true;
  		}else if (nombJSP.equals("formBandejaBusqueda")) {  
  			//actualizarLista ();
  			inicializabandejaBusqueda();
        	ejecDblClick = true;
  		}else if (nombJSP.equals("formBandejaReasigTareas")) {
 			inicializabandejaReasigTareas();
        	ejecDblClick = false;
 		}
        
      
	}
	
	private void crearMapHorario (Map<Long, ArrayList<Horario>> mapa) {
		LOG.info("Cargando Lista horario oficina....");

		List<HorarioOficina> lista = horarioOficinaBean.buscarTodaOficinaActivosHorarios();
		
		for(HorarioOficina ho: lista){
			if(ho!=null && ho.getOficina()!=null){
				ArrayList<Horario> listHorario = mapa.get(ho.getOficina().getId());
				if (listHorario==null) {
					listHorario = new ArrayList<Horario> ();
					mapa.put(ho.getOficina().getId(), listHorario);
				}
				listHorario.add(ho.getHorario());				
			}
		}

		addObjectSession(Constantes.LISTA_HORARIO_OFICINA, mapa);
		
	}	
	
	public void actualizarLista () {
		LOG.info("Metodo actualizarLista");
		renderedProd = true;
		renderedSprod = true;
		renderedMon = true;
		renderedMontoSol = true;
		renderedTerr = true;
		renderedCodRVGL = true;
		renderedNumContrato = true;
		
		listTabla = new ArrayList<ExpedienteTCWrapper> ();
		
		List<ExpedienteTCWPS> lista = (List<ExpedienteTCWPS>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		List<RetraccionTarea> listRetraccionTarea = new ArrayList<RetraccionTarea>();
		List<TareaPerfil> listTareaPerfil = new ArrayList<TareaPerfil>();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		
		if(lista!=null && lista.size()>0){
			LOG.info("lista "+lista.size());
			
			if (getNombreJSPPrincipal().equals("formBandejaBusqueda")){
				if(empleado != null && empleado.getPerfil() != null){
					listTareaPerfil = tareaPerfilBean.buscarPorIdPerfil(empleado.getPerfil().getId());
					if(listTareaPerfil!=null && listTareaPerfil.size()>0)
						listRetraccionTarea = retraccionTareaBean.buscarPorFlagRetraerTareaAnt(listTareaPerfil.get(0).getTarea().getId());
				}				
			}

			AyudaHorario ayudaHorario=null;
			
			for (ExpedienteTCWPS etc : lista){
				
				//try{
					//LOG.info("etc.getIdOficina() = "+etc.getIdOficina()+" observacion: "+etc.getObservacion());
					if(etc.getObservacion()==null || etc.getObservacion().trim().equals("")){
						etc.setObservacion(Constantes.OBSERVACION_NO_REGISTRADA);
					}else{
						etc.setObservacion(Constantes.OBSERVACION_REGISTRADA);
					}
					
					if(etc.getIdOficina()!=null && !etc.getIdOficina().equals("")){
						/*String nombJSP = getNombreJSPPrincipal();         
				        if (nombJSP.equals("formBandejaBusqueda")) {
				        	System.out.println("band-busq --> etc.getCodigoUsuarioActual() : "+etc.getCodigoUsuarioActual());
				        	Empleado emp = empleadoBean.buscarPorCodigo(etc.getCodigoUsuarioActual());
				        	if(emp!=null && emp.getOficina()!=null)
				        		ayudaHorario = new AyudaHorario(emp.getOficina().getId());				        	
				        }else{
				        	ayudaHorario = new AyudaHorario(empleado.getOficina().getId());
				        }*/
						
				        /*if (etc!=null && etc.getCodigoUsuarioAnterior()!=null){
							LOG.info("no mostrar boton retraccion: "+etc.getCodigoUsuarioAnterior() );
						}*/
						//verificamos las retracciones que pertencen al usuario.
						if(etc!=null && etc.getFlagRetraer()!=null && etc.getFlagRetraer().equals(Constantes.FLAGRETRAERUNO)){
							if(!etc.getCodigoUsuarioAnterior().equalsIgnoreCase(empleado.getCodigo()) ){
									etc.setFlagRetraer("0");
								}
							
						}
						
						/*if(ayudaHorario!=null)
							listTabla.add(new ExpedienteTCWrapper(etc, ayudaHorario, tareaBean, bbvaFacade,	expedienteBean, tipoClienteBean, ansBean));
						else{							
							if(etc.getTaskID().equals("-1") || etc.getIdTarea().equals(TAREA_MANT_GEST_CM) || etc.getIdTarea().equals(TAREA_MANT_GEST_SERVICIOS))
								listTabla.add(new ExpedienteTCWrapper(etc, ayudaHorario, tareaBean, bbvaFacade,	expedienteBean, tipoClienteBean, ansBean));
						}*/
												
						if(etc.getTaskID().equals("-1") || etc.getIdTarea().equals(TAREA_MANT_GEST_CM) || etc.getIdTarea().equals(TAREA_MANT_GEST_SERVICIOS))
							listTabla.add(new ExpedienteTCWrapper(etc, ayudaHorario, tareaBean, bbvaFacade,	expedienteBean, tipoClienteBean, ansBean));
						else
							listTabla.add(new ExpedienteTCWrapper(etc, ayudaHorario, tareaBean, bbvaFacade,	expedienteBean, tipoClienteBean, ansBean));
								
												

					}/*else{
						LOG.info("etc.getIdOficina() es nulo o vacio");
					}*/
					
			/*	} catch (NamingException e) {
					e.printStackTrace();
				}	*/
				/*El proceso envia el flag de retraer para los perfiles que lo puedan realizar*/

				//if (getNombreJSPPrincipal().equals("formBandejaPendientes")) { //JBTA
				if (getNombreJSPPrincipal().equals("formBandejaBusqueda")) {
					if (etc.getFlagRetraer()!=null && etc.getFlagRetraer().equals(Constantes.FLAGRETRAERUNO)) {
						//LOG.info("etc es Retraer");
						//Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
						//if(empleado != null && empleado.getPerfil() != null){
							//listTareaPerfil = tareaPerfilBean.buscarPorIdPerfil(empleado.getPerfil().getId());
							//if(listTareaPerfil != null && listTareaPerfil.size() > 0){
								//LOG.info("Tamaño listTareaPerfil .. "+listTareaPerfil.size());
							//	listRetraccionTarea = retraccionTareaBean.buscarPorFlagRetraer(Long.parseLong(etc.getIdTarea()), listTareaPerfil.get(0).getTarea().getId());
								if(listRetraccionTarea!=null && listRetraccionTarea.size()>0){
									//LOG.info("Tamaño listRetraccionTarea ... "+listRetraccionTarea.size());
									for (RetraccionTarea obj : listRetraccionTarea ){
									//	Long id=listTareaPerfil.get(0).getTarea().getId();
									  if (obj!=null && obj.getLlegada()==Long.parseLong(etc.getIdTarea())){
										  this.setRenderedRe(true);
										  break;
										}
									}//fin for									
								}/*else
									LOG.info("Tamaño listRetraccionTarea es nulo o vacio.. ");*/

								//LOG.info("RenderedRe..."+this.isRenderedRe());
							//}else
								//LOG.info("Tamaño listTareaPerfil nulo o vacio.. ");
						//}
					}/*else
						LOG.info("etc no es Retraer");*/
				}//Fin IF formBandejaBusqueda
			}//Fin FOR lista			
			
		}else
			LOG.info("lista es nulo o vacio");

		if(listTabla!=null){
			this.setNumRegistros(String.valueOf(this.getListTabla().size())+" registros encontrados");
			LOG.info("ListTabla tamaño::::"+this.getListTabla().size());
		}else{
			this.setNumRegistros("0 registros encontrados");
			LOG.info("ListTabla es nulo");
		} 		

		if(lista!=null && lista.size()>0){

			
			/*Ordena Lista por Tipo Cliente*/			
			//Collections.sort(listTabla, new OrdernarBandejaPendientes(false, true));
			/*Ordena Lista por Fecha*/
			//Collections.sort(listTabla, new OrdernarBandejaPendientes(false, false));
			LOG.info("listTabla ordenada");
		}
		
		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO, listTabla);
		
		//List<ExpedienteTCWrapper> listaCount = (List<ExpedienteTCWrapper>) getObjectSession(Constantes.LISTA_PEND);
		
		if(listTabla!=null && listTabla.size()>0){
			LOG.info("listTabla es nulo o vacio");
			actualiarAyudaHorario(null);
		}else{
			
			LOG.info("listaCount tamaño:::"+listTabla.size());
		}		
 		
	}
	
	public void inicializabandejaPendientes() {
		renderedRa = false; 
		renderedProd = true;
		renderedSprod = true;
		renderedDevoluciones = true;
	}
	
	public void inicializabandejaBusqueda() {
		renderedRol = true;
		renderedUsr = true;
	}
	
	public void inicializabandejaHistorica() {
		renderedProd = true;
		renderedSprod = true;	
		renderedMon = true;
	}	
	
	public void inicializabandejaReasigTareas() {
		renderedRa = true;
		renderedRol = true;
		renderedUsr = true;
	}
	
	public String  seleccionaFila() {
		String nombreNavegacionWeb = getRequestParameter("nombreNavegacionWeb");				
		//Expediente exp = obtenerExpediente(codigo);
		
		
		Expediente expediente = iniciarExpediente();
		
		LOG.info("getNombreJSPPrincipal()= "+getNombreJSPPrincipal());
		if (getNombreJSPPrincipal().equals("formBandejaPendientes")) {
			if (nombreNavegacionWeb.equals("aprobarExpediente") || nombreNavegacionWeb.equals("ejecutarEvalCrediticia")
					|| nombreNavegacionWeb.equals("revisarRegistrarDictamen") || nombreNavegacionWeb.equals("evaluarFactibilidadOp")) {
				copiarLineaCredito(expediente);
			}
			expediente.getExpedienteTC().setComentarioRechazo("");
					
			
			addObjectSession(Constantes.EXPEDIENTE_ESTADO, String.valueOf(expediente.getId()));
			addObjectSession(Constantes.NUEVO_CLIENTE, "1");
			
			Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
			expediente.setEmpleado(empleado);
			
			if (nombreNavegacionWeb.indexOf("registrarExpediente")!=-1) {
				FacesContext ctx = FacesContext.getCurrentInstance();  
				BuscarClienteMB buscarCliente = null;		
						
				buscarCliente = (BuscarClienteMB)  
							 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarCliente");
				
				buscarCliente.setTipoDOISeleccionado(String.valueOf(expediente.getClienteNatural().getTipoDoi().getId()));
				buscarCliente.setNumeroDOI(expediente.getClienteNatural().getNumDoi());
				
				addObjectSession(Constantes.CONSIDERAR_TAREA_1, "1");
				addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, 0);
				
				removeObjectSession(Constantes.LISTA_DOC_EXP_ADJ);
				removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
				removeObjectSession(Constantes.CLEANTRANSFERDIR);
				removeObjectSession(Constantes.LIST_DOC_TRANSF);
			//	removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
				removeObjectSession("strListaDocsTransferencias");
				removeObjectSession("reutilizablesSeleccionados");
				removeObjectSession("listaDocReuSession");
				removeObjectSession("tamListaGuiaDoc");
				
			}
			//verificarTipoCambio();
			
			listTabla=null;
			removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
			removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
			
			removeObjectSession("strListaDocsTransferencias");
			removeObjectSession("reutilizablesSeleccionados");
			removeObjectSession("listaDocReuSession");
			removeObjectSession("tamListaGuiaDoc");
			LOG.info("nombreNavegacionWeb= "+nombreNavegacionWeb);
			return nombreNavegacionWeb;
		} else {
			listTabla=null;
			//removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
			removeObjectSession("strListaDocsTransferencias");
			removeObjectSession("reutilizablesSeleccionados");
			removeObjectSession("listaDocReuSession");
			removeObjectSession("tamListaGuiaDoc");
			return "/visualizarExpediente/formVisualizarExpediente?faces-redirect=true";
		}
	}
	
	private void copiarLineaCredito (Expediente expediente) {
		// si no se tiene la linea de credito se copia de las solicitadas
		Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
		Long monLinCredAprob = expediente.getExpedienteTC().getTipoMonedaAprob().getId();
		if (linCredAprob == null || monLinCredAprob == null) {
			expediente.setExpedienteTC(expediente.getExpedienteTC());
		}
	}
	
	private Expediente iniciarExpediente () {
		eliminarObjetosSession();
		
		String codigo = getRequestParameter("codigo");
		String taskID = getRequestParameter("taskID");
		
		LOG.info("codigo : "+codigo);
		LOG.info("taskID : "+taskID);
		
		Timestamp fechaActivado = Timestamp.valueOf(getRequestParameter("activado"));
			
		ExpedienteTCWPS expedienteTC = null;

		for (ExpedienteTCWrapper expTC : listTabla) {
			if (taskID.equals(expTC.getTaskID())) {
				LOG.info("taskID iguales::::"+taskID);
				addObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION, expTC.getExpedienteTC());
				addObjectSession(Constantes.ESTADO_TARJETA, expTC.getEstadoTarjeta());
				expedienteTC = expTC.getExpedienteTC();
				break;
			}
		}

		Expediente expediente = obtenerExpediente(codigo);
		
		//Actualiza estado activo de Expediente para el momento de la reasignación	
		expedienteBean.actualizarEstadoExpediente(Constantes.FLAG_ESTADO_ACTIVO_EXPEDIENTE, expediente.getId());
		
		RemoteUtils bandeja = new RemoteUtils();
				
		Expediente expedienteEntrada = obtenerExpediente(codigo);
		//expediente.setNumTerminal(Util.obtenerIp());
		
		if(expedienteTC != null){
			/**
			 * Si el ID de Tarea existe, continuar
			 * con la lógica.
			 */
			if(expedienteTC.getIdTarea() != null){
			
				LOG.info("getNombreJSPPrincipal()= "+getNombreJSPPrincipal());
				
				if(!expedienteTC.getIdTarea().equals(TAREA_MANT_GEST_CM)){
					LOG.info("getNombreJSPPrincipal()= "+getNombreJSPPrincipal());
					
					if (getNombreJSPPrincipal().equals("formBandejaPendientes")){
						try{
							Tarea tarea = tareaBean.buscarPorId(Long.parseLong(expedienteTC.getIdTarea()));
							//VER:::Se necesita la descripcion???, xk buscar la tarea???
							LOG.info("tarea id:::"+expedienteTC.getIdTarea());
							LOG.info("tarea des:::"+expedienteTC.getDesTarea());
							expediente.getExpedienteTC().setTarea(tarea);
						}catch(Exception ex){
							LOG.error("No se encontró la tarea " + expedienteTC.getIdTarea());
						}
						
						if(expediente.getExpedienteTC().getTarea() != null){
							if(expediente.getExpedienteTC().getTarea().getId()!=Constantes.ID_TAREA_1 && expediente.getEstado().getId()!=Constantes.ESTADO_EN_REGISTRO_TAREA_1){
								LOG.info("Actualizacion de TI y T2 ");
								
								LOG.info("Fecha de activación =========> " + fechaActivado.toString());
								expediente.getExpedienteTC().setFechaT2(new Timestamp(bandeja.obtenerTimestampServidorProcess().getTimeInMillis()));
								expediente.getExpedienteTC().setFechaT1(fechaActivado); /*fecha de asignacion del processo*/
								LOG.info("Fecha y Hora fecha 1:::"+expediente.getExpedienteTC().getFechaT1().toString());
								LOG.info("Fecha y Hora fecha 2:::"+expediente.getExpedienteTC().getFechaT2().toString());
								
								
							}else{
								LOG.info("Tarea es tarea 1 o en pre-registro");
							}
						}else
							LOG.info("TAREA ES NULL");
						
					}else{
						expediente.getExpedienteTC().setTarea(new Tarea());
						expediente.getExpedienteTC().getTarea().setId(Long.parseLong(expedienteTC.getIdTarea()));
						LOG.info("tarea des:::"+expedienteTC.getDesTarea());
						expediente.getExpedienteTC().getTarea().setDescripcion(expedienteTC.getDesTarea());
					}
					
					if(expediente.getExpedienteTC().getTarea() != null){
						LOG.info("TAREA ID = "+expediente.getExpedienteTC().getTarea().getId());
						LOG.info("TAREA DESCRIP = "+expediente.getExpedienteTC().getTarea().getDescripcion());
						LOG.info("ESTADO ID = "+expediente.getEstado().getId());
						LOG.info("ESTADO DESCRIP = "+expediente.getEstado().getDescripcion());
					}
					

				}
			/**
			 * Si el ID de Tarea no existe, entonces
			 * es un expediente 'En proceso'.
			 */
				
			}else{
				LOG.info("Expediente en Proceso : " + codigo);
			}
		}

		if(expediente.getExpedienteTC().getFlagRetraer() != null){
			if (expediente.getExpedienteTC().getFlagRetraer().equals(Constantes.FLAGRETRAERD))
				expediente.getExpedienteTC().setFlagRetraer(Constantes.FLAGRETRAERN);
		}
		
		/**
		 * Obtiene URL Documento CM - 03 Abril 2014
		 * */
		LOG.info("Consulta CM - Inicio");
		objDocumentoExpTc= new DocumentoExpTc();
		objDocumentoExpTc.setExpediente(new Expediente());
		objDocumentoExpTc.getExpediente().setClienteNatural(new ClienteNatural());		
		objDocumentoExpTc.getExpediente().getClienteNatural().setNumDoi(expediente.getClienteNatural().getNumDoi());
		//LOG.info("Num Doi 1 ="+expediente.getClienteNatural().getNumDoi());
		LOG.info("Num Doi ="+objDocumentoExpTc.getExpediente().getClienteNatural().getNumDoi());
		
		long tiempoInicio = System.currentTimeMillis();
		try{
			listaDocumentosCM = facade.obtenerListaDocumentoCM(objDocumentoExpTc);
			LOG.info("Consulta CM - Fin");
		}catch(Exception e){
			LOG.info("e:::"+e.toString());
			e.getStackTrace();
		}
		long tiempoFin = System.currentTimeMillis();
		LOG.info("TablaBandejaPendMB.iniciarExpediente obtenerListaDocumentoCM demora: " + (tiempoFin - tiempoInicio) + " milisegundos");
		
		Map<String, Object> mapListDocumentosCM =new TreeMap<String, Object> ();
		
		if(listaDocumentosCM!=null)
			for(Documento objDocumento: listaDocumentosCM){
				mapListDocumentosCM.put(String.valueOf(objDocumento.getId()), objDocumento);
			}
		else
			LOG.info("listaDocumentosCM es nulo");
		
		addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);
		addObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE, expedienteEntrada );
		addObjectSession(Constantes.NOMBRE_BANDEJA_SESION, getNombreJSPPrincipal());	
		addObjectSession(Constantes.EXPEDIENTE_LISTA_DOCUMENTO_CM, mapListDocumentosCM);
		
		return expediente;
	} 
	
	public String retraer() {		
		
        Expediente expediente = iniciarExpediente();
                        
        expediente.getExpedienteTC().setFlagRetraer(Constantes.FLAGRETRAERR);       
        expediente.getExpedienteTC().setFechaT2(null);
        expediente.getExpedienteTC().setFechaT3(null);
        
        /**
		 * @author Daniel Flores
		 * 
		 * Obtener el estado anterior del expediente y 
		 * reestablecerlo. Esto se realizará con la consulta
		 * al historial del expediente.
		 * 
		 * Guardamos el estado actual para luego guardarlo
		 * como parte del historial del expediente, este tendrá
		 * un flag de  RETRAER.
		 * 
		 */
		
		Estado ultimoEstado = expediente.getEstado();
		
		Historial hist = historialBean.buscarMasRecienPorId(expediente.getId());
		expediente.setEstado(hist.getEstado());
        
        //Colocar la actualizacion al proceso - PENDIENTE        
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this, Constantes.ACCION_RETRAER, null);
		
		String tkiid = expedienteTCWPS.getTaskID();
		RemoteUtils objRemoteUtils=new RemoteUtils();
		
		if(hist.getEstado() != null)
			expedienteTCWPS.setEstado(hist.getEstado().getDescripcion());
		
		objRemoteUtils.completarTarea(tkiid,expedienteTCWPS);
		
		/** Guardamos el expediente con el estado anterior **/		
        expedienteBean.edit(expediente);
        
        /** En el historial se guarda el expediente con el estado inicial que después
         * fue cambiado al que tenía anteriormente.
         */
        expediente.setEstado(ultimoEstado);
		Historial historial = ConvertExpediente.convertToHistorialVO(expediente);			
		historialBean.create(historial);
		
        /*carga nuevamente la lista*/
		/*FacesContext ctx = FacesContext.getCurrentInstance();  
		BuscarBandejaPendMB buscarBandejaPend = (BuscarBandejaPendMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaPend");
		buscarBandejaPend.buscar();*/

		Consulta objConsulta=new Consulta();
		
		List<String> listUsuarios=new ArrayList<String>();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		listUsuarios.add(empleado.getCodigo()); 
		objConsulta.setUsuarios(listUsuarios);		
		ayudaExpedienteTC.actualizarListaExpedienteTC(objConsulta);
		
	    return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}
	
	public void setTablaBinding(HtmlDataTable tablaBinding) {
		this.tablaBinding = tablaBinding;
	}

	public HtmlDataTable getTablaBinding() {
		return tablaBinding;
	}

	public List<ExpedienteTCWrapper> getListTabla() {
		List<ExpedienteTCWrapper> listTemp=(List<ExpedienteTCWrapper>)getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
		if(listTemp!=null && listTemp.size()>0)
			listTabla=listTemp;
		return listTabla;
	}

	public void setListTabla(List<ExpedienteTCWrapper> listTabla) {
		this.listTabla = listTabla;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getPassedValue() {
		return passedValue;
	}

	public void setPassedValue(String passedValue) {
		this.passedValue = passedValue;
	}

	public boolean isRenderedRa() {
		return renderedRa;
	}

	public void setRenderedRa(boolean renderedRa) {
		this.renderedRa = renderedRa;
	}

	public boolean isRenderedRol() {
		return renderedRol;
	}

	public void setRenderedRol(boolean renderedRol) {
		this.renderedRol = renderedRol;
	}

	public boolean isRenderedUsr() {
		return renderedUsr;
	}

	public void setRenderedUsr(boolean renderedUsr) {
		this.renderedUsr = renderedUsr;
	}

	public boolean isRenderedProd() {
		return renderedProd;
	}

	public void setRenderedProd(boolean renderedProd) {
		this.renderedProd = renderedProd;
	}

	public boolean isRenderedSprod() {
		return renderedSprod;
	}

	public void setRenderedSprod(boolean renderedSprod) {
		this.renderedSprod = renderedSprod;
	}
	
	public boolean isRenderedMon() {
		return renderedMon;
	}
	
	public void setRenderedMon(boolean renderedMon) {
		this.renderedMon = renderedMon;
	}
	
	public boolean isEjecDblClick() {
		return ejecDblClick;
	}

	public void setEjecDblClick(boolean ejecDblClick) {
		this.ejecDblClick = ejecDblClick;
	}

	@Override
	public void ordenar (ActionEvent event) {
		LOG.info("metodo ordenar");
		String sortFieldAttribute = getAttribute(event, "sortField");
		if (sortFieldAttribute != null) {
	        if (sortField != null && sortField.equals(sortFieldAttribute)) {
	            sortAscending = !sortAscending;
	        } else {
	            sortField = sortFieldAttribute;
	            sortAscending = true;
	        }
	        // regresa a la primera página si cambia el orden
	        dataTable.setFirst(0);
		} // si no viene el atributo sortField no cambia el ordenamiento
        
		String columna = this.sortField;
		String orden = this.sortAscending ? ComparatorBase.SORT_ORDER_ASC : ComparatorBase.SORT_ORDER_DESC;
		Comparator<ExpedienteTCWPS>  comparator = null;
		if ("estado".equals(columna)) {
			comparator = ComparatorFactory.estado(orden);
		} else if ("rol".equals(columna)) {
			comparator = ComparatorFactory.rol(orden);
		} else if ("usuario".equals(columna)) {
			comparator = ComparatorFactory.usuario(orden);
		} else if ("codigoTarea".equals(columna)) {
			comparator = ComparatorFactory.tarea(orden);
		} else if ("segmento".equals(columna)) {
			comparator = ComparatorFactory.segmento(orden);
		} else if ("tipoOferta".equals(columna)) {
			comparator = ComparatorFactory.tipoOferta(orden);
		} else if ("tipoDoi".equals(columna)) {
			comparator = ComparatorFactory.tipoDOI(orden);
		} else if ("doi".equals(columna)) {
			comparator = ComparatorFactory.numeroDOI(orden);
		} else if ("apPaterno".equals(columna)) {
			comparator = ComparatorFactory.apPaterno(orden);
		} else if ("apMaterno".equals(columna)) {
			comparator = ComparatorFactory.apMaterno(orden);
		} else if ("nombres".equals(columna)) {
			comparator = ComparatorFactory.nombre(orden);
		} else if ("producto".equals(columna)) {
			comparator = ComparatorFactory.producto(orden);
		} else if ("subProducto".equals(columna)) {
			comparator = ComparatorFactory.subProducto(orden);
		} else if ("moneda".equals(columna)) {
			comparator = ComparatorFactory.moneda(orden);
		} else if ("codigo".equals(columna)) {
			comparator = ComparatorFactory.codigo(orden);
		}
		if (comparator != null) {
			Collections.sort(listTabla, comparator);
		}
		LOG.info("entrara a Actualizar horario");
		actualiarAyudaHorario(null);
	}
	
	private Expediente obtenerExpediente(String codigo) {
		return expedienteBean.buscarPorId(Long.parseLong(codigo));
	}
	
	/*Inicio Bernabe Incidencia 198*/
	public String obtenerCodigoUsuario(String codiUsuario){
		LOG.info("codigoUsuario =" + codiUsuario);
		setCodigoUsuario(codiUsuario);
		addObjectSession(Constantes.CODIGO_USUARIO_ASIGNADO, codUsuario);
		return codUsuario;		
	}
	
	public boolean obtenerValorSeleccion(boolean seleccion){
		LOG.info("seleccionado = "+ seleccion);
		setDesSeleccionado(seleccion);
		return desSeleccionado;
		
	}
	
	public void cargaUsuariosAsignar2(){
		codUsuario= (String)getObjectSession(Constantes.CODIGO_USUARIO_ASIGNADO);		
		LOG.info("codigoUsuarioActual = "+codUsuario);
		List<String> listaUsuariosAsignar=new ArrayList<String>();
		List<ExpedienteTCWPS> listaSesion =  (List<ExpedienteTCWPS>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		int j=0;
		if (codUsuario!=null){
			if (listaSesion!=null){
				for (int i=0; i<listaSesion.size();i++){
					if (!codUsuario.equals(listaSesion.get(i).getCodigoUsuarioActual().toString())){
						listaUsuariosAsignar.add(j, listaSesion.get(i).getCodigoUsuarioActual());
						j++;
					}
				}			
			}
		}		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		BuscarBandejaAsigMB buscarBandejaAsig = (BuscarBandejaAsigMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "buscarBandejaAsig");
		
		//buscarBandejaAsig.cargarUsuariosAsignados(listaUsuariosAsignar);
		//setListTabla((List<ExpedienteTCWrapper>)getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION));
		listTabla=(List<ExpedienteTCWrapper>)getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		LOG.info("tamaño tabla ="+listTabla.size());
	    //buscarBandejaAsig.buscar();
		actualizarLista();
		
		/*habilita el boton Asignar despues que cargue el combo Asignar a..*/ 
		BandejaReasigTareasMB bandejaReasig = (BandejaReasigTareasMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "bandejaReasigTareas");		
		bandejaReasig.setDesAsignar(false);
		//listTabla.
	}
	
	/*Fin Bernabe Incidencia 198*/
		
	public void cargaUsuariosAsignar(String codigoUsuarioActual, boolean seleccion) { 
		int i=0;
		for (ExpedienteTCWrapper lista : listTabla) {
			if (lista.isSeleccion()) {
                i++;				
			}
		}
		LOG.info("codigoUsuarioActual = "+codigoUsuarioActual);
		Empleado empleado = empleadoBean.buscarPorCodigo(codigoUsuarioActual);		
		List<Empleado> listaEmpxPerfil = empleadoBean.buscarPorIdPerfil(empleado.getPerfil().getId());
		
		List<Empleado> listaEmp = new ArrayList<Empleado>();
		
		for (Empleado lista : listaEmpxPerfil) {			
			if (lista.getId() != empleado.getId()) {
				listaEmp.add(lista);
			}
		}
		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		BuscarBandejaAsigMB buscarBandejaAsig = (BuscarBandejaAsigMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "buscarBandejaAsig");
		
	
		
		
		
		if (i==0) {
			buscarBandejaAsig.setUsuariosAsig(Util.listaVacia());
		}else if (i==1) {
			addObjectSession(Constantes.LISTA_ASIGNAR_USUARIO_SESION, listaEmp);
			buscarBandejaAsig.cargarListaAsignar(listaEmp);
		}else{		
			List<Empleado> lista =  (List) getObjectSession(Constantes.LISTA_ASIGNAR_USUARIO_SESION);
		    buscarBandejaAsig.cargarListaAsignar(lista);
		}
		addObjectSession(Constantes.LISTA_USUARIOS_ASIG, buscarBandejaAsig.getUsuariosAsig());
		
	}
	
	private void verificarTipoCambio() {
		
		LOG.info(" Verificando Tipo de Cambio");
		String strFecha = Util.formatDateObject("dd/MM/yyyy", new Date());
		LOG.info("Fecha Actual:" + strFecha);
		Date fecha = Util.parseStringToDate(strFecha, "dd/MM/yyyy");	
		TipoCambioCE objTipoCambioCE = tipoCambioCEBeanLocal.buscarPorFecha(fecha);		
		if (objTipoCambioCE == null) {
			LOG.info("Se intenta obtener el tipo de cambio llamando al servicio");
			objTipoCambioCE = new TipoCambioCE();
			Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION); 
			CtExtraerTipoCambioRq inCtExtraerTipoCambioRq  	= null;
			CtExtraerTipoCambioRs outCtExtraerTipoCambioRs  = null;
			CtHeader ctHeader = null;
			CtBodyRq CtBodyRq = null;
			
			inCtExtraerTipoCambioRq = new CtExtraerTipoCambioRq();	
			outCtExtraerTipoCambioRs = new CtExtraerTipoCambioRs();
			ctHeader = new CtHeader();
			CtBodyRq = new CtBodyRq();
		
			strFecha = Util.formatDateObject("dd.MM.yyyy", new Date());
			ctHeader.setUsuario(empleado.getCodigo());			
			CtBodyRq.setFechaCambio(strFecha);
			CtBodyRq.setTipoCambio("P");
			 
			inCtExtraerTipoCambioRq.setHeader(ctHeader);
			inCtExtraerTipoCambioRq.setData(CtBodyRq);
			LOG.info("-------------------------- INICIO CONSULTA AL SERVICIO EXTRAER TIPO CAMBIO ----------------------------");	
			 
			try{
			 LOG.info("DATOS REQUEST SERVICIO TIPO CAMBIO: " +
			 		"["+ "Codigo=" + ctHeader.getUsuario() +"; " 
					   + "Fecha Cambio=" + CtBodyRq.getFechaCambio() + "]");			 
			 outCtExtraerTipoCambioRs = bbvaFacade.extraerTipoCambio(inCtExtraerTipoCambioRq);
			 
			}catch(Throwable e){
			 LOG.error("Ocurrio un error de conexion con el servicio TIPO CAMBIO " + e);
			}
			LOG.info("-------------------------- FIN CONSULTA AL SERVICIO EXTRAER TIPO CAMBIO ----------------------------");
		
			if(outCtExtraerTipoCambioRs != null 
					&& outCtExtraerTipoCambioRs.getData() != null){
				LOG.info("DATOS RESPONSE EXTRAER TIPO CAMBIO: " +	"["+"TipoCambioCE=" + outCtExtraerTipoCambioRs.getData().getFechaCambio() 
						+ " " + outCtExtraerTipoCambioRs.getData().getTipoCambio() + "]");
				LOG.info("Fecha WS:" + outCtExtraerTipoCambioRs.getData().getFechaCambio());
				LOG.info("Monto WS:" + outCtExtraerTipoCambioRs.getData().getTipoCambio());
				objTipoCambioCE.setMonto(BigDecimal.valueOf(Double.valueOf(outCtExtraerTipoCambioRs.getData().getTipoCambio())));
				objTipoCambioCE.setFecha(Util.parseStringToDate(outCtExtraerTipoCambioRs.getData().getFechaCambio(), "dd/MM/yyyy"));
				LOG.info("Insertamos el tipo de cambio extraido del servicio");
				tipoCambioCEBeanLocal.create(objTipoCambioCE);		
				LOG.info("Tipo de cambio para la fecha " + strFecha + " insertado en BD.");
			}
		}else{
			LOG.info("Ya existe tipo de cambio para la fecha actual.");
		}
	}	

	public boolean isRenderedDevoluciones() {
		return renderedDevoluciones;
	}

	public void setRenderedDevoluciones(boolean renderedDevoluciones) {
		this.renderedDevoluciones = renderedDevoluciones;
	}

	public boolean isRenderedRe() {
		return renderedRe;
	}

	public void setRenderedRe(boolean renderedRe) {
		this.renderedRe = renderedRe;
	}
	public boolean isRenderedMontoSol() {
		return renderedMontoSol;
	}

	public void setRenderedMontoSol(boolean renderedMontoSol) {
		this.renderedMontoSol = renderedMontoSol;
	}

	public boolean isRenderedTerr() {
		return renderedTerr;
	}

	public void setRenderedTerr(boolean renderedTerr) {
		this.renderedTerr = renderedTerr;
	}

	public boolean isRenderedCodRVGL() {
		return renderedCodRVGL;
	}

	public void setRenderedCodRVGL(boolean renderedCodRVGL) {
		this.renderedCodRVGL = renderedCodRVGL;
	}

	public boolean isRenderedNumContrato() {
		return renderedNumContrato;
	}

	public void setRenderedNumContrato(boolean renderedNumContrato) {
		this.renderedNumContrato = renderedNumContrato;
	}

	public String getCodUsuario() {
		return codUsuario;
	}

	public void setCodigoUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}

	public boolean isDesSeleccionado() {
		return desSeleccionado;
	}

	public void setDesSeleccionado(boolean desSeleccionado) {
		this.desSeleccionado = desSeleccionado;
	}

	public String getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(String numRegistros) {
		this.numRegistros = numRegistros;
	}

	@Override
	public void actualiarAyudaHorario(ActionEvent event) {
		// TODO Apéndice de método generado automáticamente
		int j, numReg, inicial;
		
		
		
		if(dataTable!=null){
			String num = (String) getObjectSession(Constantes.PAGINA_SESSION);
			if(num!=null && num.equals("-1")){
				numReg=20000;
			}else
				numReg=dataTable.getRows();
			
			j=dataTable.getFirst();
			inicial=dataTable.getFirst();
			
		}else{
			LOG.info("dataTable es nulo");
			j=0;
			inicial=0;
			numReg=10;
		}
			
		//int j=tablaBinding.getFirst();
		int i=0;
		boolean flag=false;
		String nombJSP = getNombreJSPPrincipal(); 
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		
		List<ExpedienteTCWrapper> listaCount = this.getListTabla();
		LOG.info("INICIAL:::"+j);
		LOG.info("NUM REGISTROS:::"+numReg);
		
		if(listaCount!=null && listaCount.size()>0)
			for(ExpedienteTCWrapper obj : listaCount){
				
				if (i==j || flag){
					flag=true;
					if(j<(inicial+ numReg)){  
				        if (nombJSP.equals("formBandejaBusqueda")) {
				        	LOG.info("band-busq --> etc.getCodigoUsuarioActual() : "+obj.getExpedienteTC().getCodigoUsuarioActual());
				        	Empleado emp = empleadoBean.buscarPorCodigo(obj.getExpedienteTC().getCodigoUsuarioActual());
				        	if(emp!=null && emp.getOficina()!=null){
				        		if(obj.getAyudaHorario()==null){
				        			try {
										obj.setAyudaHorario(new AyudaHorario(emp.getOficina().getId()));
										LOG.info("Se ha creado horario..");
									} catch (NamingException e) {
										// TODO Bloque catch generado automáticamente
										e.printStackTrace();
									}
				        		}else
				        			LOG.info("Ayuda horario no es null");
				        	}else
				        		LOG.info("Empleado o Oficina es null");
				        						        	
				        }else{
				        	if(obj.getAyudaHorario()==null){
				        		LOG.info("band-pend --> idExpediente : "+obj.getExpedienteTC().getCodigo());
				        		try {
									obj.setAyudaHorario(new AyudaHorario(empleado.getOficina().getId()));
									LOG.info("Se ha creado horario...");
								} catch (NamingException e) {
									// TODO Bloque catch generado automáticamente
									e.printStackTrace();
								}
				        	}else
			        			LOG.info("Ayuda horario no es null para id exp::"+obj.getExpedienteTC().getCodigo());
				        }
					}else{
						break;
					}
					j++;
				}else{
					i++;
				}
				
				
			}
		else 
			LOG.info("listaCount es nulo o vacio");
			
			addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO, listaCount);
		
	}
	
	

}