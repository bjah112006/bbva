//
// Generated By:JAX-WS RI IBM 2.2.1-11/25/2013 11:48 AM(foreman)- (JAXB RI IBM 2.2.3-11/25/2013 12:35 PM(foreman)-)
//


package com.ibm.bbva.harec.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BodyDatosGeneralesXPersonaRs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BodyDatosGeneralesXPersonaRs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoCentral" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="doi" type="{http://grupobbva.com.pe/HarecService/}Doi"/>
 *         &lt;element name="tipoPersona" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombres" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="apellidoPaterno" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="apellidoMaterno" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="correo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="genero" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="estadoCivil" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="nacimiento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="direccion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="distrito" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="provincia" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="departamento" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="paisResidencia" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="segmento" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="clasificacion" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="sector" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="actividad" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="ocupacion" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="clasificacionBanco" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="clasificacionSBS" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="oficinaGestora" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="registro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="empresaRegistral" type="{http://grupobbva.com.pe/HarecService/}Generico"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="esCliente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="vinculacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="esPrivilegiado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="relacionGrupo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="resultado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="error" type="{http://grupobbva.com.pe/HarecService/}Error"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BodyDatosGeneralesXPersonaRs", propOrder = {
    "codigoCentral",
    "doi",
    "tipoPersona",
    "nombres",
    "apellidoPaterno",
    "apellidoMaterno",
    "telefono",
    "correo",
    "genero",
    "estadoCivil",
    "nacimiento",
    "direccion",
    "distrito",
    "provincia",
    "departamento",
    "paisResidencia",
    "segmento",
    "clasificacion",
    "sector",
    "actividad",
    "ocupacion",
    "valor",
    "clasificacionBanco",
    "clasificacionSBS",
    "oficinaGestora",
    "registro",
    "empresaRegistral",
    "estado",
    "esCliente",
    "vinculacion",
    "esPrivilegiado",
    "relacionGrupo",
    "resultado",
    "error"
})
public class BodyDatosGeneralesXPersonaRs {

    @XmlElement(required = true)
    protected String codigoCentral;
    @XmlElement(required = true)
    protected Doi doi;
    @XmlElement(required = true)
    protected String tipoPersona;
    @XmlElement(required = true)
    protected String nombres;
    @XmlElement(required = true)
    protected String apellidoPaterno;
    @XmlElement(required = true)
    protected String apellidoMaterno;
    @XmlElement(required = true)
    protected String telefono;
    @XmlElement(required = true)
    protected String correo;
    @XmlElement(required = true)
    protected Generico genero;
    @XmlElement(required = true)
    protected Generico estadoCivil;
    @XmlElement(required = true)
    protected String nacimiento;
    @XmlElement(required = true)
    protected String direccion;
    @XmlElement(required = true)
    protected Generico distrito;
    @XmlElement(required = true)
    protected Generico provincia;
    @XmlElement(required = true)
    protected Generico departamento;
    @XmlElement(required = true)
    protected Generico paisResidencia;
    @XmlElement(required = true)
    protected Generico segmento;
    @XmlElement(required = true)
    protected Generico clasificacion;
    @XmlElement(required = true)
    protected String sector;
    @XmlElement(required = true)
    protected Generico actividad;
    @XmlElement(required = true)
    protected Generico ocupacion;
    @XmlElement(required = true)
    protected String valor;
    @XmlElement(required = true)
    protected Generico clasificacionBanco;
    @XmlElement(required = true)
    protected Generico clasificacionSBS;
    @XmlElement(required = true)
    protected Generico oficinaGestora;
    @XmlElement(required = true)
    protected String registro;
    @XmlElement(required = true)
    protected Generico empresaRegistral;
    @XmlElement(required = true)
    protected String estado;
    @XmlElement(required = true)
    protected String esCliente;
    @XmlElement(required = true)
    protected String vinculacion;
    @XmlElement(required = true)
    protected String esPrivilegiado;
    @XmlElement(required = true)
    protected String relacionGrupo;
    @XmlElement(required = true)
    protected String resultado;
    @XmlElement(required = true)
    protected Error error;

    /**
     * Gets the value of the codigoCentral property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoCentral() {
        return codigoCentral;
    }

    /**
     * Sets the value of the codigoCentral property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoCentral(String value) {
        this.codigoCentral = value;
    }

    /**
     * Gets the value of the doi property.
     * 
     * @return
     *     possible object is
     *     {@link Doi }
     *     
     */
    public Doi getDoi() {
        return doi;
    }

    /**
     * Sets the value of the doi property.
     * 
     * @param value
     *     allowed object is
     *     {@link Doi }
     *     
     */
    public void setDoi(Doi value) {
        this.doi = value;
    }

    /**
     * Gets the value of the tipoPersona property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoPersona() {
        return tipoPersona;
    }

    /**
     * Sets the value of the tipoPersona property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoPersona(String value) {
        this.tipoPersona = value;
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
     * Gets the value of the apellidoPaterno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    /**
     * Sets the value of the apellidoPaterno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellidoPaterno(String value) {
        this.apellidoPaterno = value;
    }

    /**
     * Gets the value of the apellidoMaterno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    /**
     * Sets the value of the apellidoMaterno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellidoMaterno(String value) {
        this.apellidoMaterno = value;
    }

    /**
     * Gets the value of the telefono property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Sets the value of the telefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono(String value) {
        this.telefono = value;
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
     * Gets the value of the genero property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getGenero() {
        return genero;
    }

    /**
     * Sets the value of the genero property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setGenero(Generico value) {
        this.genero = value;
    }

    /**
     * Gets the value of the estadoCivil property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getEstadoCivil() {
        return estadoCivil;
    }

    /**
     * Sets the value of the estadoCivil property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setEstadoCivil(Generico value) {
        this.estadoCivil = value;
    }

    /**
     * Gets the value of the nacimiento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNacimiento() {
        return nacimiento;
    }

    /**
     * Sets the value of the nacimiento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNacimiento(String value) {
        this.nacimiento = value;
    }

    /**
     * Gets the value of the direccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Sets the value of the direccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccion(String value) {
        this.direccion = value;
    }

    /**
     * Gets the value of the distrito property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getDistrito() {
        return distrito;
    }

    /**
     * Sets the value of the distrito property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setDistrito(Generico value) {
        this.distrito = value;
    }

    /**
     * Gets the value of the provincia property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getProvincia() {
        return provincia;
    }

    /**
     * Sets the value of the provincia property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setProvincia(Generico value) {
        this.provincia = value;
    }

    /**
     * Gets the value of the departamento property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getDepartamento() {
        return departamento;
    }

    /**
     * Sets the value of the departamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setDepartamento(Generico value) {
        this.departamento = value;
    }

    /**
     * Gets the value of the paisResidencia property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getPaisResidencia() {
        return paisResidencia;
    }

    /**
     * Sets the value of the paisResidencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setPaisResidencia(Generico value) {
        this.paisResidencia = value;
    }

    /**
     * Gets the value of the segmento property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getSegmento() {
        return segmento;
    }

    /**
     * Sets the value of the segmento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setSegmento(Generico value) {
        this.segmento = value;
    }

    /**
     * Gets the value of the clasificacion property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getClasificacion() {
        return clasificacion;
    }

    /**
     * Sets the value of the clasificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setClasificacion(Generico value) {
        this.clasificacion = value;
    }

    /**
     * Gets the value of the sector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSector() {
        return sector;
    }

    /**
     * Sets the value of the sector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSector(String value) {
        this.sector = value;
    }

    /**
     * Gets the value of the actividad property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getActividad() {
        return actividad;
    }

    /**
     * Sets the value of the actividad property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setActividad(Generico value) {
        this.actividad = value;
    }

    /**
     * Gets the value of the ocupacion property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getOcupacion() {
        return ocupacion;
    }

    /**
     * Sets the value of the ocupacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setOcupacion(Generico value) {
        this.ocupacion = value;
    }

    /**
     * Gets the value of the valor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValor() {
        return valor;
    }

    /**
     * Sets the value of the valor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValor(String value) {
        this.valor = value;
    }

    /**
     * Gets the value of the clasificacionBanco property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getClasificacionBanco() {
        return clasificacionBanco;
    }

    /**
     * Sets the value of the clasificacionBanco property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setClasificacionBanco(Generico value) {
        this.clasificacionBanco = value;
    }

    /**
     * Gets the value of the clasificacionSBS property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getClasificacionSBS() {
        return clasificacionSBS;
    }

    /**
     * Sets the value of the clasificacionSBS property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setClasificacionSBS(Generico value) {
        this.clasificacionSBS = value;
    }

    /**
     * Gets the value of the oficinaGestora property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getOficinaGestora() {
        return oficinaGestora;
    }

    /**
     * Sets the value of the oficinaGestora property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setOficinaGestora(Generico value) {
        this.oficinaGestora = value;
    }

    /**
     * Gets the value of the registro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistro() {
        return registro;
    }

    /**
     * Sets the value of the registro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistro(String value) {
        this.registro = value;
    }

    /**
     * Gets the value of the empresaRegistral property.
     * 
     * @return
     *     possible object is
     *     {@link Generico }
     *     
     */
    public Generico getEmpresaRegistral() {
        return empresaRegistral;
    }

    /**
     * Sets the value of the empresaRegistral property.
     * 
     * @param value
     *     allowed object is
     *     {@link Generico }
     *     
     */
    public void setEmpresaRegistral(Generico value) {
        this.empresaRegistral = value;
    }

    /**
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Gets the value of the esCliente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsCliente() {
        return esCliente;
    }

    /**
     * Sets the value of the esCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsCliente(String value) {
        this.esCliente = value;
    }

    /**
     * Gets the value of the vinculacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVinculacion() {
        return vinculacion;
    }

    /**
     * Sets the value of the vinculacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVinculacion(String value) {
        this.vinculacion = value;
    }

    /**
     * Gets the value of the esPrivilegiado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEsPrivilegiado() {
        return esPrivilegiado;
    }

    /**
     * Sets the value of the esPrivilegiado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEsPrivilegiado(String value) {
        this.esPrivilegiado = value;
    }

    /**
     * Gets the value of the relacionGrupo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelacionGrupo() {
        return relacionGrupo;
    }

    /**
     * Sets the value of the relacionGrupo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelacionGrupo(String value) {
        this.relacionGrupo = value;
    }

    /**
     * Gets the value of the resultado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * Sets the value of the resultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultado(String value) {
        this.resultado = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link Error }
     *     
     */
    public Error getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link Error }
     *     
     */
    public void setError(Error value) {
        this.error = value;
    }

}
