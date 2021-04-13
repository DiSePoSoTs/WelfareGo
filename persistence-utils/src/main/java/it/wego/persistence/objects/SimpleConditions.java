package it.wego.persistence.objects;

import it.wego.persistence.objects.AbstractConditions.*;
import it.wego.persistence.objects.AbstractOperations.AbstractCharOperation;
import it.wego.persistence.objects.AbstractOperations.AbstractDateOperation;
import it.wego.persistence.objects.AbstractOperations.AbstractFieldOperation;
import it.wego.persistence.objects.AbstractOperations.AbstractIntOperation;
import it.wego.persistence.objects.AbstractOperations.AbstractObjectOperation;
import it.wego.persistence.objects.AbstractOperations.AbstractStringOperation;
import it.wego.persistence.objects.AbstractOperations.StaticOperation;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Query;

/**
 *
 * @author aleph
 */
public class SimpleConditions {

    public static class AndCondition extends AbstractLogicalCondition implements Condition {

        public AndCondition(Iterable<Condition> conditions) {
            super(conditions, "AND");
        }
    }

    public static class OrCondition extends AbstractLogicalCondition implements Condition {

        public OrCondition(Iterable<Condition> conditions) {
            super(conditions, "OR");
        }
    }

    public static class NegationCondition extends AbstractUnaryCondition implements Condition {

        public NegationCondition(Condition condition) {
            super(condition, "NOT");
        }
    }

    public static class LikeCondition extends AbstractStringOperation implements Condition {

        private final boolean caseInSensitive, exactMatch,startsWith;

        public LikeCondition(String field, String value, boolean caseInSensitive, boolean exactMatch,boolean startsWith) {
            super(field, value);
            this.caseInSensitive = caseInSensitive;
            this.exactMatch = exactMatch;
            this.startsWith=startsWith;
        }

//		public boolean isExactMatch() {
//			return exactMatch;
//		}
//		public void setExactMatch(boolean exactMatch) {
//			this.exactMatch = exactMatch;
//		}
//		public boolean isCaseInSensitive() {
//			return caseInSensitive;
//		}
//		public void setCaseInSensitive(boolean caseInSensitive) {
//			this.caseInSensitive = caseInSensitive;
//		}
        @Override
        public String toString() {
            if (caseInSensitive) {
                return "UPPER(" + getField() + ") LIKE :" + getParameterName();
            } else {
                return getField() + " LIKE :" + getParameterName();
            }
        }

        @Override
        public void setParameter(Query query) {
            String str = (caseInSensitive ? getValue().toUpperCase() : getValue());
            if(startsWith){
            //	str="like translate('"+str+"%', 'àâćčéèêìîòôšùûÿ', 'aacceeeiioouuy')";
             str = str + "%";
            }
            else{
            if (!exactMatch) {
                str = "%" + str + "%";
            }
            
            	
            }
            query.setParameter(getParameterName(), str);
        }
    }

    public static class IsNullCondition extends AbstractFieldOperation implements Condition {

        public IsNullCondition(String field) {
            super(field);
        }

        @Override
        public String toString() {
            return getField() + " IS NULL";
        }
    }

    public static class EqualsStringCondition extends AbstractStringOperation implements Condition {

        public EqualsStringCondition(String field, String value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " = :" + getParameterName();
        }
    }

    public static class CustomStringCondition extends AbstractStringOperation implements Condition {

        private final String condition;

        public CustomStringCondition(String condition, String field, String value) {
            super(field, value);
            this.condition = condition;
        }

        @Override
        public String toString() {
            return condition;
        }
    }

    public static class CustomIntCondition extends AbstractIntOperation implements Condition {

        private final String condition;

        public CustomIntCondition(String condition, String field, Integer value) {
            super(field, value);
            this.condition = condition;
        }

        @Override
        public String toString() {
            return condition;
        }
    }

    public static class BeforeDateCondition extends AbstractDateOperation implements Condition {

        public BeforeDateCondition(String field, Date value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " < :" + getParameterName();
        }
    }
    
    public static class EqualsDateCondition extends AbstractDateOperation implements Condition {

        public EqualsDateCondition(String field, Date value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " = :" + getParameterName();
        }
    }

    public static class AfterDateCondition extends AbstractDateOperation implements Condition {

        public AfterDateCondition(String field, Date value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " > :" + getParameterName();
        }
    }

    public static class EqualsIntCondition extends AbstractIntOperation implements Condition {

        public EqualsIntCondition(String field, Integer value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " = :" + getParameterName();
        }
    }

    public static class GreaterIntCondition extends AbstractIntOperation implements Condition {

        public GreaterIntCondition(String field, Integer value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " > :" + getParameterName();
        }
    }

    public static class LesserIntCondition extends AbstractIntOperation implements Condition {

        public LesserIntCondition(String field, Integer value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " < :" + getParameterName();
        }
    }

    public static class GreaterOrEqualIntCondition extends AbstractIntOperation implements Condition {

        public GreaterOrEqualIntCondition(String field, Integer value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " >= :" + getParameterName();
        }
    }

    public static class LesserOrEqualIntCondition extends AbstractIntOperation implements Condition {

        public LesserOrEqualIntCondition(String field, Integer value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " <= :" + getParameterName();
        }
    }

    public static class EqualsCharCondition extends AbstractCharOperation implements Condition {

        public EqualsCharCondition(String field, Character value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " = :" + getParameterName();
        }
    }

    public static class EqualsObjectCondition extends AbstractObjectOperation implements Condition {

        public EqualsObjectCondition(String field, Object value) {
            super(field, value);
        }

        @Override
        public String toString() {
            return getField() + " = :" + getParameterName();
        }
    }

    public static class IsInStringCondition extends AbstractIsInCondition {

        public IsInStringCondition(String field, Collection value) {
            super(field, value);
        }

        @Override
        public void setParameter(Query query, String paramName, Object parameter) {
            query.setParameter(paramName, parameter.toString());
        }
    }

    public static class IsInCharCondition extends AbstractIsInCondition {

        public IsInCharCondition(String field, Collection<Character> value) {
            super(field, value);
        }

        @Override
        public void setParameter(Query query, String paramName, Object parameter) {
            query.setParameter(paramName, (char) ((Character) parameter));
        }
    }

    public static class IsInIntCondition extends AbstractIsInCondition {

        public IsInIntCondition(String field, Collection value) {
            super(field, value);
        }

        @Override
        public void setParameter(Query query, String paramName, Object parameter) {
            query.setParameter(paramName, (int) Integer.valueOf(parameter.toString()));
        }
    }

    public static class IsInObjectCondition extends AbstractIsInCondition {

        public IsInObjectCondition(String field, Collection value) {
            super(field, value);
        }

        @Override
        public void setParameter(Query query, String paramName, Object parameter) {
            query.setParameter(paramName, (Object) parameter);
        }
    }
    public static final Condition FALSE = new StaticOperation("1!=1"), TRUE = new StaticOperation("1=1");
}
