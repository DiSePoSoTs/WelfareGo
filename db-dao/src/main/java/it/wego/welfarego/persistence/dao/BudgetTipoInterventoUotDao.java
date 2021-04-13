/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.BudgetTipInterventoUot;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author giuseppe
 */
public class BudgetTipoInterventoUotDao extends PersistenceAdapter {

    public BudgetTipoInterventoUotDao(EntityManager em) {
        super(em);
    }

    public List<BudgetTipInterventoUot> findByCodTipIntIdParamUot(String codTipint, int idParamUot) {
        TypedQuery<BudgetTipInterventoUot> query = this.getEntityManager().createQuery("SELECT b "
                + "FROM BudgetTipInterventoUot b "
                + "WHERE b.budgetTipInterventoUotPK.codTipint = :codTipint "
                + "AND b.budgetTipInterventoUotPK.idParamUot = :idParamUot", BudgetTipInterventoUot.class);
        query.setParameter("codTipint", codTipint);
        query.setParameter("idParamUot", idParamUot);
        return query.getResultList();
    }

    public BudgetTipInterventoUot findByKey(String codTipint, short codAnno, String codImpe, int idParamUot) {
        Query query = this.getEntityManager().createQuery("SELECT b "
                + "FROM BudgetTipInterventoUot b "
                + "WHERE b.budgetTipInterventoUotPK.codTipint = :codTipint "
                + "AND b.budgetTipInterventoUotPK.codAnno = :codAnno "
                + "AND b.budgetTipInterventoUotPK.codImpe = :codImpe "
                + "AND  b.budgetTipInterventoUotPK.idParamUot = :idParamUot");
        query.setParameter("codTipint", codTipint);
        query.setParameter("codAnno", codAnno);
        query.setParameter("codImpe", codImpe);
        query.setParameter("idParamUot", idParamUot);

        BudgetTipInterventoUot budget = (BudgetTipInterventoUot) Connection.getSingleResult(query);
        return budget;
    }
}
