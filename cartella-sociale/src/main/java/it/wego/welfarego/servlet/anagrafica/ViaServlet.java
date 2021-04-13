/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.anagrafica;

import com.google.common.base.Strings;
import it.wego.extjs.servlet.AbstractJsonServlet;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.ViaBean;
import it.wego.welfarego.model.json.JSONVia;
import it.wego.welfarego.persistence.dao.ToponomasticaDao;
import it.wego.welfarego.persistence.entities.Toponomastica;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.ViaSerializer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ID69
 * http-80-11 16 Nov 2018 09:33:35,354 [http-80-11] ERROR ViaServlet  - error
   java.lang.NullPointerException
 controlla anche le altre servlet....
 * @author giuseppe
 */
public class ViaServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, AbstractJsonServlet.Method method) throws Exception {
        EntityManager em = Connection.getEntityManager();
        try {
            JSONVia json = new JSONVia();
            String desVia = getParameter("query");
            String codComune = getParameter("codComune");
            String codVia = getParameter("codice");
            ToponomasticaDao dao = new ToponomasticaDao(em);
            if (!Strings.isNullOrEmpty(codVia)) {
                Toponomastica via = dao.findByCodVia(codVia);
                if (via == null) {
                    json.setVia(Collections.emptyList());
                    json.setSuccess(true);
                    json.setTotal(0);
                } else {
                    List<ViaBean> vie = new ArrayList<ViaBean>();
                    vie.add(ViaSerializer.serialize(via));
                    json.setVia(vie);
                    json.setSuccess(true);
                    json.setTotal(vie.size());
                }
            } else {
                List<Toponomastica> vie = dao.findByDesViaWithLike(codComune, desVia);
                List<ViaBean> vieBean = ViaSerializer.serialize(vie);
                json.setVia(vieBean);
                json.setSuccess(true);
                json.setTotal(vie.size());
            }
            return json;
        }
        catch(Exception ex){
            getLogger().error(" ", ex);
            JSONVia json = new JSONVia();
            json.setSuccess(false);
            return json;
        }
        finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
