
package pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.base.AuditRequest;
import pe.bbvacontinental.medios.gestiondemanda.contentmanagerws.ws.types.base.Parametros;


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
 *         &lt;element name="auditRequest" type="{http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/types/base}AuditRequest"/>
 *         &lt;element name="listaParametrosRequest" type="{http://bbvacontinental.pe/medios/gestiondemanda/contentmanagerws/ws/types/base}Parametros"/>
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
    "auditRequest",
    "listaParametrosRequest"
})
@XmlRootElement(name = "obtenerDocRequest")
public class ObtenerDocRequest {

    @XmlElement(required = true)
    protected AuditRequest auditRequest;
    @XmlElement(required = true)
    protected Parametros listaParametrosRequest;

    /**
     * Gets the value of the auditRequest property.
     * 
     * @return
     *     possible object is
     *     {@link AuditRequest }
     *     
     */
    public AuditRequest getAuditRequest() {
        return auditRequest;
    }

    /**
     * Sets the value of the auditRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuditRequest }
     *     
     */
    public void setAuditRequest(AuditRequest value) {
        this.auditRequest = value;
    }

    /**
     * Gets the value of the listaParametrosRequest property.
     * 
     * @return
     *     possible object is
     *     {@link Parametros }
     *     
     */
    public Parametros getListaParametrosRequest() {
        return listaParametrosRequest;
    }

    /**
     * Sets the value of the listaParametrosRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parametros }
     *     
     */
    public void setListaParametrosRequest(Parametros value) {
        this.listaParametrosRequest = value;
    }

}
