package it.wego.welfarego.pagamenti;

import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.UtentiDao;
import it.wego.welfarego.persistence.entities.Fattura;
import it.wego.welfarego.persistence.entities.FatturaDettaglio;
import it.wego.welfarego.persistence.entities.Utenti;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;


public class ExportFileUtilsTest {

    @Test
    public void aa() throws Exception {
        List<String> tag_dettagli_linee = new ArrayList<String>();
        tag_dettagli_linee.add("AAA");
        tag_dettagli_linee.add("BBB");
        tag_dettagli_linee.add("CCC");
        System.out.println(StringUtils.join(tag_dettagli_linee, "\n"));
    }


    @Test
    public void incrementa_prograssivo_generazione_file_zip(){


        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Query query = entityManager.createNamedQuery("incrementa_prograssivo_generazione_file_zip");
        query.executeUpdate();
        transaction.commit();

        query = entityManager.createNamedQuery("get_progressivo_generazione_file");
        List resultList = query.getResultList();
        BigDecimal progressivo = (BigDecimal) resultList.get(0);
        System.out.println(progressivo);

    }


    @Test
    public void espolora_dati() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        Fattura fattura3 = entityManager.find(Fattura.class, 24845213);
        System.out.println(fattura3);
    }


    @Test
    public void aag(){
        byte[] bytes = " - Serv fruiti periodo :".getBytes();
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        Charset iso8859 = Charset.forName("ISO-8859-1");
        CharBuffer decode = iso8859.decode(bb);
        String s = decode.toString();
        System.out.println(s);
    }

    @Test
    public void gestisci_fatturazione_elettronica() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Calendar data = Calendar.getInstance();
        File zipFile = new File("/tmp" + File.separator + "fatt_" + data.get(Calendar.YEAR) + "_" + (data.get(Calendar.MONTH) + 1) + "_" + data.get(Calendar.DAY_OF_MONTH) + "_" + data.get(Calendar.HOUR_OF_DAY) + "_" + data.get(Calendar.MINUTE) + "_" + data.get(Calendar.SECOND) + ".zip");
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        ExportFileUtils exportFileUtils = new ExportFileUtils(entityManager);
        List<Fattura> fatturaList = new ArrayList<Fattura>();
        PaiDocumentoDao paiDocumentoDao = new PaiDocumentoDao(entityManager);
        Fattura fattura2 = entityManager.find(Fattura.class, 24842401); // LBRLFR37C61F382A
        fattura2.setBollo(new BigDecimal(9.4324));
        fattura2.setImportoTotale(fattura2.getImportoTotale().add(new BigDecimal(0.12345)) );
        fattura2.setImpIva(fattura2.getImpIva().add(new BigDecimal(0.12345)));
        FatturaDettaglio fatturaDettaglio = fattura2.getFatturaDettaglioList().get(0);
        fatturaDettaglio.setImporto(fatturaDettaglio.getImporto().add(new BigDecimal(0.12345)));
        fatturaDettaglio.setQtInputata(fatturaDettaglio.getQtInputata().add(new BigDecimal(0.12345)));
        fatturaList.add(fattura2);

        UtentiDao utentiDao = new UtentiDao(entityManager);
        Utenti utente = utentiDao.findByUsername("admin");
        BigDecimal progressivoInvio = new BigDecimal(-13);
        boolean anteprima = true;
        exportFileUtils.gestisci_fatturazione_elettronica(fatturaList, zipOutputStream, paiDocumentoDao, utente, progressivoInvio, anteprima);


        zipOutputStream.close();

        transaction.commit();
    }


    @Test
    public void leggi_parametri_fattura_da_db(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

        ExportFileUtils aa = new ExportFileUtils(entityManager);
        Map<String, String> map = aa.leggi_parametri_fattura_da_db();
        System.out.println(map);
    }

    @Test
    public void aa2(){
        String aa = "IT44S0200802230000001170836";
        String bb = "IT 44 S 02008 02230 000001170836";
        System.out.println(aa.substring(5,10));
        System.out.println(aa.substring(10,15));
    }

}