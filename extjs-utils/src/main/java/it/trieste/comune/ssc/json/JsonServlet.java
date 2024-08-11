package it.trieste.comune.ssc.json;

import java.lang.reflect.Method;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected static final String MULTIPART_BASE64 = null;

	protected static final Object SKIP_RESPONSE = null;
	
	protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
		return null;
	}
	
	protected String getAction() {
		return null;
	}
	
	protected @Nullable String getParameter(String string) {

		return null;
	}
	

	protected Object getLogger() {
		return null;
	}

}
