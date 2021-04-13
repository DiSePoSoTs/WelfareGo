package it.wego.welfarego.serializer;

import it.wego.welfarego.model.ViaBean;
import it.wego.welfarego.persistence.entities.Toponomastica;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class ViaSerializer {

    public static List<ViaBean> serialize(List<Toponomastica> vie) {
        List<ViaBean> beans = new ArrayList<ViaBean>();
        for (Toponomastica v : vie) {
            beans.add(serialize(v));
        }
        return beans;
    }

    public static ViaBean serialize(Toponomastica via) {
        ViaBean bean = new ViaBean();
        bean.setCodVia(via.getToponomasticaPK().getCodVia());
        bean.setDesVia(via.getDesVia());
        return bean;
    }
}
