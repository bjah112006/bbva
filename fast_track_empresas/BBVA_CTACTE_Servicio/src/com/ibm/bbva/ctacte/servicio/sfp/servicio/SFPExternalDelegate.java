package com.ibm.bbva.ctacte.servicio.sfp.servicio;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import clientecontent.ClienteContent;
import com.ibm.bbva.cm.service.Documento;
import com.ibm.bbva.ctacte.bean.Cuenta;
import com.ibm.bbva.ctacte.bean.DocumentoExp;
import com.ibm.bbva.ctacte.bean.Expediente;
import com.ibm.bbva.ctacte.bean.ExpedienteCuenta;
import com.ibm.bbva.ctacte.bean.Participe;
import com.ibm.bbva.ctacte.dao.CuentaDAO;
import com.ibm.bbva.ctacte.dao.DocumentoExpDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteCuentaDAO;
import com.ibm.bbva.ctacte.dao.ExpedienteDAO;
import com.ibm.bbva.ctacte.dao.ParticipeDAO;
import com.ibm.bbva.ctacte.servicio.sfp.bean.CuentaSTD;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMActivacionOUTResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMActivacionResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMActivacionSETRequest;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMAsociarFirmaRequest;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMAsociarFirmaResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMRealizarBastanteoINResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMRealizarBastanteoSETRequest;
import com.ibm.bbva.ctacte.servicio.sfp.bean.IBMRealizarBastanteoSETResponse;
import com.ibm.bbva.ctacte.servicio.sfp.bean.ParticipeSTD;
import com.ibm.bbva.ctacte.servicio.sfp.util.Constantes;
import com.ibm.bbva.ctacte.servicio.sfp.util.SFPUtil;
import com.ibm.bbva.ctacte.util.EJBLocator;
import com.ibm.bbva.ctacte.util.ParametrosSistema;

import javax.jws.WebService;
import javax.jws.WebParam;


@WebService (targetNamespace="http://servicio.sfp.servicio.ctacte.bbva.ibm.com/", serviceName="SFPExternalService", portName="SFPExternalPort", wsdlLocation="WEB-INF/wsdl/SFPExternalService.wsdl")
public class SFPExternalDelegate{

    com.ibm.bbva.ctacte.servicio.sfp.servicio.SFPExternal _sFPExternal = null;

    public ParticipeSTD[] IBMAsociarFirmaGET (@WebParam(name="IDExpediente") String IDExpediente) {
        return _sFPExternal.IBMAsociarFirmaGET(IDExpediente);
    }

    public Documento CM_Obtener_documento_FRF (@WebParam(name="IDExpediente") String IDExpediente) {
        return _sFPExternal.CM_Obtener_documento_FRF(IDExpediente);
    }

    public IBMAsociarFirmaResponse IBMAsociarFirmaSET (@WebParam(name="IDExpediente") String IDExpediente, @WebParam(name="participes") IBMAsociarFirmaRequest[] participes) {
        return _sFPExternal.IBMAsociarFirmaSET(IDExpediente,participes);
    }

    public IBMRealizarBastanteoINResponse IBMRealizarBastanteoGET (@WebParam(name="IDExpediente") String IDExpediente) {
        return _sFPExternal.IBMRealizarBastanteoGET(IDExpediente);
    }

    public IBMRealizarBastanteoSETResponse IBMRealizarBastanteoSET (@WebParam(name="request") IBMRealizarBastanteoSETRequest request) {
        return _sFPExternal.IBMRealizarBastanteoSET(request);
    }

    public IBMActivacionResponse IBMActivacionGET (@WebParam(name="IDExpediente") String IDExpediente) {
        return _sFPExternal.IBMActivacionGET(IDExpediente);
    }

    public IBMActivacionOUTResponse IBMActivacionSET (@WebParam(name="IDExpediente") String IDExpediente, @WebParam(name="participes") IBMActivacionSETRequest[] participes) {
        return _sFPExternal.IBMActivacionSET(IDExpediente,participes);
    }

    public SFPExternalDelegate() {
        _sFPExternal = new com.ibm.bbva.ctacte.servicio.sfp.servicio.SFPExternal(); }

}