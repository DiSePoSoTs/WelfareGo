package it.wego.welfarego.pagamenti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Portlet principale che ha il compito di visualizzare la pagina principale
 * nella quale verrà caricata l'applicayione EXT-JS
 *
 * @author <a href="http://www.dot-com.si/">DOTCOM</a>
 */
@Controller
@RequestMapping("VIEW")
public class Pagamenti {
    
    private final Logger logger=LoggerFactory.getLogger(this.getClass());

    public Pagamenti() {
        logger.info("starting\n\n");
        logger.info("ready");
    }
    
    

    /**
     * Restituisce il nome della pagina contenente l'applicazione.
     *
     * @return Stringa con il nome della pagina.
     */
    @RequestMapping
    public String paginaPrincipale() {
        return ("app");
    }//paginaPrincipale
}//class Pagamenti

