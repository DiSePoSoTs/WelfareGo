package it.wego.welfarego.xsd.pratica.serializers;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Collections2;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Ente;
import it.wego.welfarego.persistence.entities.InterventiAssociati;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MapDatiSpecificiIntervento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.Struttura;
import it.wego.welfarego.persistence.entities.Tariffa;
import it.wego.welfarego.xsd.Utils;
import it.wego.welfarego.xsd.pratica.CartellaSociale;
import it.wego.welfarego.xsd.pratica.DatiSpecificiType;
import it.wego.welfarego.xsd.pratica.DecodParamType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InterventoSerializer {

    private AnagraficaSerializer anagraficaSerializer = new AnagraficaSerializer();


    public CartellaSociale.Pai.ElencoInterventi.Intervento serializeIntervento(PaiIntervento paiIntervento) {


        CartellaSociale.Pai.ElencoInterventi.Intervento interventoXml = new CartellaSociale.Pai.ElencoInterventi.Intervento();
        interventoXml.setCcEleCost(paiIntervento.getTipologiaIntervento().getCcele());
        interventoXml.setCntTipInt(BigInteger.valueOf(paiIntervento.getPaiInterventoPK().getCntTipint()));
        interventoXml.setCodTipint(paiIntervento.getPaiInterventoPK().getCodTipint());
        interventoXml.setCostoTotale(paiIntervento.getCostoPrev());


        interventoXml.setDataAtto(Utils.dateToItString(paiIntervento.getDtApe()));


        List<MapDatiSpecificiIntervento> datiSpecifici = paiIntervento.getMapDatiSpecificiInterventoList();
        for (MapDatiSpecificiIntervento d : datiSpecifici) {
            DatiSpecificiType dato = serializeDatiSpecificiType(d);
            interventoXml.getDatiSpecifici().add(dato);
        }
        interventoXml.setDesTipInt(paiIntervento.getTipologiaIntervento().getDesTipint());
        interventoXml.setQuantita(String.valueOf(paiIntervento.getQuantita()));
        interventoXml.setDtChius(Utils.dateToItString(paiIntervento.getDtChius()));
        interventoXml.setDataFineIndicativa(paiIntervento.getDataFineIndicativa() == null ? null : Utils.dateToItString(paiIntervento.getDataFineIndicativa()));
        interventoXml.setDtInizio(paiIntervento.getDtAvvio() == null ? null : Utils.dateToItString(paiIntervento.getDtAvvio()));
        interventoXml.setDtFine(Utils.dateToItString(paiIntervento.calculateDtFine()));
        interventoXml.setDtEsec(Utils.dateToItString(paiIntervento.getDtAvvio()));
        interventoXml.setDurMesi(paiIntervento.getDurMesi()== null ? null: String.valueOf(paiIntervento.getDurMesi()));
        DecodParamType esito = new DecodParamType();
        esito.setCodParam(paiIntervento.getIndEsitoInt());
        esito.setDesParamType(paiIntervento.getIndEsitoInt());
        interventoXml.setEsitoInt(esito);
        interventoXml.setNoteChius(paiIntervento.getNoteChius());
        interventoXml.setStatoInt(String.valueOf(paiIntervento.getStatoInt()).toUpperCase());

        interventoXml.getImpegnoSpesa().addAll(Collections2.transform(MoreObjects.<Collection>firstNonNull(paiIntervento.getPaiInterventoMeseList(), Collections.emptyList()), new Function<PaiInterventoMese, CartellaSociale.Pai.ElencoInterventi.Intervento.ImpegnoSpesa>() {
            public CartellaSociale.Pai.ElencoInterventi.Intervento.ImpegnoSpesa apply(PaiInterventoMese interventoMese) {
                CartellaSociale.Pai.ElencoInterventi.Intervento.ImpegnoSpesa impegno = new CartellaSociale.Pai.ElencoInterventi.Intervento.ImpegnoSpesa();
                impegno.setAnno(String.valueOf(interventoMese.getPaiInterventoMesePK().getAnnoEff()));
                if (interventoMese.getBudgetTipIntervento() != null) {
                    impegno.setConto(String.valueOf(interventoMese.getBudgetTipIntervento().getCodConto()));
                }
                if (interventoMese.getBudgetTipIntervento() != null) {
                    impegno.setSottoconto(String.valueOf(interventoMese.getBudgetTipIntervento().getCodSconto()));
                }
                impegno.setImpegno(interventoMese.getPaiInterventoMesePK().getCodImp());
                impegno.setCostoPrev(interventoMese.getBdgPrevEur());
                return impegno;
            }
        }));
        interventoXml.getMandato().addAll(Collections2.transform(MoreObjects.<Collection>firstNonNull(paiIntervento.getMandatoList(), Collections.emptyList()), new Function<Mandato, CartellaSociale.Pai.ElencoInterventi.Intervento.MandatoXml>() {
            public CartellaSociale.Pai.ElencoInterventi.Intervento.MandatoXml apply(Mandato mandato) {
                CartellaSociale.Pai.ElencoInterventi.Intervento.MandatoXml mandatoXml = new CartellaSociale.Pai.ElencoInterventi.Intervento.MandatoXml();
                mandatoXml.setNumeroMandato(mandato.getNumeroMandato());
                mandatoXml.setModalitaErogazione(mandato.getModalitaErogazione());
                mandatoXml.setImporto(mandato.getImporto());
                return mandatoXml;
            }
        }));

        interventoXml.setDataRichiestaApprovazione(paiIntervento.getDataRichiestaApprovazione() == null ? "" : Utils.dateToItString(paiIntervento.getDataRichiestaApprovazione()));
        interventoXml.setIsUltimoBatchRichiestaApprovazione(paiIntervento.isUltimoBatchRichiestaApprovazione() ? "S" : "N");


        fill_beneficiario(paiIntervento, interventoXml);

        fill_richiedente(paiIntervento, interventoXml);


        if(paiIntervento.getDsTipoPagamento()!=null){
            interventoXml.setMetodoPagamento(paiIntervento.getDsTipoPagamento());
        }
        interventoXml.setIban(paiIntervento.getIbanDelegatoObenef());
        interventoXml.setMotivazione(paiIntervento.getMotivazione());

        Tariffa tariffa = paiIntervento.getTariffa();
        if(tariffa != null){
            Struttura struttura = tariffa.getStruttura();
            Ente ente = struttura.getEnte();

            interventoXml.setNomeEnte(ente.getNome());
            interventoXml.setNomeStruttura(struttura.getNome());
            interventoXml.setIndirizzoStruttura(struttura.getIndirizzo());
            interventoXml.setCostoTariffa(tariffa.getCosto());
        }

        List<String> familiariCollegati = new ArrayList<String>();
        interventoXml.setTestoAutorizzazione(paiIntervento.getTestoAutorizzazione());
        List<InterventiAssociati> interventiFigli = paiIntervento.getInterventiFigli();

        for(InterventiAssociati relazioneTraInerventi: interventiFigli){
            if(InterventiAssociati.RELAZIONE_PARENTALE.equals(relazioneTraInerventi.getTipoLegame())){
                PaiIntervento interventoFiglio = relazioneTraInerventi.getInterventoFiglio();
                String cognomeNome = interventoFiglio.getPai().getAnagrafeSoc().getCognomeNome();
                familiariCollegati.add(cognomeNome);
            }
        }

        interventoXml.setFamiliariCollegati(familiariCollegati);
        return interventoXml;
    }

    public void fill_beneficiario(PaiIntervento paiIntervento, CartellaSociale.Pai.ElencoInterventi.Intervento interventoXml) {
        AnagrafeSoc delegato = paiIntervento.getDsCodAnaBenef();
        if (delegato != null) {
            interventoXml.setBeneficiario(anagraficaSerializer.serializeAnagraficaUtente(delegato, null));
        }
    }

    public void fill_richiedente(PaiIntervento paiIntervento, CartellaSociale.Pai.ElencoInterventi.Intervento interventoXml) {
        AnagrafeSoc dsCodAnaRich = paiIntervento.getDsCodAnaRich();
        AnagrafeSoc anagrafeSoc = paiIntervento.getPai().getAnagrafeSoc();
        CartellaSociale.AnagraficaUtente richiedente = null;

        if(dsCodAnaRich !=null){
            richiedente = anagraficaSerializer.serializeAnagraficaUtente(dsCodAnaRich, null);
        }
        else {
            richiedente = anagraficaSerializer.serializeAnagraficaUtente(anagrafeSoc, null);
        }

        interventoXml.setRichiedente(richiedente);
    }


    private DatiSpecificiType serializeDatiSpecificiType(MapDatiSpecificiIntervento m) {
        DatiSpecificiType d = new DatiSpecificiType();
        d.setCodCampo(m.getDatiSpecifici().getCodCampo());
        d.setCodValCampo(m.getCodValCampo());
        d.setDesCampo(m.getDatiSpecifici().getDesCampo());
        d.setValCampo(m.getValCampo());
        return d;
    }


}
