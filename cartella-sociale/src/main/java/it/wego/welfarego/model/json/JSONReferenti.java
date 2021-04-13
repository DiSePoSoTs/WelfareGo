package it.wego.welfarego.model.json;

import it.wego.welfarego.model.ReferenteBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONReferenti {

    private boolean success = true;
    private String message;
    private List<ReferenteBean> referenti;
    private String codAnaFamigliare;

    public JSONReferenti() {
    }

    public List<ReferenteBean> getReferenti() {
        return referenti;
    }

    public void setReferenti(List<ReferenteBean> referenti) {
        this.referenti = referenti;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCodAnaFamigliare() {
        return codAnaFamigliare;
    }

    public void setCodAnaFamigliare(String codAnaFamigliare) {
        this.codAnaFamigliare = codAnaFamigliare;
    }
}
