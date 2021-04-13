/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.utils;

import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.persistence.dao.ComuneDao;
import it.wego.welfarego.persistence.dao.ProvinciaDao;
import it.wego.welfarego.persistence.dao.StatoDao;
import it.wego.welfarego.persistence.dao.ToponomasticaCiviciDao;
import it.wego.welfarego.persistence.dao.ToponomasticaDao;
import it.wego.welfarego.persistence.entities.Comune;
import it.wego.welfarego.persistence.entities.Provincia;
import it.wego.welfarego.persistence.entities.Stato;
import it.wego.welfarego.persistence.entities.Toponomastica;
import it.wego.welfarego.persistence.entities.ToponomasticaCivici;

/**
 *
 * @author aleph
 */
public class ComboStoreUtils {

    private final PersistenceAdapter persistenceAdapter;

    public ComboStoreUtils() {
        persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
    }

    public String getStatoComboDataStr() {
        return JsonBuilder.getGson().toJson(JsonBuilder.newInstance().withData(new StatoDao(persistenceAdapter.getEntityManager()).findAll()).withTransformer(new JsonMapTransformer<Stato>() {
            @Override
            public void transformToMap(Stato stato) {
                put("text", stato.getDesStato());
                put("value", stato.getCodStato());
                put("cod_stato", stato.getCodStato());
            }
        }).buildStoreResponse().getData());
    }

    public String getProvinciaComboDataStr() {
        return JsonBuilder.getGson().toJson(JsonBuilder.newInstance().withData(new ProvinciaDao(persistenceAdapter.getEntityManager()).findAll()).withTransformer(new JsonMapTransformer<Provincia>() {
            @Override
            public void transformToMap(Provincia provincia) {
                put("text", provincia.getDesProv());
                put("value", provincia.getCodProv());
                put("cod_prov", provincia.getCodProv());
                put("cod_stato", provincia.getStato().getCodStato());
            }
        }).buildStoreResponse().getData());
    }

    public String getComuneComboDataStr() {
        return JsonBuilder.getGson().toJson(JsonBuilder.newInstance().withData(new ComuneDao(persistenceAdapter.getEntityManager()).findAll()).withTransformer(new JsonMapTransformer<Comune>() {
            @Override
            public void transformToMap(Comune comune) {
                put("text", comune.getDesCom());
                put("value", comune.getCodCom());
                put("cod_com", comune.getCodCom());
                put("cod_prov", comune.getComunePK().getCodProv());
                put("cod_stato", comune.getComunePK().getCodStato());
            }
        }).buildStoreResponse().getData());
    }

    public String getViaComboDataStr() {
        return JsonBuilder.getGson().toJson(JsonBuilder.newInstance().withData(new ToponomasticaDao(persistenceAdapter.getEntityManager()).findAll()).withTransformer(new JsonMapTransformer<Toponomastica>() {
            @Override
            public void transformToMap(Toponomastica toponomastica) {
                put("text", toponomastica.getDesVia());
                put("value", toponomastica.getCodVia());
                put("cod_via", toponomastica.getCodVia());
                put("cod_com", toponomastica.getToponomasticaPK().getCodCom());
                put("cod_prov", toponomastica.getToponomasticaPK().getCodProv());
                put("cod_stato", toponomastica.getToponomasticaPK().getCodStato());
            }
        }).buildStoreResponse().getData());
    }

    public String getCivicoComboDataStr() {
        return JsonBuilder.getGson().toJson(JsonBuilder.newInstance().withData(new ToponomasticaCiviciDao(persistenceAdapter.getEntityManager()).findAll()).withTransformer(new JsonMapTransformer<ToponomasticaCivici>() {
            @Override
            public void transformToMap(ToponomasticaCivici toponomasticaCivici) {
                put("text", toponomasticaCivici.getDesCiv());
                put("value", toponomasticaCivici.getCodCiv());
                put("cod_civico", toponomasticaCivici.getCodCiv());
                put("cod_via", toponomasticaCivici.getToponomasticaCiviciPK().getCodVia());
                put("cod_com", toponomasticaCivici.getToponomasticaCiviciPK().getCodCom());
                put("cod_prov", toponomasticaCivici.getToponomasticaCiviciPK().getCodProv());
                put("cod_stato", toponomasticaCivici.getToponomasticaCiviciPK().getCodStato());
            }
        }).buildStoreResponse().getData());
    }

    public void close() {
        persistenceAdapter.close();
    }
}
