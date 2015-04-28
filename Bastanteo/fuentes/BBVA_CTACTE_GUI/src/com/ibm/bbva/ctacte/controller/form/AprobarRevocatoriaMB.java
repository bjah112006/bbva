package com.ibm.bbva.ctacte.controller.form;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.interf.ICabeceraRevocatoria;
import com.ibm.bbva.ctacte.controller.comun.interf.IDigitaliceDocumentacion;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieAprobarRevocatoria;
import com.ibm.bbva.ctacte.controller.comun.CabeceraRevocatoriaMB;
import com.ibm.bbva.ctacte.controller.comun.DigitaliceDocumentacionMB;
import com.ibm.bbva.ctacte.controller.comun.PieAprobarRevocatoriaMB;



@ManagedBean (name="aprobarRevocatoria")
@ViewScoped
public class AprobarRevocatoriaMB extends AbstractMBean
			implements ICabeceraRevocatoria, IDigitaliceDocumentacion,IPieAprobarRevocatoria{
	
	private static final long serialVersionUID = 7386098843178571669L;
	private static final Logger LOG = LoggerFactory.getLogger(AprobarRevocatoriaMB.class);
	
	//private DatosRevocatoriaMB datosRevocatoria;
	private CabeceraRevocatoriaMB cabeceraRevocatoria;	
	private DigitaliceDocumentacionMB digitaliceDocumentacion;
	private PieAprobarRevocatoriaMB pieAprobarRevocatoria;

	
	public void setIPieAprobarRevocatoria(PieAprobarRevocatoriaMB pieAprobar) {
		this.setPieAprobarRevocatoria(pieAprobar);		
	}	
	
	public void setPieAprobarRevocatoria(PieAprobarRevocatoriaMB pieAprobarRevocatoria) {
		this.pieAprobarRevocatoria = pieAprobarRevocatoria;
	}

	public PieAprobarRevocatoriaMB getPieAprobarRevocatoria() {
		return pieAprobarRevocatoria;
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

	public void setICabeceraRevocatoria(
			CabeceraRevocatoriaMB cabeceraRevocatoria) {
	}

	public boolean siHayResultado() {
		LOG.info("esValido()");
		return pieAprobarRevocatoria.siHayResultado();		
	}
	
	public boolean siHayObservaciones(){
		LOG.info("siHayObservaciones");
		return pieAprobarRevocatoria.siHayObservaciones();
	}

	public boolean esValido() {
		return pieAprobarRevocatoria.esValido();
	}

	public CabeceraRevocatoriaMB getCabeceraRevocatoria() {
		return cabeceraRevocatoria;
	}

	public void setCabeceraRevocatoria(CabeceraRevocatoriaMB cabeceraRevocatoria) {
		this.cabeceraRevocatoria = cabeceraRevocatoria;
	}

	public void habilitarBoton(boolean habReasignar) {
	}

	
}
