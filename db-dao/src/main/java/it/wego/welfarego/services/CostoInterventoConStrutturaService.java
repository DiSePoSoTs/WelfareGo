package it.wego.welfarego.services;


import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Weeks;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Date;

public class CostoInterventoConStrutturaService {


    /**
     * @return il costo di un intervento con struttura
     * <pre>
     * casi possibili:
     * 1.   il tipo di intervento prevede una data fine.
     * 2.   il tipo di intervento prevede una durata.
     * la quantità può essere espressa in giorni|settimane|mesi
     *
     * della voce costo di tariffa, che faccio ??
     * </pre>
     */
    public BigDecimal calcolaCosto(PaiIntervento intervento, BigDecimal quantita
            , @Nullable Date dal
            , @Nullable Date al
            , @Nullable Integer durata) {

        BigDecimal costo = null;
        TipologiaIntervento tipologiaIntervento = intervento.getTipologiaIntervento();
        BigDecimal tariffa = intervento.getTariffa().getCosto();
        char flgFineDurata = tipologiaIntervento.getFlgFineDurata();
        String paramUnitaMisuraDurata = tipologiaIntervento.getIdParamUniMis().getDesParam();


        if (intervento_prevede_data_fine(flgFineDurata)) {
            costo = calcola_costo_tramite_intervallo_tra_date(paramUnitaMisuraDurata, dal, al, quantita);

        } else if (intervento_prevede_durata(flgFineDurata)) {
            costo = calcola_costo_tramite_durata(paramUnitaMisuraDurata, quantita, durata);

        } else {
            throw new IllegalArgumentException("questo intervento non si basa su una durata espressa in mesi o settimane e nemmeno su un intervallo tra date. ");
        }

//        quale dei 2 ?
//        return costo * tariffa;
        return costo ;
    }


    private BigDecimal calcola_costo_tramite_durata(String paramUnitaMisuraDurata, BigDecimal quantita, Integer durata) {
        if (paramUnitaMisuraDurata.contains("mens")) {
            return quantita.multiply(BigDecimal.valueOf(durata));

        } else if (paramUnitaMisuraDurata.contains("sett")) {
            throw new RuntimeException("al momento non è gestito il caso degli interventi a durata settimanle con struttura.");

        } else {
            String msgTemplate = "non posso calcolare il costo per questo intervento paramUnitaMisuraDurata:%s";
            String.format(msgTemplate, paramUnitaMisuraDurata);
            throw new RuntimeException(msgTemplate);
        }
    }

    /**
     * return quantita * numero  di [giorni|settimane|mesi]
     */
    private BigDecimal calcola_costo_tramite_intervallo_tra_date(String paramUnitaMisuraDurata, Date dal, Date al, BigDecimal quantita) {
        if (dal == null) {
            throw new IllegalArgumentException("la data dal deve essere valorizzata.");
        }
        if (al == null) {
            throw new IllegalArgumentException("la data al deve essere valorizzata.");
        }

        BigDecimal unitaDimisura = null;
        DateTime dtDal = new DateTime(dal).withZone(DateTimeZone.forID("Europe/Rome"));
        DateTime dtAl = new DateTime(al).withZone(DateTimeZone.forID("Europe/Rome"));
        dtAl = dtAl.plusDays(1);

        if(paramUnitaMisuraDurata.contains("euro")){
            Integer giorni = Days.daysBetween(dtDal, dtAl).getDays();
            unitaDimisura = BigDecimal.valueOf(giorni);

        } else if(paramUnitaMisuraDurata.contains("sett")){
            Integer settimane= Weeks.weeksBetween(dtDal, dtAl).getWeeks();
            unitaDimisura = BigDecimal.valueOf(settimane);

        } else if(paramUnitaMisuraDurata.contains("mens")){
            Integer mesi = org.joda.time.Months.monthsBetween(dtDal, dtAl).getMonths();
            unitaDimisura = BigDecimal.valueOf(mesi);

        } else{
            throw new RuntimeException("param unita di misura non valorizzato: " + paramUnitaMisuraDurata);
        }


        BigDecimal costo = quantita.multiply(unitaDimisura);
        return costo;
    }

    private boolean intervento_prevede_durata(char flgFineDurata) {
        return flgFineDurata == TipologiaIntervento.FLG_FINE_DURATA_D;
    }

    private boolean intervento_prevede_data_fine(char flgFineDurata) {
        return flgFineDurata == TipologiaIntervento.FLG_FINE_DURATA_F;
    }
}
