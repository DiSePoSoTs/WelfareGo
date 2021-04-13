package it.wego.welfarego.model;

/**
 *
 * @author giuseppe
 */
public class CivicoBean {

    private String desCivico;
    private String codCivico;

    public CivicoBean(String desCivico, String codCivico) {
        this.desCivico = desCivico;
        this.codCivico = codCivico;
    }

    public CivicoBean() {
    }

    public String getCodCivico() {
        return codCivico;
    }

    public void setCodCivico(String codCivico) {
        this.codCivico = codCivico;
    }

    public String getDesCivico() {
        return desCivico;
    }

    public void setDesCivico(String desCivico) {
        this.desCivico = desCivico;
    }
}
