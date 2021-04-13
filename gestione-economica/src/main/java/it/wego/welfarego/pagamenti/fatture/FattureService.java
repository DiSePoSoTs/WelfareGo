package it.wego.welfarego.pagamenti.fatture;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.utils.Connection;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class FattureService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<String> salvaAcquisizione(EntityManager entityManager, String id) throws JSONException {
        FattureDao fattureDao = new FattureDao();
        PaiInterventoMeseDao paiInterventoMeseDao = new PaiInterventoMeseDao(Connection.getEntityManager());

        List<PaiInterventoMese> lista = fattureDao.getPaiInterventoMeseFromId(id);

        List<String> acquisizioniConErrori = new ArrayList<String>();

        for (final PaiInterventoMese mese : lista) {

            PaiIntervento paiIntervento = mese.getPaiIntervento();
            AnagrafeSoc anagrafeSoc = paiIntervento.getPai().getAnagrafeSoc();

            try{
                logger.debug(String.format("persona:%s\t\tmese:%s", anagrafeSoc.getCognomeNome(), mese.getPaiInterventoMesePK()));
                fattureDao.fill_paiInterventoMese_per_SalvaAcquisizione(mese);
                paiInterventoMeseDao.update(mese);

            }catch(Exception ex){

                String acquisizioneConErrore = String.format("cognome-nome (): %s (%s)", anagrafeSoc.getCognomeNome(), anagrafeSoc.getCodFisc());
                acquisizioniConErrori.add(acquisizioneConErrore);

                String error = String.format("acquisizione con errore:%s su interventoMese:%s per:", acquisizioneConErrore, mese.getPaiInterventoMesePK());
                logger.error(error, ex);

            }
        }

        return acquisizioniConErrori;
    }

}
