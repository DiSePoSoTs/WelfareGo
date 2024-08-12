package it.wego.welfarego.azione.stores.transformer;

import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDaoCached;
import it.wego.welfarego.persistence.entities.*;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class ImpegniJsonMapTransformer extends JsonMapTransformer<BudgetTipIntervento> {

    private final Logger logger = LogManager.getLogger(ImpegniJsonMapTransformer.class);


    private EntityManager entityManager = null;
    private PaiIntervento paiIntervento = null;
    private String codTipInt = null;
    private PaiInterventoDao pdao = null;
    private PaiInterventoPK paiInterventoPK = null;

    private int id = 1;

    public ImpegniJsonMapTransformer(EntityManager entityManager, PaiIntervento paiIntervento, String codTipInt) {
        this.entityManager = entityManager;
        this.paiIntervento = paiIntervento;
        this.codTipInt = codTipInt;
        pdao = new PaiInterventoDao(entityManager);
        paiInterventoPK = paiIntervento.getPaiInterventoPK();
    }

    @Override
    public void transformToMap(it.wego.welfarego.persistence.entities.BudgetTipIntervento budgetTipIntervento) {


        PaiInterventoMeseDaoCached paiInterventoMeseDao = new PaiInterventoMeseDaoCached(budgetTipIntervento, entityManager);

        BudgetTipInterventoPK budgetTipInterventoPK = budgetTipIntervento.getBudgetTipInterventoPK();


        put("anno", Short.toString(budgetTipInterventoPK.getCodAnno()));
        put("capitolo", Integer.toString(budgetTipIntervento.getCodCap()));
        BigDecimal dispNetto = paiInterventoMeseDao.calculateRealBudgetDisp();

        put("imp_disp_netto", dispNetto.toString());
        put("imp_disp_proroghe", dispNetto.subtract(pdao.sumCostoProrghe(budgetTipIntervento.getBudgetTipInterventoPK().getCodImpe(), paiIntervento.getPaiInterventoPK().getCodTipint(), null)).toString());

        put("imp_disp", paiInterventoMeseDao.calculateBdgtDisp().toString());

        put("bdgDispQta", paiInterventoMeseDao.calculateBdgtDispQta().toString());

        put("bdgDispQtaCons", paiInterventoMeseDao.calculateBdgtDispQtaCons().toString());

        put("impegno", budgetTipInterventoPK.getCodImpe());
        if (codTipInt.equals(paiInterventoPK.getCodTipint())) {
            PaiInterventoMese propostaSpesa = paiInterventoMeseDao.findProposta(paiInterventoPK, budgetTipInterventoPK, paiIntervento.getPai().getIdParamFascia());
            put("unitaDiMisuraDesc", paiIntervento.getIdParamUniMis().getDesParam());
            if (propostaSpesa != null) {
                put("a_carico", propostaSpesa.getBdgPrevEur().toString());
                put("bdgPrevQta", propostaSpesa.getBdgPrevQta().toString());
            } else {
                put("a_carico", "0");
                put("bdgPrevQta", "0");
            }
        } else {
            put("a_carico", "0");
            put("bdgPrevQta", "0");
        }

        put("id", "" + (id++));
    }

}
