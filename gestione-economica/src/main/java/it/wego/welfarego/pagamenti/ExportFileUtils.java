/*
 * To change this template,
 choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.pagamenti;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.util.PortalUtil;
import it.wego.welfarego.pagamenti.fatturazione_elettronica.FatturaElettronicaBuilder;
import it.wego.welfarego.pagamenti.pagamenti.service.import_export.Mandato_To_Riga_Xls_Pagamenti;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.ParametriIndataDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Fattura;
import it.wego.welfarego.persistence.entities.FatturaDettaglio;
import it.wego.welfarego.persistence.entities.Luogo;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.Pai;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.persistence.entities.PaiInterventoMese;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.ParametriIndata;
import it.wego.welfarego.persistence.entities.TipologiaParametri;
import it.wego.welfarego.persistence.entities.Utenti;
import it.wego.welfarego.persistence.utils.Connection;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.portlet.ResourceRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author aleph
 */
public class ExportFileUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EntityManager entityManager;
    private final static HashMap<String, String> unitaMis = new HashMap<String, String>();
    private NumberFormat fmt_IT = NumberFormat.getNumberInstance(Locale.ITALIAN);

    static {
        unitaMis.put("ore settimanali", "ore");
        unitaMis.put("ore mensili", "ore");
        unitaMis.put("pasti settimanali", "un.");
        unitaMis.put("pasti mensili", "un.");
    }

    private final static HashMap<String, String> descInt = new HashMap<String, String>();

    static {


        descInt.put("AZ013", "Pulizie");
        descInt.put("AZ014", "SAD");
        descInt.put("AZ011", "ADI");
        descInt.put("AZ012", "Pasti");
        descInt.put("AZ018", "PID-PULIZIE");
        descInt.put("AZ016", "PID-ADI");
        descInt.put("AZ017", "PID-PASTI");
    }

    public final static Iterable<String> intestazioniXlsPagamenti = Lists.newArrayList(

            "COD_NOMINATIVO_SICRAWEB",
            "PARTITA_IVA",
            "CODICE_FISCALE",
            "NOME",
            "COGNOME",
            "RAGIONE_SOCIALE",
            "IMPONIBILE",
            "IBAN",
            "NOTE",
            "NOTE_2_LINGUA",
            "NOTE_3_LINGUA",
            "NOME_DELEGATO",
            "COGNOME_DELEGATO",
            "CF_DELEGATO",
            "PARTITA_IVA_DELEGATO",
            "RAG_SOCIALE_DELEGATO",
            "IMPEGNO",
            "METODO_PAGAMENTO"
    );

    //var14a-fatturaPosteHeaders.29, var20a-fatturaPosteHeaders.35, var26a-fatturaPosteHeaders.41, var80a-fatturaPosteHeaders.95
    private static final Iterable<String> fatturaPosteHeaders = Collections.unmodifiableList(Arrays.asList(
            "Rigadestinatario1",
            "Rigadestinatario2",
            "Rigadestinatario3",
            "Rigadestinatario4",
            "NOME1",
            "INDIRIZZO",
            "CAP",
            "DEST",
            "PROV",
            "IBAN",
            "var01d", // 11
            "var02d",
            "var01s",
            "var02s",
            "vcampot",
            "var01a",
            "var02a",
            "var03a",
            "var04a",
            "var05a", // 20
            "var06a",
            "var07a",
            "var08a",
            "var09a",
            "var10a",
            "var11a",
            "var12a",
            "var13a",
            "var14a", // 29
            "var15a",
            "var16a",
            "var17a",
            "var18a",
            "var19a",
            "var20a", //35
            "var21a",
            "var22a",
            "var23a",
            "var24a",
            "var25a",//40
            "var26a",//41
            "var27a",
            "var28a",
            "var29a",
            "var30a",
            "var31a",
            "var32a",
            "var33a",
            "var34a",
            "var35a",//50
            "var36a",
            "var37a",
            "var38a",
            "var39a",
            "var40a",
            "var41a",
            "var42a",
            "var43a",
            "var44a",
            "var45a",//60
            "var46a",
            "var47a",
            "var48a",
            "var49a",
            "var50a",
            "var51a",
            "var52a",
            "var53a",
            "var54a",
            "var55a",//70
            "var56a",
            "var57a",
            "var58a",
            "var59a",
            "var60a",
            "var61a",
            "var62a",
            "var63a",
            "var64a",
            "var65a",//80
            "var66a",
            "var67a",
            "var68a",
            "var69a",
            "var70a",
            "var71a",
            "var72a",
            "var73a",
            "var74a",
            "var75a",//90
            "var76a",
            "var77a",
            "var78a",
            "var79a",
            "var80a",//95
            "var81a",
            "var82a",
            "var83a",
            "xratat",
            "scadet"));//100

    public ExportFileUtils(EntityManager entityManager) {
        this.entityManager = entityManager;
//        this.parametriIndataDao = new ParametriIndataDao(entityManager);
    }

    private ParametriIndata getParameter(String codParam) {
        return new ParametriIndataDao(entityManager).findOneByTipParamCodParam(Parametri.PARAMETRI_FATTURE, codParam);
    }

    private String getParamVar79A() {
        return getParameter("var79a").getTxt1Param();
    }

    private String getParamCodiceServizioFatturazione() {
        return getParameter("codserfatt").getTxt1Param();
    }

    private BigDecimal getParam0139() {
        return getParameter("par0139").getDecimalParam();
    }

    private String getParamCausaleRegistrazioneEconomica() {
        return getParameter("causregeco").getTxt1Param();
    }

    private String getParamNumeroAccertamento() {
        return getParameter("numaccert").getTxt1Param();
    }

    private ParametriIndata getParamIvaDefault() {
        return new ParametriIndataDao(entityManager).findOneByTipParamCodParam(Parametri.ALIQUOTA_IVA, Parametri.ALIQUOTA_IVA_IVA_ORDINARIA);
    }

    public static class XlsCell {

        public final Object cellStyle, cellValue;

        public XlsCell(Object cellValue, Object cellStyle) {
            this.cellStyle = cellStyle;
            this.cellValue = cellValue;
        }
    }

    public static final Object US_DATE_FORMAT = new Object();

    private static void writeXls(Iterable<? extends Iterable> rows,
                                 OutputStream out) throws IOException {
        HSSFWorkbook xlsDocument = new HSSFWorkbook();
        HSSFSheet xlsSheet = xlsDocument.createSheet();

        final CellStyle dateStyleIt = xlsDocument.createCellStyle();
        dateStyleIt.setDataFormat(xlsDocument.createDataFormat().getFormat("dd/mm/yyyy"));
        //so che questo formato è uguale a quello sopra ma meglio cambiare qui che non in mezzo mondo.
        final CellStyle dateStyleUs = xlsDocument.createCellStyle();
        dateStyleUs.setDataFormat(xlsDocument.createDataFormat().getFormat("dd/mm/yyyy"));

        int rowNum = 0;
        for (Iterable row : rows) {
            HSSFRow xlsRow = xlsSheet.createRow(rowNum++);
            int cellNum = 0;
            for (Object cellValue : row) {
                Object cellStyle = null;
                if (cellValue instanceof XlsCell) {
                    cellStyle = ((XlsCell) cellValue).cellStyle;
                    cellValue = ((XlsCell) cellValue).cellValue;
                }
                HSSFCell xlsCell = xlsRow.createCell(cellNum++);
                if (cellValue instanceof Date) {
                    xlsCell.setCellValue((Date) cellValue);
                    xlsCell.setCellStyle(dateStyleIt);
                } else if (cellValue instanceof Number) {
                    xlsCell.setCellValue(((Number) cellValue).doubleValue());
                } else {
                    xlsCell.setCellValue(cellValue == null ? null : cellValue.toString());
                }
                if (cellStyle instanceof HSSFCellStyle) {
                    xlsCell.setCellStyle((HSSFCellStyle) cellStyle);
                } else if (cellStyle == US_DATE_FORMAT) {
                    xlsCell.setCellStyle(dateStyleUs);
                }
            }
        }
        for (int colNul = Iterables.size(rows.iterator().next()); colNul > 0; colNul--) {
            xlsSheet.autoSizeColumn(colNul);
        }
        xlsDocument.write(out);
        out.flush();
    }

    private static byte[] writeXlsToByteArray(Iterable<? extends Iterable> rows) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            writeXls(rows, out);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return out.toByteArray();
    }

    public static byte[] getPagamentiXlsFileData(Iterable<Mandato> mandatoList) {
        //purifico la lista dai mandati che hanno importo uguale a 0 in modo che non compaino sul file e che non esca fuori una roba a null.
        List<Mandato> mandatowithoutZero = new ArrayList<Mandato>();
        Iterator i = mandatoList.iterator();
        while (i.hasNext()) {
            Mandato m = (Mandato) i.next();
            if ("-".equalsIgnoreCase(m.getNomeDelegante())) {
                m.setNomeDelegante("");
            }
//            ID73
//            if (m.getImporto().compareTo(BigDecimal.ZERO) > 0) {
                mandatowithoutZero.add(m);
//            }
        }

        Mandato_To_Riga_Xls_Pagamenti mandato_to_riga_xls_pagamenti = new Mandato_To_Riga_Xls_Pagamenti();
        Iterable<Iterable> iterable = Iterables.transform(mandatowithoutZero, mandato_to_riga_xls_pagamenti);

        return writeXlsToByteArray(Iterables.concat(Arrays.asList(intestazioniXlsPagamenti), iterable));
    }


    private static final Function<String, String> modalitaErogazioneToCsv = new Function<String, String>() {
        public String apply(String input) {
            return Objects.equal(input, "PER_CASSA") ? "CONTA" : "ACBAN";
        }
    };


    public static File createFatturaExportFile(String basePath,
                                               Iterable<Fattura> fatturaList, ResourceRequest request, EntityManager entityManager2, boolean anteprima) throws FileNotFoundException,
            IOException, PortalException, SystemException, ParseException {
        final EntityManager entityManager = Connection.getEntityManager();
        try {
            return new ExportFileUtils(entityManager).doCreateFatturaExportFile(basePath, fatturaList, request, entityManager2, anteprima);
        } finally {
            entityManager.close();
        }
    }

    private File doCreateFatturaExportFile(String basePath,
                                           Iterable<Fattura> fatturaList, ResourceRequest request, EntityManager entityManager2, boolean anteprima) throws FileNotFoundException, IOException, PortalException, SystemException {

        PaiDocumentoDao paiDocumentoDao = new PaiDocumentoDao(entityManager2);
        UtentiDao utentiDao = new UtentiDao(entityManager2);
        String login = PortalUtil.getUser(request).getLogin();
        Utenti utente = utentiDao.findByUsername(login);

        BigDecimal progressivoInvio;
        if(anteprima){
            progressivoInvio = get_progressivo_generazione_file();
        }else{
            progressivoInvio = incrementa_progressivoInvio();
        }

        logger.info("progressivoInvio: " + progressivoInvio);

        Calendar data = Calendar.getInstance();

        String meseAnno = StringUtils.leftPad(fatturaList.iterator().next().getMeseDiRiferimento().toString(), 2, "0") + fatturaList.iterator().next().getFatturaDettaglioList().iterator().next().getAnnoEff();

        File zipFile = new File(basePath + File.separator + "fatt_" + data.get(Calendar.YEAR) + "_" + (data.get(Calendar.MONTH) + 1) + "_" + data.get(Calendar.DAY_OF_MONTH) + "_" + data.get(Calendar.HOUR_OF_DAY) + "_" + data.get(Calendar.MINUTE) + "_" + data.get(Calendar.SECOND) + ".zip");
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
        byte[] xlsPosteFileData = createFatturaPosteXlsFileData(fatturaList);


        String nomeFile_pptCopia = "FATTURE_" + meseAnno + "_PPTTCOPIA.xls";
        zipOutputStream.putNextEntry(new ZipEntry(nomeFile_pptCopia));
        IOUtils.write(xlsPosteFileData, zipOutputStream);


        byte[] txtDeltadatorFileData = createFatturaDeltadatorFileData(fatturaList);
        String nomeFile_fatture_flusso = "FATTURE_FLUSSO_" + meseAnno + "_DeltaDator.TXT";
        zipOutputStream.putNextEntry(new ZipEntry(nomeFile_fatture_flusso));
        IOUtils.write(txtDeltadatorFileData, zipOutputStream);


        gestisci_fatturazione_elettronica(fatturaList, zipOutputStream, paiDocumentoDao, utente, progressivoInvio, anteprima);


        if (anteprima == false) {
            for (Fattura fattura : fatturaList) {

                String tipoDoc = "Zip fatturazione";

                PaiIntervento intervento = fattura.getPaiIntervento();
                Pai pai = intervento.getPai();


                paiDocumentoDao.createDoc(pai, utente, tipoDoc, Base64.encodeBase64String(xlsPosteFileData), nomeFile_pptCopia);
                paiDocumentoDao.createDoc(pai, utente, tipoDoc, Base64.encodeBase64String(txtDeltadatorFileData), nomeFile_fatture_flusso);
            }
        }
        zipOutputStream.close();
        return zipFile;
    }

    /**
     * <div>devo:</div>
     * <ol>
     * <li>creare xml fattura elettronica da record fattura</li>
     * <li>aggiungere xml al file zip</li>
     * <li>salvare xml nella tabella pai_documento</li>
     * </ol>
     *  @param fatturaList
     * @param zipOutputStream
     * @param paiDocumentoDao
     * @param progressivoInvio
     * @param anteprima
     */
    void gestisci_fatturazione_elettronica(Iterable<Fattura> fatturaList, ZipOutputStream zipOutputStream, PaiDocumentoDao paiDocumentoDao, Utenti utente, BigDecimal progressivoInvio, boolean anteprima) throws IOException {


        Map<String, String> parametri = leggi_parametri_fattura_da_db();

        ParametriIndata idParamIva = new ParametriIndataDao(entityManager).findOneByTipParamCodParam(Parametri.ALIQUOTA_IVA, "or");


        Iterator<Fattura> iterator = fatturaList.iterator();

        int i = 1;
        while (iterator.hasNext()) {
            Fattura fattura_corrente = iterator.next();

            String numeroFattura = String.valueOf(fattura_corrente.getNumFatt());
            String dataCreazione = (new DateTime()).toString("yyyyMMdd");

            String nomeFile;
            if(anteprima){
                nomeFile = "IT_" + dataCreazione + "_" + progressivoInvio + "_" + numeroFattura + "_" + i + ".xml";
                i = i+1;
            }else {
                nomeFile = "IT_" + dataCreazione + "_" + progressivoInvio + "_" + numeroFattura + ".xml";
            }
            logger.info("nome file: " + nomeFile);

            PaiIntervento intervento = fattura_corrente.getPaiIntervento();
            Pai pai = intervento.getPai();

            // passo 1: creo xml della fattura elettronica
            String xmlFatturaElettronica = crea_fattura_elettronica(fattura_corrente, progressivoInvio, idParamIva, parametri);

            // passo 2: aggiungo xml nel file zip
            zipOutputStream.putNextEntry(new ZipEntry(nomeFile));
            IOUtils.write(xmlFatturaElettronica.getBytes(), zipOutputStream);


            // passo 3: salvo xml nel db
            paiDocumentoDao.createDoc(pai, utente, "fattura elettronica", Base64.encodeBase64String(xmlFatturaElettronica.getBytes()), nomeFile);
        }


    }

    Map<String, String> leggi_parametri_fattura_da_db() {
        Map<String, String> parametri = new HashMap<String, String>();
        Query query = entityManager.createNamedQuery("leggi_parametri_fattura_da_db");
        List<Object[]> resultList = query.getResultList();
        for (Object[] voceParametro : resultList) {
            String codParam = (String) voceParametro[1];
            BigDecimal decimalPart = (BigDecimal) voceParametro[4];
            String txtParam = (String) voceParametro[5];


            String value = txtParam;

            if (value == null && decimalPart != null) {
                value = decimalPart.toString();
            }

            parametri.put(codParam, value);
        }

        return parametri;
    }

    private BigDecimal incrementa_progressivoInvio() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Query query = entityManager.createNamedQuery("incrementa_prograssivo_generazione_file_zip");
        query.executeUpdate();
        transaction.commit();
        transaction = entityManager.getTransaction();

        query = entityManager.createNamedQuery("get_progressivo_generazione_file");
        List resultList = query.getResultList();
        BigDecimal progressivo = (BigDecimal) resultList.get(0);
        return progressivo;
    }

    private BigDecimal get_progressivo_generazione_file() {
        Query query = entityManager.createNamedQuery("get_progressivo_generazione_file");
        List resultList = query.getResultList();
        BigDecimal progressivo = (BigDecimal) resultList.get(0);
        return progressivo;
    }


    /**
     * (<.*?>|</.*>)
     * (<(.*?)>|</$2>)
     *
     * @param fattura_corrente
     * @param progressivoInvio
     * @param idParamIva
     * @param parametri
     * @return
     */
    String crea_fattura_elettronica(Fattura fattura_corrente, BigDecimal progressivoInvio, ParametriIndata idParamIva, Map<String, String> parametri) throws IOException {
        AnagrafeSoc anagrafeSoc = fattura_corrente.getPaiIntervento().getPai().getCartellaSociale().getAnagrafeSoc();

        String xml_path = "it/wego/welfarego/pagamenti/fatturazione_elettronica/template_fattura_elettronica.xml";
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(xml_path);
        String template_fattura_elettronica = IOUtils.toString(resourceAsStream,"ISO-8859-1");

        List<FatturaDettaglio> fatturaDettaglioList = fattura_corrente.getFatturaDettaglioList();
        BigDecimal bollo = fattura_corrente.getBollo();
        Date timbro = fattura_corrente.getTimbro();
        int numFatt = fattura_corrente.getNumFatt();
        Date dataScadenza = fattura_corrente.getScadenza();

        BigDecimal importo = calcola_Importo(fatturaDettaglioList, bollo);

        FatturaElettronicaBuilder fatturaElettronicaBuilder = new FatturaElettronicaBuilder(template_fattura_elettronica);

        fatturaElettronicaBuilder.setHeader(parametri);

        fatturaElettronicaBuilder.setProgeressivoInvio(progressivoInvio);
        fatturaElettronicaBuilder.setCessionarioCommittente(anagrafeSoc);

        fatturaElettronicaBuilder.setDatiGeneraliDocumento(timbro, numFatt, parametri, bollo);
        fatturaElettronicaBuilder.setDatiRiepilogo(importo, parametri, bollo);

        fatturaElettronicaBuilder.setDatiPagamento(importo, parametri, dataScadenza);
        fatturaElettronicaBuilder.setDettaglioLinee(fatturaDettaglioList, idParamIva, parametri, bollo);

        fatturaElettronicaBuilder.setDatiTrasmissione(parametri);
        fatturaElettronicaBuilder.setCedentePrestatore(parametri);

        fatturaElettronicaBuilder.setDatiBeniServizi(fattura_corrente, parametri, importo);
        fatturaElettronicaBuilder.setCondizioniPagamento(parametri);

        String xmlFatturaElettronica = fatturaElettronicaBuilder.getXmlFatturaElettronica();
        xmlFatturaElettronica = new String(xmlFatturaElettronica.getBytes("ISO-8859-1"), Charset.forName("ISO-8859-1"));
        return xmlFatturaElettronica;
    }

    private static BigDecimal sum(Iterable<BigDecimal> nums) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal num : nums) {
            sum = sum.add(num);
        }
        return sum;
    }


    public abstract class FatturaRow {

        public Date getDataDiEmissione() {
            return getFattura().getTimbro();
        }

        private Integer getMeseDiRiferimento() {
            return getFattura().getMeseRif();
        }

        private FatturaDettaglio getPrimoFatturaDettaglio() {
            List<FatturaDettaglio> dettagli = getFattura().getFatturaDettaglioList();
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

        private FatturaDettaglio getUltimoFatturaDettaglio() {
            List<FatturaDettaglio> dettagli = getFattura().getFatturaDettaglioList();
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

        private ParametriIndata getIdFascia() {
            return getFattura().getIdParamFascia();
        }

        private abstract class InterventoRecord {

            public Fattura getFattura() {
                return FatturaRow.this.getFattura();
            }

            public abstract PaiIntervento getPaiIntervento();

            public String getUnitaDiMisura() {
                if (getPaiIntervento() != null) {
                    return unitaMis.get(getPaiIntervento().getIdParamUniMis().getDesParam());
                } else {
                    return "";
                }
            }

            public String getDescInt() {
                if (getPaiIntervento() != null) {
                    if (descInt.get(getPaiIntervento().getPaiInterventoPK().getCodTipint()) != null) {
                        String primaParte = descInt.get(getPaiIntervento().getPaiInterventoPK().getCodTipint());
                        String descrizioneUnita = getPaiIntervento().getTipologiaIntervento().getIdParamUniMis().getDesParam();
                        if (descrizioneUnita.contains("mens")) {
                            return primaParte + " mens.";
                        }
                        if (descrizioneUnita.contains("sett")) {
                            return primaParte + " sett.";
                        }
                        return primaParte;

                    } else {
                        return getPaiIntervento().getTipologiaIntervento().getDesTipint();
                    }
                } else {
                    return "";
                }
            }

            //            public abstract List<PaiInterventoMese> getPaiInterventoMeseList();
            public Iterable<FatturaDettaglio> getFatturaDettaglioList() {
                return Iterables.filter(getFattura().getFatturaDettaglioList(), new Predicate<FatturaDettaglio>() {
                    public boolean apply(FatturaDettaglio fatturaDettaglio) {
                        if (!fatturaDettaglio.getPaiInterventoMeseList().isEmpty()) {
                            return Objects.equal(fatturaDettaglio.getPaiInterventoMeseList().iterator().next().getPaiIntervento(), getPaiIntervento());
                        } else {
                            return false;
                        }
                    }
                });
            }
//                return Iterables.concat(Iterables.transform(getFattura().getFatturaDettaglioList(), new Function<FatturaDettaglio, Iterable<PaiInterventoMese>>() {
//                    public Iterable<PaiInterventoMese> apply(FatturaDettaglio fatturaDettaglio) {
//                        return fatturaDettaglio.getPaiInterventoMeseList();
//                    }
//                }));
//            }

            public BigDecimal getQuantita() {
//                return sum(Iterables.transform(getPaiInterventoMeseList(), new Function<PaiInterventoMese, BigDecimal>() {
//                    public BigDecimal apply(PaiInterventoMese paiInterventoMese) {
//                        return paiInterventoMese.getBdgConsQta();
//                    }
//                }));
                return sum(Iterables.transform(getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
                    public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
                        return fatturaDettaglio.getQtInputata();
                    }
                }));
            }

            public BigDecimal getCostoBase() {
                if (getPaiIntervento() != null) {
                    return getImportoStandard(getPaiIntervento(), entityManager);
                } else {
                    return BigDecimal.ZERO;
                }
            }

            public BigDecimal getImporto() {
//                return sum(Iterables.transform(getPaiInterventoMeseList(), new Function<PaiInterventoMese, BigDecimal>() {
//                    public BigDecimal apply(PaiInterventoMese paiInterventoMese) {
//                        return paiInterventoMese.getBdgConsEur();
//                    }
//                }));
                return hasEsenzioneIva() ? getImportoSenzaIva() : getImportoConIva();
            }

            public BigDecimal getImportoSenzaIva() {
//                return sum(Iterables.transform(getPaiInterventoMeseList(), new Function<PaiInterventoMese, BigDecimal>() {
//                    public BigDecimal apply(PaiInterventoMese paiInterventoMese) {
//                        return paiInterventoMese.getBdgConsEur();
//                    }
//                }));
                return sum(Iterables.transform(getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
                    public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
                        return fatturaDettaglio.getImportoSenzaIva();
                    }
                }));
            }

            public BigDecimal getImportoConIva() {
//                return sum(Iterables.transform(getPaiInterventoMeseList(), new Function<PaiInterventoMese, BigDecimal>() {
//                    public BigDecimal apply(PaiInterventoMese paiInterventoMese) {
//                        return paiInterventoMese.getBdgConsEur();
//                    }
//                }));
                return sum(Iterables.transform(getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
                    public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
                        return fatturaDettaglio.getImportoConIva();
                    }
                }));
            }
//
//            public BigDecimal getSommaVariazioni() {
////                return sum(Iterables.transform(getPaiInterventoMeseList(), new Function<PaiInterventoMese, BigDecimal>() {
////                    public BigDecimal apply(PaiInterventoMese paiInterventoMese) {
////                        return paiInterventoMese.getBdgConsEur();
////                    }
////                }));
////                return sum(Iterables.transform(getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
////                    public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
////                        return Objects.firstNonNull(fatturaDettaglio.getAumento(), BigDecimal.ZERO).subtract(Objects.firstNonNull(fatturaDettaglio.getRiduzione(), BigDecimal.ZERO)).add(Objects.firstNonNull(fatturaDettaglio.getVarStraord(), BigDecimal.ZERO));
////                    }
////                }));
//                return 
//            }

            public BigDecimal getVariazionePositivaSenzaIva() {
                return sum(Iterables.transform(getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
                    public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
                        return fatturaDettaglio.getTotaleVariazioniPositiveSenzaIva();
                    }
                }));
            }

            public BigDecimal getVariazionePositiva() {
                return sum(Iterables.transform(getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
                    public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
                        return hasEsenzioneIva() ? fatturaDettaglio.getTotaleVariazioniPositiveSenzaIva() : fatturaDettaglio.getTotaleVariazioniPositiveConIva();
                    }
                }));
            }

            public BigDecimal getVariazioneNegativaSenzaIva() {
                return sum(Iterables.transform(getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
                    public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
                        return fatturaDettaglio.getTotaleVariazioniNegativeSenzaIva();
                    }
                }));
            }

            public BigDecimal getVariazioneNegativa() {
                return sum(Iterables.transform(getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
                    public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
                        return hasEsenzioneIva() ? fatturaDettaglio.getTotaleVariazioniNegativeSenzaIva() : fatturaDettaglio.getTotaleVariazioniNegativeConIva();
                    }
                }));
            }

            private ParametriIndata getIvaParam() {
                if (hasEsenzioneIva()) {
                    return FatturaRow.this.getIvaParam();
                } else {
                    return MoreObjects.firstNonNull(getPaiIntervento().getTipologiaIntervento().getIpAliquotaIva(), new ParametriIndataDao(entityManager).findOneByTipParamCodParam(Parametri.ALIQUOTA_IVA, "or"));
                }
            }

            public BigDecimal getIvaPercentuale() {
                return getIvaParam().getDecimalPercentageParamAsPercentage();
            }

            public BigDecimal getAmmontareIvaCalcolato() {
//                return getFattura().getIdParamIva().getDecimalParam().multiply(getCostoTot()); //TODO check this
                return getIvaParam().getDecimalPercentageParamAsDecimal().multiply(getImporto());
            }

            private class InterventoVariazioneRecord extends InterventoRecord {

                private final BigDecimal importo, costoBase;

                public InterventoVariazioneRecord(BigDecimal costoBase, BigDecimal importo) {
                    this.importo = importo;
                    this.costoBase = costoBase;
                }

                @Override
                public BigDecimal getImporto() {
                    return importo;
                }

                @Override
                public BigDecimal getCostoBase() {
                    return costoBase;
                }

                @Override
                public BigDecimal getQuantita() {
                    return BigDecimal.ONE;
                }

                @Override
                public String getUnitaDiMisura() {
                    return "";
                }

                @Override
                public String getDescInt() {
                    return getImporto().compareTo(BigDecimal.ZERO) > 0 ? "aumento" : "riduzione";
                }

                @Override
                public PaiIntervento getPaiIntervento() {
                    return InterventoRecord.this.getPaiIntervento();
                }
            }
        }

        //        public abstract List<Fattura> getFatturaList();
//
//        public Fattura getFattura() {
//            return getFatturaList().iterator().next();
//        }
        public abstract Fattura getFattura();

        public AnagrafeSoc getAnagrafeSocObbligato() {
            //ritorno a chi va la fattura
            return getFattura().getPaiIntervento().getDsCodAnaBenef();
        }

        //        public abstract BigDecimal getQuota();
        public Pai getPai() {
            return getFattura().getPaiIntervento().getPai();
        }

        public BigDecimal getCostoTotaleConEventualeBollo() {
            return getTotaleImponibile().add(getCostoBollo());
//            return getFattura().getImportoTotale();
//            return sum(Iterables.transform(getFatturaList(), new Function<Fattura, BigDecimal>() {
//                public BigDecimal apply(Fattura fattura) {
//                    return fattura.getImportoTotale();
//                }
//            }));
        }

        //        public BigDecimal getCostoTotale() {
//            return getFattura().getImportoTotale();
////            return getCostoTotaleSenzaBollo();
//        }
        public BigDecimal getCostoTotaleSenzaBollo() {
            return getFattura().getImportoTotale();
        }

        public BigDecimal getCostoBollo() {
            return getFattura().getBollo();
        }

        public ParametriIndata getIvaParam() {
            return getFattura().getIdParamIva();
        }

        public boolean hasEsenzioneIva() {
            return getFattura().hasEsenzioneIva();
        }

        public BigDecimal getImportoTotale() {
            return getFattura().getImportoTotale();
        }

        public BigDecimal getIvaPercentuale() {
            return getIvaParam().getDecimalPercentageParamAsPercentage();
        }

        public BigDecimal getImpostaTotale() {
            return getFattura().getImpIva();
        }

        public BigDecimal getTotaleImponibile() {
            return sum(Iterables.transform(getFattura().getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
                public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
                    return fatturaDettaglio.getImporto();
                }
            }));
        }

        public Date getDataDx() {
            return getFattura().getTimbro();// TODO
        }

        public String getResponsabileProcedimento() {
            return getFattura().getPaiIntervento().getTipologiaIntervento().getResponsabileProcedimento();
        }

        public String getUfficioDiRiferimento() {
            return getFattura().getPaiIntervento().getTipologiaIntervento().getUfficioDiRiferimento();
        }

        public Date getDataScadenza() {
            return getFattura().getScadenza();
        }

        public List<InterventoRecord> getFatturaRowInterventoRecordList() {
            List<FatturaDettaglio> fattureDettaglio = getFattura().getFatturaDettaglioList();
            for (Fattura f : getFattura().getFatturaList()) {
                for (FatturaDettaglio d : f.getFatturaDettaglioList()) {
                    fattureDettaglio.add(d);
                }
            }
            return Lists.newArrayList(Iterables.concat(Iterables.transform(Sets.newLinkedHashSet(Iterables.transform(fattureDettaglio, new Function<FatturaDettaglio, PaiIntervento>() {
                public PaiIntervento apply(FatturaDettaglio fatturaDettaglio) {
                    return fatturaDettaglio.getPaiIntervento();
                }
            })), new Function<PaiIntervento, Iterable<InterventoRecord>>() {
                public Iterable<InterventoRecord> apply(final PaiIntervento paiIntervento) {
                    //                    final PaiIntervento paiIntervento = entry.getKey();
                    InterventoRecord interventoRecord = new InterventoRecord() {
                        @Override
                        public PaiIntervento getPaiIntervento() {
                            return paiIntervento;
                        }
                    };
                    ArrayList<InterventoRecord> interventoRecordList = Lists.newArrayList(interventoRecord);
                    BigDecimal variazionePositiva = interventoRecord.getVariazionePositiva();
                    BigDecimal variazioneNegativa = interventoRecord.getVariazioneNegativa();
                    if (BigDecimal.ZERO.compareTo(variazionePositiva) != 0) {
                        interventoRecordList.add(interventoRecord.new InterventoVariazioneRecord(interventoRecord.getVariazionePositivaSenzaIva(), variazionePositiva));
                    }
                    if (BigDecimal.ZERO.compareTo(variazioneNegativa) != 0) {
                        interventoRecordList.add(interventoRecord.new InterventoVariazioneRecord(interventoRecord.getVariazioneNegativaSenzaIva().negate(), variazioneNegativa.negate()));
                    }
                    return interventoRecordList;
                }
            })));
        }
    }

    private final Function<Iterable<Fattura>, Iterable<FatturaRow>> fattura_2_FatturaRow = new Function<Iterable<Fattura>, Iterable<FatturaRow>>() {
        public Iterable<FatturaRow> apply(Iterable<Fattura> fatturaList) {
            return Lists.newArrayList(Iterables.transform(fatturaList, new Function<Fattura, FatturaRow>() {
                public FatturaRow apply(final Fattura fattura) {
                    return new FatturaRow() {
                        @Override
                        public Fattura getFattura() {
                            return fattura;
                        }
                    };
                }
            }));
        }
    };

    public static String getMeseDesc(int mese) {
        return DateFormatSymbols.getInstance(Locale.ITALY).getMonths()[mese - 1];
    }

    private byte[] createFatturaPosteXlsFileData(Iterable<Fattura> fatturaList) throws IOException {
        Iterable<Fattura> fattureWithoutZero = new ArrayList<Fattura>();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<Iterable<String>> intestazione_xls = Collections.singletonList(fatturaPosteHeaders);


        // parti da qua per generare gli xls
        Iterable<FatturaRow> FatturaRow_list = fattura_2_FatturaRow.apply(fatturaList);

        Function<FatturaRow, Iterable> FatturaRow_2_iterable = new Function<FatturaRow, Iterable>() {
            private final DateFormat dateFormatIta = new SimpleDateFormat("dd/MM/yyyy");
            private final int MAX_INT_RECORDS = 9, INT_RECORD_SIZE = 6;

            public Iterable apply(FatturaRow fatturaRow) {

                List row = Lists.newArrayList();
                AnagrafeSoc anagrafeSocPagante = fatturaRow.getAnagrafeSocObbligato();
                AnagrafeSoc anagrafeSocTitolareFattura = fatturaRow.getPai().getAnagrafeSoc();
                Date dataDiEmissione = fatturaRow.getDataDiEmissione();
                Calendar calEmiss = Calendar.getInstance();
                calEmiss.setTime(dataDiEmissione);


                String cognomeNome = anagrafeSocTitolareFattura.getCognomeNome().toUpperCase(),
                        indirizzo = anagrafeSocTitolareFattura.getLuogoDestinazione().getIndirizzoText(),
                        cap = Strings.nullToEmpty(anagrafeSocTitolareFattura.getLuogoDestinazione().getCap()),
                        comune = anagrafeSocTitolareFattura.getLuogoDestinazione().getComuneText(),
                        provincia = anagrafeSocTitolareFattura.getLuogoDestinazione().getProvinciaText();


                Luogo luogoTitolareResidenza = anagrafeSocTitolareFattura.getLuogoResidenza() != null ? anagrafeSocTitolareFattura.getLuogoResidenza() : anagrafeSocTitolareFattura.getLuogoDestinazione();

                Luogo luogoPaganteResidenza = anagrafeSocPagante.getLuogoResidenza() != null ? anagrafeSocPagante.getLuogoResidenza() : anagrafeSocPagante.getLuogoDestinazione();
                String indirizzoResidenza = luogoTitolareResidenza.getIndirizzoText(),
                        capResidenza = Strings.nullToEmpty(luogoTitolareResidenza.getCap()),
                        comuneResidenza = luogoTitolareResidenza.getComuneText(),
                        provinciaResidenza = luogoTitolareResidenza.getProvinciaText(),
                        paganteNome = anagrafeSocPagante.getFlgPersFg().equals("G") ? anagrafeSocPagante.getRagSoc().toUpperCase() : anagrafeSocPagante.getCognomeNome().toUpperCase(),
                        paganteIndirizzo = anagrafeSocPagante.getLuogoDestinazione().getIndirizzoText(),
                        paganteCap = Strings.nullToEmpty(anagrafeSocPagante.getLuogoDestinazione().getCap()),
                        paganteComune = anagrafeSocPagante.getLuogoDestinazione().getComuneText(),
                        paganteProvincia = anagrafeSocPagante.getLuogoDestinazione().getProvinciaText(),
                        codiceUnoTreNove = new DecimalFormat("0000").format(getParam0139()),
                        annoUsufrutto = fatturaRow.getFattura().getAnno(),
                        annoMeseUsufrutto = annoUsufrutto + new DecimalFormat("00").format(calEmiss.get(Calendar.MONTH) + 1),
                        numeroFatturaSeiCifre = new DecimalFormat("000000").format(fatturaRow.getFattura().getNumFatt()),
                        campoTi = codiceUnoTreNove + annoMeseUsufrutto + numeroFatturaSeiCifre,
                        dataDxItaFormat = dateFormatIta.format(dataDiEmissione),
                        campoZeroDueD = "SD FT. N. " + campoTi + " dd. " + dataDxItaFormat,
                        codiceUnoTreNoveDec = new DecimalFormat("#.##").format(Double.valueOf(codiceUnoTreNove) / 100),
                        campoZeroUnoA = codiceUnoTreNoveDec + "/" + annoUsufrutto + "/" + numeroFatturaSeiCifre,
                        datiGeneraliDocumento_Causale;

                datiGeneraliDocumento_Causale = getVar01d_oppure_var04a(fatturaRow);
                XlsCell datiGeneraliDocumento_Data = new XlsCell(dataDiEmissione, US_DATE_FORMAT);

                BigDecimal percentualeFascia = null;
                ParametriIndata fascia = fatturaRow.getIdFascia();
                if (fascia != null) {
                    percentualeFascia = fascia.getDecimalParam();
                } else {
                    percentualeFascia = new BigDecimal(BigInteger.ZERO);
                }
                percentualeFascia = (new BigDecimal("100").subtract(percentualeFascia)).divide(new BigDecimal("100"));


                row.addAll(Arrays.asList(
                        cognomeNome, //1
                        anagrafeSocPagante.equals(anagrafeSocTitolareFattura) ? (StringUtils.isBlank(anagrafeSocPagante.getPresso()) ? indirizzo : "C/O " + anagrafeSocPagante.getPresso()) : "C/O " + paganteNome,
                        anagrafeSocPagante.equals(anagrafeSocTitolareFattura) ? (StringUtils.isBlank(anagrafeSocPagante.getPresso()) ? cap + " " + comune + " (" + provincia + ")" : indirizzo) : paganteIndirizzo,
                        anagrafeSocPagante.equals(anagrafeSocTitolareFattura) ? (StringUtils.isBlank(anagrafeSocPagante.getPresso()) ? null : cap + " " + comune + " (" + provincia + ")") : paganteCap + " " + paganteComune + " (" + paganteProvincia + ")",
                        cognomeNome,
                        indirizzoResidenza,
                        capResidenza,
                        comuneResidenza,
                        provincia,
                        "N", //IBAN, sempre N (mah) variabile 10
                        datiGeneraliDocumento_Causale,  //var01d o var04a
                        campoZeroDueD,
                        fatturaRow.getCostoTotaleConEventualeBollo(),
                        campoZeroDueD,
                        campoTi,
                        campoZeroUnoA,
                        datiGeneraliDocumento_Data, // var02a
                        "Fascia contributiva: " + (fascia == null ? "-" : fascia.getDesParam()),
                        datiGeneraliDocumento_Causale,
                        anagrafeSocTitolareFattura.getCodFisc(),
                        null,
                        null // variabile 22
                ));
                List<FatturaRow.InterventoRecord> fatturaRowInterventoRecordList = fatturaRow.getFatturaRowInterventoRecordList();
                Preconditions.checkArgument(fatturaRowInterventoRecordList.size() <= MAX_INT_RECORDS);
                Iterator<FatturaRow.InterventoRecord> fatturaRowInterventoRecordIterator = fatturaRowInterventoRecordList.iterator();
                fmt_IT.setMaximumFractionDigits(2);
                fmt_IT.setMinimumFractionDigits(2);

                // vedo di riempire MAX_INT_RECORDS*num_prop_che_interessano (9*[var14a..var19a]) colonne dell'xls con gli interventi
                for (int i = 0; i < MAX_INT_RECORDS; i++) {
                    if (fatturaRowInterventoRecordIterator.hasNext()) {

                        FatturaRow.InterventoRecord interventoRecord = fatturaRowInterventoRecordIterator.next();

                        boolean importo_maggiore_di_zero = interventoRecord.getImporto().compareTo(BigDecimal.ZERO) > 0;

                        if (importo_maggiore_di_zero) {
                            row.addAll(Arrays.asList(
                                    interventoRecord.getDescInt(),          //var14a
                                    interventoRecord.getUnitaDiMisura(),    //var15a
                                    interventoRecord.getQuantita(),         //var16
                                    interventoRecord.getCostoBase().multiply(percentualeFascia).setScale(2, BigDecimal.ROUND_DOWN),//var17a
                                    interventoRecord.getIvaPercentuale(),   //var18a
                                    interventoRecord.getImporto()));        //var19a
                        } else {
                            row.addAll(Collections.nCopies(INT_RECORD_SIZE, null));
                        }
                    } else {
                        row.addAll(Collections.nCopies(INT_RECORD_SIZE, null));
                    }
                }

                /*
                var14a-fatturaPosteHeaders.29, var20a-fatturaPosteHeaders.35, var26a-fatturaPosteHeaders.41, var80a-fatturaPosteHeaders.95
                 */
                row.addAll(Arrays.asList(
                        fatturaRow.getFattura().hasEsenzioneIva() ? "(Operazione esente da imposta)" : "", // var62a
                        //    fatturaRow.getImportoTotale(),
                        //cambiamento ...verificare
                        fatturaRow.getTotaleImponibile(),
                        fatturaRow.getIvaPercentuale(),
                        fatturaRow.getImpostaTotale(),
                        fatturaRow.getTotaleImponibile(), // totale imponibile ?
                        fatturaRow.getFattura().hasEsenzioneIva() ? fatturaRow.getIvaParam().getDesParam() : "", // var67a
                        null, null, null, null, null, null, null, null, null,
                        fatturaRow.getTotaleImponibile(),
                        fatturaRow.getCostoBollo(), //var78a
                        getParamVar79A(),
                        Objects.equal(BigDecimal.ZERO, fatturaRow.getCostoBollo()) ? "(Esente bollo)" : "Bollo",  //var80a
                        campoZeroDueD,
                        fatturaRow.getResponsabileProcedimento(),
                        fatturaRow.getUfficioDiRiferimento(), fatturaRow.getCostoTotaleConEventualeBollo(),
                        new XlsCell(fatturaRow.getDataScadenza(), US_DATE_FORMAT)));
                return row;
            }

            public String getVar01d_oppure_var04a(FatturaRow fatturaRow) {
                Pai pai = fatturaRow.getFattura().getPaiIntervento().getPai();
                Integer meseRif = fatturaRow.getMeseDiRiferimento();
                String var01d_oppure_var04a;
                if (meseRif != null) {
                    var01d_oppure_var04a = pai.getIdParamUot().getDesParam() + " – Servizi fruiti periodo: " + getMeseDesc(fatturaRow.getMeseDiRiferimento()) + " " + fatturaRow.getFattura().getFatturaDettaglioList().get(0).getAnnoEff();
                } else {
                    FatturaDettaglio primoDettaglio = fatturaRow.getPrimoFatturaDettaglio();
                    FatturaDettaglio ultimoDettaglio = fatturaRow.getUltimoFatturaDettaglio();
                    if (primoDettaglio.getAnnoEff() == ultimoDettaglio.getAnnoEff() && primoDettaglio.getMeseEff() == ultimoDettaglio.getMeseEff()) {
                        var01d_oppure_var04a = pai.getIdParamUot().getDesParam() + " – Servizi fruiti periodo: " + getMeseDesc(primoDettaglio.getMeseEff()) + " " + primoDettaglio.getAnnoEff();
                    } else {
                        var01d_oppure_var04a = pai.getIdParamUot().getDesParam() + " – Servizi fruiti periodo: " + getMeseDesc(primoDettaglio.getMeseEff()) + " " + primoDettaglio.getAnnoEff() + " - " + getMeseDesc(ultimoDettaglio.getMeseEff()) + " " + ultimoDettaglio.getAnnoEff();
                    }
                }
                return var01d_oppure_var04a;
            }


        };
        Iterable<Iterable> righe_xls = Iterables.transform(FatturaRow_list, FatturaRow_2_iterable);
        Iterable<Iterable> rows = Iterables.concat(intestazione_xls, righe_xls);


        writeXls(rows, out);
        return out.toByteArray();
    }


    public abstract class FatturaDDRow {

        public abstract Fattura getFattura();

        public String getCodiceFiscaleUtente() {
            return getFattura().getCodFisc();
        }

        public String getDescrizioneIntervento() {
            return getFattura().getPaiIntervento().getTipologiaIntervento().getDesTipint();
        }

        public String getMeseServiziDesc() {
            return getMeseDesc(getFattura().getMeseDiRiferimento()) + " " + getFattura().getFatturaDettaglioList().get(0).getAnnoEff();
        }

        public Date getDataEmissione() {
            return getFattura().getTimbro();
        }

        public Date getDataScadenza() {
            return getFattura().getScadenza();
        }

        public boolean shouldUseAccredito() {
            return getFattura().getPaiIntervento().shouldUseAccredito();
        }

        public BigDecimal getTotaleFattura() {
            return getFattura().getImportoTotale();
        }

        public abstract class FatturaAliquotaRow {

            /**
             * TIPO RECORD 02: DATI DI RIEPILOGO FISCALE (un record per ogni
             * aliquota ) CAMPO	LUNGHEZZA	TIPO	DESCRIZIONE	Obbligatorio 1	2	N
             * Tipo Record ('02')	S	rimane sempre 02 2	5	C	Codice tipo imposta o
             * esenzione	S	E' un codice ,la transcodifica è presente nel file in
             * allegato 3	1	C	Segno ( '+' o '-')	S 4	12	N	Imponibile (formato 10
             * interi + 2 decimali)	S	Imponibile 5	12	N	Imposta / Tassa (formato
             * 10 interi + 2 decimali)	S	Non presente in welfare go ( a meno che
             * non si intenda il bollo )
             */
            public abstract Iterable<FatturaDettaglio> getFatturaDettaglioList();

            public abstract ParametriIndata getParametroAliquota();
//            {
//                return getFattura().hasEsenzioneIva() ? getFattura().getIdParamIva() : getPaiIntervento().getTipologiaIntervento().getIpAliquotaIva();
//            }

            //            private PaiIntervento getPaiIntervento() {
//                return getFatturaDettaglioList().iterator().next().getPaiIntervento();
//            }
            public String getCodiceEsenzione() {
                if (getParametroAliquota().getIdParam().getCodParam().length() == 5) {
                    return getParametroAliquota().getIdParam().getCodParam();
                } else {
                    return getParametroAliquota().getTxt1Param();
                }
//                String codParam = getParametroAliquota().getIdParam().getCodParam();
                //            return getFatturaList().iterator().next().getPaiIntervento().getTipologiaIntervento().getCodiceAliquotaIva(); //TODO
                //                return getFatturaDettaglioList().iterator().next().getPaiIntervento().getTipologiaIntervento().getCodiceAliquotaIva();
            }

            //            public BigDecimal getTotale() {
//                retur
//            }
            public BigDecimal getImponibile() {
                return sum(Iterables.transform(getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
                    public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
                        return fatturaDettaglio.getImporto();
                    }
                }));
            }

            public BigDecimal getImposta() {
                return getImponibile().multiply(getParametroAliquota().getDecimalPercentageParamAsDecimal());
//            }
//                return sum(Iterables.transform(getFatturaDettaglioList(), new Function<FatturaDettaglio, BigDecimal>() {
//                    public BigDecimal apply(FatturaDettaglio fatturaDettaglio) {
//                        return fatturaDettaglio.getImporto();
//                    }
//                }));
            }
        }

        public List<FatturaAliquotaRow> getFatturaAliquotaRowList() {
            if (getFattura().hasEsenzioneIva()) {
                return Collections.<FatturaAliquotaRow>singletonList(new FatturaAliquotaRow() {
                    @Override
                    public List<FatturaDettaglio> getFatturaDettaglioList() {
                        return FatturaDDRow.this.getFattura().getFatturaDettaglioList();
                    }

                    @Override
                    public ParametriIndata getParametroAliquota() {
                        return FatturaDDRow.this.getFattura().getIdParamIva();
                    }
                });
            } else {
                return Lists.newArrayList(Iterables.transform(Multimaps.index(getFattura().getFatturaDettaglioList(), new Function<FatturaDettaglio, ParametriIndata>() {
                    public ParametriIndata apply(FatturaDettaglio fatturaDettaglio) {
                        return MoreObjects.firstNonNull(fatturaDettaglio.getIdParamIva(), getParamIvaDefault());
                    }
                }).asMap().entrySet(), new Function<Map.Entry<ParametriIndata, Collection<FatturaDettaglio>>, FatturaAliquotaRow>() {
                    public FatturaAliquotaRow apply(final Entry<ParametriIndata, Collection<FatturaDettaglio>> entry) {
//                        final List<FatturaDettaglio> fatturaDettaglioList = Lists.newArrayList(entry.getValue());
                        return new FatturaAliquotaRow() {
                            @Override
                            public Iterable<FatturaDettaglio> getFatturaDettaglioList() {
                                return entry.getValue();
                            }

                            @Override
                            public ParametriIndata getParametroAliquota() {
                                return entry.getKey();
                            }
                        };
                    }
                }));
            }
        }

        private abstract class FatturaCapitoloRow {

            /**
             * TIPO RECORD 03 DATI DI RIEPILOGO FINANZIARIO (Un record per ogni
             * capitolo o accertamento) CAMPO	LUNGHEZZA	TIPO	DESCRIZIONE
             * Obbligatorio 1	2	N	Tipo Record ('03')	S	Sempre uguale 2	8	N
             * Codice capitolo	N	Non presente in welfare go da aggiungere in un
             * template Di norma sempre uguale, potrebbe cambiare 3	11	N	Numero
             * Accertamento	S Non presente in welfare go Di norma sempreuguale
             * potrebbe cambiare. 4 1	C	Segno ('+' o '-')	S 5	12	N	Importo
             * (formato 10 interi + 2 decimali)	S	Importo totale fattura
             */
//            public abstract List<PaiInterventoMese> getPaiInterventoMeseList();
            public abstract Integer getCodiceCapitolo();

            public abstract Iterable<PaiInterventoMese> getPaiInterventoMeseList();

            public String getNumeroAccentramento() {
                return getParamNumeroAccertamento();
            }

            public BigDecimal getImporto() {
                return sum(Iterables.transform(getPaiInterventoMeseList(), new Function<PaiInterventoMese, BigDecimal>() {
                    public BigDecimal apply(PaiInterventoMese paiInterventoMese) {
                        return paiInterventoMese.getBdgConsEur();
                    }
                }));
            }
        }

        public List<FatturaCapitoloRow> getFatturaCapitoloRowList() {
            return Lists.newArrayList(Iterables.transform(Multimaps.index(getFattura().getPaiInterventoMeseList(), new Function<PaiInterventoMese, Integer>() {
                public Integer apply(PaiInterventoMese paiInterventoMese) {
                    return paiInterventoMese.getBudgetTipIntervento().getCodCap();
                }
            }).asMap().entrySet(), new Function<Map.Entry<Integer, Collection<PaiInterventoMese>>, FatturaCapitoloRow>() {
                public FatturaCapitoloRow apply(final Entry<Integer, Collection<PaiInterventoMese>> entry) {
                    return new FatturaCapitoloRow() {
                        @Override
                        public Integer getCodiceCapitolo() {
                            return entry.getKey();
                        }

                        @Override
                        public Iterable<PaiInterventoMese> getPaiInterventoMeseList() {
                            return entry.getValue();
                        }
                    };
                }
            }));
        }
    }

    private final Function<Iterable<Fattura>, Iterable<FatturaDDRow>> fattura_2_FatturaDDRow = new Function<Iterable<Fattura>, Iterable<FatturaDDRow>>() {
        public Iterable<FatturaDDRow> apply(Iterable<Fattura> input) {
            return Iterables.transform(input, new Function<Fattura, FatturaDDRow>() {
                public FatturaDDRow apply(final Fattura fattura) {
                    return new FatturaDDRow() {
                        @Override
                        public Fattura getFattura() {
                            return fattura;
                        }
                    };
                }
            });
        }
    };

    /**
     * metodo per togliere accenti strani
     *
     * @param s
     * @return
     */

    private static String stripAccents(String s) {

        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }


    private byte[] createFatturaDeltadatorFileData(Iterable<Fattura> fatturaList) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(byteArrayOutputStream);
        Date currentDate = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        final DecimalFormat tenDigitDecimalFormat = new DecimalFormat("0000000000.00");
        String codiceServizioFatturazione = getParamCodiceServizioFatturazione(),
                causaleRegistrazioneEconomica = getParamCausaleRegistrazioneEconomica();
        Preconditions.checkArgument(codiceServizioFatturazione.length() == 5);
        Preconditions.checkArgument(causaleRegistrazioneEconomica.length() == 5);
        // TIPO RECORD 00 Identificazione di flusso: 
        out.print("00FA"); //fisso
        out.print(codiceServizioFatturazione); // 5 char
        out.print(dateFormat.format(currentDate)); // 8 char
        out.print("12345"); // 5 char
        out.println();


        for (FatturaDDRow fatturaDdRow : fattura_2_FatturaDDRow.apply(fatturaList)) {

            /*
             1	2	N	Tipo Record ('01')	S	Sempre ''01'
             2	5	C	Codice servizio di fatturazione	S	 Non presente in welfare go-Di norma è  sempre uguale (SADYY dove YY sono le ultime cifre dell'anno) cambia ogni anno 
             3	16	C	Codice fiscale nominativo	nota 1	Codice fiscale utente
             4	11	N	Partita iva nominativo	nota 1	Di noma è sembre bianca quindi 11 spazi bianchi – lasciare 11 spazi bianchi 
             5	120	C	Descrizione fattura	nota 1	“Per I servizi di <TIPO INTERVENTO> usufruiti nel periodo di <MESE>�?
             6	8	N	Data Emissione (formato ddMMyyyy)	S	Data emissione fattura 
             7	5	C	Causale di registrazione economica	S	NON PRESENTE IN WELFARE GO  i nroma sempre uguale potrebbe cambiare 
             8	5	C	Codice Pagamento	N	In base a quanto detto nei dati specifici dell'intervento  bisogna mettere CONTA  se il pagamento  è in contanti ACBAN per accredito bancario 
             9	8	N	Data Scadenza (formato ddMMyyyy)	N	Data Scadenza fattura
             10	7	N	Numero Fattura	S	Numero fattura ( non presente in welfarego ? ) 
             11	1	C	Segno ('+' o '-')	S	
             12	12	N	Importo Fattura (formato 10 interi + 2 decimali)	S	Importo totale fattura 
             */

            String codiceFiscale = fatturaDdRow.getCodiceFiscaleUtente();
            Preconditions.checkArgument(codiceFiscale.length() == 16);
            String desc1 = "Per I servizi di ", desc2 = " usufruiti nel periodo di ", meseServDesc = fatturaDdRow.getMeseServiziDesc().toUpperCase(),
                    descrizione = StringUtils.rightPad(
                            desc1
                                    + StringUtils.abbreviate(fatturaDdRow.getDescrizioneIntervento(), 120 - (desc1.length() + desc2.length() + meseServDesc.length()))
                                    + desc2
                                    + meseServDesc, 120);
            String numeroFattura = new DecimalFormat("0000000").format(fatturaDdRow.getFattura().getNumFatt()),
                    importoTotale = "+" + tenDigitDecimalFormat.format(fatturaDdRow.getTotaleFattura()).replace(".", "").replace(",", "");

            out.print("01"); // 2
            out.print(codiceServizioFatturazione); // 5
            out.print(codiceFiscale); // 16
            out.print(StringUtils.repeat(" ", 11)); // 11
            out.print(descrizione); // 120
            out.print(dateFormat.format(fatturaDdRow.getDataEmissione())); // 8
            out.print(causaleRegistrazioneEconomica); // 5
            out.print(fatturaDdRow.shouldUseAccredito() ? "ACBAN" : "CONTA"); // 5
            out.print(dateFormat.format(fatturaDdRow.getDataScadenza())); // 8
            out.print(numeroFattura); // 7
            out.print(importoTotale); // 13
            out.println();


            for (FatturaDDRow.FatturaAliquotaRow fatturaAliquotaRow : fatturaDdRow.getFatturaAliquotaRowList()) {

                // String codiceEsenzione = fatturaAliquotaRow.getCodiceEsenzione();
                //codice esenzione fisso 30/01/2017
                String codiceEsenzione = "E1000";
                Preconditions.checkArgument(codiceEsenzione.length() == 5);

                out.print("02"); // 2
                out.print(codiceEsenzione); // 5
                out.print("+" + tenDigitDecimalFormat.format(fatturaAliquotaRow.getImponibile()).replace(".", "").replace(",", "")); // 13
                out.print(tenDigitDecimalFormat.format(fatturaAliquotaRow.getImposta()).replace(".", "").replace(",", "")); // 12
                out.println();
            }

            if (fatturaDdRow.getFattura().getBollo() != null && fatturaDdRow.getFattura().getBollo().compareTo(BigDecimal.ZERO) > 0) {

                out.print("02"); // 2
                out.print("NF000"); // 5
                out.print("+" + tenDigitDecimalFormat.format(fatturaDdRow.getFattura().getBollo()).replace(".", "").replace(",", "")); // 13
                out.print(tenDigitDecimalFormat.format(00).replace(".", "").replace(",", "")); // 12
                out.println();
            }

            for (FatturaDDRow.FatturaCapitoloRow fatturaCapitoloRow : fatturaDdRow.getFatturaCapitoloRowList()) {

                out.print("03"); // 2
                out.print(StringUtils.leftPad(fatturaCapitoloRow.getCodiceCapitolo().toString(), 8, "0")); // 8
                String numeroAccertamento = fatturaCapitoloRow.getNumeroAccentramento();
                Preconditions.checkArgument(numeroAccertamento.length() == 11);
                out.print(numeroAccertamento); // 11
                out.print(importoTotale); // 13
                out.println();
                break;
            }

        }


        out.flush();

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Ritorna il costo standard di un intervento.
     *
     * @param i
     * @param e
     * @return
     */
    private BigDecimal getImportoStandard(PaiIntervento i, EntityManager e) {
        BigDecimal importoStandard = null;
        //se ha una struttura
        if (i.getTariffa() != null) {
            importoStandard = i.getTariffa().getCosto();
        } else {
            importoStandard = i.getTipologiaIntervento().getImpStdCosto();
        }
        return importoStandard;
    }


    public BigDecimal calcola_Importo(List<FatturaDettaglio> fatturaDettaglioList, BigDecimal bollo) {
        BigDecimal imponibileImporto = new BigDecimal(0);

        imponibileImporto = imponibileImporto.add(bollo);

        for (FatturaDettaglio voceFattura : fatturaDettaglioList) {
            imponibileImporto = imponibileImporto.add(voceFattura.getImporto());
        }
        return imponibileImporto;
    }
}
