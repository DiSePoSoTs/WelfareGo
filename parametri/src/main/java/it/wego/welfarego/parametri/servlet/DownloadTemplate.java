/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.servlet;

import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Michele
 */
public class DownloadTemplate extends HttpServlet {

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

        ServletOutputStream out = response.getOutputStream();

        try {

            String cod_tmpl = request.getParameter("cod_tmpl");

            EntityManager em = Connection.getEntityManager();

            TemplateDao dao = new TemplateDao(em);

            Template tpl = dao.findByCodTemplate(Integer.valueOf(cod_tmpl));

            String nomeFile = tpl.getNomeFile().replaceFirst("[.]...$", ".odt"); // templates are always odt

            byte[] output = Base64.decodeBase64(tpl.getClobTmpl().getBytes());

            response.setContentType("application/octet-stream");
            response.setContentLength((int) output.length);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + nomeFile + "\"");

            out.write(output);

            out.flush();

        } finally {
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
