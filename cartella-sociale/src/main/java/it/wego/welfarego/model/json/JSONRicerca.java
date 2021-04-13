package it.wego.welfarego.model.json;

import it.wego.extjs.json.JsonStoreResponse;
import java.util.Collection;

/**
 *
 * @author giuseppe
 */
public class JSONRicerca {

    private boolean success;
    private int totalCount;
    private Collection risultati;

    public JSONRicerca() {
    }

    public JSONRicerca(boolean success, int totalCount, Collection risultati) {
        this.success = success;
        this.totalCount = totalCount;
        this.risultati = risultati;
    }

    public Collection getRisultati() {
        return risultati;
    }

    public void setRisultati(Collection risultati) {
        this.risultati = risultati;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTocalCount() {
        return totalCount;
    }

    public void setTocalCount(int tocalCount) {
        this.totalCount = tocalCount;
    }

    public static JSONRicerca fromJsonStoreResponse(JsonStoreResponse storeResponse) {
        return new JSONRicerca(storeResponse.getSuccess(), storeResponse.getTotal(), storeResponse.getData());
    }
}
