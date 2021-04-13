/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model;

import it.wego.welfarego.persistence.entities.MapDatiSpecTipint;

/**
 *
 * @author Michele
 */
public class MapDatiBean {

    String cod_campo;
    String des_campo;
    String flg_obb;
    String tipo_campo;
    String val_amm;
    String flg_edit;
    String flg_vis;
   
    String val_def;
    String reg_expr;
    String cod_campo_csr;
    String msg_errore;
    String lunghezza;
    String decimali;
    String row_campo;
    String col_campo;
    String cod_tipint;

    public String getCod_campo() {
        return cod_campo;
    }

    public void setCod_campo(String cod_campo) {
        this.cod_campo = cod_campo;
    }

    public String getCod_campo_csr() {
        return cod_campo_csr;
    }

    public void setCod_campo_csr(String cod_campo_csr) {
        this.cod_campo_csr = cod_campo_csr;
    }

    public String getDecimali() {
        return decimali;
    }

    public void setDecimali(String decimali) {
        this.decimali = decimali;
    }

    public String getDes_campo() {
        return des_campo;
    }

    public void setDes_campo(String des_campo) {
        this.des_campo = des_campo;
    }

    public String getFlg_edit() {
        return flg_edit;
    }

    public void setFlg_edit(String flg_edit) {
        this.flg_edit = flg_edit;
    }

    public String getFlg_obb() {
        return flg_obb;
    }

    public void setFlg_obb(String flg_obb) {
        this.flg_obb = flg_obb;
    }

    public String getFlg_vis() {
        return flg_vis;
    }

    public void setFlg_vis(String flg_vis) {
        this.flg_vis = flg_vis;
    }

    public String getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(String lunghezza) {
        this.lunghezza = lunghezza;
    }

    public String getMsg_errore() {
        return msg_errore;
    }

    public void setMsg_errore(String msg_errore) {
        this.msg_errore = msg_errore;
    }

    public String getReg_expr() {
        return reg_expr;
    }

    public void setReg_expr(String reg_expr) {
        this.reg_expr = reg_expr;
    }

    public String getTipo_campo() {
        return tipo_campo;
    }

    public void setTipo_campo(String tipo_campo) {
        this.tipo_campo = tipo_campo;
    }

    public String getVal_amm() {
        return val_amm;
    }

    public void setVal_amm(String val_amm) {
        this.val_amm = val_amm;
    }

    public String getVal_def() {
        return val_def;
    }

    public void setVal_def(String val_def) {
        this.val_def = val_def;
    }

    public String getCol_campo() {
        return col_campo;
    }

    public void setCol_campo(String col_campo) {
        this.col_campo = col_campo;
    }

    public String getRow_campo() {
        return row_campo;
    }

    public void setRow_campo(String row_campo) {
        this.row_campo = row_campo;
    }

    public String getCod_tipint() {
        return cod_tipint;
    }

    public void setCod_tipint(String cod_tipint) {
        this.cod_tipint = cod_tipint;
    }
    
    

    public MapDatiBean serialize(MapDatiSpecTipint ds) {

        MapDatiBean bean = new MapDatiBean();

        bean.setCod_campo(ds.getDatiSpecifici().getCodCampo());
        bean.setCol_campo(Short.toString(ds.getColCampo()));
        bean.setRow_campo(Short.toString(ds.getRowCampo()));

        if (ds.getDatiSpecifici().getCodCampoCsr() != null) {
            bean.setCod_campo_csr(ds.getDatiSpecifici().getCodCampoCsr());
        }

        if (ds.getDatiSpecifici().getDecimali() != null) {
            bean.setDecimali(Short.toString(ds.getDatiSpecifici().getDecimali()));
        }

        bean.setDes_campo(ds.getDatiSpecifici().getDesCampo());
        bean.setTipo_campo(Character.toString(ds.getDatiSpecifici().getTipoCampo()));
        
        bean.setFlg_edit(Character.toString(ds.getDatiSpecifici().getFlgEdit()));
        bean.setFlg_obb(Character.toString(ds.getDatiSpecifici().getFlgObb()));
        bean.setFlg_vis(Character.toString(ds.getDatiSpecifici().getFlgVis()));
        bean.setLunghezza(Short.toString(ds.getDatiSpecifici().getLunghezza()));
        
        if(ds.getDatiSpecifici().getMsgErrore()!=null) {
            bean.setMsg_errore(ds.getDatiSpecifici().getMsgErrore());
        }
            
        if(ds.getDatiSpecifici().getRegExpr()!=null) {
            bean.setReg_expr(ds.getDatiSpecifici().getRegExpr());    
        }
        
        if(ds.getDatiSpecifici().getValAmm() != null) {
            bean.setVal_amm(ds.getDatiSpecifici().getValAmm());    
        }
        
        if(ds.getDatiSpecifici().getValDef()!=null ) {
            bean.setVal_def(ds.getDatiSpecifici().getValDef());    
        }        

        bean.setCod_tipint(ds.getTipologiaIntervento().getCodTipint());
        
        return bean;

    }

	
}
