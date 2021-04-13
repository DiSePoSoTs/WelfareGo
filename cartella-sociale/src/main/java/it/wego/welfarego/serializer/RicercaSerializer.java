package it.wego.welfarego.serializer;

import com.google.common.base.Strings;
import it.wego.welfarego.model.RisultatiRicercaBean;
import it.wego.welfarego.persistence.dao.PaiDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.utils.Connection;
import javax.persistence.EntityManager;

/**
 *
 * @author giuseppe
 */
public class RicercaSerializer {

    public static RisultatiRicercaBean serializeRisultatiRicerca(AnagrafeSoc anag) {
        RisultatiRicercaBean bean = new RisultatiRicercaBean();
        bean.setCodAnag(String.valueOf(anag.getCodAna()));
        bean.setCapResidenza(Strings.nullToEmpty(anag.getLuogoResidenza().getCap()));
        if (anag.getCodStatoCitt()!= null) {
            bean.setCittadinanza(String.valueOf(anag.getCodStatoCitt().getCodStato()));
        }

        if (anag.getLuogoResidenza().getComune() != null) {
            bean.setCodComuneResidenza(anag.getLuogoResidenza().getComune().getComunePK().getCodCom());
        }
        if (anag.getLuogoResidenza().getVia() != null) {
            bean.setCodViaResidenza(anag.getLuogoResidenza().getVia().getToponomasticaPK().getCodVia());
            bean.setDesViaResidenza(anag.getLuogoResidenza().getVia().getDesVia());
        } else {
            bean.setDesViaResidenza(anag.getLuogoResidenza().getViaStr());
        }
        bean.setCodiceFiscale(anag.getCodFisc());
        bean.setCognome(anag.getCognome());
        if (anag.getLuogoNascita().getComune() != null) {
            bean.setComuneNascita(anag.getLuogoNascita().getComune().getDesCom());
        } else {
            bean.setComuneNascita(anag.getLuogoNascita().getComuneStr());
        }
        bean.setDataMorte(anag.getDtMorte());

        bean.setDataNascita(anag.getDtNasc());
        if (anag.getLuogoResidenza().getComune() != null) {
            bean.setDesComuneResidenza(anag.getLuogoResidenza().getComune().getDesCom());
        } else {
            bean.setDesComuneResidenza(anag.getLuogoResidenza().getComuneStr());
        }
        bean.setNome(anag.getNome());
        bean.setNomeConiuge(anag.getCognomeConiuge());
        bean.setSesso(anag.getFlgSex());
        if (anag.getIdParamStatoCiv() != null) {
            bean.setStatoCivile(String.valueOf(anag.getIdParamStatoCiv().getDesParam()));
        }
        bean.setTipoAnagrafe("AU");
        bean.setCodAnagComunale(anag.getCodAnaCom());
        EntityManager em = Connection.getEntityManager();
        PaiDao paiDao = new PaiDao(em);
        Pai pai = paiDao.findLastPai(anag.getCodAna());
        if (pai != null) {
            bean.setUot(String.valueOf(pai.getIdParamUot().getDesParam()));
        }
        bean.setIban(anag.getIbanPagam());
        if (em.isOpen()) {
            em.close();
        }
        return bean;
    }

}
