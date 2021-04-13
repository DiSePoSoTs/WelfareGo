package it.wego.welfarego.services;

import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.Utenti;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import static it.wego.persistence.PersistenceAdapter.getSingleResult;

public class PaiServiceTest {
    @Test
    public void testUpdatePai() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        TypedQuery<Pai> query = entityManager.createQuery("SELECT p "
                + " FROM Pai p "
                + " WHERE p.codPai = :codPai"
                + "", Pai.class);
        query.setParameter("codPai", 24045341);
        query.setMaxResults(1);
        Pai pai = getSingleResult(query);
        PaiService paiService = new PaiService(entityManager);
        paiService.updatePai(pai, getFakeUser());

    }

    private Utenti getFakeUser() {
        Utenti utenti = new Utenti();
        return utenti;
    }

}