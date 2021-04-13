/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.parametri.model;

import it.wego.welfarego.persistence.entities.Template;

/**
 *
 * @author Boss
 */
public class TemplateBean {
    
    String cod_tmpl;
    String des_tmpl;
    String nome_file;
    String clob_tmpl;
    String download_url;

    public String getClob_tmpl() {
        return clob_tmpl;
    }

    public void setClob_tmpl(String clob_tmpl) {
        this.clob_tmpl = clob_tmpl;
    }

    public String getCod_tmpl() {
        return cod_tmpl;
    }

    public void setCod_tmpl(String cod_tmpl) {
        this.cod_tmpl = cod_tmpl;
    }

    public String getDes_tmpl() {
        return des_tmpl;
    }

    public void setDes_tmpl(String des_tmpl) {
        this.des_tmpl = des_tmpl;
    }

    public String getNome_file() {
        return nome_file;
    }

    public void setNome_file(String nome_file) {
        this.nome_file = nome_file;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    
    
    
    public TemplateBean serialize(Template tpl) {

        TemplateBean bean = new TemplateBean();

        bean.setCod_tmpl( tpl.getCodTmpl().toString() );
        bean.setDes_tmpl( tpl.getDesTmpl() );       
        bean.setNome_file( tpl.getNomeFile() );
        // bean.setClob_tmpl( tpl.getClobTmpl() ); 

        String link = "<a href='/Parametri/DownloadTemplate?cod_tmpl=" + tpl.getCodTmpl() + "'>" + tpl.getNomeFile() + "</a>";
        bean.setDownload_url(link);

        return bean;
    }
    
}
