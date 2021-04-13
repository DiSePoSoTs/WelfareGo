/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.serializer;

import it.wego.welfarego.model.StatoBean;
import it.wego.welfarego.persistence.entities.Stato;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class StatoSerializer {

    public static List<StatoBean> serialize(List<Stato> stati) {
        List<StatoBean> beans = new ArrayList<StatoBean>();
        if (stati != null) {
            for (Stato s : stati) {
                beans.add(serialize(s));
            }
        }
        return beans;
    }

    public static StatoBean serialize(Stato stato) {
        StatoBean bean = new StatoBean();
        bean.setCodStato(stato.getCodStato());
        bean.setDesStato(stato.getDesStato());
        return bean;
    }
}
