//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.service.business.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "pautasClasificacionMemoria", namespace = "http://business.service.bbva.ibm.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pautasClasificacionMemoria", namespace = "http://business.service.bbva.ibm.com/", propOrder = {
    "idTipoOferta",
    "idEstadoCivilTitular",
    "idPersonaTitular",
    "idPersonaConyuge",
    "sbsTitular",
    "sbsConyuge"
})
public class PautasClasificacionMemoria {

    @XmlElement(name = "idTipoOferta", namespace = "")
    private Long idTipoOferta;
    @XmlElement(name = "idEstadoCivilTitular", namespace = "")
    private Integer idEstadoCivilTitular;
    @XmlElement(name = "idPersonaTitular", namespace = "")
    private Long idPersonaTitular;
    @XmlElement(name = "idPersonaConyuge", namespace = "")
    private Long idPersonaConyuge;
    @XmlElement(name = "sbsTitular", namespace = "")
    private Double sbsTitular;
    @XmlElement(name = "sbsConyuge", namespace = "")
    private Double sbsConyuge;

    /**
     * 
     * @return
     *     returns Long
     */
    public Long getIdTipoOferta() {
        return this.idTipoOferta;
    }

    /**
     * 
     * @param idTipoOferta
     *     the value for the idTipoOferta property
     */
    public void setIdTipoOferta(Long idTipoOferta) {
        this.idTipoOferta = idTipoOferta;
    }

    /**
     * 
     * @return
     *     returns Integer
     */
    public Integer getIdEstadoCivilTitular() {
        return this.idEstadoCivilTitular;
    }

    /**
     * 
     * @param idEstadoCivilTitular
     *     the value for the idEstadoCivilTitular property
     */
    public void setIdEstadoCivilTitular(Integer idEstadoCivilTitular) {
        this.idEstadoCivilTitular = idEstadoCivilTitular;
    }

    /**
     * 
     * @return
     *     returns Long
     */
    public Long getIdPersonaTitular() {
        return this.idPersonaTitular;
    }

    /**
     * 
     * @param idPersonaTitular
     *     the value for the idPersonaTitular property
     */
    public void setIdPersonaTitular(Long idPersonaTitular) {
        this.idPersonaTitular = idPersonaTitular;
    }

    /**
     * 
     * @return
     *     returns Long
     */
    public Long getIdPersonaConyuge() {
        return this.idPersonaConyuge;
    }

    /**
     * 
     * @param idPersonaConyuge
     *     the value for the idPersonaConyuge property
     */
    public void setIdPersonaConyuge(Long idPersonaConyuge) {
        this.idPersonaConyuge = idPersonaConyuge;
    }

    /**
     * 
     * @return
     *     returns Double
     */
    public Double getSbsTitular() {
        return this.sbsTitular;
    }

    /**
     * 
     * @param sbsTitular
     *     the value for the sbsTitular property
     */
    public void setSbsTitular(Double sbsTitular) {
        this.sbsTitular = sbsTitular;
    }

    /**
     * 
     * @return
     *     returns Double
     */
    public Double getSbsConyuge() {
        return this.sbsConyuge;
    }

    /**
     * 
     * @param sbsConyuge
     *     the value for the sbsConyuge property
     */
    public void setSbsConyuge(Double sbsConyuge) {
        this.sbsConyuge = sbsConyuge;
    }

}
