
package pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AuditRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AuditRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idTransaccion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ipAplicacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreAplicacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="usuarioAplicacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuditRequest", namespace = "http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/types/base", propOrder = {
    "idTransaccion",
    "ipAplicacion",
    "nombreAplicacion",
    "usuarioAplicacion"
})
public class AuditRequest {

    @XmlElement(required = true)
    protected String idTransaccion;
    @XmlElement(required = true)
    protected String ipAplicacion;
    @XmlElement(required = true)
    protected String nombreAplicacion;
    @XmlElement(required = true)
    protected String usuarioAplicacion;

    /**
     * Gets the value of the idTransaccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTransaccion() {
        return idTransaccion;
    }

    /**
     * Sets the value of the idTransaccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTransaccion(String value) {
        this.idTransaccion = value;
    }

    /**
     * Gets the value of the ipAplicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpAplicacion() {
        return ipAplicacion;
    }

    /**
     * Sets the value of the ipAplicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpAplicacion(String value) {
        this.ipAplicacion = value;
    }

    /**
     * Gets the value of the nombreAplicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    /**
     * Sets the value of the nombreAplicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreAplicacion(String value) {
        this.nombreAplicacion = value;
    }

    /**
     * Gets the value of the usuarioAplicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuarioAplicacion() {
        return usuarioAplicacion;
    }

    /**
     * Sets the value of the usuarioAplicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuarioAplicacion(String value) {
        this.usuarioAplicacion = value;
    }

}
