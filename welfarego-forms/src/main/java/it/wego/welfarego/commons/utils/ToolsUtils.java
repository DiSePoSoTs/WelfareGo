package it.wego.welfarego.commons.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.wego.welfarego.commons.listener.WelfaregoFormsContextListener;
import it.wego.welfarego.utils.WelfaregoUtils;


/**
 *
 * @author Mess
 */
public class ToolsUtils extends WelfaregoUtils{
	private static Logger logger = LogManager.getLogger(ToolsUtils.class);

	public static String getClassRootPath() {
		return WelfaregoFormsContextListener.getToolsClassesRoot();
	}
	
}
