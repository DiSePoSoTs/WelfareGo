package it.wego.welfarego.serializer;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import it.wego.conversions.StringConversion;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.extjs.json.JsonSortInfo;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Stato;
import it.wego.welfarego.persistence.entities.TipologiaIntervento;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;

/**
 *
 * @author giuseppe
 */
public class ParametriSerializer {

    private final Gson gson = new Gson();


    public String serializeJavascriptArray( String... tipParams) throws Exception {

        final Set<ParametriIndata> parametriIndataHeadSet = Sets.newHashSet();
        List<ParametriIndata> parametriIndataAllList = Lists.newArrayList();

        fill_liste_dei_parametri(false, parametriIndataHeadSet, parametriIndataAllList, tipParams);


        String res = gson.toJson(JsonBuilder.newInstance().withData(parametriIndataAllList).withTransformer(new JsonMapTransformer<ParametriIndata>() {
            @Override
            public void transformToMap(ParametriIndata p) {
                String name = StringConversion.escapeHtmlString(p.getDesParam());
                put("value", p.getIdParamIndata().toString());
                put("code", p.getIdParam().getCodParam());
                put("timestamp", StringConversion.timestampToString(p.getDtIniVal()));
                if (!Strings.isNullOrEmpty(p.getTxt1Param())) {
                    put("param", p.getTxt1Param());
                }
                if (!parametriIndataHeadSet.contains(p)) {
                    put("obsolete", true);
                    name += " (parametro obsoleto)";
                }
                put("name", name);
            }
        }).withSorter(new JsonSortInfo("name")).buildStoreResponse().getData());

        return res;
    }

    public String serializeJavascriptArray(boolean escludi_trieste_per_il_sociale, String... tipParams) throws Exception {

        final Set<ParametriIndata> parametriIndataHeadSet = Sets.newHashSet();
        List<ParametriIndata> parametriIndataAllList = Lists.newArrayList();

        fill_liste_dei_parametri(escludi_trieste_per_il_sociale, parametriIndataHeadSet, parametriIndataAllList, tipParams);


        String res = gson.toJson(JsonBuilder.newInstance().withData(parametriIndataAllList).withTransformer(new JsonMapTransformer<ParametriIndata>() {
            @Override
            public void transformToMap(ParametriIndata p) {
                String name = StringConversion.escapeHtmlString(p.getDesParam());
                put("value", p.getIdParamIndata().toString());
                put("code", p.getIdParam().getCodParam());
                put("timestamp", StringConversion.timestampToString(p.getDtIniVal()));
                if (!Strings.isNullOrEmpty(p.getTxt1Param())) {
                    put("param", p.getTxt1Param());
                }
                if (!parametriIndataHeadSet.contains(p)) {
                    put("obsolete", true);
                    name += " (parametro obsoleto)";
                }
                put("name", name);
            }
        }).withSorter(new JsonSortInfo("name")).buildStoreResponse().getData());

        return res;
    }

    private void fill_liste_dei_parametri(boolean escludi_trieste_per_il_sociale, Set<ParametriIndata> parametriIndataHeadSet, List<ParametriIndata> parametriIndataAllList, String[] tipParams) {
        EntityManager em = Connection.getEntityManager();
        ParametriIndataDao parametriIndataDao = new ParametriIndataDao(em);
        for (String tipParam : tipParams) {
            if(escludi_trieste_per_il_sociale){
                parametriIndataAllList.addAll(parametriIndataDao.findAll_WEGO_ByTipParamAttivo(tipParam));
            }else{
                parametriIndataAllList.addAll(parametriIndataDao.findAllByTipParamAttivo(tipParam));
            }


            parametriIndataHeadSet.addAll(parametriIndataDao.findByTipParamAttivo(tipParam));
        }
        em.close();
    }

    public String serializeCittadinanzaCombo() {
        EntityManager entityManager = Connection.getEntityManager();
        try {
            return gson.toJson(JsonBuilder.newInstance().withData(entityManager.createQuery("SELECT s FROM Stato s", Stato.class).getResultList()).withTransformer(new JsonMapTransformer<Stato>() {
                @Override
                public void transformToMap(Stato stato) {
                    put("value", stato.getCodStato());
                    put("name", stato.getDesStato());
                }
            }).withSorter(new Comparator<Map<String, String>>() {
                public int compare(Map<String, String> record1, Map<String, String> record2) {
                    return record1.get("value").equals("100") ? -1 : (record2.get("value").equals("100") ? 1 : record1.get("name").compareTo(record2.get("name")));
                }
            }).buildStoreResponse().getData());
        } finally {
            entityManager.close();
        }
    }

    /**
     *
     * @param tipoUtente
     * @return
     * @deprecated WTF! . . manual json building . . !
     */
    @Deprecated
    public String serializeUtentiJavascriptArray(String tipoUtente) {
        EntityManager em = Connection.getEntityManager();
        UtentiDao dao = new UtentiDao(em);
        List<Utenti> utenti = dao.findByTipologia(tipoUtente);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < utenti.size(); i++) {
            Utenti u = utenti.get(i);
            sb.append("{value: '").append(u.getCodUte()).append("', name: '").append(u.getCognome()).append(" ").append(u.getNome()).append("', uot: '").append(u.getIdParamUot().getIdParamIndata()).append("'}");
            if (i < utenti.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        if (em.isOpen()) {
            em.close();
        }
        return sb.toString();
    }

    @Deprecated
    public String serializeTipologiaInterventoJavascriptArray() {
        EntityManager em = Connection.getEntityManager();
        TipologiaInterventoDao dao = new TipologiaInterventoDao(em);
        List<TipologiaIntervento> tipi = dao.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < tipi.size(); i++) {
            TipologiaIntervento t = tipi.get(i);
            sb.append("{value: '").append(t.getCodTipint()).append("', name: '").append(StringConversion.escapeHtmlString(t.getDesTipint())).append("', impStdCosto: '").append(t.getImpStdCosto()).append("', classe: '").append(t.getIdParamClasseTipint().getIdParamIndata()).append("'}");
            if (i < tipi.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        if (em.isOpen()) {
            em.close();
        }
        return sb.toString();
    }

    public int fasciaDiRedditoDefault() {
        EntityManager em = Connection.getEntityManager();
        int fasciaDefault = Parametri.getIdFasciaDefault(em);
        if (em.isOpen()) {
            em.close();
        }
        return fasciaDefault;
    }
}
