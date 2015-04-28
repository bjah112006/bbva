//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package hiper.webservices.impl.hfirmas;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import hiper.spring.beans.hfirmas.webservices.xsd.EmpresaBE;


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
 *         &lt;element name="listaEmpresas" type="{http://webservices.hfirmas.beans.spring.hiper/xsd}Empresa_BE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codigoCentral" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDOI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nroDOI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="razonSocial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "listaEmpresas",
    "codigoCentral",
    "tipoDOI",
    "nroDOI",
    "razonSocial"
})
@XmlRootElement(name = "escindirEmpresas")
public class EscindirEmpresas {

    @XmlElement(nillable = true)
    protected List<EmpresaBE> listaEmpresas;
    @XmlElement(nillable = true)
    protected String codigoCentral;
    @XmlElement(nillable = true)
    protected String tipoDOI;
    @XmlElement(nillable = true)
    protected String nroDOI;
    @XmlElement(nillable = true)
    protected String razonSocial;

    /**
     * Gets the value of the listaEmpresas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaEmpresas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaEmpresas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EmpresaBE }
     * 
     * 
     */
    public List<EmpresaBE> getListaEmpresas() {
        if (listaEmpresas == null) {
            listaEmpresas = new ArrayList<EmpresaBE>();
        }
        return this.listaEmpresas;
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
     * Gets the value of the nroDOI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNroDOI() {
        return nroDOI;
    }

    /**
     * Sets the value of the nroDOI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNroDOI(String value) {
        this.nroDOI = value;
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

}
