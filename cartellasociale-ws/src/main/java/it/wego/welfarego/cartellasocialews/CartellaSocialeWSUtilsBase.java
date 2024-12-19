package it.wego.welfarego.cartellasocialews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.annotation.Nullable;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import it.wego.persistence.PersistenceAdapter;

public class CartellaSocialeWSUtilsBase extends PersistenceAdapter {

	protected static final Integer CITTADINANZA_DEFAULT = 1;
	
	protected static final String EMPTY_STRING = "", DOUBLE_X = "XX", VERSIONE = "?", DEFAULT_CIVICO = "4",
			DEFAULT_INDIRIZZO = "PIAZZA DELL'UNITA'D'ITALIA", IP_CERTIFICATO_L104_DEFAULT = "38",
			IP_PROVVEDIMENTO_GIUDIZIARIO_DEFAULT = "19", MOTIVAZIONE_CHIUSURA_CARTELLA_DEFAULT = "60",
			FRONTEGGIAMENTO_DEFAULT = "1", RILEVANZA_DEFAULT = "4", OBIETTIVO_PREVALENTE_DEFAULT = "2",
			STATO_CIVILE_DEFAULT = "1", TITOLO_DI_STUDIO_DEFAULT = "11", CONDIZIONE_PROFESSIONALE_DEFAULT = "8",
			TIPOLOGIA_MACROPROBLEMATICA_DEFAULT = "9", MICRO_PROBLEMATICA_DEFAULT = "64", RESIDENZA_DEFAULT = "35",
			PRESENZA_PIU_ADDETTI_DEFAULT = "N", STATO_CODICE_ISTAT_DEFAULT = "100", STATO_DESCR_DEFAULT = "ITALIA", 
			COMUNE_DESCR_DEFAULT = "TRIESTE", CAP_DEFAULT = "34121", COMUNE_COD_CAT_DEFAULT = "L424", 
			COMUNE_COD_ISTAT_DEFAULT = "032006";
	
	protected final CartellaSocialeWsClient wsClient;

	public CartellaSocialeWSUtilsBase(CartellaSocialeWsClient cartellaSocialeWsClient) {
		super();

		Preconditions.checkNotNull(cartellaSocialeWsClient);
		this.wsClient = cartellaSocialeWsClient;
		try {
			DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException ex) {
			throw new RuntimeException(ex);
		}

	}

	protected @Nullable String getCodiceOperatore() {
		return Strings.emptyToNull(	wsClient.getConfig("it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient.codiceOperatore"));
	}

	protected @Nullable String getComuneCartella() {
		return Strings.emptyToNull(wsClient.getConfig("it.wego.welfarego.cartellasocialews.CartellaSocialeWsClient.comuneCartella"));
	}

	protected String requireCodiceOperatore() {
		String codiceOperatore = getCodiceOperatore();
		Preconditions.checkNotNull(codiceOperatore);
		return codiceOperatore;
	}

	protected String requireComuneCartella() {
		String comuneCartella = getComuneCartella();
		Preconditions.checkNotNull(comuneCartella);
		return comuneCartella;
	}

	protected XMLGregorianCalendar getXmlDate(Date date) {
		try {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
			return date2;
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	protected XMLGregorianCalendar getXmlDate(String string) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
			Date date = formatter.parse(string);
			return getXmlDate(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
