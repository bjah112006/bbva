package com.ibm.bbva.ctacte.controller.comun;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean (name="documentoIdentidad")
@ViewScoped
public class DocumentoIdentidadMB extends AbstractMBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1868142892640879991L;

	private static final Logger LOG = LoggerFactory.getLogger(DocumentoIdentidadMB.class);
	
	private String codigoDocumento;
	private boolean iniciarApplet;
	private String tramaDescarga;
	private Integer idExpediente;
	
	@EJB
	private DocumentoExpDAO docExpDAO;
	
	@PostConstruct
	public void iniciar () {
		LOG.info("iniciar ()");
		codigoDocumento = "DOID0";
		iniciarApplet = true;
		crearTrama ();
	}
	
	public void crearTrama () {
		LOG.info("crearTrama ()");
		Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		idExpediente = expediente.getId();
		LOG.info("expediente.getId()-->"+expediente.getId());
		List<DocumentoExp> listaDocExpediente = docExpDAO.findByExpdiente(expediente.getId());
		
		StringBuilder descarga = new StringBuilder ();
		boolean esPrimeroD = true;
		for (DocumentoExp de : listaDocExpediente) {
			if (ConstantesBusiness.FLAG_ESCANEADO.equals(de.getFlagEscaneado())) {
				if (!esPrimeroD){
					descarga.append(";");
				}
				descarga.append(de.getDocumento().getCodigoDocumento());
				esPrimeroD = false;
			}
		}
		tramaDescarga = descarga.toString();
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public void setIniciarApplet(boolean iniciarApplet) {
		this.iniciarApplet = iniciarApplet;
	}

	public boolean isIniciarApplet() {
		return iniciarApplet;
	}

	public void setTramaDescarga(String tramaDescarga) {
		this.tramaDescarga = tramaDescarga;
	}

	public String getTramaDescarga() {
		return tramaDescarga;
	}

	public void setIdExpediente(Integer idExpediente) {
		this.idExpediente = idExpediente;
	}

	public Integer getIdExpediente() {
		return idExpediente;
	}

}
