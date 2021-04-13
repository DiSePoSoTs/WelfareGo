/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model;

import it.wego.welfarego.persistence.entities.BudgetTipInterventoUot;

/**
 *
 * @author Boss
 */
public class BudgetUotBean {

    String cod_tipint;
    String cod_anno;
    String cod_impe;
    String id_param_uot;
    String bdg_disp_eur;
    String bdg_disp_ore;
    String des_param_uot;

    public String getDes_param_uot() {
        return des_param_uot;
    }

    public void setDes_param_uot(String des_param_uot) {
        this.des_param_uot = des_param_uot;
    }

    public String getBdg_disp_eur() {
        return bdg_disp_eur;
    }

    public void setBdg_disp_eur(String bdg_disp_eur) {
        this.bdg_disp_eur = bdg_disp_eur;
    }

    public String getCod_anno() {
        return cod_anno;
    }

    public void setCod_anno(String cod_anno) {
        this.cod_anno = cod_anno;
    }

    public String getCod_impe() {
        return cod_impe;
    }

    public void setCod_impe(String cod_impe) {
        this.cod_impe = cod_impe;
    }

    public String getCod_tipint() {
        return cod_tipint;
    }

    public void setCod_tipint(String cod_tipint) {
        this.cod_tipint = cod_tipint;
    }

    public String getBdg_disp_ore() {
        return bdg_disp_ore;
    }

    public void setBdg_disp_ore(String dbg_disp_ore) {
        this.bdg_disp_ore = dbg_disp_ore;
    }

    public String getId_param_uot() {
        return id_param_uot;
    }

    public void setId_param_uot(String id_param_uot) {
        this.id_param_uot = id_param_uot;
    }

    public void serialize(BudgetTipInterventoUot b) {

        this.setCod_tipint(b.getBudgetTipInterventoUotPK().getCodTipint());
        
        this.setCod_anno( String.valueOf(b.getBudgetTipInterventoUotPK().getCodAnno()) );
        
        this.setCod_impe( String.valueOf(b.getBudgetTipInterventoUotPK().getCodImpe()) );
        
        this.setId_param_uot( String.valueOf(b.getParametriIndataUot().getIdParamIndata()) );
        
        this.setDes_param_uot( b.getParametriIndataUot().getDesParam() );
        
        if(b.getBdgDispEur() != null)
        {
            this.setBdg_disp_eur( b.getBdgDispEur().toString() );    
        }
        
        if(b.getBdgDispOre() != null)
        {
            this.setBdg_disp_ore( b.getBdgDispOre().toString() );    
        }
        
    }
}
