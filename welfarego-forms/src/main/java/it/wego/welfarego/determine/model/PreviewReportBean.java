package it.wego.welfarego.determine.model;

import java.util.List;

/**
 *
 * @author Muscas
 */
public class PreviewReportBean {
    private String tipIntervento;
    private String tipoReport;
	private List<String> eventi;
	private String proroghe;
	private String mesi;
	private String al;

	public List<String> getEventi() {
		return eventi;
	}

	public void setEventi(List<String> eventi) {
		this.eventi = eventi;
	}

	public String getTipoReport() {
		return tipoReport;
	}

	public void setTipoReport(String tipoReport) {
		this.tipoReport = tipoReport;
	}

	public String getTipIntervento() {
		return tipIntervento;
	}

	public void setTipIntervento(String tipIntervento) {
		this.tipIntervento = tipIntervento;
	}

	public String getProroghe() {
		return proroghe;
	}

	public void setProroghe(String proroghe) {
		this.proroghe = proroghe;
	}

	public String getMesi() {
		return mesi;
	}

	public void setMesi(String mesi) {
		this.mesi = mesi;
	}

	public String getAl() {
		return al;
	}

	public void setAl(String al) {
		this.al = al;
	}
}
