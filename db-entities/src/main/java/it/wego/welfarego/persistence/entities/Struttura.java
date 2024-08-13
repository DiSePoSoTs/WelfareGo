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
import lombok.Setter;
import lombok.Getter;

@Entity
@Getter
@Setter
@Table(name = "STRUTTURA")
@NamedQueries({ @NamedQuery(name = "Struttura.findAll", query = "SELECT s FROM Struttura s"),
		@NamedQuery(name = "Struttura.findById", query = "SELECT s from Struttura s WHERE s.id=:id"),
		@NamedQuery(name = "Struttura.findByNome", query = "SELECT s from Struttura s WHERE s.nome=:nome") })
public class Struttura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "strutturaSequence")
	@SequenceGenerator(name = "strutturaSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID", nullable = false)
	private Integer id;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "INDIRIZZO")
	private String indirizzo;

	public Ente getEnte() {
		return ente;
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipologiaIntervento getIntervento() {
		return intervento;
	}

	public void setIntervento(TipologiaIntervento intervento) {
		this.intervento = intervento;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public void setCsrId(String csrId) {
		this.csrId = csrId;
	}

	public String getCsrId() {
		return csrId;
	}

	@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT")
	@ManyToOne
	private TipologiaIntervento intervento;

	@JoinColumn(name = "ID_ENTE", referencedColumnName = "ID")
	@ManyToOne
	private Ente ente;

	@Column(name = "CSR_ID")
	private String csrId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "struttura")
	private List<Tariffa> tariffaList;

}
