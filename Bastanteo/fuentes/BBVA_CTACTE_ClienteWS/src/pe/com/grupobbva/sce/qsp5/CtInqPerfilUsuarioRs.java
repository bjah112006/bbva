//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package pe.com.grupobbva.sce.qsp5;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ctInqPerfilUsuarioRs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ctInqPerfilUsuarioRs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Header" type="{http://grupobbva.com.pe/sce/qsp5/}ctHeaderRs"/>
 *         &lt;element name="Data" type="{http://grupobbva.com.pe/sce/qsp5/}ctBodyRs" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ctInqPerfilUsuarioRs", propOrder = {
    "header",
    "data"
})
public class CtInqPerfilUsuarioRs {

    @XmlElement(name = "Header", required = true)
    protected CtHeaderRs header;
    @XmlElement(name = "Data")
    protected CtBodyRs data;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link CtHeaderRs }
     *     
     */
    public CtHeaderRs getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link CtHeaderRs }
     *     
     */
    public void setHeader(CtHeaderRs value) {
        this.header = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link CtBodyRs }
     *     
     */
    public CtBodyRs getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link CtBodyRs }
     *     
     */
    public void setData(CtBodyRs value) {
        this.data = value;
    }

}
