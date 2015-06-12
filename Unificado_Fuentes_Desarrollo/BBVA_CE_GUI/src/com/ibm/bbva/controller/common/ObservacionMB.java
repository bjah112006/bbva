package com.ibm.bbva.controller.common;

import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.form.AprobarExpedienteMB;
import com.ibm.bbva.controller.form.EjecutarEvalCrediticiaMB;
import com.ibm.bbva.controller.form.RevisarRegistrarDictamenMB;
import com.ibm.bbva.controller.form.VerificarResultadoDomiciliariaMB;

@SuppressWarnings("serial")
@ManagedBean(name = "observacion")
@RequestScoped
public class ObservacionMB extends AbstractMBean {

	private boolean mostrarDialogo = false; // inicialmente no se muestra
	private String comentario;
	private String titulo;
	private HtmlPanelGroup dialogoModal;
	private String nombreFormPadre;
	
	private static final Logger LOG = LoggerFactory.getLogger(ObservacionMB.class);
	
	public ObservacionMB() {
		String nombJSP = getNombreJSPPrincipal();
		nombreFormPadre = nombJSP.replaceFirst("form", "frm");
	}

	public boolean isMostrarDialogo() {
		return mostrarDialogo;
	}

	public void setMostrarDialogo(boolean mostrarDialogo) {
		this.mostrarDialogo = mostrarDialogo;
	}
	
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String aceptar () {
		String nombJSP = getNombreJSPPrincipal();
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		if (nombJSP.equals("formAprobarExpediente")) {
			AprobarExpedienteMB aprobarExpedienteMB = (AprobarExpedienteMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "aprobarExpediente");
			aprobarExpedienteMB.accionVentObs(comentario);
			mostrarDialogo = false;
		}else if (nombJSP.equals("formEjecutarEvalCrediticia")) {
			EjecutarEvalCrediticiaMB ejecutarEvalCrediticiaMB = (EjecutarEvalCrediticiaMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "ejecutarEvalCrediticia");
			ejecutarEvalCrediticiaMB.accionVentObs(comentario);
			mostrarDialogo = false;
		}else if (nombJSP.equals("formRevisarRegistrarDictamen")) {
			RevisarRegistrarDictamenMB revisarRegistrarDictamenMB = (RevisarRegistrarDictamenMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "revisarRegistrarDictamen");
			revisarRegistrarDictamenMB.accionVentObs(comentario);
			mostrarDialogo = false;
		}else if (nombJSP.equals("formVerificarResultadoDomiciliaria")) {
			VerificarResultadoDomiciliariaMB verificarResultadoDomiciliariaMB = (VerificarResultadoDomiciliariaMB) FacesContext
					.getCurrentInstance().getApplication().getELResolver()
					.getValue(elContext, null, "verificarResultadoDomiciliaria");
			verificarResultadoDomiciliariaMB.accionVentObs(comentario);
			mostrarDialogo = false;
		// TODO falta el managed bean
		}/*else if (nombJSP.equals("formEvaluarFactibilidadOp")) {
			EvaluarFactibilidadOpMB evaluarFactibilidadOpMB = (EvaluarFactibilidadOpMB) getObjectRequest("evaluarFactibilidadOp");
			evaluarFactibilidadOpMB.accionVentObs(comentario);
			mostrarDialogo = false;
		}*/
		return "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
	}
	
	public String cancelar () {
		mostrarDialogo = false;
		return null;
	}

	public HtmlPanelGroup getDialogoModal() {
		return dialogoModal;
	}

	public void setDialogoModal(HtmlPanelGroup dialogoModal) {
		this.dialogoModal = dialogoModal;
	}

	public String getNombreFormPadre() {
		return nombreFormPadre;
	}

	public void setNombreFormPadre(String nombreFormPadre) {
		this.nombreFormPadre = nombreFormPadre;
	}
	
}
