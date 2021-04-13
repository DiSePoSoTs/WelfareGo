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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fabio Bonaccorso classe che mappa la tabella per i token del
 *         webservice
 *
 */
@Entity
@Getter
@Setter
@Table(name = "WS_TOKEN")
public class WSToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "wsTokenSequence")
	@SequenceGenerator(name = "wsTokenSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID", nullable = false)
	private Integer id;

	@JoinColumn(name = "COD_UTE", referencedColumnName = "COD_UTE", nullable = false)
	@ManyToOne(optional = false)
	private Utenti codUte;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "EXPIRES", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsScadenza;

	public WSToken() {
	}

	public WSToken(Utenti u, String token, Date expires) {
		this.codUte = u;
		this.token = token;
		this.tsScadenza = expires;
	}

}
