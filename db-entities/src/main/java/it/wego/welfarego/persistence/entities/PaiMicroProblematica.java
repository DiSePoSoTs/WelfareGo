/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 *
 * @author aleph
 */
@Entity
@Table(name = "PAI_MICRO_PROBLEMATICHE")
public class PaiMicroProblematica implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected PaiMicroProblematicaPK paiMicroProblematicaPK = new PaiMicroProblematicaPK();

	@JoinColumn(name = "IP_MACRO_PROBLEMATICA", referencedColumnName = "ID_PARAM_INDATA", nullable = false, insertable = false, updatable = false)
	@ManyToOne
	private ParametriIndata ipMacroProblematica;
	/**
	 * tip param 'mi' TipologiaParametri.MICRO_PROBLEMATICHE
	 */
	@JoinColumn(name = "IP_MICRO_PROBLEMATICA", referencedColumnName = "ID_PARAM_INDATA", nullable = false, insertable = false, updatable = false)
	@ManyToOne
	private ParametriIndata ipMicroProblematica;

	@JoinColumns({
			@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "IP_MACRO_PROBLEMATICA", referencedColumnName = "IP_MACRO_PROBLEMATICA", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private PaiMacroProblematica paiMacroProblematica;

	public ParametriIndata getIpMicroProblematica() {
		return ipMicroProblematica;
	}

	public PaiMicroProblematica() {
	}

	public PaiMicroProblematica(PaiMicroProblematicaPK paiMicroProblematicaPK) {
		this.paiMicroProblematicaPK = paiMicroProblematicaPK;
	}

	public PaiMicroProblematica(PaiMacroProblematicaPK paiMacroProblematichePK, int ipMicroProblematica) {
		this(new PaiMicroProblematicaPK(paiMacroProblematichePK, ipMicroProblematica));
	}
}
