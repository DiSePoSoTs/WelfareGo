
package it.trieste.comune.cartellasocialews.beans;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java per AzioneBackOfficeType.
 * 
 * &lt;p&gt;Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="AzioneBackOfficeType"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&amp;gt;
 *     &amp;lt;enumeration value="ChiusuraMassivaCartelle"/&amp;gt;
 *     &amp;lt;enumeration value="AggiornamentoNomenclatura"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
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
