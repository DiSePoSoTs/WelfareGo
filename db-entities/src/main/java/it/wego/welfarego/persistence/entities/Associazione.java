/**
 * 
 */
package it.wego.welfarego.persistence.entities;

import javax.persistence.Basic;
import javax.persistence.*;
import java.io.Serializable;


/**
 * @author Fabio Bonaccorso
 *
 */
@Entity
@Table(name = "ASSOCIAZIONE")
@NamedQueries({ @NamedQuery(name = "Associazione.findAll", query = "SELECT a FROM Associazione a"),
		@NamedQuery(name = "Associazione.findById", query = "SELECT a from Associazione a WHERE a.id=:id") })
public class Associazione implements Serializable {

	private static final long serialVersionUID = -2632037258206480140L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "associazioneSequence")
	@SequenceGenerator(name = "associazioneSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID")
	private Integer id;

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Column(name = "NOME")
	private String nome;

	@Override
	public String toString() {
		return "id=" + id + ", nome=" + nome;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
