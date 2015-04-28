package com.ibm.bbva.ctacte.servicio.cm;

import com.ibm.bbva.ctacte.bean.DocumentoCM;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.dao.DocumentoCMDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.servicio.cm.bean.DocumentoExpediente;
import com.ibm.bbva.ctacte.servicio.cm.util.ContentManagerUtil;
import com.ibm.bbva.ctacte.util.EJBLocator;
import javax.jws.WebService;
import javax.jws.WebParam;


@WebService (targetNamespace="http://cm.servicio.ctacte.bbva.ibm.com/", serviceName="CEContentManagerServiceService", portName="CEContentManagerServicePort", wsdlLocation="WEB-INF/wsdl/CEContentManagerServiceService.wsdl")
public class CEContentManagerServiceDelegate{

    com.ibm.bbva.ctacte.servicio.cm.CEContentManagerService _cEContentManagerService = null;

    public DocumentoCM obtenerDocumentoCM (@WebParam(name="idExpediente") int idExpediente, @WebParam(name="idDocumento") int idDocumento) {
        return _cEContentManagerService.obtenerDocumentoCM(idExpediente,idDocumento);
    }

    public DocumentoCM obtenerDocumentoCMxCodigo (@WebParam(name="idExpediente") int idExpediente, @WebParam(name="codigoDocumento") String codigoDocumento) {
        return _cEContentManagerService.obtenerDocumentoCMxCodigo(idExpediente,codigoDocumento);
    }

    public String actualizarEstadoDocumento_CC (@WebParam(name="idExpediente") int idExpediente, @WebParam(name="idDocumento") int idDocumento, @WebParam(name="flagCM") String flagCM, @WebParam(name="idCM") int idCM, @WebParam(name="pidCM") String pidCM) {
        return _cEContentManagerService.actualizarEstadoDocumento_CC(idExpediente,idDocumento,flagCM,idCM,pidCM);
    }

    public String actualizarEstadoDocumentoxCodigo_CC (@WebParam(name="idExpediente") int idExpediente, @WebParam(name="codigoDocumento") String codigoDocumento, @WebParam(name="flagCM") String flagCM, @WebParam(name="idCM") int idCM, @WebParam(name="pidCM") String pidCM) {
        return _cEContentManagerService.actualizarEstadoDocumentoxCodigo_CC(idExpediente,codigoDocumento,flagCM,idCM,pidCM);
    }

    public String actualizarDocumentoExp_IdCMCopia (@WebParam(name="idExpediente") int idExpediente, @WebParam(name="codigoDocumento") String codigoDocumento, @WebParam(name="idCMCopia") int idCMCopia, @WebParam(name="pidCM") String pidCM) {
        return _cEContentManagerService.actualizarDocumentoExp_IdCMCopia(idExpediente,codigoDocumento,idCMCopia,pidCM);
    }

    public Integer obtenerDocumentoxCodigo_CC (@WebParam(name="idExpediente") int idExpediente, @WebParam(name="codigoDocumento") String codigoDocumento) {
        return _cEContentManagerService.obtenerDocumentoxCodigo_CC(idExpediente,codigoDocumento);
    }

    public DocumentoExpediente obtenerDocumentoExpediente_CC (@WebParam(name="idExpediente") int idExpediente, @WebParam(name="codigoDocumento") String codigoDocumento) {
        return _cEContentManagerService.obtenerDocumentoExpediente_CC(idExpediente,codigoDocumento);
    }

    public CEContentManagerServiceDelegate() {
        _cEContentManagerService = new com.ibm.bbva.ctacte.servicio.cm.CEContentManagerService(); }

}