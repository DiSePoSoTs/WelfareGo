/**
 * 
 */
package it.wego.welfarego.lettere.servlet;

import java.io.File;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import it.wego.extjs.servlet.JsonServlet;


/**
 * @author fabio Bonaccorso 
 *
 */
public class ScaricaZipServlet extends JsonServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see it.wego.extjs.servlet.AbstractJsonServlet#handleJsonRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, it.wego.extjs.servlet.AbstractJsonServlet.Method)
	 */
	@Override
	protected Object handleJsonRequest(HttpServletRequest request,
			HttpServletResponse response, Method method) throws Exception {
		 String fileName = getParameter("scaricaFile");
		    File daScaricare = new File("/tmp/"+fileName);
	    if(daScaricare.exists()){
	   
	     response.setContentType("application/vnd.oasis.opendocument.text");
	     response.addHeader("Content-Disposition", "attachment; filename=" + daScaricare.getName());
	     response.setContentLength((int) daScaricare.length());
	     OutputStream out = response.getOutputStream();
	     FileUtils.copyFile(daScaricare, out);
	     out.flush();
	     out.close();
	     getLogger().debug("lunghezza file da scaricare" +daScaricare.length());
	    	            
        
	   
	    
		
	}
		return new Object();

}
}	
