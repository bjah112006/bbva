package com.ibm.bbva.ctacte.controller.form;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.DatosExpediente2MB;
import com.ibm.bbva.ctacte.controller.comun.DatosExpedienteMB;
import com.ibm.bbva.ctacte.controller.comun.PieEjecutarConfirmarModificatoriasMB;
import com.ibm.bbva.ctacte.controller.comun.VisorInstruccionesMB;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosExpediente;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieEjecutarConfirmarModificatorias;
import com.ibm.bbva.ctacte.controller.comun.interf.IVisorInstrucciones;

@ManagedBean (name="ejecutarConfirmarModificatoria")
@ViewScoped
public class EjecutarConfirmarModificatoriaMB extends AbstractMBean
                 implements IDatosExpediente, IVisorInstrucciones, IPieEjecutarConfirmarModificatorias{
	
	private static final long serialVersionUID = 7386098843178571669L;
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarNuevoBastanteoMB.class);
	
	private DatosExpedienteMB datosExpediente;
	private DatosExpediente2MB datosExpediente2;
	private PieEjecutarConfirmarModificatoriasMB pieEjecutar;
	private VisorInstruccionesMB visorInstrucciones;

	public DatosExpediente2MB getDatosExpediente2() {
		return datosExpediente2;
	}

	public void setDatosExpediente2(DatosExpediente2MB datosExpediente2) {
		this.datosExpediente2 = datosExpediente2;
	}

	public VisorInstruccionesMB getVisorInstrucciones() {
		return visorInstrucciones;
	}

	public void setVisorInstrucciones(VisorInstruccionesMB visorInstrucciones) {
		this.visorInstrucciones = visorInstrucciones;
	}

	public void setIDatosExpediente(DatosExpedienteMB datosExpediente) {
		this.setDatosExpediente(datosExpediente);	
	}

	public void setIPieEjecutarConfirmarModificatorias(
			PieEjecutarConfirmarModificatoriasMB pieEjecutar) {
		this.setPieEjecutar(pieEjecutar);		
	}

	public void setDatosExpediente(DatosExpedienteMB datosExpediente) {
		this.datosExpediente = datosExpediente;
	}

	public DatosExpedienteMB getDatosExpediente() {
		return datosExpediente;
	}

	public void setPieEjecutar(PieEjecutarConfirmarModificatoriasMB pieEjecutar) {
		this.pieEjecutar = pieEjecutar;
	}

	public PieEjecutarConfirmarModificatoriasMB getPieEjecutar() {
		return pieEjecutar;
	}

	public void setIDatosExpediente2(DatosExpediente2MB datosexpediente2) {
		this.datosExpediente2=datosexpediente2;		
	}

	
	
}
