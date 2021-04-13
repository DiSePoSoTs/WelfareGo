/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.anagrafica;

import it.wego.extjs.servlet.AbstractJsonServlet;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.ComuneBean;
import it.wego.welfarego.model.json.JSONComune;
import it.wego.welfarego.persistence.dao.ComuneDao;
import it.wego.welfarego.persistence.entities.Comune;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.ComuneSerializer;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class ComuneServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, AbstractJsonServlet.Method method) throws Exception {
        EntityManager em = Connection.getEntityManager();
        try {
            String desComune = request.getParameter("query");
            if (desComune != null) {
                desComune = desComune.toLowerCase();
            }

            String codProv = request.getParameter("codProv");
            String codComune = request.getParameter("codice");
            JSONComune json = new JSONComune();
            ComuneDao dao = new ComuneDao(em);
            if (codComune != null && !"".equals(codComune)) {
                List<ComuneBean> comuni = new ArrayList<ComuneBean>();
                int total = dao.totalCount(codComune);
                if (total > 0) {
                    Comune comune = dao.findByCodCom(codComune);
                    comuni.add(ComuneSerializer.serialize(comune));
                    json.setComune(comuni);
                    json.setSuccess(true);
                    json.setTotal(comuni.size());
                } else {
                    ComuneBean comune = new ComuneBean();
                    comune.setDesComune(codComune);
                    comuni.add(comune);
                    json.setComune(comuni);
                    json.setSuccess(true);
                    json.setTotal(comuni.size());
                }
            } else {
                List<Comune> comuni = dao.findByDesComuneWithLike(codProv, desComune);
                List<ComuneBean> statiBean = ComuneSerializer.serialize(comuni);
                int count = dao.totalCount();
                json.setComune(statiBean);
                json.setSuccess(true);
                json.setTotal(count);
            }
            return json;
        }
        catch(Exception ex){
            getLogger().error(" ", ex);
            JSONComune json = new JSONComune();
            json.setSuccess(false);
            return json;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

    }
}
