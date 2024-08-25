/**
 * 
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * @author Fabio Bonaccorso
 *
 */
@Entity
@Table(name = "NOTE_CONDIVISE")
@NamedQueries({ @NamedQuery(name = "NoteCondivise.findAll", query = "SELECT n FROM NoteCondivise n"),
		@NamedQuery(name = "NoteCondivise.findByCodAna", query = "SELECT n FROM NoteCondivise n WHERE n.anagrafeSoc.codAna = :codAna")

})
public class NoteCondivise implements Serializable {
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Id
	@GeneratedValue(generator = "noteCondiviseSequence")
	@SequenceGenerator(name = "noteCondiviseSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID", nullable = false)
	private Integer id;

	@JoinColumn(name = "COD_UTE", referencedColumnName = "COD_UTE")
	@ManyToOne
	private Utenti codUte;

	@JoinColumns({ @JoinColumn(name = "COD_ANA", referencedColumnName = "COD_ANA", nullable = false) })
	@ManyToOne(optional = false)
	private AnagrafeSoc anagrafeSoc;

	public Integer getId() {
		return id;
	}

	public Utenti getCodUte() {
		return codUte;
	}

	public void setAnagrafeSoc(AnagrafeSoc anagrafeSoc) {
		this.anagrafeSoc = anagrafeSoc;
	}

	public void setCodUte(Utenti codUte) {
		this.codUte = codUte;
	}

	public void setDtInserimento(Date dtInserimento) {
		this.dtInserimento = dtInserimento;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public void setEsteso(String esteso) {
		this.esteso = esteso;
	}

	public AnagrafeSoc getAnagrafeSoc() {
		return anagrafeSoc;
	}

	public Date getDtInserimento() {
		return dtInserimento;
	}

	public String getTitolo() {
		return titolo;
	}

	public String getEsteso() {
		return esteso;
	}

	@Column(name = "DATA_INSERIMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtInserimento = new Date();

	@Column(name = "TITOLO")
	private String titolo;

	@Column(name = "ESTESO")
	private String esteso;

}
