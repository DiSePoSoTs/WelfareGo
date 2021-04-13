package it.wego.welfarego.webdav;

import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.ConflictException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import it.wego.welfarego.persistence.entities.PaiInterventoPK;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author aleph
 */
public class MultipleDocumentDavResource extends DocumentDavResource {

	private List<DocumentDavResource> documentDavResources;

	public MultipleDocumentDavResource(List<PaiInterventoPK> paiInterventoPKList, String codTipdoc) {
		super(paiInterventoPKList.get(0), codTipdoc);
		documentDavResources = new ArrayList<DocumentDavResource>(paiInterventoPKList.size());
		for (PaiInterventoPK paiInterventoPK : paiInterventoPKList) {
			documentDavResources.add(new DocumentDavResource(paiInterventoPK, null, codTipdoc, false));
		}
	}

	@Override
	protected void storeData(InputStream in, String string, String string1) throws IOException, ConflictException, NotAuthorizedException, BadRequestException {
		try {
			byte[] data = IOUtils.toByteArray(in);
			getPersistenceAdapter().initTransaction();
			for (DocumentDavResource documentDavResource : documentDavResources) {
				documentDavResource.storeData(new ByteArrayInputStream(data), string, string1);
			}
			getPersistenceAdapter().commitTransaction();
			getPersistenceAdapter().close();
			getLogger().debug("document list saved");
		} catch (Exception e) {
			getLogger().error("error saving document list", e);
			getPersistenceAdapter().close();
			throw new IOException(e.toString());
		}
	}
}
