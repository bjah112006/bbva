//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.service.business.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "delegacionRiesgos", namespace = "http://business.service.bbva.ibm.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "delegacionRiesgos", namespace = "http://business.service.bbva.ibm.com/", propOrder = {
    "idTipoCategoria",
    "idExpediente"
})
public class DelegacionRiesgos {

    @XmlElement(name = "idTipoCategoria", namespace = "")
    private Integer idTipoCategoria;
    @XmlElement(name = "idExpediente", namespace = "")
    private Long idExpediente;

    /**
     * 
     * @return
     *     returns Integer
     */
    public Integer getIdTipoCategoria() {
        return this.idTipoCategoria;
    }

    /**
     * 
     * @param idTipoCategoria
     *     the value for the idTipoCategoria property
     */
    public void setIdTipoCategoria(Integer idTipoCategoria) {
        this.idTipoCategoria = idTipoCategoria;
    }

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
