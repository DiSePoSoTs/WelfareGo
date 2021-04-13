package it.wego.welfarego.azione.forms;

import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.models.VerificaImpegniGenericDataModel;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.entities.*;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 *
 * @author aleph
 */
public abstract class VerificaImpegniGenericFormListener extends AbstractForm {

    /**
     * carica dal database i dati per la form
     *
     * @param <T>
     * @param clazz
     * @return
     * @throws Exception
     */
    public <T extends VerificaImpegniGenericDataModel> T loadData(Class<T> clazz) throws Exception {
        T data = clazz.newInstance();
        UniqueTasklist task = getTask();

        Pai pai = task.getCodPai();
        PaiIntervento paiIntervento = task.getPaiIntervento();
        AnagrafeSoc anagrafeSoc = pai.getAnagrafeSoc();

        data.setNomeUtenteVerDati(anagrafeSoc.getNome());
        data.setCognomeUt(anagrafeSoc.getCognome());
        data.setDataApertPai(pai.getDtApePai());
        data.setAssistSoc(pai.getCodUteAs().getCognome() + " " + pai.getCodUteAs().getNome());
        data.setnPai(pai.getCodPai().toString());
        data.setDescrizione(paiIntervento.getTipologiaIntervento().getDesTipint());
        data.setInterv(paiIntervento.getPaiInterventoPK().getCodTipint());


        char flgDocumentoDiApprovazione         = paiIntervento.getTipologiaIntervento().getFlgDocumentoDiAutorizzazione();
        data.setFlgDocumentoDiAutorizzazione(flgDocumentoDiApprovazione);

        Integer codTmplDocumentoDiApprovazione    = -1;

        if('S' == flgDocumentoDiApprovazione) {
            codTmplDocumentoDiApprovazione    = paiIntervento.getTipologiaIntervento().getTmplDocumentoDiAutorizzazione().getCodTmpl();
            data.setCodTmplDocumentoDiAutorizzazione(codTmplDocumentoDiApprovazione);
        } else {
            data.setCodTmplDocumentoDiAutorizzazione(codTmplDocumentoDiApprovazione);
        }

        Tariffa tariffa = paiIntervento.getTariffa();
        if(tariffa !=null){
        	BigDecimal costo;
        	if(paiIntervento.getTipologiaIntervento().getFlgFineDurata()== TipologiaIntervento.FLG_FINE_DURATA_F && paiIntervento.getTipologiaIntervento().getIdParamUniMis().getDesParam().contains("euro")){
				costo = new BigDecimal(1);	
        	}
        	else{
				costo = tariffa.getCosto();
        	}
			data.setSpesaUnitaria(costo.toString());	
        }
        else {
			data.setSpesaUnitaria(paiIntervento.getTipologiaIntervento().getImpStdCosto().toString());
        }
        data.setDurata(paiIntervento.getDurMesi() != null ? paiIntervento.getDurMesi().toString() : "");
        data.setImpMens(paiIntervento.getQuantita().toString());
        data.setCostTot(getCostoTot().toString());
        data.setDataFine(paiIntervento.getDtFine());
        // setto la data di avvio ad oggi 
        data.setDataAvvio(new Date());
        data.setDataAvvioProposta(paiIntervento.getDataAvvioProposta());
        data.setCodParamUnitaMisura(paiIntervento.getIdParamUniMis().getIdParam().getCodParam());
        data.setIsProrogabile(paiIntervento.getTipologiaIntervento().getFlgRinnovo().equals(TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_PROROGA)? "S":"N");
        
        data.setNote(paiIntervento.getMotivazione() != null ? paiIntervento.getMotivazione() : "");
        List<InterventiAssociati> interventiFigli = paiIntervento.getInterventiFigli();
        if(interventiFigli !=null && interventiFigli.isEmpty()==false){
            Struttura struttura = tariffa.getStruttura();
            String strutturaNome = struttura.getNome();
            String tariffaDescrizione = tariffa.getDescrizione();
            data.setNote(data.getNote() + "\n\n" + "STRUTTURA: " + strutturaNome + " - TARIFFA: " + tariffaDescrizione);
        	for (InterventiAssociati a : interventiFigli){
        			data.setNote(data.getNote()+ "\nINTERVENTO ASSOCIATO " + a.getInterventoFiglio().getPai().getAnagrafeSoc().getCognomeNome()
					+ " RETTA GIORNALIERA: " + decimalNumberFormat.format(a.getInterventoFiglio().getQuantita())
					+ " COSTO PREVISTO: " + decimalNumberFormat.format(a.getInterventoFiglio().getCostoPrev()));
        	}
			data.setNote(data.getNote()
				+ "\n\nSI AVVISA CHE LE MODIFICHE DELLE TARIFFE DEI FIGLI DEVONO ESSERE"
				+ " ESEGUITE DIRETTAMENTE NEGLI INTERVENTI CONTENUTI NEL PAI");
        }

        return data;
    }

    public BigDecimal getCostoTot() throws Exception {
    	
        PaiIntervento paiIntervento = getTask().getPaiIntervento();
        CalcolaCostoInterventoService calcolaCostoInterventoService = new CalcolaCostoInterventoService();
        return calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(paiIntervento));
    }
}
