package it.wego.welfarego.scheduler;

import com.google.gson.Gson;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.scheduler.rinnovi.helper.SchedulerHelper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;


public class SchedulerHelperTest {


    @Test
    public  void dumpDefectivePaiInt() throws IOException {
        Gson gson = new Gson();
        List<PaiIntervento> interventos = new ArrayList<PaiIntervento>();
        PaiIntervento fakeIntervento_1 = FakeInterventoFactory.getFakeIntervento(1);
        PaiIntervento fakeIntervento_2 = FakeInterventoFactory.getFakeIntervento(2);
        interventos.add(fakeIntervento_1);
        interventos.add(fakeIntervento_2);
        String s = SchedulerHelper.dumpDefectivePaiInt(interventos);
        assertTrue(s.contains(gson.toJson(fakeIntervento_1.getPaiInterventoPK())));
        assertTrue(s.contains(gson.toJson(fakeIntervento_2.getPaiInterventoPK())));
    }

}