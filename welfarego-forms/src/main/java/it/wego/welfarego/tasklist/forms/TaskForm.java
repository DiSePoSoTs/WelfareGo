package it.wego.welfarego.tasklist.forms;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.trieste.comune.ssc.beans.Order;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonMapTransformer;
import it.trieste.comune.ssc.json.JsonStoreResponse;
import it.wego.persistence.objects.Condition;
import it.wego.welfarego.abstracts.AbstractStoreListener;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.*;

import static it.wego.persistence.ConditionBuilder.*;

/**
 *
 * @author Gabri
 */
public class TaskForm extends AbstractStoreListener {

    private final static Map<String, String> conditionsMap;
    private static final String AUTO_SEARCH = "AUTO";
    private static final Function<UniqueTasklist, Map> taskTransformerFunction = new JsonMapTransformer<UniqueTasklist>() {
        @Override
        public void transformToMap(UniqueTasklist task) {
            Pai paiTask = task.getCodPai();
            PaiIntervento paiIntervento = task.getPaiIntervento();
            put("attivita", task.getDesTask());
            put("id", task.getId().toString());
            put("task_id", task.getTaskid());
            put("cognome", paiTask.getAnagrafeSoc().getCognome());
            put("nome", paiTask.getAnagrafeSoc().getNome());
            put("assistente", paiTask.getCodUteAs().getCognome() + " " + paiTask.getCodUteAs().getNome());
            put("pai", paiTask.getCodPai().toString());


            put("ruolo", task.getRuolo());

            if (paiIntervento != null) {
                TipologiaIntervento tipologiaIntervento = paiIntervento.getTipologiaIntervento();
                put("intervento", tipologiaIntervento.getDesTipint());
                put("servizio", tipologiaIntervento.getIdParamSrv().getIdParam().getCodParam() + " (" + tipologiaIntervento.getIdParamSrv().getDesParam() + ")");
                put("urgente", Character.toString(paiIntervento.getUrgente()));
                if (tipologiaIntervento.getFlgAppTec() == 'S') {
                    put("approvato", Character.toString(paiIntervento.getApprovazioneTecnica()));
                } else {
                    put("approvato", 'N');
                }

            } else {
                put("intervento", "");
                put("servizio", "nessun servizio");
                put("urgente", "N");
                put("approvato", 'N');
            }

            put("uot", paiTask.getIdParamUot().getDesParam());
            if (task.getCodTmpl() != null) {
                put("templateFile", task.getCodTmpl().getNomeFile());
                put("templateDesc", task.getCodTmpl().getDesTmpl());
            }
            put("timestamp", task.getTsCreazione() == null ? null : task.getTsCreazione().getTime() / 1000);
        }
    };
    private static final Function<Appuntamento, Map> appTransformerFunction = new JsonMapTransformer<Appuntamento>() {
        @Override
        public void transformToMap(Appuntamento appuntamento) {
            Pai paiTask = appuntamento.getCodPai();
            put("attivita", "Appuntamento");
            put("id", appuntamento.getIdApp());
            put("task_id", "");
            put("cognome", paiTask.getAnagrafeSoc().getCognome());
            put("nome", paiTask.getAnagrafeSoc().getNome());
            put("assistente", paiTask.getCodUteAs().getCognome() + " " + paiTask.getCodUteAs().getNome());
            put("pai", paiTask.getCodPai().toString());

            put("ruolo", "TODO");

//            if (paiInterventoTask != null) {
//                TipologiaIntervento tipologiaIntervento = paiInterventoTask.getTipologiaIntervento();
//                put("intervento", tipologiaIntervento.getDesTipint());
//                put("servizio", tipologiaIntervento.getIdParamSrv().getIdParam().getCodParam() + " (" + tipologiaIntervento.getIdParamSrv().getDesParam() + ")");
//            } else {
            put("intervento", "");
            put("servizio", "nessun servizio");
//            }

            put("uot", paiTask.getIdParamUot().getDesParam());
//            if (appuntamento.getCodTmpl() != null) {
//                put("templateFile", appuntamento.getCodTmpl().getNomeFile());
//                put("templateDesc", appuntamento.getCodTmpl().getDesTmpl());
//            }
            put("timestamp", appuntamento.getTsIniApp().getTime() / 1000);
        }
    };

    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put("NOME", TaskDao.TASK_NOME);
        map.put("COGNOME", TaskDao.TASK_COGNOME);
        map.put("COGNOME_UTENTE", TaskDao.TASK_COGNOME);
        map.put("ATTIVITA", TaskDao.TASK_ATTIVITA);
        map.put("PAI", TaskDao.TASK_PAI);
        map.put("NUMERO_PAI", TaskDao.TASK_PAI);
        map.put("INTERVENTO", TaskDao.TASK_INTERVENTO);
        map.put("UOT", TaskDao.TASK_COD_PARAM_UOT);
        map.put("ASSISTENTE", TaskDao.TASK_ASS_SOC_FILTER);
        map.put("PROTOCOLLO", TaskDao.TASK_PROTOCOLLO);
        map.put("DATA_APERTURA", TaskDao.TASK_DATA_APERTURA_PAI);
        map.put("ASS_SOC_FILTER", TaskDao.TASK_ASS_SOC_FILTER);
        map.put("COD_FORM", TaskDao.TASK_FORM);
        conditionsMap = Collections.unmodifiableMap(map);
    }

    @Override
    public Object load() throws Exception {
        return super.load();
    }

    @Override
    public Object load(int offset, int limit, int page, List<Order> orderList) throws Exception {
        String paramWhat = getParameter("paramWhat");
        String paramBy = getParameter("paramBy");
        String uotFilter = getParameter("uotFilter");
        String assistenteFilter = getParameter("asSocFilter");
        String poFilter = getParameter("poFilter");
        String attivitaFilter = getParameter("attivitaFilter");
        String interventiFilter = getParameter("interventiFilter");

        TaskDao dao = new TaskDao(getEntityManager());
        //List<UniqueTasklist> taskList = dao.findAllActiveTask(token, uotFilter, assistenteFilter, paramWhat, paramBy, orderProperty, orderDirection, limit, offset);
        List<Condition> conditions = new ArrayList<Condition>();
        if (uotFilter != null) {
//			conditions.add(isEqual(TaskDao.TASK_COD_PARAM_UOT, Integer.parseInt(uotFilter)));
            conditions.add(isEqual(TaskDao.TASK_COD_PARAM_UOT, uotFilter));
        }
        if (StringUtils.isNotBlank(assistenteFilter)) {
            conditions.add(isEqual(TaskDao.TASK_ASS_SOC_FILTER, Integer.parseInt(assistenteFilter)));
        }
        if (StringUtils.isNotBlank(interventiFilter)) {
            conditions.add(isEqual("tipInt.codTipint", interventiFilter));
        }

        if (StringUtils.isNotBlank(attivitaFilter)) {
            String descAttivita = TaskDao.mapAttivita.get(Integer.valueOf(attivitaFilter));
            conditions.add(isEqual("t.desTask", descAttivita));
        }
        if (StringUtils.isNotBlank(poFilter)) {
            Integer poId = Integer.valueOf(poFilter);
            conditions.add(isEqual("asp.idParamPo.idParamIndata", poId));
        }


        if (!StringUtils.isBlank(paramWhat) && !StringUtils.isBlank(paramBy)) {
            if (conditionsMap.containsKey(paramBy)) {
                conditions.add(ilike(conditionsMap.get(paramBy), paramWhat));
            } else if (Objects.equal(paramBy, AUTO_SEARCH)) {
                conditions.add(smartMatchSplit(paramWhat,
                        TaskDao.TASK_NOME,
                        TaskDao.TASK_COGNOME,
                        TaskDao.TASK_PAI,
                        TaskDao.TASK_INTERVENTO,
                        TaskDao.TASK_DESC_FORM,
                        TaskDao.TASK_DESC,
                        TaskDao.TASK_DESC_INTERVENTO));
            } else {
                getLogger().warn("filter key not recognised = '{}'", paramBy);
            }
        }

        //getLogger().debug("tasklist user : "+JSonUtils.getGsonPrettyPrinting().toJson(user));
        if (getLiferayUser() == null) {
            clearHttpSession();
        }
        Validate.notNull(getLiferayUser(), "devi essere loggato per caricare la tasklist");
        Validate.notNull(getUtente(), "l'utente di sessione liferay (" + getLiferayUser().getLogin() + ") non è presente sul db Welfarego");
//		String userSrvDeswc = getUtente().getIdParamSer().getDesParam(),userSrvCod=getUtente().getIdParamSer().getIdParam().getCodParam();
        getLogger().debug("utente : " + getUserInfo() + "\n"
                + "\truoli : " + StringUtils.join(getUserRoles(), ", ") + "\n"
                + "\tuot : " + StringUtils.join(getUserUots(), ", ") + "\n"
                + "\tservizi : " + StringUtils.join(getUserServices(), ", "));

        Condition isFoCondition = isNull(TaskDao.TASK_INTERVENTO);

//		Condition srvCondition;
//		if (!userSrvCod.contains("-")) {
//			srvCondition = elike(TaskDao.TASK_INTERVENTO_SERVIZIO_COMP_COD, userSrvCod + "-%");
//		} else {
//			srvCondition = isEqual(TaskDao.TASK_INTERVENTO_SERVIZIO_COMP, (Object) getUtente().getIdParamSer());
        Condition uotCondition = isIn(TaskDao.TASK_UOT_COD, getUserUots());
        //TODO DA TOGLIEREEEEEEE?
        Condition srvCondition = isIn(TaskDao.TASK_INTERVENTO_SERVIZIO_COMP_COD, getUserServices());
//		}

      /*  conditions.add(or(
                and(isFoCondition, uotCondition),
                and(not(isFoCondition), srvCondition)
                ));*/

        conditions.add(isIn(TaskDao.TASK_RUOLO, getUserRoles()));

//        conditions.add(ConditionBuilder.equals("field", "poFilter"));
//        conditions.add(ConditionBuilder.equals("field", "attivitaFilter"));
//        conditions.add(ConditionBuilder.equals("field", "interventiFilter"));

//        List<it.wego.persistence.objects.Order> orders = ConditionUtils.parseOrder(orderList, conditionsMap);

        List<UniqueTasklist> taskList = dao.findAllActiveTask(conditions, null, null, null);
//        Integer count = dao.findAllActiveTaskCount(conditions).intValue();

        List<Map> data = Lists.newArrayList(Lists.transform(taskList, taskTransformerFunction));

        JsonBuilder jsonBuilder = JsonBuilder.newInstance().withData(taskList);
        jsonBuilder = jsonBuilder.withTransformer(taskTransformerFunction);
        jsonBuilder = jsonBuilder.withParameters(getParameters());
        return jsonBuilder.buildStoreResponse();
    }
}
