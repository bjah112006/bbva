//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.cm.iice.service;

import java.io.Serializable;
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
 *         &lt;element name="documentos" type="{http://impl.service.cm.bbva.ibm.com}ArrayOf_1659910637_nillable_Documento"/>
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
    "documentos"
})
@XmlRootElement(name = "insertAll")
public class InsertAll
    implements Serializable
{

    @XmlElement(required = true, nillable = true)
    protected ArrayOf1659910637NillableDocumento documentos;

    /**
     * Gets the value of the documentos property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOf1659910637NillableDocumento }
     *     
     */
    public ArrayOf1659910637NillableDocumento getDocumentos() {
        return documentos;
    }

    /**
     * Sets the value of the documentos property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOf1659910637NillableDocumento }
     *     
     */
    public void setDocumentos(ArrayOf1659910637NillableDocumento value) {
        this.documentos = value;
    }

}
