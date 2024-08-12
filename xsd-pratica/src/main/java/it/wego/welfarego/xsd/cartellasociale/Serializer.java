/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.xsd.cartellasociale;

import it.wego.welfarego.persistence.entities.AnagrafeFam;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoCivObb;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.xsd.Utils;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.ElencoPersoneRiferimento;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.ElencoPersoneRiferimento.PersonaRiferimento;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.NonPai;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.DiagnosiEObiettivi;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.FondoAutonomiaPossibile;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.FondoAutonomiaPossibile.Dettaglio02;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.NonStrutturato;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.NonStrutturato.Dettaglio;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.RedditoDiBase;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.RedditoDiBase.Dettaglio01;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.Residenziale;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.Residenziale.InterventoCasaAlbergo;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.Residenziale.InterventoComunitaAlloggio;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.Residenziale.InterventoComunitaMadreBambino;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.Residenziale.InterventoResidenzaPolifunzionale;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.Residenziale.InterventoResidenzaPolifunzionaleModuliFasciaA;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.Residenziale.InterventoResidenzaProtetta;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.Residenziale.InterventoResidenzialeSenzaStruttura;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.Residenziale.InterventoRsa;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.ElencoInterventi.Intervento.SpecificitaIntervento.Residenziale.InterventoUtenzaDiversificata;
import it.wego.welfarego.xsd.cartellasociale.ElencoCartelleSociali.CartellaSociale.Pai.Profilo;
import it.wego.welfarego.xsd.pratica.DecodParamType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giuseppe
 */
@Deprecated
public class Serializer {

    private static final String PATTERN = "ddMMyyyy";
    private static final String COMUNE_ESTERO = "000000";
    private static final String ITALIA = "000";
    private static final String COMUNE_TRIESTE = "032006";
    private Map<it.wego.welfarego.persistence.entities.Pai, PaiIntervento> interventiNonPai = new HashMap<it.wego.welfarego.persistence.entities.Pai, PaiIntervento>();

    private Pai serializePai(it.wego.welfarego.persistence.entities.Pai pai) throws ParseException, Exception {
        Pai p = new Pai();
        p.setDataApertura(Utils.dateToString(pai.getDtApePai(), PATTERN));
        if (pai.getDtChiusPai() != null) {
            p.setDataChiusura(Utils.dateToString(pai.getDtChiusPai(), PATTERN));
        }

        ElencoInterventi interventi = serializeElencoInterventi(pai);
        p.setElencoInterventi(interventi);
        p.setNoteChiusura(pai.getMotivChius());
        p.setNumeroProtocollo(pai.getNumPg());
        Profilo profilo = serializeProfilo(pai);
        p.setProfilo(profilo);
        if (pai.getCodAna().getAnagrafeSoc().getCodSegnDa() != null) {
            p.setSegnalazione(pai.getCodAna().getAnagrafeSoc().getCodSegnDa().getIdParam().getCodParam());
        }
        return p;
    }

    /**
     * @deprecated old cartella sociale regionale integration
     */
    @Deprecated
    private NonPai serializeNonPai(it.wego.welfarego.persistence.entities.Pai pai, PaiIntervento intervento) throws ParseException {
        NonPai np = new NonPai();
        AnagrafeSoc anagrafe = pai.getCodAna().getAnagrafeSoc();
        np.setDataApertura(Utils.dateToString(pai.getDtApePai(), PATTERN));
        if (pai.getNumPg() != null) {
            np.setNumeroProtocollo(pai.getNumPg());
        }
        if (anagrafe.getCodSegnDa() != null) {
            np.setSegnalazione(anagrafe.getCodSegnDa().getIdParam().getCodParam());
        }
        //np.setMotivoRicorsoServizio(ITALIA);
        if (anagrafe.getIdParamStatoCiv() != null) {
            np.setStatoCivile(anagrafe.getIdParamStatoCiv().getTxt1Param());
        } else {
            np.setStatoCivile("01");
        }
        if (anagrafe.getIdParamCondProf() != null) {
            np.setCondizioneProfessionale(anagrafe.getIdParamCondProf().getIdParam().getCodParam());
        } else {
            //Defaul: 02 Disoccupato
            np.setCondizioneProfessionale("02");
        }
        if (anagrafe.getCondFam() != null) {
            np.setTipologiaNucleoFamiliare(anagrafe.getCondFam().getIdParam().getCodParam());
        } else {
            //Default: 01	Persona sola
            np.setTipologiaNucleoFamiliare("01");
        }
        if (pai.getNumNuc() != null) {
            np.setNumerositaNucleoFamiliare(String.valueOf(pai.getNumNuc()));
        } else {
            np.setNumerositaNucleoFamiliare("1");
        }
        if (pai.getNumFigli() != null) {
            String zero = "";
            if (pai.getNumFigli().intValue() < 10) {
                zero = "0";
            }
            np.setNumeroFigli(zero + String.valueOf(pai.getNumFigli()));
        } else {
            np.setNumeroFigli("00");
        }
        if (pai.getNumFigliConv() != null) {
            String zero = "";
            if (pai.getNumFigliConv().intValue() < 10) {
                zero = "0";
            }
            np.setNumeroFigliConviventi(zero + String.valueOf(pai.getNumFigliConv()));
        } else {
            np.setNumeroFigliConviventi("00");
        }

        if (pai.getIdParamCertificatoL104() != null) {
            np.setCertificatoL104(new DecodParamType(pai.getIdParamCertificatoL104()));
        }
        np.setInterventoNonPai(InterventoNonPaiType.fromValue(intervento.getTipologiaIntervento().getCodIntCsr()));
        //La forzo per ora alla data di apertura
        np.setDataChiusura(Utils.dateToString(pai.getDtApePai(), PATTERN));
        int eta = Utils.calcolaEta(anagrafe.getDtNasc(), pai.getDtApePai());
        np.setEta(String.valueOf(eta));
        TipologiaUtenteType tipo = getTipologiaUtente(eta);
        np.setTipologiaUtente(tipo);
        return np;
    }

    private ElencoPersoneRiferimento serializePersoneRiferimento(AnagrafeSoc anag) throws ParseException {
        ElencoPersoneRiferimento epr = new ElencoPersoneRiferimento();
        for (AnagrafeFam f : anag.getAnagrafeFamListAsSource()) {
            AnagrafeSoc soc = f.getAnagrafeSocTarget();
            if (soc.getCodAna() != anag.getCodAna()) {
                PersonaRiferimento pr = serializePersonaRiferimento(soc, f.getCodQual());
                epr.getPersonaRiferimento().add(pr);
            }
        }
        for (AnagrafeFam f : anag.getAnagrafeFamListAsTarget()) {
            AnagrafeSoc soc = f.getAnagrafeSocSource();
            if (soc.getCodAna() != anag.getCodAna()) {
                PersonaRiferimento pr = serializePersonaRiferimento(soc, f.getCodQual()); // !!! TODO GET REVERSE QUAL !!!
                epr.getPersonaRiferimento().add(pr);
            }
        }
        return epr;
    }

    private PersonaRiferimento serializePersonaRiferimento(AnagrafeSoc a, ParametriIndata relazione) throws ParseException {
        PersonaRiferimento pr = new PersonaRiferimento();
        pr.setCampoNote(a.getNote());
        pr.setCodiceFiscale(a.getCodFisc().toUpperCase());
        pr.setCodiceIndividualeAnagrafeComunale(a.getCodAnaCom());
        pr.setCodiceNucleoFamiliareAnagrafeComunale(a.getCodAnaFamCom());
        pr.setCognome(a.getCognome());
        if (a.getLuogoResidenza().getComune() != null) {
            pr.setComuneResidenza(a.getLuogoResidenza().getComune().getComunePK().getCodCom());
        } else {
            pr.setComuneResidenza(COMUNE_ESTERO);
        }
        if (a.getIdParamCondProf() != null) {
            pr.setCondizioneProfessionale(a.getIdParamCondProf().getIdParam().getCodParam());
        } else {
            //Defaul: 02 Disoccupato
            pr.setCondizioneProfessionale("02");
        }
        pr.setDataNascita(Utils.dateToString(a.getDtNasc(), PATTERN));

        if (a.getLuogoResidenza().getVia() != null) {
            String indirizzo = a.getLuogoResidenza().getVia().getDesVia();
            if (a.getLuogoResidenza().getCivico() != null) {
                indirizzo = indirizzo + " " + a.getLuogoResidenza().getCivico().getDesCiv();
            }
            pr.setIndirizzoResidenza(indirizzo);
        } else {
            pr.setIndirizzoResidenza(a.getLuogoResidenza().getViaStr() + " " + a.getLuogoResidenza().getCivicoStr());
        }
        InformazioniTerritorialiType infoNascita = serializeInformazioniTerritorialiNascita(a);
        pr.setInformazioniTerritorialiNascita(infoNascita);
        StatoType nazionalita = serializeNazionalita(a);
        pr.setNazionalita(nazionalita);
        pr.getCittadinanza().add(nazionalita);
        pr.setNome(a.getNome());
        if (a.getIdParamPosAna() != null) {
            pr.setPosizioneAnagrafica(a.getIdParamPosAna().getTxt1Param());
        } else {
            //Default residente
            pr.setPosizioneAnagrafica("10");
        }
        if (a.getFlgSex() != null) {
            pr.setSesso(SessoType.fromValue(a.getFlgSex().toString().toUpperCase()));
        } else {
            pr.setSesso(SessoType.M);
        }

        if (a.getIdParamStatoCiv() != null) {
            pr.setStatoCivile(a.getIdParamStatoCiv().getTxt1Param());
        } else {
            //Default Celibe o Nubile
            pr.setStatoCivile("01");
        }
        pr.setTelefonoCasa(a.getNumTel());
        pr.setTelefonoCellulare(a.getNumCell());
        if (relazione != null) {
            String zero = "";
            if (Integer.valueOf(relazione.getIdParam().getCodParam()).intValue() < 10) {
                zero = "0";
            }
            pr.setTipoRelazione(zero + relazione.getIdParam().getCodParam());
        } else {
            //Default: 24	Altra relazione di affinita
            pr.setTipoRelazione("24");
        }
        if (a.getIdParamTit() != null) {
            pr.setTitoloStudio(a.getIdParamTit().getIdParam().getCodParam());
        } else {
            //Default: 99	Non rilevabile
            pr.setTitoloStudio("99");
        }
        //TODO: campi mancanti ma non obbligatori
        //pr.setCompartecipazioneSpesa(SiNoType.S);
        //pr.setTelefonoLavoro(null);
        return pr;
    }

    /**
     * @deprecated old cartella sociale regionale integration, unused
     */
    @Deprecated
    private DiagnosiEObiettivi serializeDiagnosiObiettivi(it.wego.welfarego.persistence.entities.Pai pai, boolean empty) throws ParseException {
        DiagnosiEObiettivi deo = new DiagnosiEObiettivi();
        if (empty) {
            deo.setDataCompilazione(Utils.dateToString(pai.getDtApePai(), PATTERN));
            deo.setNoteIntegrativeDiagnosi("");
            deo.setObiettivi("");
            //Default: Minore in stato di abbandono
            deo.setDiagnosiSocialePrevalente(DiagnosiSocialeType.C_02);
        } else {
            deo.setDataCompilazione(Utils.dateToString(pai.getDtApePai(), PATTERN));
            deo.setObiettivi("");
        }
        return deo;
    }

    private StatoType serializeNazionalita(AnagrafeSoc anag) {
        StatoType stato = new StatoType();
        if (anag.getCodStatoNaz() != null) {
            if (anag.getCodStatoNaz().getIdParam().getCodParam().equals("0")) {
                stato.setStatoItaliano("000");
            } else {
                stato.setStatoNonItaliano(anag.getCodStatoNaz().getIdParam().getCodParam());
            }
        } else {
            //Forzo la nazionalità ad italiana
            stato.setStatoItaliano("000");
        }
        return stato;
    }

    private Profilo serializeProfilo(it.wego.welfarego.persistence.entities.Pai pai) {
        Profilo profilo = new Profilo();
        AnagrafeSoc a = pai.getCodAna().getAnagrafeSoc();
        //TODO: Non obbligatorio ma lo devo forzare a S
        profilo.setUtentePregresso(SiNoType.S);
        if (a.getFlgAccomp() != null) {
            profilo.setAssegnoAccompagnamento(SiNoType.fromValue(a.getFlgAccomp().toString().toUpperCase()));
        } else {
            //Default No
            profilo.setAssegnoAccompagnamento(SiNoType.N);
        }
        if (a.getLuogoDomicilio().getComune() != null) {
            profilo.setComuneDomicilio(a.getLuogoDomicilio().getComune().getComunePK().getCodCom());
        } else {
            profilo.setComuneDomicilio(COMUNE_ESTERO);
        }
        if (a.getIdParamTipAll() != null) {
            profilo.setCondizioneAbitativa(a.getIdParamTipAll().getTxt1Param());
        }
        if (a.getIdParamCondProf() != null) {
            profilo.setCondizioneProfessionale(a.getIdParamCondProf().getIdParam().getCodParam());
        } else {
            //Default: 02 Disoccupato
            profilo.setCondizioneProfessionale("02");
        }
        if (pai.getIdParamCertificatoL104() != null) {
            profilo.setCertificatoL104(new DecodParamType(pai.getIdParamCertificatoL104()));
        }
        int eta = Utils.calcolaEta(a.getDtNasc(), pai.getDtApePai());
        profilo.setEta(String.valueOf(eta));
        if (pai.getIdParamFascia() != null) {
            profilo.setFasceRedditoLavoroDipendente(pai.getIdParamFascia().getIdParam().getCodParam());
        } else {
            //Default: 99 Oltre 30.000 euro
            profilo.setFasceRedditoLavoroDipendente("99");
        }
        if (a.getLuogoDomicilio().getVia() != null) {
            String indirizzo = a.getLuogoDomicilio().getVia().getDesVia();
            if (a.getLuogoDomicilio().getCivico() != null) {
                indirizzo = indirizzo + a.getLuogoDomicilio().getCivico().getDesCiv();
            }
            profilo.setIndirizzoDomicilio(indirizzo);
        } else {
            profilo.setIndirizzoDomicilio(a.getLuogoDomicilio().getViaStr() + " " + a.getLuogoDomicilio().getCivicoStr());
        }
        if (pai.getIsee() != null) {
            ImportoType isee = serializeImporto(pai.getIsee());
            profilo.setIsee(isee);
        } else {
            ImportoType isee = serializeImporto(BigDecimal.ZERO);
            profilo.setIsee(isee);
        }
        if (a.getMedicoBase() != null) {
            if (a.getMedicoBase().length() > 50) {
                profilo.setMedicoMedicinaGenerale(a.getMedicoBase().substring(0, 49));
            } else {
                profilo.setMedicoMedicinaGenerale(a.getMedicoBase());
            }
        }
        if (pai.getNumFigli() != null) {
            String zero = "";
            if (pai.getNumFigli().intValue() < 10) {
                zero = "0";
            }
            profilo.setNumeroFigli(zero + String.valueOf(pai.getNumFigli()));
        } else {
            profilo.setNumeroFigli("00");
        }
        if (pai.getNumFigliConv() != null) {
            String zero = "";
            if (pai.getNumFigliConv().intValue() < 10) {
                zero = "0";
            }
            profilo.setNumeroFigliConviventi(zero + String.valueOf(pai.getNumFigliConv()));
        } else {
            profilo.setNumeroFigliConviventi("00");
        }
        if (pai.getNumNuc() != null) {
            profilo.setNumerositaNucleoFamiliare(String.valueOf(pai.getNumNuc()));
        } else {
            profilo.setNumerositaNucleoFamiliare("1");
        }
        //TODO: campo non presente e obbligatorio
        //Default: 00	 Assenza di richieste / provvedimenti giudiziari
        profilo.setProvvedimentiGiudiziari("00");

        if (a.getIdParamStatoCiv() != null) {
            profilo.setStatoCivile(a.getIdParamStatoCiv().getTxt1Param());
        } else {
            //Stato civile default: 01	Celibe o Nubile
            profilo.setStatoCivile("01");
        }
        profilo.setTelefonoDomicilio(a.getNumTel());
        if (a.getCondFam() != null) {
            profilo.setTipologiaNucleoFamiliare(a.getCondFam().getIdParam().getCodParam());
        } else {
            //Default: 01	Persona sola
            profilo.setTipologiaNucleoFamiliare("01");
        }

        TipologiaUtenteType tipoUtente = getTipologiaUtente(eta);
        profilo.setTipologiaUtente(tipoUtente);
        if (a.getIdParamTit() != null) {
            profilo.setTitoloStudio(a.getIdParamTit().getIdParam().getCodParam());
        } else {
            //Default: 99	Non rilevabile
            profilo.setTitoloStudio("99");
        }
        return profilo;
    }

    private InformazioniTerritorialiType serializeInformazioniTerritorialiNascita(AnagrafeSoc a) {
        InformazioniTerritorialiType info = new InformazioniTerritorialiType();
        if (a.getLuogoNascita().getStato() != null) {
            if (a.getLuogoNascita().getStato().getCodStato().equals(ITALIA)) {
                InformazioniTerritorialiItalianeType it = new InformazioniTerritorialiItalianeType();
                it.setStatoNascita(ITALIA);
                it.setComuneItalianoNascita(a.getLuogoNascita().getComune().getComunePK().getCodCom());
                it.setComuneStranieroNascita("");
                info.setInformazioniTerritorialiItaliane(it);
            } else {
                InformazioniTerritorialiNonTalianeType noit = new InformazioniTerritorialiNonTalianeType();
                noit.setStatoNascita(a.getLuogoNascita().getStato().getCodStato());
                noit.setComuneStranieroNascita(a.getLuogoNascita().getComuneStr());
                noit.setComuneItalianoNascita("");
                info.setInformazioniTerritorialiNonItaliane(noit);
            }
        } else {
            //Se non ho informazioni sulla nascita forzo a Trieste
            InformazioniTerritorialiItalianeType it = new InformazioniTerritorialiItalianeType();
            it.setStatoNascita(ITALIA);
            it.setComuneItalianoNascita(COMUNE_TRIESTE);
            it.setComuneStranieroNascita("");
            info.setInformazioniTerritorialiItaliane(it);
        }
        return info;
    }

    private ImportoType serializeImporto(BigDecimal i) {
        ImportoType importo = new ImportoType();
        BigDecimal[] d = i.divideAndRemainder(BigDecimal.ONE);
        String parteIntera = String.valueOf(d[0].intValue());
        BigDecimal decimal = i.remainder(BigDecimal.ONE).setScale(2, BigDecimal.ROUND_HALF_UP).movePointRight(2);
        String parteDecimale = decimal.toString();
        importo.setImportoParteDecimale(parteDecimale);
        importo.setImportoParteIntera(parteIntera);
        return importo;
    }

    private ElencoInterventi serializeElencoInterventi(it.wego.welfarego.persistence.entities.Pai pai) throws ParseException, Exception {
        ElencoInterventi interventi = new ElencoInterventi();
        for (PaiIntervento i : pai.getPaiInterventoList()) {
            //Salto l'intervento di default
            if (!i.isInterventoAccesso() ) {
                String codIntCSR = i.getTipologiaIntervento().getCodIntCsr();
                if (codIntCSR != null) {
                    if (!isInterventoNonPai(codIntCSR)) {
                        Intervento intervento = serializeIntervento(pai.getCodAna().getAnagrafeSoc(), i);
                        if (intervento != null) {
                            interventi.getIntervento().add(intervento);
                        }
                    } else {
                        interventiNonPai.put(pai, i);
                    }
                } else {
                    continue;
                }
            }
        }
        return interventi;
    }

    private Intervento serializeIntervento(AnagrafeSoc anag, PaiIntervento i) throws ParseException, Exception {
        Intervento intervento = new Intervento();
        SpecificitaIntervento specificita = serializeSpecificitaIntervento(i);
        if (specificita != null) {
            intervento.setSpecificitaIntervento(specificita);
            if (i.getPaiInterventoCivObbList() != null) {
                List<PaiInterventoCivObb> civObbList = i.getPaiInterventoCivObbList();
                BigDecimal importo = BigDecimal.ZERO;
                for (PaiInterventoCivObb civObb : civObbList) {
                    importo.add(civObb.getImpCo());
                }
                ImportoType civObb = serializeImporto(importo);
                intervento.setContributoCaricoCivilmenteObbligati(civObb);
            }
            intervento.setDataAtto(Utils.dateToString(i.getDtAvvio(), PATTERN));
            intervento.setDataInizio(Utils.dateToString(i.getDtAvvio(), PATTERN));
            if (i.getDtChius() != null) {
                intervento.setDataChiusura(Utils.dateToString(i.getDtChius(), PATTERN));
            }
            if (i.getDurMesi() != null) {
                //TODO: Solo per test: RIMUOVERE
                if (i.getDurMesi().intValue() == 0) {
                    intervento.setDurata("1");
                } else {
                    intervento.setDurata(String.valueOf(i.getDurMesi()));
                }

            }
            int eta = Utils.calcolaEta(anag.getDtNasc(), i.getDtApe());
            intervento.setEtaAllaDataAperturaIntervento(BigInteger.valueOf(eta));
            if (eta < 18) {
                throw new Exception("L'età alla data di apertura dell'intervento non può essere inferiore ai 18 anni");
            }

            intervento.setIndicatoreEsito(i.getIndEsitoInt());
            intervento.setNoteChiusura(i.getNoteChius());
            return intervento;

        } else {
            return null;
        }
    }

    private SpecificitaIntervento serializeSpecificitaIntervento(PaiIntervento intervento) {
        SpecificitaIntervento s = null;
        String codIntCsr = intervento.getTipologiaIntervento().getCodIntCsr();
        try {
            TipoInterventoFondoAutonomiaPossibileType.fromValue(codIntCsr);
            FondoAutonomiaPossibile fondoAutonomiaPossibile = serializeFondoAutonomiaPossibile(intervento);
            s = new SpecificitaIntervento();
            s.setFondoAutonomiaPossibile(fondoAutonomiaPossibile);
            return s;
        } catch (IllegalArgumentException ex) {
        }
        try {
            AltroTipoInterventoType.fromValue(codIntCsr);
            s = new SpecificitaIntervento();
            NonStrutturato nonStrutturato = serializeNonStrutturato(intervento);
            s.setNonStrutturato(nonStrutturato);
            return s;
        } catch (IllegalArgumentException ex) {
        }
        try {
            TipoInterventoRedditoDiBaseType.fromValue(codIntCsr);
            s = new SpecificitaIntervento();
            RedditoDiBase redditoBase = serializeRedditoBase(intervento);
            s.setRedditoDiBase(redditoBase);
            return s;
        } catch (IllegalArgumentException ex) {
        }
        try {
            TipoInterventoResidenzialeMaggiorenneType.fromValue(codIntCsr);
            s = new SpecificitaIntervento();
            Residenziale residenziale = serializeResidenziale(intervento);
            s.setResidenziale(residenziale);
            return s;
        } catch (IllegalArgumentException ex) {
        }

        return s;
    }

    private TipologiaUtenteType getTipologiaUtente(int eta) {
        if (eta < 18) {
            return TipologiaUtenteType.A_01;
        } else if (eta >= 18 && eta < 35) {
            return TipologiaUtenteType.A_02;
        } else if (eta > 36 && eta < 64) {
            return TipologiaUtenteType.A_03;
        } else if (eta > 66 && eta < 75) {
            return TipologiaUtenteType.A_04;
        } else {
            return TipologiaUtenteType.A_05;
        }
    }

    private FondoAutonomiaPossibile serializeFondoAutonomiaPossibile(PaiIntervento intervento) {
        FondoAutonomiaPossibile f = new FondoAutonomiaPossibile();
        f.setCorrelatoConFondoAutonomiaPossibile(SiNoType.S);
        f.setCorrelatoConRedditoBase(SiNoType.N);
        Dettaglio02 dettaglio = serializeDettaglio02(intervento);
        f.setDettaglio02(dettaglio);
        return f;
    }

    private NonStrutturato serializeNonStrutturato(PaiIntervento intervento) {
        NonStrutturato ns = new NonStrutturato();
        if (intervento.getTipologiaIntervento().getFlgRdbfap() != null) {
            if (intervento.getTipologiaIntervento().getFlgRdbfap() == 'F') {
                ns.setCorrelatoConFondoAutonomiaPossibile(SiNoType.S);
            } else {
                ns.setCorrelatoConFondoAutonomiaPossibile(SiNoType.N);
            }
            if (intervento.getTipologiaIntervento().getFlgRdbfap() == 'R') {
                ns.setCorrelatoConRedditoBase(SiNoType.S);
            } else {
                ns.setCorrelatoConRedditoBase(SiNoType.N);
            }
        } else {
            ns.setCorrelatoConFondoAutonomiaPossibile(SiNoType.N);
            ns.setCorrelatoConRedditoBase(SiNoType.N);
        }
        Dettaglio dettaglio = serializeDettaglio(intervento);
        ns.setDettaglio(dettaglio);
        return ns;
    }

    private RedditoDiBase serializeRedditoBase(PaiIntervento intervento) {
        RedditoDiBase r = new RedditoDiBase();
        if (intervento.getTipologiaIntervento().getFlgRdbfap() != null) {
            if (intervento.getTipologiaIntervento().getFlgRdbfap() == 'F') {
                r.setCorrelatoConFondoAutonomiaPossibile(SiNoType.S);
            } else {
                r.setCorrelatoConFondoAutonomiaPossibile(SiNoType.N);
            }
            if (intervento.getTipologiaIntervento().getFlgRdbfap() == 'R') {
                r.setCorrelatoConRedditoBase(SiNoType.S);
            } else {
                r.setCorrelatoConRedditoBase(SiNoType.N);
            }
        } else {
            r.setCorrelatoConFondoAutonomiaPossibile(SiNoType.N);
            r.setCorrelatoConRedditoBase(SiNoType.N);
        }
        Dettaglio01 dettaglio = serializeDettaglio01(intervento);
        r.setDettaglio01(dettaglio);
        return r;
    }

    private Residenziale serializeResidenziale(PaiIntervento intervento) {
        Residenziale r = new Residenziale();
        if (intervento.getTipologiaIntervento().getFlgRdbfap() != null) {
            if (intervento.getTipologiaIntervento().getFlgRdbfap() == 'F') {
                r.setCorrelatoConFondoAutonomiaPossibile(SiNoType.S);
            }
            if (intervento.getTipologiaIntervento().getFlgRdbfap() == 'R') {
                r.setCorrelatoConRedditoBase(SiNoType.S);
            }
        } else {
            r.setCorrelatoConFondoAutonomiaPossibile(SiNoType.N);
            r.setCorrelatoConRedditoBase(SiNoType.N);
        }
        serializeResidenza(r, intervento);
        return r;
    }

    private Dettaglio02 serializeDettaglio02(PaiIntervento p) {
        Dettaglio02 d = new Dettaglio02();
        String demenza = getValoreDatoSpecifico(p, "ds_demenza");
        if (demenza != null && !"".equals(demenza)) {
            d.setAffettoDemenza(SiNoType.fromValue(demenza));
        } else {
            //Codice per retrocompatibilità
            if (p.getPai().getFlgDemenza() != null) {
                demenza = String.valueOf(p.getPai().getFlgDemenza());
                SiNoType affettoDaDemenza = SiNoType.fromValue(demenza);
                d.setAffettoDemenza(affettoDaDemenza);
            } else {
                d.setAffettoDemenza(SiNoType.N);
            }
        }

        d.setDurata(p.getDurMesi());
        Date dataNascita = p.getPai().getCodAna().getAnagrafeSoc().getDtNasc();
        int eta = Utils.calcolaEta(dataNascita, p.getDtApe());
        if (d.getAffettoDemenza().value().equals("N") && eta > 17) {
            String adl = getValoreDatoSpecifico(p, "ds_adl");
            if (adl != null && !"".equals(adl)) {
                d.setNonAutosufficienza(Integer.valueOf(adl));
            } else {
                //Codice per retrocompatibilità
                if (p.getPai().getAdl() != null) {
                    int valAdl = p.getPai().getAdl().intValue();
                    d.setNonAutosufficienza(valAdl);
                } else {
                    d.setNonAutosufficienza(0);
                }
            }
        }
        d.setEtaAllaDataAperturaIntervento(BigInteger.valueOf(eta));
        if (p.getPai().getIsee() != null) {
            ImportoType isee = serializeImporto(p.getPai().getIsee());
            d.setIsee(isee);
        } else {
            ImportoType isee = serializeImporto(BigDecimal.ZERO);
            d.setIsee(isee);
        }
        //Forzo il motivo della chiusura a
        //5	Altro motivo
        d.setMotivoChiusura("5");

        String codIntCsr = p.getTipologiaIntervento().getCodIntCsr();
        d.setTipoInterventoFap(TipoInterventoFondoAutonomiaPossibileType.fromValue(codIntCsr));
        //TODO: campi non obbligatori nell'XSD ma richiesti dall'applicativo
       // String tipoFap = getValoreDatoSpecifico(p, "ds_tip_fap");
        String tipoFap = p.getTipologiaIntervento().getCodTipint();
        BigDecimal imp = BigDecimal.ZERO;
        if (p.getCostoPrev() != null) {
            imp = p.getCostoPrev();
        } else {
            for (PaiInterventoMese m : p.getPaiInterventoMeseList()) {
                imp = imp.add(m.getBdgPrevEur());
            }
        }
        ImportoType importo = serializeImporto(imp);
        d.setAssegnoAutonomiaApa(serializeImporto(BigDecimal.ZERO));
        d.setContributoAiutoFamiliare(serializeImporto(BigDecimal.ZERO));
        d.setSostegnoVitaIndipendente(serializeImporto(BigDecimal.ZERO));
        d.setSostegnoProgettiSaluteMentale(serializeImporto(BigDecimal.ZERO));
        if (tipoFap != null) {
            if (tipoFap.contains("AZ008")) {
                d.setAssegnoAutonomiaApa(importo);
            } else if (tipoFap.contains("AZ007")) {
                d.setContributoAiutoFamiliare(importo);
            } else if (tipoFap.equals("AZ009")) {
                d.setSostegnoVitaIndipendente(importo);
            }else if (tipoFap.equals("AZ010")) {
                d.setSostegnoAltreFormediEmancipazione(importo);
            } else {
                d.setSostegnoProgettiSaluteMentale(serializeImporto(BigDecimal.ONE));
            }
        }
        //TODO: campi non obbligatori e non richiesti
        String ore = getValoreDatoSpecifico(p, "ds_ore_sett_badante");
        if (ore != null && !"".equals(ore)) {
            d.setOreContratto(Integer.valueOf(ore));
        }
        return d;
    }

    private Dettaglio serializeDettaglio(PaiIntervento p) {
        Dettaglio d = new Dettaglio();
        String codIntCsr = p.getTipologiaIntervento().getCodIntCsr();
        d.setAltroTipoIntervento(AltroTipoInterventoType.fromValue(codIntCsr));
        return d;
    }

    private Dettaglio01 serializeDettaglio01(PaiIntervento p) {
        Dettaglio01 d = new Dettaglio01();
        String codIntCsr = p.getTipologiaIntervento().getCodIntCsr();
        d.setTipoInterventoRdb(TipoInterventoRedditoDiBaseType.fromValue(codIntCsr));
        //TODO: campi mancanti e non obbligatori
        //d.setCoinvolgimentoNucleoFamiliare(SiNoType.S);
        //d.setDataDichiarazioneDisponibilita(ITALIA);
        //d.setDataPattoDefinitivo(ITALIA);
        //d.setDataPattoPreliminare(ITALIA);
        //d.setDataPattoServizio(ITALIA);
        //d.setDataRiavvio(ITALIA);
        //d.setDataSospensioneRdb(ITALIA);
        //d.setEsitoValutazioneObiettiviPatto(null);
        //d.setIdentificativoCee(ITALIA);
        //d.setImportoFinaleErogatoRdb(null);
        //d.setImportoMaggiorazioneRdb(null);
        //d.setImportoRdb(null);
        //d.setImportoRdbDaErogare(null);
        //d.setImportoRdbErogatoSospensione(null);
        //d.setImportoRdbGiaErogato(null);
        //d.setImportoRdbRidefinitoRiavvio(null);
        //d.setMotivoChiusuraRdb(ITALIA);
        //d.setMotivoSospensioneRdb(ITALIA);
        //d.setNotePatto(ITALIA);
        //d.setObiettiviPatto(null);
        //d.setOccupazione(ITALIA);
        //d.setProgettoPersonalizzato(SiNoType.S);
        //d.setRedditoCee(null);
        //d.setSoggettiCoinvolti(null);
        //d.setVersione(ITALIA);
        return d;
    }

    private void serializeResidenza(Residenziale r, PaiIntervento intervento) {
        String codIntCsr = intervento.getTipologiaIntervento().getCodIntCsr();
        if (codIntCsr.equals("de2200")) {
            InterventoComunitaMadreBambino interventoComunitaMadreBambino = serializeComunitaMadreBambino(intervento);
            r.setInterventoComunitaMadreBambino(interventoComunitaMadreBambino);
        } else if (codIntCsr.equals("de1200")) {
            InterventoCasaAlbergo interventoCasaAlbergo = serializerInterventoCasaAlbergo(intervento);
            r.setInterventoCasaAlbergo(interventoCasaAlbergo);
        } else if (codIntCsr.equals("de1400")) {
            InterventoResidenzaProtetta interventoResidenzaProtetta = serializeInterventoResidenzaProtetta(intervento);
            r.setInterventoResidenzaProtetta(interventoResidenzaProtetta);
        } else if (codIntCsr.equals("de1300")) {
            InterventoComunitaAlloggio interventoComunitaAlloggio = serializeInterventoComunitaAlloggio(intervento);
            r.setInterventoComunitaAlloggio(interventoComunitaAlloggio);
        } else if (codIntCsr.equals("de1800")) {
            InterventoRsa interventoRsa = serializeInterventoRsa(intervento);
            r.setInterventoRsa(interventoRsa);
        } else if (codIntCsr.equals("de1500")) {
            InterventoUtenzaDiversificata interventoUtenzaDiversificata = serializeInterventoutenzaDiversificata(intervento);
            r.setInterventoUtenzaDiversificata(interventoUtenzaDiversificata);
        } else if (codIntCsr.equals("de1600")) {
            InterventoResidenzaPolifunzionale interventoResidenzaPolifunzionale = serializeInterventoresidenzaPolifunzionale(intervento);
            r.setInterventoResidenzaPolifunzionale(interventoResidenzaPolifunzionale);
        } else if (codIntCsr.equals("de1700")) {
            InterventoResidenzaPolifunzionaleModuliFasciaA interventoResidenzaPolifunzionale = serializeInterventoresidenzaPolifunzionaleFasciaA(intervento);
            r.setInterventoResidenzaPolifunzionaleModuliFasciaA(interventoResidenzaPolifunzionale);
        } else {
            InterventoResidenzialeSenzaStruttura interventoResidenzialeSenzaStruttura = serializeInterventoResidenzialeSenzaStruttura(intervento);
            r.setInterventoResidenzialeSenzaStruttura(interventoResidenzialeSenzaStruttura);
        }
    }

    private InterventoComunitaMadreBambino serializeComunitaMadreBambino(PaiIntervento p) {
        InterventoComunitaMadreBambino i = new InterventoComunitaMadreBambino();
        i.setTipoInterventoResidenzialeMaggiorenne(TipoInterventoResidenzialeMaggiorenneType.DE_2200);
        //TODO: campo mancante
//        String struttura = getValoreDatoSpecifico(p, "ds_struttura");
//        i.setStruttura(struttura);
        return i;
    }

    private InterventoCasaAlbergo serializerInterventoCasaAlbergo(PaiIntervento intervento) {
        InterventoCasaAlbergo i = new InterventoCasaAlbergo();
        i.setTipoInterventoResidenzialeMaggiorenne(TipoInterventoResidenzialeMaggiorenneType.DE_1200);
        //TODO: campo mancante
        //i.setStruttura(ITALIA);
        return i;
    }

    private InterventoResidenzaProtetta serializeInterventoResidenzaProtetta(PaiIntervento intervento) {
        InterventoResidenzaProtetta i = new InterventoResidenzaProtetta();
        i.setTipoInterventoResidenzialeMaggiorenne(TipoInterventoResidenzialeMaggiorenneType.DE_1400);
        //TODO: campo mancante
        //i.setStruttura("")
        return i;
    }

    private InterventoComunitaAlloggio serializeInterventoComunitaAlloggio(PaiIntervento p) {
        InterventoComunitaAlloggio i = new InterventoComunitaAlloggio();
        i.setTipoInterventoResidenzialeMaggiorenne(TipoInterventoResidenzialeMaggiorenneType.DE_1300);
        //TODO: campo mancante
        //i.setStruttura("");
        return i;
    }

    private InterventoRsa serializeInterventoRsa(PaiIntervento p) {
        InterventoRsa i = new InterventoRsa();
        i.setTipoInterventoResidenzialeMaggiorenne(TipoInterventoResidenzialeMaggiorenneType.DE_1800);
        //TODO: campo mancante
        //i.setStruttura("");
        return i;
    }

    private InterventoUtenzaDiversificata serializeInterventoutenzaDiversificata(PaiIntervento p) {
        InterventoUtenzaDiversificata i = new InterventoUtenzaDiversificata();
        i.setTipoInterventoResidenzialeMaggiorenne(TipoInterventoResidenzialeMaggiorenneType.DE_1500);
        //TODO: campo mancante
        //i.setStruttura("");
        return i;
    }

    private InterventoResidenzaPolifunzionale serializeInterventoresidenzaPolifunzionale(PaiIntervento p) {
        InterventoResidenzaPolifunzionale i = new InterventoResidenzaPolifunzionale();
        i.setTipoInterventoResidenzialeMaggiorenne(TipoInterventoResidenzialeMaggiorenneType.DE_1600);
        //TODO: campo mancante
        //i.setStruttura("");
        return i;
    }

    private InterventoResidenzaPolifunzionaleModuliFasciaA serializeInterventoresidenzaPolifunzionaleFasciaA(PaiIntervento p) {
        InterventoResidenzaPolifunzionaleModuliFasciaA i = new InterventoResidenzaPolifunzionaleModuliFasciaA();
        i.setTipoInterventoResidenzialeMaggiorenne(TipoInterventoResidenzialeMaggiorenneType.DE_1700);
        //TODO: campo mancante
        //i.setStruttura("");
        return i;
    }

    private InterventoResidenzialeSenzaStruttura serializeInterventoResidenzialeSenzaStruttura(PaiIntervento p) {
        InterventoResidenzialeSenzaStruttura i = new InterventoResidenzialeSenzaStruttura();
        String codIntCsr = p.getTipologiaIntervento().getCodIntCsr();
        i.setTipoInterventoResidenzialeMaggiorenne(TipoInterventoResidenzialeMaggiorenneType.fromValue(codIntCsr));
        return i;
    }

    private String getValoreDatoSpecifico(PaiIntervento p, String codiceCampo) {
        List<MapDatiSpecificiIntervento> datiSpecifici = p.getMapDatiSpecificiInterventoList();
        String result = null;
        for (MapDatiSpecificiIntervento dato : datiSpecifici) {
            if (dato.getMapDatiSpecificiInterventoPK().getCodCampo().equals(codiceCampo)) {
                //è una combo
                if (dato.getCodValCampo() != null) {
                    result = dato.getCodValCampo();
                } else {
                    result = dato.getValCampo();
                }
                break;
            }
        }
        return result;
    }

    private boolean isInterventoNonPai(String codTipint) {
        try {
            InterventoNonPaiType.fromValue(codTipint);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
