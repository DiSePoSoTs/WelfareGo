/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import javax.persistence.EntityManager;

/**
 *
 * @author giuseppe
 */
public class FatturaDao extends PersistenceAdapter {

    public FatturaDao(EntityManager em) {
        super(em);
    }
}
