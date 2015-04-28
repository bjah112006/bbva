package com.ibm.bbva.ctacte.controller.comun;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.ibm.bean.ExpedienteCC;
import pe.ibm.bpd.RemoteUtils;

import com.ibm.bbva.ctacte.bean.EstadoExpediente;
import com.ibm.bbva.ctacte.bean.EstadoTarea;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteTarea;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieEjecutarRevocatoria;
import com.ibm.bbva.ctacte.controller.form.RevisarEjecutarRevocatoriaMB;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.util.AyudaExpedienteCC;
import com.ibm.bbva.ctacte.util.Util;


@ManagedBean(name="pieEjecutarRevocatoria")
@ViewScoped
public class PieEjecutarRevocatoriaMB extends AbstractMBean{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(PieEjecutarRevocatoriaMB.class);
	@ManagedProperty(value="#{revisarEjecutarRevocatoria}")
	private RevisarEjecutarRevocatoriaMB revisarEjecutarRevocatoria;
	private IPieEjecutarRevocatoria iPieEjecutarRevocatoria;
	private Expediente expediente;
	private String value;	
	private String strResultado;
	private boolean habilitarFinalizar;	
	private int idTarea;
	@EJB
	private ExpedienteDAO expedienteDAO;
    
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		if ("formRevisarEjecutarRevocatoria".equals(pagina)){
			iPieEjecutarRevocatoria=revisarEjecutarRevocatoria;
		}
		habilitarFinalizar=true;
		strResultado=null;
		expediente=(Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		for (ExpedienteTarea expedienteTarea : expediente.getExpedienteTareas()) {
			if (ConstantesAdmin.FORM_REVISAR_EJECUTAR_REVOCATORIA.equalsIgnoreCase(expedienteTarea.getTarea().getNombrePagina())){
				idTarea = expedienteTarea.getTarea().getId().intValue();
				break;
			}
		}
		
		//value=new String (expediente.getResultadoRevocatoria());
		iPieEjecutarRevocatoria.setIPieEjecutarRevocatoria(this);		
	}
	
	public void habilitarBoton(ValueChangeEvent v){
		strResultado=(String) v.getNewValue();
		LOG.info("Resultado" + strResultado);
		habilitarFinalizar=false;
	}
	
	public void finalizarRevocatoria(ActionEvent ae){
		LOG.info("ingresarRevocatoria()");
		String accion=ConstantesBusiness.ACCION_EXPEDIENTE_FINALIZAR;
		ejecutarFinalizarRevocatoria(accion);	
		redirectAction("../bandeja/bandeja");
	}
	
	private void ejecutarFinalizarRevocatoria(String accion){
		LOG.info("ejecutarFinalizarRevocatoria(String accion)");
		Tarea tarea=new Tarea();
		EstadoTarea estadoTarea=new EstadoTarea();
		if (accion.equalsIgnoreCase(ConstantesBusiness.ACCION_EXPEDIENTE_FINALIZAR)){
			expediente.setEstado(new EstadoExpediente());
			expediente.getEstado().setId(ConstantesBusiness.ID_ESTADO_EXPEDIENTE_ENCURSO);
			tarea.setId(ConstantesBusiness.ID_TAREA_VERIFICAR_RESULTADO_TRAMITE);
			estadoTarea.setId(ConstantesBusiness.ID_ESTADO_TAREA_PENDIENTE);
		}
		expediente.setResultadoRevocatoria(value.getBytes());
		expediente.setResultado(strResultado);
		LOG.info("Opcion Resultado : "+expediente.getResultado());
		Util.generarRegistroHistExp(expediente);
		expediente.setAccion(accion);
		expediente = expedienteDAO.update(expediente);
		
		AyudaExpedienteCC ayudaExpedienteCC = new AyudaExpedienteCC();
		ExpedienteCC expedienteCC = ayudaExpedienteCC.copiarDatosGenericoCU2729(ConstantesBusiness.APROBADO.equalsIgnoreCase(strResultado) ? 
				ConstantesBusiness.REVOCATORIA_APROBADA : ConstantesBusiness.REVOCATORIA_RECHAZADA);
		expedienteCC.getDatosFlujoCtaCte().setAccion(accion);
		String tkiid = expedienteCC.getTaskID();
		RemoteUtils bandeja = new RemoteUtils();
		//bandeja.enviarExpediente(tkiid, expedienteCC);
		bandeja.completarTarea(tkiid, expedienteCC);
	}

	public RevisarEjecutarRevocatoriaMB getRevisarEjecutarRevocatoria() {
		return revisarEjecutarRevocatoria;
	}

	public void setRevisarEjecutarRevocatoria(
			RevisarEjecutarRevocatoriaMB revisarEjecutarRevocatoria) {
		this.revisarEjecutarRevocatoria = revisarEjecutarRevocatoria;
	}

	public IPieEjecutarRevocatoria getIPieEjecutarRevocatoria() {
		return iPieEjecutarRevocatoria;
	}

	public void setIPieEjecutarRevocatoria(
			IPieEjecutarRevocatoria pieEjecutarRevocatoria) {
		iPieEjecutarRevocatoria = pieEjecutarRevocatoria;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setHabilitarFinalizar(boolean habilitarFinalizar) {
		this.habilitarFinalizar = habilitarFinalizar;
	}

	public boolean isHabilitarFinalizar() {
		return habilitarFinalizar;
	}
	
	public String getStrResultado() {
		return strResultado;
	}

	public void setStrResultado(String strResultado) {
		this.strResultado = strResultado;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}


	 
}
