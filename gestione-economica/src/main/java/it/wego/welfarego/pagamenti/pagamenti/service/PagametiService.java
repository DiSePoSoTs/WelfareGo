package it.wego.welfarego.pagamenti.pagamenti.service;

import it.wego.welfarego.pagamenti.pagamenti.dao.PagamentiDao;
import it.wego.welfarego.persistence.entities.Mandato;
import it.wego.welfarego.persistence.entities.MandatoDettaglio;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;

public class PagametiService {
    private PagamentiDao pagamentiDao;

    public PagametiService(){
        pagamentiDao = new PagamentiDao();
    }

    public Mandato getMandato(String id ) {
        return pagamentiDao.getMandato(id);
    }

    public BigDecimal calcolaAssenzeTotali(MandatoDettaglio dettaglio) {
        return pagamentiDao.calcolaAssenzeTotali(dettaglio);
    }

}
