package it.wego.welfarego.pagamenti.fatturazione_elettronica;

import it.wego.welfarego.pagamenti.ExportFileUtils;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Fattura;
import it.wego.welfarego.persistence.entities.FatturaDettaglio;
import it.wego.welfarego.persistence.entities.Luogo;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FatturaElettronicaBuilder {

	static final String PROGRESSIVO_INVIO = "${variabile.FatturaElettronicaHeader.DatiTrasmissione.ProgressivoInvio}";
	static final String COGNOME = "${variabile.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.Anagrafica.Cognome}";
	static final String NOME = "${variabile.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.Anagrafica.Nome}";
	static final String COD_FISCALE = "${variabile.FatturaElettronicaHeader.CessionarioCommittente.DatiAnagrafici.CodiceFiscale}";
	static final String CAP = "${variabile.FatturaElettronicaHeader.CessionarioCommittente.Sede.CAP}";
	static final String COMUNE = "${variabile.FatturaElettronicaHeader.CessionarioCommittente.Sede.Comune}";
	static final String INDIRIZZO = "${variabile.FatturaElettronicaHeader.CessionarioCommittente.Sede.Indirizzo}";
	public static final String STATO = "${variabile.FatturaElettronicaHeader.CessionarioCommittente.Sede.Nazione}";
	public static final String DATA_DOCUMENTO = "${variabile.FatturaElettronicaBody.DatiGeneraliDocumento.Data}";
	public static final String NUMERO_DOCUMENTO = "${variabile.FatturaElettronicaBody.DatiGeneraliDocumento.Numero}";
	public static final String IMPONIBILE_IMPORTO = "${variabile.FatturaElettronicaBody.DatiRiepilogo.ImponibileImporto}";
	public static final String IMPORTO_PAGAMENTO = "${variabile.FatturaElettronicaBody.DatiPagamento.DettaglioPagamento.ImportoPagamento}";
	public static final String DATA_SCADENZA_PAGAMENTO = "${variabile.FatturaElettronicaBody.DatiPagamento.DettaglioPagamento.DataScadenzaPagamento}";
	public static final String DETTAGLI_LINEE = "${dettagli_linee}";
	public static final String NUMERO_RIGA = "${variabile.FatturaElettronicaBody.DatiBeniServizi.DettaglioLinee.NumeroLinea}";
	public static final String DESCRIZIONE = "${variabile.FatturaElettronicaBody.DatiBeniServizi.DettaglioLinee.Descrizione}";
	public static final String QUANTITA = "${variabile.FatturaElettronicaBody.DatiBeniServizi.DettaglioLinee.Quantita}";
	public static final String PREZZO_UNITARIO = "${variabile.FatturaElettronicaBody.DatiBeniServizi.DettaglioLinee.PrezzoUnitario}";
	public static final String PREZZO_TOTALE = "${variabile.FatturaElettronicaBody.DatiBeniServizi.DettaglioLinee.PrezzoTotale}";
	public static final String CAUSALE = "${variabile.FatturaElettronicaBody.DatiGeneraliDocumento.Causale}";
	public static final String IMPORTO = "${variabile.FatturaElettronicaBody.DatiGeneraliDocumento.ImportoTotaleDocumento}";
	public static final String IMPOSTA = "${variabile.FatturaElettronicaBody.DatiRiepilogo.Imposta}";
	public static final String FORAMATO_DATA = "yyyy-MM-dd";

	private String xmlFatturaElettronica;

	public FatturaElettronicaBuilder(String template_fattura_elettronica) {
		this.xmlFatturaElettronica = template_fattura_elettronica;
	}

	public String getXmlFatturaElettronica() {
		return xmlFatturaElettronica;
	}

	public FatturaElettronicaBuilder setProgeressivoInvio(
			BigDecimal progressivoInvio) {
		xmlFatturaElettronica = xmlFatturaElettronica.replace(PROGRESSIVO_INVIO, progressivoInvio.toString());
		return this;
	}

	public FatturaElettronicaBuilder setCessionarioCommittente(
			AnagrafeSoc anagrafeSoc) {
		String cognome = anagrafeSoc.getCognome();
		xmlFatturaElettronica = xmlFatturaElettronica.replace(COGNOME, cognome);

		String nome = anagrafeSoc.getNome();
		xmlFatturaElettronica = xmlFatturaElettronica.replace(NOME, nome);

		String codFisc = anagrafeSoc.getCodFisc();
		xmlFatturaElettronica = xmlFatturaElettronica.replace(COD_FISCALE, codFisc);

		Luogo residenza = anagrafeSoc.getLuogoResidenza();
		String cap = residenza.getCap();
		xmlFatturaElettronica = xmlFatturaElettronica.replace(CAP, cap);

		String comune = residenza.getComuneText();
		xmlFatturaElettronica = xmlFatturaElettronica.replace(COMUNE, comune);

		String indirizzoText = residenza.getIndirizzoText();
		xmlFatturaElettronica = xmlFatturaElettronica.replace(INDIRIZZO, indirizzoText);

		String statoText = residenza.getStatoText();
		if ("italia".equalsIgnoreCase(statoText)) {
			statoText = "IT";
		}
		xmlFatturaElettronica = xmlFatturaElettronica.replace(STATO, statoText);

		return this;
	}

	public FatturaElettronicaBuilder setDatiGeneraliDocumento(Date timbro,
			int numFatt, Map<String, String> parametri, BigDecimal bollo)
			throws IOException {

		SimpleDateFormat sdf = new SimpleDateFormat(FORAMATO_DATA);
		String dataDocumento = sdf.format(timbro);
		xmlFatturaElettronica = xmlFatturaElettronica.replace(DATA_DOCUMENTO, dataDocumento);
		xmlFatturaElettronica = xmlFatturaElettronica.replace(NUMERO_DOCUMENTO,	String.valueOf(numFatt));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${tipodoc}",	parametri.get("tipodoc"));

		xmlFatturaElettronica = xmlFatturaElettronica.replace("${divisa}", parametri.get("divisa"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${bollo}", get_tag_bollo(bollo));

		return this;
	}

	private String get_tag_bollo(BigDecimal bollo) throws IOException {
		String tag_bollo = "";

		if (bollo != null && bollo.compareTo(new BigDecimal(0)) > 0) {
			String xml_path = "it/wego/welfarego/pagamenti/fatturazione_elettronica/tag_bollo.xml";
			InputStream resourceAsStream = this.getClass().getClassLoader()
					.getResourceAsStream(xml_path);
			tag_bollo = IOUtils.toString(resourceAsStream);
			bollo = bollo.setScale(2, BigDecimal.ROUND_DOWN);
			tag_bollo = tag_bollo.replace("${importoBollo}", bollo.toString());
		}

		return tag_bollo;
	}

	public FatturaElettronicaBuilder setDatiRiepilogo (
			BigDecimal imponibileImporto, Map<String, String> parametri,
			BigDecimal bollo) throws IOException {

		String aliqiva = parametri.get("aliqiva");
		BigDecimal aliquotaIva = new BigDecimal(aliqiva);
		BigDecimal imposta = imponibileImporto.multiply(aliquotaIva);

		imponibileImporto = imponibileImporto.setScale(2, BigDecimal.ROUND_DOWN);
		imposta = imposta.setScale(2, BigDecimal.ROUND_DOWN);

		String imponibileImportoDatiRiepilogoString = imponibileImporto.toString();
		
		if (bollo != null && bollo.compareTo(new BigDecimal(0)) > 0) {
			bollo = bollo.setScale(2, BigDecimal.ROUND_DOWN);

			String xml_path = "it/wego/welfarego/pagamenti/fatturazione_elettronica/riepilogo_bollo.xml";
			InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(xml_path);
			String riepilogo_bollo = IOUtils.toString(resourceAsStream);

			xmlFatturaElettronica = xmlFatturaElettronica.replace("${riepilogo_bollo}", riepilogo_bollo);
			xmlFatturaElettronica = xmlFatturaElettronica.replace("${nat_bollo}", parametri.get("nat_bollo"));
			
			imponibileImportoDatiRiepilogoString = imponibileImporto.subtract(bollo).toString();
		}

		xmlFatturaElettronica = xmlFatturaElettronica.replace(IMPOSTA,imposta.toString());
		xmlFatturaElettronica = xmlFatturaElettronica.replace(IMPONIBILE_IMPORTO, imponibileImportoDatiRiepilogoString);

		return this;
	}

	public FatturaElettronicaBuilder setDatiPagamento(
			BigDecimal imponibileImporto, Map<String, String> parametri,
			Date dataScadenza) {

		xmlFatturaElettronica = xmlFatturaElettronica.replace(
				IMPORTO_PAGAMENTO, imponibileImporto.toString());

		SimpleDateFormat sdf = new SimpleDateFormat(FORAMATO_DATA);
		String dataScadenzaDocumento = sdf.format(dataScadenza);
		xmlFatturaElettronica = xmlFatturaElettronica.replace(
				DATA_SCADENZA_PAGAMENTO, dataScadenzaDocumento);
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dp_mod2}",
				parametri.get("dp_mod2"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dp_codufpo}",
				parametri.get("dp_codufpo"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dp_mod1}",
				parametri.get("dp_mod1"));
		String iban = parametri.get("dp_iban");
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dp_iban}",
				iban);
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${abi}",
				iban.substring(5, 10));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cab}",
				iban.substring(10, 15));
		return this;
	}

	public FatturaElettronicaBuilder setDettaglioLinee(
			List<FatturaDettaglio> fatturaDettaglioList,
			ParametriIndata idParamIva, Map<String, String> parametri,
			BigDecimal bollo) throws IOException {

		String xml_path = "it/wego/welfarego/pagamenti/fatturazione_elettronica/dettaglio_linea_tag.xml";
		InputStream resourceAsStream = this.getClass().getClassLoader()
				.getResourceAsStream(xml_path);
		String dettaglio_linea_tag = IOUtils.toString(resourceAsStream);

		List<String> tag_dettagli_linee = new ArrayList<String>();
		String tag_da_aggiungere = "";

		for (int i = 0; i < fatturaDettaglioList.size(); i++) {
			FatturaDettaglio voceFattura = fatturaDettaglioList.get(i);

			BigDecimal qtInputata = voceFattura.getQtInputata();
			BigDecimal importo = voceFattura.getImporto();
			qtInputata = qtInputata.setScale(2, BigDecimal.ROUND_DOWN);
			importo = importo.setScale(2, BigDecimal.ROUND_DOWN);

			tag_da_aggiungere = dettaglio_linea_tag.replace(NUMERO_RIGA, String.valueOf(i + 1));
			tag_da_aggiungere = tag_da_aggiungere.replace(DESCRIZIONE, voceFattura.getCodTipint().getDesTipint());
			tag_da_aggiungere = tag_da_aggiungere.replace(QUANTITA, qtInputata.toString());
			tag_da_aggiungere = tag_da_aggiungere.replace(PREZZO_UNITARIO, calcola_prezzo_unitario(voceFattura));
			tag_da_aggiungere = tag_da_aggiungere.replace(PREZZO_TOTALE, importo.toString());
			tag_da_aggiungere = tag_da_aggiungere.replace("${aliqiva}", parametri.get("aliqiva"));
			tag_da_aggiungere = tag_da_aggiungere.replace("${natura}", parametri.get("natura"));

			tag_dettagli_linee.add(tag_da_aggiungere);
		}

		if (bollo.compareTo(new BigDecimal(0)) > 0) {
			bollo = bollo.setScale(2, BigDecimal.ROUND_DOWN);
			BigDecimal quantita = new BigDecimal(1);
			quantita = quantita.setScale(2, BigDecimal.ROUND_DOWN);
			tag_da_aggiungere = dettaglio_linea_tag.replace(NUMERO_RIGA, String.valueOf(fatturaDettaglioList.size() + 1));
			tag_da_aggiungere = tag_da_aggiungere.replace(DESCRIZIONE, "Bollo");
			tag_da_aggiungere = tag_da_aggiungere.replace(QUANTITA, quantita.toString());
			tag_da_aggiungere = tag_da_aggiungere.replace(PREZZO_UNITARIO, bollo.toString());
			tag_da_aggiungere = tag_da_aggiungere.replace(PREZZO_TOTALE, bollo.toString());
			tag_da_aggiungere = tag_da_aggiungere.replace("${aliqiva}", parametri.get("aliqiva"));
			tag_da_aggiungere = tag_da_aggiungere.replace("${natura}", parametri.get("nat_bollo"));

			tag_dettagli_linee.add(tag_da_aggiungere);
		}

		xmlFatturaElettronica = xmlFatturaElettronica.replace(DETTAGLI_LINEE, StringUtils.join(tag_dettagli_linee, "\n"));
		return this;
	}

	private String calcola_prezzo_unitario(FatturaDettaglio voceFattura) {

		BigDecimal qtInputata = voceFattura.getQtInputata();
		BigDecimal importo = voceFattura.getImporto();
		BigDecimal prezzo_unitario = new BigDecimal(0);
		prezzo_unitario = prezzo_unitario.setScale(2);

		if (qtInputata.compareTo(new BigDecimal(0)) > 0) {
			prezzo_unitario = importo.divide(qtInputata, 4,
					RoundingMode.HALF_DOWN);
			prezzo_unitario.setScale(2, BigDecimal.ROUND_DOWN);
		} else {
			//
		}

		return prezzo_unitario.toString();
	}

	private String calcola_prezzo_unitario_1(FatturaDettaglio voceFattura) {
		PaiIntervento paiIntervento = voceFattura.getPaiIntervento();

		BigDecimal importoStandard = null;
		if (paiIntervento.getTariffa() != null) {
			importoStandard = paiIntervento.getTariffa().getCosto();
		} else {
			importoStandard = paiIntervento.getTipologiaIntervento()
					.getImpStdCosto();
		}

		BigDecimal percentualeFascia = null;
		ParametriIndata fascia = voceFattura.getFattura().getIdParamFascia();
		if (fascia != null) {
			percentualeFascia = fascia.getDecimalParam();
		} else {
			percentualeFascia = new BigDecimal(BigInteger.ZERO);
		}
		percentualeFascia = (new BigDecimal("100").subtract(percentualeFascia))
				.divide(new BigDecimal("100"));

		BigDecimal prezzo_unitario = importoStandard
				.multiply(percentualeFascia);// var17a

		prezzo_unitario = prezzo_unitario.setScale(2, BigDecimal.ROUND_DOWN);

		return prezzo_unitario.toString();
	}

	public FatturaElettronicaBuilder setDatiTrasmissione(
			Map<String, String> parametri) {
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dt_email}",
				parametri.get("dt_email"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dt_tel}",
				parametri.get("dt_tel"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dt_coddest}",
				parametri.get("dt_coddest"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dt_formtr}",
				parametri.get("dt_formtr"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dt_idcod}",
				parametri.get("dt_idcod"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dt_idpaese}",
				parametri.get("dt_idpaese"));

		return this;
	}

	public void setHeader(Map<String, String> parametri) {
		xmlFatturaElettronica = xmlFatturaElettronica.replace(
				"${header_dt_formtr}", parametri.get("dt_formtr"));
	}

	public FatturaElettronicaBuilder setCedentePrestatore(
			Map<String, String> parametri) {
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_nazione}",
				parametri.get("cp_nazione"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_prov}",
				parametri.get("cp_prov"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_comune}",
				parametri.get("cp_comune"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_cap}",
				parametri.get("cp_cap"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_numciv}",
				parametri.get("cp_numciv"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_indiriz}",
				parametri.get("cp_indiriz"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_regfisc}",
				parametri.get("cp_regfisc"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_denomin}",
				parametri.get("cp_denomin"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_codfisc}",
				parametri.get("cp_codfisc"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_idcod}",
				parametri.get("cp_idcod"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${cp_idpaese}",
				parametri.get("cp_idpaese"));
		return this;
	}

	public FatturaElettronicaBuilder setDatiBeniServizi(
			Fattura fattura_corrente, Map<String, String> parametri,
			BigDecimal importo) throws IOException {
		importo = importo.setScale(2, BigDecimal.ROUND_DOWN);
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${natura}",
				parametri.get("natura"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${aliqiva}",
				parametri.get("aliqiva"));
		xmlFatturaElettronica = xmlFatturaElettronica.replace(IMPORTO,
				importo.toString());
		xmlFatturaElettronica = xmlFatturaElettronica.replace(CAUSALE,
				costruisci_causale(fattura_corrente));
		return this;
	}

	private String costruisci_causale(Fattura fattura_corrente) {

		String causale = "";

		Pai pai = fattura_corrente.getPaiIntervento().getPai();
		Integer meseRif = fattura_corrente.getMeseRif();

		byte[] bytes = " - Serv fruiti periodo :".getBytes();
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		Charset iso8859 = Charset.forName("ISO-8859-1");
		CharBuffer decode = iso8859.decode(bb);
		String s = decode.toString();

		if (meseRif != null) {
			causale = pai.getIdParamUot().getDesParam()
					+ s
					+ ExportFileUtils.getMeseDesc(meseRif).toUpperCase()
							.substring(0, 3)
					+ " "
					+ (fattura_corrente.getFatturaDettaglioList().get(0)
							.getAnnoEff() - 2000);
		} else {
			FatturaDettaglio primoDettaglio = getPrimoFatturaDettaglio(fattura_corrente);
			FatturaDettaglio ultimoDettaglio = getUltimoFatturaDettaglio(fattura_corrente);
			if (primoDettaglio.getAnnoEff() == ultimoDettaglio.getAnnoEff()
					&& primoDettaglio.getMeseEff() == ultimoDettaglio
							.getMeseEff()) {
				causale = pai.getIdParamUot().getDesParam()
						+ s
						+ ExportFileUtils
								.getMeseDesc(primoDettaglio.getMeseEff())
								.toUpperCase().substring(0, 3) + " "
						+ (primoDettaglio.getAnnoEff() - 2000);
			} else {
				causale = pai.getIdParamUot().getDesParam()
						+ s
						+ ExportFileUtils
								.getMeseDesc(primoDettaglio.getMeseEff())
								.toUpperCase().substring(0, 3)
						+ " "
						+ (primoDettaglio.getAnnoEff() - 2000)
						+ " - "
						+ ExportFileUtils
								.getMeseDesc(ultimoDettaglio.getMeseEff())
								.toUpperCase().substring(0, 3) + " "
						+ (ultimoDettaglio.getAnnoEff() - 2000);
			}
		}

		return causale;
	}

	FatturaDettaglio getPrimoFatturaDettaglio(Fattura fattura) {
		List<FatturaDettaglio> dettagli = fattura.getFatturaDettaglioList();
		FatturaDettaglio dettaglio = dettagli.get(0);
		for (int i = 1; i < dettagli.size(); i++) {
			FatturaDettaglio det = dettagli.get(i);
			if (det.getAnnoEff() < dettaglio.getAnnoEff()) {
				dettaglio = det;
			} else if (det.getAnnoEff().equals(dettaglio.getAnnoEff())) {
				if (det.getMeseEff() < dettaglio.getMeseEff()) {
					dettaglio = det;
				}
			}
		}
		return dettaglio;
	}

	FatturaDettaglio getUltimoFatturaDettaglio(Fattura fattura) {
		List<FatturaDettaglio> dettagli = fattura.getFatturaDettaglioList();
		FatturaDettaglio dettaglio = dettagli.get(0);
		for (int i = 1; i < dettagli.size(); i++) {
			FatturaDettaglio det = dettagli.get(i);
			if (det.getAnnoEff() > dettaglio.getAnnoEff()) {
				dettaglio = det;
			} else if (det.getAnnoEff().equals(dettaglio.getAnnoEff())) {
				if (det.getMeseEff() > dettaglio.getMeseEff()) {
					dettaglio = det;
				}
			}
		}
		return dettaglio;
	}

	public FatturaElettronicaBuilder setCondizioniPagamento(
			Map<String, String> parametri) {
		String dp_condpag = parametri.get("dp_condpag");
		xmlFatturaElettronica = xmlFatturaElettronica.replace("${dp_condpag}",
				dp_condpag);
		return this;
	}

}
