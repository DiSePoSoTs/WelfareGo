package it.wego.welfarego.services;

import it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient;
import it.wego.welfarego.persistence.dao.PaiInterventoMeseDao;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.xsd.pratica.Pratica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class PaiService {
    private final static Logger logger = LoggerFactory.getLogger(PaiService.class);

    private EntityManager em;

    public PaiService() {
    }

    public PaiService(EntityManager em) {
        this.em = em;
    }

    public void updatePai(Pai pai, Utenti connectedUser) {
        logger.debug("update " + pai.toString() + ", utente: " + connectedUser.toString());

        PaiInterventoMeseDao pdao = new PaiInterventoMeseDao(em);

		try {
			em.getTransaction().begin();
			em.merge(pai);
			if (pai.getDtCambioFascia() != null) {
                pdao.aggiornaFasciaReddito(pai.getCodPai(), pai.getIdParamFascia(), pai.getDtCambioFascia());
			}
			CartellaSocialeWsClient.newInstance().withEntityManager(em).loadConfigFromDatabase().withPai(pai).sincronizzaCartellaSociale();
			PaiEvento evento = Pratica.serializePaiEvento(pai, null, pai.getCartellaSociale(), PaiEvento.PAI_UPDATE, connectedUser);
			em.persist(evento);
            em.getTransaction().commit();
		} catch (Exception e) {

			em.getTransaction().rollback();
			throw new RuntimeException("", e);
		} finally {

			em.close();
		}

    }
}

