package com.ibm.bbva.ctacte.controller.comun;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Cliente;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.business.ClienteBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.IDatosCliente;
import com.ibm.bbva.ctacte.controller.form.AyudaMemoriaMB;
import com.ibm.bbva.ctacte.controller.form.HistorialExpedienteMB;
import com.ibm.bbva.ctacte.controller.form.LogExpedienteMB;
import com.ibm.bbva.ctacte.controller.form.ObservacionesMB;
import com.ibm.bbva.ctacte.controller.form.VerificarCalidadDocumentoMB;
import com.ibm.bbva.ctacte.util.Util;

//@001A 11092015 Mostrar flag de migracion.
@ManagedBean (name="datosCliente")
@ViewScoped
public class DatosClienteMB extends AbstractMBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DatosClienteMB.class);

	@ManagedProperty (value="#{verificarCalidadDocumentos}")	
	private VerificarCalidadDocumentoMB verificarCalidadDocumentosMB;
	@ManagedProperty (value="#{ayudaMemoria}")
	private AyudaMemoriaMB ayudaMemoriaMB;
	@ManagedProperty (value="#{logExpediente}")
	private LogExpedienteMB logExpedienteMB;
	@ManagedProperty (value="#{historialExpediente}")
	private HistorialExpedienteMB historialExpedienteMB;
	@ManagedProperty (value="#{observacionesExp}")
	private ObservacionesMB observacionesMB;
	
	private IDatosCliente managedBean;
	private Expediente expediente;
	private Cliente cliente;
	private String tipoPJ;
	private String esMigrado;
	@EJB
	private ClienteBusiness buscarCliente;
	
	public String getTipoPJ() {
		return tipoPJ;
	}

	public void setTipoPJ(String tipoPJ) {
		this.tipoPJ = tipoPJ;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	
	
	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		int idCliente = 0;
		String tipo_pj = "";		
		if (!(expediente == null || expediente.getId() == null)) {
		   idCliente = expediente.getCliente().getId();
		   tipo_pj = expediente.getDesTipoPj();
		}
		setTipoPJ(tipo_pj);
		
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("idcliente-->"+idCliente);
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("tipo_pj-->"+tipo_pj);
		String pagina = getNombrePrincipal();
		LOG.info("Pagina actual {}", pagina);
		
		Cliente c = buscarCliente.getDatosCliente(idCliente);
		if (c!=null) {
		   setCliente(c);
		   //@001A05L
		   try {
				esMigrado = (!c.getFlagOrigenSFP().equalsIgnoreCase("1")) ? "No" : "Si"; 
			} catch (Exception e) {
				LOG.info("Error al verificar si el cliente es migrado", e);
			}
		}
		
		if (ConstantesAdmin.FORM_VERIFICAR_CALIDAD_DOCUMENTOS.contains(pagina)) {
			managedBean = verificarCalidadDocumentosMB;
		}else if (ConstantesAdmin.FORM_AYUDA_MEMORIA.contains(pagina)) {
			managedBean = ayudaMemoriaMB;
		}else if (ConstantesAdmin.FORM_HISTORIAL_EXPEDIENTE.contains(pagina)) {
			managedBean = historialExpedienteMB;
			
		}else if (ConstantesAdmin.FORM_LOG.contains(pagina)) {
			managedBean = logExpedienteMB;
			
		}else if (ConstantesAdmin.FORM_OBSERVACIONES_EXPEDIENTE.contains(pagina)) {
			managedBean = observacionesMB;
			
		}	
		//managedBean.setDatosCliente(this);
	}

	public VerificarCalidadDocumentoMB getVerificarCalidadDocumentosMB() {
		return verificarCalidadDocumentosMB;
	}

	public void setVerificarCalidadDocumentosMB(
			VerificarCalidadDocumentoMB verificarCalidadDocumentosMB) {
		this.verificarCalidadDocumentosMB = verificarCalidadDocumentosMB;
	}

	public AyudaMemoriaMB getAyudaMemoriaMB() {
		return ayudaMemoriaMB;
	}

	public void setAyudaMemoriaMB(AyudaMemoriaMB ayudaMemoriaMB) {
		this.ayudaMemoriaMB = ayudaMemoriaMB;
	}

	public LogExpedienteMB getLogExpedienteMB() {
		return logExpedienteMB;
	}

	public void setLogExpedienteMB(LogExpedienteMB logExpedienteMB) {
		this.logExpedienteMB = logExpedienteMB;
	}

	public HistorialExpedienteMB getHistorialExpedienteMB() {
		return historialExpedienteMB;
	}

	public void setHistorialExpedienteMB(HistorialExpedienteMB historialExpedienteMB) {
		this.historialExpedienteMB = historialExpedienteMB;
	}

	public ObservacionesMB getObservacionesMB() {
		return observacionesMB;
	}

	public void setObservacionesMB(ObservacionesMB observacionesMB) {
		this.observacionesMB = observacionesMB;
	}

	public String getEsMigrado() {
		return esMigrado;
	}

	public void setEsMigrado(String esMigrado) {
		this.esMigrado = esMigrado;
	}


}
