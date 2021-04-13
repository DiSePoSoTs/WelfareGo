package it.wego.welfarego.persistence.dao;

import com.google.common.collect.Iterables;
import static it.wego.persistence.ConditionBuilder.*;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author giuseppe
 */
public class ParametriDao extends PersistenceAdapter implements ORMDao {

    public ParametriDao(EntityManager em) {
        super(em);
    }

    public Parametri findByKey(int idParam, String tipParam) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM Parametri p "
                + "WHERE p.idParam = :idParam "
                + "AND p.tipParam.tipParam = :tipParam");
        query.setParameter("idParam", idParam);
        query.setParameter("tipParam", tipParam);
        Parametri parametro = (Parametri) Connection.getSingleResult(query);
        return parametro;
    }

    public List<Parametri> findByTipParam(String tipParam) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM Parametri p "
                + "WHERE p.tipParam.tipParam = :tipParam");
        query.setParameter("tipParam", tipParam);
        List<Parametri> parametri = query.getResultList();
        return parametri;
    }

    public List<Parametri> findByCodParamTipParam(String codParam, String tipParam) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM Parametri p "
                + "WHERE p.tipParam.tipParam = :tipParam "
                + "AND p.codParam = :codParam");
        query.setParameter("tipParam", tipParam);
        query.setParameter("codParam", codParam);
        List<Parametri> parametri = query.getResultList();
        return parametri;
    }

    public Set<String> getParamCodesByTipParam(String tipParam) {
        List<Parametri> parametri = findByTipParam(tipParam);
        Set<String> res = new TreeSet<String>();
        for (Parametri param : parametri) {
            res.add(param.getCodParam());
        }
        return res;
    }


    public Parametri findOneByCodParamTipParam(String codParam, String tipParam) {
        return findOne(Parametri.class, "SELECT p FROM Parametri p",
                isEqual("p.tipParam.tipParam", tipParam),
                isEqual("p.codParam", codParam));
    }

    public List<Parametri> findAll() {
        return find(Parametri.class, "SELECT p FROM Parametri p", (Condition) null);
    }
}
