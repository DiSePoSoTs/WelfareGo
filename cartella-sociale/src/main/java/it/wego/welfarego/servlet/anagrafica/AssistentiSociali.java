/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.anagrafica;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonSortInfo;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.ComboBean;
import it.wego.welfarego.model.json.JSONCombo;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.entities.Utenti.Ruoli;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class AssistentiSociali extends JsonServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        EntityManager em = Connection.getEntityManager();
        String educatori = request.getParameter("educatori");
        try {
        	List<ComboBean> rows = null;
        	//se mi si chiedono gli educatori allora restituisco questi se no procediamo con la vecchia implementazione made in wego 
        	if(educatori!=null){
        		rows = Lists.newArrayList(Lists.transform(new UtentiDao(em).findByTipologia(Ruoli.EDU_UOT.toString()), new Function<Utenti, ComboBean>() {
                    public ComboBean apply(Utenti utente) {
                        ComboBean bean = new ComboBean();
                        bean.setName(utente.getCognomeNome());
                        bean.setValue(String.valueOf(utente.getCodUte()));
                        return bean;
                    }} ));
        		ComboBean empty = new ComboBean();
        		empty.setName("-");
        		empty.setValue(null);
        		rows.add(empty);
        	}
        	else {
            rows = Lists.newArrayList(Lists.transform(new UtentiDao(em).findByTipologia( Utenti.ASSISTENTE_SOCIALE_UOT.toString()), new Function<Utenti, ComboBean>() {
                public ComboBean apply(Utenti utente) {
                    ComboBean bean = new ComboBean();
                    bean.setName(utente.getCognomeNome());
                    bean.setValue(String.valueOf(utente.getCodUte()));
                    return bean;
                }
            }));
        	}
            Collections.sort(rows, JsonBuilder.buildComparator(new JsonSortInfo("name")));
            JSONCombo json = new JSONCombo();
            json.setRows(rows);
            json.setSuccess(true);
            return json;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
