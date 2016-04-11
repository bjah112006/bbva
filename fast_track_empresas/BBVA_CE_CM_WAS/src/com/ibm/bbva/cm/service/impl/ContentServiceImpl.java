package com.ibm.bbva.cm.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.CalendarConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.bean.Documento;
import com.ibm.bbva.cm.bean.DocumentoPid;
import com.ibm.bbva.cm.dao.DocumentDAO;
import com.ibm.bbva.cm.dao.impl.DocumentDAOImpl;
import com.ibm.bbva.cm.exception.ICMDocumentNotFoundException;
import com.ibm.bbva.cm.exception.ICMException;
import com.ibm.bbva.cm.service.ContentService;
import com.ibm.bbva.cm.util.Util;

public class ContentServiceImpl extends BaseServiceImpl implements ContentService, Serializable {
	
	private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -6269869214114082482L;
	private DocumentDAO dao;	
	
	public ContentServiceImpl() {
		dao = new DocumentDAOImpl();
	}

	public String delete(Documento documento) {
		String retorno = "OK";
		try {
			dao.remove(Util.translateToDocPid(documento));
		} catch (ICMException e) {
			logger.error("Error delete()", e);
			retorno = "KO";
		}
		return retorno;
	}

	public String deleteAll(Documento[] documentos) {
		String retorno = "OK";
		try {			
			dao.remove(Arrays.asList(Util.translateToPid(documentos)));
		} catch (ICMException e) {
			logger.error("Error deleteAll()", e);
			retorno = "KO";
		}		
		return retorno;
	}

	public Documento find(Documento documento) {
		DocumentoPid document = null;
		
		try {
			logger.info("*********TRY-ContentServiceImpl---Documento find(Documento documento)"+documento);
			document = dao.retrieve(Util.translateToDocPid(documento));
			
			// hack para que el servicio web devuelva el contenido en null
			if (documento.getUrlContent() != null && documento.getUrlContent().equals("flagContenidoNull") && document != null) {
				document.setContenido(null);
			}
		} catch (ICMDocumentNotFoundException e) {
			logger.error("*********catch-ICMDocumentNotFoundException-ContentServiceImpl---Documento find(Documento documento)"+documento);
			logger.error("Error find()", e);
		} catch (ICMException e) {
			logger.error("*********catch-ICMEXCEPTION-ContentServiceImpl---Documento find(Documento documento)"+documento);
			logger.error("Error find()", e);
			throw e;
		}		
		
		return Util.translateToDoc(document);
	}
	
	public Documento[] findAll(Documento documento) {
		Documento[] document = null;
		try {
			List<DocumentoPid> listDocPid = dao.getDocuments(Util.translateToDocPid(documento));
			List<Documento> listDocumento = Util.translateToDocumentoList(listDocPid);
			Collections.sort(listDocumento, new Comparator<Documento>(){
				public int compare(Documento o1, Documento o2){
					return o1.getFechaCreacion().compareTo(o2.getFechaCreacion());
				}
			});
			document = listDocumento.toArray(new Documento[0]);
			
		} catch (ICMDocumentNotFoundException e) {
			logger.error("Error findAll()", e);
		} catch (ICMException e) {
			logger.error("Error findAll()", e);
			throw e;
		}		
		
		return document;
	}

	public Documento findAsImage(Documento documento, String mimeType) {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	public String insert(Documento documento) {
		String retorno = "OK";
		try {
			logger.info("*********TRY-ContentServiceImpl-insert(Documento documento)****"+documento);
			dao.store(Util.translateToDocPid(documento));
		} catch (ICMException e) {
			logger.error("*********CATCH-ContentServiceImpl-insert(Documento documento)****");
			logger.error("Error insert()", e);
			retorno = "KO";
		}
		return retorno;
	}
	
	public String insert_PID(Documento documento) {
		try {
			return dao.store_PID(Util.translateToDocPid(documento));
		} catch (ICMException e) {
			logger.error("Error insert_PID()", e);
			return null;
		}
	}

	public String insertAll(Documento[] documentos) {
		String retorno = "OK";
		try {
			dao.store(Arrays.asList(Util.translateToPid(documentos)));
		} catch (ICMException e) {
			logger.error("Error insertAll()", e);
			retorno = "KO";
		}
		return retorno;
	}

	public String update(Documento documento) {
		Documento docFiltro = new Documento();
		docFiltro.setId(documento.getId());
		docFiltro.setOrigen(documento.getOrigen());
		Documento docUpdate = find(docFiltro);
		String retorno = "KO";
		if(docUpdate != null){
			try {
				delete(docUpdate);
				ConvertUtils.register(new CalendarConverter(null), Calendar.class);
				//BeanUtils.copyProperties(docUpdate, documento);
				//Copiar Propiedades no Nulas	
				docUpdate.setCodCliente(getNotNull(documento.getCodCliente(), docUpdate.getCodCliente()));//
				docUpdate.setContenido(getNotNull(documento.getContenido(), docUpdate.getContenido()));//
				docUpdate.setFechaExpiracion(getNotNull(documento.getFechaExpiracion(), docUpdate.getFechaExpiracion()));//
				docUpdate.setId(getNotNull(documento.getId(), docUpdate.getId()));//
				docUpdate.setMandatorio(getNotNull(documento.getMandatorio(), docUpdate.getMandatorio()));//
				docUpdate.setNumDoi(getNotNull(documento.getNumDoi(), docUpdate.getNumDoi()));//
				docUpdate.setTipo(getNotNull(documento.getTipo(), docUpdate.getTipo()));//
				docUpdate.setTipoDoi(getNotNull(documento.getTipoDoi(), docUpdate.getTipoDoi()));//
				docUpdate.setNombreArchivo(getNotNull(documento.getNombreArchivo(), docUpdate.getNombreArchivo()));
				docUpdate.setOrigen(getNotNull(documento.getOrigen(), docUpdate.getOrigen()));
				retorno = insert_PID(docUpdate);
			} catch (Exception e) {				
				logger.error("Ocurrieron problemas en la actualizacion del documento", e);
				retorno = "KO";
			} 
			
		}else{
			logger.info("No se encontro el documento a actualizar");
			retorno = "KO";
		}		
		
		return retorno;
	}
	
	public String actualizarTipoDoc(Integer id , String tipoDoc){
		Documento doc = new Documento();
		doc.setId(id);
		doc.setTipo(tipoDoc);
		return update(doc);
	}
	
	private<T> T getNotNull(T obj1 , T obj2){
		return obj1==null?obj2:obj1;
	}
	
	 

}
