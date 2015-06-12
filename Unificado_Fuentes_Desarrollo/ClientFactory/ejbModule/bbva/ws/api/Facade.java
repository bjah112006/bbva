package bbva.ws.api;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bbva.ws.api.view.FacadeLocal;

import com.ibm.bbva.cm.service.impl.ContentServiceImplPortProxy;
import com.ibm.bbva.cm.service.impl.Documento;
import com.ibm.bbva.entities.DocumentoExpTc;
import com.ibm.bbva.service.business.client.DocumentoCM;
import com.ibm.bbva.service.business.client.EmpleadoDTO;
import com.ibm.bbva.service.business.client.ExpedienteDTO;
import com.ibm.bbva.service.business.client.IbmBbvaBusinessPortProxy;
import com.ibm.bbva.service.business.client.PerfilDTO;
import com.ibm.bbva.session.ParametrosConfBeanLocal;

/**
 * Session Bean implementation class Facade
 */
@Singleton
@Local(FacadeLocal.class)
public class Facade implements FacadeLocal {
	
	private static final Logger LOG = LoggerFactory.getLogger(Facade.class);

	private ContentServiceImplPortProxy content;
	private ParametrosConfBeanLocal parametrosConfBean;
	private IbmBbvaBusinessPortProxy ibmbbvaPortProxy = null;
	private String CONTENT_SERVICE_IMPL= "";	
	public Facade() {
	}

	@PostConstruct
	public void init() {
		
		try{
			parametrosConfBean = (ParametrosConfBeanLocal) new InitialContext()
			.lookup("ejblocal:com.ibm.bbva.session.ParametrosConfBeanLocal");
			
			CONTENT_SERVICE_IMPL = parametrosConfBean.buscarPorVariable(1, "CONTENT_SERVICE_IMPL").getValorVariable();
			
		}catch (NamingException e) {
			// TODO Bloque catch generado automáticamente
			LOG.error(e.getMessage(), e);
		}
		
		this.content = new ContentServiceImplPortProxy();
		content._getDescriptor().setEndpoint(CONTENT_SERVICE_IMPL);

		this.ibmbbvaPortProxy = new IbmBbvaBusinessPortProxy();
		//PATTY
		//ibmbbvaPortProxy._getDescriptor().setEndpoint("http://localhost:9081/BBVA_CE_WS/IbmBbvaBusinessService");
		//CARLOS
		ibmbbvaPortProxy._getDescriptor().setEndpoint("http://localhost:9080/BBVA_CE_WS/IbmBbvaBusinessService");
		//BENJAMIN
		//ibmbbvaPortProxy._getDescriptor().setEndpoint("http://localhost:9084/BBVA_CE_WS/IbmBbvaBusinessService");
	}

	@Override
	public boolean ServiceIBMBBVA_delegacionRiesgosWS(Integer idTipoCategoria,
			Long idExpediente) {
		return ibmbbvaPortProxy
				.delegacionRiesgos(idTipoCategoria, idExpediente);
	}

	@Override
	public boolean ServiceIBMBBVA_delegacionOficinaMemoriaWS(ExpedienteDTO objExpedienteDTO) {
		return ibmbbvaPortProxy.delegacionOficinaMemoria(objExpedienteDTO);
	}

	@Override
	public boolean ServiceIBMBBVA_pautasClasificacionMemoria(Long idTipoOferta, Integer idEstadoCivilTitular, Long idPersonaTitular, Long idPersonaConyuge, Double sbsTitular, Double sbsConyuge) {
		return ibmbbvaPortProxy.pautasClasificacionMemoria(idTipoOferta, idEstadoCivilTitular, idPersonaTitular, idPersonaConyuge, sbsTitular, sbsConyuge);
	}

	@Override
	public DocumentoCM ServiceIBMBBVA_consultarDocumentoExpediente(
			Long idExpediente, String codigoTipoDoc) {
		return ibmbbvaPortProxy.consultarDocumentoExpediente(idExpediente,
				codigoTipoDoc);
	}

	@Override
	public boolean ServiceIBMBBVA_actualizarDocumentoExpTC(
			DocumentoCM objDocumentoCM) {
		return ibmbbvaPortProxy.actualizarDocumentoExpTC(objDocumentoCM);
	}

	@Override
	public EmpleadoDTO ServiceIBMBBVA_distribucionCarga(Long idTerritorio,
			Long idExpediente) {
		return ibmbbvaPortProxy.distribucionCarga(idTerritorio, idExpediente);
	}

	@Override
	public PerfilDTO ServiceIBMBBVA_lineaMaximaMemoria(ExpedienteDTO objExpedienteDTO) {
		return ibmbbvaPortProxy.lineaMaximaMemoria(objExpedienteDTO);
	}

	@Override
	public boolean ServiceIBMBBVA_retraerTareas(Long idAccion, Long salida, Long llegada) {
		return ibmbbvaPortProxy.retraerTareas(idAccion, salida, llegada);
	}

	public String Content_delete(Documento documento) {
		return content.delete(documento);
	}

	public void Content_deleteAll(List<Documento> documentos) {
		content.deleteAll(documentos);
	}

	public Documento Content_find(Documento documento) {
		LOG.info("Content_find");
		return content.find(documento);
	}
	
	public List<Documento> Content_findAll(Documento documento) {
		LOG.info("Content_find");
		return content.findAll(documento);
	}	

	@Override
	public Documento obtenerDocumentoCM(DocumentoExpTc documentoExp) {
		Documento documentoCM;
		Documento documentoCM2;
		documentoCM = new Documento();
		documentoCM.setId(documentoExp.getIdCm().intValue());
		documentoCM.setOrigen("PTC"); // PTC
		documentoCM.setTipo(documentoExp.getTipoDocumento().getCodigo());
		LOG.info("obtenerDocumentoCM(Id,Tipo): {"+documentoCM.getId()+","+documentoExp.getTipoDocumento().getCodigo()+"}");
		
		documentoCM2 = Content_find(documentoCM);
		LOG.info("obtenerDocumentoCM " + documentoCM2.getUrlContent());
		
		return documentoCM2;
	}
	
	@Override
	public List<Documento> obtenerListaDocumentoCM(DocumentoExpTc documentoExp) {
		Documento documentoCM;
		List<Documento> listaDocumentoCM;
		documentoCM = new Documento();
		documentoCM.setNumDoi(documentoExp.getExpediente().getClienteNatural().getNumDoi());
		LOG.info("numero doi Exp ="+documentoExp.getExpediente().getClienteNatural().getNumDoi());
		documentoCM.setOrigen("PTC");
		
		listaDocumentoCM = Content_findAll(documentoCM);
		LOG.info("obtenerDocumentoCM " + listaDocumentoCM.size());

		return listaDocumentoCM;
	}	

	@Override
	public String eliminarDocumentoCM(DocumentoExpTc documentoExp) {
		Documento documentoCM = new Documento();
		documentoCM.setId(documentoExp.getIdCm().intValue());
		documentoCM.setOrigen("PTC"); // PTC
		documentoCM.setTipo(documentoExp.getTipoDocumento().getCodigo());
		LOG.info("eliminarDocumentoCM(Id,Tipo): {"+documentoCM.getId()+","+documentoExp.getTipoDocumento().getCodigo()+"}");
		
		return Content_delete(documentoCM);
	}

	public Documento Content_findAsImage(Documento documento, String mimeType) {
		return content.findAsImage(documento, mimeType);
	}

	public String Content_insert(Documento documento) {
		return content.insert(documento);
	}

	public String Content_insertAll(List<Documento> documentos) {
		return content.insertAll(documentos);
	}

	public String Content_update(Documento documento) {

		return content.update(documento);
	}

	public String Content_actualizarTipoDoc(int id, String tipoDoc) {
		return content.actualizarTipoDoc(id, tipoDoc);
	}

}
