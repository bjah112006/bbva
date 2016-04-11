package com.ibm.bbva.ctacte.controller.comun;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.Tarea;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.ISubsanarFinalizarSolicitud;
import com.ibm.bbva.ctacte.controller.form.SubsanarDocumentoMB;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="subsanarFinalizarSolicitud")
@ViewScoped
public class SubsanarFinalizarSolicitudMB extends AbstractMBean {

	private static final long serialVersionUID = -5188963060411724143L;
	private static final Logger LOG = LoggerFactory.getLogger(SubsanarFinalizarSolicitudMB.class);	
	
	@ManagedProperty (value="#{subsanarDocumento}")
	private SubsanarDocumentoMB subsanarDocumento;	
	private ISubsanarFinalizarSolicitud iSubsanarFinalizarSolicitud;	
	private Expediente expediente;
	private Tarea tarea;
	@EJB
	private ExpedienteDAO expedienteDAO;
	
	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");	
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		 if ("formSubsanarDocumentos".equals(pagina)){
			//iSubsanarFinalizarSolicitud=subsanarDocumento;
		}
		expediente=(Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);		
		//managedBean.setFinaliceSolicitud1(this);
	}
	
	public void reenviarExpediente(ActionEvent ae){
		LOG.info("reenviarExpediente(ActionEvent ae");
//		CGC expediente.setEstadoExpediente("Expediente finalizado de subsanar documentos");
		expediente = expedienteDAO.update(expediente);	
		
	}
	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setISubsanarFinalizarSolicitud(
			ISubsanarFinalizarSolicitud iSubsanarFinalizarSolicitud) {
		this.iSubsanarFinalizarSolicitud = iSubsanarFinalizarSolicitud;
	}

	public ISubsanarFinalizarSolicitud getISubsanarFinalizarSolicitud() {
		return iSubsanarFinalizarSolicitud;
	}


}
