package com.ibm.bbva.ctacte.controller.comun;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.util.Util;

@ManagedBean(name = "envioFTP")
@SessionScoped
public class EnvioFTPMB extends AbstractMBean {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(EnvioFTPMB.class);
	
	private String numeroExpediente;
	private boolean finalizado;
	private boolean abrirVentana;
	private String convertirTIF;
	private String codConv;
	
	public void iniciar () {
		finalizado = false;
		abrirVentana = false;
		codConv = null;
	}

	public void subirArchivos(boolean subirArchivos, String convertirTIF, boolean crearDocumentos) {
		LOG.info("subirArchivos(boolean subirArchivos, String convertirTIF, boolean crearDocumentos) {}, {}, {}", 
				new Object[]{subirArchivos, convertirTIF, crearDocumentos});
		abrirVentana = subirArchivos && !finalizado;
		LOG.info("finalizado : {}", finalizado);
		if (abrirVentana) {
			if (subirArchivos) {
				Expediente expediente = (Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);		
				numeroExpediente = String.valueOf(expediente.getId());
				this.convertirTIF = convertirTIF;
				if (crearDocumentos) {
					byte[] insBast = expediente.getInstruccionesBastanteo();
					LOG.info("insBast : {}", insBast!=null);
					codConv = "";
					if (insBast!=null && insBast.length>0) {
						codConv = ConstantesBusiness.CODIGO_DOCUMENTO_INSTRUCCIONES;
						Util.insertarDocumentoExp(ConstantesBusiness.CODIGO_DOCUMENTO_INSTRUCCIONES);
					}
					byte[] dicBast = expediente.getDictamenBastanteo();
					LOG.info("dicBast : {}", dicBast!=null);
					if (dicBast!=null && dicBast.length>0) {
						if (codConv!=null) {
							codConv += ";";
						}
						codConv += ConstantesBusiness.CODIGO_DOCUMENTO_DICTAMEN;
						Util.insertarDocumentoExp(ConstantesBusiness.CODIGO_DOCUMENTO_DICTAMEN);
					}
					LOG.info("codConv : {}", codConv);
				}
			} else {
				this.convertirTIF = null;
			}
		}
	}
	
	public void finalizado (ActionEvent event) {
		LOG.info("finalizado (ActionEvent event)");
		finalizado = true;
		abrirVentana = false;
		codConv = null;
	}

	public void setConvertirTIF(String convertirTIF) {
		this.convertirTIF = convertirTIF;
	}

	public String getConvertirTIF() {
		return convertirTIF;
	}

	public String getNumeroExpediente() {
		return numeroExpediente;
	}

	public void setNumeroExpediente(String numeroExpediente) {
		this.numeroExpediente = numeroExpediente;
	}

	public void setAbrirVentana(boolean abrirVentana) {
		this.abrirVentana = abrirVentana;
	}

	public boolean isAbrirVentana() {
		return abrirVentana;
	}

	public void setCodConv(String codConv) {
		this.codConv = codConv;
	}

	public String getCodConv() {
		return codConv;
	}
	
}
