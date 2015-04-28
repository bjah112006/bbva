package com.ibm.bbva.ctacte.controller.form;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.interf.ICabeceraRevocatoria;
import com.ibm.bbva.ctacte.controller.comun.interf.IDigitaliceDocumentacion;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieVerificarRealizarBastanteo;
import com.ibm.bbva.ctacte.controller.comun.CabeceraRevocatoriaMB;
import com.ibm.bbva.ctacte.controller.comun.DigitaliceDocumentacionMB;
import com.ibm.bbva.ctacte.controller.comun.PieVerificarRealizarBastanteoMB;

@ManagedBean(name="verificarRealizarBastanteo")
@ViewScoped
public class VerificarRealizarBastanteoMB extends AbstractMBean 
	implements ICabeceraRevocatoria,IPieVerificarRealizarBastanteo, IDigitaliceDocumentacion{
	
	private static final long serialVersionUID = 7386098843178571669L;
	private static final Logger LOG = LoggerFactory.getLogger(VerificarRealizarBastanteoMB.class);
	
	private CabeceraRevocatoriaMB cabeceraRevocatoria;
	private PieVerificarRealizarBastanteoMB verificarRealizarBastanteo;
	private DigitaliceDocumentacionMB digitaliceDocumentacion;

	public void setICabeceraRevocatoria(
			CabeceraRevocatoriaMB cabeceraRevocatoria) {
		this.setICabeceraRevocatoria(cabeceraRevocatoria);		
	}

	public void setCabeceraRevocatoria(CabeceraRevocatoriaMB cabeceraRevocatoria) {
		this.cabeceraRevocatoria = cabeceraRevocatoria;
	}

	public CabeceraRevocatoriaMB getCabeceraRevocatoria() {
		return cabeceraRevocatoria;
	}	
	
	public void setVerificarRealizarBastanteo(PieVerificarRealizarBastanteoMB verificarRealizarBastanteo) {
		this.verificarRealizarBastanteo = verificarRealizarBastanteo;
	}

	public PieVerificarRealizarBastanteoMB getVerificarRealizarBastanteo() {
		return verificarRealizarBastanteo;
	}

	public boolean esValido() {
		return verificarRealizarBastanteo.esValido();
	}
	
	public boolean ingresarDictamen(){
		return verificarRealizarBastanteo.ingresarDictamen();
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

	public boolean siHayDevuelto() {
		return verificarRealizarBastanteo.siHayDevuelto();
	}

	public void habilitarBoton(boolean habReasignar) {
		
	}

}
