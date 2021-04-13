package it.wego.welfarego.services;

import it.wego.welfarego.persistence.dao.AnagrafeFamigliaDao;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;

import javax.persistence.EntityManager;
import java.util.List;

public class AnagrafeFamigliaService {

    public List<VistaAnagrafe> getNucleo_familiare_anagrafe_comunale(EntityManager em, int codAna) {
        AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(em);
        AnagrafeFamigliaDao anagrafeFamigliaDao = new AnagrafeFamigliaDao(em);

        AnagrafeSoc soggetto = anagrafeSocDao.findByCodAna(codAna);
        List<VistaAnagrafe> famigliareAnagrafeComunale = anagrafeFamigliaDao.find_Familiari_Da_AnagrafeComunale(soggetto);

        return famigliareAnagrafeComunale;
    }

}
