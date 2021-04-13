/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.commons.servlet;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.bre.utils.BreMessage;
import it.wego.welfarego.bre.utils.BreUtils;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author aleph
 */
public class TestBreServlet extends JsonServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        setGson(JsonBuilder.getGsonPrettyPrinting());
    }
    
    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method httpMethod) throws Exception {
        String codPai = Strings.emptyToNull(getParameter("codPai")),
                codTipint = Strings.emptyToNull(getParameter("codTipint")),
                cntTipint = Strings.emptyToNull(getParameter("cntTipint")),
                property = Strings.emptyToNull(getParameter("property")),
                method = Strings.emptyToNull(getParameter("method")),
                objects = Strings.emptyToNull(getParameter("objects"));
        boolean reload = getParameter("reload") != null;
        if ((codPai == null || codTipint == null || cntTipint == null) && !reload && objects == null) {
            return "usage: url?codPai=x&codTipint=y&cntTipint=z OR url?reload OR url?codPai=x&codTipint=y&cntTipint=z&method=m OR url?codPai=x&codTipint=y&cntTipint=z&property=p OR url?objects=codTipInt_codPai_cntTipint,...";
        }
        if (reload) {
            getLogger().info("reloading bre");
            BreUtils.reloadAll();
            return JsonBuilder.newInstance().buildResponse();
        } else {
            EntityManager entityManager = Connection.getEntityManager();
            try {
                if (objects != null) {
                    String[] split = objects.split("[^a-zA-Z0-9]");
                    int i, n = split.length / 3;
                    List list = Lists.newArrayList();
                    for (i = 0; i < n; i++) {
                        codTipint = split[i * 3];
                        codPai = split[i * 3 + 1];
                        cntTipint = split[i * 3 + 2];
                        PaiIntervento paiIntervento = new PaiInterventoDao(entityManager).findByKey(Integer.parseInt(codPai), codTipint, cntTipint);
                        Preconditions.checkNotNull(paiIntervento, "no paiIntervento found for key %s-%s:%s", codPai, codTipint, cntTipint);
                        list.add(paiIntervento);
                    }
                    return BreUtils.getBreMessages(list);
                } else {
                    PaiIntervento paiIntervento = new PaiInterventoDao(entityManager).findByKey(Integer.parseInt(codPai), codTipint, cntTipint);
                    Preconditions.checkNotNull(paiIntervento, "no paiIntervento found for key %s-%s:%s", codPai, codTipint, cntTipint);
                    if (method != null) {
                        return Collections.singletonMap(method, MoreObjects.firstNonNull(BeanUtils.findMethodWithMinimalParameters(PaiIntervento.class, method).invoke(paiIntervento), "-null-"));
                    } else if (property != null) {
                        return Collections.singletonMap(property, MoreObjects.firstNonNull(PropertyUtils.getProperty(paiIntervento, property), "-null-"));
                    } else {
                        List<BreMessage> breMessages = BreUtils.getBreMessages(paiIntervento);
                        return breMessages;
                    }
                }
            } finally {
                entityManager.close();
            }
        }
    }
}
