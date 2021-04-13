/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.xsd.pratica;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiMacroProblematica;
import it.wego.welfarego.persistence.entities.PaiMicroProblematica;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import it.wego.welfarego.xsd.Utils;
import it.wego.welfarego.xsd.pratica.CartellaSociale.AnagraficaUtente;
import it.wego.welfarego.xsd.pratica.CartellaSociale.Pai;
import it.wego.welfarego.xsd.pratica.CartellaSociale.Pai.ElencoInterventi;
import it.wego.welfarego.xsd.pratica.CartellaSociale.Pai.ElencoInterventi.Intervento;
import it.wego.welfarego.xsd.pratica.CartellaSociale.Pai.ElencoInterventi.Intervento.MandatoXml;
import it.wego.welfarego.xsd.pratica.CartellaSociale.Pai.MacroProblematica;
import it.wego.welfarego.xsd.pratica.serializers.AnagraficaSerializer;
import it.wego.welfarego.xsd.pratica.serializers.InterventoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;



/**
 *
 * @author giuseppe
 */
public class Pratica {

    private static final Logger logger = LoggerFactory.getLogger(Pratica.class);
 

    public static PaiEvento serializePaiEvento(String descrizione, PaiIntervento paiIntervento, Utenti utente) throws Exception {
        return serializePaiEvento(paiIntervento.getPai().getAnagrafeSoc(), paiIntervento.getPai(), paiIntervento, descrizione, utente);
    }

    public static PaiEvento serializePaiEvento(AnagrafeSoc anagrafe, it.wego.welfarego.persistence.entities.Pai pai, PaiIntervento intervento, String descrizione, Utenti utente) throws JAXBException, Exception {
        if (utente != null) {
            PaiEvento evento = new PaiEvento();
            evento.setCodPai(pai);
            evento.setDesEvento(descrizione);
            evento.setPaiIntervento(intervento);
            evento.setTsEvePai(new Date());
            evento.setCodUte(utente);
            evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
            String xmlData = getXmlCartellaSociale(anagrafe, pai);
            evento.setPaiDox(xmlData);
            return evento;
        } else {
            throw new Exception("Non sono state trovate le informazioni dell'utente connesso. Aggiornare la pagina");
        }
    }

    public static PaiEvento serializePaiEvento(it.wego.welfarego.persistence.entities.Pai pai, PaiIntervento intervento, it.wego.welfarego.persistence.entities.CartellaSociale cartella, String descrizione, Utenti utente) throws JAXBException, Exception {
        if (utente != null) {
            PaiEvento evento = new PaiEvento();
            evento.setCodPai(pai);
            evento.setDesEvento(descrizione);
            evento.setPaiIntervento(intervento);
            evento.setTsEvePai(new Date());
            evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
            evento.setCodUte(utente);
            AnagrafeSoc anagrafe = cartella.getAnagrafeSoc();
            try {
            	if(anagrafe!=null && pai!=null){
                String xmlData = getXmlCartellaSociale(anagrafe, pai);
                evento.setPaiDox(xmlData);
            	}
            	else {
            		evento.setPaiDox("x");
            	}
            } catch (Exception ex) {
                logger.warn("unable to create pai dox for cartella = {}", cartella);
                logger.warn("TODO, must fix later", ex);
                evento.setPaiDox("x");
            }
            return evento;
        } else {
            throw new Exception("Non sono state trovate le informazioni dell'utente connesso. Aggiornare la pagina");
        }
    }

    @Deprecated
    public static String getXmlCartellaSociale(AnagrafeSoc anagrafe, @Nullable it.wego.welfarego.persistence.entities.Pai pai) throws Exception {
        return getXmlCartellaSociale(anagrafe, pai, null, null, null, null);
    }

    private static String getXmlCartellaSociale(AnagrafeSoc anagrafe, @Nullable it.wego.welfarego.persistence.entities.Pai pai, @Nullable PaiIntervento paiIntervento, @Nullable Function<it.wego.welfarego.xsd.pratica.CartellaSociale, it.wego.welfarego.xsd.pratica.CartellaSociale> postProcessor,@Nullable  String numProt , @Nullable String  dtProt) throws Exception {
        Preconditions.checkNotNull(anagrafe);
        Pratica pratica = new Pratica();
        it.wego.welfarego.xsd.pratica.CartellaSociale cs = pratica.getCartellaSociale(anagrafe, pai, paiIntervento, numProt, dtProt);
        if (postProcessor != null) {
            cs = postProcessor.apply(cs);
        }
//        XmlUtils xmlUtils = XmlUtils.getInstance();  cannot use xmlutils, cause lib conflict 
        JAXBContext context = JAXBContext.newInstance(cs.getClass());
        Marshaller marshaller = context.createMarshaller();
        StringWriter st = new StringWriter();
//        try {
//            return new String(xmlUtils.marshallJaxbObject(cs));
//        } finally {
//            xmlUtils.close();
//        }
        marshaller.marshal(cs, st);
//        String xmlData = st.toString();
        return st.toString();
    }


    public static String getXmlCartellaSociale(PaiIntervento paiIntervento) throws Exception {
        it.wego.welfarego.persistence.entities.Pai pai = paiIntervento.getPai();
        return getXmlCartellaSociale(pai.getAnagrafeSoc(), pai, paiIntervento, null, null, null);
    }
    
    public static String getXmlCartellaSociale(PaiIntervento paiIntervento,String numProt,String dtProt) throws Exception {
        return getXmlCartellaSociale(paiIntervento.getPai().getAnagrafeSoc(), paiIntervento.getPai(), paiIntervento, null, numProt,dtProt);
    }

    public static String getXmlCartellaSociale(final Mandato mandato) throws Exception {
        PaiIntervento paiIntervento = mandato.getPaiIntervento();
        return getXmlCartellaSociale(paiIntervento.getPai().getAnagrafeSoc(), paiIntervento.getPai(), paiIntervento, new Function<it.wego.welfarego.xsd.pratica.CartellaSociale, it.wego.welfarego.xsd.pratica.CartellaSociale>() {
            public CartellaSociale apply(CartellaSociale cartellaSocialeXml) {
                Iterables.removeIf(cartellaSocialeXml.getInterventoCorrente().iterator().next().getMandato(), new Predicate<MandatoXml>() {
                    public boolean apply(MandatoXml mandatoXml) {
                        return !Objects.equal(mandatoXml.getNumeroMandato(), mandato.getNumeroMandato());
                    }
                });
                return cartellaSocialeXml;
            }
        }, null, null);
    }
    
    public static String getXmlCartellaSociale(final Mandato mandato,String numProt,String dtProt) throws Exception {
        PaiIntervento paiIntervento = mandato.getPaiIntervento();
        return getXmlCartellaSociale(paiIntervento.getPai().getAnagrafeSoc(), paiIntervento.getPai(), paiIntervento, new Function<it.wego.welfarego.xsd.pratica.CartellaSociale, it.wego.welfarego.xsd.pratica.CartellaSociale>() {
            public CartellaSociale apply(CartellaSociale cartellaSocialeXml) {
                Iterables.removeIf(cartellaSocialeXml.getInterventoCorrente().iterator().next().getMandato(), new Predicate<MandatoXml>() {
                    public boolean apply(MandatoXml mandatoXml) {
                        return !Objects.equal(mandatoXml.getNumeroMandato(), mandato.getNumeroMandato());
                    }
                });
                return cartellaSocialeXml;
            }
        }, numProt,dtProt);
    }

    public static String getXmlCartellaSociale(it.wego.welfarego.persistence.entities.Pai pai) throws Exception {
        return getXmlCartellaSociale(pai.getAnagrafeSoc(), pai, null, null, null, null);
    }

    private CartellaSociale getCartellaSociale(AnagrafeSoc anagrafe, @Nullable it.wego.welfarego.persistence.entities.Pai pai, @Nullable PaiIntervento paiIntervento,@Nullable String numProt , @Nullable String dtProt ) {
        Preconditions.checkNotNull(anagrafe);
        CartellaSociale cartella = new CartellaSociale();
        InterventoSerializer  interventoSerializer = new InterventoSerializer();
        AnagraficaSerializer anagraficaSerializer = new AnagraficaSerializer();

        AnagraficaUtente anagrafica = anagraficaSerializer.serializeAnagraficaUtente(anagrafe, pai);

        cartella.setAnagraficaUtente(anagrafica);

        if (anagrafe.getCartellaSociale() != null) {
            cartella.setDtApeCartSoc(Utils.dateToItString(anagrafe.getCartellaSociale().getDtApCs()));
            if (anagrafe.getCartellaSociale().getPaiList() != null) {
                for (it.wego.welfarego.persistence.entities.Pai p : anagrafe.getCartellaSociale().getPaiList()) {
                    Pai cp = serializePai(p);
                    cartella.getPai().add(cp);
                }
            }
        } else {
            cartella.setDtApeCartSoc(null);
        }
        if (paiIntervento != null) {
            cartella.setInterventoCorrente(interventoSerializer.serializeIntervento(paiIntervento));
        }
        
        if(numProt!=null){
        	cartella.setNumProt(numProt);
        }
        if(dtProt!=null){
        	cartella.setDtProt(dtProt);
        }
        return cartella;
    }





    //    private void setDiagSocPrinc(it.wego.welfarego.persistence.entities.Pai p, Pai pai) {
//        DecodParamType diagSocPrinc = new DecodParamType();
////        diagSocPrinc.setCodParam(p.getIdParamDiagSoc().getIdParam().getCodParam());
////        diagSocPrinc.setDesParamType(p.getIdParamDiagSoc().getDesParam());
//        pai.setDiagSocPrinc(diagSocPrinc);
//    }



    private Pai serializePai(it.wego.welfarego.persistence.entities.Pai pai) {
        Pai paiXml = new Pai();
        paiXml.setCarAltro(pai.getCarAltro());
        paiXml.setCodPai(BigInteger.valueOf(pai.getCodPai()));
        paiXml.setCodUot(pai.getIdParamUot().getDesParam());
        String assistenteSociale = pai.getCodUteAs() == null ? "" : (pai.getCodUteAs().getNome() + " " + pai.getCodUteAs().getCognome());
        paiXml.setCodUteAs(assistenteSociale);
//        if (p.getIdParamDiagSoc() != null) {
//            setDiagSocPrinc(p, pai);
//        }
        paiXml.setDtApePai(Utils.dateToItString(pai.getDtApePai()));
        paiXml.setDtChiusPai(Utils.dateToItString(pai.getDtChiusPai()));
        paiXml.setDtInvioCsr(Utils.dateToItString(pai.getDtInvioCsr()));
        paiXml.setDtPg(Utils.dateToItString(pai.getDtPg()));
        if (pai.getIdParamFascia() != null) {
            paiXml.setFasciaRedd(new DecodParamType(pai.getIdParamFascia()));
        }

        ElencoInterventi interventi = serializeElencoInterventi(pai.getPaiInterventoList());
        paiXml.setElencoInterventi(interventi);
        if (pai.getFlgAgeSan() != null) {
            paiXml.setFlgAgeSan(SiNoType.fromValue(String.valueOf(pai.getFlgAgeSan()).toUpperCase()));
        }
        if (pai.getFlgCarAltro() != null) {
            paiXml.setFlgCarAltro(SiNoType.fromValue(String.valueOf(pai.getFlgCarAltro()).toUpperCase()));
        }
        if (pai.getFlgCarParr() != null) {
            paiXml.setFlgCarParr(SiNoType.fromValue(String.valueOf(pai.getFlgCarParr()).toUpperCase()));
        }
        if (pai.getFlgCarSa() != null) {
            paiXml.setFlgCarSa(SiNoType.fromValue(String.valueOf(pai.getFlgCarSa()).toUpperCase()));
        }
        if (pai.getFlgCarSs() != null) {
            paiXml.setFlgCarSs(SiNoType.fromValue(String.valueOf(pai.getFlgCarSs()).toUpperCase()));
        }
        if (pai.getFlgCarVol() != null) {
            paiXml.setFlgCarVol(SiNoType.fromValue(String.valueOf(pai.getFlgCarVol()).toUpperCase()));
        }
        if (pai.getFlgCsr() != null) {
            paiXml.setFlgCsr(SiNoType.fromValue(String.valueOf(pai.getFlgCsr()).toUpperCase()));
        }
        if (pai.getIdParamCertificatoL104() != null) {
            paiXml.setCertificatoL104(new DecodParamType(pai.getIdParamCertificatoL104()));
        }
        if (pai.getFlgModCsr() != null) {
            paiXml.setFlgModCsr(SiNoType.fromValue(String.valueOf(pai.getFlgModCsr()).toUpperCase()));
        }
        paiXml.setFlgStatoPai(String.valueOf(pai.getFlgStatoPai()).toUpperCase());
        paiXml.setMotivChius(pai.getMotivChius());
//        pai.setNoteDiagSocPrinc(p.getNoteDiagSoc());
        if (pai.getNumFigli() != null) {
            paiXml.setNumFigli(BigInteger.valueOf(pai.getNumFigli()));
        }
        if (pai.getNumFigliConv() != null) {
            paiXml.setNumFigliConv(BigInteger.valueOf(pai.getNumFigliConv()));
        }
        if (pai.getNumNuc() != null) {
            paiXml.setNumNuc(BigInteger.valueOf(pai.getNumNuc()));
        }
        paiXml.setNumPg(pai.getNumPg());
        if(pai.getIdParamProvvedimentoGiudiziario()!=null){
        paiXml.setProvvGiudiz(new DecodParamType(pai.getIdParamProvvedimentoGiudiziario()));
        }
//        pai.setObiettivi(p.getObiettivi());
                 
        paiXml.setMacroProblematica(Lists.newArrayList(Iterables.transform(pai.getPaiMacroProblematicaList(), new Function<PaiMacroProblematica, MacroProblematica>() {
            public MacroProblematica apply(PaiMacroProblematica paiMacroProblematica) {
                MacroProblematica macroProblematicaXml = new MacroProblematica();
                macroProblematicaXml.setTitolo(paiMacroProblematica.getIpMacroProblematica().getDesParam());
                macroProblematicaXml.setRilevanza(paiMacroProblematica.getIpRilevanza() == null ? null : paiMacroProblematica.getIpRilevanza().getDesParam());
                macroProblematicaXml.setFronteggiamento(paiMacroProblematica.getIpFronteggiamento() == null ? null : paiMacroProblematica.getIpFronteggiamento().getDesParam());
                macroProblematicaXml.setObiettivo(paiMacroProblematica.getIpObiettivoPrevalente() == null ? null : paiMacroProblematica.getIpObiettivoPrevalente().getDesParam());
                macroProblematicaXml.setNote(paiMacroProblematica.getDettaglioNote());
                macroProblematicaXml.setMicroProblematica(Lists.newArrayList(Iterables.transform(paiMacroProblematica.getPaiMicroProblematicaList(), new Function<PaiMicroProblematica, String>() {
                    public String apply(PaiMicroProblematica input) {
                        return input.getIpMicroProblematica().getDesParam();
                    }
                })));
                return macroProblematicaXml;
            }
        })));
        return paiXml;
    }

    private ElencoInterventi serializeElencoInterventi(List<PaiIntervento> paiInterventoList) {

        final InterventoSerializer  interventoSerializer = new InterventoSerializer();

        return new ElencoInterventi(Lists.newArrayList(Iterables.transform(paiInterventoList, new Function<PaiIntervento, Intervento>() {
            public Intervento apply(PaiIntervento intervento) {
                return interventoSerializer.serializeIntervento(intervento);
            }
        })));
    }

    /**
     * ritorna l'xml che serve per il template del riepilogo famiglia anagrafica.
     * @param anaUtente
     * @param familiari
     * @return
     */
    public static String getXmlAnagrafeComunale(VistaAnagrafe anaUtente, List<VistaAnagrafe> familiari){
        it.wego.welfarego.xsd.pratica.FamigliaAnagrafica.AnagraficaUtente anaUtenteXml = new it.wego.welfarego.xsd.pratica.FamigliaAnagrafica.AnagraficaUtente();
        anaUtenteXml.setCodAnaCom(anaUtente.getNumeroIndividuale().toString());
        anaUtenteXml.setCodAnaComFam(String.valueOf(anaUtente.getNumeroFamiglia()));
        anaUtenteXml.setCittadinanza(anaUtente.getCittadinanza());
        anaUtenteXml.setCodFisc(anaUtente.getCodiceFiscale());
        anaUtenteXml.setDataNascita(Utils.dateToItString(anaUtente.getDataNascita()));
        anaUtenteXml.setFlgSex(anaUtente.getSesso());
        InfoIndirizzoType indirizzo = new InfoIndirizzoType();
        indirizzo.setDesVia(anaUtente.getDescrizioneViaResidenza());
        indirizzo.setDesCiv(anaUtente.getDescrizioneCivicoResidenza());
        indirizzo.setCap(anaUtente.getCap());
        anaUtenteXml.setIndirResid(indirizzo);
        InfoTerrType terr = new InfoTerrType();
        terr.setDesComItaEst(anaUtente.getDescrizioneComuneResidenza());
        terr.setDesProvItaEst(anaUtente.getDescrizioneProvinciaResidenza());
        terr.setDesStato(anaUtente.getDescrizioneStatoResidenza());
        anaUtenteXml.setInfoResid(terr);






        return null;
    }

}

