package com.ibm.bbva.ctacte.controller.comun;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.Empleado;
import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.bean.Operacion;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieAprobarRevocatoria;
import com.ibm.bbva.ctacte.controller.form.AprobarRevocatoriaMB;
import com.ibm.bbva.ctacte.dao.EmpleadoDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.ListSorter;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name = "pieAprobarRevocatoria")
@ViewScoped
public class PieAprobarRevocatoriaMB extends AbstractMBean {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PieAprobarRevocatoriaMB.class);
	@ManagedProperty(value = "#{aprobarRevocatoria}")
	private AprobarRevocatoriaMB aprobarRevocatoria;
	private IPieAprobarRevocatoria managedBean;
	private List<SelectItem> abogadosExternos;
	private List<SelectItem> motivosReasignados;
	private boolean rbAprobado;
	private boolean rbRechazado;
	private boolean disTab;
	private boolean reasignados1;
	private boolean observacion;
	private boolean botonReasignar;
	private boolean botonFinalizar;
	private boolean habilitarOpciones;
	private boolean habilitarAprobado;
	private boolean habilitarRechazado;
	private String idAbogado;
	private Expediente expediente;	
	private EstadoTarea estado;
	private Operacion operacion;
	private String valor;
	private String strObservaciones;
	private String accion;
	private int idTarea;
	private String strResultado;
	@EJB
	private EmpleadoDAO empleadoDAO;
	@EJB
	private MotivoDAO motivoDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;
	
	@PostConstruct
	public void iniciar() {
		LOG.info("iniciar()");
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual{}", pagina);
		if ("formAprobarRevocatoria".equals(pagina)) {
			managedBean = aprobarRevocatoria;
		}
		
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		LOG.info("habilitar Aprobado"+ habilitarAprobado);
		LOG.info("habilitar Rechazado"+ habilitarRechazado);		
		Motivo motivo=new Motivo();		
		expediente.setMotivo(motivo);
		crearListaAbogados();
		crearListaMotivosReasignacion();
		strResultado=null;
		disTab = true;		
		reasignados1 = true;
		observacion = true; // texto Observaciones, inhabilitado al inicio
		botonReasignar = true;
		botonFinalizar = false;
		// verificar en que tarea Estamos
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_APROBAR_REVOCATORIA.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}
		}
		managedBean.setIPieAprobarRevocatoria(this);
	}

	public void habilitarControles(ValueChangeEvent ae) {
		LOG.info("habilitarControles()");
		idAbogado = (String) ae.getNewValue();
		LOG.info("idAbogado" + idAbogado);
		if (ConstantesAdmin.CODIGO_CAMPO_VACIO.equalsIgnoreCase(idAbogado)){
			LOG.info("idAbogado" + idAbogado);
			reasignados1 = true;
			observacion = true;
			botonReasignar = true;
			botonFinalizar = false;
			//habilitarOpciones=true;
			habilitarAprobado=false;
			habilitarRechazado=false;
			LOG.info("habilitar Aprobado"+ habilitarAprobado);
			LOG.info("habilitar Rechazado"+ habilitarRechazado);
		}else
		{				
			LOG.info("idAbogado" + idAbogado);
			reasignados1 = false;
			observacion = false;
			botonReasignar = false;
			botonFinalizar = true;
			//habilitarOpciones=true;
			habilitarAprobado=true;
			habilitarRechazado=true;
			LOG.info("habilitar Aprobado"+ habilitarAprobado);
			LOG.info("habilitar Rechazado"+ habilitarRechazado);
		}
	}	
	
	public void habilitarEditor(ValueChangeEvent ae){
		strResultado=(String) ae.getNewValue();;
		LOG.info("habilitarEditor()");
		LOG.info("strResultado" + strResultado);	
		if (strResultado.equalsIgnoreCase(ConstantesBusiness.RECHAZADO)){
			disTab=false;
		}else if (strResultado.equalsIgnoreCase(ConstantesBusiness.APROBADO)){
			disTab=true;
		}
	
	}
	
	private void crearListaAbogados() {
		List<Empleado> listaAbogados = empleadoDAO.getAbogadosBastanteo(ConstantesBusiness.EMPLEADO_TIPO_ABOGADO);
		abogadosExternos = Util.crearItems(listaAbogados, true, "codigo","nombres,apepat,apemat", "{0} {1} {2}", true);
	}

	private void crearListaMotivosReasignacion() {
		List<Motivo> listaReasignados = motivoDAO.findByTarea(ConstantesBusiness.ID_TAREA_APROBAR_REVOCATORIA, ConstantesBusiness.MOTIVO_REASIGNADOS);
		motivosReasignados = Util.crearItems(listaReasignados, true, "id","descripcion");
		ListSorter.ordenarMotivos(motivosReasignados);
	}

	public void finalizarRevocatoria(ActionEvent ae) {
		LOG.info("finalizarRevocatoria(ActionEvent ae)");
		String accion=ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR;
		grabarFinalizarRevocatoria(accion);		
	}
	
	public void reasignarExpediente(ActionEvent ae) {
		LOG.info("reasignarExpediente(ActionEvent ae)");
		String accion =ConstantesBusiness.ACCION_REASIGNAR_EXPEDIENTE;
		grabarReasignarExpediente(accion);	
		redirectAction("../bandeja/bandeja");
	}
	
	public boolean siHayResultado () {
		LOG.info("siHayResultado ()");
		boolean esCorrecto = true;
		if (StringUtils.isEmpty(valor)) {
			mensajeErrorPrincipal("idTabResultado:idEditor", "Ingrese el resultado de la Revocatoria");
			esCorrecto = false;
			}			
		return esCorrecto;
	}
	
	public boolean siHayObservaciones(){
		LOG.info("siHayObservaciones()");
		boolean hayObservaciones=true;
		if (StringUtils.isEmpty(strObservaciones)){
			mensajeErrorPrincipal("idReasignarExpediente:idObservaciones","Ingrese las observaciones");
		}
		return hayObservaciones;
	}
	
	public boolean esValido(){
		LOG.info("esValido()");
		boolean esCorrecto = true;
		if (StringUtils.isEmpty(strResultado)) {
			mensajeErrorPrincipal("idFinaliceSol:idIndAd", "Ingrese las indicaciones Adicionales");
			esCorrecto = false;
		}
		
		return esCorrecto;
		
	}
	
	private void grabarFinalizarRevocatoria(String accion){
		Tarea tarea=new Tarea();
		EstadoTarea estadoTarea=new EstadoTarea();
		if (managedBean.siHayResultado()|| managedBean.esValido()){
			if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_APROBAR)){
				expediente.setEstado(new EstadoExpediente());
				expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
				tarea.setId(ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE);
				estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
			}
			try{
				expediente.setResultado(strResultado);
				LOG.info("valorResultado"+expediente.getResultado());
				expediente.setMotivo(null);
				//expediente.setResultadoRevocatoria(valor.getBytes());	
				expediente.setResultadoRevocatoria(valor==null?null:valor.getBytes());
				Util.generarRegistroHistExp(expediente);
				expediente = expedienteDAO.update(expediente);
				LOG.info("Grabo expediente");
				LOG.info("expediente al process");
				AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
				ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenericoCU2729(ConstantesBusiness.APROBADO.equalsIgnoreCase(strResultado) ? 
						ConstantesBusiness.REVOCATORIA_APROBADA : ConstantesBusiness.REVOCATORIA_RECHAZADA);
				LOG.info("StrResultado");
				String tkiid = expedienteCC.getTaskID();
				LOG.info("tkkid " +tkiid.toString() );
				RemoteUtils bandeja = new RemoteUtils();
				LOG.info("enviara expediente");
				//bandeja.enviarExpediente(tkiid, expedienteCC);
				LOG.info("completara tarea");
				bandeja.completarTarea(tkiid, expedienteCC);
				
				// solo debe redirigir a bandeja si finaliza la revocatoria
				LOG.info("enviara bandeja");
				redirectAction("../bandeja/bandeja");
			} catch(Exception e) {
				//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Falta colocar valores: "+e.getMessage());
				mensajeErrorPrincipal("idTabResultado:idEditor", "Ocurrió un error al grabar.");
				LOG.error("Ocurrió un error al finalizar revocatoria.", e);
			}
		}
	}

	private void grabarReasignarExpediente(String accion){
		Tarea tarea=new Tarea();
		EstadoTarea estadoTarea=new EstadoTarea();
		if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_REASIGNAR_EXPEDIENTE)){
			expediente.setEstado(new EstadoExpediente());
			expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);		
			tarea.setId(ConstantesBusiness.ID_TAREA_REVISAR_EJECUTAR_REVOCATORIA);
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
		}
		for (ExpedienteTarea expedienteTarea:expediente.getExpedienteTareas()){
			if (ConstantesAdmin.FORM_APROBAR_REVOCATORIA.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				expedienteTarea.setTarea(tarea);
				expedienteTarea.setEstadoTarea(estadoTarea);
				expedienteTarea.setExpediente(expediente);
				break;
			}
		}
		if (expediente.getMotivo().getId() > 0) {
			expediente.setMotivo(motivoDAO.load(expediente.getMotivo().getId()));
		} else {
			expediente.setMotivo(null);
		}
		expediente.setObservaciones(strObservaciones);
		expediente.setAccion(accion);
		Util.generarRegistroHistExp(expediente);
		expediente = expedienteDAO.update(expediente);
		LOG.info("Descripcion del Estado: " + expediente.getEstado().getDescripcion());
		
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		expedienteCC.setCodUsuarioActual(idAbogado);
		Empleado empAB = empleadoDAO.findByCodigo(idAbogado);
		expedienteCC.setNomUsuarioActual(empAB.getNombres()+" "+empAB.getApepat()+" "+empAB.getApemat());
		expedienteCC.getDatosFlujoCtaCte().setAccion(ConstantesBusiness.ACCION_REASIGNAR_EXPEDIENTE);		
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("expedienteCC.getCodUsuarioActual() : "+expedienteCC.getCodUsuarioActual());
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("expedienteCC.getNomUsuarioActual() : "+expedienteCC.getNomUsuarioActual());
		String tkiid = expedienteCC.getTaskID();
		RemoteUtils bandeja = new RemoteUtils();
		//bandeja.enviarExpediente(tkiid, expedienteCC);
		bandeja.completarTarea(tkiid, expedienteCC);
	}
	
	
	public IPieAprobarRevocatoria getManagedBean() {
		return managedBean;
	}

	public void setManagedBean(IPieAprobarRevocatoria managedBean) {
		this.managedBean = managedBean;
	}

	public boolean isRbAprobado() {
		return rbAprobado;
	}

	public void setRbAprobado(boolean rbAprobado) {
		this.rbAprobado = rbAprobado;
	}

	public boolean isRbRechazado() {
		return rbRechazado;
	}

	public void setRbRechazado(boolean rbRechazado) {
		this.rbRechazado = rbRechazado;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	
	public EstadoTarea getEstado() {
		return estado;
	}

	public void setEstado(EstadoTarea estado) {
		this.estado = estado;
	}
	
	public String getStrObservaciones() {
		return strObservaciones;
	}

	public void setStrObservaciones(String strObservaciones) {
		this.strObservaciones = strObservaciones;
	}

	public AprobarRevocatoriaMB getAprobarRevocatoria() {
		return aprobarRevocatoria;
	}

	public void setAprobarRevocatoria(AprobarRevocatoriaMB aprobarRevocatoria) {
		this.aprobarRevocatoria = aprobarRevocatoria;
	}

	public List<SelectItem> getAbogadosExternos() {
		return abogadosExternos;
	}

	public void setAbogadosExternos(List<SelectItem> abogadosExternos) {
		this.abogadosExternos = abogadosExternos;
	}



	public List<SelectItem> getMotivosReasignados() {
		return motivosReasignados;
	}

	public void setMotivosReasignados(List<SelectItem> motivosReasignados) {
		this.motivosReasignados = motivosReasignados;
	}	
	

	
	public String getStrResultado() {
		return strResultado;
	}

	public void setStrResultado(String strResultado) {
		this.strResultado = strResultado;
	}

	public boolean isDisTab() {
		return disTab;
	}

	public void setDisTab(boolean disTab) {
		this.disTab = disTab;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public void setReasignados1(boolean reasignados1) {
		this.reasignados1 = reasignados1;
	}

	public boolean isReasignados1() {
		return reasignados1;
	}
	
	public void setBotonReasignar(boolean botonReasignar) {
		this.botonReasignar = botonReasignar;
	}

	public boolean isBotonReasignar() {
		return botonReasignar;
	}

	public boolean isBotonFinalizar() {
		return botonFinalizar;
	}

	public void setBotonFinalizar(boolean botonFinalizar) {
		this.botonFinalizar = botonFinalizar;
	}

	public boolean isObservacion() {
		return observacion;
	}

	public void setObservacion(boolean observacion) {
		this.observacion = observacion;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public void setIdAbogado(String idAbogado) {
		this.idAbogado = idAbogado;
	}

	public String getIdAbogado() {
		return idAbogado;
	}

	public boolean isHabilitarOpciones() {
		return habilitarOpciones;
	}

	public void setHabilitarOpciones(boolean habilitarOpciones) {
		this.habilitarOpciones = habilitarOpciones;
	}

	public boolean isHabilitarAprobado() {
		return habilitarAprobado;
	}

	public void setHabilitarAprobado(boolean habilitarAprobado) {
		this.habilitarAprobado = habilitarAprobado;
	}

	public boolean isHabilitarRechazado() {
		return habilitarRechazado;
	}

	public void setHabilitarRechazado(boolean habilitarRechazado) {
		this.habilitarRechazado = habilitarRechazado;
	}
	
}
