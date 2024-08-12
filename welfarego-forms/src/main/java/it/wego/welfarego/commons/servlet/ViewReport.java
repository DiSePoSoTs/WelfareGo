/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.commons.servlet;

import com.google.gson.reflect.TypeToken;
import it.trieste.comune.ssc.json.JSonUtils;
import it.wego.web.WebUtils;
import it.wego.welfarego.commons.utils.ReportUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author Muscas
 */
public class ViewReport extends AbstractReportServlet {

    private static Logger logger = LogManager.getLogger(ViewReport.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        OutputStream out = response.getOutputStream();
        try {
            Map<String, String> parameters = WebUtils.getParametersMap(request);
            byte[] resultContent = null;

            Map<String,String> reportParameters=JSonUtils.getGson().fromJson(parameters.get("report_parameters"),new TypeToken<Map<String,String>>(){}.getType());

            if (reportParameters.get("report_type") == null) {
                throw new Exception("Tipo report non supportato");
            }
            if (reportParameters.get("report_name") == null) {
                throw new Exception("Report non presente");
            }

            resultContent = ReportUtils.executeGenericReport(reportParameters.get("report_name") + ".jasper", reportParameters.get("report_type"), reportParameters);

            if (reportParameters.get("report_type").equalsIgnoreCase(ReportUtils.HTML)) {
                response.setContentType("text/html;charset=UTF-8");
            }

            if (reportParameters.get("report_type").equalsIgnoreCase(ReportUtils.XLS)) {
                response.setContentType("application/vnd.oasis.opendocument.spreadsheet");
                response.setHeader("Content-disposition", ": attachment;filename=" + reportParameters.get("report_name").replaceAll("[^a-zA-Z0-9]+", "_") + ".ods");
                response.setContentLength(resultContent.length);
            }

            if (reportParameters.get("report_type").equalsIgnoreCase(ReportUtils.RTF)) {
                response.setContentType("application/rtf");
                response.setHeader("Content-disposition", ": attachment;filename=" + reportParameters.get("report_name").replaceAll("[^a-zA-Z0-9]+", "_") + ".rtf");
                response.setContentLength(resultContent.length);
            }

            if (reportParameters.get("report_type").equalsIgnoreCase(ReportUtils.PDF)) {
                response.setContentType("application/pdf");
                response.setHeader("Content-disposition", ": attachment;filename=" + reportParameters.get("report_name").replaceAll("[^a-zA-Z0-9]+", "_") + ".pdf");
                response.setContentLength(resultContent.length);
            }

            out.write(resultContent);
            out.flush();

        } catch (Exception e) {
            response.setContentType("text/html;charset=UTF-8");
            out.write(e.getMessage().getBytes());
            logger.error("Errore durante la generazione del report", e);
        } finally {
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
