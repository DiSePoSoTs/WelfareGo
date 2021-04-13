/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.anagrafica;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.CivicoBean;
import it.wego.welfarego.model.json.JSONCivico;
import it.wego.welfarego.model.json.JSONProvincia;
import it.wego.welfarego.persistence.dao.ToponomasticaCiviciDao;
import it.wego.welfarego.persistence.dao.ToponomasticaDao;
import it.wego.welfarego.persistence.entities.Toponomastica;
import it.wego.welfarego.persistence.entities.ToponomasticaCivici;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.CivicoSerializer;
import java.util.Collections;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class CivicoServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        EntityManager em = Connection.getEntityManager();
        try {
            String desCivico = request.getParameter("query");
            String codVia = Strings.emptyToNull(request.getParameter("codVia"));
            String codCivico = Strings.emptyToNull(request.getParameter("codice"));
            JSONCivico json = new JSONCivico();
            ToponomasticaCiviciDao dao = new ToponomasticaCiviciDao(em);
            if (codCivico != null && codVia != null) {
                Toponomastica toponomastica = new ToponomasticaDao(em).findByCodVia(codVia);
                if (toponomastica == null) {
                    json.setCivico(Collections.singletonList(new CivicoBean(codCivico, codCivico))); //fake civico for non-ts
                } else {
                    ToponomasticaCivici civico = toponomastica.getToponomasticaCivici(codCivico);
                    json.setCivico(Collections.singletonList(CivicoSerializer.serialize(civico)));
                }
                json.setTotal(1);
            } else if (codVia != null && desCivico != null) {

                json.setCivico(CivicoSerializer.serialize(dao.findByDesCiviciWithLike(codVia, desCivico)));
                json.setTotal(dao.totalCount());
            } else if (codCivico == null && codVia != null) {
                Toponomastica toponomastica = new ToponomasticaDao(em).findByCodVia(codVia);
                Preconditions.checkNotNull(toponomastica);
                json.setCivico(Lists.newArrayList(Lists.transform(toponomastica.getToponomasticaCiviciList(), new Function<ToponomasticaCivici, CivicoBean>() {
                    public CivicoBean apply(ToponomasticaCivici civico) {
                        return CivicoSerializer.serialize(civico);
                    }
                })));
            }
            else {
            	json.setSuccess(false);
            }
            return json;
        }
        catch(Exception ex){
            getLogger().error(" ", ex);
            JSONCivico json = new JSONCivico();
            json.setSuccess(false);
            return json;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
