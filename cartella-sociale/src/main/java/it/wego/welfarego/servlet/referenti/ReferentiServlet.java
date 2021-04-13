/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.referenti;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.ReferenteBean;
import it.wego.welfarego.model.json.JSONReferenteSingolo;
import it.wego.welfarego.model.json.JSONReferenti;
import it.wego.welfarego.persistence.dao.AnagrafeFamigliaDao;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.VistaAnagrafeDao;
import it.wego.welfarego.persistence.entities.AnagrafeFam;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.AnagrafeFamigliaSerializer;
import it.wego.welfarego.serializer.AnagrafeSocSerializer;
import it.wego.welfarego.serializer.ReferentiSerializer;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class ReferentiServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

// TODO merge with famigliaServlet    
	@Override
	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method)
			throws Exception {
		String action = request.getParameter("action");
		EntityManager em = Connection.getEntityManager();
		try {
			if (Objects.equal(action, "read")) {
				int limit = Integer.valueOf(request.getParameter("limit"));
				int offset = Integer.valueOf(request.getParameter("start"));
				int codAna = Integer.valueOf(request.getParameter("codAnag"));
				return serializeJsonFamigliaSociale(codAna, limit, offset);
			} else if (Objects.equal(action, "readForm")) {
				int codAna = Integer.valueOf(request.getParameter("codAnag"));
				ReferenteBean bean = null;
				if (request.getParameter("codAnagFamiliare") != null) {
					int codAnagFamiliare = Integer.valueOf(request.getParameter("codAnagFamiliare"));
					AnagrafeSocDao dao = new AnagrafeSocDao(em);
					AnagrafeSoc anagrafe = dao.findByCodAna(codAnagFamiliare);
					bean = ReferentiSerializer.serializeAnagrafeSoc(anagrafe, codAna, codAnagFamiliare);
				}
				if (request.getParameter("codAnagComunale") != null) {
					int numeroIndividuale = Integer.valueOf(request.getParameter("codAnagComunale"));
					VistaAnagrafeDao dao = new VistaAnagrafeDao(em);
					VistaAnagrafe anagrafe = dao.findByNumeroIndividuale(numeroIndividuale);
					bean = ReferentiSerializer.serializeVistaAnagrafe(anagrafe);
				}
				JSONReferenteSingolo json = new JSONReferenteSingolo();
				json.setData(bean);
				return json;
			} else if (Objects.equal(action, "write")) {
				em.getTransaction().begin();
				int codAnag = Integer.valueOf(request.getParameter("codAnag"));
				AnagrafeFamigliaDao famigliaDao = new AnagrafeFamigliaDao(em);
				if (request.getParameter("codAnaFamigliare") != null
						&& !"".equals(request.getParameter("codAnaFamigliare").trim())) {
					int codAnaFamigliare = Integer.valueOf(request.getParameter("codAnaFamigliare"));
					AnagrafeFam referente = famigliaDao.findByKey(codAnag, codAnaFamigliare);
					ParametriIndataDao parametriDao = new ParametriIndataDao(em);
					if (referente != null) {
						updateReferente(request, parametriDao, referente);
					} else {
						AnagrafeFamigliaSerializer famigliaSerializer = new AnagrafeFamigliaSerializer(em);
						AnagrafeFam famiglia = famigliaSerializer.serializeAnagrafeFam(request);
						famigliaDao.insert(famiglia);
					}
				}
				if (request.getParameter("codAnaComunale") != null
						&& !"".equals(request.getParameter("codAnaComunale").trim())) {
					// é censito sull'anagrafe comunale, quindi devo inserirlo prima inserirlo in
					// quella sociale
					int numeroIndividuale = Integer.valueOf(request.getParameter("codAnaComunale"));
					VistaAnagrafeDao vistaAnagraficaDao = new VistaAnagrafeDao(em);
					VistaAnagrafe anagrafeComunale = vistaAnagraficaDao.findByNumeroIndividuale(numeroIndividuale);
					AnagrafeSocSerializer serializer = new AnagrafeSocSerializer(em);
					AnagrafeSoc anagrafeSociale = serializer.vistaAnagraficaToAnagrafeSoc(anagrafeComunale);
					AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(em);
					String codAnaCom = request.getParameter("codAnaComunale");
					String cf = anagrafeSociale.getCodFisc();
					AnagrafeSoc a = anagrafeSocDao.findByCodFiscCodAnaCom(cf, codAnaCom);
					if (a != null) {
						throw new Exception(
								"Attenzione! E' già presente in banca dati un utente con lo stesso codice fiscale o lo stesso codice anagrafica comunale");
					} else {
						anagrafeSocDao.insert(anagrafeSociale);
					}

					String qualifica = request.getParameter("qualifica");
					AnagrafeFamigliaSerializer famigliaSerializer = new AnagrafeFamigliaSerializer(em);
					AnagrafeFam famiglia = famigliaSerializer.serializeFamiglia(codAnag, anagrafeSociale, qualifica);
					famigliaDao.insert(famiglia);
				}
				em.getTransaction().commit();
				JSONReferenti json = new JSONReferenti();
				json.setMessage("Operazione eseguita correttamente");
				return json;
			} else if (Objects.equal(action, "delete")) {
				int codAnag = Integer.valueOf(request.getParameter("codAnag"));
				int codAnaFamigliare = Integer.valueOf(request.getParameter("codAnaFamigliare"));
				AnagrafeFamigliaDao dao = new AnagrafeFamigliaDao(em);
				AnagrafeFam fam1 = dao.findByKey(codAnag, codAnaFamigliare);
				AnagrafeFam fam2 = dao.findByKey(codAnaFamigliare, codAnag);
				em.getTransaction().begin();
				if (fam1 != null) {
					em.remove(fam1);
				}
				if (fam2 != null) {
					em.remove(fam2);
				}
				em.getTransaction().commit();
				JSONReferenti json = new JSONReferenti();
				json.setMessage("Operazione eseguita con successo");
				return json;
			} else {
				Preconditions.checkArgument(false, "unknown action : %s", action);
				return null; // unreacheable
			}
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	private void updateReferente(HttpServletRequest request, ParametriIndataDao parametriDao, AnagrafeFam referente)
			throws NumberFormatException {
		String qualificaString = request.getParameter("qualifica");
		if (qualificaString != null && !"".equals(qualificaString.trim())) {
			int id = Integer.valueOf(qualificaString);
			ParametriIndata qualifica = parametriDao.findByIdParamIndata(id);
			referente.setCodQual(qualifica);
		} else {
			ParametriIndata qualifica = parametriDao.findByIdParamIndata(Parametri.QUALIFICA_PARENTE);
			referente.setCodQual(qualifica);
		}
	}

	private JSONReferenti serializeJsonFamigliaSociale(int codAna, int limit, int offset) {
		JSONReferenti json = new JSONReferenti();
		EntityManager em = Connection.getEntityManager();
		List<AnagrafeFam> referenti;
		try {

			referenti = new AnagrafeFamigliaDao(em).getAnagrafeFamListMerge(codAna,
					AnagrafeFamigliaDao.getNotRelazioneDiParentelaFilter());
		} finally {
			em.close();
		}
		List<ReferenteBean> referentiBean = new ArrayList<ReferenteBean>();
		for (AnagrafeFam referente : referenti) {
			ReferenteBean bean = ReferentiSerializer.serializeAnagrafeSoc(referente.getAnagrafeSocTarget(),
					referente.getAnagrafeFamPK().getCodAna(), referente.getAnagrafeFamPK().getCodAnaFam());
			Pai paiReferente = new PaiDao(Connection.getEntityManager())
					.findAnyLastPai(referente.getAnagrafeSocTarget().getCodAna());
			if (paiReferente != null) {
				if (paiReferente.getFlgStatoPai() == 'C') {
					bean.setFlgPai("Chiuso");
				} else {
					bean.setFlgPai("Aperto");
				}
				bean.setPai(paiReferente.getCodPai().toString());
			} else {
				bean.setFlgPai("No");
			}
			bean.setQualifica(
					referente.getCodQual() == null ? null : String.valueOf(referente.getCodQual().getIdParamIndata()));
			bean.setQualificaDes(referente.getCodQual() == null ? "" : referente.getCodQual().getDesParam());
			referentiBean.add(bean);
		}
		json.setReferenti(referentiBean);
		return json;
	}
}
