package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.NoteCondivise;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by max on 13/10/17.
 */
public class NoteCondiviseDaoITest {
    @Test
    public void testGetNoteByCodAna() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        NoteCondiviseDao noteCondiviseDao = new NoteCondiviseDao(entityManager);
        List<NoteCondivise> noteByCodAna = noteCondiviseDao.getNoteByCodAna(24114902);
        assertTrue(noteByCodAna.size() == 1);
    }

}
