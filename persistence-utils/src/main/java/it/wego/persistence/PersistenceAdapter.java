package it.wego.persistence;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import it.wego.persistence.objects.Assignment;
import it.wego.persistence.objects.Order;
import it.wego.persistence.objects.Condition;
import java.util.Collection;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import static it.wego.persistence.ConditionUtils.*;
import javax.annotation.Nullable;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;

/**
 * E' un wrapper attorno ad un entitymanager, fornisce un minimo logging, ed una
 * gestione più comoda delle transazioni (più transazioni possono essere aperte
 * e chiuse nel codice, questa classe fa si che solo la più ampia sia
 * effettivamente propagata all'entitymanager). Tutti i metodi di questa classe
 * effettuano più controlli di quelli dell'entitymanager, causando quindi meno
 * eccezzioni e permettendo un uso più libero.
 *
 * @author aleph
 */
public class PersistenceAdapter {

    private EntityManager em;
    private final static Logger logger = LoggerFactory.getLogger(PersistenceAdapter.class);
    private PersistenceUnitUtil persistenceUnitUtil;

    public Logger getLogger() {
        return logger;
    }

    public PersistenceAdapter() {
        this(null);
    }

    public PersistenceAdapter(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     * chiude le risorse associate con questo PersistenceAdapter
     */
    public void close() {
        if (getEntityManager() != null) {
            if (getEntityManager().isOpen()) {
                rollbackTransaction();
                getEntityManager().close();
            }
            setEntityManager(null);
            PersistenceAdapterFactory.releasePersistenceAdapter(this);
            logger.debug("closed persistence adapter");
        }
    }
    private int managedTransactions = 0;

    /**
     * inizializza una transazione se non ve ne è già una attiva
     */
    public void initTransaction() {
        if (managedTransactions == 0 && getEntityManager().getTransaction().isActive()) {
            return;
        }
        managedTransactions++;
        if (managedTransactions == 1) {
//            logger.debug(em + " initializing transaction");
            getEntityManager().getTransaction().begin();
        }
    }

    /**
     * committa una transazione se è stata inizializzata a questo livello (ie
     * non più in alto)
     */
    public void commitTransaction() {
        if (managedTransactions == 1) {
//            logger.debug(em + " committing transaction");
            getEntityManager().getTransaction().commit();
        }
        if (managedTransactions > 0) {
            managedTransactions--;
        }
    }

    /**
     * effettua il rollback di eventuali transazioni attive
     */
    public void rollbackTransaction() {
        if (getEntityManager().getTransaction().isActive()) {
            logger.debug(em + " rollback transaction");
            getEntityManager().getTransaction().rollback();
        }
        managedTransactions = 0;
    }

    /**
     * inserisce un oggetto nel contesto di persistenza (em.persist()). gestisce
     * la transazione se necessario
     *
     * @param object
     * @throws Exception
     */
    public void insert(Object object) throws Exception {
        initTransaction();
        getEntityManager().persist(object);
        commitTransaction();
    }

    /**
     * aggiorna un'entità presente nel contesto di persistenza (em.merge()).
     * gestisce la transazione se necessario
     *
     * @param object
     * @throws Exception
     */
    public void update(Object object) throws Exception {
        initTransaction();
        getEntityManager().merge(object);
        commitTransaction();
    }

    /**
     * rimuove un'entità dal contesto di persistenza (em.remove()). gestisce la
     * transazione se necessario
     *
     * @param object
     * @throws Exception
     */
    public void delete(Object object) throws Exception {
        initTransaction();
        getEntityManager().remove(object);
        commitTransaction();
    }

    /**
     * restituisce un singolo risultato per una query.
     *
     * @param <T>
     * @param query
     * @return un singolo risultato <T>, o null se la query restituisce 0
     * risultati.
     */
    public static <T> T getSingleResult(TypedQuery<T> query) {
        return (T) getSingleResult((Query) query);
    }

    /**
     * restituisce un singolo risultato per una query.
     *
     * @param query
     * @return un singolo risultato <T>, o null se la query restituisce 0
     * risultati.
     */
    public static @Nullable
    Object getSingleResult(Query query) {
        List list = query.getResultList();
        if (list.size() > 1) {
            logger.warn("more than one result was returned for query.getSingleResult() : {}", list);
            throw new NonUniqueResultException("more than one result was returned for query.getSingleResult()");
        }
        return list.isEmpty() ? null : list.get(0);
    }

    public @Nullable
    <T extends Object> T findSafe(Class<T> clazz, Object key) {
        try {
            return getEntityManager().find(clazz, key);
        } catch (Exception ex) {
            getLogger().warn("error retriaving object by key, class = {}, key = {}", clazz, key);
            getLogger().warn("error retriaving object by key, error = ", ex);
            return null;
        }
    }

    public <T extends Object> List<T> find(Class<T> clazz, String customSelect, Collection<Condition> conditions, List<Order> orders, Integer limit, Integer offset) {
        return createQuery(getEntityManager(), clazz, customSelect, conditions, null, orders, limit, offset).getResultList();
    }

    public <T extends Object> List<T> find(Class<T> clazz, String customSelect, Condition condition, List<Order> orders, Integer limit, Integer offset) {
        return createQuery(getEntityManager(), clazz, customSelect, condition, null, orders, limit, offset).getResultList();
    }

    public <T extends Object> List<T> find(Class<T> clazz, String customSelect, Collection<Condition> conditions) {
        return createQuery(getEntityManager(), clazz, customSelect, conditions, null, null, null, null).getResultList();
    }

    public <T extends Object> List<T> find(Class<T> clazz, String customSelect, Condition condition) {
        return createQuery(getEntityManager(), clazz, customSelect, condition, null, null, null, null).getResultList();
    }

    public <T extends Object> List<T> find(Class<T> clazz, String customSelect, Condition... conditions) {
        return find(clazz, customSelect, Arrays.asList(conditions));
    }

    public <T extends Object> T findOne(Class<T> clazz, String customSelect, Condition... conditions) {
        return findOne(clazz, customSelect, Arrays.asList(conditions));
    }

    public <T extends Object> T findOne(Class<T> clazz, String customSelect, List<Condition> conditions) {
        return getSingleResult(createQuery(getEntityManager(), clazz, customSelect, conditions, null, null, null, null));
    }

    public int executeUpdate(Class clazz, String query, Condition condition, Collection<Assignment> assignments) {
        return createQuery(getEntityManager(), clazz, query, condition, assignments, null, null, null).executeUpdate();
    }

    public int executeUpdate(Class clazz, String query, Condition condition, Assignment... assignments) {
        return executeUpdate(clazz, query, condition, Arrays.asList(assignments));
    }

    public PersistenceUnitUtil getPersistenceUnitUtil() {
        if (persistenceUnitUtil == null) {
            persistenceUnitUtil = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
        }
        return persistenceUnitUtil;
    }

    /**
     * build a managed reference for the (supposedly detached) provided
     * reference
     *
     * @param <T>
     * @param detachedInstance
     * @return
     */
    public @Nullable
    <T> T getReference(@Nullable T detachedInstance) {
        return detachedInstance == null ? null : (T) getEntityManager().getReference(detachedInstance.getClass(), getPersistenceUnitUtil().getIdentifier(detachedInstance));
    }
}
