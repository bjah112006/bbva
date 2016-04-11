package com.ibm.bbva.ctacte.controller.comun;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Motivo;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieEjecutarConfirmarModificatorias;
import com.ibm.bbva.ctacte.controller.form.EjecutarConfirmarModificatoriaMB;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.MotivoDAO;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.ListSorter;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name = "pieEjecutar")
@ViewScoped
public class PieEjecutarConfirmarModificatoriasMB extends AbstractMBean {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PieEjecutarConfirmarModificatoriasMB.class);
	@ManagedProperty(value = "#{ejecutarConfirmarModificatoria}")
	private EjecutarConfirmarModificatoriaMB ejecutarConfirmarModificar;
	private IPieEjecutarConfirmarModificatorias iPieEjecutarConfirmarModificatorias;
	private List<SelectItem> listaMotivo;
	private String descripcionMotivo;
	private String motivos;
	private String indicaciones;
	private String strInstrucciones;
	private Motivo motivo;	
	private boolean disTextoArea;
	private boolean disTerminarAccion;
	private boolean disBotonInstruccion;
	private boolean disInstrucciones;
	private boolean boolIndicaciones;
	private Expediente expediente;
	private EstadoTarea estado;
	private String selMotivo;
	private int idTarea;
	@EJB
	private MotivoDAO motivoDAO;
	@EJB
	private ExpedienteDAO expedienteDAO;

	@PostConstruct
	public void iniciar() {
		LOG.info("iniciar()");
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual{}", pagina);
		if ("formEjecutarConfirmarModificatoria".endsWith(pagina)) {
			iPieEjecutarConfirmarModificatorias = ejecutarConfirmarModificar;
		}		
		controlesAlInicio();
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		
		/* Visor Instrucciones*/
		
		LOG.info("Instrucciones Bastanteo"+expediente.getInstruccionesBastanteo());
		disInstrucciones=true;
		/*strInstrucciones= new String(expediente.getInstruccionesBastanteo());
		LOG.info("strInstrucciones"+strInstrucciones);*/
		byte[] byteInstrucciones = expediente.getInstruccionesBastanteo();
		strInstrucciones=byteInstrucciones==null ? null : new String (byteInstrucciones);
		LOG.info("strInstrucciones"+strInstrucciones);
		expediente.setInstruccionesBastanteo(strInstrucciones==null ? null : strInstrucciones.getBytes());
		//strInstrucciones=new String (expediente.getInstruccionesBastanteo());// para recuperar el valor q grabe del BLOB
		LOG.info("Instrucciones"+strInstrucciones);
		
		/*Fin Visor Instrucciones*/
		
		Motivo motivo = new Motivo();
		expediente.setMotivo(motivo);
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_EJECUTAR_CONFIRMAR_MODIFICATORIA.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}
		}
		crearListaMotivos();
		iPieEjecutarConfirmarModificatorias.setIPieEjecutarConfirmarModificatorias(this);
		
		
	}
	
	public void controlesAlInicio(){
		disTextoArea=true;
		disTerminarAccion = false;
		disBotonInstruccion = true;	
		boolIndicaciones=false;
	}

	public void habilitarTexto(ValueChangeEvent ae ) {
		LOG.info("habilitarTexto(ValueChangeEvent ae)");
		if (boolIndicaciones=true){
			LOG.info("if (boolIndicaciones=true)");
			disTextoArea = false;				
			//boolIndicaciones=false;
		}else {
			LOG.info("(boolIndicaciones=false)");
			disTextoArea = true;				
			//boolIndicaciones=true;
		}
	}

	public boolean isBoolIndicaciones() {
		return boolIndicaciones;
	}

	public void setBoolIndicaciones(boolean boolIndicaciones) {
		this.boolIndicaciones = boolIndicaciones;
	}

	public void habilitarBotones(AjaxBehaviorEvent event) {
		LOG.info("disBotonDevolver()");
		// if (motivos!=null && !motivos.equals("")){
		selMotivo = getRequestParameter("form:motivos");
		LOG.info("selMotivo: "+selMotivo);
		if (ConstantesAdmin.CODIGO_CAMPO_VACIO.equals(selMotivo)) {
			disTerminarAccion = false;
			disBotonInstruccion = true;
		} else {
			disTerminarAccion = true;
			disBotonInstruccion = false;
		}
		// }
	}

	public String getSelMotivo() {
		return selMotivo;
	}

	public void setSelMotivo(String selMotivo) {
		this.selMotivo = selMotivo;
	}

	public void accionBotonDevolver(ActionEvent actionEvent) {
		LOG.info("accionBotonDevolver(ActionEvent ae");
		String accion=ConstantesBusiness.ACCION_DEVOLVER_INSTRUCCION;
		ejecutarAccionBotonDevolver(accion);		
	}
	
	private void ejecutarAccionBotonDevolver(String accion){
		LOG.info("ejecutarAccionBotonDevolver(String accion)");
		Tarea tarea=new Tarea();
		EstadoTarea estadoTarea=new EstadoTarea();
		if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_DEVOLVER_INSTRUCCION)){
			expediente.setEstado(new EstadoExpediente());
			expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
			tarea.setId(ConstantesBusiness.ID_TAREA_REVISAR_APROBAR_BASTANTEO);
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
		}
		
		
		
		LOG.info("selMotivo"  + selMotivo);	
		int idMotivo=Integer.valueOf(selMotivo);
		LOG.info("idMotivo"  + idMotivo);	
		motivo = motivoDAO.load(idMotivo);
		expediente.setMotivo(motivo);		
		
		expediente.setAccion(accion);
		
		
		Util.generarRegistroHistExp(expediente);
		expediente = expedienteDAO.update(expediente);
		
		// FIX para que no se pierda la descripción del estado del expediente
		Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);
		
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
		String tkiid = expedienteCC.getTaskID();
		RemoteUtils bandeja = new RemoteUtils();
		//bandeja.enviarExpediente(tkiid, expedienteCC);
		bandeja.completarTarea(tkiid, expedienteCC);
		redirectAction("../bandeja/bandeja");
	}

	public void accionConforme(ActionEvent actionEvent) {
		LOG.info("accionConforme(ActionEvent ae)");
		String accion=ConstantesBusiness.ACCION_TERMINAR_ATENCION_CONFORME;
		ejecutarAccionConforme(accion);
		
	}
	
	private void ejecutarAccionConforme(String accion){			
		if (strInstrucciones!=null){ // existe modificatoria
			LOG.info("Existe modificatoria");
			Tarea tarea=new Tarea();
			EstadoTarea estadoTarea=new EstadoTarea();
			if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_TERMINAR_ATENCION_CONFORME)){
				expediente.setEstado(new EstadoExpediente());
				expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
				tarea.setId(ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE);
				estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
			}
			expediente.setAccion(accion);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("accionaccionaccion  "+accion);
			expediente.setMotivo(motivo);
			expediente.setObservaciones(indicaciones);
			Util.generarRegistroHistExp(expediente);
			expediente = expedienteDAO.update(expediente);
		}else // no existe modificatoria
		{   LOG.info("No existe modificatoria");
			Tarea tarea=new Tarea();
			EstadoTarea estadoTarea=new EstadoTarea();
			if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_TERMINAR_ATENCION_CONFORME)){
				expediente.setEstado(new EstadoExpediente());
				expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
				tarea.setId(ConstantesBusiness.ID_TAREA_REVISAR_APROBAR_BASTANTEO);
				estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
			}
			expediente.setAccion(accion);
			expediente.setMotivo(motivo);
			//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("accionaccionaccion  "+accion);
			expediente.setObservaciones(indicaciones);
			//expediente.setMotivo(null);
			Util.generarRegistroHistExp(expediente);
			expediente = expedienteDAO.update(expediente);
		}
		
		// FIX para que no se pierda la descripción del estado del expediente
		Util.addObjectSession(ConstantesAdmin.EXPEDIENTE_SESION, expediente);
		
		//context.addMessage(null, new FacesMessage("Mensaje Confirmacion", "El resultado de activar firmas y poderes fue: "+expediente.getResultado()));
		//mensajeErrorPrincipal("idTabResultado:idEditor", "El resultado de activar firmas y poderes fue: "+expediente.getResultado());
		LOG.info(" Grabara el expediente en el process");
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenerico();
		expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
		LOG.info(" Grabo la accion del expediente");
		String tkiid = expedienteCC.getTaskID();
		RemoteUtils bandeja = new RemoteUtils();
		//bandeja.enviarExpediente(tkiid, expedienteCC);
		LOG.info(" Completara la tarea");
		bandeja.completarTarea(tkiid, expedienteCC);
		LOG.info("Completo la tarea");
		redirectAction("../bandeja/bandeja");
		LOG.info("Fue a la bandeja");
	}

	private void crearListaMotivos() {
		List<Motivo> listadevolucion=motivoDAO.findByTarea(ConstantesBusiness.ID_TAREA_EJECUTAR_CONFIRMAR_MODIFICATORIAS, ConstantesBusiness.MOTIVO_DEVOLUCION);
		listaMotivo=Util.crearItems(listadevolucion,true,"id","descripcion");
		ListSorter.ordenarMotivos(listaMotivo);
	}

	public String getDescripcionMotivo() {
		return descripcionMotivo;
	}

	public void setDescripcionMotivo(String descripcionMotivo) {
		this.descripcionMotivo = descripcionMotivo;
	}

	public EjecutarConfirmarModificatoriaMB getEjecutarConfirmarModificar() {
		return ejecutarConfirmarModificar;
	}

	public void setEjecutarConfirmarModificar(
			EjecutarConfirmarModificatoriaMB ejecutarConfirmarModificar) {
		this.ejecutarConfirmarModificar = ejecutarConfirmarModificar;
	}

	public void setMotivos(String motivos) {
		this.motivos = motivos;
	}

	public IPieEjecutarConfirmarModificatorias getManagedBean() {
		return iPieEjecutarConfirmarModificatorias;
	}

	public void setManagedBean(IPieEjecutarConfirmarModificatorias managedBean) {
		this.iPieEjecutarConfirmarModificatorias = managedBean;
	}

	public boolean isDisBotonInstruccion() {
		return disBotonInstruccion;
	}

	public void setDisBotonInstruccion(boolean disBotonInstruccion) {
		this.disBotonInstruccion = disBotonInstruccion;
	}

	public String getIndicaciones() {
		return indicaciones;
	}

	public void setIndicaciones(String indicaciones) {
		this.indicaciones = indicaciones;
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

	public String getMotivos() {
		return motivos;
	}

	public boolean isDisTextoArea() {
		return disTextoArea;
	}

	public void setDisTextoArea(boolean disTextoArea) {
		this.disTextoArea = disTextoArea;
	}

	public boolean isDisTerminarAccion() {
		return disTerminarAccion;
	}

	public void setDisTerminarAccion(boolean disTerminarAccion) {
		this.disTerminarAccion = disTerminarAccion;
	}

	public void setListaMotivo(List<SelectItem> listaMotivo) {
		this.listaMotivo = listaMotivo;
	}

	public List<SelectItem> getListaMotivo() {
		return listaMotivo;
	}

	public IPieEjecutarConfirmarModificatorias getIPieEjecutarConfirmarModificatorias() {
		return iPieEjecutarConfirmarModificatorias;
	}

	public void setIPieEjecutarConfirmarModificatorias(
			IPieEjecutarConfirmarModificatorias pieEjecutarConfirmarModificatorias) {
		iPieEjecutarConfirmarModificatorias = pieEjecutarConfirmarModificatorias;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public String getStrInstrucciones() {
		return strInstrucciones;
	}

	public void setStrInstrucciones(String strInstrucciones) {
		this.strInstrucciones = strInstrucciones;
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public boolean isDisInstrucciones() {
		return disInstrucciones;
	}

	public void setDisInstrucciones(boolean disInstrucciones) {
		this.disInstrucciones = disInstrucciones;
	}

	
	
	
}
