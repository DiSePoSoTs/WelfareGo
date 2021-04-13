package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.*;

public class PaiDocumentoDaoTest {

    @Test
    public void aa() throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");

        EntityManager entityManager = factory.createEntityManager();
        PaiDocumentoDao paiDocumentoDao = new PaiDocumentoDao(entityManager);
        PaiDocumento paiDocumento = paiDocumentoDao.findByIdDocumento(24659303);
        System.out.println(paiDocumento.getNomeFile());
        String blobDoc = paiDocumento.getBlobDoc();
        FileUtils.writeByteArrayToFile(new File("aaa_1.odt"), blobDoc.getBytes());
        byte[] decodeBase64 = Base64.decodeBase64(blobDoc);
        FileUtils.writeByteArrayToFile(new File("aaa_2.odt"), decodeBase64);
    }
}