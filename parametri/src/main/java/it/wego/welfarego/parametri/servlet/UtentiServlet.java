/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.servlet;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import it.wego.extjs.json.JsonBuilder;
import it.wego.extjs.json.JsonMapTransformer;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.welfarego.model.json.JSONGeneric;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.Associazione;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * @author Michele
 */
public class UtentiServlet extends JsonServlet {

    {
        setGson(new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create());
    }

//    public static final String METHOD_POST = "POST";
//    public static final String METHOD_GET = "GET";
//    private Logger logger = Logger.getLogger(UtentiServlet.class);
//    private Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
//
//    @Override
//    public void init() throws ServletException {
//        super.init();
//        setGson(new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create());
//    }
    public Object load() throws Exception {
        EntityManager em = Connection.getEntityManager();
        try {
            return JsonBuilder.newInstance().withSourceData(new UtentiDao(em).findAll()).withParameters(getParameters()).withTransformer(new JsonMapTransformer<Utenti>() {
                Map<String, User> liferayUsers = Maps.uniqueIndex(UserLocalServiceUtil.getUsers(0, Integer.MAX_VALUE), new Function<User, String>() {
                    public String apply(User input) {
                        try {
                            return input.getLogin();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                @Override
                public void transformToMap(Utenti utente) {
                    put("cod_ute", utente.getCodUte().toString());
                    put("cognome", utente.getCognome());
                    put("nome", utente.getNome());
                    put("cod_fisc", utente.getCodFisc());
                    put("email", utente.getEmail());
                    put("num_cell", utente.getNumCell());
                    put("num_tel", utente.getNumTel());
                    put("note", utente.getNote());
                    put("username", utente.getUsername());
                    put("motivazione",utente.getMotivazione());
                    put("profilo",utente.getProfilo());
                    put("problematiche",utente.getProblematiche());

					if (utente.getAssociazione() != null)
					{
						put("id_associazione", utente.getAssociazione().getId());
					}
                    if (utente.getIdParamLvlAbil() != null) {
                        put("id_param_lvl_abil_des", utente.getIdParamLvlAbil().getDesParam());
                        put("id_param_lvl_abil", utente.getIdParamLvlAbil().getIdParamIndata().toString());

                    }
                    if (utente.getIdParamSer() != null) {
                        put("id_param_ser_des", utente.getIdParamSer().getDesParam());
                        put("id_param_ser", utente.getIdParamSer().getIdParamIndata().toString());
                    }


                    if (utente.getIdParamUot() != null) {
                        put("id_param_uot_des", utente.getIdParamUot().getDesParam());
                        put("id_param_uot", utente.getIdParamUot().getIdParamIndata().toString());
                    }
                    
                    if (utente.getIdParamPo() != null) {
                        put("id_param_po_des", utente.getIdParamPo().getDesParam());
                        put("id_param_po", utente.getIdParamPo().getIdParamIndata().toString());
                    }
                    User liferayUser = liferayUsers.get(utente.getUsername());
                    if (liferayUser != null) {
                        put("liferay_user_id", liferayUser.getUserId());
                    }
                }
            }).buildStoreResponse();
        } finally {
            em.close();
        }




    }

    private Object save()
            throws Exception {

//        Gson gson = new Gson();

//        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            /**
             * Per quanto riguarda i parametri
             */
            UtentiDao dao = new UtentiDao(em);
            Utenti utente = new Utenti();

            if (!getParameter("cod_ute").isEmpty()) {
                utente.setCodUte(Integer.valueOf(getParameter("cod_ute")));
            }

            utente.setNome(getParameter("nome"));
            utente.setNumTel(getParameter("num_tel"));
            utente.setCognome(getParameter("cognome"));
            utente.setNumCell(getParameter("num_cell"));
            utente.setCodFisc(getParameter("cod_fisc").toUpperCase().replaceAll("[^A-Z0-9]", ""));
            utente.setEmail(getParameter("email"));
            utente.setUsername(getParameter("username"));
            String motivazione = getParameter("motivazione");
            String problematiche = getParameter("problematiche");
            String profilo = getParameter("profilo");
            if(!Strings.isNullOrEmpty(motivazione)){
            	utente.setMotivazione('S');
            }
            else {
            utente.setMotivazione('N');
            }
            if(!Strings.isNullOrEmpty(problematiche)){
            	utente.setProblematiche('S');
            }
            else {
            utente.setProblematiche('N');
            }
            if(!Strings.isNullOrEmpty(profilo)){
            	utente.setProfilo('S');
            }
            else {
            utente.setProfilo('N');
            }

            if (!Strings.isNullOrEmpty(getParameter("id_param_ser"))) {
                ParametriIndata ser = new ParametriIndata(Integer.valueOf(getParameter("id_param_ser")));
                utente.setIdParamSer(ser);
            }
            if (!Strings.isNullOrEmpty(getParameter("id_param_po"))) {
                ParametriIndata po = new ParametriIndata(Integer.valueOf(getParameter("id_param_po")));
                utente.setIdParamPo(po);
            }

            if (!Strings.isNullOrEmpty(getParameter("id_param_uot"))) {
                ParametriIndata uot = new ParametriIndata(Integer.valueOf(getParameter("id_param_uot")));
                utente.setIdParamUot(uot);
            }

            if (!Strings.isNullOrEmpty(getParameter("id_param_lvl_abil"))) {
                ParametriIndata lvlabil = new ParametriIndata(Integer.valueOf(getParameter("id_param_lvl_abil")));
                utente.setIdParamLvlAbil(lvlabil);
            }
			if (!Strings.isNullOrEmpty(getParameter("id_associazione"))){
				Associazione ass = new Associazione();
				ass.setId(Integer.valueOf(getParameter("id_associazione")));
				utente.setAssociazione(ass);
			}

            getLogger().info("salviamo utente {}", utente);
            em.getTransaction().begin();

            // poi il dato collegato
            em.merge(utente);

            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Aggiornamento avvenuto correttamente");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);
            return jsonObj;

        } catch (Exception e) {
//
//
//            e.printStackTrace();
//            logger.debug("UtentiServlet save() : " + e);
//
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
//            data.setCode("KO");
//            data.setMessage(e.getMessage());
//
//            jsonObj.setSuccess(false);
//            jsonObj.setData(data);

        } finally {

            em.close();
//            String json = gson.toJson(jsonObj);
//            out.write(json);
//            out.flush();
//            out.close();

        }

    }

    private Object delete()
            throws Exception {

//        Gson gson = new Gson();
//
//        PrintWriter out = response.getWriter();
        JSONGeneric jsonObj = new JSONGeneric();
        JSONGeneric.Data data = new JSONGeneric.Data();
        EntityManager em = Connection.getEntityManager();

        try {

            em.getTransaction().begin();

            String codUte = Strings.emptyToNull(getParameter("codUte"));
            Preconditions.checkNotNull(codUte);

            UtentiDao dao = new UtentiDao(em);

            // carico l'utente
            Utenti utente = dao.findByCodUte(codUte);
            Preconditions.checkNotNull(utente, "utente non trovato per codUte = %s", codUte);

            getLogger().info("eliminazione utente {}", utente);
            // cancello l'utente
            em.remove(utente);

            em.getTransaction().commit();

            data.setCode("OK");
            data.setMessage("Eliminazione eseguita correttamente.");

            jsonObj.setSuccess(true);
            jsonObj.setData(data);
            return jsonObj;

        } catch (Exception e) {

//            e.printStackTrace();
//            logger.debug("UtentiServlet delete() : " + e);

            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
//            data.setCode("KO");
//            data.setMessage("Impossibile eliminare il dato poiché risultà in uso.");
//
//            jsonObj.setSuccess(false);
//            jsonObj.setData(data);

        } finally {

            em.close();
//            String json = gson.toJson(jsonObj);
//            out.write(json);
//            out.flush();
//            out.close();

        }

    }

    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        switch (Operation.valueOf(getAction().toUpperCase())) {
            case LOAD:
                return load();
            case SAVE:
                return save();
            case DELETE:
                return delete();
        }
        Preconditions.checkArgument(false);
        return null; //unreacheable
    }

    private static enum Operation {

        LOAD, SAVE, DELETE
    }
}
