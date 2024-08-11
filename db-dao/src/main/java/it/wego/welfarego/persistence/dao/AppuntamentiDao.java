package it.wego.welfarego.persistence.dao;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.persistence.objects.Order.OrderDir;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.entities.Appuntamento;
import it.wego.welfarego.persistence.entities.Pai;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import it.wego.extjs.beans.Order;
import static it.wego.persistence.ConditionBuilder.*;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.Nullable;

/**
 *
 * @author giuseppe
 */
public class AppuntamentiDao extends PersistenceAdapter implements ORMDao {

    public AppuntamentiDao(EntityManager em) {
        super(em);
    }

    public List<Appuntamento> findAppuntamenti(@Nullable Utenti utente, Date from, Date to) {
        String query = "SELECT a FROM Appuntamento a";
        Condition condition = or(
                between("a.tsIniApp", from, to),
                between("a.tsFineApp", from, to));
        if (utente != null) {
            condition = and(condition, isEqual("a.utenti", utente));
        }
        return find(Appuntamento.class, query, condition);
    }

    public List<Appuntamento> findAppuntamenti(ParametriIndata uot, Date from, Date to) {
        Preconditions.checkArgument(Objects.equal(uot.getIdParam().getTipParam().getTipParam(), Parametri.CODICE_UOT));
        return find(Appuntamento.class, "SELECT a FROM Appuntamento a", or(
                between("a.tsIniApp", from, to),
                between("a.tsFineApp", from, to)), isEqual("a.utenti.idParamUot", uot));
    }

    public List<Appuntamento> findAppuntamenti(Date from, Date to) {
        return findAppuntamenti((Utenti) null, from, to);
    }

    public List<Appuntamento> findAppuntamentiByCodPai(int codPai, int limit, int offset, Order order) {
        String queryString = "SELECT a "
                + "FROM Appuntamento a JOIN a.codPai b "
                + "WHERE b.codPai = :codPai ";
        if (order != null) {
            queryString = queryString + "ORDER BY a." + order.getProperty() + " " + order.getDirection();
        }
        Query query = getEntityManager().createQuery(queryString, List.class);
        query.setParameter("codPai", codPai);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Appuntamento> appuntamenti = query.getResultList();
        return appuntamenti;
    }

    public List<Appuntamento> findAppuntamentyByCodAna(int codAnag, int limit, int offset, Order order) {
        PaiDao dao = new PaiDao(getEntityManager());
        List<Pai> listaPai = dao.findPaiByCodAna(codAnag);
        if (listaPai == null) {
            return Collections.emptyList();
        }
		List<Appuntamento> appuntamenti = new ArrayList<Appuntamento>();
		for (Pai pai : listaPai)
		{
			appuntamenti.addAll(findAppuntamentiByCodPai(pai.getCodPai(), limit, offset, order));
		}//for
        return appuntamenti;
    }

    public List<Appuntamento> findAppuntamentiByCodAs(int codAs, Date from, Date to) {
        Query query = getEntityManager().createQuery("SELECT a "
                + "FROM Appuntamento a "
                + "WHERE a.codAs=:codAs AND a.tsIniApp>:from and a.tsFineApp<:to", List.class);
        query.setParameter("codAs", codAs);
        query.setParameter("from", from);
        query.setParameter("to", to);
        List<Appuntamento> appuntamenti = query.getResultList();
        return appuntamenti;
    }

    public boolean containsAppuntamento(int codAs, Date from, Date to, Date referenceDate) {
    	Date fromplusone = new Date();
    	fromplusone.setTime(from.getTime()+1000);
    	Date tominusone = new Date();
    	tominusone.setTime(to.getTime()- 1000);
        Query query = getEntityManager().createQuery("SELECT a "
                + "FROM Appuntamento a "
                +  "WHERE a.codAs=:codAs AND ((a.tsIniApp=:from and a.tsFineApp=:to) or (a.tsIniApp between :fromplusone  and :tominusone) or (a.tsFineApp between :fromplusone and :tominusone))", List.class);
        query.setParameter("codAs", codAs);
        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("fromplusone", fromplusone);
        query.setParameter("tominusone",tominusone);
        List<Appuntamento> appuntamenti = query.getResultList();
        if (appuntamenti.size() == 1 && appuntamenti.get(0).getTsIniApp().equals(referenceDate)) {
            return false;
        }
        return appuntamenti.size() > 0;
    }

    public @Nullable
    Appuntamento findByKey(String key) {
        return Strings.isNullOrEmpty(key) ? null : findByKey(Integer.valueOf(key));
    }

    public @Nullable
    Appuntamento findByKey(Integer key) {
        return getEntityManager().find(Appuntamento.class, key);
    }

}
