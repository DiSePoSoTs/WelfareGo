//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.22 at 06:46:27 PM CEST 
//


package it.wego.welfarego.xsd.cartellasociale;

import java.util.ArrayList;
import java.util.List;
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
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="assistente_sociale" type="{}assistente_sociale_type"/>
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
    "assistenteSociale"
})
@XmlRootElement(name = "elenco_assistenti_sociali")
public class ElencoAssistentiSociali {

    @XmlElement(name = "assistente_sociale", required = true)
    protected List<AssistenteSocialeType> assistenteSociale;

    /**
     * Gets the value of the assistenteSociale property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assistenteSociale property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssistenteSociale().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssistenteSocialeType }
     * 
     * 
     */
    public List<AssistenteSocialeType> getAssistenteSociale() {
        if (assistenteSociale == null) {
            assistenteSociale = new ArrayList<AssistenteSocialeType>();
        }
        return this.assistenteSociale;
    }

}
