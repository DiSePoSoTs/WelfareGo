package it.trieste.comune.ssc.beans;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** 
 * semplice Comparator che lavora tramite reflection, per l'uso con l'ordinamento
 * remoto per gli store extjs
 * @author aleph
 */
@Deprecated
public class MultipleFieldPojoComparator implements Comparator {
	private List<Comparator> comparators;

	public MultipleFieldPojoComparator(Order... orderList) {
		comparators = new ArrayList<Comparator>(orderList.length);
		for (Order order : orderList) {
			comparators.add(new SimplePojoComparator(order));
		}
	}

	public int compare(Object o1, Object o2) {
		for (Comparator comparator : comparators) {
			int res = comparator.compare(o1, o2);
			if (res != 0) {
				return res;
			}
		}
		return 0;
	}
}
