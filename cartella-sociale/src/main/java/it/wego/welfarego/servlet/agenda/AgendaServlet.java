/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.servlet.agenda;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.wego.conversions.StringConversion;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.model.DettaglioBean;
import it.wego.welfarego.model.GiornoBean;
import it.wego.welfarego.model.ImpegnoBean;
import it.wego.welfarego.model.OrarioBean;
import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.model.json.JSONImpegni;
import it.wego.welfarego.model.json.JSONOrario;
import it.wego.welfarego.persistence.dao.AppuntamentiDao;
import it.wego.welfarego.persistence.dao.IndisponibilitaDao;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.Appuntamento;
import it.wego.welfarego.persistence.entities.Indisponibilita;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.utils.Log;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author giuseppe
 */
public class AgendaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
    public void init() throws ServletException {
        super.init();
        Locale.setDefault(Locale.ITALY);
    }

    @SuppressWarnings("deprecation")
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String action = request.getParameter("action");

            if (Objects.equal(action, "load")) {
                loadImpegni(request, response);
            } else if (Objects.equal(action, "insert")) {
                insert(request, response);
            } else if (Objects.equal(action, "detail")) {
                loadDetail(request, response);
            }
            if (Objects.equal(action, "null") || action == null) {
                Log.APP.error("action is null");
            }else {
                Preconditions.checkArgument(false, "unhandled action code = '%s'", action);
            }

        } catch (Exception ex) {
            Log.APP.error("Errore nella richiesta", ex);
            throw new RuntimeException(ex);
        }
    }

    private void loadImpegni(HttpServletRequest request, HttpServletResponse response) throws Exception {

        JsonBuilder jsonBuilder = JsonBuilder.newInstance().withWriter(response.getWriter());
        JSONImpegni jsonObj = new JSONImpegni();
        EntityManager em = Connection.getEntityManager();
        try {
            ImpegnoBean impegno = new ImpegnoBean();

            String currentDateStr = request.getParameter("agenda_current_date");
            Date currentDate = StringConversion.stringToDate(MoreObjects.firstNonNull(Strings.emptyToNull(currentDateStr),
                    new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())), "yyyy-MM-dd");

            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(currentDate);
            int giornoSettimana = currentCal.get(Calendar.DAY_OF_WEEK);
            int lunediNumero = currentCal.getFirstDayOfWeek();
            int giorniDaLunedi = giornoSettimana - lunediNumero;

            currentCal.add(Calendar.DAY_OF_YEAR, giorniDaLunedi * -1);

            Date lunedi = currentCal.getTime();
            List<GiornoBean> giorni = new ArrayList<GiornoBean>();
            GiornoBean giorno;

            for (int i = 0; i < 7; i++) {
                giorno = new GiornoBean();
                giorno.setDesc(StringConversion.dateToItDayWeekString(currentCal.getTime()));
                giorno.setTipo("I");
                giorni.add(giorno);

                currentCal.add(Calendar.DAY_OF_YEAR, 1);
            }
            Date domenica = currentCal.getTime();

            List<List<OrarioBean>> orario = new ArrayList<List<OrarioBean>>();

            Map<String, OrarioBean> indisponibilita = new HashMap<String, OrarioBean>();
            Map<String, OrarioBean> appuntamenti = new HashMap<String, OrarioBean>();
            OrarioBean orarioBean;
            PaiDao paiDao = new PaiDao(em);
            int codAnag = Integer.valueOf(request.getParameter("codAnag"));
            Pai pai = paiDao.findLastPai(codAnag);
            if (pai == null) {
                pai = paiDao.findLastClosedPai(codAnag);
            }
            
            Integer codAs = Integer.valueOf(request.getParameter("codAs"));
            if(codAs==null){
            	 codAs = pai.getCodUteAs().getCodUte();
            }

            IndisponibilitaDao indisponibilitaDao = new IndisponibilitaDao(em);
            List<Indisponibilita> indisponibilitaList = indisponibilitaDao.findIndisponibilitaByCodAs(codAs, lunedi, domenica);
            AppuntamentiDao appuntamentiDao = new AppuntamentiDao(em);
            List<Appuntamento> appuntamentiList = appuntamentiDao.findAppuntamentiByCodAs(codAs, lunedi, domenica);

            int giornoLoop, hour, minute;
            String hourIdLoop;
            Calendar cal = Calendar.getInstance();

            for (Indisponibilita indisponibilitaLoop : indisponibilitaList) {
                orarioBean = new OrarioBean();
                Date loopDate = indisponibilitaLoop.getTsIniApp();
                Date fineDate=  indisponibilitaLoop.getTsFineApp();
                if(loopDate.before(lunedi)){
                	loopDate=lunedi;
                }
                if(fineDate.after(domenica)){
                	fineDate=domenica;
                }
                while (loopDate.before(fineDate)) {
                    cal.setTime(loopDate);
                    giornoLoop = cal.get(Calendar.DAY_OF_WEEK) - 2;

                    hour = cal.get(Calendar.HOUR_OF_DAY);
                    minute = cal.get(Calendar.MINUTE);

                    if (minute >= 30) {
                        hourIdLoop = hour + ":30-" + (hour + 1) + ":00";
                    } else {
                        hourIdLoop = hour + ":00-" + hour + ":30";
                    }
                    cal.add(Calendar.MINUTE, 30);
                    loopDate = cal.getTime();

                    orarioBean.setId(hourIdLoop + "-" + giornoLoop);
                    orarioBean.setTipo(2);
                    orarioBean.setGiorno(giornoLoop);
                    orarioBean.setOra(hourIdLoop);
                    orarioBean.setIdImpegno(indisponibilitaLoop.getIdInd().toString());
                    orarioBean.setDesc("[" + StringConversion.dateToHourString(indisponibilitaLoop.getTsIniApp()) + "-" + StringConversion.dateToHourString(indisponibilitaLoop.getTsFineApp()) + "]");
                    indisponibilita.put(orarioBean.getId(), orarioBean);
                }
            }
            for (Appuntamento appuntamentoLoop : appuntamentiList) {
                orarioBean = new OrarioBean();
                Date loopDate = appuntamentoLoop.getTsIniApp();
                while (loopDate.before(appuntamentoLoop.getTsFineApp())) {
                    cal.setTime(loopDate);

                    giornoLoop = cal.get(Calendar.DAY_OF_WEEK) - 2;
                    hour = cal.get(Calendar.HOUR_OF_DAY);
                    minute = cal.get(Calendar.MINUTE);

                    if (minute >= 30) {
                        hourIdLoop = hour + ":30-" + (hour + 1) + ":00";
                    } else {
                        hourIdLoop = hour + ":00-" + hour + ":30";
                    }
                    cal.add(Calendar.MINUTE, 30);
                    loopDate = cal.getTime();

                    orarioBean.setId(hourIdLoop + "-" + giornoLoop);
                    orarioBean.setTipo(3);
                    orarioBean.setGiorno(giornoLoop);
                    orarioBean.setOra(hourIdLoop);
                    orarioBean.setIdImpegno(appuntamentoLoop.getIdApp().toString());
                    orarioBean.setDesc(appuntamentoLoop.getCodPai().getCodAna().getAnagrafeSoc().getCognome() + " " + appuntamentoLoop.getCodPai().getCodAna().getAnagrafeSoc().getNome());
                    orarioBean.setDesc(orarioBean.getDesc() + " [" + StringConversion.dateToHourString(appuntamentoLoop.getTsIniApp()) + "-" + StringConversion.dateToHourString(appuntamentoLoop.getTsFineApp()) + "]");
                    appuntamenti.put(orarioBean.getId(), orarioBean);
                }
            }

            List<OrarioBean> oreSettimana;
            OrarioBean oraGiorno;
            for (int i = 7; i < 20; i++) {
                for (int j = 0; j < 2; j++) {
                    oreSettimana = new ArrayList<OrarioBean>();
                    oraGiorno = new OrarioBean();
                    oraGiorno.setTipo(1);
                    if (j == 0) {
                        oraGiorno.setDesc(i + ":00-" + i + ":30");
                    } else {
                        oraGiorno.setDesc(i + ":30-" + (i + 1) + ":00");
                    }
                    oreSettimana.add(oraGiorno);

                    for (int k = 0; k < 7; k++) {
                        if (j == 0 && indisponibilita.containsKey(i + ":00-" + i + ":30" + "-" + k)) {
                            oreSettimana.add(indisponibilita.get(i + ":00-" + i + ":30" + "-" + k));
                        } else {
                            if (j == 1 && indisponibilita.containsKey(i + ":30-" + (i + 1) + ":00" + "-" + k)) {
                                oreSettimana.add(indisponibilita.get(i + ":30-" + (i + 1) + ":00" + "-" + k));
                            } else {
                                if (j == 0 && appuntamenti.containsKey(i + ":00-" + i + ":30" + "-" + k)) {
                                    oreSettimana.add(appuntamenti.get(i + ":00-" + i + ":30" + "-" + k));
                                } else {
                                    if (j == 1 && appuntamenti.containsKey(i + ":30-" + (i + 1) + ":00" + "-" + k)) {
                                        oreSettimana.add(appuntamenti.get(i + ":30-" + (i + 1) + ":00" + "-" + k));
                                    } else {
                                        oraGiorno = new OrarioBean();
                                        oraGiorno.setDesc("");
                                        oraGiorno.setTipo(4);
                                        oreSettimana.add(oraGiorno);
                                    }
                                }
                            }
                        }
                    }
                    orario.add(oreSettimana);
                }
            }
            impegno.setGiorni(giorni);
            impegno.setOre(orario);

            jsonObj.setSuccess(true);
            jsonObj.setImpegno(impegno);
            jsonBuilder.sendResponse(jsonObj);
        } catch (Exception e) {
            Log.APP.error("Si è verificato un errore", e);
            jsonBuilder.withError(e).buildResponse();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    private void insert(HttpServletRequest request, HttpServletResponse response) throws Exception {

        JsonBuilder jsonBuilder = JsonBuilder.newInstance().withWriter(response.getWriter());

        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();
        try {
            PaiDao paiDao = new PaiDao(em);
            Integer codAna = Integer.valueOf(request.getParameter("codAna"));

            Pai pai = paiDao.findLastPai(codAna);
            Preconditions.checkNotNull(pai, "Non è stato selezionata la UOT e/o l'assistente sociale. Selezionarli nella sezione Anagrafica, salvare e fissare quindi l'appuntamento");

            IndisponibilitaDao indisponibilitaDao = new IndisponibilitaDao(em);
            AppuntamentiDao appuntamentiDao = new AppuntamentiDao(em);

            String impegnoNote = request.getParameter("note");
            String impegnoData = request.getParameter("dataAppuntamento");
            String impegnoDalle = request.getParameter("dalleOre");
            String impegnoAlle = request.getParameter("alleOre");
            boolean forceInsert = Boolean.parseBoolean(request.getParameter("forceInsert"));

            Date impegnoDataDate = StringConversion.itStringToDate(impegnoData);
            Date impegnoDalleDate = StringConversion.hourStringToDate(impegnoDalle);
            Date impegnoAlleDate = StringConversion.hourStringToDate(impegnoAlle);

            Calendar impegnoDataCal = Calendar.getInstance();
            impegnoDataCal.setTime(impegnoDataDate);
            Calendar impegnoDalleCal = Calendar.getInstance();
            impegnoDalleCal.setTime(impegnoDalleDate);
            Calendar impegnoAlleCal = Calendar.getInstance();
            impegnoAlleCal.setTime(impegnoAlleDate);


            impegnoDalleCal.set(Calendar.DAY_OF_YEAR, impegnoDataCal.get(Calendar.DAY_OF_YEAR));
            impegnoDalleCal.set(Calendar.YEAR, impegnoDataCal.get(Calendar.YEAR));
            impegnoAlleCal.set(Calendar.DAY_OF_YEAR, impegnoDataCal.get(Calendar.DAY_OF_YEAR));
            impegnoAlleCal.set(Calendar.YEAR, impegnoDataCal.get(Calendar.YEAR));

            if (!impegnoAlleCal.after(impegnoDalleCal)) {
                throw new Exception("La fine dell'appuntamento deve essere sucessiva al suo inizio");
            }

            Integer codAs =  Integer.valueOf(request.getParameter("appuntamentoAssistenteSociale"));

            Preconditions.checkArgument(forceInsert
                    || !appuntamentiDao.containsAppuntamento(codAs, impegnoDalleCal.getTime(), impegnoAlleCal.getTime(), impegnoDataDate),
                    "errore_sovrapposizione_intervallo");

            if (indisponibilitaDao.containsIndisponibilita(codAs, impegnoDalleCal.getTime(), impegnoAlleCal.getTime(), impegnoDataDate)) {
                throw new Exception("L'intervallo selezionato si sovrappone con un periodo di 'Indisponibilità'");
            }

            Appuntamento appuntamento = new Appuntamento();

            UtentiDao udao = new UtentiDao(em);
            appuntamento.setUtenti(udao.findByCodUte(codAs.toString()));
            appuntamento.setCodAs(udao.findByCodUte(codAs.toString()).getCodUte());
           
            Preconditions.checkNotNull(appuntamento.getUtenti());
            appuntamento.setTsIniApp(impegnoDalleCal.getTime());
            String codUte = request.getParameter("as");
            UtentiDao utenti = new UtentiDao(em);
            Utenti utente = utenti.findByCodUte(codUte);
            appuntamento.setCodUte(utente);
            appuntamento.setNote(impegnoNote);
            appuntamento.setTsFineApp(impegnoAlleCal.getTime());

            em.getTransaction().begin();


            appuntamento.setCodPai(pai);

            em.persist(appuntamento);
            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);
            jsonObj.setMessage(data.getMessage());

            jsonBuilder.sendResponse(jsonObj);
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            if (e instanceof RollbackException && e.getCause() != null && e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                e = new IllegalArgumentException("e' gia' presente un'appuntamento con questa data di inizio", e);
            }
            jsonBuilder.withError(e).buildResponse();

            Log.APP.error("Si è verificato un errore", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    private void loadDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer codAs = 2;
        JsonBuilder jsonBuilder = JsonBuilder.newInstance().withWriter(response.getWriter());

        JSONOrario jsonObj = new JSONOrario();
        EntityManager em = Connection.getEntityManager();
        try {
            String tipo = request.getParameter("tipo");
            String idImpegno = request.getParameter("idImpegno");

            IndisponibilitaDao indisponibilitaDao = new IndisponibilitaDao(em);
            AppuntamentiDao appuntamentiDao = new AppuntamentiDao(em);

            DettaglioBean dettaglioBean = new DettaglioBean();
            if (tipo.equalsIgnoreCase("2")) {
                Indisponibilita indisponibilita = indisponibilitaDao.findByKey(idImpegno);
                dettaglioBean.setTipo(2);
                dettaglioBean.setTipoHidden(2);
                dettaglioBean.setDalleOre(StringConversion.dateToHourString(indisponibilita.getTsIniApp()));
                dettaglioBean.setDataAppuntamento(StringConversion.dateToItString(indisponibilita.getTsIniApp()));
                dettaglioBean.setAlleOre(StringConversion.dateToHourString(indisponibilita.getTsFineApp()));
                dettaglioBean.setNome("");
                dettaglioBean.setNote("");
                dettaglioBean.setAs(String.valueOf(codAs));
                dettaglioBean.setTs(indisponibilita.getIdInd().toString());

            }
            if (tipo.equalsIgnoreCase("3")) {
                Appuntamento appuntamento = appuntamentiDao.findByKey(idImpegno);
                dettaglioBean.setTipo(3);
                dettaglioBean.setTipoHidden(3);
                dettaglioBean.setDalleOre(StringConversion.dateToHourString(appuntamento.getTsIniApp()));
                dettaglioBean.setDataAppuntamento(StringConversion.dateToItString(appuntamento.getTsIniApp()));
                dettaglioBean.setAlleOre(StringConversion.dateToHourString(appuntamento.getTsFineApp()));
                dettaglioBean.setNome(appuntamento.getCodPai().getAnagrafeSoc().getCognome() + " " + appuntamento.getCodPai().getAnagrafeSoc().getNome());
                dettaglioBean.setNote(appuntamento.getNote());
                dettaglioBean.setAs(String.valueOf(codAs));
                dettaglioBean.setTs(appuntamento.getIdApp().toString());
            }

            jsonObj.setSuccess(true);
            jsonObj.setImpegno(dettaglioBean);


            jsonBuilder.sendResponse(jsonObj);
        } catch (Exception e) {
            Log.APP.error("Si è verificato un errore", e);
            jsonBuilder.withError(e).buildResponse();

        } finally {
            if (em.isOpen()) {
                em.close();
            }

        }
    }

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
    }
}
