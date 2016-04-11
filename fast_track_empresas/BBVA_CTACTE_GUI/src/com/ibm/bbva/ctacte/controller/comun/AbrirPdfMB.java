package com.ibm.bbva.ctacte.controller.comun;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.ctacte.cm.ConsultaContentManager;

@ManagedBean(name = "abrirPDF")
@RequestScoped
public class AbrirPdfMB implements Serializable {

	private static final long serialVersionUID = -4990318622014182410L;

	private static final Logger LOG = LoggerFactory.getLogger(AbrirPdfMB.class);

	private String rutaCM;

	@PostConstruct
	public void iniciar() {
		String idCm = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idcm");
		String redirect = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("redirect");
		String url = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("url");
		LOG.info("idcm={}", idCm);
		LOG.info("redirect={}", redirect);
		LOG.info("url={}", url);
		
		if (url != null && url.trim().length() > 0) {
			this.rutaCM = url;
		} else if (idCm != null && idCm.trim().length() > 0) {
			try {
				ConsultaContentManager consulta = new ConsultaContentManager();
				Documento doc = consulta.CM_ObtenerDocumentItemType(new Integer(idCm));
				if (doc != null && doc.getUrlContent() != null) {
					this.rutaCM = doc.getUrlContent();
				} else {
					LOG.warn("No se encontró el documento con idCm="+idCm+" en el Content.");
					this.rutaCM = null;
				}
			} catch(Exception e) {
				this.rutaCM = null;
				LOG.error(e.getMessage(), e);
			}
			if (redirect != null && redirect.equals("1")) {
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect(rutaCM);
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}

	public String getRutaCM() {
		return rutaCM;
	}

	public void setRutaCM(String rutaCM) {
		this.rutaCM = rutaCM;
	}

}
