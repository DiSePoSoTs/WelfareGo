package it.wego.welfarego.determine.servlet;


import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import it.wego.conversions.StringConversion;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.web.WebUtils;
import it.wego.webdav.DavResourceContext;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.abstracts.AbstractServlet;
import it.wego.welfarego.azione.stores.ImpegniStoreListener;
import it.wego.welfarego.commons.utils.ToolsUtils;
import it.wego.welfarego.determine.model.PaiEventoBean;
import it.wego.welfarego.determine.model.PreviewReportBean;
import it.wego.welfarego.determine.servlet.logica.applicativa.DeterminaBusinessLogic;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.constants.Documenti;
import it.wego.welfarego.persistence.dao.PaiEventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.*;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.persistence.validators.PaiInterventoBudgetValidator;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;
import it.wego.welfarego.webdav.MultipleDocumentDavResource;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static it.wego.welfarego.determine.servlet.logica.applicativa.DeterminaBusinessLogic.findEvento;



/*
 * @author Mess
 */
public class DetermineForm extends AbstractForm implements AbstractForm.Loadable, AbstractForm.Proceedable {

    private static final Map<StatoIntervento, Character[]> statoInt2statiInt;
    private static final Function<PaiEventoBean, Map<String,Object>> determineTransformerFunction = new JsonMapTransformer<PaiEventoBean>() {
        @Override
        public void transformToMap(PaiEventoBean bean) {
            put("id", bean.getId());
            put("cognome", bean.getCognome());
            put("nome", bean.getNome());
            put("data_richiesta", bean.getData());
            put("data_fine_intervento", bean.getDataFineIntervento());
            put("isee", bean.getIsee());
            put("uot", bean.getUot());
            put("stato", bean.getStato());
            put("costo", bean.getCostoTotale());
            put("quantita", bean.getQuantitaTotale());
            put("budget", bean.getBudget());

        }
    };

    static {
        Map<StatoIntervento, Character[]> map = new EnumMap<StatoIntervento, Character[]>(StatoIntervento.class);
        map.put(StatoIntervento.AR, new Character[]{PaiIntervento.STATO_INTERVENTO_APERTO, PaiIntervento.STATO_INTERVENTO_RIMANDATO});
        map.put(StatoIntervento.E, new Character[]{PaiIntervento.STATO_INTERVENTO_ESECUTIVO});
        map.put(StatoIntervento.C, new Character[]{PaiIntervento.STATO_INTERVENTO_CHIUSO});
        statoInt2statiInt = Collections.unmodifiableMap(map);
    }

    private Gson gson = new Gson();

    private DeterminaBusinessLogic determinaBusinessLogic = null;


    public DetermineForm(){
        EntityManager entityManager = getEntityManager();
        determinaBusinessLogic = new DeterminaBusinessLogic(entityManager);
    }

    @Override
    public Object load() throws Exception {

        getLogger().debug("handling load request");

        String impegniProroghe = Strings.emptyToNull(getParameter("impegniProroghe"));
        if (impegniProroghe != null) {
            //se mi servono gli impegni per prorogare allora faccio questo...
            PreviewReportBean data = gson.fromJson(getParameter("parameters"), PreviewReportBean.class);
            if (data == null || data.getEventi().size() == 0) {
                return null;
            }

            String tipoIntervento = getParameter("tipo_intervento");
            Integer numeroMesiProroga = Strings.isNullOrEmpty(getParameter("numero_mesi")) == false && getParameter("numero_mesi") != "0" ? Integer.valueOf(Strings.emptyToNull(getParameter("numero_mesi"))) : null;
            Date al = null;
            if (Strings.emptyToNull(getParameter("al")) != null) {

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                al = format.parse(getParameter("al"));
            }
            JsonBuilder b = calcolaBudgetProroghe(numeroMesiProroga, al, data, tipoIntervento);
            return b.buildStoreResponse().getData();
        }
        String budgetIntervento = Strings.emptyToNull(getParameter("budgetIntervento"));
        if (budgetIntervento != null && Strings.emptyToNull(getParameter("idEvento")) != null) {
            if (getParameter(AbstractServlet.METHOD_PROP).equals(AbstractServlet.METHOD_GET)) {
                return new ImpegniStoreListener(this).load();
            } else {
                return new ImpegniStoreListener(this).update();
            }
        }


        String requireDocument = getParameter("requireDocument");
        if (requireDocument != null) {
            PreviewReportBean data = gson.fromJson(getParameter("parameters"), PreviewReportBean.class);
            DavResourceContext resourceContext = DavResourceContext.getResourceContext(getParameter(WebUtils.REQUEST_PATH_PROP_KEY), "/DetermineServlet([^a-zA-Z0-9].*)?", ToolsUtils.getConfig("it.wego.wefarego.webdav.port"));
            List<PaiInterventoPK> paiInterventoPKList = new ArrayList<PaiInterventoPK>(data.getEventi().size());
            for (String idEvento : data.getEventi()) {
                PaiEvento evento = Connection.getEntityManager().find(PaiEvento.class, Integer.valueOf(idEvento));
                PaiInterventoPK paiInterventoPK = evento.getPaiIntervento().getPaiInterventoPK();
                paiInterventoPKList.add(paiInterventoPK);
            }
            switch (Document.valueOf(requireDocument.toUpperCase())) {
                case DETERMINA:
                    return JsonBuilder.newInstance()
                            .withData(resourceContext.getJavascriptFunctionForResource(new MultipleDocumentDavResource(paiInterventoPKList, Documenti.TIPO_DOC_TEMPLATE_DETERMINA))).buildResponse();
                case ALLEGATO:
                    return JsonBuilder.newInstance()
                            .withData(resourceContext.getJavascriptFunctionForResource(new MultipleDocumentDavResource(paiInterventoPKList, Documenti.TIPO_DOC_REPORT_DETERMINA))).buildResponse();
                default:
                    throw new UnsupportedOperationException("supposed unreacheable code");
            }
        } else {
            if (getParameter("tipo_intervento") == null || getParameter("stato_intervento") == null) {
                return Collections.EMPTY_LIST;
            }
            String tipoIntervento = getParameter("tipo_intervento");
            StatoIntervento statoIntervento = StatoIntervento.valueOf(getParameter("stato_intervento"));
            Date dataAvvio = null;
            if (!Strings.isNullOrEmpty(getParameter("data_avvio"))) {
                dataAvvio = StringConversion.itStringToDate(getParameter("data_avvio"));
            }
            List<PaiEventoBean> listaDetermineBean = new ArrayList<PaiEventoBean>();
            List<PaiIntervento> interventiDaDeterminare = new ArrayList<PaiIntervento>();
            for (PaiEvento paiEvento : new PaiEventoDao(getEntityManager()).findDetermine(tipoIntervento, dataAvvio, statoInt2statiInt.get(statoIntervento))) {
                if (!interventiDaDeterminare.contains(paiEvento.getPaiIntervento())) {
                    listaDetermineBean.add(PaiEventoBean.fromPaiEvento(paiEvento));
                    interventiDaDeterminare.add(paiEvento.getPaiIntervento());
                } else {
                    getLogger().info("Attenzione doppio evento cambio flag stampcosi non verra ripescato ");
                    paiEvento.setFlgDxStampa('N');
                    new PaiEventoDao(getEntityManager()).update(paiEvento);
                }
            }

            JsonBuilder jsonBuilder = JsonBuilder.newInstance().withData(listaDetermineBean);
            jsonBuilder = jsonBuilder.withTransformer(determineTransformerFunction);
            jsonBuilder = jsonBuilder.withParameters(getParameters());
            return jsonBuilder.buildStoreResponse();
        }
    }

    @Override
    public Object proceed() throws Exception {
        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(getEntityManager());

        boolean escludiVerificaEsistenzaBudget = Boolean.valueOf(getParameter("escludiVerificaEsistenzaBudget"));
        boolean parameterProrogheIsNull = Strings.isNullOrEmpty(getParameter("proroghe"));
        String numero_mesi = getParameter("numero_mesi");
        String al = getParameter("al");

        PreviewReportBean previewReportBean = getDataParameter(PreviewReportBean.class);
        Validate.isTrue(previewReportBean != null && previewReportBean.getEventi().size() > 0, "Almeno un PaiIntervento deve essere selezionato");

        marca_interventi_per_non_essere_processati_dallo_schedulatore(paiInterventoDao, previewReportBean, escludiVerificaEsistenzaBudget);

        if (parameterProrogheIsNull == false) {
            determinaBusinessLogic.determina_parzialmente(previewReportBean, numero_mesi, al, escludiVerificaEsistenzaBudget);
        }

        determinaBusinessLogic.procedi_con_transazioni(previewReportBean);

        return JsonBuilder.buildResponse("I documenti per la determina sono stati generati correttamente.");
    }


    public Object proceedPerTest() throws Exception {
        PreviewReportBean previewReportBean = getDataParameter(PreviewReportBean.class);
        Validate.isTrue(previewReportBean != null && previewReportBean.getEventi().size() > 0, "Almeno un PaiIntervento deve essere selezionato");
        return previewReportBean;
    }


    /**
     * meotodo per la simulazione delle proroghe da mostrare agli amministrativi in griglia .
     *
     * @param mesiProroga
     * @param al
     * @param data
     * @param tipoIntervento
     * @return
     */
    private JsonBuilder calcolaBudgetProroghe(Integer mesiProroga, Date al, PreviewReportBean data, final String tipoIntervento) {

        CalcolaCostoInterventoService calcolaCostoInterventoService = new CalcolaCostoInterventoService();


        //verifichiamo che la nuova data fine non sia superiore alla data fine

        //vediamo prima i budget da aggiungere
        final PaiInterventoMeseDao pimdao = new PaiInterventoMeseDao(getEntityManager());
        final PaiInterventoDao pdao = new PaiInterventoDao(getEntityManager());
        List<BudgetTipIntervento> budgets = new ArrayList<BudgetTipIntervento>();
        List<PaiIntervento> pi = new ArrayList<PaiIntervento>();
        for (String e : data.getEventi()) {
            PaiEvento evento = Connection.getEntityManager().find(PaiEvento.class, Integer.valueOf(e));
            if (!pi.contains(evento.getPaiIntervento())) {
                pi.add(evento.getPaiIntervento());
            }

            for (PaiInterventoMese pim : evento.getPaiIntervento().getPaiInterventoMeseList()) {
                if (pim.getBdgPrevEur().compareTo(BigDecimal.ZERO) > 0 && !budgets.contains(pim.getBudgetTipIntervento())) {
                    budgets.add(pim.getBudgetTipIntervento());
                }
            }
        }
        //cosi ho la lista di tutti i budget che saranno colpiti dal cambiamento.
        //verifichiamo che la nuova data fine non sia superiore alla data fine

        //adesso calcolo per ogni budget quanto andrà aggiunto al budget impegnato ( quantità che ritorna ) e quanto andrà sottratto dal budget proroghe ( quantità che sarà prorogata )
        final HashMap<BudgetTipIntervento, BigDecimal> budgetdaSottrarre = new HashMap<BudgetTipIntervento, BigDecimal>();
        for (BudgetTipIntervento budget : budgets) {
            budgetdaSottrarre.put(budget, BigDecimal.ZERO);
        }

        // per ogni intervento mi calcolo quanto sarà prorogato
        for (PaiIntervento intervento : pi) {
            List<PaiInterventoMese> proposte = intervento.getPaiInterventoMeseList();


            if (mesiProroga != null) {
                DateTime dataInizio = new DateTime(intervento.getDtAvvio());
                DateTime nuovaDataFine = dataInizio.plusMonths(mesiProroga);

                Validate.isTrue(nuovaDataFine.isAfter(new DateTime(intervento.getDtAvvio())), "Attenzione l'intervento di " + intervento.getPai().getAnagrafeSoc().getCognome() + " " + intervento.getPai().getAnagrafeSoc().getNome() + " non può essere determinato per questa durata di tempo poichè la data di fine impostate è  inferiore alla data di inizio intervento originale");
                if (nuovaDataFine.isAfter(new DateTime(intervento.calculateDtFine()))) {
                    nuovaDataFine = new DateTime(intervento.calculateDtFine());
                }
                DateTime fineAnno = new DateTime(Calendar.getInstance().get(Calendar.YEAR), 12, 31, 23, 59);
                //se la nuova data di fine è prima della fine dell'anno nessun problema
                Validate.isTrue(nuovaDataFine.isBefore(fineAnno), "ATTENZIONE l'intervento di " + intervento.getPai().getAnagrafeSoc().getCognome() + " " + intervento.getPai().getAnagrafeSoc().getNome() + "non può essere determinato per qquesta durata perchè la data di fine va oltre la fine dell'anno in corso");

                BigDecimal vecchioCosto = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(intervento));
                BigDecimal nuovoCosto = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(intervento, mesiProroga));


            /*	//faccio un check se la quantità è settimanale in questo caso moltiplico per 4
                if(p.getTipologiaIntervento().getIdParamUniMis().getDesParam().contains("sett")){
    				nuovoCosto=nuovoCosto.multiply(new BigDecimal(4));
    				vecchioCosto= vecchioCosto.multiply(new BigDecimal(4));
    			}*/
                for (PaiInterventoMese proposta : proposte) {
                    //se trovo un pai intervento mese con proposta maggiore di 0 e che abbia l'anno di spesa uguale
                    if (budgets.contains(proposta.getBudgetTipIntervento()) && proposta.getBudgetTipIntervento().getAnnoErogazione() == Calendar.getInstance().get(Calendar.YEAR)) {
                        BigDecimal dc = budgetdaSottrarre.get(proposta.getBudgetTipIntervento()).add(vecchioCosto.subtract(nuovoCosto));
                        budgetdaSottrarre.put(proposta.getBudgetTipIntervento(), dc);
                        break;
                    }
                }
            }

            else if (al != null) {
                DateTime nuovaDataFine = new DateTime(al);


                Validate.isTrue(nuovaDataFine.isAfter(new DateTime(intervento.getDtAvvio())),
                        "Attenzione l'intervento di " + intervento.getPai().getAnagrafeSoc().getCognome() + " " + intervento.getPai().getAnagrafeSoc().getNome() +
                                " non può essere determinato per questa durata di tempo poichè la data di fine impostata è  inferiore alla data di inizio intervento originale");
                //se la nuova data fine è superiore alla data fine dell'intervento allora metto come nuova data fine la data fine dell'intervento stesso .
                if (nuovaDataFine.isAfter(new DateTime(intervento.calculateDtFine()))) {
                    nuovaDataFine = new DateTime(intervento.calculateDtFine());
                }
                DateTime fineAnno = new DateTime(Calendar.getInstance().get(Calendar.YEAR), 12, 31, 23, 59);

                Validate.isTrue(nuovaDataFine.isBefore(fineAnno), "ATTENZIONE l'intervento di " + intervento.getPai().getAnagrafeSoc().getCognome() + " " + intervento.getPai().getAnagrafeSoc().getNome() + "non può essere determinato per qquesta durata perchè la data di fine va oltre la fine dell'anno in corso");

                BigDecimal vecchioCosto = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(intervento));
                BigDecimal nuovoCosto = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(intervento, al));

                for (PaiInterventoMese proposta : proposte) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(intervento.getDtAvvio());
                    //se trovo un pai intervento mese con proposta maggiore di 0 e che abbia l'anno di spesa uguale
                    if (budgets.contains(proposta.getBudgetTipIntervento()) && proposta.getBudgetTipIntervento().getAnnoErogazione() == cal.get(Calendar.YEAR)) {
                        BigDecimal dc = budgetdaSottrarre.get(proposta.getBudgetTipIntervento()).add(vecchioCosto.subtract(nuovoCosto));
                        budgetdaSottrarre.put(proposta.getBudgetTipIntervento(), dc);

                        break;
                    }
                }
            }
        }
        return JsonBuilder.newInstance().withData(budgets).withTransformer(new JsonMapTransformer<BudgetTipIntervento>() {
            int id = 1;

            @Override
            public void transformToMap(BudgetTipIntervento budgetTipIntervento) {
                BudgetTipInterventoPK budgetTipInterventoPK = budgetTipIntervento.getBudgetTipInterventoPK();
                put("anno", Short.toString(budgetTipInterventoPK.getCodAnno()));
                put("anno_erogazione", Short.toString(budgetTipIntervento.getAnnoErogazione()));
                put("capitolo", Integer.toString(budgetTipIntervento.getCodCap()));
                put("impegno", budgetTipInterventoPK.getCodImpe());
                put("imp_disp_proroghe", pimdao.calculateRealBudgetDisp(budgetTipIntervento).add(budgetdaSottrarre.get(budgetTipIntervento)).toString());
                put("imp_disp_netto", pimdao.calculateRealBudgetDisp(budgetTipIntervento).toString());
                put("imp_disp", pimdao.calculateBdgtDisp(budgetTipIntervento).toString());
                put("id", "" + (id++));
            }
        });
    }


    private void marca_interventi_per_non_essere_processati_dallo_schedulatore(PaiInterventoDao paiInterventoDao, PreviewReportBean previewReportBean, boolean escludiVerificaEsistenzaBudget) throws Exception {

        for (String idEvento : previewReportBean.getEventi()) {
            PaiEvento evento = findEvento(idEvento);
            PaiIntervento paiIntervento = evento.getPaiIntervento();

            AnagrafeSoc dsCodAnaBenef = paiIntervento.getDsCodAnaBenef();
            String cognomeNome = dsCodAnaBenef.getCognomeNome();

            List<PaiInterventoMese> proposte = paiIntervento.getPaiInterventoMeseList();
            PaiInterventoBudgetValidator.verificaEsistenzaBudget(proposte, cognomeNome, escludiVerificaEsistenzaBudget);

            DeterminaBusinessLogic.marca_intervento_per_non_essere_processato_dallo_schedulatore(paiIntervento);
            paiInterventoDao.update(paiIntervento);
        }
    }

    private enum Document {

        DETERMINA, ALLEGATO
    }

    private enum StatoIntervento {

        AR, E, C
    }
}
