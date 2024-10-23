package it.wego.welfarego.commons.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.google.common.base.Preconditions;

import it.wego.welfarego.commons.listener.WelfaregoFormsContextListener;

import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.utils.WelfaregoUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRAbstractExporter;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOdsReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;




/**
 *
 * @author Mess
 */
public class ReportUtils {

	private static Logger logger = LogManager.getLogger(ReportUtils.class);
	public static final String PDF = "pdf";
	public static final String XLS = "xls";
	public static final String RTF = "rtf";
	public static final String HTML = "html";

	public static byte[] executeDetermineReport(String templateFileName, String outType, Map parametersMap)
			throws Exception {
		String templatePath = "determine" + File.separator + templateFileName;
		return executeReport(outType, parametersMap, templatePath);
	}

	public static byte[] executeListeAttesaReport(String templateFileName, String outType, Map parametersMap)
			throws Exception {
		String templatePath = "liste_attesa" + File.separator + templateFileName;
		return executeReport(outType, parametersMap, templatePath);
	}

	public static byte[] executeGenericReport(String templateFileName, String outType, Map parametersMap)
			throws Exception {
		String templatePath = "generici" + File.separator + templateFileName;
		return executeReport(outType, parametersMap, templatePath);
	}

	public static byte[] executeReport(String outType, Map parametersMap, String templateRelPath) throws Exception {
		File templateFile = getReportTemplateFile(templateRelPath,
				templateRelPath.replaceFirst("_[^_]+.jasper$", "_generic.jasper"));
		Preconditions.checkNotNull(templateFile, "report not found for path = {}", templateRelPath);
		String templateFileName = templateFile.getName(), templatePath = templateFile.getPath();
		logger.debug("processing template : " + templateFileName);

		InputStream reportInputStream = null;
		ByteArrayOutputStream reportOutputStream = null;
		EntityManager em = null;
		try {

			File reportFile = new File(templatePath);
			if (!reportFile.exists())
				throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");

			reportInputStream = new FileInputStream(templatePath);
			reportOutputStream = new ByteArrayOutputStream();

			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());

			em = Connection.getEntityManager();

			em.getTransaction().begin();
			java.sql.Connection connection = em.unwrap(java.sql.Connection.class);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap, connection);

			reportInputStream = new ByteArrayInputStream(reportOutputStream.toByteArray());
			reportOutputStream = new ByteArrayOutputStream();

			JRAbstractExporter exporter = null;

			if (outType.equals(HTML)) {
				exporter = new HtmlExporter();
				SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(reportOutputStream);
				exporterOutput.setImageHandler(new WebHtmlResourceHandler("/WelfaregoForms/css/images/"));

			}
			if (outType.equals(PDF)) {
				exporter = new JRPdfExporter();
			}
			if (outType.equals(RTF)) {
				exporter = new JRRtfExporter();
			}
			if (outType.equals(XLS)) {
				exporter = new JRXlsExporter();
				SimpleOdsReportConfiguration odsReportConfiguration = new SimpleOdsReportConfiguration();
				odsReportConfiguration.setOnePagePerSheet(true);
				odsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
				odsReportConfiguration.setDetectCellType(true);
				exporter.setConfiguration(odsReportConfiguration);
			}

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportOutputStream));
			exporter.exportReport();

			byte[] result = reportOutputStream.toByteArray();

			logger.debug("processed template : " + templateFileName);
			return result;

		} finally {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			if (reportInputStream != null) {
				reportInputStream.close();
			}
			if (reportOutputStream != null) {
				reportOutputStream.close();
			}
		}
	}

	public static String serializeReportArray() {

		StringBuilder sb = new StringBuilder();
		String templateDirPath = WelfaregoFormsContextListener.getToolsRoot() + File.separator + "WEB-INF"
				+ File.separator + "reports" + File.separator + "generici" + File.separator;
		File f = new File(templateDirPath);

		String[] reportList = f.list(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.endsWith(".jasper")) {
					return true;
				}
				return false;
			}
		});
		Arrays.sort(reportList);
		sb.append("[");
		for (int i = 0; i < reportList.length; i++) {
			sb.append("{report: '").append(reportList[i].substring(0, reportList[i].length() - 7)).append("'}");
			if (i < reportList.length - 1) {
				sb.append(", ");
			}
		}

		sb.append("]");
		return sb.toString();
	}

	public static List<String> serializeReportParametersArray(String reportName) throws Exception {
		List<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		String reportPath = WelfaregoFormsContextListener.getToolsRoot() + File.separator + "WEB-INF" + File.separator
				+ "reports" + File.separator + "generici" + File.separator + reportName + ".jasper";
		JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportPath);
		JRParameter[] parameters = jasperReport.getParameters();

		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].getName().toUpperCase().startsWith("PAR_")) {
				list.add(parameters[i].getName());
			}
		}
		return list;
	}

	private static final Map<Character, String> reportFileAttr;

	static {
		Map<Character, String> map = new HashMap<Character, String>();
		map.put(PaiIntervento.STATO_INTERVENTO_APERTO, "esecutivita");
		map.put(PaiIntervento.STATO_INTERVENTO_RIMANDATO, "esecutivita");
		map.put(PaiIntervento.STATO_INTERVENTO_ESECUTIVO, "variazione");
		map.put(PaiIntervento.STATO_INTERVENTO_CHIUSO, "chiusura");
		reportFileAttr = Collections.unmodifiableMap(map);
	}

	public static String getNomeFileReportDetermine(String codTipInt, char statoInt) {
		return "report_determine_" + reportFileAttr.get(statoInt) + "_" + codTipInt + ".jasper";
	}

	public static String getNomeFileReportDetermine(PaiIntervento paiIntervento) {
		return getNomeFileReportDetermine(paiIntervento.getPaiInterventoPK().getCodTipint(),
				paiIntervento.getStatoInt());
	}

	public static Collection<File> getReportTemplateBaseDirs() {
		final String REPORTS = "reports", WEBINF = "WEB-INF", CONFIG_PARAM = "it.wego.welfarego.templates.TemplateDir";
		File webappDir = new File(
				WelfaregoFormsContextListener.getToolsRoot() + File.separator + WEBINF + File.separator + REPORTS);
		Collection<File> res = new ArrayList<File>();
		String otherDirs = null;
		try {
			otherDirs = WelfaregoUtils.getConfig(CONFIG_PARAM);
		} catch (Exception ex) {
			logger.error("error while querying configs", ex);
		}
		if (otherDirs != null) {
			for (String dirName : otherDirs.split(",")) {
				res.add(new File(dirName));
			}
		}
		res.add(webappDir);
		Iterator<File> iterator = res.iterator();
		while (iterator.hasNext()) {
			File file = iterator.next();
			if (!file.isDirectory()) {
				logger.warn("unexisting directory for report template loading : " + file.getPath());
				iterator.remove();
			}
		}
		return res;
	}

	public static File getReportTemplateFile(String templatePath) {
		for (File baseDir : getReportTemplateBaseDirs()) {
			File templateFile = new File(baseDir, templatePath);
			if (templateFile.isFile()) {
				return templateFile;
			}
		}
		return null;
	}

	public static File getReportTemplateFile(String... templatePaths) {
		for (String templatePath : templatePaths) {
			File templateFile = getReportTemplateFile(templatePath);
			if (templateFile != null) {
				return templateFile;
			}
		}
		return null;
	}
}
