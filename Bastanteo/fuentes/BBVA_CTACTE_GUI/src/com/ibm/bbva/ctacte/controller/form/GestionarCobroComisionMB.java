package com.ibm.bbva.ctacte.controller.form;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.DatosAdicionalesCargoComisionMB;
import com.ibm.bbva.ctacte.controller.comun.DatosExpediente2MB;
import com.ibm.bbva.ctacte.controller.comun.DatosExpedienteMB;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosAdicionalesCargoComision;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosExpediente;

@ManagedBean (name="gestionarCobroComision")
@ViewScoped
public class GestionarCobroComisionMB extends AbstractMBean 
	implements IDatosExpediente, IDatosAdicionalesCargoComision {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarNuevoBastanteoMB.class);
	
	private DatosExpedienteMB datosExpediente;
	private DatosAdicionalesCargoComisionMB datosAdicionales;

	public DatosExpedienteMB getDatosExpediente() {
		return datosExpediente;
	}
	public DatosAdicionalesCargoComisionMB getDatosAdicionales() {
		return datosAdicionales;
	}
	
	public void setDatosExpediente(DatosExpedienteMB datosExpediente) {
		this.datosExpediente = datosExpediente;
	}
	public void setDatosAdicionales(DatosAdicionalesCargoComisionMB datosAdicionales) {
		this.datosAdicionales = datosAdicionales;
	}
	public void setIDatosExpediente(DatosExpedienteMB datosexpediente) {
		this.setDatosExpediente(datosexpediente);
		
	}
	public void setIDatosExpediente2(DatosExpediente2MB datosexpediente2) {
	}
	public void setIDatosAdicionales(
			DatosAdicionalesCargoComisionMB datosAdcionalesMB) {
		this.setDatosAdicionales(datosAdcionalesMB);
		
	}

	
}
