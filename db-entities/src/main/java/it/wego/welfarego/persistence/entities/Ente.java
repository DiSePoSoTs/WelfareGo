package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author DOTCOM s.r.l.
 */
@Entity
@Getter
@Setter
@Table(name = "ENTE")
public class Ente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "enteSequence")
	@SequenceGenerator(name = "enteSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID", nullable = false)
	private Integer id;

	@Basic(optional = false)
	@Column(name = "NOME", length = 765)
	private String nome;

	@Column(name = "DESCRIZIONE", length = 765)
	private String descrizione;

	@OneToMany(mappedBy = "ente")    
	private List<Struttura> strutturaList;

}
