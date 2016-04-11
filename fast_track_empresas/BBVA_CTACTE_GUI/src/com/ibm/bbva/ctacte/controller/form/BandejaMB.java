package com.ibm.bbva.ctacte.controller.form;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ConsultaCC;
import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.EstudioAbogado;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.ExpedienteTareaProceso;
import com.ibm.bbva.ctacte.bean.Historial;
import com.ibm.bbva.ctacte.bean.Oficina;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.bean.Territorio;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractTablaMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.form.model.TareaBandejaVOModel;
import com.ibm.bbva.ctacte.dao.ClienteDAO;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.EstadoExpedienteDAO;
import com.ibm.bbva.ctacte.dao.EstadoTareaDAO;
import com.ibm.bbva.ctacte.dao.EstudioAbogadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteTareaProcesoDAO;
import com.ibm.bbva.ctacte.dao.HistorialDAO;
import com.ibm.bbva.ctacte.dao.OficinaDAO;
import com.ibm.bbva.ctacte.dao.OperacionDAO;
import com.ibm.bbva.ctacte.dao.TareaDAO;
import com.ibm.bbva.ctacte.dao.TerritorioDAO;
import com.ibm.bbva.ctacte.util.AyudaHorario;
import com.ibm.bbva.ctacte.util.DateRange;
import com.ibm.bbva.ctacte.util.GenericPredicate;
import com.ibm.bbva.ctacte.util.ListSorter;
import com.ibm.bbva.ctacte.util.SortHeader;
import com.ibm.bbva.ctacte.util.Util;
import com.ibm.bbva.ctacte.vo.TareaBandejaVO;

@ManagedBean(name = "bandejaMB")
@ViewScoped
public class BandejaMB extends AbstractTablaMBean {

	private static final String ROJO = "rojo.png";
	private static final String VERDE = "verde.png";
	private static final String AMARILLO = "amarillo.png";

	private static final String SUF_PRE_REGISTRO = "_PR";
	private static final String SUF_PENDIENTES = "_PD";
	private static final long serialVersionUID = -7704990404847285647L;
	private static final Logger LOG = LoggerFactory.getLogger(BandejaMB.class);

	private TareaBandejaVO filtro;
	private List<TareaBandejaVO> listaTareas;
	private List<TareaBandejaVO> listaTareasBase;
	private List<String> listaCriterios;
	private boolean ordenNatural;
	private TareaBandejaVO tareaSeleccionada;
	private TareaBandejaVOModel modelo;

	private List<EstadoTarea> listaEstado;
	private List<EstadoExpediente> listaEstadoExp;
	private List<Operacion> listaOperacion;
	private List<Territorio> listaTerritorio;
	private List<Tarea> listaTarea;
	private List<Oficina> listaOficina;
	private List<Empleado> listaAbogado;
	private List<EstudioAbogado> listaEstudioAbogado;
	private List<?> listaResponsable;
	private List<?> listaANS;
	private int contador;
	private List<SelectItem> oficinaItemsTarea;
	private List<SelectItem> oficinaItemsExp;
	private List<SelectItem> abogadoItems;
	private List<SelectItem> ansItems;
	private String codSemaforo;
	//private List<Empleado> list
	
	private String mensajeError;
	private boolean menuColapsado = false;

	/*
	 * Nro Tarea, Estado tarea, Nro Expediente, Estado Expediente, Nombre tarea
	 * COdigo Central, Operacion, fecha de asignacion, Nro DOI, Razon Social
	 * fecha de atencion,Gestor, territorioExp, OficinaExp, Responsable ANS,
	 * Fecha inicio, Estudio, territorioTarea, OficinaTarea fecha de termino,
	 * Abogado
	 */
	int arrayBusqueda[] = new int[22]; // 22 criterios de busqueda
	// Criterios de busqueda validos para cada repositorio
	int arrayProcess[] = new int[] { 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 0, 1 }; // 22 elementos
	int arrayTablaHistorico[] = new int[] { 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 1, 0, 1, 1, 1, 1, 1, 1 };
	int arrayTablaExpediente[] = new int[] { 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0,
			1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0 };

	private Map<String, SortHeader> mapaHeader;
	
	@EJB
	private EmpleadoDAO empleadoDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	@EJB
	private TareaDAO tareaDAO;
	@EJB
	private EstadoTareaDAO estadoTareaDAO;
	@EJB
	private EstadoExpedienteDAO estadoExpedienteDAO;
	@EJB
	private OficinaDAO oficinaDAO;
	@EJB
	private TerritorioDAO territorioDAO;
	@EJB
	private OperacionDAO operacionDAO;
	@EJB
	private EstudioAbogadoDAO estudioAbogadoDAO;
	@EJB
	private HistorialDAO historialDAO;
	@EJB
	private ClienteDAO clienteDAO;
	@EJB
	private ExpedienteTareaDAO expedienteTareaDAO;
	@EJB
	private ExpedienteTareaProcesoDAO expTareaProcesoDAO;

	public BandejaMB() {
	}
	
	@PostConstruct
	public void iniciar() {
		LOG.info("BandejaMB()");
		filtro = new TareaBandejaVO();
		listaCriterios = new ArrayList<String>();
		mapaHeader = new HashMap<String, SortHeader>();

		putMapa("codTarea", "Tarea");
		putMapa("nomTarea", "Nombre");
		putMapa("expediente", "Expediente");
		putMapa("strFechaAsignacion", "Fecha de Asignación");
		putMapa("dscEstadoTarea", "Estado Tarea");
		putMapa("ans", "ANS");
		putMapa("strFechaAtencion", "Fecha de Atención");
		putMapa("responsable", "Responsable");
		putMapa("oficinaTarea", "Oficina");
		putMapa("territorioTarea", "Territorio");
		putMapa("operacion", "Operación");
		putMapa("dscCliente", "Cliente");
		putMapa("strFechaInicio", "Fecha de Inicio");
		putMapa("dscEstadoExp", "Estado Expediente");
		putMapa("strFechaTermino", "Fecha de Término");
		putMapa("abogadoEstudio", "Abogado");
		putMapa("estudio", "Estudio");

		//obtenerMisPedientes(); // ya no cargará los pendientes al entrar a la bandeja
		limpiarResultados();

		// Inicializando el campo Estado Expediente a 2 segun el CU16
		filtro.setCodEstadoTarea(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE));
		
		//Valores por defecto del paginador
		setRowsPerPage(new Integer(5));
		List<SelectItem> listPaginas = new ArrayList<SelectItem>();
		listPaginas.add(new SelectItem("5", "5"));
		listPaginas.add(new SelectItem("10", "10"));
		listPaginas.add(new SelectItem("15", "15"));
		setListOfSelectItems(listPaginas);
		
		devolverValoresFiltroBusqueda();
		
	}
	
	public boolean devolverValoresFiltroBusqueda(){
		TareaBandejaVO filtroTmp = (TareaBandejaVO)Util.getObjectSession(Constantes.BANDJ_FILTRO_BASTANTEO);
		String existeListaTareas = (String) Util.getObjectSession(Constantes.BANDJ_TRABAJO_BASTANTEO);

		if(existeListaTareas!=null && existeListaTareas.equals("1")){

			LOG.info("Resultado Busqueda anterior : " + listaTareas.size());	
			
			if(filtroTmp!=null){
				LOG.info("Se realizo la busqueda");
				filtro=filtroTmp;
				filtraTareas(null);
				if (filtro.getOficinaTarea() != null
						&& !ConstantesAdmin.CODIGO_NO_SELECCIONADO.equals(filtro
								.getOficinaTarea())){
					LOG.info("Se selecciono oficinaTarea ");
					cambiarOficinasTarea(null);
				}
				if (filtro.getOficinaExp() != null
						&& !ConstantesAdmin.CODIGO_NO_SELECCIONADO.equals(filtro
								.getOficinaExp())){
					LOG.info("Se selecciono OficinaExp ");
					cambiarOficinasExp(null);
				}
				if (filtro.getAbogadoEstudio() != null
						&& !ConstantesAdmin.CODIGO_NO_SELECCIONADO.equals(filtro
								.getAbogadoEstudio())){
					LOG.info("Se selecciono AbogadoEstudio ");
					cambiarAbogados(null);
				}	
				filtro=filtroTmp;
				return true;
			}else{
				LOG.info("Obteniendo mis pendientes ");
				obtenerMisPedientes();
			}			
		}else
			LOG.info("Busqueda por primera vez...");

		return false;
	}
	
	public void limpiarResultados() {
		listaTareas = new ArrayList<TareaBandejaVO>();
		listaTareasBase = new ArrayList<TareaBandejaVO>();
		crearModelo(listaTareas);
		super.pageFirst(null);
		llenarCombos();
	}

	public String obtenerMisPedientes() {
		LOG.info("obtenerMisPedientes()");
		ArrayList<String> usuarios = new ArrayList<String>();

		listaTareas = new ArrayList<TareaBandejaVO>();
		listaTareasBase = new ArrayList<TareaBandejaVO>();

		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		RemoteUtils bandejaTareasBDelegate = new RemoteUtils();
		List<ExpedienteCC> expedientes = null;
		ConsultaCC c = new ConsultaCC(); 
		//+POR SOLICITUD BBVA+LOG.info("*********************isSupervisor(): "+isSupervisor());	
		if (isSupervisor()) {
			List<Empleado> empleadosList = getUsuariosVistosPorPerfil();
			for (Empleado e : empleadosList) {
				usuarios.add(e.getCodigo());
			}

			c.setConsiderarUsuarios(true);
			c.setUsuarios(usuarios);

			try {
				expedientes = bandejaTareasBDelegate.obtenerInstanciasTareasPorUsuarioCC(c);				
			} catch (RuntimeException e) {
				LOG.error("Error al obtener las tareas", e);
				expedientes = Collections.EMPTY_LIST;
				mensajeErrorPrincipal("tablaTask", ConstantesAdmin.MSG_ERROR_CONSULTA_REST_PROCESS);
			}
		} else {
			if (empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR)){
				List<Empleado> listEmp = empleadoDAO.getEmpleadosPorPerfilOficina(empleado.getPerfil().getId(), empleado.getOficina().getId());
				//+POR SOLICITUD BBVA+LOG.info("******************listEmp : "+listEmp.size());
				for (Empleado e : listEmp) {
					usuarios.add(e.getCodigo());
				}
				c.setConsiderarUsuarios(true);
				c.setUsuarios(usuarios);
			}else{
				//+POR SOLICITUD BBVA+LOG.info("******************c.setCodUsuarioActual(empleado.getCodigo()) : "+c.getCodUsuarioActual());
				c.setCodUsuarioActual(empleado.getCodigo());
				c.setConsiderarUsuarios(false); // para que no filtre en java el usuario
			}
			
			try {				
				//+POR SOLICITUD BBVA+LOG.info("TRY --bandejaTareasBDelegate.obtenerInstanciasTareasPorUsuarioCC(c)");
				expedientes = bandejaTareasBDelegate.obtenerInstanciasTareasPorUsuarioCC(c);	
			    //+POR SOLICITUD BBVA+LOG.info("expedientes.size()"+expedientes.size());		
			} 
			catch (RuntimeException e) {
				LOG.error("Error al obtener las tareas", e);
				//+POR SOLICITUD BBVA+LOG.info("************TaskOperationException---Error al obtener las tareas");
				expedientes = Collections.EMPTY_LIST;
				//+POR SOLICITUD BBVA+LOG.info("************catch---expedientes.size()"+expedientes.size());
				mensajeErrorPrincipal("tablaTask", ConstantesAdmin.MSG_ERROR_CONSULTA_REST_PROCESS);
			}
			catch (Exception e) {
				LOG.error("Error al obtener las tareas", e);
				//+POR SOLICITUD BBVA+LOG.info("************Exception Error al obtener las tareas");
				expedientes = Collections.EMPTY_LIST;
				//+POR SOLICITUD BBVA+LOG.info("************catch---expedientes.size()"+expedientes.size());
				mensajeErrorPrincipal("tablaTask", ConstantesAdmin.MSG_ERROR_CONSULTA_REST_PROCESS);
			}
		}

		LOG.info("Expedientes : {}", expedientes.size());
		//+POR SOLICITUD BBVA+LOG.info("************(ExpedienteCC expedienteCC : expedientes)************");
		for (ExpedienteCC expedienteCC : expedientes) {
			TareaBandejaVO tareaBandeja = crearTareaBandejaVO(expedienteCC);
			listaTareasBase.add(tareaBandeja);
		}

		if (isSupervisor() || empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR)) {
			LOG.info("Agrega Lista de Pre-registros a la bandeja para todos los gestores y supervisores");
			for (String codUsuario : usuarios) { // por cada codigo de usuario
													// supervisado
				List<Expediente> listaExpe = expedienteDAO.findByEstado(codUsuario,ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO);
				for (Expediente e : listaExpe) {
					listaTareasBase.add(transformExpediente(e));
				}
			}
		}

		listaTareas = new ArrayList<TareaBandejaVO>(listaTareasBase);
		//+POR SOLICITUD BBVA+LOG.info("************actualizaAns(listaTareas)************");
		actualizaAns(listaTareas);

		// [Begin]-[15.07.23]-[Ordenar lista Bandeja Trabajo]
		ListSorter.ordenarSemaforo(listaTareas, "Pendiente");
		// [End]-[15.07.23]
		ListSorter.ordenarSemaforo(listaTareas, ConstantesAdmin.CODIGO_ROJO);
		ListSorter.ordenarOperaciones(listaTareas,ConstantesBusiness.DESCRIPCION_OPERACION_REVOCATORIA);
		
		removeObjectSession(Constantes.BANDJ_FILTRO_BASTANTEO);
		if(listaTareas!=null && listaTareas.size()>0)
			addObjectSession(Constantes.BANDJ_TRABAJO_BASTANTEO, "1");
		else
			removeObjectSession(Constantes.BANDJ_TRABAJO_BASTANTEO);
		
		crearModelo(listaTareas);
		super.pageFirst(null);
		llenarCombos();
		
		return null;
	}
	
	private List<TareaBandejaVO> cargaRegProcess(ConsultaCC consulta) {
		List<TareaBandejaVO> lista = new ArrayList<TareaBandejaVO>();
		LOG.info("*********************cargaRegProcess(ConsultaCC consulta)*********************");
		RemoteUtils bandejaTareasBDelegate = new RemoteUtils();
		List<ExpedienteCC> expedientes = null;
		
//	if (!(consulta.getCodUsuarioActual()==null || consulta.getCodUsuarioActual().trim().equals(""))) {
//		List<Empleado> listaEmpleado = listaEmpleados(consulta.getCodUsuarioActual().toUpperCase());
//		if (listaEmpleado != null && !listaEmpleado.isEmpty()){
//			LOG.info("listaEmpleado.size()"+listaEmpleado.size());
//			for (Empleado listaEmp : listaEmpleado) {
//				LOG.info("BuscarResponsablePro"+listaEmp.getCodigo()+"-"+listaEmp.getNombresCompletos());
//				consulta.setCodUsuarioActual(listaEmp.getCodigo());
//				try {
//					expedientes = bandejaTareasBDelegate.obtenerInstanciasTareasPorUsuarioCC(consulta);
//				} catch (RuntimeException e) {
//					LOG.error("Error al obtener las tareas", e);
//					expedientes = Collections.EMPTY_LIST;
//				}
//				LOG.info("Expedientes : {}", expedientes.size());
//				for (ExpedienteCC expedienteCC : expedientes) {
//					TareaBandejaVO tareaBandeja = crearTareaBandejaVO(expedienteCC); // al crear tareabandeja llena la fechAsignacion
//					//+POR SOLICITUD BBVA+System.out..println("tareaBandeja.getCodSemaforo() " + tareaBandeja.getCodSemaforo());
//					lista.add(tareaBandeja);
//				}					
//			}
//		}
//	}else{
			//----------------
			try {
				expedientes = bandejaTareasBDelegate.obtenerInstanciasTareasPorUsuarioCC(consulta);
			} catch (RuntimeException e) {
				LOG.error("Error al obtener las tareas", e);
				expedientes = Collections.EMPTY_LIST;
			}
			LOG.info("Expedientes : {}", expedientes.size());
			for (ExpedienteCC expedienteCC : expedientes) {
				TareaBandejaVO tareaBandeja = crearTareaBandejaVO(expedienteCC); // al crear tareabandeja llena la fechAsignacion
				//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("tareaBandeja.getCodSemaforo() "	+ tareaBandeja.getCodSemaforo());
				lista.add(tareaBandeja);
			}
	        //----------------
//		}
		
		LOG.info("listaTareas Process: " + lista.size());
		return lista;
	}

	private void crearModelo(List<TareaBandejaVO> lista) {
		modelo = new TareaBandejaVOModel(lista);
	}

	private void actualizaAns(List<TareaBandejaVO> listTareaBandeja) {

		for (TareaBandejaVO tareaBandejaVO : listTareaBandeja) {
			// Logica
			if (tareaBandejaVO.getExpedienteCC() == null) {
				tareaBandejaVO.setCodSemaforo(VERDE);				
			} else {
				AyudaHorario ayudaHorario = new AyudaHorario(Integer.valueOf(tareaBandejaVO.getExpedienteCC().getCodOficina()));
				//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("++ActualizarAns : "+tareaBandejaVO.getExpedienteCC().getNumeroTarea()+" - "+tareaBandejaVO.getCodTarea());
				List<Tarea> lisTar = tareaDAO.findById(Integer.valueOf(tareaBandejaVO.getCodTarea()));

				tareaBandejaVO.setVerdeMinutos(lisTar.get(0).getVerdeMinutos());				

				if (lisTar == null || lisTar.isEmpty()) {
				} else {
					//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("tareaBandejaVO.getExpedienteCC().getActivado() : "+tareaBandejaVO.getExpedienteCC().getActivado().getTime());
					//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Calendar.getInstance() : "+Calendar.getInstance().getTime());
					
					Calendar calendarP = Calendar.getInstance();
					calendarP.setTime(tareaBandejaVO.getExpedienteCC().getFechaServidorP());
					
					//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("getFechaServidorP : "+calendarP.getTime());
					tareaBandejaVO.setMinutos(ayudaHorario.calcularMinutos(tareaBandejaVO.getExpedienteCC().getActivado(),calendarP)); /*Calendar.getInstance()));*/
					//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("++lisTar.get(0).getVerdeMinutos() : "+lisTar.get(0).getVerdeMinutos());
					//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("++lisTar.get(0).getAmarilloMinutos() : "+lisTar.get(0).getAmarilloMinutos());
					//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("++tareaBandejaVO.getMinutos() : "+tareaBandejaVO.getMinutos());
					try {
						if(tareaBandejaVO.getExpedienteCC().getEstadoTarea().equalsIgnoreCase(""+ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE+"")){
							Historial historial = new Historial();
							historial.setFechaEnvio(tareaBandejaVO.getExpedienteCC().getActivado().getTime());
							historial.setFechaFin(tareaBandejaVO.getExpedienteCC().getFechaServidorP());
							Empleado empleado = empleadoDAO.findByCodigo(tareaBandejaVO.getCodResponsable());
							int ans = Util.obtenerAnsTienpoReal(historial, empleado);
							tareaBandejaVO.setMinutos(ans);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if (lisTar.get(0).getVerdeMinutos() >= tareaBandejaVO.getMinutos()) {
						tareaBandejaVO.setCodSemaforo(VERDE);
						//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("++verde ");
					} else {
						if (lisTar.get(0).getVerdeMinutos()+ lisTar.get(0).getAmarilloMinutos() >= tareaBandejaVO.getMinutos()) {
							tareaBandejaVO.setCodSemaforo(AMARILLO);							
						} else {
							tareaBandejaVO.setCodSemaforo(ROJO);							
						}
					}
					tareaBandejaVO.getExpedienteCC().setCodSemaforo(
							tareaBandejaVO.getCodSemaforo());

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(tareaBandejaVO.getExpedienteCC().getActivado().getTime());
					calendar.add(Calendar.MINUTE, tareaBandejaVO.getVerdeMinutos());
					tareaBandejaVO.setStrFechaAtencion(new DateRange(calendar.getTime()));
				}
			}
		}
	}

	private void llenarCombos() {
		listaEstado = estadoTareaDAO.findAll();
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("listaEstado : " + listaEstado.size());
		listaEstadoExp = estadoExpedienteDAO.findAll();
		List<Tarea> listaTareaTmp = tareaDAO.findAll();
		listaTarea = new ArrayList<Tarea>();
		listaTarea.addAll(listaTareaTmp);
		// se debe remover la tarea 30 porque no se usa en el process
		if (listaTarea.size() > 0) {
			for (int i=0; i<listaTarea.size(); i++) {
				Tarea tarea = listaTarea.get(i);
				if (tarea.getId().intValue() == ConstantesBusiness.ID_TAREA_VERIFICAR_REALIZAR_BASTANTEO_2) {
					listaTarea.remove(i);
					break;
				}
			}
		}
		listaOficina = oficinaDAO.findAll();
		listaTerritorio = territorioDAO.findAllOrderedByCodigo();
		listaOperacion = operacionDAO.findAll();
		listaAbogado = empleadoDAO.getEmpleados("1");
		listaEstudioAbogado = estudioAbogadoDAO.findAll();

		ansItems = new ArrayList();
		ansItems.add(new SelectItem(ConstantesAdmin.CODIGO_AMARRILLO,
				ConstantesAdmin.DESCRIPCION_AMARRILLO));
		ansItems.add(new SelectItem(ConstantesAdmin.CODIGO_ROJO,
				ConstantesAdmin.DESCRIPCION_ROJO));
		ansItems.add(new SelectItem(ConstantesAdmin.CODIGO_VERDE,
				ConstantesAdmin.DESCRIPCION_VERDE));
	}

	public int mySort(Object s1, Object s2) {
		return 0;
	}

	public void limpiarFiltros(AjaxBehaviorEvent event) {
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("limpiarFiltros");
		this.filtro = new TareaBandejaVO();

		filtro.setCodTarea("");
		setCodSemaforo("0");
		filtro.setResponsable("");
		filtro.setCodEstadoExp("0");
		filtro.setCodEstadoTarea("0");
		filtro.setNomTarea("0");
		filtro.setOperacion("0");
		filtro.setTerritorioExp("0");
		filtro.setOficinaExp("0");
		filtro.setTerritorioTarea("0");
		filtro.setOficinaTarea("0");
		filtro.setEstudio("0");
		filtro.setAbogadoEstudio("0");

		filtro.setCodCentral("");
		filtro.setNumDoi("");
		filtro.setRazSoc("");
		filtro.setGestor("");
		filtro.setExpediente("");

		obtenerMisPedientes();
		// Inicicalizando el campo Estado Expediente a 2 segun el CU16
		filtro.setCodEstadoTarea(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE));
	}

	public void agregarFiltro(String prop) {
		if (!listaCriterios.contains(prop)) {
			listaCriterios.add(prop);
		}
		if (mapaHeader.get(prop).isNeutral()) {
			contador++;
			mapaHeader.get(prop).setOrder(contador);
		}
		mapaHeader.get(prop).doReverse();
		ordenarLista();
	}

	private void reorderMap(SortHeader head) {
		boolean any = true;
		for (Map.Entry<String, SortHeader> entry : mapaHeader.entrySet()) {
			any = any && entry.getValue().isNeutral();
			if (entry.getValue().getOrder() > head.getOrder()) {
				entry.getValue().setOrder(entry.getValue().getOrder() - 1);
				contador = entry.getValue().getOrder();
			} else {
				contador = head.getOrder();
			}
		}
		if (any) {
			contador = 0;
		}
	}

	public void quitarFiltro(String prop) {
		listaCriterios.remove(prop);
		mapaHeader.get(prop).doNeutral();
		reorderMap(mapaHeader.get(prop));
		ordenarLista();
	}

	private void ordenarLista() {
		String[] arr = listaCriterios
				.toArray(new String[listaCriterios.size()]);
		ordenNatural = !ordenNatural;
		ListSorter.ordenar(listaTareas, ordenNatural, arr);
		crearModelo(listaTareas);
	}

	public void buscarTareas() {
		List<TareaBandejaVO> listaTareVO = new ArrayList<TareaBandejaVO>();
		List<Historial> listaHisto = historialDAO.findAll();
		List<Expediente> listaExpe = expedienteDAO.findAll();
		for (Historial h : listaHisto) {
			listaTareVO.add(transformHistorial(h));
		}
		for (Expediente e : listaExpe) {
			listaTareVO.add(transformExpediente(e));
		}

	}

	public TareaBandejaVO transformHistorial(Historial hist) {
		TareaBandejaVO vo = new TareaBandejaVO();
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Cliente ID :"+hist.getId());
		Cliente cliente = clienteDAO.load(hist.getCliente().getId());
		Empleado empl = empleadoDAO.load(hist.getEmpleado().getId());
		// vo.setAbogadoEstudio(empl.getNombresCompletos());
		vo.setCodCentral(cliente.getCodigoCentral());
		vo.setCodEstadoExp(hist.getEstado().getId() + "");
		EstadoExpediente estExp = estadoExpedienteDAO.load(hist
				.getEstado().getId());
		vo.setDscEstadoExp(estExp.getDescripcion());
		Tarea tarea = tareaDAO.load(hist.getTarea().getId());
		vo.setCodTarea(String.valueOf(tarea.getId()));

		vo.setDscCliente(cliente.getRazonSocial());
		vo.setExpediente(String.valueOf(hist.getExpediente().getId()));
		//vo.setGestor(empl.getNombresCompletos());
		vo.setGestor(empl.getNombres().trim()+" "+empl.getApepat().trim()+" "+empl.getApemat());
		vo.setNomTarea(tarea.getDescripcion());
		vo.setNumDoi(cliente.getNumeroDoi());
		Oficina ofi = oficinaDAO.load(hist.getOficina().getId());
		vo.setOficinaTarea(ofi.getDescripcion());
		Operacion ope = operacionDAO.load(hist.getOperacion().getId());
		vo.setOperacion(ope.getDescripcion());
		vo.setRazSoc(cliente.getRazonSocial());
		vo.setResponsable(empl.getNombres().trim()+" "+empl.getApepat().trim()+" "+empl.getApemat());
		vo.setCodResponsable(empl.getCodigo());
		/*
		vo.setStrFechaAsignacion(new DateRange(hist.getFechaRegistro()));
		vo.setStrFechaAtencion(new DateRange(hist.getFechaProgramada()));
		vo.setStrFechaInicio(new DateRange(hist.getFechaEnvio()));
		vo.setStrFechaTermino(new DateRange(hist.getFechaFin()));
		*/
		vo.setStrFechaAsignacion(new DateRange(hist.getFechaEnvio()));
		vo.setStrFechaAtencion(new DateRange(hist.getFechaProgramada()));
		vo.setStrFechaInicio(new DateRange(hist.getFechaRegistro()));
		vo.setStrFechaTermino(new DateRange(hist.getFechaFin()));
		
		Territorio terri = territorioDAO
				.load(ofi.getTerritorio().getId());
		vo.setTerritorioTarea(terri.getDescripcion());
		// vo.setCodSemaforo("rojo.png");

		// Seteando el valor del estado de la tarea
		if (hist.getEstado().getId() == ConstantesBusiness.ID_ESTADO_EXPEDIENTE_CANCELADO
				&& hist.getTarea().getId() == ConstantesBusiness.ID_TAREA_GESTIONAR_COBRO_COMISION) {
			//vo.setDscEstadoTarea(ConstantesBusiness.DESC_ESTADO_TAREA_CANCELADO);
			//vo.setCodEstadoTarea(Integer.toString(ConstantesBusiness.ID_ESTADO_TAREA_CANCELADO));
			vo.setDscEstadoTarea(ConstantesBusiness.DESC_ESTADO_TAREA_COMPLETADO);
			vo.setCodEstadoTarea(Integer.toString(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO));
		} else {
			vo
					.setDscEstadoTarea(ConstantesBusiness.DESC_ESTADO_TAREA_COMPLETADO);
			vo.setCodEstadoTarea(Integer.toString(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO));
		}
		vo.setId(String.valueOf(hist.getExpediente().getId())+tarea.getId());
		return vo;
	}

	private TareaBandejaVO crearTareaBandejaVO(ExpedienteCC expedienteCC) {
		TareaBandejaVO tareaBandeja = new TareaBandejaVO();
		tareaBandeja.setExpedienteCC(expedienteCC);
		tareaBandeja.setId(expedienteCC.getCodigoExpediente()
				+ expedienteCC.getDatosFlujoCtaCte().getIdTarea()
				+ SUF_PENDIENTES);
		tareaBandeja.setTerritorioExp(expedienteCC.getDesTerritorio());
		tareaBandeja.setCodTarea(expedienteCC.getDatosFlujoCtaCte()
				.getIdTarea());
		
		/*Comentado 20-11-2015*/
//		tareaBandeja.getStrFechaAsignacion().setTarget(
//				expedienteCC.getActivado().getTime());
//		tareaBandeja.getStrFechaInicio().setTarget(
//				expedienteCC.getFechaRegistro());
		
		tareaBandeja.getStrFechaAsignacion().setTarget(
				expedienteCC.getActivado().getTime());
		tareaBandeja.getStrFechaInicio().setTarget(
				expedienteCC.getActivado().getTime());
		
		
		
		/*---------------------*/		
		// fecha en la q se registro el
													// expediente
		// tareaBandeja.getStrFechaAtencion().setTarget(expedienteCC.getFechaAtencion());
		// tareaBandeja.getStrFechaTermino().setTarget(Util.parseStringToDate("24/05/2012",
		// "dd/MM/yyyy"));
		// tareaBandeja.setCodSemaforo("verde.png");
		tareaBandeja.setDscCliente(expedienteCC.getRazonSocialCliente());
		tareaBandeja.setDscEstadoTarea(expedienteCC.getEstadoTarea());
		tareaBandeja.setDscEstadoExp(expedienteCC.getEstadoExpediente());
		tareaBandeja.setExpediente(expedienteCC.getCodigoExpediente());
		tareaBandeja.setNomTarea(expedienteCC.getDatosFlujoCtaCte()
				.getNombreTarea());
		tareaBandeja.setOficinaExp(expedienteCC.getDesOficina());
		tareaBandeja.setOperacion(expedienteCC.getDesOperacion());
		tareaBandeja.setResponsable(expedienteCC.getNomUsuarioActual());
		tareaBandeja.setCodResponsable(expedienteCC.getCodUsuarioActual());
		tareaBandeja.setExpedienteCC(expedienteCC);
		tareaBandeja.setTerritorioTarea(expedienteCC.getDesTerritorio());
		tareaBandeja.setOficinaTarea(expedienteCC.getDesOficina());
		
		if (expedienteCC.getDatosFlujoCtaCte().getIdTarea().equals("12") ||
			expedienteCC.getDatosFlujoCtaCte().getIdTarea().equals("13") ||
			expedienteCC.getDatosFlujoCtaCte().getIdTarea().equals("14")) {
		    
		    Empleado empAB = empleadoDAO.findByCodigo(expedienteCC.getCodUsuarioActual());
		    if (empAB != null && empAB.getEstudio() != null) {
			    EstudioAbogado estudioDAO = estudioAbogadoDAO.findByIdEstudio(empAB.getEstudio().getId());
			    tareaBandeja.setEstudio(estudioDAO.getDescripcion().trim());
			    tareaBandeja.setAbogadoEstudio(empAB.getNombres().trim()+" "+empAB.getApepat().trim()+" "+empAB.getApemat().trim());
		    }
		}else{
		    tareaBandeja.setAbogadoEstudio(expedienteCC.getDatosFlujoCtaCte().getNomUsuarioAbogado());
		    tareaBandeja.setEstudio(expedienteCC.getDatosFlujoCtaCte().getNomEstudioAbogado());
		}		
		
		return tareaBandeja;
	}

	public TareaBandejaVO transformExpediente(Expediente ex) {
		TareaBandejaVO vo = new TareaBandejaVO();
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("ex.getId() ** " + ex.getId());
		vo.setId(ex.getId() + SUF_PRE_REGISTRO);
		Cliente cliente = ex.getCliente();
		Empleado empl = ex.getEmpleado();
		// vo.setAbogadoEstudio(empl.getNombresCompletos());
		vo.setCodCentral(cliente.getCodigoCentral());
		EstadoExpediente estEx = estadoExpedienteDAO.load(ex.getEstado()
				.getId());
		vo.setCodEstadoExp(estEx.getId() + "");
		List<ExpedienteTarea> lista = expedienteTareaDAO
				.findByIdExpediente(ex.getId());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("lista.size() ** " + lista.size());
		ExpedienteTarea expeTar = lista.isEmpty() ? new ExpedienteTarea()
				: lista.get(0);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("expeTar.getTarea() ** " + expeTar.getTarea());
		Tarea tarea = tareaDAO.load(expeTar.getTarea().getId());
		vo.setCodTarea(String.valueOf(tarea.getId()));

		vo.setDscCliente(cliente.getRazonSocial());
		vo.setExpediente(String.valueOf(ex.getId()));
		//vo.setGestor(empl.getNombresCompletos());
		vo.setGestor(empl.getNombres().trim()+" "+empl.getApepat().trim()+" "+empl.getApemat());
		vo.setNomTarea(tarea.getDescripcion());
		vo.setNumDoi(cliente.getNumeroDoi());
		Operacion ope = operacionDAO.load(ex.getOperacion().getId());
		vo.setOperacion(ope.getDescripcion());
		vo.setRazSoc(cliente.getRazonSocial());
		vo.setResponsable(empl.getNombres().trim()+" "+empl.getApepat().trim()+" "+empl.getApemat());
		vo.setCodResponsable(empl.getCodigo());
		/*vo.setStrFechaAsignacion(new DateRange(ex.getFechaRegistro()));
		vo.setStrFechaAtencion(new DateRange(ex.getFechaProgramada()));
		vo.setStrFechaInicio(new DateRange(ex.getFechaEnvio()));
		vo.setStrFechaTermino(new DateRange(ex.getFechaFin()));
		*/
		vo.setStrFechaAsignacion(new DateRange(ex.getFechaEnvio()));
		vo.setStrFechaAtencion(new DateRange(ex.getFechaProgramada()));
		vo.setStrFechaInicio(new DateRange(ex.getFechaRegistro()));
		vo.setStrFechaTermino(new DateRange(ex.getFechaFin()));
		
		vo.setDscEstadoTarea(ex.getExpedienteTareas().iterator().next()
				.getEstadoTarea().getDescripcion());
		vo.setDscEstadoExp(ex.getEstado().getDescripcion());

		Oficina ofi = oficinaDAO.load(ex.getOficina().getId());
		vo.setOficinaTarea(ofi.getDescripcion());
		Territorio terri = territorioDAO
				.load(ofi.getTerritorio().getId());
		vo.setTerritorioTarea(terri.getDescripcion());
		// vo.setCodSemaforo("rojo.png");

		EstudioAbogado estudioAbogado = empl.getEstudio();
		vo.setEstudio(estudioAbogado == null ? "" : estudioAbogado
				.getDescripcion());
		return vo;
	}


	
	
	
	public void filtraTareas(AjaxBehaviorEvent event) {
		LOG.info("filtraTareas()");
		LOG.info("estado expediente:"+filtro.getCodEstadoExp());
		listaTareas = new ArrayList<TareaBandejaVO>();
		// SI se ha selecciona el estado del expediente, usamos el valor como
		// criterio para buscar en repositorios
		addObjectSession(Constantes.BANDJ_FILTRO_BASTANTEO, filtro);
		
		if (!filtro.getCodEstadoExp().equalsIgnoreCase("0")) {
			// Si el estado es 3.Terminado o 4.Cancelado
			LOG.info("filtro <> 0:"+filtro.getCodEstadoExp());
			if (filtro.getCodEstadoExp().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_TERMINADO))
					&& !filtro.getCodEstadoTarea().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE))
					&& !filtro.getCodEstadoTarea().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PREREGISTRO))) {
				listaTareas = buscarEnHistorial(filtro);
				LOG.info("Ternminado ..." + listaTareas.size());
				

			} else if (filtro.getCodEstadoExp().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_CANCELADO))
					&& !filtro.getCodEstadoTarea().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE))
					&& !filtro.getCodEstadoTarea().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PREREGISTRO))) {
				listaTareas = buscarEnHistorial(filtro);
				LOG.info("Cancelado..." + listaTareas.size());

			} else if (filtro.getCodEstadoExp().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_PREREGISTRO))
					&& (filtro.getCodEstadoTarea().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PREREGISTRO)) || 
						filtro.getCodEstadoTarea().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO)) || 
						filtro.getCodEstadoTarea().equals(String.valueOf(0)) 
						)
					   ) { // Si el estado es 1.Pre registro
				LOG.info("********************PRE REGISTRO*******************************");
				listaTareas = buscarEnExpediente(filtro);
				LOG.info("Preregistro.." + listaTareas.size());
			}else if (filtro.getCodEstadoExp().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO))
                         && filtro.getCodEstadoTarea().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO))) {
                 LOG.info("********************EXPEDIENTE EN CURSO Y TAREA COMPLETADA*******************************");
                 listaTareas = buscarEnHistorial(filtro); 
			}else if (filtro.getCodEstadoExp().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO))
                         && filtro.getCodEstadoTarea().equals(String.valueOf("0"))) {
                 LOG.info("********************EXPEDIENTE EN CURSO Y TAREA TODOS*******************************");
                 ConsultaCC consulta = new ConsultaCC();
                 LOG.info("********************CONSULTA EN PROCESS*******************************");
 				 consulta = filtroProcess();
 				 consulta.setConsiderarUsuarios(false);
 				 listaTareasBase = cargaRegProcess(consulta);
 				 listaTareas = new ArrayList<TareaBandejaVO>(listaTareasBase);
				 LOG.info("en curso...." + listaTareas.size());
				 LOG.info("********************CONSULTA EN PROCESS*******************************");
				 listaTareas.addAll(buscarEnHistorial(filtro));  
			} else if (filtro.getCodEstadoExp().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO))) { // Si el estado es 2.En curso
				LOG.info("********************EN CURSO*******************************");
				ConsultaCC consulta = new ConsultaCC();
				consulta = filtroProcess();
				consulta.setConsiderarUsuarios(false);
				listaTareasBase = cargaRegProcess(consulta);
				LOG.info("********************listaTareasBase.size()*******************************"+listaTareasBase.size());
				listaTareas = new ArrayList<TareaBandejaVO>(listaTareasBase);
				LOG.info("en curso...." + listaTareas.size());
			}
		} else {
			if (filtro.getCodEstadoTarea().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO))) {
				LOG.info("********************EXPEDIENTE TODOS Y TAREA COMPLETADA********************");
				listaTareas = buscarEnHistorial(filtro);
			} else if (filtro.getCodEstadoTarea().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PREREGISTRO))) {
				LOG.info("********************EXPEDIENTE TODOS Y TAREA PRE REGISTRO********************");
				listaTareas = buscarEnExpediente(filtro);
			} else if (filtro.getCodEstadoTarea().equals(String.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE))) {
				LOG.info("********************EXPEDIENTE TODOS Y TAREA EN PENDIENTE*******************************");
				ConsultaCC consulta = new ConsultaCC();
				consulta = filtroProcess();
				consulta.setConsiderarUsuarios(false);
				listaTareasBase = cargaRegProcess(consulta);
				LOG.info("********************listaTareasBase.size()*******************************"+listaTareasBase.size());
				listaTareas = new ArrayList<TareaBandejaVO>(listaTareasBase);
				LOG.info("en curso...." + listaTareas.size());
			} else {
				LOG.info("buscarSegunFiltrosLLenados()--no se ingreso codigo Expediente");
				buscarSegunFiltrosLLenados();
			}
		}
		actualizaAns(listaTareas);
		
		filtarEnMemoria();
		
		// [Begin]-[15.07.23]-[Ordenar lista Bandeja Trabajo]
		ListSorter.ordenarSemaforo(listaTareas, "Pendiente");
		// [End]-[15.07.23]
		ListSorter.ordenarSemaforo(listaTareas, ConstantesAdmin.CODIGO_ROJO);
		ListSorter.ordenarOperaciones(listaTareas,ConstantesAdmin.ID_OPERACION_REVOCATORIA);

		//LOG.info("***********ordenando expedientex de la bandeja***********");
		//ListSorter.ordenarExpedientes(listaTareas);
        //for (Persona p : personas) {
            //System.out.println(p);
        //}	
		if(listaTareas!=null && listaTareas.size()>0)
			addObjectSession(Constantes.BANDJ_TRABAJO_BASTANTEO, "1");
		else
			removeObjectSession(Constantes.BANDJ_TRABAJO_BASTANTEO);
		
		crearModelo(listaTareas);
		super.pageFirst(null);
		LOG.info("Resultado Busqueda : " + listaTareas.size());		
	}

	private void filtarEnMemoria(){
		TareaBandejaVO nuevoFiltro = new TareaBandejaVO();
		nuevoFiltro.setCodEstadoExp("");
		nuevoFiltro.setCodEstadoTarea("");
		nuevoFiltro.setNomTarea("");
		nuevoFiltro.setOperacion("");
		nuevoFiltro.setTerritorioExp("");
		nuevoFiltro.setOficinaExp("");
		nuevoFiltro.setTerritorioTarea("");
		nuevoFiltro.setOficinaTarea("");
		nuevoFiltro.setEstudio("");
		nuevoFiltro.setAbogadoEstudio("");		
		nuevoFiltro.setCodSemaforo("");
		nuevoFiltro.setDscEstadoTarea("");
		
		String codSemaforo = getCodSemaforo();
		if (codSemaforo != null && !codSemaforo.equals("0")) {
			nuevoFiltro.setCodSemaforoT(codSemaforo);			
		}

		DateRange rangoFechaAtencion = filtro.getStrFechaAtencion();
		if (rangoFechaAtencion != null
				&& (rangoFechaAtencion.getLimInf() != null
				|| rangoFechaAtencion.getLimSup() != null)) {		
			listaTareas = filtroRangoFechas( rangoFechaAtencion );			
		}
		
		String estTarea = filtro.getDscEstadoTarea();
		if (filtro.getCodEstadoTarea() != null && !filtro.getCodEstadoTarea().equals("0")
				&& Integer.valueOf(filtro.getCodEstadoTarea()) == ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO) {			
			nuevoFiltro.setDscEstadoTarea(estTarea);			
		}

		String estudio = filtro.getEstudio();
		if (estudio != null	&& !estudio.equals("0")) {			
			EstudioAbogado estudioAbogado = estudioAbogadoDAO.findByIdEstudio(Integer.valueOf(estudio));
			nuevoFiltro.setEstudio(estudioAbogado.getDescripcion().trim());			
		}	
		
		String abogado = filtro.getAbogadoEstudio();
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("abogadoMemoria :"+abogado);
		if (abogado != null	&& !abogado.equals("0")) {
			Empleado empleado = empleadoDAO.findByCodigo(abogado);
			nuevoFiltro.setAbogadoEstudio(empleado.getNombres().trim()+" "+empleado.getApepat().trim()+" "+empleado.getApemat().trim());	
		}
		
		CollectionUtils.filter(listaTareas,	new GenericPredicate<TareaBandejaVO>(nuevoFiltro));	
	}
	
	private List<TareaBandejaVO> buscarEnHistorial(TareaBandejaVO f) {
		List<TareaBandejaVO> lista = new ArrayList<TareaBandejaVO>();
		List<Historial> listaHistorial = null;

		//String EstadoTarea = (f.getCodEstadoTarea().equals("") ? "0" : f.getCodEstadoTarea());
		
		String strIdTarea = (f.getCodTarea().equals("") ? "0" : f.getCodTarea());
		String strCodResponsableTarea = (f.getResponsable().equals("") ? null
				: f.getResponsable());
		String strNombreTarea = (f.getNomTarea().equals("") ? "0" : f
				.getNomTarea());
		String strCodigoCentral = (f.getCodCentral().equals("") ? null :
				f.getCodCentral());
		String strNroDoi = (f.getNumDoi().equals("") ? null :
				f.getNumDoi());
		String strRzSocial = (f.getRazSoc().equals("") ? null : f.getRazSoc());
		String strCodGestor = (f.getGestor().equals("") ? null : 
				f.getGestor());
		String nroExp = (f.getExpediente().equals("") ? "0" : f.getExpediente());

		Date strFechaAsignacionLimInf = Util.truncateDate(f.getStrFechaAsignacion().getLimInf());
		Date strFechaAsignacionLimSup = Util.truncateDate(f.getStrFechaAsignacion().getLimSup());

		Date strFechaAtencionLimInf = Util.truncateDate(f.getStrFechaAtencion().getLimInf());
		Date strFechaAtencionLimSup = Util.truncateDate(f.getStrFechaAtencion().getLimSup());

		Date strFechaInicioLimInf = Util.truncateDate(f.getStrFechaInicio().getLimInf());
		Date strFechaInicioLimSup = Util.truncateDate(f.getStrFechaInicio().getLimSup());

		Date strFechaTerminoLimInf = Util.truncateDate(f.getStrFechaTermino().getLimInf());
		Date strFechaTerminoLimSup = Util.truncateDate(f.getStrFechaTermino().getLimSup());

		LOG.info("strFechaAsignacionLimInf.." + strFechaAsignacionLimInf);
		LOG.info("strFechaAsignacionLimSup.." + strFechaAsignacionLimSup);
		LOG.info("strFechaAtencionLimInf.." + strFechaAtencionLimInf);
		LOG.info("strFechaAtencionLimSup.." + strFechaAtencionLimSup);

		LOG.info("strFechaInicioLimInf.." + strFechaInicioLimInf);
		LOG.info("strFechaInicioLimSup.." + strFechaInicioLimSup);
		LOG.info("strFechaTerminoLimInf.." + strFechaTerminoLimInf);
		LOG.info("strFechaTerminoLimSup.." + strFechaTerminoLimSup);

		if (strCodResponsableTarea!=null) {
			List<Empleado> listaEmpleado = listaEmpleados(strCodResponsableTarea.toUpperCase());
			if (listaEmpleado!=null || listaEmpleado.isEmpty()){				
				for (Empleado listaEmp : listaEmpleado) {
					//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("BuscarResponsableHis"+listaEmp.getCodigo()+"-"+listaEmp.getNombresCompletos());
					strCodResponsableTarea = listaEmp.getCodigo();
					listaHistorial = historialDAO.findByFiltrofromHistorial(
							f.getCodEstadoExp(), strIdTarea, strCodResponsableTarea, f
							.getOficinaTarea(), f.getTerritorioTarea(), strNombreTarea,
							strCodigoCentral, f.getOperacion(), strNroDoi, strRzSocial,
							strCodGestor, nroExp, f.getTerritorioExp(), f.getOficinaExp(),
							f.getEstudio(), f.getAbogadoEstudio(), strFechaAtencionLimInf,
							strFechaAtencionLimSup, strFechaAsignacionLimInf,
							strFechaAsignacionLimSup, strFechaInicioLimInf,
							strFechaInicioLimSup, strFechaTerminoLimInf,
							strFechaTerminoLimSup);
					
					for (Historial h : listaHistorial) {
						lista.add(transformHistorial(h));
					}
				}
			}
		}else{
			LOG.info("findByFiltrofromHistorial() estado de la tarea:" + f.getCodEstadoTarea()+" estado del expediente:"+f.getCodEstadoExp()+" nro expediente:"+nroExp);
			listaHistorial = historialDAO.findByFiltrofromHistorial(f
					.getCodEstadoExp(), strIdTarea, strCodResponsableTarea, f
					.getOficinaTarea(), f.getTerritorioTarea(), strNombreTarea,
					strCodigoCentral, f.getOperacion(), strNroDoi, strRzSocial,
					strCodGestor, nroExp, f.getTerritorioExp(), f.getOficinaExp(),
					f.getEstudio(), f.getAbogadoEstudio(), strFechaAtencionLimInf,
					strFechaAtencionLimSup, strFechaAsignacionLimInf,
					strFechaAsignacionLimSup, strFechaInicioLimInf,
					strFechaInicioLimSup, strFechaTerminoLimInf,
					strFechaTerminoLimSup);
			// (10, 24, 1, 1, null, "81546", 1, "68552456", "RZ Soc", 24, 3147, 1,
			// 1, 0, 0, null, null, null, null);
			LOG.info("lista historial size:" + listaHistorial.size());
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("lista historial size:" + listaHistorial.size());
			Map<Integer,String> mapaExpedientes = new HashMap<Integer,String>();
			Map<Integer,Date> mapaFechas = new HashMap<Integer,Date>();

			for (Historial h : listaHistorial) {
				if(h.getEstado().getId()==ConstantesBusiness.ID_ESTADO_EXPEDIENTE_CANCELADO || h.getEstado().getId()==ConstantesBusiness.ID_ESTADO_EXPEDIENTE_TERMINADO)
				{
					mapaExpedientes.put(h.getExpediente().getId(),(h.getEstado().getId()==3?"Terminado":"Cancelado"));
					//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Mapa-----idExpediente: " + h.getExpediente().getId()+" descripcion:"+(h.getEstado().getId()==3?"Terminado":"Cancelado"));
				}
				
				if(h.getTarea().getId()==ConstantesBusiness.ID_TAREA_REGISTRAR_EXPEDIENTE )
				{
					mapaFechas.put(h.getExpediente().getId(),h.getFechaRegistro());
					//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Mapa-----idExpediente: " + h.getExpediente().getId()+" Fecha:"+h.getFechaRegistro().toString());
				}
			}
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("expedientes terminados-acabados: " + mapaExpedientes.size());
			LOG.info("expedientes terminados-acabados: " + mapaExpedientes.size());
			
			Historial hAux;
			for (Historial h : listaHistorial) {
				hAux=new Historial();
				hAux=h;
				if(encuentraHistorial(mapaExpedientes,h.getExpediente().getId()))
				{
					hAux=actualizarHistorial(h,mapaExpedientes,true);
				}
				actualizarHistorial(hAux,mapaFechas,false);
				
				lista.add(transformHistorial(hAux));
			}
		}
		LOG.info("listaTareas Historial: " + lista.size());
		return lista;
	}

	private boolean encuentraHistorial(Map mapa,int idExpediente)
	{
		boolean flag=false;
		Iterator it = mapa.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			if(Integer.parseInt(e.getKey().toString())==idExpediente)
			{
				flag= true;
				//+POR SOLICITUD BBVA+System.out..println("expediente encontrado: " + e.getKey().toString()+ " descripción:" +e.getValue().toString());
				break;
			}
			else
			{
				flag= false;
			}
		}
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("fin encuentra-historial");
		return flag;
	}

	private Historial actualizarHistorial(Historial h,Map mapa,boolean bol)
	{
		Historial hTemp=null; 
		EstadoExpediente estadoExp=null;
		
		Iterator it = mapa.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
				if(Integer.parseInt(e.getKey().toString().trim())==h.getExpediente().getId())
				{
					if(bol)
					{
						hTemp=new Historial();
						estadoExp=new EstadoExpediente();
						hTemp=h;
						estadoExp.setId((e.getValue().toString().trim()=="Terminado"?3:4));
						estadoExp.setDescripcion(e.getValue().toString());
						hTemp.setEstado(estadoExp);
						break;
					}
					else 
					{
						hTemp=new Historial();
						hTemp=h;
						Date fechaReg=new Date();
						fechaReg=(Date)(e.getValue());
						hTemp.setFechaRegistro(fechaReg);
					}
				}
		}
		return hTemp;
	}
	
	private List<TareaBandejaVO> buscarEnExpediente(TareaBandejaVO f) {
		List<Expediente> listaExpediente = null;
		List<TareaBandejaVO> lista = new ArrayList<TareaBandejaVO>();

		String nroExp = (f.getExpediente().equals("") ? "0" : f.getExpediente());
		String strCodEstadoTarea = (f.getCodEstadoTarea().equals("") ? "0" : f
				.getCodEstadoTarea());
		String strCodigoCentral = (f.getCodCentral().equals("") ? null :
				f.getCodCentral());
		// String strIdOperacion =
		// (f.getOperacion().equals("")?"0":f.getOperacion());
		String strNroDoi = (f.getNumDoi().equals("") ? null :
				f.getNumDoi());
		String strRzSocial = (f.getRazSoc().equals("") ? null : f.getRazSoc());
		String strCodGestor = (f.getGestor().equals("") ? null :
				f.getGestor());
		Date strFechaInicioLimInf = Util.truncateDate(f.getStrFechaInicio().getLimInf());
		Date strFechaInicioLimSup = null;
		if (f.getStrFechaInicio().getLimSup() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(f.getStrFechaInicio().getLimSup());
			calendar.add(Calendar.DATE, 1);
			strFechaInicioLimSup = Util.truncateDate(calendar.getTime());
		}
		
		Date strFechaAsignacionLimInf = Util.truncateDate(f.getStrFechaAsignacion().getLimInf());
		Date strFechaAsignacionLimSup = null;
		if (f.getStrFechaAsignacion().getLimSup() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(f.getStrFechaAsignacion().getLimSup());
			calendar.add(Calendar.DATE, 1);
			strFechaAsignacionLimSup = Util.truncateDate(calendar.getTime());
		}
				
		String strCodResponsableTarea = (f.getResponsable().equals("") ? null
				: "'" + f.getResponsable() + "'");
		
		LOG.info("strFechaInicioLimInf.." + strFechaInicioLimInf);
		LOG.info("strFechaInicioLimSup.." + strFechaInicioLimSup);
		
		LOG.info("nroExp: {}", nroExp);
		LOG.info("getCodEstadoExp: {}", f.getCodEstadoExp());
		LOG.info("strCodigoCentral: {}", strCodigoCentral);
		LOG.info("getOperacion: {}", f.getOperacion());
		LOG.info("strNroDoi: {}", strNroDoi);
		LOG.info("strRzSocial: {}", strRzSocial);
		LOG.info("strCodGestor: {}", strCodGestor);
		LOG.info("getTerritorioExp: {}", f.getTerritorioExp());
		LOG.info("getOficinaExp: {}", f.getOficinaExp());
		LOG.info("strCodGestor: {}", strCodGestor);

		if (strCodEstadoTarea.equalsIgnoreCase(String
				.valueOf(ConstantesBusiness.ID_ESTADO_TAREA_PREREGISTRO))
				|| strCodEstadoTarea.equals("0")) {
		
			// no es necesario validar los limites superiores porque se supone que vienen los 2 campos
			if (strFechaAsignacionLimInf != null && strFechaInicioLimInf == null) {
				strFechaInicioLimInf = strFechaAsignacionLimInf;
				strFechaInicioLimSup = strFechaAsignacionLimSup;
			}
			
			if (strCodGestor!=null) {
				List<Empleado> listaEmpleado = listaEmpleados(strCodGestor.toUpperCase());
				if (listaEmpleado!=null && !listaEmpleado.isEmpty()){				
					for (Empleado listaEmp : listaEmpleado) {
						//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("BuscarResponsableExp"+listaEmp.getCodigo()+"-"+listaEmp.getNombresCompletos());
						strCodGestor = listaEmp.getCodigo();
			
						listaExpediente = expedienteDAO.findByFiltrofromExpediente(
								nroExp, f.getCodEstadoExp(), strCodigoCentral, f
										.getOperacion(), strNroDoi, strRzSocial,
								strCodGestor, f.getTerritorioExp(), f.getOficinaExp(),
								strCodResponsableTarea, strFechaInicioLimInf, strFechaInicioLimSup);						
						for (Expediente e : listaExpediente) {
							lista.add(transformExpediente(e));
						}
					}
				}
			}else{
				listaExpediente = expedienteDAO.findByFiltrofromExpediente(
						nroExp, f.getCodEstadoExp(), strCodigoCentral, f
								.getOperacion(), strNroDoi, strRzSocial,
						strCodGestor, f.getTerritorioExp(), f.getOficinaExp(),
						strCodResponsableTarea, strFechaInicioLimInf, strFechaInicioLimSup);
				// (10, 24, 1, 1, null, "81546", 1, "68552456", "RZ Soc", 24, 3147,
				// 1, 1, 0, 0, null, null, null, null);
				for (Expediente e : listaExpediente) {
					lista.add(transformExpediente(e));
				}	
			}
		}
		// else no ejecutar la consulta, porque en la tabla solo hay tareas con
		// estado Preregistro

		LOG.info("listaTareas Expediente: " + lista.size());
		return lista;
	}

	private int armandoArregloFiltros() {
		// Retorna la cantidad de filtros seleccionados
		LOG.info("armandoArregloFiltros : {}");

		for (int i = 0; i < arrayBusqueda.length; i++) {
			arrayBusqueda[i] = 0;
		}

		if (filtro.getCodTarea() != null && !filtro.getCodTarea().equals("")) {
			arrayBusqueda[0] = 1;
			LOG.info("criterio getCodTarea.." + filtro.getCodTarea());
		}
		
		if (filtro.getCodEstadoTarea() != null && !filtro.getCodEstadoTarea().equals("0")
				&& Integer.valueOf(filtro.getCodEstadoTarea()) != ConstantesBusiness.ID_ESTADO_TAREA_COMPLETADO) {
			arrayBusqueda[1] = 1;
			LOG.info("criterio getCodEstadoTarea.."+ filtro.getCodEstadoTarea());
			LOG.info("criterio filtro.getDscEstadoTarea().."+ filtro.getDscEstadoTarea());
			LOG.info("criterio filtro.getDscEstadoExp().."+ filtro.getDscEstadoExp());
		}

		if (filtro.getExpediente() != null
				&& !filtro.getExpediente().equals("")) {
			arrayBusqueda[2] = 1;
			LOG.info("criterio getExpediente.." + filtro.getExpediente());
		}

		if (filtro.getCodEstadoExp() != null
				&& !filtro.getCodEstadoExp().equals("0")) {
			arrayBusqueda[3] = 1;
			LOG.info("criterio getCodEstadoExp.." + filtro.getCodEstadoExp());
		}

		if (filtro.getNomTarea() != null && !filtro.getNomTarea().equals("0")) {
			arrayBusqueda[4] = 1;
			LOG.info("criterio getNomTarea.." + filtro.getNomTarea());
		}
		if (filtro.getCodCentral() != null
				&& !filtro.getCodCentral().equals("")) {
			arrayBusqueda[5] = 1;
			LOG.info("criterio getCodCentral.." + filtro.getCodCentral());
		}
		if (filtro.getOperacion() != null && !filtro.getOperacion().equals("0")) {
			arrayBusqueda[6] = 1;
			LOG.info("criterio getOperacion.." + filtro.getOperacion());
		}
		if (filtro.getStrFechaAsignacion() != null
				&& filtro.getStrFechaAsignacion().getLimInf() != null
				&& filtro.getStrFechaAsignacion().getLimSup() != null) {
			arrayBusqueda[7] = 1;
			LOG.info("criterio getStrFechaAsignacion.."
					+ filtro.getStrFechaAsignacion().getLimInf());
		}
		if (filtro.getNumDoi() != null && !filtro.getNumDoi().equals("")) {
			arrayBusqueda[8] = 1;
			LOG.info("criterio getNumDoi.." + filtro.getNumDoi());
		}
		if (filtro.getRazSoc() != null && !filtro.getRazSoc().equals("")) {
			arrayBusqueda[9] = 1;
			LOG.info("criterio getRazSoc.." + filtro.getRazSoc());
		}
		if (filtro.getStrFechaAtencion() != null
				&& filtro.getStrFechaAtencion().getLimInf() != null
				&& filtro.getStrFechaAtencion().getLimSup() != null) {
			arrayBusqueda[10] = 1;
			LOG.info("criterio getStrFechaAtencion.."
					+ filtro.getStrFechaAtencion());
		}
		if (filtro.getGestor() != null && !filtro.getGestor().equals("")) {
			arrayBusqueda[11] = 1;
			LOG.info("criterio getGestor.." + filtro.getGestor());
		}
		if (filtro.getTerritorioExp() != null
				&& !filtro.getTerritorioExp().equals("0")) {
			arrayBusqueda[12] = 1;
			LOG.info("criterio getTerritorioExp.." + filtro.getTerritorioExp());
		}
		if (filtro.getOficinaExp() != null
				&& !filtro.getOficinaExp().equals("0")) {
			arrayBusqueda[13] = 1;
			LOG.info("criterio getOficinaExp.." + filtro.getOficinaExp());
		}
		if (filtro.getResponsable() != null
				&& !filtro.getResponsable().equals("")) {
			arrayBusqueda[14] = 1;
			LOG.info("criterio getResponsable.." + filtro.getResponsable());
		}
		/*
		 * if ( getCodSemaforo() != null && !getCodSemaforo().equals("0")) {
		 * arrayBusqueda[15] = 1;
		 * LOG.info("criterio getCodSemaforo.."+getCodSemaforo() ); }
		 */
		if (filtro.getStrFechaInicio() != null
				&& filtro.getStrFechaInicio().getLimInf() != null
				&& filtro.getStrFechaInicio().getLimSup() != null) {
			arrayBusqueda[16] = 1;
			LOG.info("criterio getStrFechaInicio.."
					+ filtro.getStrFechaInicio());
		}
		/*if (filtro.getEstudio() != null && !filtro.getEstudio().equals("0")) {
			arrayBusqueda[17] = 1;
			LOG.info("criterio getEstudio.." + filtro.getEstudio());
		}*/
		if (filtro.getTerritorioTarea() != null
				&& !filtro.getTerritorioTarea().equals("0")) {
			arrayBusqueda[18] = 1;
			LOG.info("criterio getTerritorioTarea.."
					+ filtro.getTerritorioTarea());
		}
		if (filtro.getOficinaTarea() != null
				&& !filtro.getOficinaTarea().equals("0")) {
			arrayBusqueda[19] = 1;
			LOG.info("criterio getOficinaTarea.." + filtro.getOficinaTarea());
		}
		if (filtro.getStrFechaTermino() != null
				&& filtro.getStrFechaTermino().getLimInf() != null
				&& filtro.getStrFechaTermino().getLimSup() != null) {
			arrayBusqueda[20] = 1;
			LOG.info("criterio getStrFechaTermino.."
					+ filtro.getStrFechaTermino());
		}
		if (filtro.getAbogadoEstudio() != null
				&& !filtro.getAbogadoEstudio().equals("0")) {
			arrayBusqueda[21] = 1;
			LOG.info("criterio getAbogadoEstudio.."
					+ filtro.getAbogadoEstudio());
		}

		int cant = 0;
		for (int i = 0; i < arrayBusqueda.length; i++) {
			if (arrayBusqueda[i] == 1)
				cant = cant + 1;
		}

		return cant;
	}

	public void buscarSegunFiltrosLLenados() {
		LOG.info("buscarSegunFiltrosLLenados : {}");
		int sumatoria = 0;
		int numFiltros = armandoArregloFiltros();
		LOG.info("# criterios de busqueda..." + numFiltros);

		for (int i = 0; i < arrayBusqueda.length; i++) {
			sumatoria = sumatoria + arrayBusqueda[i] * arrayTablaHistorico[i];
		}

		if (numFiltros == sumatoria) { // buscar en Historico
			LOG.info("# coincidencias en historial..." + sumatoria);
			listaTareas.addAll(buscarEnHistorial(filtro));
			LOG.info("# coincidencias en historial...size " + listaTareas.size());
		}

		sumatoria = 0;
		for (int i = 0; i < arrayBusqueda.length; i++) {
			sumatoria = sumatoria + arrayBusqueda[i] * arrayTablaExpediente[i];
		}

		if (numFiltros == sumatoria) { // buscar en Expediente
			LOG.info("# coincidencias en expediente..." + sumatoria);
			listaTareas.addAll(buscarEnExpediente(filtro));
			LOG.info("# coincidencias en expediente...size " + listaTareas.size());
		}

		sumatoria = 0;
		for (int i = 0; i < arrayBusqueda.length; i++) {
			sumatoria = sumatoria + arrayBusqueda[i] * arrayProcess[i];
		}
		LOG.info("numFiltros process.." + numFiltros);
		LOG.info("sumatoria process..." + sumatoria);
		if (numFiltros == sumatoria) { // buscar en process
			LOG.info("# coincidencias en process..." + numFiltros);
			/**/
			ConsultaCC consulta = filtroProcess();
			consulta.setConsiderarUsuarios(false);
			listaTareasBase = cargaRegProcess(consulta);
			/**/
			listaTareas.addAll(listaTareasBase);
			LOG.info("# coincidencias en process...size " + listaTareas.size());
		}
	}

	public ConsultaCC filtroProcess() {
		ConsultaCC consulta = new ConsultaCC();
		// consulta.setIdEstadoExpediente(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO+"");
		LOG.info("*********************BandejaMB-filtroProcess()**************************");
		LOG.info("*********************consulta.getIdEstadoExpediente()**********"+consulta.getIdEstadoExpediente());
		if (!(filtro.getExpediente() == null || filtro.getExpediente().equals(""))) {
			LOG.info("f1- filtro.getExpediente(): " + filtro.getExpediente());
			consulta.setCodigoExpediente(filtro.getExpediente());
		}
		if (!(filtro.getCodEstadoExp() == null || filtro.getCodEstadoExp().equals("0"))) {
			LOG.info("f2-filtro.getCodEstadoExp(): " + filtro.getCodEstadoExp());
			consulta.setIdEstadoExpediente(filtro.getCodEstadoExp());
		}
		if (!(filtro.getCodEstadoTarea() == null || filtro.getCodEstadoTarea().equals("0"))) {
			LOG.info("f3- filtro.getCodEstadoTarea(): " + filtro.getCodEstadoTarea());
			consulta.setIdEstadoTarea(filtro.getCodEstadoTarea());
		}
		if (!(filtro.getCodTarea() == null || filtro.getCodTarea().equals(""))) {
			LOG.info("f4-filtro.getCodTarea(): " + filtro.getCodTarea());
			consulta.setNumeroTarea(filtro.getCodTarea());
		}
		if (!(filtro.getNomTarea() == null || filtro.getNomTarea().equals("0"))) {
			LOG.info("f5-filtro.getNomTarea(): " + filtro.getNomTarea());
			consulta.setNombreTarea(filtro.getNomTarea());
		}
		if (!(filtro.getCodCentral() == null || filtro.getCodCentral().equals(""))) {
			LOG.info("f6-filtro.getCodCentral(): " + filtro.getCodCentral());
			consulta.setCodCentralCliente(filtro.getCodCentral());
		}
		if (!(filtro.getOperacion() == null || filtro.getOperacion().equals("0"))) {
			LOG.info("f7-filtro.getOperacion(): " + filtro.getOperacion());
			consulta.setIdOperacion(filtro.getOperacion());
		}
		if (!(filtro.getNumDoi() == null || filtro.getNumDoi().equals(""))) {
			LOG.info("f8-filtro.getNumDoi(): " + filtro.getNumDoi());
			consulta.setNumDOICliente(filtro.getNumDoi());
		}
		if (!(filtro.getRazSoc() == null || filtro.getRazSoc().equals(""))) {
			LOG.info("f9-filtro.getRazSoc(): " + filtro.getRazSoc());
			consulta.setRazonSocialCliente(filtro.getRazSoc());
		}
		if (!(filtro.getResponsable() == null || filtro.getResponsable().equals(""))) {
			LOG.info("f10-filtro.getResponsable(): " + filtro.getResponsable());
			consulta.setCodUsuarioActual(filtro.getResponsable());
		}
		if (!(filtro.getOficinaExp() == null || filtro.getOficinaExp().equals("0"))) {
			LOG.info("f11-filtro.getOficinaExp(): " + filtro.getOficinaExp());
			consulta.setCodOficina(filtro.getOficinaExp());
		}
		if (!(filtro.getTerritorioExp() == null || filtro.getTerritorioExp().equals("0"))) {
			LOG.info("f12-filtro.getTerritorioExp(): " + filtro.getTerritorioExp());
			consulta.setIdTerritorio(filtro.getTerritorioExp());
		}
		if (!(filtro.getAbogadoEstudio() == null|| filtro.getAbogadoEstudio().equals("0") || filtro.getAbogadoEstudio().equals(""))) {
			LOG.info("f13-filtro.getAbogadoEstudio(): " + filtro.getAbogadoEstudio());
			consulta.setCodUsuarioAbogado(filtro.getAbogadoEstudio());
		}
		if (!(filtro.getGestor() == null || filtro.getGestor().equals(""))) {
			LOG.info("f14-filtro.getGestor(): " + filtro.getGestor());
			consulta.setCodUsuarioResponsable(filtro.getGestor());
		}
		if (!(filtro.getEstudio() == null || filtro.getEstudio().equals("0") || filtro.getEstudio().equals(""))) {
			LOG.info("f15-filtro.getEstudio(): " + filtro.getEstudio());
			consulta.setCodEstudioAbogado(filtro.getEstudio());
		}
		// falta
		/*
		 * ANS
		 */

		if (!(filtro.getStrFechaAsignacion().getLimInf() == null || filtro
				.getStrFechaAsignacion().getLimInf().equals(""))) {
			LOG.info("f16: "
					+ filtro.getStrFechaAsignacion().getLimInf());
			consulta.setFechaAsignacionInf(filtro.getStrFechaAsignacion()
					.getLimInf());
		}
		if (!(filtro.getStrFechaAsignacion().getLimSup() == null || filtro
				.getStrFechaAsignacion().getLimSup().equals(""))) {
			LOG.info("f17: "
					+ filtro.getStrFechaAsignacion().getLimSup());
			consulta.setFechaAsignacionSup(filtro.getStrFechaAsignacion()
					.getLimSup());
		}

		if (!(filtro.getStrFechaInicio().getLimInf() == null || filtro
				.getStrFechaInicio().getLimInf().equals(""))) {
			LOG.info("f18: " + filtro.getStrFechaInicio().getLimInf());
			consulta.setFechaInicioInf(filtro.getStrFechaInicio().getLimInf());
		}
		if (!(filtro.getStrFechaInicio().getLimSup() == null || filtro
				.getStrFechaInicio().getLimSup().equals(""))) {
			LOG.info("f19: " + filtro.getStrFechaInicio().getLimSup());
			consulta.setFechaInicioSup(filtro.getStrFechaInicio().getLimSup());
		}		
		if (!(filtro.getOficinaTarea() == null || filtro.getOficinaTarea()
				.equals("0"))) {
			LOG.info("f20: " + filtro.getOficinaTarea());
			consulta.setCodOficinaTarea(filtro.getOficinaTarea());
		}
		if (!(filtro.getTerritorioTarea() == null || filtro
				.getTerritorioTarea().equals("0"))) {
			LOG.info("f21: " + filtro.getTerritorioTarea());
			consulta.setIdTerritorioTarea(filtro.getTerritorioTarea());
		}
		
		return consulta;
	}

	public void dblTareaSeleccionada() {
		LOG.info("dblTareaSeleccionada (ActionEvent ae)");

		FacesContext context = FacesContext.getCurrentInstance();
		Map requestMap = context.getExternalContext().getRequestParameterMap();
		String idExpTar = (String) requestMap.get("idExpTar");
		String codResponsable = (String) requestMap.get("codResponsable");			

		LOG.info("***********idExpTar*********"+idExpTar);
		LOG.info("***********codResponsable*********"+codResponsable);
		TareaBandejaVO tareaBandejaVO=null;;
		ExpedienteCC expedienteCC=null;
		
		try{
			//TareaBandejaVO tareaBandejaVO = buscarExpedienteCC(idExpTar);
			//ExpedienteCC expedienteCC = tareaBandejaVO.getExpedienteCC();
			tareaBandejaVO = buscarExpedienteCC(idExpTar);
			LOG.info ("tareaBandejaVO: "+tareaBandejaVO);
			expedienteCC = tareaBandejaVO.getExpedienteCC();
			
			LOG.info ("getCodCentral: "+tareaBandejaVO.getCodCentral());
			LOG.info ("getCodEstadoExp: "+tareaBandejaVO.getCodEstadoExp());
			LOG.info ("getCodEstadoTarea: "+tareaBandejaVO.getCodEstadoTarea());
			LOG.info ("getCodTarea: "+tareaBandejaVO.getCodTarea());
			LOG.info ("getExpediente: "+tareaBandejaVO.getExpediente());
			
		}catch(Exception e){
			LOG.error("**********cacth dblTareaSeleccionada()************", e);
			return;
		}
		
		String tkiid = null;
		RemoteUtils util = new RemoteUtils();
		
		if (expedienteCC != null) {
			tkiid = expedienteCC.getTaskID();
			expedienteCC = util.obtenerDetalleExpediente(expedienteCC);
		}
	    
		LOG.info("***********tkiid*********"+tkiid);
		
		Empleado empleado = (Empleado) Util.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		String codUsuario = empleado.getCodigo();
		int idTarea = -1;
		if (expedienteCC != null) {
			idTarea = Integer.parseInt(expedienteCC.getDatosFlujoCtaCte().getIdTarea());
		} else {
			idTarea = Integer.parseInt(tareaBandejaVO.getCodTarea());
		}
		
		LOG.info("Perfil "+empleado.getPerfil().getCodigo());
		LOG.info("CodUsuarioActual1 : "+ (expedienteCC==null ? "expedienteCC es nulo" : expedienteCC.getCodUsuarioActual()));
		LOG.info("codUsuario : "+codUsuario);
		LOG.info("idTarea : "+idTarea);
		LOG.info("tkiid : "+tkiid);
		
		Empleado empleadoHistorial = null; // si se reasigna debe guardarse en el Historial el Empleado anterior
		
		if ((idTarea == ConstantesBusiness.ID_TAREA_SUBSANAR_DOCUMENTO
				|| idTarea == ConstantesBusiness.ID_TAREA_SUBSANAR_FIRMA
				|| idTarea == ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE
				|| idTarea == ConstantesBusiness.ID_TAREA_GESTIONAR_COBRO_COMISION)
			&& empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR) 
			&& expedienteCC != null
			&& !expedienteCC.getCodUsuarioActual().equals(codUsuario)
			&& empleado.getOficina().getId().toString().equals(expedienteCC.getCodOficina())){

			LOG.info("***********REASIGNAR EXPEDIENTE********");
			LOG.info("***********EmpleadoDAO().findByCodigo(codUsuario)********");
			Empleado de = empleadoDAO.findByCodigo(expedienteCC.getCodUsuarioActual());
			Empleado a = empleadoDAO.findByCodigo(codUsuario);
			
			util.transferirTarea(expedienteCC.getCodUsuarioActual(),codUsuario,tkiid);
			
			expedienteCC.setCodUsuarioActual(codUsuario);
			expedienteCC.setNomUsuarioActual(a.getNombres().trim()+" "+a.getApepat().trim()+" "+a.getApemat().trim());
			expedienteCC.getOperacionesCtaCte().setIdEmpleadoActual(a.getId());
			
			util.enviarExpediente(tkiid, expedienteCC);
			
			// se actualiza la carga de trabajo al nuevo usuario
			Integer idExpediente = Integer.parseInt(tareaBandejaVO.getExpediente());
			ExpedienteTareaProceso expTarProc = expTareaProcesoDAO.findExpedienteTareaProceso(idExpediente, de.getId(), idTarea);
			if (expTarProc != null) {
				expTarProc.setIdEmpleado(a.getId());
				expTareaProcesoDAO.update(expTarProc);
			} else {
				LOG.warn("No existe carga de trabajo: (idExpediente={}, idEmpleado={}, idTarea={})", new Object[]{idExpediente, de.getId(), idTarea});
			}
		}	
		
		LOG.info("tareaBandejaVO.getOperacion(): " + tareaBandejaVO.getOperacion());
		LOG.info("tareaBandejaVO.getCodResponsable(): " + tareaBandejaVO.getCodResponsable());
		if (idTarea == ConstantesBusiness.ID_TAREA_PRE_REGISTRO  &&
				empleado.getPerfil().getCodigo().equals(ConstantesBusiness.CODIGO_PERFIL_GESTOR) &&
				tareaBandejaVO.getOperacion().toUpperCase().equals(ConstantesBusiness.DESC_SUBSANACION_BASTANTEO) &&
				!tareaBandejaVO.getCodResponsable().equals(codUsuario)){
			LOG.info("***********REASIGNAR EXPEDIENTE SUBSANACION********");
			Empleado a = empleadoDAO.findByCodigo(codUsuario);
			tareaBandejaVO.setCodResponsable(codUsuario);
			tareaBandejaVO.setResponsable(a.getNombres().trim()+" "+a.getApepat().trim()+" "+a.getApemat().trim());
		}
		

		
		/**/
		LOG.info("***********ExpedienteDAO ********");
		Expediente expediente = expedienteDAO.load(Integer.parseInt(tareaBandejaVO.getExpediente()));
		
		expediente.setNumTerminal(Util.obtenerIp());
		Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);		
		Util.addObjectSession(ConstantesAdmin.RESPONSABLE_SESION, codResponsable);
		
		//ExpedienteCC expedienteCC = tareaBandejaVO.getExpedienteCC();
		if (expedienteCC == null) {
			expedienteCC = new ExpedienteCC();
			expedienteCC.setCodUsuarioActual(tareaBandejaVO.getCodResponsable());
		} else {

			expediente.setFechaEnvio(expedienteCC.getActivado().getTime());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(expediente.getFechaEnvio());
			calendar.add(Calendar.MINUTE, tareaBandejaVO.getVerdeMinutos());
			expediente.setFechaProgramada(calendar.getTime());

			LOG.info("************SEMAFORO****************");
			LOG.info("expedienteCC-->"+ expedienteCC.getCodSemaforo());
			LOG.info("expedienteCC.getIdEstadoExpediente() "+ expedienteCC.getIdEstadoExpediente());
		}

		Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_PROCESO_SESION,expedienteCC);

		if (tareaBandejaVO.getId().endsWith(SUF_PENDIENTES)) {
			LOG.info("********IF-tareaBandejaVO.getId().endsWith(SUF_PENDIENTES)***********"+tareaBandejaVO.getId().endsWith(SUF_PENDIENTES));

			Tarea tarea = tareaDAO.load(idTarea);

			ExpedienteTarea expedienteTarea = expediente.getExpedienteTareas().iterator().next();
			expedienteTarea.setTarea(tarea);
			
			if (empleadoHistorial != null) {
				Util.generarRegistroHistExp(expediente, empleadoHistorial);
			}
			
			String pagina = "../" + tarea.getNombrePagina();
			LOG.info("redirect : {}", pagina);
			LOG.info("********BandejaMB-URL-Pagina***********");
			redirectAction(pagina);
		} else {
			LOG.info("********ELSE-tareaBandejaVO.getId().endsWith(SUF_PENDIENTES)***********"+tareaBandejaVO.getId().endsWith(SUF_PENDIENTES));
			Tarea tarea = tareaDAO.load(idTarea);
			
			ExpedienteTarea expedienteTarea = expediente.getExpedienteTareas().iterator().next();
			expedienteTarea.setTarea(tarea);
			LOG.info("********tareaBandejaVO.getCodEstadoTarea()***********"+tareaBandejaVO.getCodEstadoTarea());
			String pagina = "../" + tarea.getNombrePagina();
			LOG.info("redirect : {}", pagina);
			LOG.info("********BandejaMB-URL-Pagina***********");
			redirectAction(pagina, new String[] {ConstantesAdmin.PARAMETRO_TIPO_PRE_REGISTRO,expediente.getOperacion().getCodigoOperacion() }, new String[] {ConstantesAdmin.PARAMETRO_CODIGO_ESTADO_TAREA,tareaBandejaVO.getCodEstadoTarea() });
		}
	}

	private TareaBandejaVO buscarExpedienteCC(String idExpTar) {
		LOG.info("buscarExpedienteCC (String idExpTar)");
		LOG.info("*****listaTareas*****"+listaTareas);
		
		TareaBandejaVO TareaBanVOaux=null;
		try{
			LOG.info("*********TRY-buscarExpedienteCC(String idExpTar)*********");
			for (TareaBandejaVO tareaBandejaVO : listaTareas) {
				LOG.info("tareaBandejaVO.getId() " + tareaBandejaVO.getId());
				if (tareaBandejaVO.getId().equals(idExpTar)) {
					TareaBanVOaux= tareaBandejaVO;
					break;
				}
			}
			return TareaBanVOaux;
		}catch(Exception ex){
			LOG.error("*********CATCH-buscarExpedienteCC(String idExpTar)*********", ex);
			return TareaBanVOaux;
			//throw new Exception();
		}
		/*
		for (TareaBandejaVO tareaBandejaVO : listaTareas) {
			LOG.info("tareaBandejaVO.getId() " + tareaBandejaVO.getId());
			if (tareaBandejaVO.getId().equals(idExpTar)) {
				return tareaBandejaVO;
			}
		}
		return null;
		*/
	}

	public boolean isMostrarSeccionAbogadoEstudio() {
		Empleado empleado = (Empleado) Util
				.getObjectSession(ConstantesAdmin.EMPLEADO_SESION);
		if (empleado.getPerfil().getCodigo().equals(
				ConstantesBusiness.CODIGO_PERFIL_GESTOR)
				|| empleado.getPerfil().getCodigo().equals(
						ConstantesBusiness.CODIGO_PERFIL_MESA_DOCUMENTOS)
				|| empleado.getPerfil().getCodigo().equals(
						ConstantesBusiness.CODIGO_PERFIL_MESA_FIRMAS)
				|| empleado.getPerfil().getCodigo().equals(
						ConstantesBusiness.CODIGO_PERFIL_DIGITADOR)
				|| empleado.getPerfil().getCodigo().equals(
						ConstantesBusiness.CODIGO_PERFIL_GERENTE_OFICINA)
				|| empleado.getPerfil().getCodigo().equals(
						ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_FIRMA)
				|| empleado.getPerfil().getCodigo().equals(
						ConstantesBusiness.CODIGO_PERFIL_SUPERVISOR_DIGITADOR)
				|| empleado.getPerfil().getCodigo().equals(
						ConstantesBusiness.CODIGO_PERFIL_ABOGADO_REVISION)) {
			return false;
		}

		return true;
	}

	public void cambiarOficinasTarea(AjaxBehaviorEvent event) {
		if (filtro.getTerritorioTarea() == null
				|| ConstantesAdmin.CODIGO_NO_SELECCIONADO.equals(filtro
						.getTerritorioTarea())) {
			this.oficinaItemsTarea = Util.listaVacia();
		} else {
			crearListaOficinasTareas(Integer.parseInt(filtro
					.getTerritorioTarea()));
		}
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("cambiarOficinasTarea--> : " + listaTareas.size());
	}

	private void crearListaOficinasTareas(Integer idTerritorio) {
		this.oficinaItemsTarea = Util.crearItems(
				oficinaDAO.findByIdTerritorio(idTerritorio), false, "id",
				"codigo,descripcion", "{0} {1}", true);
	}

	public void cambiarOficinasExp(AjaxBehaviorEvent event) {
		if (filtro.getTerritorioExp() == null
				|| ConstantesAdmin.CODIGO_NO_SELECCIONADO.equals(filtro
						.getTerritorioExp())) {
			this.oficinaItemsExp = Util.listaVacia();
		} else {
			crearListaOficinasExp(Integer.parseInt(filtro.getTerritorioExp()));
		}
	}

	private void crearListaOficinasExp(Integer idTerritorio) {
		this.oficinaItemsExp = Util.crearItems(
				oficinaDAO.findByIdTerritorio(idTerritorio), false, "id",
				"codigo,descripcion", "{0} {1}", true);
	}

	public void cambiarAbogados(AjaxBehaviorEvent event) {
		//+POR SOLICITUD BBVA+System.out..println("cambiarAbogados " + filtro.getEstudio());
		if (filtro.getEstudio() == null
				|| ConstantesAdmin.CODIGO_NO_SELECCIONADO.equals(filtro.getEstudio())) {
			this.abogadoItems = Util.listaVacia();
		} else {
			crearListaAbogados(Integer.parseInt(filtro.getEstudio()));
		}
	}

	private void crearListaAbogados(Integer idEstudioAbog) {
		//+POR SOLICITUD BBVA+System.out..println("empleadoDAO.findByEstudio(idEstudioAbog) "				+ empleadoDAO.findByEstudio(idEstudioAbog).size());
		
		List<Empleado> listaAbo = empleadoDAO.findByEstudio(idEstudioAbog);		
		this.abogadoItems = new ArrayList<SelectItem> ();
		for (Empleado emp : listaAbo) {			
			this.abogadoItems.add(Util.crearItem(emp.getCodigo(), emp.getNombres().trim()+" "+emp.getApepat().trim()+" "+emp.getApemat().trim()));
		}
	}
	
	/*private void crearListaAbogados(Integer idEstudioAbog) {
		EmpleadoDAO empleadoDAO = DAOFactory.getInstance().getEmpleadoDAO();
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("empleadoDAO.findByEstudio(idEstudioAbog) "
				+ empleadoDAO.findByEstudio(idEstudioAbog).size());
		this.abogadoItems = Util.crearItems(empleadoDAO
				.findByEstudio(idEstudioAbog), false, "codigo",
				"nombresCompletos");
	}*/
	
	public void colapsarExpandirMenu(AjaxBehaviorEvent event) {
		this.menuColapsado = !menuColapsado;
	}
	
	/*  mensaje de error    */
	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	
	public List<?> getListaResponsable() {
		return listaResponsable;
	}

	public void setListaResponsable(List<?> listaResponsable) {
		this.listaResponsable = listaResponsable;
	}

	public void ordenarTareas(TareaBandejaVO bandejaVO) {
		ListSorter.ordenar(listaTareas, true, bandejaVO);
	}

	public List<TareaBandejaVO> getListaTareas() {
		return listaTareas;
	}

	public void setListaTareas(List<TareaBandejaVO> listaTareas) {
		this.listaTareas = listaTareas;
	}

	public TareaBandejaVO getFiltro() {
		return filtro;
	}

	public void setFiltro(TareaBandejaVO filtro) {
		this.filtro = filtro;
	}

	public List<TareaBandejaVO> getListaTareasBase() {
		return listaTareasBase;
	}

	public void setListaTareasBase(List<TareaBandejaVO> listaTareasBase) {
		this.listaTareasBase = listaTareasBase;
	}

	public List<String> getListaCriterios() {
		return listaCriterios;
	}

	public void setListaCriterios(List<String> listaCriterios) {
		this.listaCriterios = listaCriterios;
	}

	public List<Operacion> getListaOperacion() {
		return listaOperacion;
	}

	public void setListaOperacion(List<Operacion> listaOperacion) {
		this.listaOperacion = listaOperacion;
	}

	public List<Territorio> getListaTerritorio() {
		return listaTerritorio;
	}

	public void setListaTerritorio(List<Territorio> listaTerritorio) {
		this.listaTerritorio = listaTerritorio;
	}

	public List<Tarea> getListaTarea() {
		return listaTarea;
	}

	public void setListaTarea(List<Tarea> listaTarea) {
		this.listaTarea = listaTarea;
	}

	public List<Oficina> getListaOficina() {
		return listaOficina;
	}

	public void setListaOficina(List<Oficina> listaOficina) {
		this.listaOficina = listaOficina;
	}

	public List<Empleado> getListaAbogado() {
		return listaAbogado;
	}

	public void setListaAbogado(List<Empleado> listaAbogado) {
		this.listaAbogado = listaAbogado;
	}

	public List<EstadoTarea> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<EstadoTarea> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public List<?> getListaANS() {
		return listaANS;
	}

	public Map<String, SortHeader> getMapaHeader() {
		return mapaHeader;
	}

	public void setMapaHeader(Map<String, SortHeader> mapaHeader) {
		this.mapaHeader = mapaHeader;
	}

	public void setListaANS(List<?> listaANS) {
		this.listaANS = listaANS;
	}

	private void putMapa(String nomProperty, String label) {
		mapaHeader.put(nomProperty, new SortHeader(label));
	}

	public void setTareaSeleccionada(TareaBandejaVO tareaSeleccionada) {
		this.tareaSeleccionada = tareaSeleccionada;
	}

	public TareaBandejaVO getTareaSeleccionada() {
		return tareaSeleccionada;
	}

	public void setModelo(TareaBandejaVOModel modelo) {
		this.modelo = modelo;
	}

	public TareaBandejaVOModel getModelo() {
		if (!this.modelo.mapa.isEmpty() && listaTareas != null) {
			crearModelo(listaTareas);
		}
		return modelo;
	}

	public List<SelectItem> getEstadoItems() {
		return Util.crearItems(listaEstado, "id", "descripcion");
	}

	public List<SelectItem> getEstadoExpItems() {
		return Util.crearItems(listaEstadoExp, "id", "descripcion");
	}

	public List<SelectItem> getNomTareaItems() {
		return Util.crearItems(listaTarea, "id", "descripcion");
	}

	public List<SelectItem> getOperacionItems() {
		return Util.crearItems(listaOperacion, "id", "descripcion");
	}

	public List<SelectItem> getTerritorioItems() {
		return Util.crearItems(listaTerritorio, false, "id", "codigo,descripcion", "{0} {1}", true);
	}

	public List<SelectItem> getOficinaItemsTarea() {
		return oficinaItemsTarea; // Util.crearItems(listaOficina, "id",
									// "descripcion");
	}

	public List<SelectItem> getOficinaItemsExp() {
		return oficinaItemsExp; // Util.crearItems(listaOficina, "id",
								// "descripcion");
	}

	public List<SelectItem> getAnsItems() {
		return ansItems;
	}

	public List<SelectItem> getEstudioItems() {
		return Util.crearItems(listaEstudioAbogado, "id", "descripcion");
	}

	public List<SelectItem> getAbogadoItems() {
		return abogadoItems; // Util.crearItems(listaAbogado, "id",
								// "nombresCompletos");
	}

	public List<EstadoExpediente> getListaEstadoExp() {
		return listaEstadoExp;
	}

	public void setListaEstadoExp(List<EstadoExpediente> listaEstadoExp) {
		this.listaEstadoExp = listaEstadoExp;
	}

	public void setOficinaItemsTarea(List<SelectItem> oficinaItemsTarea) {
		this.oficinaItemsTarea = oficinaItemsTarea;
	}

	public void setOficinaItemsExp(List<SelectItem> oficinaItemsExp) {
		this.oficinaItemsExp = oficinaItemsExp;
	}

	public void setAnsItems(List<SelectItem> ansItems) {
		this.ansItems = ansItems;
	}

	public String getCodSemaforo() {
		return codSemaforo;
	}

	public void setCodSemaforo(String codSemaforo) {
		this.codSemaforo = codSemaforo;
	}
	
	public boolean isMenuColapsado() {
		return menuColapsado;
	}

	public void setMenuColapsado(boolean menuColapsado) {
		this.menuColapsado = menuColapsado;
	}

	public List<Empleado> listaEmpleados(String responsableTarea) {
		responsableTarea = responsableTarea + "%";
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("FiltroParalistaEmpleados :"+responsableTarea);
		List<Empleado> listaEmp = empleadoDAO.getPosiblesNombres(responsableTarea);
		return listaEmp;
	}
	
	public List<TareaBandejaVO> filtroRangoFechas(DateRange rangoFechas ) {
		List<TareaBandejaVO> listaFechas = new ArrayList<TareaBandejaVO>();

		Date fechaInicio = rangoFechas.getLimInf();
		Date fechaFin = rangoFechas.getLimSup();
		DateRange fechaOrigen;
		Date fechaBusq;

		if (fechaInicio!=null && fechaFin!=null /*&& fechaInicio.equals(fechaFin)*/) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(fechaFin);
			cal.add(Calendar.DATE, 1);
			fechaFin = cal.getTime();
		}
		
		for (TareaBandejaVO lista: listaTareas) {
			fechaOrigen = lista.getStrFechaAtencion();
			fechaBusq = fechaOrigen.getTarget();
			
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("fechaInicio > fechaTarget > fechaFin: "+fechaInicio+" > "+fechaOrigen+" > "+fechaFin);
	        if (fechaOrigen!=null &&
	        	((fechaInicio!=null && fechaFin!=null && fechaInicio.before(fechaBusq) && fechaFin.after(fechaBusq)) ||
	             (fechaInicio!=null && fechaFin!=null && fechaInicio.equals(fechaFin) && fechaInicio.equals(fechaBusq)) ||
	             (fechaInicio!=null && fechaFin==null && fechaInicio.before(fechaBusq)) ||
	             (fechaInicio==null && fechaFin!=null && fechaFin.after(fechaBusq)))	              
	           ) {
	        	//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Entroxxxxx : "+fechaBusq);	        	
			    listaFechas.add(lista);
	        }
		}
		return listaFechas;
	}

	@Override
	public int getRowCount() {
		if (modelo != null) {
			return modelo.size();
		} else {
			return 0;
		}
	}
	
	public void pinwWEBSEAL (ActionEvent event) { 
		LOG.info("En el metodo pinWebSeal de bandeja de trabajo - BASTANTEO");
	}
	
}
