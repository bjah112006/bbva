
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
 *         &lt;element name="convertirTIFReturn" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
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
    "convertirTIFReturn"
})
@XmlRootElement(name = "convertirTIFResponse")
public class ConvertirTIFResponse {

    @XmlElement(required = true)
    protected byte[] convertirTIFReturn;

    /**
     * Gets the value of the convertirTIFReturn property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getConvertirTIFReturn() {
        return convertirTIFReturn;
    }

    /**
     * Sets the value of the convertirTIFReturn property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setConvertirTIFReturn(byte[] value) {
        this.convertirTIFReturn = ((byte[]) value);
    }

}
