//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package hiper.spring.beans.hfirmas.webservices.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Cuenta_BE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Cuenta_BE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fecha_creacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="indicador_poder" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listaCombinaciones" type="{http://webservices.hfirmas.beans.spring.hiper/xsd}Combinacion_BE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="moneda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="moneda_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nroCuenta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="producto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="producto_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="situacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="situacion_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subproducto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subproducto_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cuenta_BE", propOrder = {
    "fechaCreacion",
    "indicadorPoder",
    "listaCombinaciones",
    "moneda",
    "monedaDesc",
    "nroCuenta",
    "producto",
    "productoDesc",
    "situacion",
    "situacionDesc",
    "subproducto",
    "subproductoDesc"
})
public class CuentaBE {

    @XmlElement(name = "fecha_creacion", nillable = true)
    protected String fechaCreacion;
    @XmlElement(name = "indicador_poder", nillable = true)
    protected String indicadorPoder;
    @XmlElement(nillable = true)
    protected List<CombinacionBE> listaCombinaciones;
    @XmlElement(nillable = true)
    protected String moneda;
    @XmlElement(name = "moneda_desc", nillable = true)
    protected String monedaDesc;
    @XmlElement(nillable = true)
    protected String nroCuenta;
    @XmlElement(nillable = true)
    protected String producto;
    @XmlElement(name = "producto_desc", nillable = true)
    protected String productoDesc;
    @XmlElement(nillable = true)
    protected String situacion;
    @XmlElement(name = "situacion_desc", nillable = true)
    protected String situacionDesc;
    @XmlElement(nillable = true)
    protected String subproducto;
    @XmlElement(name = "subproducto_desc", nillable = true)
    protected String subproductoDesc;

    /**
     * Gets the value of the fechaCreacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Sets the value of the fechaCreacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCreacion(String value) {
        this.fechaCreacion = value;
    }

    /**
     * Gets the value of the indicadorPoder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndicadorPoder() {
        return indicadorPoder;
    }

    /**
     * Sets the value of the indicadorPoder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndicadorPoder(String value) {
        this.indicadorPoder = value;
    }

    /**
     * Gets the value of the listaCombinaciones property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaCombinaciones property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaCombinaciones().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CombinacionBE }
     * 
     * 
     */
    public List<CombinacionBE> getListaCombinaciones() {
        if (listaCombinaciones == null) {
            listaCombinaciones = new ArrayList<CombinacionBE>();
        }
        return this.listaCombinaciones;
    }

    /**
     * Gets the value of the moneda property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * Sets the value of the moneda property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoneda(String value) {
        this.moneda = value;
    }

    /**
     * Gets the value of the monedaDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonedaDesc() {
        return monedaDesc;
    }

    /**
     * Sets the value of the monedaDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonedaDesc(String value) {
        this.monedaDesc = value;
    }

    /**
     * Gets the value of the nroCuenta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNroCuenta() {
        return nroCuenta;
    }

    /**
     * Sets the value of the nroCuenta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNroCuenta(String value) {
        this.nroCuenta = value;
    }

    /**
     * Gets the value of the producto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProducto() {
        return producto;
    }

    /**
     * Sets the value of the producto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProducto(String value) {
        this.producto = value;
    }

    /**
     * Gets the value of the productoDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductoDesc() {
        return productoDesc;
    }

    /**
     * Sets the value of the productoDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductoDesc(String value) {
        this.productoDesc = value;
    }

    /**
     * Gets the value of the situacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSituacion() {
        return situacion;
    }

    /**
     * Sets the value of the situacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSituacion(String value) {
        this.situacion = value;
    }

    /**
     * Gets the value of the situacionDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSituacionDesc() {
        return situacionDesc;
    }

    /**
     * Sets the value of the situacionDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSituacionDesc(String value) {
        this.situacionDesc = value;
    }

    /**
     * Gets the value of the subproducto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubproducto() {
        return subproducto;
    }

    /**
     * Sets the value of the subproducto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubproducto(String value) {
        this.subproducto = value;
    }

    /**
     * Gets the value of the subproductoDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubproductoDesc() {
        return subproductoDesc;
    }

    /**
     * Sets the value of the subproductoDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubproductoDesc(String value) {
        this.subproductoDesc = value;
    }

}
