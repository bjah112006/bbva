package com.ibm.bbva.ctacte.controller.comun;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "abrirPDF")
@RequestScoped
public class AbrirPdfMB implements Serializable {

	private static final long serialVersionUID = -4990318622014182410L;

	private static final Logger LOG = LoggerFactory.getLogger(AbrirPdfMB.class);

	private String rutaCM;

	@PostConstruct
	public void iniciar() {
		String url = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("url");
		LOG.info("Parámetro URL Content: " + url);
		this.rutaCM = url;
	}

	public String getRutaCM() {
		return rutaCM;
	}

	public void setRutaCM(String rutaCM) {
		this.rutaCM = rutaCM;
	}

}
