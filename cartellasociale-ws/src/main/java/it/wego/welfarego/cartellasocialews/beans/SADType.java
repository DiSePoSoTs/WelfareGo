
package it.wego.welfarego.cartellasocialews.beans;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per SADType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="SADType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="obiettivi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="macrointerventoSad" type="{http://cartellasociale.sanita.insiel.it}DettaglioMacrointerventoSADType" maxOccurs="unbounded"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SADType", propOrder = {
    "obiettivi",
    "macrointerventoSad"
})
public class SADType {

    @XmlElement(nillable = true)
    protected String obiettivi;
    @XmlElement(required = true)
    protected List<DettaglioMacrointerventoSADType> macrointerventoSad;

    /**
     * Recupera il valore della proprietà obiettivi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObiettivi() {
        return obiettivi;
    }

    /**
     * Imposta il valore della proprietà obiettivi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObiettivi(String value) {
        this.obiettivi = value;
    }

    /**
     * Gets the value of the macrointerventoSad property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the macrointerventoSad property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getMacrointerventoSad().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link DettaglioMacrointerventoSADType }
     * 
     * 
     */
    public List<DettaglioMacrointerventoSADType> getMacrointerventoSad() {
        if (macrointerventoSad == null) {
            macrointerventoSad = new ArrayList<DettaglioMacrointerventoSADType>();
        }
        return this.macrointerventoSad;
    }

}
