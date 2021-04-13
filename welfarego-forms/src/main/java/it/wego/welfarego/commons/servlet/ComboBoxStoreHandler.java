package it.wego.welfarego.commons.servlet;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.dao.*;
import it.wego.welfarego.persistence.entities.*;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author aleph
 */
public class ComboBoxStoreHandler extends AbstractForm implements AbstractForm.Loadable {

    public static final String PARAM_KEY_LISTA_ATTESA = "lista_attesa", PARAM_KEY_TABLE = "table";

    private EntityManager entityManager = null;

    public ComboBoxStoreHandler() {
    }

    public ComboBoxStoreHandler(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static String getStoreAsJsonArray(Store store) throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(PARAM_KEY_TABLE, store.toString());
        return AbstractForm.getJsonResponse(Action.LOAD, parameters, ComboBoxStoreHandler.class);
    }

    public static List<ComboBoxDataModel> convertParameterListByCodParam(List<ParametriIndata> list) {
        List<ComboBoxDataModel> res = new ArrayList<ComboBoxDataModel>(list.size());
        for (ParametriIndata par : list) {
            res.add(new ComboBoxDataModel(par.getDesParam(), par.getIdParam().getCodParam()));
        }
        return res;
    }

    public static List<ComboBoxDataModel> convertUserList(List<Utenti> list) {
        List<ComboBoxDataModel> res = new ArrayList<ComboBoxDataModel>(list.size());
        for (Utenti par : list) {
            res.add(new ComboBoxDataModel(par));
        }
        return res;
    }

    public static List<ComboBoxDataModel> sortByName(Iterable<ComboBoxDataModel> src) {
        List<ComboBoxDataModel> newList = Lists.newArrayList(src);
        Collections.sort(newList, new Comparator<ComboBoxDataModel>() {
            public int compare(ComboBoxDataModel o1, ComboBoxDataModel o2) {
                return ComparisonChain.start().compare(o1.getName(), o2.getName()).result();
            }
        });
        return newList;
    }

    @Override
    public Object load() {
        String table = getParameter(PARAM_KEY_TABLE).toUpperCase();
        getLogger().debug("processing request : " + table);

        switch (Store.valueOf(table)) {
            case CLASSE_TIPOLOGIA_INTERVENTO:
                return loadClasseTipologiaIntervento();
            case TIPOLOGIA_INTERVENTO:
                return sortByName(loadTipologiaIntervento());
            case LISTA_ATTESA:
                return sortByName(loadListaAttesa());
            case ASSISTENTE:
                return sortByName(loadUsers(Utenti.ASSISTENTE_SOCIALE_UOT.toString()));
            case UOT:
                return loadParameters(Parametri.CODICE_UOT);
            case CAUSE_RESPINGIMENTO:
                return sortByName(loadReasonsChiusura());
            case COND_FAMILIARE:
                return sortByName(loadCondFamiliare());
            case PERSONE_GIURIDICHE:
                return loadPersoneGiuridiche();
            case PO:
                return loadPo();
            case ATTIVITA:
                return loadAttivita();
            case INTERVENTI:
                return loadInterventi();

        }
        throw new UnsupportedOperationException("unknown table : " + table);
    }

    public List<ComboBoxDataModel> loadParameters(String paramType) {
        return convertParameterListByCodParam(new ParametriIndataDao(getEntityManager()).findByTipParam(paramType));
    }

    public List<ComboBoxDataModel> loadUsers(String userType) {
        return convertUserList(new UtentiDao(getEntityManager()).findByTipologia(userType));
    }

    public List<ComboBoxDataModel> loadCondFamiliare() {
        return Lists.newArrayList(Iterables.concat(sortByName(Iterables.transform(Iterables.filter(new ParametriIndataDao(getEntityManager()).findByTipParam(Parametri.TIPOLOGIA_NUCLEO_FAMIGLIARE), new Predicate<ParametriIndata>() {

            public boolean apply(@Nullable ParametriIndata input) {
                // TODO Auto-generated method stub
                return true;
            }

        }), new Function<ParametriIndata, ComboBoxDataModel>() {
            public ComboBoxDataModel apply(ParametriIndata input) {
                return new ComboBoxDataModel(input.getDesParam(), input.getIdParamIndata().toString());
            }
        }))));
    }

    public List<ComboBoxDataModel> loadReasonsChiusura() {
        return Lists.newArrayList(Iterables.concat((Arrays.asList(new ComboBoxDataModel("Scegli una motivazione in caso di respingimento dell'intervento", "")
        )), sortByName(Iterables.transform(Iterables.filter(new ParametriIndataDao(getEntityManager()).findByTipParam(Parametri.CAUSE_RESPINGIMENTO), new Predicate<ParametriIndata>() {

            public boolean apply(@Nullable ParametriIndata input) {
                // TODO Auto-generated method stub
                return true;
            }

        }), new Function<ParametriIndata, ComboBoxDataModel>() {
            public ComboBoxDataModel apply(ParametriIndata input) {
                return new ComboBoxDataModel(input.getDesParam(), input.getDesParam());
            }
        }))));
    }

    public EntityManager getEntityManager() {
        if (entityManager != null) {
            return entityManager;
        } else {
            return super.getEntityManager();
        }
    }

    List<ComboBoxDataModel> loadPo() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT ");
        sb.append("	ID_PARAM_INDATA, des_param ");
        sb.append(" FROM ");
        sb.append("	PARAMETRI_INDATA ");
        sb.append(" WHERE ");
        sb.append("	ID_PARAM_INDATA IN ( ");
        sb.append("		SELECT");
        sb.append("			DISTINCT(id_param_po) ");
        sb.append("		FROM ");
        sb.append("			utenti ");
        sb.append("		WHERE ");
        sb.append("			id_param_po IS NOT NULL ");
        sb.append("	)");

        List<ComboBoxDataModel> list = new ArrayList<ComboBoxDataModel>();


        Query query = getEntityManager().createNativeQuery(sb.toString());
        List<Object[]> resultList = query.getResultList();

        for (Object[] item : resultList) {
            list.add(new ComboBoxDataModel(item[1] + "", item[0] + ""));
        }

        return list;
    }

    List<ComboBoxDataModel> loadAttivita() {

        List<ComboBoxDataModel> list = new ArrayList<ComboBoxDataModel>();
        for (int i = 0; i < TaskDao.mapAttivita.size(); i++) {
            list.add(new ComboBoxDataModel(TaskDao.mapAttivita.get(i), i + ""));
        }
        return list;
    }

    List<ComboBoxDataModel> loadInterventi() {
        List<ComboBoxDataModel> list = new ArrayList<ComboBoxDataModel>();

        Query query = getEntityManager().createNativeQuery("SELECT DISTINCT COD_TIPINT, DES_TIPINT FROM TIPOLOGIA_INTERVENTO ORDER BY DES_TIPINT");
        List<Object[]> resultList = query.getResultList();
        for (Object[] item : resultList) {
            list.add(new ComboBoxDataModel(item[1] + "", item[0] + ""));
        }
        return list;
    }

    public List<ComboBoxDataModel> loadClasseTipologiaIntervento() {
        ParametriIndataDao parametriIndataDao = new ParametriIndataDao(getEntityManager());

        //max_todo: caricare solo le classi di wego e non quelle di trieste per il sociale
        List<ParametriIndata> tipParam = parametriIndataDao.findByTipParam(Parametri.CLASSE_INTERVENTO);

        return Lists.newArrayList(Iterables.concat(Arrays.asList(new ComboBoxDataModel("qualsiasi classe", "0")),
                sortByName(Iterables.transform(Iterables.filter(
                        tipParam,
                        new Predicate<ParametriIndata>() {
                            public boolean apply(ParametriIndata parametriIndata) {
                                TipologiaInterventoDao tipologiaInterventoDao = new TipologiaInterventoDao(getEntityManager());
                                return !tipologiaInterventoDao.findByClasse(parametriIndata).isEmpty();
                            }
                        }), new Function<ParametriIndata, ComboBoxDataModel>() {
                    public ComboBoxDataModel apply(ParametriIndata input) {
                        return new ComboBoxDataModel(input.getDesParam(), input.getIdParamIndata().toString());
                    }
                }))));
    }

    public List<ComboBoxDataModel> loadTipologiaIntervento() {

        String codListaAttesa = getParameter(PARAM_KEY_LISTA_ATTESA);
        List<TipologiaIntervento> tipologiaInterventoList;
        if (codListaAttesa != null) {
            ListaAttesa listaAttesa = getEntityManager().find(ListaAttesa.class, Integer.valueOf(codListaAttesa));
            tipologiaInterventoList = listaAttesa.getTipologiaInterventoList();
        } else {
            tipologiaInterventoList = new TipologiaInterventoDao(getEntityManager()).findAll();
        }
        List<ComboBoxDataModel> list = Lists.newArrayList(Iterables.transform(tipologiaInterventoList, new Function<TipologiaIntervento, ComboBoxDataModel>() {
            public ComboBoxDataModel apply(TipologiaIntervento tipologiaIntervento) {
                return new TipintComboBoxDataModel(
                        tipologiaIntervento.getDesTipint(),
                        tipologiaIntervento.getCodTipint(),
                        tipologiaIntervento.getIdParamClasseTipint().getIdParamIndata().toString(),
                        tipologiaIntervento.getImpStdCosto(),
                        tipologiaIntervento.getFlgRinnovo(),
                        tipologiaIntervento.getFlgFineDurata());

            }
        }));
        Collections.sort(list);
        return list;
    }

    public List<ComboBoxDataModel> loadListaAttesa() {
        List<ComboBoxDataModel> list = new ArrayList<ComboBoxDataModel>();
        for (ListaAttesa listaAttesa : new ListaAttesaDao(getEntityManager()).findAll()) {
            list.add(new ComboBoxDataModel(
                    listaAttesa.getDesListaAtt(),
                    listaAttesa.getCodListaAtt().toString()));
        }
        return list;
    }

    public List<ComboBoxDataModel> loadPersoneGiuridiche() {
        List<ComboBoxDataModel> list = new ArrayList<ComboBoxDataModel>();
        for (AnagrafeSoc giuridiche : new AnagrafeSocDao(getEntityManager()).findPersoneGiuridiche()) {
            list.add(new ComboBoxDataModel(
                    giuridiche.getRagSoc(),
                    giuridiche.getCodAna().toString()));
        }
        return list;
    }

    public enum Store {

        CLASSE_TIPOLOGIA_INTERVENTO, TIPOLOGIA_INTERVENTO, LISTA_ATTESA, ASSISTENTE, UOT, CAUSE_RESPINGIMENTO, COND_FAMILIARE, PERSONE_GIURIDICHE, PO, ATTIVITA, INTERVENTI
    }

    public static class ComboBoxDataModel implements Comparable {

        private String name, value;

        public ComboBoxDataModel(Utenti parametro) {
            this(parametro.getCognome() + " " + parametro.getNome(), parametro.getCodUte().toString());
        }

        public ComboBoxDataModel() {
        }

        public ComboBoxDataModel(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int compareTo(Object o) {
            ComboBoxDataModel comp = (ComboBoxDataModel) o;
            return this.getName().compareTo(comp.getName());
        }
    }

    public static class TipintComboBoxDataModel extends ComboBoxDataModel {

        private String classTipint;
        private BigDecimal impStdCosto;
        private String rinnovo;
        private char misura;

        public TipintComboBoxDataModel() {
        }

        public TipintComboBoxDataModel(String name, String value, String classTipint, BigDecimal impStdCosto, String rinnovo, char misura) {
            super(name, value);
            this.classTipint = classTipint;
            this.impStdCosto = impStdCosto;
            this.setRinnovo(rinnovo);
            this.misura = misura;

        }

        public String getClassTipint() {
            return classTipint;
        }

        public void setClassTipint(String classTipint) {
            this.classTipint = classTipint;
        }

        public BigDecimal getImpStdCosto() {
            return impStdCosto;
        }

        public void setImpStdCosto(BigDecimal impStdCosto) {
            this.impStdCosto = impStdCosto;
        }

        public String getRinnovo() {
            return rinnovo;
        }

        public void setRinnovo(String rinnovo) {
            this.rinnovo = rinnovo;
        }

        public char getMisura() {
            return misura;
        }
        public void setMisura(char misura) {
            this.misura = misura;
        }
    }
}
