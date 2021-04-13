/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model;

/**
 *
 * @author giuseppe
 */
public class DatoSpecifico {

    private String codCampo;
    private String desCampo;
    private String flgObb;
    private String tipoCampo;
    private String valAmm;
    private String flgEdit;
    private String flgVis;
    private String valDef;
    private String regExpr;
    private String codCampoCsr;
    private String msgErrore;
    private String lunghezza;
    private String decimali;
    private String col;
    private String row;

    public DatoSpecifico() {
    }

    public String getCodCampo() {
        return codCampo;
    }

    public void setCodCampo(String codCampo) {
        this.codCampo = codCampo;
    }

    public String getCodCampoCsr() {
        return codCampoCsr;
    }

    public void setCodCampoCsr(String codCampoCsr) {
        this.codCampoCsr = codCampoCsr;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getDecimali() {
        return decimali;
    }

    public void setDecimali(String decimali) {
        this.decimali = decimali;
    }

    public String getDesCampo() {
        return desCampo;
    }

    public void setDesCampo(String desCampo) {
        this.desCampo = desCampo;
    }

    public String getFlgEdit() {
        return flgEdit;
    }

    public void setFlgEdit(String flgEdit) {
        this.flgEdit = flgEdit;
    }

    public String getFlgObb() {
        return flgObb;
    }

    public void setFlgObb(String flgObb) {
        this.flgObb = flgObb;
    }

    public String getFlgVis() {
        return flgVis;
    }

    public void setFlgVis(String flgVis) {
        this.flgVis = flgVis;
    }

    public String getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(String lunghezza) {
        this.lunghezza = lunghezza;
    }

    public String getMsgErrore() {
        return msgErrore;
    }

    public void setMsgErrore(String msgErrore) {
        this.msgErrore = msgErrore;
    }

    public String getRegExpr() {
        return regExpr;
    }

    public void setRegExpr(String regExpr) {
        this.regExpr = regExpr;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(String tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public String getValAmm() {
        return valAmm;
    }

    public void setValAmm(String valAmm) {
        this.valAmm = valAmm;
    }

    public String getValDef() {
        return valDef;
    }

    public void setValDef(String valDef) {
        this.valDef = valDef;
    }
}
