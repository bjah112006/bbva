package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.DocumentoHist;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.DocumentoHistDAO;

/**
 * Session Bean implementation class DocumentoHistBean
 */
@Stateless
@Local(DocumentoHistDAO.class)
public class DocumentoHistDAOImpl extends GenericDAO<DocumentoHist, Integer> implements DocumentoHistDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public DocumentoHistDAOImpl() {
        super(DocumentoHist.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<DocumentoHist> getListDocHistoricobyClienteDocumento(
			int idCliente, int idDocumento) {
		String strSQL =   " SELECT o "  
				+ " FROM DocumentoHist o "
				+ " where o.flagEscaneado='1' "
				+ " AND o.cliente.id = :codCliente "
				+ " and o.documento.id =  :codDocumento "
				+ " ORDER BY o.fechaEscaneo DESC ";
		
		//+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("strSQL-->"+strSQL);
		Query query = em.createQuery(strSQL);
		query.setParameter("codCliente", idCliente);
		query.setParameter("codDocumento",idDocumento);		
		List<DocumentoHist> listaDocHist= query.getResultList();
		return listaDocHist;
	}

}
