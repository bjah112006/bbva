package com.ibm.bbva.controller.form;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.AbstractMBean;
import com.ibm.bbva.controller.Constantes;

@SuppressWarnings("serial")
@ManagedBean(name = "gestionarDocumentoCM")
@RequestScoped
public class GestionarDocumentoCMMB extends AbstractMBean {
	private String nombrebandeja = "";
	
	private static final Logger LOG = LoggerFactory.getLogger(GestionarDocumentoCMMB.class);
	
	public GestionarDocumentoCMMB() {
			
	}
	
	@PostConstruct
	public void init() {
		nombrebandeja = getObjectSession(Constantes.NOMBRE_BANDEJA_SESION).toString();	
	}
	
	public String salir() {		
		String retorna = "";		
		if (nombrebandeja.equals("formBandejaPendientes")) {
			retorna = "/bandejaPendientes/formBandejaPendientes?faces-redirect=true";
		}else if (nombrebandeja.equals("formBandejaBusqueda")) {
			retorna = "/bandejaBusqueda/formBandejaBusqueda?faces-redirect=true";
		}else if (nombrebandeja.equals("formBandejaHistorica")) {
			retorna = "/bandejaHistorica/formBandejaHistorica?faces-redirect=true";
		}else if (nombrebandeja.equals("formBandejaReasigTareas")) {
			retorna = "/bandejaReasigTareas/formBandejaReasigTareas?faces-redirect=true";
			addObjectSession(Constantes.CONSULTA_BANDASIGN_EXP, true);
		}		
		return retorna;
	}

	public String getNombrebandeja() {
		return nombrebandeja;
	}

	public void setNombrebandeja(String nombrebandeja) {
		this.nombrebandeja = nombrebandeja;
	}
}
