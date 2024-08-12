/**
 * 
 */
package it.wego.welfarego.cassa.servlet;

import static it.wego.persistence.ConditionBuilder.ilike;
import static it.wego.persistence.ConditionBuilder.between;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import it.wego.conversions.StringConversion;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.persistence.dao.LogCassaDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.RicevutaCassaDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.entities.LogCassa;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.RicevutaCassa;

/**
 * @author Fabio Bonaccorso
 *
 */
public class CassaForm extends AbstractForm implements AbstractForm.Loadable, AbstractForm.Saveable{
     public final String INTERVENTO_CASSA_CODE = "AD009";
     private final static Map<String, String> conditionsMap;
     static{
    	 Map<String, String> map = new HashMap<String, String>();
    	 map.put("DATA_OPERAZIONE", "l.dataOperazione");
    	 map.put("UTENTE", "l.parametri");
    	 conditionsMap= Collections.unmodifiableMap(map);
     }
     private static final String DATA_SEARCH = "DATA_OPERAZIONE";
	 public Object load() throws Exception{
	  
		String operation = getParameter("type"); 
		if(operation.equals("daLiquidare")){
			String what = getParameter("paramWhat");
			List<PaiIntervento> result = new ArrayList<PaiIntervento>();
		    List<PaiIntervento> filteredResult = new ArrayList<PaiIntervento>();
			//prendo la lista di tutti gli interventi cassa a mani ancora in esecutivita 
			result= new PaiInterventoDao(getEntityManager()).findByStatusTipint(PaiIntervento.STATO_INTERVENTO_ESECUTIVO, new TipologiaInterventoDao(getEntityManager()).findByCodTipint(INTERVENTO_CASSA_CODE));
			if(what !=null && what!=""){
				for(PaiIntervento i : result){
					if(what.equals(i.getPai().getAnagrafeSoc().getCognome())){
						filteredResult.add(i);
					}
				}
			}
			else {
				filteredResult= result;
			}
			return JsonBuilder.newInstance().withData(filteredResult).withParameters(getParameters()).withTransformer(interventoTransformerFunction).buildStoreResponse();
		}
		if(operation.equals("logCassa")){
			  String query = "SELECT l FROM LogCassa l";
			  String paramWhat = getParameter("paramWhat"),
		               paramBy = getParameter("paramBy");
			List<Condition> conditions = new ArrayList<Condition>();
			 if (!StringUtils.isBlank(paramWhat) && !StringUtils.isBlank(paramBy)) {
				 if(Objects.equal(paramBy,DATA_SEARCH )){
					 try{
					 conditions.add(between(conditionsMap.get(paramBy),StringConversion.itStringToDate(paramWhat),addaDay(StringConversion.itStringToDate(paramWhat))));
					 }
					 catch(Exception e){
						getLogger().debug("Conversione della data non andata a buon fine");
					 }
				 }
				 
				 else  if (conditionsMap.containsKey(paramBy)) {
		                conditions.add(ilike(conditionsMap.get(paramBy), paramWhat));
		            }
			 }
			List<LogCassa> result = new ArrayList<LogCassa>();
			result = new LogCassaDao(getEntityManager()).find(LogCassa.class, query, conditions);
			return JsonBuilder.newInstance().withData(result).withParameters(getParameters()).withTransformer(logcassaTransformerFunction).buildStoreResponse();
		}
		return null;
	 }
	 
	 
	 @SuppressWarnings("rawtypes")
	private static final Function<PaiIntervento, Map> interventoTransformerFunction = new JsonMapTransformer<PaiIntervento>() {
	        @Override
	        public void transformToMap(PaiIntervento bean ) {
	           put("pai",bean.getPai().getCodPai());
	           put("num_intervento",bean.getPaiInterventoPK().getCntTipint());
	           put("cognome",bean.getPai().getAnagrafeSoc().getCognome());
	           put("nome",bean.getPai().getAnagrafeSoc().getNome());
	           put("data_esecutivita",bean.getDtEsec());
	           put("importo",bean.getQuantita());
	           put("delegato",bean.getDsCodAnaBenef().getNome()+" "+bean.getDsCodAnaBenef().getCognome());
	          
	         
	        }
	    };
	    
	    @SuppressWarnings("rawtypes")
		private static final Function<LogCassa, Map> logcassaTransformerFunction = new JsonMapTransformer<LogCassa>() {
	        @Override
	        public void transformToMap(LogCassa bean ) {
	          put("id",bean.getId());
	           if(bean.getCodPai()!=null){
	           put("pai",bean.getCodPai());
	           put("intervento",bean.getIntervento());
	           put("flgRicevuta","S");
	           }
	           else {
	        	   put("pai","");
		           put("intervento","");
	        	   put("flgRicevuta","N");
	           }
	           put("data_operazione",bean.getDataOperazione());
	           put("tipo",bean.getTipoOperazione());
	           put("dettagli",bean.getParametri());
	           put("importo",bean.getImporto());
	           put("totale",bean.getImportoTotale());
	          
	          
	         
	        }
	    };


	public Object save() throws Exception {
		
		String operation = getParameter("operation");
		RicevutaCassaDao dao = new RicevutaCassaDao(getEntityManager());
		LogCassaDao logDao = new LogCassaDao(getEntityManager());
		if(operation.equals("prelievo")){
		String codPai = getParameter("codPai");
	    String cntTipInt = getParameter("cntTipint");
		if(codPai!=null && cntTipInt!=null){
		PaiIntervento pi = new PaiInterventoDao(getEntityManager()).findByKey(Integer.parseInt(codPai),INTERVENTO_CASSA_CODE,cntTipInt);
		Preconditions.checkNotNull(pi, "Attenzione: questo intervento non esiste ");
		if(pi.getStatoInt()!=PaiIntervento.STATO_INTERVENTO_ESECUTIVO){
			throw new IllegalStateException("Attenzione:L'intervento risulta non esecutivo. Aggiornare la lista degli interventi e assicurarsi che non sia stato già incassato");
			
		}
		;
		if(!isDisponibilita(pi.getQuantita(),logDao.findLastLog().getImportoTotale()) ){
			throw new IllegalStateException("Attenzione il budget conseguente a questo prelievo risulta negativo. Non si può procedere con l'operazione ");
		}
		
	   	
	  try {
		initTransaction();
		RicevutaCassa ricevuta = new RicevutaCassa(codPai,cntTipInt);
		ricevuta.setUtente( pi.getPai().getAnagrafeSoc().getNome() +" "+ pi.getPai().getAnagrafeSoc().getCognome());
		ricevuta.setImporto(pi.getQuantita());
		ricevuta.setDataEmissione(new Date());
		ricevuta.setNumeroRicevuta(dao.findLastRicevuta().getNumeroRicevuta()+1);
		getEntityManager().persist(ricevuta);
		LogCassa log = new LogCassa();
		log.setDataOperazione(new Date());
		log.setImporto(pi.getQuantita());
		log.setImportoTotale(logDao.findLastLog().getImportoTotale().subtract(pi.getQuantita()));
		log.setTipoOperazione(LogCassa.OPERAZIONE_PRELIEVO);
		log.setCodPai(new BigDecimal(codPai));
		log.setIntervento(new BigDecimal(cntTipInt));
		log.setParametri("Utente:"+ pi.getPai().getAnagrafeSoc().getNome() +" "+ pi.getPai().getAnagrafeSoc().getCognome()+ " -Operatore: " + getUtente().getCognomeNome());
		getEntityManager().persist(log);
		commitTransaction();
	  }	
	  catch(Exception e){
		  getPersistenceAdapter().rollbackTransaction();
		  throw e;
	  }
		
		for(PaiInterventoMese pim : pi.getPaiInterventoMeseList()){
			pim.setBdgConsEur(pim.getBdgPrevEur());
			pim.setBdgConsQta(pim.getBdgPrevQta());
			pim.setNote("Intervento cassa a mani erogato.");
			pim.setGenerato(0);
			new PaiInterventoDao(getEntityManager()).update(pim);
		}
		pi.setDtChius(new Date());
		pi.setStatoAttuale(PaiIntervento.CHIUSO);
		pi.setNoteChius("Somma incassata dall'utente");
		pi.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
		new PaiInterventoDao(getEntityManager()).update(pi);
	    return JsonBuilder.newInstance().withSuccess(true).buildResponse();
		
		
		}		
		else {
			throw new IllegalArgumentException("Attenzione il numero pai, il numero dell'intervento o il contatore intervento non è valorizzato");
		}
		
	}
		else {
			
		    BigDecimal importo = new BigDecimal(getParameter("importoVersamento").replace(",", "."));
		    String mandato = getParameter("mandatoVersamento");
		    String determina = getParameter("determinaVersamento");
		    LogCassa log = new LogCassa();
		    log.setDataOperazione(new Date());
		    log.setTipoOperazione(LogCassa.OPERAZIONE_AGGIUNTA);
		    log.setImporto(importo);
		    log.setImportoTotale(logDao.findLastLog().getImportoTotale().add(importo));
		    log.setParametri("Mandato:" + mandato +" Determina:"+determina + " Operatore:"+ getUtente().getCognomeNome());
		    logDao.insert(log);
		    return JsonBuilder.newInstance().withSuccess(true).buildResponse();
		}
		
	}
	
	
	private boolean isDisponibilita(BigDecimal importoDaPrelevare,BigDecimal lastImporto){
		if((lastImporto.subtract(importoDaPrelevare).compareTo(BigDecimal.ZERO))==-1){
			return false;
		}
		else {
			return true;
		}
	
	}
	
	private Date addaDay(Date date){
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1); //minus number would decrement the days
        return cal.getTime();
	}
}
