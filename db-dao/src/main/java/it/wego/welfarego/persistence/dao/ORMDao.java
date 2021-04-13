package it.wego.welfarego.persistence.dao;

/**
 *
 * @author giuseppe
 */
public interface ORMDao {
    
    public void insert(Object object) throws Exception;
    public void update(Object object) throws Exception;
    public void delete(Object object) throws Exception;
}
