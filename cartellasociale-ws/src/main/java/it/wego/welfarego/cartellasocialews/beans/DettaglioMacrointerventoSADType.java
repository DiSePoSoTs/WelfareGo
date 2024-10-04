
package it.wego.welfarego.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per DettaglioMacrointerventoSADType complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="DettaglioMacrointerventoSADType"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="tipologiaMacroInterventoSad" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="arcoTemporale" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="frequenza" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *         &amp;lt;element name="numeroVolte" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *         &amp;lt;element name="erogatore" type="{http://www.w3.org/2001/XMLSchema}string"/&amp;gt;
 *         &amp;lt;element name="totOre" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *         &amp;lt;element name="totMin" type="{http://www.w3.org/2001/XMLSchema}int"/&amp;gt;
 *         &amp;lt;element name="note" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NoteType"/&amp;gt;
 *         &amp;lt;element name="listaMicrointerventiCollegatiAttivi" type="{http://cartellasociale.sanita.insiel.it}listaMicroInterventiSADType"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DettaglioMacrointerventoSADType", propOrder = {
    "tipologiaMacroInterventoSad",
    "arcoTemporale",
    "frequenza",
    "numeroVolte",
    "erogatore",
    "totOre",
    "totMin",
    "note",
    "listaMicrointerventiCollegatiAttivi"
})
public class DettaglioMacrointerventoSADType {

    @XmlElement(required = true)
    protected String tipologiaMacroInterventoSad;
    @XmlElement(required = true)
    protected String arcoTemporale;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer frequenza;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer numeroVolte;
    @XmlElement(required = true, nillable = true)
    protected String erogatore;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer totOre;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer totMin;
    @XmlElement(required = true, nillable = true)
    protected String note;
    @XmlElement(required = true)
    protected ListaMicroInterventiSADType listaMicrointerventiCollegatiAttivi;

    /**
     * Recupera il valore della proprietà tipologiaMacroInterventoSad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipologiaMacroInterventoSad() {
        return tipologiaMacroInterventoSad;
    }

    /**
     * Imposta il valore della proprietà tipologiaMacroInterventoSad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipologiaMacroInterventoSad(String value) {
        this.tipologiaMacroInterventoSad = value;
    }

    /**
     * Recupera il valore della proprietà arcoTemporale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArcoTemporale() {
        return arcoTemporale;
    }

    /**
     * Imposta il valore della proprietà arcoTemporale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArcoTemporale(String value) {
        this.arcoTemporale = value;
    }

    /**
     * Recupera il valore della proprietà frequenza.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFrequenza() {
        return frequenza;
    }

    /**
     * Imposta il valore della proprietà frequenza.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFrequenza(Integer value) {
        this.frequenza = value;
    }

    /**
     * Recupera il valore della proprietà numeroVolte.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroVolte() {
        return numeroVolte;
    }

    /**
     * Imposta il valore della proprietà numeroVolte.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroVolte(Integer value) {
        this.numeroVolte = value;
    }

    /**
     * Recupera il valore della proprietà erogatore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErogatore() {
        return erogatore;
    }

    /**
     * Imposta il valore della proprietà erogatore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErogatore(String value) {
        this.erogatore = value;
    }

    /**
     * Recupera il valore della proprietà totOre.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotOre() {
        return totOre;
    }

    /**
     * Imposta il valore della proprietà totOre.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotOre(Integer value) {
        this.totOre = value;
    }

    /**
     * Recupera il valore della proprietà totMin.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotMin() {
        return totMin;
    }

    /**
     * Imposta il valore della proprietà totMin.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotMin(Integer value) {
        this.totMin = value;
    }

    /**
     * Recupera il valore della proprietà note.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Imposta il valore della proprietà note.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

    /**
     * Recupera il valore della proprietà listaMicrointerventiCollegatiAttivi.
     * 
     * @return
     *     possible object is
     *     {@link ListaMicroInterventiSADType }
     *     
     */
    public ListaMicroInterventiSADType getListaMicrointerventiCollegatiAttivi() {
        return listaMicrointerventiCollegatiAttivi;
    }

    /**
     * Imposta il valore della proprietà listaMicrointerventiCollegatiAttivi.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaMicroInterventiSADType }
     *     
     */
    public void setListaMicrointerventiCollegatiAttivi(ListaMicroInterventiSADType value) {
        this.listaMicrointerventiCollegatiAttivi = value;
    }

}
