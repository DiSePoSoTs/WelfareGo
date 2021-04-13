/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.cartella;

import com.google.gson.Gson;
import it.wego.welfarego.insiel.cartellasociale.client.ProxyAuthenticator;
import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.servlet.SessionConstants;
import it.wego.welfarego.utils.Log;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.Authenticator;

/**
 *
 * @author giuseppe
 */
public class StoreInSession extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        EntityManager em = Connection.getEntityManager();
        JSONGeneric json = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        setSSLModel();
        setProxyModel();
        HttpSession session = request.getSession();
        try {
            if (action != null && "connectedUser".equals(action)) {
                String username = request.getParameter("username");
                UtentiDao utentiDao = new UtentiDao(em);
                Utenti utente = utentiDao.findByUsername(username);
                if (utente != null) {
                    session.setAttribute(SessionConstants.CONNECTED_USER, utente);
                    json.setSuccess(true);
                    data.setMessage("Operazione eseguita correttamente");
                    json.setData(data);
                } else {
                    throw new Exception("Utente non registrato nella banca dati dei Servizi Sociali");
                }
            }
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            json.setSuccess(false);
            data.setMessage(ex.getMessage());
            json.setData(data);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
            Gson gson = new Gson();
            String jsonString = gson.toJson(json);
            out.write(jsonString);
            out.flush();
            out.close();
        }
    }

    private void setSSLModel() {
        ServletContext context = getServletContext();
        Boolean enableSSL = Boolean.valueOf(context.getInitParameter("enableSSL"));
        if (enableSSL) {
            String keystorePath = context.getInitParameter("pathKeystore");

            String keystorePassword = context.getInitParameter("keystorePassword");
            String keystorePathServer = context.getInitParameter("pathKeystoreServer");
            String keystorePasswordServer = context.getInitParameter("keystorePasswordServer");
            Log.APP.info("Setto le proprietà relative all'SSL");
            Log.APP.info("- javax.net.ssl.trustStore: " + context.getRealPath(keystorePathServer));
            System.setProperty("javax.net.ssl.trustStore", context.getRealPath(keystorePathServer));

            Log.APP.info("- javax.net.ssl.trustStorePassword: " + keystorePasswordServer);
            System.setProperty("javax.net.ssl.trustStorePassword", keystorePasswordServer);

            Log.APP.info("- javax.net.ssl.keyStore: " + context.getRealPath(keystorePath));
            System.setProperty("javax.net.ssl.keyStore", context.getRealPath(keystorePath));

            Log.APP.info("- javax.net.ssl.keyStorePassword: " + keystorePassword);
            System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
        }
    }

    private void setProxyModel() {
        EntityManager em = Connection.getEntityManager();
        try {
            if (Parametri.isProxyEnabled(em)) {
                Log.APP.info("Setto le proprietà del proxy");
                Log.APP.info("- proxySet: true");
                System.getProperties().put("http.proxySet", "true");
                Log.APP.info("- http.proxySet: true");
                System.getProperties().put("http.proxySet", "true");
                Log.APP.info("- http.proxyHost: " + Parametri.getProxyURL(em));
                System.getProperties().put("http.proxyHost", Parametri.getProxyURL(em));
                Log.APP.info("- http.proxyPort: " + Parametri.getProxyPort(em));
                System.getProperties().put("http.proxyPort", Parametri.getProxyPort(em));
                String username = Parametri.getProxyUsername(em);
                String password = Parametri.getProxyPassword(em);
                Log.APP.info("- proxy username: " + username);
                Log.APP.info("- proxy password: " + password);
                ProxyAuthenticator pa = new ProxyAuthenticator(username, password);
                Authenticator.setDefault(pa);
            }
        } catch (Exception ex) {
            Log.APP.error("Si è verificato un errore cercando di reperire la configurazione del proxy", ex);
        } finally {
            em.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
