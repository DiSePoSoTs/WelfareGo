/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author aleph
 */
@Entity
@Getter
@Setter
@Table(name = "LUOGO")
public class Luogo implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "luogoSequence")
	@SequenceGenerator(name = "luogoSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "COD_LUOGO", nullable = false)
	private Long codLuogo;

	@JoinColumn(name = "COD_STATO", referencedColumnName = "COD_STATO")
	@ManyToOne
	private Stato stato;

	public Stato getStato() {
		return stato;
	}

	@JoinColumns({
			@JoinColumn(name = "COD_STATO", referencedColumnName = "COD_STATO", insertable = false, updatable = false),
			@JoinColumn(name = "COD_PROV", referencedColumnName = "COD_PROV") })
	@ManyToOne
	private Provincia provincia;

	public String getProvinciaStr() {
		return provinciaStr;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	@JoinColumns({
			@JoinColumn(name = "COD_STATO", referencedColumnName = "COD_STATO", insertable = false, updatable = false),
			@JoinColumn(name = "COD_PROV", referencedColumnName = "COD_PROV", insertable = false, updatable = false),
			@JoinColumn(name = "COD_COM", referencedColumnName = "COD_COM") })
	@ManyToOne
	private Comune comune;

	public Comune getComune() {
		return comune;
	}

	@JoinColumns({
			@JoinColumn(name = "COD_STATO", referencedColumnName = "COD_STATO", insertable = false, updatable = false),
			@JoinColumn(name = "COD_PROV", referencedColumnName = "COD_PROV", insertable = false, updatable = false),
			@JoinColumn(name = "COD_COM", referencedColumnName = "COD_COM", insertable = false, updatable = false),
			@JoinColumn(name = "COD_VIA", referencedColumnName = "COD_VIA") })
	@ManyToOne
	private Toponomastica via;

	public Toponomastica getVia() {
		return via;
	}

	@JoinColumns({
			@JoinColumn(name = "COD_STATO", referencedColumnName = "COD_STATO", insertable = false, updatable = false),
			@JoinColumn(name = "COD_PROV", referencedColumnName = "COD_PROV", insertable = false, updatable = false),
			@JoinColumn(name = "COD_COM", referencedColumnName = "COD_COM", insertable = false, updatable = false),
			@JoinColumn(name = "COD_VIA", referencedColumnName = "COD_VIA", insertable = false, updatable = false),
			@JoinColumn(name = "COD_CIV", referencedColumnName = "COD_CIV") })
	@ManyToOne()
	private ToponomasticaCivici civico;

	public ToponomasticaCivici getCivico() {
		return civico;
	}

	@Column(name = "DES_PROV")
	private String provinciaStr;

	@Column(name = "DES_COM")
	private String comuneStr;

	public String getComuneStr() {
		return comuneStr;
	}

	@Column(name = "DES_VIA")
	private String viaStr;

	public String getViaStr() {
		return viaStr;
	}

	@Column(name = "DES_CIV")
	private String civicoStr;

	public String getCivicoStr() {
		return civicoStr;
	}

	@Column(name = "CAP")
	private String cap;

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCap() {
		return cap;
	}

	public Luogo() {
	}

	public Luogo(Stato stato, Provincia provincia, String provinciaStr, Comune comune, String comuneStr,
			Toponomastica via, String viaStr, ToponomasticaCivici civico, String civicoStr, String cap) {
		this.stato = stato;
		this.provincia = provincia;
		this.comune = comune;
		this.via = via;
		this.civico = civico;
		this.provinciaStr = provinciaStr;
		this.comuneStr = comuneStr;
		this.viaStr = viaStr;
		this.civicoStr = civicoStr;
		this.cap = cap;
	}

	public String getStatoText() {
		return stato == null ? "" : stato.getDesStato();
	}

	public void setStato(Stato stato) {
		if (!Objects.equal(stato, this.stato)) {
			provincia = null;
			provinciaStr = null;
			comune = null;
			comuneStr = null;
			via = null;
			viaStr = null;
			civico = null;
			civicoStr = null;
		}
		this.stato = stato;
	}

	public void setProvincia(Provincia provincia) {
		this.provinciaStr = null;
		if (provincia != null) {
			this.stato = provincia.getStato();
		}
		if (!Objects.equal(provincia, this.provincia)) {
			comune = null;
			comuneStr = null;
			via = null;
			viaStr = null;
			civico = null;
			civicoStr = null;
		}
		this.provincia = provincia;
	}

	public void setComune(Comune comune) {
		this.comuneStr = null;
		if (comune != null) {
			this.provincia = comune.getProvincia();
			this.stato = provincia.getStato();
		}
		if (!Objects.equal(comune, this.comune)) {
			via = null;
			viaStr = null;
			civico = null;
			civicoStr = null;
		}
		this.comune = comune;
	}

	public void setVia(Toponomastica via) {
		viaStr = null;
		if (via != null) {
			this.comune = via.getComune();
			this.provincia = comune.getProvincia();
			this.stato = provincia.getStato();

		}
		if (!Objects.equal(via, this.via)) {
			civico = null;
			civicoStr = null;
		}
		this.via = via;
	}

	public void setCivico(ToponomasticaCivici civico) {
		civicoStr = null;
		if (civico != null) {
			this.via = civico.getToponomastica();
			this.comune = via.getComune();
			this.provincia = comune.getProvincia();
			this.stato = provincia.getStato();
		}
		this.civico = civico;
	}

	public String getProvinciaText() {
		return Strings.nullToEmpty(provincia != null ? provincia.getDesProv() : provinciaStr);
	}

	public void setProvinciaStr(String provinciaStr) {
		this.provinciaStr = provinciaStr;
		provincia = null;
		comune = null;
		comuneStr = null;
		via = null;
		viaStr = null;
		civico = null;
		civicoStr = null;
	}

	public String getComuneText() {
		return Strings.nullToEmpty(comune != null ? comune.getDesCom() : comuneStr);
	}

	public void setComuneStr(String comuneStr) {
		this.comuneStr = comuneStr;
		comune = null;
		via = null;
		viaStr = null;
		civico = null;
		civicoStr = null;
	}

	public String getViaText() {
		return Strings.nullToEmpty(via != null ? via.getDesVia() : viaStr);
	}

	public void setViaStr(String viaStr) {
		this.viaStr = viaStr;

		via = null;
		civico = null;
		civicoStr = null;

	}

	public String getCivicoText() {
		return Strings.nullToEmpty(civico != null ? civico.getDesCiv() : civicoStr);
	}

	public void setCivicoStr(String civicoStr) {
		this.civicoStr = civicoStr;
		civico = null;
	}

	public @Nullable String getCodStato() {
		return stato == null ? null : stato.getCodStato();
	}

	public @Nullable String getCodProv() {
		return provincia == null ? null : provincia.getCodProv();
	}

	public @Nullable String getCodCom() {
		return comune == null ? null : comune.getCodCom();
	}

	public @Nullable String getCodVia() {
		return via == null ? null : via.getCodVia();
	}

	public @Nullable String getCodCiv() {
		return civico == null ? null : civico.getCodCiv();
	}

	public String getIndirizzoText() {
		return getViaText() + " " + getCivicoText();
	}

	public String getIndirizzoTextPerUot() {

		return getViaText().toLowerCase();
	}

	public void clear() {
		civicoStr = null;
		viaStr = null;
		comuneStr = null;
		provinciaStr = null;
		civico = null;
		via = null;
		comune = null;
		provincia = null;
		stato = null;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("codLuogo", codLuogo).add("stato", stato)
				.add("provincia", provincia).add("provinciaStr", provinciaStr).add("comune", comune)
				.add("comuneStr", comuneStr).add("via", via).add("viaStr", viaStr).add("civico", civico)
				.add("civicoStr", civicoStr).toString();
	}

	public String getLuogoText() {
		return Joiner.on(", ").join(getStatoText(), getProvinciaText(), getComuneText(), getViaText(), getCivicoText())
				.replaceAll("  +", " ").replaceAll("(, *)+", ", ").replaceFirst(", *$", "");
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 47 * hash + (this.stato != null ? this.stato.hashCode() : 0);
		hash = 47 * hash + (this.provincia != null ? this.provincia.hashCode() : 0);
		hash = 47 * hash + (this.comune != null ? this.comune.hashCode() : 0);
		hash = 47 * hash + (this.via != null ? this.via.hashCode() : 0);
		hash = 47 * hash + (this.civico != null ? this.civico.hashCode() : 0);
		hash = 47 * hash + (this.provinciaStr != null ? this.provinciaStr.hashCode() : 0);
		hash = 47 * hash + (this.comuneStr != null ? this.comuneStr.hashCode() : 0);
		hash = 47 * hash + (this.viaStr != null ? this.viaStr.hashCode() : 0);
		hash = 47 * hash + (this.civicoStr != null ? this.civicoStr.hashCode() : 0);
		hash = 47 * hash + (this.cap != null ? this.cap.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Luogo)) {
			return false;
		}
		final Luogo other = (Luogo) obj;
		if (this.stato != other.stato && (this.stato == null || !this.stato.equals(other.stato))) {
			return false;
		}
		if (this.provincia != other.provincia && (this.provincia == null || !this.provincia.equals(other.provincia))) {
			return false;
		}
		if (this.comune != other.comune && (this.comune == null || !this.comune.equals(other.comune))) {
			return false;
		}
		if (this.via != other.via && (this.via == null || !this.via.equals(other.via))) {
			return false;
		}
		if (this.civico != other.civico && (this.civico == null || !this.civico.equals(other.civico))) {
			return false;
		}
		if ((this.provinciaStr == null) ? (other.provinciaStr != null)
				: !this.provinciaStr.equals(other.provinciaStr)) {
			return false;
		}
		if ((this.comuneStr == null) ? (other.comuneStr != null) : !this.comuneStr.equals(other.comuneStr)) {
			return false;
		}
		if ((this.viaStr == null) ? (other.viaStr != null) : !this.viaStr.equals(other.viaStr)) {
			return false;
		}
		if ((this.civicoStr == null) ? (other.civicoStr != null) : !this.civicoStr.equals(other.civicoStr)) {
			return false;
		}
		if ((this.cap == null) ? (other.cap != null) : !this.cap.equals(other.cap)) {
			return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return Strings.isNullOrEmpty(getStatoText()) && Strings.isNullOrEmpty(getProvinciaText())
				&& Strings.isNullOrEmpty(getComuneText()) && Strings.isNullOrEmpty(getViaText())
				&& Strings.isNullOrEmpty(getCivicoText()) && Strings.isNullOrEmpty(getCap());
	}

	public void updateFrom(Luogo luogoResidenzaTmp) {

		cap = luogoResidenzaTmp.getCap();
		civicoStr = luogoResidenzaTmp.getCivicoText();
		comuneStr = luogoResidenzaTmp.getComuneText();
		provinciaStr = luogoResidenzaTmp.getProvinciaText();
		viaStr = luogoResidenzaTmp.getViaText();

		civico = luogoResidenzaTmp.getCivico();
		comune = luogoResidenzaTmp.getComune();
		provincia = luogoResidenzaTmp.getProvincia();
		stato = luogoResidenzaTmp.getStato();
		via = luogoResidenzaTmp.getVia();
	}
}
