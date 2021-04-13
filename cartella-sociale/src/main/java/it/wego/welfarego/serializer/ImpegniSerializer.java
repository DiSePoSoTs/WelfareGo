/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.serializer;

import it.wego.welfarego.model.ImpegnoSpesaBean;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.utils.Utils;
import java.math.BigDecimal;
import javax.persistence.EntityManager;

/**
 *
 * @author giuseppe
 */
    @Deprecated
public class ImpegniSerializer {

    private EntityManager em;

    public ImpegniSerializer(EntityManager em) {
        this.em = em;
    }


    /**
     * 
     * @param paiIntervento
     * @param impegno
     * @param anno
     * @return
     * @throws Exception
     * @deprecated use new PaiInterventoMeseDao(em).sumBdgtPrevAndConsPaiIntervento(paiIntervento, impegno, anno);
     */
    @Deprecated
    public BigDecimal getACarico(PaiIntervento paiIntervento, String impegno, int anno) throws Exception {
        return new PaiInterventoMeseDao(em).sumBdgtPrevAndConsPaiIntervento(paiIntervento, impegno, anno);
    }
}
