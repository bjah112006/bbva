package com.ibm.bbva.controller.common;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.cm.service.impl.Documento;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.TipoDocumento;

@ManagedBean(name = "abrirPDF")
@RequestScoped
public class AbrirPdfMB implements Serializable {

	private static final long serialVersionUID = -4990318622014182410L;

	private static final Logger LOG = LoggerFactory.getLogger(AbrirPdfMB.class);
	
	@EJB
	private FacadeLocal facade;

	private String rutaCM;

	@PostConstruct
	public void iniciar() {
		String idCm = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idcm");
		String codTipoDoc = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codtipodoc");
		String redirect = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("redirect");
		
		if (idCm != null && idCm.trim().length() > 0) {
			DocumentoExpTc documentoExp = new DocumentoExpTc();
			documentoExp.setIdCm(new BigDecimal(idCm));
			documentoExp.setTipoDocumento(new TipoDocumento());
			documentoExp.getTipoDocumento().setCodigo(codTipoDoc);
			
			if (documentoExp.getIdCm().intValue() > 0) {
				Documento doc = facade.obtenerDocumentoCM(documentoExp);
				if (doc != null) {
					this.rutaCM = doc.getUrlContent();
				} else {
					LOG.warn("No se encontró el documento con idCm="+idCm+" en el Content.");
					this.rutaCM = null;
				}
			} else {
				this.rutaCM = null;
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
