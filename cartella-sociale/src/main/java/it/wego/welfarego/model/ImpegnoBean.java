package it.wego.welfarego.model;

import java.util.List;

/**
 *
 * @author giuseppe
 */
public class ImpegnoBean {

    private List<List<OrarioBean>> ore;
    private List<GiornoBean> giorni;

    public ImpegnoBean() {
    }

    public List<GiornoBean> getGiorni() {
        return giorni;
    }

    public void setGiorni(List<GiornoBean> giorni) {
        this.giorni = giorni;
    }

    public List<List<OrarioBean>> getOre() {
        return ore;
    }

    public void setOre(List<List<OrarioBean>> ore) {
        this.ore = ore;
    }

    public ImpegnoBean serialize() {
        ImpegnoBean impegnoBean = new ImpegnoBean();


        return impegnoBean;
    }
}
