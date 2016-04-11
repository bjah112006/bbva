package com.ibm.bbva.ctacte.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;

/**
 * Session Bean implementation class DocumentoExpBean
 */
@Stateless
@Local(DocumentoExpDAO.class)
public class DocumentoExpDAOImpl extends GenericDAO<DocumentoExp, Integer> implements DocumentoExpDAO {
	
	private static final Logger LOG=LoggerFactory.getLogger(DocumentoExpDAOImpl.class);

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public DocumentoExpDAOImpl() {
        super(DocumentoExp.class);
    }
       
    @Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public List<DocumentoExp> findByExpdiente(int idExpediente) {
		LOG.info("**************DocumentoExpDAO-findByExpdiente**********");
		Query query = em.createQuery("select o from DocumentoExp o where o.expediente.id=:idExp order by o.documento.descripcion");
		LOG.info("*************query**********"+query);
		query.setParameter("idExp", idExpediente);
		List<DocumentoExp> documentos = query.getResultList();
		return documentos;
	}

	@Override
	public List<DocumentoExp> findByDocumentosLegales(int idExpediente,
			String documentosLegales) {
		Query query=em.createQuery(
			"select o from DocumentoExp o where o.expediente.id=:idExp and o.documento.tipoDocumento=:docLegales order by o.documento.descripcion");
		query.setParameter("idExp",idExpediente);
		query.setParameter("docLegales",documentosLegales);
		List<DocumentoExp> documentos = query.getResultList();
		return documentos;
	}

	@Override
	public List<DocumentoExp> findByDocumentosLegalesRechazados(
			int idExpediente, String documentosLegales) {
		Query query=em.createQuery(
			"select o from DocumentoExp o where o.expediente.id=:idExp and o.documento.tipoDocumento=:docLegales and o.flagRechazado='1' order by o.documento.descripcion");
		query.setParameter("idExp",idExpediente);	
		query.setParameter("docLegales",documentosLegales);
		List<DocumentoExp> documentos = query.getResultList();
		return documentos;
	}

	@Override
	public List<DocumentoExp> findByExpdienteEscaneado(int idExpediente,
			String flagEscaneado, String codDOI, String codFRF) {
		Query query = em.createQuery(
 			"select o from DocumentoExp o where o.expediente.id=:idExp and o.flagEscaneado=:flgEsc and (o.documento.codigoDocumento=:cDOI or o.documento.codigoDocumento=:cFRF ) order by o.documento.descripcion");
		query.setParameter("idExp", idExpediente);
		query.setParameter("flgEsc", flagEscaneado);
		query.setParameter("cDOI",codDOI);
		query.setParameter("cFRF",codFRF);
		List<DocumentoExp> documentos = query.getResultList();
		return documentos;
	}

	@Override
	public List<DocumentoExp> findByExpdienteRechazado(int idExpediente,
			String codDOI, String codFRF) {
		Query query = em.createQuery(
 			"select o from DocumentoExp o where o.expediente.id=:idExp and o.flagRechazado='1' and (o.documento.codigoDocumento=:cDOI or o.documento.codigoDocumento=:cFRF ) order by o.documento.descripcion");
		query.setParameter("idExp", idExpediente);		
		query.setParameter("cDOI",codDOI);
		query.setParameter("cFRF",codFRF);
		List<DocumentoExp> documentos = query.getResultList();
		return documentos;
	}

	@Override
	public List<DocumentoExp> findByExpdienteCartaRevocatoria(int idExpediente,
			String codCartaRevocatoria) {
		Query query=em.createQuery("" +
				"select o from DocumentoExp o where o.expediente.id=:idExp and o.documento.codigoDocumento=:cCR");	
		query.setParameter("idExp", idExpediente);
		query.setParameter("cCR",codCartaRevocatoria);
		List<DocumentoExp> documentos=query.getResultList();
		return documentos;
	}

	@Override
	public List<DocumentoExp> findHistorialDocumentoxCliente(
			String codigoDocumento, String codCliente, int cantDocumento,
			DocumentoExp docExp) {
		Query query = em
				.createQuery("select o from DocumentoExp o where o.documento.codigoDocumento=:codigoDocumento and o.expediente.cliente.codigoCentral=:codCliente and o.idCm is not null and o.pidCM is not null and o.flagEscaneado='1' and o.flagCm='1' order by o.fechaEscaneo ASC");
		query.setParameter("codigoDocumento", codigoDocumento);
		query.setParameter("codCliente",codCliente);
		List<DocumentoExp> listTemporal = query.getResultList();
		List<DocumentoExp> listdocumento = new ArrayList<DocumentoExp>();
		if (listTemporal != null && listTemporal.size() >= 1) {
			if (docExp != null && docExp.getIdCm() != null && docExp.getIdCm().intValue() > 0) {
				List<DocumentoExp> listaDocHist = new ArrayList<DocumentoExp>();
				for (int i=0; i<listTemporal.size(); i++) {
					// vienen ordenados ascendentemente por fecha de escaneo
					if (docExp.getIdCm().intValue() != listTemporal.get(i).getIdCm().intValue()) {
						listaDocHist.add(listTemporal.get(i));
					} else {
						break;
					}
				}
				listTemporal = listaDocHist;
			}
			
			int numDocumentos = 0;
			if (cantDocumento == 0) {
				numDocumentos = listTemporal.size();
			} else if (cantDocumento == 1) {
				numDocumentos = 1;
			} else if (listTemporal.size() > cantDocumento) {
				numDocumentos = cantDocumento;
			} else if (listTemporal.size() <= cantDocumento) {
				numDocumentos = listTemporal.size();
			}
			// se traen los n más recientes ordenados del más reciente al más antiguo
			int j = listTemporal.size()-1;
			for(int i=0;i<numDocumentos;i++){
				listdocumento.add(listTemporal.get(j));
				j--;
			}
		}
		return listdocumento;
	}

	@Override
	public DocumentoExp findByDocExp(Integer idDoc, Integer idExp) {
		try {
			Query query = em.createQuery(
	 			"select o from DocumentoExp o where o.documento.id=:idDoc and o.expediente.id=:idExp");
			query.setParameter("idDoc", idDoc);
			query.setParameter("idExp", idExp);
			DocumentoExp docExp = (DocumentoExp) query.getSingleResult();
			return docExp;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public DocumentoExp findByCodDocExp(String codDoc, Integer idExp) {
		try {
			Query query = em.createQuery(
	 			"select o from DocumentoExp o where o.documento.codigoDocumento=:codDoc and o.expediente.id=:idExp");
			query.setParameter("codDoc", codDoc);
			query.setParameter("idExp", idExp);
			DocumentoExp docExp = (DocumentoExp) query.getSingleResult();
			return docExp;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public DocumentoExp findByCodDocCodCent(String codDoc, String codigoCentral) {
		try {
			Query query = em.createQuery(
	 			"select o from DocumentoExp o where o.documento.codigoDocumento=:codDoc and o.expediente.cliente.codigoCentral=:codCent order by o.id desc");
			query.setParameter("codDoc", codDoc);
			query.setParameter("codCent", codigoCentral);
			query.setMaxResults(1);
			DocumentoExp docExp = (DocumentoExp) query.getSingleResult();
			return docExp;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public DocumentoExp findByIdCm(Integer idCm) {
		try {
			Query query = em.createQuery(
	 			"select o from DocumentoExp o where o.idCm=:idCm order by o.id desc");
			query.setParameter("idCm", idCm);
			query.setMaxResults(1);
			DocumentoExp docExp = (DocumentoExp) query.getSingleResult();
			return docExp;
		} catch (NoResultException e) {
			LOG.error("El documento con ID_CM="+idCm+" no existe en BD.");
			return null;
		}
	}

	@Override
	public int delDocumentoExpediente(int codExpediente) {
		// FIXME ya no se usa, creo que está mal hecho el query y borraría todos
		// los documentos si se cumple la condición
		String strSQL =   "DELETE from DocumentoExp o " +
				  " WHERE EXISTS ( " +
				  " SELECT d " +
				  " FROM DocumentoExp d " +
				  "	WHERE d.fechaEscaneo >= d.expediente.fechaRegistro " +
				  " AND d.expediente.id = :codExpediente"+
				  " )";
		Query query = em.createQuery(strSQL);
		query.setParameter("codExpediente", codExpediente);
		int result = query.executeUpdate();
		return result;
	}

	@Override
	public List<DocumentoExp> getListaDocumentos(Integer idExpediente) {
		LOG.info("entrara al query");
		Query query = em.createQuery(			
			"select o from DocumentoExp o where o.expediente.id=:idExpediente");
		LOG.info("query"+query);
		query.setParameter("idExpediente", idExpediente);
		LOG.info("listara!!!!");
		List<DocumentoExp> listaDocumentos=query.getResultList();
		LOG.info("query exitoso");
		return listaDocumentos;
	}

	@Override
	public String validarFlagEnvioContent(Integer idExpediente) {
		Query query = em.createQuery("select o from DocumentoExp o where o.expediente.id=:idExp and o.flagEscaneado = '1' and (o.flagCm is null or o.flagCm <> '1')");
		query.setParameter("idExp", idExpediente);
		List<DocumentoExp> documentos = query.getResultList();
		String resultado;
		if (documentos != null && documentos.size() > 0) {
			resultado = "1";
		} else {
			resultado = "0";
		}
		LOG.info("validarFlagEnvioContent -> resultado = "+resultado);
		return resultado;
	}

}
