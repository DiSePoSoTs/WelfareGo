
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per EsitoOperazioneEnum.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="EsitoOperazioneEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OK"/>
 *     &lt;enumeration value="ERRORI"/>
 *     &lt;enumeration value="WARNING"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
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
