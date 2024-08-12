/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.trieste.comune.ssc.beans.Order;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Appuntamento;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiMacroProblematica;
import it.wego.welfarego.persistence.utils.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author giuseppe
 */
public class PaiDao extends PersistenceAdapter {

    public PaiDao(EntityManager em) {
        super(em);
    }

    public List<PaiMacroProblematica> removeMacroProblematiche(Pai pai) {
        if (pai.getPaiMacroProblematicaList().isEmpty()) {
            return Collections.emptyList();
        }
        List<PaiMacroProblematica> paiMacroProblematicaList = Lists.newArrayList(pai.getPaiMacroProblematicaList());
        pai.getPaiMacroProblematicaList().clear();
        initTransaction();
        for (PaiMacroProblematica paiMacroProblematica : paiMacroProblematicaList) {
            getEntityManager().remove(paiMacroProblematica);
        }
        commitTransaction();
        return paiMacroProblematicaList;
    }

    public Pai findPai(String codPai) throws Exception {
        return findPai(Integer.valueOf(codPai));
    }

    public Pai findPai(Integer codPai) throws Exception {
        return getEntityManager().find(Pai.class, codPai);
    }

    @Deprecated
    public Pai findPaiByCodPai(int codPai) throws Exception {
        return findPai(codPai);
    }

    @Deprecated
    public Pai findPaiByPaiId(int codPai) throws Exception {
        Query query = getEntityManager().createNamedQuery("Pai.findByCodPai");
        query.setParameter("codPai", codPai);
        Pai pai = (Pai) Connection.getSingleResult(query);
        return pai;
    }

    public int countPaiByCodAna(int codAna) {
        Query query = getEntityManager().createQuery("SELECT COUNT(p) "
                + "FROM Pai p JOIN p.codAna l "
                + "WHERE l.codAna = :codAna ");
        query.setParameter("codAna", codAna);
        int count = 0;
        Long result = (Long) query.getSingleResult();
        if (result != null) {
            count = result.intValue();
        }
        return count;
    }

    public List<Pai> findPaiByCodAnaOrdered(int codAna, int limit, int offset) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM Pai p JOIN p.codAna l "
                + "WHERE l.codAna = :codAna "
                + "ORDER BY p.dtApePai DESC");
        query.setParameter("codAna", codAna);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        List<Pai> pai = query.getResultList();
        return pai;
    }

    /**
     * @param codAna
     * @param limit
     * @param offset
     * @param order
     * @return
     * @deprecated use terrible ordering tecnique . . use JsonBuilder ordering
     * instead, plz
     */
    @Deprecated
    public List<Pai> findPaiByCodAnaOrdered(int codAna, int limit, int offset, Order order) {
        if (order != null) {
            String property = order.getProperty();
            if (Objects.equal(property, "statoPai")) { //ugly fix for a latter problem . . 
                property = "flgStatoPai";
            }
            Query query = getEntityManager().createQuery("SELECT p "
                    + "FROM Pai p JOIN p.codAna l "
                    + "WHERE l.codAna = :codAna "
                    + "ORDER BY p." + property + " " + order.getDirection()); // this ordering tecnique is . . horrible . . 
            query.setParameter("codAna", codAna);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            List<Pai> pai = query.getResultList();
            return pai;
        } else {
            return findPaiByCodAnaOrdered(codAna, limit, offset);
        }
    }

    public List<Pai> findPaiByCodAna(int codAna, int limit, int offset) {
        Query query = getFindByCodAnaQuery(codAna);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Pai> pai = query.getResultList();
        return pai;
    }

    public List<Pai> findPaiByCodAna(int codAna) {
        Query query = getFindByCodAnaQuery(codAna);
        List<Pai> pai = query.getResultList();
        return pai;
    }

    private Query getFindByCodAnaQuery(int codAna) {
        Query query = getEntityManager().createQuery("SELECT p " + "FROM Pai p JOIN p.codAna l " + "WHERE l.codAna = :codAna ");
        query.setParameter("codAna", codAna);
        return query;
    }

    public Pai findLastClosedPai(int codAna) {
        TypedQuery<Pai> query = getEntityManager().createQuery("SELECT p "
                + "FROM Pai p "
                + "WHERE p.codAna.codAna = :codAna "
                + "ORDER BY p.dtChiusPai DESC", Pai.class);
        query.setParameter("codAna", codAna);
        query.setMaxResults(1);
        Pai pai = getSingleResult(query);
        return pai;
    }

    public Pai closeAllButLastPai(List<Pai> list) {
        Pai lastPai = Collections.max(list, new Comparator<Pai>() {
            public int compare(Pai o1, Pai o2) {
                return o1.getDtApePai().compareTo(o2.getDtApePai());
            }
        });
        for (Pai pai : Iterables.filter(list, Predicates.not(Predicates.equalTo(lastPai)))) {
            getLogger().debug("chiudiamo il pai {}", pai);
            initTransaction();
            pai.setDtChiusPai(new Date());
            pai.setFlgStatoPai(Pai.STATO_CHIUSO);
            commitTransaction();
        }
        return lastPai;
    }

    public @Nullable
    Pai findLastPai(int codAna) {
        TypedQuery<Pai> query = getEntityManager().createQuery("SELECT p "
                + "FROM Pai p JOIN p.codAna c "
                + "JOIN c.anagrafeSoc l "
                + "WHERE l.codAna = :codAna "
                + "AND p.dtChiusPai IS NULL ", Pai.class);
        query.setParameter("codAna", codAna);
        List<Pai> list = query.getResultList();
        if (list.size() > 1) {
            getLogger().debug("more than one pai open for codAna = {}, fixing . . ", codAna);
            return closeAllButLastPai(list);
        } else if (list.isEmpty()) {
            return null;
        } else {
            return list.iterator().next();
        }
    }

    /**
     * Metodo che trova e chiude tutti i pai che non abbiano operazioni da sei mesi.
     *
     * @return
     * @throws Exception
     */

    public List<Pai> findVecchiDiSeiMesi() {
        Logger chiusuraAutomaticaSchedulerLogger = LoggerFactory.getLogger("it.wego.welfarego.scheduler.ChiusuraAutomaticaScheduler");
        DateTime dt = new DateTime();
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Pai> query = cb.createQuery(Pai.class);
        Root<Pai> paiRoot = query.from(Pai.class);

        query.select(paiRoot);

        //non ci devono essere pai evento piu recenti di sei mesi
        Subquery<PaiEvento> subquery1 = query.subquery(PaiEvento.class);
        Root<PaiEvento> subRoot1 = subquery1.from(PaiEvento.class);
        subquery1.select(subRoot1).distinct(true);
        Predicate p1 = cb.and(cb.equal(subRoot1.get("codPai"), paiRoot), cb.greaterThanOrEqualTo(subRoot1.<Date>get("tsEvePai"), dt.minusMonths(8).toDate()));
        subquery1.where(p1);

        query.where(cb.not(cb.exists(subquery1)), cb.isNull(paiRoot.get("dtChiusPai")));
        TypedQuery<Pai> typedQuery = getEntityManager().createQuery(query);

        List<Pai> result = typedQuery.getResultList();


        List<Pai> trueResult = new ArrayList<Pai>();
        for (Pai pai : result) {
            boolean aggiungi = true;
            
            //controllo che non ci siano appuntamenti con data di partenza inferiore a sei mesi.
            for (Appuntamento app : pai.getAppuntamentoList()) {
                if (app.getTsIniApp().after(dt.minusMonths(8).toDate())) ;
                {
                    aggiungi = false;
                    break;
                }

            }
            
            //controllo che non ci siano interventi non chiusi oppure con data di chiusura inferiore di sei mesi
            for (PaiIntervento intervento : pai.getPaiInterventoList()) {
                if (intervento.getDtChius() == null || intervento.getDtChius().after(dt.minusMonths(8).toDate())) {
                    aggiungi = false;
                    break;
                }
            }

            //controllo che non ci siano documenti prodotti da meno di sei mesi
            for (PaiDocumento documenti : pai.getPaiDocumentoList()) {
                if (documenti.getDtDoc().after(dt.minusMonths(8).toDate())) {
                    aggiungi = false;
                    break;
                }
            }
            if (aggiungi) {
                trueResult.add(pai);
            }
        }

        for (Pai pai : trueResult) {
            chiusuraAutomaticaSchedulerLogger.debug("chiudiamo il pai {}", pai);

            pai.setDtChiusPai(new Date());
            pai.setFlgStatoPai(Pai.STATO_CHIUSO);
            try {
                update(pai);
            } catch (Exception e) {
                chiusuraAutomaticaSchedulerLogger.error("Errore non sono riuscito a chiudere il PAI n " + pai.getCodPai() + " perchè" + e.getMessage());
            }
        }

        return trueResult;

    }


    public @Nullable
    Pai findAnyLastPai(int codAna) {
        TypedQuery<Pai> query = getEntityManager().createQuery("SELECT p "
                + "FROM Pai p "
                + "WHERE p.codAna.anagrafeSoc.codAna = :codAna ORDER BY p.dtApePai DESC", Pai.class);
        query.setParameter("codAna", codAna);
        query.setMaxResults(1);
        return getSingleResult(query);
    }

    public boolean isOpenPai(int codAna) {
        TypedQuery<Pai> query = getEntityManager().createQuery("SELECT p "
                + "FROM Pai p JOIN p.codAna l "
                + "WHERE l.anagrafeSoc.codAna = :codAna "
                + "AND p.dtChiusPai IS NULL", Pai.class);
        query.setParameter("codAna", codAna);
        return !query.getResultList().isEmpty();
    }

    public Long countAll() {
        return findOne(Long.class, "SELECT COUNT(p) FROM  Pai p");
    }
}
