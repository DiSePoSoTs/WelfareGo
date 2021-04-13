/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Mess
 */
public class WebUtils {

    public static final String REQUEST_PATH_PROP_KEY = "it.wego.webutils.requestPath";

    public static Map<String, String> getParametersMap(HttpServletRequest request) throws Exception {
        Map<String, String> parametersMap = new HashMap<String, String>();
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            fileItemFactory.setSizeThreshold(1 * 1024 * 1024);
            ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
            List items = upload.parseRequest(request);

            for (Iterator i = items.iterator(); i.hasNext();) {
                FileItem fileItem = (FileItem) i.next();
                if (fileItem.isFormField()) {
                    parametersMap.put(fileItem.getFieldName(), fileItem.getString());
                } else {
                    String nomeFile = fileItem.getName().substring(fileItem.getName().lastIndexOf('\\') + 1);
                    String tipoFile = fileItem.getContentType();
                    byte[] raw = fileItem.get();
                    String b64 = new String(Base64.encodeBase64(raw));
                    parametersMap.put(fileItem.getFieldName(), new String(raw, "UTF-8"));
                    parametersMap.put(fileItem.getFieldName() + "_b64", b64);
                    parametersMap.put(fileItem.getFieldName() + "_raw", new String(raw));
                    parametersMap.put(fileItem.getFieldName() + "_filename", nomeFile);
                    parametersMap.put(fileItem.getFieldName() + "_type", tipoFile);

                    fileItem.delete();
                }
            }
        } else {
        	
        	Map<String, String[]> parMap = request.getParameterMap();

        	parMap.entrySet().forEach(e -> parametersMap.put((String) e.getKey(), ((String[]) e.getValue())[0]));;

            parametersMap.put("rawData", IOUtils.toString(request.getInputStream()));
        }
        parametersMap.put(REQUEST_PATH_PROP_KEY, request.getRequestURL().toString());

        return parametersMap;
    }
}
