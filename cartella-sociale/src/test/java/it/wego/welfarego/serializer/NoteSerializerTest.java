package it.wego.welfarego.serializer;

import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonStoreResponse;
import it.wego.welfarego.persistence.dao.NoteCondiviseDao;
import it.wego.welfarego.persistence.entities.NoteCondivise;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * Created by max on 13/10/17.
 */
public class NoteSerializerTest {
    @Test
    public void testGetNoteMinifiedSerializer() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

        List<NoteCondivise> n = new NoteCondiviseDao(entityManager).getNoteByCodAna(Integer.valueOf(24114908));
        JsonStoreResponse jsonStoreResponse = JsonBuilder.newInstance()
                .withParameters(getParameters())
                .withData(n)
                .withTransformer(NoteSerializer.getNoteMinifiedSerializer()).buildStoreResponse();

        assertTrue(jsonStoreResponse.getMessage().length() > 0);

    }

    private Map getParameters() {
        Map map = new HashMap();
        map.put("k1", "v1");
        return map;
    }

}