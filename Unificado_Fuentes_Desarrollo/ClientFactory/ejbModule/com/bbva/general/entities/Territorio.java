//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.bbva.general.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Territorio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Territorio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoTerritorio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descripcionTerritorio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Territorio", propOrder = {
    "codigoTerritorio",
    "descripcionTerritorio"
})
public class Territorio {

    @XmlElement(required = true, nillable = true)
    protected String codigoTerritorio;
    @XmlElement(required = true, nillable = true)
    protected String descripcionTerritorio;

    /**
     * Gets the value of the codigoTerritorio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoTerritorio() {
        return codigoTerritorio;
    }

    /**
     * Sets the value of the codigoTerritorio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoTerritorio(String value) {
        this.codigoTerritorio = value;
    }

    /**
     * Gets the value of the descripcionTerritorio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionTerritorio() {
        return descripcionTerritorio;
    }

    /**
     * Sets the value of the descripcionTerritorio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionTerritorio(String value) {
        this.descripcionTerritorio = value;
    }

}
