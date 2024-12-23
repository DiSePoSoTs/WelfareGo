/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "PAI_EVENTO")
@NamedQueries({ @NamedQuery(name = "PaiEvento.findAll", query = "SELECT p FROM PaiEvento p"),
		@NamedQuery(name = "PaiEvento.findByTsEvePai", query = "SELECT p FROM PaiEvento p WHERE p.tsEvePai = :tsEvePai"),
		@NamedQuery(name = "PaiEvento.findByDesEvento", query = "SELECT p FROM PaiEvento p WHERE p.desEvento = :desEvento"),
		@NamedQuery(name = "PaiEvento.findByIdEvento", query = "SELECT p FROM PaiEvento p WHERE p.idEvento = :idEvento") })
public class PaiEvento implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final char FLG_STAMPA_SI = 'S', FLG_STAMPA_NO = 'N';

	public static final String PAI_APERTURA = "Apertura PAI", PAI_APERTURA_INTERVENTO = "Apertura nuovo intervento",
			PAI_APERTURA_INTERVENTO_DUPLICATO = "Apertura nuovo intervento duplicato",
			PAI_UPDATE_INTERVENTO = "Aggiornamento intervento", PAI_CHIUDI_INTERVENTO = "Chiusura intervento",
			PAI_UPDATE_CIVILMENTE_OBBLIGATO = "Aggiunto/modificato importo civilmente obbligato",
			PAI_DELETE_CIVILMENTE_OBBLIGATO = "Rimosso importo civilmente obbligato",
			PAI_PROTOCOLLAZIONE_INTERVENTO = "Protocollazione domanda intervento",
			PAI_UPDATE_PROPOSTE_IMPEGNI = "Aggiornamento proposte intervento",
			PAI_CONFERMA_INTERVENTO = "Approvata proposta intervento",
			PAI_APPROVA_INTERVENTO = "Approvata proposta intervento",
			PAI_RESPINGI_INTERVENTO = "Respinta proposta intervento",
			PAI_RIMANDA_INTERVENTO = "Rimandata proposta intervento",
			PAI_UPDATE = "Aggiornamento pai",
			PAI_INSERT = "Creazione pai",
			PAI_CLOSE = "Chiusura pai",
			PAI_TRASFERIMENTO = "Trasferimento/Decesso dell'utente", PAI_VALIDA_INTERVENTI = "Validazione interventi",
			PAI_PRODUCI_DOMANDA = "Produzione domanda ",
			PAI_REGISTRAZIONE_DETERMINA = "Registrazione determina singola",
			PAI_REGISTRAZIONE_DETERMINA_MULTIPLA = "Registrazione determina multipla",
			PAI_PROTOCOLLAZIONE_DOCUMENTO = "Protocollazione documento", PAI_VERIFICA_DOCUMENTO = "Verifica documento",
			PAI_APPROVAZIONE_LISTA_ATTESA = "Approvazione lista d'attesa",
			PAI_RIFIUTO_LISTA_ATTESA = "Rifiuto lista d'attesa",
			PAI_RICHIESTA_APPROVAZIONE = "Richiesta approvazione per il PAI",
			PAI_VISUALIZZA_NOTIFICA_REVISIONE = "Notifica esito revisione",
			PAi_VISUALIZZA_NOTIFICA_REVISIONE_INTERVENTO = "Notifica esito rivisione intervento",
			PAI_VISUALIZZA_NOTIFICA_RIFIUTO_INTERVENTO = "Notifica esito rifiuto intervento",
			AVVIO_ESECUTIVITA_INTERVENTO = "Avvio esecutivita' intervento",
			SINCRONIZZA_DA_VISTA_ANAGRAFE = "Sincronizzazione dati anagrafici da anagrafe comunale";


	public static final String MODIFICA_INTERVENTO_DA_TS_SOCIALE = "Modifica intervento da Trieste per il sociale";

	public static final String ELIMINA_INTERVENTO_DA_TS_SOCIALE = "Elimina intervento da Trieste per il sociale";

	@Basic(optional = false)
	@Column(name = "TS_EVE_PAI", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsEvePai;

	public Date getTsEvePai() {
		return tsEvePai;
	}

	public String getPaiDox() {
		return paiDox;
	}

	public char getFlgDxStampa() {
		return flgDxStampa;
	}

	public Integer getIdEvento() {
		return idEvento;
	}

	public String getDesEvento() {
		return desEvento;
	}

	@Basic(optional = false)
	@Lob
	@Column(name = "PAI_DOX", nullable = false)
	private String paiDox;

	@Lob
	@Column(name = "BRE_DOX")
	private String breDox;

	@Column(name = "DES_EVENTO", length = 3000)
	private String desEvento;

	public void setTsEvePai(Date tsEvePai) {
		this.tsEvePai = tsEvePai;
	}

	public void setPaiDox(String paiDox) {
		this.paiDox = paiDox;
	}

	public void setBreDox(String breDox) {
		this.breDox = breDox;
	}

	public void setDesEvento(String desEvento) {
		this.desEvento = desEvento;
	}

	public void setFlgDxStampa(char flgDxStampa) {
		this.flgDxStampa = flgDxStampa;
	}

	public void setIdEvento(Integer idEvento) {
		this.idEvento = idEvento;
	}

	public void setCodUte(Utenti codUte) {
		this.codUte = codUte;
	}

	public void setPaiIntervento(PaiIntervento paiIntervento) {
		this.paiIntervento = paiIntervento;
	}

	public void setCodPai(Pai codPai) {
		this.codPai = codPai;
	}

	public void setIdDetermina(Determine idDetermina) {
		this.idDetermina = idDetermina;
	}

	@Basic(optional = false)
	@Column(name = "FLG_DX_STAMPA", nullable = false)
	private char flgDxStampa;

	@Id
	@Basic(optional = false)
	@Column(name = "ID_EVENTO", nullable = false)
	@GeneratedValue(generator = "paiEventoSequence")
	@SequenceGenerator(name = "paiEventoSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	private Integer idEvento;

	@JoinColumn(name = "COD_UTE", referencedColumnName = "COD_UTE", nullable = false)
	@ManyToOne(optional = false)
	private Utenti codUte;

	@JoinColumn(name = "COD_TMPL", referencedColumnName = "COD_TMPL")
	@ManyToOne
	private Template codTmpl;

	@JoinColumns({
			@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private PaiIntervento paiIntervento;

	@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false)
	@ManyToOne(optional = false)
	private Pai codPai;

	public Utenti getCodUte() {
		return codUte;
	}

	public PaiIntervento getPaiIntervento() {
		return paiIntervento;
	}

	public Pai getCodPai() {
		return codPai;
	}

	@JoinColumn(name = "ID_DETERMINA", referencedColumnName = "ID_DETERMINA")
	@ManyToOne
	private Determine idDetermina;

	public Determine getIdDetermina() {
		return idDetermina;
	}

	public PaiEvento() {
	}

	public PaiEvento(Integer idEvento) {
		this.idEvento = idEvento;
	}

	public PaiEvento(Integer idEvento, Date tsEvePai, String paiDox, char flgDxStampa) {
		this.idEvento = idEvento;
		this.tsEvePai = tsEvePai;
		this.paiDox = paiDox;
		this.flgDxStampa = flgDxStampa;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idEvento != null ? idEvento.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PaiEvento)) {
			return false;
		}
		PaiEvento other = (PaiEvento) object;
		if ((this.idEvento == null && other.idEvento != null)
				|| (this.idEvento != null && !this.idEvento.equals(other.idEvento))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "idEvento=" + idEvento;
	}
}
