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
 *         &lt;element name="bytesPDF" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="nombreOut" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "bytesPDF",
    "nombreOut"
})
@XmlRootElement(name = "convertirPDF")
public class ConvertirPDF {

    @XmlElement(required = true)
    protected byte[] bytesPDF;
    @XmlElement(required = true, nillable = true)
    protected String nombreOut;

    /**
     * Gets the value of the bytesPDF property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBytesPDF() {
        return bytesPDF;
    }

    /**
     * Sets the value of the bytesPDF property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBytesPDF(byte[] value) {
        this.bytesPDF = ((byte[]) value);
    }

    /**
     * Gets the value of the nombreOut property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreOut() {
        return nombreOut;
    }

    /**
     * Sets the value of the nombreOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreOut(String value) {
        this.nombreOut = value;
    }

}
