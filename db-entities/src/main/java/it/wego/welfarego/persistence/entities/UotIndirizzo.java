/**
 * 
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Fabio Bonaccorso Classe che mappa la tabella uot_indirizzo
 *
 */
@Entity
@Getter
@Setter
@Table(name = "UOT_INDIRIZZO")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "UotIndirizzo.findAll", query = "SELECT u FROM UotIndirizzo u"),
		@NamedQuery(name = "UotIndirizzo.findById", query = "SELECT u FROM UotIndirizzo u WHERE u.id= :id "),
		@NamedQuery(name = "UotIndirizzo.findByIndirizzo", query = "SELECT u FROM UotIndirizzo u WHERE u.indirizzo like :indirizzo")

})
public class UotIndirizzo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "ID", nullable = false)
	private Integer id;

	@Column(name = "UOT")
	private Integer uot;

	@Column(name = "INDIRIZZO")
	private String indirizzo;

	@Column(name = "CIVICO")
	private String civico;

	@Column(name = "BARRATO")
	private String barrato;

}
