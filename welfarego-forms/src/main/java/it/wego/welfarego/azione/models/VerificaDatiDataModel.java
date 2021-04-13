package it.wego.welfarego.azione.models;

/**
 *
 * @author aleph
 */
public class VerificaDatiDataModel extends VerificaImpegniGenericDataModel{

	private Esito esito;
	private TipoDetermina tipoDetermina;

	public static enum Esito { approvato, respinto }

	public static enum TipoDetermina { singola, multipla }


	public Esito getEsito() {
		return esito;
	}

	public void setEsito(Esito esito) {
		this.esito = esito;
	}


	public TipoDetermina getTipoDetermina() {
		return tipoDetermina;
	}

	public void setTipoDetermina(TipoDetermina tipoDetermina) {
		this.tipoDetermina = tipoDetermina;
	}

	
}
