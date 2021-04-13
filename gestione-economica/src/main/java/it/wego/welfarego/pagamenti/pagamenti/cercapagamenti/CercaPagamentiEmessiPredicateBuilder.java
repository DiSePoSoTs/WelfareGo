package it.wego.welfarego.pagamenti.pagamenti.cercapagamenti;


import com.google.common.collect.Sets;
import it.wego.welfarego.pagamenti.FiltraPerDelegatoJpaPredicateBuilder;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.persistence.criteria.*;
import java.util.*;

public class CercaPagamentiEmessiPredicateBuilder {

    private final Join joinPaiIntervento;
    private final Join joinPaiInterventoMeseList;
    private final Join joinDelegato;
    private final Join joinMandatoDettaglio;
    private List<Predicate> elencoFiltriNonNull = null;
    private CriteriaBuilder criteriaBuilder = null;
    private Root tblMandato = null;
    private Map<String, Object> parametri = null;
    private String mese_di_competenza_mese;
    private String mese_di_competenza_anno;
    private String periodo_considerato_dal_mese;
    private String periodo_considerato_al_mese;
    private String periodo_considerato_dal_anno;
    private String periodo_considerato_al_anno;

    public CercaPagamentiEmessiPredicateBuilder(CriteriaBuilder criteriaBuilder, Root tblMandato, String mese_di_competenza_mese, String mese_di_competenza_anno, String periodo_considerato_dal_mese, String periodo_considerato_al_mese, String periodo_considerato_dal_anno, String periodo_considerato_al_anno) {
        elencoFiltriNonNull = new ArrayList<Predicate>();
        this.criteriaBuilder = criteriaBuilder;
        this.tblMandato = tblMandato;
        parametri = new HashMap<String, Object>();
        this.mese_di_competenza_mese = blankToNull(mese_di_competenza_mese);
        this.mese_di_competenza_anno = blankToNull(mese_di_competenza_anno);
        this.periodo_considerato_dal_mese = blankToNull(periodo_considerato_dal_mese);
        this.periodo_considerato_al_mese = blankToNull(periodo_considerato_al_mese);
        this.periodo_considerato_dal_anno = blankToNull(periodo_considerato_dal_anno);
        this.periodo_considerato_al_anno = blankToNull(periodo_considerato_al_anno);

        this.joinMandatoDettaglio = tblMandato.join("mandatoDettaglioList");
        this.joinPaiIntervento = joinMandatoDettaglio.join("paiIntervento");
        this.joinPaiInterventoMeseList = joinPaiIntervento.join("paiInterventoMeseList");
        this.joinDelegato = joinPaiIntervento.join("dsCodAnaBenef", JoinType.LEFT);
    }

    private String blankToNull(String value) {
        if (null != value && value.trim().length() == 0) {
            value = null;
        }
        return value;
    }


    public void filtra_per_cod_tip_int(String codTipint) {
        if (StringUtils.isNotBlank(codTipint)) {
            Predicate predicate = criteriaBuilder.equal(joinPaiIntervento.get("paiInterventoPK").get("codTipint"), codTipint);
            elencoFiltriNonNull.add(predicate);
        }
    }

    public void filtra_per_periodo() {

        Path paiInterventomesePk = joinPaiInterventoMeseList.get("paiInterventoMesePK");

        if (mese_di_competenza_mese != null && mese_di_competenza_anno != null) {
            int year = Integer.parseInt(mese_di_competenza_anno);
            int month = Integer.parseInt(mese_di_competenza_mese);
            DateTime fromDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            DateTime toDate = fromDate.dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();

            Predicate interventoMeseEff = criteriaBuilder.equal(paiInterventomesePk.get("meseEff"), month);
            Predicate interventoAnnoEff = criteriaBuilder.equal(paiInterventomesePk.get("annoEff"), year);
            Predicate filtro_su_intervento_mese = criteriaBuilder.and(interventoMeseEff, interventoAnnoEff);


            Predicate periodoDal = criteriaBuilder.greaterThanOrEqualTo(tblMandato.get("periodoDal"), fromDate.toDate());
            Predicate periodoAl = criteriaBuilder.lessThanOrEqualTo(tblMandato.get("periodoAl"), toDate.toDate());
            Predicate filtro_su_mandato = criteriaBuilder.and(periodoDal, periodoAl);

            Predicate filtro_su_mandato_dettaglio = criteriaBuilder.equal(joinMandatoDettaglio.get("meseEff"), month);

            Predicate filtro_su_interventi_mese__or__mandato = criteriaBuilder.or(filtro_su_intervento_mese, filtro_su_mandato);
            Predicate predicate = criteriaBuilder.and(filtro_su_interventi_mese__or__mandato, filtro_su_mandato_dettaglio);

            elencoFiltriNonNull.add(predicate);

        } else {
            if (periodo_considerato_dal_mese != null && periodo_considerato_al_mese != null && periodo_considerato_dal_anno != null && periodo_considerato_al_anno != null) {
                int fromYear = Integer.parseInt(periodo_considerato_dal_anno);
                int toYear = Integer.parseInt(periodo_considerato_al_anno);
                int fromMonth = Integer.parseInt(periodo_considerato_dal_mese);
                int toMonth = Integer.parseInt(periodo_considerato_al_mese);
                DateTime fromDate = DateTime.now().withYear(fromYear).withMonthOfYear(fromMonth).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
                DateTime toDate = DateTime.now().withYear(toYear).withMonthOfYear(toMonth).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();

                Predicate interventoMese_AnnoBetween = criteriaBuilder.between(paiInterventomesePk.get("annoEff"), fromYear, toYear);
                Predicate interventoMese_MeseBetween = criteriaBuilder.between(paiInterventomesePk.get("meseEff"), fromMonth, toMonth);
                Predicate filtro_su_intervento_mese = criteriaBuilder.and(interventoMese_AnnoBetween, interventoMese_MeseBetween);

                Predicate periodoDal = criteriaBuilder.greaterThanOrEqualTo(tblMandato.get("periodoDal"), fromDate.toDate());
                Predicate periodoAl = criteriaBuilder.lessThanOrEqualTo(tblMandato.get("periodoAl"), toDate.toDate());
                Predicate filtro_su_mandato = criteriaBuilder.and(periodoDal, periodoAl);

                Predicate filtro_su_intervento_mese__or__mandato = criteriaBuilder.or(filtro_su_intervento_mese, filtro_su_mandato);

                Predicate filtro_su_mandato_dettaglio = criteriaBuilder.between(joinMandatoDettaglio.get("meseEff"), fromMonth, toMonth);

                Predicate predicate = criteriaBuilder.and(filtro_su_intervento_mese__or__mandato, filtro_su_mandato_dettaglio);
                elencoFiltriNonNull.add(predicate);
            }
        }
    }

    public void filtra_per_uot(String uot_struttura) {
        if (StringUtils.isNotBlank(uot_struttura)) {
            Predicate predicate = criteriaBuilder.equal(joinPaiIntervento.get("pai").get("idParamUot").get("idParamIndata"), Integer.parseInt(uot_struttura));
            elencoFiltriNonNull.add(predicate);
        }
    }

    public void filtra_per_cognome(String cognome) {
        if (StringUtils.isNotBlank(cognome)) {
            Predicate predicate = criteriaBuilder.like(joinPaiIntervento.get("pai").get("codAna").get("anagrafeSoc").get("cognome"), cognome + "%");
            elencoFiltriNonNull.add(predicate);
        }
    }

    public void filtra_per_nome(String nome) {
        if (StringUtils.isNotBlank(nome)) {
            Predicate predicate = criteriaBuilder.like(joinPaiIntervento.get("pai").get("codAna").get("anagrafeSoc").get("nome"), nome + "%");
            elencoFiltriNonNull.add(predicate);
        }
    }

    public void filtra_per_tip_param_uguale_a_fs() {
        Predicate predicate = criteriaBuilder.equal(tblMandato.get("idParamStato").get("idParam").get("tipParam").get("tipParam"), "fs");
        elencoFiltriNonNull.add(predicate);
    }

    public void filtra_per_stato_pagamenti(String stato_pagamenti) {
        if (StringUtils.isNotBlank(stato_pagamenti)) {
            Set<String> stati = Sets.newHashSet();

            if (stato_pagamenti.equals("da_emettere")) {
                stati.add("de");
            }
            if (stato_pagamenti.equals("emesse")) {
                stati.add("em");
            }
            if (stato_pagamenti.equals("da_inviare")) {
                stati.add("di");
            }
            if (stato_pagamenti.equals("inviate")) {
                stati.add("in");
            }
            if (stato_pagamenti.equals("pagate")) {
                stati.add("pa");
            }

            if (stato_pagamenti.equals("annullate")) {
                stati.add("an");
            }

            Predicate in = tblMandato.get("idParamStato").get("idParam").get("codParam").in(stati);
            elencoFiltriNonNull.add(in);
        }
    }

    public void filtra_per_delegato(String filterDelegato) {
        FiltraPerDelegatoJpaPredicateBuilder filtraPerDelegatoJpaPredicateBuilder = new FiltraPerDelegatoJpaPredicateBuilder(filterDelegato, criteriaBuilder, joinDelegato);
        filtraPerDelegatoJpaPredicateBuilder.invoke();
        Predicate predicate = filtraPerDelegatoJpaPredicateBuilder.getPredicate();
        parametri.putAll(filtraPerDelegatoJpaPredicateBuilder.getParametri());
        if (predicate != null) {
            elencoFiltriNonNull.add(predicate);
        }
    }

    public Predicate[] getFiltriDaApplicare() {
        Predicate[] filtriDaApplicare = new Predicate[elencoFiltriNonNull.size()];

        for (int i = 0; i < elencoFiltriNonNull.size(); i++) {
            filtriDaApplicare[i] = elencoFiltriNonNull.get(i);
        }

        return filtriDaApplicare;
    }

    public Map<String, Object> getParameters() {
        return parametri;
    }
}
