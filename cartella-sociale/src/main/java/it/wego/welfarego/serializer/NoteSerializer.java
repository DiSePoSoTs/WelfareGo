/**
 *
 */
package it.wego.welfarego.serializer;

import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.wego.welfarego.persistence.entities.NoteCondivise;


import javax.persistence.EntityManager;

import com.google.common.base.Function;
import it.wego.welfarego.persistence.entities.Utenti;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabio Bonaccorso
 */
public class NoteSerializer {

    private final static Logger logger = LoggerFactory.getLogger(NoteSerializer.class);

    private EntityManager em;

    public NoteSerializer(EntityManager em) {
        this.em = em;
    }

    public static Function<NoteCondivise, ? extends Object> getNoteMinifiedSerializer() {
        return new JsonMapTransformer<NoteCondivise>() {

            @Override
            public void transformToMap(NoteCondivise noteCondivise) {
                Utenti utente = noteCondivise.getCodUte();
                Integer codiceUtente = utente.getCodUte();
                String nomeAssociazione = "";
                try {
                    nomeAssociazione = utente.getAssociazione().getNome();
                } catch (Exception e) {
                    // utente  non associato, lo segnalo nella nota o no ?
                    logger.error("utente: " + codiceUtente + " senza associazione");
                }

                put("id", noteCondivise.getId());
                put("titolo", noteCondivise.getTitolo());
                put("nota", noteCondivise.getEsteso());
                put("cognomeNomeOperatore", utente.getCognomeNome());
                put("dataApertura", noteCondivise.getDtInserimento());
                put("associazione", nomeAssociazione);

            }

        };
    }

}
