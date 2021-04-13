package it.wego.welfarego.xsd.pratica.serializers;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import it.wego.welfarego.persistence.dao.AnagrafeFamigliaDao;
import it.wego.welfarego.persistence.entities.AnagrafeFam;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Stato;
import it.wego.welfarego.xsd.Utils;
import it.wego.welfarego.xsd.pratica.CartellaSociale;
import it.wego.welfarego.xsd.pratica.DecodParamType;
import it.wego.welfarego.xsd.pratica.InfoIndirizzoType;
import it.wego.welfarego.xsd.pratica.InfoTerrType;
import it.wego.welfarego.xsd.pratica.SessoType;
import it.wego.welfarego.xsd.pratica.dto.AnagrafeDto;
import org.joda.time.DateTime;
import org.joda.time.Years;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AnagraficaSerializer {


    public CartellaSociale.AnagraficaUtente serializeAnagraficaUtente(final AnagrafeSoc soggettoInteressato, @Nullable it.wego.welfarego.persistence.entities.Pai pai) {
        Preconditions.checkNotNull(soggettoInteressato);
        CartellaSociale.AnagraficaUtente anagrafeUtente = new CartellaSociale.AnagraficaUtente();
        anagrafeUtente.setCodAna(String.valueOf(soggettoInteressato.getCodAna()));
        anagrafeUtente.setCodAnaCivilia(soggettoInteressato.getCodAnaCivilia());
        anagrafeUtente.setCodAnaCom(soggettoInteressato.getCodAnaCom());
        anagrafeUtente.setCodAnaComFam(soggettoInteressato.getCodAnaFamCom());
        anagrafeUtente.setCodAss(soggettoInteressato.getCodAss());
        anagrafeUtente.setCodFisc(soggettoInteressato.getCodFisc());
        if (soggettoInteressato.getIdParamPosAna() != null) {
            setCodPosAna(soggettoInteressato, anagrafeUtente);
        }
        anagrafeUtente.setCognome(soggettoInteressato.getCognome());
        anagrafeUtente.setCognomeConiuge(soggettoInteressato.getCognomeConiuge());

        fill_cittadinanza(soggettoInteressato, anagrafeUtente);

        if(soggettoInteressato.getRagSoc()!=null){
            anagrafeUtente.setRagSoc(soggettoInteressato.getRagSoc());
        }
        if (soggettoInteressato.getIdParamTipAll() != null) {
            setCondAb(soggettoInteressato, anagrafeUtente);
        }
        if (soggettoInteressato.getCondFam() != null) {
            setCondFam(soggettoInteressato, anagrafeUtente);
        }
        if (soggettoInteressato.getIdParamCondProf() != null) {
            setCondProf(soggettoInteressato, anagrafeUtente);
        }
        anagrafeUtente.setDataNascita(Utils.dateToItString(soggettoInteressato.getDtNasc()));
        anagrafeUtente.setDistSan(String.valueOf(soggettoInteressato.getDistSan()));
        anagrafeUtente.setDtAggAb(Utils.dateToItString(soggettoInteressato.getDtAggAb()));
        anagrafeUtente.setDtMorte(Utils.dateToItString(soggettoInteressato.getDtMorte()));
        anagrafeUtente.setEMail(soggettoInteressato.getEmail());
        anagrafeUtente.setEnteGestore(soggettoInteressato.getEnteGestore());
        anagrafeUtente.setFlgAccomp(Utils.cleanStringForXml(String.valueOf(soggettoInteressato.getFlgAccomp())));
        anagrafeUtente.setFlgEMail(Utils.cleanStringForXml(String.valueOf(soggettoInteressato.getFlgEmail())));
        anagrafeUtente.setFlgPersFg(Utils.cleanStringForXml(String.valueOf(soggettoInteressato.getFlgPersFg())));
        anagrafeUtente.setFlgSex(Strings.isNullOrEmpty(soggettoInteressato.getFlgSex()) ? null : SessoType.fromValue(String.valueOf(soggettoInteressato.getFlgSex()).toUpperCase()));
        anagrafeUtente.setFlgSms(Utils.cleanStringForXml(String.valueOf(soggettoInteressato.getFlgSms())));
        anagrafeUtente.setDiario(soggettoInteressato.getDiario());
        if (soggettoInteressato.getIdParamRedd() != null) {
            setFormReddito(soggettoInteressato, anagrafeUtente);
        }
        String isMinore="NO";
        DateTime now = new DateTime();
        DateTime  dataNascita = new DateTime(soggettoInteressato.getDtNasc());


        if(Years.yearsBetween(dataNascita, now).getYears()<18){
            isMinore="SI";
        }
        anagrafeUtente.setIsMinore(isMinore);
        anagrafeUtente.setIban(soggettoInteressato.getIbanPagam());
        anagrafeUtente.setPresso(soggettoInteressato.getPresso());
        InfoIndirizzoType indDom = getDomicilio(soggettoInteressato);
        anagrafeUtente.setIndirDomic(indDom);
        InfoIndirizzoType indRes = getResidenza(soggettoInteressato);
        anagrafeUtente.setIndirResid(indRes);
        InfoTerrType infoTerrNasc = getLuogoNascita(soggettoInteressato);
        anagrafeUtente.setInfoNasc(infoTerrNasc);
        InfoTerrType infoTerrRes = getInfoRes(soggettoInteressato);
        anagrafeUtente.setInfoResid(infoTerrRes);
        InfoTerrType infoTerrDom = getInfoDom(soggettoInteressato);
        anagrafeUtente.setInfoDomic(infoTerrDom);


        anagrafeUtente.setMedicoBase(soggettoInteressato.getMedicoBase());

        if (soggettoInteressato.getCodStatoNaz() != null) {
            anagrafeUtente.setNazionalita(String.valueOf(soggettoInteressato.getCodStatoNaz().getDesParam()));
        }

        anagrafeUtente.setNome(soggettoInteressato.getNome());
        anagrafeUtente.setNote(soggettoInteressato.getNote());
        anagrafeUtente.setNumCell(soggettoInteressato.getNumCell());
        anagrafeUtente.setNumTel(soggettoInteressato.getNumTel());
        anagrafeUtente.setPartIva(soggettoInteressato.getPartIva());
        anagrafeUtente.setRagSoc(soggettoInteressato.getRagSoc());
        anagrafeUtente.setRedditoMensile(soggettoInteressato.getReddMens());

        if (soggettoInteressato.getCodSegnDa() != null) {
            setSegnalatoDao(soggettoInteressato, anagrafeUtente);
        }
        anagrafeUtente.setSottozona(soggettoInteressato.getSottozona());
        if (soggettoInteressato.getIdParamStatoCiv() != null) {
            setStatoCivile(soggettoInteressato, anagrafeUtente);
        }

        if(soggettoInteressato.getFlgAccomp()!=null){
            setFlgAccompagnamento(soggettoInteressato, anagrafeUtente);
        }
        if(soggettoInteressato.getPercInvCiv()!=null){
            setInvaliditaCivile(soggettoInteressato, anagrafeUtente);
        }
        if (soggettoInteressato.getIdParamStatoFis() != null) {
            setStatoFisico(soggettoInteressato, anagrafeUtente);
        }
        if (soggettoInteressato.getIdParamTit() != null) {
            setTitoloStudio(soggettoInteressato, anagrafeUtente);
        }
        anagrafeUtente.setZona(String.valueOf(soggettoInteressato.getZona()));
        if (pai != null) {
            anagrafeUtente.setDtScadIsee(Utils.dateToItString(pai.getDtScadIsee()));
            anagrafeUtente.setIsee(pai.getIsee());
        }
        
        
        CartellaSociale.AnagraficaUtente.Famiglia.Familiare seStesso = new CartellaSociale.AnagraficaUtente.Famiglia.Familiare();
        seStesso.setCognome(soggettoInteressato.getCognome());
        seStesso.setNome(soggettoInteressato.getNome());
        seStesso.setDataNascita(Utils.dateToItString(soggettoInteressato.getDtNasc()));
        seStesso.setLuogoNascita(soggettoInteressato.getLuogoNascita().getLuogoText());
        seStesso.setRelazioneDiParentela("N/A");
        seStesso.setAttivitaLavoroStudio(soggettoInteressato.getAttivitaLavoroStudio());
        seStesso.setEntrate2mesi(MoreObjects.firstNonNull(soggettoInteressato.getReddMens(), "").toString());
        List<CartellaSociale.AnagraficaUtente.Famiglia.Familiare> familiari = anagrafeUtente.getFamiglia().getFamiliare();
        familiari.add(seStesso);

        fill_familiari(soggettoInteressato, familiari);


        return anagrafeUtente;
    }

    public void fill_cittadinanza(AnagrafeSoc soggettoInteressato, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        Stato statoCittadinanza = soggettoInteressato.getCodStatoCitt();
        if(statoCittadinanza != null){
            List<String> cittadinanza = anagrafeUtente.getCittadinanza();
            String desStato = statoCittadinanza.getDesStato();
            cittadinanza.add(desStato);
        }
    }

    private void setCodPosAna(AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        DecodParamType posAna = new DecodParamType();
        posAna.setCodParam(String.valueOf(anagrafeSociale.getIdParamPosAna().getIdParam().getCodParam()));
        posAna.setDesParamType(anagrafeSociale.getIdParamPosAna().getDesParam());
        anagrafeUtente.setCodPosAna(posAna);
    }

    private void setCondFam(AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        DecodParamType condFam = new DecodParamType();
        condFam.setCodParam(String.valueOf(anagrafeSociale.getCondFam().getIdParam().getCodParam()));
        condFam.setDesParamType(anagrafeSociale.getCondFam().getDesParam());
        anagrafeUtente.setCondFam(condFam);
    }

    private void setCondAb(AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        DecodParamType condAb = new DecodParamType();
        if (anagrafeSociale.getIdParamTipAll() != null) {
            condAb.setCodParam(String.valueOf(anagrafeSociale.getIdParamTipAll().getIdParam().getCodParam()));
            condAb.setDesParamType(anagrafeSociale.getIdParamTipAll().getDesParam());
        }
        anagrafeUtente.setCondAb(condAb);
    }

    private void setFormReddito(AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        DecodParamType formaReddito = new DecodParamType();
        formaReddito.setCodParam(String.valueOf(anagrafeSociale.getIdParamRedd().getIdParam().getCodParam()));
        formaReddito.setDesParamType(anagrafeSociale.getIdParamRedd().getDesParam());
        anagrafeUtente.setFormaReddito(formaReddito);
    }

    private void setCondProf(AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        DecodParamType condProf = new DecodParamType();
        condProf.setCodParam(String.valueOf(anagrafeSociale.getIdParamCondProf().getIdParam().getCodParam()));
        condProf.setDesParamType(anagrafeSociale.getIdParamCondProf().getDesParam());
        anagrafeUtente.setCondProf(condProf);
    }

    private InfoIndirizzoType getResidenza(AnagrafeSoc anagrafeSociale) {
        InfoIndirizzoType indRes = new InfoIndirizzoType();
        if (anagrafeSociale.getLuogoResidenza().getCap() != null) {
            indRes.setCap(String.valueOf(anagrafeSociale.getLuogoResidenza().getCap()));
        }
        if (anagrafeSociale.getLuogoResidenza().getVia() != null) {
            indRes.setCodViaTs(anagrafeSociale.getLuogoResidenza().getVia().getToponomasticaPK().getCodVia());
            indRes.setDesVia(anagrafeSociale.getLuogoResidenza().getVia().getDesVia());
        } else {
            indRes.setCodViaTs(anagrafeSociale.getLuogoResidenza().getViaStr());
            indRes.setDesVia(anagrafeSociale.getLuogoResidenza().getViaStr());
        }
        if (anagrafeSociale.getLuogoResidenza().getCivico() != null) {
            indRes.setCodCivTs(anagrafeSociale.getLuogoResidenza().getCivico().getToponomasticaCiviciPK().getCodCiv());
            indRes.setDesCiv(anagrafeSociale.getLuogoResidenza().getCivico().getDesCiv());
        } else {
            indRes.setDesCiv(anagrafeSociale.getLuogoResidenza().getCivicoStr());
            indRes.setDesVia(anagrafeSociale.getLuogoResidenza().getCivicoStr());
        }
        return indRes;
    }

    private InfoIndirizzoType getDomicilio(AnagrafeSoc anagrafeSociale) {
        InfoIndirizzoType indDom = new InfoIndirizzoType();
        if (anagrafeSociale.getLuogoDomicilio().getCap() != null) {
            indDom.setCap(String.valueOf(anagrafeSociale.getLuogoDomicilio().getCap()));
        }
        if (anagrafeSociale.getLuogoDomicilio().getVia() != null) {
            indDom.setCodViaTs(anagrafeSociale.getLuogoDomicilio().getVia().getToponomasticaPK().getCodVia());
            indDom.setDesVia(anagrafeSociale.getLuogoDomicilio().getVia().getDesVia());
        } else {
            indDom.setCodViaTs(anagrafeSociale.getLuogoDomicilio().getViaStr());
            indDom.setDesVia(anagrafeSociale.getLuogoDomicilio().getViaStr());
        }
        if (anagrafeSociale.getLuogoDomicilio().getCivico() != null) {
            indDom.setCodCivTs(anagrafeSociale.getLuogoDomicilio().getCivico().getToponomasticaCiviciPK().getCodCiv());
            indDom.setDesCiv(anagrafeSociale.getLuogoDomicilio().getCivico().getDesCiv());
        } else {
            indDom.setDesCiv(anagrafeSociale.getLuogoDomicilio().getCivicoStr());
            indDom.setDesVia(anagrafeSociale.getLuogoDomicilio().getCivicoStr());
        }
        return indDom;
    }

    private InfoTerrType getLuogoNascita(AnagrafeSoc anagrafeSociale) {
        InfoTerrType infoTerrNasc = new InfoTerrType();
        infoTerrNasc.setCodComIta(anagrafeSociale.getLuogoNascita().getCodCom());
        infoTerrNasc.setDesComItaEst(anagrafeSociale.getLuogoNascita().getComuneText());

        infoTerrNasc.setCodProvIta(anagrafeSociale.getLuogoNascita().getCodProv());
        infoTerrNasc.setDesProvItaEst(anagrafeSociale.getLuogoNascita().getProvinciaText());

        infoTerrNasc.setCodStato(anagrafeSociale.getLuogoNascita().getCodStato());
        infoTerrNasc.setDesStato(anagrafeSociale.getLuogoNascita().getStatoText());

        return infoTerrNasc;
    }

    private InfoTerrType getInfoRes(AnagrafeSoc anagrafeSociale) {
        InfoTerrType infoTerrRes = new InfoTerrType();
        if (anagrafeSociale.getLuogoResidenza().getComune() != null) {
            infoTerrRes.setCodComIta(anagrafeSociale.getLuogoResidenza().getComune().getComunePK().getCodCom());
            infoTerrRes.setDesComItaEst(anagrafeSociale.getLuogoResidenza().getComune().getDesCom());
        } else {
            infoTerrRes.setDesComItaEst(anagrafeSociale.getLuogoResidenza().getComuneStr());
        }
        if (anagrafeSociale.getLuogoResidenza().getProvincia() != null) {
            infoTerrRes.setCodProvIta(anagrafeSociale.getLuogoResidenza().getProvincia().getProvinciaPK().getCodProv());
            infoTerrRes.setDesProvItaEst(anagrafeSociale.getLuogoResidenza().getProvincia().getDesProv());
        } else {
            infoTerrRes.setDesProvItaEst(anagrafeSociale.getLuogoResidenza().getProvinciaStr());
        }
        if (anagrafeSociale.getLuogoResidenza().getStato() != null) {
            infoTerrRes.setCodStato(anagrafeSociale.getLuogoResidenza().getStato().getCodStato());
            infoTerrRes.setDesStato(anagrafeSociale.getLuogoResidenza().getStato().getDesStato());
        }
        return infoTerrRes;
    }

    private InfoTerrType getInfoDom(AnagrafeSoc anagrafeSociale) {
        InfoTerrType infoTerrDom = new InfoTerrType();
        if (anagrafeSociale.getLuogoDomicilio().getComune() != null) {
            infoTerrDom.setCodComIta(anagrafeSociale.getLuogoDomicilio().getComune().getComunePK().getCodCom());
            infoTerrDom.setDesComItaEst(anagrafeSociale.getLuogoDomicilio().getComune().getDesCom());
        } else {
            infoTerrDom.setDesComItaEst(anagrafeSociale.getLuogoDomicilio().getComuneStr());
        }
        if (anagrafeSociale.getLuogoDomicilio().getProvincia() != null) {
            infoTerrDom.setCodProvIta(anagrafeSociale.getLuogoDomicilio().getProvincia().getProvinciaPK().getCodProv());
            infoTerrDom.setDesProvItaEst(anagrafeSociale.getLuogoDomicilio().getProvincia().getDesProv());
        } else {
            infoTerrDom.setDesProvItaEst(anagrafeSociale.getLuogoDomicilio().getProvinciaStr());
        }
        if (anagrafeSociale.getLuogoDomicilio().getStato() != null) {
            infoTerrDom.setCodStato(anagrafeSociale.getLuogoDomicilio().getStato().getCodStato());
            infoTerrDom.setDesStato(anagrafeSociale.getLuogoDomicilio().getStato().getDesStato());
        }
        return infoTerrDom;
    }

    private void setFlgAccompagnamento (AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente){
        if(anagrafeSociale.getFlgAccomp().equals("S")){
            anagrafeUtente.setFlgAccomp("SI");
        }
        else {
            anagrafeUtente.setFlgAccomp("NO");
        }


    }

    private void setSegnalatoDao(AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        DecodParamType segnalatoDa = new DecodParamType();
        segnalatoDa.setCodParam(String.valueOf(anagrafeSociale.getCodSegnDa().getIdParam().getCodParam()));
        segnalatoDa.setDesParamType(anagrafeSociale.getCodSegnDa().getDesParam());
        anagrafeUtente.setSegnDa(segnalatoDa);
    }

    private void setTitoloStudio(AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        DecodParamType titStudio = new DecodParamType();
        titStudio.setCodParam(String.valueOf(anagrafeSociale.getIdParamTit().getIdParam().getCodParam()));
        titStudio.setDesParamType(anagrafeSociale.getIdParamTit().getDesParam());
        anagrafeUtente.setTitStudio(titStudio);
    }

    private void setStatoFisico(AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        DecodParamType statoFisico = new DecodParamType();
        statoFisico.setCodParam(String.valueOf(anagrafeSociale.getIdParamStatoFis().getIdParam().getCodParam()));
        statoFisico.setDesParamType(anagrafeSociale.getIdParamStatoFis().getDesParam());
        anagrafeUtente.setStatoFisico(statoFisico);
    }

    private void setStatoCivile(AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        DecodParamType statoCivile = new DecodParamType();
        statoCivile.setCodParam(String.valueOf(anagrafeSociale.getIdParamStatoCiv().getIdParam().getCodParam()));
        statoCivile.setDesParamType(anagrafeSociale.getIdParamStatoCiv().getDesParam());
        anagrafeUtente.setStatoCiv(statoCivile);
    }

    private void setInvaliditaCivile(AnagrafeSoc anagrafeSociale, CartellaSociale.AnagraficaUtente anagrafeUtente) {
        switch(anagrafeSociale.getPercInvCiv()){
            case 0:
                anagrafeUtente.setInvalCiv("Non invalido");
                break;
            case 1:
                anagrafeUtente.setInvalCiv("Invalido dal 34% al 66%");
                break;
            case 2:
                anagrafeUtente.setInvalCiv("Invalido dal 67% al 73%");
                break;
            case 3:
                anagrafeUtente.setInvalCiv("Invalido dal 74% al 99%");
                break;
            case 4:
                anagrafeUtente.setInvalCiv("Invalido al 100% Necessità di assistenza");
                break;
            case 5:
                anagrafeUtente.setInvalCiv("Invalido al 100% Inabile al lavoro");
                break;
            case 6:
                anagrafeUtente.setInvalCiv("Invalido al 100% Impossibilità deambulazione");
                break;
        }
    }

    void fill_familiari(final AnagrafeSoc soggettoInteressato, List<CartellaSociale.AnagraficaUtente.Famiglia.Familiare> familiari) {
        List<AnagrafeDto> listFamiliariAsAnagrafeDto = new ArrayList<AnagrafeDto>();
        Predicate<AnagrafeFam> relazioneDiParentelaFilter = AnagrafeFamigliaDao.getRelazioneDiParentelaFilter();
        List<AnagrafeFam> anagrafeFamListAsAny = soggettoInteressato.getAnagrafeFamListAsAny();
        Iterable<AnagrafeFam> familiariAsAnagrafeFam = Iterables.filter(anagrafeFamListAsAny, relazioneDiParentelaFilter);

        Function<AnagrafeFam, AnagrafeDto> anagrafeFam_2_AnagrafeDto = new Function<AnagrafeFam, AnagrafeDto>() {
            public AnagrafeDto apply(@Nullable AnagrafeFam anagrafeFam) {
                AnagrafeSoc anagrafeSocTarget = anagrafeFam.getAnagrafeSocTarget();
                AnagrafeSoc anagrafeSocSource = anagrafeFam.getAnagrafeSocSource();

                AnagrafeSoc anagrafeSociale = null;
                if(soggettoInteressato.getCodAna().equals(anagrafeSocSource.getCodAna())){
                    anagrafeSociale = anagrafeSocTarget;
                } else {
                    anagrafeSociale = anagrafeSocSource;
                }

                AnagrafeDto anagrafeDto =  new AnagrafeDto(anagrafeSociale, getRelazioneDiParentela(anagrafeFam));

                return anagrafeDto;
            }

            public String getRelazioneDiParentela(@Nullable AnagrafeFam anagrafeFam) {
                return anagrafeFam.getCodQual() == null ? null : anagrafeFam.getCodQual().getDesParam();
            }
        };

        Function<AnagrafeDto, CartellaSociale.AnagraficaUtente.Famiglia.Familiare> anagrafeDto_2_Familiare = new Function<AnagrafeDto, CartellaSociale.AnagraficaUtente.Famiglia.Familiare>() {
            public CartellaSociale.AnagraficaUtente.Famiglia.Familiare apply(@Nullable AnagrafeDto anagrafeDto) {
                AnagrafeSoc anagrafeSociale = anagrafeDto.getAnagrafeSoc();
                CartellaSociale.AnagraficaUtente.Famiglia.Familiare familiare = new CartellaSociale.AnagraficaUtente.Famiglia.Familiare();
                familiare.setAttivitaLavoroStudio(anagrafeSociale.getAttivitaLavoroStudio());
                familiare.setCognome(anagrafeSociale.getCognome());
                familiare.setNome(anagrafeSociale.getNome());
                familiare.setDataNascita(Utils.dateToItString(anagrafeSociale.getDtNasc()));
                familiare.setEntrate2mesi(MoreObjects.firstNonNull(anagrafeSociale.getReddito(), "").toString());
                familiare.setLuogoNascita(anagrafeSociale.getLuogoNascita().getComuneText());
                familiare.setRelazioneDiParentela(anagrafeDto.getRelazioneDiParentela());
                return familiare;
            }
        };

        Iterable<AnagrafeDto> familiariAsAnagrafeDto = Iterables.transform(familiariAsAnagrafeFam, anagrafeFam_2_AnagrafeDto);

        Map<Integer, AnagrafeDto> removeAnagrafeSocDuplicati = new HashMap<Integer, AnagrafeDto>();
        for(AnagrafeDto anagrafeDto: familiariAsAnagrafeDto){
            removeAnagrafeSocDuplicati.put(anagrafeDto.getAnagrafeSoc().getCodAna(), anagrafeDto);
        }

        Iterator<Map.Entry<Integer, AnagrafeDto>> iterator = removeAnagrafeSocDuplicati.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer, AnagrafeDto> next = iterator.next();
            AnagrafeDto anagrafeDto = next.getValue();
            listFamiliariAsAnagrafeDto.add(anagrafeDto);
        }

        Iterables.addAll(familiari, Iterables.transform(listFamiliariAsAnagrafeDto, anagrafeDto_2_Familiare));
    }
}

