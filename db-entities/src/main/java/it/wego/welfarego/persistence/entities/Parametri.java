/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.entities;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.eclipse.persistence.annotations.JoinFetch;
import org.eclipse.persistence.annotations.JoinFetchType;
import lombok.Getter;
import lombok.Setter;


/**
 *
 * @author giuseppe
 */
@Entity
@Getter
@Setter
@Table(name = "PARAMETRI", uniqueConstraints = { @UniqueConstraint(columnNames = { "TIP_PARAM", "COD_PARAM" }) })
@NamedQueries({ @NamedQuery(name = "Parametri.findAll", query = "SELECT p FROM Parametri p"),
		@NamedQuery(name = "Parametri.findByIdParam", query = "SELECT p FROM Parametri p WHERE p.idParam = :idParam"),
		@NamedQuery(name = "Parametri.findByCodParam", query = "SELECT p FROM Parametri p WHERE p.codParam = :codParam"),
		@NamedQuery(name = "Parametri.findByFlgAttivo", query = "SELECT p FROM Parametri p WHERE p.flgAttivo = :flgAttivo") })
public class Parametri implements Serializable {

	public static final String TIPOLOGIA_ABITATIVA = "ab";
	public static final String CONDIZIONE_FAMIGLIARE = "cf";
	public static final String CLASSE_INTERVENTO = "ci";
	public static final String CONDIZIONE_PROFESSIONALE = "co";
	public static final String CODICE_DEL_SERVIZIO = "cs";
	public static final String DIAGNOSI_SOCIALE_PRINCIPALE = "ds";
	public static final String ESITO_INTERVENTO = "ei";
	public static final String FASCIA_DI_REDDITO = "fa";
	public static final String FORMA_DI_REDDITO = "fr";
	public static final String GRADO_DI_PARENTELA = "gp";
	public static final String MODALITA_PAGAMENTO = "mp";
	public static final String TIPOLOGIA_NUCLEO_FAMIGLIARE = "nf";
	public static final String NAZIONALITA = "nz";
	public static final String POSIZIONE_ANAGRAFICA = "pa";
	public static final String QUALITA_ANAGRAFE_SOCIALE = "qa";
	public static final String MOTIVAZIONI_CHIUSURA = "rc";
	public static final String CAUSE_RESPINGIMENTO = "cr";
	public static final String RELAZIONI_REFERENTE = "re";
	public static final String STRUTTURE_ACCOGLIENZA = "sa";
	public static final String STATO_CIVILE = "sc";
	public static final String SEGNALATO_DA = "se";
	public static final String STATO_FISICO = "sf";
	public static final String SCOLARITA = "so";
	public static final String TIPOLOGIE_INTERVENTO = "ti";
	public static final String UNITA_MISURA = "um";
	public static final String CODICE_UOT = "uo";
	public static final String LIVELLO_ABILITAZIONE = "al";
	public static final String PARAMETRO_UTENTI = "qa";
	public static final String GRUPPO_ASSISTENTI_SOCIALI = "3";
	public static final String QUALIFICA = "qu";
	public static final String MOTIVAZIONI_VARIAZIONE_SPESA = "mv";
	public static final int QUALIFICA_PARENTE = 314;
	public static final int CITTADINANZA_ITALIANA = 48;
	public static final String PROVVEDIMENTO_GIUDIZIARIO = "pg";
	public static final String TIPOLOGIA_RESIDENZA = "tr";
	public static final String CERTIFICATO_L104 = "cl";
	public static final String MACRO_PROBLEMATICHE = "ma";
	public static final String MICRO_PROBLEMATICHE = "mi";
	public static final String PROBLEMATICA_RILEVANZA = "pr";
	public static final String PROBLEMATICA_FRONTEGGIAMENTO = "pf";
	public static final String PROBLEMATICA_OBIETTIVO_PREVALENTE = "po";
	public static final String PARAMETRI_FATTURE = "paf";
	public static final String ALIQUOTA_IVA = "iv";
	public static final String PO = "posop";
	public static final String MACRO_PROBLEMATICA_DEFAULT = "1417";
	
	public static final String UNITA_MISURA_ORE_SETTIMANALI = "os", 
			UNITA_MISURA_ORE_MENSILI = "om",
			UNITA_MISURA_PASTI_SETTIMANALI = "ps", 
			UNITA_MISURA_PASTI_MENSILI = "pm", 
			ALIQUOTA_IVA_IVA_ORDINARIA = "or",
			ALIQUOTA_IVA_DEFAULT = ALIQUOTA_IVA_IVA_ORDINARIA;
	public static final Set<String> UNITA_MISURA_SETTIMANALI = Collections.unmodifiableSet(
			Sets.newHashSet(Parametri.UNITA_MISURA_PASTI_SETTIMANALI, Parametri.UNITA_MISURA_ORE_SETTIMANALI));
	public static final Map<String, String> UNITA_MISURA_SETTIMANALI_TO_MENSILI;
	public static final String CLASSE_TIPOLOGIA_INTERVENTO_ECONOMICI = "07",
			CLASSE_TIPOLOGIA_INTERVENTO_DOMICILIARI = "08", CLASSE_TIPOLOGIA_INTERVENTO_RESIDENZIALI = "09",
			CLASSE_TIPOLOGIA_INTERVENTO_SEMI_RESIDENZIALI = "10";

	static {
		Map<String, String> map = Maps.newHashMap();
		map.put(Parametri.UNITA_MISURA_PASTI_SETTIMANALI, Parametri.UNITA_MISURA_PASTI_MENSILI);
		map.put(Parametri.UNITA_MISURA_ORE_SETTIMANALI, Parametri.UNITA_MISURA_ORE_MENSILI);
		UNITA_MISURA_SETTIMANALI_TO_MENSILI = Collections.unmodifiableMap(map);
	}

	public static int getIdFasciaDefault(EntityManager em) {
		return Integer.valueOf(em.find(Configuration.class, "pai.fascia.default").getValue());
	}

	public static int getTipoAssistenteSociale(EntityManager em) {
		return Integer.valueOf(em.find(Configuration.class, "utente.assistente.sociale").getValue());
	}

	public static int getTipoCoordinatoreUot(EntityManager em) {
		return Integer.valueOf(em.find(Configuration.class, "utente.coordiantore.uot").getValue());
	}

	public static String getURLApprovazioneIntalio(EntityManager em) {
		return em.find(Configuration.class, "START_APPROVAZIONE_URL").getValue();
	}

	public static String getURLDocumentaleIntalio(EntityManager em) {
		return em.find(Configuration.class, "START_DOCUMENTALE_URL").getValue();
	}

	public static String getCodiceTemplateChiusura(EntityManager em) {
		return em.find(Configuration.class, "cod.tmpl.chius.int").getValue();
	}

	public static String getCodiceTemplateDomanda(EntityManager em) {
		return em.find(Configuration.class, "cod.tmpl.domanda").getValue();
	}

	public static String getCodiceTemplateCartellaSociale(EntityManager em) {
		return em.find(Configuration.class, "cod.tmpl.cartella").getValue();
	}

	public static String getCodiceTemplateDiario(EntityManager em) {
		return em.find(Configuration.class, "cod.tmpl.diario").getValue();
	}

	public static String getURLDocumentiDinamici(EntityManager em) {
		return em.find(Configuration.class, "PROPERTY_DYN_DOC_URL").getValue();
	}

	public static int getCodiceInterventoDefault(EntityManager em) {
		return Integer.parseInt(em.find(Configuration.class, "cod.int.default").getValue());
	}

	public static int getCodiceMotivazioneTrasferimento(EntityManager em) {
		return Integer.parseInt(em.find(Configuration.class, "cod.motiv.trasferimento").getValue());
	}

	public static int getCodiceMotivazioneDecesso(EntityManager em) {
		return Integer.parseInt(em.find(Configuration.class, "cod.motiv.decesso").getValue());
	}

	public static int getMiltonPort(EntityManager em) {
		return Integer.parseInt(em.find(Configuration.class, "it.wego.wefarego.webdav.port").getValue());
	}

	public static String getURLCSR(EntityManager em) {
		return em.find(Configuration.class, "url.insiel.csr").getValue();
	}

	public static boolean isCSREnabled(EntityManager em) {
		return Boolean.valueOf(em.find(Configuration.class, "abilita.csr").getValue());
	}

	public static boolean isProxyEnabled(EntityManager em) {
		return Boolean.valueOf(em.find(Configuration.class, "proxy.enabled").getValue());
	}

	public static String getProxyURL(EntityManager em) {
		return em.find(Configuration.class, "proxy.url").getValue();
	}

	public static String getProxyPort(EntityManager em) {
		return em.find(Configuration.class, "proxy.port").getValue();
	}

	public static String getProxyUsername(EntityManager em) {
		return em.find(Configuration.class, "proxy.user").getValue();
	}

	public static String getProxyPassword(EntityManager em) {
		return em.find(Configuration.class, "proxy.password").getValue();
	}

	public static final char FLAG_ATTIVO_S = 'S';

	private static final long serialVersionUID = 1L;

	public Integer getIdParam() {
		return idParam;
	}

	@Id
	@Basic(optional = false)
	@GeneratedValue(generator = "parametriSequence")
	@SequenceGenerator(name = "parametriSequence", sequenceName = "WG_SEQ", allocationSize = 50)
	@Column(name = "ID_PARAM", nullable = false)
	private Integer idParam;

	@Basic(optional = false)
	@Column(name = "COD_PARAM", nullable = false, length = 10)
	private String codParam;

	public String getCodParam() {
		return codParam;
	}

	@Basic(optional = false)
	@Column(name = "FLG_ATTIVO", nullable = false)
	private char flgAttivo;

	@JoinFetch(value = JoinFetchType.INNER)
	@JoinColumn(name = "TIP_PARAM", referencedColumnName = "TIP_PARAM", nullable = false)
	@ManyToOne(optional = false)
	private TipologiaParametri tipParam;

	public TipologiaParametri getTipParam() {
		return tipParam;
	}

	@JoinFetch(value = JoinFetchType.OUTER)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idParam")
	private List<ParametriIndata> parametriIndataList;

	public List<ParametriIndata> getParametriIndataList() {
		return parametriIndataList;
	}

	public Parametri() {
	}

	public Parametri(Integer idParam, String codParam, char flgAttivo) {
		this.idParam = idParam;
		this.codParam = codParam;
		this.flgAttivo = flgAttivo;
	}

	public @Nullable ParametriIndata getParametriIndataSingle() {
		if (parametriIndataList == null) {
			return null;
		}
		return Iterables.getOnlyElement(parametriIndataList, null);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idParam != null ? idParam.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Parametri)) {
			return false;
		}
		Parametri other = (Parametri) object;
		return !((this.idParam == null && other.idParam != null)
				|| (this.idParam != null && !this.idParam.equals(other.idParam)));
	}

	@Override
	public String toString() {
		return "it.wego.welfarego.persistence.entities.Parametri[idParam=" + idParam + "]";
	}
}
