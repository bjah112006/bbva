//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.service.business;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for actualizarDocumentoExpTC complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="actualizarDocumentoExpTC">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objDocumentoCM" type="{http://business.service.bbva.ibm.com/}documentoCM" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actualizarDocumentoExpTC", propOrder = {
    "objDocumentoCM"
})
public class ActualizarDocumentoExpTC {

    protected DocumentoCM objDocumentoCM;

    /**
     * Gets the value of the objDocumentoCM property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentoCM }
     *     
     */
    public DocumentoCM getObjDocumentoCM() {
        return objDocumentoCM;
    }

    /**
     * Sets the value of the objDocumentoCM property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentoCM }
     *     
     */
    public void setObjDocumentoCM(DocumentoCM value) {
        this.objDocumentoCM = value;
    }

}
