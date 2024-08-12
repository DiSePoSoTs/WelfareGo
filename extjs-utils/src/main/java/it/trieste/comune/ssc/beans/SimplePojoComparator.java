package it.trieste.comune.ssc.beans;

//import it.wego.conversions.StringConversion;
import java.lang.reflect.Method;
import java.util.Comparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * semplice Comparator che lavora tramite reflection, per l'uso con
 * l'ordinamento remoto per gli store extjs (Nota: funziona solo con campi che
 * sono oggetti ed implementano Comparable, non con tipi nativi, quindi se una
 * classe deve essere processata da questo oggetto, preferire ad es Integer a
 * int ecc.
 *
 * @author aleph
 */
@Deprecated
public class SimplePojoComparator implements Comparator {

    private static Logger logger = LoggerFactory.getLogger(SimplePojoComparator.class);
    private String field;
    private int dir;
    private Method getter, comparator;

    public SimplePojoComparator(Order order) {
//        this(StringConversion.toCamelCase(order.getProperty()), order.getDirection().equals(Order.DIR_ASC) ? true : false);
        this(order.getProperty(), order.getDirection().equals(Order.DIR_ASC) ? true : false);
    }

    public SimplePojoComparator(String field, boolean dir) {
        logger.debug("sorting by " + field + " " + (dir ? "ASC" : "DESC"));
        this.field = field;
        this.dir = dir ? 1 : -1;
    }

//    @Override
    public int compare(Object o1, Object o2) {
        try {
            if (getter == null) {
                getter = o1.getClass().getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
            }
            Object field1 = getter.invoke(o1), field2 = getter.invoke(o2);
            if (field1 == null || field2 == null) {
                return 0;
            }
            if (comparator == null) {
                comparator = field1.getClass().getMethod("compareTo", field1.getClass());
            }
            return dir * (Integer) comparator.invoke(field1, field2);
        } catch (Exception ex) {
            logger.warn("comparator error", ex);
            return 0;
        }
    }
}
