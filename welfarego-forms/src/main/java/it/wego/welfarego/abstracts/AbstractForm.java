package it.wego.welfarego.abstracts;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.GsonObject;
import it.wego.persistence.PersistenceAdapter;
import it.wego.web.WebUtils;
import it.wego.webdav.DavResourceContext;
import it.wego.welfarego.azione.models.InterventoDataModel;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.ParametriDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiEvento;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.webdav.DocumentDavResource;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.entities.Utenti.Ruoli;
import it.wego.welfarego.utils.WelfaregoUtils;
import it.wego.welfarego.xsd.pratica.Pratica;

import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rappresenta la componente lato server di supporto ad una form. Gli oggetti
 * che estendono questa classe devono gestire almeno uno dei metodi load,save,
 * proceed.
 *
 * @author Aleph
 */
public abstract class AbstractForm {

    public static final String PARAM_TASKID_KEY = "task_id";
    public static final String PORTLET_SESSION_ATTRS_KEY = "it.wego.welfarego.portlet.jspSessionAttrs";

    public static final String PARAM_DATA_KEY = "data";
    public static final String PARAM_ACTION_KEY = "action";
    public final static NumberFormat decimalNumberFormat = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.ITALY));
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static enum Action {

        LOAD, SAVE, PROCEED, UPDATE, DELETE, PRINT;
    }


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, String> parameters = null;
    private Map<String, Object> attributes = null;
    private Set<String> liferayGroupNames = null;
    private Set<String> userUot = null;
    private Set<String> userRoles = null;
    private Set<String> userServices = null;
    private Object data = null;
    private PersistenceAdapter persistenceAdapter = null;
    private UniqueTasklist task = null;
    private static final Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();


    public Gson getGson() {
        return gson;
    }

    public Map<String, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<String, Object>();
        }
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Object getAttribute(String key) {
        return getAttributes().get(key);
    }

    public void setAttribute(String key, Object value) {
        getAttributes().put(key, value);
    }

    public AbstractForm() {
        getLogger().debug("form handler ready");
    }

    @Deprecated
    public AbstractForm(Map<String, String> parameters) {
        this();
        this.parameters = parameters;
    }

    public AbstractForm(AbstractForm parent) {
        this();
        this.parameters = parent.parameters;
        this.attributes = parent.attributes;
    }

    public Object handleRequest() throws Exception {
        if (getParameter(PARAM_ACTION_KEY) == null) {
            getParameters().put(PARAM_ACTION_KEY, Action.LOAD.toString());
        }
        Action.valueOf(getParameter(PARAM_ACTION_KEY).toUpperCase());
        Class<? extends AbstractForm> aClass = getClass();
        Method method = aClass.getMethod(getParameter(PARAM_ACTION_KEY).toLowerCase());


        getLogger().debug("da AbstractForm eseguo : " +aClass.getCanonicalName() + "." + method.getName());

        return method.invoke(this);
    }

    public Object handleRequest(Map<String, String> parameters) throws Exception {
        setParameters(parameters);
        return handleRequest();
    }

    public void handleRequest(Writer writer) throws Exception {
        Getable getable = (Getable) this;
        getable.get(writer);
    }

    public String getUserInfo() {
        User user = getLiferayUser();
        if (user == null) {
            return null;
        } else {
            try {
                return user.getLogin() + "@" + user.getLoginIP();
            } catch (Exception ex) {
                logger.error("error retrieving user info", ex);
                return "<error>";
            }
        }
    }


    /**
     * recupera dai parametri il json inviato dal client e lo spacchetta
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public <T extends Object> T getDataParameter(Class<T> clazz) {
        return (T) (data != null ? data : (data = gson.fromJson(
                getParameter(PARAM_DATA_KEY),
                clazz)));
    }

    public Object prepareDavDocument() throws Exception {
        return prepareDavDocument(getCodTipDoc());
    }

    public boolean hasDocument(String codTipDoc) {
        return new PaiDocumentoDao(getEntityManager()).findLastDoc(getTask().getCodPai(), getTask().getPaiIntervento(), codTipDoc) != null;
    }

    public Object prepareDavDocument(String codTipdoc) throws Exception {
        Pai pai = getTask().getCodPai();
        //TODO: tooooooooo bad. Gestire meglio
        if (codTipdoc.length() > 19) {
            codTipdoc = codTipdoc.substring(0, 19);
        }
        PaiIntervento paiIntervento = getTask().getPaiIntervento();
        DavResourceContext resourceContext = DavResourceContext.getResourceContext(
                MoreObjects.firstNonNull(Strings.emptyToNull(WelfaregoUtils.getConfig("it.wego.wefarego.webdav.host")), getParameter(WebUtils.REQUEST_PATH_PROP_KEY)),
                "/[^/]*Servlet.*",
                WelfaregoUtils.getConfig("it.wego.wefarego.webdav.port"));

        DocumentDavResource documentDavResource = paiIntervento != null
                ? new DocumentDavResource(paiIntervento.getPaiInterventoPK(), codTipdoc)
                : new DocumentDavResource(pai.getCodPai(), codTipdoc);
        return JsonBuilder.newInstance().withData(resourceContext.getJavascriptFunctionForResource(documentDavResource)).buildResponse();
    }

    public static String getJsonResponse(Action action, Map<String, String> parameters, Class<? extends AbstractForm> clazz) throws Exception {
        AbstractForm abstractForm = clazz.newInstance();
        String res = null;
        parameters = new HashMap<String, String>(parameters);
        try {
            parameters.put(PARAM_ACTION_KEY, action.toString());
            if (parameters.get(AbstractServlet.METHOD_PROP) == null) {
                parameters.put(AbstractServlet.METHOD_PROP, AbstractServlet.METHOD_GET);
            }
            res = gson.toJson(abstractForm.handleRequest(parameters));
            return res;
        } catch (Exception e) {
            throw e;
        }
    }

    public PersistenceAdapter getPersistenceAdapter() {
        if (persistenceAdapter == null) {
            persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
        }
        return persistenceAdapter;
    }

    public void setPersistenceAdapter(PersistenceAdapter persistenceAdapter) {
        this.persistenceAdapter = persistenceAdapter;
    }


    public UniqueTasklist getTask() {
        if (task == null) {
            String taskId = null;
            try {
                taskId = getParameter(PARAM_TASKID_KEY);
                if (taskId != null) {
                    task = getPersistenceAdapter().getEntityManager().find(UniqueTasklist.class, new BigDecimal(taskId));
                }
            } catch (Exception e) {
                getLogger().error("error while retrieving form associated task", e);
            }
            if (task == null) {
                getLogger().warn("task not found : " + taskId);
            }
        }
        return task;
    }

    public void init(AbstractForm form) {
        this.setAttributes(form.getAttributes());
        this.setParameters(form.getParameters());
    }

    public String getCodTipDoc() {
        // TODO soluzione improvvisata, da rivedere
        String desTmpl = getTask().getCodTmpl().getDesTmpl();
        return desTmpl.substring(0, Math.min(19, desTmpl.length()));
    }

    public String getCodTipDoc(UniqueTasklist task) {
        // TODO soluzione improvvisata, da rivedere
        String desTmpl = task.getCodTmpl().getDesTmpl();
        return desTmpl.substring(0, Math.min(19, desTmpl.length()));
    }

    public PaiEvento insertEvento(Pai pai, PaiIntervento paiIntervento, String descrizioneEvento) throws Exception {
        logger.debug("inserting event : " + descrizioneEvento);
        initTransaction();
        Utenti utente = getUtente();
        Validate.notNull(utente, "Devi essere loggato per completare questa operazione");
        PaiEvento paiEvento = Pratica.serializePaiEvento(
                pai,
                paiIntervento,
                pai.getCartellaSociale(),
                descrizioneEvento,
                utente);
        getEntityManager().persist(paiEvento);
        commitTransaction();
        logger.debug("inserted event : " + descrizioneEvento);
        return paiEvento;
    }

    public PaiEvento insertEvento(Pai pai, String descrizioneEvento) throws Exception {
        return insertEvento(pai, null, descrizioneEvento);
    }

    public PaiEvento insertEvento(PaiIntervento paiIntervento, String descrizioneEvento) throws Exception {
        return insertEvento(paiIntervento.getPai(), paiIntervento, descrizioneEvento);
    }

    public void initTransaction() {
        getPersistenceAdapter().initTransaction();
    }

    public void commitTransaction() {
        getPersistenceAdapter().commitTransaction();
    }

    public EntityManager getEntityManager() {
        return getPersistenceAdapter().getEntityManager();
    }

    public PaiEvento insertEvento(UniqueTasklist task, String descrizioneEvento) throws Exception {
        return insertEvento(task.getCodPai(), task.getPaiIntervento(), descrizioneEvento);
    }

    public PaiEvento insertEvento(String descrizioneEvento) throws Exception {
        return insertEvento(getTask(), descrizioneEvento);
    }

    public Map<String, String> getParameters() {
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getParameter(String key) {
        return getParameters().get(key);
    }

    public <T> T getParameter(String key, Type type) {
        String dataStr = getParameter(key);
        return (T) (Strings.isNullOrEmpty(dataStr) ? null : gson.fromJson(dataStr, type));
    }

    public String setParameter(String key, String value) {
        return getParameters().put(key, value);
    }

    public final Logger getLogger() {
        return logger;
    }

    public Map<String, Object> getPortletSessionAttributes() {
        Map<String, Object> attrs = (Map<String, Object>) getAttribute(PORTLET_SESSION_ATTRS_KEY);
        if (attrs == null || attrs.isEmpty()) {
            getLogger().warn("got no session attributes");
            return Collections.EMPTY_MAP;
        } else {
            return attrs;
        }
    }

    public static enum HandleMode {

        JSON, RAW
    }

    public HandleMode getHandleMode() {
        return HandleMode.JSON;
    }

    public Object getPortletSessionAttribute(String key) {
        return getPortletSessionAttributes().get(key);
    }

    public void setPortletSessionAttributes(Map<String, Object> attrs) {
        setAttribute(PORTLET_SESSION_ATTRS_KEY, attrs);
    }

    private User liferayUser;

    public User getLiferayUser() {
        if (liferayUser == null) {
            try {
                Long userId = (Long) getPortletSessionAttribute(WebKeys.USER_ID);
                if (userId != null) {
                    liferayUser = UserLocalServiceUtil.getUserById(userId);
                }
                if (liferayUser == null) {
                    getLogger().debug("no liferay user found, userId = {}", userId);
                } else {
                    getLogger().debug("got liferay user = '{}'", liferayUser.getLogin());
                }
            } catch (Exception ex) {
                getLogger().error("error while retrieving user id", ex);
            }
        }
        return liferayUser;
    }

    private Utenti user = null;

    public Utenti getUtente() {
        if (user == null) {
            try {
                getLiferayUser();
                if (liferayUser != null) {
                    String userName = liferayUser.getLogin();
                    user = (new UtentiDao(getEntityManager())).findByUsername(userName);
                    if (user == null) {
                        getLogger().warn("welfarego user not found for intalio user '{}'", userName);
                    }
                }
            } catch (Exception ex) {
                getLogger().error("error while retrieving user id", ex);
            }
        }
        return user;
    }

    public Set<String> getLiferayGroupNames() throws Exception {
        if (getLiferayUser() == null) {
            return Collections.emptySet();
        }
        if (liferayGroupNames == null) {
            liferayGroupNames = new TreeSet<String>();
            Collection<Role> roles = new ArrayList<Role>();
            roles.addAll(getLiferayUser().getRoles());
            for (UserGroup group : getLiferayUser().getUserGroups()) {
                liferayGroupNames.add(group.getName());
                roles.addAll(RoleLocalServiceUtil.getGroupRoles(group.getGroup().getGroupId()));
            }
            for (Role role : roles) {
                liferayGroupNames.add(role.getName());
            }
            getLogger().debug("got user group names : " + StringUtils.join(liferayGroupNames, ","));
        }
        return liferayGroupNames == null ? Collections.EMPTY_SET : liferayGroupNames;
    }

    public Set<String> getUserGroups(String paramType) throws Exception {
        return getUserGroups(null, paramType);
    }

    public Set<String> getUserGroups(Predicate<String> predicate) throws Exception {
        return Sets.newTreeSet(Iterables.filter(getLiferayGroupNames(), predicate));
    }

    public Set<String> getUserGroups(final String prefix, String paramType) throws Exception {
        Set<String> res = new TreeSet<String>(), names = getLiferayGroupNames();
        if (prefix != null && prefix.length() > 0) {
            for (String name : names) {
                if (name.matches("^" + prefix + ".*$")) {
                    res.add(name.replaceFirst(prefix, ""));
                }
            }
        } else {
            res.addAll(names);
        }
        res.retainAll(new ParametriDao(getEntityManager()).getParamCodesByTipParam(paramType));
        return res;
    }

    public Set<String> getUserUots() throws Exception {
        if (userUot == null) {
            userUot = getUserGroups("UOT +", Parametri.CODICE_UOT);
            if (getUtente() != null && getUtente().getIdParamUot() != null) {
                userUot.add(getUtente().getIdParamUot().getIdParam().getCodParam());
            }
        }
        return userUot;
    }

    public Set<String> getUserRoles() throws Exception {
        if (userRoles == null) {
            userRoles = getUserGroups(new Predicate<String>() {
                private final Set<String> names = Sets.newHashSet(Iterables.transform(Arrays.asList(Utenti.Ruoli.values()), new Function<Ruoli, String>() {
                    public String apply(Ruoli input) {
                        return input.name();
                    }
                }));

                public boolean apply(String input) {
                    return names.contains(input);
                }
            });
            if (getUtente() != null && getUtente().getIdParamLvlAbil() != null) {
                userRoles.add(getUtente().getIdParamLvlAbil().getIdParam().getCodParam());
            }
        }
        return userRoles;
    }

    public Set<String> getUserServices() throws Exception {
        if (userServices == null) {
            userServices = getUserGroups("SERVIZIO +", Parametri.CODICE_DEL_SERVIZIO);
            if (getUtente() != null && getUtente().getIdParamSer() != null) {
                userServices.add(getUtente().getIdParamSer().getIdParam().getCodParam());
            }
        }
        return userServices;
    }

    public UserInfoDataModel getUserInfoDataModel() throws Exception {
        if (getUtente() == null) {
            return null;
        }
        UserInfoDataModel res = new UserInfoDataModel();
        res.setGroups(getLiferayGroupNames());
        res.setRoles(getUserRoles());
        res.setServices(getUserServices());
        res.setUot(getUserUots());
        res.setUserName(getLiferayUser().getLogin());
        res.setIp(getLiferayUser().getLoginIP());
        res.setId(getUtente().getCodUte().toString());
        return res;
    }

    public void clearHttpSession() {
        HttpSession session = (HttpSession) getAttribute(AbstractServlet.SESSION_ATTR);
        if (session != null) {
            getLogger().debug("destroying http session");
            session.invalidate();
            getAttributes().remove(AbstractServlet.SESSION_ATTR);
        }
        getLogger().debug("clearing session cookies");
        HttpServletRequest request = (HttpServletRequest) getAttribute(AbstractServlet.SERVLET_REQUEST_ATTR);
        HttpServletResponse response = (HttpServletResponse) getAttribute(AbstractServlet.SERVLET_RESPONSE_ATTR);
        for (Cookie cookie : request.getCookies()) {
            if ("JSESSIONID".equals(cookie.getName())) {
                getLogger().debug("clearing cookie : " + JsonBuilder.getGsonPrettyPrinting().toJson(cookie));
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public <T extends InterventoDataModel> T loadInterventoData(Class<T> clazz) throws Exception {
        T res = clazz.newInstance();
        Pai pai = getTask().getCodPai();
        AnagrafeSoc anagrafeSoc = pai.getCodAna().getAnagrafeSoc();
        Utenti utente = pai.getCodUteAs();
        res.setNomeUtente(anagrafeSoc.getNome());
        res.setCognomeUtente(anagrafeSoc.getCognome());
        res.setAssistSoc(utente.getCognome() + " " + utente.getNome());
        res.setDataApertPai(pai.getDtApePai());
        return res;
    }

    public boolean isInLiferay() {
        return getAttribute(GenericFormPortlet.PORTLET_CONTEXT) != null;
    }

    public static interface Loadable {

        public Object load() throws Exception;
    }

    public static interface Saveable {

        public Object save() throws Exception;
    }

    public static interface Proceedable {

        public Object proceed() throws Exception;
    }

    public static interface Printable {

        public Object print(HttpServletRequest request, HttpServletResponse response) throws Exception;
    }

    public static interface Updatable {

        public Object update() throws Exception;
    }

    public static interface Deletable {

        public Object delete() throws Exception;
    }

    public static interface Viewable {

        public void doView(Writer writer) throws Exception;
    }

    public static interface Getable {

        public void get(Writer writer) throws Exception;
    }

    public static class UserInfoDataModel extends GsonObject {

        private Collection<String> groups, roles, uot, services;
        private String userName, ip, id;

        public Collection<String> getGroups() {
            return groups;
        }

        public void setGroups(Collection<String> groups) {
            this.groups = groups;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Collection<String> getRoles() {
            return roles;
        }

        public void setRoles(Collection<String> roles) {
            this.roles = roles;
        }

        public Collection<String> getServices() {
            return services;
        }

        public void setServices(Collection<String> services) {
            this.services = services;
        }

        public Collection<String> getUot() {
            return uot;
        }

        public void setUot(Set<String> uot) {
            this.uot = uot;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
