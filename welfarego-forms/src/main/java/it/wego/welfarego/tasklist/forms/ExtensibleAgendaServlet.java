/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.tasklist.forms;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.AppuntamentiDao;
import it.wego.welfarego.persistence.dao.IndisponibilitaDao;
import it.wego.welfarego.persistence.dao.ParametriDao;
import it.wego.welfarego.persistence.entities.Appuntamento;
import it.wego.welfarego.persistence.entities.Indisponibilita;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Utenti;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author aleph
 */
public class ExtensibleAgendaServlet extends JsonServlet {

    private final static Integer APPUNTAMENTO_CID = 1, INDISPONIBILITA_CID = 2;

    private final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis().withZone(DateTimeZone.forID("CET"));
    private final Function<Appuntamento, Map> appuntamentoToMapTransformer = new JsonMapTransformer<Appuntamento>() {
        @Override
        public void transformToMap(Appuntamento appuntamento) {
            put("id", appuntamento.getIdApp());
            put("cid", APPUNTAMENTO_CID);
            put("start", dateTimeFormatter.print(appuntamento.getTsIniApp().getTime()).replaceFirst("[+].*", ""));
            put("end", dateTimeFormatter.print(appuntamento.getTsFineApp().getTime()).replaceFirst("[+].*", ""));
            put("notes", appuntamento.getNote());
            put("title", generaNotePerAppuntamento(appuntamento) + (Strings.isNullOrEmpty(appuntamento.getNote()) ? "" : (" : " + appuntamento.getNote())));
            put("ad", isAllDay(appuntamento.getTsIniApp(), appuntamento.getTsFineApp()));
        }
    };
    private final Function<Indisponibilita, Map> indisponibilitaToMapTransformer = new JsonMapTransformer<Indisponibilita>() {
        @Override
        public void transformToMap(Indisponibilita indisponibilita) {
            put("id", indisponibilita.getIdInd());
            put("cid", INDISPONIBILITA_CID);
            put("start", dateTimeFormatter.print(indisponibilita.getTsIniApp().getTime()).replaceFirst("[+].*", ""));
            put("end", dateTimeFormatter.print(indisponibilita.getTsFineApp().getTime()).replaceFirst("[+].*", ""));
            put("notes", "");
            put("title", indisponibilita.getUtenti().getCognomeNome() + " : non disponibile");
            boolean ad = isAllDay(indisponibilita.getTsIniApp(), indisponibilita.getTsFineApp());
            getLogger().debug("serializing indisponibilita' {} -> {} (allDay = {})", new Object[]{indisponibilita.getTsIniApp(), indisponibilita.getTsFineApp(), ad});
            put("ad", ad);
        }
    };


    private static LocalDateTime toMax(LocalDateTime date) {
        return date.hourOfDay().setCopy(22).minuteOfHour().withMinimumValue();
    }

    private static LocalDateTime toMin(LocalDateTime date) {
        return date.hourOfDay().setCopy(2).minuteOfHour().withMinimumValue();
    }

    private static boolean isAllDay(LocalDateTime from, LocalDateTime to) {
        return from.isBefore(from.hourOfDay().setCopy(4).minuteOfHour().withMinimumValue())
                && to.isAfter(to.hourOfDay().setCopy(20).minuteOfHour().withMinimumValue());
    }

    private static boolean isAllDay(Date from, Date to) {
        return isAllDay(new LocalDateTime(from), new LocalDateTime(to));
    }

    @Override
    public Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
        try {
            AppuntamentiDao appuntamentiDao = new AppuntamentiDao(persistenceAdapter.getEntityManager());
            IndisponibilitaDao indisponibilitaDao = new IndisponibilitaDao(persistenceAdapter.getEntityManager());
            String codUte = Strings.emptyToNull(getParameter("codUte"));
            Utenti utente = persistenceAdapter.getEntityManager().find(Utenti.class, Integer.valueOf(codUte));
            String uotFilter = Strings.emptyToNull(getParameter("uotFilter")), assSocFilter = Strings.emptyToNull(getParameter("assSocFilter"));
            switch (method) {
                case GET: {
                    LocalDateTime from = toMin(getParameter("startDate", LocalDateTime.class)),
                            to = toMax(getParameter("endDate", LocalDateTime.class));
                    getLogger().debug("got get request from {} to {}", from, to);
                    Iterable appuntamenti, indisponibilita;
                    if (assSocFilter != null) {
                        Utenti assSoc = persistenceAdapter.getEntityManager().find(Utenti.class, Integer.valueOf(assSocFilter));
                        Preconditions.checkNotNull(assSoc, "assSoc not found for filter = %s", assSocFilter);
                        appuntamenti = appuntamentiDao.findAppuntamenti(assSoc, from.toDate(), to.toDate());
                        indisponibilita = indisponibilitaDao.findIndisponibilita(assSoc, from.toDate(), to.toDate());
                    } else if (uotFilter != null) {
                        //TODO fix this for multiple uot
                        ParametriIndata uot = new ParametriDao(persistenceAdapter.getEntityManager()).findOneByCodParamTipParam(uotFilter, Parametri.CODICE_UOT).getParametriIndataSingle();
                        Preconditions.checkNotNull(uot, "uot not found for filter = %s", uotFilter);
                        appuntamenti = appuntamentiDao.findAppuntamenti(uot, from.toDate(), to.toDate());
                        indisponibilita = indisponibilitaDao.findIndisponibilita(uot, from.toDate(), to.toDate());
                    } else {
                        appuntamenti = appuntamentiDao.findAppuntamenti(from.toDate(), to.toDate());
                        indisponibilita = indisponibilitaDao.findIndisponibilita(from.toDate(), to.toDate());
                    }

                    return JsonBuilder.newInstance()
                            .withData(
                            Iterables.concat(
                            Iterables.transform(appuntamenti, appuntamentoToMapTransformer),
                            Iterables.transform(indisponibilita, indisponibilitaToMapTransformer)))
                            .buildStoreResponse();
                }
                case POST: {
                    Map<String, String> data = getRequestPayloadAsMap();
                    Integer cid = Integer.valueOf(data.get("cid"));
                    checkCid(cid);
                    getLogger().debug("got create request");
                    if (cid == APPUNTAMENTO_CID) {
                        Preconditions.checkArgument(false, "inserimento appuntamenti solo da cartella sociale");
                        return JsonBuilder.newInstance()
                                .withData(Collections.EMPTY_LIST)
                                .buildStoreResponse();
                    } else if (cid == INDISPONIBILITA_CID) {
                        LocalDateTime from = new LocalDateTime(data.get("start")),
                                to = new LocalDateTime(data.get("end"));
                        boolean allDay = Boolean.parseBoolean(data.get("ad"));
                        if (allDay) {
                            from = toMin(from);
                            to = toMax(to);
                        }
                        getLogger().debug("new indisponibilita' {} -> {} (allDay = {})", new Object[]{from, to, allDay});
                        Preconditions.checkArgument(from.isBefore(to));
                        Indisponibilita indisponibilita = new Indisponibilita();
                        indisponibilita.setTsIniApp(from.toDate());
                        indisponibilita.setTsFineApp(to.toDate());
                        indisponibilita.setUtenti(utente);
                        persistenceAdapter.insert(indisponibilita);
                        return JsonBuilder.newInstance()
                                .withData(indisponibilita)
                                .withTransformer(indisponibilitaToMapTransformer)
                                .buildStoreResponse();
                    }
                }
                case PUT: {
                    Map<String, String> data = getRequestPayloadAsMap();
                    Integer cid = Integer.valueOf(data.get("cid"));
                    checkCid(cid);
                    Integer id = Integer.valueOf(data.get("id"));

                    getLogger().debug("got update request for id = {} -> {}", data.get("id"), id);
                    LocalDateTime from = new LocalDateTime(data.get("start")),
                            to = new LocalDateTime(data.get("end"));
                    boolean allDay = Boolean.parseBoolean(data.get("ad"));
                    if (allDay) {
                        from = toMin(from);
                        to = toMax(to);
                    }
                    Preconditions.checkArgument(from.isBefore(to), "end date can't be before start date ( from %s -> to %s )", from, to);
                    getLogger().debug("salviamo modifiche data : {} -> {} (allDay = {})", new Object[]{from, to, allDay});
                    if (cid == APPUNTAMENTO_CID) {
                        persistenceAdapter.initTransaction();
                        Appuntamento appuntamento = persistenceAdapter.getEntityManager().find(Appuntamento.class, id);
                        Preconditions.checkNotNull(appuntamento, "appuntamento not found per id = %s", id);
                        appuntamento.setTsFineApp(to.toDate());
                        appuntamento.setTsIniApp(from.toDate());
                        String newNote = Strings.nullToEmpty(estraiNotePerAppuntamento(appuntamento, data.get("title")));
                        if (Objects.equal(newNote, Strings.nullToEmpty(appuntamento.getNote()))) {
                            newNote = Strings.nullToEmpty(data.get("notes"));
                        }
                        appuntamento.setNote(newNote);
                        persistenceAdapter.commitTransaction();
                        return JsonBuilder.newInstance()
                                .withData(appuntamento)
                                .withTransformer(appuntamentoToMapTransformer)
                                .buildStoreResponse();
                    } else if (cid == INDISPONIBILITA_CID) {
                        persistenceAdapter.initTransaction();
                        Indisponibilita indisponibilita = persistenceAdapter.getEntityManager().find(Indisponibilita.class, id);
                        Preconditions.checkNotNull(indisponibilita, "indisponibilita not found per id = %s", id);
                        indisponibilita.setTsFineApp(to.toDate());
                        indisponibilita.setTsIniApp(from.toDate());
                        persistenceAdapter.commitTransaction();
                        return JsonBuilder.newInstance()
                                .withData(indisponibilita)
                                .withTransformer(indisponibilitaToMapTransformer)
                                .buildStoreResponse();
                    }
                }
                case DELETE:
                    Map<String, String> data = getRequestPayloadAsMap();
                    Integer cid = Integer.valueOf(data.get("cid"));
                    checkCid(cid);
                    Integer id = Integer.valueOf(data.get("id"));
                    getLogger().debug("got delete request for id = {} -> {}", data.get("id"), id);
                    if (cid == APPUNTAMENTO_CID) {
                        Appuntamento appuntamento = persistenceAdapter.getEntityManager().find(Appuntamento.class, id);
                        Preconditions.checkNotNull(appuntamento, "appuntamento not found per id = %s", id);
                        persistenceAdapter.delete(appuntamento);
                        return JsonBuilder.newInstance().buildStoreResponse();
                    } else if (cid == INDISPONIBILITA_CID) {
                        Indisponibilita indisponibilita = persistenceAdapter.getEntityManager().find(Indisponibilita.class, id);
                        Preconditions.checkNotNull(indisponibilita, "indisponibilita not found per id = %s", id);
                        persistenceAdapter.delete(indisponibilita);
                        return JsonBuilder.newInstance().buildStoreResponse();
                    }
                default:
                    Preconditions.checkArgument(false);
                    return null;// unreacheable
            }
        } finally {
            persistenceAdapter.close();
        }
    }

    private static void checkCid(Integer cid) {
        Preconditions.checkArgument(cid == INDISPONIBILITA_CID || cid == APPUNTAMENTO_CID);
    }

    private static String generaNotePerAppuntamento(Appuntamento appuntamento) {
        return appuntamento.getCodPai().getAnagrafeSoc().getCognomeNome() + ", appuntamento con as " + appuntamento.getUtenti().getCognomeNome();
    }

    private static String estraiNotePerAppuntamento(Appuntamento appuntamento, String note) {
        return Strings.nullToEmpty(note).replaceFirst(generaNotePerAppuntamento(appuntamento) + "[ :]*", "");
    }

}
