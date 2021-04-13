package it.wego.welfarego.persistence.dao;

import it.wego.persistence.objects.Condition;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

import static it.wego.persistence.ConditionBuilder.isEqual;

public class TaskDaoTest {

    @Test
    public void testFindTask() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        TaskDao taskDao = new TaskDao(entityManager);
        List<Condition> conditions = new ArrayList<Condition>();
        String uotFilter = "100";
        String interventiFilter = "DI019";
        String attivitaFilter = TaskDao.mapAttivita.get(12);
        Integer poFilter = 23573614;

        conditions.add(isEqual("tipInt.codTipint", interventiFilter));
        conditions.add(isEqual("t.desTask", attivitaFilter));
        conditions.add(isEqual("asp.idParamPo.idParamIndata", poFilter));


        List<UniqueTasklist> taskList = taskDao.findAllActiveTask(conditions, null, 10, 0);
        for (UniqueTasklist item : taskList) {
            System.out.println(item.getPaiIntervento().getTipologiaIntervento().getDesTipint().equals("Intervento a favore di disabili TRASPORTI"));
            System.out.println(item.getDesTask().equals("Verifica dati esecutività"));
        }

    }
}