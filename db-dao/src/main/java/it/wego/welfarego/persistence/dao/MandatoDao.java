package it.wego.welfarego.persistence.dao;

import com.google.common.base.Preconditions;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.math.BigInteger;

public class MandatoDao extends PersistenceAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MandatoDao(EntityManager em) {
        super(em);
    }


    public Mandato loadById(Integer idMandato) {
        Mandato mandato = getEntityManager().find(Mandato.class, idMandato);
        Preconditions.checkArgument(mandato != null, "mandato non trovato per id = %s", idMandato);
        return mandato;
    }


    public void aggiornaNumeroMandato(Mandato mandato, BigInteger numeroMandato) {
        mandato.setNumeroMandato(numeroMandato);
        for (MandatoDettaglio mandatoDettaglio : mandato.getMandatoDettaglioList()) {
            mandatoDettaglio.setNumeroMandato(numeroMandato);
        }
    }
}
