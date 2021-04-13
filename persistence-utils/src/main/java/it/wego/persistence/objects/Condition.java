package it.wego.persistence.objects;

import javax.persistence.Query;

/**
 * rappresenta una condizione, da usare nella sezione WHERE di una query JPQL.
 * L'uso corretto richiede due passi: 1) accodare alla query la stringa ottenuta
 * tramite getCondition() e 2) impostare eventuali parametri sulla query tramite
 * il metodo setParameter(query)
 *
 * @author aleph
 */
public interface Condition extends Operation {

    /**
     * restituisce la stringa JPQL che esprime questa condizione
     *
     * @return la stringa JPQL che esprime questa condizione
     */
    @Override
    public String toString();

    /**
     * imposta eventuali parametri della condizione
     *
     * @param query la query su cui impostare eventuali parametri
     */
    public void setParameter(Query query);
}
