/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.anagrafica;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.json.JSONProvincia;
import it.wego.welfarego.persistence.dao.ProvinciaDao;
import it.wego.welfarego.persistence.entities.Provincia;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.Collections;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class ProvinciaServlet extends JsonServlet {


	private static final long serialVersionUID = 1L;
	
	public Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        EntityManager em = Connection.getEntityManager();
        try {
            String desProvincia = Strings.emptyToNull(getParameter("query"));
            String codStato = Strings.emptyToNull(getParameter("codStato"));
            String codProvincia = Strings.emptyToNull(getParameter("codice"));
            ProvinciaDao dao = new ProvinciaDao(em);
            JsonBuilder jsonBuilder = JsonBuilder.newInstance().withTransformer(provinciaTransformer).withParameters(getParameters());
            if (codProvincia != null) {
                Provincia provincia = dao.findByCodProv(codProvincia);
                return jsonBuilder.withData(provincia == null ? Collections.emptyList() : Collections.singletonList(provincia)).buildStoreResponse();
            } else if (desProvincia != null) {
                Preconditions.checkNotNull(codStato);
                return jsonBuilder.withData(dao.findByDesProvinciaWithLike(codStato, desProvincia)).buildStoreResponse();
            } else {
                Preconditions.checkArgument(false, "a parameter 'query' or 'codice' must be set");
                return null; //unreacheable
            }
        }
        catch(Exception ex){
            getLogger().error(" ", ex);
            JSONProvincia json = new JSONProvincia();
            json.setSuccess(false);
            return json;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
    private final Function<Provincia, Map<String,Object>> provinciaTransformer = new JsonMapTransformer<Provincia>() {
        @Override
        public void transformToMap(Provincia provincia) {
            put("codProvincia", provincia.getProvinciaPK().getCodProv());
            put("desProvincia", provincia.getDesProv());
        }
    };
}
