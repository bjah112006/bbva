package com.ibm.bbva.ctacte.controller.form;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosExpediente;
import com.ibm.bbva.ctacte.controller.comun.interf.IParticipeVerificarResultado;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieVerificarResultado;
import com.ibm.bbva.ctacte.controller.comun.interf.IVisorDocumentos;
import com.ibm.bbva.ctacte.controller.comun.DatosExpediente2MB;
import com.ibm.bbva.ctacte.controller.comun.DatosExpedienteMB;
import com.ibm.bbva.ctacte.controller.comun.ParticipeVerificarResultadoMB;
import com.ibm.bbva.ctacte.controller.comun.PieVerificarResultadoMB;
import com.ibm.bbva.ctacte.controller.comun.VisorDocumentosMB;

@ManagedBean (name="verificarResultadoTramite")
@ViewScoped
public class VerificarResultadoTramiteMB extends AbstractMBean
         implements IDatosExpediente,IParticipeVerificarResultado, IVisorDocumentos,IPieVerificarResultado{
	
	
	private static final long serialVersionUID = 7386098843178571669L;
	private static final Logger LOG = LoggerFactory.getLogger(VerificarResultadoTramiteMB.class);

	private DatosExpedienteMB datosExpediente;
	private DatosExpediente2MB datosExpediente2;
	private ParticipeVerificarResultadoMB participeVerificarResultado;
	private VisorDocumentosMB visorDocumentos;
	private PieVerificarResultadoMB pieVerificarResultado;
	
	public void setIDatosExpediente(DatosExpedienteMB datosexpediente) {
		this.setDatosExpediente(datosexpediente);
		
	}

	public void setDatosExpediente(DatosExpedienteMB datosExpediente) {
		this.datosExpediente = datosExpediente;
	}

	public DatosExpedienteMB getDatosExpediente() {
		return datosExpediente;
	}

	public void setDatosExpediente2(DatosExpediente2MB datosExpediente2) {
		this.datosExpediente2 = datosExpediente2;
	}

	public DatosExpediente2MB getDatosExpediente2() {
		return datosExpediente2;
	}
	
	public static Logger getLOG() {
		return LOG;
	}

	public void setIDatosExpediente2(DatosExpediente2MB datosexpediente2) {
		this.setDatosExpediente2(datosexpediente2);
		
	}

	public void setParticipeVerificarResultado(
			ParticipeVerificarResultadoMB participeVerificarResultado) {
		this.participeVerificarResultado = participeVerificarResultado;
	}

	public ParticipeVerificarResultadoMB getParticipeVerificarResultado() {
		return participeVerificarResultado;
	}

	public void setIParticipeVerificarResultado(
			ParticipeVerificarResultadoMB participeVerificarResultado) {
		this.setParticipeVerificarResultado(participeVerificarResultado);
		
	}

	public void setPieVerificarResultado(PieVerificarResultadoMB pieVerificarResultado) {
		this.pieVerificarResultado = pieVerificarResultado;
	}

	public PieVerificarResultadoMB getPieVerificarResultado() {
		return pieVerificarResultado;
	}

	public void setIPieVerificarResultado(
			PieVerificarResultadoMB pieVerificarResultado) {
		this.setIPieVerificarResultado(pieVerificarResultado);
		
	}

	public VisorDocumentosMB getVisorDocumentos() {
		return visorDocumentos;
	}

	public void setVisorDocumentos(VisorDocumentosMB visorDocumentos) {
		this.visorDocumentos = visorDocumentos;
	}


	

	
	

}
