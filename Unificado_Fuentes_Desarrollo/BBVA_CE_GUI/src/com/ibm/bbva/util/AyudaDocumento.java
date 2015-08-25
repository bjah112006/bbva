package com.ibm.bbva.util;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.controller.Constantes;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.GuiaDocumentaria;
import com.ibm.bbva.session.DocumentoExpTcBeanLocal;
//import com.ibm.bbva.tabla.util.vo.DocumentoReutilizable;

public class AyudaDocumento {

	private static final Logger LOG = LoggerFactory.getLogger(AyudaDocumento.class);
	
	private DocumentoExpTcBeanLocal documentoExpTcBean;

	public AyudaDocumento() {
		super();
		try {
			documentoExpTcBean = (DocumentoExpTcBeanLocal) new InitialContext()
					.lookup("ejblocal:com.ibm.bbva.session.DocumentoExpTcBeanLocal");
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void adjuntarDocumentoExpediente() {
	
		Expediente expediente = (Expediente) FacesContext.getCurrentInstance().
		getExternalContext().getSessionMap().get(Constantes.EXPEDIENTE_SESION);
				
		List<GuiaDocumentaria> listGuiaDocumentaria = (List<GuiaDocumentaria>) FacesContext.getCurrentInstance().
		getExternalContext().getSessionMap().get(Constantes.LISTA_DOC_EXP_ADJ);
		
		/*List<DocumentoReutilizable> listaDocumentoReutilizable = (List<DocumentoReutilizable>) FacesContext.getCurrentInstance().
		getExternalContext().getSessionMap().get(Constantes.LISTA_DOC_REUTILIZABLES);
		
		List<DocumentoExpTc> listDocExpAdjVO = documentoExpTcBean.buscarPorExpediente(expediente.getId());*/
		
		String strListaDocsTransferencias = (String) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("strListaDocsTransferencias");
		
		//for(DocumentoExpTc documentoExpTc : listDocExpAdjVO) {		
		    //documentoExpTcBean.remove(documentoExpTc);
		//}
		
		if (!(listGuiaDocumentaria == null || listGuiaDocumentaria.isEmpty())){
		    for (GuiaDocumentaria guia : listGuiaDocumentaria){
		    	LOG.info("Adjuntando Documento Expediente");
		    	LOG.info("Guía: " +  guia.getId());
		    	if (guia.getId() <= 0) continue; // estos son documentos que se adjuntaron en tareas anteriores y solo se visualizan, no son parte de la guía para la tarea
		    	
		    	List<DocumentoExpTc> lstDocumentoExpTc = documentoExpTcBean.consultarDocumentosExpediente(expediente.getId(), guia.getTipoDocumento().getCodigo());
		    	boolean canCreate = false;
		    	if (lstDocumentoExpTc != null) {
					for (DocumentoExpTc docExpTc: lstDocumentoExpTc) {						
						if (docExpTc.getTarea().getId() == expediente.getExpedienteTC().getTarea().getId()) {
							canCreate = false;
							break;
						} else {
							canCreate = true;
						}					
					}
				}else{
					canCreate = true;
				}
		    	LOG.info("canCreate: " +  canCreate);
		    	DocumentoExpTc documentoExpTc;
		    	if (canCreate) {
		    		documentoExpTc = new DocumentoExpTc();
				} else {
					continue; // evito crear registros repetidos
				}
		    	
		    	documentoExpTc.setNombre(guia.getTipoDocumento().getDescripcion());
		    	documentoExpTc.setTipoDoi(expediente.getClienteNatural().getTipoDoi());
		    	documentoExpTc.setNroDoi(expediente.getClienteNatural().getNumDoi());
		    	if(!expediente.getClienteNatural().getCodCliente().equalsIgnoreCase(Constantes.CODIGO_CLIENTE_NUEVO)){
		    		documentoExpTc.setCodCliente(expediente.getClienteNatural().getCodCliente());
				}
		    	LOG.info("getTipoDocumento: " +  guia.getTipoDocumento().getDescripcion());
		    	documentoExpTc.setTipoDocumento(guia.getTipoDocumento());
				documentoExpTc.setExpediente(expediente);
				documentoExpTc.setTarea(expediente.getExpedienteTC().getTarea());
				if (guia.getPersona() == null || guia.getPersona().getId() == 0) {
		    		documentoExpTc.setPersona(null); // vino como null en el query pero le setearon el titular
		    	} else {
		    		documentoExpTc.setPersona(guia.getPersona());
		    	}
				documentoExpTc.setObligatorio(guia.getObligatorio());
				//documentoExpTc.setTipoDoi(null);
				documentoExpTc.setFlagDocReutilizable(Constantes.FLAG_DOCUMENTO_REUTILIZABLE_CERO);
		    	/*for (DocumentoExpTc listadoc :listDocExpAdjVO) {
		    		if (listadoc.getTipoDocumento().getId() == guia.getTipoDocumento().getId()) {
		    			documentoExpTc.setFlagDocReutilizable(listadoc.getFlagDocReutilizable());
		    			break;
		    		}
		    	}
				
		    	if (listaDocumentoReutilizable != null) {
					for (DocumentoReutilizable documentoReutilizable : listaDocumentoReutilizable) {
						if (documentoReutilizable.getId() == guia.getTipoDocumento().getId()) {
							documentoExpTc.setFlagDocReutilizable(documentoReutilizable.isSeleccion() == true ? Constantes.FLAG_DOCUMENTO_REUTILIZABLE_UNO : Constantes.FLAG_DOCUMENTO_REUTILIZABLE_CERO);
					        break;
						}
					}
		    	}*/
		    	
		    	/* Se agregó el flag escaneado */
		    	documentoExpTc.setFlagEscaneado("0");
		    	LOG.info("strListaDocsTransferencias: "+strListaDocsTransferencias);
		    	if(strListaDocsTransferencias != null && !strListaDocsTransferencias.equals("null") && strListaDocsTransferencias.split(",").length > 0){
		    		for (String codTipoDoc : strListaDocsTransferencias.split(",")) {
						if (guia.getTipoDocumento().getCodigo().equals(codTipoDoc)) {
							documentoExpTc.setFlagEscaneado("1");
							break;
						}
					}
		    	}
		    	documentoExpTcBean.create(documentoExpTc);	
		    	LOG.info("DocumentoExpTc: " + documentoExpTc.getId());
		    }
		}
	}
	
	/**
	 * Metodo que permite actualizar el flag de escaneado cuando el expediente ha sido grabado previamente y ha grabado los flags de los documentos en cero
	 */
	public void editarDocumentoExpediente() {
		Expediente expediente = (Expediente) FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get(Constantes.EXPEDIENTE_SESION);
		
		String strListaDocsTransferencias = (String) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap()
				.get("strListaDocsTransferencias");
		
		if(strListaDocsTransferencias != null && !strListaDocsTransferencias.equals("null") && strListaDocsTransferencias.split(",").length > 0){
			List<DocumentoExpTc> lstDocumentoExpTc = documentoExpTcBean.buscarPorExpediente(expediente.getId());
			for(DocumentoExpTc documentoExpTc : lstDocumentoExpTc){
	    		for (String codTipoDoc : strListaDocsTransferencias.split(",")) {
					if (documentoExpTc.getTipoDocumento().getCodigo().equals(codTipoDoc)
							&& "0".equals(documentoExpTc.getFlagEscaneado())
							&& documentoExpTc.getTarea().getId() == expediente.getExpedienteTC().getTarea().getId()) {
						documentoExpTc.setFlagEscaneado("1");
						documentoExpTcBean.edit(documentoExpTc);	
						break;
					}
				}
			}
    	}
		
		List<DocumentoExpTc> listDocumentoExpTc = documentoExpTcBean.buscarPorExpediente(expediente.getId());
	    String lisDocumentosAdjuntos = "";
	    if (!(listDocumentoExpTc == null || listDocumentoExpTc.isEmpty())){
	    	for (DocumentoExpTc docExpTc : listDocumentoExpTc) {
	    		if (docExpTc.getTarea().getId() == expediente.getExpedienteTC().getTarea().getId()
	    				&& (docExpTc.getFlagCm()==null || !docExpTc.getFlagCm().equals("1"))) {
	    			lisDocumentosAdjuntos = lisDocumentosAdjuntos + docExpTc.getTipoDocumento().getCodigo() + ";" + docExpTc.getId() + "|";
	    		}
	    	}
	    	LOG.info("lisDocumentosAdjuntos: " + lisDocumentosAdjuntos);
	    	FacesContext.getCurrentInstance().getExternalContext()
    		.getSessionMap().put(Constantes.LISTA_DOCUMENTOS_ADJUNTOS, lisDocumentosAdjuntos);
	    }
	}
}