/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.pagamenti;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.trieste.comune.ssc.json.JsonBuilder;
import it.trieste.comune.ssc.json.JsonResponse;
import it.trieste.comune.ssc.json.JsonServlet;
import it.wego.welfarego.persistence.utils.Connection;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.wego.welfarego.services.gestione_economica.AggiornaMandatiDaSicrawebService;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author aleph
 */
public class ImportaFileServlet extends JsonServlet {


	private static final long serialVersionUID = 1L;

	@Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {

        AggiornaMandatiDaSicrawebService aggiornaMandatiDaSicrawebService = new AggiornaMandatiDaSicrawebService();
        EntityManager entityManager = Connection.getEntityManager();
        int rowNum = 0;

        try {
            byte[] fileData = Base64.decodeBase64(getParameter("file" + MULTIPART_BASE64));
            Preconditions.checkNotNull(fileData);


            HSSFWorkbook xlsWorkbook = aggiornaMandatiDaSicrawebService.convert_byteArray_to_xls(fileData);
            HSSFSheet xlsSheet = xlsWorkbook.getSheetAt(0);
            Iterator<Row> rowIterator = xlsSheet.iterator();
            Iterators.advance(rowIterator, 1); //skip headers


            Map<Integer, Exception> errors = Maps.newLinkedHashMap();

            rowNum = 1;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Integer mandatoId = aggiornaMandatiDaSicrawebService.estrai_IdMandato(row);
                BigInteger numeroMandato = aggiornaMandatiDaSicrawebService.estrai_numeroMandato(row);

                try {
                    aggiornaMandatiDaSicrawebService.set_numero_mandato_su_mandato(entityManager, mandatoId, numeroMandato);
                } catch (Exception ex) {
                    errors.put(rowNum, ex);
                    getLogger().error("##catch nel while", ex);
                } finally {
                    rowNum++;
                }
            }
            rowNum--;

            List<String> messages = Lists.newLinkedList();
            if (!errors.isEmpty()) {
                messages.addAll(Collections2.transform(errors.entrySet(), new Function<Entry<Integer, Exception>, String>() {
                    public String apply(Entry<Integer, Exception> entry) {
                        return "riga " + entry.getKey() + " : " + (entry.getValue().getClass().equals(IllegalArgumentException.class) ? entry.getValue().getMessage() : entry.getValue().toString());
                    }
                   
                }));
                messages.add(0, "record con errori : " + (errors.size()));
            }
            messages.add(0, "record importati con successo : " + (rowNum - errors.size()));


            JsonBuilder jsonBuilder = JsonBuilder.newInstance();
            jsonBuilder.withMessage(Joiner.on("<br />").join(messages));
            jsonBuilder.withSuccess((rowNum - errors.size()) > 0);
            JsonResponse jsonResponse = jsonBuilder.buildResponse();

            return jsonResponse;
        } finally {
            entityManager.close();
        }
    }




}
