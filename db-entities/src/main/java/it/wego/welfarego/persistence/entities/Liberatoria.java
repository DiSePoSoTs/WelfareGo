/**
 * 
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;


/**
 * @author Fabio Bonaccorso classe che rappresenta la tabella liberatoria(social
 *         crt)
 *
 */
@Entity
@Table(name = "LIBERATORIA")
@Getter
@Setter
public class Liberatoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "liberatoriaSequence")
	@SequenceGenerator(name = "liberatoriaSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID")
	private BigDecimal id;

	@JoinColumns({ @JoinColumn(name = "COD_ANA", referencedColumnName = "COD_ANA", nullable = false) })
	@ManyToOne(optional = false)
	private AnagrafeSoc anagrafeSoc;

	@JoinColumns({ @JoinColumn(name = "ID_ASSOCIAZIONE", referencedColumnName = "ID", nullable = false) })
	private Associazione associazione;

	@Column(name = "DT_FIRMA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFirma = new Date();

	@JoinColumn(name = "COD_UTE", referencedColumnName = "COD_UTE")
	@ManyToOne
	private Utenti codUte;

	public BigDecimal getId() {
		return id;
	}

	public void setAssociazione(Associazione associazione) {
		this.associazione = associazione;
	}

	public void setDtFirma(Date dtFirma) {
		this.dtFirma = dtFirma;
	}

	public void setCodUte(Utenti codUte) {
		this.codUte = codUte;
	}

	public Associazione getAssociazione() {
		return associazione;
	}

	public Date getDtFirma() {
		return dtFirma;
	}

	public Utenti getCodUte() {
		return codUte;
	}

	public AnagrafeSoc getAnagrafeSoc() {
		return anagrafeSoc;
	}

	public void setAnagrafeSoc(AnagrafeSoc anagrafeSoc) {
		this.anagrafeSoc = anagrafeSoc;
	}

	public Liberatoria() {

	}

	public Liberatoria(AnagrafeSoc ana, Associazione associazione) {
		this.anagrafeSoc = ana;
		this.associazione = associazione;
	}

}
