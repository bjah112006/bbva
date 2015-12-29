
package pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="auditResponse" type="{http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/types/base}AuditResponse"/>
 *         &lt;element name="listaParametrosResponse" type="{http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/types/base}Parametros"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "auditResponse",
    "listaParametrosResponse"
})
@XmlRootElement(name = "obtenerDocResponse")
public class ObtenerDocResponse {

    @XmlElement(required = true)
    protected AuditResponse auditResponse;
    @XmlElement(required = true)
    protected Parametros listaParametrosResponse;

    /**
     * Gets the value of the auditResponse property.
     * 
     * @return
     *     possible object is
     *     {@link AuditResponse }
     *     
     */
    public AuditResponse getAuditResponse() {
        return auditResponse;
    }

    /**
     * Sets the value of the auditResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuditResponse }
     *     
     */
    public void setAuditResponse(AuditResponse value) {
        this.auditResponse = value;
    }

    /**
     * Gets the value of the listaParametrosResponse property.
     * 
     * @return
     *     possible object is
     *     {@link Parametros }
     *     
     */
    public Parametros getListaParametrosResponse() {
        return listaParametrosResponse;
    }

    /**
     * Sets the value of the listaParametrosResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parametros }
     *     
     */
    public void setListaParametrosResponse(Parametros value) {
        this.listaParametrosResponse = value;
    }

}
