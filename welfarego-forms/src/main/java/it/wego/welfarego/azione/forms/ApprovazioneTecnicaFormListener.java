package it.wego.welfarego.azione.forms;

import java.text.SimpleDateFormat;
import java.util.Date;

import it.wego.json.JsonForm;
import it.wego.json.JsonMessage;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.models.InterventoDataModel;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.entities.InterventiAssociati;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.UniqueForm;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;

/**
 *
 * @author Fabio Bonaccorso 
 */
@SuppressWarnings("deprecation")
public class ApprovazioneTecnicaFormListener  extends AbstractForm implements AbstractForm.Loadable,AbstractForm.Proceedable{
	

	@Override
	public Object load() throws Exception {
		getLogger().debug("handling load request");
		ApprovazioneTecnicaDataModel data=loadInterventoData(ApprovazioneTecnicaDataModel.class);
		data.setMotivazione(getTask().getPaiIntervento().getMotivazione());
		String informazioni;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		if(getTask().getPaiIntervento().getDurMesi()!=null){
			informazioni="Intervento con inizio in data:"+ format.format(getTask().getPaiIntervento().getDataAvvioProposta()) +" e durata  " + getTask().getPaiIntervento().getDurMesi() +" mesi";
		}
		else {
			informazioni = "Intervento con inizio in data:"+ format.format(getTask().getPaiIntervento().getDataAvvioProposta()) +" e data fine: " + format.format(getTask().getPaiIntervento().getDtFine());
		}
		data.setInformazioni(informazioni);
		return new JsonForm(data);
	}
	
	
	@Override
	public Object proceed() throws Exception {
		getLogger().debug("handling proceed request");
		initTransaction();
		ApprovazioneTecnicaDataModel model = getDataParameter(ApprovazioneTecnicaDataModel.class);
		PaiIntervento i = getTask().getPaiIntervento();
		UniqueTasklist t = new UniqueTasklist();
		t.setPaiIntervento(i);
		t.setRuolo(Utenti.COORDINATORE_UOT.name());
		t.setUot(i.getPai().getIdParamUot().getIdParam().getCodParam());
		switch(model.getEsito()){
		case approvato:
			t.setForm(getEntityManager().getReference(UniqueForm.class, UniqueForm.COD_FORM_VERIFICA_ESECUTIVITA));
        	t.setDesTask("Verifica dati esecutività");
        	i.setMotivazione(i.getMotivazione()+"\n info approvazione tecnica:"+ model.getInformazioni());
        	getEntityManager().persist(t);
        	break;
		
		case respinto:
			PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(getEntityManager());
			  i.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
	            i.setDtChius(new Date());
	            
	            i.setNoteChius("Respinta approvazione tecnica Con le seguenti info:" + model.getInformazioni());
	            i.setStatoAttuale(PaiIntervento.RESPINTO);
	            
	            paiInterventoMeseDao.removeAllProps(i);
	            //se l'intervento ha dei figli chiudo anche i figli
	            if(i.getInterventiFigli()!=null){
	            	for(InterventiAssociati ia : i.getInterventiFigli()){
	            		PaiIntervento intervento= ia.getInterventoFiglio();
	            		intervento.setStatoInt(PaiIntervento.STATO_INTERVENTO_CHIUSO);
	            	    intervento.setDtChius(new Date());
	                    intervento.setNoteChius("Approvazione tecnica non autorizzata");
	                    intervento.setStatoAttuale(PaiIntervento.RESPINTO);
	                    paiInterventoMeseDao.removeAllProps(intervento);
	                    
	            		
	            	}
	            }
	            //task di creazione avvertimento per assistente sociale 
	       	    insertEvento(PaiEvento.PAI_RESPINGI_INTERVENTO);
	       		new TaskDao(getEntityManager())
	            .withPaiIntervento(i)
	            .withForm(getEntityManager().getReference(UniqueForm.class, UniqueForm.COD_FORM_RESPINTO))
	            .withRuolo(Utenti.ASSISTENTE_SOCIALE_UOT.name())
	            .withUot(i.getPai().getIdParamUot().getIdParam().getCodParam()).withDesTask("Notifica intervento respinto")
	            .withCampoFlow1("Approvazione tecninca non concessa").insertNewTask();
	       	  //task di produzione lettera respinto 
	       	   new TaskDao(getEntityManager())
	          .withPaiIntervento(i)
	          .withForm(getEntityManager().getReference(UniqueForm.class, UniqueForm.COD_FORM_GENERAZIONE_DOCUMENTO))
	          .withRuolo(Utenti.OPERATORE_SEDE_CENTRALE.name())
	          .withUot(i.getPai().getIdParamUot().getIdParam().getCodParam())
	          .withDesTask("Predisposizione documento Diniego intervento")
	          .withTemplate(new TemplateDao(getEntityManager()).findByCodTemplate(new ConfigurationDao(getEntityManager()).getConfig("cod.template.domandarespinta"))).insertNewTask();
	            
	            i.getPai().setFlgStatoPai(Pai.STATO_APERTO);
	            break;
		default:
			break;
		}
		

	
		new TaskDao(getEntityManager()).markCompleted(getTask());
		commitTransaction();
		
		return new JsonMessage("operazione completata");
	}
	
	public static class ApprovazioneTecnicaDataModel extends InterventoDataModel {
		private String motivazione;
		private String informazioni;
		private Esito esito;
       
		
	  public static enum Esito {

	            approvato, rimandato, respinto
	      }
		public String getMotivazione() {
			return motivazione;
		}

		public void setMotivazione(String motivazione) {
			this.motivazione = motivazione;
		}

		public String getInformazioni() {
			return informazioni;
		}

		public void setInformazioni(String informazioni) {
			this.informazioni = informazioni;
		}
		
		   public Esito getEsito() {
	            return esito;
	        }

	        public void setEsito(Esito esito) {
	            this.esito = esito;
	        }
		
		
		
	}
}
