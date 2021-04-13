package it.wego.welfarego.scheduler;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.VistaAnagrafeDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import it.wego.welfarego.persistence.utils.Connection;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;

public class MortiScheduler implements MessageListener {
	 protected final static Logger logger = LoggerFactory.getLogger(MortiScheduler.class);
     protected static EntityManager entityManager;
     private List<AnagrafeSoc> sincronizzati = new ArrayList<AnagrafeSoc>();


	public void receive(Message arg0) {
	   logger.info("Sincronizzazione con anagrafe per recupero morti");
	   
	   entityManager = Connection.getEntityManager();
	   PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
	   VistaAnagrafeDao vistaAnagrafeDao = new VistaAnagrafeDao(entityManager);
	   AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(entityManager);
	      for (PaiIntervento paiIntervento : paiInterventoDao.findByStatus(PaiIntervento.STATO_INTERVENTO_ESECUTIVO)) {
	    	  AnagrafeSoc anagrafe = paiIntervento.getPai().getAnagrafeSoc();
	    	  if(anagrafe.getCodAnaCom()!=null){
	    	  VistaAnagrafe vista = vistaAnagrafeDao.findByNumeroIndividuale(anagrafe.getCodAnaCom());
	    	 if(!sincronizzati.contains(anagrafe)){ 
	    	  if(anagrafe.getDtMorte()==null &&  vista!=null &&  vista.getDataMorte()!=null){
	    		  logger.info("ATTENZIONE il sigor/ra " + anagrafe.getNome() +" " + anagrafe.getCognome()+ "è morto (RIP) in data "+new SimpleDateFormat("dd/MM/yyyy").format(vista.getDataMorte())+ " aggiorniamo la sua data morte in welfarego ");
	    		  anagrafe.setDtMorte(vista.getDataMorte());
	    		
	    		 try{
	    		  anagrafeDao.update(anagrafe);
	    		  logger.info("Data morte correttamente aggiornata");
	    		 }
	    		 catch(Exception e){
	    			 e.printStackTrace();
	    			 logger.error("Attenzione si sono verificati errori e non ho potuto sincronizzare" +e.getMessage());
	    		 }
	    	  }
	    	  sincronizzati.add(anagrafe);
	    	 }  
	   	  }  
	      }
	}

}
