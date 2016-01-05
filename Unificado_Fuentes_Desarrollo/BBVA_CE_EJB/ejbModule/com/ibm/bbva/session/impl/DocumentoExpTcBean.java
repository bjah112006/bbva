package com.ibm.bbva.session.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.entities.Expediente;
import com.ibm.bbva.entities.TipoDocumento;
import com.ibm.bbva.session.AbstractFacade;
import com.ibm.bbva.session.DocumentoExpTcBeanLocal;

/**
 * Session Bean implementation class DocumentoExpTcBean
 */
@Stateless
@Local(DocumentoExpTcBeanLocal.class)
public class DocumentoExpTcBean extends AbstractFacade<DocumentoExpTc> implements DocumentoExpTcBeanLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(DocumentoExpTcBean.class);

	@PersistenceContext(unitName = "BBVA_CE_JPA")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public DocumentoExpTcBean() {
    	super(DocumentoExpTc.class);
    }

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public DocumentoExpTc buscarPorId(long id) {
		return (DocumentoExpTc) em.createNamedQuery("DocumentoExpTc.findById").setParameter("id", id).getSingleResult();		 
	}	
	
	@Override
	public List<DocumentoExpTc> buscarPorExpediente(long id) {
		List<DocumentoExpTc> resultList = em.createNamedQuery("DocumentoExpTc.findByExpediente").setParameter("id", id).getResultList();
		return resultList;
	}
	
	
	@Override
	public List<DocumentoExpTc> buscarPorExpedienteFlagCM(long id, String flagCm) {
		List<DocumentoExpTc> resultList = em.createNamedQuery("DocumentoExpTc.findByExpedienteFlagCM").setParameter("id", id).setParameter("flagCm", flagCm).getResultList();
		return resultList;
	}	
	
	@Override
	public DocumentoExpTc consultarDocumentoExpediente(long idExpediente, String codigoTipoDoc){
		try{
			List<DocumentoExpTc> listResult= em.createNamedQuery("DocumentoExpTc.consultarDocumentoExpediente")
					.setParameter("idExpediente", idExpediente)
					.setParameter("codigoTipoDoc", codigoTipoDoc.trim())
					.getResultList();
			if (listResult == null || listResult.isEmpty()) {
				return null;
			} else {
				return listResult.get(0); //debería ser uno solo
			}
		} catch(NoResultException e){
			return null;
		}		
	}
	
	@Override
	public DocumentoExpTc consultarDocumentoExpedienteCM(long idExpediente, String codigoTipoDoc){
		try{
			List<DocumentoExpTc> listResult= em.createNamedQuery("DocumentoExpTc.consultarDocumentoExpedienteCM")
					.setParameter("idExpediente", idExpediente)
					.setParameter("codigoTipoDoc", codigoTipoDoc.trim())
					.getResultList();
			if (listResult == null || listResult.isEmpty()) {
				return null;
			} else {
				return listResult.get(0); //debería ser uno solo
			}
		} catch(NoResultException e){
			return null;
		}		
	}	
	
	@Override
	public DocumentoExpTc findByTipoDocisNullCM(long idExpediente, long idTipoDocumento){
		try{
			List<DocumentoExpTc> listResult= em.createNamedQuery("DocumentoExpTc.findByTipoDocisNullCM")
					.setParameter("id", idExpediente)
					.setParameter("idTipoDocumento", idTipoDocumento)
					.getResultList();
			if (listResult == null || listResult.isEmpty()) {
				return null;
			} else {
				return listResult.get(0); //debería ser uno solo
			}
		} catch(NoResultException e){
			return null;
		}		
	}	
	
	@Override
	public List<DocumentoExpTc> consultarDocumentosExpediente(long idExpediente, String codigoTipoDoc){
		try{
			List<DocumentoExpTc> listResult= em.createNamedQuery("DocumentoExpTc.consultarDocumentoExpediente")
					.setParameter("idExpediente", idExpediente)
					.setParameter("codigoTipoDoc", codigoTipoDoc.trim())
					.getResultList();
			if (listResult == null || listResult.isEmpty()) {
				return null;
			} else {
				return listResult;
			}
		} catch(NoResultException e){
			return null;
		}		
	}	
	
	@Override
	public int actualizarDocumentoExpediente(DocumentoExpTc objDocumentoExpTc){
		//String query="UPDATE DocumentoExpTc d SET d.idCm=:idCm, d.pidCm=:pidCm, d.fecReg=:fecReg, d.flagCm=:flagCm, d.nombreArchivo=:nombreArchivo where d.expediente.id=:idExpediente and d.tipoDocumento.id=:idTipoDocumento";
		String query="UPDATE DocumentoExpTc d SET d.idCm=:idCm, d.pidCm=:pidCm, d.fecReg=:fecReg, d.flagCm=:flagCm, d.nombreArchivo=:nombreArchivo where d.id=:idDocExp";
		try{
			//LOG.info("idDocExp: " + objDocumentoExpTc.getId());
			int updatedEntities = em.createQuery( query )
					.setParameter("idCm", objDocumentoExpTc.getIdCm())
					.setParameter("pidCm", objDocumentoExpTc.getPidCm())
					.setParameter("fecReg", objDocumentoExpTc.getFecReg())
					.setParameter("flagCm", objDocumentoExpTc.getFlagCm())
					.setParameter("nombreArchivo", objDocumentoExpTc.getNombreArchivo())
					.setParameter("idDocExp", objDocumentoExpTc.getId())
					//.setParameter("idExpediente", objDocumentoExpTc.getExpediente().getId())
					//.setParameter("idTipoDocumento", objDocumentoExpTc.getTipoDocumento().getId())
			        .executeUpdate();
					
			return updatedEntities;	
			
		}catch(NoResultException e){
			LOG.info("e" + e.getMessage());
			return 0;
		}

	}
	

	@Override
	public DocumentoExpTc consultarUltimoDocumentoExpediente(long idExpediente, String codigoTipoDoc){
		try{
			List<DocumentoExpTc> listResult= em.createNamedQuery("DocumentoExpTc.consultarUltimoDocumentoExpediente")
					.setParameter("idExpediente", idExpediente)
					.setParameter("codigoTipoDoc", codigoTipoDoc.trim())
					.getResultList();
			if (listResult == null || listResult.isEmpty()) {
				return null;
			} else {
				return listResult.get(0); //Se retorna el ultimo.
			}
		} catch(NoResultException e){
			return null;
		}		
	}
	

	@Override
	public DocumentoExpTc consultarDocumentoExpediente(long idExpediente, BigDecimal idCm){
		try{
			List<DocumentoExpTc> listResult= em.createNamedQuery("DocumentoExpTc.consultarDocumentoExpedienteByIdCm")
					.setParameter("idExpediente", idExpediente)
					.setParameter("idCm", idCm)
					.getResultList();
			if (listResult == null || listResult.isEmpty()) {
				return null;
			} else {
				return listResult.get(0); //debería ser uno solo
			}
		} catch(NoResultException e){
			return null;
		}		
	}
	

	@Override
	public DocumentoExpTc create(DocumentoExpTc entity) {
		return super.create(entity);
	}
	
	@Override
	public void edit(DocumentoExpTc entity) {
		super.edit(entity);
	}

	@Override
	public void remove(DocumentoExpTc entity) {
		super.remove(entity);
	}
	
	@Override
	public List<DocumentoExpTc> buscarPorExpedienteFlagEscaneado(long id, String flagEscaneado) {
		List<DocumentoExpTc> resultList = em.createNamedQuery("DocumentoExpTc.findByExpedienteFlagEscaneado").setParameter("id", id).setParameter("flagEscaneado", flagEscaneado).getResultList();
		return resultList;
	}
	
	@Override
	public List<DocumentoExpTc> buscarDocumentosReutilizables(long idTipoDoi, String numeroDoi){
		String query = "select td.id as idTipoDoc, td.codigo as codTipoDoc, td.descripcion as descTipoDoc, de.id_expediente_fk as idExp, de.id as idDocExp, de.fec_reg as fecRegDocExp " +
						"from conele.tbl_ce_ibm_documento_exp_tc de, " +
						"conele.tbl_ce_ibm_expediente e, " +
						"conele.tbl_ce_ibm_tipo_documento td, " +
						"conele.tbl_ce_ibm_cliente_natural cn " +
						"where e.id_cliente_natural_fk = cn.id " +
						"and de.id_expediente_fk = e.id " +
						"and de.id_tipo_documento_fk = td.id " +
						"and td.flag_reutilizable = '1' " +
						"and cn.id_tipo_doi_fk = ? " +
						"and cn.num_doi = ? " +
						"and de.fec_reg is not null " +
						"and ( " +
						"	(td.codigo <> 'DOID0' and (sysdate - trunc(de.fec_reg)) <= td.validez) " +
						"	or (td.codigo = 'DOID0' and (sysdate - trunc(de.fec_reg)) <= td.validez and de.fec_ven is not null and sysdate <= trunc(de.fec_ven) " +
						"	or (td.codigo = 'DOID0' and (sysdate - trunc(de.fec_reg)) <= td.validez and de.fec_ven is null)) " +
						") " +
						"order by td.codigo, de.fec_reg desc";
		try{
			List<Object[]> listResult = em.createNativeQuery(query)
					.setParameter(1, idTipoDoi)
					.setParameter(2, numeroDoi.trim())
					.getResultList();
			if (listResult == null || listResult.isEmpty()) {
				return null;
			} else {
				List<DocumentoExpTc> listDocumentoExpTc = new ArrayList<DocumentoExpTc>();
				LOG.info("listResult:::::"+listResult.size());
				for(Object[] objects : listResult){
					DocumentoExpTc documentoExpTc = new DocumentoExpTc();
					// tipo de documento
					TipoDocumento tipoDocumento = new TipoDocumento();
					tipoDocumento.setId(((BigDecimal)objects[0]).longValue());
					tipoDocumento.setCodigo(String.valueOf(objects[1]));
					tipoDocumento.setDescripcion(String.valueOf(objects[2]));
					documentoExpTc.setTipoDocumento(tipoDocumento);
					// expediente
					Expediente expediente = new Expediente();
					expediente.setId(((BigDecimal)objects[3]).longValue());
					documentoExpTc.setExpediente(expediente);
					documentoExpTc.setId(((BigDecimal)objects[4]).longValue());
					documentoExpTc.setFecReg((Timestamp)objects[5]);
					listDocumentoExpTc.add(documentoExpTc);
				}
				LOG.info("listDocumentoExpTc:::::::"+listDocumentoExpTc.size());
				return listDocumentoExpTc;
			}
		} catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public int actualizarDocumentosNoObservados(long idExpediente, String iDsCM){
		String query = "update conele.tbl_ce_ibm_documento_exp_tc " +
						"set FLAG_OBS='0' " +
						"where id_expediente_fk = " + idExpediente + 
						"  and ID_CM in (" + iDsCM + ")";
		int result =0;
		try{
			result = em.createNativeQuery(query)
					.executeUpdate();
		} catch(Exception e){
			LOG.error(e.getMessage(),e);
			return result;
		}
		return result;
	}
	
}
