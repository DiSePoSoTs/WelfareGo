/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.pai;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import it.wego.conversions.StringConversion;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonStoreResponse;
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.welfarego.model.CronologiaPaiBean;
import it.wego.welfarego.model.json.JSONCronologiaPai;
import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class CronologiaServlet extends JsonServlet {

	@Override
	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method)
			throws Exception {
		EntityManager em = Connection.getEntityManager();
		try {
			JSONCronologiaPai json = new JSONCronologiaPai();
			List<CronologiaPaiBean> cronologia = Lists.newArrayList();
			String action = request.getParameter("action");
			String type = request.getParameter("type");
			if (action != null && "read".equals(action)) {
				if (type != null && type.equals("pai")) {
					String codPaiString = request.getParameter("codPai");
					if (codPaiString != null && !"".equals(codPaiString)) {
						int codPai = Integer.valueOf(request.getParameter("codPai"));
						PaiEventoDao paiEventoDao = new PaiEventoDao(em);
						List<PaiEvento> eventoList = paiEventoDao.findByCodPai(codPai);
						List<CronologiaPaiBean> eventoBeanList = new ArrayList<CronologiaPaiBean>(
								JsonBuilder.newInstance().withData(eventoList).withSorter(request.getParameter("sort"))
										.withTransformer(new Function<PaiEvento, CronologiaPaiBean>() {
											public CronologiaPaiBean apply(PaiEvento evento) {
												return serializer(evento);
											}
										}).buildStoreResponse().getData());
						getLogger().debug("ottenuti {} eventi cronologia totali", eventoBeanList.size());
						Iterator<CronologiaPaiBean> iterator = Lists.reverse(eventoBeanList).iterator();
						boolean lastEventOfTypeAgg = false;
						while (iterator.hasNext()) {
							CronologiaPaiBean thisBean = iterator.next();
							boolean thisEventOfTypeAgg = Objects.equal(thisBean.getDesEvento(), PaiEvento.PAI_UPDATE);
							if (thisEventOfTypeAgg && lastEventOfTypeAgg) {
								iterator.remove();
							}
							lastEventOfTypeAgg = thisEventOfTypeAgg;
						}
						getLogger().debug("ridotti a {} eventi cronologia", eventoBeanList.size());

						JsonStoreResponse jsonStoreResponse = JsonBuilder.newInstance().withParameters(getParameters())
								.withData(eventoBeanList).buildStoreResponse();

						getLogger().debug("restituiamo {} eventi cronologia", jsonStoreResponse.getData().size());

						json.setCronologia(jsonStoreResponse.getData());
						json.setTotal(jsonStoreResponse.getTotal());
						json.setSuccess(true);
						return json;
					} else {
						json.setSuccess(true);
						json.setTotal(0);
						json.setCronologia(new ArrayList<CronologiaPaiBean>());
						return json;
					}
				} else if (type != null && type.equals("intervento")) {
					PaiEventoDao paiEventoDao = new PaiEventoDao(em);
					int codPai = Integer.valueOf(request.getParameter("codPai"));
					int cntTipint = Integer.valueOf(request.getParameter("cntTipint"));
					String codTipint = request.getParameter("codTipint");
					PaiInterventoDao paiInterventoDao = new PaiInterventoDao(em);
					PaiIntervento intervento = paiInterventoDao.findByKey(codPai, codTipint, cntTipint);
					List<PaiEvento> eventi = paiEventoDao.findByPaiIntervento(intervento);
					if (eventi != null) {
						for (PaiEvento evento : eventi) {
							CronologiaPaiBean bean = serializer(evento);
							cronologia.add(bean);
						}
					}
					json.setSuccess(true);
				} else {
					json.setSuccess(false);
				}
			} else {
				json.setSuccess(false);
			}
			json.setCronologia(cronologia);
			return json;
		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	private CronologiaPaiBean serializer(PaiEvento evento) {
		CronologiaPaiBean bean = new CronologiaPaiBean();
		Date data = evento.getTsEvePai();
		bean.setTsEvePai(StringConversion.dateToItString(data));
		bean.setDesEvento(evento.getDesEvento());
		// Potrei avere un pai anziché un pai intervento
		if (evento.getPaiIntervento() != null) {
			bean.setIntervento(evento.getPaiIntervento().getTipologiaIntervento().getDesTipint());
		}
		String operatore = "";
		if (evento.getCodUte() != null) {
			operatore = evento.getCodUte().getCognome() + " " + evento.getCodUte().getNome();
		}
		bean.setOperatore(operatore);
		return bean;
	}
}
