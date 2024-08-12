/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.anagrafica;

import it.trieste.comune.ssc.servlet.AbstractJsonServlet;
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.welfarego.model.StatoBean;
import it.wego.welfarego.model.json.JSONStato;
import it.wego.welfarego.persistence.dao.StatoDao;
import it.wego.welfarego.persistence.entities.Stato;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.StatoSerializer;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class StatoServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response,
			AbstractJsonServlet.Method method) throws Exception {
		EntityManager em = Connection.getEntityManager();
		try {
			String desStato = getParameter("query");
			if (desStato != null) {
				desStato = desStato.toLowerCase();
			}
			String codStato = getParameter("codice");
			JSONStato json = new JSONStato();
			StatoDao dao = new StatoDao(em);
			if (codStato != null && !"".equals(codStato.trim())) {
				Stato stato = dao.findByCodStato(codStato);
				StatoBean statoBean = StatoSerializer.serialize(stato);
				List<StatoBean> stati = new ArrayList<StatoBean>();
				stati.add(statoBean);
				json.setStato(stati);
				json.setSuccess(true);
				json.setTotal(stati.size());
			} else {
				List<Stato> stati = dao.findByDesStatoWithLike(desStato);
				List<StatoBean> statiBean = StatoSerializer.serialize(stati);
				int count = stati.size();
				json.setStato(statiBean);
				json.setSuccess(true);
				json.setTotal(count);
			}
			return json;
		} catch (Exception ex) {
			getLogger().error(" ", ex);
			JSONStato json = new JSONStato();
			json.setSuccess(false);
			return json;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}
}
