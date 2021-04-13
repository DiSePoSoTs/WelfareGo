/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model;

import com.google.common.base.Function;
import it.wego.welfarego.persistence.entities.DatiSpecifici;

/**
 *
 * @author Michele
 */
public class DatiBean {

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

    public DatiBean serialize(DatiSpecifici ds) {
        return valueOf(ds);
    }

    public static DatiBean valueOf(DatiSpecifici ds) {

        DatiBean bean = new DatiBean();

        bean.setCod_campo(ds.getCodCampo());

        if (ds.getCodCampoCsr() != null) {
            bean.setCod_campo_csr(ds.getCodCampoCsr());
        }

        if (ds.getDecimali() != null) {
            bean.setDecimali(Short.toString(ds.getDecimali()));
        }

        bean.setDes_campo(ds.getDesCampo());
        bean.setFlg_edit(Character.toString(ds.getFlgEdit()));
        bean.setFlg_obb(Character.toString(ds.getFlgObb()));
        bean.setFlg_vis(Character.toString(ds.getFlgVis()));
        bean.setLunghezza(Short.toString(ds.getLunghezza()));
        bean.setTipo_campo(Character.toString(ds.getTipoCampo()));

        if (ds.getMsgErrore() != null) {
            bean.setMsg_errore(ds.getMsgErrore());
        }

        if (ds.getRegExpr() != null) {
            bean.setReg_expr(ds.getRegExpr());
        }

        if (ds.getValAmm() != null) {
            bean.setVal_amm(ds.getValAmm());
        }

        if (ds.getValDef() != null) {
            bean.setVal_def(ds.getValDef());
        }

        return bean;
    }

    public static Function<DatiSpecifici, DatiBean> getDatiSpecificiToDatiBeanFunc() {
        return DatiSpecificiToDatiBeanFunc.INSTANCE;
    }

    private static enum DatiSpecificiToDatiBeanFunc implements com.google.common.base.Function<DatiSpecifici, DatiBean> {

        INSTANCE;

        public DatiBean apply(DatiSpecifici input) {
            return DatiBean.valueOf(input);
        }
    }

	
}
