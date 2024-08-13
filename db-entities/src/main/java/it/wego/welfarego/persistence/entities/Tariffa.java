package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import lombok.Setter;
import lombok.Getter;

/**
 *
 * @author DOTCOM s.r.l.
 */
@Entity
@Getter
@Setter
@Table(name = "TARIFFA")
@NamedQueries({
		@NamedQuery(name = "Tariffa.findByStruttura", query = "SELECT t FROM Tariffa t WHERE t.struttura.id=:id") })
public class Tariffa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "tariffaSequence")
	@SequenceGenerator(name = "tariffaSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID", nullable = false)
	private Integer id;

	@Basic(optional = false)
	@Column(name = "ANNO", nullable = false)
	private Integer anno;

	@Basic(optional = false)
	@Column(name = "COSTO", nullable = false)
	private BigDecimal costo;

	public BigDecimal getCosto() {
		return costo;
	}

	@Column(name = "DESCRIZIONE")
	private String descrizione;

	@Basic(optional = false)
	@Column(name = "FORFAIT", nullable = false)
	private char forfait;

	public Integer getAnno() {
		return anno;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public Struttura getStruttura() {
		return struttura;
	}

	public char getForfait() {
		return forfait;
	}

	public Integer getId() {
		return id;
	}

	@JoinColumn(name = "ID_STRUTTURA", referencedColumnName = "ID")
	@ManyToOne
	private Struttura struttura;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tariffa")
	private List<PaiIntervento> paiIntervento;
}
