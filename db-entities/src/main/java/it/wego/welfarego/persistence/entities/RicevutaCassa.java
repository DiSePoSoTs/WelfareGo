/**
 * 
 */
package it.wego.welfarego.persistence.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fabio Bonaccorso Classe che serve
 *
 */
@Entity
@Getter
@Setter
@Table(name = "RICEVUTA_CASSA")
@NamedQueries({ @NamedQuery(name = "RicevutaCassa.findAll", query = "SELECT r FROM RicevutaCassa r"),
		@NamedQuery(name = "RicevutaCassa.findByDataEmissione", query = "SELECT r FROM RicevutaCassa r WHERE r.dataEmissione = :dataEmissione")

})
public class RicevutaCassa implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected RicevutaCassaPK ricevutaCassaPK;

	@Column(name = "NUMERO_RICEVUTA")
	private Integer numeroRicevuta;

	@Column(name = "UTENTE")
	private String utente;

	@Column(name = "IMPORTO", precision = 9, scale = 2)
	private BigDecimal importo;

	@Column(name = "DATA_EMISSIONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissione;

	@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Pai pai;

	public RicevutaCassa() {

	}

	public RicevutaCassa(String codPai, String cntTipInt) {
		this.ricevutaCassaPK = new RicevutaCassaPK(Integer.valueOf(codPai), Integer.valueOf(cntTipInt));
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof RicevutaCassa)) {
			return false;
		}
		RicevutaCassa other = (RicevutaCassa) object;
		if ((this.ricevutaCassaPK == null && other.ricevutaCassaPK != null)
				|| (this.ricevutaCassaPK != null && !this.ricevutaCassaPK.equals(other.ricevutaCassaPK))) {
			return false;
		}
		return true;
	}

}
