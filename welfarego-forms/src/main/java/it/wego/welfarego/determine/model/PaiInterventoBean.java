package it.wego.welfarego.determine.model;

import it.wego.conversions.StringConversion;
import it.wego.welfarego.persistence.entities.PaiIntervento;

/**
 *
 * @author giuseppe
 */
public class PaiInterventoBean {

    private String codPai, codTipInt, cntTipInt, nome, cognome, intervento, dataRichiesta, isee, stato;
    private boolean approvato, respinto;

    public PaiInterventoBean() {
    }

    public String getCntTipInt() {
        return cntTipInt;
    }

    public void setCntTipInt(String cntTipInt) {
        this.cntTipInt = cntTipInt;
    }

    public String getCodPai() {
        return codPai;
    }

    public void setCodPai(String codPai) {
        this.codPai = codPai;
    }

    public String getCodTipInt() {
        return codTipInt;
    }

    public void setCodTipInt(String codTipInt) {
        this.codTipInt = codTipInt;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDataRichiesta() {
        return dataRichiesta;
    }

    public void setDataRichiesta(String dataRichiesta) {
        this.dataRichiesta = dataRichiesta;
    }

    public String getIntervento() {
        return intervento;
    }

    public void setIntervento(String intervento) {
        this.intervento = intervento;
    }

    public String getIsee() {
        return isee;
    }

    public void setIsee(String isee) {
        this.isee = isee;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isApprovato() {
        return approvato;
    }

    public void setApprovato(boolean approvato) {
        this.approvato = approvato;
    }

    public boolean isRespinto() {
        return respinto;
    }

    public void setRespinto(boolean respinto) {
        this.respinto = respinto;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
	 
    public static PaiInterventoBean fromPaiIntervento(PaiIntervento paiIntervento) throws Exception {
		 return fromPaiIntervento(paiIntervento,PaiInterventoBean.class);
	 }

    public static <T extends PaiInterventoBean> T fromPaiIntervento(PaiIntervento paiIntervento,Class<T> clazz) throws Exception {
        T paiInterventoBean = clazz.newInstance();

        paiInterventoBean.setCodPai(paiIntervento.getPai().getCodPai().toString());
        paiInterventoBean.setCodTipInt(paiIntervento.getPaiInterventoPK().getCodTipint());
        paiInterventoBean.setCntTipInt(String.valueOf(paiIntervento.getPaiInterventoPK().getCntTipint()));
        paiInterventoBean.setIntervento(paiIntervento.getTipologiaIntervento().getDesTipint());
        paiInterventoBean.setCognome(paiIntervento.getPai().getCodAna().getAnagrafeSoc().getCognome());
        paiInterventoBean.setNome(paiIntervento.getPai().getCodAna().getAnagrafeSoc().getNome());
        paiInterventoBean.setDataRichiesta(StringConversion.dateToItString(paiIntervento.getPai().getDtApePai()));
        paiInterventoBean.setApprovato(false);
        paiInterventoBean.setRespinto(false);
        paiInterventoBean.setIsee("");
        if (paiIntervento.getPai().getIsee() != null) {
            paiInterventoBean.setIsee(paiIntervento.getPai().getIsee().toString());
        }
//        paiInterventoBean.setStato("");
//        if (paiIntervento.getPai().getCodAna().getAnagrafeSoc().getIdParamStatoFis() != null) {
//            paiInterventoBean.setStato(paiIntervento.getPai().getCodAna().getAnagrafeSoc().getIdParamStatoFis().getDesParam());
				paiInterventoBean.setStato(paiIntervento.getStatoInt()+"");
//        }


        return paiInterventoBean;
    }
}
