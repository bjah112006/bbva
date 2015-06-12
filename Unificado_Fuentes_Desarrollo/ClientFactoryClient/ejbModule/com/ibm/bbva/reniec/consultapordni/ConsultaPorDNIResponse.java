//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.ibm.bbva.reniec.consultapordni;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConsultaPorDNIResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConsultaPorDNIResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuestaDatos" type="{http://pe.grupobbva.com/SIR/ents/body/consultaPorDNI}RespuestaDatos" minOccurs="0"/>
 *         &lt;element name="respuestaDNI" type="{http://pe.grupobbva.com/SIR/ents/body/consultaPorDNI}RespuestaDNI" minOccurs="0"/>
 *         &lt;element name="respuestaImagenes" type="{http://pe.grupobbva.com/SIR/ents/body/consultaPorDNI}RespuestaImagenes" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultaPorDNIResponse", propOrder = {
    "respuestaDatos",
    "respuestaDNI",
    "respuestaImagenes"
})
public class ConsultaPorDNIResponse {

    protected RespuestaDatos respuestaDatos;
    protected RespuestaDNI respuestaDNI;
    protected RespuestaImagenes respuestaImagenes;

    /**
     * Gets the value of the respuestaDatos property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaDatos }
     *     
     */
    public RespuestaDatos getRespuestaDatos() {
        return respuestaDatos;
    }

    /**
     * Sets the value of the respuestaDatos property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaDatos }
     *     
     */
    public void setRespuestaDatos(RespuestaDatos value) {
        this.respuestaDatos = value;
    }

    /**
     * Gets the value of the respuestaDNI property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaDNI }
     *     
     */
    public RespuestaDNI getRespuestaDNI() {
        return respuestaDNI;
    }

    /**
     * Sets the value of the respuestaDNI property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaDNI }
     *     
     */
    public void setRespuestaDNI(RespuestaDNI value) {
        this.respuestaDNI = value;
    }

    /**
     * Gets the value of the respuestaImagenes property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaImagenes }
     *     
     */
    public RespuestaImagenes getRespuestaImagenes() {
        return respuestaImagenes;
    }

    /**
     * Sets the value of the respuestaImagenes property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaImagenes }
     *     
     */
    public void setRespuestaImagenes(RespuestaImagenes value) {
        this.respuestaImagenes = value;
    }

}