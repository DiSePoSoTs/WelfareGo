/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.serializer;

import it.wego.welfarego.model.CivilmenteObbligatoBean;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoCivObb;
import it.wego.welfarego.persistence.entities.PaiInterventoCivObbPK;
import java.math.BigDecimal;

/**
 *
 * @author giuseppe
 */
public class PaiInterventoCivObbSerializer {

    public static PaiInterventoCivObb serialize(PaiInterventoCivObb civObb, PaiIntervento intervento, CivilmenteObbligatoBean bean) {
        if (civObb == null) {
            civObb = new PaiInterventoCivObb();
            PaiInterventoCivObbPK key = new PaiInterventoCivObbPK();
            key.setCntTipint(intervento.getPaiInterventoPK().getCntTipint());
            key.setCodAnaCo(bean.getCodAnag());
            key.setCodPai(intervento.getPaiInterventoPK().getCodPai());
            key.setCodTipint(intervento.getPaiInterventoPK().getCodTipint());
            civObb.setPaiInterventoCivObbPK(key);
        }
        civObb.setImpCo(new BigDecimal(bean.getImportoMensile().replace(',','.')));
        return civObb;
    }
}
