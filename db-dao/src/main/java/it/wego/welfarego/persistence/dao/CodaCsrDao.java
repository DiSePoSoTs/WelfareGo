/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.CodaCsr;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author giuseppe
 */
public class CodaCsrDao extends PersistenceAdapter {

    public CodaCsrDao(EntityManager em) {
        super(em);
    }

    public int countCodaCartellaSociale(int codAna, String azione) {
        Query query = getEntityManager().createQuery("SELECT COUNT(p) "
                + "FROM CodaCsr p "
                + "WHERE p.codAna = :codAna "
                + "AND p.codPai is null "
                + "AND p.codTipint is null "
                + "AND p.cntTipint is null "
                + "AND p.azione = :azione "
                + "AND p.dtInscsr is null "
                +"AND p.numeroTentativi < 2");
        query.setParameter("codAna", codAna);
        query.setParameter("azione", azione);
        int count = 0;
        Long result = (Long) query.getSingleResult();
        if (result != null) {
            count = result.intValue();
        }
        return count;
    }

    public int countCodaInterventi(int codAna, int codPai, String codTipint, int cntTipint, String azione) {
        Query query = getEntityManager().createQuery("SELECT COUNT(p) "
                + "FROM CodaCsr p "
                + "WHERE p.codAna = :codAna "
                + "AND p.codPai = :codPai "
                + "AND p.codTipint = :codTipint "
                + "AND p.cntTipint = :cntTipint "
                + "AND p.azione = :azione "
                + "AND p.dtInscsr is null " 
                + "AND p.numeroTentativi < 2");
        query.setParameter("codAna", codAna);
        query.setParameter("codPai", codPai);
        query.setParameter("codTipint", codTipint);
        query.setParameter("cntTipint", cntTipint);
        query.setParameter("azione", azione);
        int count = 0;
        Long result = (Long) query.getSingleResult();
        if (result != null) {
            count = result.intValue();
        }
        return count;
    }

    public List<CodaCsr> findCodaDaEvadere() {
        Query query = getEntityManager().createQuery("SELECT p "
                + "FROM CodaCsr p "
                + "WHERE  p.dtInscsr is null AND p.numeroTentativi < 2 " +
                "  AND (p.dtAvvio is null OR p.dtAvvio <= :dtAvvio) "
                + "ORDER by p.idCoda");
        Date today =  new Date();
        query.setParameter("dtAvvio", today);
        List<CodaCsr> listCsr = query.getResultList();
        return listCsr;
    }
    
}
