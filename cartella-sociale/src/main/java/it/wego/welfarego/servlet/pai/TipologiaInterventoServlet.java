/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.pai;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.trieste.comune.ssc.json.JsonSortInfo;
import it.trieste.comune.ssc.json.JsonStoreResponse;
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class TipologiaInterventoServlet extends JsonServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method)
			throws Exception {
		EntityManager em = Connection.getEntityManager();
		try {
			TipologiaInterventoDao tipologiaInterventoDao = new TipologiaInterventoDao(em);
			ParametriIndataDao parametriIndataDao = new ParametriIndataDao(em);
			List data = Lists.newArrayList();
			String classeIntervento = Strings.emptyToNull(request.getParameter("codClasse"));
			String codice = Strings.emptyToNull(request.getParameter("codice"));
			if (classeIntervento != null) {
				data.addAll(tipologiaInterventoDao.findByClasseEVisibile(
						parametriIndataDao.findByIdParamIndata(Integer.valueOf(classeIntervento))));
			} else if (codice != null) {
				data.addAll(tipologiaInterventoDao
						.findByClasse(tipologiaInterventoDao.findByCodTipint(codice).getIdParamClasseTipint()));
			} else {
				data.addAll(tipologiaInterventoDao.findAll());
			}
			return buildStoreResponse(data);
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	public static JsonStoreResponse buildStoreResponse(List<TipologiaIntervento> tipologiaInterventoList) {
		return JsonBuilder.newInstance().withData(tipologiaInterventoList)
				.withTransformer(new JsonMapTransformer<TipologiaIntervento>() {
					@Override
					public void transformToMap(TipologiaIntervento intervento) {
						put("name", intervento.getDesTipint());
						put("value", intervento.getCodTipint());
						put("impStdCosto", String.valueOf(intervento.getImpStdCosto()));
						put("label", intervento.getIdParamUniMis().getDesParam());
						put("flgFineDurata", intervento.getFlgFineDurata());
						put("codClasse", intervento.getIdParamClasseTipint().getIdParamIndata());
						// FB 03 10 cambiato match in equals per controllo più sicuro
						if (intervento.getCodTipint().equals("AZ016") || intervento.getCodTipint().equals("AZ017")
								|| intervento.getCodTipint().equals("AZ018")) {
							put("maxDurataMesi", 3);
						} else {
							put("maxDurataMesi", 12);
						}
					}
				}).withSorter(JsonBuilder.buildComparator(new JsonSortInfo("name"))).buildStoreResponse();
	}
}
