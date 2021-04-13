package it.wego.welfarego.tasklist.model;

/**
 *
 * @author giuseppe
 */
public class GiornoBean {

    private String tipo, desc;

    public GiornoBean() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public GiornoBean serialize() {
        GiornoBean giornoBean = new GiornoBean();


        return giornoBean;
    }
}
