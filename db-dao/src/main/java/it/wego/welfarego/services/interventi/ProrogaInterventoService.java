package it.wego.welfarego.services.interventi;

import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Weeks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProrogaInterventoService {

    private PaiInterventoDao paiInterventoDao = null;

    public ProrogaInterventoService(PaiInterventoDao paiInterventoDao) {
        this.paiInterventoDao = paiInterventoDao;
    }


    public PaiIntervento prorogaIntervento(PaiIntervento daProrogare) throws Exception {

        PaiIntervento nuovoIntervento = clona_intervento(daProrogare);

        valorizza_proprieta(nuovoIntervento, daProrogare);

        return nuovoIntervento;
    }

    public PaiIntervento clona_intervento(PaiIntervento daProrogare) throws Exception {
        PaiInterventoPK paiInterventoPK = daProrogare.getPaiInterventoPK();
        Integer codPai = paiInterventoPK.getCodPai();
        String codTipInt = paiInterventoPK.getCodTipint();
        Integer cntTipint = paiInterventoPK.getCntTipint();


        return paiInterventoDao.clonaIntervento(String.valueOf(codPai), codTipInt, String.valueOf(cntTipint));
    }

    void valorizza_proprieta(PaiIntervento nuovoIntervento, PaiIntervento daProrogare) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date dataFineNuovoIntervento = null;
        Date dataInizioNuovoIntervento = null;
        Integer durataMesiNuovoIntervento = null;


        Date dataFinePadre = daProrogare.calculateDtFine(); // deve essere 31/12... entra dentro e controlla
        DateTime jodatDataInizio = new DateTime(dataFinePadre).plusDays(1);
        dataInizioNuovoIntervento = jodatDataInizio.toDate();

        if (is_proroga_per_date(daProrogare)) {

            int year = jodatDataInizio.getYear();
            dataFineNuovoIntervento = sdf.parse("31/12/" + year);


        } else {

            int year = jodatDataInizio.getYear();
            int monthOfYear = jodatDataInizio.getMonthOfYear();
            dataInizioNuovoIntervento = sdf.parse("01/" + monthOfYear + "/" + year);
            dataFineNuovoIntervento = sdf.parse("31/12/" + year);
            durataMesiNuovoIntervento = Months.monthsBetween(new DateTime(dataInizioNuovoIntervento), new DateTime(dataFineNuovoIntervento)).getMonths();
            durataMesiNuovoIntervento = durataMesiNuovoIntervento + 1;
        }


        fill_data_fine_proroga(nuovoIntervento);

        // ---
        // i campi relativi alle durate li tengo insieme
        fill_data_avvio(nuovoIntervento, dataInizioNuovoIntervento);
        fill_data_fine(nuovoIntervento, dataFineNuovoIntervento);
        fill_durata_mesi(nuovoIntervento, durataMesiNuovoIntervento);
        fill_durata_settimane(nuovoIntervento);
        // ---

        fill_rinnovato(nuovoIntervento);


        fill_durata_mesi_proroga(nuovoIntervento);
        fill_stato_attuale(nuovoIntervento);
        fill_quantita(nuovoIntervento, daProrogare);
        fill_codice_impegno_proroga(nuovoIntervento);
        fill_data_richiesta_approvazione(nuovoIntervento, daProrogare);
        fill_dati_originali(nuovoIntervento, daProrogare);
        fill_data_avvio_proposta(nuovoIntervento, dataInizioNuovoIntervento);




    }

    private void fill_durata_settimane(PaiIntervento nuovoIntervento) {

        TipologiaIntervento tipologiaIntervento = nuovoIntervento.getTipologiaIntervento();
        ParametriIndata idParamUniMis = tipologiaIntervento.getIdParamUniMis();
        String unitaMis = idParamUniMis.getDesParam();
        Date dal = nuovoIntervento.getDtAvvio();
        Date al = nuovoIntervento.calculateDtFine();
        Integer numSettimane = Weeks.weeksBetween(new DateTime(dal), new DateTime(al)).getWeeks();

        if (unitaMis.contains("sett") ) {
            nuovoIntervento.setDurSettimane(numSettimane);
        }
    }

    /**
     *
     * @param daProrogare
     * @return euro, sett, mese a seconda che il tipo di intervento sia configurato come intervento
     * giornaliero, settimanale, mensile.
     */
    private String get_tipologia_unita_misura(PaiIntervento daProrogare) {
        return daProrogare.getTipologiaIntervento().getIdParamUniMis().getDesParam();
    }

    private void fill_durata_mesi(PaiIntervento nuovoIntervento, Integer durataMesi) {
        nuovoIntervento.setDurMesi(durataMesi);
    }

    private void fill_data_fine(PaiIntervento nuovoIntervento, Date nuovaDataFine) {
        nuovoIntervento.setDtFine(nuovaDataFine);
    }

    private boolean is_proroga_per_date(PaiIntervento daProrogare) {
        TipologiaIntervento tipologiaIntervento = daProrogare.getTipologiaIntervento();
        boolean b = tipologiaIntervento.getFlgFineDurata() == TipologiaIntervento.FLG_FINE_DURATA_F;
        return b;
    }

    void fill_quantita(PaiIntervento nuovoIntervento, PaiIntervento daProrogare) {
        nuovoIntervento.setQuantita(daProrogare.getQuantita());
    }

    void fill_codice_impegno_proroga(PaiIntervento nuovoIntervento) {
        nuovoIntervento.setCodImpProroga(null);
    }

    void fill_data_fine_proroga(PaiIntervento nuovoIntervento) {
        nuovoIntervento.setDtFineProroga(null);
    }

    void fill_durata_mesi_proroga(PaiIntervento nuovoIntervento) {
        nuovoIntervento.setDurMesiProroga(null);
    }

    void fill_rinnovato(PaiIntervento nuovoIntervento) {
        String flgRinnovo = nuovoIntervento.getTipologiaIntervento().getFlgRinnovo();
        if (flgRinnovo.equals(TipologiaIntervento.FLGRINNOVO_AUTOMATICO_BUDGET_PRECEDENTE)
                || flgRinnovo.equals(TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_S)) {
            nuovoIntervento.setRinnovato(PaiInterventoDao.DA_RINNOVARE);
        } else {
            nuovoIntervento.setRinnovato(PaiInterventoDao.RINNOVATO);
        }
    }

    void fill_data_avvio_proposta(PaiIntervento nuovoIntervento, Date nuovaDataInizio) {
        nuovoIntervento.setDataAvvioProposta(nuovaDataInizio);
    }

    void fill_stato_attuale(PaiIntervento nuovoIntervento) {
        nuovoIntervento.setStatoAttuale("Proroga da determinare");
    }

    void fill_dati_originali(PaiIntervento nuovoIntervento, PaiIntervento daProrogare) {
        PaiInterventoPK daProrogarePk = daProrogare.getPaiInterventoPK();
        String codPai = daProrogarePk.getCodPai() + "";
        String codTipInt = daProrogarePk.getCodTipint();
        String cntTipint = daProrogarePk.getCntTipint() + "";

        String datiOriginali = daProrogare.getDatiOriginali();
        if (datiOriginali == null) {
            datiOriginali = "";
        }
        nuovoIntervento.setDatiOriginali("Proroga generata automaticamente del seguente intervento: codice pai " + codPai +
                " - tipo intervento " + codTipInt + " - contatore " + cntTipint + "\n" + datiOriginali.replace(PaiInterventoDao.DICITURA_PROROGA, ""));
    }

    void fill_data_richiesta_approvazione(PaiIntervento nuovoIntervento, PaiIntervento daProrogare) {
        nuovoIntervento.setDataRichiestaApprovazione(daProrogare.getDataRichiestaApprovazione());
    }

    void fill_data_avvio(PaiIntervento nuovoIntervento, Date nuovaDataInizio) {
        nuovoIntervento.setDtAvvio(nuovaDataInizio);
    }
}

