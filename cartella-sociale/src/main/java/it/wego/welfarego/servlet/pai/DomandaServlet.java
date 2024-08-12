package it.wego.welfarego.servlet.pai;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.webdav.DavResourceContext;
import it.wego.webdav.DavUtils;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.dao.PaiDocumentoDao;
import it.wego.welfarego.persistence.entities.PaiDocumento;
import it.wego.welfarego.persistence.utils.Connection;
import it.wego.welfarego.webdav.DomandaDavResource;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author giuseppe
 */
public class DomandaServlet extends JsonServlet {

    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        EntityManager em = Connection.getEntityManager();
        try {
            String codDoc = Strings.emptyToNull(request.getParameter("codDoc"));
            Preconditions.checkNotNull(codDoc, "Non è stato inviato nessun codice documento (codDoc)");
            PaiDocumentoDao paiDocumentoDao = new PaiDocumentoDao(em);
            PaiDocumento documento = paiDocumentoDao.findLastDocByIdDocumento(Integer.valueOf(codDoc));
            Preconditions.checkNotNull(documento, "nessun documento trovato per codDoc = %s", codDoc);
            em.detach(documento);
            String miltonPort = String.valueOf(Parametri.getMiltonPort(em));
            String path = request.getRequestURL().toString();
            DavResourceContext resourceContext = DavResourceContext.getResourceContext(path, "/DomandaServlet", miltonPort);
            DomandaDavResource documentDavResource = new DomandaDavResource(documento, "domanda");
            Object params = DavUtils.getDavParams(documentDavResource, resourceContext);
            return params;
        } 
        finally {

            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
