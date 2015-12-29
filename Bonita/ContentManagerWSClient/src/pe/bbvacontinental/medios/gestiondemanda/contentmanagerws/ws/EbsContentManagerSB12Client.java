package pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;

import pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.InsertarDocRequest;
import pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.InsertarDocResponse;
import pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.base.AuditRequest;
import pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.base.Parametros;

public class EbsContentManagerSB12Client {

	private static final Logger LOG = Logger.getLogger(EbsContentManagerSB12Client.class);

	public DocumentoResponse procesar(DocumentoRequest doc, byte[] fileByte, String endpoint) {
		DocumentoResponse result = new DocumentoResponse();
		try {
			LOG.info("Invocacion a webservice: [" + endpoint + "]");

			InsertarDocRequest request = new InsertarDocRequest();
			AuditRequest auditRequest = new AuditRequest();
			auditRequest.setIdTransaccion(doc.getId());
			auditRequest.setNombreAplicacion(doc.getNombreAplicacion());
			auditRequest.setUsuarioAplicacion(doc.getUsuarioAplicacion());
			auditRequest.setIpAplicacion(doc.getIpAplicacion());
			request.setAuditRequest(auditRequest);

			Parametros listaParametrosRequest = new Parametros();
			for(String key : doc.keySet()) {
				agregarParametro(listaParametrosRequest, key, doc.get(key));
			}

			request.setListaParametrosRequest(listaParametrosRequest);
			request.setFileData(fileByte);

			URL wsdlLocation = new URL(endpoint);
			QName qname = new QName("http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws", "ContentManagerWSService");

			Service contentManagerWSService = Service.create(wsdlLocation,qname);
			ContentManagerWSPortType contentManagerWSPortType = contentManagerWSService.getPort(ContentManagerWSPortType.class);
			BindingProvider binding = (BindingProvider) contentManagerWSPortType;
			Map<String, Object> mapRequest = binding.getRequestContext();
			mapRequest.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);

			SOAPBinding soapBinding = (SOAPBinding) binding.getBinding();
			soapBinding.setMTOMEnabled(true);

			LOG.info("Esperando respuesta del servicio web");
			long i1 = System.currentTimeMillis();
			// LOG.info("Inicio:" + i1);
			InsertarDocResponse response = contentManagerWSPortType.insertarDoc(request);
			LOG.info("Codigo de respuesta: " + response.getAuditResponse().getCodigoRespuesta());
			LOG.info("Id transaccion de respuesta: " + response.getAuditResponse().getIdTransaccion());
			LOG.info("Mensaje de respuesta: " + response.getAuditResponse().getMensajeRespuesta());
			long i2 = System.currentTimeMillis();
			// LOG.info("Fin:" + i2);
			LOG.info("Demoro: " + (i2 - i1) + "ms");
			LOG.info("Response[Parametro]:");
			for (Parametros.Parametro param_retorno : response.getListaParametrosResponse().getParametro()) {
				result.put(param_retorno.getNombre(), param_retorno.getValor());
				LOG.info("nombre: [" + param_retorno.getNombre() + "] / valor: [" + param_retorno.getValor() + "]");
			}

		} catch (Exception e) {
			LOG.error("Error al subir el documento", e);
			result = null;
		}
		
		return result;
	}

	public static byte[] getFileData(String ruta) throws IOException {
		File f = new File(ruta);
		byte[] data = getBytesFromFile(f);
		return data;
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
			}
		}
		return ous.toByteArray();
	}

	private void agregarParametro(Parametros listaParametrosRequest, String nombre, String valor) {
		if (valor != null) {
			Parametros.Parametro param = new Parametros.Parametro();
			param.setNombre(nombre);
			param.setValor(valor);
			listaParametrosRequest.getParametro().add(param);
		}
	}

	public static void main(String[] args) throws IOException {
		DocumentoRequest doc = new DocumentoRequest();
		doc.setId("10425526176");
		doc.setNombreAplicacion("Cliente Bonita");
		doc.setUsuarioAplicacion("BBVA");
		doc.setIpAplicacion(Inet4Address.getLocalHost().getHostAddress());
		doc.put("id", "10425526176")
			.put("numExpediente", "10425526176")
			.put("tipo", "CPLIT")
			.put("mandatorio", "1")
			.put("origen", "FNE")
			.put("tipoDOI", "R")
			.put("numDOI", "10425526176")
			.put("codigoCliente", "88888888")
			.put("nombreArchivo", "Meld-3.14.0-win32.msi")
			.put("fechaCreacion", "2015-12-01 14:50:00");
		
		EbsContentManagerSB12Client cliente = new EbsContentManagerSB12Client();
		cliente.procesar(doc, EbsContentManagerSB12Client.getFileData("/jquedena/01_Instaladores/Meld-3.14.0-win32.msi") ,"http://192.168.97.5:9080/ContentManagerEARWeb/ContentManagerServiceSB12");
	}
}
