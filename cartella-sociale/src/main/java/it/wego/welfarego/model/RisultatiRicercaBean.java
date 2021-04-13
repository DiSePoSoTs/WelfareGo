package it.wego.welfarego.model;

import java.util.Date;

/**
 *
 * @author giuseppe
 */
public class RisultatiRicercaBean {

    private String tipoAnagrafe;
    private String codAnag;
    private String codAnagComunale;
    private String relazione;
    private String cognome;
    private String nome;
    private Date dataNascita;
    private String cancellato;
    private String desComuneResidenza;
    private String codComuneResidenza;
    private String desViaResidenza;
    private String codViaResidenza;
    private String codiceFiscale;
    private String nomeConiuge;
    private String uot;
    private String sesso;
    private Date dataMorte;
    private String comuneNascita;
    private String cittadinanza;
    private String dataC;
    private String statoCivile;
    private String capResidenza;
    private String provinciaResidenza;
    private String iban;

    public RisultatiRicercaBean() {
        tipoAnagrafe = "";
        cognome = "";
        nome = "";
        dataNascita = null;
        cancellato = "";
        desComuneResidenza = "";
        codComuneResidenza = "";
        desViaResidenza = "";
        codViaResidenza = "";
        codiceFiscale = "";
        nomeConiuge = "";
        uot = "";
        sesso = "";
        dataMorte = null;
        comuneNascita = "";
        cittadinanza = "";
        dataC = "";
        statoCivile = "";
        capResidenza = "";
        provinciaResidenza = "";
        relazione = "";
        iban="";
    }

    public String getCancellato() {
        return cancellato;
    }

    public void setCancellato(String cancellato) {
        this.cancellato = cancellato;
    }

    public String getCapResidenza() {
        return capResidenza;
    }

    public void setCapResidenza(String capResidenza) {
        this.capResidenza = capResidenza;
    }

    public String getCittadinanza() {
        return cittadinanza;
    }

    public void setCittadinanza(String cittadinanza) {
        this.cittadinanza = cittadinanza;
    }

    public String getCodComuneResidenza() {
        return codComuneResidenza;
    }

    public void setCodComuneResidenza(String codComuneResidenza) {
        this.codComuneResidenza = codComuneResidenza;
    }

    public String getCodViaResidenza() {
        return codViaResidenza;
    }

    public void setCodViaResidenza(String codViaResidenza) {
        this.codViaResidenza = codViaResidenza;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getComuneNascita() {
        return comuneNascita;
    }

    public void setComuneNascita(String comuneNascita) {
        this.comuneNascita = comuneNascita;
    }

    public String getDataC() {
        return dataC;
    }

    public void setDataC(String dataC) {
        this.dataC = dataC;
    }

    public Date getDataMorte() {
        return dataMorte;
    }

    public void setDataMorte(Date dataMorte) {
        this.dataMorte = dataMorte;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getDesComuneResidenza() {
        return desComuneResidenza;
    }

    public void setDesComuneResidenza(String desComuneResidenza) {
        this.desComuneResidenza = desComuneResidenza;
    }

    public String getDesViaResidenza() {
        return desViaResidenza;
    }

    public void setDesViaResidenza(String desViaResidenza) {
        this.desViaResidenza = desViaResidenza;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeConiuge() {
        return nomeConiuge;
    }

    public void setNomeConiuge(String nomeConiuge) {
        this.nomeConiuge = nomeConiuge;
    }

    public String getProvinciaResidenza() {
        return provinciaResidenza;
    }

    public void setProvinciaResidenza(String provinciaResidenza) {
        this.provinciaResidenza = provinciaResidenza;
    }

    public String getRelazione() {
        return relazione;
    }

    public void setRelazione(String relazione) {
        this.relazione = relazione;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public String getStatoCivile() {
        return statoCivile;
    }

    public void setStatoCivile(String statoCivile) {
        this.statoCivile = statoCivile;
    }

    public String getTipoAnagrafe() {
        return tipoAnagrafe;
    }

    public void setTipoAnagrafe(String tipoAnagrafe) {
        this.tipoAnagrafe = tipoAnagrafe;
    }

    public String getUot() {
        return uot;
    }

    public void setUot(String uot) {
        this.uot = uot;
    }

    public String getCodAnag() {
        return codAnag;
    }

    public void setCodAnag(String codAnag) {
        this.codAnag = codAnag;
    }

    public String getCodAnagComunale() {
        return codAnagComunale;
    }

    public void setCodAnagComunale(String codAnagComunale) {
        this.codAnagComunale = codAnagComunale;
    }

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}
}
