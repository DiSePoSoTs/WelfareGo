/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.serializer;

import it.wego.welfarego.model.CivilmenteObbligatoBean;
import it.wego.welfarego.persistence.entities.PaiInterventoCivObb;

/**
 *
 * @author giuseppe
 */
public class CivilmenteObbligatoSerializer {

    public CivilmenteObbligatoBean serialize(PaiInterventoCivObb civilmenteObbligato) {
        CivilmenteObbligatoBean bean = new CivilmenteObbligatoBean();
        bean.setCntTipInt(String.valueOf(civilmenteObbligato.getPaiInterventoCivObbPK().getCntTipint()));
        bean.setCodAnag(civilmenteObbligato.getPaiInterventoCivObbPK().getCodAnaCo());
        bean.setCodPai(civilmenteObbligato.getPaiInterventoCivObbPK().getCodPai());
        bean.setCodTipInt(civilmenteObbligato.getPaiInterventoCivObbPK().getCodTipint());
        bean.setCodicefiscale(civilmenteObbligato.getAnagrafeSoc().getCodFisc());
        bean.setCognome(civilmenteObbligato.getAnagrafeSoc().getCognome());
        bean.setNome(civilmenteObbligato.getAnagrafeSoc().getNome());
        bean.setImportoMensile(civilmenteObbligato.getImpCo().toString());
        return bean;
    }
}
