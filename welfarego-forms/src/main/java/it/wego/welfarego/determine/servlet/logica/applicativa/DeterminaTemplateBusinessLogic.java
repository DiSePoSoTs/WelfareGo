package it.wego.welfarego.determine.servlet.logica.applicativa;

import it.wego.welfarego.persistence.constants.Determine;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Template;

public class DeterminaTemplateBusinessLogic {
    private PaiEvento primoEvento;
    private Template template;
    private String tipoDetermina;

    public DeterminaTemplateBusinessLogic(PaiEvento primoEvento) {
        this.primoEvento = primoEvento;
    }

    public Template getTemplate() {
        return template;
    }

    public String getTipoDetermina() {
        return tipoDetermina;
    }

    public DeterminaTemplateBusinessLogic invoke() {
        PaiIntervento paiIntervento = primoEvento.getPaiIntervento();
        char statoIntervento = paiIntervento.getStatoInt();

        switch (statoIntervento) {
            case PaiIntervento.STATO_INTERVENTO_APERTO:
                tipoDetermina = Determine.ESECUTIVITA;
                template = paiIntervento.getTipologiaIntervento().getCodTmplEseMul();
                break;
            case PaiIntervento.STATO_INTERVENTO_RIMANDATO:
                tipoDetermina = Determine.ESECUTIVITA;
                template = paiIntervento.getTipologiaIntervento().getCodTmplEseMul();
                break;
            case PaiIntervento.STATO_INTERVENTO_CHIUSO:
                tipoDetermina = Determine.CHIUSURA;
                template = paiIntervento.getTipologiaIntervento().getCodTmplChiusMul();
                break;
            default:
                tipoDetermina = Determine.VARIAZIONE;
                template = paiIntervento.getTipologiaIntervento().getCodTmplVarMul();
        }

        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DetarminaTemplateBusinessLogic{");
        sb.append("tipoDetermina='").append(tipoDetermina).append('\'');
        sb.append('}');
        return sb.toString();
    }
}