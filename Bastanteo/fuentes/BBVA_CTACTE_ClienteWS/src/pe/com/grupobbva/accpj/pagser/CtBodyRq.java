//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package pe.com.grupobbva.accpj.pagser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ctBodyRq complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ctBodyRq">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numCuenta" type="{http://grupobbva.com.pe/accpj/pagser/}stNumCuenta"/>
 *         &lt;element name="descCargo" type="{http://grupobbva.com.pe/accpj/pagser/}stDescCargo"/>
 *         &lt;element name="codCentralCliente" type="{http://grupobbva.com.pe/accpj/pagser/}stCodCentral"/>
 *         &lt;element name="registroUsuario" type="{http://grupobbva.com.pe/accpj/pagser/}stRegUsu"/>
 *         &lt;element name="oficinaUsuario" type="{http://grupobbva.com.pe/accpj/pagser/}stOfiUsu"/>
 *         &lt;element name="centroContable" type="{http://grupobbva.com.pe/accpj/pagser/}stCenCon"/>
 *         &lt;element name="referencia" type="{http://grupobbva.com.pe/accpj/pagser/}stRef"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctBodyRq", propOrder = {
    "numCuenta",
    "descCargo",
    "codCentralCliente",
    "registroUsuario",
    "oficinaUsuario",
    "centroContable",
    "referencia"
})
public class CtBodyRq {

    @XmlElement(required = true)
    protected String numCuenta;
    @XmlElement(required = true)
    protected String descCargo;
    @XmlElement(required = true)
    protected String codCentralCliente;
    @XmlElement(required = true)
    protected String registroUsuario;
    @XmlElement(required = true)
    protected String oficinaUsuario;
    @XmlElement(required = true)
    protected String centroContable;
    @XmlElement(required = true)
    protected String referencia;

    /**
     * Gets the value of the numCuenta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumCuenta() {
        return numCuenta;
    }

    /**
     * Sets the value of the numCuenta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumCuenta(String value) {
        this.numCuenta = value;
    }

    /**
     * Gets the value of the descCargo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescCargo() {
        return descCargo;
    }

    /**
     * Sets the value of the descCargo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescCargo(String value) {
        this.descCargo = value;
    }

    /**
     * Gets the value of the codCentralCliente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodCentralCliente() {
        return codCentralCliente;
    }

    /**
     * Sets the value of the codCentralCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodCentralCliente(String value) {
        this.codCentralCliente = value;
    }

    /**
     * Gets the value of the registroUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistroUsuario() {
        return registroUsuario;
    }

    /**
     * Sets the value of the registroUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistroUsuario(String value) {
        this.registroUsuario = value;
    }

    /**
     * Gets the value of the oficinaUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOficinaUsuario() {
        return oficinaUsuario;
    }

    /**
     * Sets the value of the oficinaUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOficinaUsuario(String value) {
        this.oficinaUsuario = value;
    }

    /**
     * Gets the value of the centroContable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCentroContable() {
        return centroContable;
    }

    /**
     * Sets the value of the centroContable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCentroContable(String value) {
        this.centroContable = value;
    }

    /**
     * Gets the value of the referencia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * Sets the value of the referencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencia(String value) {
        this.referencia = value;
    }

}
