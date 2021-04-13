package it.wego.welfarego.pagamenti;

import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiltraPerDelegatoJpaPredicateBuilder {
    private String filterDelegato = null;
    private CriteriaBuilder criteriaBuilder = null;
    private Join joinDelegato = null;
    private Predicate predicate = null;
    private String[] paroleDelFiltro = {};
    private Map<String, Object> parametri = null;

    public FiltraPerDelegatoJpaPredicateBuilder(String filterDelegato, CriteriaBuilder criteriaBuilder, Join joinDelegato) {
        this.filterDelegato = filterDelegato;
        this.criteriaBuilder = criteriaBuilder;
        this.joinDelegato = joinDelegato;
        parametri = new HashMap<String, Object>();
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public String[] getParoleDelFiltro() {
        return paroleDelFiltro;
    }


    public FiltraPerDelegatoJpaPredicateBuilder invoke() {

        if(StringUtils.isBlank(filterDelegato)){
            return this;
        }

        filterDelegato = DelegatoParamNormalizer.normalize(filterDelegato);

        // ricerca per persone giuridiche
        paroleDelFiltro = filterDelegato.split(" ");
        int numParoleInFiltro = paroleDelFiltro.length;


        List<ParameterExpression<String>> paramExpressionDelegato = new ArrayList<ParameterExpression<String>>();
        for (int i = 0; i < numParoleInFiltro; i++) {
            String paramDelegato = "paramDelegato";
            parametri.put(paramDelegato + i, "%" + paroleDelFiltro[i].toLowerCase() + "%");
            paramExpressionDelegato.add(criteriaBuilder.parameter(String.class, paramDelegato + i));
        }
        Expression<String> replacedRagSoc = joinDelegato.get("ragSoc");
        replacedRagSoc = criteriaBuilder.function("REPLACE", String.class, replacedRagSoc, criteriaBuilder.literal("."), criteriaBuilder.literal(""));
        Predicate[] restrictionsOnRagioneSociale = new Predicate[numParoleInFiltro];

        for (int i = 0; i < numParoleInFiltro; i++) {
            restrictionsOnRagioneSociale[i]= criteriaBuilder.like(criteriaBuilder.lower(replacedRagSoc), paramExpressionDelegato.get(i));
        }
        Predicate predicatelikeSuRagioneSociale = criteriaBuilder.and(restrictionsOnRagioneSociale);


        // ricerca per persone fisiche

        Predicate restrictionOnPersonaFisica = null;
        Expression expressionCognome = criteriaBuilder.lower(joinDelegato.get("cognome"));
        Expression expressionNome = criteriaBuilder.lower(joinDelegato.get("nome"));
        switch (numParoleInFiltro) {
            case 1:
                Predicate restrictionOnCognome = criteriaBuilder.like(expressionCognome, "%" + filterDelegato.toLowerCase() + "%");
                Predicate restrictionOnNome = criteriaBuilder.like(expressionNome, "%" + filterDelegato.toLowerCase() + "%");
                restrictionOnPersonaFisica = criteriaBuilder.or(restrictionOnCognome, restrictionOnNome);
                break;
            default:
                Predicate predicatelikeSuCognome_1 = criteriaBuilder.like(expressionCognome, "%" + paroleDelFiltro[0].toLowerCase() + "%");
                Predicate predicatelikeSuNome_1 = criteriaBuilder.like(expressionNome, "%" + paroleDelFiltro[1].toLowerCase() + "%");
                Predicate predicateOn_CognomeNome = criteriaBuilder.and(predicatelikeSuCognome_1, predicatelikeSuNome_1);


                Predicate predicatelikeSuCognome_2 = criteriaBuilder.like(expressionCognome, "%" + paroleDelFiltro[1].toLowerCase() + "%");
                Predicate predicatelikeSuNome_2 = criteriaBuilder.like(expressionNome, "%" + paroleDelFiltro[0].toLowerCase() + "%");
                Predicate predicateOn_NomeCognome = criteriaBuilder.and(predicatelikeSuCognome_2, predicatelikeSuNome_2);


                restrictionOnPersonaFisica = criteriaBuilder.or(predicateOn_CognomeNome, predicateOn_NomeCognome);
        }

        List<Predicate> listPredicatesForDelegato = new ArrayList<Predicate>();
        if (predicatelikeSuRagioneSociale != null){
            listPredicatesForDelegato.add(predicatelikeSuRagioneSociale);
        }
        if (restrictionOnPersonaFisica != null){
            listPredicatesForDelegato.add(restrictionOnPersonaFisica);
        }

        Predicate[] restrictions = new Predicate[listPredicatesForDelegato.size()];
        for(int i = 0; i < listPredicatesForDelegato.size(); i++){
            restrictions[i] = listPredicatesForDelegato.get(i);
        }


        Predicate predicateFiltroPerNomeCognomeRagioneSociale = criteriaBuilder.or(restrictions);
        predicate = predicateFiltroPerNomeCognomeRagioneSociale;
        return this;
    }

    public String getFilterDelegato() {
        return filterDelegato;
    }

    public static String getDelegato(PaiIntervento paiIntervento) {
        String delegato = "";

        AnagrafeSoc dsCodAnaBenef = paiIntervento.getDsCodAnaBenef();

        if(dsCodAnaBenef != null){
            if(AnagrafeSoc.PERSONA_FISICA_F.equals(dsCodAnaBenef.getFlgPersFg())) {
                delegato = dsCodAnaBenef.getCognome() + " " + dsCodAnaBenef.getNome();
            }else{
                delegato = dsCodAnaBenef.getRagSoc();
            }
        }
        return  delegato;
    }

    public Map<? extends String,?> getParametri() {
        return parametri;
    }
}