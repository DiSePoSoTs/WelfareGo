
package it.wego.welfarego.cartellasocialews.beans;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per RispostaBase complex type.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType name="RispostaBase"&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="esito" type="{http://framework.cartellasociale.sanita.insiel.it}Esito"/&amp;gt;
 *         &amp;lt;element name="errori" type="{http://framework.cartellasociale.sanita.insiel.it}Messaggio" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 *         &amp;lt;element name="avvisi" type="{http://framework.cartellasociale.sanita.insiel.it}Messaggio" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RispostaBase", namespace = "http://framework.cartellasociale.sanita.insiel.it", propOrder = {
    "esito",
    "errori",
    "avvisi"
})
@XmlSeeAlso({
    RicevutaChiudiInterventiDaLista.class,
    RicevutaRiattivaCartella.class,
    RicevutaChiudiCartella.class,
    RicevutaPersonaRiferimento.class,
    RicevutaModificaDiario.class,
    RicevutaModificaIntervento.class,
    RicevutaModificaProgetto.class,
    RicevutaModificaProfilo.class,
    RicevutaInserimentoProfilo.class,
    RicevutaModificaAnagrafica.class,
    RicevutaDiario.class,
    RicevutaIntervento.class,
    RicevutaCartella.class,
    RicevutaAzioniBackOffice.class
})
public class RispostaBase {

    @XmlElement(required = true)
    protected Esito esito;
    protected List<Messaggio> errori;
    protected List<Messaggio> avvisi;

    /**
     * Recupera il valore della proprietà esito.
     * 
     * @return
     *     possible object is
     *     {@link Esito }
     *     
     */
    public Esito getEsito() {
        return esito;
    }

    /**
     * Imposta il valore della proprietà esito.
     * 
     * @param value
     *     allowed object is
     *     {@link Esito }
     *     
     */
    public void setEsito(Esito value) {
        this.esito = value;
    }

    /**
     * Gets the value of the errori property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the errori property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getErrori().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link Messaggio }
     * 
     * 
     */
    public List<Messaggio> getErrori() {
        if (errori == null) {
            errori = new ArrayList<Messaggio>();
        }
        return this.errori;
    }

    /**
     * Gets the value of the avvisi property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the avvisi property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getAvvisi().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link Messaggio }
     * 
     * 
     */
    public List<Messaggio> getAvvisi() {
        if (avvisi == null) {
            avvisi = new ArrayList<Messaggio>();
        }
        return this.avvisi;
    }

}
