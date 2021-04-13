package it.wego.welfarego.serializer;

import it.wego.welfarego.model.ComuneBean;
import it.wego.welfarego.persistence.entities.Comune;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class ComuneSerializer {

    public static List<ComuneBean> serialize(List<Comune> comuni) {
        List<ComuneBean> beans = new ArrayList<ComuneBean>();
        for (Comune c : comuni) {
            beans.add(serialize(c));
        }
        return beans;
    }

    public static ComuneBean serialize(Comune comune) {
        ComuneBean bean = new ComuneBean();
        bean.setCodComune(comune.getComunePK().getCodCom());
        bean.setDesComune(comune.getDesCom());
        return bean;
    }
}
