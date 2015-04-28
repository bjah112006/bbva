package com.ibm.bbva.ctacte.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ibm.bbva.ctacte.bean.DocumentoCM;
import com.ibm.bbva.ctacte.dao.DocumentoCMDAO;

/**
 * Session Bean implementation class DocumentoCMDAOImpl
 */
@Stateless
@Local(DocumentoCMDAO.class)
public class DocumentoCMDAOImpl implements DocumentoCMDAO {
	
	@PersistenceContext(unitName = "BBVA_CTACTE_JPA")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public DocumentoCMDAOImpl() {
    }

	@Override
	public DocumentoCM getDocumentoCM(int idExpediente, int idDocumento) {
		DocumentoCM documentoCM = new DocumentoCM();
		
		//String strSQL =   " SELECT CLI.CODIGO_CENTRAL AS codCliente, CLI.NUMERO_DOI AS nroDocumento, CLI.TIPO_DOI AS tipoDoi, CONELE.SEQ_CE_IBM_DOC_EXP_CM_CC.NEXTVAL AS id, DOCEXP.FECHA_VIGENCIA AS fechaExpiracion, DOCEXP.FLAG_OBLIGATORIO AS mandatorio "  
		String strSQL =   " SELECT CLI.CODIGO_CENTRAL AS codCliente, CLI.NUMERO_DOI AS nroDocumento, CLI.TIPO_DOI AS tipoDoi, 0 AS id, DOCEXP.FECHA_VIGENCIA AS fechaExpiracion, DOCEXP.FLAG_OBLIGATORIO AS mandatorio "
				+ " FROM  "
				+ " CONELE.TBL_CE_IBM_EXPEDIENTE_CC EXP "
				+ " INNER JOIN CONELE.TBL_CE_IBM_CLIENTE_CC CLI "
				+ " ON CLI.ID = EXP.ID_CLIENTE_FK "
				+ " INNER JOIN CONELE.TBL_CE_IBM_DOCUMENTO_EXP_CC DOCEXP "
				+ " ON DOCEXP.ID_EXPEDIENTE_CC_FK = EXP.ID "
				+ " WHERE EXP.ID = ?1"
				+ " AND DOCEXP.ID_DOCUMENTO_CC_FK = ?2";
		
		Query query = em.createNativeQuery(strSQL);
		query.setParameter(1, idExpediente);
		query.setParameter(2, idDocumento);
		List<Object[]> list = query.getResultList();
		List<DocumentoCM> listdocumentoCM = new ArrayList<DocumentoCM>();
		for (Object[] obj : list) {
			DocumentoCM doc = new DocumentoCM();
			doc.setCodCliente((String) obj[0]);
			doc.setNroDocumento((String) obj[1]);
			doc.setTipoDoi((String) obj[2]);
			doc.setId((BigDecimal) obj[3]);
			
			// modificanco el id a partir de la secuencia
			String strSubSQL = "SELECT CONELE.SEQ_CE_IBM_DOC_EXP_CM_CC.NEXTVAL FROM DUAL";
			Query subQuery = em.createNativeQuery(strSubSQL);
			BigDecimal id = (BigDecimal)subQuery.getSingleResult();
			doc.setId(id);
			
			doc.setFechaExpiracion((Date) obj[4]);
			doc.setMandatorio(((String) obj[5]).charAt(0));
			
			listdocumentoCM.add(doc);
		}
		documentoCM = listdocumentoCM.get(0);
		
		return documentoCM;
	}

	@Override
	public DocumentoCM getDocumentoCMxCodigo(int idExpediente,
			String codigoDocumento) {
		DocumentoCM documentoCM = new DocumentoCM();
		
		//String strSQL =   " SELECT CLI.CODIGO_CENTRAL AS codCliente, CLI.NUMERO_DOI AS nroDocumento, CLI.TIPO_DOI AS tipoDoi, CONELE.SEQ_CE_IBM_DOC_EXP_CM_CC.NEXTVAL AS id, DOCEXP.FECHA_VIGENCIA AS fechaExpiracion, DOCEXP.FLAG_OBLIGATORIO AS mandatorio "  
		String strSQL =   " SELECT CLI.CODIGO_CENTRAL AS codCliente, CLI.NUMERO_DOI AS nroDocumento, CLI.TIPO_DOI AS tipoDoi, 0 AS id, DOCEXP.FECHA_VIGENCIA AS fechaExpiracion, DOCEXP.FLAG_OBLIGATORIO AS mandatorio "
				+ " FROM  "
				+ " CONELE.TBL_CE_IBM_EXPEDIENTE_CC EXP "
				+ " INNER JOIN CONELE.TBL_CE_IBM_CLIENTE_CC CLI "
				+ " ON CLI.ID = EXP.ID_CLIENTE_FK "
				+ " INNER JOIN (CONELE.TBL_CE_IBM_DOCUMENTO_EXP_CC DOCEXP "
				+ " INNER JOIN CONELE.TBL_CE_IBM_DOCUMENTO_CC DOC "
				+ " ON DOC.ID = DOCEXP.ID_DOCUMENTO_CC_FK) "
				+ " ON DOCEXP.ID_EXPEDIENTE_CC_FK = EXP.ID "
				+ " WHERE EXP.ID = ?1"
				+ " AND DOC.CODIGO_DOCUMENTO = ?2";
		Query query = em.createNativeQuery(strSQL);
		query.setParameter(1, idExpediente);
		query.setParameter(2, codigoDocumento);
		List<Object[]> list = query.getResultList();
		List<DocumentoCM> listdocumentoCM = new ArrayList<DocumentoCM>();
		for (Object[] obj : list) {
			DocumentoCM doc = new DocumentoCM();
			doc.setCodCliente((String) obj[0]);
			doc.setNroDocumento((String) obj[1]);
			doc.setTipoDoi((String) obj[2]);
			doc.setId((BigDecimal) obj[3]);
			
			// modificanco el id a partir de la secuencia
			String strSubSQL = "SELECT CONELE.SEQ_CE_IBM_DOC_EXP_CM_CC.NEXTVAL FROM DUAL";
			Query subQuery = em.createNativeQuery(strSubSQL);
			BigDecimal id = (BigDecimal)subQuery.getSingleResult();
			doc.setId(id);
			
			doc.setFechaExpiracion((Date) obj[4]);
			doc.setMandatorio(((String) obj[5]).charAt(0));
			
			listdocumentoCM.add(doc);
		}
		documentoCM = listdocumentoCM.get(0);
		
		return documentoCM;
	}

}
