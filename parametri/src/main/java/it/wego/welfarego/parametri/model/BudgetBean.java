/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model;

import it.wego.conversions.StringConversion;
import it.wego.welfarego.persistence.entities.BudgetTipIntervento;

/**
 *
 * @author Michele
 */
public class BudgetBean {

    String cod_tipint;
    String cod_anno;
    String cod_impe;
    String cod_conto;
    String cod_sconto;
    String cod_cap;
    String num_dx;
    String dt_dx;
    String bdg_disp_eur;
    String bdg_disp_ore;
    String annoSpesa;

    public String getBdg_disp_eur() {
        return bdg_disp_eur;
    }

    public void setBdg_disp_eur(String bdg_disp_eur) {
        this.bdg_disp_eur = bdg_disp_eur;
    }

    public String getBdg_disp_ore() {
        return bdg_disp_ore;
    }

    public void setBdg_disp_ore(String bdg_disp_ore) {
        this.bdg_disp_ore = bdg_disp_ore;
    }

    public String getCod_anno() {
        return cod_anno;
    }

    public void setCod_anno(String cod_anno) {
        this.cod_anno = cod_anno;
    }

    public String getCod_cap() {
        return cod_cap;
    }

    public void setCod_cap(String cod_cap) {
        this.cod_cap = cod_cap;
    }

    public String getCod_conto() {
        return cod_conto;
    }

    public void setCod_conto(String cod_conto) {
        this.cod_conto = cod_conto;
    }

    public String getCod_impe() {
        return cod_impe;
    }

    public void setCod_impe(String cod_impe) {
        this.cod_impe = cod_impe;
    }

    public String getCod_sconto() {
        return cod_sconto;
    }

    public void setCod_sconto(String cod_sconto) {
        this.cod_sconto = cod_sconto;
    }

    public String getCod_tipint() {
        return cod_tipint;
    }

    public void setCod_tipint(String cod_tipint) {
        this.cod_tipint = cod_tipint;
    }

    public String getDt_dx() {
        return dt_dx;
    }

    public void setDt_dx(String dt_dx) {
        this.dt_dx = dt_dx;
    }

    public String getNum_dx() {
        return num_dx;
    }

    public void setNum_dx(String num_dx) {
        this.num_dx = num_dx;
    }

    public BudgetBean serialize(BudgetTipIntervento b) {

        BudgetBean bud = new BudgetBean();

        bud.setCod_tipint(b.getBudgetTipInterventoPK().getCodTipint());
        bud.setCod_anno(Short.toString(b.getBudgetTipInterventoPK().getCodAnno()));
        bud.setCod_impe(b.getBudgetTipInterventoPK().getCodImpe());

        bud.setCod_cap(Integer.toString(b.getCodCap()));
        bud.setCod_conto(Integer.toString(b.getCodConto()));
        bud.setCod_sconto(Integer.toString(b.getCodSconto()));
        bud.setDt_dx(StringConversion.dateToItString(b.getDtDx()));
        bud.setNum_dx(Integer.toString(b.getNumDx()));
        bud.setAnnoSpesa(Short.toString(b.getAnnoErogazione()));

        try {
            bud.setBdg_disp_eur(b.getBdgDispEur().toString());
        } catch (NullPointerException e) {
            bud.setBdg_disp_eur("0");
        }

        try {
            bud.setBdg_disp_ore(b.getBdgDispOre().toString());
        } catch (NullPointerException e) {
            bud.setBdg_disp_ore("0");
        }


        return bud;
    }

	public String getAnnoSpesa() {
		return annoSpesa;
	}

	public void setAnnoSpesa(String annoSpesa) {
		this.annoSpesa = annoSpesa;
	}
}