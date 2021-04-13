package it.wego.welfarego.servlet.appuntamenti;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.wego.conversions.StringConversion;
import it.wego.extjs.beans.Order;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.json.JSONAppuntamenti;
import it.wego.welfarego.persistence.dao.AppuntamentiDao;
import it.wego.welfarego.persistence.entities.Appuntamento;
import it.wego.welfarego.persistence.utils.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;

/**
 *
 * @author giuseppe
 */
public class AppuntamentiServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;
	
	private final DateFormat oraFormat = new SimpleDateFormat("kk:mm");

    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        EntityManager em = Connection.getEntityManager();
        try {
            int limit = getParameter("limit", Integer.class);
            int offset = getParameter("start", Integer.class);
            int codAnag = getParameter("codAnag", Integer.class);
            String sortString = getParameter("sort");
            Order order = null;
            if (sortString != null && !"".equals(sortString)) {
                order = Order.deserialize(sortString);
            }
            AppuntamentiDao dao = new AppuntamentiDao(em);
            List<Appuntamento> appuntamenti = dao.findAppuntamentyByCodAna(codAnag, limit, offset, order);

            List listaAppuntamenti = Lists.newArrayList(Iterables.transform(appuntamenti, new JsonMapTransformer<Appuntamento>() {
                @Override
                public void transformToMap(Appuntamento appuntamento) {
                    put("idApp", appuntamento.getIdApp().toString());
                    put("tsIniApp", StringConversion.dateToItString(appuntamento.getTsIniApp()));
                    put("nomeCompleto", appuntamento.getUtenti().getCognomeNome());
                    put("oraAppuntamento", oraFormat.format(appuntamento.getTsIniApp()));
                    put("fissato", appuntamento.getCodUte().getCognomeNome());
                    put("note", appuntamento.getNote());
                }
            }));

            JSONAppuntamenti jsonObj = new JSONAppuntamenti();
            jsonObj.setAppuntamenti(listaAppuntamenti);
            jsonObj.setSuccess(true);
            return jsonObj;
        }
        finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
