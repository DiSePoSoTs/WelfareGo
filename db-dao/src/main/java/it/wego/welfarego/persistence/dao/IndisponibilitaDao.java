package it.wego.welfarego.persistence.dao;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.wego.persistence.objects.Condition;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Indisponibilita;
import it.wego.welfarego.persistence.entities.Utenti;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import static it.wego.persistence.ConditionBuilder.*;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import javax.annotation.Nullable;

/**
 *
 * @author giuseppe
 */
public class IndisponibilitaDao extends PersistenceAdapter implements ORMDao {

    public IndisponibilitaDao(EntityManager em) {
        super(em);
    }

    public List<Indisponibilita> findIndisponibilita(Date from, Date to) {
        return findIndisponibilita((Utenti) null, from, to);
    }

    public List<Indisponibilita> findIndisponibilita(@Nullable Utenti utente, Date from, Date to) {
        String query = "SELECT i FROM Indisponibilita i";
        Condition condition = or(
                between("i.tsIniApp", from, to),
                between("i.tsFineApp", from, to));
        if (utente != null) {
            condition = and(condition, isEqual("i.utenti", utente));
        }
        return find(Indisponibilita.class, query, condition);
    }

    public List<Indisponibilita> findIndisponibilita(ParametriIndata uot, Date from, Date to) {
        Preconditions.checkArgument(Objects.equal(uot.getIdParam().getTipParam().getTipParam(), Parametri.CODICE_UOT));
        return find(Indisponibilita.class, "SELECT i FROM Indisponibilita i", or(
                between("i.tsIniApp", from, to),
                between("i.tsFineApp", from, to)), isEqual("i.utenti.idParamUot", uot));
    }

    public List<Indisponibilita> findIndisponibilitaByCodAs(int codAs, Date from, Date to) {
        Query query = getEntityManager().createQuery("SELECT a "
                + "FROM Indisponibilita a "
              +  "WHERE a.codAs=:codAs AND ((:from>a.tsIniApp and :from<a.tsFineApp) or (:to>a.tsIniApp and :to<a.tsFineApp))", List.class);
        query.setParameter("codAs", codAs);
        query.setParameter("from", from);
        query.setParameter("to", to);
        List<Indisponibilita> indisponibilita = query.getResultList();
        return indisponibilita;
    }

    public boolean containsIndisponibilita(int codAs, Date from, Date to, Date referenceDate) {
        Query query = getEntityManager().createQuery("SELECT a "
                + "FROM Indisponibilita a "
                + "WHERE a.codAs=:codAs AND ((:from>a.tsIniApp and :from<a.tsFineApp) or (:to>a.tsIniApp and :to<a.tsFineApp))", List.class);
        query.setParameter("codAs", codAs);
        query.setParameter("from", from);
        query.setParameter("to", to);
        List<Indisponibilita> indisponibilita = query.getResultList();
        if (indisponibilita.size() == 1 && indisponibilita.get(0).getTsIniApp().equals(referenceDate)) {
            return false;
        }
        return indisponibilita.size() > 0;
    }

    public @Nullable
    Indisponibilita findByKey(String key) {
        return Strings.isNullOrEmpty(key) ? null : findByKey(Integer.valueOf(key));
    }

    public @Nullable
    Indisponibilita findByKey(Integer key) {
        return getEntityManager().find(Indisponibilita.class, key);
    }

}
