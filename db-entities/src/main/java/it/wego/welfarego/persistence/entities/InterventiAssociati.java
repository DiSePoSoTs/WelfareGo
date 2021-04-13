/**
 *
 */
package it.wego.welfarego.persistence.entities;

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
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fabio Bonaccorso Classe che mappa la tabella degli interventi
 *         associati (ovvero interventi figli di un altro intervento dello
 *         stesso tipo)
 */
@Entity
@Getter
@Setter
@Table(name = "INTERVENTI_ASSOCIATI")
@NamedQueries({ @NamedQuery(name = "InterventiAssociati.findAll", query = "SELECT i FROM InterventiAssociati i"),
		@NamedQuery(name = "InterventiAssociati.findById", query = "SELECT i from InterventiAssociati i WHERE i.id=:id") })
public class InterventiAssociati implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String RELAZIONE_PARENTALE = "RELAZIONE_PARENTALE";

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "interventiassociatiSequence")
	@SequenceGenerator(name = "interventiassociatiSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID", nullable = false)
	private Integer id;

	@JoinColumns({ @JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false) })
	@ManyToOne(optional = false)
	private PaiIntervento interventoPadre;

	@JoinColumns({ @JoinColumn(name = "COD_PAI1", referencedColumnName = "COD_PAI", nullable = false),
			@JoinColumn(name = "COD_TIPINT1", referencedColumnName = "COD_TIPINT", nullable = false),
			@JoinColumn(name = "CNT_TIPINT1", referencedColumnName = "CNT_TIPINT", nullable = false) })
	private PaiIntervento interventoFiglio;

	@Column(name = "TIPO_LEGAME")
	private String tipoLegame;

}
