package it.wego.welfarego.servlet.anagrafica;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.Validate;
import static it.wego.persistence.ConditionBuilder.*;
import it.wego.welfarego.persistence.dao.LuogoDao;
import it.wego.welfarego.persistence.entities.Luogo;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author aleph
 */
public class AnagraficaSocServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;
	public static final String SEDE_LEGALE_STATO = "sedeLegaleStato";
	public static final String SEDE_LEGALE_PROVINCIA = "sedeLegaleProvincia";
	public static final String SEDE_LEGALE_COMUNE = "sedeLegaleComune";
	public static final String SEDE_LEGALE_VIA = "sedeLegaleVia";
	public static final String SEDE_LEGALE_CIVICO = "sedeLegaleCivico";
	public static final String SEDE_LEGALE_CAP = "sedeLegaleCap";

	private static enum Action {
		SAVE, SEARCH, LOAD
	}

	@Override
	protected Object handleJsonRequest(HttpServletRequest req, HttpServletResponse resp, Method method)
			throws Exception {
		Action action = getAction(Action.class);
		Map<String, String> data = getData();
		PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
		EntityManager entityManager = persistenceAdapter.getEntityManager();
		LuogoDao luogoDao = new LuogoDao(entityManager);
		AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(entityManager);

		try {
			switch (action) {
			case SAVE: {

				return salva_anagrafica_persona_giuridica(data, luogoDao, anagrafeSocDao, entityManager);
			}
			case SEARCH: {
				String filter = Strings.nullToEmpty(req.getParameter("filter"));
				getLogger().debug("got search request with filter : {}", filter);
				List<AnagrafeSoc> res = persistenceAdapter.find(AnagrafeSoc.class, "SELECT a FROM AnagrafeSoc a",
						and(not(isEqual("a.flgPersFg", AnagrafeSoc.PERSONA_FISICA_F)),
								or(ilike("a.codFisc", filter), ilike("a.partIva", filter), ilike("a.ragSoc", filter))));
				return JsonBuilder.newInstance().withData(res).withParameters(req.getParameterMap())
						.withTransformer(new JsonMapTransformer<AnagrafeSoc>() {
							@Override
							public void transformToMap(AnagrafeSoc obj) {
								put("codiceAnagrafica", obj.getCodAna());
								put("codiceFiscale", obj.getCodFisc());
								put("ragioneSociale", obj.getRagSoc());
								put("partitaIva", obj.getPartIva());
							}
						}).buildStoreResponse();
			}
			case LOAD: {
				String codAna = req.getParameter("codiceAnagrafica");
				getLogger().debug("got load request with codAna : {}", codAna);
				Validate.notEmpty(codAna, "codAna mancante");
				AnagrafeSoc anagrafeSoc = anagrafeSocDao.findByCodAna(Integer.parseInt(codAna));
				Validate.notNull(anagrafeSoc, "record non trovato per codAna = " + codAna);
				return JsonBuilder.newInstance().withData(anagrafeSoc)
						.withTransformer(new JsonMapTransformer<AnagrafeSoc>() {
							@Override
							public void transformToMap(AnagrafeSoc obj) {
								put("cellulare", obj.getNumCell());
								put("email", obj.getEmail());
								put("telefono", obj.getNumTel());
								put("codiceAnagrafica", obj.getCodAna());
								put("codiceFiscale", obj.getCodFisc());
								put("ragioneSociale", obj.getRagSoc());
								put("partitaIva", obj.getPartIva());
								Luogo sedeLegale = obj.getLuogoResidenza();
								put("sedeLegaleCap", sedeLegale.getCap());
								// questo lo facciamo perchè per qualche strano motivo ( bug ? 9 non viene
								// effettuaa la chiamata lato servlet per convertire il cod civico nella sua
								// descrizione )
								put("sedeLegaleCivico", sedeLegale.getCivicoText());
								put("sedeLegaleVia", sedeLegale.getCodVia() != null ? sedeLegale.getCodVia()
										: sedeLegale.getViaText());
								put("sedeLegaleComune", sedeLegale.getCodCom() != null ? sedeLegale.getCodCom()
										: sedeLegale.getComuneText());
								put("sedeLegaleProvincia", sedeLegale.getCodProv() != null ? sedeLegale.getCodProv()
										: sedeLegale.getProvinciaText());
								put("sedeLegaleStato", sedeLegale.getCodStato());
								Luogo sedeOperativa = obj.getLuogoDomicilio();
								put("sedeOperativaCap", sedeOperativa.getCap());
								put("sedeOperativaCivico", sedeOperativa.getCivicoText());
								put("sedeOperativaVia", sedeOperativa.getCodVia() != null ? sedeOperativa.getCodVia()
										: sedeOperativa.getViaText());
								put("sedeOperativaComune", sedeOperativa.getCodCom() != null ? sedeOperativa.getCodCom()
										: sedeOperativa.getComuneText());
								put("sedeOperativaProvincia",
										sedeOperativa.getCodProv() != null ? sedeOperativa.getCodProv()
												: sedeOperativa.getProvinciaText());
								put("sedeOperativaStato", sedeOperativa.getCodStato());
								put("IBAN", obj.getIbanPagam());
							}
						}).buildResponse();
			}
			}
			throw new AssertionError("unreacheable code");
		} catch (Exception ex) {
			persistenceAdapter.rollbackTransaction();
			throw ex;
		} finally {
			persistenceAdapter.close();
		}
	}

	Object salva_anagrafica_persona_giuridica(Map<String, String> data, LuogoDao luogoDao,
			AnagrafeSocDao anagrafeSocDao, EntityManager entityManager) throws Exception {
		getLogger().debug("got save request for data : {}", data);
		AnagrafeSoc anagrafeSoc;
		String codAna = data.get("codiceAnagrafica");
		boolean insertNew = Strings.isNullOrEmpty(codAna);
		if (insertNew) {
			anagrafeSoc = new AnagrafeSoc();
			anagrafeSoc.setFlgPersFg(AnagrafeSoc.PERSONA_FISICA_G);
		} else {
			anagrafeSoc = anagrafeSocDao.findByCodAna(Integer.parseInt(codAna));
			Validate.isTrue(anagrafeSoc != null, "record non trovato per codAna = ", codAna);
			Validate.isTrue(Objects.equal(anagrafeSoc.getFlgPersFg(), AnagrafeSoc.PERSONA_FISICA_G),
					"il codAna = " + codAna + " corrisponde ad una persona fisica");
		}

		anagrafeSoc.setEmail(data.get("email"));
		anagrafeSoc.setIbanPagam(Strings.emptyToNull(data.get("IBAN")));
		anagrafeSoc.setNumTel(data.get("telefono"));
		anagrafeSoc.setPartIva(data.get("partitaIva"));
		anagrafeSoc.setCodFisc(data.get("codiceFiscale"));
		anagrafeSoc.setRagSoc(data.get("ragioneSociale"));
		anagrafeSoc.setNumCell(data.get("cellulare"));

		String errori = null;
		try {
			String sedeLegaleStato = data.get(SEDE_LEGALE_STATO);
			String sedeLegaleProvincia = data.get(SEDE_LEGALE_PROVINCIA);
			String sedeLegaleComune = data.get(SEDE_LEGALE_COMUNE);
			String sedeLegaleVia = data.get(SEDE_LEGALE_VIA);
			String sedeLegaleCivico = data.get(SEDE_LEGALE_CIVICO);
			String sedeLegaleCap = data.get(SEDE_LEGALE_CAP);

			Luogo luogoResidenza = anagrafeSoc.getLuogoResidenza();

			Luogo luogoResidenzaTmp = luogoDao.newLuogo(sedeLegaleStato, sedeLegaleProvincia, sedeLegaleComune,
					sedeLegaleVia, sedeLegaleCivico, sedeLegaleCap);

			if (luogoResidenza != null) {
				luogoResidenza.updateFrom(luogoResidenzaTmp);

				luogoDao.update(luogoResidenza);
			} else {
				luogoDao.insert(luogoResidenza);
			}

			getLogger().debug("residenza: " + luogoResidenza);

			anagrafeSoc.setLuogoResidenza(luogoResidenza);
		} catch (Exception ex) {
			errori = "residenza";
			getLogger().error("", ex);
		}

		try {

			String sedeOperativaStato = data.get("sedeOperativaStato");
			String sedeOperativaProvincia = data.get("sedeOperativaProvincia");
			String sedeOperativaComune = data.get("sedeOperativaComune");
			String sedeOperativaVia = data.get("sedeOperativaVia");
			String sedeOperativaCivico = data.get("sedeOperativaCivico");
			String sedeOperativaCap = data.get("sedeOperativaCap");

			Luogo luogoDomicilio = anagrafeSoc.getLuogoDomicilio();
			Luogo luogoDomicilioTmp = luogoDao.newLuogo(sedeOperativaStato, sedeOperativaProvincia, sedeOperativaComune,
					sedeOperativaVia, sedeOperativaCivico, sedeOperativaCap);

			if (luogoDomicilio != null) {

				luogoDomicilio.updateFrom(luogoDomicilioTmp);

				luogoDao.update(luogoDomicilio);
			} else {
				luogoDao.insert(luogoDomicilio);
			}

			getLogger().debug("domicilio: " + luogoDomicilio);
			anagrafeSoc.setLuogoDomicilio(luogoDomicilio);

		} catch (Exception ex) {
			errori += "   domicilio";
			getLogger().error("", ex);
		}

		if (errori != null) {
			throw new RuntimeException("si sono verificati degli errori nel salvataggio degli indirizzi:" + errori);
		}

		if (insertNew) {
			try {
				anagrafeSocDao.insert(anagrafeSoc);
			} catch (SQLIntegrityConstraintViolationException e) {
				getLogger().warn("", e);
				anagrafeSocDao.checkDuplicateCfPiva(anagrafeSoc);
				throw e;
			}
		} else {
			anagrafeSocDao.update(anagrafeSoc);
		}
		getLogger().debug("saved anagrafeSoc record with codAna = {}", anagrafeSoc.getCodAna());
		Map respData = Collections.singletonMap("codiceAnagrafica", anagrafeSoc.getCodAna());
		return JsonBuilder.newInstance().withData(respData).buildResponse();
	}
}
