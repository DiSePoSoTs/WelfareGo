package it.wego.welfarego.abstracts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author aleph
 */
public class JavascriptNamespacerFilter implements Filter {

	private String namespaceParamKey = "ns", namespaceToken = "namespace", filterPath = "JavascriptNamespacerFilter";
	private Logger logger = LogManager.getLogger(this.getClass());
	private FilterConfig filterConfig;

	public void init(FilterConfig fc) throws ServletException {
		this.filterConfig = fc;
		String param = fc.getInitParameter("namespaceToken");
		if (param != null) {
			namespaceToken = param;
		}
		param = fc.getInitParameter("namespaceParamKey");
		if (param != null) {
			namespaceParamKey = param;
		}
		param = fc.getInitParameter("filterPath");
		if (param != null) {
			filterPath = param;
		}
		logger.info("initialized (param,token,path) : " + namespaceParamKey + " , " + namespaceToken + " , " + filterPath);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String nameSpace = request.getParameter(namespaceParamKey);
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURL().toString(), path = httpRequest.getRequestURI().replaceFirst(httpRequest.getContextPath(), "");
		logger.info("sono arrivato fin qui ciao chiara");
//		logger.debug("intercepring request : " + url);
		if (nameSpace == null) {
			if (url.matches(".*/" + filterPath + "/[^/]+/.*")) {
				nameSpace = url.replaceFirst(".*/" + filterPath + "/([^/]+)/.*", "$1");
				String newPath = path.replaceFirst("/" + filterPath + "/[^/]+/", "/");
//				newPath += "?" + namespaceParamKey + "=" + nameSpace;
				String query = httpRequest.getQueryString();
				if (query != null && query.length() != 0) {
					newPath += "?" + query;
				}
				request.setAttribute("AAAA", nameSpace);
//				logger.debug("redirecting " + url + " -> " + newPath);
//				logger.debug("filtering js (ns,path) : " + nameSpace + " , " + newPath);
				ByteResponseWrapper responseWrapper = new ByteResponseWrapper((HttpServletResponse) response);
				filterConfig.getServletContext().getRequestDispatcher(newPath).forward(request, responseWrapper);
				doNamespaceFilter((HttpServletResponse) response, responseWrapper, nameSpace);
				return;
			}
		}
		if (nameSpace != null) {
//			logger.debug("filtering js file : " + ((HttpServletRequest) request).getRequestURL().toString());
//			logger.debug("filtering js (ns,path) : " + nameSpace + " , " + path);
			ByteResponseWrapper responseWrapper = new ByteResponseWrapper((HttpServletResponse) response);
			chain.doFilter(request, responseWrapper);
//			byte[] data = responseWrapper.getBytes();
//			String dataStr = new String(data);
//			logger.debug("got data");

			doNamespaceFilter((HttpServletResponse) response, responseWrapper, nameSpace);
//			OutputStream out=response.getOutputStream();
//			//out.write(data);
//			out.write("\n//filtered by JavascriptNamespacerFilter".getBytes());
//			out.flush();
//			out.close();
//			response.flushBuffer()
//			dataStr = dataStr.replaceAll("([^a-zA-Z0-9])" + namespaceToken + "([^a-zA-Z0-9])", "$1" + nameSpace + "$2");
//			PrintWriter writer = response.getWriter();
//			writer.print(dataStr);
//			writer.print("\n// filtered by JavascriptNamespacerFilter\n");

//			logger.debug("printed data");
		} else {
			chain.doFilter(request, response);
		}
	}

	private void doNamespaceFilter(HttpServletResponse response, ByteResponseWrapper responseWrapper, String nameSpace) throws IOException {
//		logger.debug("filtering js with ns : " + nameSpace);
		byte[] data = responseWrapper.getBytes();
		String charEnc = responseWrapper.getCharacterEncoding();
		String dataStr = new String(data, charEnc);
		dataStr = dataStr.replaceAll("([^a-zA-Z0-9])" + namespaceToken + "([^a-zA-Z0-9])", "$1" + nameSpace + "$2");
		PrintWriter writer = response.getWriter();
		writer.print(dataStr);
		writer.print("\n// filtered by JavascriptNamespacerFilter\n");

//		logger.debug("printed data");
	}

	public void destroy() {
	}

	static class ByteResponseWrapper extends HttpServletResponseWrapper {

		private PrintWriter writer;
		private ByteOutputStream output;

		public byte[] getBytes() {
			writer.flush();
			return output.getBytes();
		}

		public ByteResponseWrapper(HttpServletResponse response) {
			super(response);
			output = new ByteOutputStream();
			writer = new PrintWriter(output);
		}

		@Override
		public PrintWriter getWriter() {
			return writer;
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return output;
		}

		@Override
		public void setContentLength(int len) {
		}
	}

	static class ByteOutputStream extends ServletOutputStream {

		private ByteArrayOutputStream bos = new ByteArrayOutputStream();

		@Override
		public void write(int b) throws IOException {
			bos.write(b);
		}

		public byte[] getBytes() {
			return bos.toByteArray();
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			// TODO Auto-generated method stub
		}
	}
}
