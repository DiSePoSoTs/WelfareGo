package it.wego.welfarego.pagamenti;

public class DelegatoParamNormalizer {
    public static String normalize(String filterDelegato) {
        filterDelegato = remove_double_spaceses_on_filterDelegato( filterDelegato);
        filterDelegato = remove_dots_on_filtroDelegato(filterDelegato);
        return  filterDelegato;
    }


    private static String  remove_dots_on_filtroDelegato(String filterDelegato) {
        filterDelegato = filterDelegato.replaceAll("\\.", "");
        return filterDelegato;
    }

    private static String  remove_double_spaceses_on_filterDelegato(String filterDelegato) {
        filterDelegato = filterDelegato.trim();
        filterDelegato = filterDelegato.replaceAll("\\s+", " ");
        return filterDelegato;
    }

}
