/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.UniqueForm;
import it.wego.welfarego.persistence.utils.Connection;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author giuseppe
 */
public class FormDao implements ORMDao {

    private EntityManager em;

    public FormDao(EntityManager em) {
        this.em = em;
    }

//    @Override
    public void insert(Object object) throws Exception {
    }

//    @Override
    public void update(Object object) throws Exception {
    }

//    @Override
    public void delete(Object object) throws Exception {
    }

    public UniqueForm findForm(String codForm) throws Exception {
        Query query = em.createQuery("SELECT f FROM UniqueForm f WHERE f.codForm = :codForm");
        query.setParameter("codForm", codForm);
        UniqueForm form = (UniqueForm) Connection.getSingleResult(query);
        if (form == null) {
            query = em.createQuery("SELECT f FROM UniqueForm f WHERE f.codForm = :codForm");
            query.setParameter("codForm", "default");
            form = (UniqueForm) Connection.getSingleResult(query);
        }
        return form;

    }
}
