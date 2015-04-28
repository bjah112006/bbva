package com.ibm.bbva.ctacte.controller.form;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.CabeceraRevocatoriaMB;
import com.ibm.bbva.ctacte.controller.comun.DigitaliceDocumentacionMB;
import com.ibm.bbva.ctacte.controller.comun.PieEjecutarRevocatoriaMB;
import com.ibm.bbva.ctacte.controller.comun.RevisarDigitaliceDocumentacionMB;

import com.ibm.bbva.ctacte.controller.comun.interf.ICabeceraRevocatoria;
import com.ibm.bbva.ctacte.controller.comun.interf.IDigitaliceDocumentacion;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieEjecutarRevocatoria;
import com.ibm.bbva.ctacte.controller.comun.interf.IRevisarDigitaliceDocumentacion;


@ManagedBean(name="revisarEjecutarRevocatoria")
@ViewScoped
public class RevisarEjecutarRevocatoriaMB extends AbstractMBean
   implements ICabeceraRevocatoria,IPieEjecutarRevocatoria, 
             IDigitaliceDocumentacion, IRevisarDigitaliceDocumentacion
   				{
	
	private static final long serialVersionUID = 7386098843178571669L;
	private static final Logger LOG = LoggerFactory.getLogger(RevisarEjecutarRevocatoriaMB.class);
	
	private CabeceraRevocatoriaMB cabeceraRevocatoria;
	
	private PieEjecutarRevocatoriaMB pieEjecutarRevocatoria;	
	private DigitaliceDocumentacionMB digitaliceDocumentacion;
	// agregado
	private RevisarDigitaliceDocumentacionMB revisarDigitaliceDocumentacion;
	

	public void setCabeceraRevocatoria(CabeceraRevocatoriaMB cabeceraRevocatoria) {
		this.cabeceraRevocatoria = cabeceraRevocatoria;
	}

	public CabeceraRevocatoriaMB getCabeceraRevocatoria() {
		return cabeceraRevocatoria;
	}

	public void setICabeceraRevocatoria(
			CabeceraRevocatoriaMB cabeceraRevocatoria) {
		this.setCabeceraRevocatoria(cabeceraRevocatoria);		
	}

	public void setIPieEjecutarRevocatoria(
			PieEjecutarRevocatoriaMB pieEjecutarRevocatoria) {
		this.setPieEjecutarRevocatoria(pieEjecutarRevocatoria);		
	}

	public void setPieEjecutarRevocatoria(PieEjecutarRevocatoriaMB pieEjecutarRevocatoria) {
		this.pieEjecutarRevocatoria = pieEjecutarRevocatoria;
	}

	public PieEjecutarRevocatoriaMB getPieEjecutarRevocatoria() {
		return pieEjecutarRevocatoria;
	}

	public DigitaliceDocumentacionMB getDigitaliceDocumentacion() {
		return digitaliceDocumentacion;
	}

	public void setDigitaliceDocumentacion(
			DigitaliceDocumentacionMB digitaliceDocumentacion) {
		this.digitaliceDocumentacion = digitaliceDocumentacion;
	}

	public void actualizarSubidaDocumentos(Character flagDocumentos) {
		
	}

	public RevisarDigitaliceDocumentacionMB getRevisarDigitaliceDocumentacion() {
		return revisarDigitaliceDocumentacion;
	}

	public void setRevisarDigitaliceDocumentacion(
			RevisarDigitaliceDocumentacionMB revisarDigitaliceDocumentacion) {
		this.revisarDigitaliceDocumentacion = revisarDigitaliceDocumentacion;
	}

	public void habilitarBoton(boolean habReasignar) {
	}

	
}
