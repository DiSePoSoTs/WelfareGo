package it.wego.welfarego.azione.models;

import it.wego.json.GsonObject;

import java.util.Date;

/**
 *
 * @author aleph
 */
public class VerificaImpegniGenericDataModel extends GsonObject {

    private Date dataApertPai, dataAvvio, dataAvvioProposta, dataFine;
    private String nomeUtenteVerDati, nPai, cognomeUt, assistSoc, descrizione, interv, costTot, budgRest, impMens, durata, spesaUnitaria, codParamUnitaMisura,note,trasformaIn,durMesiProroga,isProrogabile,motivazione;

    private char flgDocumentoDiAutorizzazione;
    private Integer codTmplDocumentoDiAutorizzazione;

    public String getCodParamUnitaMisura() {
        return codParamUnitaMisura;
    }

    public void setCodParamUnitaMisura(String codParamUnitaMisura) {
        this.codParamUnitaMisura = codParamUnitaMisura;
    }

    public Date getDataAvvio() {
        return dataAvvio;
    }

    public void setDataAvvio(Date dataInizio) {
        this.dataAvvio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public String getAssistSoc() {
        return assistSoc;
    }

    public void setAssistSoc(String assistSoc) {
        this.assistSoc = assistSoc;
    }

    public String getBudgRest() {
        return budgRest;
    }

    public void setBudgRest(String budgRest) {
        this.budgRest = budgRest;
    }

    public String getCognomeUt() {
        return cognomeUt;
    }

    public void setCognomeUt(String cognomeUt) {
        this.cognomeUt = cognomeUt;
    }

    public String getCostTot() {
        return costTot;
    }

    public void setCostTot(String costTot) {
        this.costTot = costTot;
    }

    public Date getDataApertPai() {
        return dataApertPai;
    }

    public void setDataApertPai(Date dataApertPai) {
        this.dataApertPai = dataApertPai;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getImpMens() {
        return impMens;
    }

    public void setImpMens(String impMens) {
        this.impMens = impMens;
    }

    public String getInterv() {
        return interv;
    }

    public void setInterv(String interv) {
        this.interv = interv;
    }

    public String getnPai() {
        return nPai;
    }

    public void setnPai(String nPai) {
        this.nPai = nPai;
    }

    public String getNomeUtenteVerDati() {
        return nomeUtenteVerDati;
    }

    public void setNomeUtenteVerDati(String nomeUtenteVerDati) {
        this.nomeUtenteVerDati = nomeUtenteVerDati;
    }

    public String getSpesaUnitaria() {
        return spesaUnitaria;
    }

    public void setSpesaUnitaria(String spesaUnitaria) {
        this.spesaUnitaria = spesaUnitaria;
    }

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDataAvvioProposta() {
		return dataAvvioProposta;
	}

	public void setDataAvvioProposta(Date dataAvvioProposta) {
		this.dataAvvioProposta = dataAvvioProposta;
	}

	public String getTrasformaIn() {
		return trasformaIn;
	}

	public void setTrasformaIn(String trasformaIn) {
		this.trasformaIn = trasformaIn;
	}
	
	public String getDurMesiProroga(){
		return durMesiProroga;
	}
	
	public void setDurataMesiProroga(String durMesiProroga){
		this.durMesiProroga=durMesiProroga;
	}

	public String getIsProrogabile() {
		return isProrogabile;
	}

	public void setIsProrogabile(String isProrogabile) {
		this.isProrogabile = isProrogabile;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}


    public char getFlgDocumentoDiAutorizzazione() {
        return flgDocumentoDiAutorizzazione;
    }

    public void setFlgDocumentoDiAutorizzazione(char flgDocumentoDiAutorizzazione) {
        this.flgDocumentoDiAutorizzazione = flgDocumentoDiAutorizzazione;
    }

    public Integer getCodTmplDocumentoDiAutorizzazione() {
        return codTmplDocumentoDiAutorizzazione;
    }

    public void setCodTmplDocumentoDiAutorizzazione(Integer codTmplDocumentoDiAutorizzazione) {
        this.codTmplDocumentoDiAutorizzazione = codTmplDocumentoDiAutorizzazione;
    }
}
