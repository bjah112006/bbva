//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.cm.service.impl.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "findResponse", namespace = "http://impl.service.cm.bbva.ibm.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findResponse", namespace = "http://impl.service.cm.bbva.ibm.com/")
public class FindResponse {

    @XmlElement(name = "return", namespace = "")
    private com.ibm.bbva.cm.bean.Documento _return;

    /**
     * 
     * @return
     *     returns Documento
     */
    public com.ibm.bbva.cm.bean.Documento getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(com.ibm.bbva.cm.bean.Documento _return) {
        this._return = _return;
    }

}
