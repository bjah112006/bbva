package com.ibm.bbva.ctacte.controller.comun;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;

@ManagedBean (name="reenvioDocumentosMB")
@ViewScoped
public class ReenvioDocumentosMB extends AbstractMBean {

	private static final long serialVersionUID = 9222694277255395321L;
	private static final Logger LOG = LoggerFactory.getLogger(ReenvioDocumentosMB.class);
	
	private boolean visibleApplet;
	private boolean visibleTabla;
	private String numeroExpediente;
	private String flagConsultarApplet;
	private List<String> listaDocumentos;
	
	@PostConstruct
	public void iniciar () {
		LOG.info("Iniciar");
		this.visibleApplet = false;
		this.visibleTabla = false;
		this.numeroExpediente = "";
		this.flagConsultarApplet = "0";
	}
	
	public String buscar() {
		LOG.info("Buscar: "+numeroExpediente);
		if (!numeroExpediente.trim().equals("")) {
			this.visibleApplet = true;
			this.visibleTabla = false;
			this.flagConsultarApplet = "1";
		} else {
			this.visibleApplet = false;
			this.visibleTabla = false;
			this.flagConsultarApplet = "0";
			mensajeErrorPrincipal("idNumExp", "Debe ingresar el número de expediente");
		}
		
		return null;
	}
	
	public void cargarTablaDocumentos (ActionEvent ae) {
		String strListaDocumentos = getRequestParameter("listaDocumentos");
		this.listaDocumentos = Arrays.asList(strListaDocumentos.split(","));
		this.visibleTabla = true;
	}

	public boolean isVisibleApplet() {
		return visibleApplet;
	}

	public void setVisibleApplet(boolean visibleApplet) {
		this.visibleApplet = visibleApplet;
	}

	public boolean isVisibleTabla() {
		return visibleTabla;
	}

	public void setVisibleTabla(boolean visibleTabla) {
		this.visibleTabla = visibleTabla;
	}

	public String getNumeroExpediente() {
		return numeroExpediente;
	}

	public void setNumeroExpediente(String numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}

	public String getFlagConsultarApplet() {
		return flagConsultarApplet;
	}

	public void setFlagConsultarApplet(String flagConsultarApplet) {
		this.flagConsultarApplet = flagConsultarApplet;
	}

	public List<String> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(List<String> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}
}
