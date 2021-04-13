package it.wego.welfarego.serializer;

import it.wego.welfarego.model.CivicoBean;
import it.wego.welfarego.persistence.entities.ToponomasticaCivici;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class CivicoSerializer {

    public static List<CivicoBean> serialize(List<ToponomasticaCivici> civici) {
        List<CivicoBean> beans = new ArrayList<CivicoBean>();
        for (ToponomasticaCivici c : civici) {
            beans.add(serialize(c));
        }
        return beans;
    }

    public static CivicoBean serialize(ToponomasticaCivici civico) {
        CivicoBean bean = new CivicoBean();
        bean.setCodCivico(civico.getToponomasticaCiviciPK().getCodCiv());
        bean.setDesCivico(civico.getDesCiv());
        return bean;
    }
}
