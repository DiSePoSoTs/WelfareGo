
package it.trieste.comune.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per EsitoOperazioneEnum.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="EsitoOperazioneEnum"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="OK"/&amp;gt;
 *     &amp;lt;enumeration value="ERRORI"/&amp;gt;
 *     &amp;lt;enumeration value="WARNING"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "EsitoOperazioneEnum", namespace = "http://framework.cartellasociale.sanita.insiel.it")
@XmlEnum
public enum EsitoOperazioneEnum {

    OK,
    ERRORI,
    WARNING;

    public String value() {
        return name();
    }

    public static EsitoOperazioneEnum fromValue(String v) {
        return valueOf(v);
    }

}
