//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.bbva.general.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Centro complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Centro">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoOficina" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="oficinaBaja" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="departamento" type="{http://entities.general.bbva.com}Ubigeo"/>
 *         &lt;element name="provincia" type="{http://entities.general.bbva.com}Ubigeo"/>
 *         &lt;element name="distrito" type="{http://entities.general.bbva.com}Ubigeo"/>
 *         &lt;element name="territorio" type="{http://entities.general.bbva.com}Territorio"/>
 *         &lt;element name="tipoVia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreVia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroInterior" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroExterior" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipZona" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreZona" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="prefijoTelefono" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="telefono1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="telefono2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroFax" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fechaApertura" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="fechaBaja" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="fechaAltaCentro" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="tipoOficina" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoOficinaCentro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="centroSuperiores" type="{http://entities.general.bbva.com}ArrayOfCentroSuperior"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Centro", propOrder = {
    "codigoOficina",
    "nombre",
    "oficinaBaja",
    "departamento",
    "provincia",
    "distrito",
    "territorio",
    "tipoVia",
    "nombreVia",
    "numeroInterior",
    "numeroExterior",
    "tipZona",
    "nombreZona",
    "prefijoTelefono",
    "telefono1",
    "telefono2",
    "numeroFax",
    "fechaApertura",
    "fechaBaja",
    "fechaAltaCentro",
    "tipoOficina",
    "tipoOficinaCentro",
    "centroSuperiores"
})
public class Centro {

    @XmlElement(required = true, nillable = true)
    protected String codigoOficina;
    @XmlElement(required = true, nillable = true)
    protected String nombre;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean oficinaBaja;
    @XmlElement(required = true, nillable = true)
    protected Ubigeo departamento;
    @XmlElement(required = true, nillable = true)
    protected Ubigeo provincia;
    @XmlElement(required = true, nillable = true)
    protected Ubigeo distrito;
    @XmlElement(required = true, nillable = true)
    protected Territorio territorio;
    @XmlElement(required = true, nillable = true)
    protected String tipoVia;
    @XmlElement(required = true, nillable = true)
    protected String nombreVia;
    @XmlElement(required = true, nillable = true)
    protected String numeroInterior;
    @XmlElement(required = true, nillable = true)
    protected String numeroExterior;
    @XmlElement(required = true, nillable = true)
    protected String tipZona;
    @XmlElement(required = true, nillable = true)
    protected String nombreZona;
    @XmlElement(required = true, nillable = true)
    protected String prefijoTelefono;
    @XmlElement(required = true, nillable = true)
    protected String telefono1;
    @XmlElement(required = true, nillable = true)
    protected String telefono2;
    @XmlElement(required = true, nillable = true)
    protected String numeroFax;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaApertura;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaBaja;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAltaCentro;
    @XmlElement(required = true, nillable = true)
    protected String tipoOficina;
    @XmlElement(required = true, nillable = true)
    protected String tipoOficinaCentro;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfCentroSuperior centroSuperiores;

    /**
     * Gets the value of the codigoOficina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOficina() {
        return codigoOficina;
    }

    /**
     * Sets the value of the codigoOficina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOficina(String value) {
        this.codigoOficina = value;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the oficinaBaja property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOficinaBaja() {
        return oficinaBaja;
    }

    /**
     * Sets the value of the oficinaBaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOficinaBaja(Boolean value) {
        this.oficinaBaja = value;
    }

    /**
     * Gets the value of the departamento property.
     * 
     * @return
     *     possible object is
     *     {@link Ubigeo }
     *     
     */
    public Ubigeo getDepartamento() {
        return departamento;
    }

    /**
     * Sets the value of the departamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ubigeo }
     *     
     */
    public void setDepartamento(Ubigeo value) {
        this.departamento = value;
    }

    /**
     * Gets the value of the provincia property.
     * 
     * @return
     *     possible object is
     *     {@link Ubigeo }
     *     
     */
    public Ubigeo getProvincia() {
        return provincia;
    }

    /**
     * Sets the value of the provincia property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ubigeo }
     *     
     */
    public void setProvincia(Ubigeo value) {
        this.provincia = value;
    }

    /**
     * Gets the value of the distrito property.
     * 
     * @return
     *     possible object is
     *     {@link Ubigeo }
     *     
     */
    public Ubigeo getDistrito() {
        return distrito;
    }

    /**
     * Sets the value of the distrito property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ubigeo }
     *     
     */
    public void setDistrito(Ubigeo value) {
        this.distrito = value;
    }

    /**
     * Gets the value of the territorio property.
     * 
     * @return
     *     possible object is
     *     {@link Territorio }
     *     
     */
    public Territorio getTerritorio() {
        return territorio;
    }

    /**
     * Sets the value of the territorio property.
     * 
     * @param value
     *     allowed object is
     *     {@link Territorio }
     *     
     */
    public void setTerritorio(Territorio value) {
        this.territorio = value;
    }

    /**
     * Gets the value of the tipoVia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoVia() {
        return tipoVia;
    }

    /**
     * Sets the value of the tipoVia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoVia(String value) {
        this.tipoVia = value;
    }

    /**
     * Gets the value of the nombreVia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreVia() {
        return nombreVia;
    }

    /**
     * Sets the value of the nombreVia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreVia(String value) {
        this.nombreVia = value;
    }

    /**
     * Gets the value of the numeroInterior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroInterior() {
        return numeroInterior;
    }

    /**
     * Sets the value of the numeroInterior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroInterior(String value) {
        this.numeroInterior = value;
    }

    /**
     * Gets the value of the numeroExterior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroExterior() {
        return numeroExterior;
    }

    /**
     * Sets the value of the numeroExterior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroExterior(String value) {
        this.numeroExterior = value;
    }

    /**
     * Gets the value of the tipZona property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipZona() {
        return tipZona;
    }

    /**
     * Sets the value of the tipZona property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipZona(String value) {
        this.tipZona = value;
    }

    /**
     * Gets the value of the nombreZona property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreZona() {
        return nombreZona;
    }

    /**
     * Sets the value of the nombreZona property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreZona(String value) {
        this.nombreZona = value;
    }

    /**
     * Gets the value of the prefijoTelefono property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrefijoTelefono() {
        return prefijoTelefono;
    }

    /**
     * Sets the value of the prefijoTelefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrefijoTelefono(String value) {
        this.prefijoTelefono = value;
    }

    /**
     * Gets the value of the telefono1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono1() {
        return telefono1;
    }

    /**
     * Sets the value of the telefono1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono1(String value) {
        this.telefono1 = value;
    }

    /**
     * Gets the value of the telefono2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono2() {
        return telefono2;
    }

    /**
     * Sets the value of the telefono2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono2(String value) {
        this.telefono2 = value;
    }

    /**
     * Gets the value of the numeroFax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroFax() {
        return numeroFax;
    }

    /**
     * Sets the value of the numeroFax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroFax(String value) {
        this.numeroFax = value;
    }

    /**
     * Gets the value of the fechaApertura property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaApertura() {
        return fechaApertura;
    }

    /**
     * Sets the value of the fechaApertura property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaApertura(XMLGregorianCalendar value) {
        this.fechaApertura = value;
    }

    /**
     * Gets the value of the fechaBaja property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaBaja() {
        return fechaBaja;
    }

    /**
     * Sets the value of the fechaBaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaBaja(XMLGregorianCalendar value) {
        this.fechaBaja = value;
    }

    /**
     * Gets the value of the fechaAltaCentro property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaAltaCentro() {
        return fechaAltaCentro;
    }

    /**
     * Sets the value of the fechaAltaCentro property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaAltaCentro(XMLGregorianCalendar value) {
        this.fechaAltaCentro = value;
    }

    /**
     * Gets the value of the tipoOficina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoOficina() {
        return tipoOficina;
    }

    /**
     * Sets the value of the tipoOficina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoOficina(String value) {
        this.tipoOficina = value;
    }

    /**
     * Gets the value of the tipoOficinaCentro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoOficinaCentro() {
        return tipoOficinaCentro;
    }

    /**
     * Sets the value of the tipoOficinaCentro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoOficinaCentro(String value) {
        this.tipoOficinaCentro = value;
    }

    /**
     * Gets the value of the centroSuperiores property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCentroSuperior }
     *     
     */
    public ArrayOfCentroSuperior getCentroSuperiores() {
        return centroSuperiores;
    }

    /**
     * Sets the value of the centroSuperiores property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCentroSuperior }
     *     
     */
    public void setCentroSuperiores(ArrayOfCentroSuperior value) {
        this.centroSuperiores = value;
    }

}
