//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:27 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:17 AM(foreman)-)
//


package com.ibm.bbva.convert;

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
 *         &lt;element name="convertirPDFReturn" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
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
    "convertirPDFReturn"
})
@XmlRootElement(name = "convertirPDFResponse")
public class ConvertirPDFResponse {

    @XmlElement(required = true)
    protected byte[] convertirPDFReturn;

    /**
     * Gets the value of the convertirPDFReturn property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getConvertirPDFReturn() {
        return convertirPDFReturn;
    }

    /**
     * Sets the value of the convertirPDFReturn property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setConvertirPDFReturn(byte[] value) {
        this.convertirPDFReturn = ((byte[]) value);
    }

}
