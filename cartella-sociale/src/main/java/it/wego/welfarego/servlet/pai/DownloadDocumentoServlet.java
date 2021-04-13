package it.wego.welfarego.servlet.pai;


import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.wego.dynodtpp.DynamicOdtUtils;
import it.wego.extjs.servlet.AbstractJsonServlet;
import it.wego.extjs.servlet.JsonServlet;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.persistence.dao.ConfigurationDao;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.dao.TemplateDao;
import it.wego.welfarego.persistence.entities.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.document.DocumentFormat;
import org.jodconverter.document.DocumentFormatRegistry;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadDocumentoServlet extends JsonServlet {

	private static final long serialVersionUID = 1L;
	private static final DocumentFormatRegistry documentFormatRegistry = DefaultDocumentFormatRegistry.getInstance();

    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {


        Integer idDocumento = null;
        String idDocumentoAsString = getParameter("idDocumento");
        try {
            getLogger().debug("idDocumentoAsString: " + idDocumentoAsString);
            idDocumento = Integer.valueOf(idDocumentoAsString);
        }
        catch(Exception ex){
            throw new IllegalArgumentException(String.format("idDocumento:[%s] non valido", idDocumentoAsString));
        }

        PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
        EntityManager entityManager = persistenceAdapter.getEntityManager();
        PaiDocumentoDao paiDocumentoDao = new PaiDocumentoDao(entityManager);
        PaiDocumento paiDocumento = paiDocumentoDao.findByIdDocumento(idDocumento);
        if(paiDocumento == null){
            throw new IllegalArgumentException("nessun documento associato all'id: " + idDocumentoAsString);
        }

        String blobDoc = paiDocumento.getBlobDoc();
        DocumentFormat documentFormat = documentFormatRegistry.getFormatByExtension("odt");
        response.setContentType(documentFormat.getMediaType());
        String fileName = paiDocumento.getNomeFile();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "");
        IOUtils.write(Base64.decodeBase64(blobDoc), response.getOutputStream());

        return SKIP_RESPONSE;
    }
}
