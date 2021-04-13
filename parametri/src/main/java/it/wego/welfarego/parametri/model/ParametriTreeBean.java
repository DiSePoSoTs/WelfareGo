/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model;

import java.util.List;

/**
 *
 * @author Boss
 */
public class ParametriTreeBean {

    String text;
    String pk;
    String tip_param;
    Boolean leaf;
    List<ParametriTreeBean> children;
    String qtip;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public List<ParametriTreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<ParametriTreeBean> children) {
        this.children = children;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTip_param() {
        return tip_param;
    }

    public void setTip_param(String tip_param) {
        this.tip_param = tip_param;
    }

    public String getQtip() {
        return qtip;
    }

    public void setQtip(String qtip) {
        this.qtip = qtip;
    }
    
    
    
}
