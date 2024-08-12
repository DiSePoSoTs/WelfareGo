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
import it.trieste.comune.ssc.servlet.JsonServlet;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;
import it.wego.welfarego.persistence.utils.Connection;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author aleph
 */
public class ImportaFileServlet extends JsonServlet {

    @Override
    protected Object handleJsonRequest(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        EntityManager entityManager = Connection.getEntityManager();
        int rowNum = 0;
        try {
            byte[] fileData = Base64.decodeBase64(getParameter("file" + MULTIPART_BASE64));
            Preconditions.checkNotNull(fileData);
            HSSFWorkbook xlsWorkbook = new HSSFWorkbook(new ByteArrayInputStream(fileData));
            HSSFSheet xlsSheet = xlsWorkbook.getSheetAt(0);
            Iterator<Row> rowIterator = xlsSheet.iterator();
            Iterators.advance(rowIterator, 5); //skip headers

            Map<Integer, Exception> errors = Maps.newLinkedHashMap();
            rowNum = 1;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    entityManager.getTransaction().begin();
                    Cell mandatoIdCell = row.getCell(31), mandatoNumCell = row.getCell(32), dataCell = row.getCell(33);
                    getLogger().info("loaded row {} {} {}", new Object[]{mandatoIdCell, mandatoNumCell, dataCell});
                    Preconditions.checkArgument(mandatoIdCell != null && mandatoIdCell.getCellType() != CellType.BLANK, "id mandato mancante");
                    Integer mandatoId;
                    try {
                        mandatoId = mandatoIdCell.getCellType() == CellType.NUMERIC ? (int) mandatoIdCell.getNumericCellValue() : Integer.valueOf(mandatoIdCell.getStringCellValue());
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("id mandato mancante o invalido", ex);
                    }
                    Mandato mandato = entityManager.find(Mandato.class, mandatoId);
                    Preconditions.checkArgument(mandato != null, "mandato non trovato per id = %s", mandatoId);
                    Preconditions.checkArgument(!mandato.hasNumeroMandato(), "dati gia' caricati per id = %s", mandatoId);
                    try {
                        BigInteger mandatoNum = mandatoNumCell.getCellType() == CellType.NUMERIC ? BigInteger.valueOf((long) mandatoNumCell.getNumericCellValue()) : BigInteger.valueOf(Long.parseLong(mandatoNumCell.getStringCellValue()));
                        mandato.setNumeroMandato(mandatoNum);
                        for (MandatoDettaglio mandatoDettaglio : mandato.getMandatoDettaglioList()) {
                            mandatoDettaglio.setNumeroMandato(mandatoNum);
                        }
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("numero mandato mancante o invalido", ex);
                    }

                    entityManager.getTransaction().commit();
                } catch (Exception ex) {
                    if (entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().rollback();
                    }
                    errors.put(rowNum, ex);
                    getLogger().warn("", ex);
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
            return JsonBuilder.newInstance().withMessage(Joiner.on("<br />").join(messages)).withSuccess((rowNum - errors.size()) > 0).buildResponse();
        } finally {
            entityManager.close();
        }
    }
}
