package it.wego.welfarego.commons.servlet;

import it.wego.welfarego.azione.utils.IntalioAdapter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author aleph
 */
public class IntalioAdapterJobServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(IntalioAdapterJobServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("hand triggering intalio adapter job");
        IntalioAdapter.executeJob();
        resp.getWriter().append("OK").close();
        resp.flushBuffer();
    }
}
