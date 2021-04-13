package it.wego.welfarego.pagamenti.pagamenti.cercapagamenti;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.pagamenti.DelegatoParamNormalizer;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import java.util.List;
import java.util.Set;

import static it.wego.persistence.ConditionBuilder.*;
import static it.wego.persistence.ConditionBuilder.between;

public class CercaPagamentiEmessiConditionBuilder {


    private List<Condition> conditions = null;

    public CercaPagamentiEmessiConditionBuilder(){
        conditions = Lists.newArrayList();
    }

    public void filtra_per_stato_pagamenti(String stato_pagamenti) {
        Set<String> stati = Sets.newHashSet();


        if (stato_pagamenti.equals("da_emettere")) {
            stati.add("de");
        }
        if (stato_pagamenti.equals("emesse") ) {
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

        Condition condition = ConditionBuilder.isIn("m.idParamStato.idParam.codParam", stati);
        conditions.add(condition);

}
    public void filtra_per_tip_param_uguale_a_fs() {
        conditions.add(ConditionBuilder.equals("m.idParamStato.idParam.tipParam.tipParam", "fs"));
    }

    public void filtra_per_nome(String nome) {
        if(nome!=null){
            nome=nome.toUpperCase();
            conditions.add(and(beginsWith("m.paiIntervento.pai.codAna.anagrafeSoc.nome", nome)));
        }
    }

    public void filtra_per_cognome(String cognome) {
        if(cognome!=null){
            cognome=cognome.toUpperCase();
            Condition condition = beginsWith("m.paiIntervento.pai.codAna.anagrafeSoc.cognome", cognome);
            conditions.add(and(condition));
        }
    }

    public void filtra_per_uot(String uot_struttura) {
        if (uot_struttura != null) {
            try {
                conditions.add(and(isEqual("m.paiIntervento.pai.idParamUot.idParamIndata",Integer.parseInt(uot_struttura))));
            }
            catch (NumberFormatException ex) {
                //L'id dell'uot non è un numerico, eseguo la ricerca ignorando il parametro errato.
            }
        }
    }

    public void filtra_per_periodo(String mese_di_competenza_mese, String mese_di_competenza_anno, String periodo_considerato_dal_mese, String periodo_considerato_al_mese, String periodo_considerato_dal_anno, String periodo_considerato_al_anno) {
        if (mese_di_competenza_mese != null && mese_di_competenza_anno != null) {
            int year = Integer.parseInt(mese_di_competenza_anno),
                    month = Integer.parseInt(mese_di_competenza_mese);
            DateTime fromDate = DateTime.now().withYear(year).withMonthOfYear(month).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
            DateTime toDate = fromDate.dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();
            Condition interventoMese = isEqual("pim.paiInterventoMesePK.meseEff", month);
            Condition interventoAnno = isEqual("pim.paiInterventoMesePK.annoEff", year);
            Condition periodoDal = before("m.periodoDal", toDate.toDate());
            Condition periodoAl = after("m.periodoAl", fromDate.toDate());
            conditions.add(or(
                    and(interventoMese,interventoAnno),
                    and(periodoDal,periodoAl))
            );
        } else {
            if (periodo_considerato_dal_mese != null && periodo_considerato_al_mese != null && periodo_considerato_dal_anno != null && periodo_considerato_al_anno != null) {
                int fromYear = Integer.parseInt(periodo_considerato_dal_anno),
                        toYear = Integer.parseInt(periodo_considerato_al_anno),
                        fromMonth = Integer.parseInt(periodo_considerato_dal_mese),
                        toMonth = Integer.parseInt(periodo_considerato_al_mese);
                DateTime fromDate = DateTime.now().withYear(fromYear).withMonthOfYear(fromMonth).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue();
                DateTime toDate = DateTime.now().withYear(toYear).withMonthOfYear(toMonth).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue();


                Condition periodoDal                    = before("m.periodoDal", toDate.toDate());
                Condition periodoAl                     = after("m.periodoAl", fromDate.toDate());
                Condition interventoMese_AnnoBetween = between("pim.paiInterventoMesePK.annoEff", fromYear, toYear);
                Condition interventoMese_MeseBetween = between("pim.paiInterventoMesePK.meseEff", fromMonth, toMonth);
                conditions.add(
                        or(
                            and(periodoDal,periodoAl),
                            and(interventoMese_AnnoBetween,interventoMese_MeseBetween)
                        )
                );
            }
        }
    }

    public void filtra_per_cod_tip_int(String codTipint) {
        if (codTipint != null) {
            Condition condition = ConditionBuilder.equals("m.paiIntervento.paiInterventoPK.codTipint", codTipint);
            conditions.add(condition);
        }
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void filtra_per_delegato(String filterDelegato) {
        if(StringUtils.isNotBlank(filterDelegato)) {
            filterDelegato = DelegatoParamNormalizer.normalize(filterDelegato);
            String[] paroleDelFiltro = filterDelegato.split(" ");

            switch (paroleDelFiltro.length){
                case 1:
                    Condition conditionOnRagSoc = ConditionBuilder.like("m.paiIntervento.dsCodAnaBenef.ragSoc", filterDelegato);
                    Condition conditionOnCognome = ConditionBuilder.like("m.paiIntervento.dsCodAnaBenef.cognome", filterDelegato);
                    Condition conditionOnNome = ConditionBuilder.like("m.paiIntervento.dsCodAnaBenef.nome", filterDelegato);
                    Condition condition = ConditionBuilder.or(conditionOnRagSoc, conditionOnCognome, conditionOnNome);
                    conditions.add(condition);
                    break;
                default:
                    Condition conditionOnRagSoc1 = ConditionBuilder.like("m.paiIntervento.dsCodAnaBenef.ragSoc", paroleDelFiltro[0]);
                    Condition conditionOnRagSoc2 = ConditionBuilder.like("m.paiIntervento.dsCodAnaBenef.ragSoc", paroleDelFiltro[1]);
                    conditionOnRagSoc = ConditionBuilder.and(conditionOnRagSoc1, conditionOnRagSoc2);

                    Condition conditionOnCognome1 = ConditionBuilder.like("m.paiIntervento.dsCodAnaBenef.cognome", paroleDelFiltro[0]);
                    Condition conditionOnNome1 = ConditionBuilder.like("m.paiIntervento.dsCodAnaBenef.nome", paroleDelFiltro[1]);

                    Condition conditionOnCognome2 = ConditionBuilder.like("m.paiIntervento.dsCodAnaBenef.cognome", paroleDelFiltro[1]);
                    Condition conditionOnNome2 = ConditionBuilder.like("m.paiIntervento.dsCodAnaBenef.nome", paroleDelFiltro[0]);


                    Condition conditionOnCognomeNome = ConditionBuilder.and(conditionOnCognome1, conditionOnNome1);
                    Condition conditionOnNomeCognome = ConditionBuilder.and(conditionOnCognome2, conditionOnNome2);;
                    condition = ConditionBuilder.or(conditionOnRagSoc, conditionOnCognomeNome, conditionOnNomeCognome);
                    conditions.add(condition);
            }

        }
    }
}
