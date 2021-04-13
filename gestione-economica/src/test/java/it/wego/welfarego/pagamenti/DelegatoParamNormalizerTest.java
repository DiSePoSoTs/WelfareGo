package it.wego.welfarego.pagamenti;

import it.wego.welfarego.pagamenti.FiltraPerDelegatoJpaPredicateBuilder;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class DelegatoParamNormalizerTest {

    @Test
    public void aa() {
        String a = "  a      b  c    ";
        a = a.trim();
        System.out.println(String.format("[%s]", a.replaceAll("(\\s)\\1", "$1")));
        System.out.println(String.format("[%s]", a.replaceAll("\\s+", " ")));
        System.out.println(String.format("[%s]", a.replaceAll("\\s{2}", " ")));

    }

    @Test
    public void test_rimuovi_punti_dall_input() throws Exception {
        String filtroDelegatoNormalizzato = DelegatoParamNormalizer.normalize("s.r.l.");
        assertEquals(filtroDelegatoNormalizzato, "srl");
    }


    @Test
    public void testGetParoleDelFiltro() throws Exception {
        String filtroDelegatoNormalizzato = DelegatoParamNormalizer.normalize("  a   b  ");
        assertEquals(filtroDelegatoNormalizzato, "a b");
    }

}