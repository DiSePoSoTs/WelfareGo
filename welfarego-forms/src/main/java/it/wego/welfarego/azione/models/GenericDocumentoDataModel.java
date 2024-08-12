package it.wego.welfarego.azione.models;

import it.trieste.comune.ssc.json.GsonObject;
import java.util.Date;

/**
 *
 * @author aleph
 */
public class GenericDocumentoDataModel extends GsonObject{
	
		private String nomeUtenteVerDati, nPai, cognomeUt, assistSoc, descrizione, intervento, documento;
		private Date dataApertPai;

	public String getAssistSoc() {
		return assistSoc;
	}

	public void setAssistSoc(String assistSoc) {
		this.assistSoc = assistSoc;
	}

	public String getCognomeUt() {
		return cognomeUt;
	}

	public void setCognomeUt(String cognomeUt) {
		this.cognomeUt = cognomeUt;
	}

	public Date getDataApertPai() {
		return dataApertPai;
	}

	public void setDataApertPai(Date dataApertPai) {
		this.dataApertPai = dataApertPai;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getIntervento() {
		return intervento;
	}

	public void setIntervento(String intervento) {
		this.intervento = intervento;
	}

	public String getnPai() {
		return nPai;
	}

	public void setnPai(String nPai) {
		this.nPai = nPai;
	}

	public String getNomeUtenteVerDati() {
		return nomeUtenteVerDati;
	}

	public void setNomeUtenteVerDati(String nomeUtenteVerDati) {
		this.nomeUtenteVerDati = nomeUtenteVerDati;
	}
		
		
}
