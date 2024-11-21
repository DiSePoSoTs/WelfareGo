package it.wego.welfarego.persistence.dao;

import com.google.common.base.Objects;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import it.wego.persistence.AssignmentBuilder;
import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Associazione;
import it.wego.welfarego.persistence.entities.InterventiAssociati;
import it.wego.welfarego.persistence.entities.ListaAttesa;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MapDatiSpecTipint;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoAnagrafica;
import it.wego.welfarego.persistence.entities.PaiInterventoCivObb;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.entities.UniqueForm;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;
import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import static it.wego.persistence.ConditionBuilder.isEqual;

/**
 * @author giuseppe
 */
public class PaiInterventoDao extends PersistenceAdapter {
	public static final HashMap<String, String> VISIBILI_IN_SOCIAL_CRT = new HashMap<String, String>() {
		{
			put("EC100", "Misura di sostegno al reddito");
			put("EC001", "Contributo Economico Periodico");
			put("EC002", "Contributo Economico Periodico");
			put("EC003", "Contributo Economico Straordinario");
			put("EC004", "Contributo Economico Straordinario");
			put("EC005", "Contributo Economico Periodico");
			put("EC100A", "Contributo Economico");
			put("EC100B", "Reddito di inclusione (REI)");
			put("AD020", "Servizio mensa");
		}

	};
	public static final String DICITURA_PROROGA = "Intervento originario.\n";
	// private final static BigDecimal QUATTRO = new BigDecimal(4);
	public static final BigDecimal SETTIMANE_IN_MESE = new BigDecimal(4);

	/**
	 * DA_RINNOVARE = 1
	 */
	public static final int DA_RINNOVARE = 1;

	/**
	 * RINNOVATO = 0
	 */
	public static final Integer RINNOVATO = 0;
	// Lista di interventi che NON deve essere chiusa se un nuovo intervento dello
	// stesso tipo viene reso esecutivo
	public final List<String> codiciDaNonChiudere = new ArrayList<String>() {

		private static final long serialVersionUID = 1L;

		{
			// contributo straordinario
			add("EC003");
			// cassa a mani
			add("AD009");
			// contributi finalizzati disabili
			add("MI101");

		}

	};
	public final List<String> nonProdurreLetteraChiusura = new ArrayList<String>() {
		{
			// fap apa
			add("AZ008A");
			add("AZ008B");
			// fap caf
			add("AZ007A");
			add("AZ007B");
			// fap svi
			add("AZ009");
			// nuovo fap
			add("AZ010");
		}

	};

	public PaiInterventoDao(EntityManager em) {
		super(em);
	}

	public void insert(PaiIntervento intervento) throws Exception {
		if (intervento.getAssociazione() == null) {
			throw new IllegalArgumentException("associazione mancante per intervento: " + intervento);
		}
		super.insert(intervento);
	}

	public List<Object[]> countInterventi(Integer idAssociazione, String from, String to) {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append(
				" SELECT DISTINCT(T.DES_TIPINT) AS INTERVENTO,P.DES_PARAM AS CLASSE_INTERVENTO,COUNT(*) AS NUM_INTERVENTI, SUM(I.COSTO_PREV) AS SPESA_TOTALE_PER_INTERVENTO FROM PAI_INTERVENTO I ");
		stringBuffer.append(" JOIN TIPOLOGIA_INTERVENTO T ON T.COD_TIPINT=I.COD_TIPINT ");
		stringBuffer.append(" JOIN PARAMETRI_INDATA P ON P.ID_PARAM_INDATA=T.ID_PARAM_CLASSE_TIPINT ");
		stringBuffer.append(" WHERE ");
		stringBuffer.append(" I.DT_AVVIO BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') ");
		stringBuffer.append(" AND I.ID_ASSOCIAZIONE=? ");
		stringBuffer.append(" GROUP BY T.DES_TIPINT,P.DES_PARAM ");
		stringBuffer.append(" ORDER BY 2,1");

		Query query = getEntityManager().createNativeQuery(stringBuffer.toString());
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, idAssociazione);

		List<Object[]> resultList = query.getResultList();
		return resultList;
	}

	public List<PaiIntervento> findByStatus(char status) {
		EntityManager entityManager = getEntityManager();
		Query query = entityManager.createQuery("SELECT p FROM PaiIntervento p WHERE p.statoInt = :statoInt ");
		query.setParameter("statoInt", status);
		List<PaiIntervento> interventi = query.getResultList();
		return interventi;
	}

	/**
	 * Ritorna una lista di interventi dal al per tipologia e associazione (report
	 * social crt)
	 *
	 * @param codTipint
	 * @param idAssocazione
	 * @param from
	 * @param to
	 * @return
	 */
	public List<PaiIntervento> findByCodTipintEAssoziazione(String codTipint, Integer idAssocazione, Date from,
			Date to) {
		String queryString = "SELECT p from PaiIntervento p WHERE p.paiInterventoPK.codTipint = :codTipint "
				+ "AND p.associazione.id= :idAssociazione " + "AND (p.dtApe between :from AND :to)";
		Query query = getEntityManager().createQuery(queryString);
		query.setParameter("codTipint", codTipint);
		query.setParameter("idAssociazione", idAssocazione);
		query.setParameter("from", from);
		query.setParameter("to", to);
		@SuppressWarnings("unchecked")
		List<PaiIntervento> result = query.getResultList();
		return result;

	}

	public Long countInterventiForSocialCrtByUserVisible(Integer codAna) {
		String queryString = "SELECT p from PaiIntervento p WHERE p.pai.rawCodAna =:codAna "
				+ "AND (p.tipologiaIntervento.codTipint IN :codInt OR p.associazione.id!=1 ) "
				+ "AND p.associazione.id IN"
				+ "(SELECT l.associazione.id from Liberatoria l where l.anagrafeSoc.codAna= :codAna) "
				+ "ORDER BY p.dtAvvio DESC";
		Query query = getEntityManager().createQuery(queryString);
		query.setParameter("codAna", codAna);
		query.setParameter("codInt", VISIBILI_IN_SOCIAL_CRT.keySet());
		List<PaiIntervento> result = query.getResultList();

		List<PaiIntervento> filteredResult = filtraInterventiComune(result);

		int size = filteredResult.size();

		return Long.valueOf(size);
	}

	public List<PaiIntervento> findInterventiForSocialCrtByUserVisible(Integer codAna, Integer rows, Integer page) {
		String queryString = "SELECT p from PaiIntervento p WHERE p.pai.rawCodAna =:codAna "
				+ "AND (p.tipologiaIntervento.codTipint IN :codInt OR p.associazione.id!=1 ) "
				+ "AND p.associazione.id IN"
				+ "(SELECT l.associazione.id from Liberatoria l where l.anagrafeSoc.codAna= :codAna) "
				+ "ORDER BY p.dtAvvio DESC";
		Query query = getEntityManager().createQuery(queryString);
		query.setParameter("codAna", codAna);
		query.setParameter("codInt", VISIBILI_IN_SOCIAL_CRT.keySet());
		query.setMaxResults(rows);
		int startPosition = rows * (page - 1);
		query.setFirstResult(startPosition);
		@SuppressWarnings("unchecked")
		List<PaiIntervento> result = query.getResultList();

		List<PaiIntervento> filteredResult = filtraInterventiComune(result);

		return filteredResult;

	}

	private List<PaiIntervento> filtraInterventiComune(List<PaiIntervento> result) {
		System.out.println();
		List<PaiIntervento> filteredResult = new ArrayList<PaiIntervento>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
		try {
			Date primoGennaio2017 = simpleDateFormat.parse("01/01/2017");

			for (PaiIntervento paiIntervento : result) {

				if (isInseritoDalComune(paiIntervento)) {

					if (paiIntervento.getDtApe().before(primoGennaio2017)) {
						// non restituisco a social crt gli interventi del comune prima del il 1/1/2017
					} else {
						paiIntervento.setMotivazione(null);
						filteredResult.add(paiIntervento);
						// non devo mostrare a socialcrt le motivazioni inserite dal comune.
					}
				} else {
					filteredResult.add(paiIntervento);
				}

			}
		} catch (ParseException e) {
			throw new RuntimeException("", e);
		}
		return filteredResult;
	}

	private boolean isInseritoDalComune(PaiIntervento paiIntervento) {
		return paiIntervento.getAssociazione().getId().equals(1) || paiIntervento.getAssociazione() == null;
	}

	/**
	 * Trova tutti gli interventi di un singolo utente che sono visibili per le
	 * varie associazioni
	 *
	 * @param codAna
	 * @return
	 */
	public List<PaiIntervento> findByUserVisible(Integer codAna) {
		String queryString = "SELECT p from PaiIntervento p WHERE p.pai.rawCodAna =:codAna "
				+ "AND (p.tipologiaIntervento.idParamClasseTipint.idParamIndata =:economica OR p.associazione.id != 1 ) "
				+ "AND p.associazione.id IN"
				+ "(SELECT l.associazione.id from Liberatoria l where l.anagrafeSoc.codAna= :codAna) "
				+ "ORDER BY p.dtAvvio DESC";
		Query query = getEntityManager().createQuery(queryString);
		query.setParameter("codAna", codAna);
		query.setParameter("economica", 459);
		@SuppressWarnings("unchecked")
		List<PaiIntervento> result = query.getResultList();
		return result;

	}

	public List<PaiIntervento> findByStatusList(char status, ListaAttesa listaAttesa) {
		Query query = getEntityManager().createQuery(
				"SELECT p FROM PaiIntervento p WHERE p.statoInt = :statoInt AND p.tipologiaIntervento.codListaAtt = :listaAttesa");
		query.setParameter("statoInt", status);
		query.setParameter("listaAttesa", listaAttesa);
		List<PaiIntervento> interventi = query.getResultList();
		return interventi;
	}

	public List<PaiIntervento> findByStatusTipint(char status, TipologiaIntervento tipInt) {
		Query query = getEntityManager().createQuery(
				"SELECT p FROM PaiIntervento p WHERE p.statoInt = :statoInt AND p.tipologiaIntervento = :tipInt");
		query.setParameter("statoInt", status);
		query.setParameter("tipInt", tipInt);
		List<PaiIntervento> interventi = query.getResultList();
		return interventi;
	}

	public List<PaiIntervento> findByCodPai(int codPai, int limit, int offset) {
		Query query = getEntityManager().createQuery("SELECT p " + "FROM PaiIntervento p "
				+ "WHERE p.paiInterventoPK.codPai = :codPai " + "ORDER BY p.dtApe DESC");
		query.setParameter("codPai", codPai);
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		List<PaiIntervento> interventi = query.getResultList();
		return interventi;
	}

	public List<PaiIntervento> findByCodPai(Pai pai, char statoInt) {
		return find(PaiIntervento.class, "SELECT p FROM PaiIntervento p", isEqual("p.pai", pai),
				isEqual("p.statoInt", statoInt));
	}

	public List<PaiIntervento> findByCodPai(String codPai, String orderField, Boolean asc) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PaiIntervento> cq = cb.createQuery(PaiIntervento.class);
        Root<PaiIntervento> c = cq.from(PaiIntervento.class);
        ParameterExpression<String> p = cb.parameter(String.class);
        cq.select(c).where(cb.equal(c.get("paiInterventoPK").get("codPai"), p));
        Order order = asc?cb.asc(c.get(orderField)):cb.desc(c.get(orderField));
        cq.orderBy(order);
        TypedQuery<PaiIntervento> query = getEntityManager().createQuery(cq);
        query.setParameter(p,codPai);
        List<PaiIntervento> results = query.getResultList();
        return results;
	}
	
	public List<PaiIntervento> findByCodPai(String codPai) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(codPai));
		return findByCodPai(Integer.valueOf(codPai));
	}

	@SuppressWarnings("unchecked")
	public List<PaiIntervento> findByCodPai(int codPai) {
		Query query = getEntityManager().createNamedQuery("PaiIntervento.findByCodPaiInterventiComune", PaiIntervento.class);
		query.setParameter("codPai", codPai);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PaiIntervento> findByCodPaiSocialCrt(int codPai) {
		Query query = getEntityManager().createNamedQuery("PaiIntervento.findByCodPaiInterventiEsterni", PaiIntervento.class);
		query.setParameter("codPai", codPai);
		return query.getResultList();
	}

	public int countByCodPai(int codPai) {
		Query query = getEntityManager()
				.createQuery("SELECT COUNT(p) FROM PaiIntervento p WHERE p.paiInterventoPK.codPai = :codPai");
		query.setParameter("codPai", codPai);
		int count = 0;
		Long result = (Long) query.getSingleResult();
		if (result != null) {
			count = result.intValue();
		}
		return count;
	}

	public List<PaiIntervento> findByCodPaiCodTipint(int codPai, String codTipint) {
		TypedQuery<PaiIntervento> query = getEntityManager().createQuery("SELECT p FROM PaiIntervento p "
				+ "WHERE p.paiInterventoPK.codPai = :codPai AND p.paiInterventoPK.codTipint = :codTipint",
				PaiIntervento.class);
		query.setParameter("codPai", codPai);
		query.setParameter("codTipint", codTipint);
		return query.getResultList();
	}

	public List<PaiIntervento> findByCodPaiCodTipintStato(int codPai, String codTipint, char statoInt) {
		TypedQuery<PaiIntervento> query = getEntityManager().createQuery(
				"SELECT p FROM PaiIntervento p WHERE p.paiInterventoPK.codPai = :codPai "
						+ "AND p.paiInterventoPK.codTipint = :codTipint AND p.statoInt = :statoInt",
				PaiIntervento.class);
		query.setParameter("codPai", codPai);
		query.setParameter("codTipint", codTipint);
		query.setParameter("statoInt", statoInt);
		return query.getResultList();
	}

	public PaiIntervento findByKey(Integer codPai, String codTipint, String cntTipint) {
		return findByKey(codPai, codTipint, Integer.valueOf(cntTipint));
	}

	public PaiIntervento findByKey(int codPai, String codTipint, int cntTipint) {
		EntityManager entityManager = getEntityManager();

		return entityManager.find(PaiIntervento.class, new PaiInterventoPK(codPai, codTipint, cntTipint));
	}

	public Number findMaxCnt(String codPai, String codTipint) {
		return findMaxCnt(Integer.parseInt(codPai), codTipint);
	}

	public Number findMaxCnt(int codPai, String codTipint) {
		Query query = getEntityManager()
				.createQuery("SELECT MAX(p.paiInterventoPK.cntTipint) " + "FROM PaiIntervento p "
						+ "WHERE p.paiInterventoPK.codPai = :codPai " + "AND p.paiInterventoPK.codTipint = :codTipint");
		query.setParameter("codPai", codPai);
		query.setParameter("codTipint", codTipint);
		return MoreObjects.firstNonNull((Number) getSingleResult(query), BigInteger.ZERO);
	}

	public Number getACarico(PaiIntervento paiIntervento) {
		return paiIntervento.getQuantita().multiply(new BigDecimal(paiIntervento.getDurMesi()))
				.multiply(paiIntervento.getTipologiaIntervento().getImpStdCosto());
	}

	public PaiIntervento createPaiInterventoDefault(Pai pai) throws Exception {
		AssociazioneDao associazioneDao = new AssociazioneDao(getEntityManager());
		Associazione comuneDiTrieste = associazioneDao.findById(1);
		PaiIntervento intervento = new PaiIntervento();
		intervento.setAssociazione(comuneDiTrieste);
		PaiInterventoPK key = new PaiInterventoPK();
		key.setCntTipint(0);
		key.setCodPai(pai.getCodPai());
		key.setCodTipint(PaiIntervento.INTERVENTO_DEFAULT);
		intervento.setPaiInterventoPK(key);
		intervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
		intervento.setDtApe(new Date());
		intervento.setDtAvvio(new Date());
		intervento.setDtChius(new Date());
		intervento.setDurMesi(0);

		insert(intervento);
		return intervento;
	}

	public void chiudiIntervento(PaiIntervento intervento, String note, String esito, Date dataChiusura)
			throws Exception {
		EntityManager em = getEntityManager();
		PaiInterventoMeseDao dao = new PaiInterventoMeseDao(em);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataChiusura);
		int anno = cal.get(Calendar.YEAR);
		int mese = cal.get(Calendar.MONTH) + 1;
		int codPai = intervento.getPai().getCodPai();
		String codTipint = intervento.getPaiInterventoPK().getCodTipint();
		int cntTipint = intervento.getPaiInterventoPK().getCntTipint();
		dao.findAndClosePimNonAncoraConsuntivati(codPai, codTipint, cntTipint, mese, anno);
		dao.chiudiProposteIntervento(intervento);
		intervento.setNoteChius(note);
		intervento.setIndEsitoInt(esito);
		intervento.setDtChius(dataChiusura);
		intervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
		intervento.setStatoAttuale(PaiIntervento.CHIUSO);
	}

	public void setDataRichiestaApprovazionePerInterventiPendenti(Date date, Pai pai) {
		initTransaction();
		executeUpdate(PaiIntervento.class, "UPDATE PaiIntervento pi",
				ConditionBuilder.and(ConditionBuilder.equals("pi.pai", pai),
						ConditionBuilder.isIn("pi.statoInt", PaiIntervento.STATO_INTERVENTO_APERTO,
								PaiIntervento.STATO_INTERVENTO_RIMANDATO),
						ConditionBuilder.isIn("pi.statoAttuale", PaiIntervento.APERTO, PaiIntervento.RIMANDATO)),

				AssignmentBuilder.assign("pi.dataRichiestaApprovazione", date),
				AssignmentBuilder.assign("pi.statoAttuale", PaiIntervento.IN_APPROVAZIONE));

		commitTransaction();
	}

	/**
	 * Ritorna tutti gli interventi che devono essere sottoposti al coordinatore sia
	 * che siano nuovi ( data richiesta approvazione nulla ) sia che siano stati
	 * rimandati quindi da risottoporre
	 *
	 * @param pai
	 * @return
	 */
	public List<PaiIntervento> getInterventiDaApprovare(Pai pai) {
		List<PaiIntervento> paiInterventos = find(PaiIntervento.class, "SELECT pi FROM PaiIntervento pi",
				ConditionBuilder.and(ConditionBuilder.equals("pi.pai", pai),
						ConditionBuilder.isIn("pi.statoInt", PaiIntervento.STATO_INTERVENTO_APERTO,
								PaiIntervento.STATO_INTERVENTO_RIMANDATO),
						ConditionBuilder.isIn("pi.statoAttuale", PaiIntervento.APERTO, PaiIntervento.RIMANDATO))
		);
		return paiInterventos;
	}

	public List<PaiIntervento> getLastRichiestaApprovazioneBatch(Pai pai) {
		return find(PaiIntervento.class, "SELECT pi FROM PaiIntervento pi", ConditionBuilder.equals("pi.pai", pai),
				ConditionBuilder.isNotNull("pi.dataRichiestaApprovazione"), ConditionBuilder.isTrue(
						"pi.dataRichiestaApprovazione = (SELECT MAX(pi2.dataRichiestaApprovazione) FROM PaiIntervento pi2 WHERE pi2.pai=pi.pai)"));
	}


	public Long countAll() {
		return findOne(Long.class, "SELECT COUNT(pi) FROM  PaiIntervento pi");
	}

	public PaiIntervento findLastByCodPai(Integer codPai) {
		List<PaiIntervento> list = findByCodPai(codPai);
		return list.isEmpty() ? null : Collections.max(list, new Comparator<PaiIntervento>() {
			public int compare(PaiIntervento o1, PaiIntervento o2) {
				return ComparisonChain.start().compare(o1.getDtApe(), o2.getDtApe()).result();
			}
		});
	}

	public List<PaiIntervento> findAll() {
		return find(PaiIntervento.class, "SELECT p FROM PaiIntervento p");
	}

	public PaiIntervento clonaIntervento(String codPai, String codTipint, String cntTipint) throws Exception {
		PaiIntervento clone = findByKey(Integer.parseInt(codPai), codTipint, cntTipint);
		Preconditions.checkNotNull(clone);

		getEntityManager().detach(clone);

		final Integer newCntTipint = findMaxCnt(codPai, codTipint).intValue() + 1;
		clone.getPaiInterventoPK().setCntTipint(newCntTipint);

		clone.setDtApe(new Date());
		clone.setDtAvvio(new Date());
		clone.setDataAvvioProposta(new Date());
		clone.setDtChius(null);
		clone.setDtEsec(null);
		clone.setDataRichiestaApprovazione(null);
		clone.setNumDetermina(null);
		clone.setIndEsitoInt(null);
		clone.setNoteChius(null);
		clone.setDtFine(null);
		clone.setDurMesi(0);
		clone.setDurSettimane(null);
		clone.setDsCodAnaRich(null);
		clone.setDatiOriginali(null);
		clone.setStatoAttuale(PaiIntervento.APERTO);
		clone.setQuantita(BigDecimal.ZERO);
		clone.setIdCsr(null);
		clone.setPaiInterventoMeseList(null);
		clone.setPaiInterventoAnagraficaList(null);
		clone.setPaiInterventoCivObbList(null);
		clone.setInterventiFigli(null);
		clone.setInterventoPadre(null);

		clone.setStatoInt(PaiIntervento.STATO_INTERVENTO_APERTO);

		for (MapDatiSpecificiIntervento datiSpecifici : clone.getMapDatiSpecificiInterventoList()) {
			getEntityManager().detach(datiSpecifici);
			datiSpecifici.getMapDatiSpecificiInterventoPK().setCntTipint(newCntTipint);
			getEntityManager().persist(datiSpecifici);
		}
		// copy entity fk

		PropertyUtils.describe(clone).entrySet().forEach(p -> {
			Object value = p.getValue();
			if (value != null && (value instanceof ParametriIndata || value instanceof AnagrafeSoc) && PropertyUtils.isWriteable(clone, p.getKey().toString())) {
				try {
					PropertyUtils.setProperty(clone, p.getKey().toString(), value);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// TODO copy paiInterventoMese

		getEntityManager().persist(clone);

		return clone;
	}

	public PaiIntervento clonaInterventoPerFamiliare(PaiIntervento paiInterventoPadre, Pai paiFiglio)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		String codTipint = paiInterventoPadre.getPaiInterventoPK().getCodTipint();
		int cntTipint = findMaxCnt(paiFiglio.getCodPai(), codTipint).intValue() + 1;
		PaiIntervento clone = new PaiIntervento(paiFiglio.getCodPai(), codTipint, cntTipint);
		clone.setPai(paiFiglio);
		clone.setDtApe(new Date());
		clone.setDtAvvio(new Date());
		clone.setDataAvvioProposta(new Date());
		clone.setDtChius(null);
		clone.setDtEsec(null);
		clone.setDataRichiestaApprovazione(null);
		clone.setNumDetermina(null);
		clone.setIndEsitoInt(null);
		clone.setNoteChius(null);
		clone.setDtFine(null);
		clone.setDurMesi(0);
		clone.setDurSettimane(null);
		clone.setDsCodAnaRich(null);
		clone.setDatiOriginali(null);
		clone.setStatoAttuale(PaiIntervento.APERTO);
		clone.setQuantita(BigDecimal.ZERO);
		clone.setIdCsr(null);
		clone.setPaiInterventoMeseList(null);
		clone.setPaiInterventoAnagraficaList(null);
		clone.setPaiInterventoCivObbList(null);
		clone.setMapDatiSpecificiInterventoList(null);
		clone.setPaiDocumentoList(null);
		clone.setPaiEventoList(null);
		clone.setAssociazione(paiInterventoPadre.getAssociazione());
		clone.setTariffa(paiInterventoPadre.getTariffa());

		clone.setStatoInt(PaiIntervento.STATO_INTERVENTO_APERTO);
		return clone;
	}

	/**
	 * Metodo che cancella un intervento
	 *
	 * @param intervento
	 * @throws Exception
	 */
	public void deleteIntervento(PaiIntervento intervento) throws Exception {

		// per prima cosa cancelliamo gli interventi mese
		for (PaiInterventoMese pim : intervento.getPaiInterventoMeseList()) {
			delete(pim);
		}
		// cancello i task associati
		for (UniqueTasklist task : intervento.getUniqueTasklistList()) {

			delete(task);

		}
		// poi via lo storico
		for (PaiEvento pem : intervento.getPaiEventoList()) {
			delete(pem);
		}
		// cancello i documenti
		for (PaiDocumento pd : intervento.getPaiDocumentoList()) {
			delete(pd);
		}
		// cancello i civilmente obbiligati
		for (PaiInterventoCivObb civ : intervento.getPaiInterventoCivObbList()) {
			delete(civ);
		}

		for (PaiInterventoAnagrafica pia : intervento.getPaiInterventoAnagraficaList()) {
			delete(pia);
		}

		InterventiAssociati ia = intervento.getInterventoPadre();
		if (ia != null) {
			delete(ia);
		}
		delete(intervento);
	}

	/**
	 * Prende il vecchio intervento e ne restituisce uno di tipo diverso con i dati
	 * specifici del vecchio
	 *
	 * @param oldIntervento
	 * @param codiceNuovo
	 * @return
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public PaiIntervento trasformaIntervento(PaiIntervento oldIntervento, String codiceNuovo) throws Exception {
		Integer codPai = oldIntervento.getPaiInterventoPK().getCodPai();
		int cntTipint = findMaxCnt(codPai, codiceNuovo).intValue() + 1;

		PaiIntervento newIntervento = new PaiIntervento(codPai, codiceNuovo, cntTipint);
		newIntervento.setAssociazione(oldIntervento.getAssociazione());
		newIntervento.setDsCodAnaBenef(oldIntervento.getDsCodAnaBenef());
		newIntervento.setDsCodAnaRich(oldIntervento.getDsCodAnaRich());
		newIntervento.setDataAvvioProposta(oldIntervento.getDataAvvioProposta());
		newIntervento.setDtApe(new Date());
		newIntervento.setDataRichiestaApprovazione(new Date());
		newIntervento.setMotivazione(oldIntervento.getMotivazione());
		newIntervento.setIdCsr(null);

		newIntervento.setDataFineIndicativa(oldIntervento.getDataFineIndicativa());
		newIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_APERTO);

		newIntervento.setIbanDelegato(oldIntervento.getIbanDelegato());
		MapDatiSpecTipIntDao dao = new MapDatiSpecTipIntDao(getEntityManager());
		MapDatiSpecificiInterventoDao pdao = new MapDatiSpecificiInterventoDao(getEntityManager());
		List<MapDatiSpecTipint> datiSpecificiNuovi = dao.findByCodTipInt(codiceNuovo);
		List<MapDatiSpecTipint> datiSpecificiVecchi = dao
				.findByCodTipInt(oldIntervento.getPaiInterventoPK().getCodTipint());
		List<MapDatiSpecificiIntervento> datiSpecificiConvertiti = new ArrayList<MapDatiSpecificiIntervento>();
		for (MapDatiSpecTipint nuovo : datiSpecificiNuovi) {
			MapDatiSpecificiIntervento daConvertire = new MapDatiSpecificiIntervento(codPai,
					newIntervento.getPaiInterventoPK().getCodTipint(), cntTipint,
					nuovo.getMapDatiSpecTipintPK().getCodCampo());
			for (MapDatiSpecTipint vecchio : datiSpecificiVecchi) {

				if (nuovo.getMapDatiSpecTipintPK().getCodCampo()
						.equals(vecchio.getMapDatiSpecTipintPK().getCodCampo())) {

					if (oldIntervento.getValDatoSpecifico(daConvertire.getCodCampo()) != null) {
						daConvertire.setValCampo(oldIntervento.getValDatoSpecifico(daConvertire.getCodCampo()));
					}
					daConvertire.setCodValCampo(oldIntervento.getCodValDatoSpecifico(daConvertire.getCodCampo()));

				}

			}
			datiSpecificiConvertiti.add(daConvertire);

		}

		newIntervento.setMapDatiSpecificiInterventoList(datiSpecificiConvertiti);
		getEntityManager().persist(newIntervento);
		return newIntervento;
	}

	/**
	 * chiude forzatamente eventuale intervento precedente a quello in argomento con
	 * lo stesso tipo
	 *
	 * @param paiIntervento
	 */
	public void chiudiInterventoPrecedenteStessoTipo(final PaiIntervento paiIntervento) throws Exception {
		List<PaiIntervento> daChiudere = new ArrayList<PaiIntervento>();
		for (PaiIntervento p : paiIntervento.getPai().getPaiInterventoList()) {
			// controllo che non sia lo stesso intervento
			if (!p.equals(paiIntervento)) {
				// controllo sulla tipologia di intervento e sul suo stato
				if (p.getTipologiaIntervento().equals(paiIntervento.getTipologiaIntervento())
						&& (p.getStatoInt() == PaiIntervento.STATO_INTERVENTO_APERTO
								|| p.getStatoInt() == PaiIntervento.STATO_INTERVENTO_ESECUTIVO))
				// controllo sulle date
				{
					if ((paiIntervento.getDtAvvio().after(p.getDtAvvio())
							|| paiIntervento.getDtAvvio().equals(p.getDtAvvio()))
							&& paiIntervento.getDtAvvio().before(p.calculateDtFine())) {
						daChiudere.add(p);
					}

				}
			}
		}
		for (PaiIntervento vecchioIntervento : daChiudere) {
			getLogger().info("chiudiamo intervento per esecutivita' intervento duplicato : {}", vecchioIntervento);
			chiudiIntervento(vecchioIntervento, "aperto nuovo intervento sostitutivo", null,
					new LocalDateTime(paiIntervento.getDtAvvio()).dayOfYear().addToCopy(-1).toDate());
			Template template = paiIntervento.getTipologiaIntervento().getCodTmplChius();
			if (!nonProdurreLetteraChiusura.contains(paiIntervento.getPaiInterventoPK().getCodTipint())) {
				new TaskDao(getEntityManager()).withPaiIntervento(paiIntervento)
						.withForm(getEntityManager().getReference(UniqueForm.class,
								UniqueForm.COD_FORM_DOCUMENTO_CHIUSURA))
						.withRuolo(Utenti.OPERATORE_SEDE_CENTRALE.name())
						.withUot(paiIntervento.getPai().getIdParamUot().getIdParam().getCodParam())
						.withDesTask("Predisposizione documento chiusura anticipata intervento").withTemplate(template)
						.insertNewTask();

			}
		}
	}

	public void passaInterventoInStatoEsecutivo(PaiIntervento paiIntervento) throws Exception {
		paiIntervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_ESECUTIVO);
		if (!codiciDaNonChiudere.contains(paiIntervento.getPaiInterventoPK().getCodTipint())) {
			chiudiInterventoPrecedenteStessoTipo(paiIntervento);
		}
		if (paiIntervento.getPaiInterventoPK().getCodTipint().equals("EC100")) {
			if (paiIntervento.getDsCodAnaBenef().getNumCell() == null) {
				creaTaskProduzioneLetteraAssegnazioneContributo(paiIntervento);
			}

		} else {
			creaTaskProduzioneLetteraAssegnazioneContributo(paiIntervento);
		}

		// questo deve esserci solo per quegli interventi che hanno durata mensile e non
		// dal al.
		if (paiIntervento.getDtFine() == null) {
			convertiQuantitaSettimanali(paiIntervento);
		}
		paiIntervento.setStatoAttuale(PaiIntervento.GESTIONE_ECONOMICA);
	}

	private TaskDao predisponiTaskGenerazioneDocumento(PaiIntervento paiIntervento) throws Exception {
		return new TaskDao(getEntityManager()).withPaiIntervento(paiIntervento)
				.withForm(getEntityManager().getReference(UniqueForm.class, UniqueForm.COD_FORM_GENERAZIONE_DOCUMENTO))
				.withRuolo(Utenti.OPERATORE_UOT_O_SEDE_CENTRALE.name())
				.withUot(paiIntervento.getPai().getIdParamUot().getIdParam().getCodParam());
	}

	public void creaTaskProduzioneLetteraAssegnazioneContributo(PaiIntervento paiIntervento) throws Exception {
		Template template = paiIntervento.getTipologiaIntervento().getCodTmplLettPag();
		String desc = "Produzione lettera di assegnazione contributo";
		if (template == null) {
			getLogger().debug("niente task {}, template nullo per int = {}", desc, paiIntervento);
			return;
		}
		predisponiTaskGenerazioneDocumento(paiIntervento).withTemplate(template).withDesTask(desc)
				.withTaskExtraParameter(UniqueTasklist.EXTRA_PARAM_STAMPA_MINUTA, Boolean.TRUE.toString())
				.insertNewTask();
	}

	/**
	 * Ritorna il totale del costo delle proroghe per un singolo intervento su un
	 * singolo budget ( proroghe non ancora scalate)
	 *
	 * @param codImpProroga
	 * @param codTipint
	 * @return
	 */
	public BigDecimal sumCostoProrghe(String codImpProroga, String codTipint, @Nullable Integer idUot) {
		BigDecimal result = BigDecimal.ZERO;
		Query query = getEntityManager()
				.createQuery("SELECT P " + "FROM PaiIntervento p " + "WHERE p.paiInterventoPK.codTipint = :codTipint"
						+ " AND p.codImpProroga = :codImpProroga" + " AND p.rinnovato=1");

		query.setParameter("codTipint", codTipint);
		query.setParameter("codImpProroga", codImpProroga);
		List<PaiIntervento> pis = query.getResultList();
		for (PaiIntervento pi : pis) {
			if (idUot == null) {
				BigDecimal costo = BigDecimal.ZERO;
				if (pi.getDurMesiProroga() != null) {
					costo = pi.getQuantita().multiply(new BigDecimal(pi.getDurMesiProroga()));
				} else {
					Integer numeroGiorni = Days
							.daysBetween(new DateTime(pi.getDtFine()).plusDays(1), new DateTime(pi.getDtFineProroga()))
							.getDays();
					costo = pi.getQuantita().multiply(new BigDecimal(numeroGiorni));
				}
				result = result.add(costo);
			} else {
				if (pi.getPai().getIdParamUot().getIdParamIndata().equals(idUot)) {

					BigDecimal costo = BigDecimal.ZERO;
					if (pi.getDurMesiProroga() != null) {
						costo = pi.getQuantita().multiply(new BigDecimal(pi.getDurMesiProroga()));
					} else {
						Integer numeroGiorni = Days.daysBetween(new DateTime(pi.getDtFine()).plusDays(1),
								new DateTime(pi.getDtFineProroga())).getDays();
						costo = pi.getQuantita().multiply(new BigDecimal(numeroGiorni));
					}
					result = result.add(costo);
				}
			}
		}

		return result;
	}

	public void creaTaskProduzioneLetteraComunicazoneMandato(Mandato mandato) throws Exception {
		PaiIntervento paiIntervento = mandato.getPaiIntervento();
		Template template = paiIntervento.getTipologiaIntervento().getCodTmplComliq();
		String desc = "Produzione lettera di comunicazione mandato";
		if (template == null) {
			getLogger().debug("niente task {}, template nullo per int = {}", desc, paiIntervento);
			return;
		}
		if (!Objects.equal(paiIntervento.getTipologiaIntervento().getFlgFatt(), TipologiaIntervento.FLG_FATTURA_N)) { // intervento
																														// da
																														// fattura
			getLogger().debug("niente task {}, l'intervento {} non produce mandato", desc, paiIntervento);
			return;
		}
		predisponiTaskGenerazioneDocumento(paiIntervento).withTemplate(template).withDesTask(desc)
				.withTaskExtraParameter("idMandato", mandato.getIdMan().toString()).insertNewTask();
	}

	private void convertiQuantitaSettimanali(PaiIntervento paiIntervento) {
		if (Parametri.UNITA_MISURA_SETTIMANALI.contains(paiIntervento.getIdParamUniMis().getIdParam().getCodParam())) {// se
																														// ore
																														// settimanali
			if (paiIntervento.shouldUseSettimane()) {
				getLogger().debug(
						"convertiamo ore settimanali in ore mensili per intervento {} passato in esecutivo con la seguente quantità di settimane "
								+ paiIntervento.getDurSettimane(),
						paiIntervento);
				for (PaiInterventoMese paiInterventoMese : paiIntervento.getPaiInterventoMeseList()) {
					paiInterventoMese.setBdgPrevQta(paiInterventoMese.getBdgPrevQta() == null ? null
							: paiInterventoMese.getBdgPrevQta()
									.multiply(new BigDecimal(paiIntervento.getDurSettimane())));
					paiInterventoMese.setBdgConsQta(paiInterventoMese.getBdgConsQta() == null ? null
							: paiInterventoMese.getBdgConsQta()
									.multiply(new BigDecimal(paiIntervento.getDurSettimane())));
					paiInterventoMese.setBdgConsQtaBenef(paiInterventoMese.getBdgConsQtaBenef() == null ? null
							: paiInterventoMese.getBdgConsQtaBenef()
									.multiply(new BigDecimal(paiIntervento.getDurSettimane())));
				}
			}
			getLogger().debug("convertiamo ore settimanali in ore mensili per intervento {} passato in esecutivo",
					paiIntervento);
			for (PaiInterventoMese paiInterventoMese : paiIntervento.getPaiInterventoMeseList()) {
				paiInterventoMese.setBdgPrevQta(paiInterventoMese.getBdgPrevQta() == null ? null
						: paiInterventoMese.getBdgPrevQta().multiply(SETTIMANE_IN_MESE));
				paiInterventoMese.setBdgConsQta(paiInterventoMese.getBdgConsQta() == null ? null
						: paiInterventoMese.getBdgConsQta().multiply(SETTIMANE_IN_MESE));
				paiInterventoMese.setBdgConsQtaBenef(paiInterventoMese.getBdgConsQtaBenef() == null ? null
						: paiInterventoMese.getBdgConsQtaBenef().multiply(SETTIMANE_IN_MESE));
			}
		}
	}

	/**
	 * @return
	 *         <ul>
	 *         <li>interventi con rinnovato != 0</li>
	 *         <li>stato esecutivo</li>
	 *         <li>si conclude nel prossimo mese</li>
	 *         </ul>
	 */
	public List<InterventoDto> getInterventiPerProrogaAutomatica_native(Date fromDate) {
		List<InterventoDto> dtos = new ArrayList<InterventoDto>();
		EntityManager entityManager = getEntityManager();

		LocalDate now_plus_one_month = new LocalDate(fromDate).plusMonths(1);
		Date toDate = now_plus_one_month.toDate();

		Query query = entityManager.createNamedQuery("interventi_per_proroga_automatica");
		query.setParameter(1, fromDate, TemporalType.DATE);
		query.setParameter(2, toDate, TemporalType.DATE);
		query.setParameter(3, fromDate, TemporalType.DATE);
		query.setParameter(4, toDate, TemporalType.DATE);
		List<Object[]> resultList = query.getResultList();

		for (Object[] item : resultList) {
			int codPai = ((BigDecimal) item[0]).intValue();
			String codTipint = String.valueOf(item[1]);
			int cntTipint = ((BigDecimal) item[2]).intValue();
			InterventoDto interventoDto = new InterventoDto(codPai, codTipint, cntTipint);
			dtos.add(interventoDto);
		}

		return dtos;
	}

	public PaiIntervento findByKey(PaiIntervento interventoPadre) {
		PaiInterventoPK paiInterventoPK = interventoPadre.getPaiInterventoPK();
		Integer codPai = paiInterventoPK.getCodPai();
		String codTipInt = paiInterventoPK.getCodTipint();
		Integer cntTipIntPadre = paiInterventoPK.getCntTipint();
		return findByKey(codPai, codTipInt, cntTipIntPadre);
	}

	public PaiIntervento findByKey(InterventoDto interventoDto) {
		Integer codPai = interventoDto.getCodPai();
		String codTipInt = interventoDto.getCodTipint();
		Integer cntTipInt = interventoDto.getCntTipint();
		return findByKey(codPai, codTipInt, cntTipInt);
	}

	public PaiIntervento getReference(InterventoDto interventoDto) {
		Integer codPai = interventoDto.getCodPai();
		String codTipInt = interventoDto.getCodTipint();
		Integer cntTipInt = interventoDto.getCntTipint();

		EntityManager entityManager = getEntityManager();
		PaiInterventoPK pk = new PaiInterventoPK(codPai, codTipInt, cntTipInt);
		PaiIntervento paiIntervento = entityManager.getReference(PaiIntervento.class, pk);
		return paiIntervento;
	}

	public List<InterventoDto> find_interventi_aperti_che_terminano_entro_native(Date now) {
		List<InterventoDto> dtos = new ArrayList<InterventoDto>();

		EntityManager entityManager = getEntityManager();

		Query query = entityManager.createNamedQuery("interventi_aperti_da_chiudere");
		query.setParameter(1, now, TemporalType.DATE);
		query.setParameter(2, now, TemporalType.DATE);
		List<Object[]> resultList = query.getResultList();

		for (Object[] item : resultList) {
			int codPai = ((BigDecimal) item[0]).intValue();
			String codTipint = String.valueOf(item[1]);
			int cntTipint = ((BigDecimal) item[2]).intValue();
			InterventoDto interventoDto = new InterventoDto(codPai, codTipint, cntTipint);
			dtos.add(interventoDto);
		}

		return dtos;
	}
}
