//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.cm.servicepid.impl.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "findAll", namespace = "http://impl.servicepid.cm.bbva.ibm.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findAll", namespace = "http://impl.servicepid.cm.bbva.ibm.com/")
public class FindAll {

    @XmlElement(name = "documento", namespace = "")
    private com.ibm.bbva.cm.bean.DocumentoPid documento;

    /**
     * 
     * @return
     *     returns DocumentoPid
     */
    public com.ibm.bbva.cm.bean.DocumentoPid getDocumento() {
        return this.documento;
    }

    /**
     * 
     * @param documento
     *     the value for the documento property
     */
    public void setDocumento(com.ibm.bbva.cm.bean.DocumentoPid documento) {
        this.documento = documento;
    }

}
