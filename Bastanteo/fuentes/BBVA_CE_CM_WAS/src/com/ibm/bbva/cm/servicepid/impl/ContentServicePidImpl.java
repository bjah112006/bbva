package com.ibm.bbva.cm.servicepid.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.bean.DocumentoPid;
import com.ibm.bbva.cm.dao.DocumentDAO;
import com.ibm.bbva.cm.dao.impl.DocumentDAOImpl;
import com.ibm.bbva.cm.exception.ICMDocumentNotFoundException;
import com.ibm.bbva.cm.exception.ICMException;
import com.ibm.bbva.cm.service.impl.BaseServiceImpl;
import com.ibm.bbva.cm.servicepid.ContentServicePid;

public class ContentServicePidImpl extends BaseServiceImpl implements
		ContentServicePid, Serializable {

	private static final Logger logger = LoggerFactory.getLogger(ContentServicePidImpl.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DocumentDAO dao;	
	
	public ContentServicePidImpl() {
		dao = new DocumentDAOImpl();
	}
	
	public DocumentoPid[] findAll(DocumentoPid documento) {
		DocumentoPid[] document = null;
		try {
			List<DocumentoPid> listDocPid = dao.getDocuments(documento);
			Collections.sort(listDocPid, new Comparator<DocumentoPid>(){
				public int compare(DocumentoPid o1, DocumentoPid o2){
					return o1.getFechaCreacion().compareTo(o2.getFechaCreacion());
				}
			});
			document = listDocPid.toArray(new DocumentoPid[0]);
			
		} catch (ICMDocumentNotFoundException e) {
			logger.info("Error findAll()", e);
		} catch (ICMException e) {
			logger.error("Error findAll()", e);
			throw e;
		}		
		
		return document;
	}

}
