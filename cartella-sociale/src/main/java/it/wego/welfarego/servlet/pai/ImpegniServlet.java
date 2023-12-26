/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.pai;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.json.JSONImpegniSpesa;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.*;
import it.wego.welfarego.persistence.entities.*;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.servlet.SessionConstants;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author giuseppe
 */
public class ImpegniServlet extends JsonServlet {

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

		final EntityManager em = Connection.getEntityManager();
		JSONImpegniSpesa json = new JSONImpegniSpesa();
		try {
			String action = getParameter("action");
			Utenti connectedUser = (Utenti) request.getSession().getAttribute(SessionConstants.CONNECTED_USER);
			List<Map> impegni = Lists.newArrayList();
			String emptyString = getParameter("empty");

			Validate.notNull(connectedUser, "no user logged");
			if (Objects.equal(action, "read")) {

				String codTipInt = Strings.emptyToNull(getParameter("codTipInt"));
				String cntTipint = Strings.emptyToNull(getParameter("cntTipint"));
				String codPai = Strings.emptyToNull(getParameter("codPai"));
				final PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(em);
				BudgetTipoInterventoDao budgetTipoInterventoDao = new BudgetTipoInterventoDao(em);
				BudgetTipoInterventoUotDao budgetTipoInterventoUotDao = new BudgetTipoInterventoUotDao(em);

				Pai pai = new PaiDao(em).findPai(codPai);
				final PaiIntervento paiIntervento = cntTipint == null || pai == null ? null
						: new PaiInterventoDao(em).findByKey(pai.getCodPai(), codTipInt, cntTipint);
				final AtomicReference<Boolean> isACaricoSet = new AtomicReference<Boolean>(false);
				final Function<BudgetTipInterventoUot, Map> serializer = new JsonMapTransformer<BudgetTipInterventoUot>() {

					@Override
					public void transformToMap(BudgetTipInterventoUot budgetTipInterventoUot) {
						Integer anno = Integer.valueOf(budgetTipInterventoUot.getBudgetTipIntervento()
								.getBudgetTipInterventoPK().getCodAnno());
						String impegno = budgetTipInterventoUot.getBudgetTipInterventoUotPK().getCodImpe();
						put("anno", anno.toString());
						put("capitolo", budgetTipInterventoUot.getBudgetTipIntervento().getCodCap());
						put("impegno", impegno);
						put("importoDisponibile",
								paiInterventoMeseDao.calculateBdgtDisp(budgetTipInterventoUot).toString());
						if (budgetTipInterventoUot.getBdgDispOre() == null) {
						} else if (Objects.equal(budgetTipInterventoUot.getBdgDispOre(), BigDecimal.ZERO)) {
							put("unitaDisponibili", BigDecimal.ZERO);
						} else {
							put("unitaDisponibili", budgetTipInterventoUot.getBdgDispOre()
									.subtract(paiInterventoMeseDao.sumBdgtPrevAndConsQta(budgetTipInterventoUot)));
						}
						put("importoComplessivo", budgetTipInterventoUot.getBdgDispEur().toString());
						if (paiIntervento != null
								&& Objects.equal(paiIntervento.getPai().getIdParamUot().getIdParamIndata(),
										budgetTipInterventoUot.getParametriIndataUot().getIdParamIndata())) {
							BigDecimal sumBdgtPrevAndConsPaiIntervento = paiInterventoMeseDao
									.sumBdgtPrevAndConsPaiIntervento(paiIntervento, impegno, anno);
							put("aCarico", sumBdgtPrevAndConsPaiIntervento.toString());
							if (!Objects.equal(sumBdgtPrevAndConsPaiIntervento, BigDecimal.ZERO)) {
								isACaricoSet.set(true);
							}
						}
						put("centroElementareDiCosto",
								new TipologiaInterventoDao(em)
										.findByCodTipint(
												budgetTipInterventoUot.getBudgetTipInterventoUotPK().getCodTipint())
										.getCcele());
						put("uot", budgetTipInterventoUot.getParametriIndataUot().getDesParam());
					}
				};

				final Function<BudgetTipIntervento, Map> serializerBudg = new JsonMapTransformer<BudgetTipIntervento>() {

					@Override
					public void transformToMap(BudgetTipIntervento budgetTipIntervento) {
						Integer anno = Integer.valueOf(budgetTipIntervento.getBudgetTipInterventoPK().getCodAnno());
						String impegno = budgetTipIntervento.getBudgetTipInterventoPK().getCodImpe();
						put("anno", anno.toString());
						put("capitolo", budgetTipIntervento.getCodCap());
						put("impegno", impegno);
						put("importoDisponibile",
								paiInterventoMeseDao.calculateBdgtDisp(budgetTipIntervento).toString());
						if (budgetTipIntervento.getBdgDispOre() == null) {
						} else if (Objects.equal(budgetTipIntervento.getBdgDispOre(), BigDecimal.ZERO)) {
							put("unitaDisponibili", BigDecimal.ZERO);
						} else {
							put("unitaDisponibili", budgetTipIntervento.getBdgDispOre()
									.subtract(paiInterventoMeseDao.sumBdgtPrevAndConsQta(budgetTipIntervento)));
						}
						put("importoComplessivo", budgetTipIntervento.getBdgDispEur().toString());
						if (paiIntervento != null) {
							BigDecimal sumBdgtPrevAndConsPaiIntervento = paiInterventoMeseDao
									.sumBdgtPrevAndConsPaiIntervento(paiIntervento, impegno, anno);
							put("aCarico", sumBdgtPrevAndConsPaiIntervento.toString());
							if (!Objects.equal(sumBdgtPrevAndConsPaiIntervento, BigDecimal.ZERO)) {
								isACaricoSet.set(true);
							}
						}
						put("centroElementareDiCosto",
								new TipologiaInterventoDao(em)
										.findByCodTipint(budgetTipIntervento.getBudgetTipInterventoPK().getCodTipint())
										.getCcele());
						put("uot", "N/A");
					}
				};

				// vediamo
				boolean hasBudgetUot = true;

				ParametriIndata uot = pai == null ? null : pai.getIdParamUot();
				// operazione di trasformazione per visualizzare i budget
				uot = transformUot(uot, budgetTipoInterventoUotDao, codTipInt, new ParametriIndataDao(em));
				if (leggeSoloBudgetUot(connectedUser, em)) {
					// Prendo il budget associato alla UOT che segue l'utente

					if (uot != null) {
						impegni.addAll(Collections2.transform(
								budgetTipoInterventoUotDao.findByCodTipIntIdParamUot(codTipInt, uot.getIdParamIndata()),
								serializer));
					} else {
						getLogger().warn("uot not set for pai {}, unable to retrieve BudgetTipInterventoUot", pai);
					}
				} else {
					if (uot != null && budgetTipoInterventoUotDao
							.findByCodTipIntIdParamUot(codTipInt, uot.getIdParamIndata()).isEmpty()) {
						hasBudgetUot = false;
					}

					if (hasBudgetUot == false) {
						impegni.addAll(Collections2.transform(budgetTipoInterventoDao.findByCodTipint(codTipInt),serializerBudg));
					} else {
						for (BudgetTipIntervento budgetTipIntervento : budgetTipoInterventoDao.findByCodTipint(codTipInt)) {

							impegni.addAll(Collections2.transform(budgetTipIntervento.getBudgetTipInterventoUotList(),serializer));
						}
					}
				}

				String costoPrevistoStr = getParameter("costoPrevisto");
				if (!Strings.isNullOrEmpty(costoPrevistoStr) && !isACaricoSet.get() && pai != null) {

					final String userUot = pai.getIdParamUot().getDesParam();
					if (!impegni.isEmpty() && hasBudgetUot == true) {
						Map<String,Object> defaultImpegnoSpesa = new Ordering<Map<String,Object>>() {

							@Override
							public int compare(Map<String,Object> left, Map<String,Object> right) {
								return ComparisonChain.start()
										.compare(Objects.equal(left.get("uot").toString(), userUot),
												Objects.equal(right.get("uot").toString(), userUot))
										.compare(left.get("anno").toString(), right.get("anno").toString()).result();
							}
						}.max(impegni);
						defaultImpegnoSpesa.put("aCarico", costoPrevistoStr);
					}
					// significa che non sto usando un budget uot ma un budget generico
					// dell'intervento
					else if (!impegni.isEmpty() && hasBudgetUot == false) {
						Map<String,Object> defaultImpegnoSpesa = new Ordering<Map<String,Object>>() {

							@Override
							public int compare(Map<String,Object> left, Map<String,Object> right) {
								return ComparisonChain.start().compare((String) left.get("anno"), (String) right.get("anno")).result();
							}
						}.max(impegni);
						defaultImpegnoSpesa.put("aCarico", costoPrevistoStr);
					}
				}

				json.setSuccess(true);
			}
			if (!Strings.isNullOrEmpty(emptyString) && Boolean.valueOf(emptyString) == true) {
				json.setSuccess(true);
			}
			json.setImpegni(JsonBuilder.newInstance().withSourceData(impegni).withParameters(request.getParameterMap()).buildStoreResponse().getData());
			json.setTotal(impegni.size());
			return json;
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	/**
	 * Metodo per capire se la uot è 3 o 4 e se esiste il budget per queste uot in
	 * caso trasforma la uot in 1 o 2
	 * 
	 * @param Uot
	 * @param bdao
	 * @param codTipint
	 * @return
	 */
	private ParametriIndata transformUot(ParametriIndata uot, BudgetTipoInterventoUotDao bdao, String codTipint,
			ParametriIndataDao pdao) {
		ParametriIndata result = uot;
		ParametriIndata uot1 = pdao.findUot(1);
		ParametriIndata uot2 = pdao.findUot(2);
		if (uot == null) {
			// non fare nulla
			return result;
		}
		// caso uot 3
		if ((uot.getIdParamIndata() == 7)) {
			// se è uot 3, non ha budget mentre uot 1 lo ha
			if ((bdao.findByCodTipIntIdParamUot(codTipint, uot.getIdParamIndata()).isEmpty())
					&& (bdao.findByCodTipIntIdParamUot(codTipint, uot1.getIdParamIndata()).isEmpty() == false)) {
				result = uot1;
			}
		}
		// caso uot 4
		if ((uot.getIdParamIndata() == 8)) {
			// se è uot 4, non ha budget mentre uot 2 lo ha
			if ((bdao.findByCodTipIntIdParamUot(codTipint, uot.getIdParamIndata()).isEmpty())
					&& (bdao.findByCodTipIntIdParamUot(codTipint, uot1.getIdParamIndata()).isEmpty() == false)) {
				result = uot2;
			}
		}

		return result;
	}

	private boolean leggeSoloBudgetUot(Utenti connectedUser, EntityManager em) {
		int tipoUtente = connectedUser.getIdParamLvlAbil().getIdParamIndata().intValue();
		int assSociale = Parametri.getTipoAssistenteSociale(em);
		int coordUot = Parametri.getTipoCoordinatoreUot(em);
		return (tipoUtente == assSociale || tipoUtente == coordUot);

	}
}
