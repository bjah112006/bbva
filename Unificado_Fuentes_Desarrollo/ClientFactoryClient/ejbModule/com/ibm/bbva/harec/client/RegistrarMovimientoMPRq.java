//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.ibm.bbva.harec.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RegistrarMovimientoMPRq complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegistrarMovimientoMPRq">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="head" type="{http://grupobbva.com.pe/HarecService/}HeadRq"/>
 *         &lt;element name="body" type="{http://grupobbva.com.pe/HarecService/}BodyRegistrarMovimientoMPRq"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistrarMovimientoMPRq", propOrder = {
    "head",
    "body"
})
public class RegistrarMovimientoMPRq {

    @XmlElement(required = true)
    protected HeadRq head;
    @XmlElement(required = true)
    protected BodyRegistrarMovimientoMPRq body;

    /**
     * Gets the value of the head property.
     * 
     * @return
     *     possible object is
     *     {@link HeadRq }
     *     
     */
    public HeadRq getHead() {
        return head;
    }

    /**
     * Sets the value of the head property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeadRq }
     *     
     */
    public void setHead(HeadRq value) {
        this.head = value;
    }

    /**
     * Gets the value of the body property.
     * 
     * @return
     *     possible object is
     *     {@link BodyRegistrarMovimientoMPRq }
     *     
     */
    public BodyRegistrarMovimientoMPRq getBody() {
        return body;
    }

    /**
     * Sets the value of the body property.
     * 
     * @param value
     *     allowed object is
     *     {@link BodyRegistrarMovimientoMPRq }
     *     
     */
    public void setBody(BodyRegistrarMovimientoMPRq value) {
        this.body = value;
    }

}
