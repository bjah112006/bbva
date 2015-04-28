package com.ibm.bbva.ctacte.controller.comun;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.controller.AbstractMBean;
import com.ibm.bbva.ctacte.controller.ConstantesAdmin;
import com.ibm.bbva.ctacte.controller.comun.interf.ISubsanarDigitaliceDocumentacion;
import com.ibm.bbva.ctacte.controller.form.SubsanarDocumentoMB;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.util.Util;



@ManagedBean(name="subsanarDigitaliceDocumentacion")
@ViewScoped
public class SubsanarDigitaliceDocumentacionMB extends AbstractMBean{
	
	private static final long serialVersionUID = -6399120168925416691L;
	private static final Logger LOG = LoggerFactory.getLogger(SubsanarDigitaliceDocumentacionMB.class);
	
	@ManagedProperty(value="#{subsanarDocumento}")
	private SubsanarDocumentoMB subsanarDocumento;
	private ISubsanarDigitaliceDocumentacion isubsanarDigitaliceDocumentacion;
	private boolean mostrar;
	//private StreamedContent archivo; // TODO migrar
	private DocumentoExp documentoExp;
	private String tipoDocumento;
	
	private List<DocumentoExp> listaDocExpediente;	
	
	@EJB
	private DocumentoExpDAO documentoExpDAO;
	
	@PostConstruct
	public void iniciar(){						
		LOG.info("iniciar()");
		Expediente expediente=(Expediente) Util.getObjectSession(ConstantesAdmin.EXPEDIENTE_SESION);
		LOG.info("expediente.getID() = "+expediente.getId());
		listaDocExpediente=documentoExpDAO.findByExpdiente(expediente.getId());
		LOG.info("lista tamaño"+listaDocExpediente.size());
		
		//seteando el nombre del tipo de documento
		String tipoDocumento=documentoExp.getDocumento().getTipoDocumento();
		setTipoDocumento(tipoDocumento);
		
		//validar que el documento sea obligatorio
		
		
		
		String pagina=getNombrePrincipal();
		LOG.info("Pagina actual {}",pagina);
		if ("formSubsanarDocumentos".equals(pagina)){
			LOG.info("entro al if");
			//isubsanarDigitaliceDocumentacion=subsanarDocumento;
		}				
				
	}		
	
	public void escanear(ActionEvent ae){
		LOG.info("escanear(ActionEvent ae)");	
		//implementar
		isubsanarDigitaliceDocumentacion.escanear();
	}
	
	public void actualizar(ActionEvent ae){
		LOG.info("actualizar(ActionEvent ae)");
		//implementar
		isubsanarDigitaliceDocumentacion.actualizar();
	}


	public void setSubsanarDocumento(SubsanarDocumentoMB subsanarDocumento) {
		this.subsanarDocumento = subsanarDocumento;
	}

	public SubsanarDocumentoMB getSubsanarDocumento() {
		return subsanarDocumento;
	}

	public void setIsubsanarDigitaliceDocumentacion(
			ISubsanarDigitaliceDocumentacion isubsanarDigitaliceDocumentacion) {
		this.isubsanarDigitaliceDocumentacion = isubsanarDigitaliceDocumentacion;
	}

	public ISubsanarDigitaliceDocumentacion getIsubsanarDigitaliceDocumentacion() {
		return isubsanarDigitaliceDocumentacion;
	}

	public void setMostrar(boolean mostrar) {
		this.mostrar = mostrar;
	}

	public boolean isMostrar() {
		return mostrar;
	}

	// TODO migrar
	/*public void setArchivo(StreamedContent archivo) {
		this.archivo = archivo;
	}

	public StreamedContent getArchivo() {
		return archivo;
	}*/

	public void setDocumentoExp(DocumentoExp documentoExp) {
		this.documentoExp = documentoExp;
	}

	public DocumentoExp getDocumentoExp() {
		return documentoExp;
	}

	public void setListaDocExpediente(List<DocumentoExp> listaDocExpediente) {
		this.listaDocExpediente = listaDocExpediente;
	}

	public List<DocumentoExp> getListaDocExpediente() {
		return listaDocExpediente;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	
	

}
