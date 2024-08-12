package it.wego.welfarego.servlet.pai;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.trieste.comune.ssc.servlet.AbstractJsonServlet;
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.welfarego.model.json.JSONStruttura;
import it.wego.welfarego.persistence.dao.TariffaDao;
import it.wego.welfarego.persistence.entities.Tariffa;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Servlet che si occupa della gestione delle tariffe per struttura.
 *
 * @author DOTCOM S.R.L.
 */
public class TariffaServlet extends JsonServlet
{

	/* (non-Javadoc)
	 * @see it.wego.extjs.servlet.AbstractJsonServlet#handleJsonRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, it.wego.extjs.servlet.AbstractJsonServlet.Method)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Object handleJsonRequest(HttpServletRequest request,
			HttpServletResponse response, AbstractJsonServlet.Method method) throws Exception
	{

		final EntityManager em = Connection.getEntityManager();
		JSONStruttura json = new JSONStruttura();
		try
		{
			String action = getParameter("action");
			if ("list".equals(action))
			{
				String idStrutt = getParameter("idStruttura");
				if (idStrutt != null && !idStrutt.isEmpty() && NumberUtils.isDigits(idStrutt))
				{
					Integer idStruttura = Integer.parseInt(idStrutt);
					TariffaDao tdao = new TariffaDao(em);
					List<Tariffa> tariffe = tdao.findTariffaByStruttura(idStruttura);
					final Function<Tariffa, Map> serializer = new JsonMapTransformer<Tariffa>()
					{
						@Override
						public void transformToMap(Tariffa obj)
						{
							put("anno", obj.getAnno());
							put("costo", obj.getCosto());
							put("descrizione", obj.getDescrizione());
							put("id", obj.getId());
							put("forfait", obj.getForfait()=='S'? "Si" : "No");
						}
					};
					json.setData(Collections2.transform(tariffe, serializer));
					json.setSuccess(Boolean.TRUE);
					return json;
				}
				else
				{
					json.setSuccess(Boolean.FALSE);
					return json;
				}
			}
		}
		finally
		{
			if (em.isOpen())
			{
				em.close();
			}
		}
		return null;
	}
}
