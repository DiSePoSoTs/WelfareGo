package it.wego.persistence.objects;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import it.wego.persistence.objects.AbstractOperations.*;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author aleph
 */
public class AbstractConditions {

    public static abstract class AbstractLogicalCondition extends AbstractOperation implements Condition {

        private final Iterable<Condition> conditions;
        private final String operation;

        public AbstractLogicalCondition(Iterable<Condition> conditions, String operation) {
            this.conditions = Iterables.unmodifiableIterable(conditions);
            this.operation = operation;
        }

        @Override
        public String toString() {
            return StringUtils.join(Iterables.transform(conditions, new Function<Condition, String>() {

                public String apply(Condition condition) {
                    return "( " + condition.toString() + " )";
                }
            }).iterator(), " " + operation + " ");
        }

        @Override
        public void setParameter(Query query) {
            for (Condition condition : conditions) {
                condition.setParameter(query);
            }
        }
    }

    public static abstract class AbstractUnaryCondition extends AbstractOperation implements Condition {

        private final Condition condition;
        private final String operation;

        public AbstractUnaryCondition(Condition condition, String operation) {
            this.condition = condition;
            this.operation = operation;
        }

        @Override
        public void setParameter(Query query) {
            condition.setParameter(query);
        }

        @Override
        public String toString() {
            return operation + " ( " + condition.toString() + " )";
        }
    }

    public static abstract class AbstractIsInCondition extends AbstractFieldOperation implements Condition {

        private final Iterable value;

        public AbstractIsInCondition(String field, Iterable value) {
            super(field);
            this.value = Iterables.unmodifiableIterable(value);
        }

        @Override
        public String toString() {
            if (Iterables.isEmpty(value)) {
                return SimpleConditions.FALSE.toString();
            }
            return getField() + " IN (" + StringUtils.join(Iterables.transform(value, new Function<Object, String>() {

                int i = 0;

                public String apply(Object input) {
                    return ":" + getParameterName(i++);
                }
            }).iterator(), ",") + ")";
        }

        @Override
        public void setParameter(Query query) {
            int i = 0;
            for (Object obj : value) {
                setParameter(query, getParameterName(i++), obj);
            }
        }

        public abstract void setParameter(Query query, String paramName, Object parameter);
    }
}
