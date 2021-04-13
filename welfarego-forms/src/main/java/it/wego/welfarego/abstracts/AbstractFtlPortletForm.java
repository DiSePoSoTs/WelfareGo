package it.wego.welfarego.abstracts;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateExceptionHandler;
import it.wego.json.JsonMessage;
import it.wego.welfarego.commons.listener.WelfaregoFormsContextListener;
import it.wego.welfarego.commons.utils.ToolsUtils;
import it.wego.welfarego.persistence.constants.Configurations;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.portlet.RenderRequest;

import org.apache.logging.log4j.LogManager;


/**
 *
 * @author aleph
 */
public abstract class AbstractFtlPortletForm extends AbstractForm implements AbstractForm.Viewable {

    public static String ERROR_TEMPLATE = "error.html";
    private String templateName;

    public AbstractFtlPortletForm(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public void doView(Writer writer) throws Exception {
        Map<String, Object> ftlData = new HashMap<String, Object>();
        try {
            Template template = getFtlConfiguration().getTemplate(templateName);
            loadVariablesData(ftlData);
            ftlData = loadViewData(ftlData);
            template.process(ftlData, writer);
        } catch (Exception e) {
            getLogger().error("error while rendering portlet", e);
            Template template = getFtlConfiguration().getTemplate(ERROR_TEMPLATE);
            ftlData.put("error", new JsonMessage(e).getMessage());
            template.process(ftlData, writer);
        }
    }

    public void loadVariablesData(Map<String, Object> ftlData) throws Exception {
        ftlData.put("ns", "ns_" + UUID.randomUUID().toString().toLowerCase().replaceAll("[^a-z0-9]+", "").substring(0, 4));
        ftlData.put("gns", "wfg");
        ftlData.put("nsFilterPath", ToolsUtils.getConfig(ToolsUtils.PROPERTY_KEY_CONTEXT_PATH) + "/" + "JavascriptNamespacerFilter");
        ftlData.put("contextPath", ToolsUtils.getConfig(ToolsUtils.PROPERTY_KEY_CONTEXT_PATH));
        ftlData.put("cacheDisabled", ToolsUtils.getConfig(Configurations.PROPERTY_KEY_DISABLE_EXTJS_CACHE, Boolean.TRUE.toString()));
        ftlData.put("portletNamespace", getAttribute(GenericFormPortlet.PORTLET_NAMESPACE));
        if (getUtente() != null) {
            ftlData.put("userInfo", getUserInfoDataModel());
        }
    }
    private static final Configuration ftlConfiguration;

    static {
        ftlConfiguration = new Configuration(Configuration.VERSION_2_3_28);
        try {
        	ftlConfiguration.setDefaultEncoding("UTF-8");
        	ftlConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        	DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        	owb.setForceLegacyNonListCollections(false);
        	owb.setDefaultDateType(TemplateDateModel.DATETIME);
        	ftlConfiguration.setObjectWrapper(owb.build());
        	File file = new File(WelfaregoFormsContextListener.getServletContext().getRealPath("/WEB-INF/templates"));
            ftlConfiguration.setDirectoryForTemplateLoading(file);
        } catch (IOException ex) {
            LogManager.getLogger(AbstractFtlPortletForm.class).error("", ex);
        }
    }

    public static Configuration getFtlConfiguration() {
        return ftlConfiguration;
    }

    public RenderRequest getRequest() {
        return (RenderRequest) getAttribute(GenericFormPortlet.PORTLET_REQUEST);
    }

    public abstract Map<String, Object> loadViewData(Map<String, Object> ftlData) throws Exception;
}
