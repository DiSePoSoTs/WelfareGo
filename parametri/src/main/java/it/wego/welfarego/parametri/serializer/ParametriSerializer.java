package it.wego.welfarego.parametri.serializer;

import com.google.common.base.Function;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import it.wego.conversions.StringConversion;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.dao.AssociazioneDao;
import it.wego.welfarego.persistence.dao.ListaAttesaDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.dao.TipologiaParametriDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.Associazione;
import it.wego.welfarego.persistence.entities.ListaAttesa;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Template;
import it.wego.welfarego.persistence.entities.TipologiaParametri;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import org.apache.tools.ant.taskdefs.optional.extension.Compatibility;

/**
 *
 * @author giuseppe
 */
public class ParametriSerializer extends PersistenceAdapter {

    public ParametriSerializer() {
        super(Connection.getEntityManager());
    }

    private static Gson gson = new Gson();
    
    private static final Function<ParametriIndata, Map> PARAMETRI_INDATA_TO_GSON_FUNCTION = new JsonMapTransformer<ParametriIndata>() {
        @Override
        public void transformToMap(ParametriIndata p) {
            putRecord(StringConversion.escapeHtmlString(p.getDesParam()), p.getIdParamIndata().toString());
            put("timestamp", StringConversion.timestampToString(p.getDtIniVal()));
        }
    };
    
    private static final Function<Utenti, Map> UTENTI_TO_GSON_FUNCTION = new JsonMapTransformer<Utenti>() {
        @Override
        public void transformToMap(Utenti u) {
            putRecord(u.getCognome() + " " + u.getNome(), u.getCodUte().toString());
        }
    };
	
	private static final Function<Associazione, Map> ASSOCIAZIONI_TO_GSON_FUNCTION = new JsonMapTransformer<Associazione>()
	{
		@Override
		public void transformToMap(Associazione ass)
		{
			putRecord(ass.getNome(), ass.getId());
		}
	};

    public String serializeParametriJavascriptArray(String tipParam) {
        return gson.toJson(Lists.transform(new ParametriIndataDao(getEntityManager()).findByTipParam(tipParam), PARAMETRI_INDATA_TO_GSON_FUNCTION));


    }

    public String serializeUtentiJavascriptArray(String tipoUtente) {
        return gson.toJson(Lists.transform(new UtentiDao(getEntityManager()).findByTipologia(tipoUtente), UTENTI_TO_GSON_FUNCTION));
    }
    
    private static final Function<Template, Map> templatesToGsonFunction = new JsonMapTransformer<Template>() {
        @Override
        public void transformToMap(Template u) {
            putRecord(u.getDesTmpl(), u.getCodTmpl().toString());
        }
    };

    public String serializeTemplatesCombo() {
        return gson.toJson(Lists.newArrayList(Iterables.transform(new TemplateDao(getEntityManager()).findAll(), templatesToGsonFunction)));
    }

    public String serializeTemplatesMayBeNullCombo() {
        return gson.toJson(Lists.newArrayList(Iterables.concat(Iterables.transform(new TemplateDao(getEntityManager()).findAll(), templatesToGsonFunction),
                Collections.singleton(JsonMapTransformer.createRecord("[ nessun template ]", "null")))));
    }
    
    private static final Function<ListaAttesa, Map> listeAttesaToGsonFunction = new JsonMapTransformer<ListaAttesa>() {
    
    	@Override
        public void transformToMap(ListaAttesa u) {
            putRecord(u.getDesListaAtt(), u.getCodListaAtt().toString());
        }
    };

    public String serializeListeAttesaCombo() {
        return gson.toJson(Lists.transform(new ListaAttesaDao(getEntityManager()).findAll(), listeAttesaToGsonFunction));
    }

    private static final Function<TipologiaParametri, Map> tipologieParametroToGsonFunction = new JsonMapTransformer<TipologiaParametri>() {
        @Override
        public void transformToMap(TipologiaParametri u) {
            putRecord(u.getTipParam() + " - " + u.getDesTipParam(), u.getTipParam());
        }
    };

    private static final Comparator<TipologiaParametri> tipologieParametroSorter = new Comparator<TipologiaParametri>() {
    	public int compare(TipologiaParametri o1, TipologiaParametri o2) {
            return ComparisonChain.start().compare(o1.getTipParam(), o2.getTipParam()).compare(o1.getDesTipParam(), o2.getDesTipParam()).result();
        }
    };

    public String serializeTipologieParametroCombo() {
        return gson.toJson(JsonBuilder.newInstance().withSourceData(new TipologiaParametriDao(getEntityManager()).findAll()).withTransformer(tipologieParametroToGsonFunction).withSorter(tipologieParametroSorter, true).buildStoreResponse().getData());
    }
	
	public String serializeAssociazioni() {
		return gson.toJson(Lists.transform(new AssociazioneDao(this.getEntityManager()).findAll(), ASSOCIAZIONI_TO_GSON_FUNCTION));
	}
}
