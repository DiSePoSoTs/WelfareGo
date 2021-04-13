package it.wego.welfarego;

import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.exception.BudgetAssentiException;
import it.wego.welfarego.persistence.validators.PaiInterventoBudgetValidator;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class PaiInterventoBudgetValidatorTest {

    @Test
    public void a() throws BudgetAssentiException {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        PaiInterventoDao paiIntervenoDao = new PaiInterventoDao(entityManager);
        //senza budget
        PaiIntervento paiIntervento = paiIntervenoDao.findByKey(24529963, "MI005", 13);


        // con budget
//        PaiIntervento paiIntervento = paiIntervenoDao.findByKey(23506569 , "MI005", 1);
        List<PaiInterventoMese> proposte = paiIntervento.getPaiInterventoMeseList();
        PaiInterventoBudgetValidator.verificaEsistenzaBudget(proposte, "dfasf", false);
    }
}
