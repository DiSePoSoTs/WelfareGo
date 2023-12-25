package it.wego.welfarego.servlet.famiglia;

import com.google.common.base.*;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.model.json.JSONFamiglia;
import it.wego.welfarego.model.json.JSONFamigliaSingola;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.AnagrafeFamigliaDao;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.VistaAnagrafeDao;
import it.wego.welfarego.persistence.entities.AnagrafeFam;
import it.wego.welfarego.persistence.entities.AnagrafeFamPK;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import it.wego.welfarego.serializer.AnagrafeFamigliaSerializer;
import it.wego.welfarego.serializer.AnagrafeSocSerializer;
import it.wego.welfarego.serializer.FamigliaSerializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.wego.welfarego.services.AnagrafeFamigliaService;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author giuseppe
 */
public class FamigliaServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

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
		String type = request.getParameter("type");
		String action = request.getParameter("action");
		Integer codAnaFam = Strings.isNullOrEmpty(request.getParameter("codAnaFamigliare")) ? null
				: Integer.valueOf(request.getParameter("codAnaFamigliare"));
		Integer codAna = Strings.isNullOrEmpty(request.getParameter("codAnag")) ? null
				: Integer.valueOf(request.getParameter("codAnag"));
		String codAnagComunale = request.getParameter("codAnaComunale");
		PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
		EntityManager em = persistenceAdapter.getEntityManager();
		try {

			if (Objects.equal(action, "write")) {
				persistenceAdapter.initTransaction();
				AnagrafeFamigliaDao famigliaDao = new AnagrafeFamigliaDao(em);
				int codAnag = Integer.valueOf(request.getParameter("codAnag"));
				AnagrafeFamigliaSerializer famigliaSerializer = new AnagrafeFamigliaSerializer(em);
				AnagrafeFam famiglia = null;
				AnagrafeSoc anagrafeSociale = null;
				if (codAnaFam != null) {
					famiglia = famigliaDao.findByKey(codAnag, codAnaFam);
					if (famiglia != null) {
					} else {
						famiglia = famigliaSerializer.serializeAnagrafeFam(request);
						persistenceAdapter.insert(famiglia);
					}

					String redditoStr = request.getParameter("redditoFamiliare");
					if (!Strings.isNullOrEmpty(redditoStr)) {
						AnagrafeSoc anagrafeSocFam = persistenceAdapter.getEntityManager().find(AnagrafeSoc.class,
								codAnaFam);
						anagrafeSocFam.setReddito(new BigDecimal(redditoStr.replace(',', '.')));
						anagrafeSocFam.setDataUpdateReddito(new Date());
					}
					anagrafeSociale = persistenceAdapter.getEntityManager().find(AnagrafeSoc.class, codAnaFam);
				} else if (!StringUtils.isBlank(codAnagComunale)) {
					// é censito sull'anagrafe comunale, quindi devo inserirlo prima inserirlo in
					// quella sociale
					int numeroIndividuale = Integer.valueOf(codAnagComunale);
					anagrafeSociale = copiaVistaInAnagrafe(em, numeroIndividuale);
					AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(em);
					String codAnaCom = codAnagComunale;
					String cf = anagrafeSociale.getCodFisc();
					Preconditions.checkArgument(anagrafeSocDao.findByCodFiscCodAnaCom(cf, codAnaCom) == null,
							"Attenzione! E' già presente in banca dati un utente con lo stesso codice fiscale o lo stesso codice anagrafica comunale");
					anagrafeSocDao.insert(anagrafeSociale);
					famiglia = famigliaSerializer.serializeFamiglia(codAnag, anagrafeSociale, null);
					persistenceAdapter.insert(famiglia);
				}
				if (anagrafeSociale != null) {
					anagrafeSociale
							.setAttivitaLavoroStudio(Strings.emptyToNull(request.getParameter("attivitaLavoroStudio")));
				}
				if (famiglia != null) {
					famiglia.setCodQual(
							em.find(ParametriIndata.class, Integer.valueOf(request.getParameter("codQual"))));
				}
				persistenceAdapter.commitTransaction();
				JSONFamiglia json = new JSONFamiglia();
				json.setSuccess(true);
				json.setMessage("Operazione eseguita correttamente");
				return json;
			} else if (Objects.equal(action, "read")) {
				Function<VistaAnagrafe, Map> vistaAnagrafeSerializer = FamigliaSerializer.getVistaAnagrafeSerializer(em);
				JSONFamiglia json = new JSONFamiglia();
				if (type.equals("anagrafica")) {

					List famiglieBean = getNucleo_familiare_anagrafe_comunale(codAna, em, vistaAnagrafeSerializer);

					json.setTotalCount(famiglieBean.size());
					json.setFamiglia(famiglieBean);
					json.setSuccess(true);
				} else if (type.equals("sociale")) {
					json = serializeJsonFamigliaSociale(codAna, true);
				} else if (type.equals("socialeReferenti")) {
					json = serializeJsonFamigliaSociale(codAna, false);
				}
				// usato solo nella finestra di ricerca per mostrare in anteprima la famiglia
				// sociale
				else if (type.equals("anagraficaRicerca")) {
					String codAnaCom = getParameter("codAnaCom");
					if (codAnaCom != null) {
						VistaAnagrafeDao dao = new VistaAnagrafeDao(em);
						int numeroFamiglia = dao.findByNumeroIndividuale(codAnaCom).getNumeroFamiglia();
						// mi prendo solo quelli che non sono morti
						List famiglieBean = Lists.newArrayList(Iterables
								.transform(dao.findByNumeroFamigliaNonMorti(numeroFamiglia), vistaAnagrafeSerializer));
						json.setTotalCount(famiglieBean.size());
						json.setFamiglia(famiglieBean);
						json.setSuccess(true);
					}

				}
				return json;
			} else if (Objects.equal(action, "readForm")) {
				Map<String,Object> map = null;
				Function<VistaAnagrafe, Map> vistaAnagrafeSerializer = FamigliaSerializer.getVistaAnagrafeSerializer(em);
				if (request.getParameter("codAnagFamiliare") != null) {
					int codAnagFamiliare = Integer.valueOf(request.getParameter("codAnagFamiliare"));
					AnagrafeSocDao dao = new AnagrafeSocDao(em);
					AnagrafeSoc anagrafe = dao.findByCodAna(codAnagFamiliare);
					map = FamigliaSerializer.INSTANCE.serializeAnagrafeSoc(anagrafe, codAna, codAnagFamiliare);
				}
				if (request.getParameter("codAnagComunale") != null) {
					int numeroIndividuale = Integer.valueOf(request.getParameter("codAnagComunale"));
					VistaAnagrafeDao dao = new VistaAnagrafeDao(em);
					VistaAnagrafe anagrafe = dao.findByNumeroIndividuale(numeroIndividuale);
					map = vistaAnagrafeSerializer.apply(anagrafe);
				}
				map.put("codQual", request.getParameter("codQual"));
				JSONFamigliaSingola json = new JSONFamigliaSingola();
				json.setFamiglia(map);
				json.setSuccess(true);
				return json;
			} else if (Objects.equal(action, "delete")) {
				new AnagrafeFamigliaDao(em).removeAnagrafeFam(codAna, codAnaFam);
				JSONFamiglia json = new JSONFamiglia();
				json.setSuccess(true);
				json.setMessage("Operazione eseguita correttamente");
				return json;
			} else if (Objects.equal(action, "copy")) {
				int numeroIndividuale = Integer.valueOf(codAnagComunale);
				AnagrafeSoc anagrafeSocFromAnaCom = copiaVistaInAnagrafe(em, numeroIndividuale);
				AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(em);
				AnagrafeFamigliaDao anagrafeFamigliDao = new AnagrafeFamigliaDao(em);
				String codAnaCom = request.getParameter("codAnaComunale");
				String codAnag = request.getParameter("codAnag");
				String cf = anagrafeSocFromAnaCom.getCodFisc();
				AnagrafeSoc anagrafeSocFromDb = anagrafeSocDao.findByCodFiscCodAnaCom(cf, codAnaCom), anagrafeSocToBind;
				AnagrafeSoc assistito = anagrafeSocDao.findByCodAna(codAnag);
				// controllo che non tentino di associare fra l'utente e se stesso.!!!!
				if (assistito.getCodAnaCom().equals(codAnaCom)) {
					throw new Exception("ATTENZIONE: Una persona non può essere parente di se stessa");
				}
				em.getTransaction().begin();
				if (anagrafeSocFromDb != null) {

					List<AnagrafeFam> myFamily = anagrafeFamigliDao.getAnagrafeFamListMerge(codAnag);
					for (AnagrafeFam membro : myFamily) {
						// FB 3/10 check per vedere che non si accoppi un mamrod ella famiglia comunale
						// con qualcuno già esistente.
						if (membro.getAnagrafeSocTarget().getCodAnaCom().equals(codAnaCom)) {
							throw new Exception("ATTENZIONE: Esiste già questa relazione di parentela.");
						}

					}
					anagrafeSocToBind = anagrafeSocFromDb;
				} else {
					anagrafeSocDao.insert(anagrafeSocFromAnaCom);
					anagrafeSocToBind = anagrafeSocFromAnaCom;
				}
				AnagrafeFam fam = createAnagrafeFam(codAnag, anagrafeSocToBind, em);
				em.merge(fam);
				em.getTransaction().commit();
				JSONFamiglia json = new JSONFamiglia();
				json.setSuccess(true);
				json.setMessage("Operazione eseguita correttamente");
				return json;
			}
			Preconditions.checkArgument(false, "supposed unreacheable");
			return null;
		} catch (Exception ex) {
			getLogger().error("Errore cercando di eseguire una operazione sulla famiglia", ex);
			JSONFamiglia json = new JSONFamiglia();
			json.setSuccess(false);
			json.setMessage(ex.getMessage());
			persistenceAdapter.rollbackTransaction();
			return json;
		} finally {
			persistenceAdapter.close();
		}
	}

	private List getNucleo_familiare_anagrafe_comunale(Integer codAna, EntityManager em, Function<VistaAnagrafe, Map> vistaAnagrafeSerializer) {
		AnagrafeFamigliaService anagrafeFamigliaService = new AnagrafeFamigliaService();
		List<VistaAnagrafe> famigliareAnagrafeComunale = anagrafeFamigliaService.getNucleo_familiare_anagrafe_comunale(em, codAna);

		Iterable<Map> transform = Iterables.transform(famigliareAnagrafeComunale, vistaAnagrafeSerializer);
		return Lists.newArrayList(transform);
	}

	private AnagrafeFam createAnagrafeFam(String codAnag, AnagrafeSoc anagrafeSociale, EntityManager em)
			throws NumberFormatException {
		AnagrafeFam fam = new AnagrafeFam();
		AnagrafeFamPK key = new AnagrafeFamPK();
		key.setCodAna(Integer.valueOf(codAnag));
		key.setCodAnaFam(anagrafeSociale.getCodAna());
		fam.setAnagrafeFamPK(key);
		ParametriIndata parente = getParente(em);
		fam.setCodQual(parente);
		return fam;
	}

	private AnagrafeSoc copiaVistaInAnagrafe(EntityManager em, int numeroIndividuale) throws Exception {
		VistaAnagrafeDao vistaAnagraficaDao = new VistaAnagrafeDao(em);
		VistaAnagrafe anagrafeComunale = vistaAnagraficaDao.findByNumeroIndividuale(numeroIndividuale);
		AnagrafeSocSerializer serializer = new AnagrafeSocSerializer(em);
		AnagrafeSoc anagrafeSociale = serializer.vistaAnagraficaToAnagrafeSoc(anagrafeComunale);
		return anagrafeSociale;
	}

	private JSONFamiglia serializeJsonFamigliaSociale(final int codAna, boolean soloParenti) {
		JSONFamiglia json = new JSONFamiglia();
		PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
		EntityManager entityManager = persistenceAdapter.getEntityManager();
		AnagrafeFamigliaDao anagrafeFamigliaDao = new AnagrafeFamigliaDao(entityManager);
		Predicate<AnagrafeFam> filter = soloParenti == true ? AnagrafeFamigliaDao.getRelazioneDiParentelaFilter()
				: null;
		Iterable<AnagrafeFam> anagrafeFams = anagrafeFamigliaDao.getAnagrafeFamListMerge(codAna, filter);
		json.setFamiglia(Lists.newArrayList(Iterables.transform(anagrafeFams, FamSocTransformerFunction.INSTANCE)));
		json.setSuccess(true);
		json.setTotalCount(Iterables.size((Iterable) json.getFamiglia()));
		persistenceAdapter.close();
		return json;
	}

	private enum FamSocTransformerFunction implements Function<AnagrafeFam, Map<String, Object>> {

		INSTANCE;

		public Map<String, Object> apply(AnagrafeFam input) {
			Map<String, Object> map = FamigliaSerializer.INSTANCE.apply(input.getAnagrafeSocTarget());
			map.put("desQual", input.getCodQual().getDesParam());
			map.put("codQual", input.getCodQual().getIdParamIndata().toString());
			Pai pai = new PaiDao(PersistenceAdapterFactory.getPersistenceAdapter().getEntityManager())
					.findAnyLastPai(input.getAnagrafeSocTarget().getCodAna());
			if (pai != null) {
				if (pai.getFlgStatoPai() == 'C') {
					map.put("flgPai", "Chiuso");
				} else {
					map.put("flgPai", "Aperto");
				}
				map.put("pai", pai.getCodPai().toString());
			} else {
				map.put("flgPai", "No");
			}
			return map;
		}
	}

	private ParametriIndata getParente(EntityManager em) {
		ParametriIndataDao dao = new ParametriIndataDao(em);
		ParametriIndata parente = dao.findByIdParamIndata(Parametri.QUALIFICA_PARENTE);
		return parente;
	}
}
