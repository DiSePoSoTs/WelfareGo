
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Dati per l'autenticazione del chiamante.
 * 
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="utente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uorgStrt0" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "utente",
    "password",
    "uorgStrt0"
})
@XmlRootElement(name = "AutenticazioneE", namespace = "http://framework.cartellasociale.sanita.insiel.it")
public class AutenticazioneE {

    @XmlElement(namespace = "http://framework.cartellasociale.sanita.insiel.it", required = true)
    protected String utente;
    @XmlElement(namespace = "http://framework.cartellasociale.sanita.insiel.it", required = true)
    protected String password;
    @XmlElement(namespace = "http://framework.cartellasociale.sanita.insiel.it")
    protected long uorgStrt0;

    /**
     * Recupera il valore della proprietà utente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUtente() {
        return utente;
    }

    /**
     * Imposta il valore della proprietà utente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUtente(String value) {
        this.utente = value;
    }

    /**
     * Recupera il valore della proprietà password.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta il valore della proprietà password.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Recupera il valore della proprietà uorgStrt0.
     * 
     */
    public long getUorgStrt0() {
        return uorgStrt0;
    }

    /**
     * Imposta il valore della proprietà uorgStrt0.
     * 
     */
    public void setUorgStrt0(long value) {
        this.uorgStrt0 = value;
    }

}
