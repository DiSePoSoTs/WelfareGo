package it.wego.welfarego.determine.servlet.logica.applicativa;

import com.google.common.base.Strings;
import it.wego.dynodtpp.DynamicOdtUtils;
import it.wego.extjs.json.JsonBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.commons.utils.ReportUtils;
import it.wego.welfarego.determine.model.PreviewReportBean;
import it.wego.welfarego.dto.InterventoDto;
import it.wego.welfarego.persistence.constants.Documenti;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.*;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.services.interventi.CalcolaCostoInterventoService;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DeterminaBusinessLogic {

    private PersistenceAdapter persistenceAdapter = null;
    private EntityManager entityManager;


    private Logger logger = LoggerFactory.getLogger(DeterminaBusinessLogic.class);

    public DeterminaBusinessLogic(EntityManager entityManager) {
        this.entityManager = entityManager;
        persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
    }

    public static PaiEvento findEvento(String idEvento) {
        return Connection.getEntityManager().find(PaiEvento.class, Integer.valueOf(idEvento));
    }

    /**
     * @param paiIntervento
     * @link InterventiScheduler.interventi_in_esecutivita qui trovi la query
     */
    public static void marca_intervento_per_non_essere_processato_dallo_schedulatore(PaiIntervento paiIntervento) {
        paiIntervento.setRinnovato(PaiInterventoDao.RINNOVATO);
    }

    public void determina_parzialmente(PreviewReportBean previewReportBean, String numero_mesi, String al, boolean escludiVerificaEsistenzaBudget) throws Exception {

        CalcolaCostoInterventoService calcolaCostoInterventoService = new CalcolaCostoInterventoService();
        Integer numeroMesiProroga = Strings.isNullOrEmpty(numero_mesi) == false && numero_mesi != "0" ? Integer.valueOf(Strings.emptyToNull(numero_mesi)) : null;
        Date determinaFinoAl = null;


        // se numero mesi esiste allora non preoccuparti della data ...
        if (numero_mesi == null || numero_mesi.trim().length() == 0) {
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                determinaFinoAl = format.parse(al);
            } catch (ParseException ex) {
                throw new RuntimeException("la data ricevuta [" + al + "] non è valida.");
            }
        }


        PaiInterventoDao paiInterventoDao = new PaiInterventoDao(entityManager);
        for (String idEvento : previewReportBean.getEventi()) {
            PaiEvento evento = findEvento(idEvento);
            PaiIntervento paiIntervento = evento.getPaiIntervento();
            Date dataFineIntervento = paiIntervento.calculateDtFine();

            if (dataFineIntervento == null) {
                DateTime tmp = new DateTime(dataFineIntervento);
                tmp = tmp.plusMonths(Integer.valueOf(numero_mesi));
                dataFineIntervento = tmp.toDate();
            }

            if (determinaFinoAl == null) {
                DateTime tmp = new DateTime(new Date());
                tmp = tmp.plusMonths(Integer.valueOf(numero_mesi));
                determinaFinoAl = tmp.toDate();
            }

            logger.debug("dataFineIntervento: " + dataFineIntervento + ", determinaFinoAl: " + determinaFinoAl);

            if (dataFineIntervento.before(determinaFinoAl)) {
                String msgTemplate = "per l' intervento %s viene saltata la procedura di suddivisione dell' intervento";
                logger.info(String.format(msgTemplate, paiIntervento.toString()));
                continue;
            }


            List<PaiInterventoMese> proposte = paiIntervento.getPaiInterventoMeseList();


            BigDecimal nuovoCosto = BigDecimal.ZERO;
            BigDecimal nuovaQuantita = BigDecimal.ZERO;
            DateTime dataInizio = new DateTime(paiIntervento.getDtAvvio());
            DateTime nuovaDataFine = null;
            DateTime vecchiaDataFine = new DateTime(dataFineIntervento);
            //faccio i check dovuti poi spezzetto l'intervento
            //caso intervento con data dal al

            if (determinaFinoAl != null) {
                nuovaDataFine = new DateTime(determinaFinoAl);
                nuovaQuantita = paiIntervento.getQuantita();
                nuovoCosto = paiIntervento.getQuantita();
                Validate.isTrue(nuovaDataFine.isAfter(new DateTime(paiIntervento.getDtAvvio())), "Attenzione l'intervento di " + paiIntervento.getPai().getAnagrafeSoc().getCognome() + " " + paiIntervento.getPai().getAnagrafeSoc().getNome() + " non può essere determinato per questa durata di tempo poichè la data di fine impostate è  inferiore alla data di inizio intervento originale");
                //se la nuova data fine è superiore alla data fine dell'intervento allora metto come nuova data fine la data fine dell'intervento stesso .
                if (nuovaDataFine.isAfter(vecchiaDataFine)) {
                    nuovaDataFine = vecchiaDataFine;
                }
                DateTime fineAnno = new DateTime(Calendar.getInstance().get(Calendar.YEAR), 12, 31, 23, 59);

                Validate.isTrue(nuovaDataFine.isBefore(fineAnno), "ATTENZIONE l'intervento di " + paiIntervento.getPai().getAnagrafeSoc().getCognome() + " " + paiIntervento.getPai().getAnagrafeSoc().getNome() + " non può essere determinato per questa durata perchè la data di fine va oltre la fine dell'anno in corso");

                nuovoCosto = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(paiIntervento, determinaFinoAl));

                String misura = paiIntervento.getTipologiaIntervento().getIdParamUniMis().getDesParam();
                if (misura.contains("mens")) {
                    Integer mesiNuovi = org.joda.time.Months.monthsBetween(dataInizio, nuovaDataFine).getMonths();

                    nuovaQuantita = nuovaQuantita.multiply(new BigDecimal(mesiNuovi));
                } else if (misura.contains("sett")) {
                    Integer settimaneNuove = Weeks.weeksBetween(dataInizio, nuovaDataFine).getWeeks();
                    paiIntervento.setDurSettimane(settimaneNuove);
                    nuovaQuantita = nuovaQuantita.multiply(new BigDecimal(settimaneNuove));

                } else if (misura.contains("euro")) {
                    Integer giorniNuovi = Days.daysBetween(dataInizio, nuovaDataFine).getDays() + 1;
                    nuovaQuantita = nuovaQuantita.multiply(new BigDecimal(giorniNuovi));
                }

                paiIntervento.setDtFineProroga(paiIntervento.getDtFine());
                paiIntervento.setDtFine(determinaFinoAl);
            }

            //check intervento con numero mesi proroga
            else if (numeroMesiProroga != null) {
                nuovaDataFine = dataInizio.plusMonths(numeroMesiProroga);
                Validate.isTrue(nuovaDataFine.isAfter(new DateTime(paiIntervento.getDtAvvio())), "Attenzione l'intervento di " + paiIntervento.getPai().getAnagrafeSoc().getCognome() + " " + paiIntervento.getPai().getAnagrafeSoc().getNome() + " non può essere determinato per questa durata di tempo poichè la data di fine impostate è  inferiore alla data di inizio intervento originale");
                //se la nuova data fine è superiore alla data fine dell'intervento allora metto come nuova data fine la data fine dell'intervento stesso .
                if (nuovaDataFine.isAfter(vecchiaDataFine)) {
                    nuovaDataFine = vecchiaDataFine;
                }



                nuovoCosto = calcolaCostoInterventoService.calcolaBdgPrevEur(new InterventoDto(paiIntervento, numeroMesiProroga));
                //    nuovoCosto = pdao.calcolaCostoIntervento(paiIntervento.getTipologiaIntervento().getCodTipint(),paiIntervento.getQuantita(),numeroMesiProroga,paiIntervento.getDtAvvio(),null);


                nuovaQuantita = paiIntervento.getQuantita().multiply(new BigDecimal(numeroMesiProroga));

                //faccio un check se la quantità è settimanale in questo caso moltiplico per 4
                if (paiIntervento.getTipologiaIntervento().getIdParamUniMis().getDesParam().contains("sett")) {
                    nuovaQuantita = nuovaQuantita.multiply(new BigDecimal(4));
                }
                paiIntervento.setDurMesiProroga(paiIntervento.getDurMesi() - numeroMesiProroga);
                paiIntervento.setDurMesi(numeroMesiProroga);
            }


            //controllo che per l'intervento la nuova data fine sia effettivamente diversa dalla vecchia prima di fare l'update
            if (!nuovaDataFine.isEqual(vecchiaDataFine)) {
                PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(entityManager);
                boolean budgetFound = false;
                for (PaiInterventoMese proposta : proposte) {
                    //se trovo un pai intervento mese con proposta maggiore di 0 e che abbia l'anno di spesa uguale
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(paiIntervento.getDtAvvio());
                    if ((proposta.getBdgPrevEur().compareTo(BigDecimal.ZERO)) > 0) {
                        logger.debug("proposta è " + proposta.getBdgPrevEur());
                    }
                    if (proposta.getBudgetTipIntervento().getAnnoErogazione() == cal.get(Calendar.YEAR) &&
                            budgetFound == false &&
                            (proposta.getBdgPrevEur().compareTo(BigDecimal.ZERO)) > 0) {
                        proposta.setBdgPrevEur(nuovoCosto);
                        proposta.setBdgPrevQta(nuovaQuantita);
                        paiIntervento.setCodImpProroga(proposta.getBudgetTipIntervento().getBudgetTipInterventoPK().getCodImpe());
                        budgetFound = true;
                    } else {
                        logger.debug("imposto euro e quantita a 0 per: " + proposta.getPaiInterventoMesePK().toString());
                        proposta.setBdgPrevEur(BigDecimal.ZERO);
                        proposta.setBdgPrevQta(BigDecimal.ZERO);
                    }
                    paiInterventoMeseDao.update(proposta);
                }


                marca_intervento_per_essere_processato_dallo_schedulatore(paiIntervento);
                paiIntervento.setDatiOriginali(PaiInterventoDao.DICITURA_PROROGA + paiIntervento.getDatiOriginali());
                paiInterventoDao.update(paiIntervento);
            }//fine if vecchiadata = nuova data

        }//fine for
    }

    /**
     * @param paiIntervento
     * @link InterventiScheduler.interventi_in_esecutivita qui trovi la query
     */
    private void marca_intervento_per_essere_processato_dallo_schedulatore(PaiIntervento paiIntervento) {
        paiIntervento.setRinnovato(PaiInterventoDao.DA_RINNOVARE);
    }

    /**
     * seleziono template in base al primo evento che arriva dal front end
     * genero e salvo sul db la documentazione
     * crea task di protocollazione
     *
     * @param previewReportBean
     * @throws Exception
     */
    public void procedi_con_transazioni(PreviewReportBean previewReportBean) throws Exception {

        try {
            persistenceAdapter.initTransaction();

            List<String> eventiDaFrontEnd = previewReportBean.getEventi();
            logger.debug("eventiDaFrontEnd: " + eventiDaFrontEnd.toString());
            PaiEvento primoEvento = entityManager.find(PaiEvento.class, Integer.valueOf(eventiDaFrontEnd.get(0)));


            DeterminaTemplateBusinessLogic detarminaTemplateBusinessLogic = new DeterminaTemplateBusinessLogic(primoEvento).invoke();
            Template template = detarminaTemplateBusinessLogic.getTemplate();
            String tipoDetermina = detarminaTemplateBusinessLogic.getTipoDetermina();

            Validate.notNull(template, "il template associato non può essere nullo");


            it.wego.welfarego.persistence.entities.Determine determina = new it.wego.welfarego.persistence.entities.Determine();
            determina.setTsDetermina(new Date());
            entityManager.persist(determina);

            List<PaiEvento> eventi = new ArrayList<PaiEvento>();
            genera_e_salva_documentazione(previewReportBean, primoEvento, template, tipoDetermina, eventi, determina);

            determina.setPaiEventoList(eventi);

            crea_task_di_protocollazione_per_la_determina(primoEvento, tipoDetermina, determina);

            persistenceAdapter.commitTransaction();
        } catch (Exception ex) {
            persistenceAdapter.rollbackTransaction();
            throw new RuntimeException("", ex);

        } finally {
            if (persistenceAdapter != null) {
                persistenceAdapter.close();
            }
        }
    }


    private void crea_task_di_protocollazione_per_la_determina(PaiEvento primoEvento, String tipoDetermina, it.wego.welfarego.persistence.entities.Determine determina) {

        UniqueTasklist newTaskList = new UniqueTasklist();
        newTaskList.setPai(primoEvento.getPaiIntervento().getPai());
        newTaskList.setPaiIntervento(primoEvento.getPaiIntervento());
        newTaskList.setDesTask("Protocollazione " + tipoDetermina);
        newTaskList.setFlgEseguito("N");
        newTaskList.setFlgTasknot("N");
        newTaskList.setTsCreazione(new Date());
        newTaskList.setForm(entityManager.getReference(UniqueForm.class, "P99A010"));
        newTaskList.setRuolo(Utenti.OPERATORE_SEDE_CENTRALE.toString());
        newTaskList.setCampoFlow1(determina.getIdDetermina().toString());
        entityManager.persist(newTaskList);
    }

    private void genera_e_salva_documentazione(PreviewReportBean previewReportBean, PaiEvento primoEvento, Template template, String tipoDetermina, List<PaiEvento> eventi, it.wego.welfarego.persistence.entities.Determine nttDetermina) throws Exception {

        String nomeFilePrefix = it.wego.files.FileUtils.descriptionToFileName(primoEvento.getPaiIntervento().getTipologiaIntervento().getDesTipint());
        String nomeReport = ReportUtils.getNomeFileReportDetermine(primoEvento.getPaiIntervento());
        String allegato = "allegato_" + nomeFilePrefix + ".ods";
        String nomeReportDetermina = "determina_" + nomeFilePrefix + ".odt";
        String msg = String.format("report: %s, file: %s, report determina: %s", nomeReport, allegato, nomeReportDetermina);
        logger.debug(msg);

        Map parametersMap = new HashMap();
        parametersMap.put("tipo_intervento", previewReportBean.getTipIntervento());
        parametersMap.put("lista_eventi", previewReportBean.getEventi());


        msg = String.format("template: %s, parametri: %s", nomeReport, JsonBuilder.getGsonPrettyPrinting().toJson(parametersMap));
        logger.debug(msg);


        byte[] reportResult = ReportUtils.executeDetermineReport(nomeReport, ReportUtils.XLS, parametersMap);

        //GENERO IL DOCUMENTO DINAMICO

        byte[] documentResult = DynamicOdtUtils.newInstance()
                .withTemplateBase64(template.getClobTmpl())
                .withDataXml("<cartella_sociale></cartella_sociale>")
                .withConfig(new ConfigurationDao(entityManager).getConfigWithPrefix(DynamicOdtUtils.CONFIG_PARAM_PREFIX))
                .getResult();
        logger.debug("**** Dimensione documento = {}", documentResult.length);

        String reportResultEncoded = org.apache.commons.codec.binary.Base64.encodeBase64String(reportResult);
        String documentResultEncoded = org.apache.commons.codec.binary.Base64.encodeBase64String(documentResult);
        PaiDocumentoDao paiDocumentoDao = new PaiDocumentoDao(entityManager);

        logger.debug("**** Totale eventi " + previewReportBean.getEventi().size());

        for (String idEvento : previewReportBean.getEventi()) {
            logger.debug("**** Id evento " + idEvento);
            logger.debug("**** Creo i documenti da salvare in banca dati");
            PaiEvento evento = entityManager.find(PaiEvento.class, Integer.valueOf(idEvento));
            evento.setFlgDxStampa(PaiEvento.FLG_STAMPA_NO);
            evento.setIdDetermina(nttDetermina);
            eventi.add(evento);
            paiDocumentoDao.createDoc(evento, Documenti.TIPO_DOC_REPORT_DETERMINA, reportResultEncoded, allegato, tipoDetermina);
            paiDocumentoDao.createDoc(evento, Documenti.TIPO_DOC_TEMPLATE_DETERMINA, documentResultEncoded, nomeReportDetermina, tipoDetermina);
        }
    }

}
