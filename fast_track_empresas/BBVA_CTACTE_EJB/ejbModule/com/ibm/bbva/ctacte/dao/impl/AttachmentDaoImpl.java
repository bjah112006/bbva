package com.ibm.bbva.ctacte.dao.impl;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.ctacte.bean.Attachment;
import com.ibm.bbva.ctacte.dao.AttachmentDAO;
import com.ibm.bbva.ctacte.dao.GenericDAO;
import com.ibm.bbva.ctacte.model.RequerimientoModel;

@Stateless
@Local(AttachmentDAO.class)
public class AttachmentDaoImpl extends GenericDAO<Attachment, Integer> implements AttachmentDAO{

	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

	private static final Logger LOG = LoggerFactory.getLogger(AttachmentDaoImpl.class);

	public AttachmentDaoImpl() {
		super(Attachment.class);
	}
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	@Override
	public List<Attachment> listaAttachment(Attachment filtro) throws SQLException {
		
		List<Attachment> listAttachments =new ArrayList<Attachment>();	

		String q="select a from Attachment a where a.id_Requerimiento=:id_Requerimiento";
		
		Query query = em.createNamedQuery(q);
		query.setParameter("id_Requerimiento", filtro.getId_Requerimiento());
		
		listAttachments = (List<Attachment>)query.getResultList();

		LOG.info("query :" + q);

		return listAttachments;

	}
	@Override
	public Attachment obtener(Long id) throws SQLException {
		return null;

	}
}
