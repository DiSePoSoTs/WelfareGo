/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.serializer;

import it.wego.conversions.StringConversion;
import it.wego.welfarego.model.ReferenteBean;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;

/**
 *
 * @author giuseppe
 */
public class ReferentiSerializer {

    public static ReferenteBean serializeAnagrafeSoc(AnagrafeSoc anagrafe, int codAna, int codAnaFam) {
        ReferenteBean referente = new ReferenteBean();
        if (anagrafe.getLuogoResidenza().getCivico() != null) {
            referente.setCivicoResidenza(anagrafe.getLuogoResidenza().getCivico().getDesCiv());
            referente.setCivicoResidenzaDes(anagrafe.getLuogoResidenza().getCivico().getDesCiv());
        }
        referente.setCodAnaFamigliare(String.valueOf(codAnaFam));
        referente.setCodAnag(String.valueOf(codAna));
        referente.setCodiceFiscale(anagrafe.getCodFisc());
        referente.setCognome(anagrafe.getCognome());
        if (anagrafe.getLuogoNascita().getComune() != null) {
            referente.setComuneNascita(anagrafe.getLuogoNascita().getComune().getDesCom());
            referente.setComuneNascitaDes(anagrafe.getLuogoNascita().getComune().getDesCom());
        } else {
            referente.setComuneNascita(anagrafe.getLuogoNascita().getComuneStr());
            referente.setComuneNascitaDes(anagrafe.getLuogoNascita().getComuneStr());
        }
        if (anagrafe.getLuogoResidenza().getComune() != null) {
            referente.setComuneResidenza(anagrafe.getLuogoResidenza().getComune().getDesCom());
            referente.setComuneResidenzaDes(anagrafe.getLuogoResidenza().getComune().getDesCom());
        } else {
            referente.setComuneResidenza(anagrafe.getLuogoResidenza().getComuneStr());
            referente.setComuneResidenzaDes(anagrafe.getLuogoResidenza().getComuneStr());
        }

        referente.setDataMorte(StringConversion.dateToItString(anagrafe.getDtMorte()));
        referente.setDataNascita(StringConversion.dateToItString(anagrafe.getDtNasc()));
        if (anagrafe.getCodStatoNaz() != null) {
            referente.setNazionalita(String.valueOf(anagrafe.getCodStatoNaz().getIdParam().getIdParam()));
        }

        referente.setNome(anagrafe.getNome());
        if (anagrafe.getLuogoResidenza().getProvincia() != null) {
            referente.setProvinciaResidenza(anagrafe.getLuogoResidenza().getProvincia().getDesProv());
            referente.setProvinciaResidenzaDes(anagrafe.getLuogoResidenza().getProvincia().getDesProv());
        } else {
            referente.setProvinciaResidenza(anagrafe.getLuogoNascita().getProvinciaStr());
            referente.setProvinciaResidenzaDes(anagrafe.getLuogoNascita().getProvinciaStr());
        }
        referente.setSesso(String.valueOf(anagrafe.getFlgSex()));
        if (anagrafe.getIdParamStatoCiv() != null) {
            referente.setStatoCivile(String.valueOf(anagrafe.getIdParamStatoCiv().getIdParamIndata()));
        }
        if (anagrafe.getLuogoNascita().getStato() != null) {
            referente.setStatoNascita(anagrafe.getLuogoNascita().getStato().getDesStato());
        }
        if (anagrafe.getLuogoResidenza().getStato() != null) {
            referente.setStatoResidenza(anagrafe.getLuogoResidenza().getStato().getDesStato());
        }

        if (anagrafe.getLuogoResidenza().getVia() != null) {
            referente.setViaResidenza(anagrafe.getLuogoResidenza().getVia().getDesVia());
            referente.setViaResidenzaDes(anagrafe.getLuogoResidenza().getVia().getDesVia());
        } else {
            referente.setViaResidenza(anagrafe.getLuogoResidenza().getViaStr());
            referente.setViaResidenzaDes(anagrafe.getLuogoResidenza().getViaStr());
        }
        return referente;
    }

    public static ReferenteBean serializeVistaAnagrafe(VistaAnagrafe f) {
        ReferenteBean b = new ReferenteBean();
        b.setNome(f.getNome());
        b.setCognome(f.getCognome());
        if (f.getSesso().equals("1")) {
            b.setSesso("M");
        } else {
            b.setSesso("F");
        }
        b.setStatoCivile(f.getStatoCivile());
        b.setCodiceFiscale(f.getCodiceFiscale());
        b.setDataNascita(StringConversion.dateToItString(f.getDataNascita()));
        b.setStatoNascita(null);
        b.setComuneNascita(f.getLuogoNascita());
        b.setNazionalita(f.getCittadinanza());
        b.setStatoResidenza(null);
        b.setProvinciaResidenza(null);
        b.setComuneResidenza(null);
        b.setViaResidenza(null);
        b.setCivicoResidenza(null);
        b.setDataMorte(StringConversion.dateToItString(f.getDataMorte()));
        b.setCodAnaComunale(String.valueOf(f.getNumeroIndividuale()));
        return b;
    }
}
