package it.wego.welfarego.azione.stores;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import it.wego.welfarego.abstracts.AbstractStoreListener;
import it.wego.extjs.beans.Order;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.welfarego.persistence.dao.BudgetTipoInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aleph
 */
public class InterventiStoreListener extends AbstractStoreListener {

    public static final String PARAM_SOLO_INTERVENTI_ATTIVI = "activeOnly",
            PARAM_BUDGET_PER_UOT = "uotbudget",
            PARAM_NO_STATO_DETERMINE = "noStatoDetermine",
            PARAM_NO_TASK_ATTIVI = "noTaskAttivi";

    public InterventiStoreListener(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public Collection load(int start, int limit, int page, List<Order> order) throws Exception {
        Iterable<PaiIntervento> paiInterventoList = getTask().getCodPai().getPaiInterventoList();
        if (Objects.equal(getParameter(PARAM_SOLO_INTERVENTI_ATTIVI), Boolean.TRUE.toString())) {
            paiInterventoList = Iterables.filter(paiInterventoList, new Predicate<PaiIntervento>() {
                public boolean apply(PaiIntervento input) {
                    return input.getStatoInt() == PaiIntervento.STATO_INTERVENTO_APERTO;
                }
            });
        }
        if (Objects.equal(getParameter(PARAM_NO_TASK_ATTIVI), Boolean.TRUE.toString())) {
            paiInterventoList = Iterables.filter(paiInterventoList, new Predicate<PaiIntervento>() {
                public boolean apply(PaiIntervento paiIntervento) {
                    return !paiIntervento.hasActiveTask();
                }
            });
        }
        if (Objects.equal(getParameter(PARAM_NO_STATO_DETERMINE), Boolean.TRUE.toString())) {
            paiInterventoList = Iterables.filter(paiInterventoList, new Predicate<PaiIntervento>() {
                public boolean apply(PaiIntervento paiIntervento) {
                    return !Iterables.any(paiIntervento.getPaiEventoList(), new Predicate<PaiEvento>() {
                        public boolean apply(PaiEvento paiEvento) {
                            return Objects.equal(PaiEvento.FLG_STAMPA_SI, paiEvento.getFlgDxStampa());
                        }
                    });
                }
            });
        }
        final boolean getBudgetPerUot = Boolean.parseBoolean(getParameter(PARAM_BUDGET_PER_UOT));
        return JsonBuilder.newInstance().withData(paiInterventoList).withParameters(getParameters()).withTransformer(new JsonMapTransformer<PaiIntervento>() {
            int id = 0;
            final BudgetTipoInterventoDao budgetTipoInterventoDao = new BudgetTipoInterventoDao(getEntityManager());
            final PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(getEntityManager());

            @Override
            public void transformToMap(PaiIntervento paiIntervento) {
                put("id", "" + (id++));
                put("intervento", paiIntervento.getPaiInterventoPK().getCodTipint() + " - " + paiIntervento.getTipologiaIntervento().getDesTipint());
                put("data_apertura", paiIntervento.getDtApe());
                put("budget_disp", decimalNumberFormat.format((getBudgetPerUot ? budgetTipoInterventoDao.getBdgDispUot(paiIntervento) : budgetTipoInterventoDao.getBdgDisp(paiIntervento.getTipologiaIntervento().getCodTipint()))));
                put("costo_prev", decimalNumberFormat.format(paiInterventoMeseDao.findSumProp(paiIntervento.getPaiInterventoPK())));
                put("pai_intervento_pk", paiIntervento.getPaiInterventoPK());
                put("motivazione", Strings.nullToEmpty(paiIntervento.getMotivazione()));
            }
        }).buildStoreResponse().getData();
    }

    @Override
    public Object update(String data) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
