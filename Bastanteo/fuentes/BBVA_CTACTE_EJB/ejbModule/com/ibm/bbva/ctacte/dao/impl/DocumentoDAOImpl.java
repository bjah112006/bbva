package com.ibm.bbva.ctacte.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.Documento;
import com.ibm.bbva.ctacte.dao.DocumentoDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class DocumentoBean
 */
@Stateless
@Local(DocumentoDAO.class)
public class DocumentoDAOImpl extends GenericDAO<Documento, Integer> implements
		DocumentoDAO {

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public DocumentoDAOImpl() {
		super(Documento.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Documento findByCodigo(String codigo) {
		try {
			Query query = em
					.createQuery("select o from Documento o where o.codigoDocumento=:codDoc");
			query.setParameter("codDoc", codigo);
			Documento doc = (Documento) query.getSingleResult();
			return doc;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Documento> getListaDocumentos(int idExpediente) {
		// FIXME no se usa y el query está mal hecho (campos de diferentes tablas)
		String strSQL = "select o.documento from DocumentoExp o "
				+ "where o.flagObligatorio='1' "
				+ "and o.expediente.id=:idExp "
				+ "and o.nombreArchivo is not null and o.tipoVisor='B'";
		// +POR SOLICITUD BBVA+//+POR SOLICITUD
		// BBVA+System.out..println("strSQL-->"+strSQL);
		Query query = em.createQuery(strSQL);
		query.setParameter("idExp", idExpediente);
		List<Documento> listaDoc = query.getResultList();
		return listaDoc;
	}

	@Override
	public List<Documento> getListaDocumentoCU15(int idExpediente) {
		String strSQL = "select o.documento from DocumentoExp o "
				+ "and o.expediente.id=:idExp "
				+ "and (o.documento.id=2 or o.documento.id=3)";
		// +POR SOLICITUD BBVA+//+POR SOLICITUD
		// BBVA+System.out..println("strSQL-->"+strSQL);
		Query query = em.createQuery(strSQL);
		query.setParameter("idExp", idExpediente);
		List<Documento> listaDoc = query.getResultList();
		return listaDoc;
	}

}
