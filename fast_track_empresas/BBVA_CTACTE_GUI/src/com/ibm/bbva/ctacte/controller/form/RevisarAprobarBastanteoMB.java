package com.ibm.bbva.ctacte.controller.form;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.comun.CabeceraRevocatoriaMB;
import com.ibm.bbva.ctacte.controller.comun.DigitaliceDocumentacionMB;
import com.ibm.bbva.ctacte.controller.comun.PieAprobarBastanteoMB;

import com.ibm.bbva.ctacte.controller.comun.interf.ICabeceraRevocatoria;
import com.ibm.bbva.ctacte.controller.comun.interf.IDigitaliceDocumentacion;
import com.ibm.bbva.ctacte.controller.comun.interf.IPieAprobarBastanteo;


@ManagedBean(name="revisarAprobarBastanteo")
@ViewScoped
public class RevisarAprobarBastanteoMB extends AbstractMBean
		implements IPieAprobarBastanteo,IDigitaliceDocumentacion,
		ICabeceraRevocatoria{

	private static final long serialVersionUID = 7386098843178571669L;
	private static final Logger LOG = LoggerFactory.getLogger(RevisarAprobarBastanteoMB.class);
	
	
	private CabeceraRevocatoriaMB cabeceraRevocatoria;
	private DigitaliceDocumentacionMB digitaliceDocumentacion;
	private PieAprobarBastanteoMB pieAprobarBastanteo;
	private RevisarAprobarBastanteoMB revisarAprobarBastanteo;
	
	
	public PieAprobarBastanteoMB getPieAprobarBastanteo() {
		return pieAprobarBastanteo;
	}

	public void setPieAprobarBastanteo(PieAprobarBastanteoMB pieAprobarBastanteo) {
		this.pieAprobarBastanteo = pieAprobarBastanteo;
	}

	public CabeceraRevocatoriaMB getCabeceraRevocatoria() {
		return cabeceraRevocatoria;
	}

	public void setCabeceraRevocatoria(CabeceraRevocatoriaMB cabeceraRevocatoria) {
		this.cabeceraRevocatoria = cabeceraRevocatoria;
	}

	

	public void setICabeceraRevocatoria(
			CabeceraRevocatoriaMB cabeceraRevocatoria) {
		this.cabeceraRevocatoria=cabeceraRevocatoria;
		
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

	public RevisarAprobarBastanteoMB getRevisarAprobarBastanteo() {
		return revisarAprobarBastanteo;
	}

	public void setRevisarAprobarBastanteo(
			RevisarAprobarBastanteoMB revisarAprobarBastanteo) {
		this.revisarAprobarBastanteo = revisarAprobarBastanteo;
	}

	public boolean noSeleccionados() {
		return pieAprobarBastanteo.noSeleccionados();
	}

	public void habilitarBoton(boolean habReasignar) {
	}

	

	
	
}
