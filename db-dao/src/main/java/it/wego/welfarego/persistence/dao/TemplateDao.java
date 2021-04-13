/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.Template;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author giuseppe
 */
public class TemplateDao extends PersistenceAdapter {

    public TemplateDao(EntityManager em) {
        super(em);
    }

    public Template findByCodTemplate(int codTemplate) {
        TypedQuery<Template> query = getEntityManager().createNamedQuery("Template.findByCodTmpl", Template.class);
        query.setParameter("codTmpl", codTemplate);
        return getSingleResult(query);
    }

    public Template findByCodTemplate(String codTemplate) {
        return findByCodTemplate(Integer.parseInt(codTemplate));
    }

    public Template getReference(Integer codTemplate) {
        return getEntityManager().getReference(Template.class, codTemplate);
    }

    public Template getReference(String codTemplate) {
        return getReference(Integer.valueOf(codTemplate));
    }

    public List<Template> findAll() {
        Query query = getEntityManager().createNamedQuery("Template.findAll");
        List<Template> tpls = query.getResultList();
        return tpls;
    }
}
