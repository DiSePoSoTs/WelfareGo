package it.wego.welfarego.pagamenti.pagamenti.dao;

import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import it.wego.welfarego.persistence.utils.Connection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;



public class PagamentiDao {


    public Mandato getMandato(String id) {
        Query query = Connection.getEntityManager().createNamedQuery("Mandato.findByIdMan");
        query.setParameter("idMan", Integer.parseInt(id));
        return (Mandato) query.getSingleResult();
    }

    public BigDecimal calcolaAssenzeTotali(MandatoDettaglio dettaglio) {
        Query query2 = Connection.getEntityManager().createQuery("SELECT SUM(p.ggAssenza) FROM PaiInterventoMese p WHERE p.paiInterventoMesePK.annoEff = :anno "
                + " AND p.idManDettaglio IS NOT NULL AND p.paiInterventoMesePK.cntTipint = :cntTipint"
                + " AND p.paiInterventoMesePK.codPai = :codPai"
                + " AND p.paiInterventoMesePK.codTipint = :codTipint"
                + " AND p.paiInterventoMesePK.meseEff < :mese");
        query2.setParameter("anno", dettaglio.getPaiInterventoMeseList().get(0).getPaiInterventoMesePK().getAnnoEff());
        query2.setParameter("cntTipint", dettaglio.getPaiInterventoMeseList().get(0).getPaiInterventoMesePK().getCntTipint());
        query2.setParameter("codPai", dettaglio.getPaiInterventoMeseList().get(0).getPaiInterventoMesePK().getCodPai());
        query2.setParameter("codTipint", dettaglio.getPaiInterventoMeseList().get(0).getPaiInterventoMesePK().getCodTipint());
        query2.setParameter("mese", dettaglio.getMeseEff());
        return (BigDecimal) query2.getSingleResult();
    }



}
