package it.trieste.comune.ssc.json;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author aleph
 */
    public class JsonStoreResponse extends JsonResponse {

    private Collection data;
    private Integer total;

    public JsonStoreResponse(boolean success, String message) {
        super(success, message);
    }

    public JsonStoreResponse() {
    }

    public JsonStoreResponse(Collection data) {
        this.data = data;
    }

    public JsonStoreResponse(Collection data, Integer total) {
        this.data = data;
        this.total = total;
    }

    public Collection getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
