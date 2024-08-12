package it.wego.welfarego.azione.models;

import it.trieste.comune.ssc.json.GsonObject;
import java.util.Date;

/**
 *
 * @author aleph
 */
public class ProtocolloDataModel  extends InterventoDataModel{
	private String numero;
	private Date dataProtoc;

	public Date getDataProtoc() {
		return dataProtoc;
	}

	public void setDataProtoc(Date dataProtoc) {
		this.dataProtoc = dataProtoc;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	
}
