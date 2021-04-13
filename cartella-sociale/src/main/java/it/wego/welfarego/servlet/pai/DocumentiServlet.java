/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.pai;

import com.google.common.base.Function;
import com.google.gson.Gson;
import it.wego.conversions.StringConversion;
import it.wego.extjs.beans.Order;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.CronologiaPaiBean;
import it.wego.welfarego.model.DocumentoBean;
import it.wego.welfarego.model.json.JSONDocumenti;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.utils.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class DocumentiServlet extends JsonServlet {

	private DocumentoBean serializer(PaiDocumento documento) {
		DocumentoBean bean = new DocumentoBean();
		String autore = documento.getCodUteAut().getCognome() + " " + documento.getCodUteAut().getNome();
		bean.setCodUteAut(autore);
		bean.setDtDoc(StringConversion.dateToItString(documento.getDtDoc()));
		bean.setNomeFile(documento.getNomeFile());
		bean.setIdDocumento(String.valueOf(documento.getIdDocumento()));
		if (documento.getPaiIntervento() != null) {
			bean.setPaiIntervento(documento.getPaiIntervento().getTipologiaIntervento().getDesTipint());
		}
		bean.setVer(String.valueOf(documento.getVer()));
		bean.setTipologia(documento.getTipoDetermina());
		return bean;
	}

	private String getAzioniButton(PaiDocumento documento) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"/CartellaSociale/DomandaServlet?idDoc=").append(documento.getIdDocumento()).append(
				"\"><img alt=\"Apri documento\" title=\"Apri documento\" src=\"/CartellaSociale/images/pencil.png\"></a>");
		return sb.toString();
	}

	@Override
	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method)
			throws Exception {
		String action = request.getParameter("action");
		Object result = null;
		String codPaiString = request.getParameter("codPai");
		EntityManager em = Connection.getEntityManager();
		if (action != null && "read".equals(action) && !codPaiString.equals("")) {

			Pai pai = new PaiDao(em).findPai(codPaiString);
			PaiDocumentoDao paiDocumentoDao = new PaiDocumentoDao(em);
			List<PaiDocumento> documenti = paiDocumentoDao.findByCodPai(pai);
			result = JsonBuilder.newInstance().withParameters(getParameters())
					.withSorter(request.getParameter("sorter")).withData(documenti)
					.withTransformer(new Function<PaiDocumento, DocumentoBean>() {
						public DocumentoBean apply(PaiDocumento documento) {
							return serializer(documento);
						}
					}).buildStoreResponse();
		}

		return result;
	}
}
