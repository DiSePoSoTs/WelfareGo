/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.ListaAttesa;
import it.wego.welfarego.persistence.entities.UniqueForm;
import it.wego.welfarego.persistence.utils.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author giuseppe
 */
public class ListaAttesaDao implements ORMDao {

    private EntityManager em;

    public ListaAttesaDao(EntityManager em) {
        this.em = em;
    }


    public void insert(Object object) throws Exception {
        throw new RuntimeException("implementazione vuota");
    }


    public void update(Object object) throws Exception {
        throw new RuntimeException("implementazione vuota");
    }

    
    public void delete(Object object) throws Exception {
        throw new RuntimeException("implementazione vuota");
    }

    public List<ListaAttesa> findAll() {
        Query query = em.createQuery("SELECT l FROM ListaAttesa l");
        List<ListaAttesa> listeAttesa = query.getResultList();
        return listeAttesa;

    }
    
    public ListaAttesa findByKey(int codListaAtt)
    {
        Query query = em.createNamedQuery("ListaAttesa.findByCodListaAtt");
        query.setParameter("codListaAtt", codListaAtt);
        ListaAttesa la = (ListaAttesa) Connection.getSingleResult(query);
        
        return la;
    }
    
}
