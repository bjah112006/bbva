//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-07/05/2013 05:25 AM(foreman)-)
//


package com.ibm.bbva.service.business.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "actualizarDocumentoExpTC", namespace = "http://business.service.bbva.ibm.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actualizarDocumentoExpTC", namespace = "http://business.service.bbva.ibm.com/")
public class ActualizarDocumentoExpTC {

    @XmlElement(name = "objDocumentoCM", namespace = "")
    private com.ibm.bbva.service.bean.DocumentoCM objDocumentoCM;

    /**
     * 
     * @return
     *     returns DocumentoCM
     */
    public com.ibm.bbva.service.bean.DocumentoCM getObjDocumentoCM() {
        return this.objDocumentoCM;
    }

    /**
     * 
     * @param objDocumentoCM
     *     the value for the objDocumentoCM property
     */
    public void setObjDocumentoCM(com.ibm.bbva.service.bean.DocumentoCM objDocumentoCM) {
        this.objDocumentoCM = objDocumentoCM;
    }

}