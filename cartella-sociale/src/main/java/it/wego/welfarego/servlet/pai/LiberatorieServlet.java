/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.pai;

import com.google.gson.Gson;
import it.trieste.comune.ssc.json.JsonSortInfo;
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.welfarego.model.LiberatoriaBean;
import it.wego.welfarego.model.json.JSONLiberatoria;
import it.wego.welfarego.persistence.dao.LiberatoriaDao;
import it.wego.welfarego.persistence.entities.Liberatoria;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author DOTCOM
 */
public class LiberatorieServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method)
			throws Exception {
		final EntityManager em = Connection.getEntityManager();
		LiberatoriaDao dao = new LiberatoriaDao(em);
		Integer offset = Integer.valueOf(getParameter("start"));
		Integer limit = Integer.valueOf(getParameter("limit"));
		Integer page = Integer.valueOf(getParameter("page"));
		String sort = getParameter("sort");
		Gson gson = new Gson();
		JsonSortInfo[] sortInfo = gson.fromJson(sort, JsonSortInfo.SORT_INFO_LIST_TYPE);
		List<Liberatoria> liberatorie = dao.getLiberatoriaByUser(Integer.valueOf(getParameter("codAna")),
				sortInfo[0].getProperty(), sortInfo[0].getDirection());
		JSONLiberatoria ret = new JSONLiberatoria();
		ret.setSuccess(Boolean.TRUE);
		ret.setTotal(liberatorie.size());
		Integer maxIndex = offset + (limit * page);
		if (liberatorie.size() > offset) {
			if (liberatorie.size() < maxIndex) {
				maxIndex = liberatorie.size();
			}
			ArrayList<LiberatoriaBean> list = new ArrayList<LiberatoriaBean>(maxIndex - offset);
			for (int i = offset; i < maxIndex; i++) {
				list.add(new LiberatoriaBean(liberatorie.get(i)));
			}
			ret.setData(list);
		}
		return ret;
	}

}
