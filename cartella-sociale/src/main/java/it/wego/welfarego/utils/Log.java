package it.wego.welfarego.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author giuseppe
 */

public class Log {

    private Log() {
    }
    public static final Logger ROOT = LoggerFactory.getLogger("it.wego.welfarego.cartellasociale");
    public static final Logger SQL = LoggerFactory.getLogger("it.wego.welfarego.cartellasociale.sql");
    public static final Logger WS = LoggerFactory.getLogger("it.wego.welfarego.cartellasociale.ws");
    public static final Logger APP = LoggerFactory.getLogger("it.wego.welfarego.cartellasociale.app");
}
