package it.wego.welfarego.dto;

import java.math.BigDecimal;

public class CostiInterventoMultiAnno {

	private BigDecimal costoPrimoAnno = null;
	private BigDecimal costoSecondoAnno = null;
	
	
	public CostiInterventoMultiAnno(BigDecimal costoPrimoAnno, BigDecimal costoSecondoAnno) {
		this.costoPrimoAnno = costoPrimoAnno;
		this.costoSecondoAnno = costoSecondoAnno;
	}
	
	
	public BigDecimal getCostoPrimoAnno() {
		return costoPrimoAnno;
	}
	public void setCostoPrimoAnno(BigDecimal costoPrimoAnno) {
		this.costoPrimoAnno = costoPrimoAnno;
	}
	public BigDecimal getCostoSecondoAnno() {
		return costoSecondoAnno;
	}
	public void setCostoSecondoAnno(BigDecimal costoSecondoAnno) {
		this.costoSecondoAnno = costoSecondoAnno;
	}
	
	
}
