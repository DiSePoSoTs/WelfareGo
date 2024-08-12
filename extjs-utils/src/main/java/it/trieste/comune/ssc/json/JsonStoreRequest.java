package it.trieste.comune.ssc.json;

/**
 *
 * @author aleph
 */
public class JsonStoreRequest {
    private Integer page,start,limit;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
    
}
