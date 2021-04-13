package it.wego.welfarego.commons.utils;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.wego.conversions.StringConversion;
import it.wego.web.WebUtils;
import it.wego.welfarego.commons.model.AnagraficaBean;
import it.wego.welfarego.persistence.constants.ComuneConstants;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.ComuneDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.ProvinciaDao;
import it.wego.welfarego.persistence.dao.StatoDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.dao.VistaAnagrafeDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.CartellaSociale;
import it.wego.welfarego.persistence.entities.Comune;
import it.wego.welfarego.persistence.entities.Luogo;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Provincia;
import it.wego.welfarego.persistence.entities.Stato;
import it.wego.welfarego.persistence.entities.Toponomastica;
import it.wego.welfarego.persistence.entities.ToponomasticaCivici;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giuseppe
 */
public class ImportazioneUtils {

    private final static Logger logger = LoggerFactory.getLogger(ImportazioneUtils.class);
    private EntityManager em;

    public ImportazioneUtils(EntityManager em) {
        this.em = em;
    }

    public static AnagraficaBean anagrafeSocToAnagraficaBean(AnagrafeSoc anagrafe) {
        return anagrafeSocToAnagraficaBean(anagrafe, null);
    }

    public static AnagraficaBean anagrafeSocToAnagraficaBean(AnagrafeSoc anagrafe, @Nullable Pai pai) {
        AnagraficaBean anagraficaBean = new AnagraficaBean();
        anagraficaBean.setAnagraficaNote(anagrafe.getNote());
        anagraficaBean.setAnagraficaCodiceAnagrafico(anagrafe.getCodAnaCom());
        anagraficaBean.setAnagraficaNome(anagrafe.getNome());
        anagraficaBean.setAnagraficaDataDiNascita(StringConversion.dateToItString(anagrafe.getDtNasc()));
        if(anagrafe.getCodUteEducatore()!=null){
            anagraficaBean.setAnagraficaEducatore(String.valueOf(anagrafe.getCodUteEducatore().getCodUte()));
            anagraficaBean.setAnagraficaEducatoreValue(String.valueOf(anagrafe.getCodUteEducatore().getCodUte()));
        }

        anagraficaBean.setAnagraficaStatoNascita(anagrafe.getLuogoNascita().getCodStato());
        anagraficaBean.setAnagraficaStatoNascitaDesc(anagrafe.getLuogoNascita().getStatoText());

        if (anagrafe.getCodStatoNaz() != null) {
            anagraficaBean.setAnagraficaNazionalita(String.valueOf(anagrafe.getCodStatoNaz().getIdParamIndata()));
        }
        if (anagrafe.getCodStatoCitt() != null) {
            anagraficaBean.setAnagraficaCittadinanza(String.valueOf(anagrafe.getCodStatoCitt().getCodStato()));
        }
        anagraficaBean.setAnagraficaCodiceFiscale(anagrafe.getCodFisc());
        if (anagrafe.getLuogoResidenza().getStato() != null) {
            anagraficaBean.setAnagraficaStatoDiResidenza(anagrafe.getLuogoResidenza().getStato().getCodStato());

        }

        anagraficaBean.setAnagraficaStatoDomicilio(anagrafe.getLuogoDomicilio().getCodStato());

        if (anagrafe.getIdParamTipologiaResidenza() != null) {
            anagraficaBean.setAnagraficaTipologiaResidenza(anagrafe.getIdParamTipologiaResidenza().getIdParamIndata().toString());
        }
        anagraficaBean.setAnagraficaTelefono(anagrafe.getNumTel());
        anagraficaBean.setAnagraficaEmail(anagrafe.getEmail());
        anagraficaBean.setAnagraficaEnteGestore(anagrafe.getEnteGestore());
        anagraficaBean.setAnagraficaMedicoDiBase(anagrafe.getMedicoBase());
        if (String.valueOf(anagrafe.getFlgSms()).equals("S")) {
            anagraficaBean.setAnagraficaNotificaSMS("on");
        }
        if (String.valueOf(anagrafe.getFlgEmail()).equals("S")) {
            anagraficaBean.setAnagraficaNotificaEmail("on");
        }

        if (anagrafe.getCodSegnDa() != null) {
            anagraficaBean.setAnagraficaSegnalatoDa(String.valueOf(anagrafe.getCodSegnDa().getIdParamIndata()));
        }
        anagraficaBean.setAnagraficaCodiceNucleoFamiliare(anagrafe.getCodAnaFamCom());
        anagraficaBean.setAnagraficaCognome(anagrafe.getCognome());
        anagraficaBean.setAnagraficaSesso(String.valueOf(anagrafe.getFlgSex()).toUpperCase());
        anagraficaBean.setAnagraficaCognomeConiuge(anagrafe.getCognomeConiuge());

        anagraficaBean.setAnagraficaComuneDiNascita(anagrafe.getLuogoNascita().getCodCom());

        anagraficaBean.setAnagraficaComuneEsteroDiNascita(anagrafe.getLuogoNascita().getComuneStr());

        anagraficaBean.setAnagraficaComuneDiNascitaDesc(anagrafe.getLuogoNascita().getCodCom()==null?anagrafe.getLuogoNascita().getComuneStr():anagrafe.getLuogoNascita().getCodCom());
        if (anagrafe.getIdParamPosAna() != null) {
            anagraficaBean.setAnagraficaPosizioneAnagrafica(String.valueOf(anagrafe.getIdParamPosAna().getIdParamIndata()));
        }
        anagraficaBean.setAnagraficaDataMorte(StringConversion.dateToItString(anagrafe.getDtMorte()));

        anagraficaBean.setAnagraficaProvinciaDomicilio(anagrafe.getLuogoDomicilio().getCodProv());

        anagraficaBean.setAnagraficaProvinciaEsteraDomicilio(anagrafe.getLuogoDomicilio().getProvinciaStr());
        anagraficaBean.setAnagraficaProvinciaNascita(anagrafe.getLuogoNascita().getCodProv());
        anagraficaBean.setAnagraficaProvinciaEsteraNascita(anagrafe.getLuogoNascita().getProvinciaStr());

        anagraficaBean.setAnagraficaProvinciaNascitaDesc(anagrafe.getLuogoNascita().getCodProv()==null?anagrafe.getLuogoNascita().getProvinciaStr():anagrafe.getLuogoNascita().getCodProv());

        anagraficaBean.setAnagraficaProvinciaDomicilio(anagrafe.getLuogoDomicilio().getCodProv());
        anagraficaBean.setAnagraficaProvinciaEsteraDomicilio(anagrafe.getLuogoDomicilio().getProvinciaStr());
        anagraficaBean.setAnagraficaProvinciaResidenza(anagrafe.getLuogoResidenza().getCodProv());
        anagraficaBean.setAnagraficaProvinciaEsteraResidenza(anagrafe.getLuogoResidenza().getProvinciaStr());
        anagraficaBean.setAnagraficaComuneDomicilio(anagrafe.getLuogoDomicilio().getCodCom());
        anagraficaBean.setAnagraficaComuneEsteroDomicilio(anagrafe.getLuogoDomicilio().getComuneStr());
        anagraficaBean.setAnagraficaViaDomicilio(anagrafe.getLuogoDomicilio().getCodVia());
        anagraficaBean.setAnagraficaCivicoDomicilio(anagrafe.getLuogoDomicilio().getCodCiv());
        anagraficaBean.setAnagraficaViaNoTsDomicilio(anagrafe.getLuogoDomicilio().getViaStr());
        anagraficaBean.setAnagraficaCivicoNoTsDomicilio(anagrafe.getLuogoDomicilio().getCivicoStr());
        anagraficaBean.setAnagraficaComuneResidenza(anagrafe.getLuogoResidenza().getCodCom());
        anagraficaBean.setAnagraficaComuneEsteroResidenza(anagrafe.getLuogoResidenza().getComuneStr());
        anagraficaBean.setAnagraficaCivicoResidenza(anagrafe.getLuogoResidenza().getCodCiv());
        anagraficaBean.setAnagraficaViaResidenza(anagrafe.getLuogoResidenza().getCodVia());
        anagraficaBean.setAnagraficaCivicoNoTsResidenza(anagrafe.getLuogoResidenza().getCivicoStr());
        anagraficaBean.setAnagraficaViaNoTsResidenza(anagrafe.getLuogoResidenza().getViaStr());
        anagraficaBean.setAnagraficaCapResidenza(StringConversion.objectToString(anagrafe.getLuogoResidenza().getCap()));
        anagraficaBean.setAnagraficaCapDomicilio(StringConversion.objectToString(anagrafe.getLuogoDomicilio().getCap()));
        anagraficaBean.setAnagraficaCellulare(anagrafe.getNumCell());
        if (anagrafe.getZona() != null) {
            anagraficaBean.setAnagraficaZona(anagrafe.getZona().toString());
        }
        anagraficaBean.setAnagraficaSottozona(anagrafe.getSottozona());
        anagraficaBean.setAnagraficaAss(anagrafe.getCodAss());
        anagraficaBean.setAnagraficaPresso(anagrafe.getPresso());
        if (anagrafe.getDistSan() != null) {
            anagraficaBean.setAnagraficaDistrettoASS(anagrafe.getDistSan().toString());
        }
        anagraficaBean.setCodAna(anagrafe.getCodAna());
        if (pai != null && pai.getFlgStatoPai() != 'C') {
            if (pai.getIdParamUot() != null) {
                anagraficaBean.setAnagraficaUot(String.valueOf(pai.getIdParamUot().getIdParamIndata()));
            }
            if (pai.getCodUteAs() != null) {
                anagraficaBean.setAnagraficaAssistenteSociale(String.valueOf(pai.getCodUteAs().getCodUte()));
                anagraficaBean.setAnagraficaAssistenteSocialeValue(String.valueOf(pai.getCodUteAs().getCodUte()));
            }
        }

        return anagraficaBean;
    }

    public AnagrafeSoc serializeFamiglia(HttpServletRequest request) throws Exception {
        AnagrafeSoc anagrafe;
        String codAnagFamigliare = request.getParameter("codAnaFamigliare");
        if (codAnagFamigliare != null && !"".equals(codAnagFamigliare)) {
            AnagrafeSocDao dao = new AnagrafeSocDao(em);
            anagrafe = dao.findByCodAna(Integer.valueOf(codAnagFamigliare));
        } else {
            anagrafe = new AnagrafeSoc();
        }
        anagrafe.setNome(request.getParameter("nome"));
        anagrafe.setCognome(request.getParameter("cognome"));
        anagrafe.setCodFisc(request.getParameter("codiceFiscale"));
        if (request.getParameter("dataNascita") != null && !"".equals(request.getParameter("dataNascita").trim())) {
            anagrafe.setDtNasc(StringConversion.itStringToDate(request.getParameter("dataNascita")));
        }
        if (request.getParameter("dataMorte") != null && !"".equals(request.getParameter("dataMorte").trim())) {
            anagrafe.setDtMorte(StringConversion.itStringToDate(request.getParameter("dataMorte")));
        }
        anagrafe.setFlgSex(request.getParameter("sesso"));
        ParametriIndataDao parametriDao = new ParametriIndataDao(em);
        String statoCivileString = request.getParameter("statoCivile");
        if (statoCivileString != null && !"".equals(statoCivileString.trim())) {
            int id = Integer.valueOf(statoCivileString);
            ParametriIndata statoCivile = parametriDao.findByIdParamIndata(id);
            anagrafe.setIdParamStatoCiv(statoCivile);
        }
        StatoDao statoDao = new StatoDao(em);
        Stato statoNascita = statoDao.findByCodStato(request.getParameter("statoNascita"));
        anagrafe.getLuogoNascita().setStato(statoNascita);
        Stato statoResidenza = statoDao.findByCodStato(request.getParameter("statoResidenza"));
        anagrafe.getLuogoResidenza().setStato(statoResidenza);
        String nazionalita = request.getParameter("nazionalita");
        if (nazionalita != null && !"".equals(nazionalita)) {
            int id = Integer.valueOf(nazionalita);
            ParametriIndata naz = parametriDao.findByIdParamIndata(id);
            anagrafe.setCodStatoNaz(naz);
        }
        String codStatoCittadinanza = Strings.emptyToNull(request.getParameter("anagraficaCittadinanza"));
        if (codStatoCittadinanza != null) {
            anagrafe.setCodStatoCitt(statoDao.findByCodStato(codStatoCittadinanza));
        }

        ComuneDao comuneDao = new ComuneDao(em);
        Comune comuneNascita = comuneDao.findByCodCom(request.getParameter("comuneNascita"));
        if (comuneNascita != null) {
            anagrafe.getLuogoNascita().setComune(comuneNascita);
        } else {
            anagrafe.getLuogoNascita().setComuneStr(request.getParameter("comuneNascita"));
        }
        ProvinciaDao provinciaDao = new ProvinciaDao(em);
        Provincia provinciaNasc = provinciaDao.findByCodProv(request.getParameter("provinciaNascita"));
        if (provinciaNasc != null) {
            anagrafe.getLuogoNascita().setProvincia(provinciaNasc);
        } else {
            anagrafe.getLuogoNascita().setProvinciaStr(request.getParameter("provinciaNascita"));
        }
        Provincia provinciaRes = provinciaDao.findByCodProv(request.getParameter("provinciaResidenza"));
        if (provinciaRes != null) {
            anagrafe.getLuogoResidenza().setProvincia(provinciaRes);
        } else {
            anagrafe.getLuogoResidenza().setProvinciaStr(request.getParameter("provinciaResidenza"));
        }
        Comune comuneResidenza = comuneDao.findByCodCom(request.getParameter("comuneResidenza"));
        if (comuneResidenza != null) {
            anagrafe.getLuogoResidenza().setComune(comuneResidenza);
        } else {
            anagrafe.getLuogoResidenza().setComuneStr(request.getParameter("comuneResidenza"));
        }
        if (request.getParameter("comuneResidenza").equals(ComuneConstants.COMUNE_TRIESTE) && comuneResidenza != null) {
            Toponomastica toponomastica = comuneResidenza.getToponomastica(request.getParameter("viaResidenza"));
            ToponomasticaCivici toponomasticaCivici = toponomastica == null ? null : toponomastica.getToponomasticaCivici(request.getParameter("civicoResidenza"));
            anagrafe.getLuogoResidenza().setVia(toponomastica);
            anagrafe.getLuogoResidenza().setCivico(toponomasticaCivici);
        } else {
            anagrafe.getLuogoResidenza().setViaStr(request.getParameter("viaResidenza"));
            anagrafe.getLuogoResidenza().setCivicoStr(request.getParameter("civicoResidenza"));
        }
        return anagrafe;
    }

    public AnagrafeSoc serializeReferenti(HttpServletRequest request) throws Exception {
        AnagrafeSoc anagrafe = serializeFamiglia(request);
        return anagrafe;

    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     * @deprecated use
     * requestParamsToAnagrafeSoc(WebUtils.getParametersMap(request)) instead
     */
    @Deprecated
    public AnagrafeSoc serializeAnagrafica(HttpServletRequest request) throws Exception {
        return requestParamsToAnagrafeSoc(WebUtils.getParametersMap(request));
    }

    public AnagrafeSoc requestParamsToAnagrafeSoc(Map<String, String> params) throws ParseException {
        AnagrafeSoc anagrafe;
        String codAnag = params.get("codAna");
        if (!Strings.isNullOrEmpty(codAnag) && Integer.valueOf(codAnag) > 0) {
            AnagrafeSocDao dao = new AnagrafeSocDao(em);
            anagrafe = dao.findByCodAna(Integer.valueOf(codAnag));
        } else {
            anagrafe = new AnagrafeSoc();
        }
        //TODO: Lo forzo ad essere fisica. non è ancora prevista la gestione delle persone giuridiche
        anagrafe.setFlgPersFg(AnagrafeSoc.PERSONA_FISICA_F);
        anagrafe.setNome(params.get("anagraficaNome"));
        anagrafe.setDtNasc(StringConversion.stringToDate(params.get("anagraficaDataDiNascita"), "dd/MM/yyyy"));
        anagrafe.setCognome(params.get("anagraficaCognome"));
        anagrafe.setFlgSex(params.get("anagraficaSesso"));
        anagrafe.setEmail(params.get("anagraficaEmail"));
        anagrafe.setNumTel(params.get("anagraficaTelefono"));
        anagrafe.setNumCell(params.get("anagraficaCellulare"));
        anagrafe.setCodFisc(params.get("anagraficaCodiceFiscale"));
        anagrafe.setNote(params.get("anagraficaNote"));
        anagrafe.setEnteGestore(params.get("anagraficaEnteGestore"));
        anagrafe.setMedicoBase(params.get("anagraficaMedicoDiBase"));
        if (Objects.equal("on", params.get("anagraficaNotificaSMS"))) {
            anagrafe.setFlgSms('S');
        } else {
            anagrafe.setFlgSms('N');
        }
        if (Objects.equal("on", params.get("anagraficaNotificaEmail"))) {
            anagrafe.setFlgEmail('S');
        } else {
            anagrafe.setFlgEmail('N');
        }
        anagrafe.setZona(Strings.emptyToNull(params.get("anagraficaZona")));
        anagrafe.setDistSan(Strings.emptyToNull(params.get("anagraficaDistrettoASS")));
        anagrafe.setSottozona(Strings.emptyToNull(params.get("anagraficaSottozona")));
        anagrafe.setCodAss(params.get("anagraficaAss"));
        anagrafe.setCodAnaCom(params.get("anagraficaCodiceAnagrafico"));
        anagrafe.setCodAnaFamCom(params.get("anagraficaCodiceNucleoFamiliare"));
        anagrafe.setPresso(params.get("anagraficaPresso"));
        cleanFields(anagrafe);
        StatoDao statoDao = new StatoDao(em);
        Stato statoNascita = statoDao.findByCodStato(params.get("anagraficaStatoNascita"));
        anagrafe.getLuogoNascita().setStato(statoNascita);
        Stato statoResidenza = statoDao.findByCodStato(params.get("anagraficaStatoDiResidenza"));
        anagrafe.getLuogoResidenza().setStato(statoResidenza);
        Stato statoDomicilio = statoDao.findByCodStato(params.get("anagraficaStatoDomicilio"));
        anagrafe.getLuogoDomicilio().setStato(statoDomicilio);
        ProvinciaDao provinciaDao = new ProvinciaDao(em);
        if (params.get("anagraficaProvinciaResidenza") != null) {
            Provincia provinciaRes = provinciaDao.findByCodProv(params.get("anagraficaProvinciaResidenza"));
            if (provinciaRes != null) {
                anagrafe.getLuogoResidenza().setProvincia(provinciaRes);
            } else {
                anagrafe.getLuogoResidenza().setProvinciaStr(params.get("anagraficaProvinciaResidenza"));
            }
        }
        if (params.get("anagraficaProvinciaDomicilio") != null) {
            Provincia provinciaDom = provinciaDao.findByCodProv(params.get("anagraficaProvinciaDomicilio"));
            if (provinciaDom != null) {
                anagrafe.getLuogoDomicilio().setProvincia(provinciaDom);
            } else {
                anagrafe.getLuogoDomicilio().setProvinciaStr(params.get("anagraficaProvinciaDomicilio"));
            }
        }
        if (params.get("anagraficaProvinciaNascita") != null) {
            Provincia provinciaNas = provinciaDao.findByCodProv(params.get("anagraficaProvinciaNascita"));
            if (provinciaNas != null) {
                anagrafe.getLuogoNascita().setProvincia(provinciaNas);
            } else {
                anagrafe.getLuogoNascita().setProvinciaStr(params.get("anagraficaProvinciaNascita"));
            }
        }

        ComuneDao comuneDao = new ComuneDao(em);

        if (params.get("anagraficaComuneDiNascita") != null) {
            Comune comuneNascita = comuneDao.findByCodCom(params.get("anagraficaComuneDiNascita"));
            if (comuneNascita != null) {
                anagrafe.getLuogoNascita().setComune(comuneNascita);
            } else {
                anagrafe.getLuogoNascita().setComuneStr(params.get("anagraficaComuneDiNascita"));
            }
        }
        if (params.get("anagraficaComuneResidenza") != null) {
            Comune comuneResidenza = comuneDao.findByCodCom(params.get("anagraficaComuneResidenza"));
            if (comuneResidenza != null) {
                anagrafe.getLuogoResidenza().setComune(comuneResidenza);
            } else {
                anagrafe.getLuogoResidenza().setComuneStr(params.get("anagraficaComuneResidenza"));
            }
            if (comuneResidenza != null && !comuneResidenza.getToponomasticaList().isEmpty()) {
                Toponomastica toponomastica = comuneResidenza.getToponomastica(params.get("anagraficaViaResidenza"));
                Preconditions.checkArgument(toponomastica != null, "via residenza %s non presente nello stradario", params.get("anagraficaViaResidenza"));
                anagrafe.getLuogoResidenza().setVia(toponomastica);
                if(!Strings.isNullOrEmpty(params.get("anagraficaCivicoResidenza"))){
                    ToponomasticaCivici toponomasticaCivici = toponomastica.getToponomasticaCivici(params.get("anagraficaCivicoResidenza"));
                    Preconditions.checkArgument(toponomasticaCivici != null, "numero civico residenza %s non presente nello stradario", params.get("anagraficaCivicoResidenza"));
                    anagrafe.getLuogoResidenza().setCivico(toponomasticaCivici);
                }

            } else {
                anagrafe.getLuogoResidenza().setViaStr(params.get("anagraficaViaResidenza"));
                anagrafe.getLuogoResidenza().setCivicoStr(params.get("anagraficaCivicoResidenza"));
            }
        }
        if (params.get("anagraficaComuneDomicilio") != null) {
            Comune comuneDomicilio = comuneDao.findByCodCom(params.get("anagraficaComuneDomicilio"));
            if (comuneDomicilio != null) {
                anagrafe.getLuogoDomicilio().setComune(comuneDomicilio);
            } else {
                anagrafe.getLuogoDomicilio().setComuneStr(params.get("anagraficaComuneDomicilio"));
            }
            if (comuneDomicilio != null && !comuneDomicilio.getToponomasticaList().isEmpty()) {
                Toponomastica toponomastica = comuneDomicilio.getToponomastica(params.get("anagraficaViaDomicilio"));
                Preconditions.checkArgument(toponomastica != null, "via domicilio %s non presente nello stradario", params.get("anagraficaViaDomicilio"));
                anagrafe.getLuogoDomicilio().setVia(toponomastica);
                ToponomasticaCivici toponomasticaCivici = toponomastica.getToponomasticaCivici(params.get("anagraficaCivicoDomicilio"));
                Preconditions.checkArgument(toponomasticaCivici != null, "numero civico domicilio %s non presente nello stradario", params.get("anagraficaCivicoDomicilio"));
                anagrafe.getLuogoDomicilio().setCivico(toponomasticaCivici);
            } else {
                anagrafe.getLuogoDomicilio().setViaStr(params.get("anagraficaViaDomicilio"));
                anagrafe.getLuogoDomicilio().setCivicoStr(params.get("anagraficaCivicoDomicilio"));
            }
        }

        ParametriIndataDao parametriDao = new ParametriIndataDao(em);
        String nazionalita = params.get("anagraficaNazionalita");
        if (!Strings.isNullOrEmpty(nazionalita)) {
            int id = Integer.valueOf(nazionalita);
            ParametriIndata naz = parametriDao.findByIdParamIndata(id);
            anagrafe.setCodStatoNaz(naz);
        }
        String codStatoCittadinanza = Strings.emptyToNull(params.get("anagraficaCittadinanza"));
        if (codStatoCittadinanza != null) {
            anagrafe.setCodStatoCitt(statoDao.findByCodStato(codStatoCittadinanza));
        }
        String tipologiaResidenzaStr = params.get("anagraficaTipologiaResidenza");
        if (!Strings.isNullOrEmpty(tipologiaResidenzaStr)) {
            anagrafe.setIdParamTipologiaResidenza(parametriDao.findByIdParamIndata(Integer.parseInt(tipologiaResidenzaStr)));
        }

        if (!Strings.isNullOrEmpty(params.get("anagraficaPosizioneAnagrafica"))) {
            ParametriIndata posizioneAnagrafica = parametriDao.findByIdParamIndata(Integer.valueOf(params.get("anagraficaPosizioneAnagrafica")));
            anagrafe.setIdParamPosAna(posizioneAnagrafica);
        } else {
            anagrafe.setIdParamPosAna(null);
        }

        String capDomicilio = params.get("anagraficaCapDomicilio");
        anagrafe.getLuogoDomicilio().setCap(capDomicilio);
        String capResidenza = params.get("anagraficaCapResidenza");
        anagrafe.getLuogoResidenza().setCap(capResidenza);

        anagrafe.setCognomeConiuge(params.get("anagraficaCognomeConiuge"));
        if (!Strings.isNullOrEmpty(params.get("anagraficaSegnalatoDa"))) {
            int segnalatoDaId = Integer.valueOf(params.get("anagraficaSegnalatoDa"));
            ParametriIndata segnalato = parametriDao.findByIdParamIndata(segnalatoDaId);
            anagrafe.setCodSegnDa(segnalato);
        } else {
            anagrafe.setCodSegnDa(null);
        }

        String dataMorteString = params.get("anagraficaDataMorte");
        if (StringUtils.isNotBlank(dataMorteString)) {
            anagrafe.setDtMorte(StringConversion.stringToDate(params.get("anagraficaDataMorte"), "dd/MM/yyyy"));
        } else {
            anagrafe.setDtMorte(null);
        }
        anagrafe.setCodAnaCivilia(null);

        String value;
        if (StringUtils.isNotBlank(value = params.get("anagraficaPartitaIva"))) {
            anagrafe.setPartIva(value);
        }
        //check se mi è arrivato l'educatore
        if(!Strings.isNullOrEmpty(params.get("anagraficaEducatore"))){
            UtentiDao utentiDao = new UtentiDao(em);
            Utenti utente = utentiDao.findByCodUte(params.get("anagraficaEducatore"));
            anagrafe.setCodUteEducatore(utente);
        }
        else {
            anagrafe.setCodUteEducatore(null);
        }
        //Se attivato, calcola lato server il codice fiscale dinamicamente
//        request.getParameter("anagraficaForzaCodiceFiscale");
        return anagrafe;
    }

    public Pai serializeAnagraficaPai(HttpServletRequest request, AnagrafeSoc anagrafe) {
        Pai pai = new Pai();
        int idUOT = Integer.valueOf(request.getParameter("anagraficaUot"));
        ParametriIndataDao parametriDao = new ParametriIndataDao(em);
        ParametriIndata uot = parametriDao.findByIdParamIndata(idUOT);
        pai.setIdParamUot(uot);
        UtentiDao utentiDao = new UtentiDao(em);
        Utenti utente = utentiDao.findByCodUte(request.getParameter("anagraficaAssistenteSociale"));
        pai.setCodUteAs(utente);
        return pai;
    }

    public CartellaSociale serializeAnagraficaCartella(HttpServletRequest request, AnagrafeSoc anagrafe, List<Pai> pai) throws ParseException {
        CartellaSociale cartella = new CartellaSociale();
        cartella.setCodAna(anagrafe.getCodAna());
        cartella.setDtApCs(StringConversion.stringToDate(request.getParameter("anagraficaDataAperturaCartella"), "dd/MM/yyyy"));
        cartella.setAnagrafeSoc(anagrafe);
        cartella.setPaiList(pai);
        return cartella;
    }

    public AnagrafeSoc serializeCondizione(HttpServletRequest request) throws ParseException, Exception {
        String codAnag = request.getParameter("codAnag");
        AnagrafeSocDao dao = new AnagrafeSocDao(em);
        AnagrafeSoc a = dao.findByCodAna(Integer.valueOf(codAnag));
        a.setIbanPagam(request.getParameter("condizioneIBAN"));
        ParametriIndataDao parametriDao = new ParametriIndataDao(em);
        if (request.getParameter("condizioneStatoCivile") != null && !"".equals(request.getParameter("condizioneStatoCivile"))) {
            int statoCivileId = Integer.valueOf(request.getParameter("condizioneStatoCivile"));
            ParametriIndata statoCivile = parametriDao.findByIdParamIndata(statoCivileId);
            a.setIdParamStatoCiv(statoCivile);
        } else {
            throw new Exception("Non è stato inserito lo stato civile");
        }
        if (request.getParameter("condizioneCondizioneProfessionale") != null && !"".equals(request.getParameter("condizioneCondizioneProfessionale"))) {
            int condProfId = Integer.valueOf(request.getParameter("condizioneCondizioneProfessionale"));
            ParametriIndata condizioneProfessionale = parametriDao.findByIdParamIndata(condProfId);
            a.setIdParamCondProf(condizioneProfessionale);
        } else {
            a.setIdParamCondProf(null);
        }
        if (request.getParameter("condizioneAbitazione") != null && !"".equals(request.getParameter("condizioneAbitazione"))) {
            int condAll = Integer.valueOf(request.getParameter("condizioneAbitazione"));
            ParametriIndata condizioneAbitazione = parametriDao.findByIdParamIndata(condAll);
            a.setIdParamTipAll(condizioneAbitazione);
            a.setDtAggAb(new Date());
        } else {
            a.setIdParamTipAll(null);
            a.setDtAggAb(new Date());
        }
        if (request.getParameter("condizioneFormaReddito") != null && !"".equals(request.getParameter("condizioneFormaReddito"))) {
            int formaRedditoId = Integer.valueOf(request.getParameter("condizioneFormaReddito"));
            ParametriIndata formaReddito = parametriDao.findByIdParamIndata(formaRedditoId);
            a.setIdParamRedd(formaReddito);
        } else {
            a.setIdParamRedd(null);
        }
        a.setNote(request.getParameter("condizioneNote"));
        if (request.getParameter("condizioneTitoloStudio") != null && !"".equals(request.getParameter("condizioneTitoloStudio"))) {
            int titoloStudioId = Integer.valueOf(request.getParameter("condizioneTitoloStudio"));
            ParametriIndata titoloStudio = parametriDao.findByIdParamIndata(titoloStudioId);
            a.setIdParamTit(titoloStudio);
        } else {
            a.setIdParamTit(null);
        }
        if (request.getParameter("condizioneCondizioneFamiliare") != null && !"".equals(request.getParameter("condizioneCondizioneFamiliare"))) {
            int condFamId = Integer.valueOf(request.getParameter("condizioneCondizioneFamiliare"));
            ParametriIndata condizioneFamigliare = parametriDao.findByIdParamIndata(condFamId);
            a.setCondFam(condizioneFamigliare);
        } else {
            a.setCondFam(null);
        }
        if (request.getParameter("condizioneStatoFisico") != null && !"".equals(request.getParameter("condizioneStatoFisico"))) {
            int statoFisicoId = Integer.valueOf(request.getParameter("condizioneStatoFisico"));
            ParametriIndata statofisico = parametriDao.findByIdParamIndata(statoFisicoId);
            a.setIdParamStatoFis(statofisico);
        } else {
            a.setIdParamStatoFis(null);
        }
        if (request.getParameter("condizioneRedditoMensile") != null && !"".equals(request.getParameter("condizioneRedditoMensile"))) {
            a.setReddMens(new BigDecimal(request.getParameter("condizioneRedditoMensile").replace(',', '.')));
        } else {
            a.setReddMens(null);
        }
        if (request.getParameter("condizioneAccompagnamento") != null && !"".equals(request.getParameter("condizioneAccompagnamento"))) {
            a.setFlgAccomp(request.getParameter("condizioneAccompagnamento").substring(0, 1).toUpperCase());
        } else {
            a.setFlgAccomp(null);
        }

        String value;
        value = request.getParameter("condizioneRichiestaAssegnoAccompagnamento");
        a.setRichiestaAssegnoAccompagnamento(StringUtils.isBlank(value) ? null : value.charAt(0));

        value = request.getParameter("condizioneDataRichiestaAssegnoAccompagnamento");
        a.setDataRichiestaAssegnoAccompagnamento(StringUtils.isBlank(value) ? null : StringConversion.stringToDate(value, "dd/MM/yyyy"));

        String condizioneInvaliditaCivile = Strings.emptyToNull(request.getParameter("condizioneInvaliditaCivile"));
        a.setPercInvCiv(condizioneInvaliditaCivile == null ? null : Short.valueOf(condizioneInvaliditaCivile.replaceAll("%", "")));
        return a;
    }

    /**
     *
     * @param vistaAnagrafe
     * @return
     * @deprecated use
     * VistaAnagrafeDao(em).vistaAnagraficaToAnagrafeSoc(vistaAnagrafe);
     */
    @Deprecated
    public AnagrafeSoc vistaAnagraficaToAnagrafeSoc(VistaAnagrafe vistaAnagrafe) {
        VistaAnagrafeDao vistaAnagrafeDao = new VistaAnagrafeDao(em);
        return vistaAnagrafeDao.anagrafeComune2AnagrafeWego(vistaAnagrafe);
    }

    public AnagraficaBean vistaAnagraficaToAnagraficaBean(VistaAnagrafe vistaAnagrafe) {
        return anagrafeSocToAnagraficaBean(vistaAnagraficaToAnagrafeSoc(vistaAnagrafe));
    }


    private void cleanFields(AnagrafeSoc anagrafe) {

        for (Luogo luogo : Arrays.asList(anagrafe.getLuogoNascita(), anagrafe.getLuogoDomicilio(), anagrafe.getLuogoResidenza())) {
            luogo.clear();
        }


    }
}
