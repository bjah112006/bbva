package test;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import com.bbva.content.manager.ws.client.ContentManagerWSSOAPProxy;
import com.bbva.content.manager.ws.client.DocumentoRequest;
import com.bbva.content.manager.ws.client.InsertarRequest;
import com.bbva.content.manager.ws.client.InsertarResponse;
import com.ibm.bbva.cm.support.impl.DateHelper;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		URL wsdlLocation = null;
		try {
			wsdlLocation = new URL("http://118.180.188.27:80/BBVA_ContentManager/ContentManagerWS?wsdl");
			ContentManagerWSSOAPProxy proxy = new ContentManagerWSSOAPProxy(wsdlLocation);
			DocumentoRequest documento = new DocumentoRequest();
			
			File file= new File("D:\\jquedena\\pdf-test.pdf");
			FileInputStream input = new FileInputStream(file);
			Long size = file.length();
			byte[] bytes = new byte[size.intValue()]; 
			input.read(bytes);
			input.close();			

			documento.setId(1);
			documento.setCodigoCliente("11110000");
			documento.setNumDOI("42552617");
			documento.setTipoDOI("R"); // Longitud 1
			documento.setTipo("TS0T");
			documento.setMandatorio("0");
			documento.setNombreArchivo("demooooo");
			documento.setOrigen("DDD"); // Longitud 3
			documento.setFechaExpiracion(null);			
			documento.setContenido(bytes);

 			documento.setFechaCreacion(DateHelper.toXMLGregorianCalendar(new Date()));
			InsertarRequest parameters = new InsertarRequest(); 
			parameters.getBody().getDocumento().add(documento);		
			InsertarResponse response = proxy.insertar(parameters);
			
//			EliminarRequest parameters = new EliminarRequest();
//			parameters.getBody().getDocumento().add(documento);
//			EliminarResponse response = proxy.eliminar(parameters);
			
//			ActualizarRequest parameters = new ActualizarRequest();
//			documento.setFechaCreacion(DateHelper.toXMLGregorianCalendar(new Date()));
//			parameters.getBody().getDocumento().add(documento);
//			ActualizarResponse response = proxy.actualizar(parameters);
		
			System.out.println(response.getHeader().getMensaje());
			// System.out.println(response.getBody().getDocumentos().get(0).getUrlContent());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
