package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.CartellaSociale;
import it.wego.welfarego.persistence.utils.Connection;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author giuseppe
 */
public class CartellaDao implements ORMDao {

    private EntityManager em;

    public CartellaDao(EntityManager em) {
        this.em = em;
    }

    public void insert(Object object) throws Exception {
        em.persist(object);
    }

    public void update(Object object) throws Exception {
        CartellaSociale cartellaSociale = (CartellaSociale) object;
        CartellaDao dao = new CartellaDao(em);
        CartellaSociale cartella = dao.findByCodAna(cartellaSociale.getCodAna());
        cartella.setAnagrafeSoc(cartellaSociale.getAnagrafeSoc());
        cartella.setDesCs(cartellaSociale.getDesCs());
        cartella.setDtAggAll(cartellaSociale.getDtAggAll());
        cartella.setDtApCs(cartellaSociale.getDtApCs());
        cartella.setDtChCs(cartellaSociale.getDtChCs());
        cartella.setIdParamTipAll(cartellaSociale.getIdParamTipAll());
    }

    public void delete(Object object) throws Exception {
        throw new RuntimeException("implementazione vuota");
    }

    public CartellaSociale findByCodAna(int codAna) {
        Query query = em.createNamedQuery("CartellaSociale.findByCodAna");
        query.setParameter("codAna", codAna);

        CartellaSociale cartella = (CartellaSociale) Connection.getSingleResult(query);
        return cartella;
    }
}
