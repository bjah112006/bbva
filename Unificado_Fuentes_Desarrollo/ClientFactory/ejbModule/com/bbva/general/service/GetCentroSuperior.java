//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.bbva.general.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoOficina" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codigoAgrupacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "codigoOficina",
    "codigoAgrupacion"
})
@XmlRootElement(name = "getCentroSuperior")
public class GetCentroSuperior {

    @XmlElement(required = true, nillable = true)
    protected String codigoOficina;
    @XmlElement(required = true, nillable = true)
    protected String codigoAgrupacion;

    /**
     * Gets the value of the codigoOficina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOficina() {
        return codigoOficina;
    }

    /**
     * Sets the value of the codigoOficina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOficina(String value) {
        this.codigoOficina = value;
    }

    /**
     * Gets the value of the codigoAgrupacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoAgrupacion() {
        return codigoAgrupacion;
    }

    /**
     * Sets the value of the codigoAgrupacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoAgrupacion(String value) {
        this.codigoAgrupacion = value;
    }

}