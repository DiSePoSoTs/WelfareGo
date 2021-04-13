package it.wego.persistence.objects;

/**
 *
 * @author aleph
 */
public class Order {

	private String field;
	private OrderDir dir = OrderDir.DESC;

	public OrderDir getDir() {
		return dir;
	}

	public void setDir(OrderDir dir) {
		this.dir = dir;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Order() {
	}

	public Order(String field, OrderDir dir) {
		this.field = field;
		this.dir = dir;
	}

	public static enum OrderDir {

		ASC, DESC
	}
}
