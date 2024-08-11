/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.pagamenti;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import it.trieste.comune.ssc.json.JsonServlet;
import java.io.File;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public class ScaricaFileServlet extends JsonServlet {
    
	private static final long serialVersionUID = 1L;

	private final static int CACHE_LIMIT = 1000;
     
   private static   Map<String, String> map = new LinkedHashMap<String, String>(CACHE_LIMIT+1,.75F,true) {
		@Override
        protected boolean removeEldestEntry(Entry<String, String> eldest) {
        	logger.info("Sto cancellando il file"+  eldest.getValue() + " con chiava" + eldest.getKey() +" Il size della mappa è" + size());
            return size() > CACHE_LIMIT;
        }
    };
    private final static Logger logger = LoggerFactory.getLogger(ScaricaFileServlet.class);


  
    static {
        if (logger.isDebugEnabled()) {
            try {
                File file = File.createTempFile("test_", ".csv");
                FileUtils.writeStringToFile(file, "questo e' un file di prova\n");
                map.put("test", "atest");
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
    }  
    
    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        String action = getAction();
        map = (Map)Collections.synchronizedMap(map);
        logger.info("Prima di fare il get dentro la mappaabbiamo  è " + map.values());
        File file = null;
        if (action == null || Objects.equal(action, "getFileByCode")) {
            String fileCode = Strings.emptyToNull(getParameter("fileCode"));
            Preconditions.checkNotNull(fileCode, "missing parameter fileCode");
            file = new File(fileCode);
            logger.info("got file request for fileCode = '{}' file = '{}'", fileCode, file);
            Preconditions.checkNotNull(file, "file not found for fileCode = '%s'", fileCode);

        } else if (Objects.equal(action, "getFileForMandato")) {
            String idMandato = Strings.emptyToNull(getParameter("idMandato"));
            Preconditions.checkNotNull(idMandato, "e' necessario fornire un idMandato");
        } else {
            Preconditions.checkArgument(false, "unsupported action = '%s'", action);
        }
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
        FileUtils.copyFile(file, response.getOutputStream());
        return SKIP_RESPONSE;
    }




}
