package it.wego.welfarego.scheduler.rinnovi.helper;

import com.google.gson.Gson;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SchedulerHelper {
    public static final String INTERVENTI_SENZA_BUDGET = "AZ020";

    public static String interventi_aperti_senza_data_fine_o_durata(List<PaiIntervento> defectivePaiInt) {
        String msg = String.format("%s interventi aperti senza data fine o durata : %s", defectivePaiInt.size(), SchedulerHelper.dumpDefectivePaiInt(defectivePaiInt));
        return msg;
    }

    public static String dumpPkIntervento(PaiIntervento item) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(item.getPaiInterventoPK());
        return jsonString;
    }

        public static String dumpDefectivePaiInt(List<PaiIntervento> defectivePaiInt) {

        List<String> interventiKeys = new ArrayList<String>();
        for (PaiIntervento item : defectivePaiInt) {
            String interventoPk = dumpPkIntervento(item);
            interventiKeys.add(interventoPk);
        }

        String dump = String.format("{\"interventiConDifetti\": [%s]}", StringUtils.join(interventiKeys, ','));
        return dump;
    }


    /**
     * @param paiIntervento
     * @return true se flg_rinnovo == FLG_RINNOVO_AUTOMATICO_S || FLG_RINNOVO_AUTOMATICO_BUDGET_PRECEDENTE
     */
    public static boolean is_Rinnovo_Automatico(PaiIntervento paiIntervento) {
        String flgRinnovo = paiIntervento.getTipologiaIntervento().getFlgRinnovo();
        boolean isRinnovoAutomatico = flgRinnovo.equals(TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_S) ||
                flgRinnovo.equals(TipologiaIntervento.FLGRINNOVO_AUTOMATICO_BUDGET_PRECEDENTE);

        return isRinnovoAutomatico;
    }

    /**
     * @param paiIntervento
     * @return true is flg_rinnovo == FLG_RINNOVO_AUTOMATICO_DETERMINA
     */
    public static boolean is_Rinnovo_Per_Determina_Parziale(PaiIntervento paiIntervento) {
        String flgRinnovo = paiIntervento.getTipologiaIntervento().getFlgRinnovo();
        boolean isRinnovoPerDetermina = flgRinnovo.equals(TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_DETERMINA);
        return isRinnovoPerDetermina;
    }

    /**
     * @param paiIntervento
     * @return true se flg_rinnovo = FLG_RINNOVO_AUTOMATICO_PROROGA e durata Mesi Proroga != null
     * <pre>
     *     SELECT
     * dt_avvio, MESI_PROROGA, INTERVENTO.cod_tipint, tip_intervento.DES_TIPINT
     * --MESI_PROROGA, dur_mesi, INTERVENTO.*
     * FROM
     * PAI_INTERVENTO intervento
     * JOIN TIPOLOGIA_INTERVENTO tip_intervento
     * ON INTERVENTO.COD_TIPINT = tip_intervento.COD_TIPINT
     * WHERE
     * 1=1
     * --AND intervento.DT_FINE < TO_DATE('31-12-2018', 'dd-MM-yyyy')
     * --AND intervento.DT_AVVIO > to_date('01-01-2018', 'dd-MM-yyyy')
     * --AND intervento.COD_TIPINT IN ('EC001', 'EC002')
     * --AND INTERVENTO.STATO_INT = 'E'
     * AND MESI_PROROGA IS NOT NULL
     * ORDER BY dt_avvio desc
     * ;
     * In comune ne hanno dismesso l'uso intorno al 2016, i record con mesi_proroga successivi a quelle date sono dovuti ad un utilizzo non conforme
     * del programma da parte di alcuni utenti.
     *
     * </pre>
     */
    public static boolean is_Rinnovo_Per_Proroga(PaiIntervento paiIntervento) {
        String flgRinnovo = paiIntervento.getTipologiaIntervento().getFlgRinnovo();
        return flgRinnovo.equals(TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_PROROGA) && paiIntervento.getDurMesiProroga() != null;
    }

}
