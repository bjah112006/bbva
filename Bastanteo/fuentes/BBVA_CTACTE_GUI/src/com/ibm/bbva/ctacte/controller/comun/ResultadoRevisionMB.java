package com.ibm.bbva.ctacte.controller.comun;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.interf.IResultadoRevision;
import com.ibm.bbva.ctacte.controller.form.VerificarCalidadDocumentoMB;


@ManagedBean (name="resultadorevision")
@ViewScoped
public class ResultadoRevisionMB extends AbstractMBean {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ResultadoRevisionMB.class);

	@ManagedProperty ("#{verificarCalidadDocumentos}")
	private VerificarCalidadDocumentoMB verificarCalidadDocumentos;
	private IResultadoRevision managedBean;
	private boolean desHabilitarBotonAprobar;
	private boolean desHabilitarBotonRechazar;
	private String comentario;

	
	public boolean isDesHabilitarBotonAprobar() {
		return desHabilitarBotonAprobar;
	}

	public void setDesHabilitarBotonAprobar(boolean desHabilitarBotonAprobar) {
		this.desHabilitarBotonAprobar = desHabilitarBotonAprobar;
	}

	public boolean isDesHabilitarBotonRechazar() {
		return desHabilitarBotonRechazar;
	}

	public void setDesHabilitarBotonRechazar(boolean desHabilitarBotonRechazar) {
		this.desHabilitarBotonRechazar = desHabilitarBotonRechazar;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
//		desHabilitarBotonAprobar = false;
//		desHabilitarBotonRechazar = true;
		
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		if ("formVerificarCalidadDocumentos".equals(pagina)) {
			managedBean = verificarCalidadDocumentos;
			managedBean.setResultadoRevision(this);		
		}
		//managedBean.actualizarBtnAprobarRechazarbyErrores();
	}
	
	public VerificarCalidadDocumentoMB getVerificarCalidadDocumentos() {
		return verificarCalidadDocumentos;
	}

	public void setVerificarCalidadDocumentos(
			VerificarCalidadDocumentoMB verificarCalidadDocumentos) {
		this.verificarCalidadDocumentos = verificarCalidadDocumentos;
	}

	public void aprobarDocumentacion(ActionEvent action){
		//implemento 
		//guardar en la base de datos
		String accion=ConstantesBusiness.ACCION_APROBAR_DOCUMENTACION;
		managedBean.aprobarDocumentacion(accion);
		redirectAction("../bandeja/bandeja");
		
	}
	
	public void rechazarDocumentacion(ActionEvent action){
		//implemento 
		//guardar en la base de datos
		String accion=ConstantesBusiness.ACCION_RECHAZAR_DOCUMENTACION;
		managedBean.rechazarDocumentacion(accion);
		
		redirectAction("../bandeja/bandeja");
	}

}
