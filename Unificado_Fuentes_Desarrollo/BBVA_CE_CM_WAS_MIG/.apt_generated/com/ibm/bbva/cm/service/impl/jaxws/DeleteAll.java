//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.cm.service.impl.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "deleteAll", namespace = "http://impl.service.cm.bbva.ibm.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteAll", namespace = "http://impl.service.cm.bbva.ibm.com/")
public class DeleteAll {

    @XmlElement(name = "documentos", namespace = "", nillable = true)
    private com.ibm.bbva.cm.bean.Documento[] documentos;

    /**
     * 
     * @return
     *     returns Documento[]
     */
    public com.ibm.bbva.cm.bean.Documento[] getDocumentos() {
        return this.documentos;
    }

    /**
     * 
     * @param documentos
     *     the value for the documentos property
     */
    public void setDocumentos(com.ibm.bbva.cm.bean.Documento[] documentos) {
        this.documentos = documentos;
    }

}
