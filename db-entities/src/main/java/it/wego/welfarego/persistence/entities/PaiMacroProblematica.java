/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author aleph
 */
@Entity
@Table(name = "PAI_MACRO_PROBLEMATICHE")
@Getter
@Setter
public class PaiMacroProblematica implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected PaiMacroProblematicaPK paiMacroProblematicaPK = new PaiMacroProblematicaPK();

	/**
	 * tip param 'ma' TipologiaParametri.MACRO_PROBLEMATICHE
	 */
	@JoinColumn(name = "IP_MACRO_PROBLEMATICA", referencedColumnName = "ID_PARAM_INDATA", nullable = false, insertable = false, updatable = false)
	@ManyToOne
	private ParametriIndata ipMacroProblematica;

	/**
	 * tip param 'pr' TipologiaParametri.PROBLEMATICA_RILEVANZA
	 */
	@JoinColumn(name = "IP_RILEVANZA", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata ipRilevanza;

	/**
	 * tip param 'pf' TipologiaParametri.PROBLEMATICA_FRONTEGGIAMENTO
	 */
	@JoinColumn(name = "IP_FRONTEGGIAMENTO", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata ipFronteggiamento;

	/**
	 * tip param 'po' TipologiaParametri.PROBLEMATICA_OBIETTIVO_PREVALENTE
	 */
	@JoinColumn(name = "IP_OBIETTIVO_PREVALENTE", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata ipObiettivoPrevalente;

	@Column(name = "DETTAGLIO_NOTE")
	private String dettaglioNote;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paiMacroProblematica")
	private List<PaiMicroProblematica> paiMicroProblematicaList;

	@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Pai pai;

	public PaiMacroProblematica() {
	}

	public PaiMacroProblematica(PaiMacroProblematicaPK paiMacroProblematicaPK) {
		this.paiMacroProblematicaPK = paiMacroProblematicaPK;
	}

	public List<PaiMicroProblematica> getPaiMicroProblematicaList() {
		if (paiMicroProblematicaList == null) {
			paiMicroProblematicaList = Lists.newArrayList();
		}
		return paiMicroProblematicaList;
	}

}
