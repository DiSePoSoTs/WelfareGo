package it.wego.welfarego.persistence.dao;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.persistence.ConditionBuilder;

import static it.wego.persistence.ConditionBuilder.*;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.dao.paiInterventoMese.distribuzioneCosti.IntervalliDiDistribuzioneEvaluator;
import it.wego.welfarego.persistence.entities.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author giuseppe
 */
public class PaiInterventoMeseDao extends PersistenceAdapter implements ORMDao {

    private static Logger logger = LoggerFactory.getLogger(PaiInterventoMeseDao.class);

    public PaiInterventoMeseDao(EntityManager em) {
        super(em);
    }

    /**
     * @param intervento
     * @param anno
     * @param codImp
     * @return
     * @deprecated use sumBdgtConsPaiIntervento(PaiIntervento intervento, String
     * codImp, int anno)
     */
    @Deprecated
    public BigDecimal sumBdgtConsPaiIntervento(PaiIntervento intervento, int anno, String codImp) {
        return sumBdgtConsPaiIntervento(intervento, codImp, anno);
    }

    public BigDecimal sumBdgtConsPaiIntervento(PaiIntervento intervento) {
        return sumBdgtConsPaiIntervento(intervento, null, null);
    }

    public BigDecimal sumBdgtConsPaiIntervento(PaiIntervento intervento, @Nullable String codImp, @Nullable Integer anno) {
        List conditions = Lists.newArrayList(isEqual("p.paiIntervento", intervento));
        if (!Strings.isNullOrEmpty(codImp)) {
            conditions.add(isEqual("p.paiInterventoMesePK.codImp", codImp));
        }
        if (anno != null) {
            conditions.add(isEqual("p.paiInterventoMesePK.anno", anno));
        }
        return (BigDecimal) MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgConsEur) FROM PaiInterventoMese p", conditions), BigDecimal.ZERO); //TODO è giusto cosi ? 
    }

    public BigDecimal sumBdgtPrevQtaPaiIntervento(PaiIntervento paiIntervento, String codImp, Integer anno) {

        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevQta) FROM PaiInterventoMese p",
                ConditionBuilder.equals("p.paiIntervento", paiIntervento),
                ConditionBuilder.equals("p.paiInterventoMesePK.codImp", codImp),
                ConditionBuilder.equals("p.paiInterventoMesePK.anno", anno),
                ConditionBuilder.isNull("p.bdgConsQta")), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgtConsQtaPaiIntervento(PaiIntervento paiIntervento, String codImp, Integer anno) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgConsQta) FROM PaiInterventoMese p",
                ConditionBuilder.equals("p.paiIntervento", paiIntervento),
                ConditionBuilder.equals("p.paiInterventoMesePK.codImp", codImp),
                ConditionBuilder.equals("p.paiInterventoMesePK.anno", anno)), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgtPrevAndConsQtaPaiIntervento(PaiIntervento paiIntervento, String codImp, Integer anno) {
        return sumBdgtPrevQtaPaiIntervento(paiIntervento, codImp, anno).add(sumBdgtConsQtaPaiIntervento(paiIntervento, codImp, anno));
    }

    public BigDecimal sumBdgtPrevQta(BudgetTipIntervento budgetTipIntervento) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevQta) FROM PaiInterventoMese p",
                ConditionBuilder.equals("p.budgetTipIntervento", budgetTipIntervento),
                ConditionBuilder.isNull("p.bdgConsQta")), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgtConsQta(BudgetTipIntervento budgetTipIntervento) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgConsQta) FROM PaiInterventoMese p",
                ConditionBuilder.equals("p.budgetTipIntervento", budgetTipIntervento)), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgtPrevAndConsQta(BudgetTipIntervento budgetTipIntervento) {
        return sumBdgtPrevQta(budgetTipIntervento).add(sumBdgtConsQta(budgetTipIntervento));
    }

    public BigDecimal calculateBdgtDispQta(BudgetTipIntervento budgetTipIntervento) {
        return budgetTipIntervento.getBdgDispOre().subtract(sumBdgtPrevAndConsQta(budgetTipIntervento));
    }

    public BigDecimal calculateBdgtDispQtaCons(BudgetTipIntervento budgetTipIntervento) {
        return budgetTipIntervento.getBdgDispOre().subtract(sumBdgtConsQta(budgetTipIntervento));
    }

    public BigDecimal calculateBdgtDispQtaConsUot(BudgetTipInterventoUot budgetTipInterventoUot) {
        return budgetTipInterventoUot.getBdgDispOre().subtract(sumBdgtConsQta(budgetTipInterventoUot));
    }




    public BigDecimal sumBdgtPrev(PaiIntervento paiIntervento, BudgetTipInterventoUot budgetTipInterventoUot) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevEur) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.paiIntervento", paiIntervento),
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipInterventoUot.getBudgetTipIntervento())), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgtPrevPaiIntervento(PaiIntervento intervento, String codImp, int anno) {
        TypedQuery<BigDecimal> query = getEntityManager().createQuery("SELECT SUM(p.bdgPrevEur) "
                + "FROM PaiInterventoMese p "
                + "WHERE p.paiInterventoMesePK.codImp = :codImp "
                + "AND p.paiInterventoMesePK.anno = :anno "
                + "AND p.paiInterventoMesePK.codPai = :codPai "
                + "AND p.paiInterventoMesePK.codTipint = :codTipint "
                + "AND p.paiInterventoMesePK.cntTipint = :cntTipint "
                + "AND p.bdgConsEur IS NULL ", BigDecimal.class);
        query.setParameter("codImp", codImp);
        query.setParameter("anno", anno);
        query.setParameter("codPai", intervento.getPaiInterventoPK().getCodPai());
        query.setParameter("codTipint", intervento.getPaiInterventoPK().getCodTipint());
        query.setParameter("cntTipint", intervento.getPaiInterventoPK().getCntTipint());
        return MoreObjects.firstNonNull(getSingleResult(query), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgtPrevAndConsPaiIntervento(PaiIntervento intervento, String codImp, int anno) {
        BigDecimal sumBdgtPrev = sumBdgtPrevPaiIntervento(intervento, codImp, anno);
        BigDecimal sumBdgCons = sumBdgtConsPaiIntervento(intervento, codImp, anno);
        BigDecimal res = sumBdgtPrev.add(sumBdgCons);
        logger.debug("sumBdgtPrev = {} , sumBdgCons = {} , sumBdgtPrevAndCons = {}", new Object[]{sumBdgtPrev, sumBdgCons, res});
        return res;
    }

    public BigDecimal sumBdgtPrev(BudgetTipInterventoUot budgetTipInterventoUot) {

        BigDecimal result = MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevEur) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.paiIntervento.pai.idParamUot", budgetTipInterventoUot.getParametriIndataUot()),
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipInterventoUot.getBudgetTipIntervento()),
                ConditionBuilder.isNull("p.bdgConsEur")), BigDecimal.ZERO);

        return result;
    }

    public BigDecimal sumBdgtPrevSoloApprovati(BudgetTipInterventoUot budgetTipInterventoUot) {

        PaiEventoDao edao = new PaiEventoDao(getEntityManager());

        List<PaiEvento> daDeterminare = edao.findDetermine(budgetTipInterventoUot.getBudgetTipIntervento().getBudgetTipInterventoPK().getCodTipint(), null, 'A');
        //mi prendo tutti gli interventi che sono in liste per quella tipologia
        BigDecimal result = BigDecimal.ZERO;
        List<PaiIntervento> interventi = new ArrayList<PaiIntervento>();

        for (PaiEvento pe : daDeterminare) {
            // mi prendo solo gli interventi della UOT che mi interessa e mi assicuro che il busdget venga preso solo una volta
            if (pe.getCodPai().getIdParamUot().getIdParamIndata().equals(budgetTipInterventoUot.getParametriIndataUot().getIdParamIndata()) && !interventi.contains(pe)) {
                logger.debug("Pippo pluto e paperino il result è " + result);
                BigDecimal pippo = result.add(sumBdgtPrev(pe.getPaiIntervento(), budgetTipInterventoUot));
                result = pippo;
                logger.debug("Sono oltre l'istruzione");
                interventi.add(pe.getPaiIntervento());

            }
        }

        return result;
    }

    //Mi trova il budget previsto dei PIM non ancora acquisiti.
    public BigDecimal sumBdgtPrevDaAcquisire(BudgetTipInterventoUot budgetTipInterventoUot) {

        BigDecimal result = MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevEur) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.paiIntervento.pai.idParamUot", budgetTipInterventoUot.getParametriIndataUot()),
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipInterventoUot.getBudgetTipIntervento()),
                ConditionBuilder.isNull("p.bdgConsEur"),
                ConditionBuilder.isEqual("p.generato", 1),
                ConditionBuilder.isEqual("p.flgProp", 'N')),
                BigDecimal.ZERO);

        return result;
    }

    //Mi trova il budget previsto dei PIM non ancora acquisiti.
    public BigDecimal sumBdgtPrevDaAcquisire(BudgetTipIntervento budgetTipIntervento) {

        BigDecimal result = MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevEur) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipIntervento),
                ConditionBuilder.isNull("p.bdgConsEur"),
                ConditionBuilder.isEqual("p.generato", 1),
                ConditionBuilder.isEqual("p.flgProp", 'N')),
                BigDecimal.ZERO);

        return result;
    }

    //Mi trova il budget previsto dei PIM non ancora determinati
    public BigDecimal sumBdgtPrevDaDeterminare(BudgetTipInterventoUot budgetTipInterventoUot) {

        BigDecimal result = MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevEur) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.paiIntervento.pai.idParamUot", budgetTipInterventoUot.getParametriIndataUot()),
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipInterventoUot.getBudgetTipIntervento()),
                ConditionBuilder.isNull("p.bdgConsEur"),
                ConditionBuilder.isEqual("p.generato", 1),
                ConditionBuilder.isEqual("p.flgProp", 'S'),
                ConditionBuilder.isEqual("p.paiIntervento.statoAttuale", PaiIntervento.DETERMINA)),
                BigDecimal.ZERO);

        return result;
    }

    public BigDecimal sumBdgtPrevDaDeterminare(BudgetTipIntervento budgetTipIntervento) {

        BigDecimal result = MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevEur) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipIntervento),
                ConditionBuilder.isNull("p.bdgConsEur"),
                ConditionBuilder.isEqual("p.generato", 1),
                ConditionBuilder.isEqual("p.flgProp", 'S'),
                ConditionBuilder.isEqual("p.paiIntervento.statoAttuale", PaiIntervento.DETERMINA)),
                BigDecimal.ZERO);

        return result;
    }

    public BigDecimal sumBdgtCons(BudgetTipInterventoUot budgetTipInterventoUot) {
        BigDecimal result = MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgConsEur) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.paiIntervento.pai.idParamUot", budgetTipInterventoUot.getParametriIndataUot()),
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipInterventoUot.getBudgetTipIntervento())), BigDecimal.ZERO);

        return result;
    }

    //ritorna il budget proposte
    public BigDecimal sumBdgtProposte(BudgetTipInterventoUot budgetTipInterventoUot) {
        BigDecimal result = MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevEur) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.paiIntervento.pai.idParamUot", budgetTipInterventoUot.getParametriIndataUot()),
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipInterventoUot.getBudgetTipIntervento()),
                ConditionBuilder.isNull("p.bdgConsEur"),
                ConditionBuilder.isEqual("p.flgProp", 'S')),
                BigDecimal.ZERO);

        return result;
    }

    public BigDecimal sumBdgtPrevAndCons(BudgetTipInterventoUot budgetTipInterventoUot) {
        return sumBdgtPrev(budgetTipInterventoUot).add(sumBdgtCons(budgetTipInterventoUot));
    }

    public BigDecimal sumBdgtPrev(BudgetTipIntervento budgetTipIntervento) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevEur) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipIntervento),
                ConditionBuilder.isNull("p.bdgConsEur")), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgtCons(BudgetTipIntervento budgetTipIntervento) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgConsEur) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipIntervento)), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgtPrevAndCons(BudgetTipIntervento budgetTipIntervento) {
        return sumBdgtPrev(budgetTipIntervento).add(sumBdgtCons(budgetTipIntervento));
    }

    public BigDecimal sumBdgEsentPrev(BudgetTipIntervento budgetTipIntervento) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevEur*(100-p.paiIntervento.pai.idParamFascia.decimalParam)/100) FROM PaiInterventoMese p", ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipIntervento),
                ConditionBuilder.isNull("p.bdgConsEur"),
                ConditionBuilder.isNotNull("p.paiIntervento.pai.idParamFascia"),
                ConditionBuilder.isIn("p.paiInterventoMesePK.codTipint", PaiIntervento.codTipintFasciaRedd)), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgEsentCons(BudgetTipIntervento budgetTipIntervento) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgConsEur*(100-p.paiIntervento.pai.idParamFascia.decimalParam)/100) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipIntervento),
                ConditionBuilder.isNotNull("p.paiIntervento.pai.idParamFascia"),
                ConditionBuilder.isIn("p.paiInterventoMesePK.codTipint", PaiIntervento.codTipintFasciaRedd)), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgEsentPrev(BudgetTipInterventoUot budgetTipInterventoUot) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevEur*(100-p.paiIntervento.pai.idParamFascia.decimalParam)/100) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipInterventoUot.getBudgetTipIntervento()),
                ConditionBuilder.isEqual("p.paiIntervento.pai.idParamUot", budgetTipInterventoUot.getParametriIndataUot()),
                ConditionBuilder.isNull("p.bdgConsEur"),
                ConditionBuilder.isNotNull("p.paiIntervento.pai.idParamFascia"),
                ConditionBuilder.isIn("p.paiInterventoMesePK.codTipint", PaiIntervento.codTipintFasciaRedd)), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgEsentCons(BudgetTipInterventoUot budgetTipInterventoUot) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgConsEur*(100-p.paiIntervento.pai.idParamFascia.decimalParam)/100) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipInterventoUot.getBudgetTipIntervento()),
                ConditionBuilder.isEqual("p.paiIntervento.pai.idParamUot", budgetTipInterventoUot.getParametriIndataUot()),
                ConditionBuilder.isNotNull("p.paiIntervento.pai.idParamFascia"),
                ConditionBuilder.isIn("p.paiInterventoMesePK.codTipint", PaiIntervento.codTipintFasciaRedd)), BigDecimal.ZERO);
    }

    public BigDecimal calculateBdgtDisp(BudgetTipInterventoUot budgetTipInterventoUot) {
        return budgetTipInterventoUot.getBdgDispEur()
                .subtract(sumBdgtPrev(budgetTipInterventoUot))
                .subtract(sumBdgtCons(budgetTipInterventoUot))
                .add(sumBdgEsentPrev(budgetTipInterventoUot))
                .add(sumBdgEsentCons(budgetTipInterventoUot));
    }

    public BigDecimal calculateBdgtDisp(BudgetTipIntervento budgetTipIntervento) {
        return budgetTipIntervento.getBdgDispEur()
                .subtract(sumBdgtPrev(budgetTipIntervento))
                .subtract(sumBdgtCons(budgetTipIntervento))
                .add(sumBdgEsentPrev(budgetTipIntervento))
                .add(sumBdgEsentCons(budgetTipIntervento));
    }

    public BigDecimal calculateRealBudgetDisp(BudgetTipIntervento budgetTipIntervento) {
        return budgetTipIntervento.getBdgDispEur()
                .subtract(sumBdgtCons(budgetTipIntervento))
                .add(sumBdgEsentCons(budgetTipIntervento)).subtract(sumBdgtPrevDaDeterminare(budgetTipIntervento)).subtract(sumBdgtPrevDaAcquisire(budgetTipIntervento));
    }

    public BigDecimal calculateRealBdgtDisp(BudgetTipInterventoUot budgetTipInterventoUot) {
        return budgetTipInterventoUot.getBdgDispEur()
                .subtract(sumBdgtCons(budgetTipInterventoUot))
                .add(sumBdgEsentCons(budgetTipInterventoUot)).subtract(sumBdgtPrevDaDeterminare(budgetTipInterventoUot)).subtract(sumBdgtPrevDaAcquisire(budgetTipInterventoUot));
    }

    public BigDecimal calculateBudgetPrenotato(BudgetTipInterventoUot budgetTipInterventoUot) {


        return sumBdgtPrev(budgetTipInterventoUot)
                .subtract(sumBdgEsentPrev(budgetTipInterventoUot)).subtract(sumBdgtPrevDaDeterminare(budgetTipInterventoUot)).subtract(sumBdgtPrevDaAcquisire(budgetTipInterventoUot));

    }

    public BigDecimal calculateBudgetPrenotato(BudgetTipIntervento budgetTipIntervento) {


        return sumBdgtPrev(budgetTipIntervento)
                .subtract(sumBdgEsentPrev(budgetTipIntervento)).subtract(sumBdgtPrevDaDeterminare(budgetTipIntervento)).subtract(sumBdgtPrevDaAcquisire(budgetTipIntervento));

    }

    public BigDecimal sumBdgtPrevQta(BudgetTipInterventoUot budgetTipInterventoUot) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgPrevQta) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.paiIntervento.pai.idParamUot", budgetTipInterventoUot.getParametriIndataUot()),
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipInterventoUot.getBudgetTipIntervento()),
                ConditionBuilder.isNull("p.bdgConsQta")), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgtConsQta(BudgetTipInterventoUot budgetTipInterventoUot) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.bdgConsQta) FROM PaiInterventoMese p",
                ConditionBuilder.isEqual("p.paiIntervento.pai.idParamUot", budgetTipInterventoUot.getParametriIndataUot()),
                ConditionBuilder.isEqual("p.budgetTipIntervento", budgetTipInterventoUot.getBudgetTipIntervento())), BigDecimal.ZERO);
    }

    public BigDecimal sumBdgtPrevAndConsQta(BudgetTipInterventoUot budgetTipInterventoUot) {
        return sumBdgtPrevQta(budgetTipInterventoUot).add(sumBdgtConsQta(budgetTipInterventoUot));
    }


    public @Nullable
    PaiInterventoMese findByKey(int codPai, String codTipint, int cntTipint,
                                short annoEff, short meseEff, short anno, String codImp) {
        PaiInterventoMesePK paiInterventoMesePK = new PaiInterventoMesePK();
        paiInterventoMesePK.setAnno(anno);
        paiInterventoMesePK.setAnnoEff(annoEff);
        paiInterventoMesePK.setCodImp(codImp);
        paiInterventoMesePK.setCntTipint(cntTipint);
        paiInterventoMesePK.setCodTipint(codTipint);
        paiInterventoMesePK.setCodPai(codPai);
        paiInterventoMesePK.setMeseEff(meseEff);
        return getEntityManager().find(PaiInterventoMese.class, paiInterventoMesePK);
    }

    public @Nullable
    PaiInterventoMese findByKey(String codPai, String codTipint, String cntTipint,
                                String annoEff, String meseEff, String anno, String codImp) {
        return findByKey(Integer.valueOf(codPai), codTipint, Integer.valueOf(cntTipint),
                Short.valueOf(annoEff), Short.valueOf(meseEff), Short.valueOf(anno), codImp);
    }

    /**
     * conta i giorni per ogni mese nell'intervallo specificato
     *
     * @param dataInizio
     * @param dataFine
     * @return
     */
    public static List<Integer> contaGiorniPerMese(Calendar dataInizio, Calendar dataFine) {
        if ((dataFine.get(Calendar.YEAR) < dataInizio.get(Calendar.YEAR)) || (dataFine.get(Calendar.YEAR) == dataInizio.get(Calendar.YEAR) && dataFine.get(Calendar.MONTH) < dataInizio.get(Calendar.MONTH))) {
            Preconditions.checkArgument(false, "data fine (%s) prima di data inizio (%s)!!", dataFine.getTime().toGMTString(), dataInizio.getTime().toGMTString());
        }
        if (dataFine.get(Calendar.MONTH) == dataInizio.get(Calendar.MONTH) && dataFine.get(Calendar.YEAR) == dataInizio.get(Calendar.YEAR)) { //single month
            return Lists.newArrayList(dataFine.get(Calendar.DAY_OF_MONTH) - dataInizio.get(Calendar.DAY_OF_MONTH) + 1);
        }
        List<Integer> res = Lists.newArrayList(dataInizio.getActualMaximum(Calendar.DAY_OF_MONTH) - dataInizio.get(Calendar.DAY_OF_MONTH) + 1);
        dataInizio = (Calendar) dataInizio.clone();
        dataInizio.add(Calendar.MONTH, 1);
        dataInizio.set(Calendar.DAY_OF_MONTH, dataInizio.getActualMinimum(Calendar.DAY_OF_MONTH));
        dataInizio.set(Calendar.HOUR_OF_DAY, dataInizio.getActualMinimum(Calendar.HOUR_OF_DAY));
        dataInizio.set(Calendar.MINUTE, dataInizio.getActualMinimum(Calendar.MINUTE));
        dataInizio.set(Calendar.SECOND, dataInizio.getActualMinimum(Calendar.SECOND));
        dataInizio.set(Calendar.MILLISECOND, dataInizio.getActualMinimum(Calendar.MILLISECOND));
        Iterables.addAll(res, contaGiorniPerMese(dataInizio, dataFine));
        return res;
    }

    /**
     * distribuisce il costo totale secondo la distribuzione indicata
     *
     * @param spesaTot
     * @param distribuzioneCosti
     * @param useDecimal         se true, divide il piu' esattamente possibile; se
     *                           false, divide arrotondando all'intero piu' vicino; in ogni caso, scarica
     *                           eventuali avanzi sull'ultimo record
     * @return
     * @throws IllegalArgumentException se distribuzioneCosti non ha elementi
     */
    public static Iterable<BigDecimal> distribuisciCosti(BigDecimal spesaTot, Iterable<Integer> distribuzioneCosti, boolean useDecimal) {
        if (distribuzioneCosti == null || distribuzioneCosti.iterator().hasNext() == false) {
            throw new IllegalArgumentException("distribuzioneCosti non deve esseree vuoto o null.");
        }

        int size = Iterables.size(distribuzioneCosti);
        if (spesaTot == null || spesaTot == BigDecimal.ZERO) {
            return Collections.nCopies(size, spesaTot);
        }


        List<BigDecimal> result = Lists.newArrayListWithCapacity(size);
        int giorniTot = 0;
        for (int mese : distribuzioneCosti) {
            result.add(spesaTot.multiply(new BigDecimal(mese)));
            giorniTot += mese;
        }

        BigDecimal giorniTotBudget = new BigDecimal(giorniTot);
        for (int i = 0; i < result.size(); i++) {
            BigDecimal costo = result.get(i);
            if (useDecimal) {
                costo = costo.divide(giorniTotBudget, 2, RoundingMode.HALF_DOWN);
            } else {
                costo = costo.divideToIntegralValue(giorniTotBudget);
            }
            spesaTot = spesaTot.subtract(costo);
            result.set(i, costo);
        }

        int last = result.size() - 1;
        result.set(last, result.get(last).add(spesaTot));

        return result;
    }

    /**
     * inserisce uno o più mesi di spesa
     */
    public void insertMany(PaiInterventoPK paiInterventoPK, BudgetTipInterventoPK budgetTipInterventoPK, Iterable<BigDecimal> distribuzioneSpese, Iterable<BigDecimal> distribuzioneQuantita, int annoEff, int meseEff, ParametriIndata fascia) throws Exception {
        int numMesi = Iterables.size(distribuzioneSpese);

        Iterator<BigDecimal> spese = distribuzioneSpese.iterator();
        Iterator<BigDecimal> quantita = distribuzioneQuantita.iterator();
        while (spese.hasNext()) {
            PaiInterventoMese paiInterventoMese = insertSingle(paiInterventoPK, budgetTipInterventoPK, spese.next(), quantita.next(), annoEff, meseEff, false, fascia);
            logger.debug("inserito: " + paiInterventoMese);
            meseEff = (meseEff % 12) + 1;
            if (meseEff == 1) {
                annoEff++;
            }
        }
    }

    /**
     * inserisce una proposta di spesa
     *
     * @throws Exception
     */
    public PaiInterventoMese insertProp(PaiInterventoPK paiInterventoPK, BudgetTipInterventoPK budgetTipInterventoPK, BigDecimal bdgPrevEur, BigDecimal bdgPrevQta, ParametriIndata fascia) {
        initTransaction();
        PaiInterventoMese paiInterventoMese = insertSingle(paiInterventoPK, budgetTipInterventoPK, bdgPrevEur, bdgPrevQta, 0, 0, true, fascia);
        commitTransaction();
        return paiInterventoMese;
    }

    /**
     * inserisce un singolo mese di spesa (o proposta di spesa)
     *
     * @param bdgPrevEur
     * @param annoEff
     * @param meseEff
     * @param isProposta true se è una proposta, false altrimenti
     * @throws Exception
     */
    public PaiInterventoMese insertSingle(PaiInterventoPK paiInterventoPK, BudgetTipInterventoPK budgetTipInterventoPK, BigDecimal bdgPrevEur, BigDecimal bdgPrevQta, int annoEff, int meseEff, boolean isProposta, ParametriIndata fascia) {
        initTransaction();
        PaiInterventoMese paiInterventoMese = new PaiInterventoMese();
        paiInterventoMese.setPaiInterventoMesePK(new PaiInterventoMesePK(
                paiInterventoPK.getCodPai(),
                paiInterventoPK.getCodTipint(),
                paiInterventoPK.getCntTipint(),
                (short) annoEff,
                (short) meseEff,
                budgetTipInterventoPK.getCodAnno(),
                budgetTipInterventoPK.getCodImpe()));
        if (isProposta) {
            paiInterventoMese.setFlgProp('S');
        } else {
            paiInterventoMese.setFlgProp('N');
        }
        paiInterventoMese.setBdgPrevEur(bdgPrevEur);
        paiInterventoMese.setBdgPrevQta(bdgPrevQta);
        paiInterventoMese.setIdParamFascia(fascia);
        getEntityManager().merge(paiInterventoMese);
        commitTransaction();
        return paiInterventoMese;
    }

    public PaiInterventoMese findProposta(PaiInterventoPK paiInterventoPK, BudgetTipInterventoPK budgetTipInterventoPK, ParametriIndata fascia) {
        return findProposta(paiInterventoPK, budgetTipInterventoPK, true, fascia);
    }

    public PaiInterventoMese findProposta(PaiInterventoPK paiInterventoPK, BudgetTipInterventoPK budgetTipInterventoPK, boolean create, ParametriIndata fascia) {
        logger.debug("searching PaiInterventoMese prop for : {} , {}", paiInterventoPK, budgetTipInterventoPK);
        PaiInterventoMese prop = findOne(PaiInterventoMese.class, "SELECT p FROM PaiInterventoMese p",
                isEqual("p.paiIntervento.paiInterventoPK", paiInterventoPK),
                isEqual("p.budgetTipIntervento.budgetTipInterventoPK", budgetTipInterventoPK),
                isEqual("p.flgProp", PaiInterventoMese.FLG_PROPOSTA_S),
                isEqual("p.paiInterventoMesePK.annoEff", 0),
                isEqual("p.paiInterventoMesePK.meseEff", 0));
        if (prop == null && create) {
            prop = insertProp(paiInterventoPK, budgetTipInterventoPK, BigDecimal.ZERO, BigDecimal.ZERO, fascia);
        }
        return prop;
    }

    public BigDecimal findSumProp(PaiInterventoPK paiInterventoPK) {
        logger.debug("searching PaiInterventoMese prop sum for : {}", paiInterventoPK);
        BigDecimal res = getEntityManager().createQuery("SELECT SUM(p.bdgPrevEur) FROM PaiInterventoMese p WHERE "
                + "p.paiInterventoMesePK.codPai=:codPai "
                + "AND p.paiInterventoMesePK.codTipint=:codTipInt "
                + "AND p.paiInterventoMesePK.cntTipint=:cntTipInt "
                + "AND p.flgProp='S' "
                + "AND p.paiInterventoMesePK.annoEff=0 "
                + "AND p.paiInterventoMesePK.meseEff=0", BigDecimal.class).setParameter("codPai", paiInterventoPK.getCodPai()).setParameter("codTipInt", paiInterventoPK.getCodTipint()).setParameter("cntTipInt", paiInterventoPK.getCntTipint()).getSingleResult();
        return res == null ? BigDecimal.ZERO : res;
    }

    public void conferma_proposta_with_transaction(PaiInterventoPK paiInterventoPK, BudgetTipInterventoPK budgetTipInterventoPK,
                                                   IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator,
                                                   boolean deleteProp, ParametriIndata fascia) throws Exception {


        PaiInterventoMese paiInterventoMese = findProposta(paiInterventoPK, budgetTipInterventoPK, false, fascia);

        if (paiInterventoMese != null) {
            initTransaction();

            BigDecimal bdgPrevEuro = paiInterventoMese.getBdgPrevEur();
            BigDecimal bdgPrevQuantita = paiInterventoMese.getBdgPrevQta();
            boolean esisteBudgetInEuro = !bdgPrevEuro.equals(BigDecimal.ZERO);
            boolean esisteBudgetInQuntita = !bdgPrevQuantita.equals(BigDecimal.ZERO);
            boolean devoDistribuireICosti = esisteBudgetInEuro || esisteBudgetInQuntita;

            if (devoDistribuireICosti) {
                intervalliDiDistribuzioneEvaluator.crea_intervalli_per_distribuzione_costi();

                Iterable<Integer> intervalliDiDistribuzione = intervalliDiDistribuzioneEvaluator.getElencoIntervalliPerSuddivisione();
                int anno_effettivo_pagamento = intervalliDiDistribuzioneEvaluator.getAnnoEffettiviPagamenti();
                int mese_effettivo_pagamento = intervalliDiDistribuzioneEvaluator.getMeseEffettivoPagamento();


                String msgTemplate_1 = "confermo proposta paiInterventoPK:[%s], budgetTipInterventoPK:[%s], annoEff:[%s], meseEff:[%s], distribuzioneCosti:[%s], deleteProp:[%s], fascia:[%s]";
                String msgTemplate_2 = "esisteBudgetInEuro:%s, esisteBudgetInQuntita:%s, devoDistribuireICosti:%s";
                String msg_1 = String.format(msgTemplate_1, paiInterventoPK, budgetTipInterventoPK, anno_effettivo_pagamento, mese_effettivo_pagamento, intervalliDiDistribuzione, deleteProp, fascia.getIdParamIndata() + "_" + fascia.getIdParam());
                String msg_2 = String.format(msgTemplate_2, esisteBudgetInEuro, esisteBudgetInQuntita, devoDistribuireICosti);
                logger.info(msg_1 + ", " + msg_2);

                conferma_proposta(paiInterventoPK, budgetTipInterventoPK, fascia, intervalliDiDistribuzione, anno_effettivo_pagamento, mese_effettivo_pagamento, bdgPrevEuro, bdgPrevQuantita, devoDistribuireICosti);
            }

            rimuovi_proposta(paiInterventoMese, deleteProp);

            commitTransaction();
        }
    }

    void conferma_proposta(
            PaiInterventoPK paiInterventoPK,
            BudgetTipInterventoPK budgetTipInterventoPK,
            ParametriIndata fascia,
            Iterable<Integer> intervalliDiDistribuzione,
            int anno_effettivo_pagamento,
            int mese_effettivo_pagamento,
            BigDecimal bdgPrevEuro, BigDecimal bdgPrevQuantita, boolean devoDistribuireICosti) throws Exception {


        if (devoDistribuireICosti) {
            // TODO testare se la divisione quantita' e' per interi (es: pasti) o frazioni (es:ore?)
            BigDecimal costoTot = bdgPrevEuro;
            BigDecimal quantitaTot = bdgPrevQuantita;

            Iterable<BigDecimal> costi = distribuisciCosti(costoTot, intervalliDiDistribuzione, true);
            Iterable<BigDecimal> quantita = distribuisciCosti(quantitaTot, intervalliDiDistribuzione, true);

            if (logger.isDebugEnabled()) {
                logger.debug("distribuzione costi: {} -> {}", costoTot, JsonBuilder.getGson().toJson(costi));
                logger.debug("distribuzione quantita: {} -> {}", quantitaTot, JsonBuilder.getGson().toJson(quantita));
            }
            insertMany(paiInterventoPK, budgetTipInterventoPK, costi, quantita, anno_effettivo_pagamento, mese_effettivo_pagamento, fascia);
        }
    }

    private void rimuovi_proposta(PaiInterventoMese paiInterventoMese, boolean deleteProp) {
        if (deleteProp) {
            logger.debug("removing PaiInterventoMese paiInterventoMese : " + paiInterventoMese);
            getEntityManager().remove(paiInterventoMese);
            getEntityManager().flush();
        }
    }

    public void confirmAllProps(PaiIntervento paiIntervento) throws Exception {
        confirmAllProps(paiIntervento, true, false);
    }

    public void confirmWithoutArmonizzazione(PaiIntervento paiIntervento) throws Exception {
        confirmAllProps(paiIntervento, true, true);
    }

    public void confirmAllProps(PaiIntervento paiIntervento, boolean deleteProp, boolean ignoraArmonizzazione) throws Exception {
        PaiInterventoPK paiInterventoPK = paiIntervento.getPaiInterventoPK();

        List<BudgetTipIntervento> budgetPerTipoInterventoList = new BudgetTipoInterventoDao(getEntityManager()).findByCodTipint(paiInterventoPK.getCodTipint());


        Calendar dataInizio = new GregorianCalendar();
        dataInizio.setTime(paiIntervento.getDtAvvio());
        boolean useDurataMesi = paiIntervento.getTipologiaIntervento().getFlgFineDurata() == TipologiaIntervento.FLG_FINE_DURATA_D;


        List<Integer> distribuzioneCosti = inizializza_distribuzione_costi(paiIntervento, dataInizio, useDurataMesi);

        for (BudgetTipIntervento budgetTipIntervento : budgetPerTipoInterventoList) {
            List<Integer> distribuzioneCostiLocal = distribuzioneCosti;
            IntervalliDiDistribuzioneEvaluator intervalliDiDistribuzioneEvaluator = new IntervalliDiDistribuzioneEvaluator(paiIntervento, ignoraArmonizzazione, useDurataMesi, budgetTipIntervento, distribuzioneCostiLocal);

            conferma_proposta_with_transaction(
                    paiInterventoPK,
                    budgetTipIntervento.getBudgetTipInterventoPK(),
                    intervalliDiDistribuzioneEvaluator,
                    deleteProp,
                    paiIntervento.getPai().getIdParamFascia());
        }

    }

    private List<Integer> inizializza_distribuzione_costi(PaiIntervento paiIntervento, Calendar dataInizio, boolean useDurataMesi) {
        List<Integer> distribuzioneCosti;
        if (useDurataMesi) {
            distribuzioneCosti = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), paiIntervento.getDurMesi()));

        } else if (paiIntervento.getDtFine() == null) {
            logger.info("ATTENZIONE L'INTERVENTO NON PREVEDE DURATA DAL AL EPPURE LA DATA DI FINE NON C'È.. PROVIAMO A PRENDERE LA DURATA MESI PROBABILMENTE INTERVENTO CHE HA CAMBIATO TIPO DI DURATA");
            distribuzioneCosti = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), paiIntervento.getDurMesi()));
        } else if (paiIntervento.getTipologiaIntervento().getIdParamUniMis().getDesParam().contains("mens")) {
            //distribuzione costi in caso di intervento dal al e quantita mensile
            distribuzioneCosti = Lists.newArrayList(Iterables.limit(Iterables.cycle(1), Months.monthsBetween(new DateTime(paiIntervento.getDtAvvio()), new DateTime(paiIntervento.getDtFine()).plusDays(1)).getMonths()));
        } else {
            // fine ms - inizio ms * ( mesi / ms )
            Calendar dataFine = new GregorianCalendar();
            dataFine.setTime(paiIntervento.getDtFine());
            logger.debug("conteggio giorni per mese da {} a {}", dataInizio.getTime(), dataFine.getTime());
            distribuzioneCosti = contaGiorniPerMese(dataInizio, dataFine);
        }
        return distribuzioneCosti;
    }

    public void findAndClosePimNonAncoraConsuntivati(int codPai, String codTipint, int cntTipint, int mese, int anno) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM PaiInterventoMese p "
                + "WHERE p.paiInterventoMesePK.codPai = :codPai "
                + "AND p.paiInterventoMesePK.codTipint = :codTipint "
                + "AND p.paiInterventoMesePK.cntTipint = :cntTipint "
                + "AND ((p.paiInterventoMesePK.meseEff > :mese AND p.paiInterventoMesePK.annoEff = :anno) "
                + "OR (p.paiInterventoMesePK.annoEff > :anno))"
                + "AND (p.generato=1 OR p.generato=2)"
                + "");
        query.setParameter("codPai", codPai);
        query.setParameter("codTipint", codTipint);
        query.setParameter("cntTipint", cntTipint);
        query.setParameter("mese", mese);
        query.setParameter("anno", anno);


        List<PaiInterventoMese> interventi = query.getResultList();
        for (PaiInterventoMese intervento : interventi) {
            intervento.setBdgConsRientr(intervento.getBdgPrevEur());
            intervento.setBdgPrevEur(BigDecimal.ZERO);
            intervento.setBdgPrevQta(BigDecimal.ZERO);
            intervento.setBdgConsEur(BigDecimal.ZERO);
            intervento.setBdgConsQta(BigDecimal.ZERO);

            intervento.setGenerato(0);
        }

    }

    public void chiudiProposteIntervento(PaiIntervento intervento) {
        for (PaiInterventoMese proposta : intervento.getPaiInterventoMeseList()) {
            if (proposta.getFlgProp() == 'S') {
                proposta.setBdgPrevEur(BigDecimal.ZERO);
                proposta.setBdgPrevQta(BigDecimal.ZERO);
                proposta.setGenerato(0);
            }
        }
    }

    public void modificaPropPerModificaIntervento(PaiIntervento old, PaiIntervento nuovo) {
        List<PaiInterventoMese> proposte = findForPaiInt(old.getPai().getCodPai(), old.getPaiInterventoPK().getCodTipint(), old.getPaiInterventoPK().getCntTipint());
        for (PaiInterventoMese proposta : proposte) {
            if (proposta.getFlgProp() == 'S') {

                PaiInterventoMese paiInterventoMese = insertSingle(nuovo.getPaiInterventoPK(), proposta.getBudgetTipIntervento().getBudgetTipInterventoPK(), proposta.getBdgPrevEur(), proposta.getBdgPrevQta(), 0, 0, true, old.getPai().getIdParamFascia());
                getEntityManager().persist(paiInterventoMese);

            }
        }
    }

    /**
     * Prende un vecchio pai intervento mese e gli cambia impegno.
     *
     * @param old
     * @param nuovoCodiceImpegno
     * @param nuovoAnno
     * @return
     */
    public PaiInterventoMese trasformaInterventoMese(PaiInterventoMese old, String nuovoCodiceImpegno, short nuovoAnno) {
        initTransaction();

        PaiInterventoMese nuovoInterventoMese = new PaiInterventoMese(old.getPaiInterventoMesePK().getCodPai(), old.getPaiInterventoMesePK().getCodTipint(), old.getPaiInterventoMesePK().getCntTipint(), old.getPaiInterventoMesePK().getAnnoEff(), old.getPaiInterventoMesePK().getMeseEff(), nuovoAnno, nuovoCodiceImpegno);
        nuovoInterventoMese.setFlgProp('N');
        nuovoInterventoMese.setBdgPrevEur(old.getBdgPrevEur());
        nuovoInterventoMese.setBdgPrevQta(old.getBdgPrevQta());
        nuovoInterventoMese.setPaiIntervento(old.getPaiIntervento());
        BudgetTipoInterventoDao bdao = new BudgetTipoInterventoDao(getEntityManager());
        nuovoInterventoMese.setBudgetTipIntervento(bdao.findByKey(old.getPaiInterventoMesePK().getCodTipint(), String.valueOf(nuovoAnno), nuovoCodiceImpegno));
        getEntityManager().persist(nuovoInterventoMese);
        getEntityManager().remove(old);
        getEntityManager().flush();
        return nuovoInterventoMese;
    }


    public void aggiornaFasciaReddito(int codPai, ParametriIndata idParamFascia, Date dataCambioFascia) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(dataCambioFascia);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        // prima ci recuperiamo tutti gli interventi mese con anni meggiori ql corrente
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM PaiInterventoMese p "
                + "WHERE p.paiInterventoMesePK.codPai = :codPai "
                + "AND p.paiInterventoMesePK.annoEff > :anno");
        query.setParameter("codPai", codPai);
        query.setParameter("anno", year);

        List<PaiInterventoMese> interventiProssimiAnni = query.getResultList();
        for (PaiInterventoMese pim : interventiProssimiAnni) {
            pim.setIdParamFascia(idParamFascia);
            this.update(pim);
        }
        Query monthquery = getEntityManager().createQuery("SELECT p "
                + "FROM PaiInterventoMese p "
                + "WHERE p.paiInterventoMesePK.codPai = :codPai "
                + "AND p.paiInterventoMesePK.annoEff = :anno "
                + "AND p.paiInterventoMesePK.meseEff >=:mese");
        monthquery.setParameter("codPai", codPai);
        monthquery.setParameter("anno", year);
        monthquery.setParameter("mese", month);
        List<PaiInterventoMese> interventiProssimiMesi = monthquery.getResultList();
        for (PaiInterventoMese ipm : interventiProssimiMesi) {
            ipm.setIdParamFascia(idParamFascia);
            this.update(ipm);
        }


    }

    /**
     * aggiorna quantita
     *
     * @param codPai
     * @param dataCambioFascia
     * @throws Exception
     */
    public void aggiornaQuantita(int codPai, String codTipInt, int cntTipInt, BigDecimal nuovaQuantita, Date dataCambioFascia) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(dataCambioFascia);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        // prima ci recuperiamo tutti gli interventi mese con anni meggiori ql corrente
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM PaiInterventoMese p "
                + "WHERE p.paiInterventoMesePK.codPai = :codPai "
                + " AND p.paiInterventoMesePK.codTipint = :codTipint"
                + " AND p.paiInterventoMesePK.cntTipint = :cntTipint"
                + " AND  p.bdgConsEur is null"
                + " AND p.paiInterventoMesePK.annoEff > :anno");
        query.setParameter("codPai", codPai);
        query.setParameter("codTipint", codTipInt);
        query.setParameter("cntTipint", cntTipInt);

        query.setParameter("anno", year);

        List<PaiInterventoMese> interventiProssimiAnni = query.getResultList();
        for (PaiInterventoMese pim : interventiProssimiAnni) {
            pim.setBdgPrevEur(nuovaQuantita);
            pim.setBdgPrevQta(nuovaQuantita);
            this.update(pim);
        }
        Query monthquery = getEntityManager().createQuery("SELECT p "
                + "FROM PaiInterventoMese p "
                + "WHERE p.paiInterventoMesePK.codPai = :codPai "
                + "AND p.paiInterventoMesePK.annoEff = :anno "
                + "AND p.paiInterventoMesePK.meseEff >=:mese"
                + " AND p.paiInterventoMesePK.codTipint = :codTipint"
                + " AND p.paiInterventoMesePK.cntTipint = :cntTipint"
                + " AND  p.bdgConsEur is null");
        monthquery.setParameter("codPai", codPai);
        monthquery.setParameter("codTipint", codTipInt);
        monthquery.setParameter("cntTipint", cntTipInt);
        monthquery.setParameter("anno", year);
        monthquery.setParameter("mese", month);
        List<PaiInterventoMese> interventiProssimiMesi = monthquery.getResultList();
        for (PaiInterventoMese ipm : interventiProssimiMesi) {
            ipm.setBdgPrevEur(nuovaQuantita);
            ipm.setBdgPrevQta(nuovaQuantita);
            this.update(ipm);
        }


    }

    public void removeAllProps(PaiIntervento paiIntervento) {
        executeUpdate(PaiInterventoMese.class, "DELETE FROM PaiInterventoMese p", isEqual("p.paiIntervento", paiIntervento));
    }

    public List<PaiInterventoMese> findForPaiInt(int codPai, String codTipint, int cntTipint) {
        logger.debug("PaiInterventoMeseDao.findForPaiInt  codPai: " + codPai + ", codTipint: " + codTipint + ", cntTipint: " + cntTipint);

        return find(PaiInterventoMese.class, "SELECT p FROM PaiInterventoMese p",
                isEqual("p.paiInterventoMesePK.codPai", codPai),
                isEqual("p.paiInterventoMesePK.codTipint", codTipint),
                isEqual("p.paiInterventoMesePK.cntTipint", cntTipint));
    }

    public BigDecimal contaGiorniAssenza(PaiIntervento paiIntervento) {
        return contaGiorniAssenza(paiIntervento.getPaiInterventoPK().getCodPai().toString(),
                paiIntervento.getPaiInterventoPK().getCodTipint(),
                paiIntervento.getPaiInterventoPK().getCntTipint().toString());
    }

    public BigDecimal contaGiorniAssenza(String codPai, String codTipInt, String cntTipInt) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.ggAssenza) FROM PaiInterventoMese p",
                isEqual("p.paiInterventoMesePK.codPai", Integer.valueOf(codPai)),
                isEqual("p.paiInterventoMesePK.codTipint", codTipInt),
                isEqual("p.paiInterventoMesePK.cntTipint", Integer.valueOf(cntTipInt))), BigDecimal.ZERO);
    }

    public BigDecimal contaGiorniAssenza(String anno, String codPai, String codTipInt, String cntTipInt) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(p.ggAssenza) FROM PaiInterventoMese p",
                isEqual("p.paiInterventoMesePK.annoEff", Integer.valueOf(anno)),
                isEqual("p.paiInterventoMesePK.codPai", Integer.valueOf(codPai)),
                isEqual("p.paiInterventoMesePK.codTipint", codTipInt),
                isEqual("p.paiInterventoMesePK.cntTipint", Integer.valueOf(cntTipInt))), BigDecimal.ZERO);
    }


    /**
     * chiudiamo/azzeriamo i pim rimasti aperti
     *
     * @param intervento
     */
    public void chiudiPerIntervento(PaiIntervento intervento) {
        for (PaiInterventoMese paiInterventoMese : intervento.getPaiInterventoMeseList()) {
            if (paiInterventoMese.getBdgConsEur() == null) {
                paiInterventoMese.setBdgConsEur(BigDecimal.ZERO);
                paiInterventoMese.setBdgConsQta(BigDecimal.ZERO);
                paiInterventoMese.setBdgConsQtaBenef(BigDecimal.ZERO);
                paiInterventoMese.setGenerato(0);
            }
        }
    }
}
