/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "D00.GDA@GDA.ITS")
public class VistaAnagrafe implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "NUMERO_FAMIGLIA", nullable = false)
	private int numeroFamiglia;

	@Id
	@Basic(optional = false)
	@Column(name = "NUMERO_INDIVIDUALE", nullable = false)
	private Integer numeroIndividuale;

	@Basic
	@Column(name = "FAMIGLIA_CONVIVENZA")
	private Integer famigliaConvivenza;

	@Basic(optional = false)
	@Column(name = "POSIZIONE_ANAGRAFICA", nullable = false, length = 10)
	private String posizioneAnagrafica;

	@Basic(optional = false)
	@Column(name = "SESSO", nullable = false, length = 1)
	private String sesso;

	public int getNumeroFamiglia() {
		return numeroFamiglia;
	}

	public Integer getNumeroIndividuale() {
		return numeroIndividuale;
	}

	public Integer getFamigliaConvivenza() {
		return famigliaConvivenza;
	}

	public String getPosizioneAnagrafica() {
		return posizioneAnagrafica;
	}

	public String getSesso() {
		return sesso;
	}

	public String getCognome() {
		return cognome;
	}

	public String getNome() {
		return nome;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public Date getDataMorte() {
		return dataMorte;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public String getCittadinanza() {
		return cittadinanza;
	}

	public String getCodStatoCittadinanza() {
		return codStatoCittadinanza;
	}

	public String getCognomeConiuge() {
		return cognomeConiuge;
	}

	public String getNomeConiuge() {
		return nomeConiuge;
	}

	public Date getDatac() {
		return datac;
	}

	public String getStatoCivile() {
		return statoCivile;
	}

	public String getRelazioneParentela() {
		return relazioneParentela;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public String getTitoloStudio() {
		return titoloStudio;
	}

	public String getProfessione() {
		return professione;
	}

	public String getFlagPensione() {
		return flagPensione;
	}

	public String getNumPensioni() {
		return numPensioni;
	}

	public String getCap() {
		return cap;
	}

	public String getCodiceComune() {
		return codiceComune;
	}

	public String getCodiceProvincia() {
		return codiceProvincia;
	}

	public Integer getCodiceComuneNascita() {
		return codiceComuneNascita;
	}

	public Integer getCodiceComuneResidenza() {
		return codiceComuneResidenza;
	}

	public Integer getCodiceStatoResidenza() {
		return codiceStatoResidenza;
	}

	public Integer getCodiceViaResidenza() {
		return codiceViaResidenza;
	}

	public Integer getCodiceProvinciaNascita() {
		return codiceProvinciaNascita;
	}

	public Integer getCodiceProvinciaREsidenza() {
		return codiceProvinciaREsidenza;
	}

	public Integer getCodiceStatoNascita() {
		return codiceStatoNascita;
	}

	public String getDescrizioneComuneResidenza() {
		return descrizioneComuneResidenza;
	}

	public String getDescrizioneCivicoResidenza() {
		return descrizioneCivicoResidenza;
	}

	public String getDescrizioneStatoResidenza() {
		return descrizioneStatoResidenza;
	}

	public String getDescrizioneViaResidenza() {
		return descrizioneViaResidenza;
	}

	public String getDescrizioneProvinciaNascita() {
		return descrizioneProvinciaNascita;
	}

	public String getDescrizioneProvinciaResidenza() {
		return descrizioneProvinciaResidenza;
	}

	public String getDescrizioneStatoNascita() {
		return descrizioneStatoNascita;
	}

	public String getCodiceCivicoResidenza() {
		return codiceCivicoResidenza;
	}

	@Basic(optional = false)
	@Column(name = "COGNOME", nullable = false, length = 60)
	private String cognome;

	@Basic(optional = false)
	@Column(name = "NOME", nullable = false, length = 60)
	private String nome;

	@Column(name = "DATA_NASCITA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascita;

	@Column(name = "DATA_MORTE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataMorte;

	@Basic(optional = false)
	@Column(name = "INDIRIZZO", nullable = false, length = 80)
	private String indirizzo;

	@Basic(optional = false)
	@Column(name = "LUOGO_NASCITA", nullable = false, length = 60)
	private String luogoNascita;

	@Basic(optional = false)
	@Column(name = "CITTADINANZA", nullable = false, length = 60)
	private String cittadinanza;

	@Basic
	@Column(name = "CODICE_STATO_CITTAD")
	private String codStatoCittadinanza;

	@Column(name = "COGNOME_CONIUGE", length = 60)
	private String cognomeConiuge;

	@Column(name = "NOME_CONIUGE", length = 60)
	private String nomeConiuge;

	@Column(name = "ISCADATAC")
	@Temporal(TemporalType.TIMESTAMP)
	private Date datac;

	@Column(name = "STATO_CIVILE", length = 2)
	private String statoCivile;

	@Basic(optional = false)
	@Column(name = "RELAZIONE_PARENTELA", nullable = false, length = 2)
	private String relazioneParentela;

	@Basic(optional = false)
	@Column(name = "CODICE_FISCALE", nullable = false, length = 16)
	private String codiceFiscale;

	@Column(name = "TITOLO_STUDIO", length = 20)
	private String titoloStudio;

	@Column(name = "PROFESSIONE", length = 10)
	private String professione;

	@Column(name = "FLAG_PENSIONE", length = 2)
	private String flagPensione;

	@Column(name = "NUM_PENSIONI", length = 2)
	private String numPensioni;

	@Column(name = "CAP", length = 20)
	private String cap;

	@Column(name = "CODICE_COMUNE", length = 10)
	private String codiceComune;

	@Column(name = "CODICE_PROVINCIA", length = 10)
	private String codiceProvincia;

	@Column(name = "COD_COMUNE_NASCITA")
	private Integer codiceComuneNascita;

	@Column(name = "COD_COMUNE_RESIDENZA")
	private Integer codiceComuneResidenza;

	@Column(name = "CODICE_STATO_RESIDENZA")
	private Integer codiceStatoResidenza;

	@Column(name = "CODICE_VIA_RESIDENZA")
	private Integer codiceViaResidenza;

	@Column(name = "COD_PROVINCIA_NASCITA")
	private Integer codiceProvinciaNascita;

	@Column(name = "COD_PROVINCIA_RESIDENZA")
	private Integer codiceProvinciaREsidenza;

	@Column(name = "COD_STATO_NASCITA")
	private Integer codiceStatoNascita;

	@Column(name = "COMUNE_RESIDENZA")
	private String descrizioneComuneResidenza;

	@Column(name = "DESCRIZIONE_CIVICO_RESIDENZA")
	private String descrizioneCivicoResidenza;

	@Column(name = "DESCRIZIONE_STATO_RESIDENZA")
	private String descrizioneStatoResidenza;

	@Column(name = "DESCRIZIONE_VIA_RESIDENZA")
	private String descrizioneViaResidenza;

	@Column(name = "PROVINCIA_NASCITA")
	private String descrizioneProvinciaNascita;

	@Column(name = "PROVINCIA_RESIDENZA")
	private String descrizioneProvinciaResidenza;

	@Column(name = "STATO_NASCITA")
	private String descrizioneStatoNascita;

	@Column(name = "CODICE_CIVICO_RESIDENZA_NEW")
	private String codiceCivicoResidenza;

	public VistaAnagrafe() {
	}

	public VistaAnagrafe(Integer numeroIndividuale) {
		this.numeroIndividuale = numeroIndividuale;
	}

	public VistaAnagrafe(Integer numeroIndividuale, int numeroFamiglia, String posizioneAnagrafica, String sesso,
			String cognome, String nome, String indirizzo, String luogoNascita, String cittadinanza,
			String relazioneParentela, String codiceFiscale) {
		this.numeroIndividuale = numeroIndividuale;
		this.numeroFamiglia = numeroFamiglia;
		this.posizioneAnagrafica = posizioneAnagrafica;
		this.sesso = sesso;
		this.cognome = cognome;
		this.nome = nome;
		this.indirizzo = indirizzo;
		this.luogoNascita = luogoNascita;
		this.cittadinanza = cittadinanza;
		this.relazioneParentela = relazioneParentela;
		this.codiceFiscale = codiceFiscale;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (numeroIndividuale != null ? numeroIndividuale.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof VistaAnagrafe)) {
			return false;
		}
		VistaAnagrafe other = (VistaAnagrafe) object;
		if ((this.numeroIndividuale == null && other.numeroIndividuale != null)
				|| (this.numeroIndividuale != null && !this.numeroIndividuale.equals(other.numeroIndividuale))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "numeroIndividuale=" + numeroIndividuale;
	}
}
