package it.wego.welfarego.determine.servlet.logica.applicativa;

import org.testng.annotations.Test;

public class DeterminaBusinessLogicTest {
    @Test
    public void testDetermina_parzialmente() throws Exception {
        DeterminaBusinessLogic determinaBusinessLogic = new DeterminaBusinessLogic(null);
//        determinaBusinessLogic.determina_parzialmente(null, "3", "2018-03-02",false);
//        determinaBusinessLogic.determina_parzialmente(null, "", "018-03-02",false);
//        determinaBusinessLogic.determina_parzialmente(null, "  ", "018-03-02",false);
        determinaBusinessLogic.determina_parzialmente(null, null, "018-03-02", false);
//        determinaBusinessLogic.determina_parzialmente(null, "3", "",false);

    }

}