/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.commons.servlet;

import it.wego.json.JSonUtils;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.web.WebUtils;
import it.wego.welfarego.commons.utils.ReportUtils;
import it.wego.welfarego.determine.model.PreviewReportBean;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;
import org.joda.time.DateTime;

import com.google.common.base.Strings;

/**
 *
 * @author Mess
 */
public class PreviewReport extends AbstractReportServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		CalcolaCostoInterventoService calcolaCostoInterventoService = new CalcolaCostoInterventoService();
		EntityManager em = null;
		try {
			Map<String, String> parameters = WebUtils.getParametersMap(request);
			if (parameters.get("parameters") == null) {
				throw new Exception("Parametri insufficienti per generare il report");
			}
			PreviewReportBean data = JSonUtils.getGson().fromJson(parameters.get("parameters"), PreviewReportBean.class);
			Map parametersMap = new HashMap();

			String nomeReport = "", nomeFile = "";
			byte[] compiledReport = null;
			em = Connection.getEntityManager();
			if (data.getTipoReport().toUpperCase().equals("DETERMINE")) {
				
		           List<PaiIntervento> interventi = new ArrayList<PaiIntervento>(data.getEventi().size());
				   List<String> paiInterventoPKnewList = new ArrayList<String>(data.getEventi().size());
				   PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
			       EntityManager entityManager = persistenceAdapter.getEntityManager();
                   PaiInterventoMeseDao pmdao = new PaiInterventoMeseDao(entityManager);
                 
                   PaiInterventoDao pdao = new PaiInterventoDao(entityManager);
                 
                     
                   Map<PaiIntervento,Object> interventiDurata= new HashMap<PaiIntervento,Object>();
                   Map<PaiIntervento , BigDecimal> budgetCosto = new HashMap<PaiIntervento,BigDecimal>();
            	if(!Strings.isNullOrEmpty(data.getProroghe())){
            		  
                    for (String idEvento : data.getEventi()) {
                        PaiEvento evento = Connection.getEntityManager().find(PaiEvento.class, Integer.valueOf(idEvento));
                        PaiInterventoPK paiInterventoPK = evento.getPaiIntervento().getPaiInterventoPK();
                     
                        interventi.add(evento.getPaiIntervento());
                    }
            	
            		//caso di anteprima di proroghe....
            		//prendo il numero dei mesi proroga o il dal al .
            		Integer numeroMesiProroga = Strings.isNullOrEmpty(data.getMesi())==false&&data.getMesi()!="0"?Integer.valueOf(Strings.emptyToNull(data.getMesi())):null;
                   	Date al = null;
                
                   	if(Strings.emptyToNull(data.getAl())!=null){
                   		
                   		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                   		al = format.parse(data.getAl());
                   	}
                   	//scorro la lista degli interventi .
              		
                   	for(PaiIntervento vecchio : interventi ){
                   	   interventiDurata.put(vecchio, vecchio.getDtFine()!=null?vecchio.getDtFine():vecchio.getDurMesi());
                   		if(al!=null){
                   			DateTime dataFineOriginale= new DateTime(vecchio.getDtFine());
                   			DateTime dataFineNuova = new DateTime(al);
                   			//attenzione faccio questa cosa per evitare che se si fà l'anteprima di un intervento che ha la data fine originale prima della data fine nuova la data fine originale venga sovrascritta 
                   			if(dataFineNuova.isBefore(dataFineOriginale)){
                   			vecchio.setDtFine(al);
                   			}
                   		}
                   		else {
                   			vecchio.setDurMesi(numeroMesiProroga);
                   		}
                   		pdao.update(vecchio);

						BigDecimal costo = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(vecchio, numeroMesiProroga));

                   		for(PaiInterventoMese proposta : vecchio.getPaiInterventoMeseList()){
                   			if(proposta.getBdgPrevEur().compareTo(BigDecimal.ZERO)==1){
                   		       budgetCosto.put(vecchio,proposta.getBdgPrevEur());
                   				proposta.setBdgPrevEur(costo);
                   				pmdao.update(proposta);
                   			}
                   		}
                   		                   			
                   		}
                   		
                   	}//fine duplicazione interventi 
                   	// 
                  
                  PaiEvento primoEvento = em.find(PaiEvento.class, Integer.valueOf(data.getEventi().get(0)));
				nomeReport = ReportUtils.getNomeFileReportDetermine(primoEvento.getPaiIntervento());

				nomeFile = nomeReport.replaceFirst("jasper$", "ods");
				parametersMap.put("tipo_intervento", data.getTipIntervento());
				parametersMap.put("lista_eventi",data.getEventi());
				compiledReport = ReportUtils.executeDetermineReport(nomeReport, ReportUtils.XLS, parametersMap);

				
				for(PaiIntervento i : interventi){
					Object durata = interventiDurata.get(i);
					if(durata.getClass().equals(Date.class)){
						i.setDtFine((Date)durata);
					}
					else {
						i.setDurMesi((Integer)durata);
					}
					pdao.update(i);
					for(PaiInterventoMese m : i.getPaiInterventoMeseList()){
						if(m.getBdgPrevEur().compareTo(BigDecimal.ZERO)==1){
							BigDecimal costo = budgetCosto.get(i);
							m.setBdgPrevEur(costo);
							pmdao.update(m);
						}
					}
					
				}
				
			}
			if (data.getTipoReport().toUpperCase().equals("LISTE_ATTESA")) {
				nomeReport = "report_liste_attesa_" + data.getTipIntervento() + ".jasper";
				nomeFile = "report_liste_attesa_" + data.getTipIntervento() + ".ods";
				parametersMap.put("tipo_intervento", data.getTipIntervento());
				compiledReport = ReportUtils.executeListeAttesaReport(nomeReport, ReportUtils.XLS, parametersMap);
			}

			response.setContentType("application/ms-excel");
			response.setHeader("Content-disposition", ": attachment;filename=" + nomeFile);
			response.setContentLength(compiledReport.length);

			OutputStream out = response.getOutputStream();
			out.write(compiledReport);
			out.flush();
			out.close();

		} catch (Exception ex) {
			throw new ServletException("Errore durante la creazione per il Report di anteprima", ex);

		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public void doPost(HttpServletRequest request,
			  HttpServletResponse response)
			  throws IOException, ServletException {
		doGet(request, response);
	}
}
