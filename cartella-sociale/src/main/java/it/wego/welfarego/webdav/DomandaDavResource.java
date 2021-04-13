/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.webdav;

import it.wego.persistence.PersistenceAdapter;
import it.wego.webdav.DavResource;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.utils.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import javax.persistence.EntityManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.ConflictException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;


/**
 *
 * @author giuseppe
 */
public class DomandaDavResource extends DavResource {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    private PaiDocumento documento;
    private PersistenceAdapter persistenceAdapter;
    private String fileName, codTipdoc;
    private boolean storedNew = false;

    public DomandaDavResource(PaiDocumento documento, String codTipdoc) {
        this.documento = documento;
        this.codTipdoc = codTipdoc;
        this.fileName = documento.getNomeFile();
        this.useInstanceUUID();
        this.cache();
    }

    @Override
    public boolean isReadOnly() {
        return false; // TODO
    }

    @Override
    protected void storeData(InputStream in, String string, String string1) throws IOException, ConflictException, NotAuthorizedException, BadRequestException {
        EntityManager em = Connection.getEntityManager();
        try {
            byte[] data = IOUtils.toByteArray(in);
            em.getTransaction().begin();
            PaiDocumentoDao paiDocumentoDao = new PaiDocumentoDao(em);
            logger.debug("Salvo il documento : {}", documento);
            documento.setBlobDoc(new String(Base64.encodeBase64((data))));
            documento.setDtDoc(new Date());
            documento = paiDocumentoDao.insertDoc(documento);
            em.getTransaction().commit();
            em.detach(documento);
            logger.debug("Documento salvato : {}", documento);
        } catch (Exception e) {
            logger.error("Errore salvando il documento : " + documento, e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new IOException(e.toString());
        } finally {
            em.close();
        }
    }

    public String getName() {
        return fileName;
    }

    public void sendContent(OutputStream out, Range range, Map<String, String> map, String string) throws IOException, NotAuthorizedException, BadRequestException {
        IOUtils.write(Base64.decodeBase64(documento.getBlobDoc()), out);
        out.flush();
        out.close();
    }
}