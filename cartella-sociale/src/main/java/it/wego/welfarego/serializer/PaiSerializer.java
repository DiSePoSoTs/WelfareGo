package it.wego.welfarego.serializer;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import it.wego.conversions.StringConversion;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.CartellaDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author giuseppe
 */
public class PaiSerializer {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private EntityManager em;
    
    public PaiSerializer(EntityManager em) {
        this.em = em;
    }
    
    public Pai serializePaiAnagrafica(HttpServletRequest request, CartellaSociale cartella) throws Exception {
        PaiDao dao = new PaiDao(em);
        ParametriIndataDao parametriDao = new ParametriIndataDao(em);
        Pai pai = dao.findLastPai(cartella.getCodAna());
        if (pai != null) {
            String codAssSoc = request.getParameter("anagraficaAssistenteSociale");
          
            if(codAssSoc!=null){
            Utenti utente = new UtentiDao(em).findByCodUte(codAssSoc);
            pai.setCodUteAs(utente);
            }
            String codUot = request.getParameter("anagraficaUot");
            ParametriIndata uot = parametriDao.findByIdParamIndata(Integer.valueOf(codUot));
            pai.setIdParamUot(uot);
            pai.setCodAna(cartella);
            return pai;
        } else {
            pai = createPai(request, parametriDao);
            pai.setCodAna(cartella);
            PaiInterventoDao paiInterventoDao = new PaiInterventoDao(em);
            paiInterventoDao.createPaiInterventoDefault(pai);
            return pai;
        }
    }
    
    private Pai createPai(HttpServletRequest request, ParametriIndataDao parametriDao) throws NumberFormatException {
        Pai pai = new Pai();
        String codAssSoc = request.getParameter("anagraficaAssistenteSociale");
        UtentiDao utentiDao = new UtentiDao(em);
        Utenti utente = utentiDao.findByCodUte(codAssSoc);
        pai.setCodUteAs(utente);
        pai.setDtApePai(new Date());
        pai.setFlgStatoPai(Pai.STATO_APERTO);
        pai.setFasciaDefault(em);
        pai.setNumNuc(new Integer(1));
        ParametriIndata uot = parametriDao.findByIdParamIndata(Integer.valueOf(request.getParameter("anagraficaUot")));
        pai.setIdParamUot(uot);
        em.persist(pai);
        return pai;
    }
    
    public Pai serializePai(HttpServletRequest request, String action) throws Exception {
        Pai pai;
        CartellaDao cartellaDao = new CartellaDao(em);
        ParametriIndataDao parametriDao = new ParametriIndataDao(em);
        String codAnaStr = Strings.emptyToNull(request.getParameter("codAnag"));
        Preconditions.checkNotNull(codAnaStr, "missing codAna");
        int codAna = Integer.valueOf(codAnaStr);
        CartellaSociale cartella = cartellaDao.findByCodAna(codAna);
        String codPaiString = request.getParameter("codPai");
        if (codPaiString != null && !"".equals(codPaiString.trim())
                && !action.equals("copy")) {
            PaiDao paiDao = new PaiDao(em);
            pai = paiDao.findPai(Integer.valueOf(codPaiString));
        } else {
            pai = new Pai();
            pai.setFlgStatoPai(Pai.STATO_APERTO);
            PaiDao dao = new PaiDao(em);
            String assSoc = request.getParameter("assSoc");
            String codUot = request.getParameter("uot");
            Pai lastPai = dao.findLastClosedPai(codAna);
            if (codUot != null && !"".equals(codUot.trim())) {
                ParametriIndata uot = parametriDao.findByIdParamIndata(Integer.valueOf(codUot));
                pai.setIdParamUot(uot);
            } else {
                pai.setIdParamUot(lastPai.getIdParamUot());
            }
            if (assSoc != null && !"".equals(assSoc.trim())) {
                UtentiDao utentiDao = new UtentiDao(em);
                Utenti assistenteSociale = utentiDao.findByCodUte(assSoc);
                pai.setCodUteAs(assistenteSociale);
            } else {
                pai.setCodUteAs(lastPai.getCodUteAs());
            }
            pai.setFasciaDefault(em);
        }
        if (pai.getDtApePai() == null) {
            pai.setDtApePai(new Date());
        }
        if(cartella==null){
        	cartella = new CartellaSociale();
        	cartella.setCodAna(codAna);
        	cartella.setDtApCs(new Date());
        	cartella.setAnagrafeSoc(new AnagrafeSocDao(em).findByCodAna(codAna));
        	cartellaDao.insert(cartella);
        }
        pai.setCodAna(cartella);
        
        String dataChiusuraString = request.getParameter("dataChiusura");
        if (dataChiusuraString != null && !"".equals(dataChiusuraString.trim()) && !action.equals("copy")) {
            pai.setDtChiusPai(StringConversion.itStringToDate(dataChiusuraString));
            pai.setFlgStatoPai(Pai.STATO_CHIUSO);
        }
        String protocolloString = request.getParameter("protocollo");
        pai.setNumPg(protocolloString);
        
        String certificatoL104 = request.getParameter("certificatoL104");
        if (!Strings.isNullOrEmpty(certificatoL104)) {
            pai.setIdParamCertificatoL104(parametriDao.findByIdParamIndata(certificatoL104));
        }
        String provvedimentoGiudiziario = request.getParameter("provvedimentoGiudiziario");
        if (!Strings.isNullOrEmpty(provvedimentoGiudiziario)) {
            pai.setIdParamProvvedimentoGiudiziario(parametriDao.findByIdParamIndata(provvedimentoGiudiziario));
        }
        pai.setMotiv(request.getParameter("motivo"));
        String numeroFigli = request.getParameter("numeroFigli");
        if (numeroFigli != null && !"".equals(numeroFigli)) {
            pai.setNumFigli(Integer.valueOf(numeroFigli));
        } else {
            pai.setNumFigli(null);
        }
        String numeroNucleoFamigliare = request.getParameter("numeroNucleoFamigliare");
        if (numeroNucleoFamigliare != null && !"".equals(numeroNucleoFamigliare)) {
            pai.setNumNuc(Integer.valueOf(numeroNucleoFamigliare));
        } else {
            pai.setNumNuc(1);
        }
        String numeroFigliConviventi = request.getParameter("numeroFigliConviventi");
        if (numeroFigliConviventi != null && !"".equals(numeroFigliConviventi)) {
            pai.setNumFigliConv(Integer.valueOf(numeroFigliConviventi));
        } else {
            pai.setNumFigliConv(null);
        }
        String diagnosiInCaricoSA = request.getParameter("diagnosiInCaricoSA");
        if (diagnosiInCaricoSA != null && "1".equals(diagnosiInCaricoSA)) {
            pai.setFlgCarSa('S');
        } else {
            pai.setFlgCarSa('N');
        }
        String diagnosiInCaricoS = request.getParameter("diagnosiInCaricoS");
        if (diagnosiInCaricoS != null && "2".equals(diagnosiInCaricoS)) {
            pai.setFlgCarSs('S');
        } else {
            pai.setFlgCarSs('N');
        }
        String diagnosiInCaricoV = request.getParameter("diagnosiInCaricoV");
        if (diagnosiInCaricoV != null && "3".equals(diagnosiInCaricoV)) {
            pai.setFlgCarVol('S');
        } else {
            pai.setFlgCarVol('N');
        }
        String diagnosiInCaricoP = request.getParameter("diagnosiInCaricoP");
        if (diagnosiInCaricoP != null && "4".equals(diagnosiInCaricoP)) {
            pai.setFlgCarParr('S');
        } else {
            pai.setFlgCarParr('N');
        }
        String diagnosiInCaricoA = request.getParameter("diagnosiInCaricoA");
        if (diagnosiInCaricoA != null && "5".equals(diagnosiInCaricoA)) {
            pai.setFlgCarAltro('S');
        } else {
            pai.setFlgCarAltro('N');
        }
        pai.setCarAltro(request.getParameter("diagnosiInCaricoAltro"));
        String dataDiagnosi = request.getParameter("dataDiagnosi");
        if (dataDiagnosi != null && !"".equals(dataDiagnosi)) {
            pai.setDtDiag(StringConversion.itStringToDate(dataDiagnosi));
        }

        String dataProtocollo = request.getParameter("dataProtocollo");
        if (dataProtocollo != null && !"".equals(dataProtocollo)) {
            pai.setDtPg(StringConversion.itStringToDate(dataProtocollo));
        }

        new PaiDao(em).removeMacroProblematiche(pai);
        for (Object macroProbId : Maps.filterKeys(request.getParameterMap(), Predicates.containsPattern("^macroProblematiche_.*")).values()) {
            ParametriIndata parametriIndataMacroProb = parametriDao.findByIdParamIndata(((String[]) macroProbId)[0]);
            Validate.notNull(parametriIndataMacroProb);
            String paiMacroProblematicaCod = parametriIndataMacroProb.getIdParam().getCodParam();
            PaiMacroProblematica paiMacroProblematica = new PaiMacroProblematica(new PaiMacroProblematicaPK(pai.getCodPai(), parametriIndataMacroProb.getIdParamIndata()));
            paiMacroProblematica.setDettaglioNote(Strings.emptyToNull(request.getParameter("dettaglioNote_" + paiMacroProblematicaCod)));
            paiMacroProblematica.setIpFronteggiamento(parametriDao.findByIdParamIndata(request.getParameter("fronteggiamento_" + paiMacroProblematicaCod)));
            paiMacroProblematica.setIpRilevanza(parametriDao.findByIdParamIndata(request.getParameter("rilevanza_" + paiMacroProblematicaCod)));
            paiMacroProblematica.setIpObiettivoPrevalente(parametriDao.findByIdParamIndata(request.getParameter("obiettivoPrevalente_" + paiMacroProblematicaCod)));
            for (Object microProbId : Maps.filterKeys(request.getParameterMap(), Predicates.containsPattern("^microProblematiche_" + paiMacroProblematicaCod + "_.*")).values()) {
                PaiMicroProblematica paiMicroProblematica = new PaiMicroProblematica(paiMacroProblematica.getPaiMacroProblematicaPK(), Integer.parseInt(((String[]) microProbId)[0]));
                em.persist(paiMicroProblematica);
                paiMacroProblematica.getPaiMicroProblematicaList().add(paiMicroProblematica);
            }
            em.persist(paiMacroProblematica);
            pai.getPaiMacroProblematicaList().add(paiMacroProblematica);
        }
        
        String isee = request.getParameter("isee");
        if (!Strings.isNullOrEmpty(isee)) {
            pai.setIsee(new BigDecimal(isee.replace(",", ".")));
        } else {
            pai.setIsee(null);
        }
        String isee2 = request.getParameter("isee2");
        if (!Strings.isNullOrEmpty(isee2)) {
            pai.setIsee2(new BigDecimal(isee2.replace(",", ".")));
        } else {
            pai.setIsee2(null);
        }
        String isee3 = request.getParameter("isee3");
        if (!Strings.isNullOrEmpty(isee3)) {
            pai.setIsee3(new BigDecimal(isee3.replace(",", ".")));
        } else {
            pai.setIsee3(null);
        }
        
        String cambioFasciaIsee = request.getParameter("dtCambioFascia");
        if(cambioFasciaIsee!=null && !"".equals(cambioFasciaIsee)){
        	pai.setDtCambioFascia(StringConversion.itStringToDate(cambioFasciaIsee));
        }
        String scadenzaIsee = request.getParameter("dataScadenzaIsee");
        if (scadenzaIsee != null && !"".equals(scadenzaIsee)) {
            pai.setDtScadIsee(StringConversion.itStringToDate(scadenzaIsee));
        } else {
            pai.setDtScadIsee(null);
        }
        String fasciaDiReddito = request.getParameter("fasciaDiReddito");
        if (fasciaDiReddito != null && !"".equals(fasciaDiReddito)) {
            if (Integer.valueOf(fasciaDiReddito) > 0) {
                ParametriIndata fascia = parametriDao.findByIdParamIndata(Integer.valueOf(fasciaDiReddito));
                pai.setIdParamFascia(fascia);
            } else {
                int fasciaDefault = Parametri.getIdFasciaDefault(em);
                ParametriIndata fascia = parametriDao.findByIdParamIndata(fasciaDefault);
                pai.setIdParamFascia(fascia);
            }
        }
        String habitat = request.getParameter("habitat");
        if (habitat != null && !habitat.equals("")) {
            pai.setHabitat(habitat.charAt(0));
        } else {
            pai.setHabitat('N');
        }

        return pai;
    }
    
    public Function<Pai, Map<String,Object>> getPaiSerializer() {
        return new JsonMapTransformer<Pai>() {
            @Override
            public void transformToMap(Pai pai) {
                put("nome", pai.getAnagrafeSoc().getNome());
                put("cognome", pai.getAnagrafeSoc().getCognome());
                put("codPai", String.valueOf(pai.getCodPai()));
                put("dtApePai", StringConversion.dateToItString(pai.getDtApePai()));
                if (pai.getDtChiusPai() != null) {
                    put("dtChiusPai", StringConversion.dateToItString(pai.getDtChiusPai()));
                }
                
                put("dataDiagnosi", StringConversion.dateToItString(pai.getDtDiag()));
                put("motivo", pai.getMotiv());
                if (pai.getDtPg() != null) {
                    put("dataProtocollo", StringConversion.dateToItString(pai.getDtPg()));
                }
                
                if(pai.getDtCambioFascia()!=null){
                	put("dtCambioFascia",StringConversion.dateToItString(pai.getDtCambioFascia()));
                }
                if (String.valueOf(pai.getFlgCarAltro()).equals("S")) {
                    put("diagnosiInCaricoA", "5");
                }
                put("diagnosiInCaricoAltro", pai.getCarAltro());
                if (String.valueOf(pai.getFlgCarParr()).equals("S")) {
                    put("diagnosiInCaricoP", "4");
                }
                if (String.valueOf(pai.getFlgCarSs()).equals("S")) {
                    put("diagnosiInCaricoS", "2");
                }
                if (String.valueOf(pai.getFlgCarSa()).equals("S")) {
                    put("diagnosiInCaricoSA", "1");
                }
                if (String.valueOf(pai.getFlgCarVol()).equals("S")) {
                    put("diagnosiInCaricoV", "3");
                }

                if (pai.getIdParamCertificatoL104() != null) {
                    put("certificatoL104", pai.getIdParamCertificatoL104().getIdParamIndata().toString());
                }
                if (pai.getIdParamProvvedimentoGiudiziario() != null) {
                    put("provvedimentoGiudiziario", pai.getIdParamProvvedimentoGiudiziario().getIdParamIndata().toString());
                }

                put("numeroFigli", String.valueOf(pai.getNumFigli()));
                put("numeroFigliConviventi", String.valueOf(pai.getNumFigliConv()));
                put("numeroNucleoFamigliare", String.valueOf(pai.getNumNuc()));
                if (pai.getNumPg() != null) {
                    put("protocollo", String.valueOf(pai.getNumPg()));
                }
                put("statoPai", String.valueOf(pai.getFlgStatoPai()));
                put("codAnag", String.valueOf(pai.getAnagrafeSoc().getCodAna()));
                put("codUot", String.valueOf(pai.getIdParamUot().getIdParamIndata()));
                if (pai.getIsee() != null) {
                    put("isee", String.valueOf(pai.getIsee()));
                }
                if (pai.getIsee2() != null) {
                    put("isee2", String.valueOf(pai.getIsee2()));
                }
                
                if (pai.getIsee3() != null) {
                    put("isee3", String.valueOf(pai.getIsee3()));
                }
                if (pai.getDtScadIsee() != null) {
                    put("dataScadenzaIsee", StringConversion.dateToItString(pai.getDtScadIsee()));
                }
                if (pai.getIdParamFascia() != null) {
                    put("fasciaDiReddito", String.valueOf(pai.getIdParamFascia().getIdParamIndata()));
                }
                put("affettoDaDemenza", String.valueOf(pai.getFlgDemenza()));
                if (pai.getAdl() != null) {
                    put("numeroADL", String.valueOf(pai.getAdl()));
                }
                put("habitat", String.valueOf(pai.getHabitat()));


                try {
                    for (PaiMacroProblematica paiMacroProblematica : pai.getPaiMacroProblematicaList()) {
                        String paiMacroProblematicaCod = paiMacroProblematica.getIpMacroProblematica().getIdParam().getCodParam();
                        put("macroProblematiche_" + paiMacroProblematicaCod, paiMacroProblematica.getIpMacroProblematica().getIdParamIndata().toString());
                        if (paiMacroProblematica.getIpRilevanza() != null) {
                            put("rilevanza_" + paiMacroProblematicaCod, paiMacroProblematica.getIpRilevanza().getIdParamIndata().toString());
                        }
                        if (paiMacroProblematica.getIpFronteggiamento() != null) {
                            put("fronteggiamento_" + paiMacroProblematicaCod, paiMacroProblematica.getIpFronteggiamento().getIdParamIndata().toString());
                        }
                        if (paiMacroProblematica.getIpObiettivoPrevalente() != null) {
                            put("obiettivoPrevalente_" + paiMacroProblematicaCod, paiMacroProblematica.getIpObiettivoPrevalente().getIdParamIndata().toString());
                        }
                        put("dettaglioNote_" + paiMacroProblematicaCod, paiMacroProblematica.getDettaglioNote());
                        for (PaiMicroProblematica paiMicroProblematica : paiMacroProblematica.getPaiMicroProblematicaList()) {
                            put("microProblematiche_" + paiMacroProblematicaCod + "_" + paiMicroProblematica.getIpMicroProblematica().getIdParam().getCodParam(), paiMicroProblematica.getIpMicroProblematica().getIdParamIndata().toString());
                        }
                    }
                } catch (Exception ex) {
                    logger.warn("TODO:  solve this . . ignoring for now", ex);
                }

            }
        };
    }
    
    public @Nullable Map<String, Object> serializePaiBean(Pai pai) {
        return getPaiSerializer().apply(pai);
    }

}
