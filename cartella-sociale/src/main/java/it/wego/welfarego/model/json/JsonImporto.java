/**
 * 
 */
package it.wego.welfarego.model.json;

import java.math.BigDecimal;

/**
 * @author fabio
 *
 */
public class JsonImporto {
	private boolean success;
	private BigDecimal importo;
	private BigDecimal importoUnitario;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public BigDecimal getImportoUnitario() {
		return importoUnitario;
	}
	public void setImportoUnitario(BigDecimal importoUnitario) {
		this.importoUnitario = importoUnitario;
	}
	

}
