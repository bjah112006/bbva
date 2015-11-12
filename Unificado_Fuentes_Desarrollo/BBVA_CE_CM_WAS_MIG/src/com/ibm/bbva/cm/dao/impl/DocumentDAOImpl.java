package com.ibm.bbva.cm.dao.impl;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.bean.Documento;
import com.ibm.bbva.cm.bean.DocumentoPid;
import com.ibm.bbva.cm.dao.DocumentDAO;
import com.ibm.bbva.cm.support.impl.ICMDocument;

public class DocumentDAOImpl extends BaseDAOImpl implements DocumentDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentDAOImpl.class);

	public DocumentDAOImpl() {
		super();

		System.out.println("itemType :"+getItemType());
		System.out.println("mimeType :"+getMimeType());
		System.out.println("mimeType_tiff :"+getMimeType_Tiff());
		
	}
	
	public DocumentoPid retrieve(DocumentoPid filter) {
		System.out.println(filter);
		ICMDocument icmFilter = buildICMDocument(filter);
		ICMDocument icmDocument = getIcmTemplate().retrieve(icmFilter);
	
		return (DocumentoPid)getTranslator().translateToDomain(icmDocument);
	}
	
	public List<DocumentoPid> getDocuments(DocumentoPid filter) {
		ICMDocument icmFilter = buildICMDocument(filter);
		List <ICMDocument> icmDocument = getIcmTemplate().getDocuments(icmFilter);
		List<DocumentoPid> documento = new ArrayList<DocumentoPid>();
		ICMDocument icmDocumentU = new ICMDocument(); 
		for (int i=0;i<icmDocument.size();i++){
			icmDocumentU = icmDocument.get(i);
			documento.add((DocumentoPid)getTranslator().translateToDomain(icmDocumentU));
		}
		return documento;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.cao.DocumentCao#store(com.bbvabancocontinental.vv.domain.Document)
	 */
	public void store(DocumentoPid document) {
		ICMDocument icmDocument = buildICMDocument(document);
		icmDocument.setContent(document.getContenido());
		getIcmTemplate().store(icmDocument);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.cao.DocumentCao#store(com.bbvabancocontinental.vv.domain.Document)
	 */
	public String store_PID(DocumentoPid document) {
		ICMDocument icmDocument = buildICMDocument(document);
		icmDocument.setContent(document.getContenido());
		return getIcmTemplate().store_PID(icmDocument);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.cao.DocumentCao#store(java.util.List)
	 */
	public void store(List<DocumentoPid> documents) {
		List<ICMDocument> icmDocuments = new ArrayList<ICMDocument>();
		for (DocumentoPid document : documents) {
			ICMDocument icmDocument = buildICMDocument(document);
			icmDocument.setContent(document.getContenido());
			icmDocuments.add(icmDocument);
		}

		getIcmTemplate().store(icmDocuments);
	}

	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.cao.DocumentCao#remove(com.bbvabancocontinental.vv.domain.Document)
	 */
	public void remove(DocumentoPid document) {
		ICMDocument icmDocument = buildICMDocument(document);
		getIcmTemplate().remove(icmDocument);
	}	
	
	/*
	 * (non-Javadoc)
	 * @see com.bbvabancocontinental.vv.cao.DocumentCao#remove(java.util.List)
	 */
	public void remove(List<DocumentoPid> documents) {
		List<ICMDocument> icmDocuments = new ArrayList<ICMDocument>();

		for (DocumentoPid document : documents) {
			ICMDocument icmDocument = buildICMDocument(document);
			icmDocuments.add(icmDocument);
		}

		getIcmTemplate().remove(icmDocuments);
	}

	/**
	 * Construye un documento ICM en base a un documento.
	 * 
	 * @param documento
	 *            documento
	 * @return documento ICM
	 */
	private ICMDocument buildICMDocument(DocumentoPid document) {
		
		String strTipo = "";
		logger.debug("buildICMDocument: strTipo : "+strTipo);
		if (document.getTipo()!=null){
			strTipo = document.getTipo();
			if (strTipo.indexOf(".")>0){
				if (strTipo.toUpperCase().indexOf("TIF")>0)
					document.setTipo(strTipo);
				else
					document.setTipo(strTipo.substring(0, strTipo.indexOf(".")));
			}
		}
		logger.debug("buildICMDocument: document.getTipo() : "+document.getTipo());
		strTipo = strTipo.toUpperCase();
		ICMDocument icmDocument = getTranslator().translateToICMDocument(document);
		icmDocument.setItemType(getItemType());
		icmDocument.setMimeType(getMimeType());
		
		if (strTipo.indexOf("TIF")>0){
			icmDocument.setMimeType(getMimeType_Tiff());
		}
		
		logger.debug("buildICMDocument: icmDocument.getMimeType() : "+icmDocument.getMimeType());
		return icmDocument;
	}

	public Documento findAsImage(Documento documento, String mimeType) {
		// TODO Apéndice de método generado automáticamente
		return null;
	}
}
