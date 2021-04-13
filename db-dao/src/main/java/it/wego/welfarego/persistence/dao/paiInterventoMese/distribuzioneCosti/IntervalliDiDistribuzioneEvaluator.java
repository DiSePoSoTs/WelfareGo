package it.wego.welfarego.persistence.dao.paiInterventoMese.distribuzioneCosti;


import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Calendar;

public class IntervalliDiDistribuzioneEvaluator {
    private PaiIntervento paiIntervento;
    private boolean ignoraArmonizzazione;
    private boolean useDurataMesi;
    private BudgetTipIntervento budgetTipIntervento;
    private List<Integer> elencoIntervalliPerSuddivisione;
    private int annoEffettiviPagamenti;
    private int meseInizioEffettiviPagamenti;


    public IntervalliDiDistribuzioneEvaluator(PaiIntervento paiIntervento, boolean ignoraArmonizzazione, boolean useDurataMesi, BudgetTipIntervento budgetTipIntervento, List<Integer> distribuzioneCostiLocal
    ) {
        this.paiIntervento = paiIntervento;
        this.ignoraArmonizzazione = ignoraArmonizzazione;
        this.useDurataMesi = useDurataMesi;
        this.budgetTipIntervento = budgetTipIntervento;
        this.elencoIntervalliPerSuddivisione = distribuzioneCostiLocal;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(paiIntervento.getDtAvvio());
        this.annoEffettiviPagamenti = calendar.get(Calendar.YEAR);
        this.meseInizioEffettiviPagamenti = calendar.get(Calendar.MONTH) + 1;
    }


    public List<Integer> getElencoIntervalliPerSuddivisione() {
        return elencoIntervalliPerSuddivisione;
    }

    public int getAnnoEffettiviPagamenti() {
        return annoEffettiviPagamenti;
    }

    public int getMeseEffettivoPagamento() {
        return meseInizioEffettiviPagamenti;
    }

    public IntervalliDiDistribuzioneEvaluator crea_intervalli_per_distribuzione_costi() throws IllegalArgumentException {
        if (useDurataMesi || paiIntervento.getDtFine() == null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            Short annoErogazioneBudget = budgetTipIntervento.getAnnoErogazione();

            if ((annoErogazioneBudget >= annoEffettiviPagamenti) && ignoraArmonizzazione == false) {
                if (annoErogazioneBudget == annoEffettiviPagamenti) {
                    anni_concordi(formatter);
                } else {
                    anno_budget_maggiore_partenza(formatter, annoErogazioneBudget);
                }
            } else {
                Integer durMesi = paiIntervento.getDurMesi();
                if (durMesi == null){
                    durMesi = Months.monthsBetween(new DateTime(paiIntervento.getDtAvvio()), new DateTime(paiIntervento.calculateDtFine())).getMonths();
                }

                elencoIntervalliPerSuddivisione = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), durMesi));
            }
        }
        return this;
    }

    private void anno_budget_maggiore_partenza(DateTimeFormatter formatter, Short annoSpesaBgd) throws IllegalArgumentException {
        //solo in questo caso metto l'anno di partenza e il mese di partenza uguali a gennaio dell'anno del budget

        int annoInizioIntervento_orig = this.annoEffettiviPagamenti;
        int meseInizioIntervento_orig = this.meseInizioEffettiviPagamenti;


        Integer durataIntervento = paiIntervento.getDurMesi();
        DateTime dataNormalizzataInizioIntervento = formatter.parseDateTime("01/" + meseInizioEffettiviPagamenti + "/" + annoEffettiviPagamenti);
        DateTime dataNormalizzataFineIntervento = dataNormalizzataInizioIntervento.plusMonths(durataIntervento);
        DateTime dataInizioErogazioneBudget = formatter.parseDateTime("01/01/" + annoSpesaBgd);

        int mesi = Months.monthsBetween(dataInizioErogazioneBudget, dataNormalizzataFineIntervento).getMonths();

        DateTime dataFineIntervento = new DateTime(paiIntervento.calculateDtFine());
        boolean giorniDiInterventoResidui = Days.daysBetween(dataInizioErogazioneBudget, dataFineIntervento).isGreaterThan(Days.ZERO);

        meseInizioEffettiviPagamenti = 1;
        annoEffettiviPagamenti = annoSpesaBgd;


        if (mesi > 12) {
            elencoIntervalliPerSuddivisione = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), 12));
        } else if (mesi > 0 && mesi <= 12) {
            elencoIntervalliPerSuddivisione = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), mesi));
        } else if (mesi == 0 && giorniDiInterventoResidui) {
            elencoIntervalliPerSuddivisione = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), 1));
        } else if (mesi < 0) {
            Date data_inizio_intervento = this.paiIntervento.getDtAvvio();
            Date data_fine_intervento = this.paiIntervento.calculateDtFine();
            LocalDate data_inizio = new LocalDate(data_inizio_intervento);
            LocalDate data_fine = new LocalDate(data_fine_intervento);

            Months months = Months.monthsBetween(data_inizio, data_fine);
            mesi = months.getMonths();
            elencoIntervalliPerSuddivisione = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), mesi));
            annoEffettiviPagamenti = annoInizioIntervento_orig;
            meseInizioEffettiviPagamenti = meseInizioIntervento_orig;
        } else {
            String msgTemplate = "valore non gestito per la differenza in mesi:%s, tra le date (dataInizioErogazioneBudget,dataNormalizzataFineIntervento):(%tF, %tF), intervento:%s";
            PaiInterventoPK paiInterventoPK = paiIntervento.getPaiInterventoPK();
            Date date = dataInizioErogazioneBudget.toDate();
            Date date1 = dataNormalizzataFineIntervento.toDate();
            throw new IllegalArgumentException(String.format(msgTemplate, mesi, date, date1, paiInterventoPK));
        }


    }

    private void anni_concordi(DateTimeFormatter formatter) {
        //mi calcolo i mesi che intercorrono frà l'avvio dell'intervento e la fine dell'anno

        DateTime dt = formatter.parseDateTime("31/12/" + annoEffettiviPagamenti).plusDays(1);
        DateTime dtStart = formatter.parseDateTime("01/" + meseInizioEffettiviPagamenti + "/" + annoEffettiviPagamenti);
        int mesi = Months.monthsBetween(dtStart, dt).getMonths();
        //se i mesi sono uguali o minori alla durata intervento uso quelli
        if (mesi <= paiIntervento.getDurMesi()) {
            elencoIntervalliPerSuddivisione = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), mesi));
        } else {
            elencoIntervalliPerSuddivisione = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), paiIntervento.getDurMesi()));
        }
    }

}
