/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.serializer;

import com.google.common.base.Strings;
import it.wego.welfarego.persistence.dao.AnagrafeSocDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.entities.AnagrafeFam;
import it.wego.welfarego.persistence.entities.AnagrafeFamPK;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.Parametri;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author giuseppe
 */
public class AnagrafeFamigliaSerializer {

    private EntityManager em;

    public AnagrafeFamigliaSerializer(EntityManager em) {
        this.em = em;
    }

    public AnagrafeFam serializeAnagrafeFam(HttpServletRequest request) {
        int codAnag = Integer.valueOf(request.getParameter("codAnag"));
        int codAnaFamigliare = Integer.valueOf(request.getParameter("codAnaFamigliare"));
        AnagrafeFam famiglia = new AnagrafeFam();
        AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(em);
        AnagrafeSoc anagrafe = anagrafeSocDao.findByCodAna(codAnaFamigliare);
        AnagrafeFamPK key = new AnagrafeFamPK();
        key.setCodAna(codAnag);
        key.setCodAnaFam(anagrafe.getCodAna());
        famiglia.setAnagrafeFamPK(key);

        ParametriIndataDao parametriDao = new ParametriIndataDao(em);
        String qualificaString = request.getParameter("qualifica");
        if (qualificaString != null && !"".equals(qualificaString.trim())) {
            int id = Integer.valueOf(qualificaString);
            ParametriIndata qualifica = parametriDao.findByIdParamIndata(id);
            famiglia.setCodQual(qualifica);
        } else {
            ParametriIndata qualifica = parametriDao.findByIdParamIndata(Parametri.QUALIFICA_PARENTE);
            famiglia.setCodQual(qualifica);
        }

        return famiglia;
    }

    public AnagrafeFam serializeFamiglia(int codAnag, AnagrafeSoc anagrafeSociale, String qualifica) {
        AnagrafeFam famiglia = new AnagrafeFam();
        AnagrafeFamPK key = new AnagrafeFamPK();
        key.setCodAna(codAnag);
        key.setCodAnaFam(anagrafeSociale.getCodAna());
        famiglia.setAnagrafeFamPK(key);
        ParametriIndataDao parametriDao = new ParametriIndataDao(em);
        if (!Strings.isNullOrEmpty(qualifica)) {
            int id = Integer.valueOf(qualifica);
            ParametriIndata q = parametriDao.findByIdParamIndata(id);
            famiglia.setCodQual(q);
        } else {
            ParametriIndata q = parametriDao.findByIdParamIndata(Parametri.QUALIFICA_PARENTE);
            famiglia.setCodQual(q);
        }
        return famiglia;
    }
}
