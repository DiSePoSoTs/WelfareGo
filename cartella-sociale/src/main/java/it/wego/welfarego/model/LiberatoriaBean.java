/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.model;

import it.wego.welfarego.persistence.entities.Liberatoria;
import java.util.Date;

/**
 *
 * @author DOTCOM
 */
public class LiberatoriaBean
{
	String associazione;
	String utente;
	Date dataFirma;

	public LiberatoriaBean(Liberatoria liberatoria)
	{
		this.associazione = liberatoria.getAssociazione().getNome();
		this.utente = liberatoria.getCodUte().getUsername();
		this.dataFirma = liberatoria.getDtFirma();
	}//LiberatoriaBean

	public String getAssociazione()
	{
		return associazione;
	}

	public String getUtente()
	{
		return utente;
	}

	public Date getDataFirma()
	{
		return dataFirma;
	}
	
}//LiberatoriaBean
