/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.utils;

import it.wego.persistence.PersistenceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Map;
import java.util.Stack;
import java.lang.ClassLoader;

/**
 *
 * @author giuseppe
 */
public class Connection {

	public static final Logger logger = LoggerFactory.getLogger(Connection.class);

	public static final String PERSISTENCE_UNIT = "welfaregoPU", NONJDNI_PERSISTENCE_UNIT = "welfaregoSimplePU";

	// private static final EntityManagerFactory emf =
	// Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

	public static EntityManager getEntityManager() {

		try {
			return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT).createEntityManager();
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(e.getMessage());
		}
		return null;

		// EntityManager em = emf.createEntityManager();
		// return em;
	}

	public static EntityManager getEntityManager(Map<?, ?> extraParams) {
		try {

			EntityManagerFactory nonJdniEntityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT,
					extraParams);
			EntityManager em = nonJdniEntityManagerFactory.createEntityManager();
			return em;
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(e.getMessage());
			return null;
		}

	}

	/**
	 *
	 * @param query
	 * @return
	 * @deprecated use PersistenceAdapter.getSingleResult(query)
	 */
	@Deprecated
	public static Object getSingleResult(Query query) {
		return PersistenceAdapter.getSingleResult(query);
	}

	public static void getCurrentClassloaderDetail() {

		StringBuffer classLoaderDetail = new StringBuffer();
		Stack<ClassLoader> classLoaderStack = new Stack<ClassLoader>();

		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

		classLoaderDetail.append("\n-----------------------------------------------------------------\n");

		// Build a Stack of the current ClassLoader chain
		while (currentClassLoader != null) {

			classLoaderStack.push(currentClassLoader);

			currentClassLoader = currentClassLoader.getParent();
		}

		// Print ClassLoader parent chain
		while (classLoaderStack.size() > 0) {

			ClassLoader classLoader = classLoaderStack.pop();

			// Print current
			classLoaderDetail.append(classLoader);

			if (classLoaderStack.size() > 0) {
				classLoaderDetail.append("\n--- delegation ---\n");
			} else {
				classLoaderDetail.append(" **Current ClassLoader**");
			}
		}

		classLoaderDetail.append("\n-----------------------------------------------------------------\n");

		logger.info(classLoaderDetail.toString());
	}

}
