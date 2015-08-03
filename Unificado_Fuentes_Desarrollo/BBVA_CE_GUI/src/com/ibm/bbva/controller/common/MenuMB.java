package com.ibm.bbva.controller.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.com.grupobbva.sce.tc84.CtBodyRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRq;
import pe.com.grupobbva.sce.tc84.CtExtraerTipoCambioRs;
import pe.com.grupobbva.sce.tc84.CtHeader;
import pe.com.grupobbva.sce.tc84.CtTipoCambio;
import pe.com.grupobbva.sce.tc84.CtTipos;
import pe.ibm.bean.Cliente;
import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bean.ExpedienteTCWPSWeb;
import pe.ibm.bean.Producto;
import bbva.ws.api.view.BBVAFacadeLocal;

import com.ibm.bbva.controller.AbstractLinksMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.ConstantesAdmin;
import com.ibm.bbva.controller.mantenimiento.DescargaLDAP_UI;
import com.ibm.bbva.controller.mantenimiento.OficinaTemporal_UI;
import com.ibm.bbva.entities.CartEmpleadoCE;
import com.ibm.bbva.entities.CartTerritorioCE;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.TipoCambioCE;
import com.ibm.bbva.entities.VistaBandjCartProd;
import com.ibm.bbva.session.CartEmpleadoCEBeanLocal;
import com.ibm.bbva.session.CartTerritorioCEBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.ParametrosConfBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.TipoCambioCEBeanLocal;
import com.ibm.bbva.session.VistaBandjCartProdBeanLocal;
import com.ibm.bbva.tabla.ejb.impl.TablaFacadeBean;
import com.ibm.bbva.tabla.util.vo.HistorialDetalle;
import com.ibm.bbva.util.AyudaExpedienteTC;
import com.ibm.bbva.util.AyudaExpedienteVO;
import com.ibm.bbva.util.Util;


@SuppressWarnings("serial")
@ManagedBean(name = "menu")
@RequestScoped
public class MenuMB extends AbstractLinksMBean {

	@EJB
	private PerfilBeanLocal perfilBean;
	@EJB
	private CartTerritorioCEBeanLocal cartTerritorioCEBeanLocalBean;
	
	@EJB
	private CartEmpleadoCEBeanLocal cartEmpleadoCEBeanLocalBean;
	
	@EJB
	ExpedienteBeanLocal expedienteBean;
	@EJB
	private EmpleadoBeanLocal empleadoBean;
	@EJB
	private BBVAFacadeLocal bbvaFacade;
	
	@EJB
	private TipoCambioCEBeanLocal tipoCambioCEBeanLocal;
	
	@EJB
	private VistaBandjCartProdBeanLocal vistaBandjCartProdBeanLocal;
	
	private boolean renderedRegExp;
	private boolean renderedReasigTareas;
	private boolean renderedBanMant = false;
	private boolean renderedPendientes;
	private HtmlCommandButton btnCerrarSesion;
	
	
	private boolean habMenuRegistrarExpediente;
	private boolean habMenuBandejaPendientes;
	private boolean habMenuBusqueda;
	private boolean habMenuBandejaHistorica;
	private boolean habMenuBandejaAsignacion;
	private boolean habMenuBandejaMantenimiento;
	private boolean habMenuReportes;
	private boolean habMenuReporteHistorial;
	private boolean habMenuReporteConsolidado;
	private boolean habMenuReporteTOE;
	private boolean habMenuHorario;
	private boolean habMenuBandejaMonitoreo;	
	private boolean habMenuDescargaLDAP;
	private boolean habMenuOficinaTemporal;
	
	private static final Logger LOG = LoggerFactory.getLogger(MenuMB.class);
	
	public MenuMB() {
		restringuirAcceso();
	}
	
	@PostConstruct
	public void init() {
	}
	
	public void inicializarOpcionesMenu(){
		this.habMenuRegistrarExpediente = false;
		this.habMenuBandejaPendientes = false;
		this.habMenuBusqueda = false;
		this.habMenuBandejaHistorica = false;
		this.habMenuBandejaAsignacion = false;
		this.habMenuBandejaMantenimiento = false;
		this.habMenuReportes = false;
		this.habMenuReporteHistorial = false;
		this.habMenuReporteConsolidado = false;
		this.habMenuReporteTOE = false;
		this.habMenuHorario = false;
		this.habMenuBandejaMonitoreo = false;
		this.habMenuDescargaLDAP = false;
		this.habMenuOficinaTemporal = false;
	}
	
	public String nuevoExpediente () {
		
		//verificarTipoCambio();
		// Inicio agregó Emitson
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarClienteMB buscarCliente = (BuscarClienteMB)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarCliente");
		buscarCliente.setNumeroDOI(null);
		// Fin agregó Emitson
		
		eliminarObjetosSession();
		
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		
		Expediente expediente = AyudaExpedienteVO.instanciar();	
		
		addObjectSession(Constantes.EXPEDIENTE_SESION, expediente);
		addObjectSession(Constantes.CONSIDERAR_TAREA_1, "1");
		addObjectSession(Constantes.CODIGO_ACCION_DEVOLVER_ELEGIDO, 0);
		removeObjectSession(Constantes.LISTA_HORARIO_OFICINA);
		
		ExpedienteTCWPSWeb expedienteTC = new ExpedienteTCWPSWeb();
		expedienteTC.setCliente(new Cliente());
		expedienteTC.setProducto(new Producto());		
		addObjectSession(Constantes.EXPEDIENTE_PROCESO_SESION, expedienteTC);

		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		
		String carterizacion = verificarExisteCarterizacion(empleado.getOficina().getTerritorio().getId(), empleado.getId());
		String mensajeNoCarterizado="";		
		if (carterizacion.equals(Constantes.NO_EXISTE_CARTERIZACION) ) {			 
			mensajeNoCarterizado = "Usuario no Carterizado, usted no tiene acceso a esta opción, comuníquese con el Administrador";	
			addObjectSession(Constantes.TIPO_MENSAJE, Constantes.TIPO_MENSAJE_NO_EXISTE_CARTEIZACION);
 			addObjectSession(Constantes.DESCRIPCION_MENSAJE, mensajeNoCarterizado);			
			return "/mensajes/formMensajes?faces-redirect=true";
		}
		if (carterizacion.equals(Constantes.EXISTE_CARTERIZACION) ) {			 
		 
				String territorio = empleado.getOficina().getTerritorio().getDescripcion();
				String roles = validaExisteCarterizacion(empleado.getOficina().getTerritorio().getId());
				LOG.info("roles = "+roles)	;
				String mensaje="";
				
				if (!roles.equals("") && !roles.equals("ERROR")) {
					/*
					String mensaje = "VERIFICAR CONFIGURACIÓN : "+ 
						             "No se pueden registrar expedientes, "+
					                 "el territorio "+ territorio +" al que pertenece no tiene Carterizacion configurada "+
					                 "para el (los) siguente(s) rol(es) : "+ roles.substring(0, roles.length() - 1) +			                 
					                 ", comuniquese con el administrador de tarjetas de credito para que realice "+
					                 "la configuracion de la Carterizacion";
					*/
					LOG.info("LOG 1")	;
					mensaje = "VERIFICAR CONFIGURACIÓN : "+ 
						             "No se pueden registrar expedientes, "+
					                 "el territorio "+ territorio +" al que pertenece no tiene Carterización configurada "+
					                 "para el (los) siguente(s) rol(es) : "+ roles.substring(0, roles.length() - 1) +			                 
					                 ", comuníquese con el administrador para que realice "+
					                 "la configuración de la Carterización";
					
					addObjectSession(Constantes.TIPO_MENSAJE, Constantes.TIPO_MENSAJE_NO_EXISTE_ROLES);
					addObjectSession(Constantes.DESCRIPCION_MENSAJE, mensaje);			
					return "/mensajes/formMensajes?faces-redirect=true";
		
				}else{
					if(roles.equals("ERROR")){
						LOG.info("LOG 2")	;
						mensaje = "VERIFICAR CONFIGURACIÓN : "+ 
					             "No se pueden registrar expedientes, "+
				                 "el territorio "+ territorio +" al que pertenece no tiene Carterización configurada"+		                 
				                 ", comuníquese con el administrador para que realice "+
				                 "la configuración de la Carterización";
						
						addObjectSession(Constantes.TIPO_MENSAJE, Constantes.TIPO_MENSAJE_NO_EXISTE_ROLES);
						addObjectSession(Constantes.DESCRIPCION_MENSAJE, mensaje);			
						return "/mensajes/formMensajes?faces-redirect=true";
						
					}
					
				}
		}
		LOG.info("Eliminando session ");
		removeObjectSession(Constantes.LISTA_DOC_EXP_ADJ);
		removeObjectSession(Constantes.LISTA_AYUDA_PANEL_DOCUMENTOS);
		removeObjectSession(Constantes.CLEANTRANSFERDIR);
		removeObjectSession(Constantes.LIST_DOC_TRANSF);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
		removeObjectSession("strListaDocsTransferencias");
		removeObjectSession("reutilizablesSeleccionados");
		removeObjectSession("listaDocReuSession");
		removeObjectSession("tamListaGuiaDoc");
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
		return "/registrarExpediente/formRegistrarExpediente?faces-redirect=true";
	}

	public String bandejaPendientes () {
		//verificarTipoCambio();
		LOG.info("bandejaPendientes INICIO");
		eliminarObjetosSession();
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
		removeObjectSession(Constantes.LIST_DOC_TRANSF);
		removeObjectSession(Constantes.PAGINA_SESSION);
		removeObjectSession(Constantes.LISTA_HORARIO_OFICINA);
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC();
		Consulta objConsulta=new Consulta();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		/*List<String> listUsuarios=new ArrayList<String>();
		
		listUsuarios.add(empleado.getCodigo()); 
		objConsulta.setUsuarios(listUsuarios);	*/
		ayudaExpedienteTC.actualizarListaExpedienteTC(objConsulta);
		
		addObjectSession(Constantes.INICIO_BAND_PEND, "1");
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		
		String carterizacion = verificarExisteCarterizacion(empleado.getOficina().getTerritorio().getId(), empleado.getId());
		String mensaje="";		
		if (carterizacion.equals(Constantes.NO_EXISTE_CARTERIZACION) ) {			 
			mensaje = "Usuario no Carterizado, usted no tiene acceso a esta opción, comuníquese con el Administrador";	
			addObjectSession(Constantes.TIPO_MENSAJE, Constantes.TIPO_MENSAJE_NO_EXISTE_CARTEIZACION);
 			addObjectSession(Constantes.DESCRIPCION_MENSAJE, mensaje);			
			return "/mensajes/formMensajes?faces-redirect=true";
		}
		

		
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}
	
	public String bandejaBusqueda () {
		eliminarObjetosSession();
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
		removeObjectSession(Constantes.LIST_DOC_TRANSF);
		removeObjectSession(Constantes.PAGINA_SESSION);
		removeObjectSession(Constantes.LISTA_HORARIO_OFICINA);
		/*Consulta consulta = new Consulta ();
		consulta.setConsiderarUsuarios(false);
		AyudaExpedienteTC ayudaExpedienteTC = new AyudaExpedienteTC ();
		ayudaExpedienteTC.actualizarListaExpedienteTC(consulta);*/
		
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();		
		
		List<ExpedienteTCWPS> lista = new ArrayList<ExpedienteTCWPS>();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
				Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
		
		return "/bandejaBusqueda/formBandejaBusqueda?faces-redirect=true";
		//return "/reporteTOE/formReporteTOE?faces-redirect=true";
	}	
	
	public String bandejaHistorica () {
		eliminarObjetosSession();
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
		removeObjectSession(Constantes.LIST_DOC_TRANSF);
		removeObjectSession(Constantes.PAGINA_SESSION);
		removeObjectSession(Constantes.LISTA_HORARIO_OFICINA);
		
		FacesContext ctx = FacesContext.getCurrentInstance();  

		BuscarBandejaHistMB buscarBandejaHist = (BuscarBandejaHistMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaHist");
		
		addObjectRequest("buscarBandejaHist", buscarBandejaHist);
		//List<HistorialDetalle> lista = buscarBandejaHist.buscar();
		
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		
		List<HistorialDetalle> lista = new ArrayList<HistorialDetalle> ();
		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
		return "/bandejaHistorica/formBandejaHistorica?faces-redirect=true";
	}	
	
	public String bandejaReasigTareas () {
		LOG.info("bandejaReasigTareas.Inicio")	;
		eliminarObjetosSession();
		addObjectSession(Constantes.CONSULTA_BANDASIGN_EXP, false);
		addObjectSession(Constantes.BOTON_BANDASIGN_ASIGNA, false);
		addObjectSession(Constantes.BOTON_BANDASIGN_CARTPROD, true);
		addObjectSession(Constantes.BOTON_BANDASIGN_REFRESH, true);
		addObjectSession(Constantes.CONSULTA_BANDASIGN_ASIGNA, true);
		removeObjectSession(Constantes.LISTA_HORARIO_OFICINA);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
		removeObjectSession(Constantes.LIST_DOC_TRANSF);
		removeObjectSession(Constantes.CONSULTA_BANDASIGN);
		removeObjectSession(Constantes.PAGINA_SESSION);
		removeObjectSession(Constantes.LISTA_USUARIOS_ASIG);
		removeObjectSession(Constantes.PROV_BAND_ASIG);
		removeObjectSession(Constantes.CODIGO_TAREA_ASIGNADO);
		removeObjectSession(Constantes.CODIGO_USU_ASIGNADO_EXP);
		removeObjectSession(Constantes.CODIGO_ESTADO_ASIGNADO);
		
		//Nuevos
		removeObjectSession(Constantes.LISTA_PEND);
		removeObjectSession(Constantes.LISTA_USUARIOS_TAREA_ASIG);
		removeObjectSession(Constantes.PROV_BAND_ASIG);
		removeObjectSession(Constantes.ROL_SELECCIONADO);
		removeObjectSession(Constantes.LISTA_OFICINAS_CARGADAS);
		removeObjectSession(Constantes.LISTA_USUARIOS_ROL_ASIG);
		removeObjectSession(Constantes.LISTA_CART_X_PRODUCTO_USUARIO_SESION);
		
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
	
	//	ArrayList<String> usuarios = new ArrayList<String> ();
	//	for (Empleado empl : empleadoBean.buscarPorIdPerfil(empleado.getPerfil().getId())) {
	//		usuarios.add(empl.getCodigo());
	//	}
		
	//	RemoteUtils tareasBDelegate = new RemoteUtils();
	//	Consulta consulta = new Consulta ();
	//	consulta.setUsuarios(usuarios);
		
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		
		List<ExpedienteTCWPS> lista = null;//tareasBDelegate.obtenerInstanciasTareasPorUsuario(consulta);
		
		String carterizacion = verificarExisteCarterizacion(empleado.getOficina().getTerritorio().getId(), empleado.getId());

		String mensaje="";		
		if (carterizacion.equals(Constantes.NO_EXISTE_CARTERIZACION) ) {			 
			mensaje = "Usuario no Carterizado, usted no tiene acceso a esta opción, comuníquese con el Administrador";	
			addObjectSession(Constantes.TIPO_MENSAJE, Constantes.TIPO_MENSAJE_NO_EXISTE_CARTEIZACION);
 			addObjectSession(Constantes.DESCRIPCION_MENSAJE, mensaje);			
			return "/mensajes/formMensajes?faces-redirect=true";
		}else{
			
			/**
			 * Cambio 07 Mayo 2015 
			 * Carterización por Producto
			 * */
			LOG.info("OBTENIEDO PRODUCTOS DE CARTERIZACION DE USUARIO");
			LOG.info("Perfil:::"+empleado.getPerfil().getId());
			LOG.info("Territorio:::"+empleado.getOficina().getTerritorio().getId());
			LOG.info("Empleado:::"+empleado.getId());
			
			List<VistaBandjCartProd> listCartProducto = vistaBandjCartProdBeanLocal.verificarCartXProducto(empleado.getPerfil().getId(), 
					empleado.getOficina().getTerritorio().getId(), empleado.getId());
			
			List<Long> listIdsProd=new ArrayList<Long>();
			
			if(listCartProducto!=null && listCartProducto.size()>0){
				LOG.info("Tamaño de Lista listCartProducto="+listCartProducto.size());
				for(VistaBandjCartProd obj : listCartProducto){
					if(obj!=null && obj.getIdProduto()>0){
						LOG.info("Producto:::"+obj.getIdProduto());
						listIdsProd.add(new Long(obj.getIdProduto()));
					}else{
						LOG.info("Objeto VistaBandjCartProd ó Producto de Objeto es nulo");
					}
				}
			}else
				LOG.info("Lista listCartProducto es nula o vacía");
		
			addObjectSession(Constantes.LISTA_CART_X_PRODUCTO_USUARIO_SESION, listIdsProd);
			
			/**
			 * */
		}
		
		addObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION, lista);
		return "/bandejaReasigTareas/formBandejaReasigTareas?faces-redirect=true";
	}	
	
	public String reporteToeExcel () {
		eliminarObjetosSession();
		removeObjectSession(Constantes.LISTA_HORARIO_OFICINA);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
		
		List<String> listUsuarios=new ArrayList<String>();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		listUsuarios.add(empleado.getCodigo()); 
		
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		
		return "/reporteTOE/formGeneraReporteTOE?faces-redirect=true";
	}	

	public String reporteHistoricoTc() {
		eliminarObjetosSession();
		removeObjectSession(Constantes.LISTA_HORARIO_OFICINA);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
		
		List<String> listUsuarios=new ArrayList<String>();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		listUsuarios.add(empleado.getCodigo());
		addObjectSession(Constantes.TIPO_REPORTE_TC, Constantes.ID_TC_HISTORICO);
		
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		
		//addObjectSession(Constantes.TITULO_REPORTE_TC, Constantes.TITULO_REPORTE_TC_HISTORICO);
		return "/reporteTC/formGeneraReporteTC?faces-redirect=true";
	}
	
	public String reporteConsolidadoTc() {
		eliminarObjetosSession();
		removeObjectSession(Constantes.LISTA_HORARIO_OFICINA);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION);
		removeObjectSession(Constantes.LISTA_EXPEDIENTE_PROCESO_SESION_NUEVO);
	
		List<String> listUsuarios=new ArrayList<String>();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		listUsuarios.add(empleado.getCodigo());
		addObjectSession(Constantes.TIPO_REPORTE_TC, Constantes.ID_TC_CONSOLIDADO);
		
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		
		//addObjectSession(Constantes.TITULO_REPORTE_TC, Constantes.TITULO_REPORTE_TC_CONSOLIDADO);
		return "/reporteTC/formGeneraReporteTC?faces-redirect=true";
	}	
	
	public void restringuirAcceso() {
		
		inicializarOpcionesMenu();
		Empleado empleado = (Empleado) getObjectSession(Constantes.USUARIO_SESION);
		if(empleado!=null && empleado.getPerfil()!=null){
			/*
			String flagRegistraExp = empleado.getPerfil().getFlagRegistraExp();
			String flagReasigTareas = empleado.getPerfil().getFlagAsignacion();
			String flagBanMant = empleado.getPerfil().getFlagAdministracion();
			String flagBanPend = empleado.getPerfil().getFlagPendientes();
			
			if (Constantes.FLAG_REGISTRA_EXPEDIENTE.equals(flagRegistraExp)) {
				renderedRegExp = true;		
			}
			if (Constantes.FLAG_BANDEJA_PENDIENTES.equals(flagBanPend)) {
				renderedPendientes = true;		
			}
			if (Constantes.FLAG_REASIGNA_TAREA.equals(flagReasigTareas)) {
				renderedReasigTareas = true;
			}
			if (Constantes.FLAG_BANDEJA_MANTENIMIENTO.equals(flagBanMant)) {
				renderedBanMant = true;		
			}
			*/
							
			String flagMenuRegistraExpediente = empleado.getPerfil().getFlagMenuRegistraExpediente();
			String flagMenuBandejaPendientes = empleado.getPerfil().getFlagMenuBandejaPendientes();
			String flagMenuBusqueda = empleado.getPerfil().getFlagMenuBusqueda();
			String flagMenuBandejaHistorica = empleado.getPerfil().getFlagMenuBandejaHistorica();
			String flagMenuBandejaAsignacion = empleado.getPerfil().getFlagMenuBandejaAsignacion();
			String flagMenuBandejaMantenimiento = empleado.getPerfil().getFlagMenuBandejaMantenimiento();			
			String flagMenuReporteHistorial = empleado.getPerfil().getFlagMenuReporteHistorial();
			String flagMenuReporteConsolidado = empleado.getPerfil().getFlagMenuReporteConsolidado();
			String flagMenuReporteTOE = empleado.getPerfil().getFlagMenuReporteTOE();
			String flagMenuHorario = empleado.getPerfil().getFlagMenuHorario();
			String flagMenuDescargaLDAP = empleado.getPerfil().getFlagMenuDescargaLDAP();
			String flagMenuOficinaTemporal = empleado.getPerfil().getFlagMenuOficinaTemporal();
			
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuRegistraExpediente)) {
				habMenuRegistrarExpediente = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuBandejaPendientes)) {
				habMenuBandejaPendientes = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuBusqueda)) {
				habMenuBusqueda = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuBandejaHistorica)) {
				habMenuBandejaHistorica = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuBandejaAsignacion)) {
				habMenuBandejaAsignacion = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuBandejaMantenimiento)) {
				habMenuBandejaMantenimiento = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuBandejaMantenimiento)) {
				habMenuBandejaMantenimiento = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuReporteHistorial) || 
					Constantes.OPCION_MENU_VISIBLE.equals(flagMenuReporteConsolidado) || 
					Constantes.OPCION_MENU_VISIBLE.equals(flagMenuReporteTOE)) {
				habMenuReportes = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuReporteHistorial)) {
				habMenuReporteHistorial = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuReporteConsolidado)) {
				habMenuReporteConsolidado = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuReporteTOE)) {
				habMenuReporteTOE = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuHorario)) {
				habMenuHorario = true;
			}	
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuDescargaLDAP)) {
				habMenuDescargaLDAP = true;
			}
			if (Constantes.OPCION_MENU_VISIBLE.equals(flagMenuOficinaTemporal)) {
				habMenuOficinaTemporal = true;
			}
		}
		if(getObjectSession(Constantes.USUARIO_AD_SESION) != null){
			habMenuBandejaMonitoreo = true;
		}

	}

	public boolean isRenderedRegExp() {
		return renderedRegExp;
	}

	public void setRenderedRegExp(boolean renderedRegExp) {
		this.renderedRegExp = renderedRegExp;
	}

	public boolean isRenderedReasigTareas() {
		return renderedReasigTareas;
	}

	public void setRenderedReasigTareas(boolean renderedReasigTareas) {
		this.renderedReasigTareas = renderedReasigTareas;
	}

	public void setRenderedBanMant(boolean renderedBanMant) {
		this.renderedBanMant = renderedBanMant;
	}

	public boolean isRenderedBanMant() {
		return renderedBanMant;
	}
	
	public String bandejaMantenimiento () {
		addObjectSession(Constantes.TIPO_TABLA_SESION, "1");
		actualizarEstadoExpediente();
		eliminarObjetosSession();
		return "/mantenimiento/formManTablasMaestras?faces-redirect=true";
	}
	
	public String bandejaReporte () {
		eliminarObjetosSession();
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();		
		return "/reporte/formReporte?faces-redirect=true";
	}
	
	public String bandejaHorario () {
		eliminarObjetosSession();
		
		FacesContext ctx = FacesContext.getCurrentInstance();  

		HorarioSemanalMB horario = (HorarioSemanalMB)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "horarioSemanal");
		
		if (horario==null) {
			horario = new HorarioSemanalMB ();
			addObjectSession("horarioSemanal", horario);
		}
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		
		return "/horario/formHorario?faces-redirect=true";
	}
	
	public String bandejaMonitoreo () {	
		removeObjectSession(Constantes.LISTA_BANDEJA_MONITOREO);
		removeObjectSession(Constantes.TIPO_BUSQUEDA_BM);
		return "/bandejaMonitoreo/formBandejaMonitoreo?faces-redirect=true";
	}
	
	public String descargaLDAP () 
	{	
		FacesContext ctx = FacesContext.getCurrentInstance();  
		DescargaLDAP_UI DescargaLDAP_MB = (DescargaLDAP_UI)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "descargaLDAP");
		
		DescargaLDAP_MB.cargarCarterizacion();
		DescargaLDAP_MB.cargarTipo();
		DescargaLDAP_MB.cargarEstado();
		DescargaLDAP_MB.cargarPerfil();
		DescargaLDAP_MB.cargarOficina();
		
		return "/descargaLDAP/formManConsultaDescargaLDAP?faces-redirect=true";
				
	}
	
	public String oficinaTemporal () 
	{	
		FacesContext ctx = FacesContext.getCurrentInstance();  
		OficinaTemporal_UI OficinaTemporal_MB = (OficinaTemporal_UI)  
				 ctx.getApplication().getVariableResolver().resolveVariable(ctx, "oficinaTemporal");
		
		OficinaTemporal_MB.cargarOficina();
		OficinaTemporal_MB.cargarEstado();
		
		return "/oficinaTemporal/formManConsultaOficinaTemporal?faces-redirect=true";
				
	}
	
	public String validaExisteCarterizacion(long idTerritorio) {
		String resultado = "";
		LOG.info("-------------- 1.- cartTerritorioCEBeanLocalBean.buscarPorIdProdIdTerr -------------");
		List<CartTerritorioCE> listCartTerritoriooCE=cartTerritorioCEBeanLocalBean.buscarPorIdTerr(idTerritorio);		
		LOG.info("listaPerfil Tamaño= "+listCartTerritoriooCE.size());
		
		
		LOG.info("-------------- 2.- cartEmpleadoCEBeanLocalBean.buscarPorIdCaractIdPerfil -------------");
		List<Perfil> listPerfil = new ArrayList<Perfil>();			
		for(CartTerritorioCE ct : listCartTerritoriooCE){
			LOG.info("c.getCarterizacionCE().getId() " + ct.getCarterizacionCE().getId());
			listPerfil.addAll(perfilBean.buscarPorIdCaract(ct.getCarterizacionCE().getId()));
		}
		LOG.info("listCartEmpleadoCE Tamaño= "+listPerfil.size());
		
		//Uno
		
		if(listPerfil!=null &&  listPerfil.size()>0){
			
			LOG.info("lista.getEmpleado().getPerfil().getId ---1"+listPerfil.get(0).getId());
			if (!validaExistePerfil(listPerfil, Constantes.ID_PERFIL_FORMALIZADOR)) {
			
				resultado = resultado + obtenerPerfil(Constantes.ID_PERFIL_FORMALIZADOR)+",";
			}
			if (!validaExistePerfil(listPerfil, Constantes.ID_PERFIL_ANALISIS_Y_ALTAS)) {
				resultado = resultado + obtenerPerfil(Constantes.ID_PERFIL_ANALISIS_Y_ALTAS)+",";
			}
			if (!validaExistePerfil(listPerfil, Constantes.ID_PERFIL_MESA_DE_CONTROL)) {
				resultado = resultado + obtenerPerfil(Constantes.ID_PERFIL_MESA_DE_CONTROL)+",";
			}
			if (!validaExistePerfil(listPerfil, Constantes.ID_PERFIL_ANALISIS_DE_RIESGOS)) {
				resultado = resultado + obtenerPerfil(Constantes.ID_PERFIL_ANALISIS_DE_RIESGOS)+",";
			}
//			if (!validaExistePerfil(listaPerfil, Constantes.ID_PERFIL_CALIFICACION_Y_CONTROL)) {
//				resultado = resultado + obtenerPerfil(Constantes.ID_PERFIL_CALIFICACION_Y_CONTROL)+",";
//			}
			if (!validaExistePerfil(listPerfil, Constantes.ID_PERFIL_CONTROLLER)) {
				resultado = resultado + obtenerPerfil(Constantes.ID_PERFIL_CONTROLLER)+",";
			}			
		}else{
			LOG.info("No esta configuracion la Carterizacion por idTerritorio "+idTerritorio);	
			resultado="ERROR";
		}
		LOG.info("resultado "+resultado);	
		
		return resultado;
	}
	
	public boolean validaExistePerfil(List<Perfil> listPerfil, Integer idPerfil) {
		LOG.info("lista.getEmpleado().getPerfil().getId"+listPerfil.get(0).getId());
		
		for (Perfil lista : listPerfil) {
			LOG.info("lista.getEmpleado().getPerfil().getId"+lista.getCodigo());
			
			if (lista.getId() == idPerfil.longValue()) {
				return true;
			}
		}

		return false;
	}
	
	public String obtenerPerfil(Integer idPerfil) {
		LOG.info("Metodo obtenerPerfil, idPerfil = "+idPerfil);	
		Perfil perfil = perfilBean.buscarPorId(idPerfil.longValue());
		
		if(perfil!=null)
			return perfil.getDescripcion();
		else
			return null;
	}

	public boolean isRenderedPendientes() {
		return renderedPendientes;
	}

	public void setRenderedPendientes(boolean renderedPendientes) {
		this.renderedPendientes = renderedPendientes;
	}
	
	/**
	 * Metodo para verificar si existe el tipo de cambio del día en la tabla
	 * Si no existe llamar al Servicio de tipo de cambio del banco
	 * Grabar el tipo de cambio del día en la tabla
	 */
	
	private void verificarTipoCambio() {
		
		LOG.info(" Verificando Tipo de Cambio");
		String strFecha = Util.formatDateObject("dd/MM/yyyy", new Date());
		LOG.info("Fecha Actual:" + strFecha);
		Date fecha = Util.parseStringToDate(strFecha, "dd/MM/yyyy");		
		TipoCambioCE objTipoCambioCE = tipoCambioCEBeanLocal.buscarPorFecha(fecha);		
		if (objTipoCambioCE == null) {
			LOG.info("Se intenta obtener el tipo de cambio llamando al servicio");
			objTipoCambioCE = null;
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
				CtTipos tipos = outCtExtraerTipoCambioRs.getData().getTipos();
				List<CtTipoCambio> lstCambios = tipos.getTipoCambio();
				for (int i = 0; i < lstCambios.size() ; i++) {
					CtTipoCambio cambio = lstCambios.get(i);
					if (cambio.getDivisa().trim().equals("USD")) {
						objTipoCambioCE = new TipoCambioCE();
						Double monto = Util.convertStringToDouble(cambio.getVendedor(), ',', ' ');
						LOG.info("Monto Vendedor Tipo Cambio:" + monto);
						objTipoCambioCE.setMonto(BigDecimal.valueOf(monto));
						String strOutFechaTC = outCtExtraerTipoCambioRs.getData().getFechaCambio();
						objTipoCambioCE.setFecha(Util.parseStringToDate(strOutFechaTC.replace('.', '/'), "dd/MM/yyyy"));
						i = lstCambios.size();
					}
					
				}
				LOG.info("TIPO CAMBIO--> Fecha:" + objTipoCambioCE.getFecha() + " Monto:" + objTipoCambioCE.getMonto());
				LOG.info("Insertamos el tipo de cambio extraido del servicio");
				tipoCambioCEBeanLocal.create(objTipoCambioCE);		
				LOG.info("Tipo de cambio para la fecha " + strFecha + " insertado en BD.");
			}
		}else{
			LOG.info("Ya existe tipo de cambio para la fecha actual.");
		}
	}
	
	/**
	 * Metodo que invalida la sesion del was y redirige a la pantalla de sesion expirada
	 * */
	public String irCerrarSesion(){
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
		HttpSession session = request.getSession(false);
		session.invalidate();
		return "/error/errorTimeOut.faces?faces-redirect=true";
	}
	
	/**
	 * Metodo que redirige a una pagina configurable luego de terminarse la sesion
	 * */
	public void timeOutWAS(){
		HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
		HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
		HttpSession session = request.getSession(false);
		session.invalidate();
		try {
			ParametrosConfBeanLocal parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext().lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			String paginaInicioUN = parametrosConfBean.buscarPorVariable(Constantes.ID_APLICATIVO_TC, Constantes.PAGINA_INICIO_UN).getValorVariable();
			LOG.info("paginaInicioUN::::"+paginaInicioUN);
			response.sendRedirect(paginaInicioUN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que termina la sesion de WAS y de WEBSEAL
	 * */
	public void logout(){
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
		HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
		try {
			HttpSession session = request.getSession(false);
			session.invalidate();
			String logoutPage = "/../../../pkmslogout?filename=logout.html";
			response.sendRedirect(response.encodeURL(logoutPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public String verificarExisteCarterizacion(long idTerritorio,long idEmpleado) {
		String resultado = "";
		LOG.info("-------------- 1.- cartTerritorioCEBeanLocalBean.buscarPorIdProdIdTerr :: "+idTerritorio);
		List<CartTerritorioCE> listCartTerritoriooCE=cartTerritorioCEBeanLocalBean.buscarPorIdTerr(idTerritorio);		
		LOG.info("CARTERIZACION X TERRITORIO : "+listCartTerritoriooCE.size());
		
		
		LOG.info("-------------- 2.- cartEmpleadoCEBeanLocalBean.buscarPorIdCaractIdPerfil -------------");
		List<CartEmpleadoCE> listCarterizacion= new ArrayList<CartEmpleadoCE>();			
		for(CartTerritorioCE ct : listCartTerritoriooCE){
			LOG.info("CARTERIZACION X EMPLEADO : getId_CARTERIZACION : " + ct.getCarterizacionCE().getId()+"ID_EMPLeado: "+idEmpleado);
			listCarterizacion.addAll(cartEmpleadoCEBeanLocalBean.buscarPorIdCaractIdEmpleado(ct.getCarterizacionCE().getId(), idEmpleado));
//			listPerfil.addAll(perfilBean.buscarPorIdCaractIdPerfi   ((ct.getCarterizacionCE().getId() )));
		}
		LOG.info("listCartEmpleadoCE Tamaño= "+listCarterizacion.size());
		if(listCarterizacion!=null &&  listCarterizacion.size()>0){ 
			resultado=resultado+Constantes.EXISTE_CARTERIZACION; 
		}else{			
			resultado=resultado+Constantes.NO_EXISTE_CARTERIZACION; 
		}

		LOG.info("resultado "+resultado);	
		
		return resultado;
	}
	
	public void actualizarEstadoExpediente(){
		//Eliminar estado activo del ultimo expediente trabajado en session
		String idExpediente = (String) getObjectSession(Constantes.EXPEDIENTE_ESTADO);
		LOG.info("idExpediente ::: "+idExpediente);
		
		if(idExpediente!=null && !idExpediente.trim().equals("")){
			//Actualiza estado activo de Expediente para el momento de la reasignación			
			expedienteBean.actualizarEstadoExpediente(Constantes.FLAG_ESTADO_INACTIVO_EXPEDIENTE, Long.parseLong(idExpediente));			
		}
		removeObjectSession(Constantes.EXPEDIENTE_ESTADO);		
	}
	
	public HtmlCommandButton getBtnCerrarSesion() {
		return btnCerrarSesion;
	}

	public void setBtnCerrarSesion(HtmlCommandButton btnCerrarSesion) {
		this.btnCerrarSesion = btnCerrarSesion;
	}
	
	/**
	 * Metodo que invalida la sesion del WAS
	 * */
	public void cerrarSesionWAS(AjaxBehaviorEvent event){
		LOG.info("en el metodo cerrarSesionWAS");
		//Eliminar estado activo del ultimo expediente trabajado en session
		actualizarEstadoExpediente();
		
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
	}

	public boolean isHabMenuRegistrarExpediente() {
		return habMenuRegistrarExpediente;
	}

	public void setHabMenuRegistrarExpediente(boolean habMenuRegistrarExpediente) {
		this.habMenuRegistrarExpediente = habMenuRegistrarExpediente;
	}
	
	public boolean isHabMenuBandejaPendientes() {
		return habMenuBandejaPendientes;
	}

	public void setHabMenuBandejaPendientes(boolean habMenuBandejaPendientes) {
		this.habMenuBandejaPendientes = habMenuBandejaPendientes;
	}

	public boolean isHabMenuBusqueda() {
		return habMenuBusqueda;
	}

	public void setHabMenuBusqueda(boolean habMenuBusqueda) {
		this.habMenuBusqueda = habMenuBusqueda;
	}

	public boolean isHabMenuBandejaHistorica() {
		return habMenuBandejaHistorica;
	}

	public void setHabMenuBandejaHistorica(boolean habMenuBandejaHistorica) {
		this.habMenuBandejaHistorica = habMenuBandejaHistorica;
	}

	public boolean isHabMenuBandejaAsignacion() {
		return habMenuBandejaAsignacion;
	}

	public void setHabMenuBandejaAsignacion(boolean habMenuBandejaAsignacion) {
		this.habMenuBandejaAsignacion = habMenuBandejaAsignacion;
	}

	public boolean isHabMenuBandejaMantenimiento() {
		return habMenuBandejaMantenimiento;
	}

	public void setHabMenuBandejaMantenimiento(boolean habMenuBandejaMantenimiento) {
		this.habMenuBandejaMantenimiento = habMenuBandejaMantenimiento;
	}
	
	public boolean isHabMenuReportes() {
		return habMenuReportes;
	}

	public void setHabMenuReportes(boolean habMenuReportes) {
		this.habMenuReportes = habMenuReportes;
	}

	public boolean isHabMenuReporteHistorial() {
		return habMenuReporteHistorial;
	}

	public void setHabMenuReporteHistorial(boolean habMenuReporteHistorial) {
		this.habMenuReporteHistorial = habMenuReporteHistorial;
	}

	public boolean isHabMenuReporteConsolidado() {
		return habMenuReporteConsolidado;
	}

	public void setHabMenuReporteConsolidado(boolean habMenuReporteConsolidado) {
		this.habMenuReporteConsolidado = habMenuReporteConsolidado;
	}

	public boolean isHabMenuReporteTOE() {
		return habMenuReporteTOE;
	}

	public void setHabMenuReporteTOE(boolean habMenuReporteTOE) {
		this.habMenuReporteTOE = habMenuReporteTOE;
	}

	public boolean isHabMenuHorario() {
		return habMenuHorario;
	}

	public void setHabMenuHorario(boolean habMenuHorario) {
		this.habMenuHorario = habMenuHorario;
	}
	
	public boolean isHabMenuBandejaMonitoreo() {
		return habMenuBandejaMonitoreo;
	}

	public void setHabMenuBandejaMonitoreo(boolean habMenuBandejaMonitoreo) {
		this.habMenuBandejaMonitoreo = habMenuBandejaMonitoreo;
	}

	public boolean isHabMenuDescargaLDAP() {
		return habMenuDescargaLDAP;
	}

	public void setHabMenuDescargaLDAP(boolean habMenuDescargaLDAP) {
		this.habMenuDescargaLDAP = habMenuDescargaLDAP;
	}

	public boolean isHabMenuOficinaTemporal() {
		return habMenuOficinaTemporal;
	}

	public void setHabMenuOficinaTemporal(boolean habMenuOficinaTemporal) {
		this.habMenuOficinaTemporal = habMenuOficinaTemporal;
	}
	
}