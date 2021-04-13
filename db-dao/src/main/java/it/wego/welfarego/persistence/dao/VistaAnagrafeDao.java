package it.wego.welfarego.persistence.dao;

import com.google.common.base.Objects;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import it.wego.extjs.beans.Order;
import it.wego.extjs.json.JsonBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Luogo;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Stato;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * @author giuseppe
 */
public class VistaAnagrafeDao extends PersistenceAdapter {

    public VistaAnagrafeDao(EntityManager em) {
        super(em);
    }

    /**
     * @deprecated use plain query via PersistenceUtils
     */
    @Deprecated
    public int countGenericSearch(String nome, String cognome, String codiceFiscale, @Nullable Order order) {
        Query query = getGenericSearchQuery(nome, cognome, codiceFiscale, order);
        List<VistaAnagrafe> risultati = query.getResultList();
        return risultati != null ? risultati.size() : 0;
    }

    /**
     * @deprecated use plain query via PersistenceUtils
     */
    @Deprecated
    public List<VistaAnagrafe> genericSearch(String nome, String cognome, String codiceFiscale, int limit, int offset, @Nullable Order order) {
        Query query = getGenericSearchQuery(nome, cognome, codiceFiscale, order);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        List<VistaAnagrafe> risultati = query.getResultList();
        return risultati;
    }

    /**
     * @deprecated use plain query via PersistenceUtils
     */
    @Deprecated
    private Query getGenericSearchQuery(String nome, String cognome, String codiceFiscale, @Nullable Order order) {
        //TODO rewrite this
        String q = "SELECT v " + "FROM VistaAnagrafe v ";
        boolean isFirst = true;
        boolean alreadyInserted = false;
        if (nome != null && !"".equals(nome)) {
            if (isFirst) {
                q = q + "WHERE ";
                isFirst = false;
            }
            q = q + "LOWER(v.nome) LIKE :nome ";
            alreadyInserted = true;
        }
        if (cognome != null && !"".equals(cognome)) {
            if (isFirst) {
                q = q + "WHERE ";
                isFirst = false;
            }
            if (alreadyInserted) {
                q = q + "AND ";
            }
            q = q + "LOWER(v.cognome) LIKE :cognome ";
            alreadyInserted = true;
        }
        if (codiceFiscale != null && !"".equals(codiceFiscale)) {
            if (isFirst) {
                q = q + "WHERE ";
                isFirst = false;
            }
            if (alreadyInserted) {
                q = q + "AND ";
            }
            q = q + "LOWER(v.codiceFiscale) LIKE :codiceFiscale ";
            alreadyInserted = true;
        }
        if (order != null) {
            String column = order.getProperty();
            String direction = order.getDirection().toUpperCase();
            q = q + " ORDER BY v." + column + " " + direction;
        }
        Query query = getEntityManager().createQuery(q);
        if (nome != null && !"".equals(nome)) {
            query.setParameter("nome", "%" + nome.toLowerCase() + "%");
        }
        if (cognome != null && !"".equals(cognome)) {
            query.setParameter("cognome", "%" + cognome.toLowerCase() + "%");
        }
        if (codiceFiscale != null && !"".equals(codiceFiscale)) {
            query.setParameter("codiceFiscale", "%" + codiceFiscale.toLowerCase() + "%");
        }
        return query;
    }

    /**
     * @param numeroFamiglia
     * @param limit          (ignorato)
     * @param offset         (ignorato)
     * @return
     * @deprecated use findByNumeroFamiglia(int numeroFamiglia)
     */
    @Deprecated
    public List<VistaAnagrafe> findByNumeroFamiglia(int numeroFamiglia, int limit, int offset) {
        return findByNumeroFamiglia(numeroFamiglia);
    }

    /**
     FLAG_CONVIVENZA = 1 : la persona risiede con un nucleo famigliare di cui NON vanno mostrati i componenti.
     FLAG_CONVIVENZA = NULL: la persona risiede con un nucleo famigliare di cui vanno mostrati i componenti.
     * @param numeroFamiglia
     * @return
     */
    public List<VistaAnagrafe> findByNumeroFamiglia(int numeroFamiglia) {
        Query query = getEntityManager().createQuery("SELECT v FROM VistaAnagrafe v WHERE v.numeroFamiglia = :numeroFamiglia and v.famigliaConvivenza is null");
        query.setParameter("numeroFamiglia", numeroFamiglia);
        return query.getResultList();
    }

    public List<VistaAnagrafe> findByNumeroFamigliaNonMorti(int numeroFamiglia) {
        Query query = getEntityManager().createQuery("SELECT v FROM VistaAnagrafe v WHERE v.numeroFamiglia = :numeroFamiglia and v.dataMorte is null ");
        query.setParameter("numeroFamiglia", numeroFamiglia);
        return query.getResultList();
    }

    public VistaAnagrafe findByNumeroIndividuale(int numeroIndividuale) {
        TypedQuery<VistaAnagrafe> query = getEntityManager().createQuery("SELECT v FROM VistaAnagrafe v WHERE v.numeroIndividuale = :numeroIndividuale", VistaAnagrafe.class);
        query.setParameter("numeroIndividuale", numeroIndividuale);
        return getSingleResult(query);
    }

    public VistaAnagrafe findByCodiceFiscale(String codiceFiscale) {
        TypedQuery<VistaAnagrafe> query = getEntityManager().createQuery("SELECT v FROM VistaAnagrafe v WHERE v.codiceFiscale = :codiceFiscale", VistaAnagrafe.class);
        query.setParameter("codiceFiscale", codiceFiscale);
        return getSingleResult(query);
    }

    public VistaAnagrafe findByNumeroIndividuale(String codiceAnagrafico) {
        return findByNumeroIndividuale(Integer.valueOf(codiceAnagrafico));
    }

    public AnagrafeSoc anagrafeComune2AnagrafeWego(VistaAnagrafe vistaAnagrafe) {
        AnagrafeSoc anagrafeSoc = new AnagrafeSoc();
        anagrafeSoc.setCodAnaCom(String.valueOf(vistaAnagrafe.getNumeroIndividuale()));
        anagrafeSoc.setCodAnaFamCom(String.valueOf(vistaAnagrafe.getNumeroFamiglia()));
        anagrafeSoc.setCodFisc(vistaAnagrafe.getCodiceFiscale());
        anagrafeSoc.setCognome(vistaAnagrafe.getCognome());
        anagrafeSoc.setCognomeConiuge(vistaAnagrafe.getCognomeConiuge());
        anagrafeSoc.setDtMorte(vistaAnagrafe.getDataMorte());
        anagrafeSoc.setDtNasc(vistaAnagrafe.getDataNascita());
        anagrafeSoc.setNome(vistaAnagrafe.getNome());

        anagrafeSoc.setCodStatoNaz(new ParametriIndataDao(getEntityManager()).findNazionalita(vistaAnagrafe.getDescrizioneStatoNascita()));

        if (vistaAnagrafe.getSesso().equals("1")) {
            anagrafeSoc.setFlgSex("M");
        } else {
            anagrafeSoc.setFlgSex("F");
        }
        anagrafeSoc.setFlgSms('N');
        anagrafeSoc.setFlgEmail('N');

        if (!Strings.isNullOrEmpty(vistaAnagrafe.getPosizioneAnagrafica())) {
            anagrafeSoc.setIdParamPosAna(new ParametriDao(getEntityManager()).findOneByCodParamTipParam(vistaAnagrafe.getPosizioneAnagrafica(), Parametri.POSIZIONE_ANAGRAFICA).getParametriIndataSingle());
        }

        anagrafeSoc.setLuogoNascita(new LuogoDao(getEntityManager()).newLuogo(
                MoreObjects.firstNonNull(vistaAnagrafe.getCodiceStatoNascita(), Stato.COD_STATO_ITALIA), vistaAnagrafe.getDescrizioneStatoNascita(),
                vistaAnagrafe.getCodiceStatoNascita() != null ? null : vistaAnagrafe.getCodiceProvinciaNascita(), Strings.isNullOrEmpty(vistaAnagrafe.getDescrizioneProvinciaNascita()) == true ? "NON DISPONIBILE" : vistaAnagrafe.getDescrizioneProvinciaNascita(),
                vistaAnagrafe.getCodiceStatoNascita() != null ? null : vistaAnagrafe.getCodiceComuneNascita(), vistaAnagrafe.getLuogoNascita(),
                null, null,
                null, null,
                null));

        anagrafeSoc.setLuogoResidenza(new LuogoDao(getEntityManager()).newLuogo(
                MoreObjects.firstNonNull(vistaAnagrafe.getCodiceStatoResidenza(), Stato.COD_STATO_ITALIA), vistaAnagrafe.getDescrizioneStatoResidenza(),
                vistaAnagrafe.getCodiceProvinciaREsidenza(), vistaAnagrafe.getDescrizioneProvinciaResidenza(),
                vistaAnagrafe.getCodiceComuneResidenza(), vistaAnagrafe.getDescrizioneComuneResidenza(),
                vistaAnagrafe.getCodiceViaResidenza(), vistaAnagrafe.getDescrizioneViaResidenza(),
                vistaAnagrafe.getCodiceCivicoResidenza(), vistaAnagrafe.getDescrizioneCivicoResidenza(),
                vistaAnagrafe.getCap()));

        //TODO stato civile

        String codiceFiscale = vistaAnagrafe.getCodiceFiscale();

        StatoDao statoDao = new StatoDao(getEntityManager());
        String codStatoCittadinanza = vistaAnagrafe.getCodStatoCittadinanza();
        Stato byCodStato = statoDao.findByCodStato(codStatoCittadinanza);


        if (codiceFiscale != null) {
            codiceFiscale = codiceFiscale.substring(11);
            String codCatasto = codiceFiscale.substring(0, codiceFiscale.length() - 1);

            if (!"ITALIA".equals(vistaAnagrafe.getCittadinanza())) {
                if (codCatasto != null) {
                    Stato cittadinanza = statoDao.findByCodiceCatastale(codCatasto);
                    if (cittadinanza != null) {
                        anagrafeSoc.setStatoCittadinanza(cittadinanza);
                    } else {
                        anagrafeSoc.setStatoCittadinanza(byCodStato);
                    }
                }
            } else {
                anagrafeSoc.setStatoCittadinanza(byCodStato);
            }
        } else {
            String msgTemplate = "su wego non esiste una corrispondenza per il codice stato: %s per (numero individuale, nome): (%s, %s)";
            String errMessage = String.format(msgTemplate, codStatoCittadinanza, vistaAnagrafe.getNumeroIndividuale(), vistaAnagrafe.getCognome() + " " + vistaAnagrafe.getNome());
            getLogger().error(errMessage);
            anagrafeSoc.setStatoCittadinanza(byCodStato);
        }

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("importing vistaAnagrafe = {}", JsonBuilder.getGsonPrettyPrinting().toJson(vistaAnagrafe));
            getLogger().debug("luogo nascita = {}", anagrafeSoc.getLuogoNascita());
            getLogger().debug("luogo residenza = {}", anagrafeSoc.getLuogoResidenza());
        }

        return anagrafeSoc;
    }

    private final static List<String> vistaAnagrafeProperties = Collections.unmodifiableList(Lists.newArrayList(new String[]{
            "codFisc",
            "cognome",
            "cognomeConiuge",
            "dtMorte",
            "dtNasc",
            "nome",
            "flgSex",
            "flgSms",
            "flgEmail",
            "luogoNascita",
            "luogoResidenza",
            "codStatoCitt",
            "codAnaFamCom",
            "codAnaCom",
            "idParamPosAna",
    }));

    public void allineaAnagrafeWegoDaAnagrafeComune(VistaAnagrafe vistaAnagrafe, AnagrafeSoc anagrafeSoc) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        AnagrafeSoc newAnagrafeSoc = anagrafeComune2AnagrafeWego(vistaAnagrafe);
        for (String propertyName : vistaAnagrafeProperties) {
            PropertyUtils.setProperty(anagrafeSoc, propertyName, PropertyUtils.getProperty(newAnagrafeSoc, propertyName));
        }
    }


    /**
     *<div>Esegue una verifica sulla corrispondenza di questi campi:</div>
     * <pre>
     "codFisc",
     "cognome",
     "cognomeConiuge",
     "dtMorte",
     "dtNasc",
     "nome",
     "flgSex",
     "flgSms",
     "flgEmail",
     "luogoNascita",
     "luogoResidenza",
     "codStatoCitt",
     "codAnaFamCom",
     "codAnaCom",
     "idParamPosAna",
     * </pre>
     * <div>vedi @see VistaAnagrafeDao.vistaAnagrafeProperties</div>
     * @param vistaAnagrafe
     * @param anagrafeSoc
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public List<VistaAnagrafeToAnagrafePropertyDifference> getVistaAnagraficaToAnagrafeSocDifferences(VistaAnagrafe vistaAnagrafe, AnagrafeSoc anagrafeSoc) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        getLogger().debug("searching differences from {} to {}", anagrafeSoc, vistaAnagrafe);
        List<VistaAnagrafeToAnagrafePropertyDifference> res = Lists.newArrayList();

        AnagrafeSoc newAnagrafeSoc = anagrafeComune2AnagrafeWego(vistaAnagrafe);

        for (final String propertyName : vistaAnagrafeProperties) {
            final Object oldValue = PropertyUtils.getProperty(anagrafeSoc, propertyName);
            final Object newValue = PropertyUtils.getProperty(newAnagrafeSoc, propertyName);

            if (!Objects.equal(oldValue, newValue)
                    && (
                    !(oldValue instanceof String && newValue instanceof String) ||
                            !StringUtils.equalsIgnoreCase((String) oldValue, (String) newValue)
            )
                    ) {
                res.add(new VistaAnagrafeToAnagrafePropertyDifference() {
                    private String toString(Object value) {
                        if (value == null) {
                            return "&lt;NULL&gt;";
                        } else if (value instanceof String) {
                            return (String) value;
                        } else if (value instanceof Date) {
                            return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ITALY).format(value);
                        } else if (value instanceof ParametriIndata) {
                            return ((ParametriIndata) value).getDesParam();
                        } else if (value instanceof Luogo) {
                            return ((Luogo) value).getLuogoText();
                        } else if (value instanceof Stato) {
                            return ((Stato) value).getDesStato();
                        } else {
                            return String.valueOf(value);
                        }
                    }

                    public String getVistaAnagrafeStringValue() {
                        return toString(newValue);
                    }

                    public String getAnagrafeSocStringValue() {
                        return toString(oldValue);
                    }

                    public String getPropertyName() {
                        return propertyName;
                    }
                });
            }
        }
        return res;
    }

    public static interface VistaAnagrafeToAnagrafePropertyDifference {

        public String getVistaAnagrafeStringValue();

        public String getAnagrafeSocStringValue();

        public String getPropertyName();
    }
}
