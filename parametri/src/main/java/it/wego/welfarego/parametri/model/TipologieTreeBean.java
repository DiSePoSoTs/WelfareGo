/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model;

import java.util.List;

/**
 *
 * @author Michele
 */
public class TipologieTreeBean {

    String text;
    String pk;
    Boolean leaf;
    List<TipologieTreeBean> children;
    String qtip;
    // poi di seguito i dati del form
    String cod_tipint;
    String des_tipint;
    String cod_grp_tipint;
    String des_grp_tipint;

    public List<TipologieTreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<TipologieTreeBean> children) {
        this.children = children;
    }

    public String getCod_grp_tipint() {
        return cod_grp_tipint;
    }

    public void setCod_grp_tipint(String cod_grp_tipint) {
        this.cod_grp_tipint = cod_grp_tipint;
    }

    public String getCod_tipint() {
        return cod_tipint;
    }

    public void setCod_tipint(String cod_tipint) {
        this.cod_tipint = cod_tipint;
    }

    public String getDes_grp_tipint() {
        return des_grp_tipint;
    }

    public void setDes_grp_tipint(String des_grp_tipint) {
        this.des_grp_tipint = des_grp_tipint;
    }

    public String getDes_tipint() {
        return des_tipint;
    }

    public void setDes_tipint(String des_tipint) {
        this.des_tipint = des_tipint;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getQtip() {
        return qtip;
    }

    public void setQtip(String qtip) {
        this.qtip = qtip;
    }
}