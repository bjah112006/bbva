//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.ibm.bbva.service.business.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "lineaMaximaBD", namespace = "http://business.service.bbva.ibm.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lineaMaximaBD", namespace = "http://business.service.bbva.ibm.com/")
public class LineaMaximaBD {

    @XmlElement(name = "idExpediente", namespace = "")
    private Long idExpediente;

    /**
     * 
     * @return
     *     returns Long
     */
    public Long getIdExpediente() {
        return this.idExpediente;
    }

    /**
     * 
     * @param idExpediente
     *     the value for the idExpediente property
     */
    public void setIdExpediente(Long idExpediente) {
        this.idExpediente = idExpediente;
    }

}
