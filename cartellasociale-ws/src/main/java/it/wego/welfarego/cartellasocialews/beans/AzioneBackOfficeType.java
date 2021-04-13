
package it.wego.welfarego.cartellasocialews.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per AzioneBackOfficeType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="AzioneBackOfficeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ChiusuraMassivaCartelle"/>
 *     &lt;enumeration value="AggiornamentoNomenclatura"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AzioneBackOfficeType")
@XmlEnum
public enum AzioneBackOfficeType {

    @XmlEnumValue("ChiusuraMassivaCartelle")
    CHIUSURA_MASSIVA_CARTELLE("ChiusuraMassivaCartelle"),
    @XmlEnumValue("AggiornamentoNomenclatura")
    AGGIORNAMENTO_NOMENCLATURA("AggiornamentoNomenclatura");
    private final String value;

    AzioneBackOfficeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AzioneBackOfficeType fromValue(String v) {
        for (AzioneBackOfficeType c: AzioneBackOfficeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
