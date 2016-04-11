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
 * <p>Java class for ClientePJ_BE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClientePJ_BE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="articulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigo_central" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estadoVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estadoVersion_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estructura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fecha_escritura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fecha_version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jurisdiccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jurisdiccion_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listaCombinaciones" type="{http://webservices.hfirmas.beans.spring.hiper/xsd}Combinacion_BE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaCuentas" type="{http://webservices.hfirmas.beans.spring.hiper/xsd}Cuenta_BE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaRepresentantes" type="{http://webservices.hfirmas.beans.spring.hiper/xsd}Representante_BE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="notario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="notario_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nroExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nroVersion" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="numero_DOI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="objeto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oficina_registral" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oficina_registral_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="partida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="poderEspecial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="razon_social" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo_DOI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo_DOI_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo_persona" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo_persona_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zona_registral" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zona_registral_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClientePJ_BE", propOrder = {
    "articulo",
    "codError",
    "codigoCentral",
    "descError",
    "estado",
    "estadoVersion",
    "estadoVersionDesc",
    "estadoDesc",
    "estructura",
    "fechaEscritura",
    "fechaVersion",
    "jurisdiccion",
    "jurisdiccionDesc",
    "listaCombinaciones",
    "listaCuentas",
    "listaRepresentantes",
    "notario",
    "notarioDesc",
    "nroExpediente",
    "nroVersion",
    "numeroDOI",
    "objeto",
    "oficinaRegistral",
    "oficinaRegistralDesc",
    "partida",
    "poderEspecial",
    "razonSocial",
    "tipoDOI",
    "tipoDOIDesc",
    "tipoPersona",
    "tipoPersonaDesc",
    "zonaRegistral",
    "zonaRegistralDesc"
})
public class ClientePJBE {

    @XmlElement(nillable = true)
    protected String articulo;
    @XmlElement(nillable = true)
    protected String codError;
    @XmlElement(name = "codigo_central", nillable = true)
    protected String codigoCentral;
    @XmlElement(nillable = true)
    protected String descError;
    @XmlElement(nillable = true)
    protected String estado;
    @XmlElement(nillable = true)
    protected String estadoVersion;
    @XmlElement(name = "estadoVersion_desc", nillable = true)
    protected String estadoVersionDesc;
    @XmlElement(name = "estado_desc", nillable = true)
    protected String estadoDesc;
    @XmlElement(nillable = true)
    protected String estructura;
    @XmlElement(name = "fecha_escritura", nillable = true)
    protected String fechaEscritura;
    @XmlElement(name = "fecha_version", nillable = true)
    protected String fechaVersion;
    @XmlElement(nillable = true)
    protected String jurisdiccion;
    @XmlElement(name = "jurisdiccion_desc", nillable = true)
    protected String jurisdiccionDesc;
    @XmlElement(nillable = true)
    protected List<CombinacionBE> listaCombinaciones;
    @XmlElement(nillable = true)
    protected List<CuentaBE> listaCuentas;
    @XmlElement(nillable = true)
    protected List<RepresentanteBE> listaRepresentantes;
    @XmlElement(nillable = true)
    protected String notario;
    @XmlElement(name = "notario_desc", nillable = true)
    protected String notarioDesc;
    @XmlElement(nillable = true)
    protected String nroExpediente;
    protected Integer nroVersion;
    @XmlElement(name = "numero_DOI", nillable = true)
    protected String numeroDOI;
    @XmlElement(nillable = true)
    protected String objeto;
    @XmlElement(name = "oficina_registral", nillable = true)
    protected String oficinaRegistral;
    @XmlElement(name = "oficina_registral_desc", nillable = true)
    protected String oficinaRegistralDesc;
    @XmlElement(nillable = true)
    protected String partida;
    @XmlElement(nillable = true)
    protected String poderEspecial;
    @XmlElement(name = "razon_social", nillable = true)
    protected String razonSocial;
    @XmlElement(name = "tipo_DOI", nillable = true)
    protected String tipoDOI;
    @XmlElement(name = "tipo_DOI_desc", nillable = true)
    protected String tipoDOIDesc;
    @XmlElement(name = "tipo_persona", nillable = true)
    protected String tipoPersona;
    @XmlElement(name = "tipo_persona_desc", nillable = true)
    protected String tipoPersonaDesc;
    @XmlElement(name = "zona_registral", nillable = true)
    protected String zonaRegistral;
    @XmlElement(name = "zona_registral_desc", nillable = true)
    protected String zonaRegistralDesc;

    /**
     * Gets the value of the articulo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArticulo() {
        return articulo;
    }

    /**
     * Sets the value of the articulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArticulo(String value) {
        this.articulo = value;
    }

    /**
     * Gets the value of the codError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodError() {
        return codError;
    }

    /**
     * Sets the value of the codError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodError(String value) {
        this.codError = value;
    }

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
     * Gets the value of the descError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescError() {
        return descError;
    }

    /**
     * Sets the value of the descError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescError(String value) {
        this.descError = value;
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
     * Gets the value of the estadoVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoVersion() {
        return estadoVersion;
    }

    /**
     * Sets the value of the estadoVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoVersion(String value) {
        this.estadoVersion = value;
    }

    /**
     * Gets the value of the estadoVersionDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoVersionDesc() {
        return estadoVersionDesc;
    }

    /**
     * Sets the value of the estadoVersionDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoVersionDesc(String value) {
        this.estadoVersionDesc = value;
    }

    /**
     * Gets the value of the estadoDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoDesc() {
        return estadoDesc;
    }

    /**
     * Sets the value of the estadoDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoDesc(String value) {
        this.estadoDesc = value;
    }

    /**
     * Gets the value of the estructura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstructura() {
        return estructura;
    }

    /**
     * Sets the value of the estructura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstructura(String value) {
        this.estructura = value;
    }

    /**
     * Gets the value of the fechaEscritura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEscritura() {
        return fechaEscritura;
    }

    /**
     * Sets the value of the fechaEscritura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEscritura(String value) {
        this.fechaEscritura = value;
    }

    /**
     * Gets the value of the fechaVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaVersion() {
        return fechaVersion;
    }

    /**
     * Sets the value of the fechaVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaVersion(String value) {
        this.fechaVersion = value;
    }

    /**
     * Gets the value of the jurisdiccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJurisdiccion() {
        return jurisdiccion;
    }

    /**
     * Sets the value of the jurisdiccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJurisdiccion(String value) {
        this.jurisdiccion = value;
    }

    /**
     * Gets the value of the jurisdiccionDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJurisdiccionDesc() {
        return jurisdiccionDesc;
    }

    /**
     * Sets the value of the jurisdiccionDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJurisdiccionDesc(String value) {
        this.jurisdiccionDesc = value;
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
     * Gets the value of the listaCuentas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaCuentas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaCuentas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CuentaBE }
     * 
     * 
     */
    public List<CuentaBE> getListaCuentas() {
        if (listaCuentas == null) {
            listaCuentas = new ArrayList<CuentaBE>();
        }
        return this.listaCuentas;
    }

    /**
     * Gets the value of the listaRepresentantes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaRepresentantes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaRepresentantes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RepresentanteBE }
     * 
     * 
     */
    public List<RepresentanteBE> getListaRepresentantes() {
        if (listaRepresentantes == null) {
            listaRepresentantes = new ArrayList<RepresentanteBE>();
        }
        return this.listaRepresentantes;
    }

    /**
     * Gets the value of the notario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotario() {
        return notario;
    }

    /**
     * Sets the value of the notario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotario(String value) {
        this.notario = value;
    }

    /**
     * Gets the value of the notarioDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotarioDesc() {
        return notarioDesc;
    }

    /**
     * Sets the value of the notarioDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotarioDesc(String value) {
        this.notarioDesc = value;
    }

    /**
     * Gets the value of the nroExpediente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNroExpediente() {
        return nroExpediente;
    }

    /**
     * Sets the value of the nroExpediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNroExpediente(String value) {
        this.nroExpediente = value;
    }

    /**
     * Gets the value of the nroVersion property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNroVersion() {
        return nroVersion;
    }

    /**
     * Sets the value of the nroVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNroVersion(Integer value) {
        this.nroVersion = value;
    }

    /**
     * Gets the value of the numeroDOI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroDOI() {
        return numeroDOI;
    }

    /**
     * Sets the value of the numeroDOI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroDOI(String value) {
        this.numeroDOI = value;
    }

    /**
     * Gets the value of the objeto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjeto() {
        return objeto;
    }

    /**
     * Sets the value of the objeto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjeto(String value) {
        this.objeto = value;
    }

    /**
     * Gets the value of the oficinaRegistral property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOficinaRegistral() {
        return oficinaRegistral;
    }

    /**
     * Sets the value of the oficinaRegistral property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOficinaRegistral(String value) {
        this.oficinaRegistral = value;
    }

    /**
     * Gets the value of the oficinaRegistralDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOficinaRegistralDesc() {
        return oficinaRegistralDesc;
    }

    /**
     * Sets the value of the oficinaRegistralDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOficinaRegistralDesc(String value) {
        this.oficinaRegistralDesc = value;
    }

    /**
     * Gets the value of the partida property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartida() {
        return partida;
    }

    /**
     * Sets the value of the partida property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartida(String value) {
        this.partida = value;
    }

    /**
     * Gets the value of the poderEspecial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoderEspecial() {
        return poderEspecial;
    }

    /**
     * Sets the value of the poderEspecial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoderEspecial(String value) {
        this.poderEspecial = value;
    }

    /**
     * Gets the value of the razonSocial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Sets the value of the razonSocial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocial(String value) {
        this.razonSocial = value;
    }

    /**
     * Gets the value of the tipoDOI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDOI() {
        return tipoDOI;
    }

    /**
     * Sets the value of the tipoDOI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDOI(String value) {
        this.tipoDOI = value;
    }

    /**
     * Gets the value of the tipoDOIDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDOIDesc() {
        return tipoDOIDesc;
    }

    /**
     * Sets the value of the tipoDOIDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDOIDesc(String value) {
        this.tipoDOIDesc = value;
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
     * Gets the value of the tipoPersonaDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoPersonaDesc() {
        return tipoPersonaDesc;
    }

    /**
     * Sets the value of the tipoPersonaDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoPersonaDesc(String value) {
        this.tipoPersonaDesc = value;
    }

    /**
     * Gets the value of the zonaRegistral property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZonaRegistral() {
        return zonaRegistral;
    }

    /**
     * Sets the value of the zonaRegistral property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZonaRegistral(String value) {
        this.zonaRegistral = value;
    }

    /**
     * Gets the value of the zonaRegistralDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZonaRegistralDesc() {
        return zonaRegistralDesc;
    }

    /**
     * Sets the value of the zonaRegistralDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZonaRegistralDesc(String value) {
        this.zonaRegistralDesc = value;
    }

}
