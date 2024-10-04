
package it.wego.welfarego.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per anonymous complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="anagrafica" type="{http://cartellasociale.sanita.insiel.it}anagraficaType"/&amp;gt;
 *         &amp;lt;element name="profilo" type="{http://cartellasociale.sanita.insiel.it}profiloType"/&amp;gt;
 *         &amp;lt;element name="progetto" type="{http://cartellasociale.sanita.insiel.it}progettoType"/&amp;gt;
 *         &amp;lt;element name="codice" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}CodiceOperatoreType"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *       &amp;lt;attribute name="versione" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "anagrafica",
    "profilo",
    "progetto",
    "codice"
})
@XmlRootElement(name = "InserimentoCartellaSociale")
public class InserimentoCartellaSociale {

    @XmlElement(required = true)
    protected AnagraficaType anagrafica;
    @XmlElement(required = true)
    protected ProfiloType profilo;
    @XmlElement(required = true)
    protected ProgettoType progetto;
    @XmlElement(required = true)
    protected String codice;
    @XmlAttribute(name = "versione", required = true)
    protected String versione;

    /**
     * Recupera il valore della proprietà anagrafica.
     * 
     * @return
     *     possible object is
     *     {@link AnagraficaType }
     *     
     */
    public AnagraficaType getAnagrafica() {
        return anagrafica;
    }

    /**
     * Imposta il valore della proprietà anagrafica.
     * 
     * @param value
     *     allowed object is
     *     {@link AnagraficaType }
     *     
     */
    public void setAnagrafica(AnagraficaType value) {
        this.anagrafica = value;
    }

    /**
     * Recupera il valore della proprietà profilo.
     * 
     * @return
     *     possible object is
     *     {@link ProfiloType }
     *     
     */
    public ProfiloType getProfilo() {
        return profilo;
    }

    /**
     * Imposta il valore della proprietà profilo.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfiloType }
     *     
     */
    public void setProfilo(ProfiloType value) {
        this.profilo = value;
    }

    /**
     * Recupera il valore della proprietà progetto.
     * 
     * @return
     *     possible object is
     *     {@link ProgettoType }
     *     
     */
    public ProgettoType getProgetto() {
        return progetto;
    }

    /**
     * Imposta il valore della proprietà progetto.
     * 
     * @param value
     *     allowed object is
     *     {@link ProgettoType }
     *     
     */
    public void setProgetto(ProgettoType value) {
        this.progetto = value;
    }

    /**
     * Recupera il valore della proprietà codice.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodice() {
        return codice;
    }

    /**
     * Imposta il valore della proprietà codice.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodice(String value) {
        this.codice = value;
    }

    /**
     * Recupera il valore della proprietà versione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersione() {
        return versione;
    }

    /**
     * Imposta il valore della proprietà versione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersione(String value) {
        this.versione = value;
    }

}
