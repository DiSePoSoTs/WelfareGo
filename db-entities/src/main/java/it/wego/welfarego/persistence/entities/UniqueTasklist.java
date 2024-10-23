/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import it.trieste.comune.ssc.json.JsonBuilder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import javax.annotation.Nullable;
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
import org.slf4j.LoggerFactory;


/**
 *
 * @author giuseppe
 */
@Entity
@Table(name = "UNIQUE_TASKLIST")
@NamedQueries({ @NamedQuery(name = "UniqueTasklist.findAll", query = "SELECT u FROM UniqueTasklist u"),
		@NamedQuery(name = "UniqueTasklist.findById", query = "SELECT u FROM UniqueTasklist u WHERE u.id = :id"),
		@NamedQuery(name = "UniqueTasklist.findByTaskid", query = "SELECT u FROM UniqueTasklist u WHERE u.taskid = :taskid"),
		@NamedQuery(name = "UniqueTasklist.findByDesTask", query = "SELECT u FROM UniqueTasklist u WHERE u.desTask = :desTask"),
		@NamedQuery(name = "UniqueTasklist.findByTsCreazione", query = "SELECT u FROM UniqueTasklist u WHERE u.tsCreazione = :tsCreazione"),
		@NamedQuery(name = "UniqueTasklist.findByFlgEseguito", query = "SELECT u FROM UniqueTasklist u WHERE u.flgEseguito = :flgEseguito"),
		@NamedQuery(name = "UniqueTasklist.findByFlgTasknot", query = "SELECT u FROM UniqueTasklist u WHERE u.flgTasknot = :flgTasknot"),
		@NamedQuery(name = "UniqueTasklist.findByRuolo", query = "SELECT u FROM UniqueTasklist u WHERE u.ruolo = :ruolo"),
		@NamedQuery(name = "UniqueTasklist.findByUot", query = "SELECT u FROM UniqueTasklist u WHERE u.uot = :uot"),
		@NamedQuery(name = "UniqueTasklist.findByCampoFlow1", query = "SELECT u FROM UniqueTasklist u WHERE u.campoFlow1 = :campoFlow1"),
		@NamedQuery(name = "UniqueTasklist.findByCampoFlow2", query = "SELECT u FROM UniqueTasklist u WHERE u.campoFlow2 = :campoFlow2"),
		@NamedQuery(name = "UniqueTasklist.findByCampoFlow3", query = "SELECT u FROM UniqueTasklist u WHERE u.campoFlow3 = :campoFlow3"),
		@NamedQuery(name = "UniqueTasklist.findByCampoFlow4", query = "SELECT u FROM UniqueTasklist u WHERE u.campoFlow4 = :campoFlow4"),
		@NamedQuery(name = "UniqueTasklist.findByCampoFlow5", query = "SELECT u FROM UniqueTasklist u WHERE u.campoFlow5 = :campoFlow5"),
		@NamedQuery(name = "UniqueTasklist.findByCampoFlow6", query = "SELECT u FROM UniqueTasklist u WHERE u.campoFlow6 = :campoFlow6"),
		@NamedQuery(name = "UniqueTasklist.findByCampoFlow7", query = "SELECT u FROM UniqueTasklist u WHERE u.campoFlow7 = :campoFlow7"),
		@NamedQuery(name = "UniqueTasklist.findByCampoFlow8", query = "SELECT u FROM UniqueTasklist u WHERE u.campoFlow8 = :campoFlow8"),
		@NamedQuery(name = "UniqueTasklist.findByCampoForm1", query = "SELECT u FROM UniqueTasklist u WHERE u.campoForm1 = :campoForm1"),
		@NamedQuery(name = "UniqueTasklist.findByCampoForm2", query = "SELECT u FROM UniqueTasklist u WHERE u.campoForm2 = :campoForm2"),
		@NamedQuery(name = "UniqueTasklist.findByCampoForm3", query = "SELECT u FROM UniqueTasklist u WHERE u.campoForm3 = :campoForm3"),
		@NamedQuery(name = "UniqueTasklist.findByCampoForm4", query = "SELECT u FROM UniqueTasklist u WHERE u.campoForm4 = :campoForm4"),
		@NamedQuery(name = "UniqueTasklist.findByCampoForm5", query = "SELECT u FROM UniqueTasklist u WHERE u.campoForm5 = :campoForm5"),
		@NamedQuery(name = "UniqueTasklist.findByCampoForm6", query = "SELECT u FROM UniqueTasklist u WHERE u.campoForm6 = :campoForm6"),
		@NamedQuery(name = "UniqueTasklist.findByCampoForm7", query = "SELECT u FROM UniqueTasklist u WHERE u.campoForm7 = :campoForm7"),
		@NamedQuery(name = "UniqueTasklist.findByCampoForm8", query = "SELECT u FROM UniqueTasklist u WHERE u.campoForm8 = :campoForm8"),
		@NamedQuery(name = "UniqueTasklist.findByEsito", query = "SELECT u FROM UniqueTasklist u WHERE u.esito = :esito") })
public class UniqueTasklist implements Serializable {

	public static final String STANDALONE_TASK_ID = "STANDALONE_TASK",
			GENERAZIONE_DOCUMENTO_STANDALONE = "GENERAZIONE_DOCUMENTO_STANDALONE", FLG_ESEGUITO_NO = "N",
			FLG_TASKNOT_N = "N", EXTRA_PARAM_STAMPA_MINUTA = "stampaMinuta";

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "ID", nullable = false, precision = 22)
	@GeneratedValue(generator = "uniqueTasklistSequence")
	@SequenceGenerator(name = "uniqueTasklistSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	private BigDecimal id;

	@Column(name = "TASKID", length = 255)
	private String taskid = STANDALONE_TASK_ID;

	@Basic(optional = false)
	@Column(name = "DES_TASK", nullable = false, length = 255)
	private String desTask = "";

	@Basic(optional = false)
	@Column(name = "TS_CREAZIONE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsCreazione = new Date();
 
	public String getCampoForm1() {
		return campoForm1;
	}

	public String getCampoForm2() {
		return campoForm2;
	}

	public String getCampoForm3() {
		return campoForm3;
	}

	public String getCampoForm4() {
		return campoForm4;
	}

	public String getCampoForm5() {
		return campoForm5;
	}

	public String getCampoForm6() {
		return campoForm6;
	}

	public String getCampoForm7() {
		return campoForm7;
	}

	public void setCampoForm2(String campoForm2) {
		this.campoForm2 = campoForm2;
	}

	public String getCampoForm8() {
		return campoForm8;
	}

	public void setCampoForm1(String campoForm1) {
		this.campoForm1 = campoForm1;
	}

	public String getCampoFlow8() {
		return campoFlow8;
	}

	public String getCampoFlow1() {
		return campoFlow1;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDesTask() {
		return desTask;
	}

	public void setDesTask(String desTask) {
		this.desTask = desTask;
	}

	public Date getTsCreazione() {
		return tsCreazione;
	}

	public void setTsCreazione(Date tsCreazione) {
		this.tsCreazione = tsCreazione;
	}

	public String getFlgEseguito() {
		return flgEseguito;
	}

	public void setFlgEseguito(String flgEseguito) {
		this.flgEseguito = flgEseguito;
	}

	public String getFlgTasknot() {
		return flgTasknot;
	}

	public void setFlgTasknot(String flgTasknot) {
		this.flgTasknot = flgTasknot;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getUot() {
		return uot;
	}

	public void setUot(String uot) {
		this.uot = uot;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public UniqueForm getForm() {
		return form;
	}

	public void setForm(UniqueForm form) {
		this.form = form;
	}

	public Template getCodTmpl() {
		return codTmpl;
	}

	public void setCodTmpl(Template codTmpl) {
		this.codTmpl = codTmpl;
	}

	public void setCodPai(Pai codPai) {
		this.codPai = codPai;
	}

	@Basic(optional = false)
	@Column(name = "FLG_ESEGUITO", nullable = false, length = 1)
	private String flgEseguito = FLG_ESEGUITO_NO;

	@Basic(optional = false)
	@Column(name = "FLG_TASKNOT", nullable = false, length = 1)
	private String flgTasknot = FLG_TASKNOT_N;

	@Column(name = "RUOLO", length = 255)
	private String ruolo;

	@Column(name = "UOT", length = 255)
	private String uot;

	@Column(name = "CAMPO_FLOW_1", length = 255)
	private String campoFlow1;

	public void setCampoFlow1(String campoFlow1) {
		this.campoFlow1 = campoFlow1;
	}

	public void setCampoFlow2(String campoFlow2) {
		this.campoFlow2 = campoFlow2;
	}

	public void setCampoFlow3(String campoFlow3) {
		this.campoFlow3 = campoFlow3;
	}

	@Column(name = "CAMPO_FLOW_2", length = 255)
	private String campoFlow2;

	@Column(name = "CAMPO_FLOW_3", length = 255)
	private String campoFlow3;

	@Column(name = "CAMPO_FLOW_4", length = 255)
	private String campoFlow4;

	@Column(name = "CAMPO_FLOW_5", length = 255)
	private String campoFlow5;

	@Column(name = "CAMPO_FLOW_6", length = 255)
	private String campoFlow6;

	@Column(name = "CAMPO_FLOW_7", length = 255)
	private String campoFlow7;

	@Column(name = "CAMPO_FLOW_8", length = 255)
	private String campoFlow8;

	@Column(name = "CAMPO_FORM_1", length = 255)
	private String campoForm1;

	@Column(name = "CAMPO_FORM_2", length = 255)
	private String campoForm2;

	@Column(name = "CAMPO_FORM_3", length = 255)
	private String campoForm3;

	@Column(name = "CAMPO_FORM_4", length = 255)
	private String campoForm4;

	@Column(name = "CAMPO_FORM_5", length = 255)
	private String campoForm5;

	@Column(name = "CAMPO_FORM_6", length = 255)
	private String campoForm6;

	@Column(name = "CAMPO_FORM_7", length = 255)
	private String campoForm7;

	@Column(name = "CAMPO_FORM_8", length = 255)
	private String campoForm8;

	@Column(name = "ESITO", length = 10)
	private String esito;

	@JoinColumn(name = "FORM", referencedColumnName = "COD_FORM", nullable = false)
	@ManyToOne(optional = false)
	private UniqueForm form;

	@JoinColumn(name = "COD_TMPL", referencedColumnName = "COD_TMPL")
	@ManyToOne
	private Template codTmpl;

	@JoinColumns({
			@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_TIPINT", referencedColumnName = "COD_TIPINT", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "CNT_TIPINT", referencedColumnName = "CNT_TIPINT", nullable = false, insertable = false, updatable = false) })
	@ManyToOne(optional = false)
	private PaiIntervento paiIntervento;

	@JoinColumn(name = "COD_PAI", referencedColumnName = "COD_PAI", nullable = false)
	@ManyToOne(optional = false)
	private Pai codPai;

	public PaiIntervento getPaiIntervento() {
		return paiIntervento;
	}

	public Pai getCodPai() {
		return codPai;
	}

	public UniqueTasklist() {
	}

	public UniqueTasklist(BigDecimal id) {
		this.id = id;
	}

	public UniqueTasklist(BigDecimal id, String desTask, Date tsCreazione, String flgEseguito, String flgTasknot) {
		this.id = id;
		this.desTask = desTask;
		this.tsCreazione = tsCreazione;
		this.flgEseguito = flgEseguito;
		this.flgTasknot = flgTasknot;
	}

	public UniqueTasklist(String desTask, String flgEseguito, String flgTasknot) {
		this(null, desTask, new Date(), flgEseguito, flgTasknot);
	}

	public void setPaiIntervento(PaiIntervento paiIntervento) {
		if (paiIntervento != null) {
			this.codPai = paiIntervento.getPai();
		}
		this.paiIntervento = paiIntervento;
	}

	public void setPai(Pai codPai) {
		this.codPai = codPai;
		if (paiIntervento != null && !Objects.equal(paiIntervento.getPai(), codPai)) {
			paiIntervento = null;
		}
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof UniqueTasklist)) {
			return false;
		}
		UniqueTasklist other = (UniqueTasklist) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.UniqueTasklist[id=" + id + "]";
	}

	public @Nullable String getExtraParameterFromCampoFlow8(String key) {
		return getExtraParametersFromCampoFlow8().get(key);
	}

	public Map<String, String> getExtraParametersFromCampoFlow8() {
		if (Strings.isNullOrEmpty(campoFlow8)) {
			return Collections.emptyMap();
		} else {
			try {
				return JsonBuilder.getGson().fromJson(campoFlow8, JsonBuilder.MAP_OF_STRINGS);
			} catch (Exception ex) {
				LoggerFactory.getLogger(this.getClass()).warn("", ex);
				return Collections.emptyMap();
			}
		}
	}

	public void putExtraParameterInCampoFlow8(String key, @Nullable String value) {
		Map<String, String> map = Maps.newHashMap(getExtraParametersFromCampoFlow8());
		if (value == null) {
			map.remove(key);
		} else {
			map.put(key, value);
		}
		campoFlow8 = JsonBuilder.getGson().toJson(map);
	}
}
