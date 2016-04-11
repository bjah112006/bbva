package com.ibm.bbva.ctacte.business.impl;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.business.BuscarExpediente;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;

/**
 * Session Bean implementation class BuscarExpediente
 */
@Stateless
@Local(BuscarExpediente.class)
public class BuscarExpedienteImpl implements BuscarExpediente {

	private static final Logger LOG = LoggerFactory.getLogger(BuscarExpedienteImpl.class);
	
	@EJB
	private ExpedienteDAO expedienteDAO;
	
    /**
     * Default constructor. 
     */
    public BuscarExpedienteImpl() {
    }

	@Override
	public Expediente buscarExpedientebyID(int idExp) {
		LOG.info("buscarExpedientebyID (int idExp)");
		Expediente expedienteBean = expedienteDAO.load(idExp);
		
		return expedienteBean;
	}

}
