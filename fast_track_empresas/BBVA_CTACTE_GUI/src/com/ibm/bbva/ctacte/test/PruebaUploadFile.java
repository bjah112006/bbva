package com.ibm.bbva.ctacte.test;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.bbva.cm.service.ContentServiceImplPortProxy;
import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.ctacte.constantes.ConstantesBusiness;

public class PruebaUploadFile {
	private static final Logger LOG = LoggerFactory.getLogger(PruebaUploadFile.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			File f=new File("C:\\Users\\avillarm\\Downloads\\rename_me.zip");
			File f=new File("C:\\Users\\avillarm\\Downloads\\hoja test 05 49.pdf");
//			File f=new File("C:\\Users\\avillarm\\Downloads\\Reglas.pdf");
			
			byte[] bytes =  new byte[(int) f.length()];
			LOG.info("ARCHIVO EN MEMORIA");
			Documento documento = new Documento();
			documento.setCodCliente("77783117");
			documento.setContenido(bytes);
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(new Date());
			XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			documento.setFechaCreacion(date2);		
			documento.setMandatorio(true);
			documento.setNombreArchivo(f.getName());
			documento.setOrigen(ConstantesBusiness.ORIGEN_ARCHIVO);
			documento.setTipo(ConstantesBusiness.TIPO_ARCHIVO); 
			documento.setTipoDoi("R");
			documento.setNumDoi("20788962356");

//			String url	= (String) ParametrosSistema.getInstance().getProperties(ParametrosSistema.CONF).get(ConstantesParametros.SERVICIO_CONTENT);
			ContentServiceImplPortProxy serviceIBMBBVA = new ContentServiceImplPortProxy();
//			
//			ContentServiceImplService serviceIBMBBVA = new ContentServiceImplService();
//			BindingProvider provider = (BindingProvider) serviceIBMBBVA.getContentServiceImplPort();
//			Map<String, Object> map = provider.getRequestContext();
//			map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://127.0.0.1:9081/BBVA_CE_CM_WAS/ContentServiceImplService");
//			LOG.info("ARCHIVO ANTES DE REGISTRAR");
//			serviceIBMBBVA.getContentServiceImplPort().insertPID(documento);
//			LOG.info("REGISTRO");
//			documento = serviceIBMBBVA.getContentServiceImplPort().find(documento);
			
			serviceIBMBBVA._getDescriptor().setEndpoint("http://127.0.0.1:9080/BBVA_CE_CM_WAS/ContentServiceImplService");
			serviceIBMBBVA._getDescriptor().setMTOMEnabled(true);
			LOG.info("ARCHIVO ANTES DE REGISTRAR");
			
			LOG.info("REGISTRO {}", serviceIBMBBVA.insertPID(documento));
			// documento = serviceIBMBBVA.find(documento);

			// LOG.info("SIN ERRORES URL:"+documento.getUrlContent());
		} catch (Exception e) {
			LOG.info("CON ERRORES");
			LOG.error("", e);
		}

	}

}
