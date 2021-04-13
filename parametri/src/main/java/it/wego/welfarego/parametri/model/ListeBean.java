/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model;

import it.wego.welfarego.persistence.entities.ListaAttesa;

/**
 *
 * @author Boss
 */
public class ListeBean {

    String cod_lista_att;
    String des_lista_att;
    String flg_tipint;
    String flg_cog;
    String flg_nom;
    String flg_cod_fisc;
    String flg_isee;
    String flg_dt_nasc;
    String flg_dt_dom;
    String flg_bina;
    String flg_as;
    String flg_uot;
    String flg_dist_san;
    String flg_ref;
    String flg_num_figli;

    public String getCod_lista_att() {
        return cod_lista_att;
    }

    public void setCod_lista_att(String cod_lista_att) {
        this.cod_lista_att = cod_lista_att;
    }

    public String getDes_lista_att() {
        return des_lista_att;
    }

    public void setDes_lista_att(String des_lista_att) {
        this.des_lista_att = des_lista_att;
    }

    public String getFlg_as() {
        return flg_as;
    }

    public void setFlg_as(String flg_as) {
        this.flg_as = flg_as;
    }

    public String getFlg_bina() {
        return flg_bina;
    }

    public void setFlg_bina(String flg_bina) {
        this.flg_bina = flg_bina;
    }

    public String getFlg_cod_fisc() {
        return flg_cod_fisc;
    }

    public void setFlg_cod_fisc(String flg_cod_fisc) {
        this.flg_cod_fisc = flg_cod_fisc;
    }

    public String getFlg_cog() {
        return flg_cog;
    }

    public void setFlg_cog(String flg_cog) {
        this.flg_cog = flg_cog;
    }

    public String getFlg_dist_san() {
        return flg_dist_san;
    }

    public void setFlg_dist_san(String flg_dist_san) {
        this.flg_dist_san = flg_dist_san;
    }

    public String getFlg_dt_dom() {
        return flg_dt_dom;
    }

    public void setFlg_dt_dom(String flg_dt_dom) {
        this.flg_dt_dom = flg_dt_dom;
    }

    public String getFlg_dt_nasc() {
        return flg_dt_nasc;
    }

    public void setFlg_dt_nasc(String flg_dt_nasc) {
        this.flg_dt_nasc = flg_dt_nasc;
    }

    public String getFlg_isee() {
        return flg_isee;
    }

    public void setFlg_isee(String flg_isee) {
        this.flg_isee = flg_isee;
    }

    public String getFlg_nom() {
        return flg_nom;
    }

    public void setFlg_nom(String flg_nom) {
        this.flg_nom = flg_nom;
    }

    public String getFlg_num_figli() {
        return flg_num_figli;
    }

    public void setFlg_num_figli(String flg_num_figli) {
        this.flg_num_figli = flg_num_figli;
    }

    public String getFlg_ref() {
        return flg_ref;
    }

    public void setFlg_ref(String flg_ref) {
        this.flg_ref = flg_ref;
    }

    public String getFlg_tipint() {
        return flg_tipint;
    }

    public void setFlg_tipint(String flg_tipint) {
        this.flg_tipint = flg_tipint;
    }

    public String getFlg_uot() {
        return flg_uot;
    }

    public void setFlg_uot(String flg_uot) {
        this.flg_uot = flg_uot;
    }

    public ListeBean serialize(ListaAttesa lis) {

        ListeBean bean = new ListeBean();
        
        bean.setCod_lista_att( lis.getCodListaAtt().toString() );
        bean.setDes_lista_att( lis.getDesListaAtt() );
        bean.setFlg_as( Character.toString(lis.getFlgAs()) );
        bean.setFlg_bina( Character.toString(lis.getFlgBina()) );
        bean.setFlg_cod_fisc( Character.toString(lis.getFlgCodFisc()) );
        bean.setFlg_cog( Character.toString(lis.getFlgCog()) );
        bean.setFlg_dist_san( Character.toString(lis.getFlgDistSan()) );
        bean.setFlg_dt_dom( Character.toString(lis.getFlgDtDom()) );
        bean.setFlg_dt_nasc( Character.toString(lis.getFlgDtNasc()) );
        bean.setFlg_isee( Character.toString(lis.getFlgIsee()) );
        bean.setFlg_nom( Character.toString(lis.getFlgNom()) );
        bean.setFlg_num_figli( Character.toString(lis.getFlgNumFigli()) );
        bean.setFlg_ref( Character.toString(lis.getFlgRef()) );
        bean.setFlg_tipint( Character.toString(lis.getFlgTipint()) );
        bean.setFlg_uot( Character.toString(lis.getFlgUot()) );
        
        return bean;
        
    }
}
