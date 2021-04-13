/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.cartella;

import it.wego.dynodtpp.DynamicOdtUtils;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.utils.Log;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author giuseppe
 */
public class StampaServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        String action = request.getParameter("action");
        EntityManager em = Connection.getEntityManager();
        try {
            TemplateDao templateDao = new TemplateDao(em);
            String URL = Parametri.getURLDocumentiDinamici(em);
            if (action != null && "domanda".equals(action)) {
                String codAna = request.getParameter("codAnag");
                String codTemplate = Parametri.getCodiceTemplateDomanda(em);
                AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(em);
                PaiDao paiDao = new PaiDao(em);
                AnagrafeSoc anagrafe = anagrafeDao.findByCodAna(Integer.valueOf(codAna));
                Pai pai = paiDao.findLastPai(Integer.valueOf(codAna));
                String parameters = "";
                try {
                    parameters = Pratica.getXmlCartellaSociale(anagrafe, pai);
                } catch (Exception ex) {
                    response.setContentType("text/html;charset=UTF-8");
                    out.write("Si è verificato un errore generando l'XML per il template. Contattare l'amministratore".getBytes());
                    Log.APP.error("Si è verificato un errore generando l'XML per il template", ex);
                }
                Template template = templateDao.findByCodTemplate(Integer.valueOf(codTemplate));
                try {
                    DynamicOdtUtils.newInstance()
                            .withTemplateBase64(template.getClobTmpl())
                            .withDataXml(parameters)
                            .withConfig(new ConfigurationDao(em).getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX))
                            .sendResponse(template.getNomeFile(), response);
                } catch (Exception ex) {
                    response.setContentType("text/html;charset=UTF-8");
                    out.write("Si è verificato un errore cercando di stampare il template della domanda".getBytes());
                    Log.APP.error("Si è verificato un errore cercando di stampare il template", ex);
                }
            }
            if (action != null && "cartella".equals(action)) {
                String codAna = request.getParameter("codAnag");
                String codTemplate = Parametri.getCodiceTemplateCartellaSociale(em);
                AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(em);
                PaiDao paiDao = new PaiDao(em);
                AnagrafeSoc anagrafe = anagrafeDao.findByCodAna(Integer.valueOf(codAna));
                Pai pai = paiDao.findLastPai(Integer.valueOf(codAna));
                String parameters = "";
                try {
                    parameters = Pratica.getXmlCartellaSociale(anagrafe, pai);
                } catch (Exception ex) {
                    response.setContentType("text/html;charset=UTF-8");
                    out.write("Si è verificato un errore generando l'XML per il template. Contattare l'amministratore".getBytes());
                    Log.APP.error("Si è verificato un errore generando l'XML per il template", ex);
                }
                if (logger.isDebugEnabled()) {
                    File tFile = File.createTempFile("stampa_cartella_data_", ".xml");
                    FileUtils.write(tFile, parameters);
                    logger.debug("stampa cartella, dumped data to file = {}", tFile.getCanonicalPath());
                }
                Template template = templateDao.findByCodTemplate(Integer.valueOf(codTemplate));

                try {


                    DynamicOdtUtils.newInstance()
                            .withTemplateBase64(template.getClobTmpl())
                            .withDataXml(parameters)
                            .withConfig(new ConfigurationDao(em).getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX))
                            .sendResponse(template.getNomeFile(), response);
                } catch (Exception ex) {
                    response.setContentType("text/html;charset=UTF-8");
                    out.write("Si è verificato un errore cercando di stampare il template della cartella".getBytes());
                    Log.APP.error("Si è verificato un errore cercando di stampare il template", ex);
                }
            }
            if (action != null && "diario".equals(action)) {
            	  String codAna = request.getParameter("codAnag");
            	  String codTemplate = Parametri.getCodiceTemplateDiario(em);
                  AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(em);
                  PaiDao paiDao = new PaiDao(em);
                  AnagrafeSoc anagrafe = anagrafeDao.findByCodAna(Integer.valueOf(codAna));
                  Pai pai = paiDao.findLastPai(Integer.valueOf(codAna));
                  String parameters = "";
                  try {
                      parameters = Pratica.getXmlCartellaSociale(anagrafe, pai);
                  } catch (Exception ex) {
                      response.setContentType("text/html;charset=UTF-8");
                      out.write("Si è verificato un errore generando l'XML per il template. Contattare l'amministratore".getBytes());
                      Log.APP.error("Si è verificato un errore generando l'XML per il template", ex);
                  }
                  if (logger.isDebugEnabled()) {
                      File tFile = File.createTempFile("stampa_diario_data_", ".xml");
                      FileUtils.write(tFile, parameters);
                      logger.debug("stampa cartella, dumped data to file = {}", tFile.getCanonicalPath());
                  }
                  Template template = templateDao.findByCodTemplate(Integer.valueOf(codTemplate));

                  try {
                
                      DynamicOdtUtils.newInstance()
                              .withTemplateBase64(template.getClobTmpl())
                              .withDataXml(parameters)
                              .withConfig(new ConfigurationDao(em).getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX))
                              .sendResponse(template.getNomeFile(), response);
                  } catch (Exception ex) {
                      response.setContentType("text/html;charset=UTF-8");
                      out.write("Si è verificato un errore cercando di stampare il template del diario".getBytes());
                      Log.APP.error("Si è verificato un errore cercando di stampare il template", ex);
                  }
                  
                  
            	  
            }
            
        } finally {
            out.flush();
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
