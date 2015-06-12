
package com.ibm.bbva.app.servicio.web;

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
 *         &lt;element name="codigoDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idExpediente" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "codigoDocumento",
    "idExpediente"
})
@XmlRootElement(name = "CM_Obtener_documentoxCodigo")
public class CMObtenerDocumentoxCodigo {

    @XmlElement(required = true, nillable = true)
    protected String codigoDocumento;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer idExpediente;

    /**
     * Gets the value of the codigoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    /**
     * Sets the value of the codigoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoDocumento(String value) {
        this.codigoDocumento = value;
    }

    /**
     * Gets the value of the idExpediente property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdExpediente() {
        return idExpediente;
    }

    /**
     * Sets the value of the idExpediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdExpediente(Integer value) {
        this.idExpediente = value;
    }

}
