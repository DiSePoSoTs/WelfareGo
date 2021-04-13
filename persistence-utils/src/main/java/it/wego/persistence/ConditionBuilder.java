package it.wego.persistence;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.wego.persistence.objects.AbstractOperations.StaticOperation;
import it.wego.persistence.objects.Condition;
import it.wego.persistence.objects.SimpleConditions;
import it.wego.persistence.objects.SimpleConditions.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 *
 * @author aleph
 */
public class ConditionBuilder {

    public static Condition or(Iterable<Condition> conditions) {
        return new OrCondition(conditions);
    }

    public static Condition and(Iterable<Condition> conditions) {
        return new AndCondition(conditions);
    }

    public static Condition or(Condition... conditions) {
        return or(Arrays.asList(conditions));
    }

    public static Condition and(Condition... conditions) {
        return and(Arrays.asList(conditions));
    }

    public static Condition not(Condition condition) {
        return new NegationCondition(condition);
    }

    public static Condition isNull(String field) {
        return new IsNullCondition(field);
    }

    public static Condition isNotNull(String field) {
        return not(isNull(field));
    }

    public static Condition isIn(String field, Character... values) {
        return isIn(field, Arrays.asList(values));
    }

    public static Condition isIn(String field, Object... values) {
        return isIn(field, Arrays.asList(values));
    }

    public static Condition isIn(String field, Collection values) {
        if (values.isEmpty()) {
            return SimpleConditions.FALSE;
        } else {
            if (values.iterator().next() instanceof Character) {
                return new IsInCharCondition(field, values);
            } else if (values.iterator().next() instanceof Integer) {
                return new IsInIntCondition(field, values);
            } else {
                return new IsInStringCondition(field, values);
            }
        }
    }

    public static Condition like(String field, String value) {
        return new LikeCondition(field, value, false, false,false);
    }

    public static Condition elike(String field, String value) {
        return new LikeCondition(field, value, false, true,false);
    }

    public static Condition eilike(String field, String value) {
        return new LikeCondition(field, value, true, true,false);
    }
    
    public static Condition beginsWith(String field,String value){
    	return new LikeCondition(field, value, true, false,true);
    }

    public static Condition ilike(String field, String value) {
        return new LikeCondition(field, value, true, false,false);
    }

    public static Condition equals(String field, String value) {
        return new EqualsStringCondition(field, value);
    }

    public static Condition before(String field, Date value) {
        return new BeforeDateCondition(field, value);
    }
    
    public static Condition equals(String field, Date value) {
        return new EqualsDateCondition(field, value);
    }


    public static Condition after(String field, Date value) {
        return new AfterDateCondition(field, value);
    }

    public static Condition between(String field, Date from, Date to) {
        return and(before(field, to), after(field, from));
    }

    public static Condition between(String field, int from, int to) {
        return and(lesserOrEqualThan(field, to), greatherOrEqualThan(field, from));
    }

    public static Condition equals(String field, int value) {
        return new EqualsIntCondition(field, value);
    }

    public static Condition greaterThan(String field, int value) {
        return new GreaterIntCondition(field, value);
    }

    public static Condition lesserThan(String field, int value) {
        return new LesserIntCondition(field, value);
    }

    public static Condition greatherOrEqualThan(String field, int value) {
        return new GreaterOrEqualIntCondition(field, value);
    }

    public static Condition lesserOrEqualThan(String field, int value) {
        return new LesserOrEqualIntCondition(field, value);
    }

    public static Condition equals(String field, char value) {
        return new EqualsCharCondition(field, value);
    }

    public static Condition equals(String field, Object value) {
        return new EqualsObjectCondition(field, value);
    }

    public static Condition isEqual(String field, String value) {
        return equals(field, value);
    }

    public static Condition isEqual(String field, int value) {
        return equals(field, value);
    }

    public static Condition isEqual(String field, char value) {
        return equals(field, value);
    }

    public static Condition isEqual(String field, Object value) {
        return equals(field, value);
    }
    
    public static Condition isEqual(String field, Date value) {
        return equals(field, value);
    }

    public static Condition fromString(String condition) {
        return new StaticOperation(condition);
    }

    public static Condition isTrue(String condition) {
        return fromString(condition);
    }

    public static Condition isTrue(String condition, String parameterName, String parameter) {
        return new CustomStringCondition(condition, parameterName, parameter);
    }

    public static Condition isTrue(String condition, String parameterName, int parameter) {
        return new CustomIntCondition(condition, parameterName, parameter);
    }

    public static Condition isTrue(String condition, String parameter) {
        String oldName = condition.replaceFirst(".*(:[a-zA-Z]+).*", "$1"), newName = ":parameter";
        return new CustomStringCondition(condition.replaceAll(oldName, newName), newName, parameter);
    }

    public static Condition isTrue(String condition, int parameter) {
        String oldName = condition.replaceFirst(".*(:[a-zA-Z]+).*", "$1"), newName = ":parameter";
        return new CustomIntCondition(condition.replaceAll(oldName, newName), newName, parameter);
    }

    public static Condition fromBoolean(boolean bool) {
        return bool ? SimpleConditions.TRUE : SimpleConditions.FALSE;
    }

    /**
     * or( ilike( field1 , value ) ... ilike( fieldn , value )
     *
     * @param value
     * @param fields
     * @return
     */
    public static Condition smartMatch(final String value, String... fields) {
        Validate.notEmpty(value, "value must not be empty");
        Validate.notEmpty(fields, "fields must not be empty");
        return or(Lists.newArrayList(Lists.transform(Lists.newArrayList(fields), new Function<String, Condition>() {
            
        	public Condition apply(String field) {
                return ilike(field, value);
            }
        })));
    }

    /**
     * and( or( ilike( field1 , value1 ) ... ilike( fieldn , value1 ) ) ... or(
     * ilike( field1 , valuen ) ... ilike( fieldn , valuen ) )
     *
     * @param values
     * @param fields
     * @return
     */
    public static Condition smartMatch(Iterable<String> values, final String... fields) {
        Validate.isTrue(!Iterables.isEmpty(values), "values must not be empty");
        return and(Lists.newArrayList(Iterables.transform(values, new Function<String, Condition>() {

            public Condition apply(String value) {
                return smartMatch(value, fields);
            }
        })));
    }

    /**
     * split first argument around witespaces, then apply
     * smartMatch(Iterable<String> values, final String... fields)
     *
     * @param toSplit
     * @param fields
     * @return
     */
    public static Condition smartMatchSplit(String toSplit, String... fields) {
        return smartMatch(Lists.newArrayList(StringUtils.split(toSplit)), fields);
    }
}
