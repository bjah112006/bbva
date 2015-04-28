package com.ibm.bbva.ctacte.controller.comun;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosExpediente;
import com.ibm.bbva.ctacte.controller.form.VerificarResultadoTramiteMB;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="datosExpediente2")
@ViewScoped
public class DatosExpediente2MB extends AbstractMBean{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DatosExpediente2MB.class);
	@ManagedProperty (value="#{verificarResultadoTramite}")
	private VerificarResultadoTramiteMB verificarResultadoTramite;	
	private IDatosExpediente iDatosExpediente;	
	private Expediente expediente;
	private String exonerado;
	private int idTarea;
	
	@PostConstruct
	public void iniciar(){
		LOG.info("iniciar()");
		String pagina=getNombrePrincipal();
		LOG.info("Pagina actual{}", pagina);
		if ("formVerificarResultadoTramite".equals(pagina)){			
			iDatosExpediente=verificarResultadoTramite;			
			iDatosExpediente.setIDatosExpediente2(this);
		}		
		expediente = (Expediente)Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		exonerado = String.valueOf(expediente.getFlagExoneracionComision());
	}
	
	public String getExonerado() {
		return exonerado;
	}

	public void setExonerado(String exonerado) {
		this.exonerado = exonerado;
	}

	public VerificarResultadoTramiteMB getVerificarResultadoTramite() {
		return verificarResultadoTramite;
	}

	public void setVerificarResultadoTramite(
			VerificarResultadoTramiteMB verificarResultadoTramite) {
		this.verificarResultadoTramite = verificarResultadoTramite;
	}

	public IDatosExpediente getIDatosExpediente() {
		return iDatosExpediente;
	}

	public void setIDatosExpediente(IDatosExpediente datosExpediente) {
		iDatosExpediente = datosExpediente;
	}

	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	


}
