package it.wego.welfarego.commons.servlet;

import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.commons.utils.ReportUtils;
import java.util.List;

/**
 *
 * @author Muscas
 */
public class ReportForm extends AbstractForm implements AbstractForm.Loadable{

    @Override
    public Object load() throws Exception {
        String reportName = getParameter("report_name");
        List<String> reportParameter = ReportUtils.serializeReportParametersArray(reportName);
        return reportParameter;
    }
    
    
}
