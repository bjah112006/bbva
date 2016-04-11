package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.GuiaDocument;
import com.ibm.bbva.ctacte.bean.GuiaDocumentId;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.dao.GuiaDocumentDAO;

/**
 * Session Bean implementation class GuiaDocumentBean
 */
@Stateless
@Local(GuiaDocumentDAO.class)
public class GuiaDocumentDAOImpl extends GenericDAO<GuiaDocument, GuiaDocumentId> implements GuiaDocumentDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public GuiaDocumentDAOImpl() {
        super(GuiaDocument.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<GuiaDocument> findByDocumento(GuiaDocument guiaDocumento) {
		//FIXME no se usa y el query está mal hecho (campo que no existe)
		Query query = em.createQuery(
 			"select o from GuiaDocument o where o.tblCeIbmDocumentoCc=:guiaDocumento");
		query.setParameter("guiaDocumento", guiaDocumento);
		List<GuiaDocument> documentos = query.getResultList();
		return documentos;
	}

	@Override
	public List<GuiaDocument> findByTipoPJOperacionCasoNegocio(String codTipoPj,
			Integer operacion, Integer casoNegocio) {
		Query query = em.createQuery(
			"select o from GuiaDocument o where o.id.codTipoPj=:codTipoPj and o.operacion.id=:operacion and o.casoNegocio.id=:casoNegocio");
		query.setParameter("codTipoPj", codTipoPj);
		query.setParameter("operacion", operacion);
		query.setParameter("casoNegocio", casoNegocio);
		List<GuiaDocument> documentos = query.getResultList();
		return documentos;
	}

}
