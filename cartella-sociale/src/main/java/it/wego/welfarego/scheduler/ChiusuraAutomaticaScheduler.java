package it.wego.welfarego.scheduler;



import java.util.List;

import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.utils.Connection;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;

public class ChiusuraAutomaticaScheduler implements MessageListener {
	 protected final static Logger logger = LoggerFactory.getLogger(ChiusuraAutomaticaScheduler.class);
     protected static EntityManager entityManager;
  


	public void receive(Message arg0) {
	   logger.info("Proviamo a vedere quanti PAI aperti da sei mesi fermi li ci sono");
	   
	   entityManager = Connection.getEntityManager();
	   PaiDao pdao = new PaiDao(entityManager);
	   List<Pai> chiusi = pdao.findVecchiDiSeiMesi();
	   
	   if(chiusi.isEmpty()){
		   logger.info("Non ci sono pai da chiudere");
	   }
	   else {
		   logger.info("Sono stati chiusi" + chiusi.size() +" pai");
	   }

	}

}
