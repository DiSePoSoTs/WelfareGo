/**
 * 
 */
package it.wego.welfarego.commons.model;

import java.util.List;

/**
 * @author Fabio Bonaccorso 
 *
 */
public class ReportTreeBean {
	String reportName;
	Boolean leaf;
	List<ReportTreeBean> children;
	
	
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	public List<ReportTreeBean> getChildren() {
		return children;
	}
	public void setChildren(List<ReportTreeBean> children) {
		this.children = children;
	}
	

}
