package it.wego.welfarego.abstracts;

import it.wego.extjs.beans.Order;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 * rappresenta un generico componente lato server per la gestione delle
 * richieste di uno store Extjs (load, update)
 *
 * @author aleph
 */
public abstract class AbstractStoreListener extends AbstractForm implements AbstractForm.Loadable, AbstractForm.Updatable {

    public static final String PARAM_RAWDATA_KEY = "rawData";

    public AbstractStoreListener() {
        super();
    }

    @Deprecated
    public AbstractStoreListener(Map<String, String> parameters) {
        super(parameters);
    }

    public AbstractStoreListener(AbstractForm parent) {
        super(parent);
    }

    @Override
    public Object load() throws Exception {
        String limitStr = getParameter("limit");
        String pageStr = getParameter("page");
        String startStr = getParameter("start");
        String orderStr = getParameter("sort");

        int limit = limitStr != null ? Integer.parseInt(limitStr) : 100,
                page = pageStr != null ? Integer.parseInt(pageStr) : 100,
                start = startStr != null ? Integer.parseInt(startStr) : 0;
        List<Order> order = orderStr != null ? Order.deserializeList(getParameter("sort")) : Collections.EMPTY_LIST;
        return load(start, limit, page, order);
    }

    @Override
    public Object update() throws Exception {
        return update(getParameter(PARAM_RAWDATA_KEY));
    }

    public abstract Object load(int start, int limit, int page, List<Order> order) throws Exception;

    public Object update(String data) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
