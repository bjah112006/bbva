//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.ibm.bbva.service.business.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for empleadoDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="empleadoDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="apemat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="apepat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="correo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fecegr" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fecing" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="flagAbogado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="flagActivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="flagAutorizaEjecutivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="idCategoria" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idEstudioAbogado" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idOficina" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idPerfil" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="nombreCategoria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreEstudioAbogado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreOficina" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombrePerfil" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombres" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombresCompletos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "empleadoDTO", propOrder = {
    "apemat",
    "apepat",
    "codigo",
    "correo",
    "fecegr",
    "fecing",
    "flagAbogado",
    "flagActivo",
    "flagAutorizaEjecutivo",
    "id",
    "idCategoria",
    "idEstudioAbogado",
    "idOficina",
    "idPerfil",
    "nombreCategoria",
    "nombreEstudioAbogado",
    "nombreOficina",
    "nombrePerfil",
    "nombres",
    "nombresCompletos"
})
public class EmpleadoDTO {

    protected String apemat;
    protected String apepat;
    protected String codigo;
    protected String correo;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fecegr;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fecing;
    protected String flagAbogado;
    protected String flagActivo;
    protected String flagAutorizaEjecutivo;
    protected long id;
    protected Long idCategoria;
    protected Long idEstudioAbogado;
    protected Long idOficina;
    protected Long idPerfil;
    protected String nombreCategoria;
    protected String nombreEstudioAbogado;
    protected String nombreOficina;
    protected String nombrePerfil;
    protected String nombres;
    protected String nombresCompletos;

    /**
     * Gets the value of the apemat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApemat() {
        return apemat;
    }

    /**
     * Sets the value of the apemat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApemat(String value) {
        this.apemat = value;
    }

    /**
     * Gets the value of the apepat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApepat() {
        return apepat;
    }

    /**
     * Sets the value of the apepat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApepat(String value) {
        this.apepat = value;
    }

    /**
     * Gets the value of the codigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Sets the value of the codigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Gets the value of the correo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Sets the value of the correo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorreo(String value) {
        this.correo = value;
    }

    /**
     * Gets the value of the fecegr property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecegr() {
        return fecegr;
    }

    /**
     * Sets the value of the fecegr property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecegr(XMLGregorianCalendar value) {
        this.fecegr = value;
    }

    /**
     * Gets the value of the fecing property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecing() {
        return fecing;
    }

    /**
     * Sets the value of the fecing property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecing(XMLGregorianCalendar value) {
        this.fecing = value;
    }

    /**
     * Gets the value of the flagAbogado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagAbogado() {
        return flagAbogado;
    }

    /**
     * Sets the value of the flagAbogado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagAbogado(String value) {
        this.flagAbogado = value;
    }

    /**
     * Gets the value of the flagActivo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagActivo() {
        return flagActivo;
    }

    /**
     * Sets the value of the flagActivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagActivo(String value) {
        this.flagActivo = value;
    }

    /**
     * Gets the value of the flagAutorizaEjecutivo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagAutorizaEjecutivo() {
        return flagAutorizaEjecutivo;
    }

    /**
     * Sets the value of the flagAutorizaEjecutivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagAutorizaEjecutivo(String value) {
        this.flagAutorizaEjecutivo = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the idCategoria property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdCategoria() {
        return idCategoria;
    }

    /**
     * Sets the value of the idCategoria property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdCategoria(Long value) {
        this.idCategoria = value;
    }

    /**
     * Gets the value of the idEstudioAbogado property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdEstudioAbogado() {
        return idEstudioAbogado;
    }

    /**
     * Sets the value of the idEstudioAbogado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdEstudioAbogado(Long value) {
        this.idEstudioAbogado = value;
    }

    /**
     * Gets the value of the idOficina property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdOficina() {
        return idOficina;
    }

    /**
     * Sets the value of the idOficina property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdOficina(Long value) {
        this.idOficina = value;
    }

    /**
     * Gets the value of the idPerfil property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdPerfil() {
        return idPerfil;
    }

    /**
     * Sets the value of the idPerfil property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdPerfil(Long value) {
        this.idPerfil = value;
    }

    /**
     * Gets the value of the nombreCategoria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    /**
     * Sets the value of the nombreCategoria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreCategoria(String value) {
        this.nombreCategoria = value;
    }

    /**
     * Gets the value of the nombreEstudioAbogado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreEstudioAbogado() {
        return nombreEstudioAbogado;
    }

    /**
     * Sets the value of the nombreEstudioAbogado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreEstudioAbogado(String value) {
        this.nombreEstudioAbogado = value;
    }

    /**
     * Gets the value of the nombreOficina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreOficina() {
        return nombreOficina;
    }

    /**
     * Sets the value of the nombreOficina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreOficina(String value) {
        this.nombreOficina = value;
    }

    /**
     * Gets the value of the nombrePerfil property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombrePerfil() {
        return nombrePerfil;
    }

    /**
     * Sets the value of the nombrePerfil property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombrePerfil(String value) {
        this.nombrePerfil = value;
    }

    /**
     * Gets the value of the nombres property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Sets the value of the nombres property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombres(String value) {
        this.nombres = value;
    }

    /**
     * Gets the value of the nombresCompletos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombresCompletos() {
        return nombresCompletos;
    }

    /**
     * Sets the value of the nombresCompletos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombresCompletos(String value) {
        this.nombresCompletos = value;
    }

}
