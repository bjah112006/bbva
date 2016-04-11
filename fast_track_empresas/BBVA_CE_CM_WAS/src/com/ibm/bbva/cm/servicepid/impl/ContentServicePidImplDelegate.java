package com.ibm.bbva.cm.servicepid.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ibm.bbva.cm.bean.DocumentoPid;
import com.ibm.bbva.cm.dao.DocumentDAO;
import com.ibm.bbva.cm.dao.impl.DocumentDAOImpl;
import com.ibm.bbva.cm.exception.ICMDocumentNotFoundException;
import com.ibm.bbva.cm.exception.ICMException;
import com.ibm.bbva.cm.service.impl.BaseServiceImpl;
import com.ibm.bbva.cm.servicepid.ContentServicePid;
import javax.jws.WebService;
import javax.jws.WebParam;


@WebService (targetNamespace="http://impl.servicepid.cm.bbva.ibm.com/", serviceName="ContentServicePidImplService", portName="ContentServicePidImplPort", wsdlLocation="WEB-INF/wsdl/ContentServicePidImplService.wsdl")
public class ContentServicePidImplDelegate{

    com.ibm.bbva.cm.servicepid.impl.ContentServicePidImpl _contentServicePidImpl = null;

    public ContentServicePidImplDelegate() {
        _contentServicePidImpl = new com.ibm.bbva.cm.servicepid.impl.ContentServicePidImpl(); }

    public DocumentoPid[] findAll (@WebParam(name="documento") DocumentoPid documento) {
        return _contentServicePidImpl.findAll(documento);
    }

}