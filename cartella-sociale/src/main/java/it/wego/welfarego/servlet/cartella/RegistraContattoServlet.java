/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.cartella;

import com.google.gson.Gson;
import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.model.json.JSONGeneric.Data;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Contatto;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.servlet.SessionConstants;
import it.wego.welfarego.utils.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class RegistraContattoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

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
        String motivazioneChiusura = request.getParameter("motivazioneChiusura");
        String note = request.getParameter("note");
        String codAnag = request.getParameter("codAnag");
        Utenti connectedUser = (Utenti) request.getSession().getAttribute(SessionConstants.CONNECTED_USER);
        EntityManager em = Connection.getEntityManager();
        JSONGeneric json = new JSONGeneric();
        try {
            em.getTransaction().begin();
            Contatto contatto = new Contatto();
            contatto.setTsCont(new Date());
            contatto.setNote(note);
            contatto.setCodUte(connectedUser);
            AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(em);
            AnagrafeSoc anagrafe = anagrafeSocDao.findByCodAna(Integer.valueOf(codAnag));
            contatto.setCodAna(anagrafe);
            ParametriIndataDao parametriDao = new ParametriIndataDao(em);
            contatto.setIdParamMotiv(parametriDao.findByIdParamIndata(Integer.valueOf(motivazioneChiusura)));
            em.persist(contatto);
            em.getTransaction().commit();
            json.setSuccess(true);
            Data data = new Data();
            data.setMessage("Operazione eseguita correttamente");
            json.setData(data);
        } catch (Exception ex) {
            Log.APP.error("Si è verificato un errore nella registrazione del contatto", ex);
            if (em != null) {
                em.getTransaction().rollback();
            }
            Data data = new Data();
            data.setMessage(ex.getMessage());
            json.setSuccess(false);
            json.setData(data);
        } finally {
        	if(em!=null){
            if ( em.isOpen()) {
                em.close();
            }
        	}
            Gson gson = new Gson();
            String jsonString = gson.toJson(json);
            out.write(jsonString);
            out.flush();
            out.close();
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
