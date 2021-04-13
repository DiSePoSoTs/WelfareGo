/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.servlet;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.wego.dynodtpp.DynamicOdtUtils;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author aleph
 */
public class TemplateTestServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private byte[] latestContentXml, bytesOfXmlCartellaSociale;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (Objects.equal(req.getParameter("action"), "getLatestContentXml")) {
                response.setContentType("text/xml");
                IOUtils.write(latestContentXml, response.getOutputStream());
            } else if (Objects.equal(req.getParameter("action"), "getLatestPraticaDox")) {
                response.setContentType("text/xml");
                IOUtils.write(bytesOfXmlCartellaSociale, response.getOutputStream());
            } else {
                EntityManager entityManager = Connection.getEntityManager();
                DynamicOdtUtils dynamicOdtUtils = null;
                try {
                    dynamicOdtUtils = DynamicOdtUtils.newInstance();
                    String codPai = Strings.emptyToNull(req.getParameter("codPai")), codTemplate = Strings.emptyToNull(req.getParameter("codTemplate")),
                            codTipint = Strings.emptyToNull(req.getParameter("codTipint")), cntTipint = Strings.emptyToNull(req.getParameter("cntTipint"));
                    logger.info("testing template {} with data pai {}", codTemplate, codPai);

                    String xmlCartellaSociale;
                    if (codTipint != null && cntTipint != null) {
                    	logger.info("Testing with codipInt:"+ codTipint + " and cntTipInt:" + cntTipint);
                        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
                        PaiIntervento paiIntervento = paiInterventoDao.findByKey(Integer.parseInt(codPai), codTipint, cntTipint);
                        Preconditions.checkNotNull(paiIntervento, "paiIntervento not found for cod = %s %s %s", codPai, codTipint, cntTipint);
                        xmlCartellaSociale = Pratica.getXmlCartellaSociale(paiIntervento);
                    } else {
                        logger.info("Testing with solo codPai");

                        Pai pai = new PaiDao(entityManager).findPai(codPai);
                        Preconditions.checkNotNull(pai, "pai not found for cod = %s", codPai);
                        xmlCartellaSociale = Pratica.getXmlCartellaSociale(pai);
                    }

//                    logger.info("xmlCartellaSociale:\n" + xmlCartellaSociale  +"\n\n");

                    Template template = new TemplateDao(entityManager).findByCodTemplate(codTemplate);
                    Preconditions.checkNotNull(template, "template not found for cod = %s", codTemplate);
                    dynamicOdtUtils
                            .withTemplateBase64(template.getClobTmpl())
                            .withDataXml(bytesOfXmlCartellaSociale = xmlCartellaSociale.getBytes())
                            .withConfig(new ConfigurationDao(entityManager).getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX));

                    latestContentXml = dynamicOdtUtils.getContentXmlData();

                    dynamicOdtUtils.sendResponse(template.getNomeFile(), response);
                } finally {
                    entityManager.close();

                }
            }
        } catch (Exception ex) {
            logger.error("error = ", ex);
            throw new RuntimeException(ex);
        }
    }
}
