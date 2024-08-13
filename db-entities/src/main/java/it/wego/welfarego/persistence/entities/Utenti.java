/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "UTENTI", uniqueConstraints = { @UniqueConstraint(columnNames = { "COD_FISC" }) })
@NamedQueries({ @NamedQuery(name = "Utenti.findAll", query = "SELECT u FROM Utenti u"),
		@NamedQuery(name = "Utenti.findByCodUte", query = "SELECT u FROM Utenti u WHERE u.codUte = :codUte"),
		@NamedQuery(name = "Utenti.findByCodFisc", query = "SELECT u FROM Utenti u WHERE u.codFisc = :codFisc"),
		@NamedQuery(name = "Utenti.findByNome", query = "SELECT u FROM Utenti u WHERE u.nome = :nome"),
		@NamedQuery(name = "Utenti.findByCognome", query = "SELECT u FROM Utenti u WHERE u.cognome = :cognome"),
		@NamedQuery(name = "Utenti.findByNumTel", query = "SELECT u FROM Utenti u WHERE u.numTel = :numTel"),
		@NamedQuery(name = "Utenti.findByNumCell", query = "SELECT u FROM Utenti u WHERE u.numCell = :numCell"),
		@NamedQuery(name = "Utenti.findByEmail", query = "SELECT u FROM Utenti u WHERE u.email = :email"),
		@NamedQuery(name = "Utenti.findByNote", query = "SELECT u FROM Utenti u WHERE u.note = :note"),
		@NamedQuery(name = "Utenti.findByUsername", query = "SELECT u FROM Utenti u WHERE u.username = :username") })
public class Utenti implements Serializable {

	public static final Ruoli OPERATORE_ACCESSO_UOT = Ruoli.ACC_UOT, OPERATORE_UOT = Ruoli.OPE_UOT,
			OPERATORE_UOT_O_SEDE_CENTRALE = Ruoli.OPERATORE_UOT_O_SEDE_CENTRALE, ASSISTENTE_SOCIALE_UOT = Ruoli.AS_UOT,
			COORDINATORE_UOT = Ruoli.CO_UOT, EDUCATORE_UOT = Ruoli.EDU_UOT, OPERATORE_SEDE_CENTRALE = Ruoli.OPE_CENTR,
			RESPONSABILE_POSIZIONE_ORGANIZZATIVA = Ruoli.PO_CENTR, DIRIGENTE_SEDE_CENTRALE = Ruoli.DIR_CENTR,
			ADMIN = Ruoli.ADMIN, DIRETTORE_AREA = Ruoli.DIR_AREA, OPERATORE_ASSOCAZIONE = Ruoli.OP_ASS;

	public static enum Ruoli {

		ADMIN, ACC_UOT, OPE_UOT, AS_UOT, CO_UOT, OPE_CENTR, PO_CENTR, DIR_CENTR, DIR_AREA, EDU_UOT,
		OPERATORE_UOT_O_SEDE_CENTRALE, OP_ASS
	}

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "utentiSequence")
	@SequenceGenerator(name = "utentiSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "COD_UTE", nullable = false)
	private Integer codUte;

	@Basic(optional = false)
	@Column(name = "COD_FISC", nullable = false, length = 16)
	private String codFisc;

	public String getCodFisc() {
		return codFisc;
	}

	@Basic(optional = false)
	@Column(name = "NOME", nullable = false, length = 765)
	private String nome;

	public ParametriIndata getIdParamUot() {
		return idParamUot;
	}

	public Integer getCodUte() {
		return codUte;
	}

	public Associazione getAssociazione() {
		return associazione;
	}

	public void setIdParamUot(ParametriIndata idParamUot) {
		this.idParamUot = idParamUot;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	@Basic(optional = false)
	@Column(name = "COGNOME", nullable = false, length = 765)
	private String cognome;

	@Column(name = "NUM_TEL", length = 20)
	private String numTel;

	@Column(name = "NUM_CELL", length = 20)
	private String numCell;

	@Column(name = "EMAIL", length = 200)
	private String email;

	@Column(name = "NOTE", length = 3000)
	private String note;

	@Basic(optional = false)
	@Column(name = "USERNAME", nullable = false, length = 50)
	private String username;

	@Column(name = "MOTIVAZIONE", nullable = false)
	private char motivazione;

	@Column(name = "PROBLEMATICHE", nullable = false)
	private char problematiche;

	@Column(name = "PROFILO", nullable = false)
	private char profilo;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codUteAut")
	private List<PaiDocumento> paiDocumentoList;

	@OneToMany(mappedBy = "codUteAs")
	private List<Pai> paiList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "utenti")
	private List<Indisponibilita> indisponibilitaList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codUte")
	private List<LogAnagrafe> logAnagrafeList;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codUte")
	private List<Appuntamento> appuntamentoList;

	// lista appuntamenti dove this e' l'utente servizio
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "utenti")
	private List<Appuntamento> appuntamentoList1;

	// lista appuntamenti dove this e' l'assistente sociale
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codUte")
	private List<PaiEvento> paiEventoList;

	@JoinColumn(name = "ID_PARAM_SER", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamSer;

	@JoinColumn(name = "ID_PARAM_UOT", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamUot;

	@JoinColumn(name = "ID_PARAM_LVL_ABIL", referencedColumnName = "ID_PARAM_INDATA", nullable = false)
	@ManyToOne(optional = false)
	private ParametriIndata idParamLvlAbil;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "codUte")
	private List<Contatto> contattoList;

	@JoinColumn(name = "ID_PARAM_PO", referencedColumnName = "ID_PARAM_INDATA")
	@ManyToOne
	private ParametriIndata idParamPo;

	@JoinColumn(name = "ID_ASSOCIAZIONE", referencedColumnName = "ID")
	@ManyToOne
	private Associazione associazione;

	public Utenti() {
	}

	public Utenti(Integer codUte) {
		this.codUte = codUte;
	}

	public Utenti(Integer codUte, String codFisc, String nome, String cognome, String username) {
		this.codUte = codUte;
		this.codFisc = codFisc;
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codUte != null ? codUte.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Utenti)) {
			return false;
		}
		Utenti other = (Utenti) object;
		if ((this.codUte == null && other.codUte != null)
				|| (this.codUte != null && !this.codUte.equals(other.codUte))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Utenti[codUte=" + codUte + "]";
	}

	public String getCognomeNome() {
		return getCognome() + " " + getNome();
	}

}
