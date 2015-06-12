package com.ibm.bbva.controller.form;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.controller.common.BuscarBandejaMonitoreoMB;
import com.ibm.bbva.entities.Empleado;
import com.ibm.bbva.entities.EmpleadoAd;
import com.ibm.bbva.entities.Estado;
import com.ibm.bbva.entities.EstadoCE;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.Historial;
import com.ibm.bbva.entities.HistorialLog;
import com.ibm.bbva.entities.LogErrores;
import com.ibm.bbva.entities.Perfil;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.entities.TareaAd;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.ExpedienteBeanLocal;
import com.ibm.bbva.session.HistorialBeanLocal;
import com.ibm.bbva.session.HistorialLogBeanLocal;
import com.ibm.bbva.session.LogErroresBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.TareaAdBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.tabla.util.vo.ConvertExpediente;

@SuppressWarnings("serial")
@ManagedBean(name = "bandejaMonitoreo")
@RequestScoped
public class BandejaMonitoreoMB extends AbstractMBean {
	
	@EJB
	private PerfilBeanLocal perfilBean;
	@EJB
	private EmpleadoBeanLocal empleadoBean;
	@EJB
	private TareaBeanLocal tareaBean;
	@EJB
	private EstadoCEBeanLocal estadoCEBean;
	@EJB
	private ExpedienteBeanLocal expedienteBean;
	@EJB
	private LogErroresBeanLocal logErroresBean;
	@EJB
	private HistorialLogBeanLocal historialLogBean;
	@EJB
	private HistorialBeanLocal historialBean;
	@EJB
	private TareaAdBeanLocal tareaAdBean;
	
	private boolean process;
	private boolean content;
	private String strSeleccionados;
	private HtmlInputText htmlIdSeleccionados;
	
	private static final Logger LOG = LoggerFactory.getLogger(BandejaMonitoreoMB.class);
	
	public BandejaMonitoreoMB() {

	}
	
	@PostConstruct
	public void init() {
		this.process = false;
		this.content = false;
		mostrarBotones();
	}

	public String buscar() {
		LOG.info("en el metodo buscar");
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarBandejaMonitoreoMB buscarBandejaMonitoreoMB = (BuscarBandejaMonitoreoMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaMonitoreo");
		buscarBandejaMonitoreoMB.buscar();
		mostrarBotones();
		return null;
	}
	
	public String buscar(String tipoBusqueda) {
		LOG.info("en el metodo buscar");
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarBandejaMonitoreoMB buscarBandejaMonitoreoMB = (BuscarBandejaMonitoreoMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaMonitoreo");
		buscarBandejaMonitoreoMB.setTipoBusqueda(tipoBusqueda);
		buscarBandejaMonitoreoMB.buscar();
		mostrarBotones();
		return null;
	}
	
	private void mostrarBotones(){
		String tipoBusqueda = getObjectSession(Constantes.TIPO_BUSQUEDA_BM) == null ? null : (String)getObjectSession(Constantes.TIPO_BUSQUEDA_BM);
		if(tipoBusqueda != null){
			if("3".equals(tipoBusqueda)){ // content
				content = true;
				process = false;
			}
			else if("2".equals(tipoBusqueda)){ // process
				content = false;
				process = true;
			}
			else{ // log de errores
				content = false;
				process = false;
			}
		}
	}

	public String limpiar() {
		LOG.info("en el metodo limpiar");
		FacesContext ctx = FacesContext.getCurrentInstance();
		BuscarBandejaMonitoreoMB buscarBandejaMonitoreoMB = (BuscarBandejaMonitoreoMB)  
				ctx.getApplication().getVariableResolver().resolveVariable(ctx, "buscarBandejaMonitoreo");					
		buscarBandejaMonitoreoMB.limpiar();	    
		return null;
	}
	
	public String continuarContent() {
		LOG.info("en el metodo continuar");
		List<ExpedienteTCWPS> listTabla = (List<ExpedienteTCWPS>) getObjectSession(Constantes.LISTA_BANDEJA_MONITOREO);
		List<String> listaSeleccionados = new ArrayList<String>();
		if(strSeleccionados != null && !"".equals(strSeleccionados) && strSeleccionados.length() > 0){
			listaSeleccionados = Arrays.asList(strSeleccionados.split(","));
		}
		for(ExpedienteTCWPS expedienteTCWPS : listTabla){
			for(String codigo : listaSeleccionados){
				if(expedienteTCWPS.getCodigo().equals(codigo)){
					try{
						// se llama al servicio
						String tkiid = expedienteTCWPS.getTaskID();
						expedienteTCWPS.setAccion(Constantes.ACCION_CONTINUAR_MANUAL);
						RemoteUtils objRemoteUtils = new RemoteUtils();
						String resultado = objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
						LOG.info("resultado::::"+resultado);
						if("200".equals(resultado)){
							//LOG DE ERRORES
							LogErrores logErrores = new LogErrores();
							logErrores.setCantidadDocumentos((expedienteTCWPS.getCantDocumentos() != null && !"".equals(expedienteTCWPS.getCantDocumentos())) ? new BigDecimal(expedienteTCWPS.getCantDocumentos()) : null);
							logErrores.setDescripcionError(null);
							Empleado empleado = empleadoBean.buscarPorCodigo(expedienteTCWPS.getCodigoUsuarioAnterior());
							logErrores.setEmpleado(empleado);
							Expediente expediente = expedienteBean.buscarPorId(Long.parseLong(expedienteTCWPS.getCodigo()));
							logErrores.setExpediente(expediente);
							EstadoCE estadoCE = estadoCEBean.buscarPorId(expediente.getEstado().getId());
							logErrores.setEstadoCE(estadoCE);
							logErrores.setFechaCancelacion(null);
							if(expedienteTCWPS.getActivado() != null){
								Calendar fecRegistro = expedienteTCWPS.getActivado();
								Timestamp time = new Timestamp(fecRegistro.getTimeInMillis());
								logErrores.setFechaIncidente(time);
							}
							else{
								logErrores.setFechaIncidente(null);
							}
							Date fechaActual = new Date();
							long fecAct = fechaActual.getTime();
							logErrores.setFechaRestauracion(new Timestamp(fecAct));
							logErrores.setNumeroReintentos(null);
							Perfil perfil = perfilBean.buscarPorId(Long.parseLong(expedienteTCWPS.getIdPerfilUsuarioActual()));
							logErrores.setPerfil(perfil);
							Tarea tarea = tareaBean.buscarPorId(expedienteTCWPS.getIdTareaAnterior());
							logErrores.setTarea(tarea);
							logErrores.setTipoError("CONTENT");
							logErroresBean.create(logErrores);
							
							// HISTORIAL
							HistorialLog historialLog = ConvertExpediente.convertToHistorialLogVO(expediente);
							Empleado emp = (Empleado) getObjectSession(Constantes.USUARIO_AD_SESION);
							historialLog.setEmpleado(emp);
							TareaAd tareaAd = tareaAdBean.buscarPorId(Long.parseLong(Constantes.TAREA_AD_CONTENT));
							historialLog.setTareaAd(tareaAd);
							EstadoCE estadoCEH = estadoCEBean.buscarPorId(Long.parseLong(Constantes.CODIGO_ESTADO_ERROR));
							historialLog.setEstadoCE(estadoCEH);
							if(expedienteTCWPS.getActivado() != null){
								Calendar fecRegistro = expedienteTCWPS.getActivado();
								Timestamp time = new Timestamp(fecRegistro.getTimeInMillis());
								historialLog.setFechaT1(time);
							}
							else{
								historialLog.setFechaT1(null);
							}
							historialLog.setFechaT2(null);
							historialLog.setFechaT3(new Timestamp(fecAct));
							historialLogBean.create(historialLog);
						}
					} catch(Exception e){
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
		buscar();
		return null;
	}
	
	/**
	 * continuarContent por código de expediente
	 * @param codigo
	 * @return
	 */
	public String continuarContent(ExpedienteTCWPS expedienteTCWPS) {
		LOG.info("en el metodo continuar");
		try{
			// se llama al servicio
			String tkiid = expedienteTCWPS.getTaskID();
			expedienteTCWPS.setAccion(Constantes.ACCION_CONTINUAR_MANUAL);
			RemoteUtils objRemoteUtils = new RemoteUtils();
			String resultado = objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
			LOG.info("resultado::::"+resultado);
			if("200".equals(resultado)){
				//LOG DE ERRORES
				LogErrores logErrores = new LogErrores();
				logErrores.setCantidadDocumentos((expedienteTCWPS.getCantDocumentos() != null && !"".equals(expedienteTCWPS.getCantDocumentos())) ? new BigDecimal(expedienteTCWPS.getCantDocumentos()) : null);
				logErrores.setDescripcionError(null);
				Empleado empleado = empleadoBean.buscarPorCodigo(expedienteTCWPS.getCodigoUsuarioAnterior());
				logErrores.setEmpleado(empleado);
				Expediente expediente = expedienteBean.buscarPorId(Long.parseLong(expedienteTCWPS.getCodigo()));
				logErrores.setExpediente(expediente);
				EstadoCE estadoCE = estadoCEBean.buscarPorId(expediente.getEstado().getId());
				logErrores.setEstadoCE(estadoCE);
				logErrores.setFechaCancelacion(null);
				if(expedienteTCWPS.getActivado() != null){
					Calendar fecRegistro = expedienteTCWPS.getActivado();
					Timestamp time = new Timestamp(fecRegistro.getTimeInMillis());
					logErrores.setFechaIncidente(time);
				}
				else{
					logErrores.setFechaIncidente(null);
				}
				Date fechaActual = new Date();
				long fecAct = fechaActual.getTime();
				logErrores.setFechaRestauracion(new Timestamp(fecAct));
				logErrores.setNumeroReintentos(null);
				Perfil perfil = perfilBean.buscarPorId(Long.parseLong(expedienteTCWPS.getIdPerfilUsuarioActual()));
				logErrores.setPerfil(perfil);
				Tarea tarea = tareaBean.buscarPorId(expedienteTCWPS.getIdTareaAnterior());
				logErrores.setTarea(tarea);
				logErrores.setTipoError("CONTENT");
				logErroresBean.create(logErrores);
				
				// HISTORIAL
				HistorialLog historialLog = ConvertExpediente.convertToHistorialLogVO(expediente);
				Empleado emp = (Empleado) getObjectSession(Constantes.USUARIO_AD_SESION);
				historialLog.setEmpleado(emp);
				TareaAd tareaAd = tareaAdBean.buscarPorId(Long.parseLong(Constantes.TAREA_AD_CONTENT));
				historialLog.setTareaAd(tareaAd);
				EstadoCE estadoCEH = estadoCEBean.buscarPorId(Long.parseLong(Constantes.CODIGO_ESTADO_ERROR));
				historialLog.setEstadoCE(estadoCEH);
				if(expedienteTCWPS.getActivado() != null){
					Calendar fecRegistro = expedienteTCWPS.getActivado();
					Timestamp time = new Timestamp(fecRegistro.getTimeInMillis());
					historialLog.setFechaT1(time);
				}
				else{
					historialLog.setFechaT1(null);
				}
				historialLog.setFechaT2(null);
				historialLog.setFechaT3(new Timestamp(fecAct));
				historialLogBean.create(historialLog);
			}
		} catch(Exception e){
			LOG.error(e.getMessage(), e);
		}
		buscar("3");
		return "/bandejaMonitoreo/formBandejaMonitoreo?faces-redirect=true";
	}
	
	public String reintentarProcess() {
		LOG.info("en el metodo reintentar");
		List<ExpedienteTCWPS> listTabla = (List<ExpedienteTCWPS>) getObjectSession(Constantes.LISTA_BANDEJA_MONITOREO);
		List<String> listaSeleccionados = new ArrayList<String>();
		if(strSeleccionados != null && !"".equals(strSeleccionados) && strSeleccionados.length() > 0){
			listaSeleccionados = Arrays.asList(strSeleccionados.split(","));
		}
		for(ExpedienteTCWPS expedienteTCWPS : listTabla){
			for(String codigo : listaSeleccionados){
				if(expedienteTCWPS.getCodigo().equals(codigo)){
					try{
						// se llama al servicio
						String tkiid = expedienteTCWPS.getTaskID();
						expedienteTCWPS.setAccion(Constantes.ACCION_REINTENTAR_MANUAL);
						RemoteUtils objRemoteUtils = new RemoteUtils();
						String resultado = objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
						LOG.info("resultado::::"+resultado);
						if("200".equals(resultado)){
							//LOG DE ERRORES
							LogErrores logErrores = new LogErrores();
							logErrores.setCantidadDocumentos(null);
							logErrores.setDescripcionError(expedienteTCWPS.getDescripcionError());
							Empleado empleado = empleadoBean.buscarPorCodigo(expedienteTCWPS.getCodigoUsuarioAnterior());
							logErrores.setEmpleado(empleado);
							Expediente expediente = expedienteBean.buscarPorId(Long.parseLong(expedienteTCWPS.getCodigo()));
							logErrores.setExpediente(expediente);
							EstadoCE estadoCE = estadoCEBean.buscarPorId(expediente.getEstado().getId());
							logErrores.setEstadoCE(estadoCE);
							logErrores.setFechaCancelacion(null);
							if(expedienteTCWPS.getActivado() != null){
								Calendar fecRegistro = expedienteTCWPS.getActivado();
								Timestamp time = new Timestamp(fecRegistro.getTimeInMillis());
								logErrores.setFechaIncidente(time);
							}
							else{
								logErrores.setFechaIncidente(null);
							}
							Date fechaActual = new Date();
							long fecAct = fechaActual.getTime();
							logErrores.setFechaRestauracion(new Timestamp(fecAct));
							int numRei = 0;
							if(expedienteTCWPS.getNroReintentos() == null || "".equals(expedienteTCWPS.getNroReintentos())){
								numRei = 1;
							}
							else{
								numRei = Integer.parseInt(expedienteTCWPS.getNroReintentos()) + 1;
							}
							logErrores.setNumeroReintentos(new BigDecimal(numRei));
							Perfil perfil = perfilBean.buscarPorId(Long.parseLong(expedienteTCWPS.getIdPerfilUsuarioActual()));
							logErrores.setPerfil(perfil);
							Tarea tarea = tareaBean.buscarPorId(expedienteTCWPS.getIdTareaAnterior());
							logErrores.setTarea(tarea);
							logErrores.setTipoError("PROCESS");
							logErroresBean.create(logErrores);
							
							// HISTORIAL -- no se ha pedido en el documento
						}
					} catch(Exception e){
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
		buscar();
		return null;
	}
	
	/**
	 * reintentarProcess por código de expediente
	 * @param codigo
	 * @return
	 */
	public String reintentarProcess(ExpedienteTCWPS expedienteTCWPS) {
		LOG.info("en el metodo reintentar");
		try{
			// se llama al servicio
			String tkiid = expedienteTCWPS.getTaskID();
			expedienteTCWPS.setAccion(Constantes.ACCION_REINTENTAR_MANUAL);
			RemoteUtils objRemoteUtils = new RemoteUtils();
			String resultado = objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
			LOG.info("resultado::::"+resultado);
			if("200".equals(resultado)){
				//LOG DE ERRORES
				LogErrores logErrores = new LogErrores();
				logErrores.setCantidadDocumentos(null);
				logErrores.setDescripcionError(expedienteTCWPS.getDescripcionError());
				Empleado empleado = empleadoBean.buscarPorCodigo(expedienteTCWPS.getCodigoUsuarioAnterior());
				logErrores.setEmpleado(empleado);
				Expediente expediente = expedienteBean.buscarPorId(Long.parseLong(expedienteTCWPS.getCodigo()));
				logErrores.setExpediente(expediente);
				EstadoCE estadoCE = estadoCEBean.buscarPorId(expediente.getEstado().getId());
				logErrores.setEstadoCE(estadoCE);
				logErrores.setFechaCancelacion(null);
				if(expedienteTCWPS.getActivado() != null){
					Calendar fecRegistro = expedienteTCWPS.getActivado();
					Timestamp time = new Timestamp(fecRegistro.getTimeInMillis());
					logErrores.setFechaIncidente(time);
				}
				else{
					logErrores.setFechaIncidente(null);
				}
				Date fechaActual = new Date();
				long fecAct = fechaActual.getTime();
				logErrores.setFechaRestauracion(new Timestamp(fecAct));
				int numRei = 0;
				if(expedienteTCWPS.getNroReintentos() == null || "".equals(expedienteTCWPS.getNroReintentos())){
					numRei = 1;
				}
				else{
					numRei = Integer.parseInt(expedienteTCWPS.getNroReintentos()) + 1;
				}
				logErrores.setNumeroReintentos(new BigDecimal(numRei));
				Perfil perfil = perfilBean.buscarPorId(Long.parseLong(expedienteTCWPS.getIdPerfilUsuarioActual()));
				logErrores.setPerfil(perfil);
				Tarea tarea = tareaBean.buscarPorId(expedienteTCWPS.getIdTareaAnterior());
				logErrores.setTarea(tarea);
				logErrores.setTipoError("PROCESS");
				logErroresBean.create(logErrores);
				
				// HISTORIAL -- no se ha pedido en el documento
			}
		} catch(Exception e){
			LOG.error(e.getMessage(), e);
		}
		buscar("2");
		return "/bandejaMonitoreo/formBandejaMonitoreo?faces-redirect=true";
	}
	
	public String cancelar() {		
		LOG.info("en el metodo continuar");
		List<ExpedienteTCWPS> listTabla = (List<ExpedienteTCWPS>) getObjectSession(Constantes.LISTA_BANDEJA_MONITOREO);
		List<String> listaSeleccionados = new ArrayList<String>();
		if(strSeleccionados != null && !"".equals(strSeleccionados) && strSeleccionados.length() > 0){
			listaSeleccionados = Arrays.asList(strSeleccionados.split(","));
		}
		for(ExpedienteTCWPS expedienteTCWPS : listTabla){
			for(String codigo : listaSeleccionados){
				if(expedienteTCWPS.getCodigo().equals(codigo)){
					try{
						// se llama al servicio
						String tkiid = expedienteTCWPS.getTaskID();
						expedienteTCWPS.setAccion(Constantes.ACCION_CANCELAR_MANUAL);
						RemoteUtils objRemoteUtils = new RemoteUtils();
						String resultado = objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
						LOG.info("resultado::::"+resultado);
						if("200".equals(resultado)){
							// SE ACTUALIZA EL ESTADO DEL EXPEDIENTE
							Expediente expediente = expedienteBean.buscarPorId(Long.parseLong(expedienteTCWPS.getCodigo()));
							expediente.setEstado(new Estado());
							expediente.getEstado().setId(Constantes.ESTADO_CERRADO_TAREA_27);
							expedienteBean.edit(expediente);
							// HISTORIAL
							Historial historial = ConvertExpediente.convertToHistorialVO(expediente);
							historial.setTiempoObjTC(0);
							historial.setTiempoObjTE(0);
							historial.setTiempoPreTC(0);
							historial.setTiempoPreTE(0);
							historial.setNomColumna(null);
							historial.setAns(0);
							historialBean.create(historial);
							
							HistorialLog historialLog = ConvertExpediente.convertToHistorialLogVO(expediente);
							Empleado emp = (Empleado) getObjectSession(Constantes.USUARIO_AD_SESION);
							historialLog.setEmpleado(emp);
							EstadoCE estadoCEH = estadoCEBean.buscarPorId(Long.parseLong(Constantes.CODIGO_ESTADO_ERROR));
							historialLog.setEstadoCE(estadoCEH);
							historialLog.setFechaT1(null);
							historialLog.setFechaT2(null);
							historialLog.setFechaT3(null);
							String tipoError = "";
							if(process){
								tipoError = "PROCESS";
							}
							else if(content){
								tipoError = "CONTENT";
							}
							historialLog.setMotivoDevolucionCancelacion("Error del sistema - " + tipoError);
							historialLogBean.create(historialLog);
							
						}
					} catch(Exception e){
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
		buscar();
		return null;
	}
	
	public String cancelar(ExpedienteTCWPS expedienteTCWPS) {	
		LOG.info("en el metodo continuar");
		try{
			// se llama al servicio
			String tkiid = expedienteTCWPS.getTaskID();
			expedienteTCWPS.setAccion(Constantes.ACCION_CANCELAR_MANUAL);
			RemoteUtils objRemoteUtils = new RemoteUtils();
			String resultado = objRemoteUtils.completarTarea(tkiid, expedienteTCWPS);
			LOG.info("resultado::::"+resultado);
			if("200".equals(resultado)){
				// SE ACTUALIZA EL ESTADO DEL EXPEDIENTE
				Expediente expediente = expedienteBean.buscarPorId(Long.parseLong(expedienteTCWPS.getCodigo()));
				expediente.setEstado(new Estado());
				expediente.getEstado().setId(Constantes.ESTADO_CERRADO_TAREA_27);
				expedienteBean.edit(expediente);
				// HISTORIAL
				Historial historial = ConvertExpediente.convertToHistorialVO(expediente);
				historial.setTiempoObjTC(0);
				historial.setTiempoObjTE(0);
				historial.setTiempoPreTC(0);
				historial.setTiempoPreTE(0);
				historial.setNomColumna(null);
				historial.setAns(0);
				historialBean.create(historial);
				
				HistorialLog historialLog = ConvertExpediente.convertToHistorialLogVO(expediente);
				Empleado emp = (Empleado) getObjectSession(Constantes.USUARIO_AD_SESION);
				historialLog.setEmpleado(emp);
				EstadoCE estadoCEH = estadoCEBean.buscarPorId(Long.parseLong(Constantes.CODIGO_ESTADO_ERROR));
				historialLog.setEstadoCE(estadoCEH);
				historialLog.setFechaT1(null);
				historialLog.setFechaT2(null);
				historialLog.setFechaT3(null);
				String tipoError = "";
				if(process){
					tipoError = "PROCESS";
				}
				else if(content){
					tipoError = "CONTENT";
				}
				historialLog.setMotivoDevolucionCancelacion("Error del sistema - " + tipoError);
				historialLogBean.create(historialLog);
				
			}
		} catch(Exception e){
			LOG.error(e.getMessage(), e);
		}
		buscar();
		return "/bandejaMonitoreo/formBandejaMonitoreo?faces-redirect=true";
	}
	
	public boolean isProcess() {
		return process;
	}

	public void setProcess(boolean process) {
		this.process = process;
	}

	public boolean isContent() {
		return content;
	}

	public void setContent(boolean content) {
		this.content = content;
	}

	public String getStrSeleccionados() {
		return strSeleccionados;
	}

	public void setStrSeleccionados(String strSeleccionados) {
		this.strSeleccionados = strSeleccionados;
	}

	public HtmlInputText getHtmlIdSeleccionados() {
		return htmlIdSeleccionados;
	}

	public void setHtmlIdSeleccionados(HtmlInputText htmlIdSeleccionados) {
		this.htmlIdSeleccionados = htmlIdSeleccionados;
	}
	
	
}