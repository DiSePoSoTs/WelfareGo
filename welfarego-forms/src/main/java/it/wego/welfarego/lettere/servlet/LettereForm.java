/**
 * 
 */
package it.wego.welfarego.lettere.servlet;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import it.wego.dynodtpp.DynamicOdtUtils;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.utils.IntalioAdapter;
import it.wego.welfarego.lettere.model.LettereBean;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.jopendocument.dom.ODSingleXMLDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static it.wego.persistence.ConditionBuilder.isEqual;

/**
 * @author Fabio Bonaccorso
 * Componenete lato server per la gestione della produzione massiva di lettere
 *
 */
public class LettereForm extends AbstractForm implements AbstractForm.Loadable, AbstractForm.Saveable {
    TaskDao dao = new TaskDao(getEntityManager());
	public Object save() throws Exception {
		Object result = null;
		
		
			LettereBean lb = getDataParameter(LettereBean.class);
			 Validate.isTrue(lb != null && lb.getEventi().size() > 0, "Almeno una lettera  deve essere selezionata");
			 Calendar date =   Calendar.getInstance();
			 String fileName = "lettere_" + date.get(Calendar.YEAR) + "_" + (date.get(Calendar.MONTH) + 1) + "_" + date.get(Calendar.DAY_OF_MONTH) + "_" + date.get(Calendar.HOUR_OF_DAY) + "_" + date.get(Calendar.MINUTE) + "_" + date.get(Calendar.SECOND) + ".odt";
	         String numeroProtocollo = getParameter("numProt");
	         String dtProtocollo = getParameter("dtProt");
	         String anteprima = Strings.emptyToNull(getParameter("anteprima"));
			 File file = new File ("/tmp/"+fileName);
			 ODSingleXMLDocument p1 = null;
		                                                                                                                                                                                                                                                                                                                                                                                                                                          
			 List<String> lettere = lb.getEventi();
			 int lettereProdotte = 0;
			 initTransaction();  
			 for(String lettera : lettere){
				 UniqueTasklist task = dao.findTask(lettera);
				 if (task!=null){
					        
					  Template template = task.getCodTmpl();
					 Mandato mandato = Strings.isNullOrEmpty(task.getExtraParameterFromCampoFlow8("idMandato")) ? null : getEntityManager().find(Mandato.class, Integer.valueOf(task.getExtraParameterFromCampoFlow8("idMandato")));
				        String data = mandato == null ? Pratica.getXmlCartellaSociale(task.getPaiIntervento(),numeroProtocollo,dtProtocollo) : Pratica.getXmlCartellaSociale(mandato,numeroProtocollo,dtProtocollo);
				        byte[] documentResult = DynamicOdtUtils.newInstance()
				                .withTemplateBase64(template.getClobTmpl())
				                .withDataXml(data)
				                .withConfig(new ConfigurationDao(getEntityManager()).getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX))
				                .getResult();
				     File temporaneo = new File("/tmp/temporaneo"+lettera+"odt");
				  
				     FileUtils.writeByteArrayToFile(temporaneo, documentResult);
				     if(p1==null){
				    	 p1 = ODSingleXMLDocument.createFromPackage(temporaneo);
				     }
				     else {
				     ODSingleXMLDocument p2 = ODSingleXMLDocument.createFromPackage(temporaneo);
				     p1.add(p2);
				     }
				     temporaneo.delete();
				     
				    
				       if(anteprima==null){
				       new PaiDocumentoDao(getEntityManager()).createDoc(task.getCodPai(),new UtentiDao(getEntityManager()).findByUsername(getLiferayUser().getLogin()),task.getPaiIntervento(),getCodTipDoc(task), Base64.encodeBase64String(documentResult), template.getDesTmpl().replaceAll(" ","_") + ".odt");
				      
                       new TaskDao(getEntityManager()).markQueued(task);

                       IntalioAdapter.executeJob();
                       new TaskDao(getEntityManager()).markCompleted(task);
				       }
                       lettereProdotte++;
				        
				 }
				 
				
			 }
			 commitTransaction();
			 p1.saveAs(file);
			result= JsonBuilder.newInstance().withSuccess(true).withValue("filename",fileName).withMessage("Sono state prodotte "+lettereProdotte+" lettere").buildResponse();
			
		

		return result;
		
		
	}

	public Object load() throws Exception {
		 String uotFilter = getParameter("uotFilter");
	     String tipoFilter = getParameter("tipoLettereFilter");
		 List<Condition> conditions= new ArrayList<Condition>();
		 new ConditionBuilder();
		 if(tipoFilter!=null && !tipoFilter.equals("all")){
		 conditions.add(ConditionBuilder.isEqual("t.desTask", tipoFilter));
		 }
		 else {
			 conditions.add(ConditionBuilder.like("t.desTask", "Produzione lettera"));
		 }
		 
		 if (uotFilter != null) {
	            conditions.add(isEqual(TaskDao.TASK_COD_PARAM_UOT, uotFilter));
	        }
		 List<UniqueTasklist> lettereDaProdurre = dao.findAllActiveTask(conditions, null,null,null); 
		 return JsonBuilder.newInstance().withData(lettereDaProdurre).withParameters(getParameters()).withTransformer(taskTransformerFunction).buildStoreResponse();
		
	}
	
	@SuppressWarnings("rawtypes")
	private static final Function<UniqueTasklist, Map> taskTransformerFunction =  new JsonMapTransformer<UniqueTasklist>() {

		@Override
		public void transformToMap(UniqueTasklist obj) {
			put("id",obj.getId());
			put("taskid",obj.getTaskid());
			put("data_task", obj.getTsCreazione() == null ? null : obj.getTsCreazione().getTime() / 1000);
			put("nome",obj.getCodPai().getAnagrafeSoc().getNome());
			put("cognome",obj.getCodPai().getAnagrafeSoc().getCognome());
			put("uot",obj.getCodPai().getIdParamUot().getDesParam());
			put("attivita", obj.getDesTask().contains("mandato")? "Lettera mandato": "Lettera contributo");
			put("intervento",obj.getPaiIntervento().getTipologiaIntervento().getDesTipint());
			
		}
	};

}
