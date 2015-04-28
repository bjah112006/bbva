package clientecontent;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.service.ContentServiceImplPortProxy;
import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.cm.servicepid.ContentServicePidImplPortProxy;
import com.ibm.bbva.cm.servicepid.DocumentoPid;
import com.ibm.bbva.cm.util.Constantes;

public class ClienteContentCC {
	
	private static final Logger LOG = LoggerFactory.getLogger(ClienteContentCC.class);
	
	private String url;

	public ClienteContentCC(String url) {
		LOG.info("**********ClienteContentCC(String url)********");
		this.url = url;
	}

	public Documento buscarDocumentoItemTypeCM(Documento documento) {
		Documento doc = null;

		try {
			ContentServiceImplPortProxy serviceIBMBBVA = new ContentServiceImplPortProxy();
			serviceIBMBBVA._getDescriptor().setEndpoint(url);
			documento.setUrlContent("flagContenidoNull"); // hack para que el servicio web devuelva el contenido en null
			documento.setOrigen(Constantes.ORIGEN_ARCHIVO_CC);
			doc = serviceIBMBBVA.find(documento);
			doc.setContenido(null);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return doc;
	}

	public Documento[] historialDocumentoxCliente(Documento documento) {
		Documento[] listDocumento = null;
		try {
			LOG.info("**********TRY-ClienteContentCC-historialDocumentoxCliente ********");
			ContentServiceImplPortProxy serviceIBMBBVA = new ContentServiceImplPortProxy();
			serviceIBMBBVA._getDescriptor().setEndpoint(url);
			documento.setOrigen(Constantes.ORIGEN_ARCHIVO_CC);
			List<Documento> arr = serviceIBMBBVA.findAll(documento);
			listDocumento = arr.toArray(new Documento[0]);
		} catch (Exception e) {
			LOG.info("**********CATCH-Documento[] ClienteContentCC-historialDocumentoxCliente ********");
			LOG.info("**********ERROR********"+e);
			LOG.error(e.getMessage(), e);
		} 
		return listDocumento;
	}
	
	public DocumentoPid[] historialDocumentoxCliente(DocumentoPid documento) {
		DocumentoPid[] listDocumento = null;
		try {
			LOG.info("**********TRY-ClienteContentCC-DocumentoPid[] historialDocumentoxCliente ********");
			ContentServicePidImplPortProxy serviceIBMBBVA = new ContentServicePidImplPortProxy();
			serviceIBMBBVA._getDescriptor().setEndpoint(url);
			documento.setOrigen(Constantes.ORIGEN_ARCHIVO_CC);
			List<DocumentoPid> arr = serviceIBMBBVA.findAll(documento);
			listDocumento = arr.toArray(new DocumentoPid[0]);
		} catch (Exception e) {
			LOG.info("**********CATCH-ClienteContentCC-DocumentoPid[]-historialDocumentoxCliente ********");
			LOG.info("**********ERROR********"+e);
			LOG.error(e.getMessage(), e);
		} 
		return listDocumento;
	}
	
	public List<Documento> listaDocumentosCliente(Documento documento) {
		List<Documento> listDocumento = null;
		try {
			LOG.info("**********TRY-ClienteContentCC-ListaDocumentosCliente ********");
			ContentServiceImplPortProxy serviceIBMBBVA = new ContentServiceImplPortProxy();
			serviceIBMBBVA._getDescriptor().setEndpoint(url);
			documento.setOrigen(Constantes.ORIGEN_ARCHIVO_CC);
			listDocumento = serviceIBMBBVA.findAll(documento);
		} catch (Exception e) {
			LOG.info("**********CATCH-Documento[] ClienteContentCC-listDocumento ********");
			LOG.info("**********ERROR********"+e);
			LOG.error(e.getMessage(), e);
		} 
		return listDocumento;
	}
}
