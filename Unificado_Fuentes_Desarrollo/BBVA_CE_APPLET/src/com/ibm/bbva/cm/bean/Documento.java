
package com.ibm.bbva.cm.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Documento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Documento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mandatorio" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="contenido" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="fechaExpiracion" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="tipoDoi" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numDoi" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codCliente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreArchivo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="origen" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fechaCreacion" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="urlContent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Documento", propOrder = {
    "id",
    "tipo",
    "mandatorio",
    "contenido",
    "fechaExpiracion",
    "tipoDoi",
    "numDoi",
    "codCliente",
    "nombreArchivo",
    "origen",
    "fechaCreacion",
    "urlContent"
})
public class Documento {

    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer id;
    @XmlElement(required = true, nillable = true)
    protected String tipo;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean mandatorio;
    @XmlElement(required = true)
    protected byte[] contenido;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaExpiracion;
    @XmlElement(required = true, nillable = true)
    protected String tipoDoi;
    @XmlElement(required = true, nillable = true)
    protected String numDoi;
    @XmlElement(required = true, nillable = true)
    protected String codCliente;
    @XmlElement(required = true, nillable = true)
    protected String nombreArchivo;
    @XmlElement(required = true, nillable = true)
    protected String origen;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCreacion;
    @XmlElement(required = true, nillable = true)
    protected String urlContent;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Gets the value of the mandatorio property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMandatorio() {
        return mandatorio;
    }

    /**
     * Sets the value of the mandatorio property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMandatorio(Boolean value) {
        this.mandatorio = value;
    }

    /**
     * Gets the value of the contenido property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getContenido() {
        return contenido;
    }

    /**
     * Sets the value of the contenido property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setContenido(byte[] value) {
        this.contenido = ((byte[]) value);
    }

    /**
     * Gets the value of the fechaExpiracion property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaExpiracion() {
        return fechaExpiracion;
    }

    /**
     * Sets the value of the fechaExpiracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaExpiracion(XMLGregorianCalendar value) {
        this.fechaExpiracion = value;
    }

    /**
     * Gets the value of the tipoDoi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDoi() {
        return tipoDoi;
    }

    /**
     * Sets the value of the tipoDoi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDoi(String value) {
        this.tipoDoi = value;
    }

    /**
     * Gets the value of the numDoi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumDoi() {
        return numDoi;
    }

    /**
     * Sets the value of the numDoi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumDoi(String value) {
        this.numDoi = value;
    }

    /**
     * Gets the value of the codCliente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodCliente() {
        return codCliente;
    }

    /**
     * Sets the value of the codCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodCliente(String value) {
        this.codCliente = value;
    }

    /**
     * Gets the value of the nombreArchivo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * Sets the value of the nombreArchivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreArchivo(String value) {
        this.nombreArchivo = value;
    }

    /**
     * Gets the value of the origen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Sets the value of the origen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigen(String value) {
        this.origen = value;
    }

    /**
     * Gets the value of the fechaCreacion property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Sets the value of the fechaCreacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCreacion(XMLGregorianCalendar value) {
        this.fechaCreacion = value;
    }

    /**
     * Gets the value of the urlContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlContent() {
        return urlContent;
    }

    /**
     * Sets the value of the urlContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlContent(String value) {
        this.urlContent = value;
    }

}
