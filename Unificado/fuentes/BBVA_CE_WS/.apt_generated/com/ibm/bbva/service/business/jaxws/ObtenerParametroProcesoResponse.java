//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.ibm.bbva.service.business.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "obtenerParametroProcesoResponse", namespace = "http://business.service.bbva.ibm.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerParametroProcesoResponse", namespace = "http://business.service.bbva.ibm.com/")
public class ObtenerParametroProcesoResponse {

    @XmlElement(name = "return", namespace = "")
    private com.ibm.bbva.service.bean.ParametrosProcesoDTO _return;

    /**
     * 
     * @return
     *     returns ParametrosProcesoDTO
     */
    public com.ibm.bbva.service.bean.ParametrosProcesoDTO getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(com.ibm.bbva.service.bean.ParametrosProcesoDTO _return) {
        this._return = _return;
    }

}
