package com.ibm.bbva.ctacte.business.impl;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Documento;
import com.ibm.bbva.ctacte.business.BuscarDocumentoBusiness;
import com.ibm.bbva.ctacte.dao.DocumentoDAO;

/**
 * Session Bean implementation class BuscarDocumentoBusiness
 */
@Stateless
@Local(BuscarDocumentoBusiness.class)
public class BuscarDocumentoBusinessImpl implements BuscarDocumentoBusiness {
	
	private static final Logger LOG=LoggerFactory.getLogger(BuscarDocumentoBusinessImpl.class);
	
	@EJB
	private DocumentoDAO documentoDAO;

    /**
     * Default constructor. 
     */
    public BuscarDocumentoBusinessImpl() {
    }

	@Override
	public Documento buscarDocumentobyID(int idDoc) {
		LOG.info("buscarDocumentobyID(int idDoc)");
				
		Documento documentoBEAN=documentoDAO.load(idDoc);		
		return documentoBEAN;
	}

}
