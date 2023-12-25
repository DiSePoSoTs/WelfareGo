/**
 * 
 */
package it.wego.welfarego.servlet.pai;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import it.wego.extjs.json.JsonMapTransformer;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.json.JSONStruttura;
import it.wego.welfarego.persistence.dao.StrutturaDao;
import it.wego.welfarego.persistence.entities.Struttura;
import it.wego.welfarego.persistence.utils.Connection;

/**
 * @author fabio Bonaccorso 
 * Servlet che si occupa della gestione strutture 
 * @param <E>
 *
 */
public class StrutturaServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see it.wego.extjs.servlet.AbstractJsonServlet#handleJsonRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, it.wego.extjs.servlet.AbstractJsonServlet.Method)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Object handleJsonRequest(HttpServletRequest request,
			HttpServletResponse response, Method method) throws Exception {
		
		 final EntityManager em = Connection.getEntityManager();
		 JSONStruttura json = new JSONStruttura();
		 try{
			 String action = getParameter("action");
			if("lista".equals(action)){
				 String codTipInt = getParameter("codTipInt");
				 StrutturaDao sdao = new StrutturaDao(em);
				 List<Struttura> strutture = sdao.findStrutturaByCodTipInt(codTipInt);
				 
				 final Function<Struttura, Map> serializer = new JsonMapTransformer<Struttura>() {

					@Override
					public void transformToMap(Struttura obj) {
						put("id",obj.getId());
						put("nome",obj.getNome());
						put("indirizzo",obj.getIndirizzo());
						put("csrid",obj.getCsrId());
					}
				 };

				 json.setData(Collections2.transform(strutture, serializer));
				 
			}
			json.setSuccess(true);
			return json;
			 
		 }
		 finally{
			 if (em.isOpen()) {
	                em.close();
	            } 
		 }
	}
	
    
    

}
