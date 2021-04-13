/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.anagrafica;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.model.json.JSONRicerca;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Configuration;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.RicercaSerializer;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author giuseppe
 */
public class RicercaServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method)
			throws Exception {
		final EntityManager em = Connection.getEntityManager();
		try {
			String ricercaRapida = Strings.emptyToNull(getParameter("ricercaRapida"));
			String nome = Strings.emptyToNull(getParameter("nome"));
			String cognome = Strings.emptyToNull(getParameter("cognome"));
			String codiceFiscale = Strings.emptyToNull(getParameter("codiceFiscale"));
			String telefono = Strings.emptyToNull(getParameter("telefono"));
			String tipoRicerca = Strings.emptyToNull(getParameter("tipoRicerca"));
			Preconditions.checkNotNull(tipoRicerca);
			String via = Strings.emptyToNull(getParameter("via"));
			String comune = Strings.emptyToNull(getParameter("comune"));
			String exclude = Strings.emptyToNull(getParameter("exclude"));
			String dataNascitaStr = Strings.emptyToNull(getParameter("dataNascita"));
			if (dataNascitaStr != null) {
				Validate.isTrue(dataNascitaStr.matches("[0-9][0-9][/][0-9][0-9][/][0-9][0-9][0-9][0-9]"),
						"formato data non valido (gg/mm/aaaa)");
			}
			int resultLimit = new ConfigurationDao(em).getConfigInt(Configuration.CS_SEARCH_LIMIT, 1000);

			// TODO merge common parts from the two types of search
			if (tipoRicerca.equals("1")) {
				getLogger().debug("searching anagrafe soc");
				List<Condition> conditions = Lists.newArrayList();
				conditions.add(ConditionBuilder.equals("a.flgPersFg", AnagrafeSoc.PERSONA_FISICA_F));
				if (ricercaRapida != null) {
					conditions.add(ConditionBuilder.smartMatchSplit(ricercaRapida, "a.codFisc", "a.nome", "a.cognome",
							"a.codAna", "a.codAnaCom", "a.codAnaFamCom", "pai.codPai"));
				}
				if (codiceFiscale != null) {
					conditions.add(ConditionBuilder.ilike("a.codFisc", codiceFiscale));
				}
				if (nome != null) {
					conditions.add(ConditionBuilder.beginsWith("a.nome", nome));
				}
				if (cognome != null) {
					conditions.add(ConditionBuilder.beginsWith("a.cognome", cognome));
				}
				if (telefono != null) {
					conditions.add(ConditionBuilder.or(ConditionBuilder.ilike("a.numTel", telefono),
							ConditionBuilder.ilike("a.numCell", telefono)));
				}
				if (comune != null) {
					conditions.add(ConditionBuilder.or(
							ConditionBuilder.and(ConditionBuilder.isNotNull("a.luogoResidenza.comune"),
									ConditionBuilder.ilike("a.luogoResidenza.comune.desCom", comune)),
							ConditionBuilder.and(ConditionBuilder.isNotNull("a.luogoResidenza.comuneStr"),
									ConditionBuilder.ilike("a.luogoResidenza.comuneStr", comune))));
				}
				if (via != null) {
					conditions.add(ConditionBuilder.or(
							ConditionBuilder.and(ConditionBuilder.isNotNull("a.luogoResidenza.via"),
									ConditionBuilder.ilike("a.luogoResidenza.via.desVia", via)),
							ConditionBuilder.and(ConditionBuilder.isNotNull("a.luogoResidenza.viaStr"),
									ConditionBuilder.ilike("a.luogoResidenza.viaStr", via))));
				}
				if (dataNascitaStr != null) {
					conditions.add(ConditionBuilder.like("FUNC ('to_char',a.dtNasc,'dd/MM/yyyy')", dataNascitaStr));
				}
				Preconditions.checkArgument(conditions.size() > 1,
						"e' necessario specificare uno o piu' filtri di ricerca");
				if (exclude != null) {
					conditions
							.add(ConditionBuilder.not(ConditionBuilder.equals("a.codAna", Integer.parseInt(exclude))));
				}

				PersistenceAdapter persistenceAdapter = new PersistenceAdapter(em);

				Collection<Integer> codAnaList = Sets.newHashSet(persistenceAdapter.find(Integer.class,
						"SELECT a.codAna FROM AnagrafeSoc a LEFT JOIN a.cartellaSociale cs LEFT JOIN cs.paiList pai",
						conditions));
				Preconditions.checkArgument(codAnaList.size() <= resultLimit,
						"troppi risultati (%s), si prega di affinare i criteri di ricerca", codAnaList.size());

				List<AnagrafeSoc> anagrafeSocList = persistenceAdapter.find(AnagrafeSoc.class,
						"SELECT a FROM AnagrafeSoc a", ConditionBuilder.isIn("a.codAna", codAnaList));

				return JSONRicerca.fromJsonStoreResponse(JsonBuilder.newInstance().withData(anagrafeSocList)
						.withTransformer(new Function<AnagrafeSoc, Object>() {
							public Object apply(AnagrafeSoc anagrafe) {
								try {
									return RicercaSerializer.serializeRisultatiRicerca(anagrafe);
								} catch (RuntimeException ex) {
									getLogger().error("error serializing record {}", anagrafe);
									throw ex;
								}
							}
						}).withParameters(getParameters()).sortEarly(false).buildStoreResponse());
			} else if (tipoRicerca.equals("2")) {
				getLogger().debug("executing search on GDA view");
				List<Condition> conditions = Lists.newArrayList();

				if (nome != null) {
					conditions.add(ConditionBuilder.beginsWith("v.nome", nome));
				}
				if (cognome != null) {
					conditions.add(ConditionBuilder.beginsWith("v.cognome", cognome));
				}
				if (codiceFiscale != null) {
					conditions.add(ConditionBuilder.ilike("v.codiceFiscale", codiceFiscale));
				}
				if (dataNascitaStr != null) {
					conditions. add(ConditionBuilder.ilike("FUNC ('to_char',v.dataNascita,'dd/MM/yyyy')", dataNascitaStr));
				}

				Preconditions.checkArgument(conditions.size() >= 1,	"e' necessario specificare uno o piu' filtri di ricerca");

				PersistenceAdapter persistenceAdapter = new PersistenceAdapter(em);
				Long count = persistenceAdapter.findOne(Long.class, "SELECT COUNT(v) FROM VistaAnagrafe v", conditions);

				Preconditions.checkArgument(count <= resultLimit,
						"troppi risultati (%s), si prega di affinare i criteri di ricerca", count);
				List<VistaAnagrafe> anagrafiche = persistenceAdapter.find(VistaAnagrafe.class,
						"SELECT v FROM VistaAnagrafe v", conditions);
				getLogger().debug("gor {} results", anagrafiche.size());
				JsonBuilder jsonBuilder = JsonBuilder.newInstance().withData(anagrafiche);
				if (ricercaRapida != null) {
					final String pattern = ".*" + ricercaRapida.toUpperCase() + ".*";
					jsonBuilder.withFilter(new Predicate<VistaAnagrafe>() {
						public boolean apply(VistaAnagrafe vistaAnagrafe) {
							return Strings.nullToEmpty(vistaAnagrafe.getNome()).toUpperCase().matches(pattern)
									|| Strings.nullToEmpty(vistaAnagrafe.getCognome()).toUpperCase().matches(pattern)
									|| Strings.nullToEmpty(vistaAnagrafe.getCodiceFiscale()).toUpperCase()
											.matches(pattern)
									|| String.valueOf(vistaAnagrafe.getNumeroIndividuale()).matches(pattern);
						}
					}).filterEarly(true);
				}

				return JSONRicerca
						.fromJsonStoreResponse(jsonBuilder.withTransformer(new JsonMapTransformer<VistaAnagrafe>() {
							@Override
							public void transformToMap(VistaAnagrafe vistaAnagrafe) {
								put("desViaResidenza", vistaAnagrafe.getIndirizzo());
								put("capResidenza", vistaAnagrafe.getCap() != null ? vistaAnagrafe.getCap() : "");
								put("codComuneResidenza", vistaAnagrafe.getCodiceComune());
								put("desComuneResidenza", vistaAnagrafe.getCodiceComune());
								put("codViaResidenza", vistaAnagrafe.getIndirizzo());
								put("codiceFiscale", vistaAnagrafe.getCodiceFiscale());
								put("cognome", vistaAnagrafe.getCognome());
								put("comuneNascita", vistaAnagrafe.getLuogoNascita());
								put("dataMorte", vistaAnagrafe.getDataMorte());
								put("dataNascita", vistaAnagrafe.getDataNascita());
								put("nome", vistaAnagrafe.getNome());
								put("nomeConiuge",
										vistaAnagrafe.getCognomeConiuge() + " " + vistaAnagrafe.getNomeConiuge());
								put("provinciaResidenza", vistaAnagrafe.getCodiceProvincia());
								put("relazione", vistaAnagrafe.getRelazioneParentela());// Ho dei dubbi su come può
																						// essere mappata la relazione
																						// di parentela
								if (vistaAnagrafe.getSesso().equals("1")) {
									put("sesso", "M");
								} else {
									put("sesso", "F");
								}
								put("statoCivile", vistaAnagrafe.getStatoCivile());
								put("tipoAnagrafe", "AN");
								put("codAnagComunale", String.valueOf(vistaAnagrafe.getNumeroIndividuale()));
								put("cittadinanza", vistaAnagrafe.getCittadinanza());
								if (vistaAnagrafe.getCittadinanza().equals("ITALIA")) {
									put("cittadinanza", "100");
								}
								put("posizioneAnagrafica", vistaAnagrafe.getPosizioneAnagrafica());
							}
						}).withParameters(getParameters()).sortEarly(false).buildStoreResponse());
			} else {
				throw new UnsupportedOperationException("unsupported search type : " + tipoRicerca);
			}
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}
}
