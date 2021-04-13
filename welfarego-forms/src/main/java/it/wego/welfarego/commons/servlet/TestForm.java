/**
 * 
 */
package it.wego.welfarego.commons.servlet;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Strings;

import it.wego.persistence.objects.Condition;
import it.wego.persistence.objects.Order;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.UniqueTasklist;

/**
 * @author Fabio Bonaccorso
 *
 */
public class TestForm extends AbstractForm implements AbstractForm.Saveable{

	
	
	public Object save() throws Exception {
	
	String quanti=	getParameter("quanti");
	TaskDao tdao = new TaskDao(getEntityManager());
	PaiInterventoMeseDao pidao = new PaiInterventoMeseDao(getEntityManager());
	Integer offset = null;
	if(!Strings.isNullOrEmpty(quanti) && !quanti.equals("tutti")){
	  	offset= Integer.getInteger(quanti);
	  	
	}
	String query = "SELECT t FROM UniqueTasklist t WHERE t.desTask='Verifica dati esecutività' AND t.flgEseguito='N' ORDER BY t.tsCreazione desc";
           ;
           List<Condition> con = new ArrayList<Condition>();
           
           
	List<UniqueTasklist> tasks= tdao.find(UniqueTasklist.class,query,con,new ArrayList<Order>(),offset,null);
	for(UniqueTasklist task : tasks){
		initTransaction();
		task.setCampoForm1("nessuna nota");
		PaiIntervento pi = task.getPaiIntervento();
		Pai pai = pi.getPai();
	    insertEvento(pi,PaiEvento.PAI_UPDATE_INTERVENTO);
	    PaiInterventoMese pim = null ;
	    List<PaiInterventoMese> pims =  pidao.findForPaiInt(pai.getCodPai(),pi.getPaiInterventoPK().getCodTipint(),pi.getPaiInterventoPK().getCntTipint());
	  if(!pims.isEmpty()){
	    pim =  pidao.findForPaiInt(pai.getCodPai(),pi.getPaiInterventoPK().getCodTipint(),pi.getPaiInterventoPK().getCntTipint()).get(0);
	   BigDecimal res = new BigDecimal(pi.getDurMesi()).multiply(pi.getQuantita()).multiply(pi.getTipologiaIntervento().getImpStdCosto());
       if (Parametri.UNITA_MISURA_SETTIMANALI.contains(pi.getIdParamUniMis().getIdParam().getCodParam())) { //ore settimanali
           res = res.multiply(BigDecimal.valueOf(4));
       }
	   pim.setBdgPrevEur(res);
	  } 
	   task.setCampoForm1("M");
	   new TaskDao(getEntityManager()).markQueued(task,"S"); // TODO verificare sintassi esito
	   IntalioAdapter.executeJob();
	   commitTransaction();
	}
	
		return "tutto ok baby";
	}
     
}
