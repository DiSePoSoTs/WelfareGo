/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.condizione;

import com.google.gson.Gson;
import it.wego.welfarego.CSRException;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.model.json.JSONCondizioneResponse;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.serializer.AnagrafeSocSerializer;
import it.wego.welfarego.utils.Log;
import it.wego.welfarego.utils.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class CondizioneServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONCondizioneResponse json = new JSONCondizioneResponse();
        EntityManager em = Connection.getEntityManager();
        try {
            String codAnag = request.getParameter("codAnag");
            AnagrafeSocSerializer serializer = new AnagrafeSocSerializer(em);
            AnagrafeSocDao anagrafeDao = new AnagrafeSocDao(em);
            AnagrafeSoc anagrafe = anagrafeDao.findByCodAna(Integer.valueOf(codAnag));
            em.getTransaction().begin();
            serializer.serializeCondizione(request);
            CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withAnagrafeSoc(anagrafe).sincronizzaCartellaSociale();
            em.getTransaction().commit();

            json.setMessage("Operazione eseguita correttamente");
            json.setSuccess(true);
            json.setMessage("Operazione eseguita correttamente");
        } catch (Exception ex) {
            json.setSuccess(false);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            json.setMessage(ex.getMessage());
            Log.SQL.error("Errore eseguendo la query di inserimento della condizione", ex);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
            Gson gson = new Gson();
            String jsonString = gson.toJson(json);
            out.write(jsonString);
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
