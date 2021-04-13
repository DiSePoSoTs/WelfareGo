package it.wego.welfarego.persistence.dao;

import com.google.common.base.Strings;
import it.wego.extjs.beans.Order;
import java.util.Date;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import static it.wego.persistence.ConditionBuilder.*;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Pai;
import javax.annotation.Nullable;

/**
 *
 * @author giuseppe
 */
public class PaiEventoDao extends PersistenceAdapter implements ORMDao {

    public PaiEventoDao(EntityManager em) {
        super(em);
    }

    public List<PaiEvento> findByCodPai(int codPai) {
        return findByCodPai(codPai, null, null);
    }

    public List<PaiEvento> findByCodPai(int codPai, @Nullable Integer limit, @Nullable Integer offset) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM PaiEvento p JOIN p.codPai l "
                + "WHERE l.codPai = :codPai "
                + "AND p.desEvento <> :desEvento " 
                + "ORDER BY p.tsEvePai DESC");
        query.setParameter("codPai", codPai);
        query.setParameter("desEvento", PaiEvento.AVVIO_ESECUTIVITA_INTERVENTO);
        if (limit != null) {
            query.setMaxResults(limit);
        }
        if (offset != null) {
            query.setFirstResult(offset);
        }
        List<PaiEvento> eventi = query.getResultList();
        return eventi;
    }

    public int countByCodPai(int codPai) {
        Query query = getEntityManager().createQuery("SELECT COUNT(p) "
                + "FROM PaiEvento p JOIN p.codPai l "
                + "WHERE l.codPai = :codPai ");
        query.setParameter("codPai", codPai);
        int count = 0;
        Long result = (Long) query.getSingleResult();
        if (result != null) {
            count = result.intValue();
        }
        return count;
    }

    public PaiEvento findByIdEvento(int idEvento) {
        Query query = getEntityManager().createNamedQuery("PaiEvento.findByIdEvento");
        query.setParameter("idEvento", idEvento);
        PaiEvento evento = (PaiEvento) Connection.getSingleResult(query);
        return evento;
    }

    public List<PaiEvento> findDetermine(String codTipInt, Date dataAvvio,Character... statoInt) {
        String query = "SELECT p FROM PaiEvento p ";
        List<Condition> conditions = new ArrayList<Condition>();
        conditions.add(isEqual("p.flgDxStampa", PaiEvento.FLG_STAMPA_SI));
        if (statoInt != null && statoInt.length > 0) {
            conditions.add(isIn("p.paiIntervento.statoInt", statoInt));
        }
        if (!Strings.isNullOrEmpty(codTipInt)) {
            conditions.add(isEqual("p.paiIntervento.paiInterventoPK.codTipint", codTipInt));
        }
        if(dataAvvio!=null){
        	 conditions.add(isEqual("p.paiIntervento.dtAvvio", dataAvvio));
        }
        return find(PaiEvento.class, query, conditions, null, null, null);
    }

    public List<PaiEvento> findDetermineVariazione(PaiIntervento paiIntervento) {
        return find(PaiEvento.class, "SELECT p FROM PaiEvento p ",
                isEqual("p.flgDxStampa", PaiEvento.FLG_STAMPA_SI),
                isEqual("p.paiIntervento", paiIntervento));
    }

    public List<PaiEvento> findByCodPaiTipIntCntTipInt(int codPai, String codTipint, int cntTipint) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM PaiEvento p "
                + "WHERE p.codPai = :codPai "
                + "AND p.codTipint = :codTipint "
                + "AND p.cntTipint = :cntTipint");
        query.setParameter("codPai", codPai);
        query.setParameter("codTipint", codTipint);
        query.setParameter("cntTipint", cntTipint);
        List<PaiEvento> eventi = query.getResultList();
        return eventi;
    }

    public List<PaiEvento> findByPaiIntervento(PaiIntervento paiIntervento) {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM PaiEvento p "
                + "WHERE p.paiIntervento = :paiIntervento");
        query.setParameter("paiIntervento", paiIntervento);
        List<PaiEvento> eventi = query.getResultList();
        return eventi;
    }

    public List<PaiEvento> findByPaiIntervento(PaiIntervento paiIntervento, String desEvento) {
        return find(PaiEvento.class, "SELECT p FROM PaiEvento p",
                isEqual("p.paiIntervento", paiIntervento),
                isEqual("p.desEvento", desEvento));
    }

    private PaiEvento prepareEvento(String desEvento) {
        PaiEvento paiEvento = new PaiEvento();
        paiEvento.setDesEvento(desEvento);
        paiEvento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
        paiEvento.setTsEvePai(new Date());
        paiEvento.setBreDox("x");
        paiEvento.setPaiDox("x");
        return paiEvento;
    }

    public PaiEvento insertEvento(AnagrafeSoc anagrafeSoc, String desEvento) {
        PaiEvento paiEvento = prepareEvento(desEvento);
        Pai pai = new PaiDao(getEntityManager()).findLastPai(anagrafeSoc.getCodAna());
        if (pai == null) {
            pai = anagrafeSoc.getPaiList().iterator().next();
        }
        if (pai != null) {
            paiEvento.setCodPai(pai);
            paiEvento.setCodUte(pai.getCodUteAs());
        }
        initTransaction();
        getEntityManager().persist(paiEvento);
        commitTransaction();
        return paiEvento;
    }

    public PaiEvento insertEvento(PaiIntervento paiIntervento, String desEvento) {
        PaiEvento paiEvento = prepareEvento(desEvento);
        paiEvento.setPaiIntervento(paiIntervento);
        paiEvento.setCodPai(paiIntervento.getPai());
        paiEvento.setCodUte(paiIntervento.getPai().getCodUteAs());
        initTransaction();
        getEntityManager().persist(paiEvento);
        commitTransaction();
        return paiEvento;
    }

    public List<PaiEvento> findByCodPai(int codPai, int limit, int offset, Order order) {
        if (order != null) {
            Query query = getEntityManager().createQuery("SELECT p "
                    + "FROM PaiEvento p JOIN p.codPai l "
                    + "WHERE l.codPai = :codPai "
                    +"AND p.desEvento <> :desEvento " 
                    + "ORDER BY p." + order.getProperty() + " " + order.getDirection());
            query.setParameter("codPai", codPai);
            query.setParameter("desEvento", PaiEvento.AVVIO_ESECUTIVITA_INTERVENTO);
            query.setMaxResults(limit);
            query.setFirstResult(offset);
            List<PaiEvento> eventi = query.getResultList();
            return eventi;
        } else {
            return findByCodPai(codPai, limit, offset);
        }
    }
}
