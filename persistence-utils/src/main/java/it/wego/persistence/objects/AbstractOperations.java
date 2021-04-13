package it.wego.persistence.objects;

import java.util.Date;
import javax.persistence.Query;

/**
 *
 * @author aleph
 */
public class AbstractOperations {

    public static abstract class AbstractOperation implements Operation {

        private static final ThreadLocal<Long> idProvider = new ThreadLocal<Long>() {

            @Override
            protected Long initialValue() {
                return 0L;
            }
        };

        public void setParameter(Query query) {
        }
        private static final long MAX_ID = (long) Math.pow(32, 4);

        /**
         * genera un id univoco da usare come namespace per i parametri
         * impostati da questa condizione
         *
         * @return un id univoco da usare come namespace per i parametri
         * impostati da questa condizione
         */
        protected static String generateId() {
            Long id = idProvider.get();
            idProvider.set((id + 1) % MAX_ID);
            return Long.toString(id, 32);
        }
        private String id;

        public final String getId() {
            if (id == null) {
                id = generateId();
            }
            return id;
        }
    }

    public static abstract class AbstractFieldOperation extends AbstractOperation {

        private final String field, paramName;

        public AbstractFieldOperation(String field) {
            this.field = field;
            paramName = field.replaceAll("[^a-zA-Z0-9]+", "_") + "_" + getId();
        }

        public String getField() {
            return field;
        }

//        public void setField(String field) {
//            this.field = field;
//        }
//        public void setId(String id) {
//            this.id = id;
//        }
        public String getParameterName() {
            return paramName;
        }

        public String getParameterName(int index) {
            return paramName + "_i" + index;
        }
    }

    public static abstract class AbstractIntOperation extends AbstractFieldOperation {

        private Integer value;

        public AbstractIntOperation(String field, Integer value) {
            super(field);
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public void setParameter(Query query) {
            query.setParameter(getParameterName(), (int) getValue());
        }
    }

    public static abstract class AbstractCharOperation extends AbstractFieldOperation {

        private Character value;

        public AbstractCharOperation(String field, Character value) {
            super(field);
            this.value = value;
        }

        public Character getValue() {
            return value;
        }

        public void setValue(Character value) {
            this.value = value;
        }

        @Override
        public void setParameter(Query query) {
            query.setParameter(getParameterName(), (char) getValue());
        }
    }

    public static abstract class AbstractObjectOperation extends AbstractFieldOperation {

        private Object value;

        public AbstractObjectOperation(String field, Object value) {
            super(field);
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public void setParameter(Query query) {
            query.setParameter(getParameterName(), (Object) getValue());
        }
    }

    public static abstract class AbstractStringOperation extends AbstractFieldOperation {

        private String value;

        public AbstractStringOperation(String field, String value) {
            super(field);
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public void setParameter(Query query) {
            query.setParameter(getParameterName(), getValue());
        }
    }

    public static abstract class AbstractDateOperation extends AbstractFieldOperation {

        private Date value;

        public AbstractDateOperation(String field, Date value) {
            super(field);
            this.value = value;
        }

        public Date getValue() {
            return value;
        }

        public void setValue(Date value) {
            this.value = value;
        }

        @Override
        public void setParameter(Query query) {
            query.setParameter(getParameterName(), (Date) getValue());
        }
    }

    public static class StaticOperation extends AbstractOperation implements Condition, Assignment {

        private String operation;

        public StaticOperation(String operation) {
            this.operation = operation;
        }

        @Override
        public String toString() {
            return operation;
        }

        public void setString(String operation) {
            this.operation = operation;
        }
    }
}
