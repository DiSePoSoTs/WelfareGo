package it.wego.welfarego.persistence.dao;

import com.google.common.base.Preconditions;
import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.objects.Condition;
import it.wego.persistence.objects.Order;
import it.wego.welfarego.persistence.entities.*;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author giuseppe
 */
public class TaskDao extends PersistenceAdapter implements ORMDao {

    public static final String FLAG_ENQUEUED = "Q",
            FLAG_SI = "S",
            FLAG_NO = "N",
            FLAG_R = "R",
            FLAG_OK = "OK",
            FLAG_KO = "KO",
            FLAG_INTERVENTO_RESPINTO ="K";

    public static final Map<Integer, String> mapAttivita;
    public static final String TASK_NOME = "t.codPai.codAna.anagrafeSoc.nome",
            TASK_COGNOME = "t.codPai.codAna.anagrafeSoc.cognome",
            TASK_ATTIVITA = "t.desTask",
            TASK_PAI = "t.codPai.codPai",
            TASK_DESC_INTERVENTO = "tipInt.desTipint",
            TASK_INTERVENTO = "tipInt.codTipint",
            TASK_INTERVENTO_SERVIZIO_COMP = "srv",
            TASK_INTERVENTO_SERVIZIO_COMP_DESC = "srv.desParam",
            TASK_INTERVENTO_SERVIZIO_COMP_COD = "srvParam.codParam",
            TASK_COD_UOT = "uot.idParamIndata",
            TASK_COD_PARAM_UOT = "uot.idParam.codParam",
            TASK_UOT = "uot.desParam",
            TASK_UOT_COD = "uot.idParam.codParam",
            TASK_ASSISTENTE = "asp.codUte",
            TASK_PROTOCOLLO = "t.codPai.numPg",
            TASK_DATA_APERTURA_PAI = "t.codPai.dtApePai",
            TASK_ASS_SOC_FILTER = "asp.codUte",
            TASK_FORM = "t.form.codForm",
            TASK_DESC_FORM = "t.form.desForm",
            TASK_DESC = "t.desTask",
            TASK_RUOLO = "t.ruolo";

    static {
        HashMap<Integer, String> map = new HashMap();
        map.put(0, "Approvazione tecnica");
        map.put(1, "Intervento non approvato - da rivedere");
        map.put(2, "Intervento non approvato-da rivedere ");
        map.put(3, "Notifica intervento respinto");
        map.put(4, "Predisposizione documento chiusura anticipata intervento");
        map.put(5, "Predisposizione documento Diniego intervento");
        map.put(6, "Protocolla documento Diniego intervento");
        map.put(7, "Protocollazione Determina di chiusura");
        map.put(8, "Protocollazione Determina di esecutivita");
        map.put(9, "Protocollazione Determina di variazione");
        map.put(10, "Validazione documento Diniego intervento");
        map.put(11, "Verifica dati esecutività");
        mapAttivita = Collections.unmodifiableMap(map);
    }

    private UniqueTasklist task = new UniqueTasklist();

    public TaskDao(EntityManager em) {
        super(em);
    }

    /**
     * cerca per id task
     *
     * @param taskId
     * @return
     */
    public UniqueTasklist findTask(String taskId) {
        return findOne(UniqueTasklist.class,
                "SELECT u FROM UniqueTasklist u",
                ConditionBuilder.equals("u.id", new BigDecimal(taskId)),
                ConditionBuilder.equals("u.flgEseguito", FLAG_NO));
    }

    public void markCompleted(UniqueTasklist task) {
        initTransaction();
        task.setFlgEseguito(FLAG_SI);
        commitTransaction();
    }

    public void markQueued(UniqueTasklist task) {
        initTransaction();
        task.setFlgEseguito(FLAG_ENQUEUED);
        commitTransaction();
    }

    public void markQueued(UniqueTasklist task, String esito) {
        initTransaction();
        markQueued(task);
        task.setEsito(esito);
        commitTransaction();
    }

    public List<UniqueTasklist> findAllActiveTask(Collection<Condition> conditions, List<Order> orders, Integer limit, Integer offset) {
        conditions = new ArrayList<Condition>(conditions);
        String query = "SELECT t FROM UniqueTasklist t "
                + "JOIN t.codPai.codUteAs asp "
                + "JOIN t.codPai.idParamUot uot "
                + "LEFT JOIN t.paiIntervento pint "
                + "LEFT JOIN pint.tipologiaIntervento tipInt "
                + "LEFT JOIN tipInt.idParamSrv srv "
                + "LEFT JOIN srv.idParam srvParam";

        conditions.add(ConditionBuilder.equals("t.flgEseguito", FLAG_NO));
        return find(UniqueTasklist.class, query, conditions, orders, limit, offset);
    }

    public Long findAllActiveTaskCount(Collection<Condition> conditions) {
        conditions = new ArrayList<Condition>(conditions);
        String query = "SELECT COUNT(t) FROM UniqueTasklist t "
                + "JOIN t.codPai.codUteAs asp "
                + "JOIN t.codPai.idParamUot uot "
                + "LEFT JOIN t.paiIntervento pint "
                + "LEFT JOIN pint.tipologiaIntervento tipInt "
                + "LEFT JOIN tipInt.idParamSrv srv "
                + "LEFT JOIN srv.idParam srvParam";

        conditions.add(ConditionBuilder.equals("t.flgEseguito", FLAG_NO));
        return findOne(Long.class, query, conditions.toArray(new Condition[conditions.size()]));
    }

    public List<UniqueTasklist> findScheduledTask() throws Exception {
        return getEntityManager().createQuery("SELECT u FROM UniqueTasklist u WHERE u.flgEseguito='Q'", UniqueTasklist.class).getResultList();
    }

    public List<UniqueTasklist> findAllActiveTaskByPaiIntervento(PaiIntervento intervento) {
        return find(UniqueTasklist.class, "SELECT u FROM UniqueTasklist u", ConditionBuilder.equals("u.flgEseguito", "N"), ConditionBuilder.equals("u.paiIntervento", intervento));
    }

    public TaskDao withTaskid(String taskid) {
        task.setTaskid(taskid);
        return this;
    }

    public TaskDao withDesTask(String desTask) {
        task.setDesTask(desTask);
        return this;
    }

    public TaskDao withRuolo(String ruolo) {
        task.setRuolo(ruolo);
        return this;
    }

    public TaskDao withUot(String uot) {
        task.setUot(uot);
        return this;
    }

    public TaskDao withCampoFlow1(String campoFlow1) {
        task.setCampoFlow1(campoFlow1);
        return this;
    }

    public TaskDao withCampoFlow2(String campoFlow2) {
        task.setCampoFlow2(campoFlow2);
        return this;
    }

    public TaskDao withCampoFlow3(String campoFlow3) {
        task.setCampoFlow3(campoFlow3);
        return this;
    }

    public TaskDao withForm(UniqueForm form) {
        task.setForm(form);
        return this;
    }

    public TaskDao withTemplate(Template codTmpl) {
        task.setCodTmpl(codTmpl);
        return this;
    }

    public TaskDao withPaiIntervento(PaiIntervento paiIntervento) {
        task.setPaiIntervento(paiIntervento);
        return this;
    }

    public TaskDao withPai(Pai codPai) {
        task.setPai(codPai);
        return this;
    }

    public TaskDao withTaskExtraParameter(String key, String value) {
        task.putExtraParameterInCampoFlow8(key, value);
        return this;
    }

    /**
     * persist new task (won't handle transaction!! GE issue!! )
     * @return
     * @throws Exception 
     */
    public UniqueTasklist insertNewTask() throws Exception {
        Preconditions.checkNotNull(task);
        getEntityManager().persist(task);
        return task;
    }
}
