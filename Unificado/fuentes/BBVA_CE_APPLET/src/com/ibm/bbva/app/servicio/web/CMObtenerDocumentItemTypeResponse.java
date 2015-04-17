
package com.ibm.bbva.app.servicio.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.ibm.bbva.cm.bean.Documento;


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
 *         &lt;element name="CM_ObtenerDocumentItemTypeReturn" type="{http://bean.cm.bbva.ibm.com}Documento"/>
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
    "cmObtenerDocumentItemTypeReturn"
})
@XmlRootElement(name = "CM_ObtenerDocumentItemTypeResponse")
public class CMObtenerDocumentItemTypeResponse {

    @XmlElement(name = "CM_ObtenerDocumentItemTypeReturn", required = true, nillable = true)
    protected Documento cmObtenerDocumentItemTypeReturn;

    /**
     * Gets the value of the cmObtenerDocumentItemTypeReturn property.
     * 
     * @return
     *     possible object is
     *     {@link Documento }
     *     
     */
    public Documento getCMObtenerDocumentItemTypeReturn() {
        return cmObtenerDocumentItemTypeReturn;
    }

    /**
     * Sets the value of the cmObtenerDocumentItemTypeReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Documento }
     *     
     */
    public void setCMObtenerDocumentItemTypeReturn(Documento value) {
        this.cmObtenerDocumentItemTypeReturn = value;
    }

}
