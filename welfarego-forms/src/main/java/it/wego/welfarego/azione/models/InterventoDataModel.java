package it.wego.welfarego.azione.models;

import it.wego.json.GsonObject;
import java.util.Date;

/**
 *
 * @author aleph
 */
public class InterventoDataModel extends GsonObject {

	private String nomeUtente, cognomeUtente, assistSoc;
	private Date dataApertPai;

	public String getAssistSoc() {
		return assistSoc;
	}

	public void setAssistSoc(String assistSoc) {
		this.assistSoc = assistSoc;
	}

	public String getCognomeUtente() {
		return cognomeUtente;
	}

	public void setCognomeUtente(String cognomeUtente) {
		this.cognomeUtente = cognomeUtente;
	}

	public Date getDataApertPai() {
		return dataApertPai;
	}

	public void setDataApertPai(Date dataApertPai) {
		this.dataApertPai = dataApertPai;
	}

	public String getNomeUtente() {
		return nomeUtente;
	}

	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}
}