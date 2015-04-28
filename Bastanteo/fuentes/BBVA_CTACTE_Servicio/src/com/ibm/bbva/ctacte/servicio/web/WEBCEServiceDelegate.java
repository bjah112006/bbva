package com.ibm.bbva.ctacte.servicio.web;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import clientecontent.ClienteContent;
import clientecontent.ClienteContentCC;
import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.cm.servicepid.DocumentoPid;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.comun.ConstantesParametros;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.servicio.util.ListSorter;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;
import javax.jws.WebService;
import javax.jws.WebParam;


@WebService (targetNamespace="http://web.servicio.ctacte.bbva.ibm.com/", serviceName="WEBCEServiceService", portName="WEBCEServicePort", wsdlLocation="WEB-INF/wsdl/WEBCEServiceService.wsdl")
public class WEBCEServiceDelegate{

    com.ibm.bbva.ctacte.servicio.web.WEBCEService _wEBCEService = null;

    public Documento CM_Obtener_documento (@WebParam(name="idDocumento") Integer idDocumento, @WebParam(name="idExpediente") Integer idExpediente) {
        return _wEBCEService.CM_Obtener_documento(idDocumento,idExpediente);
    }

    public Documento CM_Obtener_documentoxCodigo (@WebParam(name="codigoDocumento") String codigoDocumento, @WebParam(name="idExpediente") Integer idExpediente) {
        return _wEBCEService.CM_Obtener_documentoxCodigo(codigoDocumento,idExpediente);
    }

    public Documento CM_ObtenerDocumentItemType (@WebParam(name="codigoDocumento") String codigoDocumento, @WebParam(name="idExpediente") Integer idExpediente) {
        return _wEBCEService.CM_ObtenerDocumentItemType(codigoDocumento,idExpediente);
    }

    public Documento[] CM_historialDocumentoxCliente (@WebParam(name="codigoDocumento") String codigoDocumento, @WebParam(name="codCliente") String codCliente, @WebParam(name="cantDocumento") int cantDocumento) {
        return _wEBCEService.CM_historialDocumentoxCliente(codigoDocumento,codCliente,cantDocumento);
    }

    public Documento CM_Obtener_documentoxNombreArchivo (@WebParam(name="tipoDocumento") String tipoDocumento, @WebParam(name="nombreArchivo") String nombreArchivo, @WebParam(name="idCM") Integer idCM) {
        return _wEBCEService.CM_Obtener_documentoxNombreArchivo(tipoDocumento,nombreArchivo,idCM);
    }

    public List<DocumentoPid> CM_buscarVistaUnica (@WebParam(name="codCliente") String codCliente) {
        return _wEBCEService.CM_buscarVistaUnica(codCliente);
    }

    public WEBCEServiceDelegate() {
        _wEBCEService = new com.ibm.bbva.ctacte.servicio.web.WEBCEService(); }

}