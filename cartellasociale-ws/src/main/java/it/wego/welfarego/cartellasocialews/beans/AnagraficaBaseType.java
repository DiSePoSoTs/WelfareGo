
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per AnagraficaBaseType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AnagraficaBaseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codiceNucleoFamiliare" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="nome">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cognome">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceFiscale" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}CodiceFiscaleType" minOccurs="0"/>
 *         &lt;element name="sesso" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}SessoType"/>
 *         &lt;element name="cittadinanza1" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="cittadinanza2" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="nascita" type="{http://tipigenerali.cartellasociale.sanita.insiel.it}NascitaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnagraficaBaseType", namespace = "http://tipigenerali.cartellasociale.sanita.insiel.it", propOrder = {
    "codiceNucleoFamiliare",
    "nome",
    "cognome",
    "codiceFiscale",
    "sesso",
    "cittadinanza1",
    "cittadinanza2",
    "nascita"
})
public class AnagraficaBaseType {

    protected Long codiceNucleoFamiliare;
    @XmlElement(required = true)
    protected String nome;
    @XmlElement(required = true)
    protected String cognome;
    protected String codiceFiscale;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected SessoType sesso;
    protected long cittadinanza1;
    protected Long cittadinanza2;
    @XmlElement(required = true)
    protected NascitaType nascita;

    /**
     * Recupera il valore della proprietà codiceNucleoFamiliare.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodiceNucleoFamiliare() {
        return codiceNucleoFamiliare;
    }

    /**
     * Imposta il valore della proprietà codiceNucleoFamiliare.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodiceNucleoFamiliare(Long value) {
        this.codiceNucleoFamiliare = value;
    }

    /**
     * Recupera il valore della proprietà nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietà cognome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietà cognome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietà codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietà sesso.
     * 
     * @return
     *     possible object is
     *     {@link SessoType }
     *     
     */
    public SessoType getSesso() {
        return sesso;
    }

    /**
     * Imposta il valore della proprietà sesso.
     * 
     * @param value
     *     allowed object is
     *     {@link SessoType }
     *     
     */
    public void setSesso(SessoType value) {
        this.sesso = value;
    }

    /**
     * Recupera il valore della proprietà cittadinanza1.
     * 
     */
    public long getCittadinanza1() {
        return cittadinanza1;
    }

    /**
     * Imposta il valore della proprietà cittadinanza1.
     * 
     */
    public void setCittadinanza1(long value) {
        this.cittadinanza1 = value;
    }

    /**
     * Recupera il valore della proprietà cittadinanza2.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCittadinanza2() {
        return cittadinanza2;
    }

    /**
     * Imposta il valore della proprietà cittadinanza2.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCittadinanza2(Long value) {
        this.cittadinanza2 = value;
    }

    /**
     * Recupera il valore della proprietà nascita.
     * 
     * @return
     *     possible object is
     *     {@link NascitaType }
     *     
     */
    public NascitaType getNascita() {
        return nascita;
    }

    /**
     * Imposta il valore della proprietà nascita.
     * 
     * @param value
     *     allowed object is
     *     {@link NascitaType }
     *     
     */
    public void setNascita(NascitaType value) {
        this.nascita = value;
    }

}
