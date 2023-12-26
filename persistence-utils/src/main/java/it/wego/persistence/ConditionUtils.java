package it.wego.persistence;

import it.wego.persistence.objects.Assignment;
import it.wego.persistence.objects.Condition;
import it.wego.persistence.objects.Order;
import it.wego.persistence.objects.Order.OrderDir;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public class ConditionUtils {

	private static Logger logger = LoggerFactory.getLogger(ConditionUtils.class);

	public static String createOrderString(Order... orders) {
		return createOrderString(orders);
	}

	public static String createOrderString(Iterable<Order> orders) {
		StringBuilder sb = new StringBuilder(" ORDER BY");
		boolean first = true;
		for (Order order : orders) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append(" ").append(order.getField()).append(" ").append(order.getDir().toString());
		}
		return sb.toString();
	}

//	public static List<Order> parseOrder(List<it.wego.extjs.beans.Order> orders, Map<String, String> fieldNames) {
//		List<Order> orderList = new ArrayList<Order>(orders.size());
//		for (it.wego.extjs.beans.Order order : orders) {
//			String field = fieldNames.get(order.getProperty());
//			if (field == null) {
//				field = fieldNames.get(order.getProperty().toUpperCase());
//			}
//			if (field == null) {
//				logger.warn("unknown order field : " + order.getProperty());
//				continue;
//			}
//			orderList.add(new Order(field, OrderDir.valueOf(order.getDirection().toUpperCase())));
//		}
//		return orderList;
//	}

	public static <T extends Object> TypedQuery<T> createQuery(EntityManager entityManager, Class<T> clazz, String queryStr, Collection<Condition> conditions, Collection<Assignment> assignments,List<Order> order, Integer limit, Integer offset) {
		Condition condition = (conditions == null || conditions.isEmpty()) ? null : (conditions.size() == 1 ? conditions.iterator().next() : ConditionBuilder.and(conditions));
		return createQuery(entityManager, clazz, queryStr, condition, assignments, order, limit, offset);
	}

	public static <T extends Object> TypedQuery<T> createQuery(EntityManager entityManager, Class<T> clazz, String queryStr, Condition condition, Collection<Assignment> assignments, List<Order> order, Integer limit, Integer offset) {
		StringBuilder stringBuilder = new StringBuilder(queryStr);
		if(assignments!=null&&!assignments.isEmpty()){
			List<String> list=new ArrayList<String>();
			for(Assignment assignment:assignments){
				list.add(assignment.toString());
			}
			stringBuilder.append(" SET ").append(StringUtils.join(list, " , "));
		}
		if (condition != null) {
			stringBuilder.append(" WHERE ").append(condition.toString());
		}
		if (order != null && !order.isEmpty()) {
			stringBuilder.append(createOrderString(order));
		}
		queryStr = stringBuilder.toString();
		logger.debug("buildt query : " + queryStr);
		TypedQuery<T> query = entityManager.createQuery(queryStr, clazz);
		if (limit != null) {
			query.setMaxResults(limit);
		}
		if (offset != null) {
			query.setFirstResult(offset);
		}
		if (condition != null) {
			condition.setParameter(query);
		}
		if(assignments!=null&&!assignments.isEmpty()){
			for(Assignment assignment:assignments){
				assignment.setParameter(query);
			}
		}
		return query;
	}
}
