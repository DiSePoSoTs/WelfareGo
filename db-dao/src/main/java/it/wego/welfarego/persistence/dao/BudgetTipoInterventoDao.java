/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import com.google.common.base.MoreObjects;
import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class BudgetTipoInterventoDao extends PersistenceAdapter {

    public BudgetTipoInterventoDao(EntityManager em) {
        super(em);
    }

    public List<BudgetTipIntervento> findByCodTipint(String codTipInt, int limit, int offset) {
        return getEntityManager().createNamedQuery("BudgetTipIntervento.findByCodTipint").setParameter("codTipint", codTipInt).setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    public List<BudgetTipIntervento> findByCodTipInt_And_Cod_Anno(String codTipInt, List anni, int limit, int offset) {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNamedQuery("BudgetTipIntervento.findBy_CodTipInt_And_Cod_Anno");
        query = query.setParameter("codTipint", codTipInt);
        query = query.setParameter("anni", anni);
        query = query.setFirstResult(offset);
        query = query.setMaxResults(limit);
        List resultList = query.getResultList();
        return resultList;
    }

    public List<BudgetTipIntervento> findByCodTipintOrdered(String codTipInt) {

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNamedQuery("findByCodTipint_ordered");
        query.setParameter("codTipint", codTipInt);

        List list = query.getResultList();
        return list;
    }

    public List<BudgetTipIntervento> findByCodTipint(String codTipInt) {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNamedQuery("BudgetTipIntervento.findByCodTipint");
        query.setParameter("codTipint", codTipInt);

        List list = query.getResultList();
        return list;
    }

    public BudgetTipIntervento findByKey(String codTipint, String codAnno, String codImpe) {
        return getEntityManager().find(BudgetTipIntervento.class, new BudgetTipInterventoPK(codTipint, Short.parseShort(codAnno), codImpe));
    }

    public BigDecimal getBdgDisp(String codTipInt) {
        BigDecimal res = getEntityManager().createQuery("SELECT SUM(b.bdgDispEur) FROM BudgetTipIntervento b WHERE b.budgetTipInterventoPK.codTipint=:codTipint", BigDecimal.class).setParameter("codTipint", codTipInt).getSingleResult();
        return res == null ? BigDecimal.ZERO : res;
    }

    public BigDecimal getBdgDispUot(PaiIntervento paiIntervento) {
        return MoreObjects.firstNonNull(findOne(BigDecimal.class, "SELECT SUM(b.bdgDispEur) FROM BudgetTipInterventoUot b",
                ConditionBuilder.isEqual("b.budgetTipInterventoUotPK.codTipint", paiIntervento.getPaiInterventoPK().getCodTipint()),
                ConditionBuilder.isEqual("b.budgetTipInterventoUotPK.idParamUot", paiIntervento.getPai().getIdParamUot().getIdParamIndata())), BigDecimal.ZERO);
    }

    public boolean isInterventoSenzaBudget(String codTipint) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT ");
        sql.append(" tip_int.COD_TIPINT ");
        sql.append(" FROM TIPOLOGIA_INTERVENTO tip_int ");
        sql.append(" LEFT JOIN BUDGET_TIP_INTERVENTO budget ");
        sql.append(" ON tip_int.COD_TIPINT = BUDGET.COD_TIPINT ");
        sql.append(" WHERE ");
        sql.append(" BUDGET.COD_TIPINT IS NULL ");
        sql.append(" AND tip_int.COD_TIPINT = ? ");
        Query nativeQuery = getEntityManager().createNativeQuery(sql.toString());
        nativeQuery.setParameter(1, codTipint);
        return nativeQuery.getResultList().size() > 0;
    }

    public boolean isInterventoSenzaBudget(PaiIntervento nuovoIntervento) {
        String codTipint = nuovoIntervento.getPaiInterventoPK().getCodTipint();
        return isInterventoSenzaBudget(codTipint);
    }
}
