package it.wego.welfarego.azione.forms;

import it.wego.extjs.json.JsonBuilder;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;

/**
 *
 * @author aleph
 */
public class RegistraDeterminaMultiplaFormListener extends AbstractForm implements AbstractForm.Loadable, AbstractForm.Proceedable {

    public Integer getIdDetermina() throws Exception {
        return Integer.valueOf(getTask().getCampoFlow1());
    }

    public Determine getDetermina() throws Exception {
        return getEntityManager().find(Determine.class, getIdDetermina());
    }

    @Override
    public Object load() throws Exception {
        getLogger().debug("loading data");

        Object res;

        if (getParameter("table") != null) {
            Determine determina = getDetermina();
            List list = new ArrayList();
            for (PaiEvento paiEvento : determina.getPaiEventoList()) {
                PaiIntervento paiIntervento = paiEvento.getPaiIntervento();
                Pai pai = paiIntervento.getPai();
                AnagrafeSoc anagrafeSoc = pai.getAnagrafeSoc();
                Utenti utente = pai.getCodUteAs();
                PaiStoreData data = new PaiStoreData();
                data.setNomeUtente(anagrafeSoc.getNome());
                data.setCognomeUtente(anagrafeSoc.getCognome());
                data.setDataAperturaPai(pai.getDtApePai());
                data.setAssistenteSociale(utente.getCognome() + " " + utente.getNome());
                list.add(data);
            }
            res = list;
        } else {
            RegistraDeterminaMultiplaData data = new RegistraDeterminaMultiplaData();
            data.setDataProtocollo(new Date());
            data.setIdDetermina(getIdDetermina().toString());
            res = data;
        }

        getLogger().debug("data loaded");
        return JsonBuilder.newInstance().withData(res).buildResponse();
    }

    /**
     * se la determina è di apertura (stato intervento aperto) conferma gli
     * impegni di spesa (PaiInterventoMese) e rende esecutivo l'intervento
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object proceed() throws Exception {
        getLogger().debug("saving data and proceeding");

        initTransaction();
        Determine determina = getDetermina();
        RegistraDeterminaMultiplaData dataModel = getDataParameter(RegistraDeterminaMultiplaData.class);
        getLogger().info("determina.getPaiEventoList().size(): " + determina.getPaiEventoList().size());
        char statoInt = determina.getPaiEventoList().get(0).getPaiIntervento().getStatoInt();

        if (statoInt == PaiIntervento.STATO_INTERVENTO_APERTO) {
            PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(getEntityManager());
            for (PaiEvento paiEvento : determina.getPaiEventoList()) {
            	
                PaiIntervento paiIntervento = paiEvento.getPaiIntervento();

//                List<PaiInterventoMese> proposte = paiIntervento.getPaiInterventoMeseList();
//                String cognomeNome = paiIntervento.getDsCodAnaBenef().getCognomeNome();

                if(paiIntervento.getStatoInt()!='C'){
                paiIntervento.setDtEsec(new Date());
//                paiInterventoMeseDao.updateRelQuantsProps(paiIntervento);
                paiInterventoMeseDao.confirmAllProps(paiIntervento);
                new PaiInterventoDao(getEntityManager()).passaInterventoInStatoEsecutivo(paiIntervento);
                paiIntervento.setNumDetermina(dataModel.getNumeroDetermina());
                paiIntervento.setProtocollo(dataModel.getNumeroProtocollo());
                //controllo che non sia un intervento spezzatinabile... in quel caso non lo inserisco inc sr
                if(paiIntervento.getTipologiaIntervento().getFlgRinnovo()!=TipologiaIntervento.FLG_RINNOVO_AUTOMATICO_DETERMINA){
                CartellaSocialeWsClient.newInstance().withEntityManager(getEntityManager()).loadConfigFromDatabase().withPaiIntervento(paiIntervento).sincronizzaIntervento();
                }
                } 
            }
        }
        insertEvento(getTask(), PaiEvento.PAI_REGISTRAZIONE_DETERMINA_MULTIPLA);
        new TaskDao(getEntityManager()).markCompleted(getTask());

        commitTransaction();
        getLogger().debug("data saved and proceeding");
        return JsonBuilder.buildResponse("operazione completata");
    }

    public static class RegistraDeterminaMultiplaData {

        private Date dataProtocollo;
        private String numeroProtocollo, idDetermina, numeroDetermina;

        public Date getDataProtocollo() {
            return dataProtocollo;
        }

        public void setDataProtocollo(Date dataProtocollo) {
            this.dataProtocollo = dataProtocollo;
        }

        public String getIdDetermina() {
            return idDetermina;
        }

        public void setIdDetermina(String idDetermina) {
            this.idDetermina = idDetermina;
        }

        public String getNumeroProtocollo() {
            return numeroProtocollo;
        }

        public void setNumeroProtocollo(String numeroProtocollo) {
            this.numeroProtocollo = numeroProtocollo;
        }

        public String getNumeroDetermina() {
            return numeroDetermina;
        }

        public void setNumeroDetermina(String numeroDetermina) {
            this.numeroDetermina = numeroDetermina;
        }
    }

    public static class PaiStoreData {

        private Date dataAperturaPai;
        private String nomeUtente, cognomeUtente, assistenteSociale;

        public String getAssistenteSociale() {
            return assistenteSociale;
        }

        public void setAssistenteSociale(String assistenteSociale) {
            this.assistenteSociale = assistenteSociale;
        }

        public String getCognomeUtente() {
            return cognomeUtente;
        }

        public void setCognomeUtente(String cognomeUtente) {
            this.cognomeUtente = cognomeUtente;
        }

        public Date getDataAperturaPai() {
            return dataAperturaPai;
        }

        public void setDataAperturaPai(Date dataAperturaPai) {
            this.dataAperturaPai = dataAperturaPai;
        }

        public String getNomeUtente() {
            return nomeUtente;
        }

        public void setNomeUtente(String nomeUtente) {
            this.nomeUtente = nomeUtente;
        }
    }
}
