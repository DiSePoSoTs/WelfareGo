package it.wego.welfarego.pagamenti;

import com.google.gson.Gson;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * La classe offre alle sotto classi alcunee funzionalità utilizzate da vari
 * servizi.
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
public abstract class AbstractAjaxController {

    @PersistenceContext()
    private EntityManager entityManager;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Logger getLogger() {
        return logger;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    private final Gson gson = new Gson();
    
    /**
     * Formato della data in formato italiano.
     */
    protected static final SimpleDateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM/yyyy");
    
    /**
     * Formato della data in formato spedito da EXT-JA.
     */
    @Deprecated
    protected static final SimpleDateFormat FORMATO_DATA_EXT_JS = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss");
    /**
     * Oggetto rappresentante la formattazione dell'importo
     */
    protected final static DecimalFormat FORMATO_IMPORTO = new DecimalFormat("#,##0.00");

    @Deprecated
    public void sendResponse(ResourceResponse response, Object jsonObject) throws IOException {
        PrintWriter writer = response.getWriter();
        gson.toJson(jsonObject, writer);
        writer.close();
    }

    /**
     * Scrive in formato JSON la risposta al browsre. La risposta contiebne un
     * oggetto JSON con un attriburo
     * <code>success</code> con il valote <b>true</b> e un attributo
     * <code>data</code> contenente un array con i dati.
     *
     * @param response Risposta al browser
     * @param dati I dati che dovranno essere spediti al browser.
     * @param entityManager Manager degli oggetti presenti nella base di dati.
     * Dopo che la risposta š stata mandata al browser, verrà cancellata la
     * cache del manager.
     * @throws IOException In caso di problemi durante la scrittura del mesaggio
     * di risposta.
     * @throws JSONException In caso di problemi durante la formazione della
     * risposta in formato JSON (non previsto)
     */
    @Deprecated
    protected void mandaRisposta(ResourceResponse response, JSONArray dati, @Nullable EntityManager entityManager) throws JSONException, IOException {
        JSONObject risposta = new JSONObject();
        risposta.put("success", true);
        risposta.put("data", dati);
        response.setCharacterEncoding("UTF-8");
        risposta.write(response.getWriter());
        response.getWriter().close();
    }

    /**
     * Compone l'indirizzo di residenza della persona leggendo i dati dalla sua
     * anagrafica.
     *
     * @param anagrafica Oggetto con i dati anagrafici della persona
     * @return Stringa con l'indirizzo della persona
     */
    protected String componiIndirizzo(AnagrafeSoc anagrafe) {
        StringBuilder sb = new StringBuilder();
        sb.append(anagrafe.getLuogoResidenza().getVia() != null ? anagrafe.getLuogoResidenza().getVia().getDesVia() : anagrafe.getLuogoResidenza().getViaStr());
        sb.append(',');
        sb.append(' ');
        sb.append(anagrafe.getLuogoResidenza().getCivico() != null ? anagrafe.getLuogoResidenza().getCivico().getDesCiv() : anagrafe.getLuogoResidenza().getCivicoStr());
        sb.append(' ');
        sb.append('-');
        sb.append(' ');
        sb.append(anagrafe.getLuogoResidenza().getComune() != null ? anagrafe.getLuogoResidenza().getComune().getDesCom() : anagrafe.getLuogoNascita().getComuneStr());
        return (sb.toString());
    }

    /**
     * Cerca nella base di dati il {@link ParametriIndata} appropriato per lo
     * stato corretto.
     *
     * @param em Per l'accesso alla base didati
     * @param stato stringa di 2 caratteri rappresentante lo stato della fattura
     * @return L'oggetto rappresentante lo stato della fattura.
     */
    protected ParametriIndata cercaStatoFattura(EntityManager em, String stato) {
        return (this.cercaStato(em, stato, "fs"));
    }

    /**
     * Cerca nella base di dati il {@link ParametriIndata} appropriato per lo
     * stato corretto.
     *
     * @param em Per l'accesso alla base didati
     * @param stato stringa di 2 caratteri rappresentante lo stato del pagamento
     * @return L'oggetto rappresentante lo stato del pagamento.
     */
    protected ParametriIndata cercaStatoPagamento(EntityManager em, String stato) {
        //TODO da modificare la stringa dell'entità
        return (this.cercaStato(em, stato, "fs"));
    }

    /**
     * Cerca nella base di dati il {@link ParametriIndata} appropriato per lo
     * stato corretto.
     *
     * @param em Per l'accesso alla base didati
     * @param stato stringa di 2 caratteri rappresentante lo stato
     * @param entita Stringa prappresentante l'entità per il quale si cerca lo
     * stato
     * @return L'oggetto rappresentante lo stato del pagamento.
     */
    private ParametriIndata cercaStato(EntityManager em, String stato, String entita) {
        Query q = em.createQuery("SELECT p FROM ParametriIndata p "
                + "WHERE p.idParam.tipParam.tipParam = '" + entita + "' "
                + "AND p.idParam.codParam = '" + stato + "'"
                + "AND p.dtIniVal = ( "
                + "SELECT MAX(p2.dtIniVal) FROM ParametriIndata p2 "
                + "WHERE p.idParam.idParam = p2.idParam.idParam "
                + "AND p2.dtIniVal <= :oggi"
                + ")");
        q.setParameter("oggi", new Date());
        return ((ParametriIndata) q.getSingleResult());
    }

    @ExceptionHandler
    public void handleException(Exception exception, ResourceRequest request, ResourceResponse response) throws IOException {
        getLogger().error("error", exception);
        JsonBuilder.newInstance().withWriter(response.getWriter()).withError(exception).buildResponse();
    }
}

