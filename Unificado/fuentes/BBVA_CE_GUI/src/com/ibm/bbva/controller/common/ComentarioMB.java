package com.ibm.bbva.controller.common;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.util.Util;

@SuppressWarnings("serial")
@ManagedBean(name = "comentario")
@RequestScoped
public class ComentarioMB extends AbstractMBean {

	private Expediente expedienteVO;
	private boolean comentario;
	
	private static final Logger LOG = LoggerFactory.getLogger(ComentarioMB.class);

	public ComentarioMB() {		
	}
	
	@PostConstruct
    public void init() {
		expedienteVO = (Expediente) getObjectSession(Constantes.EXPEDIENTE_SESION);
		String nombJSP = getNombreJSPPrincipal();
		comentario = true;
		if (nombJSP.equals("formRegistrarExpediente")) {			
			//comentario = Constantes.CHECK_SELECCIONADO.equals(expedienteVO.getExpedienteTC().getFlagComentario());			
		} else {
			expedienteVO.setComentario(null);
		}
	}
	public boolean esValido () {		
		String formulario = obtenerNombreFormulario();
		
		boolean existeError = false;
		
		// validar comentario
		if (!Util.esNuloVacio(expedienteVO.getComentario()) && expedienteVO.getComentario().trim().length() > 250) {
			addMessageError(formulario + ":txtComentario", "com.ibm.bbva.common.comentario.comentarios.excede");
			existeError = true;
		}		
		return !existeError;
	}
	
	public String obtenerNombreFormulario() {
		String jspPrinc = getNombreJSPPrincipal();		
		return jspPrinc.substring(0, 1).concat(jspPrinc.substring(2));
	}
	
	public Expediente getExpedienteVO() {
		return expedienteVO;
	}

	public void setExpedienteVO(Expediente expedienteVO) {
		this.expedienteVO = expedienteVO;
	}

	public void setComentario(boolean comentario) {
		this.comentario = comentario;
	}
	
	public boolean isComentario() {
		return comentario;
	}
	
	public void copiarDatosExpediente () {
		if (comentario) {
			expedienteVO.getExpedienteTC().setFlagComentario(Constantes.CHECK_SELECCIONADO);
		} else {
			expedienteVO.setComentario("");
			expedienteVO.getExpedienteTC().setFlagComentario(Constantes.CHECK_NO_SELECCIONADO);
		}
	}
}
