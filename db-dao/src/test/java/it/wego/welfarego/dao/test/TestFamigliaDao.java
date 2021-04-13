/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.dao.test;

import it.wego.welfarego.persistence.dao.AnagrafeFamigliaDao;
import it.wego.welfarego.persistence.entities.AnagrafeFam;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 *
 * @author aleph
 */
//@Ignore
public class TestFamigliaDao extends AbstractTest {

    private AnagrafeFamigliaDao anagrafeFamigliaDao;

    @Before
    public void init() {
        anagrafeFamigliaDao = new AnagrafeFamigliaDao(entityManager);
    }

    @Test
    public void testGetAnagrafeFamListMerge() {
        logger.info("getAnagrafeFamListMerge");
        List<AnagrafeFam> anagrafeFamListMerge = anagrafeFamigliaDao.getAnagrafeFamListMerge(getAnagrafeSoc());
        logger.info("getAnagrafeFamListMerge OK, result = {}", anagrafeFamListMerge);
    }

    @Test
    public void testGetAnagrafeFamListMergeWFilter() {
        logger.info("GetAnagrafeFamListMergeWFilter");
        List<AnagrafeFam> anagrafeFamListMerge = anagrafeFamigliaDao.getAnagrafeFamListMerge(getAnagrafeSoc().getCodAna(), AnagrafeFamigliaDao.getRelazioneDiParentelaFilter());
        logger.info("GetAnagrafeFamListMergeWFilter OK, result = {}", anagrafeFamListMerge);
    }
}
