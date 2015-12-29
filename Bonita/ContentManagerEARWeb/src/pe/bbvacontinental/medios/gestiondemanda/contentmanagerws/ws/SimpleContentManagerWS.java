package pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;

import org.apache.log4j.Logger;

import pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.Parametros.Parametro;

@WebService(endpointInterface = "pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.ContentManagerWSPortType", targetNamespace = "http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws", serviceName = "ContentManagerWSService", portName = "ebsContentManagerSB11")
@MTOM(enabled = true, threshold = 2048)
public class SimpleContentManagerWS implements ContentManagerWSPortType {

	private static final Logger LOG = Logger.getLogger(SimpleContentManagerWS.class);
	private static final int BUFFER_SIZE = 1024;
	
	@Override
	public InsertarDocResponse insertarDoc(InsertarDocRequest request) {
		long t1 = System.currentTimeMillis();
		// System.out.print("t1: [" + t1 + " ms]");
		LOG.info("Bytes: [" + request.getFileData().length + " byte]"); 
		
		InsertarDocResponse response = new InsertarDocResponse();
		response.setAuditResponse(new AuditResponse());
		response.setListaParametrosResponse(new Parametros());
		response.getAuditResponse().setCodigoRespuesta("0");
		response.getAuditResponse().setIdTransaccion(UUID.randomUUID().toString());
		response.getAuditResponse().setMensajeRespuesta("Proceso completado con exito");
		response.getListaParametrosResponse().getParametro().add(new Parametros.Parametro("estado", "OK"));
		String nombreArchivo = "";
		for(Parametro p : request.getListaParametrosRequest().getParametro()) {
			if(p.getNombre().equalsIgnoreCase("nombreArchivo")) {
				nombreArchivo = p.getNombre();
				break;
			}
		}
		
		ByteArrayInputStream bais = new ByteArrayInputStream(request.getFileData());
		try {
			nombreArchivo = "/pr/ebs/" + response.getAuditResponse().getIdTransaccion() + nombreArchivo;
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(nombreArchivo), BUFFER_SIZE);
			byte [] bytes = new byte [1024];
			int available  = -1;
			while((available = bais.read(bytes)) > 0) {   
				bos.write(bytes, 0, available);
				bos.flush();
			}
			bos.flush();
			bos.close();
			
			response.getListaParametrosResponse().getParametro().add(new Parametros.Parametro("documento_url", "http://192.168.97.5:9080/ContentManagerEARWeb/download?url=" + nombreArchivo));
		} catch (Exception e) {
			response.getAuditResponse().setCodigoRespuesta("1");
			response.getAuditResponse().setMensajeRespuesta(exceptionToString(e));
		}
		long t2 = System.currentTimeMillis();
		// System.out.print("t2: [" + t2 + " ms]");
		LOG.info("V: [" + (t2 - t1) + " ms]");
		return response;
	}

	@Override
	public ObtenerDocResponse obtenerDoc(ObtenerDocRequest request) {
		return null;
	}

	private String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter(0);
        PrintWriter out = new PrintWriter(sw);
        e.printStackTrace(out);

        return out.toString();
    }
}
