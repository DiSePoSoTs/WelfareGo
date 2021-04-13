package it.wego.welfarego.webdav;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.ConflictException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import it.wego.persistence.PersistenceAdapter;
import it.wego.webdav.DavResource;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author aleph
 */
public class DocumentDavResource extends DavResource {

	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(this.getClass());
	private PersistenceAdapter persistenceAdapter;
	private PaiInterventoPK paiInterventoPK;
	private Integer codPai;
	private String fileName, codTipdoc;
	private boolean indipendent;

	public DocumentDavResource(PaiInterventoPK paiInterventoPK, String codTipdoc) {
		this(paiInterventoPK, null, codTipdoc, true);
	}

	public DocumentDavResource(Integer codPai, String codTipdoc) {
		this(null, codPai, codTipdoc, true);
	}

	public DocumentDavResource(PaiInterventoPK paiInterventoPK, Integer codPai, String codTipdoc, boolean indipendent) {
		logger.debug("creating dav resource for doc : " + (paiInterventoPK != null ? paiInterventoPK.toString() : ("pai " + codPai)) + " , " + codTipdoc);
		this.paiInterventoPK = paiInterventoPK;
		this.codPai = codPai;
		this.codTipdoc = codTipdoc;
		PaiDocumento paiDocumento = getPaiDocumento();
		Validate.notNull(paiDocumento, "documento non trovato ( \"" + codTipdoc + "\" - " + (paiInterventoPK == null ? "pai " + codPai : paiInterventoPK) + " )");
		fileName = paiDocumento.getNomeFile().replaceAll("[^a-zA-Z0-9.]+", "_");
		if (this.indipendent = indipendent) {
			this.useInstanceUUID();
			this.cache();
			getPersistenceAdapter().close();
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public final PersistenceAdapter getPersistenceAdapter() {
		if (persistenceAdapter == null || persistenceAdapter.getEntityManager() == null || !persistenceAdapter.getEntityManager().isOpen()) {
			persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
		}
		return persistenceAdapter;
	}

	private PaiDocumento getPaiDocumento() {
		return new PaiDocumentoDao(getPersistenceAdapter().getEntityManager()).findLastDoc(codPai, paiInterventoPK, codTipdoc);
	}

	@Override
	public boolean isReadOnly() {
		return false; // TODO
	}
	private boolean storedNew = false;

	@Override
	protected void storeData(InputStream in, String string, String string1) throws IOException, ConflictException, NotAuthorizedException, BadRequestException {
		PaiDocumento paiDocumento = null;
		try {
			byte[] data = IOUtils.toByteArray(in);
			getPersistenceAdapter().initTransaction();
			paiDocumento = getPaiDocumento();
			if (!storedNew) {
				logger.debug("creating new version for document : " + paiDocumento);
				PaiDocumento newPaiDocumento = new PaiDocumento();
				newPaiDocumento.setPaiIntervento(paiDocumento.getPaiIntervento());
				newPaiDocumento.setCodPai(paiDocumento.getCodPai());
				newPaiDocumento.setCodTipdoc(codTipdoc);
				newPaiDocumento.setCodUteAut(paiDocumento.getCodUteAut());
				newPaiDocumento.setDtDoc(new Date());
				newPaiDocumento.setDtProt(paiDocumento.getDtProt());
				newPaiDocumento.setNomeFile(paiDocumento.getNomeFile());
				newPaiDocumento.setNumProt(paiDocumento.getNumProt());
				newPaiDocumento.setVer(paiDocumento.getVer().add(BigInteger.ONE));
				getPersistenceAdapter().insert(newPaiDocumento);
				paiDocumento = newPaiDocumento;
				storedNew = true;
			}
			logger.debug("saving document : " + paiDocumento);
			paiDocumento.setBlobDoc(new String(Base64.encodeBase64((data))));
			paiDocumento.setDtDoc(new Date()); //TODO verificare correttezza
			getPersistenceAdapter().commitTransaction();
			logger.debug("document saved : " + paiDocumento);
		} catch (Exception e) {
			logger.error("error saving document : " + paiDocumento, e);
			if (indipendent) {
				getPersistenceAdapter().close();
			}
			throw new IOException(e.toString());
		} finally {
			if (indipendent) {
				getPersistenceAdapter().close();
			}
		}
	}

	public String getName() {
		return fileName;
	}

	public void sendContent(OutputStream out, Range range, Map<String, String> map, String string) throws IOException, NotAuthorizedException, BadRequestException {
		PaiDocumento paiDocumento = getPaiDocumento();
		IOUtils.write(Base64.decodeBase64(paiDocumento.getBlobDoc()), out);
		out.flush();
		out.close();
		if (indipendent) {
			getPersistenceAdapter().close();
		}
	}

	public String getContentType(String string) {
		return "application/octet-stream"; //TODO
	}
}
