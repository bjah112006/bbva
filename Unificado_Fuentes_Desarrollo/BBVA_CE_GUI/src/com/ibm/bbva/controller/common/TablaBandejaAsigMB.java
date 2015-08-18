package com.ibm.bbva.controller.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bpd.RemoteUtils;
import bbva.ws.api.view.BBVAFacadeLocal;
import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.cm.service.impl.Documento;
import com.ibm.bbva.controller.AbstractSortPagDataTableMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.form.BandejaReasigTareasMB;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.Horario;
import com.ibm.bbva.entities.HorarioOficina;
import com.ibm.bbva.entities.RetraccionTarea;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TareaPerfil;
import com.ibm.bbva.session.AnsBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.HorarioOficinaBeanLocal;
import com.ibm.bbva.session.RetraccionTareaBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.session.TareaPerfilBeanLocal;
import com.ibm.bbva.session.TipoClienteBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaHorario;
import com.ibm.bbva.util.ComparatorFactory;
import com.ibm.bbva.util.ExpedienteTCWrapper;
import com.ibm.bbva.util.Util;
import com.ibm.bbva.util.comparators.ComparatorBase;

@SuppressWarnings("serial")
@ManagedBean(name = "tablaBandejaAsig")
@RequestScoped
public class TablaBandejaAsigMB extends AbstractSortPagDataTableMBean {

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
	private HorarioOficinaBeanLocal horarioOficinaBean;	
	
	private HtmlDataTable tablaBinding;
	private List<ExpedienteTCWrapper> listTabla;
	private String texto;
	private String passedValue;
	private String codUsuario = "";
	private String codExpediente = "";
	private boolean seleccionado;
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
	private boolean seleccionarTodos = false;
	private List<Documento> listaDocumentosCM;
	private DocumentoExpTc objDocumentoExpTc;
	private Map<Long, ArrayList<Horario>> mapaDatosHorarioOficina;
	
	private String numRegistros;
	
	private String cadExpSelecc;
	private String cadExpSeleccAnt;
	private String actionCheck;
	
	private static final Logger LOG = LoggerFactory.getLogger(TablaBandejaAsigMB.class);

	public TablaBandejaAsigMB() {

	}

	@PostConstruct
	public void init() {
		String nombJSP = getNombreJSPPrincipal();
		if (nombJSP.equals("formBandejaPendientes")) {
			inicializabandejaPendientes();
			ejecDblClick = true;
		} else if (nombJSP.equals("formBandejaBusqueda")) {
			// actualizarLista ();
			inicializabandejaBusqueda();
			ejecDblClick = true;
		} else if (nombJSP.equals("formBandejaReasigTareas")) {
			inicializabandejaReasigTareas();
			ejecDblClick = true;
		}
		listTabla = (List<ExpedienteTCWrapper>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		
		if(listTabla!=null && listTabla.size()>0){
			this.setNumRegistros(String.valueOf(this.getListTabla().size())+" registros encontrados");
			LOG.info("ListTabla tamaño::::"+this.getListTabla().size());
		}else{
			this.setNumRegistros("0 registros encontrados");
			LOG.info("ListTabla es nulo");
		}
		
        mapaDatosHorarioOficina = (Map<Long, ArrayList<Horario>>) getObjectSession(Constantes.LISTA_HORARIO_OFICINA);
        if(mapaDatosHorarioOficina==null){
        	mapaDatosHorarioOficina= new TreeMap<Long, ArrayList<Horario>>();
        	crearMapHorario(mapaDatosHorarioOficina);
        }else{
        	LOG.info("Lista horario oficina ya esta cargada");
    		if(mapaDatosHorarioOficina!=null)
    			LOG.info("Tamaño de mapa:::"+mapaDatosHorarioOficina.size());        	
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
		if(mapa!=null){
			LOG.info("Tamaño de mapa:::"+mapa.size());
			
			Iterator it = mapa.keySet().iterator();
			while(it.hasNext()){
			  Long key = (Long)it.next();
			  LOG.info("Clave: " + key.toString() );
			}
			
		}
			
		addObjectSession(Constantes.LISTA_HORARIO_OFICINA, mapa);
		
	}	

	public void limpiarLista() {
		listTabla = new ArrayList<ExpedienteTCWrapper>();
		setListTabla(listTabla);
	}

	public void actualizarLista() {
		renderedProd = true;
		renderedSprod = true;
		renderedMon = true;
		renderedMontoSol = true;
		renderedTerr = true;
		renderedCodRVGL = true;
		renderedNumContrato = true;
		listTabla=null;
		
		listTabla = new ArrayList<ExpedienteTCWrapper>();

		List<ExpedienteTCWPSWeb> lista = (List<ExpedienteTCWPSWeb>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		
		List<RetraccionTarea> listRetraccionTarea = new ArrayList<RetraccionTarea>();
		//List<TareaPerfil> listTareaPerfil = new ArrayList<TareaPerfil>();

		if (lista != null && lista.size() > 0){
			LOG.info("lista " + lista.size());
			seleccionarTodos = false;
		} else{
			LOG.info("lista es nulo o vacio");
			seleccionarTodos = false;
			/*ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			BuscarBandejaAsigMB buscarBandejaAsig = (BuscarBandejaAsigMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "buscarBandejaAsig");			
			List<Empleado> listaEmp = new ArrayList<Empleado>();		
			buscarBandejaAsig.cargarListaAsignar(listaEmp);*/
		}

		listRetraccionTarea = retraccionTareaBean.buscarTodos();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		//listTareaPerfil = tareaPerfilBean.buscarPorIdPerfil(empleado
			//	.getPerfil().getId());

		// lista.get(0).setFlagRetraer("1");
		// //<<<-----+++----BORRAR----+++++---------+++++++++++++++++++
		if (lista != null && lista.size() > 0) {
			Iterator<ExpedienteTCWPSWeb> iterador = lista.listIterator();
			while (iterador.hasNext()) {
				ExpedienteTCWPSWeb expeTCWPS = (ExpedienteTCWPSWeb) iterador.next();
				// Empleado empleadoVO =
				// empleadoBean.buscarPorCodigo(expeTCWPS.getCodigoUsuarioActual());
				AyudaHorario ayudaHorario=null;
				//try {
					//LOG.info("etc.getIdOficina() = " + expeTCWPS.getIdOficina());
					//if (expeTCWPS.getIdOficina() != null
						//	&& !expeTCWPS.getIdOficina().equals("")) {
						
						//ayudaHorario = new AyudaHorario(empleado.getOficina().getId());
						listTabla.add(new ExpedienteTCWrapper(expeTCWPS,
								ayudaHorario, tareaBean, bbvaFacade,
								expedienteBean, tipoClienteBean, ansBean));

					/*} else {
						LOG.info("etc.getIdOficina() es nulo o vacio");
					}*/

				/*} catch (NamingException e) {
					LOG.error(e.getMessage(), e);
				}*/
				//setListTabla(listTabla);
				/*
				 * El proceso envia el flag de retraer para los perfiles que lo
				 * puedan realizar
				 */
				//addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION,
					//	listTabla);
				
				//LOG.info("listTabla:::::"+listTabla.size());//VERIFICAR TAMAÑOOOOOOOO


			}//fin while
			
			/*Ordena Lista por Tipo Cliente*/			
			//Collections.sort(listTabla, new OrdernarBandejaPendientes(false, true));
			/*Ordena Lista por Fecha*/
			//Collections.sort(listTabla, new OrdernarBandejaPendientes(false, false));
			
			LOG.info("listTabla final:::::"+listTabla.size());//VERIFICAR TAMAÑOOOOOOOO
			addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION,
				listTabla);
		}

		if(this.getListTabla()!=null && this.getListTabla().size()>0){
			this.setNumRegistros(String.valueOf(this.getListTabla().size())+" registros encontrados");
			LOG.info("ListTabla tamaño::::"+this.getListTabla().size());
		}else{
			this.setNumRegistros("0 registros encontrados");
			LOG.info("ListTabla es nulo");
		}
		/*
		List<ExpedienteTCWrapper> listaCount = (List<ExpedienteTCWrapper>) getObjectSession(Constantes.LISTA_PEND);
		
		if(listaCount==null || listaCount.size()<=0){
			LOG.info("listaCount es nulo o vacio");
			actualiarAyudaHorario(null);
		}else{
			
			LOG.info("listaCount tamaño:::"+listaCount.size());
		}
		*/
		/**
		 * Ordenar Lista por Código de Expediente
		 * */
		if(listTabla!=null && listTabla.size()>0){
			
			LOG.info("Ordenando Lista por Código de Expediente");
			Comparator<ExpedienteTCWPSWeb> comparator = null;
			String orden = ComparatorBase.SORT_ORDER_ASC;
			
			comparator = ComparatorFactory.codigo(orden);
			if (comparator != null) {
				Collections.sort(listTabla, comparator);
				LOG.info("Orden realizado!");
			}
			
		}
		/**
		 * */

		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO, listTabla);
		
		//List<ExpedienteTCWrapper> listaCount = (List<ExpedienteTCWrapper>) getObjectSession(Constantes.LISTA_PEND);
		
		if(listTabla!=null && listTabla.size()>0){
			LOG.info("listTabla es nulo o vacio");
			if(dataTable!=null){
				dataTable.setFirst(0);
			}			
			actualiarAyudaHorario(null);
		}else{
			LOG.info("listaCount tamaño:::"+((listTabla==null) ? "null" : listTabla.size()));
		}	
		
		
		lista = null;
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
	
	/**
	 * 16/12/14
	 * Comentando.
	 * 
	 * Este método sirve para activar o desactivar el botón de 
	 * "Asignar" dependiendo de ciertos criterios.
	 */
	
	public void validarCantExpSeleccionados(){
		
		listTabla = (List<ExpedienteTCWrapper>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		BuscarBandejaAsigMB busqueda = (BuscarBandejaAsigMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaAsig");
		LOG.info("busqueda.getUsuarioAsigSeleccionado(): " + busqueda.getUsuarioAsigSeleccionado());
		
		String usuarioAsignadoSeleccionado =  busqueda.getUsuarioAsigSeleccionado();
		
		int cont=0;
		
		for (ExpedienteTCWrapper expTC : listTabla){
			if(expTC.isSeleccion()){
				cont++;
				break;
			}
		}	
		LOG.info("cont ::"+cont);
		
		if(cont>0 && !Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(usuarioAsignadoSeleccionado) && usuarioAsignadoSeleccionado != null){
			addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, false);
		}else
			addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, true);

	}
	
	/**
	 * 16/12/14
	 * Comentando.
	 * 
	 * Este método sirve para activar o desactivar el botón de 
	 * "Asignar" cuando se selecciona un usuario a asignar incluyendo
	 * además la evaluación de ciertos criterios como la selección al menos
	 * de un expediente.
	 */
	
	public void validarCantExpSeleccionados(AjaxBehaviorEvent event){
		
		listTabla = (List<ExpedienteTCWrapper>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		BuscarBandejaAsigMB busqueda = (BuscarBandejaAsigMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaAsig");
		LOG.info("busqueda.getUsuarioAsigSeleccionado(): " + busqueda.getUsuarioAsigSeleccionado());
		
		String usuarioAsignadoSeleccionado =  busqueda.getUsuarioAsigSeleccionado();
		
		int cont = 0;
		
		for (ExpedienteTCWrapper expTC : listTabla){
			if(expTC.isSeleccion()){
				cont++;
				break;
			}
		}	
		LOG.info("cont ::"+cont);
		
		if(cont>0 && !Constantes.CODIGO_CODIGO_CAMPO_VACIO.equals(usuarioAsignadoSeleccionado) && usuarioAsignadoSeleccionado != null){
			addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, false);
		}else
			addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, true);

	}
	
	public void actualizarUsuariosAsignacion() {
		LOG.info("actualizarUsuariosAsignacion()");
		List<Empleado> listaEmpxPerfil = (List<Empleado>) getObjectSession(Constantes.LISTA_USUARIOS_CARGADOS);
		List<Empleado> listaEmp =null;
		
		if(listaEmpxPerfil!=null && listaEmpxPerfil.size()>0)
			listaEmp=new ArrayList<Empleado>(listaEmpxPerfil);
		
		if(listaEmp!=null && listaEmp.size()>0){
			LOG.info("listaEmp es tamaño ::"+listaEmp.size());
			
			int k=0;
			for (int i = 0; i < listTabla.size(); i++) {
				if(listTabla.get(i).isSeleccion()){
					k++;
					int j = 0;
					int size = listaEmp.size();
					while(size > 0 && j < size){
						if (listaEmp.get(j).getCodigo().equals(listTabla.get(i).getCodigoUsuarioActual())) {
							listaEmp.remove(listaEmp.get(j));
						}else{
							j++;
						}
						size = listaEmp.size();
					}				
				}
			}
			
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			
			BuscarBandejaAsigMB buscarBandejaAsig = (BuscarBandejaAsigMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "buscarBandejaAsig");
			
			LOG.info("k:::"+k);
			if(k==0){
				buscarBandejaAsig.limpiarListaAsignar();
			}
			// siempre debe actualizar la lista porque es posible que el usuario del expediente no esté en la lista
			LOG.info("Metodo cargarListaAsignar");
			buscarBandejaAsig.cargarListaAsignar(listaEmp);
			
		}else
			LOG.info("listaEmp es nulo");		
	}	
	
	public String seleccionaFila() {
		
		LOG.info("seleccionaFila");
		
		LOG.info("actionCheck::::"+actionCheck);
		LOG.info("seleccionarTodos::::"+seleccionarTodos);
	
		String codigo = getRequestParameter("codigo");
		listTabla = (List<ExpedienteTCWrapper>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		boolean ok=true;
		
		if(actionCheck!=null && actionCheck.equals("1")){
			validarCantExpSeleccionados();	
			actualizarUsuariosAsignacion();
		}else{
			LOG.info("MOSTRAR EXP:::::"+codigo);
			for (ExpedienteTCWrapper expTC : listTabla){
				if(expTC.getCodigo().equals(codigo)){
					if(expTC.isSeleccion()){
						ok=false;
					}
					break;
				}
			}

			if(ok){
				actionCheck="";
				iniciarExpediente("1");
				//removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
				return "/visualizarExpediente/formVisualizarExpediente?faces-redirect=true";					
			}
		
		}

		actionCheck="";

		return null;
	}
	
	public void checkReasignar(AjaxBehaviorEvent e) {
		LOG.info("checkReasignar");
		validarCantExpSeleccionados();
		actualizarUsuariosAsignacion();
	}
	
	/*public String seleccionaFila() {
		String nombreNavegacionWeb = getRequestParameter("nombreNavegacionWeb");
		LOG.info("seleccionaFila");
		Expediente expediente = iniciarExpediente("0");
		LOG.info("getNombreJSPPrincipal()= " + getNombreJSPPrincipal());
		if (getNombreJSPPrincipal().equals("formBandejaPendientes")) {
			//Expediente expediente = iniciarExpediente();
			//Expediente expediente = iniciarExpediente();
			if(expediente!=null){
				if (nombreNavegacionWeb.equals("aprobarExpediente")
						|| nombreNavegacionWeb.equals("ejecutarEvalCrediticia")
						|| nombreNavegacionWeb.equals("revisarRegistrarDictamen")
						|| nombreNavegacionWeb.equals("evaluarFactibilidadOp")) {
					LOG.info("nombreNavegacionWeb= " + nombreNavegacionWeb);
					copiarLineaCredito(expediente);
				}
				LOG.info("2");
				Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
				expediente.setEmpleado(empleado);

				if (nombreNavegacionWeb.indexOf("registrarExpediente") != -1) {
					FacesContext ctx = FacesContext.getCurrentInstance();
					BuscarClienteMB buscarCliente = null;

					buscarCliente = (BuscarClienteMB) ctx.getApplication()
							.getVariableResolver()
							.resolveVariable(ctx, "buscarCliente");

					buscarCliente.setTipoDOISeleccionado(String.valueOf(expediente
							.getClienteNatural().getTipoDoi().getId()));
					buscarCliente.setNumeroDOI(expediente.getClienteNatural()
							.getNumDoi());
				}
				listTabla = null;
				removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);

				LOG.info("nombreNavegacionWeb= " + nombreNavegacionWeb);
				return nombreNavegacionWeb;				
			}

		}else {

			if(getNombreJSPPrincipal().equals("formBandejaReasigTareas")){

				List<SelectItem> empleado = (List<SelectItem>) getObjectSession(Constantes.LISTA_USUARIOS_ASIG);

					for (ExpedienteTCWrapper expTC : listTabla) {
						LOG.info("expTC.getCodigo() ::: "+expTC.getCodigo());
						if (expediente.getId()==Long.parseLong(expTC.getCodigo())) {
							LOG.info("expTC.isSeleccion() ::: "+expTC.isSeleccion());
							if(expTC.isSeleccion()){
								break;
							}else{
								if(expTC.isOpVisualizacion()){
									expTC.setOpVisualizacion(false);
									addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, listTabla);
									break;
								}else{
									//expediente = obtenerExpediente(codigo);
									expediente = iniciarExpediente("1");
									return "/visualizarExpediente/formVisualizarExpediente?faces-redirect=true";
								}
									
							}
								
						}
					}					

				return null;
			}else
			{
				expediente = iniciarExpediente("1");
				
				listTabla = null;
				removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);

				return "/visualizarExpediente/formVisualizarExpediente?faces-redirect=true";				
			}			
		}
		return null;
	}*/

	private void copiarLineaCredito(Expediente expediente) {
		// si no se tiene la linea de credito se copia de las solicitadas
		Double linCredAprob = expediente.getExpedienteTC().getLineaCredAprob();
		Long monLinCredAprob = expediente.getExpedienteTC()
				.getTipoMonedaAprob().getId();
		if (linCredAprob == null || monLinCredAprob == null) {
			expediente.setExpedienteTC(expediente.getExpedienteTC());
		}
	}

	private Expediente iniciarExpediente(String valor) {
		eliminarObjetosSession();

		String codigo = getRequestParameter("codigo");
		String taskID = getRequestParameter("taskID");

		LOG.info("codigo : " + codigo);
		LOG.info("taskID : " + taskID);

		Timestamp fechaActivado = Timestamp
				.valueOf(getRequestParameter("activado"));

		ExpedienteTCWPSWeb expedienteTC = null;

		for (ExpedienteTCWrapper expTC : listTabla) {
			if (taskID.equals(expTC.getTaskID())) {
				addObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION,
						expTC.getExpedienteTC());
				addObjectSession(Constantes.ESTADO_TARJETA, expTC.getEstadoTarjeta());
				expedienteTC = expTC.getExpedienteTC();
				break;
			}
		}
		LOG.info("codigo ES "+codigo);
		Expediente expediente = obtenerExpediente(codigo);
		LOG.info("expediente ES "+expediente);
		RemoteUtils bandeja = new RemoteUtils();

		Expediente expedienteEntrada = obtenerExpediente(codigo);
		if(expediente!=null){
			expediente.setNumTerminal(Util.obtenerIp());
	
			if (expedienteTC != null && expedienteTC.getIdTarea() != null) {
				Tarea tarea = tareaBean.buscarPorId(Long.parseLong(expedienteTC
						.getIdTarea()));
				expediente.getExpedienteTC().setTarea(tarea);
			}
	
			if (expediente.getExpedienteTC().getFlagRetraer()
					.equals(Constantes.FLAGRETRAERD)) {
				expediente.getExpedienteTC()
						.setFlagRetraer(Constantes.FLAGRETRAERN);
			}
			
			LOG.info("getNombreJSPPrincipal()::: " + getNombreJSPPrincipal());
			if (getNombreJSPPrincipal().equals("formBandejaPendientes") || valor.equals("1")){
				LOG.info("TAREA ID = "
						+ expediente.getExpedienteTC().getTarea().getId());
				LOG.info("TAREA DESCRIP = "
						+ expediente.getExpedienteTC().getTarea().getDescripcion());
				LOG.info("ESTADO ID = " + expediente.getEstado().getId());
				LOG.info("ESTADO DESCRIP = "
						+ expediente.getEstado().getDescripcion());
		
				if (expediente.getExpedienteTC().getTarea().getId() != Constantes.ID_TAREA_1
						&& expediente.getEstado().getId() != Constantes.ESTADO_EN_REGISTRO_TAREA_1) {
					LOG.info("Actualizacion de TI y T2 ");
					LOG.info("Actualizacion de TI y T2 ");
					expediente.getExpedienteTC().setFechaT2(
							new Timestamp(bandeja.obtenerTimestampServidorProcess()
									.getTimeInMillis()));
					expediente.getExpedienteTC().setFechaT1(fechaActivado); /*
																			 * fecha de
																			 * asignacion
																			 * del
																			 * processo
																			 */
				}
		
				/**
				 * Obtiene URL Documento CM - 03 Abril 2014
				 * */
//				LOG.info("Consulta CM");
//				objDocumentoExpTc = new DocumentoExpTc();
//				objDocumentoExpTc.setExpediente(new Expediente());
//				objDocumentoExpTc.getExpediente().setClienteNatural(
//						new ClienteNatural());
//				objDocumentoExpTc.getExpediente().getClienteNatural()
//						.setNumDoi(expediente.getClienteNatural().getNumDoi());
//				LOG.info("Num Doi 1 ="
//						+ expediente.getClienteNatural().getNumDoi());
//				LOG.info("Num Doi 2 ="
//						+ objDocumentoExpTc.getExpediente().getClienteNatural()
//								.getNumDoi());
//				
//				long tiempoInicio = System.currentTimeMillis();
//				try {
//					listaDocumentosCM = facade
//							.obtenerListaDocumentoCM(objDocumentoExpTc);
//				} catch (Exception e) {
//					e.getStackTrace();
//				}
//				long tiempoFin = System.currentTimeMillis();
//				LOG.info("TablaBandejaAsigMB.iniciarExpediente obtenerListaDocumentoCM demora: " + (tiempoFin - tiempoInicio) + " milisegundos");
//		
//				Map<String, Object> mapListDocumentosCM = new TreeMap<String, Object>();
//		
//				if (listaDocumentosCM != null)
//					for (Documento objDocumento : listaDocumentosCM) {
//						mapListDocumentosCM.put(String.valueOf(objDocumento.getId()),
//								objDocumento);
//					}
//				else
//					LOG.info("listaDocumentosCM es nulo");	
//				
//				addObjectSession(Constantes.EXPEDIENTE_LISTA_DOCUMENTO_CM,
//						mapListDocumentosCM);
				
			}
	
			addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);
			addObjectSession(Constantes.EXPEDIENTE_SESION_ENTRANTE,
					expedienteEntrada);
			addObjectSession(Constantes.NOMBRE_BANDEJA_SESION,
					getNombreJSPPrincipal());

			if (expediente != null) {
				LOG.info("exp= " + expediente.getId());
			}			
			
		}

		return expediente;
	}

	public String retraer() {
		Expediente expediente = iniciarExpediente("1");

		expediente.getExpedienteTC().setFlagRetraer(Constantes.FLAGRETRAERR);
		expediente.getExpedienteTC().setFechaT2(null);
		expediente.getExpedienteTC().setFechaT3(null);

		// Colocar la actualizacion al proceso - PENDIENTE
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		ExpedienteTCWPS expedienteTCWPS = ayudaExpedienteTC.copiarDatos(this,
				Constantes.ACCION_RETRAER, null);

		String tkiid = expedienteTCWPS.getTaskID();
		RemoteUtils objRemoteUtils = new RemoteUtils();
		objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);

		expedienteBean.edit(expediente);

		Historial historial = ConvertExpediente
				.convertToHistorialVO(expediente);
		historialBean.create(historial);

		/* carga nuevamente la lista */
		/*
		 * FacesContext ctx = FacesContext.getCurrentInstance();
		 * BuscarBandejaPendMB buscarBandejaPend = (BuscarBandejaPendMB)
		 * ctx.getApplication().getVariableResolver().resolveVariable(ctx,
		 * "buscarBandejaPend"); buscarBandejaPend.buscar();
		 */

		Consulta objConsulta = new Consulta();

		List<String> listUsuarios = new ArrayList<String>();
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
		listTabla = (List<ExpedienteTCWrapper>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		if(listTabla!=null)
			LOG.info("listTabla->->"+listTabla.size());
		return listTabla;
		
		/*List<ExpedienteTCWrapper> listTemp=(List<ExpedienteTCWrapper>)getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
		if(listTemp!=null && listTemp.size()>0)
			listTabla=listTemp;
		*/
		//return listTabla;
		
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
	public void ordenar(ActionEvent event) {
		
		LOG.info("ordenar::::");
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
		String orden = this.sortAscending ? ComparatorBase.SORT_ORDER_ASC
				: ComparatorBase.SORT_ORDER_DESC;
		Comparator<ExpedienteTCWPSWeb> comparator = null;
		if ("estado".equals(columna)) {
			comparator = ComparatorFactory.estado(orden);
		} else if ("rol".equals(columna)) {
			comparator = ComparatorFactory.rol(orden);
		} else if ("usuario".equals(columna)) {
			comparator = ComparatorFactory.usuario(orden);
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
		} else if ("fechaRegistro".equals(columna)) {
			comparator = ComparatorFactory.fechaRegistro(orden);
		} else if ("nomUsuario".equals(columna)) {
			comparator = ComparatorFactory.nomUsuarioAsig(orden);
		} else if ("lineaCredito".equals(columna)) {
			comparator = ComparatorFactory.lineaCredito(orden);
		} else if ("oficina".equals(columna)) {
			comparator = ComparatorFactory.oficina(orden);
		} else if ("territorio".equals(columna)) {
			comparator = ComparatorFactory.territorio(orden);
		} else if ("alerta".equals(columna)) {
			// se debe obtener el objeto AyudaHorario para todos los registros
			Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
			for(ExpedienteTCWrapper obj : listTabla){
	        	if(obj.getAyudaHorario()==null) {
	        		LOG.info("band-asig --> idExpediente : "+obj.getExpedienteTC().getCodigo());
	        		try {
						obj.setAyudaHorario(new AyudaHorario(empleado.getOficina().getId()));
						LOG.info("Se ha creado horario...");
					} catch (NamingException e) {
						LOG.error(e.getMessage(), e);
					}
	        	}
	        }
			comparator = ComparatorFactory.alerta(orden);
		} else if ("estadoTarjeta".equals(columna)) {
			comparator = ComparatorFactory.estadoTarjeta(orden);
		} else if ("estadoArchivos".equals(columna)) {
			comparator = ComparatorFactory.estadoArchivos(orden);
		} else if ("devoluciones".equals(columna)) {
			comparator = ComparatorFactory.devoluciones(orden);
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

	// public void cargaUsuariosAsignar2(){
	public void cargaUsuariosAsignar2(AjaxBehaviorEvent event) {
		LOG.info("cargaUsuariosAsignar2 ");
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		LOG.info("codigoUsuarioActual = " + codUsuario);
		LOG.info("codigoExpedienteActual = " + codExpediente);
		addObjectSession(Constantes.COD_USUARIO_ASIGNAR, codUsuario);
	
		LOG.info("seleccionado =" + seleccionado);
	//	seleccionado = Boolean
		//		.parseBoolean(getRequestParameter("seleccion"));	
	//	LOG.info("seleccionado =" + seleccionado);
		// int j=0;
		//ArrayList<ExpedienteTCWrapper> listaTemp = new ArrayList<ExpedienteTCWrapper>();
		List<Empleado> listaEmp = new ArrayList<Empleado>();
		listTabla = (List<ExpedienteTCWrapper>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		
		if(listTabla!=null)
		for (int i = 0; i < listTabla.size(); i++) {
			if (listTabla.get(i).getCodigo().equals(codExpediente)) {
				boolean seleccion = listTabla.get(i).isSeleccion();
				LOG.info("Seleccion Actual Expediente:" + seleccion);
				//seleccion = (seleccion == false) ? true : false;
				listTabla.get(i).setSeleccion(seleccion);
				listTabla.get(i).setOpVisualizacion(true);
				LOG.info("Seleccion Cambio Expediente:" + codExpediente + " : "
						+ listTabla.get(i).isSeleccion());
				i = listTabla.size();
			}
		}
		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, listTabla);
		if (codUsuario != null) {
		/*	Empleado empleado = empleadoBean.buscarPorCodigo(codUsuario);*/

			/*List<Empleado> listaEmpxPerfil = empleadoBean
					.buscarPorPerfilEmpleadoActivo(
							empleado.getPerfil().getId(), empleado.getOficina()
									.getId());*/
			
			List<Empleado> listaEmpxPerfil = (List<Empleado>) getObjectSession(Constantes.LISTA_USUARIOS_CARGADOS);
			listaEmp = new ArrayList<Empleado>(listaEmpxPerfil);
			
			if(listaEmp!=null && listaEmp.size()>0){
				LOG.info("listaEmp es tamaño ::"+listaEmp.size());
				for (int i = 0; i < listTabla.size(); i++) {
					if(listTabla.get(i).isSeleccion()){
						for (int j = 0; j < listaEmpxPerfil.size(); j++) {
							if (listaEmpxPerfil.get(j).getCodigo().equals(listTabla.get(i).getCodigoUsuarioActual())) {
								listaEmp.remove(listaEmpxPerfil.get(j));
							}
						}					
					}
				}
				
				BuscarBandejaAsigMB buscarBandejaAsig = (BuscarBandejaAsigMB) FacesContext
						.getCurrentInstance().getApplication().getELResolver()
						.getValue(elContext, null, "buscarBandejaAsig");
				LOG.info("Metodo cargarListaAsignar");
				buscarBandejaAsig.cargarListaAsignar(listaEmp);				
			}else
				LOG.info("listaEmp es nulo");
			

		}

		// actualizarLista();
		/* habilita el boton Asignar despues que cargue el combo Asignar a.. */
		BandejaReasigTareasMB bandejaReasig = (BandejaReasigTareasMB) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "bandejaReasigTareas");
		//bandejaReasig.setDesAsignar(false);
		
		// verificando si por lo menos hay un item seleccionado se debe habilitar el boton Asignar
		int selec = 0;
		boolean todos=true;
		for (int i = 0; i < listTabla.size(); i++) {
			if (listTabla.get(i).isSeleccion()) {
				selec++;
				if(i<=getNumeroRegistro()){
					seleccionarTodos = true;
				}
			}else{
				if(i<=getNumeroRegistro()){
					seleccionarTodos = false;
					todos=false;
				}				
			}
		}
		LOG.info("selec :::"+selec);
		if(selec == 0){
			addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, true);
		}
		else{
			addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, false);
		}
		
		LOG.info("getNumeroRegistro(): " + getNumeroRegistro());
		if(seleccionarTodos && todos){
			seleccionarTodos = true;
		}
		else{
			seleccionarTodos = false;
		}
	}

	// public void cargaUsuariosAsignar(String codigoUsuarioActual, boolean
	// seleccion){
	/*public void cargaUsuariosAsignar(AjaxBehaviorEvent event) {
		String codigoUsuarioActual = getRequestParameter("codigoUsuarioActual");
		boolean seleccion = Boolean
				.parseBoolean(getRequestParameter("seleccion"));
		int i = 0;

		for (ExpedienteTCWrapper lista : listTabla) {
			if (lista.isSeleccion()) {
				i++;
			}
		}
		LOG.info("seleccion = " + seleccion);
		LOG.info("codigoUsuarioActual = " + codigoUsuarioActual); // codigoUsuarioActual
		if(codigoUsuarioActual!=null && !codigoUsuarioActual.trim().equals("")){
			Empleado empleado = empleadoBean.buscarPorCodigo(codigoUsuarioActual);
			List<Empleado> listaEmpxPerfil = empleadoBean
					.buscarPorIdPerfil(empleado.getPerfil().getId());

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

			if (i == 0) {
				buscarBandejaAsig.setUsuariosAsig(Util.listaVacia());
			} else if (i == 1) {
				addObjectSession(Constantes.LISTA_ASIGNAR_USUARIO_SESION, listaEmp);
				buscarBandejaAsig.cargarListaAsignar(listaEmp);
			} else {
				List<Empleado> lista = (List) getObjectSession(Constantes.LISTA_ASIGNAR_USUARIO_SESION);
				buscarBandejaAsig.cargarListaAsignar(lista);
			}
			addObjectSession(Constantes.LISTA_USUARIOS_ASIG,
					buscarBandejaAsig.getUsuariosAsig());			
		}else
			LOG.info("codigoUsuarioActual es nulo ");
	}*/

	public void seleccionarExpedientes(AjaxBehaviorEvent event) {
		LOG.info("seleccionarExpedientes" );
		LOG.info("seleccionarTodos: " + seleccionarTodos);
		int inicioRegistro;
		int cantRegistros;
			listTabla = (List<ExpedienteTCWrapper>) getObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
			
			LOG.info("getNumeroRegistro(): " + getNumeroRegistro());//Elementos por paginas
			LOG.info("getCurrentPage(): " + getCurrentPage());//Numero de paginas
			
			cantRegistros=getNumeroRegistro();
			
			if(getCurrentPage()==1){
				inicioRegistro=getCurrentPage()-1;
			}else{
				inicioRegistro=(getCurrentPage()-1)*getNumeroRegistro();
			}
			LOG.info("inicioRegistro: " + inicioRegistro);//inicioRegistro
			
			if(getNumeroRegistro()==-1 && listTabla!=null && listTabla.size()>0){
				LOG.info("Tamaño de lista:::"+listTabla.size());
				cantRegistros=listTabla.size();
			}
			
			LOG.info("cantRegistros: " + cantRegistros);//Elementos por paginas
			int y=1;

			for (int i = inicioRegistro; i < listTabla.size(); i++) {
				if(y<=cantRegistros){
					listTabla.get(i).setSeleccion(seleccionarTodos);
					LOG.info("Seleccion Cambio Expediente:" + listTabla.get(i).getCodigo() + " - > "
							+ listTabla.get(i).isSeleccion());				
				}else
					break;
				y++;
			}
			addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, listTabla);
			
			validarCantExpSeleccionados();	
			actualizarUsuariosAsignacion();	
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

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}

	public String getCodUsuario() {
		return codUsuario;
	}

	public String getCodExpediente() {
		return codExpediente;
	}

	public void setCodExpediente(String codExpediente) {
		this.codExpediente = codExpediente;
	}

	public boolean isSeleccionarTodos() {
		return seleccionarTodos;
	}

	public void setSeleccionarTodos(boolean seleccionarTodos) {
		this.seleccionarTodos = seleccionarTodos;
	}

	public String getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(String numRegistros) {
		this.numRegistros = numRegistros;
	}

	public String getCadExpSelecc() {
		return cadExpSelecc;
	}

	public void setCadExpSelecc(String cadExpSelecc) {
		this.cadExpSelecc = cadExpSelecc;
	}

	public String getCadExpSeleccAnt() {
		return cadExpSeleccAnt;
	}

	public void setCadExpSeleccAnt(String cadExpSeleccAnt) {
		this.cadExpSeleccAnt = cadExpSeleccAnt;
	}

	public String getActionCheck() {
		return actionCheck;
	}

	public void setActionCheck(String actionCheck) {
		this.actionCheck = actionCheck;
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
			
		int i=0;
		boolean flag=false;
		
		List<ExpedienteTCWrapper> listaCount = this.getListTabla();
		LOG.info("INICIAL:::"+j);
		LOG.info("NUM REGISTROS:::"+numReg);
		
		if(listaCount!=null && listaCount.size()>0)
			for(ExpedienteTCWrapper obj : listaCount){
				//Empleado emp = empleadoBean.buscarPorCodigo(obj.getExpedienteTC().getCodigoUsuarioActual());
								
				if (i==j || flag){
					flag=true;
					if(j<(inicial+ numReg)){  
						
				       if(obj.getExpedienteTC().getIdOficinaUsu()!=null && !obj.getExpedienteTC().getIdOficinaUsu().equals("")){
					        	if(obj.getAyudaHorario()==null){
					        		LOG.info("band-asig --> idExpediente : "+obj.getExpedienteTC().getCodigo());
					        		try {
										obj.setAyudaHorario(new AyudaHorario(Long.parseLong(obj.getExpedienteTC().getIdOficinaUsu())));
										LOG.info("Se ha creado horario...");
									} catch (NamingException e) {
										// TODO Bloque catch generado automáticamente
										LOG.error(e.getMessage(), e);
									}
					        	}else
				        			LOG.info("Ayuda horario no es null para id exp::"+obj.getExpedienteTC().getCodigo());				        		
				       		}else
				       			LOG.info("IdOficinaUsu es vacío");
						
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
		this.setListTabla(listaCount);
			//addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO, listaCount);
			
	}
	

}
