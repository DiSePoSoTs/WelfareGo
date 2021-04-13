/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.serializer;

import it.wego.welfarego.model.ProvinciaBean;
import it.wego.welfarego.persistence.entities.Provincia;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giuseppe
 */
@Deprecated
public class ProvinciaSerializer {

    public static List<ProvinciaBean> serialize(List<Provincia> province) {
        List<ProvinciaBean> beans = new ArrayList<ProvinciaBean>();
        for (Provincia p : province) {
            beans.add(serialize(p));
        }
        return beans;
    }

    public static ProvinciaBean serialize(Provincia provincia) {
        ProvinciaBean bean = new ProvinciaBean();
        bean.setCodProvincia(provincia.getProvinciaPK().getCodProv());
        bean.setDesProvincia(provincia.getDesProv());
        return bean;
    }
}
