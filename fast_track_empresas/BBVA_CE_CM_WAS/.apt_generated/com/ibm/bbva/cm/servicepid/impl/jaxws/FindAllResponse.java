//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.cm.servicepid.impl.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "findAllResponse", namespace = "http://impl.servicepid.cm.bbva.ibm.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findAllResponse", namespace = "http://impl.servicepid.cm.bbva.ibm.com/")
public class FindAllResponse {

    @XmlElement(name = "return", namespace = "", nillable = true)
    private com.ibm.bbva.cm.bean.DocumentoPid[] _return;

    /**
     * 
     * @return
     *     returns DocumentoPid[]
     */
    public com.ibm.bbva.cm.bean.DocumentoPid[] getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(com.ibm.bbva.cm.bean.DocumentoPid[] _return) {
        this._return = _return;
    }

}
