package it.wego.welfarego.services.gestione_economica;


import it.wego.welfarego.persistence.dao.MandatoDao;
import it.wego.welfarego.persistence.dao.PaiInterventoDao;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AggiornaMandatiDaSicrawebService {
    public static final int INDICE_ID_MANDATO = 6;
    public static final int INDICE_NUMERO_MANDATO = 1;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public AggiornaMandatiDaSicrawebService() {
    }

    public HSSFWorkbook convert_byteArray_to_xls(byte[] fileData) throws IOException {
        return new HSSFWorkbook(new ByteArrayInputStream(fileData));
    }

    public BigInteger estrai_numeroMandato(Row row) {
        Cell mandatoNumCell = row.getCell(INDICE_NUMERO_MANDATO);
        return mandatoNumCell.getCellType() == CellType.NUMERIC ? BigInteger.valueOf((long) mandatoNumCell.getNumericCellValue()) : BigInteger.valueOf(Long.parseLong(mandatoNumCell.getStringCellValue()));
    }

    public Integer estrai_IdMandato(Row row) {
        Cell mandatoIdCell = row.getCell(INDICE_ID_MANDATO);

        Integer mandatoId = null;


        CellType cellType = mandatoIdCell.getCellType();
        if(cellType == CellType.NUMERIC){
            mandatoId =   (int) mandatoIdCell.getNumericCellValue();
        } else {
            String stringCellValue = mandatoIdCell.getStringCellValue();
            mandatoId =  Integer.valueOf(stringCellValue);
        }
        
        return mandatoId;
    }

    public void set_numero_mandato_su_mandato(EntityManager entityManager, Integer idMandato, BigInteger numeroMandato) throws Exception {


        MandatoDao mandatoDao = getMandatoDao(entityManager);
        PaiInterventoDao paiInterventoDao = getPaiInterventoDao(entityManager);
        try {
            entityManager.getTransaction().begin();

            Mandato mandato = mandatoDao.loadById(idMandato);

            mandatoDao.aggiornaNumeroMandato(mandato, numeroMandato);

            crea_lettera_produzione_mandato(paiInterventoDao, mandato);

            entityManager.getTransaction().commit();
            logger.info("aggiornato il mandato: {} {}", new Object[]{idMandato, numeroMandato});
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        }
    }


    void crea_lettera_produzione_mandato(PaiInterventoDao paiInterventoDao, Mandato mandato) throws Exception {

        if (shouldCreateMandato(mandato.getPaiIntervento(), mandato)) {
            paiInterventoDao.creaTaskProduzioneLetteraComunicazoneMandato(mandato);
        }

    }


    MandatoDao getMandatoDao(EntityManager entityManager) {
        return new MandatoDao(entityManager);
    }

    PaiInterventoDao getPaiInterventoDao(EntityManager entityManager) {
        return new PaiInterventoDao(entityManager);
    }


    /**
     * Metodo privato per capire se devo creare il mandato per quel tipo di intervento in base alla modalità di erogazione..
     * al momento la cosa si applica solo al fap..
     * poi magari vedremo di metterla in db
     */
    boolean shouldCreateMandato(PaiIntervento pi, Mandato mandato) {
        List<String> noMandato = new ArrayList<String>();
        List<String> mandatoWithConditions = new ArrayList<String>();
        noMandato.add("AZ008A");
        noMandato.add("AZ008B");
        noMandato.add("AZ007A");
        noMandato.add("AZ007A");
        noMandato.add("AZ009");
        noMandato.add("AZ010");
        mandatoWithConditions.add("EC001");
        mandatoWithConditions.add("EC002");
        mandatoWithConditions.add("EC003");
        mandatoWithConditions.add("EC004");
        mandatoWithConditions.add("EC005");

        // Produci lettere solo se modalità di erogazione accredito.(FAP)
        if (noMandato.contains(pi.getPaiInterventoPK().getCodTipint()) && mandato.getModalitaErogazione().equals("ACCREDITO")) {
            return false;
        }

        //produci mandato solo per determinate condizioni ( economica )
        else if (mandatoWithConditions.contains(pi.getPaiInterventoPK().getCodTipint())) {
            //se c'è un delegato produci
            if (mandato.getCodAnaBeneficiario() != mandato.getCodAnaDelegante()) {
                return true;
            }
            if (pi.getPai().getAnagrafeSoc().getNumCell() == null) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

}