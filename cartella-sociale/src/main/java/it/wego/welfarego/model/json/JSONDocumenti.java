/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model.json;

import it.wego.welfarego.model.DocumentoBean;
import java.util.List;

/**
 *
 * @author giuseppe
 */
public class JSONDocumenti {

    private boolean success;
    private List<DocumentoBean> documenti;
    private int totalCount;

    public JSONDocumenti() {
    }

    public List<DocumentoBean> getDocumenti() {
        return documenti;
    }

    public void setDocumenti(List<DocumentoBean> documenti) {
        this.documenti = documenti;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
