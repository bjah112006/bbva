package clientecontent;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.service.ContentServiceImplPortProxy;
import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.cm.util.Constantes;

public class ClienteContent {
	
	private static final Logger LOG = LoggerFactory.getLogger(ClienteContent.class);

	private String url;
	private ContentServiceImplPortProxy serviceIBMBBVA;

	public ClienteContent() {
		this.url = "http://9.6.98.125:9080/WEBCM/services/ContentServiceImpl/wsdl/ContentServiceImpl.wsdl";
		init();
	}

	public ClienteContent(String url) {
		this.url = url;
		init();
	}
	private void init() {
		serviceIBMBBVA = new ContentServiceImplPortProxy();
		serviceIBMBBVA._getDescriptor().setEndpoint(url);
	}

	public Documento buscarDocumentoCM(Documento documento) {
		Documento doc = null;
		try {
			documento.setOrigen(Constantes.ORIGEN_ARCHIVO_CC);
			doc = serviceIBMBBVA.find(documento);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} 
		return doc;
	}

	public String eliminarDocumentoCM(Documento documento) {
		String result ="KO";
		try {
			result = serviceIBMBBVA.delete(documento);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return result;
	}

	public String actualizarDocumentoCM(Documento documento) {
		String result ="KO";
		try{
			result =  serviceIBMBBVA.update(documento);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

	public String actualizarDocumentoCM(Integer id, String newTipoDoc) {
		Documento documento = new Documento();
		documento.setId(id);
		documento.setTipo(newTipoDoc);
		return actualizarDocumentoCM(documento);
	}

	public String eliminarDocumentosCM(Documento[] documentos) {
		String result ="KO";
		try {
			result = serviceIBMBBVA.deleteAll(Arrays.asList(documentos));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return result;
	}
	
}
