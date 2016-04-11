package com.ibm.bbva.cm.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.CalendarConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ibm.bbva.cm.bean.Documento;
import com.ibm.bbva.cm.bean.DocumentoPid;
import com.ibm.bbva.cm.dao.DocumentDAO;
import com.ibm.bbva.cm.dao.impl.DocumentDAOImpl;
import com.ibm.bbva.cm.exception.ICMDocumentNotFoundException;
import com.ibm.bbva.cm.exception.ICMException;
import com.ibm.bbva.cm.service.ContentService;
import com.ibm.bbva.cm.util.Util;
import javax.jws.WebService;
import javax.jws.WebParam;


@WebService (targetNamespace="http://impl.service.cm.bbva.ibm.com/", serviceName="ContentServiceImplService", portName="ContentServiceImplPort", wsdlLocation="WEB-INF/wsdl/ContentServiceImplService.wsdl")
public class ContentServiceImplDelegate{

    com.ibm.bbva.cm.service.impl.ContentServiceImpl _contentServiceImpl = null;

    public ContentServiceImplDelegate() {
        _contentServiceImpl = new com.ibm.bbva.cm.service.impl.ContentServiceImpl(); }

    public String delete (@WebParam(name="documento") Documento documento) {
        return _contentServiceImpl.delete(documento);
    }

    public String deleteAll (@WebParam(name="documentos") Documento[] documentos) {
        return _contentServiceImpl.deleteAll(documentos);
    }

    public Documento find (@WebParam(name="documento") Documento documento) {
        return _contentServiceImpl.find(documento);
    }

    public Documento[] findAll (@WebParam(name="documento") Documento documento) {
        return _contentServiceImpl.findAll(documento);
    }

    public Documento findAsImage (@WebParam(name="documento") Documento documento, @WebParam(name="mimeType") String mimeType) {
        return _contentServiceImpl.findAsImage(documento,mimeType);
    }

    public String insert (@WebParam(name="documento") Documento documento) {
        return _contentServiceImpl.insert(documento);
    }

    public String insert_PID (@WebParam(name="documento") Documento documento) {
        return _contentServiceImpl.insert_PID(documento);
    }

    public String insertAll (@WebParam(name="documentos") Documento[] documentos) {
        return _contentServiceImpl.insertAll(documentos);
    }

    public String update (@WebParam(name="documento") Documento documento) {
        return _contentServiceImpl.update(documento);
    }

    public String actualizarTipoDoc (@WebParam(name="id") Integer id, @WebParam(name="tipoDoc") String tipoDoc) {
        return _contentServiceImpl.actualizarTipoDoc(id,tipoDoc);
    }

}