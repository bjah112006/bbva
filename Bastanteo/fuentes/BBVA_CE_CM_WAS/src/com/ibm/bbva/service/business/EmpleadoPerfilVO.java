//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.service.business;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for empleadoPerfilVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="empleadoPerfilVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descripcionPerfil" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="empleadoVO" type="{http://business.service.bbva.ibm.com/}empleadoVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "empleadoPerfilVO", propOrder = {
    "descripcionPerfil",
    "empleadoVO"
})
public class EmpleadoPerfilVO {

    protected String descripcionPerfil;
    protected EmpleadoVO empleadoVO;

    /**
     * Gets the value of the descripcionPerfil property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionPerfil() {
        return descripcionPerfil;
    }

    /**
     * Sets the value of the descripcionPerfil property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionPerfil(String value) {
        this.descripcionPerfil = value;
    }

    /**
     * Gets the value of the empleadoVO property.
     * 
     * @return
     *     possible object is
     *     {@link EmpleadoVO }
     *     
     */
    public EmpleadoVO getEmpleadoVO() {
        return empleadoVO;
    }

    /**
     * Sets the value of the empleadoVO property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmpleadoVO }
     *     
     */
    public void setEmpleadoVO(EmpleadoVO value) {
        this.empleadoVO = value;
    }

}
