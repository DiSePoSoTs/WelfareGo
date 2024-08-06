package it.trieste.comune.ssc.json;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Muscas
 * @deprecated use JsonBuilder
 */
@Deprecated
public class JsonMessage extends GsonObject {

	boolean success;
	String message;

	public JsonMessage() {
	}

	public JsonMessage(String message) {
		this(true, message);
	}

	public JsonMessage(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public JsonMessage(Throwable e) {
		String msg;
		while (e.getCause() != null && e instanceof InvocationTargetException) {
			e = e.getCause();
		}
		if (e instanceof IllegalArgumentException) {
			msg = e.getMessage();
		} else {
			msg = e.toString();
		}
		this.success = false;
		this.message = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
