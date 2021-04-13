package it.wego.welfarego.azione.forms;

import it.wego.json.GsonObject;
import it.wego.json.JsonForm;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.persistence.entities.Utenti;
import java.util.Date;

/**
 *
 * @author aleph
 */
public abstract class GenericProtocolloFormListener extends AbstractForm implements AbstractForm.Loadable{

	
	@Override
	public Object load() throws Exception {
		getLogger().debug("loading data");
		ProtocolloDataModel res = loadData();
		getLogger().debug("data loaded");
		return new JsonForm(res);
	}
	
	public ProtocolloDataModel loadData() throws Exception {
		UniqueTasklist task = getTask();
		Pai pai = task.getCodPai();
		AnagrafeSoc anagrafeSoc = pai.getCodAna().getAnagrafeSoc();
		Utenti utente = pai.getCodUteAs();
		return new ProtocolloDataModel(
				  anagrafeSoc.getNome(),
				  anagrafeSoc.getCognome(),
				  utente.getCognome() + " " + utente.getNome(),
				  "",
				  pai.getDtApePai(),
				  new Date());
	}

	public static class ProtocolloDataModel extends GsonObject {

		private String nomeUtente, cognomeUtente, assistSoc, numero;
		private Date dataApertPai, dataProtoc;

		public ProtocolloDataModel() {
		}

		public ProtocolloDataModel(String nomeUtente, String cognomeUtente, String assistSoc, String numero, Date dataApertPai, Date dataProtoc) {
			this.nomeUtente = nomeUtente;
			this.cognomeUtente = cognomeUtente;
			this.assistSoc = assistSoc;
			this.numero = numero;
			this.dataApertPai = dataApertPai;
			this.dataProtoc = dataProtoc;
		}

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

		public Date getDataProtoc() {
			return dataProtoc;
		}

		public void setDataProtoc(Date dataProtoc) {
			this.dataProtoc = dataProtoc;
		}

		public String getNomeUtente() {
			return nomeUtente;
		}

		public void setNomeUtente(String nomeUtente) {
			this.nomeUtente = nomeUtente;
		}

		public String getNumero() {
			return numero;
		}

		public void setNumero(String numero) {
			this.numero = numero;
		}
	}
}
