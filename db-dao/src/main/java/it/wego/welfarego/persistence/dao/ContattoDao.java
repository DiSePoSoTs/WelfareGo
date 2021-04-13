/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.wego.welfarego.persistence.dao;

import javax.persistence.EntityManager;

/**
 *
 * @author giuseppe
 */
public class ContattoDao implements ORMDao {

    private EntityManager em;

    public ContattoDao(EntityManager em){
        this.em = em;
    }

    public void insert(Object object) throws Exception {
        em.persist(object);
    }

    public void update(Object object) throws Exception {
        throw new RuntimeException("implementazione vuota");
    }

    public void delete(Object object) throws Exception {
        throw new RuntimeException("implementazione vuota");
    }
}
