//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.ibm.bbva.reniec.consultapornombres;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConsultaPorNombresResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConsultaPorNombresResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numTotalCoindicencias" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listaDatosPersona" type="{http://pe.grupobbva.com/SIR/ents/body/consultaPorNombres}ListaDatosPersona" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultaPorNombresResponse", propOrder = {
    "numTotalCoindicencias",
    "listaDatosPersona"
})
public class ConsultaPorNombresResponse {

    protected String numTotalCoindicencias;
    protected ListaDatosPersona listaDatosPersona;

    /**
     * Gets the value of the numTotalCoindicencias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumTotalCoindicencias() {
        return numTotalCoindicencias;
    }

    /**
     * Sets the value of the numTotalCoindicencias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumTotalCoindicencias(String value) {
        this.numTotalCoindicencias = value;
    }

    /**
     * Gets the value of the listaDatosPersona property.
     * 
     * @return
     *     possible object is
     *     {@link ListaDatosPersona }
     *     
     */
    public ListaDatosPersona getListaDatosPersona() {
        return listaDatosPersona;
    }

    /**
     * Sets the value of the listaDatosPersona property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaDatosPersona }
     *     
     */
    public void setListaDatosPersona(ListaDatosPersona value) {
        this.listaDatosPersona = value;
    }

}
