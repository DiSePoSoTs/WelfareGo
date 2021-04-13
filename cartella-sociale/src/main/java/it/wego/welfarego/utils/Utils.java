/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.utils;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import it.wego.welfarego.intalio.WelfareGoIntalioManager;
import it.wego.welfarego.intalio.WelfaregoProcessBean;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author giuseppe
 */
public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    /**
     * 
     * @param pai
     * @param intervento
     * @param codTmpl
     * @param desDoc
     * @return
     * @deprecated use WelfareGoIntalioManager.getWelfaregoProcessBean()
     */
    @Deprecated
    public static WelfaregoProcessBean getWelfareGoIntalioManager(Pai pai, PaiIntervento intervento,
            String codTmpl, String desDoc) {
        return WelfareGoIntalioManager.getWelfaregoProcessBean(pai, intervento, codTmpl, desDoc);
    }

    public User getUser(HttpSession session) {
        User utenteConnesso = null;
        try {
            Long userId = (Long) session.getAttribute(WebKeys.USER_ID);
            if (userId != null) {
                utenteConnesso = UserLocalServiceUtil.getUserById(userId);
                Log.APP.info("Utente connesso: {}", utenteConnesso.getLogin());
            }
        } catch (Exception ex) {
            Log.APP.error("Errore cercando di reperire l'utente", ex);
        }
        return utenteConnesso;
    }

    public int getCodiceMotivazioneTrasferimento() {
        EntityManager em = Connection.getEntityManager();
        int result = 0;
        result = Parametri.getCodiceMotivazioneTrasferimento(em);
        em.close();
        return result;
    }

    public int getCodiceMotivazioneDecesso() {
        EntityManager em = Connection.getEntityManager();
        int result = 0;
        result = Parametri.getCodiceMotivazioneDecesso(em);
        em.close();
        return result;
    }

    public Utenti getOperatore(String username) {
        EntityManager em = Connection.getEntityManager();
        UtentiDao utentiDao = new UtentiDao(em);
        Utenti utente = utentiDao.findByUsername(username);
        return utente;
    }

}
