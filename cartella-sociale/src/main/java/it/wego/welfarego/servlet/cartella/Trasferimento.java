/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.cartella;

import com.google.gson.Gson;
import it.wego.conversions.StringConversion;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.model.json.JSONMessage;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.CartellaDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.CartellaSociale;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.servlet.SessionConstants;
import it.wego.welfarego.utils.Log;
import it.wego.welfarego.xsd.pratica.Pratica;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class Trasferimento extends HttpServlet {

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
        String datachiusura = request.getParameter("datachiusura");
        String motivazione = request.getParameter("motivazione");
        String note = request.getParameter("note");
        Utenti connectedUser = (Utenti) request.getSession().getAttribute(SessionConstants.CONNECTED_USER);
        String codAnag = request.getParameter("codAnag");
        PrintWriter out = response.getWriter();
        EntityManager em = Connection.getEntityManager();
        JSONMessage json = new JSONMessage();
        try {
            PaiDao paiDao = new PaiDao(em);
            PaiInterventoDao paiIntDao = new PaiInterventoDao(em);
            int codAna = Integer.valueOf(codAnag);
            Pai lastPai = paiDao.findLastPai(codAna);
            if (lastPai == null) {
                throw new Exception("Non risultano PAI attivi per l'utente");
            }
            List<Pai> paiList = paiDao.findPaiByCodAna(codAna);
            Date chiusura = StringConversion.itStringToDate(datachiusura);
            CartellaDao cartellaDao = new CartellaDao(em);
            CartellaSociale cartella = cartellaDao.findByCodAna(Integer.valueOf(codAnag));

            int motivazioneTrasferimento = Parametri.getCodiceMotivazioneTrasferimento(em);
            int motivazioneDecesso = Parametri.getCodiceMotivazioneDecesso(em);
            int motivazioneId = Integer.valueOf(motivazione).intValue();
            if (paiList != null) {
                em.getTransaction().begin();
                for (Pai pai : paiList) {
                    AnagrafeSoc ana = pai.getCodAna().getAnagrafeSoc();
                    if (motivazioneId == motivazioneTrasferimento) {
                        if (ana.getLuogoResidenza().getComune() != null && ana.getLuogoResidenza().getComune().getDesCom().toUpperCase().equals("TRIESTE")) {
                            throw new Exception("E' necessario modificare la residenza. L'utente risulta residente nel comune di Trieste");
                        }
                    }
                    if (motivazioneId == motivazioneDecesso) {
                        if (ana.getDtMorte() == null) {
                            throw new Exception("Non è stata specificata la data di decesso dell'utente");
                        }
                    }
                    if (pai.getDtChiusPai() != null) {
                        pai.setDtChiusPai(chiusura);
                        pai.setMotivChius(note);
                        CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withPai(pai).sincronizzaCartellaSociale();
                        List<PaiIntervento> interventi = paiIntDao.findByCodPai(pai.getCodPai());
                        for (PaiIntervento intervento : interventi) {
                            if (intervento.getDtChius() != null) {
                                intervento.setDtChius(chiusura);
                            }
                            PaiEventoDao paiEventoDao = new PaiEventoDao(em);

                            //TODO trasferimento?
                            PaiEvento evento = Pratica.serializePaiEvento(pai, intervento, cartella, PaiEvento.PAI_CHIUDI_INTERVENTO, connectedUser);
                            paiEventoDao.insert(evento);
                        }
                    }
                }
                em.getTransaction().commit();
                json.setMessage("Operazione eseguita correttamente");
                json.setSuccess(true);
            } else {
                throw new Exception("Non risultano PAI attivi per l'utente");
            }
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            Log.APP.error("Si è verificato un errore cercando di eseguire l'operazione di trasferimento/decesso", ex);
            json.setMessage(ex.getMessage());
            json.setSuccess(false);
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
