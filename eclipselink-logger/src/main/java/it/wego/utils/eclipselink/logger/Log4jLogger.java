package it.wego.utils.eclipselink.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;

/**
 *
 * @author aleph
 */
public class Log4jLogger extends AbstractSessionLog {

    private final Logger logger = LogManager.getLogger("org.eclipse.persistence.Log4jLogger");

    @Override
    public void log(SessionLogEntry sessionLogEntry) {
        try {
            String message = sessionLogEntry.getMessage();
            if (message == null) {
                message = "empty log message from eclipselink";
            }
            Throwable ex = sessionLogEntry.getException();
            if (sessionLogEntry.getLevel() >= SessionLog.SEVERE) {
                if (ex != null) {
                    logger.error(message, ex);
                } else {
                    logger.error(message);
                }
            } else if (sessionLogEntry.getLevel() >= SessionLog.WARNING) {
                if (ex != null) {
                    logger.warn(message, ex);
                } else {
                    logger.warn(message);
                }
            } else {
                if (ex != null) {
                    logger.debug(message, ex);
                } else {
                    logger.debug(message);
                }
            }
        } catch (Exception ex) {
            logger.debug("error logging message", ex);
        }
    }
}
