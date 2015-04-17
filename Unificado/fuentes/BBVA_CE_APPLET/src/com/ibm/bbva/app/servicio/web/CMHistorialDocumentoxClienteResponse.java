
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
 *         &lt;element name="CM_historialDocumentoxClienteReturn" type="{http://web.servicio.ctacte.bbva.ibm.com}ArrayOf_412342274_nillable_Documento"/>
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
    "cmHistorialDocumentoxClienteReturn"
})
@XmlRootElement(name = "CM_historialDocumentoxClienteResponse")
public class CMHistorialDocumentoxClienteResponse {

    @XmlElement(name = "CM_historialDocumentoxClienteReturn", required = true, nillable = true)
    protected ArrayOf412342274NillableDocumento cmHistorialDocumentoxClienteReturn;

    /**
     * Gets the value of the cmHistorialDocumentoxClienteReturn property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOf412342274NillableDocumento }
     *     
     */
    public ArrayOf412342274NillableDocumento getCMHistorialDocumentoxClienteReturn() {
        return cmHistorialDocumentoxClienteReturn;
    }

    /**
     * Sets the value of the cmHistorialDocumentoxClienteReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOf412342274NillableDocumento }
     *     
     */
    public void setCMHistorialDocumentoxClienteReturn(ArrayOf412342274NillableDocumento value) {
        this.cmHistorialDocumentoxClienteReturn = value;
    }

}
