package com.ibm.bbva.controller.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.Consulta;
import pe.ibm.bean.ExpedienteTCWPS;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.LogErrores;
import com.ibm.bbva.entities.Tarea;
import com.ibm.bbva.session.DocumentoExpTcBeanLocal;
import com.ibm.bbva.session.EmpleadoBeanLocal;
import com.ibm.bbva.session.EstadoCEBeanLocal;
import com.ibm.bbva.session.LogErroresBeanLocal;
import com.ibm.bbva.session.PerfilBeanLocal;
import com.ibm.bbva.session.TareaBeanLocal;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "buscarBandejaMonitoreo")
@ViewScoped
public class BuscarBandejaMonitoreoMB extends AbstractMBean {
	
	@EJB
	private PerfilBeanLocal perfilBean;
	@EJB
	private EmpleadoBeanLocal empleadoBean;
	@EJB
	private TareaBeanLocal tareaBean;
	@EJB
	private EstadoCEBeanLocal estadoCEBean;	
	@EJB
	private LogErroresBeanLocal logErroresBean;
	@EJB
	private DocumentoExpTcBeanLocal documentoExpTcBeanLocal;
	
	private List<SelectItem> roles;
	private List<SelectItem> tareas;
	private List<SelectItem> estados;	
	private String rolSeleccionado;
	private String tareaSeleccionada;
	private String usuarioSeleccionado;
	private String estadoSeleccionado;	
	
	private String tipoBusqueda;
	private String codigoExpediente;
	private Date fechaInicio;
	private Date fechaFin;
	
	private static final Logger LOG = LoggerFactory.getLogger(BuscarBandejaMonitoreoMB.class);
	
	public BuscarBandejaMonitoreoMB() {
		
	}

	@PostConstruct
	public void init() {
		
		this.limpiar();
		
		/** Obtener último tipo búsqueda **/
		tipoBusqueda = (String)getObjectSession(Constantes.TIPO_BUSQUEDA_BM);
		
		/*Carga Lista de Perfiles/Roles*/
		roles = Util.crearItems(perfilBean.buscarTodos(), true, "id", "descripcion");
		
		/*Carga Lista de Tareas*/
		tareas = Util.crearItems(tareaBean.buscarTodos(), true, "id", "descripcion");
		
		/*Carga Lista de Estado*/
		estados = Util.crearItems(estadoCEBean.buscarTodos(), true, "id", "descripcion");

	}
	
	public void buscar () {
		if(valida()){
			if(tareaSeleccionada.equals("-1")){
				tareaSeleccionada = "";
			}
			if(rolSeleccionado.equals("-1")){
				rolSeleccionado = "";
			}
			if(usuarioSeleccionado.equals("-1")){
				usuarioSeleccionado = "";
			}
			if(estadoSeleccionado.equals("-1")){
				estadoSeleccionado = "";
			}
			Consulta consulta = new Consulta();
			consulta.setFechaInicio(fechaInicio);
			consulta.setFechaFin(fechaFin);
			consulta.setIdTarea(tareaSeleccionada);
			consulta.setIdPerfilUsuarioActual(rolSeleccionado);
			consulta.setCodUsuarioActual(usuarioSeleccionado);
			consulta.setCodigoExpediente(codigoExpediente);
			consulta.setEstado(Util.obtenerDescripcion(estados, estadoSeleccionado));
			consulta.setTipoConsulta(Integer.parseInt(tipoBusqueda));
			
			List<ExpedienteTCWPS> listTabla = new ArrayList<ExpedienteTCWPS>();
			
			if("1".equals(tipoBusqueda)){
				// busqueda en base de datos
				List<LogErrores> listLogErrores = new ArrayList<LogErrores>();
				listLogErrores = logErroresBean.buscarLogErrores(fechaInicio, fechaFin, rolSeleccionado, usuarioSeleccionado, tareaSeleccionada, estadoSeleccionado, codigoExpediente);
				for(LogErrores logErrores : listLogErrores){
					ExpedienteTCWPS expedienteTCWPS = new ExpedienteTCWPS();
					//tablaMonitoreo.setCantidadDocumentos(String.valueOf(logErrores.getCantidadDocumentos())); -- no mostrar en la grilla
					expedienteTCWPS.setCodigo(String.valueOf(logErrores.getExpediente().getId()));
					
					if(logErrores.getEmpleado() != null){
						expedienteTCWPS.setCodigoUsuarioAnterior(logErrores.getEmpleado().getCodigo());
						expedienteTCWPS.setNombreUsuarioActual(logErrores.getEmpleado().getNombresCompletos());
					}

					//expedienteTCWPS.setDescripcionError(logErrores.getDescripcionError()); -- no mostrar en la grilla
					expedienteTCWPS.setPerfilUsuarioActual(logErrores.getPerfil().getDescripcion());
					expedienteTCWPS.setDesTarea(logErrores.getTarea().getDescripcion());
					expedienteTCWPS.setEstadoAnterior(logErrores.getEstadoCE().getDescripcion());
					
					String fechaCancelacion = logErrores.getFechaCancelacion() == null ? "" : Util.parseDateString(logErrores.getFechaCancelacion(),"dd/MM/yyyy");
					expedienteTCWPS.setFechaCancelacion(fechaCancelacion);
					
					String fechaRestauracion = logErrores.getFechaRestauracion() == null ? "" : Util.parseDateString(logErrores.getFechaRestauracion(),"dd/MM/yyyy");
					expedienteTCWPS.setFechaRestauracion(fechaRestauracion);
					
					String fechaIncidencia = logErrores.getFechaIncidente() == null ? "" : Util.parseDateString(logErrores.getFechaIncidente(),"dd/MM/yyyy");
					expedienteTCWPS.setFechaIncidencia(fechaIncidencia);

					//expedienteTCWPS.setNroReintentos(String.valueOf(logErrores.getExpediente().getId())); -- no mostrar en la grilla
					expedienteTCWPS.setTipoError(logErrores.getTipoError());
										
					listTabla.add(expedienteTCWPS);
				}

			}
			else{
				RemoteUtils remoteUtils = new RemoteUtils();
				listTabla = remoteUtils.obtenerListaTareasGestion(consulta);
				for(ExpedienteTCWPS expedienteTCWPS : listTabla){
					if("2".equals(tipoBusqueda)){
						expedienteTCWPS.setTipoError("PROCESS");
					}
					else if("3".equals(tipoBusqueda)){
						expedienteTCWPS.setTipoError("CONTENT");
						// se calcula la cantidad de documentos subidos
						List<DocumentoExpTc> listDocumentoExpTc = documentoExpTcBeanLocal.buscarPorExpedienteFlagEscaneado(Long.parseLong(expedienteTCWPS.getCodigo()), "1");
						int cantidadSubidos = 0;
						for(DocumentoExpTc documentoExpTc : listDocumentoExpTc){
							if(documentoExpTc.getIdCm() != null && documentoExpTc.getIdCm().intValue() > 0){
								cantidadSubidos++;
							}
						}
						expedienteTCWPS.setCantDocumentos(""+cantidadSubidos);
					}
					if(expedienteTCWPS.getActivado() != null){
						try{
							Calendar activado = expedienteTCWPS.getActivado();
							SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							String formatted = format1.format(activado.getTime());
							expedienteTCWPS.setFechaIncidencia(formatted);
						} catch(Exception e){
							expedienteTCWPS.setFechaIncidencia("");
						}
					}
					Tarea tarea = tareaBean.buscarPorId(expedienteTCWPS.getIdTareaAnterior());
					if(tarea != null)
						expedienteTCWPS.setDesTarea(tarea.getDescripcion());
				}
			}
			addObjectSession(Constantes.LISTA_BANDEJA_MONITOREO, listTabla);
			addObjectSession(Constantes.TIPO_BUSQUEDA_BM, tipoBusqueda);
			
			FacesContext ctx = FacesContext.getCurrentInstance();
			TablaBandejaMonitoreoMB tablaBandejaMonitoreoMB = (TablaBandejaMonitoreoMB)  
					ctx.getApplication().getVariableResolver().resolveVariable(ctx, "tablaBandejaMonitoreo");
			tablaBandejaMonitoreoMB.actualizarLista();
			
		}
	}
	
	public boolean valida() {
		boolean valida = true;
		if(tipoBusqueda == null || "".equals(tipoBusqueda)){
			addMessageError("frmBandejaMonitoreo:tipoBusqueda", 
					"com.ibm.bbva.common.buscarBandejaMonitoreo.msg.tipoBusqueda");
			valida = false;
		}
		return valida;
	}
	
	public void limpiar () {
		this.codigoExpediente = "";
		this.tipoBusqueda = "";
		this.fechaInicio = null;
		this.fechaFin = null;
		this.rolSeleccionado = "";
		this.tareaSeleccionada = "";
		this.usuarioSeleccionado = "";
		this.estadoSeleccionado = "";
		List<ExpedienteTCWPS> listTabla = new ArrayList<ExpedienteTCWPS>();
		addObjectSession(Constantes.LISTA_BANDEJA_MONITOREO, listTabla);
	}

	public List<SelectItem> getRoles() {
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public List<SelectItem> getTareas() {
		return tareas;
	}

	public void setTareas(List<SelectItem> tareas) {
		this.tareas = tareas;
	}

	public List<SelectItem> getEstados() {
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public String getRolSeleccionado() {
		return rolSeleccionado;
	}

	public void setRolSeleccionado(String rolSeleccionado) {
		this.rolSeleccionado = rolSeleccionado;
	}

	public String getTareaSeleccionada() {
		return tareaSeleccionada;
	}

	public void setTareaSeleccionada(String tareaSeleccionada) {
		this.tareaSeleccionada = tareaSeleccionada;
	}

	public String getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(String usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}

	public String getEstadoSeleccionado() {
		return estadoSeleccionado;
	}

	public void setEstadoSeleccionado(String estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
	}

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getCodigoExpediente() {
		return codigoExpediente;
	}

	public void setCodigoExpediente(String codigoExpediente) {
		this.codigoExpediente = codigoExpediente;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	

}