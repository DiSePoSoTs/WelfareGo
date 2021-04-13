package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.DatiSpecifici;
import it.wego.welfarego.persistence.entities.MapDatiSpecTipint;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiInterventoPK;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author aleph
 */
public class DatiSpecificiDao extends PersistenceAdapter  {

    public DatiSpecificiDao(EntityManager em) {
        super(em);
    }

    public String getValDatiSpecifici(PaiIntervento paiIntervento, String key) {
        PaiInterventoPK paiInterventoPk = paiIntervento.getPaiInterventoPK();
        MapDatiSpecificiIntervento mapDatiSpecifici = getEntityManager().find(MapDatiSpecificiIntervento.class, new MapDatiSpecificiInterventoPK(
                paiInterventoPk.getCodPai(),
                paiInterventoPk.getCodTipint(),
                paiInterventoPk.getCntTipint(),
                key));
        return mapDatiSpecifici == null ? null : mapDatiSpecifici.getValCampo();
    }

    public DatiSpecifici findByCodCampo(String codCampo) {
        Query query = getEntityManager().createNamedQuery("DatiSpecifici.findByCodCampo");
        query.setParameter("codCampo", codCampo);
        DatiSpecifici dato = (DatiSpecifici) Connection.getSingleResult(query);
        return dato;
    }

    public List<DatiSpecifici> findAll() {
        return findAll(null, null);
    }

    public List<DatiSpecifici> findAll(@Nullable Integer limit, @Nullable Integer start) {
        Query query = getEntityManager().createNamedQuery("DatiSpecifici.findAll");

        if (limit != null) {
            query.setMaxResults(limit);
        }
        if (start != null) {
            query.setFirstResult(start);
        }

        List<DatiSpecifici> lista = query.getResultList();
        return lista;
    }

    public int totalCount() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(DatiSpecifici.class)));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    public List<DatiSpecifici> findDisponibili(TipologiaIntervento codTipint) {

        Query query = getEntityManager().createQuery("SELECT d "
                + "FROM DatiSpecifici d "
                + "WHERE d.codCampo NOT IN "
                + "(SELECT d2.codCampo FROM MapDatiSpecTipint m JOIN m.datiSpecifici d2 WHERE m.tipologiaIntervento = :codTipint)");

        query.setParameter("codTipint", codTipint);

        List<DatiSpecifici> lista = query.getResultList();
        return lista;
    }

    public List<MapDatiSpecTipint> findRelazionati(TipologiaIntervento codTipint) {

        Query query = getEntityManager().createQuery("SELECT m "
                + "FROM MapDatiSpecTipint m JOIN m.datiSpecifici d "
                + "WHERE m.tipologiaIntervento = :codTipint");

        query.setParameter("codTipint", codTipint);

        List<MapDatiSpecTipint> lista = query.getResultList();
        return lista;
    }
}