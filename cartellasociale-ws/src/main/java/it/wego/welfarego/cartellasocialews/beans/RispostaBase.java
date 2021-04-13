
package it.wego.welfarego.cartellasocialews.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RispostaBase complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RispostaBase">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="esito" type="{http://framework.cartellasociale.sanita.insiel.it}Esito"/>
 *         &lt;element name="errori" type="{http://framework.cartellasociale.sanita.insiel.it}Messaggio" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="avvisi" type="{http://framework.cartellasociale.sanita.insiel.it}Messaggio" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
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
    RicevutaModificaProgetto.class,
    RicevutaDiario.class,
    RicevutaRiattivaCartella.class,
    RicevutaPersonaRiferimento.class,
    RicevutaAzioniBackOffice.class,
    RicevutaChiudiCartella.class,
    RicevutaChiudiInterventiDaLista.class,
    RicevutaModificaProfilo.class,
    RicevutaIntervento.class,
    RicevutaModificaDiario.class,
    RicevutaCartella.class,
    RicevutaModificaIntervento.class,
    RicevutaModificaAnagrafica.class
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errori property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrori().add(newItem);
     * </pre>
     * 
     * 
     * <p>
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the avvisi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAvvisi().add(newItem);
     * </pre>
     * 
     * 
     * <p>
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
