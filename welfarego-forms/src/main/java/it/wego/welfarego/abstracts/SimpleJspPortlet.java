package it.wego.welfarego.abstracts;

import java.io.IOException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * rappresenta una portlet che redirige le richieste do{View,Edit,Help} su 
 * altrettanti handler jsp nomePortlet_{
 * @author aleph
 */
public class SimpleJspPortlet extends GenericPortlet {
	public static final String BASENAME_PROP_KEY="baseName";

	private String baseName;

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public SimpleJspPortlet(String baseName) {
		this.baseName = baseName;
	}
	
	public SimpleJspPortlet() { }
	
	@Override
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
		String configBaseName=config.getInitParameter(BASENAME_PROP_KEY);
		if(configBaseName!=null)
			setBaseName(configBaseName);
	}

	@Override
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		redirect(request, response, "view");
	}

	@Override
	public void doHelp(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		redirect(request, response, "help");
	}

	@Override
	public void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		redirect(request, response, "edit");
	}

	private void redirect(RenderRequest request, RenderResponse response, String action) throws PortletException, IOException {
		response.setContentType("text/html");
		PortletRequestDispatcher dispatcher =
				  getPortletContext().getRequestDispatcher("/" + baseName + "_" + action + ".jsp");
		dispatcher.include(request, response);
	}
}
