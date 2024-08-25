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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Fabio Bonaccorso Classe che serve a mappare i log della cassa .
 *
 */
@Entity
@Table(name = "LOG_CASSA")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "LogCassa.findAll", query = "SELECT l FROM LogCassa l"),
		@NamedQuery(name = "LogCassa.findById", query = "SELECT l FROM LogCassa l WHERE l.id= :id "),
		@NamedQuery(name = "LogCassa.findByTipoOperazione", query = "SELECT l FROM LogCassa l WHERE l.tipoOperazione= :tipoOperazione ") })
public class LogCassa implements Serializable {

	public static final String OPERAZIONE_AGGIUNTA = "VERSAMENTO";

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getParametri() {
		return parametri;
	}

	public void setParametri(String parametri) {
		this.parametri = parametri;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public BigDecimal getCodPai() {
		return codPai;
	}

	public void setCodPai(BigDecimal codPai) {
		this.codPai = codPai;
	}

	public BigDecimal getIntervento() {
		return intervento;
	}

	public void setIntervento(BigDecimal intervento) {
		this.intervento = intervento;
	}

	public Date getDataOperazione() {
		return dataOperazione;
	}

	public void setDataOperazione(Date dataOperazione) {
		this.dataOperazione = dataOperazione;
	}

	public BigDecimal getId() {
		return id;
	}

	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	public static final String OPERAZIONE_PRELIEVO = "PRELIEVO";

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "logCassaSequence")
	@SequenceGenerator(name = "logCassaSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID")
	private BigDecimal id;

	@Column(name = "TIPO_OPERAZIONE")
	private String tipoOperazione;

	@Column(name = "PARAMETRI")
	private String parametri;

	@Column(name = "IMPORTO", precision = 9, scale = 2)
	private BigDecimal importo;

	@Column(name = "IMPORTO_TOTALE", precision = 9, scale = 2)
	private BigDecimal importoTotale;

	@Column(name = "COD_PAI", precision = 9)
	private BigDecimal codPai;

	@Column(name = "CNT_TIPINT", precision = 9)
	private BigDecimal intervento;

	@Column(name = "DATA_OPERAZIONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataOperazione;

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof LogCassa)) {
			return false;
		}
		LogCassa other = (LogCassa) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

}
